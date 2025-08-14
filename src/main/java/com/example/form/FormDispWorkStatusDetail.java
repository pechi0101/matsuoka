package com.example.form;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class FormDispWorkStatusDetail {
	
	//------------------------------------------------
	//チェックボックス操作に関わる項目
	private boolean deleteCheckBox;
	
	
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
	
	
	private String dispDetailInfo;
	
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
	
	
	public FormDispWorkStatusDetail() {
		
		this.deleteCheckBox     = false;
		
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
