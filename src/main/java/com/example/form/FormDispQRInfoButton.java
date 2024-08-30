package com.example.form;

import com.example.counst.ButtonKbn;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class FormDispQRInfoButton {
	
	// ボタン押下可能フラグ
	private boolean buttonEnabledFlg;
	
	// ボタン区分、ボタン表示文字列 
	// start  :作業開始
	// end    :作業完了 100%
	// halfway:作業途中 xx%
	// cancel :取消してもう一度
	private String buttonKbn;
	private String buttonString;
	
	// 作業途中時のパーセント
	private int percent;
	
	
	public FormDispQRInfoButton() {
		
	}
	
	public String getButtonString() {
		
		buttonString = "";
		
		if (buttonKbn.equals(ButtonKbn.START) == true) {
			buttonString = "作業開始";
			
		} else if (buttonKbn.equals(ButtonKbn.RESTART) == true) {
			buttonString = "作業再開";
			
		} else if (buttonKbn.equals(ButtonKbn.END) == true) {
			buttonString = "作業完了 100%";
			
		} else if (buttonKbn.equals(ButtonKbn.HALFWAY) == true) {
			buttonString = "作業中断 " + Integer.toString(percent) + "%";
			
		} else if (buttonKbn.equals(ButtonKbn.CANCEL) == true) {
			buttonString = "取消してもう一度";
			
		} else if (buttonKbn.equals(ButtonKbn.REGIST) == true) {
			buttonString = "登録/更新";
		}
		
		
		return buttonString;
	}
}
