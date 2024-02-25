package com.example.form;

import java.io.Serializable;

import com.example.counst.SpecialUser;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class FormIndexKanri implements Serializable {
	
	private String loginEmployeeId;
	private String loginEmployeeName;
	
	public FormIndexKanri() {
		// 管理画面であるためユーザは「管理者固定」
		loginEmployeeId     = SpecialUser.KANRI_USER;//管理者ユーザ固定
		loginEmployeeName   = "管理者";
	}
}
