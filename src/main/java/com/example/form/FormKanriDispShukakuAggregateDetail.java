package com.example.form;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class FormKanriDispShukakuAggregateDetail {
	
	//------------------------------------------------
	//テーブルに存在する項目
	private String houseId;
	private String houseName;
	
	private String aggregateYear; // YYYY形式
	private String aggregateMonth; // MM形式0サプレス
	
	private String boxSum; // 収穫ケース数
	
	public FormKanriDispShukakuAggregateDetail() {
		
		this.houseId            = "";
		this.houseName          = "";
		this.aggregateYear       = "";
		
		this.aggregateMonth     = "";
		this.boxSum             = "";
	}
	
	
}
