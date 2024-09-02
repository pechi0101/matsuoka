package com.example.form;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.dropDownList.DropDownEmployee;
import com.example.dropDownList.DropDownHouse;
import com.example.dropDownList.DropDownWork;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class FormKanriMainteWorkStatusDetail {
	
	//------------------------------------------------
	//テーブルに存在しない項目
	private String buttonKbn; //登録：regist、更新：update、削除：delete
	
	//------------------------------------------------
	// 選択フィルタリング条件(一覧画面から引き継いだフィルタリング条件の情報を保持するための領域)
	private String filterHouseId;
	private String filterWorkId;
	private String filterStartEmployeeId;
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate filterDateFr;
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate filterDateTo;
	
	//------------------------------------------------
	// 元々の(修正前の)主キー情報(一覧画面から引き継いだ作業の主キー情報)
	private String beforeHouseId;
	private String beforeColNo;
	private String beforeWorkId;
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime beforeStartDateTime; //【メモ】画面が年月日と時分で入力項目が分かれてる
	
	//------------------------------------------------
	// 入力用ドロップダウンリスト
	private ArrayList<DropDownHouse> dropDownHouseList;
	private ArrayList<DropDownWork> dropDownWorkList;
	private ArrayList<DropDownEmployee> dropDownStartEmployeeList;
	private ArrayList<DropDownEmployee> dropDownEndEmployeeList;
	
	//------------------------------------------------
	//テーブルに存在する項目
	private String houseId;
	private String houseName;
	private String colNo;
	private String workId;
	private String workName;
	
	private String startEmployeeId;
	private String startEmployeeName;
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate startDate; //【メモ】画面が年月日と時分で入力項目が分かれてる
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime startTime;
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime startDateTime;
	
	
	private String endEmployeeId;
	private String endEmployeeName;
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate endDate; //【メモ】画面が年月日と時分で入力項目が分かれてる
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime endTime;
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime endDateTime;

	private String percentStart;  // 進捗率_開始(%)
	private String percent;       // 進捗率_終了(%)
	
	private boolean deleteFlg;
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime deleteymdhms;
	
	private String biko;
	private String boxCount; // 収穫ケース数
	
	
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
	
	
	public FormKanriMainteWorkStatusDetail() {
		
		this.houseId            = "";
		this.houseName          = "";
		this.colNo              = "";
		this.workId             = "";
		this.workName           = "";
		this.startEmployeeId    = "";
		this.startEmployeeName  = "";
		//this.startDate          = "";
		//this.startTime          = "";
		this.endEmployeeId      = "";
		this.endEmployeeName    = "";
		//this.endDate            = "";
		//this.endTime            = "";
		this.percentStart       = "";
		this.percent            = "";
		this.deleteFlg          = false;
		//this.deleteymdhms       = "";
		this.biko               = "";
		this.boxCount           = "";
		
	}
	
	
}
