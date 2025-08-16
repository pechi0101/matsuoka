package com.example.form;

import java.io.Serializable;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class FormReadQRCode implements Serializable {
	
	private String loginEmployeeId;
	private String loginEmployeeName;
	private String selectedDeviceLabel; // 使用するデバイスのラベル
	
	private String qrcode;  // QRコードの読取り情報 例：MatsuokaQRData,10001,01,1,1000001
	private String scrName; // 画面名※どの画面から遷移してきたか？ 例：作業状況表示画面の場合「scrDispWorkStatusMobile」
	
	public FormReadQRCode() {
		this.loginEmployeeId     = "";
		this.loginEmployeeName   = "";
		this.selectedDeviceLabel = "";
		this.qrcode              = "";
		this.scrName             = "";
	}
	
}
