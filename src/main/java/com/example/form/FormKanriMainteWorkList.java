package com.example.form;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class FormKanriMainteWorkList implements Serializable {
	
	private String message;
	private String selectWorkId;
	private ArrayList<FormKanriMainteWorkDetail> workList = new ArrayList<FormKanriMainteWorkDetail>();
	
	public FormKanriMainteWorkList() {
		// 管理画面であるためユーザは「管理者固定」
		this.selectWorkId = "";
	}
	
	public void addWork(FormKanriMainteWorkDetail house) {
		this.workList.add(house);
	}
}
