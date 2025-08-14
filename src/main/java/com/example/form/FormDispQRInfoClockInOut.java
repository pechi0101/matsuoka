package com.example.form;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class FormDispQRInfoClockInOut implements Serializable {
	
	// ログインユーザ情報
	private String loginEmployeeId;
	private String loginEmployeeName;
	// 使用するデバイスのラベル
	private String selectedDeviceLabel;

	// ボタン押下情報
	private String pushedButtunKbn;     // 押下したボタンのボタン区分
	
	// ボタン表示制御情報
	//private ArrayList<FormDispQRInfoButton> buttonDispInfoList;

	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime beforeClockInDatetime;
	
	
	// ＤＢ登録情報
	private String clockInYear;
	private String clockInMonth;
	private String clockInDay;
	
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate clockInDate; //【メモ】画面が年月日と時分で入力項目が分かれてる
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime clockInTime;
	private String clockInTimeString;//HTMLのinput type="time"表示用の文字列
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime clockInDatetime;
	
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate clockOutDate; //【メモ】画面が年月日と時分で入力項目が分かれてる
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime clockOutTime;
	private String clockOutTimeString;//HTMLのinput type="time"表示用の文字列
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime clockOutDatetime;
	
	private int hourLywage;  // 出勤時の時給
	private double workingHours;//出勤時間:小数点以上２桁／小数点以下２桁
	
	private String message;
	
	public FormDispQRInfoClockInOut() {
		loginEmployeeId     = "";
		loginEmployeeName   = "";
		clockInYear         = "";
		clockInMonth        = "";
		clockInDay          = "";
		hourLywage          = 0;
		workingHours        = 0.0;
		message             = "";
		//buttonDispInfoList  = new ArrayList<FormDispQRInfoButton>();
	}
	
}
