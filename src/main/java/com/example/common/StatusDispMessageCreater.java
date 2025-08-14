package com.example.common;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StatusDispMessageCreater {
	
	private JdbcTemplate  jdbcTemplate;
	private String classId = "StatusDispMessageCreater";
	
	// コンストラクタ
	public StatusDispMessageCreater(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	
	
	// 勤怠状況の表示メッセージ取得
	public String getClockInOutStatusMsg(String employeeId) {
		
		String pgmId = classId + ".getClockInOutStatusMsg";
		
		try {
			log.info("【INF】" + pgmId + ":処理開始 社員ID=[" + employeeId + "]");
			
			// 直近で「退勤していない」出退勤情報を検索
			String sql = " select";
			sql  = sql + "     DATE_FORMAT(TT_CLO.CLOCKINDATETIME ,'%Y%m%d%H%i%S') CLOCKINDATETIME_STRING";  // YYYYMMDDHHMMSS形式で取得（時間は24時間制）
			sql  = sql + " from";
			sql  = sql + "     TT_CLOCKINOUT TT_CLO";
			sql  = sql + "    ,(";
			sql  = sql + "     select";
			sql  = sql + "         EMPLOYEEID";
			sql  = sql + "        ,MAX(CLOCKINDATETIME) CLOCKINDATETIME";
			sql  = sql + "     from";
			sql  = sql + "         TT_CLOCKINOUT";
			sql  = sql + "     where";
			sql  = sql + "         EMPLOYEEID       = ?";
			sql  = sql + "     and CLOCKOUTDATETIME is null";
			sql  = sql + "     and DELETEFLG        = false";
			sql  = sql + "     group by";
			sql  = sql + "         EMPLOYEEID";
			sql  = sql + "     ) TV_CLO";
			sql  = sql + " where";
			sql  = sql + "     TT_CLO.EMPLOYEEID      = TV_CLO.EMPLOYEEID";
			sql  = sql + " and TT_CLO.CLOCKINDATETIME = TV_CLO.CLOCKINDATETIME";
			
			// queryForListメソッドでSQLを実行し、結果MapのListで受け取る。
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql,employeeId);
			
			String returnMsg = "退勤";
			
			for (Map<String, Object> rs: rsList) {
				
				//出勤日時
				//String clockInDateString = rs.get("CLOCKINDATETIME_STRING").toString();
				
				returnMsg = "出勤中";
				break;
			}
			
			log.info("【DBG】" + pgmId + "返却メッセージ=[" + returnMsg + "]");
			log.info("【INF】" + pgmId + ":処理終了");
			return returnMsg;
			
			
		}catch(Exception e){

			log.error("【ERR】" + pgmId + ":異常終了");
			log.error("【ERR】" + pgmId + ":" + e.getMessage());
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return "";
		}
	}
	
	
	
	// 作業状況の表示メッセージ取得
	public String getWorkStatusMsg(String employeeId) {
		
		String pgmId = classId + ".getClockInOutStatusMsg";
		
		try {
			log.info("【INF】" + pgmId + ":処理開始 社員ID=[" + employeeId + "]");
			
			// 直近で「完了していない」作業情報を検索（収穫も含む）
			// ※作業名の重複はdistinctで除去
			String sql = " select distinct";
			sql  = sql + "     TM_WORK.WORKNAME";
			sql  = sql + " from";
			sql  = sql + "     TT_HOUSE_WORKSTATUS TT_STAT";
			sql  = sql + " inner join TM_WORK";
			sql  = sql + " on";
			sql  = sql + "     TT_STAT.WORKID = TM_WORK.WORKID";
			sql  = sql + " where";
			sql  = sql + "     TT_STAT.STARTEMPLOYEEID = ?";
			sql  = sql + " and TT_STAT.DELETEFLG = false";
			sql  = sql + " and TT_STAT.ENDDATETIME is null";
			sql  = sql + "      union";
			sql  = sql + " select distinct";
			sql  = sql + "     TM_WORK.WORKNAME";
			sql  = sql + " from";
			sql  = sql + "     TT_HOUSE_WORKSTATUS_SHUKAKU TT_STAT";
			sql  = sql + " inner join TM_WORK";
			sql  = sql + " on";
			sql  = sql + "     TT_STAT.WORKID = TM_WORK.WORKID";
			sql  = sql + " where";
			sql  = sql + "     TT_STAT.STARTEMPLOYEEID = ?";
			sql  = sql + " and TT_STAT.DELETEFLG = false";
			sql  = sql + " and TT_STAT.ENDDATETIME is null";
			
			// queryForListメソッドでSQLを実行し、結果MapのListで受け取る。
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql,employeeId,employeeId);
			
			String returnMsg = "";
			
			
			//作業中の作業が存在する場合は以下のようなメッセージを画面に表示する
			//例：作業状況：作業中[誘引]
			//例：作業状況：作業中[誘引、葉かき]
			//例：作業状況：作業中[誘引、葉かき …]
			int index = 0;
			for (Map<String, Object> rs: rsList) {
				
				if (index == 0) {
					returnMsg = "";
				} else if (index == 1) {
					returnMsg = returnMsg + "、";
				} else {
					// ３つ目以降は表示しないでLOOP脱出
					returnMsg = returnMsg + " …";
					break;
				}
				
				returnMsg = returnMsg + rs.get("WORKNAME").toString();
				
				index = index + 1;
			}
			
			//作業中の作業が１件もない場合は以下メッセージ
			//そうでない場合はメッセージのカッコを閉じる
			if (index == 0) {
				returnMsg = "現在作業中の作業なし";
			} else {
				returnMsg = "作業中[" + returnMsg + "]";
			}
			
			
			log.info("【DBG】" + pgmId + "返却メッセージ=[" + returnMsg + "]");
			log.info("【INF】" + pgmId + ":処理終了");
			return returnMsg;
			
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			log.error("【ERR】" + pgmId + ":" + e.getMessage());
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return "";
		}
	}
	
}
