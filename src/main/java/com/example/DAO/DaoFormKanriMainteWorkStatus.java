package com.example.DAO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.example.counst.SpecialUser;
import com.example.counst.SpecialWork;
import com.example.form.FormKanriMainteWorkStatusDetail;
import com.example.form.FormKanriMainteWorkStatusList;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DaoFormKanriMainteWorkStatus {
	
	private JdbcTemplate  jdbcTemplate;
	private String classId = "DaoFormKanriMainteWorkStatus";
	
	// コンストラクタ
	public DaoFormKanriMainteWorkStatus(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	// リストを取得
	public FormKanriMainteWorkStatusList getDispWorkStatusList(String houseId,String colNo,String workId,LocalDateTime startDateTimeFr,LocalDateTime startDateTimeTo) {
		
		// 年月日時分秒までの日時フォーマットを準備
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		String pgmId = classId + ".getWorkStatusList";
		log.info("【INF】" + pgmId + ":処理開始 ハウスID=[" + houseId + "]列No=[" + colNo + "]作業ID=[" + workId + "]、開始日時From=[" + startDateTimeFr + "]、開始日時From=[" + startDateTimeTo + "]");
		
		
		// 返却値
		FormKanriMainteWorkStatusList retForm = new FormKanriMainteWorkStatusList();
		try {
			
			// ★★★★★★★★★★★★★★★★★★★★★★★★★★★
			//
			// 動的SQLであるためSQL中に直接引数の検索条件を埋め込む
			//
			// ★★★★★★★★★★★★★★★★★★★★★★★★★★★
			
			
			//【メモ】：画面に削除した作業をグレーアウトして表示するために削除済みの作業も検索対象にする
			
			
			String sql = " select * from";
			sql  = sql + " (";
			sql  = sql + " select";
			sql  = sql + "     TT_HOUSE_WORKSTATUS.HOUSEID";
			sql  = sql + "    ,TM_HOUSE.HOUSENAME";
			sql  = sql + "    ,TT_HOUSE_WORKSTATUS.COLNO";
			sql  = sql + "    ,TT_HOUSE_WORKSTATUS.WORKID";
			sql  = sql + "    ,TM_WORK.WORKNAME";
			sql  = sql + "    ,DATE_FORMAT(TT_HOUSE_WORKSTATUS.STARTDATETIME,'%Y%m%d%H%i%S') STARTDATETIME_STRING";// YYYYMMDDHHMMSS形式で取得（時間は24時間制）
			sql  = sql + "    ,TT_HOUSE_WORKSTATUS.STARTEMPLOYEEID";
			sql  = sql + "    ,TM_EMPLOYEE_START.EMPLOYEENAME STARTEMPLOYEENAME";
			sql  = sql + "    ,DATE_FORMAT(TT_HOUSE_WORKSTATUS.ENDDATETIME,'%Y%m%d%H%i%S') ENDDATETIME_STRING";// YYYYMMDDHHMMSS形式で取得（時間は24時間制）
			sql  = sql + "    ,TT_HOUSE_WORKSTATUS.ENDEMPLOYEEID";
			sql  = sql + "    ,TM_EMPLOYEE_END.EMPLOYEENAME ENDEMPLOYEENAME";
			sql  = sql + "    ,TT_HOUSE_WORKSTATUS.PERCENT_START";
			sql  = sql + "    ,TT_HOUSE_WORKSTATUS.PERCENT";
			sql  = sql + "    ,ifnull(TT_HOUSE_WORKSTATUS.DELETEFLG,'0') DELETEFLG";
			sql  = sql + "    ,DATE_FORMAT(TT_HOUSE_WORKSTATUS.DELETEYMDHMS,'%Y%m%d%H%i%S') DELETEYMDHMS_STRING";// YYYYMMDDHHMMSS形式で取得（時間は24時間制）
			sql  = sql + "    ,TT_HOUSE_WORKSTATUS.BIKO";
			sql  = sql + "    ,null BOXCOUNT";
			sql  = sql + " from";
			sql  = sql + "     TT_HOUSE_WORKSTATUS";
			sql  = sql + " left outer join TM_HOUSE";
			sql  = sql + " on  TT_HOUSE_WORKSTATUS.HOUSEID  = TM_HOUSE.HOUSEID";
			sql  = sql + " left outer join TM_WORK";
			sql  = sql + " on  TT_HOUSE_WORKSTATUS.WORKID   = TM_WORK.WORKID";
			sql  = sql + " left outer join TM_EMPLOYEE TM_EMPLOYEE_START";
			sql  = sql + " on  TT_HOUSE_WORKSTATUS.STARTEMPLOYEEID  = TM_EMPLOYEE_START.EMPLOYEEID";
			sql  = sql + " left outer join TM_EMPLOYEE TM_EMPLOYEE_END";
			sql  = sql + " on  TT_HOUSE_WORKSTATUS.ENDEMPLOYEEID    = TM_EMPLOYEE_END.EMPLOYEEID";
			sql  = sql + " where";
			sql  = sql + "     TT_HOUSE_WORKSTATUS.STARTEMPLOYEEID <> '" + SpecialUser.TEST_USER + "'";//テストユーザは対象にしない
			if (houseId != null && "".equals(houseId) == false) {
			sql  = sql + " and TT_HOUSE_WORKSTATUS.HOUSEID          = '" + houseId + "'";
			}
			if (colNo != null   && "".equals(colNo)   == false) {
			sql  = sql + " and TT_HOUSE_WORKSTATUS.COLNO            = '" + colNo + "'";
			}
			if (workId != null  && "".equals(workId)  == false) {
			sql  = sql + " and TT_HOUSE_WORKSTATUS.WORKID           = '" + workId + "'";
			}
			if (startDateTimeFr != null) {
			sql  = sql + " and TT_HOUSE_WORKSTATUS.STARTDATETIME   >= '" + formatter.format(startDateTimeFr) + "'";
			}
			if (startDateTimeTo != null) {
			sql  = sql + " and TT_HOUSE_WORKSTATUS.STARTDATETIME   <= '" + formatter.format(startDateTimeTo) + "'";
			}
			//------------------------------------------------
			sql  = sql + "                union all";
			//------------------------------------------------
			sql  = sql + " select";
			sql  = sql + "     TT_HOUSE_WORKSTATUS_SHUKAKU.HOUSEID";
			sql  = sql + "    ,TM_HOUSE.HOUSENAME";
			sql  = sql + "    ,null COLNO";
			sql  = sql + "    ,TT_HOUSE_WORKSTATUS_SHUKAKU.WORKID";
			sql  = sql + "    ,TM_WORK.WORKNAME";
			sql  = sql + "    ,DATE_FORMAT(TT_HOUSE_WORKSTATUS_SHUKAKU.STARTDATETIME,'%Y%m%d%H%i%S') STARTDATETIME_STRING";// YYYYMMDDHHMMSS形式で取得（時間は24時間制）
			sql  = sql + "    ,TT_HOUSE_WORKSTATUS_SHUKAKU.STARTEMPLOYEEID";
			sql  = sql + "    ,TM_EMPLOYEE_START.EMPLOYEENAME STARTEMPLOYEENAME";
			sql  = sql + "    ,DATE_FORMAT(TT_HOUSE_WORKSTATUS_SHUKAKU.ENDDATETIME,'%Y%m%d%H%i%S') ENDDATETIME_STRING";// YYYYMMDDHHMMSS形式で取得（時間は24時間制）
			sql  = sql + "    ,TT_HOUSE_WORKSTATUS_SHUKAKU.ENDEMPLOYEEID";
			sql  = sql + "    ,TM_EMPLOYEE_END.EMPLOYEENAME ENDEMPLOYEENAME";
			sql  = sql + "    ,TT_HOUSE_WORKSTATUS_SHUKAKU.PERCENT_START";
			sql  = sql + "    ,TT_HOUSE_WORKSTATUS_SHUKAKU.PERCENT";
			sql  = sql + "    ,ifnull(TT_HOUSE_WORKSTATUS_SHUKAKU.DELETEFLG,'0') DELETEFLG";
			sql  = sql + "    ,DATE_FORMAT(TT_HOUSE_WORKSTATUS_SHUKAKU.DELETEYMDHMS,'%Y%m%d%H%i%S') DELETEYMDHMS_STRING";// YYYYMMDDHHMMSS形式で取得（時間は24時間制）
			sql  = sql + "    ,TT_HOUSE_WORKSTATUS_SHUKAKU.BIKO";
			sql  = sql + "    ,TT_HOUSE_WORKSTATUS_SHUKAKU.BOXCOUNT";
			sql  = sql + " from";
			sql  = sql + "     TT_HOUSE_WORKSTATUS_SHUKAKU";
			sql  = sql + " left outer join TM_HOUSE";
			sql  = sql + " on  TT_HOUSE_WORKSTATUS_SHUKAKU.HOUSEID          = TM_HOUSE.HOUSEID";
			sql  = sql + " left outer join TM_WORK";
			sql  = sql + " on  TT_HOUSE_WORKSTATUS_SHUKAKU.WORKID           = TM_WORK.WORKID";
			sql  = sql + " left outer join TM_EMPLOYEE TM_EMPLOYEE_START";
			sql  = sql + " on  TT_HOUSE_WORKSTATUS_SHUKAKU.STARTEMPLOYEEID  = TM_EMPLOYEE_START.EMPLOYEEID";
			sql  = sql + " left outer join TM_EMPLOYEE TM_EMPLOYEE_END";
			sql  = sql + " on  TT_HOUSE_WORKSTATUS_SHUKAKU.ENDEMPLOYEEID    = TM_EMPLOYEE_END.EMPLOYEEID";
			sql  = sql + " where";
			sql  = sql + "     TT_HOUSE_WORKSTATUS_SHUKAKU.STARTEMPLOYEEID <> '" + SpecialUser.TEST_USER + "'";//テストユーザは対象にしない
			if (houseId != null && "".equals(houseId) == false) {
			sql  = sql + " and TT_HOUSE_WORKSTATUS_SHUKAKU.HOUSEID          = '" + houseId + "'";
			}
			if (colNo != null   && "".equals(colNo)   == false) {
			sql  = sql + " and TT_HOUSE_WORKSTATUS_SHUKAKU.COLNO            = 'XX'"; //作業(収穫)テーブルは列NoはXX固定で登録されている
			}
			if (workId != null  && "".equals(workId)  == false) {
			sql  = sql + " and TT_HOUSE_WORKSTATUS_SHUKAKU.WORKID           = '" + workId + "'";
			}
			if (startDateTimeFr != null) {
			sql  = sql + " and TT_HOUSE_WORKSTATUS_SHUKAKU.STARTDATETIME   >= '" + formatter.format(startDateTimeFr) + "'";
			}
			if (startDateTimeTo != null) {
			sql  = sql + " and TT_HOUSE_WORKSTATUS_SHUKAKU.STARTDATETIME   <= '" + formatter.format(startDateTimeTo) + "'";
			}
			sql  = sql + " ) as WORK_VIEW";
			sql  = sql + " order by";
			sql  = sql + "     HOUSEID";
			sql  = sql + "    ,WORKID";
			sql  = sql + "    ,COLNO";
			sql  = sql + "    ,STARTDATETIME_STRING";
			
			//log.info("【INF】" + pgmId + ":検索SQL=[" + sql + "]");
			
			
			
			/*
			 * 【メモ】：MySQLの日付フォーマット
			 * %Y    4 桁の年               例：2024
			 * %y    2 桁の年               例：24
			 * %c    月                     例：0 ~ 12
			 * %m    2 桁の月               例：00 ~ 12
			 * %e    日                     例：0 ~ 31
			 * %d    2 桁の日               例：00 ~ 31
			 * %H    24時制の時間           例：00 ~ 23
			 * %h    12時制の時間           例：01 ~ 12
			 * %p    午前・午後             例：AM か PM
			 * %i    分                     例：00 ~ 59
			 * %S,%s 秒                     例：00 ~ 59
			 * %f    ミリ秒                 例：000000 ~ 999999
			 * %M    月名                   例：January ~ December
			 * %b    簡略月名               例：Jan ~ Dec
			 * %W    曜日名                 例：Sunday ~ Saturday
			 * %b    簡略曜日名             例：Sun ~ Sat
			 * %a    12時制の時間・分・秒。 例：21:40:13
			 * %T    24時制の時間・分・秒。 例：21:40:13
			 */
			
			
			
			// queryForListメソッドでSQLを実行し、結果MapのListで受け取る。
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql);
			
			
			// 【重要】この変数に作業開始、終了日時を文字列でセットし、作業状況の判定に使用する。
			String startDateTimeString = "";
			String endDateTimeString = "";
			String deleteDateTimeString = "";
			
			
			for (Map<String, Object> rs: rsList) {
				
				
				FormKanriMainteWorkStatusDetail detail = new FormKanriMainteWorkStatusDetail();
				
				
				
				// ハウスID
				detail.setHouseId(rs.get("HOUSEID").toString());
				
				//ハウス名 ※ハウス名がマスタから取得できない場合は"未登録"と一覧表示
				if (rs.get("HOUSENAME") != null) {
					detail.setHouseName(rs.get("HOUSENAME").toString());
				} else {
					detail.setHouseName("未登録");
				}
				
				//列No ※収穫の場合、列NoをSQLでnullにしている。その場合は空白表示
				if (rs.get("COLNO") != null) {
					detail.setColNo(rs.get("COLNO").toString());
				}else{
					detail.setColNo("");
				}
				
				// 作業ID
				detail.setWorkId(rs.get("WORKID").toString());
				
				//作業名 ※作業名がマスタから取得できない場合は"未登録"と一覧表示
				if (rs.get("WORKNAME") != null) {
					detail.setWorkName(rs.get("WORKNAME").toString());
				} else {
					detail.setWorkName("未登録");
				}
				
				
				
				// 年月日時分秒までの日時フォーマットを準備
				DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
				DateTimeFormatter formatterDate     = DateTimeFormatter.ofPattern("yyyyMMdd");
				DateTimeFormatter formatterTime     = DateTimeFormatter.ofPattern("HHmmss");
				
				String wkDate; //YYYYMMDDの文字列
				String wkTime; //HHMMSSの文字列
				
				// 作業開始日時
				startDateTimeString = rs.get("STARTDATETIME_STRING").toString();
				wkDate = startDateTimeString.substring(0, 8);
				wkTime = startDateTimeString.substring(8, 14);
				detail.setStartDate(LocalDate.parse(wkDate,formatterDate));
				detail.setStartTime(LocalTime.parse(wkTime,formatterTime));
				detail.setStartDateTime(LocalDateTime.parse(startDateTimeString,formatterDateTime));
				
				
				// 作業開始社員ID
				detail.setStartEmployeeId(rs.get("STARTEMPLOYEEID").toString());
				// 作業開始社員名
				detail.setStartEmployeeName(rs.get("STARTEMPLOYEENAME").toString());
				
				
				// 作業終了日時
				if (rs.get("ENDDATETIME_STRING") != null) {
					endDateTimeString   = rs.get("ENDDATETIME_STRING").toString();
					wkDate = endDateTimeString.substring(0, 8);
					wkTime = endDateTimeString.substring(8, 14);
					detail.setEndDate(LocalDate.parse(wkDate,formatterDate));
					detail.setEndTime(LocalTime.parse(wkTime,formatterTime));
					detail.setEndDateTime(LocalDateTime.parse(endDateTimeString,formatterDateTime));
				}
				
				// 作業終了社員ID
				if (rs.get("ENDEMPLOYEEID") != null) {
					detail.setEndEmployeeId(rs.get("ENDEMPLOYEEID").toString());
				}
				// 作業終了社員名
				if (rs.get("ENDEMPLOYEENAME") != null) {
					detail.setEndEmployeeName(rs.get("ENDEMPLOYEENAME").toString());
				}
				
				// 進捗率_開始
				detail.setPercentStart(rs.get("PERCENT_START").toString());
				
				// 進捗率_終了
				detail.setPercent(rs.get("PERCENT").toString());
				
				// 削除フラグ
				detail.setDeleteFlg(rs.get("DELETEFLG").toString().equals("1")); //取得結果が"1"ならTrueをセットする
				
				
				// 削除日時
				if (rs.get("DELETEYMDHMS_STRING") != null) {
					deleteDateTimeString = rs.get("DELETEYMDHMS_STRING").toString();
					detail.setDeleteymdhms(LocalDateTime.parse(deleteDateTimeString,formatterDateTime));
				}
				
				// 備考
				if (rs.get("BIKO") != null) {
					detail.setBiko(rs.get("BIKO").toString());
				}
				
				// 収穫箱数 ※収穫作業”以外”である場合は収穫箱数はnullにしてる。その場合は空白表示
				if (rs.get("BOXCOUNT") != null) {
					detail.setBoxCount(rs.get("BOXCOUNT").toString());
				}
				
				retForm.addWorkStatus(detail);
				
			}
			
			
			log.info("【INF】" + pgmId + ":処理終了 取得件数=[" + Integer.toString(retForm.getWorkStatusList().size()) + "]件");
			return retForm;
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了[" + e.toString() + "]");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return null;
		}
	}
	
	
	
	// 詳細を取得(ハウスID、列No、作業ID、作業開始日時指定)
	public FormKanriMainteWorkStatusDetail getDispWorkStatusDatail(String houseId,String colNo,String workId,LocalDateTime startDateTime) {
		
		// 年月日時分秒までの日時フォーマットを準備
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		
		String pgmId = classId + ".getWorkStatusDatail";
		log.info("【INF】" + pgmId + ":処理開始 ハウスID=[" + houseId + "]列No=[" + colNo + "]作業ID=[" + workId + "]、開始日時=[" + startDateTime + "]");
		
		
		// 返却値
		FormKanriMainteWorkStatusDetail retForm = new FormKanriMainteWorkStatusDetail();
		
		try {
			
			String sql = " select * from";
			sql  = sql + " (";
			sql  = sql + " select";
			sql  = sql + "     TT_HOUSE_WORKSTATUS.HOUSEID";
			sql  = sql + "    ,TM_HOUSE.HOUSENAME";
			sql  = sql + "    ,TT_HOUSE_WORKSTATUS.COLNO";
			sql  = sql + "    ,TT_HOUSE_WORKSTATUS.WORKID";
			sql  = sql + "    ,TM_WORK.WORKNAME";
			sql  = sql + "    ,DATE_FORMAT(TT_HOUSE_WORKSTATUS.STARTDATETIME,'%Y%m%d%H%i%S') STARTDATETIME_STRING";// YYYYMMDDHHMMSS形式で取得（時間は24時間制）
			sql  = sql + "    ,TT_HOUSE_WORKSTATUS.STARTEMPLOYEEID";
			sql  = sql + "    ,TM_EMPLOYEE_START.EMPLOYEENAME STARTEMPLOYEENAME";
			sql  = sql + "    ,DATE_FORMAT(TT_HOUSE_WORKSTATUS.ENDDATETIME,'%Y%m%d%H%i%S') ENDDATETIME_STRING";// YYYYMMDDHHMMSS形式で取得（時間は24時間制）
			sql  = sql + "    ,TT_HOUSE_WORKSTATUS.ENDEMPLOYEEID";
			sql  = sql + "    ,TM_EMPLOYEE_END.EMPLOYEENAME ENDEMPLOYEENAME";
			sql  = sql + "    ,TT_HOUSE_WORKSTATUS.PERCENT_START";
			sql  = sql + "    ,TT_HOUSE_WORKSTATUS.PERCENT";
			sql  = sql + "    ,ifnull(TT_HOUSE_WORKSTATUS.DELETEFLG,'0') DELETEFLG";
			sql  = sql + "    ,DATE_FORMAT(TT_HOUSE_WORKSTATUS.DELETEYMDHMS,'%Y%m%d%H%i%S') DELETEYMDHMS_STRING";// YYYYMMDDHHMMSS形式で取得（時間は24時間制）
			sql  = sql + "    ,TT_HOUSE_WORKSTATUS.BIKO";
			sql  = sql + "    ,null BOXCOUNT";
			sql  = sql + " from";
			sql  = sql + "     TT_HOUSE_WORKSTATUS";
			sql  = sql + " left outer join TM_HOUSE";
			sql  = sql + " on  TT_HOUSE_WORKSTATUS.HOUSEID          = TM_HOUSE.HOUSEID";
			sql  = sql + " left outer join TM_WORK";
			sql  = sql + " on  TT_HOUSE_WORKSTATUS.WORKID           = TM_WORK.WORKID";
			sql  = sql + " left outer join TM_EMPLOYEE TM_EMPLOYEE_START";
			sql  = sql + " on  TT_HOUSE_WORKSTATUS.STARTEMPLOYEEID  = TM_EMPLOYEE_START.EMPLOYEEID";
			sql  = sql + " left outer join TM_EMPLOYEE TM_EMPLOYEE_END";
			sql  = sql + " on  TT_HOUSE_WORKSTATUS.ENDEMPLOYEEID    = TM_EMPLOYEE_END.EMPLOYEEID";
			sql  = sql + " where";
			sql  = sql + "     TT_HOUSE_WORKSTATUS.HOUSEID          = ?";
			sql  = sql + " and TT_HOUSE_WORKSTATUS.COLNO            = ?";
			sql  = sql + " and TT_HOUSE_WORKSTATUS.WORKID           = ?";
			sql  = sql + " and TT_HOUSE_WORKSTATUS.STARTDATETIME    = ?";
			sql  = sql + " and TT_HOUSE_WORKSTATUS.STARTEMPLOYEEID <> ?";//テストユーザは対象にしない
			//------------------------------------------------
			sql  = sql + "                union all";
			//------------------------------------------------
			sql  = sql + " select";
			sql  = sql + "     TT_HOUSE_WORKSTATUS_SHUKAKU.HOUSEID";
			sql  = sql + "    ,TM_HOUSE.HOUSENAME";
			sql  = sql + "    ,null COLNO";
			sql  = sql + "    ,TT_HOUSE_WORKSTATUS_SHUKAKU.WORKID";
			sql  = sql + "    ,TM_WORK.WORKNAME";
			sql  = sql + "    ,DATE_FORMAT(TT_HOUSE_WORKSTATUS_SHUKAKU.STARTDATETIME,'%Y%m%d%H%i%S') STARTDATETIME_STRING";// YYYYMMDDHHMMSS形式で取得（時間は24時間制）
			sql  = sql + "    ,TT_HOUSE_WORKSTATUS_SHUKAKU.STARTEMPLOYEEID";
			sql  = sql + "    ,TM_EMPLOYEE_START.EMPLOYEENAME STARTEMPLOYEENAME";
			sql  = sql + "    ,DATE_FORMAT(TT_HOUSE_WORKSTATUS_SHUKAKU.ENDDATETIME,'%Y%m%d%H%i%S') ENDDATETIME_STRING";// YYYYMMDDHHMMSS形式で取得（時間は24時間制）
			sql  = sql + "    ,TT_HOUSE_WORKSTATUS_SHUKAKU.ENDEMPLOYEEID";
			sql  = sql + "    ,TM_EMPLOYEE_END.EMPLOYEENAME ENDEMPLOYEENAME";
			sql  = sql + "    ,TT_HOUSE_WORKSTATUS_SHUKAKU.PERCENT_START";
			sql  = sql + "    ,TT_HOUSE_WORKSTATUS_SHUKAKU.PERCENT";
			sql  = sql + "    ,ifnull(TT_HOUSE_WORKSTATUS_SHUKAKU.DELETEFLG,'0') DELETEFLG";
			sql  = sql + "    ,DATE_FORMAT(TT_HOUSE_WORKSTATUS_SHUKAKU.DELETEYMDHMS,'%Y%m%d%H%i%S') DELETEYMDHMS_STRING";// YYYYMMDDHHMMSS形式で取得（時間は24時間制）
			sql  = sql + "    ,TT_HOUSE_WORKSTATUS_SHUKAKU.BIKO";
			sql  = sql + "    ,TT_HOUSE_WORKSTATUS_SHUKAKU.BOXCOUNT";
			sql  = sql + " from";
			sql  = sql + "     TT_HOUSE_WORKSTATUS_SHUKAKU";
			sql  = sql + " left outer join TM_HOUSE";
			sql  = sql + " on  TT_HOUSE_WORKSTATUS_SHUKAKU.HOUSEID          = TM_HOUSE.HOUSEID";
			sql  = sql + " left outer join TM_WORK";
			sql  = sql + " on  TT_HOUSE_WORKSTATUS_SHUKAKU.WORKID           = TM_WORK.WORKID";
			sql  = sql + " left outer join TM_EMPLOYEE TM_EMPLOYEE_START";
			sql  = sql + " on  TT_HOUSE_WORKSTATUS_SHUKAKU.STARTEMPLOYEEID  = TM_EMPLOYEE_START.EMPLOYEEID";
			sql  = sql + " left outer join TM_EMPLOYEE TM_EMPLOYEE_END";
			sql  = sql + " on  TT_HOUSE_WORKSTATUS_SHUKAKU.ENDEMPLOYEEID    = TM_EMPLOYEE_END.EMPLOYEEID";
			sql  = sql + " where";
			sql  = sql + "     TT_HOUSE_WORKSTATUS_SHUKAKU.HOUSEID          = ?";
			sql  = sql + " and TT_HOUSE_WORKSTATUS_SHUKAKU.COLNO            = 'XX'"; //作業(収穫)テーブルは全て列NoがXX固定
			sql  = sql + " and TT_HOUSE_WORKSTATUS_SHUKAKU.WORKID           = ?";
			sql  = sql + " and TT_HOUSE_WORKSTATUS_SHUKAKU.STARTDATETIME    = ?";
			sql  = sql + " and TT_HOUSE_WORKSTATUS_SHUKAKU.STARTEMPLOYEEID <> ?";//テストユーザは対象にしない
			sql  = sql + " ) as WORK_VIEW";
			sql  = sql + " order by";
			sql  = sql + "     HOUSEID";
			sql  = sql + "    ,WORKID";
			sql  = sql + "    ,COLNO";
			sql  = sql + "    ,STARTDATETIME_STRING";
			
			
			
			/*
			 * 【メモ】：MySQLの日付フォーマット
			 * %Y    4 桁の年               例：2024
			 * %y    2 桁の年               例：24
			 * %c    月                     例：0 ~ 12
			 * %m    2 桁の月               例：00 ~ 12
			 * %e    日                     例：0 ~ 31
			 * %d    2 桁の日               例：00 ~ 31
			 * %H    24時制の時間           例：00 ~ 23
			 * %h    12時制の時間           例：01 ~ 12
			 * %p    午前・午後             例：AM か PM
			 * %i    分                     例：00 ~ 59
			 * %S,%s 秒                     例：00 ~ 59
			 * %f    ミリ秒                 例：000000 ~ 999999
			 * %M    月名                   例：January ~ December
			 * %b    簡略月名               例：Jan ~ Dec
			 * %W    曜日名                 例：Sunday ~ Saturday
			 * %b    簡略曜日名             例：Sun ~ Sat
			 * %a    12時制の時間・分・秒。 例：21:40:13
			 * %T    24時制の時間・分・秒。 例：21:40:13
			 */
			
			
			
			// queryForListメソッドでSQLを実行し、結果MapのListで受け取る。
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql
																,houseId
																,colNo
																,workId
																,formatter.format(startDateTime)
																,SpecialUser.TEST_USER
																,houseId
																,workId
																,formatter.format(startDateTime)
																,SpecialUser.TEST_USER);
			
			
			// 【重要】この変数に作業開始、終了日時を文字列でセットし、作業状況の判定に使用する。
			String startDateTimeString = "";
			String endDateTimeString = "";
			String deleteDateTimeString = "";
			
			
			for (Map<String, Object> rs: rsList) {
				
				// 年月日時分秒までの日時フォーマットを準備
				DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
				DateTimeFormatter formatterDate     = DateTimeFormatter.ofPattern("yyyyMMdd");
				DateTimeFormatter formatterTime     = DateTimeFormatter.ofPattern("HHmmss");
				
				String wkDate; //YYYYMMDDの文字列
				String wkTime; //HHMMSSの文字列
				
				
				//------------------------------------------------
				// 元々の(修正前の)主キー情報(一覧画面から引き継いだ作業の主キー情報)
				
				//ハウスID
				retForm.setBeforeHouseId(rs.get("HOUSEID").toString());
				
				//列No ※収穫の場合、列NoをSQLでnullにしている。その場合はXXをセット1
				if (rs.get("COLNO") != null) {
					retForm.setBeforeColNo(rs.get("COLNO").toString());
				}else{
					retForm.setBeforeColNo("XX");
				}
				
				// 作業ID
				retForm.setBeforeWorkId(rs.get("WORKID").toString());
				
				// 作業開始日時
				startDateTimeString = rs.get("STARTDATETIME_STRING").toString();
				retForm.setBeforeStartDateTime(LocalDateTime.parse(startDateTimeString,formatterDateTime));
				
				
				
				//------------------------------------------------
				// 表示情報
				
				// ハウスID
				retForm.setHouseId(rs.get("HOUSEID").toString());
				
				//ハウス名 ※ハウス名がマスタから取得できない場合は"未登録"と一覧表示
				if (rs.get("HOUSENAME") != null) {
					retForm.setHouseName(rs.get("HOUSENAME").toString());
				} else {
					retForm.setHouseName("未登録");
				}
				
				//列No ※収穫の場合、列NoをSQLでnullにしている。その場合は空白表示
				if (rs.get("COLNO") != null) {
					retForm.setColNo(rs.get("COLNO").toString());
				}else{
					retForm.setColNo("");
				}
				
				// 作業ID
				retForm.setWorkId(rs.get("WORKID").toString());
				
				//作業名 ※作業名がマスタから取得できない場合は"未登録"と一覧表示
				if (rs.get("WORKNAME") != null) {
					retForm.setWorkName(rs.get("WORKNAME").toString());
				} else {
					retForm.setWorkName("未登録");
				}
				
				// 作業開始日時
				startDateTimeString = rs.get("STARTDATETIME_STRING").toString();
				wkDate = startDateTimeString.substring(0, 8);
				wkTime = startDateTimeString.substring(8, 14);
				retForm.setStartDate(LocalDate.parse(wkDate,formatterDate));
				retForm.setStartTime(LocalTime.parse(wkTime,formatterTime));
				retForm.setStartDateTime(LocalDateTime.parse(startDateTimeString,formatterDateTime));
				
				
				// 作業開始社員ID
				retForm.setStartEmployeeId(rs.get("STARTEMPLOYEEID").toString());
				// 作業開始社員名
				retForm.setStartEmployeeName(rs.get("STARTEMPLOYEENAME").toString());
				
				
				// 作業終了日時
				if (rs.get("ENDDATETIME_STRING") != null) {
					endDateTimeString   = rs.get("ENDDATETIME_STRING").toString();
					wkDate = endDateTimeString.substring(0, 8);
					wkTime = endDateTimeString.substring(8, 14);
					retForm.setEndDate(LocalDate.parse(wkDate,formatterDate));
					retForm.setEndTime(LocalTime.parse(wkTime,formatterTime));
					retForm.setEndDateTime(LocalDateTime.parse(endDateTimeString,formatterDateTime));
				}
				
				// 作業終了社員ID
				if (rs.get("ENDEMPLOYEEID") != null) {
					retForm.setEndEmployeeId(rs.get("ENDEMPLOYEEID").toString());
				}
				
				// 作業終了社員名
				if (rs.get("ENDEMPLOYEENAME") != null) {
					retForm.setEndEmployeeName(rs.get("ENDEMPLOYEENAME").toString());
				}
				
				// 進捗率_開始
				retForm.setPercentStart(rs.get("PERCENT_START").toString());
				
				// 進捗率_終了
				retForm.setPercent(rs.get("PERCENT").toString());
				
				// 削除フラグ
				retForm.setDeleteFlg(rs.get("DELETEFLG").toString().equals("1")); //取得結果が"1"ならTrueをセットする
				
				
				// 削除日時
				if (rs.get("DELETEYMDHMS_STRING") != null) {
					deleteDateTimeString = rs.get("DELETEYMDHMS_STRING").toString();
					retForm.setDeleteymdhms(LocalDateTime.parse(deleteDateTimeString,formatterDateTime));
				}
				
				// 備考
				if (rs.get("BIKO") != null) {
					retForm.setBiko(rs.get("BIKO").toString());
				}
				
				// 収穫箱数 ※収穫作業”以外”である場合は収穫箱数はnullにしてる。その場合は空白表示
				if (rs.get("BOXCOUNT") != null) {
					retForm.setBoxCount(rs.get("BOXCOUNT").toString());
				}
				
				//1件のみ取得するためLOOPしない
				break;
			}
			
			log.info("【INF】" + pgmId + ":処理終了");
			return retForm;
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return null;
		}
	}
	
	
	
	// 詳細が存在するかチェック(ハウスID、列No、作業ID、作業開始日時指定)
	public boolean isExists(String houseId,String colNo,String workId,LocalDateTime startDateTime) {
		
		// 年月日時分秒までの日時フォーマットを準備
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		String pgmId = classId + ".isExists";
		log.info("【INF】" + pgmId + ":処理開始 ハウスID=[" + houseId + "]列No=[" + colNo + "]作業ID=[" + workId + "]、開始日時=[" + startDateTime + "]");
		
		int count = 0;
		
		try {
			
			if (houseId.equals(SpecialWork.SHUKAKU) == true) {
				
				
				String sql = " select";
				sql  = sql + "     count(*)";
				sql  = sql + " from";
				sql  = sql + "     TT_HOUSE_WORKSTATUS_SHUKAKU";
				sql  = sql + " where";
				sql  = sql + "     TT_HOUSE_WORKSTATUS_SHUKAKU.HOUSEID          = ?";
				sql  = sql + " and TT_HOUSE_WORKSTATUS_SHUKAKU.COLNO            = 'XX'"; //作業(収穫)テーブルは全て列NoがXX固定
				sql  = sql + " and TT_HOUSE_WORKSTATUS_SHUKAKU.WORKID           = ?";
				sql  = sql + " and TT_HOUSE_WORKSTATUS_SHUKAKU.STARTDATETIME    = ?";
				sql  = sql + " and TT_HOUSE_WORKSTATUS_SHUKAKU.STARTEMPLOYEEID <> ?";//テストユーザは対象にしない
				
				count = this.jdbcTemplate.queryForObject(sql
													,Integer.class
													,houseId
													,workId
													,formatter.format(startDateTime)
													,SpecialUser.TEST_USER);
				
			}else{
				
				String sql = " select";
				sql  = sql + "     count(*)";
				sql  = sql + " from";
				sql  = sql + "     TT_HOUSE_WORKSTATUS";
				sql  = sql + " where";
				sql  = sql + "     TT_HOUSE_WORKSTATUS.HOUSEID          = ?";
				sql  = sql + " and TT_HOUSE_WORKSTATUS.COLNO            = ?";
				sql  = sql + " and TT_HOUSE_WORKSTATUS.WORKID           = ?";
				sql  = sql + " and TT_HOUSE_WORKSTATUS.STARTDATETIME    = ?";
				sql  = sql + " and TT_HOUSE_WORKSTATUS.STARTEMPLOYEEID <> ?";//テストユーザは対象にしない
				
				count = this.jdbcTemplate.queryForObject(sql
													,Integer.class
													,houseId
													,colNo
													,workId
													,formatter.format(startDateTime)
													,SpecialUser.TEST_USER);
				
			}
			
			log.info("【INF】" + pgmId + ":処理終了 件数=[" + Integer.toString(count) + "]");
			
			// 件数が１件以上である場合Trueを返却
			return count >= 1;
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return false;
		}
	}
	
	
	
	// データ登録
	public boolean registWorkStatus(FormKanriMainteWorkStatusDetail detail ,String userName,String registPgmId) {
		
		// 年月日時分秒までの日時フォーマットを準備
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		String pgmId = classId + ".registWorkStatus";
		log.info("【INF】" + pgmId + ":処理開始 ハウスID=[" + detail.getHouseId() + "]、列No=[" + detail.getColNo() + "]、作業ID=[" + detail.getWorkId() + "]、開始日時=[" + formatter.format(detail.getStartDateTime()) + "]");
		
		try {
			
			int ret = 0;
			
			if (detail.getHouseId().equals(SpecialWork.SHUKAKU) == true) {
					
					
					//------------------------------------------------
					// ハウス作業(収穫)進捗テーブルの登録
					
					
					String sql = " insert into TT_HOUSE_WORKSTATUS_SHUKAKU (";
					sql  = sql + "     HOUSEID";
					sql  = sql + "    ,WORKID";
					sql  = sql + "    ,COLNO";
					sql  = sql + "    ,STARTDATETIME";
					sql  = sql + "    ,STARTEMPLOYEEID";
					sql  = sql + "    ,ENDDATETIME";
					sql  = sql + "    ,ENDEMPLOYEEID";
					sql  = sql + "    ,PERCENT_START";
					sql  = sql + "    ,PERCENT";
					sql  = sql + "    ,DELETEFLG";
					sql  = sql + "    ,DELETEYMDHMS";
					sql  = sql + "    ,BIKO";
					sql  = sql + "    ,BOXCOUNT";
					sql  = sql + "    ,SYSREGUSERID";
					sql  = sql + "    ,SYSREGPGMID";
					sql  = sql + "    ,SYSREGYMDHMS";
					sql  = sql + "    ,SYSUPDUSERID";
					sql  = sql + "    ,SYSUPDPGMID";
					sql  = sql + "    ,SYSUPDYMDHMS";
					sql  = sql + " )values(";
					sql  = sql + "     ?";     //HOUSEID
					sql  = sql + "    ,?";     //WORKID
					sql  = sql + "    ,'XX'";  //COLNO
					sql  = sql + "    ,?";     //STARTDATETIME
					sql  = sql + "    ,?";     //STARTEMPLOYEEID
					sql  = sql + "    ,?";     //ENDDATETIME
					sql  = sql + "    ,?";     //ENDEMPLOYEEID
					sql  = sql + "    ,?";     //PERCENT_START
					sql  = sql + "    ,?";     //PERCENT
					sql  = sql + "    ,0";     //DELETEFLG
					sql  = sql + "    ,null";  //DELETEYMDHMS
					sql  = sql + "    ,?";     //BIKO
					sql  = sql + "    ,?";     //BOXCOUNT
					sql  = sql + "    ,?";     //
					sql  = sql + "    ,?";     //
					sql  = sql + "    ,current_timestamp(3)";
					sql  = sql + "    ,?";
					sql  = sql + "    ,?";
					sql  = sql + "    ,current_timestamp(3)";
					sql  = sql + " )";
					
					ret = this.jdbcTemplate.update(sql
							,detail.getHouseId()
							,detail.getWorkId()
							,formatter.format(detail.getStartDateTime())
							,detail.getStartEmployeeId()
							,formatter.format(detail.getEndDateTime())
							,detail.getEndEmployeeId()
							,detail.getPercentStart()
							,detail.getPercent()
							,detail.getBiko()
							,detail.getBoxCount()
							,userName
							,registPgmId
							,userName
							,registPgmId);
				
				
			} else if (
				   detail.getHouseId().equals(SpecialWork.SHODOKU) == true
				&& detail.getHouseId().equals(SpecialWork.OTHER)   == true) {
					
					//------------------------------------------------
					// ハウス作業進捗テーブル の登録 ※ハウスの全列登録
					
					//【メモ】
					// 消毒、その他の作業はハウス内の全列分登録される
					//
				
				
				int maxColCount = getColCount(detail.getHouseId());
				
				
				for (int index = 1 ; index < maxColCount; index++) {
				
					String sql = " insert into TT_HOUSE_WORKSTATUS (";
					sql  = sql + "     HOUSEID";
					sql  = sql + "    ,WORKID";
					sql  = sql + "    ,COLNO";
					sql  = sql + "    ,STARTDATETIME";
					sql  = sql + "    ,STARTEMPLOYEEID";
					sql  = sql + "    ,ENDDATETIME";
					sql  = sql + "    ,ENDEMPLOYEEID";
					sql  = sql + "    ,PERCENT_START";
					sql  = sql + "    ,PERCENT";
					sql  = sql + "    ,DELETEFLG";
					sql  = sql + "    ,DELETEYMDHMS";
					sql  = sql + "    ,BIKO";
					sql  = sql + "    ,SYSREGUSERID";
					sql  = sql + "    ,SYSREGPGMID";
					sql  = sql + "    ,SYSREGYMDHMS";
					sql  = sql + "    ,SYSUPDUSERID";
					sql  = sql + "    ,SYSUPDPGMID";
					sql  = sql + "    ,SYSUPDYMDHMS";
					sql  = sql + " )values(";
					sql  = sql + "     ?";      //HOUSEID
					sql  = sql + "    ,?";      //WORKID
					sql  = sql + "    ,?";      //COLNO
					sql  = sql + "    ,?";      //STARTDATETIME
					sql  = sql + "    ,?";      //STARTEMPLOYEEID
					sql  = sql + "    ,?";      //ENDDATETIME
					sql  = sql + "    ,?";      //ENDEMPLOYEEID
					sql  = sql + "    ,?";      //PERCENT_START
					sql  = sql + "    ,?";      //PERCENT
					sql  = sql + "    ,0";      //DELETEFLG
					sql  = sql + "    ,null";   //DELETEYMDHMS
					sql  = sql + "    ,?";      //BIKO
					sql  = sql + "    ,?";
					sql  = sql + "    ,?";
					sql  = sql + "    ,current_timestamp(3)";
					sql  = sql + "    ,?";
					sql  = sql + "    ,?";
					sql  = sql + "    ,current_timestamp(3)";
					sql  = sql + " )";
					
					ret = this.jdbcTemplate.update(sql
							,detail.getHouseId()
							,detail.getWorkId()
							,Integer.toString(index)
							,formatter.format(detail.getStartDateTime())
							,detail.getStartEmployeeId()
							,formatter.format(detail.getEndDateTime())
							,detail.getEndEmployeeId()
							,detail.getPercentStart()
							,detail.getPercent()
							,detail.getBiko()
							,userName
							,registPgmId
							,userName
							,registPgmId);
				
				}
				
			}else{
					
					//------------------------------------------------
					// ハウス作業進捗テーブル の登録
					
					String sql = " insert into TT_HOUSE_WORKSTATUS (";
					sql  = sql + "     HOUSEID";
					sql  = sql + "    ,WORKID";
					sql  = sql + "    ,COLNO";
					sql  = sql + "    ,STARTDATETIME";
					sql  = sql + "    ,STARTEMPLOYEEID";
					sql  = sql + "    ,ENDDATETIME";
					sql  = sql + "    ,ENDEMPLOYEEID";
					sql  = sql + "    ,PERCENT_START";
					sql  = sql + "    ,PERCENT";
					sql  = sql + "    ,DELETEFLG";
					sql  = sql + "    ,DELETEYMDHMS";
					sql  = sql + "    ,BIKO";
					sql  = sql + "    ,SYSREGUSERID";
					sql  = sql + "    ,SYSREGPGMID";
					sql  = sql + "    ,SYSREGYMDHMS";
					sql  = sql + "    ,SYSUPDUSERID";
					sql  = sql + "    ,SYSUPDPGMID";
					sql  = sql + "    ,SYSUPDYMDHMS";
					sql  = sql + " )values(";
					sql  = sql + "     ?";       //HOUSEID
					sql  = sql + "    ,?";       //WORKID
					sql  = sql + "    ,?";       //COLNO
					sql  = sql + "    ,?";       //STARTDATETIME
					sql  = sql + "    ,?";       //STARTEMPLOYEEID
					sql  = sql + "    ,?";       //ENDDATETIME
					sql  = sql + "    ,?";       //ENDEMPLOYEEID
					sql  = sql + "    ,?";       //PERCENT_START
					sql  = sql + "    ,?";       //PERCENT
					sql  = sql + "    ,0";       //DELETEFLG
					sql  = sql + "    ,null";    //DELETEYMDHMS
					sql  = sql + "    ,?";       //BIKO
					sql  = sql + "    ,?";
					sql  = sql + "    ,?";
					sql  = sql + "    ,current_timestamp(3)";
					sql  = sql + "    ,?";
					sql  = sql + "    ,?";
					sql  = sql + "    ,current_timestamp(3)";
					sql  = sql + " )";
					
					ret = this.jdbcTemplate.update(sql
							,detail.getHouseId()
							,detail.getWorkId()
							,detail.getColNo()
							,formatter.format(detail.getStartDateTime())
							,detail.getStartEmployeeId()
							,formatter.format(detail.getEndDateTime())
							,detail.getEndEmployeeId()
							,detail.getPercentStart()
							,detail.getPercent()
							,detail.getBiko()
							,userName
							,registPgmId
							,userName
							,registPgmId);
					
			}
			
			// メモ：commitはjdbcTemplateが自動で行ってくれる
			
			log.info("【INF】" + pgmId + ":処理終了 ret=[" + ret + "]");
			
			return true;
			
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return false;
		}
	}
	
	
	
	//ハウスの列数の取得
	private int getColCount(String houseId) {
		
		String pgmId = classId + ".getColCount";
		log.info("【INF】" + pgmId + ":処理開始 ハウスID=[" + houseId + "]");
		
		try {
			
			String sql = " select count(1)";
			sql  = sql + " from";
			sql  = sql + "     TM_HOUSECOL";
			sql  = sql + " where";
			sql  = sql + "     HOUSEID       = ?";
			
			// queryForListメソッドでSQLを実行
			int count = this.jdbcTemplate.queryForObject(sql
											,Integer.class
											,houseId
											);
			
			log.info("【INF】" + pgmId + ":処理終了 件数=[" + Integer.toString(count) + "]");
			
			return count;
			
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return 0;
		}
		
	}
	
	
	
	// データ更新
	public boolean updateWorkStatus(FormKanriMainteWorkStatusDetail detail ,String userName,String registPgmId) {
		
		String pgmId = classId + ".updateWorkStatus";
		log.info("【INF】" + pgmId + ":処理開始");
		
		try {
			
			int ret = 0;
			
			if (detail.getHouseId().equals(SpecialWork.SHUKAKU) == true) {
				
				//------------------------------------------------
				// ハウス作業(収穫)進捗テーブルの更新
				
				
				
				String sql = " update TT_HOUSE_WORKSTATUS_SHUKAKU";
				sql  = sql + " set";
				sql  = sql + "     HOUSEID          = ?";
				sql  = sql + "    ,COLNO            = 'XX'";//作業(収穫)テーブルの列Noは個例でXX
				sql  = sql + "    ,WORKID           = ?";
				sql  = sql + "    ,STARTDATETIME    = ?";
				sql  = sql + "    ,STARTEMPLOYEEID  = ?";
				sql  = sql + "    ,ENDDATETIME      = ?";
				sql  = sql + "    ,ENDEMPLOYEEID    = ?";
				sql  = sql + "    ,PERCENT_START    = ?";
				sql  = sql + "    ,PERCENT          = ?";
				//sql  = sql + "    ,DELETEFLG        = ?";
				//sql  = sql + "    ,DELETEYMDHMS     = ?";
				sql  = sql + "    ,BIKO             = ?";
				sql  = sql + "    ,BOXCOUNT         = ?";
				sql  = sql + "    ,SYSUPDUSERID     = ?";
				sql  = sql + "    ,SYSUPDPGMID      = ?";
				sql  = sql + "    ,SYSUPDYMDHMS     = current_timestamp(3)";
				sql  = sql + " where";
				sql  = sql + "     HOUSEID          = ?";
				sql  = sql + " and COLNO            = ?";
				sql  = sql + " and WORKID           = ?";
				sql  = sql + " and STARTDATETIME    = ?";
				
				
				// 年月日時分秒までの日時フォーマットを準備
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				
				// 作業終了日時が未入力である場合を考慮して事前にフォーマットを整えておく
				String endDateTimeString = null;
				if (detail.getEndDateTime() != null) {
					endDateTimeString = formatter.format(detail.getEndDateTime());
				}
				
				ret = this.jdbcTemplate.update(sql
						,detail.getHouseId()
						,detail.getWorkId()
						,formatter.format(detail.getStartDateTime())
						,detail.getStartEmployeeId()
						,endDateTimeString
						,detail.getEndEmployeeId()
						,detail.getPercentStart()
						,detail.getPercent()
						,detail.getBiko()
						,detail.getBoxCount()
						,userName
						,registPgmId
						,detail.getBeforeHouseId()
						,detail.getBeforeColNo()
						,detail.getBeforeWorkId()
						,formatter.format(detail.getBeforeStartDateTime())
						);
				
				
			}else{
				
				
				//------------------------------------------------
				// ハウス作業進捗テーブルの更新
				
				
				
				String sql = " update TT_HOUSE_WORKSTATUS";
				sql  = sql + " set";
				sql  = sql + "     HOUSEID          = ?";
				sql  = sql + "    ,COLNO            = ?";
				sql  = sql + "    ,WORKID           = ?";
				sql  = sql + "    ,STARTDATETIME    = ?";
				sql  = sql + "    ,STARTEMPLOYEEID  = ?";
				sql  = sql + "    ,ENDDATETIME      = ?";
				sql  = sql + "    ,ENDEMPLOYEEID    = ?";
				sql  = sql + "    ,PERCENT_START    = ?";
				sql  = sql + "    ,PERCENT          = ?";
				//sql  = sql + "    ,DELETEFLG        = ?";
				//sql  = sql + "    ,DELETEYMDHMS     = ?";
				sql  = sql + "    ,BIKO             = ?";
				sql  = sql + "    ,SYSUPDUSERID     = ?";
				sql  = sql + "    ,SYSUPDPGMID      = ?";
				sql  = sql + "    ,SYSUPDYMDHMS     = current_timestamp(3)";
				sql  = sql + " where";
				sql  = sql + "     HOUSEID          = ?";
				sql  = sql + " and COLNO            = ?";
				sql  = sql + " and WORKID           = ?";
				sql  = sql + " and STARTDATETIME    = ?";
				
				
				// 年月日時分秒までの日時フォーマットを準備
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				
				// 作業終了日時が未入力である場合を考慮して事前にフォーマットを整えておく
				String endDateTimeString = null;
				if (detail.getEndDateTime() != null) {
					endDateTimeString = formatter.format(detail.getEndDateTime());
				}
				
				ret = this.jdbcTemplate.update(sql
						,detail.getHouseId()
						,detail.getColNo()
						,detail.getWorkId()
						,formatter.format(detail.getStartDateTime())
						,detail.getStartEmployeeId()
						,endDateTimeString
						,detail.getEndEmployeeId()
						,detail.getPercentStart()
						,detail.getPercent()
						,detail.getBiko()
						,userName
						,registPgmId
						,detail.getBeforeHouseId()
						,detail.getBeforeColNo()
						,detail.getBeforeWorkId()
						,formatter.format(detail.getBeforeStartDateTime())
						);
				
			}
			
			
			
			// メモ：commitはjdbcTemplateが自動で行ってくれる
			
			log.info("【INF】" + pgmId + ":処理終了 ret=[" + ret + "]");
			
			// 更新件数０件である場合はNGを返却
			if (ret == 0) {
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
	
	
	
	
	// データ削除(物理削除はしない)
	public boolean deleteWorkStatus(FormKanriMainteWorkStatusDetail detail ,String userName,String registPgmId) {
		
		// 年月日時分秒までの日時フォーマットを準備
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		String pgmId = classId + ".deleteWorkStatus";
		log.info("【INF】" + pgmId + ":処理開始 ハウスID=[" + detail.getBeforeHouseId() + "]列No=[" + detail.getBeforeColNo() + "]作業ID=[" + detail.getBeforeWorkId() + "]、開始日時=[" + formatter.format(detail.getBeforeStartDateTime()) + "]");
		
		try {
			
			int ret = 0;
			
			if (detail.getBeforeWorkId().equals(SpecialWork.SHUKAKU) == true) {
				
				
				//------------------------------------------------
				// ハウス作業(収穫)進捗テーブルの削除
				
				
				
				String sql = " update TT_HOUSE_WORKSTATUS_SHUKAKU";
				sql  = sql + " set";
				sql  = sql + "     DELETEFLG        = 1";
				sql  = sql + "    ,DELETEYMDHMS     = current_timestamp(3)";
				sql  = sql + "    ,BIKO             = ?";
				sql  = sql + "    ,SYSUPDUSERID     = ?";
				sql  = sql + "    ,SYSUPDPGMID      = ?";
				sql  = sql + "    ,SYSUPDYMDHMS     = current_timestamp(3)";
				sql  = sql + " where";
				sql  = sql + "     HOUSEID          = ?";
				sql  = sql + " and COLNO            = ?";
				sql  = sql + " and WORKID           = ?";
				sql  = sql + " and STARTDATETIME    = ?";
				
				ret = this.jdbcTemplate.update(sql
						,detail.getBiko()
						,userName
						,registPgmId
						,detail.getBeforeHouseId()
						,detail.getBeforeColNo()
						,detail.getBeforeWorkId()
						,formatter.format(detail.getBeforeStartDateTime())
						);
				
				
			}else{
				
				//------------------------------------------------
				// ハウス作業進捗テーブルの削除
				
				
				String sql = " update TT_HOUSE_WORKSTATUS";
				sql  = sql + " set";
				sql  = sql + "     DELETEFLG        = 1";
				sql  = sql + "    ,DELETEYMDHMS     = current_timestamp(3)";
				sql  = sql + "    ,BIKO             = ?";
				sql  = sql + "    ,SYSUPDUSERID     = ?";
				sql  = sql + "    ,SYSUPDPGMID      = ?";
				sql  = sql + "    ,SYSUPDYMDHMS     = current_timestamp(3)";
				sql  = sql + " where";
				sql  = sql + "     HOUSEID          = ?";
				sql  = sql + " and COLNO            = ?";
				sql  = sql + " and WORKID           = ?";
				sql  = sql + " and STARTDATETIME    = ?";
				
				ret = this.jdbcTemplate.update(sql
						,detail.getBiko()
						,userName
						,registPgmId
						,detail.getBeforeHouseId()
						,detail.getBeforeColNo()
						,detail.getBeforeWorkId()
						,formatter.format(detail.getBeforeStartDateTime())
						);
				
			}
			
			
			// メモ：commitはjdbcTemplateが自動で行ってくれる
			
			log.info("【INF】" + pgmId + ":処理終了 ret=[" + ret + "]");
			
			// 更新件数０件である場合はNGを返却
			if (ret == 0) {
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
	
	
	
	
	// データ復旧
	public boolean revivalWorkStatus(FormKanriMainteWorkStatusDetail detail ,String userName,String registPgmId) {
		
		// 年月日時分秒までの日時フォーマットを準備
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		String pgmId = classId + ".revivalWorkStatus";
		log.info("【INF】" + pgmId + ":処理開始 ハウスID=[" + detail.getBeforeHouseId() + "]列No=[" + detail.getBeforeColNo() + "]作業ID=[" + detail.getBeforeWorkId() + "]、開始日時=[" + formatter.format(detail.getBeforeStartDateTime()) + "]");
		
		try {
			
			int ret = 0;
			
			if (detail.getBeforeWorkId().equals(SpecialWork.SHUKAKU) == true) {
				
				
				//------------------------------------------------
				// ハウス作業(収穫)進捗テーブルの更新（復旧）
				
				
				
				String sql = " update TT_HOUSE_WORKSTATUS_SHUKAKU";
				sql  = sql + " set";
				sql  = sql + "     DELETEFLG        = 0";    // 削除フラグをOFF
				sql  = sql + "    ,DELETEYMDHMS     = null"; // 削除日時をクリア
				sql  = sql + "    ,BIKO             = ?";
				sql  = sql + "    ,SYSUPDUSERID     = ?";
				sql  = sql + "    ,SYSUPDPGMID      = ?";
				sql  = sql + "    ,SYSUPDYMDHMS     = current_timestamp(3)";
				sql  = sql + " where";
				sql  = sql + "     HOUSEID          = ?";
				sql  = sql + " and COLNO            = ?";
				sql  = sql + " and WORKID           = ?";
				sql  = sql + " and STARTDATETIME    = ?";
				
				ret = this.jdbcTemplate.update(sql
						,detail.getBiko()
						,userName
						,registPgmId
						,detail.getBeforeHouseId()
						,detail.getBeforeColNo()
						,detail.getBeforeWorkId()
						,formatter.format(detail.getBeforeStartDateTime())
						);
				
				
			}else{
				
				//------------------------------------------------
				// ハウス作業進捗テーブルの更新（復旧）
				
				
				String sql = " update TT_HOUSE_WORKSTATUS";
				sql  = sql + " set";
				sql  = sql + "     DELETEFLG        = 0";    // 削除フラグをOFF
				sql  = sql + "    ,DELETEYMDHMS     = null"; // 削除日時をクリア
				sql  = sql + "    ,BIKO             = ?";
				sql  = sql + "    ,SYSUPDUSERID     = ?";
				sql  = sql + "    ,SYSUPDPGMID      = ?";
				sql  = sql + "    ,SYSUPDYMDHMS     = current_timestamp(3)";
				sql  = sql + " where";
				sql  = sql + "     HOUSEID          = ?";
				sql  = sql + " and COLNO            = ?";
				sql  = sql + " and WORKID           = ?";
				sql  = sql + " and STARTDATETIME    = ?";
				
				ret = this.jdbcTemplate.update(sql
						,detail.getBiko()
						,userName
						,registPgmId
						,detail.getBeforeHouseId()
						,detail.getBeforeColNo()
						,detail.getBeforeWorkId()
						,formatter.format(detail.getBeforeStartDateTime())
						);
				
			}
			
			
			// メモ：commitはjdbcTemplateが自動で行ってくれる
			
			log.info("【INF】" + pgmId + ":処理終了 ret=[" + ret + "]");
			
			// 更新件数０件である場合はNGを返却
			if (ret == 0) {
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
}
