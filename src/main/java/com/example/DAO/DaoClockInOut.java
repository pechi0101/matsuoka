package com.example.DAO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.example.form.FormDispQRInfoClockInOut;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DaoClockInOut {
	
	private JdbcTemplate  jdbcTemplate;
	private String classId = "DaoClockInOut";
	
	// コンストラクタ
	public DaoClockInOut(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	
	
	
	// 指定の日付で「出勤」状態の情報があるか否かのチェック
	public boolean exsistsClockInData(String empliyeeId,String clockInYear,String clockInMonth,String clockInDay) {
		
		String pgmId = classId + ".exsistsClockInData";
		log.info("【INF】" + pgmId + ":処理開始 社員ID=[" + empliyeeId + "]、出勤年月日=[" + clockInYear + clockInMonth + clockInDay + "]");
		
		try {
			
			String sql = " select count(1)";
			sql  = sql + " from";
			sql  = sql + "     TT_CLOCKINOUT";
			sql  = sql + " where";
			sql  = sql + "     EMPLOYEEID       = ?";
			sql  = sql + " and CLOCKINYEAR      = ?";
			sql  = sql + " and CLOCKINMONTH     = ?";
			sql  = sql + " and CLOCKINDAY       = ?";
			sql  = sql + " and CLOCKOUTDATETIME is null"; // 退勤日時＝null→出勤状態の情報を検索してる
			
			// queryForListメソッドでSQLを実行
			int count = this.jdbcTemplate.queryForObject(sql
											,Integer.class
											,empliyeeId
											,clockInYear
											,clockInMonth
											,clockInDay
											);
			
			log.info("【INF】" + pgmId + ":処理終了 件数=[" + Integer.toString(count) + "]");
			
			// 件数が１件以上である場合Trueを返却
			return count >= 1;
			
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			//検索した結果を返却します
			return false;
		}
	}
	
	
	
	
	
	// 指定の出退勤日時が既存の出退勤情報と重複しているかをチェック
	public boolean isDuplicationClockInDataTime(String empliyeeId,String clockInYear,String clockInMonth,String clockInDay,LocalDateTime clockInDatetime,LocalDateTime clockOutDatetime) {
		
		// 年月日時分秒までの日時フォーマットを準備
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		String pgmId = classId + ".isDuplicationClockInDataTime";
		log.info("【INF】" + pgmId + ":処理開始 社員ID=[" + empliyeeId + "]、出勤年月日=[" + clockInYear + clockInMonth + clockInDay + "]、出勤日時=[" + clockInDatetime + "]、退勤日時=[" + clockOutDatetime + "]");
		
		try {
			
			int count = 0;
			
			if (clockOutDatetime == null) {
				
				
				String sql = " select count(1)";
				sql  = sql + " from";
				sql  = sql + "     TT_CLOCKINOUT";
				sql  = sql + " where";
				sql  = sql + "     EMPLOYEEID        = ?";
				sql  = sql + " and CLOCKINYEAR       = ?";
				sql  = sql + " and CLOCKINMONTH      = ?";
				sql  = sql + " and CLOCKINDAY        = ?";
				sql  = sql + " and CLOCKINDATETIME  <> ?";
				sql  = sql + " and (";
				sql  = sql + "     CLOCKINDATETIME  <= ? and ? <= CLOCKOUTDATETIME";
			  //sql  = sql + " or  CLOCKINDATETIME  <= ? and ? <= CLOCKOUTDATETIME";
				sql  = sql + "     )";
				
				// queryForListメソッドでSQLを実行
				count = this.jdbcTemplate.queryForObject(sql
												,Integer.class
												,empliyeeId
												,clockInYear
												,clockInMonth
												,clockInDay
												,formatter.format(clockInDatetime)
												,formatter.format(clockInDatetime)
												,formatter.format(clockInDatetime)
				//								,formatter.format(clockOutDatetime)
				//								,formatter.format(clockOutDatetime)
												);
			} else {
				
				
				String sql = " select count(1)";
				sql  = sql + " from";
				sql  = sql + "     TT_CLOCKINOUT";
				sql  = sql + " where";
				sql  = sql + "     EMPLOYEEID        = ?";
				sql  = sql + " and CLOCKINYEAR       = ?";
				sql  = sql + " and CLOCKINMONTH      = ?";
				sql  = sql + " and CLOCKINDAY        = ?";
				sql  = sql + " and CLOCKINDATETIME  <> ?";
				sql  = sql + " and (";
				sql  = sql + "     CLOCKINDATETIME  <= ? and ? <= CLOCKOUTDATETIME";
				sql  = sql + " or  CLOCKINDATETIME  <= ? and ? <= CLOCKOUTDATETIME";
				sql  = sql + "     )";
				
				// queryForListメソッドでSQLを実行
				count = this.jdbcTemplate.queryForObject(sql
												,Integer.class
												,empliyeeId
												,clockInYear
												,clockInMonth
												,clockInDay
												,formatter.format(clockInDatetime)
												,formatter.format(clockInDatetime)
												,formatter.format(clockInDatetime)
												,formatter.format(clockOutDatetime)
												,formatter.format(clockOutDatetime)
												);
				
				
				
				
			}
			
			log.info("【INF】" + pgmId + ":処理終了 件数=[" + Integer.toString(count) + "]");
			
			// 件数が１件以上である場合Trueを返却
			return count >= 1;
			
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			//検索した結果を返却します
			return false;
		}
	}
	
	
	
	
	// 指定の日付で「出勤」状態の情報を検索
	public FormDispQRInfoClockInOut getClockInData(String empliyeeId,String clockInYear,String clockInMonth,String clockInDay) {
		
		String pgmId = classId + ".getClockInData";
		log.info("【INF】" + pgmId + ":処理開始 社員ID=[" + empliyeeId + "]、出勤年月日=[" + clockInYear + clockInMonth + clockInDay + "]");
		
		FormDispQRInfoClockInOut formDispQRInfoClockInOut = new FormDispQRInfoClockInOut();
		
		formDispQRInfoClockInOut.setLoginEmployeeId(empliyeeId);
		formDispQRInfoClockInOut.setClockInYear(clockInYear);
		formDispQRInfoClockInOut.setClockInMonth(clockInMonth);
		formDispQRInfoClockInOut.setClockInDay(clockInDay);
		
		try {
			
			String sql = " select";
			sql  = sql + "     DATE_FORMAT(CLOCKINDATETIME ,'%Y%m%d%H%i%S') CLOCKINDATETIME_STRING";  // YYYYMMDDHHMMSS形式で取得（時間は24時間制）
			sql  = sql + "    ,DATE_FORMAT(CLOCKOUTDATETIME,'%Y%m%d%H%i%S') CLOCKOUTDATETIME_STRING"; // YYYYMMDDHHMMSS形式で取得（時間は24時間制）
			sql  = sql + "    ,HOURLYWAGE";
			sql  = sql + "    ,WORKING_HOUERS";
			sql  = sql + " from";
			sql  = sql + "     TT_CLOCKINOUT";
			sql  = sql + " where";
			sql  = sql + "     EMPLOYEEID       = ?";
			sql  = sql + " and CLOCKINYEAR      = ?";
			sql  = sql + " and CLOCKINMONTH     = ?";
			sql  = sql + " and CLOCKINDAY       = ?";
			sql  = sql + " and CLOCKOUTDATETIME is null"; // 退勤日時＝null→出勤状態の情報を検索してる
			
			
			
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
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(
														 sql
														,empliyeeId
														,clockInYear
														,clockInMonth
														,clockInDay
														);
			
			
			for (Map<String, Object> rs: rsList) {
				
				DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
				DateTimeFormatter formatterDate     = DateTimeFormatter.ofPattern("yyyyMMdd");
				DateTimeFormatter formatterTime     = DateTimeFormatter.ofPattern("HHmmss");
				
				String wkDate; //YYYYMMDDの文字列
				String wkTime; //HHMMSSの文字列
				
				//出勤日
				String clockInDateString = rs.get("CLOCKINDATETIME_STRING").toString();
				wkDate = clockInDateString.substring(0, 8);
				wkTime = clockInDateString.substring(8, 14);
				
				formDispQRInfoClockInOut.setClockInDate(LocalDate.parse(wkDate,formatterDate));
				formDispQRInfoClockInOut.setClockInTime(LocalTime.parse(wkTime,formatterTime));
				formDispQRInfoClockInOut.setClockInTimeString(formDispQRInfoClockInOut.getClockInTime().format(DateTimeFormatter.ofPattern("HH:mm")));
				formDispQRInfoClockInOut.setClockInDatetime(LocalDateTime.parse(clockInDateString, formatterDateTime));
				
				//退勤日
				if (rs.get("CLOCKOUTDATETIME_STRING") != null) {
					String clockOutDateString = rs.get("CLOCKOUTDATETIME_STRING").toString();
					wkDate = clockOutDateString.substring(0, 8);
					wkTime = clockOutDateString.substring(8, 14);
					
					formDispQRInfoClockInOut.setClockOutDate(LocalDate.parse(wkDate,formatterDate));
					formDispQRInfoClockInOut.setClockOutTime(LocalTime.parse(wkTime,formatterTime));
					formDispQRInfoClockInOut.setClockOutTimeString(formDispQRInfoClockInOut.getClockOutTime().format(DateTimeFormatter.ofPattern("HH:mm")));
					formDispQRInfoClockInOut.setClockOutDatetime(LocalDateTime.parse(clockOutDateString, formatterDateTime));
				}
				
				// 時給
				if (rs.get("HOURLYWAGE") != null) {
					formDispQRInfoClockInOut.setHourLywage(Integer.parseInt(rs.get("HOURLYWAGE").toString()));
				}
				
				// 勤務時間
				if (rs.get("WORKING_HOUERS") != null) {
					formDispQRInfoClockInOut.setWorkingHours((Double.parseDouble(rs.get("WORKING_HOUERS").toString())));
				}
				
				// 値は１件のみ取得されるためここでLOOP終了
				break;
			}
			
			log.info("【INF】" + pgmId + ":処理終了 取得データの出勤日=[" + formDispQRInfoClockInOut.getClockInDay().toString() + "]");
			
			
			return formDispQRInfoClockInOut;
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return null;
		}
	}
	
	
	// データ登録
	public boolean regist(FormDispQRInfoClockInOut formDispQRInfoClockInOut ,String userName,String registPgmId) {
		
		// 年月日時分秒までの日時フォーマットを準備
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		String pgmId = classId + ".regist";
		log.info("【INF】" + pgmId + ":処理開始 □出勤日時=[" + formDispQRInfoClockInOut.getClockInDatetime() + "]、退勤日時=[" + formDispQRInfoClockInOut.getClockOutDatetime() + "]、勤務時間=[" + formDispQRInfoClockInOut.getWorkingHours() + "]時間");
		
		try {
			
			// 出退勤日時をSQL登録・更新用に文字列編集
			String clockInDateTimeString = null;
			if (formDispQRInfoClockInOut.getClockInDatetime() != null) {
				clockInDateTimeString = formatter.format(formDispQRInfoClockInOut.getClockInDatetime());
			}
			String clockOutDateTimeString = null;
			if (formDispQRInfoClockInOut.getClockOutDatetime() != null) {
				clockOutDateTimeString = formatter.format(formDispQRInfoClockInOut.getClockOutDatetime());
			}
			
			String sql = " insert into TT_CLOCKINOUT (";
			sql  = sql + "     EMPLOYEEID";
			sql  = sql + "    ,CLOCKINYEAR";
			sql  = sql + "    ,CLOCKINMONTH";
			sql  = sql + "    ,CLOCKINDAY";
			sql  = sql + "    ,CLOCKINDATETIME";
			sql  = sql + "    ,CLOCKOUTDATETIME";
			sql  = sql + "    ,HOURLYWAGE";
			sql  = sql + "    ,WORKING_HOUERS";
			sql  = sql + "    ,SYSREGUSERID";
			sql  = sql + "    ,SYSREGPGMID";
			sql  = sql + "    ,SYSREGYMDHMS";
			sql  = sql + "    ,SYSUPDUSERID";
			sql  = sql + "    ,SYSUPDPGMID";
			sql  = sql + "    ,SYSUPDYMDHMS";
			sql  = sql + " )values(";
			sql  = sql + "     ?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";//CLOCKINDATETIME
			sql  = sql + "    ,?";//CLOCKOUTDATETIME
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";//WORKING_HOUERS
			
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,current_timestamp(3)";
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,current_timestamp(3)";
			sql  = sql + " )";
			
			
			
			int ret = this.jdbcTemplate.update(sql
					,formDispQRInfoClockInOut.getLoginEmployeeId()
					,formDispQRInfoClockInOut.getClockInYear()
					,formDispQRInfoClockInOut.getClockInMonth()
					,formDispQRInfoClockInOut.getClockInDay()
					,clockInDateTimeString
					,clockOutDateTimeString
					,formDispQRInfoClockInOut.getHourLywage()
					,formDispQRInfoClockInOut.getWorkingHours()
					,userName
					,registPgmId
					,userName
					,registPgmId);
			
			
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
	
	
	// データ更新
	public boolean update(FormDispQRInfoClockInOut formDispQRInfoClockInOut ,String userName,String registPgmId) {
		
		// 年月日時分秒までの日時フォーマットを準備
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		String pgmId = classId + ".regist";
		log.info("【INF】" + pgmId + ":処理開始 □出勤日時=[" + formDispQRInfoClockInOut.getClockInDatetime() + "]、退勤日時=[" + formDispQRInfoClockInOut.getClockOutDatetime() + "]、勤務時間=[" + formDispQRInfoClockInOut.getWorkingHours() + "]時間");
		
		try {
			
			// 出退勤日時をSQL登録・更新用に文字列編集
			String clockInDateTimeString = null;
			if (formDispQRInfoClockInOut.getClockInDatetime() != null) {
				clockInDateTimeString = formatter.format(formDispQRInfoClockInOut.getClockInDatetime());
			}
			String clockOutDateTimeString = null;
			if (formDispQRInfoClockInOut.getClockOutDatetime() != null) {
				clockOutDateTimeString = formatter.format(formDispQRInfoClockInOut.getClockOutDatetime());
			}
			
			
			String sql = " update TT_CLOCKINOUT ";
			sql  = sql + " set";
			sql  = sql + "     CLOCKINYEAR      = ?";
			sql  = sql + "    ,CLOCKINMONTH     = ?";
			sql  = sql + "    ,CLOCKINDAY       = ?";
			sql  = sql + "    ,CLOCKOUTDATETIME = ?";
			sql  = sql + "    ,HOURLYWAGE       = ?";
			sql  = sql + "    ,WORKING_HOUERS   = ?";
			sql  = sql + "    ,SYSUPDUSERID     = ?";
			sql  = sql + "    ,SYSUPDPGMID      = ?";
			sql  = sql + "    ,SYSUPDYMDHMS     = current_timestamp(3)";
			sql  = sql + " where";
			sql  = sql + "     EMPLOYEEID       = ?";
			sql  = sql + " and CLOCKINDATETIME  = ?";
			
			int ret = this.jdbcTemplate.update(sql
					,formDispQRInfoClockInOut.getClockInYear()
					,formDispQRInfoClockInOut.getClockInMonth()
					,formDispQRInfoClockInOut.getClockInDay()
					,clockOutDateTimeString
					,formDispQRInfoClockInOut.getHourLywage()
					,formDispQRInfoClockInOut.getWorkingHours()
					,userName
					,registPgmId
					,formDispQRInfoClockInOut.getLoginEmployeeId()
					,clockInDateTimeString
					);
			
			
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
	
	
	

}
