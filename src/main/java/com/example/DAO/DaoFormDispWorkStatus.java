package com.example.DAO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.example.counst.SpecialUser;
import com.example.form.FormDispWorkStatus;
import com.example.form.FormDispWorkStatusCol;
import com.example.form.FormDispWorkStatusHouse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DaoFormDispWorkStatus {
	
	private JdbcTemplate  jdbcTemplate;
	private String classId = "DaoHouseWorkStatus";
	
	// コンストラクタ
	public DaoFormDispWorkStatus(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	
	// 表示用のデータを取得
	public FormDispWorkStatus getDispData(int intervalDate) {
		
		String pgmId = classId + ".getDispData";
		log.info("【INF】" + pgmId + ":処理開始 期間=[" + intervalDate + "]");
		
		
		// 返却値
		FormDispWorkStatus formDispWorkStatus = new FormDispWorkStatus();
		
		try {
			
			boolean ret = false;
			
			// ------------------------------------------------
			// ハウスの情報を引数.formDispWorkStatusにセット
			ret = this.getHouseData(formDispWorkStatus);
			
			if (ret == false) {
				log.error("【ERR】" + pgmId + ":ハウス情報取得でエラーが発生しました。");
				return null;
			}
			
			
			// ------------------------------------------------
			// 列ごとの進捗情報を引数.formDispWorkStatusにセット
			ret = getColData(formDispWorkStatus, intervalDate);
			
			if (ret == false) {
				log.error("【ERR】" + pgmId + ":ハウスの列情報取得でエラーが発生しました。");
				return null;
			}
			
			
			log.info("【INF】" + pgmId + ":処理終了");
			return formDispWorkStatus;
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return null;
		}
	}
	
	
	
	// ハウスデータを取得
	private boolean getHouseData(FormDispWorkStatus formDispWorkStatus) {
		
		String pgmId = classId + ".getHouseData";
		log.info("【INF】" + pgmId + ":処理開始");
		
		
		try {
			
			ArrayList<FormDispWorkStatusHouse> houseList = new ArrayList<FormDispWorkStatusHouse>();
			
			// 列が存在するハウスのみ取得
			
			String sql = " select distinct";
			sql  = sql + "     TM_HOUSE.HOUSEID";
			sql  = sql + "    ,TM_HOUSE.HOUSENAME";
			sql  = sql + " from";
			sql  = sql + "     TM_HOUSE";
			sql  = sql + " INNER JOIN";
			sql  = sql + "     TM_HOUSECOL";
			sql  = sql + " on  TM_HOUSE.HOUSEID = TM_HOUSECOL.HOUSEID";
			
			
			// queryForListメソッドでSQLを実行し、結果MapのListで受け取る。
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql);
			
			
			for (Map<String, Object> rs: rsList) {
				
				FormDispWorkStatusHouse wkHouse = new FormDispWorkStatusHouse();
				
				wkHouse.setHouseId(rs.get("HOUSEID").toString());
				wkHouse.setHouseName(rs.get("HOUSENAME").toString());
				
				houseList.add(wkHouse);
			}
			
			formDispWorkStatus.setHouseList(houseList);
			
			
			
			log.info("【INF】" + pgmId + ":処理終了 取得件数=[" + Integer.toString(houseList.size()) + "]");
			
			if (houseList.size() == 0) {
				return false;
			}
			
			return true;
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return false;
		}
	}
	

	
	// 列の進捗データを取得
	private boolean getColData(FormDispWorkStatus formDispWorkStatus, int intervalDate) {
		
		String pgmId = classId + ".getColData";
		log.info("【INF】" + pgmId + ":処理開始 ハウス件数=[" + formDispWorkStatus.getHouseList().size() + "]、検索期間=[" + Integer.toString(intervalDate) + "]");
		
		
		try {
			
			
			for (int index = 0; index < formDispWorkStatus.getHouseList().size(); index++) {
				
				
				log.info("【DBG】" + pgmId + ":■ハウスID=[" + formDispWorkStatus.getHouseList().get(index).getHouseId() + "]");
				
				
				ArrayList<FormDispWorkStatusCol> colList = new ArrayList<FormDispWorkStatusCol>();
				
				// 列と最新の進捗情報を取得
				
				String sql = " select";
				sql  = sql + "     VM_HOUSECOL.HOUSEID";
				sql  = sql + "    ,VM_HOUSECOL.COLNO";
				sql  = sql + "    ,VM_HOUSECOL.WORKID";
				sql  = sql + "    ,TM_WORK.WORKNAME";
				sql  = sql + "    ,VM_HOUSECOL.STARTDATETIME_STRING";
				sql  = sql + "    ,VM_HOUSECOL.STARTEMPLOYEEID";
				sql  = sql + "    ,TM_EMPLOYEE_01.EMPLOYEENAME STARTEMPLOYEENAME";
				sql  = sql + "    ,VM_HOUSECOL.ENDDATETIME_STRING";
				sql  = sql + "    ,VM_HOUSECOL.ENDEMPLOYEEID";
				sql  = sql + "    ,TM_EMPLOYEE_02.EMPLOYEENAME ENDEMPLOYEENAME";
				sql  = sql + "    ,VM_HOUSECOL.PERCENT";
				sql  = sql + "    ,VM_HOUSECOL.BIKO";
				sql  = sql + " from (";
				sql  = sql + "     select";
				sql  = sql + "         TM_HOUSECOL.HOUSEID";
				sql  = sql + "        ,TM_HOUSECOL.COLNO";
				sql  = sql + "        ,TT_HOUSE_WORKSTATUS.WORKID";
				sql  = sql + "        ,DATE_FORMAT(TT_HOUSE_WORKSTATUS.STARTDATETIME,'%Y%m%d%H%i%S') STARTDATETIME_STRING";
				sql  = sql + "        ,TT_HOUSE_WORKSTATUS.STARTEMPLOYEEID";
				sql  = sql + "        ,DATE_FORMAT(TT_HOUSE_WORKSTATUS.ENDDATETIME,'%Y%m%d%H%i%S') ENDDATETIME_STRING";
				sql  = sql + "        ,TT_HOUSE_WORKSTATUS.ENDEMPLOYEEID";
				sql  = sql + "        ,TT_HOUSE_WORKSTATUS.PERCENT";
				sql  = sql + "        ,TT_HOUSE_WORKSTATUS.BIKO";
				sql  = sql + "     from";
				sql  = sql + "         TM_HOUSECOL";
				sql  = sql + "     LEFT JOIN";
				sql  = sql + "         TT_HOUSE_WORKSTATUS ";
				sql  = sql + "     on  TM_HOUSECOL.HOUSEID = TT_HOUSE_WORKSTATUS.HOUSEID";
				sql  = sql + "     and TM_HOUSECOL.COLNO   = TT_HOUSE_WORKSTATUS.COLNO";
				sql  = sql + "     and TT_HOUSE_WORKSTATUS.STARTDATETIME = ( ";
				sql  = sql + "         select MAX(STARTDATETIME)";
				sql  = sql + "         from";
				sql  = sql + "             TT_HOUSE_WORKSTATUS WT_HOUSE_WORKSTATUS";
				sql  = sql + "         where";
				sql  = sql + "             WT_HOUSE_WORKSTATUS.HOUSEID = TT_HOUSE_WORKSTATUS.HOUSEID";
				sql  = sql + "         and WT_HOUSE_WORKSTATUS.COLNO   = TT_HOUSE_WORKSTATUS.COLNO";
				sql  = sql + "         ";
				sql  = sql + "         and (WT_HOUSE_WORKSTATUS.STARTEMPLOYEEID IS NULL or WT_HOUSE_WORKSTATUS.STARTEMPLOYEEID  <> '" + SpecialUser.TEST_USER +  "')"; // テストユーザの進捗データは無視
				sql  = sql + "         and (WT_HOUSE_WORKSTATUS.ENDEMPLOYEEID   IS NULL or WT_HOUSE_WORKSTATUS.ENDEMPLOYEEID    <> '" + SpecialUser.TEST_USER +  "')"; // テストユーザの進捗データは無視
				sql  = sql + "         ";
				sql  = sql + "         and WT_HOUSE_WORKSTATUS.STARTDATETIME     >  DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL ? DAY),'%Y-%m-%d')"; // ｎ日前までのデータを表示（【注意】Oracleみたいに２日前を「 日付 - 2 」とすると月跨ぎ、年跨ぎで空白が返却される）
				sql  = sql + "         group by";
				sql  = sql + "             WT_HOUSE_WORKSTATUS.HOUSEID";
				sql  = sql + "            ,WT_HOUSE_WORKSTATUS.COLNO";
				sql  = sql + "            )";
				sql  = sql + "     where";
				sql  = sql + "         TM_HOUSECOL.HOUSEID = ?";  // ハウスＩＤを指定
				sql  = sql + "     ) VM_HOUSECOL";
				sql  = sql + " LEFT JOIN";
				sql  = sql + "     TM_WORK";
				sql  = sql + " on  VM_HOUSECOL.WORKID          = TM_WORK.WORKID";
				sql  = sql + " LEFT JOIN";
				sql  = sql + "     TM_EMPLOYEE TM_EMPLOYEE_01";
				sql  = sql + " on  VM_HOUSECOL.STARTEMPLOYEEID = TM_EMPLOYEE_01.EMPLOYEEID";
				sql  = sql + " LEFT JOIN";
				sql  = sql + "     TM_EMPLOYEE TM_EMPLOYEE_02";
				sql  = sql + " on  VM_HOUSECOL.ENDEMPLOYEEID   = TM_EMPLOYEE_02.EMPLOYEEID";
				
				// 【メモ】：MySQLの日付フォーマット
				// %Y    4 桁の年               例：2024
				// %y    2 桁の年               例：24
				// %c    月                     例：0 ~ 12
				// %m    2 桁の月               例：00 ~ 12
				// %e    日                     例：0 ~ 31
				// %d    2 桁の日               例：00 ~ 31
				// %H    24時制の時間           例：00 ~ 23
				// %h    12時制の時間           例：01 ~ 12
				// %p    午前・午後             例：AM か PM
				// %i    分                     例：00 ~ 59
				// %S,%s 秒                     例：00 ~ 59
				// %f    ミリ秒                 例：000000 ~ 999999
				// %M    月名                   例：January ~ December
				// %b    簡略月名               例：Jan ~ Dec
				// %W    曜日名                 例：Sunday ~ Saturday
				// %b    簡略曜日名             例：Sun ~ Sat
				// %a    12時制の時間・分・秒。 例：21:40:13
				// %T    24時制の時間・分・秒。 例：21:40:13
				
				// queryForListメソッドでSQLを実行し、結果MapのListで受け取る。
				List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql
															,intervalDate
															,formDispWorkStatus.getHouseList().get(index).getHouseId()
															);
				
				
				for (Map<String, Object> rs: rsList) {
					
					// 年月日時分秒までの日時フォーマットを準備
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
					
					FormDispWorkStatusCol wkCol = new FormDispWorkStatusCol();
					
					wkCol.setHouseId(rs.get("HOUSEID").toString());
					
					// ハウスID以外は取得できない可能性があるため、取得結果がnullでない場合のみ値を取得
					// ※nullである場合、rs.get("COLNO").toString() の toString()が異常終了するため
					
					if (rs.get("COLNO") != null) {
						wkCol.setColNo(rs.get("COLNO").toString());
					}
					
					// ------------------------------------------------
					if (rs.get("WORKID") != null) {
						wkCol.setWorkId(rs.get("WORKID").toString());
					}
					if (rs.get("WORKNAME") != null) {
						wkCol.setWorkName(rs.get("WORKNAME").toString());
					}
					
					// ------------------------------------------------
					if (rs.get("STARTEMPLOYEEID") != null) {
						wkCol.setStartEmployeeId(rs.get("STARTEMPLOYEEID").toString());
					}
					if (rs.get("STARTEMPLOYEENAME") != null) {
						wkCol.setStartEmployeeName(rs.get("STARTEMPLOYEENAME").toString());
					}
					if (rs.get("STARTDATETIME_STRING") != null) {
						String startDateTimeString = rs.get("STARTDATETIME_STRING").toString();
						wkCol.setStartDateTime(LocalDateTime.parse(startDateTimeString,formatter));
					}
					
					// ------------------------------------------------
					if (rs.get("ENDEMPLOYEEID") != null) {
						wkCol.setEndEmployeeId(rs.get("ENDEMPLOYEEID").toString());
					}
					if (rs.get("ENDEMPLOYEENAME") != null) {
						wkCol.setEndEmployeeName(rs.get("ENDEMPLOYEENAME").toString());
					}
					if (rs.get("ENDDATETIME_STRING") != null) {
						String endDateTimeString = rs.get("ENDDATETIME_STRING").toString();
						wkCol.setEndDateTime(LocalDateTime.parse(endDateTimeString,formatter));
					}
					
					// ------------------------------------------------
					if (rs.get("PERCENT") != null) {
						wkCol.setPercent(Integer.parseInt(rs.get("PERCENT").toString()));
					}
					
					if (rs.get("BIKO") != null) {
						wkCol.setBiko(rs.get("BIKO").toString());
					}
					
					log.info("【DBG】" + pgmId + ":列ID=[" + wkCol.getColNo() + "]"
													    + "、作業ID=[" + wkCol.getWorkId() + "]"
													    + "、開始作業者=[" + wkCol.getStartEmployeeId() + "]"
													    + "、開始日時=[" + wkCol.getStartDateTime() + "]"
													    + "、終了作業者=[" + wkCol.getEndEmployeeId() + "]"
													    + "、終了日時=[" + wkCol.getEndDateTime() + "]"
													    + "、進捗率=[" + wkCol.getPercent() + "]"
													    );
					colList.add(wkCol);
				}
				
				log.info("【DBG】" + pgmId + ":ハウスID=[" + formDispWorkStatus.getHouseList().get(index).getHouseId() + "]"
												    + "、取得列件数=[" + Integer.toString(colList.size()) + "]");
				formDispWorkStatus.getHouseList().get(index).setColList(colList);
				
				
			}
			
			
			log.info("【INF】" + pgmId + ":処理終了");
			
			
			return true;
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return false;
		}
	}
	
	
}
