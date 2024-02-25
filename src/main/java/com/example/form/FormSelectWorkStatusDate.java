package com.example.form;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class FormSelectWorkStatusDate implements Serializable {
	
	private String loginEmployeeId;
	private String loginEmployeeName;
	private int selectIntervalDate;
	
	// 何日前までのデータを表示するかのボタンを表示するためのリスト
	private ArrayList<FormSelectWorkStatusDateButton> buttonList;
	
	public FormSelectWorkStatusDate() {
		loginEmployeeId     = "";
		loginEmployeeName   = "";
		selectIntervalDate  = 0;
		buttonList          = new ArrayList<FormSelectWorkStatusDateButton>();
	}
}
