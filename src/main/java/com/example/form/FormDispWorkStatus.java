package com.example.form;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class FormDispWorkStatus implements Serializable {
	
	private String loginEmployeeId;
	private String loginEmployeeName;
	private ArrayList<FormDispWorkStatusHouse> houseList;
	
	public FormDispWorkStatus() {
		loginEmployeeId     = "";
		loginEmployeeName   = "";
		houseList           = new ArrayList<FormDispWorkStatusHouse>();
	}
}
