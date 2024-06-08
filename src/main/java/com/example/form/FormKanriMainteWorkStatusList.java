package com.example.form;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.dropDownList.DropDownEmployee;
import com.example.dropDownList.DropDownHouse;
import com.example.dropDownList.DropDownWork;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class FormKanriMainteWorkStatusList implements Serializable {
	
	private String message;
	
	// 検索条件ドロップダウンリスト
	private ArrayList<DropDownHouse> dropDownHouseList;
	private ArrayList<DropDownWork> dropDownWorkList;
	private ArrayList<DropDownEmployee> dropDownEmployeeList;
	
	// 選択フィルタリング条件
	private String filterHouseId;
	private String filterWorkId;
	private String filterStartEmployeeId;
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDateTime filterDateFr;
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDateTime filterDateTo;
	
	// 表示制御
	private String deleteWorkStatusDisp; //削除行を表示するか否か 0:表示しない 1:表示する
	
	// 選択行の情報
	private String selectHouseId;
	private String selectColNo;
	private String selectWorkId;
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime selectStartDateTime;
	
	private ArrayList<FormKanriMainteWorkStatusDetail> workStatusList = new ArrayList<FormKanriMainteWorkStatusDetail>();
	
	public FormKanriMainteWorkStatusList() {
		
		// 検索条件ドロップダウンリスト
		this.dropDownHouseList    = new ArrayList<DropDownHouse>();
		this.dropDownWorkList     = new ArrayList<DropDownWork>();
		this.dropDownEmployeeList = new ArrayList<DropDownEmployee>();
		
		// 選択検索条件
		this.filterHouseId          = "";
		this.filterWorkId           = "";
		this.filterStartEmployeeId  = "";
		
		// 表示制御
		this.deleteWorkStatusDisp   = "0"; //削除行を表示するか否か 0:表示しない 1:表示する
		
		// 選択行の情報
		this.selectHouseId          = "";
		this.selectColNo            = "";
		this.selectWorkId           = "";
		this.selectStartDateTime    = null;
	}
	
	public void addWorkStatus(FormKanriMainteWorkStatusDetail workStatus) {
		this.workStatusList.add(workStatus);
	}
}
