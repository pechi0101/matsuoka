package com.example.form;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

// @Dataアノテーションでgetter、setterが存在する状態にする
@Data
public class FormKanriDispWorkStatus implements Serializable {
	
	// ------------------------------------------------
	// スマホ側のシステムで使用する際にのみ必要な項目
	private String loginEmployeeId;
	private String loginEmployeeName;
	private String selectedDeviceLabel; // 使用するデバイスのラベル
	// 操作権限
	private boolean editAuthority;
	private boolean administrationAuthority;
	// ------------------------------------------------
	
	private String message;
	
	
	// 選択行の情報
	private String selectHouseId;
	private String selectWorkId;
	private String selectColNo;
	
	//------------------------------------------------
	//画面上部に表示する”稼働中の作業”（表が複数表示されるから"Lists"という名前にしてある）
	//※表を複数持ってるArrayList
	private ArrayList<ActiveWorkList> activeWorkLists;
	
	
	//------------------------------------------------
	//画面中部に表示する”収穫状況”
	private ArrayList<ShukakuStatus> shukakuStatusList;
	
	//------------------------------------------------
	//画面下部に表示する”全ハウス・全作業の稼働状況”
	//※表示する表は１つ。行をArrayListで持ってる
	private ArrayList<String> nonActiveWorkListHeader;     // 一覧表のヘッダ
	private ArrayList<NonActiveWorkRow> nonActiveWorkList; // 一覧表の中身
	
	
	//コンストラクタ
	public FormKanriDispWorkStatus() {
		this.message                  = "";
		this.selectHouseId            = "";
		this.selectWorkId             = "";
		this.selectColNo              = "";
		this.activeWorkLists          = new ArrayList<ActiveWorkList>();
		this.nonActiveWorkListHeader  = new ArrayList<String>();
		this.shukakuStatusList        = new ArrayList<ShukakuStatus>(); 
		this.nonActiveWorkList        = new ArrayList<NonActiveWorkRow>();
		this.editAuthority            = false;
		this.administrationAuthority  = false;
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
		@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
		private LocalDateTime firstStartDateTime;
		
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
	//インナークラス：画面中部に表示する”収穫状況”

	@Data
	public class ShukakuStatus {
		
		private String houseId;
		private String houseName;
		private double caseCount;
		
		public ShukakuStatus() {
			this.houseId = "";
			this.houseName = "";
			this.caseCount = 0;
		}
		
	}
	
	
	
	//------------------------------------------------------------------------------------------------
	//インナークラス：画面下部に表示する”全ハウス・全作業の稼働状況”
	
	//★表１行の情報
	// @Dataアノテーションでgetter、setterが存在する状態にする
	@Data
	public class NonActiveWorkRow {
		
		private String houseId;
		private String houseName;
		
		private ArrayList<NonActiveWorkCol> nonActiveWorkCol;
		
		public NonActiveWorkRow() {
			this.houseId = "";
			this.houseName = "";
			this.nonActiveWorkCol = new ArrayList<NonActiveWorkCol>();
		}
		
	}
	
	//★表１列の情報
	// @Dataアノテーションでgetter、setterが存在する状態にする
	@Data
	public class NonActiveWorkCol {
		
		private String workId;
		private String workName;
		
		@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
		private LocalDateTime startDateTime;
		@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
		private LocalDateTime resetDateTime;
		
		
		private String progressDays;  //リセットから経過した日数  n日経過／稼働中／－
		
		
		public NonActiveWorkCol() {
			
			this.workId       = "";
			this.workName     = "";
			this.progressDays = "";
			
		}
		
	}
	
}
