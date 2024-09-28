package com.example.entity;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class ShukakuBoxSum {
	
	//------------------------------------------------
	//テーブルに存在しない項目
	
	
	//------------------------------------------------
	//テーブルに存在する項目
	private String houseId;
	private String houseName;
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDateTime shukakuDate;
	private String registEmployeeid;
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime registDatetime;
	private String biko;
	private double boxSum;  // 収穫ケース数合計
	private boolean delflg;
	
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
	
	public ShukakuBoxSum() {
		//作業状況は「エラー」、新著率「０％」で初期化
		this.houseId    = "";
		this.registEmployeeid = "";
		this.biko       = "";
		this.boxSum     = 0;
		this.delflg     = false;
	}
	
}
