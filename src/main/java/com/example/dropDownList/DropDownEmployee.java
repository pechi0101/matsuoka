package com.example.dropDownList;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class DropDownEmployee {
	
	private String employeeId;
	private String employeeName;
	
	public DropDownEmployee(String id, String name) {
		employeeId = id;
		employeeName = name;
	}
}
