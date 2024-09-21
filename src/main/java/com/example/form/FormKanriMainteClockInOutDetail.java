package com.example.form;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.dropDownList.DropDownEmployee;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class FormKanriMainteClockInOutDetail {
	
	//------------------------------------------------
	//テーブルに存在しない項目
	private String buttonKbn; //登録：regist、更新：update、削除：delete
	
	//------------------------------------------------
	// 選択フィルタリング条件(一覧画面から引き継いだフィルタリング条件の情報を保持するための領域)
	
	
	private String filterTargetYM;   // フィルター条件として使いたいため"YYYY-MM"形式の文字列
	
	private String filterEmployeeId;
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate filterDateFr;
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate filterDateTo;
	
	//------------------------------------------------
	// 元々の(修正前の)主キー情報(一覧画面から引き継いだ作業の主キー情報)
	private String beforeEmployeeId;;
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime beforeStartDateTime; //【メモ】画面が年月日と時分で入力項目が分かれてる
	
	//------------------------------------------------
	// 入力用ドロップダウンリスト
	private ArrayList<DropDownEmployee> dropDownEmployeeList;
	
	//------------------------------------------------
	//テーブルに存在する項目
	private String employeeId;
	private String employeeName;
	
	private String clockInYear;
	private String clockInMonth;
	private String clockInDay;
	
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate startDate; //【メモ】画面が年月日と時分で入力項目が分かれてる
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime startTime;
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime startDateTime;
	
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate endDate; //【メモ】画面が年月日と時分で入力項目が分かれてる
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime endTime;
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime endDateTime;
	
	private int hourlywage;	//時給
	double workingHoures; //勤務時間:小数点以上２桁、小数点以下２桁
	private String biko;
	
	private boolean deleteFlg;
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime deleteymdhms;
	
	
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
	
	
	public FormKanriMainteClockInOutDetail() {
		
		this.employeeId         = "";
		this.clockInYear        = "";
		this.clockInMonth       = "";
		this.clockInDay         = "";
		
		this.hourlywage         = 0;
		this.workingHoures      = 0.00;
		this.biko               = "";
		
		this.deleteFlg          = false;
	}
	
	
}
