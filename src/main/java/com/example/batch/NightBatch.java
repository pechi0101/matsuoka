package com.example.batch;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.common.AggregateRecordCreator;
import com.example.common.AggregateTable;
import com.example.counst.SpecialUser;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NightBatch {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private String classId = "NightBatch";
	
	@Scheduled(cron = "45 59 00 * * *") // 毎日00時59分45秒に実行
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
		ret = this.deleteTestUserWorkStatus();
		
		// 処理異常の際はバッチ処理を終了する
		if (ret == false) {
			return;
		}
		
		
		
		//------------------------------------------------
		//テストユーザによる作業進捗情報(収穫)を物理削除
		ret = this.deleteTestUserWorkStatusShukaku();
		
		// 処理異常の際はバッチ処理を終了する
		if (ret == false) {
			return;
		}
		
		
		
		//------------------------------------------------
		//テストユーザによる出退勤情報を物理削除
		ret = this.deleteTestUserDeletedClockInOut();
		
		// 処理異常の際はバッチ処理を終了する
		if (ret == false) {
			return;
		}
		
		
		
		//------------------------------------------------
		//削除済みの作業進捗情報を物理削除
		ret = this.deleteDeletedWorkStatus();
		
		// 処理異常の際はバッチ処理を終了する
		if (ret == false) {
			return;
		}
		
		
		
		//------------------------------------------------
		//削除済みの作業進捗情報(収穫)を物理削除
		ret = this.deleteDeletedWorkStatusShukaku();
		
		// 処理異常の際はバッチ処理を終了する
		if (ret == false) {
			return;
		}
		
		
		
		//------------------------------------------------
		//削除済みの出退勤情報を物理削除
		ret = this.deleteDeletedClockInOut();
		
		// 処理異常の際はバッチ処理を終了する
		if (ret == false) {
			return;
		}
		
		
		
		//------------------------------------------------
		//リセット済みの作業進捗情報を移行
		ret = this.resetWorkStatus(nowDateTime);
		
		// 処理異常の際はバッチ処理を終了する
		if (ret == false) {
			return;
		}
		
		
		
		//------------------------------------------------
		//バックアップから１年経過したデータを削除
		ret = this.deleteBackUp(nowDateTime);
		
		// 処理異常の際はバッチ処理を終了する
		if (ret == false) {
			return;
		}
		
		
		
		//------------------------------------------------
		//リセット済み作業でリセットから１年経過したデータをバックアップに移行する
		ret = this.backUpReset(nowDateTime);
		
		// 処理異常の際はバッチ処理を終了する
		if (ret == false) {
			return;
		}
		
		
		
		
		
		//------------------------------------------------
		// 収穫ケース数集計用年月テーブル作成（本年度のデータ）
		LocalDate nowDate = LocalDate.now();
		AggregateTable aggregateTableNowYear = new AggregateTable(jdbcTemplate, nowDate);
		
		
		
		//------------------------------------------------
		// 収穫ケース数集計用年月テーブル作成（前年度のデータ）
		
		LocalDate prvDate = LocalDate.now().minusYears(1);
		AggregateTable aggregateTablePrvYear = new AggregateTable(jdbcTemplate, prvDate);
		
		
		
		//------------------------------------------------
		// 前年度・前年度収穫ケース数集計データ作成  ※集計用のレコードを作成するだけ。収穫ケース数合計は0で登録される。
		ret = this.createAggregateData(aggregateTableNowYear,aggregateTablePrvYear);
		
		// 処理異常の際はバッチ処理を終了する
		if (ret == false) {
			return;
		}
		
		
		
		//------------------------------------------------
		// 前年度・前年度収穫ケース数集計
		ret = this.aggregate(aggregateTableNowYear,aggregateTablePrvYear);
		
		// 処理異常の際はバッチ処理を終了する
		if (ret == false) {
			return;
		}
		
		
		
		
		
		
		
		log.info("★★★★★★日次バッチ起動終了[" + nowDateTime +"]分★★★★★★★★");
		
		
		
		
		// 月初日でない場合はココで日次処理終了。以降は月次バッチ(１日の午前１時頃実行される＝月末日の夜中２５時頃実行される)
		if (nowDateTime.getDayOfMonth() != 1) {
			log.info("☆月初日ではないため月次処理葉実施しない☆");
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
	
	
	
	private boolean deleteTestUserWorkStatusShukaku() {
		
		String pgmId = classId + ".deleteTestUserWorkStatusShukaku";
		log.info("【INF】" + pgmId + ":処理開始");
		
		try {
			int ret;
			
			// ------------------------------------------------
			// 作業進捗(収穫)情報を削除
			
			String sql = " delete from TT_HOUSE_WORKSTATUS_SHUKAKU";
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
	
	
	
	private boolean deleteTestUserDeletedClockInOut() {
		
		String pgmId = classId + ".deleteTestUserDeletedClockInOut";
		log.info("【INF】" + pgmId + ":処理開始");
		
		try {
			
			// ------------------------------------------------
			// 出退勤情報を削除
			
			String sql = " delete from TT_CLOCKINOUT";
			sql  = sql + " where";
			sql  = sql + "     EMPLOYEEID = ?";
			
			
			int ret = this.jdbcTemplate.update(sql,SpecialUser.TEST_USER);
			
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
		
		String pgmId = classId + ".deleteDeletedWorkStatus";
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
	
	
	
	private boolean deleteDeletedWorkStatusShukaku() {
		
		String pgmId = classId + ".deleteDeletedWorkStatusShukaku";
		log.info("【INF】" + pgmId + ":処理開始");
		
		try {
			
			// ------------------------------------------------
			// 作業進捗情報を削除
			
			String sql = " delete from TT_HOUSE_WORKSTATUS_SHUKAKU";
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
	
	
	
	private boolean deleteDeletedClockInOut() {
		
		String pgmId = classId + ".deleteDeletedClockInOut";
		log.info("【INF】" + pgmId + ":処理開始");
		
		try {
			
			// ------------------------------------------------
			// 出退勤情報を削除
			
			String sql = " delete from TT_CLOCKINOUT";
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
			// ハウス１  列01   20XX/XX/XX 100%
			// ハウス１  列02   
			// ハウス１  列03   
			// ハウス１  列04   20XX/XX/XX  80%
			//		        ▼
			// ハウス→列→作業進捗と結合して
			// どれか１つでも作業終了日時が
			// 入ってない、又は進捗率100%でない列があったら
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
			//
			// 指定のハウス・作業に対して列ごとに作業開始日が最終(MAX)の作業状況を検索
			//
			
			//【注意】
			// isResetExecメソッドとgetEndDateTimeStringメソッドはSQLの条件が同じであるため片方を直したらもう片方も直すこと！
			
			
			String sql = " select";
			sql  = sql + "     HOUSE.HOUSEID";
			sql  = sql + "    ,HOUSE.HOUSENAME";
			sql  = sql + "    ,COL.COLNO";
			sql  = sql + "    ,DATE_FORMAT(WORK.ENDDATETIME,'%Y%m%d%H%i%S') ENDDATETIME_STRING";
			sql  = sql + "    ,WORK.PERCENT"; //進捗率_終了
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
				
				
				// 指定のハウス、作業において作業終了日時が１列でもセットさせてない場合はリセット対象外( 「作業中」の作業があるため )
				if (rs.get("ENDDATETIME_STRING") == null) {
					return false;
				}
				
				// 指定のハウス、作業において進捗率_終了が１列でも100%でない場合はリセット対象外( 「作業を中断」して100%完了してない作業があるため )
				if (Integer.parseInt(rs.get("PERCENT").toString()) != 100) {
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
			// isResetExecメソッドとgetEndDateTimeStringメソッドはSQLの条件が同じであるため片方を直したらもう片方も直すこと！
			
			//
			// 指定のハウス・作業に対して全ての列の中で MAX の作業終了日時を取得
			//                                 ▼
			//                                 ▼
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
			sql  = sql + "    ,PERCENT_START";
			sql  = sql + "    ,PERCENT";
			sql  = sql + "    ,FORCE_RESETFLG";
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
			sql  = sql + "    ,PERCENT_START";
			sql  = sql + "    ,PERCENT";
			sql  = sql + "    ,?";             // FORCE_RESETFLG ※強制リセットフラグ
			sql  = sql + "    ,?";             // RESETYMDHMS
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
					,false  // 強制リセットフラグ
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
			sql  = sql + "    ,PERCENT_START";
			sql  = sql + "    ,PERCENT";
			sql  = sql + "    ,FORCE_RESETFLG";
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
			sql  = sql + "    ,PERCENT_START";
			sql  = sql + "    ,PERCENT";
			sql  = sql + "    ,FORCE_RESETFLG";
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
	
	
	
	private boolean createAggregateData(AggregateTable aggregateTableNowYear,AggregateTable aggregateTablePrvYear) {
		
		String pgmId = classId + ".createAggregateData";
		log.info("【INF】" + pgmId + ":処理開始");
		
		try {
			String sql = " select";
			sql  = sql + "     HOUSEID";
			sql  = sql + " from";
			sql  = sql + "     TM_HOUSE";
			
			
			// queryForListメソッドでSQLを実行し、結果MapのListで受け取る。
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql);
			
			AggregateRecordCreator aggregateRecordCreator = new AggregateRecordCreator(jdbcTemplate);
			
			for (Map<String, Object> rs: rsList) {
				
				String houseId = rs.get("HOUSEID").toString();
				
				aggregateRecordCreator.createOrUpdate(houseId, aggregateTableNowYear, SpecialUser.KANRI_USER, "NightBatch");
				aggregateRecordCreator.create(        houseId, aggregateTablePrvYear, SpecialUser.KANRI_USER, "NightBatch");
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
	
	
	private boolean aggregate(AggregateTable aggregateTableNowYear,AggregateTable aggregateTablePrvYear) {
		
		String pgmId = classId + ".aggregate";
		log.info("【INF】" + pgmId + ":処理開始");
		
		try {
			
			// 昨年度の最初の年月
			String firtYM  =  aggregateTablePrvYear.getList().get(0).getYear()
							+ aggregateTablePrvYear.getList().get(0).getMonth();
			// 今年度の最後の年月
			String lastYM  =  aggregateTableNowYear.getList().get(aggregateTableNowYear.getList().size() - 1).getYear()
							+ aggregateTableNowYear.getList().get(aggregateTableNowYear.getList().size() - 1).getMonth();
			
			
			// ハウスマスタを検索
			String sql = " select";
			sql  = sql + "     HOUSE.HOUSEID";
			sql  = sql + "    ,AGGRE.AGGREGATEYEAR";
			sql  = sql + "    ,AGGRE.AGGREGATEMONTH";
			sql  = sql + " from";
			sql  = sql + "     TM_HOUSE HOUSE";
			sql  = sql + " inner join";
			sql  = sql + "     TT_SHUKAKU_AGGREGATE AGGRE";
			sql  = sql + "     on";
			sql  = sql + "         AGGRE.HOUSEID = HOUSE.HOUSEID";
			sql  = sql + "     and concat(AGGRE.AGGREGATEYEAR, AGGRE.AGGREGATEMONTH) >= ?";
			sql  = sql + "     and concat(AGGRE.AGGREGATEYEAR, AGGRE.AGGREGATEMONTH) <= ?";
			sql  = sql + " order by";
			sql  = sql + "     HOUSE.HOUSEID";
			sql  = sql + "    ,AGGRE.AGGREGATEYEAR";
			sql  = sql + "    ,AGGRE.AGGREGATEMONTH";
			
			// queryForListメソッドでSQLを実行し、結果MapのListで受け取る。
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql, firtYM, lastYM);
			
			for (Map<String, Object> rs: rsList) {
				
				log.info("【INF】" + pgmId + "■------------------------------------------------");
				log.info("【INF】" + pgmId + "■ハウスID    =[" + rs.get("HOUSEID").toString() + "]");
				log.info("【INF】" + pgmId + "■集計年      =[" + rs.get("AGGREGATEYEAR").toString() + "]年");
				log.info("【INF】" + pgmId + "■集計月      =[" + rs.get("AGGREGATEMONTH").toString() + "]月");
				
				String houseId     = rs.get("HOUSEID").toString();
				String targetYear  = rs.get("AGGREGATEYEAR").toString();
				String targetMonth = rs.get("AGGREGATEMONTH").toString();
				
				//------------------------------------------------
				// 集計・更新を実施
				Boolean ret  = updateAggregate(houseId ,targetYear, targetMonth ,SpecialUser.KANRI_USER,"NightBatch");
				
				// 集計・更新が異常終了した場合は処理終了
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
	
	
	
	// データ更新
	public boolean updateAggregate(String houseId ,String targetYear, String targetMonth ,String userName,String registPgmId) {
		
		String pgmId = classId + ".updateAggregate";
		log.info("【INF】" + pgmId + ":処理開始");
		
		try {
			
			// 最初の日を取得
			LocalDate firstDay  = LocalDate.of(Integer.parseInt(targetYear), Integer.parseInt(targetMonth), 1);
			
			// 最後の日を取得
			YearMonth yearMonth = YearMonth.of(Integer.parseInt(targetYear), Integer.parseInt(targetMonth));
			LocalDate lastDay   = yearMonth.atEndOfMonth();
			
			
			// 集計した結果で更新を行う
			String sql = " update TT_SHUKAKU_AGGREGATE";
			sql  = sql + "     set";
			sql  = sql + "     BOXSUM =(select COALESCE(SUM(BOXSUM),0)";  //集計データが存在しない場合は収穫ケース数合計を 0 で更新
			sql  = sql + "              from   TT_SHUKAKU_BOXSUM";
			sql  = sql + "              where  HOUSEID      = ?";
			sql  = sql + "              and    SHUKAKUDATE >= ?";
			sql  = sql + "              and    SHUKAKUDATE <= ?";
			sql  = sql + "              and    DELETEFLG    = false"; //削除された情報は集計対象外
			sql  = sql + "             )";
			sql  = sql + "    ,SYSUPDUSERID  = ?";
			sql  = sql + "    ,SYSUPDPGMID   = ?";
			sql  = sql + "    ,SYSUPDYMDHMS  = current_timestamp(3)";
			sql  = sql + " where";
			sql  = sql + "     HOUSEID        = ?";
			sql  = sql + " and AGGREGATEYEAR  = ?";
			sql  = sql + " and AGGREGATEMONTH = ?";
			
			
			// 年月日での日付フォーマットを準備
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			
			
			int ret = this.jdbcTemplate.update(sql
					,houseId
					,formatter.format(firstDay)
					,formatter.format(lastDay)
					,userName
					,registPgmId
					,houseId
					,targetYear
					,targetMonth
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
