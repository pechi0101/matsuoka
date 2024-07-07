package com.example.form;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.dropDownList.DropDownHouse;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class FormKanriMainteShukakuSumList implements Serializable {
	
	private String message;
	
	// 検索条件ドロップダウンリスト
	private ArrayList<DropDownHouse> dropDownHouseList;
	
	// 選択フィルタリング条件
	private String filterHouseId;
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate filterDateFr;
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate filterDateTo;
	
	// 選択行の情報
	private String selectHouseId;
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate selectShukakuDate;
	
	private ArrayList<FormKanriMainteShukakuSumDetail> shukakuList = new ArrayList<FormKanriMainteShukakuSumDetail>();
	
	public FormKanriMainteShukakuSumList() {
		
		// 検索条件ドロップダウンリスト
		this.dropDownHouseList    = new ArrayList<DropDownHouse>();
		
		// 選択検索条件
		this.filterHouseId          = "";
		
		// 選択行の情報
		this.selectHouseId          = "";
		this.selectShukakuDate      = null;
	}
	
}
