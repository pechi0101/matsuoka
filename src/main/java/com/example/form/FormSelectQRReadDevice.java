package com.example.form;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class FormSelectQRReadDevice {
	
	//------------------------------------------------
	//テーブルに存在しない項目
	private String loginEmployeeId;
	private String loginEmployeeName;
	private String selectedDeviceLabel; // 使用するデバイスのラベル
	
	//例

	// ------------------------------------------------
	// ■Android(山田の３眼スマホ)
	// camera2 1, facing front前面カメラ
	// camera2 4, facing back 何か暗いレンズ
	// camera2 3, facing back 望遠レンズ
	// camera2 2, facing back 接眼レンズ
	// camera2 0, facing back 標準レンズ★
	// ------------------------------------------------
	// ■Android(山田の１眼スマホ)
	// camera2 1, facing front前面カメラ
	// camera2 0, facing back 標準レンズ★
	// ------------------------------------------------
	// ■iPhone(小大さんのヤツ)
	// 前面カメラ
	// 背面デュアル広角カメラ
	// 背面超広角カメラ
	// 背面カメラ             標準レンズ★
	// ------------------------------------------------
	// ■iPhone(松岡さんのヤツ)
	// 前面カメラ
	// 背面カメラ             標準レンズ★
	// 背面望遠カメラ
	// 背面超広角カメラ
	// デスクビューカメラ     Mac BookにiPhoneを付けた状態でタイプしてる手元を映すことができるカメラ
	// ------------------------------------------------
	
	//------------------------------------------------
	//テーブルに存在する項目
	
	
	public FormSelectQRReadDevice() {
		this.loginEmployeeId     = "";
		this.loginEmployeeName   = "";
		this.selectedDeviceLabel = "";
	}
	
	
}
