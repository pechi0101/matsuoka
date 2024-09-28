package com.example.form;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class FormKanriMainteHouseDetail {
	
	//------------------------------------------------
	//テーブルに存在しない項目
	private String buttonKbn; //登録：regist、更新：update、削除：delete
	
	//------------------------------------------------
	//テーブルに存在する項目
	private String houseId;
	private String houseName;
	
	//予定収穫ケース数(01月～12月)
	private double boxSumYTI01 = 0;
	private double boxSumYTI02 = 0;
	private double boxSumYTI03 = 0;
	private double boxSumYTI04 = 0;
	private double boxSumYTI05 = 0;
	private double boxSumYTI06 = 0;
	private double boxSumYTI07 = 0;
	private double boxSumYTI08 = 0;
	private double boxSumYTI09 = 0;
	private double boxSumYTI10 = 0;
	private double boxSumYTI11 = 0;
	private double boxSumYTI12 = 0;
	
	private String biko;
	private boolean deleteFlg;
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime deleteymdhms;
	
	private String colCount; //列数
	
	
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
	
}
