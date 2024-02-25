package com.example.form;

import java.io.Serializable;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class FormSelectWorkStatusDateButton implements Serializable {
	

	// 何日前までのデータを表示するか（0:当日分、1:１日前）
	private int buttonData;
	private String buttonString;
	
	// 【注意】
	// 入れ子になっているFormクラスはコンストラクタがあってはならないみたい！
	// クライアント→サーバにPOST送信時にエラーになった
	
	public String getButtonString() {
		
		buttonString = "";
		
		if (buttonData == 0) {
			buttonString = "当日分のみ";
			
		} else if (buttonData == 7)  {
			buttonString = "過去１週間分";
			
		} else if (buttonData == 14)  {
			buttonString = "過去２週間分";
			
		} else if (buttonData == 30)  {
			buttonString = "過去１ケ月分";
			
		} else {
			buttonString = "過去 " + Integer.toString(buttonData) + " 日分";
		}
		
		return buttonString;
	}
	
}




