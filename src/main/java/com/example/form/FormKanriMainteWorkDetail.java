package com.example.form;

import java.time.LocalDateTime;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class FormKanriMainteWorkDetail {
	
	//------------------------------------------------
	//テーブルに存在しない項目
	private String buttonKbn; //登録：regist、更新：update、削除：delete
	
	//------------------------------------------------
	//テーブルに存在する項目
	private String workId;
	private String workName;
	private String workKbn;
	private String resetSpan; //リセット間隔
	private String separatePersent;//作業進捗区切
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
	
}
