package com.example.dropDownList;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class DropDownWork {
	
	private String workId;
	private String workName;
	
	public DropDownWork(String id, String name) {
		workId = id;
		workName = name;
	}
}
