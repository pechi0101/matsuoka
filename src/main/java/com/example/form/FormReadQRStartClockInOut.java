package com.example.form;

import java.io.Serializable;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class FormReadQRStartClockInOut implements Serializable {
	
	private String loginEmployeeId;
	private String loginEmployeeName;
	private String selectedDeviceLabel; // 使用するデバイスのラベル

	// 出退勤状態、作業状況の表示文言
	private String strClockInOutStatusMSG;
	private String strWorkStatusMSG;
	
	private String message;
	/*
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime clockInDatetime;
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime clockOutDatetime;
	private int hourLywage;  // 出勤時の時給
	private double workingHours;//出勤時間:小数点以上２桁／小数点以下２桁
	*/
	public FormReadQRStartClockInOut() {
		this.loginEmployeeId     = "";
		this.loginEmployeeName   = "";
		this.selectedDeviceLabel = "";
		this.message             = "";
	}
}
