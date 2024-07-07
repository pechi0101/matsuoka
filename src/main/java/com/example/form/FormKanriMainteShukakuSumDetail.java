package com.example.form;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.dropDownList.DropDownHouse;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class FormKanriMainteShukakuSumDetail {
	
	//------------------------------------------------
	//テーブルに存在しない項目
	private String buttonKbn; //登録：regist、更新：update、削除：delete
	
	//------------------------------------------------
	// 選択フィルタリング条件(一覧画面から引き継いだフィルタリング条件の情報を保持するための領域)
	private String filterHouseId;
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate filterDateFr;
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate filterDateTo;
	
	//------------------------------------------------
	// 入力用ドロップダウンリスト
	private ArrayList<DropDownHouse> dropDownHouseList;
	
	//------------------------------------------------
	//テーブルに存在する項目
	private String houseId;
	private String houseName;
	
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate shukakuDate;

	private String registEmployeeId;
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime registDateTime;
	
	private String boxSum; // 収穫ケース数
	private String boxSumQR; // 収穫ケース数(QR)
	private String biko;
	private boolean deleteFlg;
	
	
	/*
	 * 【メモ】
	 * JavaSE 8からは日付／時刻を扱う新しいAPIとして、Date and Time APIが導入されました。
	 * Date-Time APIの実体は、java.timeパッケージです。
	 * 現在javaの日付型は主にこれを使用するらしい。Date型は非推奨
	 * 
	 */
	
	private String sysRegUserid;
	private String sysRegPgmid;
	private LocalDateTime sysRegYmdhms;  // 日付はLocalDateTime→Date-Time API
	private String sysUpdUserid;
	private String sysUpdPgmid;
	private LocalDateTime sysUpdYmdhms;
	
	
	public FormKanriMainteShukakuSumDetail() {
		
		this.houseId            = "";
		this.houseName          = "";
		this.registEmployeeId   = "";
		
		this.biko               = "";
		this.boxSumQR           = "";
		this.boxSum             = "";
		this.deleteFlg          = false;
	}
	
	
}
