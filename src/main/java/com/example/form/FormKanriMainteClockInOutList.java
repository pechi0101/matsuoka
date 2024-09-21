package com.example.form;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.dropDownList.DropDownEmployee;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class FormKanriMainteClockInOutList implements Serializable {
	
	private String message;
	
	// 検索条件ドロップダウンリスト
	private ArrayList<DropDownEmployee> dropDownEmployeeList;
	
	// 選択フィルタリング条件
	
	private String filterTargetYM;   // フィルター条件として使いたいため"YYYY-MM"形式の文字列
	
	private String filterEmployeeId;
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate filterDateFr;
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate filterDateTo;
	
	// 表示制御
	private String deleteClockInOutDisp; //削除行を表示するか否か 0:表示しない 1:表示する
	
	// 選択行の情報
	private String selectEmployeeId;
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime selectStartDateTime;
	
	private ArrayList<FormKanriMainteClockInOutDetail> clockInOutList = new ArrayList<FormKanriMainteClockInOutDetail>();
	
	public FormKanriMainteClockInOutList() {
		
		// 検索条件ドロップダウンリスト
		this.dropDownEmployeeList = new ArrayList<DropDownEmployee>();
		
		// 選択検索条件
		this.filterEmployeeId       = "";
		
		// 表示制御
		this.deleteClockInOutDisp   = "0"; //削除行を表示するか否か 0:表示しない 1:表示する
		
		// 選択行の情報
		this.selectEmployeeId       = "";
		this.selectStartDateTime    = null;
	}
	
	public void addClockInOut(FormKanriMainteClockInOutDetail clockInOut) {
		this.clockInOutList.add(clockInOut);
	}
}
