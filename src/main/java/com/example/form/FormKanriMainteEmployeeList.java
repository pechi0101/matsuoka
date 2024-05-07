package com.example.form;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class FormKanriMainteEmployeeList implements Serializable {
	
	private String message;
	private String selectEmployeeId;
	private ArrayList<FormKanriMainteEmployeeDetail> employeeList = new ArrayList<FormKanriMainteEmployeeDetail>();
	
	public FormKanriMainteEmployeeList() {
		// 管理画面であるためユーザは「管理者固定」
		this.selectEmployeeId = "";
	}
	
	public void addEmployee(FormKanriMainteEmployeeDetail employee) {
		this.employeeList.add(employee);
	}
}
