package com.example.DAO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.example.entity.HouseWorkStatusShukaku;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DaoHouseWorkStatusShukaku {
	
	private JdbcTemplate  jdbcTemplate;
	private String classId = "DaoHouseWorkStatusShukaku";
	
	// コンストラクタ
	public DaoHouseWorkStatusShukaku(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	// 定数:作業状況
	public static int STATUS_NOT = 0;	    // 作業未実施状態
	public static int STATUS_DONE = 1;	    // 作業完了状態
	public static int STATUS_WORKING = 2; // 作業中状態
	public static int STATUS_ERROR = 9;   // 作業状況取得エラー/作業状況判定エラー
	
	
	// 最新の作業状況を取得
	public HouseWorkStatusShukaku getLatestWorkStatus(String workId,String houseId,String startEmployeeId) {
		
		String pgmId = classId + ".getLatestWorkStatus";
		log.info("【INF】" + pgmId + ":処理開始 作業ID=[" + workId + "]、ハウスID=[" + houseId + "]、作業開始社員ID=[" + startEmployeeId + "]");
		
		
		// 返却値
		HouseWorkStatusShukaku houseWorkStatusShukaku = new HouseWorkStatusShukaku();
		
		try {
			
			String sql = " select";
			sql  = sql + "     DATE_FORMAT(STARTDATETIME,'%Y%m%d%H%i%S') STARTDATETIME_STRING"; // YYYYMMDDHHMMSS形式で取得（時間は24時間制）
			sql  = sql + "    ,STARTEMPLOYEEID";
			sql  = sql + "    ,DATE_FORMAT(ENDDATETIME,'%Y%m%d%H%i%S') ENDDATETIME_STRING"; // YYYYMMDDHHMMSS形式で取得（時間は24時間制）
			sql  = sql + "    ,ENDEMPLOYEEID";
			sql  = sql + "    ,PERCENT";
			sql  = sql + "    ,BIKO";
			sql  = sql + "    ,BOXCOUNT";
			sql  = sql + " from";
			sql  = sql + "     TT_HOUSE_WORKSTATUS_SHUKAKU";
			sql  = sql + "    ,(";
			sql  = sql + "     select";
			sql  = sql + "         WORKID";
			sql  = sql + "        ,HOUSEID";
			sql  = sql + "        ,COLNO";
			sql  = sql + "        ,MAX(STARTDATETIME) MAX_STARTDATETIME";
			sql  = sql + "     from";
			sql  = sql + "         TT_HOUSE_WORKSTATUS_SHUKAKU";
			sql  = sql + "     where";
			sql  = sql + "         WORKID          = ?";
			sql  = sql + "     and HOUSEID         = ?";
			sql  = sql + "     and STARTEMPLOYEEID = ?";
			sql  = sql + "     group by";
			sql  = sql + "         WORKID";
			sql  = sql + "        ,HOUSEID";
			sql  = sql + "        ,COLNO";
			sql  = sql + "    )VT_HOUSE_WORKSTATUS_SHUKAKU";
			sql  = sql + " where";
			sql  = sql + "     TT_HOUSE_WORKSTATUS_SHUKAKU.WORKID        = VT_HOUSE_WORKSTATUS_SHUKAKU.WORKID";
			sql  = sql + " and TT_HOUSE_WORKSTATUS_SHUKAKU.HOUSEID       = VT_HOUSE_WORKSTATUS_SHUKAKU.HOUSEID";
			sql  = sql + " and TT_HOUSE_WORKSTATUS_SHUKAKU.COLNO         = VT_HOUSE_WORKSTATUS_SHUKAKU.COLNO";
			sql  = sql + " and TT_HOUSE_WORKSTATUS_SHUKAKU.STARTDATETIME = VT_HOUSE_WORKSTATUS_SHUKAKU.MAX_STARTDATETIME";
			
			
			
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
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql,workId,houseId,startEmployeeId);
			
			
			// 【重要】この変数に作業開始、終了日時を文字列でセットし、作業状況の判定に使用する。
			String startDateTimeString = "";
			String endDateTimeString = "";
			
			
			for (Map<String, Object> rs: rsList) {
				
				houseWorkStatusShukaku.setWorkId(workId);
				houseWorkStatusShukaku.setHouseId(houseId);
				houseWorkStatusShukaku.setColNo("XX");
				
				
				// 年月日時分秒までの日時フォーマットを準備
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
				
				
				// 作業開始日時
				startDateTimeString = rs.get("STARTDATETIME_STRING").toString();
				houseWorkStatusShukaku.setStartDateTime(LocalDateTime.parse(startDateTimeString,formatter));
				
				// 作業開始社員ID
				houseWorkStatusShukaku.setStartEmployeeId(rs.get("STARTEMPLOYEEID").toString());
				
				
				// 作業終了日時
				if (rs.get("ENDDATETIME_STRING") != null) {
					endDateTimeString   = rs.get("ENDDATETIME_STRING").toString();
					houseWorkStatusShukaku.setEndDateTime(LocalDateTime.parse(endDateTimeString,formatter));
				}
				
				// 作業終了社員ID
				if (rs.get("ENDEMPLOYEEID") != null) {
					houseWorkStatusShukaku.setEndEmployeeId(rs.get("ENDEMPLOYEEID").toString());
				}
				
				
				// 進捗率
				houseWorkStatusShukaku.setPercent(Integer.parseInt(rs.get("PERCENT").toString()));
				// 備考
				if (rs.get("BIKO") != null) {
					houseWorkStatusShukaku.setBiko(rs.get("BIKO").toString());
				}
				// 収穫箱数
				if (rs.get("BOXCOUNT") != null) {
					houseWorkStatusShukaku.setBoxCount(Double.parseDouble(rs.get("BOXCOUNT").toString()));
				}
				
				// 値は１件のみ取得されるためここでLOOP終了
				break;
			}
			
			log.info("【DBG】" + pgmId + "作業開始日時=[" + startDateTimeString + "]作業完了日時=[" + endDateTimeString + "]");
			
			
			
			
			// ------------------------------------------------
			// 作業状況判定
			// 作業完了日が取得出来ていない                        ＝作業未実施状態
			// 作業完了日が取得出来ている＋作業完了日が入っている  ＝作業完了  状態
			// 作業完了日が取得出来ている＋作業完了日が入っていない＝作業中    状態
			
			
			// 返却値は「判定エラー」で初期化
			houseWorkStatusShukaku.setWorkStatus(DaoHouseWorkStatusShukaku.STATUS_ERROR);
			
			
			if (
			   startDateTimeString.trim().length() == 0) {
				
				houseWorkStatusShukaku.setWorkStatus(DaoHouseWorkStatusShukaku.STATUS_NOT); //作業未実施状態
				
			} else if (
			   startDateTimeString.trim().length() > 0
			&& endDateTimeString.trim().length()   > 0) {
				
				houseWorkStatusShukaku.setWorkStatus(DaoHouseWorkStatusShukaku.STATUS_DONE);//作業完了  状態
				
			} else if (
			   startDateTimeString.trim().length() > 0
			&& endDateTimeString.trim().length()  == 0) {
				
				houseWorkStatusShukaku.setWorkStatus(DaoHouseWorkStatusShukaku.STATUS_WORKING);//作業中    状態
				
			}
			
			log.info("【INF】" + pgmId + ":処理終了 判定結果=[" + Integer.toString(houseWorkStatusShukaku.getWorkStatus()) + "]");
			return houseWorkStatusShukaku;
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return houseWorkStatusShukaku;
		}
	}
	
	
	// 最新の作業状況を取得
	public HouseWorkStatusShukaku getLatestWorkStatusForShukaku(String workId,String houseId,String employeeId) {
		
		String pgmId = classId + ".getLatestWorkStatus";
		log.info("【INF】" + pgmId + ":処理開始 作業ID=[" + workId + "]、ハウスID=[" + houseId + "]、従業員ID=[" + employeeId + "]");
		
		
		// 返却値
		HouseWorkStatusShukaku houseWorkStatusShukaku = new HouseWorkStatusShukaku();
		
		try {
			
			String sql = " select";
			sql  = sql + "     DATE_FORMAT(TT_HOUSE_WORKSTATUS_SHUKAKU.STARTDATETIME,'%Y%m%d%H%i%S') STARTDATETIME_STRING"; // YYYYMMDDHHMMSS形式で取得（時間は24時間制）
			sql  = sql + "    ,TT_HOUSE_WORKSTATUS_SHUKAKU.STARTEMPLOYEEID";
			sql  = sql + "    ,DATE_FORMAT(TT_HOUSE_WORKSTATUS_SHUKAKU.ENDDATETIME,'%Y%m%d%H%i%S') ENDDATETIME_STRING"; // YYYYMMDDHHMMSS形式で取得（時間は24時間制）
			sql  = sql + "    ,TT_HOUSE_WORKSTATUS_SHUKAKU.ENDEMPLOYEEID";
			sql  = sql + "    ,TT_HOUSE_WORKSTATUS_SHUKAKU.PERCENT";
			sql  = sql + "    ,TT_HOUSE_WORKSTATUS_SHUKAKU.BIKO";
			sql  = sql + "    ,TT_HOUSE_WORKSTATUS_SHUKAKU.BOXCOUNT";
			sql  = sql + " from";
			sql  = sql + "     TT_HOUSE_WORKSTATUS_SHUKAKU";
			sql  = sql + "    ,(";
			sql  = sql + "     select";
			sql  = sql + "         WORKID";
			sql  = sql + "        ,HOUSEID";
			sql  = sql + "        ,STARTEMPLOYEEID";
			sql  = sql + "        ,MAX(STARTDATETIME) MAX_STARTDATETIME";
			sql  = sql + "     from";
			sql  = sql + "         TT_HOUSE_WORKSTATUS_SHUKAKU";
			sql  = sql + "     where";
			sql  = sql + "         WORKID          = ?";
			sql  = sql + "     and HOUSEID         = ?";
			sql  = sql + "     and STARTEMPLOYEEID = ?";
			sql  = sql + "     group by";
			sql  = sql + "         WORKID";
			sql  = sql + "        ,HOUSEID";
			sql  = sql + "        ,STARTEMPLOYEEID";
			sql  = sql + "    )VT_HOUSE_WORKSTATUS_SHUKAKU";
			sql  = sql + " where";
			sql  = sql + "     TT_HOUSE_WORKSTATUS_SHUKAKU.WORKID          = VT_HOUSE_WORKSTATUS_SHUKAKU.WORKID";
			sql  = sql + " and TT_HOUSE_WORKSTATUS_SHUKAKU.HOUSEID         = VT_HOUSE_WORKSTATUS_SHUKAKU.HOUSEID";
			sql  = sql + " and TT_HOUSE_WORKSTATUS_SHUKAKU.STARTEMPLOYEEID = VT_HOUSE_WORKSTATUS_SHUKAKU.STARTEMPLOYEEID";
			sql  = sql + " and TT_HOUSE_WORKSTATUS_SHUKAKU.STARTDATETIME   = VT_HOUSE_WORKSTATUS_SHUKAKU.MAX_STARTDATETIME";
			
			
			
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
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql,workId,houseId,employeeId);
			
			
			// 【重要】この変数に作業開始、終了日時を文字列でセットし、作業状況の判定に使用する。
			String startDateTimeString = "";
			String endDateTimeString = "";
			
			
			for (Map<String, Object> rs: rsList) {
				
				houseWorkStatusShukaku.setWorkId(workId);
				houseWorkStatusShukaku.setHouseId(houseId);
				houseWorkStatusShukaku.setColNo("XX"); // 収穫の場合は列№は不要であるため仮の値をセット
				
				
				// 年月日時分秒までの日時フォーマットを準備
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
				
				
				// 作業開始日時
				startDateTimeString = rs.get("STARTDATETIME_STRING").toString();
				houseWorkStatusShukaku.setStartDateTime(LocalDateTime.parse(startDateTimeString,formatter));
				
				// 作業開始社員ID
				houseWorkStatusShukaku.setStartEmployeeId(rs.get("STARTEMPLOYEEID").toString());
				
				
				// 作業終了日時
				if (rs.get("ENDDATETIME_STRING") != null) {
					endDateTimeString   = rs.get("ENDDATETIME_STRING").toString();
					houseWorkStatusShukaku.setEndDateTime(LocalDateTime.parse(endDateTimeString,formatter));
				}
				
				// 作業終了社員ID
				if (rs.get("ENDEMPLOYEEID") != null) {
					houseWorkStatusShukaku.setEndEmployeeId(rs.get("ENDEMPLOYEEID").toString());
				}
				
				
				// 進捗率
				houseWorkStatusShukaku.setPercent(Integer.parseInt(rs.get("PERCENT").toString()));
				// 備考
				if (rs.get("BIKO") != null) {
					houseWorkStatusShukaku.setBiko(rs.get("BIKO").toString());
				}
				// 収穫箱数
				if (rs.get("BOXCOUNT") != null) {
					houseWorkStatusShukaku.setBoxCount(Double.parseDouble(rs.get("BOXCOUNT").toString()));
				}
				
				// 値は１件のみ取得されるためここでLOOP終了
				break;
			}
			
			log.info("【DBG】" + pgmId + "作業開始日時=[" + startDateTimeString + "]作業完了日時=[" + endDateTimeString + "]");
			
			
			
			
			// ------------------------------------------------
			// 作業状況判定
			// 作業完了日が取得出来ていない                        ＝作業未実施状態
			// 作業完了日が取得出来ている＋作業完了日が入っている  ＝作業完了  状態
			// 作業完了日が取得出来ている＋作業完了日が入っていない＝作業中    状態
			
			
			// 返却値は「判定エラー」で初期化
			houseWorkStatusShukaku.setWorkStatus(DaoHouseWorkStatusShukaku.STATUS_ERROR);
			
			
			if (
			   startDateTimeString.trim().length() == 0) {
				
				houseWorkStatusShukaku.setWorkStatus(DaoHouseWorkStatusShukaku.STATUS_NOT); //作業未実施状態
				
			} else if (
			   startDateTimeString.trim().length() > 0
			&& endDateTimeString.trim().length()   > 0) {
				
				houseWorkStatusShukaku.setWorkStatus(DaoHouseWorkStatusShukaku.STATUS_DONE);//作業完了  状態
				
			} else if (
			   startDateTimeString.trim().length() > 0
			&& endDateTimeString.trim().length()  == 0) {
				
				houseWorkStatusShukaku.setWorkStatus(DaoHouseWorkStatusShukaku.STATUS_WORKING);//作業中    状態
				
			}
			
			log.info("【INF】" + pgmId + ":処理終了 判定結果=[" + Integer.toString(houseWorkStatusShukaku.getWorkStatus()) + "]");
			return houseWorkStatusShukaku;
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return houseWorkStatusShukaku;
		}
	}
	
	
	
	
	
	// データ存在チェック（主キー完全一致チェック）
	public boolean exsistsForPkey(String workId,String houseId,String colNo,LocalDateTime startDateTime) {
		
		String pgmId = classId + ".exsistsForPkey";
		log.info("【INF】" + pgmId + ":処理開始 作業ID=[" + workId + "]、ハウスID=[" + houseId + "]、列№=[" + colNo + "]、開始日時=[" + startDateTime.toString() + "]");
		
		try {
			
			String sql = " select count(1)";
			sql  = sql + " from";
			sql  = sql + "     TT_HOUSE_WORKSTATUS_SHUKAKU";
			sql  = sql + " where";
			sql  = sql + "     WORKID        = ?";
			sql  = sql + " and HOUSEID       = ?";
			sql  = sql + " and COLNO         = ?";
			sql  = sql + " and STARTDATETIME = ?";
			
			// 年月日時分秒までの日時フォーマットを準備
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			
			
			// queryForListメソッドでSQLを実行
			int count = this.jdbcTemplate.queryForObject(sql
											,Integer.class
											,workId
											,houseId
											,colNo
											,formatter.format(startDateTime) // 日付は決められた書式(フォーマット)の文字列で検索する
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
	
	
	
	// データ登録（作業開始）
	public boolean registStartStatus(HouseWorkStatusShukaku houseWorkStatusShukaku ,String userName,String registPgmId) {

		// 年月日時分秒までの日時フォーマットを準備
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		String pgmId = classId + ".registStartStatus";
		log.info("【INF】" + pgmId + ":処理開始 □開始日時=[" + formatter.format(houseWorkStatusShukaku.getStartDateTime()) + "]");
		
		try {
			
			String sql = " insert into TT_HOUSE_WORKSTATUS_SHUKAKU (";
			sql  = sql + "     WORKID";
			sql  = sql + "    ,HOUSEID";
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
			sql  = sql + "     ?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,null"; // 登録時は作業終了日時はnull
			sql  = sql + "    ,null"; // 登録時は作業終了社員はnull
			sql  = sql + "    ,0";    // 登録時は進捗率_開始は０％
			sql  = sql + "    ,0";    // 登録時は進捗率_終了は０％
			sql  = sql + "    ,0";    // 削除フラグ falseで初期化
			sql  = sql + "    ,null"; // 削除日時
			sql  = sql + "    ,?";    // 備考
			sql  = sql + "    ,0";    // 登録時は収穫箱数は０箱
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,current_timestamp(3)";
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,current_timestamp(3)";
			sql  = sql + " )";
			
			
			
			int ret = this.jdbcTemplate.update(sql
					,houseWorkStatusShukaku.getWorkId()
					,houseWorkStatusShukaku.getHouseId()
					,houseWorkStatusShukaku.getColNo()
					,formatter.format(houseWorkStatusShukaku.getStartDateTime())
					,houseWorkStatusShukaku.getStartEmployeeId()
					,houseWorkStatusShukaku.getBiko()
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
	
	
	
	// データ更新（作業完了）
	public boolean updateEndStatus(HouseWorkStatusShukaku houseWorkStatusShukaku ,String userName,String registPgmId) {
		
		String pgmId = classId + ".updateEndStatus";
		log.info("【INF】" + pgmId + ":処理開始");
		
		try {
			
			String sql = " update TT_HOUSE_WORKSTATUS_SHUKAKU";
			sql  = sql + " set";
			sql  = sql + "     ENDDATETIME   = ?";
			sql  = sql + "    ,ENDEMPLOYEEID = ?";
			sql  = sql + "    ,PERCENT       = ?";
			sql  = sql + "    ,BIKO          = ?";
			sql  = sql + "    ,BOXCOUNT      = ?";
			sql  = sql + "    ,SYSUPDUSERID  = ?";
			sql  = sql + "    ,SYSUPDPGMID   = ?";
			sql  = sql + "    ,SYSUPDYMDHMS  = current_timestamp(3)";
			sql  = sql + " where";
			sql  = sql + "     WORKID        = ?";
			sql  = sql + " and HOUSEID       = ?";
			sql  = sql + " and COLNO         = ?";
			sql  = sql + " and STARTDATETIME = ?";
			
			
			// 年月日時分秒までの日時フォーマットを準備
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			
			
			int ret = this.jdbcTemplate.update(sql
					,formatter.format(houseWorkStatusShukaku.getEndDateTime())
					,houseWorkStatusShukaku.getEndEmployeeId()
					,houseWorkStatusShukaku.getPercent()
					,houseWorkStatusShukaku.getBiko()
					,houseWorkStatusShukaku.getBoxCount()
					,userName
					,registPgmId
					,houseWorkStatusShukaku.getWorkId()
					,houseWorkStatusShukaku.getHouseId()
					,houseWorkStatusShukaku.getColNo()
					,formatter.format(houseWorkStatusShukaku.getStartDateTime())
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
