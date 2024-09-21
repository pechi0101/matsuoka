package com.example.DAO;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.example.form.FormKanriMainteClockInOutDetail;
import com.example.form.FormKanriMainteClockInOutList;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DaoFormKanriMainteClockInOut {
	
	private JdbcTemplate  jdbcTemplate;
	private String classId = "DaoFormKanriMainteClockInOut";
	
	// コンストラクタ
	public DaoFormKanriMainteClockInOut(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	// リストを取得
	public FormKanriMainteClockInOutList getDispClockInOutList(String employeeId ,LocalDateTime startDateTimeFr,LocalDateTime startDateTimeTo) {
		
		// 年月日時分秒までの日時フォーマットを準備
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		String pgmId = classId + ".getDispClockInOutList";
		log.info("【INF】" + pgmId + ":処理開始 社員ID=[" + employeeId + "]、開始日時From=[" + startDateTimeFr + "]、開始日時From=[" + startDateTimeTo + "]");
		
		
		// 返却値
		FormKanriMainteClockInOutList retForm = new FormKanriMainteClockInOutList();
		try {
			
			// ------------------------------------------------
			// 引数の開始、終了日時が未指定である場合、３か月前の月初～今月末を検索範囲とする
			
			if (startDateTimeFr == null && startDateTimeTo == null) {
				// 3ヶ月前の月の初日を取得し、時間を00:00:00に設定
				// 【メモ】atStartOfDay() メソッドは、LocalDate を LocalDateTime に変換し、時間を「00:00:00」に設定
				startDateTimeFr = YearMonth.from(LocalDateTime.now().minusMonths(3)).atDay(1).atStartOfDay();
				
				// 月末日の23時59分59秒を設定してLocalDateTime型に変換
				startDateTimeTo = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).atTime(23, 59, 59);;
			}
			
			log.info("【INF】" + pgmId + ":検索開始 社員ID=[" + employeeId + "]、開始日時From=[" + startDateTimeFr + "]、開始日時From=[" + startDateTimeTo + "]");
			
			// ★★★★★★★★★★★★★★★★★★★★★★★★★★★
			//
			// 動的SQLであるためSQL中に直接引数の検索条件を埋め込む
			//
			// ★★★★★★★★★★★★★★★★★★★★★★★★★★★
			
			
			//【メモ】：画面に削除した作業をグレーアウトして表示するために削除済みの作業も検索対象にする
			
			
			String sql = " select";
			sql  = sql + "     CLK.EMPLOYEEID";
			sql  = sql + "    ,EMP.EMPLOYEENAME";
			sql  = sql + "    ,CLK.CLOCKINYEAR";
			sql  = sql + "    ,CLK.CLOCKINMONTH";
			sql  = sql + "    ,CLK.CLOCKINDAY";
			sql  = sql + "    ,DATE_FORMAT(CLK.CLOCKINDATETIME ,'%Y%m%d%H%i%S') STARTDATETIME_STRING";// YYYYMMDDHHMMSS形式で取得（時間は24時間制）
			sql  = sql + "    ,DATE_FORMAT(CLK.CLOCKOUTDATETIME,'%Y%m%d%H%i%S') ENDDATETIME_STRING";  // YYYYMMDDHHMMSS形式で取得（時間は24時間制）
			sql  = sql + "    ,CLK.HOURLYWAGE";
			sql  = sql + "    ,CLK.WORKING_HOUERS";
			sql  = sql + "    ,CLK.BIKO";
			sql  = sql + "    ,CLK.DELETEFLG";
			sql  = sql + "    ,DATE_FORMAT(CLK.DELETEYMDHMS    ,'%Y%m%d%H%i%S') DELETEYMDHMS_STRING";// YYYYMMDDHHMMSS形式で取得（時間は24時間制）
			sql  = sql + " from";
			sql  = sql + "     TT_CLOCKINOUT CLK";
			sql  = sql + " inner join TM_EMPLOYEE EMP";
			sql  = sql + "     on";
			sql  = sql + "         EMP.EMPLOYEEID   = CLK.EMPLOYEEID";
			sql  = sql + " where";
			sql  = sql + "     EMP.DELETEFLG        = false";
			if (startDateTimeFr != null) {
			sql  = sql + " and CLK.CLOCKINDATETIME >= '" + formatter.format(startDateTimeFr) + "'";
			}
			if (startDateTimeTo != null) {
			sql  = sql + " and CLK.CLOCKINDATETIME <= '" + formatter.format(startDateTimeTo) + "'";
			}
			if (employeeId != null && "".equals(employeeId) == false) {
			sql  = sql + " and CLK.EMPLOYEEID       = '" + employeeId + "'";
			}
			sql  = sql + " order by";
			sql  = sql + "     CLK.EMPLOYEEID";
			sql  = sql + "    ,CLK.CLOCKINDATETIME";
			
			
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
				
				
				FormKanriMainteClockInOutDetail detail = new FormKanriMainteClockInOutDetail();
				
				
				// 社員ID
				detail.setEmployeeId(rs.get("EMPLOYEEID").toString());
				
				// 社員名
				detail.setEmployeeName(rs.get("EMPLOYEENAME").toString());
				
				// 出勤年月日
				detail.setClockInYear( rs.get("CLOCKINYEAR").toString());
				detail.setClockInMonth(rs.get("CLOCKINMONTH").toString());
				detail.setClockInDay(  rs.get("CLOCKINDAY").toString());
				
				
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
				
				// 作業終了日時
				if (rs.get("ENDDATETIME_STRING") != null) {
					endDateTimeString   = rs.get("ENDDATETIME_STRING").toString();
					wkDate = endDateTimeString.substring(0, 8);
					wkTime = endDateTimeString.substring(8, 14);
					detail.setEndDate(LocalDate.parse(wkDate,formatterDate));
					detail.setEndTime(LocalTime.parse(wkTime,formatterTime));
					detail.setEndDateTime(LocalDateTime.parse(endDateTimeString,formatterDateTime));
				}
				
				// 時給
				if (rs.get("HOURLYWAGE") != null) {
					detail.setHourlywage(Integer.parseInt(rs.get("HOURLYWAGE").toString()));
				}
				// 勤務時間
				if (rs.get("WORKING_HOUERS") != null) {
					detail.setWorkingHoures(Double.parseDouble(rs.get("WORKING_HOUERS").toString()));
				}
				
				// 備考
				if (rs.get("BIKO") != null) {
					detail.setBiko(rs.get("BIKO").toString());
				}
				
				log.info("【DBG】" + pgmId + ":★検索結果 社員=[" + rs.get("EMPLOYEENAME").toString() + "]、削除フラグ=[" + Boolean.parseBoolean(rs.get("DELETEFLG").toString()) + "]");
				
				
				// 削除フラグ
				detail.setDeleteFlg(Boolean.parseBoolean(rs.get("DELETEFLG").toString()));
				
				
				// 削除日時
				if (rs.get("DELETEYMDHMS_STRING") != null) {
					deleteDateTimeString = rs.get("DELETEYMDHMS_STRING").toString();
					detail.setDeleteymdhms(LocalDateTime.parse(deleteDateTimeString,formatterDateTime));
				}
				
				retForm.addClockInOut(detail);
				
			}
			
			
			log.info("【INF】" + pgmId + ":処理終了 取得件数=[" + Integer.toString(retForm.getClockInOutList().size()) + "]件");
			return retForm;
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了[" + e.toString() + "]");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return null;
		}
	}
	
	
	
	// 詳細を取得(社員ID、作業開始日時指定)
	public FormKanriMainteClockInOutDetail getDispClockInOutDatail(String employeeId ,LocalDateTime startDateTime) {
		
		// 年月日時分秒までの日時フォーマットを準備
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		
		String pgmId = classId + ".getDispClockInOutDatail";
		log.info("【INF】" + pgmId + ":処理開始 社員ID=[" + employeeId + "]、開始日時=[" + startDateTime + "]");
		
		
		// 返却値
		FormKanriMainteClockInOutDetail retForm = new FormKanriMainteClockInOutDetail();
		
		try {
			
			String sql = " select";
			sql  = sql + "     CLK.EMPLOYEEID";
			sql  = sql + "    ,EMP.EMPLOYEENAME";
			sql  = sql + "    ,CLK.CLOCKINYEAR";
			sql  = sql + "    ,CLK.CLOCKINMONTH";
			sql  = sql + "    ,CLK.CLOCKINDAY";
			sql  = sql + "    ,DATE_FORMAT(CLK.CLOCKINDATETIME ,'%Y%m%d%H%i%S') STARTDATETIME_STRING";// YYYYMMDDHHMMSS形式で取得（時間は24時間制）
			sql  = sql + "    ,DATE_FORMAT(CLK.CLOCKOUTDATETIME,'%Y%m%d%H%i%S') ENDDATETIME_STRING";  // YYYYMMDDHHMMSS形式で取得（時間は24時間制）
			sql  = sql + "    ,CLK.HOURLYWAGE";
			sql  = sql + "    ,CLK.WORKING_HOUERS";
			sql  = sql + "    ,CLK.BIKO";
			sql  = sql + "    ,CLK.DELETEFLG";
			sql  = sql + "    ,DATE_FORMAT(CLK.DELETEYMDHMS    ,'%Y%m%d%H%i%S') DELETEYMDHMS_STRING";// YYYYMMDDHHMMSS形式で取得（時間は24時間制）
			sql  = sql + " from";
			sql  = sql + "     TT_CLOCKINOUT CLK";
			sql  = sql + " inner join TM_EMPLOYEE EMP";
			sql  = sql + "     on";
			sql  = sql + "         EMP.EMPLOYEEID   = CLK.EMPLOYEEID";
			sql  = sql + " where";
			sql  = sql + "     EMP.DELETEFLG        = false";
			sql  = sql + " and CLK.CLOCKINDATETIME  = '" + formatter.format(startDateTime) + "'";
			sql  = sql + " and CLK.EMPLOYEEID       = '" + employeeId + "'";
			sql  = sql + " order by";
			sql  = sql + "     CLK.EMPLOYEEID";
			sql  = sql + "    ,CLK.CLOCKINDATETIME";
			
			
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
				
				// 変更前の検索条件をセット
				retForm.setBeforeEmployeeId(employeeId);
				retForm.setBeforeStartDateTime(startDateTime);
				
				
				// 社員ID
				retForm.setEmployeeId(rs.get("EMPLOYEEID").toString());
				
				// 社員名
				retForm.setEmployeeName(rs.get("EMPLOYEENAME").toString());
				
				// 出勤年月日
				retForm.setClockInYear( rs.get("CLOCKINYEAR").toString());
				retForm.setClockInMonth(rs.get("CLOCKINMONTH").toString());
				retForm.setClockInDay(  rs.get("CLOCKINDAY").toString());
				
				
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
				retForm.setStartDate(LocalDate.parse(wkDate,formatterDate));
				retForm.setStartTime(LocalTime.parse(wkTime,formatterTime));
				retForm.setStartDateTime(LocalDateTime.parse(startDateTimeString,formatterDateTime));
				
				// 作業終了日時
				if (rs.get("ENDDATETIME_STRING") != null) {
					endDateTimeString   = rs.get("ENDDATETIME_STRING").toString();
					wkDate = endDateTimeString.substring(0, 8);
					wkTime = endDateTimeString.substring(8, 14);
					retForm.setEndDate(LocalDate.parse(wkDate,formatterDate));
					retForm.setEndTime(LocalTime.parse(wkTime,formatterTime));
					retForm.setEndDateTime(LocalDateTime.parse(endDateTimeString,formatterDateTime));
				}
				
				// 時給
				if (rs.get("HOURLYWAGE") != null) {
					retForm.setHourlywage(Integer.parseInt(rs.get("HOURLYWAGE").toString()));
				}
				// 勤務時間
				if (rs.get("WORKING_HOUERS") != null) {
					retForm.setWorkingHoures(Double.parseDouble(rs.get("WORKING_HOUERS").toString()));
				}
				
				// 備考
				if (rs.get("BIKO") != null) {
					retForm.setBiko(rs.get("BIKO").toString());
				}
				
				// 削除フラグ
				retForm.setDeleteFlg(Boolean.parseBoolean(rs.get("DELETEFLG").toString()));
				
				
				// 削除日時
				if (rs.get("DELETEYMDHMS_STRING") != null) {
					deleteDateTimeString = rs.get("DELETEYMDHMS_STRING").toString();
					retForm.setDeleteymdhms(LocalDateTime.parse(deleteDateTimeString,formatterDateTime));
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
	public boolean isExists(String employeeId,LocalDateTime startDateTime) {
		
		// 年月日時分秒までの日時フォーマットを準備
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		String pgmId = classId + ".isExists";
		log.info("【INF】" + pgmId + ":処理開始 社員ID=[" + employeeId + "]、開始日時=[" + startDateTime + "]");
		
		int count = 0;
		
		try {
			
			String sql = " select";
			sql  = sql + "     count(*)";
			sql  = sql + " from";
			sql  = sql + "     TT_CLOCKINOUT CLK";
			sql  = sql + " where";
			sql  = sql + "     EMP.DELETEFLG        = false";
			sql  = sql + " and CLK.CLOCKINDATETIME  = '" + formatter.format(startDateTime) + "'";
			sql  = sql + " and CLK.EMPLOYEEID       = '" + employeeId + "'";
			
			count = this.jdbcTemplate.queryForObject(sql ,Integer.class);
			
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
	
	
	
	// 時給マスタより最新の時給を取得
	private int getHourlywage(String employeeId) {
		
		// 年月日時分秒までの日時フォーマットを準備
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		
		String pgmId = classId + ".getHourlywage";
		log.info("【INF】" + pgmId + ":処理開始 社員ID=[" + employeeId + "]");
		
		
		// 返却値
		int hourwage = 0;
		
		try {
			
			String sql = " select";
			sql  = sql + "     HOURLYWAGE";
			sql  = sql + " from";
			sql  = sql + "     TM_HOURLYWAGE WAG";
			sql  = sql + " where";
			sql  = sql + "     EMPLOYEEID  = '" + employeeId + "'";
			sql  = sql + " and VALIDFLG    = false";  //★適用中フラグ : 同社員で１レコードのみ常にtrue
			
			
			// queryForListメソッドでSQLを実行し、結果MapのListで受け取る。
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql);
			
			for (Map<String, Object> rs: rsList) {
				
				// 時給
				if (rs.get("HOURLYWAGE") != null) {
					hourwage  = Integer.parseInt(rs.get("HOURLYWAGE").toString());
				}
				//1件のみ取得するためLOOPしない
				break;
			}
			
			log.info("【INF】" + pgmId + ":処理終了");
			return hourwage;
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return 0;
		}
	}
	
	
	// 時給マスタより日付指定で時給を取得
	private int getHourlywage(String employeeId ,LocalDateTime targetDateTime) {
		
		// 年月日時分秒までの日時フォーマットを準備
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		
		String pgmId = classId + ".getHourlywage";
		log.info("【INF】" + pgmId + ":処理開始 社員ID=[" + employeeId + "]、対象日時=[" + targetDateTime + "]");
		
		
		// 返却値
		int hourwage = 0;
		
		try {
			
			String sql = " select";
			sql  = sql + "     HOURLYWAGE";
			sql  = sql + " from";
			sql  = sql + "     TM_HOURLYWAGE WAG";
			sql  = sql + " where";
			sql  = sql + "     EMPLOYEEID    = '" + employeeId + "'";
			sql  = sql + " and STARTDATETIME <= '" + formatter.format(targetDateTime) + "'";
			sql  = sql + " and ENDDATETIME   >= '" + formatter.format(targetDateTime) + "'";
			
			
			// queryForListメソッドでSQLを実行し、結果MapのListで受け取る。
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql);
			
			for (Map<String, Object> rs: rsList) {
				
				// 時給
				if (rs.get("HOURLYWAGE") != null) {
					hourwage  = Integer.parseInt(rs.get("HOURLYWAGE").toString());
				}
				//1件のみ取得するためLOOPしない
				break;
			}
			
			log.info("【INF】" + pgmId + ":処理終了");
			return hourwage;
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return 0;
		}
	}
	
	
	
	// 時給マスタより最新の時給を取得
	private double getWorkingHoures(LocalDateTime startDateTime, LocalDateTime endDateTime) {
		
		
		String pgmId = classId + ".getWorkingHoures";
		log.info("【INF】" + pgmId + ":処理開始 開始日時=[" + startDateTime + "]、終了日時=[" + endDateTime + "]");
		
		
		// 返却値
		double workingHoures = 0;
		
		try {
			
			// Duration.between(startDateTime, endDateTime)
			// ->startDateTimeからendDateTimeまでのDuration（時間の差）を計算します。
			
			// duration.toMinutes()
			// ->Durationオブジェクトから分単位で差を取得します。
			
			// duration.toMinutes() / 60.0
			// ->分単位の差を時間単位に変換します。
			
			// Math.round(hours * 10.0) / 10.0
			// ->hoursを小数点以下第1位まで四捨五入します。まず10倍して四捨五入し
			//   再び10で割ることで小数点以下第1位までの値を取得します。
			
			// Durationを使って差を計算
			Duration duration = Duration.between(startDateTime, endDateTime);
			
			// 時間として取得し、小数点以下第1位まで四捨五入
			double hours = duration.toMinutes() / 60.0;
			workingHoures = Math.round(hours * 10.0) / 10.0;
			
			
			log.info("【INF】" + pgmId + ":処理終了 勤務時間=[" + Double.toString(workingHoures) + "]時間");
			return workingHoures;
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return 0;
		}
	}
	
	
	
	
	
	
	
	// データ登録
	public boolean registClockInOut(FormKanriMainteClockInOutDetail detail ,String userName,String registPgmId) {
		
		String pgmId = classId + ".registClockInOut";
		log.info("【INF】" + pgmId + ":処理開始 社員ID=[" + detail.getEmployeeId() + "]、開始日時=[" + detail.getStartDateTime() + "]、終了日時=[" + detail.getEndDateTime() + "]");
		
		try {
			
			// 出勤年月日にそれぞれ開始日時の年月日をセットする
			
			DateTimeFormatter formatterYear  = DateTimeFormatter.ofPattern("yyyy");
			detail.setClockInYear( detail.getStartDateTime().format(formatterYear));
		
		
			DateTimeFormatter formatterMonth = DateTimeFormatter.ofPattern("MM");
			detail.setClockInMonth(detail.getStartDateTime().format(formatterMonth));
		
		
			DateTimeFormatter formatterDay   = DateTimeFormatter.ofPattern("dd");
			detail.setClockInDay(  detail.getStartDateTime().format(formatterDay));
			
			
			// 時給を取得(その社員の出勤日時時点の時給)
			int hourlywage = this.getHourlywage(detail.getEmployeeId(),detail.getStartDateTime());
			detail.setHourlywage(hourlywage);
			
			
			// 勤務時間を算出
			detail.setWorkingHoures(0); // 初期化
			if (detail.getEndDateTime() != null) {
				double workingHoures = getWorkingHoures(detail.getStartDateTime(),detail.getEndDateTime());
				detail.setWorkingHoures(workingHoures);
			}
			
			
			// 年月日時分秒までの日時フォーマットを準備
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			
			// 開始・終了日時はSQL用にフォーマットを整えておく
			String startDateTimeString = formatter.format(detail.getStartDateTime());
			
			String endDateTimeString = null;
			if (detail.getEndDateTime() != null) {
				endDateTimeString      = formatter.format(detail.getEndDateTime()); 
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
			sql  = sql + "    ,BIKO";
			sql  = sql + "    ,DELETEFLG";
			sql  = sql + "    ,DELETEYMDHMS";
			sql  = sql + "    ,SYSREGUSERID";
			sql  = sql + "    ,SYSREGPGMID";
			sql  = sql + "    ,SYSREGYMDHMS";
			sql  = sql + "    ,SYSUPDUSERID";
			sql  = sql + "    ,SYSUPDPGMID";
			sql  = sql + "    ,SYSUPDYMDHMS";
			sql  = sql + " ) values (";
			sql  = sql + "     ?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,false";
			sql  = sql + "    ,null";
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,current_timestamp(3)";
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,current_timestamp(3)";
			sql  = sql + " )";
			
			int ret = this.jdbcTemplate.update(sql
					,detail.getEmployeeId()
					,detail.getClockInYear()
					,detail.getClockInMonth()
					,detail.getClockInDay()
					,startDateTimeString
					,endDateTimeString
					,detail.getHourlywage()
					,detail.getWorkingHoures()
					,detail.getBiko()
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
	public boolean updateClockInOut(FormKanriMainteClockInOutDetail detail ,String userName,String registPgmId) {
		
		String pgmId = classId + ".updateClockInOut";
		log.info("【INF】" + pgmId + ":処理開始");
		
		try {
			
			// 出勤年月日にそれぞれ開始日時の年月日をセットする
			
			DateTimeFormatter formatterYear  = DateTimeFormatter.ofPattern("yyyy");
			detail.setClockInYear( detail.getStartDateTime().format(formatterYear));
		
		
			DateTimeFormatter formatterMonth = DateTimeFormatter.ofPattern("MM");
			detail.setClockInMonth(detail.getStartDateTime().format(formatterMonth));
		
		
			DateTimeFormatter formatterDay   = DateTimeFormatter.ofPattern("dd");
			detail.setClockInDay(  detail.getStartDateTime().format(formatterDay));
			
			
			// 時給を取得(その社員の出勤日時時点の時給)
			int hourlywage = this.getHourlywage(detail.getEmployeeId(),detail.getStartDateTime());
			detail.setHourlywage(hourlywage);
			
			
			// 勤務時間を算出
			detail.setWorkingHoures(0); // 初期化
			if (detail.getEndDateTime() != null) {
				double workingHoures = getWorkingHoures(detail.getStartDateTime(),detail.getEndDateTime());
				detail.setWorkingHoures(workingHoures);
			}
			
			
			// 年月日時分秒までの日時フォーマットを準備
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			
			// 開始・終了日時はSQL用にフォーマットを整えておく
			String startDateTimeString = formatter.format(detail.getStartDateTime());
			
			String endDateTimeString = null;
			if (detail.getEndDateTime() != null) {
				endDateTimeString      = formatter.format(detail.getEndDateTime()); 
			}
			
			String beforeStartDateTimeString = formatter.format(detail.getBeforeStartDateTime());
			
			
			
			String sql = " update TT_CLOCKINOUT";
			sql  = sql + " set";
			sql  = sql + "     EMPLOYEEID        = ?";
			sql  = sql + "    ,CLOCKINYEAR       = ?";
			sql  = sql + "    ,CLOCKINMONTH      = ?";
			sql  = sql + "    ,CLOCKINDAY        = ?";
			sql  = sql + "    ,CLOCKINDATETIME   = ?";
			sql  = sql + "    ,CLOCKOUTDATETIME  = ?";
			sql  = sql + "    ,HOURLYWAGE        = ?";
			sql  = sql + "    ,WORKING_HOUERS    = ?";
			sql  = sql + "    ,BIKO              = ?";
			//sql  = sql + "    ,DELETEFLG         = ?";
			//sql  = sql + "    ,DELETEYMDHMS      = ?";
			sql  = sql + "    ,SYSUPDUSERID      = ?";
			sql  = sql + "    ,SYSUPDPGMID       = ?";
			sql  = sql + "    ,SYSUPDYMDHMS      = current_timestamp(3)";
			sql  = sql + " where";
			sql  = sql + "     EMPLOYEEID        = ?";
			sql  = sql + " and CLOCKINDATETIME   = ?";
			
			
			int ret = this.jdbcTemplate.update(sql
					,detail.getEmployeeId()
					,detail.getClockInYear()
					,detail.getClockInMonth()
					,detail.getClockInDay()
					,startDateTimeString
					,endDateTimeString
					,detail.getHourlywage()
					,detail.getWorkingHoures()
					,detail.getBiko()
					,userName
					,registPgmId
					,detail.getBeforeEmployeeId()
					,beforeStartDateTimeString
					);
			
			
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
	public boolean deleteClockInOut(FormKanriMainteClockInOutDetail detail ,String userName,String registPgmId) {
		
		String pgmId = classId + ".deleteClockInOut";
		log.info("【INF】" + pgmId + ":処理開始 社員=[" + detail.getEmployeeId() + "]開始日時=[" + detail.getStartDateTime() + "]");
		
		try {
			
			
			// 年月日時分秒までの日時フォーマットを準備
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			
			// 開始日時はSQL用にフォーマットを整えておく
			String startDateTimeString = formatter.format(detail.getStartDateTime());
			
			String beforeStartDateTimeString = formatter.format(detail.getBeforeStartDateTime());
			
			
			
			String sql = " update TT_CLOCKINOUT";
			sql  = sql + " set";
			sql  = sql + "     BIKO              = ?";
			sql  = sql + "    ,DELETEFLG         = true";
			sql  = sql + "    ,DELETEYMDHMS      = current_timestamp(3)";
			sql  = sql + "    ,SYSUPDUSERID      = ?";
			sql  = sql + "    ,SYSUPDPGMID       = ?";
			sql  = sql + "    ,SYSUPDYMDHMS      = current_timestamp(3)";
			sql  = sql + " where";
			sql  = sql + "     EMPLOYEEID        = ?";
			sql  = sql + " and CLOCKINDATETIME   = ?";
			
			
			int ret = this.jdbcTemplate.update(sql
					,detail.getBiko()
					,userName
					,registPgmId
					,detail.getEmployeeId()
					,beforeStartDateTimeString
					);
			
			
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
	public boolean revivalClockInOut(FormKanriMainteClockInOutDetail detail ,String userName,String registPgmId) {
		
		String pgmId = classId + ".revivalClockInOut";
		log.info("【INF】" + pgmId + ":処理開始 社員=[" + detail.getEmployeeId() + "]開始日時=[" + detail.getStartDateTime() + "]");
		
		try {
			
			// 年月日時分秒までの日時フォーマットを準備
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			
			// 開始日時はSQL用にフォーマットを整えておく
			String startDateTimeString = formatter.format(detail.getStartDateTime());
			
			String beforeStartDateTimeString = formatter.format(detail.getBeforeStartDateTime());
			
			
			
			String sql = " update TT_CLOCKINOUT";
			sql  = sql + " set";
			sql  = sql + "     BIKO              = ?";
			sql  = sql + "    ,DELETEFLG         = false";
			sql  = sql + "    ,DELETEYMDHMS      = null";
			sql  = sql + "    ,SYSUPDUSERID      = ?";
			sql  = sql + "    ,SYSUPDPGMID       = ?";
			sql  = sql + "    ,SYSUPDYMDHMS      = current_timestamp(3)";
			sql  = sql + " where";
			sql  = sql + "     EMPLOYEEID        = ?";
			sql  = sql + " and CLOCKINDATETIME   = ?";
			
			
			int ret = this.jdbcTemplate.update(sql
					,detail.getBiko()
					,userName
					,registPgmId
					,detail.getEmployeeId()
					,beforeStartDateTimeString
					);
			
			
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
