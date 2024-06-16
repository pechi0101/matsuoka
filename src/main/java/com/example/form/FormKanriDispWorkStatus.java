package com.example.form;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class FormKanriDispWorkStatus implements Serializable {
	
	private String message;
	
	
	// 選択行の情報
	private String selectHouseId;
	private String selectWorkId;
	private String selectColNo;
	
	//------------------------------------------------
	//画面上部に表示する”稼働中の作業”（表が複数表示されるから"Lists"という名前にしてある）
	private ArrayList<ActiveWorkList> activeWorkLists;
	
	//------------------------------------------------
	//画面下部に表示する”全ハウス・全作業の稼働状況”
	private ArrayList<HouseStatus> nonActiveWorlList;
	
	
	//コンストラクタ
	public FormKanriDispWorkStatus() {
		this.message             = "";
		this.selectHouseId       = "";
		this.selectWorkId        = "";
		this.selectColNo         = "";
		this.activeWorkLists     = new ArrayList<ActiveWorkList>();
		this.nonActiveWorlList   = new ArrayList<HouseStatus>();
	}
	
	
	
	
	//------------------------------------------------------------------------------------------------
	//インナークラス：画面上部に表示する”稼働中の作業”
	
	// @Dataアノテーションでgetter、setterが存在する状態にする
	@Data
	public class ActiveWorkList {
		
		private String houseId;
		private String houseName;
		private String workId;
		private String workName;
		
		private ArrayList<ActiveWorkRow> activeWorKRows;
		
		//コンストラクタ
		public ActiveWorkList() {
			this.houseId    = "";
			this.houseName  = "";
			this.workId     = "";
			this.workName   = "";
			this.activeWorKRows = new ArrayList<ActiveWorkRow>();
		}
		
	}
	
	// @Dataアノテーションでgetter、setterが存在する状態にする
	@Data
	public class ActiveWorkRow {
		
		private String houseId;
		private String workId;
		private String colNo;
		
		private String startEmployeeId;
		private String startEmployeeName;
		@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
		private LocalDateTime startDateTime;
		
		private String endEmployeeId;
		private String endEmployeeName;
		@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
		private LocalDateTime endDateTime;
		
		private String percent;
		
		//コンストラクタ
		public ActiveWorkRow() {
			
			this.houseId           = "";
			this.workId            = "";
			this.colNo             = "";
			this.startEmployeeId   = "";
			this.startEmployeeName = "";
			this.startDateTime     = null;
			this.endEmployeeId     = "";
			this.endEmployeeName   = "";
			this.endDateTime       = null;
			this.percent           = "";
		}
		
	}
	
	
	//------------------------------------------------------------------------------------------------
	//インナークラス：画面下部に表示する”全ハウス・全作業の稼働状況”
	
	//★表１行の情報
	// @Dataアノテーションでgetter、setterが存在する状態にする
	@Data
	public class HouseStatus {
		
		private String houseId;
		private String houseName;
		
		private ArrayList<HouseWorkStatus> houseWorkStatus;
		
		public HouseStatus() {
			this.houseId = "";
			this.houseName = "";
			this.houseWorkStatus = new ArrayList<HouseWorkStatus>();
		}
		
	}
	
	//★表１列の情報
	// @Dataアノテーションでgetter、setterが存在する状態にする
	@Data
	public class HouseWorkStatus {
		
		private String workId;
		private String workName;
		private int progressDays;  //リセットから経過した日数
		
		
		public HouseWorkStatus() {
			
			this.workId       = "";
			this.workName     = "";
			this.progressDays = 0;
			
		}
		
	}
	
}
