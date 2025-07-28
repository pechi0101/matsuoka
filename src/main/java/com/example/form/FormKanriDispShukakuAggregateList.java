package com.example.form;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class FormKanriDispShukakuAggregateList implements Serializable {
	
	private String message;
	
	//全ハウスの合計をグラフ表示するデータ
	private ArrayList<FormKanriDispShukakuAggregateDetail> detailList_All   = new ArrayList<FormKanriDispShukakuAggregateDetail>();
	
	//ハウス毎の表示データ
	private ArrayList<FormKanriDispShukakuAggregateDetail> detailList_10001 = new ArrayList<FormKanriDispShukakuAggregateDetail>();
	private ArrayList<FormKanriDispShukakuAggregateDetail> detailList_10002 = new ArrayList<FormKanriDispShukakuAggregateDetail>();
	private ArrayList<FormKanriDispShukakuAggregateDetail> detailList_10003 = new ArrayList<FormKanriDispShukakuAggregateDetail>();
	private ArrayList<FormKanriDispShukakuAggregateDetail> detailList_10004 = new ArrayList<FormKanriDispShukakuAggregateDetail>();
	private ArrayList<FormKanriDispShukakuAggregateDetail> detailList_10005 = new ArrayList<FormKanriDispShukakuAggregateDetail>();
	private ArrayList<FormKanriDispShukakuAggregateDetail> detailList_10006 = new ArrayList<FormKanriDispShukakuAggregateDetail>();
	private ArrayList<FormKanriDispShukakuAggregateDetail> detailList_10007 = new ArrayList<FormKanriDispShukakuAggregateDetail>();
	
	public FormKanriDispShukakuAggregateList() {
		
	}
}
