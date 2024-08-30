package com.example.entity;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.DAO.DaoHouseWorkStatus;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class HouseWorkStatus {
	
	//------------------------------------------------
	//テーブルに存在しない項目
	private int workStatus; // クラスDaoHouseWorkStatusの「定数:作業状況」参照
	
	//------------------------------------------------
	//テーブルに存在する項目
	private String workId;
	private String houseId;
	private String colNo;
	private String startEmployeeId;
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime startDateTime;
	private String endEmployeeId;
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime endDateTime;
	private int percentStart;
	private int percent;
	private String biko;
	
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
	
	public HouseWorkStatus() {
		//作業状況は「エラー」、新著率「０％」で初期化
		this.workStatus   = DaoHouseWorkStatus.STATUS_ERROR;
		this.percentStart = 0;
		this.percent      = 0;
		this.biko         = "";
	}
	
}
