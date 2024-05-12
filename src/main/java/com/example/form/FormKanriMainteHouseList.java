package com.example.form;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class FormKanriMainteHouseList implements Serializable {
	
	private String message;
	private String selectHouseId;
	private ArrayList<FormKanriMainteHouseDetail> houseList = new ArrayList<FormKanriMainteHouseDetail>();
	
	public FormKanriMainteHouseList() {
		// 管理画面であるためユーザは「管理者固定」
		this.selectHouseId = "";
	}
	
	public void addHouse(FormKanriMainteHouseDetail house) {
		this.houseList.add(house);
	}
}
