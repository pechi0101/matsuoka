package com.example.form;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class FormDispWorkStatusList implements Serializable {
	
	private String loginEmployeeId;
	private String loginEmployeeName;
	
	private String selectedDeviceLabel; // 使用するデバイスのラベル
	
	// 作業状況表示画面でどのボタンが押されたか。1:更新、2:全て終了、9:閉じる
	private String pushButtonKbn;
	// 操作権限
	private boolean editAuthority;
	private boolean administrationAuthority;
	
	private ArrayList<FormDispWorkStatusDetail> strWorkStatusDetailList;
	
	
	public FormDispWorkStatusList() {
		this.loginEmployeeId         = "";
		this.loginEmployeeName       = "";
		this.selectedDeviceLabel     = "";
		this.pushButtonKbn           = "";
		this.editAuthority           = false;
		this.administrationAuthority = false;
		
		strWorkStatusDetailList = new ArrayList<FormDispWorkStatusDetail>();
	}
	
	public void addWorkStatus(FormDispWorkStatusDetail addDetail) {
		
		strWorkStatusDetailList.add(addDetail);
	}
	
}

