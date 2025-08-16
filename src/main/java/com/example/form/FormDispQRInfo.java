package com.example.form;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.DAO.DaoHouseWorkStatus;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class FormDispQRInfo implements Serializable {
	
	// ログインユーザ情報
	private String loginEmployeeId;
	private String loginEmployeeName;
	// 使用するデバイスのラベル
	private String selectedDeviceLabel;
	
	private String scrName; // 画面名※どの画面から遷移してきたか？ 例：作業状況表示画面の場合「scrDispWorkStatusMobile」
	
	// ボタン押下情報
	private String pushedButtunKbn;     // 押下したボタンのボタン区分
	private int pushedButtunPercent; // 押下したボタンの進捗率

	// ボタン表示制御情報
	private ArrayList<FormDispQRInfoButton> buttonDispInfoList;
	
	// ＤＢ登録情報
	private String workId;
	private String workName;
	private String houseId;
	private String houseName;
	private String colNo;
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime startDatetime;
	private String startEmployeeid;
	private String startEmployeeName;
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime endDatetime;
	private String endEmployeeid;
	private String endEmployeeName;
	private int percentStart;
	private int percent;
	private String biko;
	
	// その他DBに存在しない項目
	private int workStatus;  // 値の内容はクラスDaoHouseWorkStatusの「定数:作業状況」参照
	private String workStatusString;
	
	public FormDispQRInfo() {
		loginEmployeeId     = "";
		loginEmployeeName   = "";
		
		buttonDispInfoList = new ArrayList<FormDispQRInfoButton>();
	}
	
	//作業状況の文字列を編集して返却するgetter
	public String getWorkStatusString() {
		
		workStatusString = "";
		
		//【メモ】
		// 作業未実施と(前回の)作業完了状態である場合、画面には「開始未実施」と表示する
		//
		if (workStatus == DaoHouseWorkStatus.STATUS_DONE) {
			workStatusString = "開始未実施状態";
		}
		
		if (workStatus == DaoHouseWorkStatus.STATUS_NOT) {
			workStatusString = "開始未実施状態";
		}
		
		if (workStatus == DaoHouseWorkStatus.STATUS_WORKING) {
			workStatusString = "作業中：" + Integer.toString(percentStart) + "% まで完了";
		}
		
		if (workStatus == DaoHouseWorkStatus.STATUS_WORKING_STOP) {
			workStatusString = "作業中：" + Integer.toString(percent) + "% まで完了";
		}
		
		return workStatusString;
	}
}
