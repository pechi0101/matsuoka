package com.example.dropDownList;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class DropDownHouse {
	
	private String houseId;
	private String houseName;
	
	public DropDownHouse(String id, String name) {
		houseId = id;
		houseName = name;
	}
}
