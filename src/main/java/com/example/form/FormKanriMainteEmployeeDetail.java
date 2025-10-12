package com.example.form;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class FormKanriMainteEmployeeDetail {
	
	//------------------------------------------------
	//テーブルに存在しない項目
	private String buttonKbn; //登録：regist、更新：update、削除：delete
	
	//------------------------------------------------
	//テーブルに存在する項目
	private String employeeId;
	private String employeeName;
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDateTime birthday;
	private String postCd;
	private String address;
	private String phone;
	private String photo;
	private String bankName;
	private String bankBranchName;
	private String bankAccountType;
	private String bankAccountNo;
	private String biko;
	private String authorityKbn; //権限区分 0:一般ユーザ、9:特別ユーザ、Z:管理者ユーザ
	private String authorityKbnName;
	private boolean deleteFlg;
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime deleteymdhms;
	
	private String hourlyWage; //時給(円)
	
	
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
