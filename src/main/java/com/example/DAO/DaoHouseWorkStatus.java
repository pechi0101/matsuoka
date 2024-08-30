package com.example.DAO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.example.counst.IntegerErrorCode;
import com.example.entity.HouseWorkStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DaoHouseWorkStatus {
	
	private JdbcTemplate  jdbcTemplate;
	private String classId = "DaoHouseWorkStatus";
	
	// コンストラクタ
	public DaoHouseWorkStatus(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	// 定数:作業状況
	public static int STATUS_NOT = 0;	    // 作業未実施状態
	public static int STATUS_DONE = 1;	    // 作業完了状態
	public static int STATUS_WORKING = 2; // 作業中状態
	public static int STATUS_WORKING_STOP = 3; // 作業中断状態
	public static int STATUS_ERROR = 9;   // 作業状況取得エラー/作業状況判定エラー
	
	
	// 最新の作業状況を取得
	public HouseWorkStatus getLatestWorkStatus(String workId,String houseId,String colNo) {
		
		String pgmId = classId + ".getLatestWorkStatus";
		log.info("【INF】" + pgmId + ":処理開始 作業ID=[" + workId + "]、ハウスID=[" + houseId + "]、列№=[" + colNo + "]");
		
		
		// 返却値
		HouseWorkStatus houseWorkStatus = new HouseWorkStatus();
		
		try {
			
			String sql = " select";
			sql  = sql + "     DATE_FORMAT(STARTDATETIME,'%Y%m%d%H%i%S') STARTDATETIME_STRING"; // YYYYMMDDHHMMSS形式で取得（時間は24時間制）
			sql  = sql + "    ,STARTEMPLOYEEID";
			sql  = sql + "    ,DATE_FORMAT(ENDDATETIME,'%Y%m%d%H%i%S') ENDDATETIME_STRING"; // YYYYMMDDHHMMSS形式で取得（時間は24時間制）
			sql  = sql + "    ,ENDEMPLOYEEID";
			sql  = sql + "    ,PERCENT_START";
			sql  = sql + "    ,PERCENT";
			sql  = sql + "    ,BIKO";
			sql  = sql + " from";
			sql  = sql + "     TT_HOUSE_WORKSTATUS";
			sql  = sql + "    ,(";
			sql  = sql + "     select";
			sql  = sql + "         WORKID";
			sql  = sql + "        ,HOUSEID";
			sql  = sql + "        ,COLNO";
			sql  = sql + "        ,MAX(STARTDATETIME) MAX_STARTDATETIME";
			sql  = sql + "     from";
			sql  = sql + "         TT_HOUSE_WORKSTATUS";
			sql  = sql + "     where";
			sql  = sql + "         WORKID  = ?";
			sql  = sql + "     and HOUSEID = ?";
			sql  = sql + "     and COLNO   = ?";
			sql  = sql + "     group by";
			sql  = sql + "         WORKID";
			sql  = sql + "        ,HOUSEID";
			sql  = sql + "        ,COLNO";
			sql  = sql + "    )VT_HOUSE_WORKSTATUS";
			sql  = sql + " where";
			sql  = sql + "     TT_HOUSE_WORKSTATUS.WORKID        = VT_HOUSE_WORKSTATUS.WORKID";
			sql  = sql + " and TT_HOUSE_WORKSTATUS.HOUSEID       = VT_HOUSE_WORKSTATUS.HOUSEID";
			sql  = sql + " and TT_HOUSE_WORKSTATUS.COLNO         = VT_HOUSE_WORKSTATUS.COLNO";
			sql  = sql + " and TT_HOUSE_WORKSTATUS.STARTDATETIME = VT_HOUSE_WORKSTATUS.MAX_STARTDATETIME";
			
			
			
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
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql,workId,houseId,colNo);
			
			
			// 【重要】この変数に作業開始、終了日時を文字列でセットし、作業状況の判定に使用する。
			String startDateTimeString = "";
			String endDateTimeString = "";
			
			
			for (Map<String, Object> rs: rsList) {
				
				houseWorkStatus.setWorkId(workId);
				houseWorkStatus.setHouseId(houseId);
				houseWorkStatus.setColNo(colNo);
				
				
				// 年月日時分秒までの日時フォーマットを準備
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
				
				
				// 作業開始日時
				startDateTimeString = rs.get("STARTDATETIME_STRING").toString();
				houseWorkStatus.setStartDateTime(LocalDateTime.parse(startDateTimeString,formatter));
				
				// 作業開始社員ID
				houseWorkStatus.setStartEmployeeId(rs.get("STARTEMPLOYEEID").toString());
				
				
				// 作業終了日時
				if (rs.get("ENDDATETIME_STRING") != null) {
					endDateTimeString   = rs.get("ENDDATETIME_STRING").toString();
					houseWorkStatus.setEndDateTime(LocalDateTime.parse(endDateTimeString,formatter));
				}
				
				// 作業終了社員ID
				if (rs.get("ENDEMPLOYEEID") != null) {
					houseWorkStatus.setEndEmployeeId(rs.get("ENDEMPLOYEEID").toString());
				}
				
				
				// 進捗率_開始
				houseWorkStatus.setPercentStart(Integer.parseInt(rs.get("PERCENT_START").toString()));
				// 進捗率_終了
				houseWorkStatus.setPercent(Integer.parseInt(rs.get("PERCENT").toString()));
				
				// 備考
				if (rs.get("BIKO") != null) {
					houseWorkStatus.setBiko(rs.get("BIKO").toString());
				}
				
				// 値は１件のみ取得されるためここでLOOP終了
				break;
			}
			
			log.info("【DBG】" + pgmId + "作業開始日時=[" + startDateTimeString + "]作業完了日時=[" + endDateTimeString + "]進捗率_開始=[" + houseWorkStatus.getPercentStart() + "]進捗率_終了=[" + houseWorkStatus.getPercent() + "]");
			
			
			
			
			// ------------------------------------------------
			// ■作業状況判定
			// 作業完了日が取得出来ていない                                     ：作業未実施状態
			// 作業完了日が取得出来ている＋作業完了日が入っている  ＋進捗＝100% ：作業完了  状態
			// 作業完了日が取得出来ている＋作業完了日が入っている  ＋進捗≠100% ：作業中断  状態
			// 作業完了日が取得出来ている＋作業完了日が入っていない             ：作業中    状態
			
			
			// 返却値は「判定エラー」で初期化
			houseWorkStatus.setWorkStatus(DaoHouseWorkStatus.STATUS_ERROR);
			
			
			if (
			   startDateTimeString.trim().length() == 0) {
				
				houseWorkStatus.setWorkStatus(DaoHouseWorkStatus.STATUS_NOT);//作業未実施状態
				
			} else if (
			   startDateTimeString.trim().length() > 0
			&& endDateTimeString.trim().length()   > 0
			&& houseWorkStatus.getPercent()       == 100) {
				
				houseWorkStatus.setWorkStatus(DaoHouseWorkStatus.STATUS_DONE);//作業完了  状態
				
			} else if (
			   startDateTimeString.trim().length() > 0
			&& endDateTimeString.trim().length()   > 0
			&& houseWorkStatus.getPercent()       != 100) {
				
				houseWorkStatus.setWorkStatus(DaoHouseWorkStatus.STATUS_WORKING_STOP);//作業中断  状態
				
			} else if (
			   startDateTimeString.trim().length() > 0
			&& endDateTimeString.trim().length()  == 0) {
				
				houseWorkStatus.setWorkStatus(DaoHouseWorkStatus.STATUS_WORKING);//作業中    状態
				
			}
			
			log.info("【INF】" + pgmId + ":処理終了 判定結果=[" + Integer.toString(houseWorkStatus.getWorkStatus()) + "]");
			return houseWorkStatus;
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return houseWorkStatus;
		}
	}
	
	
	// 最新の作業状況を取得
	public HouseWorkStatus getLatestWorkStatusForShukaku(String workId,String houseId,String employeeId) {
		
		String pgmId = classId + ".getLatestWorkStatus";
		log.info("【INF】" + pgmId + ":処理開始 作業ID=[" + workId + "]、ハウスID=[" + houseId + "]、従業員ID=[" + employeeId + "]");
		
		
		// 返却値
		HouseWorkStatus houseWorkStatus = new HouseWorkStatus();
		
		try {
			
			String sql = " select";
			sql  = sql + "     DATE_FORMAT(TT_HOUSE_WORKSTATUS.STARTDATETIME,'%Y%m%d%H%i%S') STARTDATETIME_STRING"; // YYYYMMDDHHMMSS形式で取得（時間は24時間制）
			sql  = sql + "    ,TT_HOUSE_WORKSTATUS.STARTEMPLOYEEID";
			sql  = sql + "    ,DATE_FORMAT(TT_HOUSE_WORKSTATUS.ENDDATETIME,'%Y%m%d%H%i%S') ENDDATETIME_STRING"; // YYYYMMDDHHMMSS形式で取得（時間は24時間制）
			sql  = sql + "    ,TT_HOUSE_WORKSTATUS.ENDEMPLOYEEID";
			sql  = sql + "    ,TT_HOUSE_WORKSTATUS.PERCENT";
			sql  = sql + "    ,TT_HOUSE_WORKSTATUS.BIKO";
			sql  = sql + " from";
			sql  = sql + "     TT_HOUSE_WORKSTATUS";
			sql  = sql + "    ,(";
			sql  = sql + "     select";
			sql  = sql + "         WORKID";
			sql  = sql + "        ,HOUSEID";
			sql  = sql + "        ,STARTEMPLOYEEID";
			sql  = sql + "        ,MAX(STARTDATETIME) MAX_STARTDATETIME";
			sql  = sql + "     from";
			sql  = sql + "         TT_HOUSE_WORKSTATUS";
			sql  = sql + "     where";
			sql  = sql + "         WORKID          = ?";
			sql  = sql + "     and HOUSEID         = ?";
			sql  = sql + "     and STARTEMPLOYEEID = ?";
			sql  = sql + "     group by";
			sql  = sql + "         WORKID";
			sql  = sql + "        ,HOUSEID";
			sql  = sql + "        ,STARTEMPLOYEEID";
			sql  = sql + "    )VT_HOUSE_WORKSTATUS";
			sql  = sql + " where";
			sql  = sql + "     TT_HOUSE_WORKSTATUS.WORKID          = VT_HOUSE_WORKSTATUS.WORKID";
			sql  = sql + " and TT_HOUSE_WORKSTATUS.HOUSEID         = VT_HOUSE_WORKSTATUS.HOUSEID";
			sql  = sql + " and TT_HOUSE_WORKSTATUS.STARTEMPLOYEEID = VT_HOUSE_WORKSTATUS.STARTEMPLOYEEID";
			sql  = sql + " and TT_HOUSE_WORKSTATUS.STARTDATETIME   = VT_HOUSE_WORKSTATUS.MAX_STARTDATETIME";
			
			
			
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
				
				houseWorkStatus.setWorkId(workId);
				houseWorkStatus.setHouseId(houseId);
				houseWorkStatus.setColNo("XX"); // 収穫の場合は列№は不要であるため仮の値をセット
				
				
				// 年月日時分秒までの日時フォーマットを準備
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
				
				
				// 作業開始日時
				startDateTimeString = rs.get("STARTDATETIME_STRING").toString();
				houseWorkStatus.setStartDateTime(LocalDateTime.parse(startDateTimeString,formatter));
				
				// 作業開始社員ID
				houseWorkStatus.setStartEmployeeId(rs.get("STARTEMPLOYEEID").toString());
				
				
				// 作業終了日時
				if (rs.get("ENDDATETIME_STRING") != null) {
					endDateTimeString   = rs.get("ENDDATETIME_STRING").toString();
					houseWorkStatus.setEndDateTime(LocalDateTime.parse(endDateTimeString,formatter));
				}
				
				// 作業終了社員ID
				if (rs.get("ENDEMPLOYEEID") != null) {
					houseWorkStatus.setEndEmployeeId(rs.get("ENDEMPLOYEEID").toString());
				}
				
				
				// 進捗率
				houseWorkStatus.setPercent(Integer.parseInt(rs.get("PERCENT").toString()));
				// 備考
				if (rs.get("BIKO") != null) {
					houseWorkStatus.setBiko(rs.get("BIKO").toString());
				}
				
				// 値は１件のみ取得されるためここでLOOP終了
				break;
			}
			
			log.info("【DBG】" + pgmId + "作業開始日時=[" + startDateTimeString + "]作業完了日時=[" + endDateTimeString + "]進捗率_開始=[" + houseWorkStatus.getPercentStart() + "]進捗率_終了=[" + houseWorkStatus.getPercent() + "]");
			
			
			
			
			// ------------------------------------------------
			// ■作業状況判定
			// 作業完了日が取得出来ていない                                     ：作業未実施状態
			// 作業完了日が取得出来ている＋作業完了日が入っている  ＋進捗＝100% ：作業完了  状態
			// 作業完了日が取得出来ている＋作業完了日が入っている  ＋進捗≠100% ：作業中断  状態
			// 作業完了日が取得出来ている＋作業完了日が入っていない             ：作業中    状態
			
			
			// 返却値は「判定エラー」で初期化
			houseWorkStatus.setWorkStatus(DaoHouseWorkStatus.STATUS_ERROR);
			
			
			if (
			   startDateTimeString.trim().length() == 0) {
				
				houseWorkStatus.setWorkStatus(DaoHouseWorkStatus.STATUS_NOT);//作業未実施状態
				
			} else if (
			   startDateTimeString.trim().length() > 0
			&& endDateTimeString.trim().length()   > 0
			&& houseWorkStatus.getPercent()       == 100) {
				
				houseWorkStatus.setWorkStatus(DaoHouseWorkStatus.STATUS_DONE);//作業完了  状態
				
			} else if (
			   startDateTimeString.trim().length() > 0
			&& endDateTimeString.trim().length()   > 0
			&& houseWorkStatus.getPercent()       != 100) {
				
				houseWorkStatus.setWorkStatus(DaoHouseWorkStatus.STATUS_WORKING_STOP);//作業中断  状態
				
			} else if (
			   startDateTimeString.trim().length() > 0
			&& endDateTimeString.trim().length()  == 0) {
				
				houseWorkStatus.setWorkStatus(DaoHouseWorkStatus.STATUS_WORKING);//作業中    状態
				
			}
			
			log.info("【INF】" + pgmId + ":処理終了 判定結果=[" + Integer.toString(houseWorkStatus.getWorkStatus()) + "]");
			return houseWorkStatus;
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return houseWorkStatus;
		}
	}
	
	
	
	
	
	// データ存在チェック（主キー完全一致チェック）
	public boolean exsistsForPkey(String workId,String houseId,String colNo,LocalDateTime startDateTime) {
		
		String pgmId = classId + ".exsistsForPkey";
		log.info("【INF】" + pgmId + ":処理開始 作業ID=[" + workId + "]、ハウスID=[" + houseId + "]、列№=[" + colNo + "]、開始日時=[" + startDateTime.toString() + "]");
		
		try {
			
			String sql = " select count(1)";
			sql  = sql + " from";
			sql  = sql + "     TT_HOUSE_WORKSTATUS";
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
	

	
	
	// 最新の進捗率を取得
	public int getLatestPercent(String workId,String houseId,String colNo) {
		
		String pgmId = classId + ".getLatestPercent";
		log.info("【INF】" + pgmId + ":処理開始 作業ID=[" + workId + "]、ハウスID=[" + houseId + "]、列No=[" + colNo + "]");
		
		
		// 返却値
		int latestPersent = 0;
		
		try {
			
			// ------------------------------------------------
			// 【メモ】
			// 下の通り最新の進捗率(※)を返却する   ※返却したものを次の作業の「作業開始時点の進捗率」にする目的
			//
			// ■例１：作業未実施状態である場合
			// 検索結果なし
			//			              ▼
			//			           0%を返却
			// 
			// ■例２：作業中である場合
			// ３号隔離  01   葉かき  2024/08/25 15:00 ～ 2024/08/25 18:00   0% ～ 20%
			// ３号隔離  01   葉かき  2024/08/26 09:00 ～ 2024/08/25 12:00  20% ～ 80% ★コレを検索
			//			              ▼
			//			          80%を返却
			// 
			// ■例３：作業中である場合
			// ３号隔離  01   葉かき  2024/08/25 15:00 ～ 2024/08/25 18:00   0% ～ 20%
			// ３号隔離  01   葉かき  2024/08/26 09:00 ～ 2024/08/25 12:00  20% ～ 80% ★コレを検索
			// ３号隔離  01   葉かき  2024/08/26 09:00 ～                   80% ～ 
			//			              ▼
			//			          80%を返却
			// 
			// ■例３：作業完了状態である場合
			// ３号隔離  01   葉かき  2024/08/25 15:00 ～ 2024/08/25 18:00   0% ～ 20%
			// ３号隔離  01   葉かき  2024/08/26 09:00 ～ 2024/08/25 12:00  20% ～ 80%
			// ３号隔離  01   葉かき  2024/08/26 09:00 ～ 2024/08/26 15:00  80% ～100% ★コレを検索
			//			              ▼
			//			           0%を返却
			//
			// ------------------------------------------------
			
			String sql = " select distinct";
			sql  = sql + "     TT_HOUSE_WORKSTATUS.PERCENT";
			sql  = sql + " from";
			sql  = sql + "     TT_HOUSE_WORKSTATUS";
			sql  = sql + "    ,(";
			sql  = sql + "     select";
			sql  = sql + "         WORKID";
			sql  = sql + "        ,HOUSEID";
			sql  = sql + "        ,COLNO";
			sql  = sql + "        ,MAX(STARTDATETIME) MAX_STARTDATETIME";
			sql  = sql + "     from";
			sql  = sql + "         TT_HOUSE_WORKSTATUS";
			sql  = sql + "     where";
			sql  = sql + "         WORKID          = ?";
			sql  = sql + "     and HOUSEID         = ?";
			sql  = sql + "     and COLNO           = ?";
			sql  = sql + "     and ENDDATETIME     IS NOT NULL";
			sql  = sql + "     group by";
			sql  = sql + "         WORKID";
			sql  = sql + "        ,HOUSEID";
			sql  = sql + "        ,COLNO";
			sql  = sql + "    )VT_HOUSE_WORKSTATUS";
			sql  = sql + " where";
			sql  = sql + "     TT_HOUSE_WORKSTATUS.WORKID          = VT_HOUSE_WORKSTATUS.WORKID";
			sql  = sql + " and TT_HOUSE_WORKSTATUS.HOUSEID         = VT_HOUSE_WORKSTATUS.HOUSEID";
			sql  = sql + " and TT_HOUSE_WORKSTATUS.COLNO           = VT_HOUSE_WORKSTATUS.COLNO";
			sql  = sql + " and TT_HOUSE_WORKSTATUS.STARTDATETIME   = VT_HOUSE_WORKSTATUS.MAX_STARTDATETIME";
			
			
			
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
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql,workId,houseId,colNo);
			
			
			
			for (Map<String, Object> rs: rsList) {
				
				// 進捗率
				latestPersent = Integer.parseInt(rs.get("PERCENT").toString());
				
				// 値は１件のみ取得されるためここでLOOP終了
				break;
			}
			
			// MAXの進捗率が100%である場合は0%を返却
			if (latestPersent == 100) {
				latestPersent = 0;
			}
			
			
			log.info("【INF】" + pgmId + ":処理終了 取得進捗率=[" + Integer.toString(latestPersent) + "]%");
			return latestPersent;
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return IntegerErrorCode.ERROR;
		}
	}
	
	
	
	// データ登録（作業開始）
	public boolean registStartStatus(HouseWorkStatus houseWorkStatus ,String userName,String registPgmId) {

		// 年月日時分秒までの日時フォーマットを準備
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		String pgmId = classId + ".registStartStatus";
		log.info("【INF】" + pgmId + ":処理開始 □開始日時=[" + formatter.format(houseWorkStatus.getStartDateTime()) + "]");
		
		try {
			
			String sql = " insert into TT_HOUSE_WORKSTATUS (";
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
			sql  = sql + "    ,?";    // 進捗率_開始
			sql  = sql + "    ,0";    // 登録時は進捗率_終了は０％
			sql  = sql + "    ,0";    // 削除フラグ
			sql  = sql + "    ,null"; // 削除日時
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,current_timestamp(3)";
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,current_timestamp(3)";
			sql  = sql + " )";
			
			
			
			int ret = this.jdbcTemplate.update(sql
					,houseWorkStatus.getWorkId()
					,houseWorkStatus.getHouseId()
					,houseWorkStatus.getColNo()
					,formatter.format(houseWorkStatus.getStartDateTime())
					,houseWorkStatus.getStartEmployeeId()
					,houseWorkStatus.getPercentStart()
					,houseWorkStatus.getBiko()
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
	public boolean updateEndStatus(HouseWorkStatus houseWorkStatus ,String userName,String registPgmId) {
		
		String pgmId = classId + ".updateEndStatus";
		log.info("【INF】" + pgmId + ":処理開始");
		
		try {
			
			String sql = " update TT_HOUSE_WORKSTATUS";
			sql  = sql + " set";
			sql  = sql + "     ENDDATETIME   = ?";
			sql  = sql + "    ,ENDEMPLOYEEID = ?";
			sql  = sql + "    ,PERCENT_START = ?";
			sql  = sql + "    ,PERCENT       = ?";
			sql  = sql + "    ,BIKO          = ?";
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
					,formatter.format(houseWorkStatus.getEndDateTime())
					,houseWorkStatus.getEndEmployeeId()
					,houseWorkStatus.getPercentStart()
					,houseWorkStatus.getPercent()
					,houseWorkStatus.getBiko()
					,userName
					,registPgmId
					,houseWorkStatus.getWorkId()
					,houseWorkStatus.getHouseId()
					,houseWorkStatus.getColNo()
					,formatter.format(houseWorkStatus.getStartDateTime())
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
	
	
	
	// データ更新（作業中断）
	public boolean updateHalfWayStatus(HouseWorkStatus houseWorkStatus ,String userName,String registPgmId) {
		
		String pgmId = classId + ".updateHalfWayStatus";
		log.info("【INF】" + pgmId + ":処理開始");
		
		try {
			
			String sql = " update TT_HOUSE_WORKSTATUS";
			sql  = sql + " set";
			sql  = sql + "     ENDDATETIME   = ?";
			sql  = sql + "    ,ENDEMPLOYEEID = ?";
			sql  = sql + "    ,PERCENT_START = ?";
			sql  = sql + "    ,PERCENT       = ?";
			sql  = sql + "    ,BIKO          = ?";
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
					,formatter.format(houseWorkStatus.getEndDateTime())
					,houseWorkStatus.getEndEmployeeId()
					,houseWorkStatus.getPercentStart()
					,houseWorkStatus.getPercent()
					,houseWorkStatus.getBiko()
					,userName
					,registPgmId
					,houseWorkStatus.getWorkId()
					,houseWorkStatus.getHouseId()
					,houseWorkStatus.getColNo()
					,formatter.format(houseWorkStatus.getStartDateTime())
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
	
	
	
	// データ登録（作業開始）※ハウス内の全列分登録
	public boolean registStartStatusAllCol(HouseWorkStatus houseWorkStatus ,String userName,String registPgmId) {
		
		// 年月日時分秒までの日時フォーマットを準備
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		String pgmId = classId + ".registStartStatusAllCol";
		log.info("【INF】" + pgmId + ":処理開始 □開始日時=[" + formatter.format(houseWorkStatus.getStartDateTime()) + "]");
		
		try {
			
			
			// 指定ハウスの全列を取得
			String sql = " select";
			sql  = sql + "     COLNO";
			sql  = sql + " from";
			sql  = sql + "     TM_HOUSECOL";
			sql  = sql + " where";
			sql  = sql + "     HOUSEID = ?";
			sql  = sql + " order by";
			sql  = sql + "     COLNO";
			
			// queryForListメソッドでSQLを実行し、結果MapのListで受け取る。
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql,houseWorkStatus.getHouseId());
			
			
			
			
			// Insert用にSQLを作成
			sql        = " insert into TT_HOUSE_WORKSTATUS (";
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
			sql  = sql + "    ,?";    // 進捗率_開始
			sql  = sql + "    ,0";    // 登録時は進捗率_終了は０％
			sql  = sql + "    ,0";    // 削除フラグ
			sql  = sql + "    ,null"; // 削除日時
			sql  = sql + "    ,?";    // 備考
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,current_timestamp(3)";
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,current_timestamp(3)";
			sql  = sql + " )";
			
			int ret = 0;
			
			for (Map<String, Object> rs: rsList) {
				
				String colNo = rs.get("COLNO").toString();
				
				
				ret = this.jdbcTemplate.update(sql
						,houseWorkStatus.getWorkId()
						,houseWorkStatus.getHouseId()
						,colNo
						,formatter.format(houseWorkStatus.getStartDateTime())
						,houseWorkStatus.getStartEmployeeId()
						,houseWorkStatus.getPercentStart()
						,houseWorkStatus.getBiko()
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
	
	
	
	// データ更新（作業完了）※ハウス内の全列更新
	public boolean updateEndStatusAllCol(HouseWorkStatus houseWorkStatus ,String userName,String registPgmId) {
		
		String pgmId = classId + ".updateEndStatusAllCol";
		log.info("【INF】" + pgmId + ":処理開始");
		
		try {
			
			String sql = " update TT_HOUSE_WORKSTATUS";
			sql  = sql + " set";
			sql  = sql + "     ENDDATETIME   = ?";
			sql  = sql + "    ,ENDEMPLOYEEID = ?";
			sql  = sql + "    ,PERCENT_START = ?";
			sql  = sql + "    ,PERCENT       = ?";
			sql  = sql + "    ,BIKO          = ?";
			sql  = sql + "    ,SYSUPDUSERID  = ?";
			sql  = sql + "    ,SYSUPDPGMID   = ?";
			sql  = sql + "    ,SYSUPDYMDHMS  = current_timestamp(3)";
			sql  = sql + " where";
			sql  = sql + "     WORKID        = ?";
			sql  = sql + " and HOUSEID       = ?";
			//sql  = sql + " and COLNO         = ?"; // 全列更新なので列№は指定しない
			sql  = sql + " and STARTDATETIME = ?";
			
			
			// 年月日時分秒までの日時フォーマットを準備
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			
			
			int ret = this.jdbcTemplate.update(sql
					,formatter.format(houseWorkStatus.getEndDateTime())
					,houseWorkStatus.getEndEmployeeId()
					,houseWorkStatus.getPercentStart()
					,houseWorkStatus.getPercent()
					,houseWorkStatus.getBiko()
					,userName
					,registPgmId
					,houseWorkStatus.getWorkId()
					,houseWorkStatus.getHouseId()
			//		,houseWorkStatus.getColNo() // 全列更新なので列№は指定しない
					,formatter.format(houseWorkStatus.getStartDateTime())
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
	
	
	
	// データ更新（作業中断）※ハウス内の全列更新
	public boolean updateHalfWayStatusAllCol(HouseWorkStatus houseWorkStatus ,String userName,String registPgmId) {
		
		String pgmId = classId + ".updateHalfWayStatusAllCol";
		log.info("【INF】" + pgmId + ":処理開始");
		
		try {
			
			String sql = " update TT_HOUSE_WORKSTATUS";
			sql  = sql + " set";
			sql  = sql + "     ENDDATETIME   = ?";
			sql  = sql + "    ,ENDEMPLOYEEID = ?";
			sql  = sql + "    ,PERCENT_START = ?";
			sql  = sql + "    ,PERCENT       = ?";
			sql  = sql + "    ,BIKO          = ?";
			sql  = sql + "    ,SYSUPDUSERID  = ?";
			sql  = sql + "    ,SYSUPDPGMID   = ?";
			sql  = sql + "    ,SYSUPDYMDHMS  = current_timestamp(3)";
			sql  = sql + " where";
			sql  = sql + "     WORKID        = ?";
			sql  = sql + " and HOUSEID       = ?";
			//sql  = sql + " and COLNO         = ?"; // 全列更新なので列№は指定しない
			sql  = sql + " and STARTDATETIME = ?";
			
			
			// 年月日時分秒までの日時フォーマットを準備
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			
			
			int ret = this.jdbcTemplate.update(sql
					,formatter.format(houseWorkStatus.getEndDateTime())
					,houseWorkStatus.getEndEmployeeId()
					,houseWorkStatus.getPercentStart()
					,houseWorkStatus.getPercent()
					,houseWorkStatus.getBiko()
					,userName
					,registPgmId
					,houseWorkStatus.getWorkId()
					,houseWorkStatus.getHouseId()
			//		,houseWorkStatus.getColNo() // 全列更新なので列№は指定しない
					,formatter.format(houseWorkStatus.getStartDateTime())
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
