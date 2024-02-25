package com.example.form;

import java.io.Serializable;
import java.util.ArrayList;

import com.example.entity.Employee;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class FormIndexQR implements Serializable {
	
	private String loginEmployeeId;
	private String loginEmployeeName;
	
	ArrayList<Employee> employeeList;
	
	public FormIndexQR() {
		loginEmployeeId     = "";
		loginEmployeeName   = "";
		employeeList        = new ArrayList<Employee>();
	}
}
