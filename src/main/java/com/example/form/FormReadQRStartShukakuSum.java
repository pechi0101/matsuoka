package com.example.form;

import java.io.Serializable;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class FormReadQRStartShukakuSum implements Serializable {
	
	private String loginEmployeeId;
	private String loginEmployeeName;
	private String selectedDeviceLabel; // 使用するデバイスのラベル
	
	// 出退勤状態、作業状況の表示文言
	private String strClockInOutStatusMSG;
	private String strWorkStatusMSG;
	
	private String message;
	private double boxSum;
	
	public FormReadQRStartShukakuSum() {
		this.loginEmployeeId     = "";
		this.loginEmployeeName   = "";
		this.selectedDeviceLabel = "";
		this.message             = "";
	}
}
