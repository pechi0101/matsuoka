package com.example.DAO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.example.common.EmployeeAuthority;
import com.example.counst.SpecialWork;
import com.example.form.FormDispWorkStatusDetail;
import com.example.form.FormDispWorkStatusList;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DaoFormDispWorkStatusList {
	
	private JdbcTemplate  jdbcTemplate;
	private String classId = "DaoFormDispWorkStatusList";
	
	// コンストラクタ
	public DaoFormDispWorkStatusList(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	// リストを取得
	public FormDispWorkStatusList getDispWorkStatusList(String employeeId) {
		
		// 年月日時分秒までの日時フォーマットを準備
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		String pgmId = classId + ".getDispWorkStatusList";
		log.info("【INF】" + pgmId + ":処理開始 社員ID=[" + employeeId + "]");
		
		
		// 返却値
		FormDispWorkStatusList retForm = new FormDispWorkStatusList();
		
		// ユーザ編集権限を取得
		EmployeeAuthority employeeAuthority = new EmployeeAuthority(this.jdbcTemplate);
		retForm.setEditAuthority(employeeAuthority.IsEditAuthority(employeeId));
		retForm.setAdministrationAuthority(employeeAuthority.IsAdministrationAuthority(employeeId));
		
		
		
		try {
			
			//【メモ】：未削除で作業未完了の情報を取得
			
			
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
			sql  = sql + "    ,TM_EMPLOYEE_START.AUTHORITYKBN";
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
			sql  = sql + "     TT_HOUSE_WORKSTATUS.STARTEMPLOYEEID = ?";
			sql  = sql + " and TT_HOUSE_WORKSTATUS.DELETEFLG = false";
			sql  = sql + " and TT_HOUSE_WORKSTATUS.ENDDATETIME is null";
			
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
			sql  = sql + "    ,TM_EMPLOYEE_START.AUTHORITYKBN";
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
			sql  = sql + "     TT_HOUSE_WORKSTATUS_SHUKAKU.STARTEMPLOYEEID = ?";
			sql  = sql + " and TT_HOUSE_WORKSTATUS_SHUKAKU.DELETEFLG = false";
			sql  = sql + " and TT_HOUSE_WORKSTATUS_SHUKAKU.ENDDATETIME is null";
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
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql,employeeId,employeeId);
			
			
			// 【重要】この変数に作業開始、終了日時を文字列でセットし、作業状況の判定に使用する。
			String startDateTimeString = "";
			String endDateTimeString = "";
			String deleteDateTimeString = "";
			
			
			for (Map<String, Object> rs: rsList) {
				
				
				FormDispWorkStatusDetail detail = new FormDispWorkStatusDetail();
				
				
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
				
				
				
				// ------------------------------------------------
				// 表示メッセージ
				
				// 開始日時を"yyyy/MM/dd HH:mm"形式に編集
				DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
				DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
				LocalDateTime dateTime = LocalDateTime.parse(startDateTimeString, inputFormatter);
				String outputDateTimeString = dateTime.format(outputFormatter);
				
				
				// 【メモ】\nで改行して表示させる
				String wkInfoData = "";
				if (detail.getColNo().equals("") == true) {
					
					// 列の概念が存在しない作業(収穫作業)の場合
					wkInfoData =   "ハウス：" + detail.getHouseName()
								 + "、作業名称：" + detail.getWorkName() + "\n"
								 + "、開始日時：" + outputDateTimeString;
				} else {
					
					// 列の概念が存在する作業(例：葉かき)の場合
					wkInfoData =   "ハウス：" + detail.getHouseName()
								 + "、列："   + detail.getColNo() + "\n"
								 + "、作業名称：" + detail.getWorkName() + "\n"
								 + "、開始日時：" + outputDateTimeString;
				}
				
				detail.setDispDetailInfo(wkInfoData);
				
				
				
				retForm.addWorkStatus(detail);
				
			}
			
			
			log.info("【INF】" + pgmId + ":処理終了 取得件数=[" + Integer.toString(retForm.getStrWorkStatusDetailList().size()) + "]件");
			return retForm;
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了[" + e.toString() + "]");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return null;
		}
	}
	
	
	
	// データ更新（作業終了）
	public boolean updateWorkStatusWorkEnd(FormDispWorkStatusDetail detail ,String userName,String registPgmId) {
		
		String pgmId = classId + ".updateWorkStatusWorkEnd";

		log.info("【INF】" + pgmId + ":処理開始 社員ID=[" + userName + "]プログラムID=[" + registPgmId + "]");
		log.info("【INF】" + pgmId + ":処理開始 ハウスID=[" + detail.getHouseId() + "]列No=[" + detail.getColNo() + "]作業ID=[" + detail.getWorkId() + "]、開始日時=[" + detail.getStartDateTime() + "]");
		
		try {
			
			int ret = 0;
			
			if (detail.getWorkId().equals(SpecialWork.SHUKAKU) == true) {
				
				//log.info("【INF】" + pgmId + ":収穫を更新 BoxCount=[" + detail.getBoxCount() + "]");
				
				//------------------------------------------------
				// ハウス作業(収穫)進捗テーブルの更新
				
				
				
				String sql = " update TT_HOUSE_WORKSTATUS_SHUKAKU";
				sql  = sql + " set";
				sql  = sql + "     ENDDATETIME      = current_timestamp(3)";
				sql  = sql + "    ,ENDEMPLOYEEID    = ?";
				sql  = sql + "    ,PERCENT          = 100";
				sql  = sql + "    ,BOXCOUNT         = 0";  // 収穫ケース数は0で更新
				sql  = sql + "    ,SYSUPDUSERID     = ?";
				sql  = sql + "    ,SYSUPDPGMID      = ?";
				sql  = sql + "    ,SYSUPDYMDHMS     = current_timestamp(3)";
				sql  = sql + " where";
				sql  = sql + "     HOUSEID          = ?";
				sql  = sql + " and WORKID           = ?";
				sql  = sql + " and STARTDATETIME    = ?";
				
				
				// 年月日時分秒までの日時フォーマットを準備
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				
				ret = this.jdbcTemplate.update(sql
						,userName
						,userName
						,registPgmId
						,detail.getHouseId()
						,detail.getWorkId()
						,formatter.format(detail.getStartDateTime())
						);
				
			} else {
				
				
				//------------------------------------------------
				// ハウス作業進捗テーブルの更新
				
				
				
				String sql = " update TT_HOUSE_WORKSTATUS";
				sql  = sql + " set";
				sql  = sql + "     ENDDATETIME      = current_timestamp(3)";
				sql  = sql + "    ,ENDEMPLOYEEID    = ?";
				sql  = sql + "    ,PERCENT          = 100";
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
				
				ret = this.jdbcTemplate.update(sql
						,userName
						,userName
						,registPgmId
						,detail.getHouseId()
						,detail.getColNo()
						,detail.getWorkId()
						,formatter.format(detail.getStartDateTime())
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
			
			log.error("【ERR】" + pgmId + ":異常終了[" + e.toString() + "]");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return false;
		}
	}
	
	
	/*
	
	★★★★★★★★★★★★ 
	未使用のためコメントアウト
	
	
	// データ更新
	public boolean updateWorkStatus(FormDispWorkStatusDetail detail ,String userName,String registPgmId) {
		
		String pgmId = classId + ".updateWorkStatus";
		log.info("【INF】" + pgmId + ":処理開始 ハウスID=[" + detail.getHouseId() + "]列No=[" + detail.getColNo() + "]作業ID=[" + detail.getWorkId() + "]、開始日時=[" + detail.getStartDateTime() + "]");
		
		try {
			
			int ret = 0;
			
			if (detail.getWorkId().equals(SpecialWork.SHUKAKU) == true) {
				
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
						,detail.getHouseId()
						,detail.getColNo()
						,detail.getWorkId()
						,formatter.format(detail.getStartDateTime())
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
						,detail.getHouseId()
						,detail.getColNo()
						,detail.getWorkId()
						,formatter.format(detail.getStartDateTime())
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
	public boolean deleteWorkStatus(FormDispWorkStatusDetail detail ,String userName,String registPgmId) {
		
		// 年月日時分秒までの日時フォーマットを準備
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		String pgmId = classId + ".deleteWorkStatus";
		log.info("【INF】" + pgmId + ":処理開始 ハウスID=[" + detail.getHouseId() + "]列No=[" + detail.getColNo() + "]作業ID=[" + detail.getWorkId() + "]、開始日時=[" + formatter.format(detail.getStartDateTime()) + "]");
		
		try {
			
			int ret = 0;
			
			if (detail.getWorkId().equals(SpecialWork.SHUKAKU) == true) {
				
				
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
						,detail.getHouseId()
						,detail.getColNo()
						,detail.getWorkId()
						,formatter.format(detail.getStartDateTime())
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
						,detail.getHouseId()
						,detail.getColNo()
						,detail.getWorkId()
						,formatter.format(detail.getStartDateTime())
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
	
	
	*/
}
