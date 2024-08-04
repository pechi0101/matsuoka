package com.example.batch;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.counst.SpecialUser;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NightBatch {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private String classId = "NightBatch";
	
	@Scheduled(cron = "01 59 00 * * *") // 毎日00時59分01秒に実行
	@Transactional(rollbackFor = {Exception.class, SQLException.class})
	public void runNightBatch() {
		
		
		// ------------------------------------------------
		//【メモ】@Transactionalアノテーション
		//
		// ★★★  コミット、ロールバックの制御  ★★★
		//
		// トランザクションを管理する→つまりMySQLの自動コミットをOFFにする
		// トランザクションはメソッドが正常に終了するまでコミットされない
		// エラーが発生した場合はロールバックされる
		//
		// コミットはこのアノテーションを付けたメソッドは正常終了した際に
		// 自動的に行われる。
		//
		
		
		// バッチ処理開始時の日時
		LocalDateTime nowDateTime =  LocalDateTime.now();
		
		
		log.info("★★★★★★日次バッチ起動開始[" + nowDateTime +"]分★★★★★★★★");
		//System.out.println("★★★★★★■テストバッチ起動★★★★★★★★");
		
		boolean ret = false;
		
		
		
		//------------------------------------------------
		//テストユーザによる作業進捗情報を物理削除
		ret = deleteTestUserWorkStatus();
		
		// 処理異常の際はバッチ処理を終了する
		if (ret == false) {
			return;
		}
		
		
		
		//------------------------------------------------
		//削除済みの作業進捗情報を物理削除
		ret = deleteDeletedWorkStatus();
		
		// 処理異常の際はバッチ処理を終了する
		if (ret == false) {
			return;
		}
		
		
		
		//------------------------------------------------
		//リセット済みの作業進捗情報を移行
		ret = resetWorkStatus(nowDateTime);
		
		// 処理異常の際はバッチ処理を終了する
		if (ret == false) {
			return;
		}
		
		
		
		//------------------------------------------------
		//バックアップから１年経過したデータを削除
		ret = deleteBackUp(nowDateTime);
		
		// 処理異常の際はバッチ処理を終了する
		if (ret == false) {
			return;
		}
		
		
		
		//------------------------------------------------
		//リセット済み作業でリセットから１年経過したデータをバックアップに移行する
		ret = backUpReset(nowDateTime);
		
		
		// 処理異常の際はバッチ処理を終了する
		if (ret == false) {
			return;
		}
		
		
		log.info("★★★★★★日次バッチ起動終了[" + nowDateTime +"]分★★★★★★★★");
		
		
		
		
		// 月初日でない場合はココで日次処理終了。以降は月次バッチ(１日の午前１時頃実行される＝月末日の夜中２５時頃実行される)
		if (nowDateTime.getDayOfMonth() != 1) {
			return;
		}
		
		
		log.info("☆☆☆☆☆☆月次バッチ起動開始[" + nowDateTime +"]分☆☆☆☆☆☆☆☆");
		
		
		log.info("☆☆☆☆☆☆月次バッチ起動終了[" + nowDateTime +"]分☆☆☆☆☆☆☆☆");
		
		
		
		
	}
	
	
	
	private boolean deleteTestUserWorkStatus() {
		
		String pgmId = classId + ".deleteTestUserWorkStatus";
		log.info("【INF】" + pgmId + ":処理開始");
		
		try {
			int ret;
			
			// ------------------------------------------------
			// 作業進捗情報を削除
			
			String sql = " delete from TT_HOUSE_WORKSTATUS";
			sql  = sql + " where";
			sql  = sql + "     STARTEMPLOYEEID = ?";
			
			
			ret = this.jdbcTemplate.update(sql
					,SpecialUser.TEST_USER
					);
			
			// ------------------------------------------------
			// 作業進捗(収穫)情報を削除
			
			sql        = " delete from TT_HOUSE_WORKSTATUS_SHUKAKU";
			sql  = sql + " where";
			sql  = sql + "     STARTEMPLOYEEID = ?";
			
			
			ret = this.jdbcTemplate.update(sql
					,SpecialUser.TEST_USER
					);
			
			// メモ：commitはjdbcTemplateが自動で行ってくれる
			
			log.info("【INF】" + pgmId + ":処理終了 削除件数=[" + ret + "]");
			
			return true;
			
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return false;
		}
	}
	
	
	
	private boolean deleteDeletedWorkStatus() {
		
		String pgmId = classId + ".deleteDeletedWork";
		log.info("【INF】" + pgmId + ":処理開始");
		
		try {
			
			// ------------------------------------------------
			// 作業進捗情報を削除
			
			String sql = " delete from TT_HOUSE_WORKSTATUS";
			sql  = sql + " where";
			sql  = sql + "     DELETEFLG = 1";
			
			
			int ret = this.jdbcTemplate.update(sql);
			
			// メモ：commitはjdbcTemplateが自動で行ってくれる
			
			log.info("【INF】" + pgmId + ":処理終了 削除件数=[" + ret + "]");
			
			return true;
			
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return false;
		}
	}
	
	
	
	private boolean resetWorkStatus(LocalDateTime nowDateTime) {
		
		String pgmId = classId + ".resetWorkStatus";
		log.info("【INF】" + pgmId + ":処理開始");
		
		try {
			//------------------------------------------------
			//■以下を「ハウス／作業」毎に調査しリセット処理を実施。
			//
			//  ハウス    列     終了日時 
			// ハウス１  列01   20XX/XX/XX
			// ハウス１  列02   
			// ハウス１  列03   
			// ハウス１  列04   20XX/XX/XX
			//		        ▼
			// ハウス→列→作業進捗と結合して
			// どれか１つでも作業終了日時が
			// 入ってない列があったら
			// リセット対象"外"とする
			//		        ▼
			// リセット対象である場合、列の中で作業終了日時
			// のMAXと現在日時との間隔がｎ日である場合
			// リセットを行う。
			//------------------------------------------------
			
			
			
			// ハウス・作業マスタをクロス結合検索
			String sql = " select";
			sql  = sql + "     HOUSE.HOUSEID";
			sql  = sql + "    ,WORK.WORKID";
			sql  = sql + "    ,WORK.RESET_SPAN";
			sql  = sql + " from";
			sql  = sql + "     TM_HOUSE HOUSE";
			sql  = sql + " cross join";
			sql  = sql + "     TM_WORK WORK";
			sql  = sql + " order by";
			sql  = sql + "     HOUSE.HOUSEID";
			sql  = sql + "    ,WORK.WORKID";
			
			// queryForListメソッドでSQLを実行し、結果MapのListで受け取る。
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql);
			
			for (Map<String, Object> rs: rsList) {
				
				log.info("【INF】" + pgmId + "■------------------------------------------------");
				log.info("【INF】" + pgmId + "■ハウスID    =[" + rs.get("HOUSEID").toString() + "]");
				log.info("【INF】" + pgmId + "■作業ID      =[" + rs.get("WORKID").toString() + "]");
				log.info("【INF】" + pgmId + "■リセット間隔=[" + rs.get("RESET_SPAN").toString() + "]日");
				
				String houseId   = rs.get("HOUSEID").toString();
				String workId    = rs.get("WORKID").toString();
				String resetSpan = rs.get("RESET_SPAN").toString();
				
				//------------------------------------------------
				// 対象のハウス、作業がリセット対象であるかをチェック
				boolean resetExec = this.isResetExec(houseId,workId);
				
				// リセット対象外である場合は次のLOOPへ
				if (resetExec == false) {
					continue;
				}
				
				
				//------------------------------------------------
				// リセットするか否かを判断するための作業終了日時を取得
				// ※戻り値はYYYYMMDDHHMISS型の文字列
				String endDatetimeString = getEndDateTimeString(houseId,workId);
				
				
				
				//------------------------------------------------
				// 作業終了日時と現在の日付を比較してリセット対象であるかをチェック
				
				// 年月日時分秒までの日時フォーマットを準備
				DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
				LocalDateTime endDatetime  = LocalDateTime.parse(endDatetimeString,formatterDateTime);
				
				// 検査対象の作業終了日時＋リセット間隔 n日 ＞ 現在日時である場合、リセット対象とする
				//
				//     endDatetime ：検査対象の作業終了日時
				//     nowDateTime ：バッチ処理開始時の日時(現在日時)
				//     resetSpan   ：リセット間隔 n日
				//
				log.info("【INF】" + pgmId + "■作業終了日時=[" + endDatetime + "]日");
				log.info("【INF】" + pgmId + "■現在の  日時=[" + nowDateTime + "]");
				log.info("【INF】" + pgmId + "■リセット間隔=[" + resetSpan + "]日");
				
				// AAAA.isBefore(BBBB) →AAAAはBBBBより前の日付ですか？
				resetExec = endDatetime.plusDays(Integer.parseInt(resetSpan)).isBefore(nowDateTime);
				log.info("【INF】" + pgmId + "■リセット対象=[" + resetExec + "]");
				
				// リセット対象外である場合は次のLOOPへ
				if (resetExec == false) {
					continue;
				}
				
				
				//------------------------------------------------
				// リセットを実施
				Boolean ret  = execReset(houseId, workId, nowDateTime);
				
				// リセット処理が異常終了した場合は処理終了
				if (ret == false) {
					return false;
				}
				
				
			}
			
			
			return true;
			
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return false;
		}
	}
	
	
	
	private boolean isResetExec(String houseId,String workId) {
		
		String pgmId = classId + ".isResetExec";
		log.info("【INF】" + pgmId + ":処理開始");
		
		try {
			
			String sql = " select";
			sql  = sql + "     HOUSE.HOUSEID";
			sql  = sql + "    ,HOUSE.HOUSENAME";
			sql  = sql + "    ,COL.COLNO";
			sql  = sql + "    ,DATE_FORMAT(WORK.ENDDATETIME,'%Y%m%d%H%i%S') ENDDATETIME_STRING";
			sql  = sql + " from";
			sql  = sql + "     TM_HOUSE HOUSE";
			sql  = sql + " inner join";
			sql  = sql + "     TM_HOUSECOL COL";
			sql  = sql + "     on  HOUSE.HOUSEID      = COL.HOUSEID";
			sql  = sql + " left join";
			sql  = sql + "     TT_HOUSE_WORKSTATUS WORK";
			sql  = sql + "     on  WORK.HOUSEID       = COL.HOUSEID";
			sql  = sql + "     and WORK.COLNO         = COL.COLNO";
			sql  = sql + "     and WORK.WORKID        = ?";
			sql  = sql + "     and WORK.STARTDATETIME = (select  MAX(STARTDATETIME)";
			sql  = sql + "                               from    TT_HOUSE_WORKSTATUS";
			sql  = sql + "                               where   HOUSEID = WORK.HOUSEID";
			sql  = sql + "                               and     COLNO   = WORK.COLNO";
			sql  = sql + "                               and     WORKID  = WORK.WORKID";
			sql  = sql + "                               )";
			sql  = sql + " where";
			sql  = sql + "     HOUSE.HOUSEID  = ?";
			
			
			// queryForListメソッドでSQLを実行し、結果MapのListで受け取る。
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql,workId,houseId);
			
			for (Map<String, Object> rs: rsList) {
				
				log.info("【INF】" + pgmId + "□------------------------------------------------");
				log.info("【INF】" + pgmId + "□列No        =[" + rs.get("COLNO").toString() + "]");
				log.info("【INF】" + pgmId + "□作業終了日時=[" + rs.get("ENDDATETIME_STRING") + "]");
				
				
				// 作業終了日時が１つでのセットさせてない場合はリセット対象外
				if (rs.get("ENDDATETIME_STRING") == null) {
					return false;
				}
			}
			
			
			return true;
			
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return false;
		}
	}
	
	
	// 戻り値はYYYYMMDDHHMISS型の文字列
	private String getEndDateTimeString(String houseId,String workId) {
		
		String pgmId = classId + ".getEndDateTime";
		log.info("【INF】" + pgmId + ":処理開始");
		
		try {
			
			//★TT_HOUSE_WORKSTATUSの下記データは事前処理で　物理削除済み　であるため下記SQLの検索条件で問わない
			//       ・論理削除済みデータ
			//       ・テストユーザが開始したデータ
			
			//【注意】
			// isResetExecとgetEndDateTimeStringのメソッドはSQLの条件が同じであるため片方を直したらもう片方も直すこと！
			
			
			String sql = " select";
			sql  = sql + "     DATE_FORMAT(   MAX(WORK.ENDDATETIME)  ,'%Y%m%d%H%i%S') ENDDATETIME_STRING";
			sql  = sql + " from";
			sql  = sql + "     TM_HOUSE HOUSE";
			sql  = sql + " inner join";
			sql  = sql + "     TM_HOUSECOL COL";
			sql  = sql + "     on  HOUSE.HOUSEID      = COL.HOUSEID";
			sql  = sql + " left join";
			sql  = sql + "     TT_HOUSE_WORKSTATUS WORK";
			sql  = sql + "     on  WORK.HOUSEID       = COL.HOUSEID";
			sql  = sql + "     and WORK.COLNO         = COL.COLNO";
			sql  = sql + "     and WORK.WORKID        = ?";
			sql  = sql + "     and WORK.STARTDATETIME = (select  MAX(STARTDATETIME)";
			sql  = sql + "                               from    TT_HOUSE_WORKSTATUS";
			sql  = sql + "                               where   HOUSEID = WORK.HOUSEID";
			sql  = sql + "                               and     COLNO   = WORK.COLNO";
			sql  = sql + "                               and     WORKID  = WORK.WORKID";
			sql  = sql + "                               )";
			sql  = sql + " where";
			sql  = sql + "     HOUSE.HOUSEID  = ?";
			
			
			// queryForListメソッドでSQLを実行し、結果MapのListで受け取る。
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql,workId,houseId);
			
			String endDateTime = "";
			
			for (Map<String, Object> rs: rsList) {
				
				log.info("【INF】" + pgmId + "★作業終了日時=[" + rs.get("ENDDATETIME_STRING").toString() + "]");
				
				
				endDateTime = rs.get("ENDDATETIME_STRING").toString();
			}
			
			//ありえないが念のためチェック
			if ("".equals(endDateTime) == true) {
				log.info("【ERR】" + pgmId + "作業終了日時がNULLです");
			}
			
			return endDateTime;
			
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return "";
		}
	}
	
	
	
	private boolean execReset(String houseId,String workId,LocalDateTime nowDateTime) {
		
		String pgmId = classId + ".execReset";
		log.info("【INF】" + pgmId + ":処理開始");
		
		try {
			
			
			// ------------------------------------------------
			// 作業進捗情報をリセットテーブルに移行
			
			
			String sql = " insert into TT_HOUSE_WORKSTATUS_RESET";
			sql  = sql + " (";
			sql  = sql + "     HOUSEID";
			sql  = sql + "    ,COLNO";
			sql  = sql + "    ,WORKID";
			sql  = sql + "    ,STARTDATETIME";
			sql  = sql + "    ,STARTEMPLOYEEID";
			sql  = sql + "    ,ENDDATETIME";
			sql  = sql + "    ,ENDEMPLOYEEID";
			sql  = sql + "    ,PERCENT";
			sql  = sql + "    ,RESETYMDHMS";
			sql  = sql + "    ,BIKO";
			sql  = sql + "    ,SYSREGUSERID";
			sql  = sql + "    ,SYSREGPGMID";
			sql  = sql + "    ,SYSREGYMDHMS";
			sql  = sql + "    ,SYSUPDUSERID";
			sql  = sql + "    ,SYSUPDPGMID";
			sql  = sql + "    ,SYSUPDYMDHMS";
			sql  = sql + " )";
			sql  = sql + " select";
			sql  = sql + "     HOUSEID";
			sql  = sql + "    ,COLNO";
			sql  = sql + "    ,WORKID";
			sql  = sql + "    ,STARTDATETIME";
			sql  = sql + "    ,STARTEMPLOYEEID";
			sql  = sql + "    ,ENDDATETIME";
			sql  = sql + "    ,ENDEMPLOYEEID";
			sql  = sql + "    ,PERCENT";
			sql  = sql + "    ,?";
			sql  = sql + "    ,BIKO";
			sql  = sql + "    ,SYSREGUSERID";
			sql  = sql + "    ,SYSREGPGMID";
			sql  = sql + "    ,SYSREGYMDHMS";
			sql  = sql + "    ,SYSUPDUSERID";
			sql  = sql + "    ,SYSUPDPGMID";
			sql  = sql + "    ,SYSUPDYMDHMS";
			sql  = sql + " from";
			sql  = sql + "     TT_HOUSE_WORKSTATUS";
			sql  = sql + " where";
			sql  = sql + "     HOUSEID   = ?";
			sql  = sql + " and WORKID    = ?";
			
			
			int ret = this.jdbcTemplate.update(sql
					,nowDateTime
					,houseId
					,workId
					);
			
			log.info("【INF】" + pgmId + ":処理終了 登録件数=[" + ret + "]件");
			
			
			// ------------------------------------------------
			// 移行した作業進捗情報を削除
			
			sql        = " delete from TT_HOUSE_WORKSTATUS";
			sql  = sql + " where";
			sql  = sql + "     HOUSEID = ?";
			sql  = sql + " and WORKID  = ?";
			
			
			ret = this.jdbcTemplate.update(sql
					,houseId
					,workId
					);
			
			// メモ：commitはjdbcTemplateが自動で行ってくれる
			
			log.info("【INF】" + pgmId + ":処理終了 削除件数=[" + ret + "]件");
			
			return true;
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	
	
	private boolean deleteBackUp(LocalDateTime nowDateTime) {
		
		String pgmId = classId + ".deleteBackUp";
		log.info("【INF】" + pgmId + ":処理開始");
		
		try {
			
			// ------------------------------------------------
			// バックアップから２年が経過した作業進捗情報バックアップを削除
			
			String sql = " delete from TT_HOUSE_WORKSTATUS_BACKUP";
			sql  = sql + " where";
			sql  = sql + "     BACKUPYMDHMS < DATE_SUB(?, INTERVAL 2 YEAR)";  // DATE_SUB：日付から日数を引くための関数
			
			
			int ret = this.jdbcTemplate.update(sql
											  ,nowDateTime
											  );
			
			// メモ：commitはjdbcTemplateが自動で行ってくれる
			
			log.info("【INF】" + pgmId + ":処理終了 削除件数=[" + ret + "]");
			
			return true;
			
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return false;
		}
	}
	
	
	
	private boolean backUpReset(LocalDateTime nowDateTime) {
		
		String pgmId = classId + ".execReset";
		log.info("【INF】" + pgmId + ":処理開始");
		
		try {
			
			
			// ------------------------------------------------
			// 作業進捗情報をリセットテーブルに移行
			
			
			String sql = " insert into TT_HOUSE_WORKSTATUS_BACKUP";
			sql  = sql + " (";
			sql  = sql + "     HOUSEID";
			sql  = sql + "    ,COLNO";
			sql  = sql + "    ,WORKID";
			sql  = sql + "    ,STARTDATETIME";
			sql  = sql + "    ,STARTEMPLOYEEID";
			sql  = sql + "    ,ENDDATETIME";
			sql  = sql + "    ,ENDEMPLOYEEID";
			sql  = sql + "    ,PERCENT";
			sql  = sql + "    ,RESETYMDHMS";
			sql  = sql + "    ,BACKUPYMDHMS";
			sql  = sql + "    ,BIKO";
			sql  = sql + "    ,SYSREGUSERID";
			sql  = sql + "    ,SYSREGPGMID";
			sql  = sql + "    ,SYSREGYMDHMS";
			sql  = sql + "    ,SYSUPDUSERID";
			sql  = sql + "    ,SYSUPDPGMID";
			sql  = sql + "    ,SYSUPDYMDHMS";
			sql  = sql + " )";
			sql  = sql + " select";
			sql  = sql + "     HOUSEID";
			sql  = sql + "    ,COLNO";
			sql  = sql + "    ,WORKID";
			sql  = sql + "    ,STARTDATETIME";
			sql  = sql + "    ,STARTEMPLOYEEID";
			sql  = sql + "    ,ENDDATETIME";
			sql  = sql + "    ,ENDEMPLOYEEID";
			sql  = sql + "    ,PERCENT";
			sql  = sql + "    ,RESETYMDHMS";
			sql  = sql + "    ,?";
			sql  = sql + "    ,BIKO";
			sql  = sql + "    ,SYSREGUSERID";
			sql  = sql + "    ,SYSREGPGMID";
			sql  = sql + "    ,SYSREGYMDHMS";
			sql  = sql + "    ,SYSUPDUSERID";
			sql  = sql + "    ,SYSUPDPGMID";
			sql  = sql + "    ,SYSUPDYMDHMS";
			sql  = sql + " from";
			sql  = sql + "     TT_HOUSE_WORKSTATUS_RESET";
			sql  = sql + " where";
			sql  = sql + "     RESETYMDHMS < DATE_SUB(?, INTERVAL 1 YEAR)";
			
			
			int ret = this.jdbcTemplate.update(sql
					,nowDateTime
					,nowDateTime
					);
			
			log.info("【INF】" + pgmId + ":処理終了 登録件数=[" + ret + "]件");
			
			
			// ------------------------------------------------
			// 移行したリセット情報を削除
			
			sql        = " delete from TT_HOUSE_WORKSTATUS_RESET";
			sql  = sql + " where";
			sql  = sql + "     RESETYMDHMS < DATE_SUB(?, INTERVAL 1 YEAR)";
			
			
			ret = this.jdbcTemplate.update(sql
					,nowDateTime
					);
			
			// メモ：commitはjdbcTemplateが自動で行ってくれる
			
			log.info("【INF】" + pgmId + ":処理終了 削除件数=[" + ret + "]件");
			
			return true;
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
}
