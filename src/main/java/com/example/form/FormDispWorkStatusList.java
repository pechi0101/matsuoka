package com.example.form;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class FormDispWorkStatusList implements Serializable {
	
	private String loginEmployeeId;
	private String loginEmployeeName;
	
	private ArrayList<FormDispWorkStatusDetail> strWorkStatusDetailList;
	
	private String selectedDeviceLabel; // 使用するデバイスのラベル
	private Boolean editAuthority;
	
	public FormDispWorkStatusList() {
		this.loginEmployeeId     = "";
		this.loginEmployeeName   = "";
		this.selectedDeviceLabel = "";
		this.editAuthority       = false;
		
	}
	
	public void addWorkStatus(FormDispWorkStatusDetail addDetail) {
		
		strWorkStatusDetailList.add(addDetail);
	}
	
}

