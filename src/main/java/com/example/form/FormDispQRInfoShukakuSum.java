package com.example.form;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class FormDispQRInfoShukakuSum implements Serializable {
	
	// ログインユーザ情報
	private String loginEmployeeId;
	private String loginEmployeeName;
	
	// 使用するデバイスのラベル
	private String selectedDeviceLabel;
	
	private String scrName; // 画面名※どの画面から遷移してきたか？ 例：作業状況表示画面の場合「scrDispWorkStatusMobile」
	
	
	// ボタン押下情報
	private String pushedButtunKbn;     // 押下したボタンのボタン区分
	//private int pushedButtunPercent; // 押下したボタンの進捗率
	
	// ボタン表示制御情報
	private ArrayList<FormDispQRInfoButton> buttonDispInfoList;
	
	// ＤＢ登録情報
	private String houseId;
	private String houseName;
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDateTime shukakuDate;
	private String registEmployeeid;
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime registDatetime;
	private String biko;
	private double boxSum;  // 収穫ケース数合計
	
	public FormDispQRInfoShukakuSum() {
		loginEmployeeId     = "";
		loginEmployeeName   = "";
		boxSum              = 0;// 収穫ケース数合計は0で初期化
		buttonDispInfoList  = new ArrayList<FormDispQRInfoButton>();
	}
	
}
