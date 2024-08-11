package com.example.form;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class FormKanriDispShukakuAggregateList implements Serializable {
	
	private String message;
	
	private ArrayList<FormKanriDispShukakuAggregateDetail> detailList = new ArrayList<FormKanriDispShukakuAggregateDetail>();
	
	public FormKanriDispShukakuAggregateList() {
		
	}
}
