package com.example.form;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class FormDispWorkStatusHouse implements Serializable {
	
	private String houseId;
	private String houseName;
	private ArrayList<FormDispWorkStatusCol> colList;
	
	public FormDispWorkStatusHouse() {
		colList = new ArrayList<FormDispWorkStatusCol>();
	}
}
