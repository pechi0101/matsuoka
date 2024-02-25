package com.example.form;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class FormDispWorkStatusCol implements Serializable {
	

	//------------------------------------------------
	//テーブルに存在しない項目
	private String workStatusString;
	
	//------------------------------------------------
	//テーブルに存在する項目
	private String houseId;
	private String colNo;
	private String workId;
	private String workName;
	private String startEmployeeId;
	private String startEmployeeName;
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime startDateTime;
	private String endEmployeeId;
	private String endEmployeeName;
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime endDateTime;
	private int percent;
	private String biko;
	
	
	public FormDispWorkStatusCol() {
		this.percent    = 0;
		this.biko       = "";
	}

	
	//作業状況の文字列を編集して返却するgetter
	public String getWorkStatusString() {
		
		workStatusString = "";
		
		
		// 表示作業なし
		if (workId == null) {
			workStatusString = "";
		} else {
			
			// 表示作業なし
			if (workId.equals("") == true) {
				workStatusString = "";
			}
			
			if (workId.equals("") == false && percent == 0) {
				workStatusString = "作業開始";
			}
			
			if (workId.equals("") == false && percent > 0) {
				workStatusString = "作業中";
			}
			
			if (workId.equals("") == false && percent < 0) {
				workStatusString = "【エラー】";
			}
		}
		
		return workStatusString;
	}
}
