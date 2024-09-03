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
	
	private String dataType; // データタイプ 「前年度実績」「本年度予定」「本年度実績」
	
	
	public FormKanriDispShukakuAggregateDetail() {
		
		this.houseId            = "";
		this.houseName          = "";
		this.aggregateYear       = "";
		
		this.aggregateMonth     = "";
		this.boxSum             = "";
		this.dataType           = "";
	}
	
	public class AggregateDispDataType {
		
		public static String PRV_YEAR      = "前年度実績";
		public static String NOW_YEAR_YTI = "本年度予定";
		public static String NOW_YEAR      = "本年度実績";
		
	}
}
