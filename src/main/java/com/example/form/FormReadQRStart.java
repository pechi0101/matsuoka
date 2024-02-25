package com.example.form;

import java.io.Serializable;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class FormReadQRStart implements Serializable {
	
	private String loginEmployeeId;
	private String loginEmployeeName;
	private String selectedDeviceLabel; // 使用するデバイスのラベル
	
	private String message;
	
	public FormReadQRStart() {
		this.loginEmployeeId     = "";
		this.loginEmployeeName   = "";
		this.selectedDeviceLabel = "";
		this.message             = "";
	}
}
