package com.example.common;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class AggregateRecordCreator {
	
	private JdbcTemplate  jdbcTemplate;
	private String classId = "AggregateRecordCreator";
	
	// コンストラクタ
	public AggregateRecordCreator(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		
	}
	
	
	
	
	// 収穫ケース数集計用のレコードを作成(存在する場合は更新しない)
	public boolean create(String houseId,AggregateTable aggregateTable ,String userName,String registPgmId) {
		
		String pgmId = classId + ".create";
		
		try {
			log.info("【INF】" + pgmId + ":処理開始");
			
			
			for (int index = 0; index < aggregateTable.getList().size(); index++) {
				
				
				
				String sql = " select count(1)";
				sql  = sql + " from";
				sql  = sql + "     TT_SHUKAKU_AGGREGATE";
				sql  = sql + " where";
				sql  = sql + "     HOUSEID        = ?";
				sql  = sql + " and AGGREGATEYEAR  = ?";
				sql  = sql + " and AGGREGATEMONTH = ?";
				
				// queryForListメソッドでSQLを実行
				int count = this.jdbcTemplate.queryForObject(sql
												,Integer.class
												,houseId
												,aggregateTable.getList().get(index).getYear()
												,aggregateTable.getList().get(index).getMonth()
												);
				
				log.info("【INF】" + pgmId + ":ハウス=[" + houseId + "]" 
						+ "年月=[" + aggregateTable.getList().get(index).getYear() + aggregateTable.getList().get(index).getMonth() + "]" 
						+ "件数=[" + Integer.toString(count) + "]");
				
				
				//------------------------------------------------
				// データが存在しなかった場合のみ後続の登録処理を行う
				//------------------------------------------------
				if (count > 0) {
					continue;
				}
				
				
				//------------------------------------------------
				//ハウスマスタから収穫ケース数＿予定を取得
				
				double boxSumYTI = 0.0;
				
				//★文字列結合してどの月の予定を取得するかを指定
				String strBOXSUM_YTI_MONTH = "BOXSUM_YTI_" + aggregateTable.getList().get(index).getMonth();
				
				sql        = " select";
				sql  = sql + "     " + strBOXSUM_YTI_MONTH; 
				sql  = sql + " from";
				sql  = sql + "     TM_HOUSE";
				sql  = sql + " where";
				sql  = sql + "     HOUSEID = ?";
				
				// queryForListメソッドでSQLを実行し、結果MapのListで受け取る。
				List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql,houseId);
				
				for (Map<String, Object> rs: rsList) {
					boxSumYTI = Double.parseDouble(rs.get(strBOXSUM_YTI_MONTH).toString());
				}
				
				
				//------------------------------------------------
				//収穫ケース数集計を登録
				
				int ret = 0;
				
				sql        = " insert into TT_SHUKAKU_AGGREGATE (";
				sql  = sql + "     HOUSEID";
				sql  = sql + "    ,AGGREGATEYEAR";
				sql  = sql + "    ,AGGREGATEMONTH";
				sql  = sql + "    ,BOXSUM";
				sql  = sql + "    ,BOXSUM_YTI";
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
				sql  = sql + "    ,0"; //収穫ケース数合計_実績 0で初期化
				sql  = sql + "    ,?"; //収穫ケース数合計_予定 マスタの値で初期化
				sql  = sql + "    ,?";
				sql  = sql + "    ,?";
				sql  = sql + "    ,current_timestamp(3)";
				sql  = sql + "    ,?";
				sql  = sql + "    ,?";
				sql  = sql + "    ,current_timestamp(3)";
				sql  = sql + " )";
				
				ret = this.jdbcTemplate.update(sql
						,houseId
						,aggregateTable.getList().get(index).getYear()
						,aggregateTable.getList().get(index).getMonth()
						,boxSumYTI
						,userName
						,registPgmId
						,userName
						,registPgmId);
				
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
	
	
	
	
	// 収穫ケース数集計用のレコードを作成(存在する場合は更新を行う)
	public boolean createOrUpdate(String houseId,AggregateTable aggregateTable ,String userName,String registPgmId) {
		
		String pgmId = classId + ".createOrUpdate";
		
		try {
			log.info("【INF】" + pgmId + ":処理開始");
			
			
			for (int index = 0; index < aggregateTable.getList().size(); index++) {
				
				
				
				String sql = " select count(1)";
				sql  = sql + " from";
				sql  = sql + "     TT_SHUKAKU_AGGREGATE";
				sql  = sql + " where";
				sql  = sql + "     HOUSEID        = ?";
				sql  = sql + " and AGGREGATEYEAR  = ?";
				sql  = sql + " and AGGREGATEMONTH = ?";
				
				// queryForListメソッドでSQLを実行
				int count = this.jdbcTemplate.queryForObject(sql
												,Integer.class
												,houseId
												,aggregateTable.getList().get(index).getYear()
												,aggregateTable.getList().get(index).getMonth()
												);
				
				log.info("【INF】" + pgmId + ":ハウス=[" + houseId + "]" 
						+ "年月=[" + aggregateTable.getList().get(index).getYear() + aggregateTable.getList().get(index).getMonth() + "]" 
						+ "件数=[" + Integer.toString(count) + "]");
				
				
				
				
				//------------------------------------------------
				//ハウスマスタから収穫ケース数＿予定を取得
				
				double boxSumYTI = 0.0;
				
				//★文字列結合してどの月の予定を取得するかを指定
				String strBOXSUM_YTI_MONTH = "BOXSUM_YTI_" + aggregateTable.getList().get(index).getMonth();
				
				sql        = " select";
				sql  = sql + "     " + strBOXSUM_YTI_MONTH; 
				sql  = sql + " from";
				sql  = sql + "     TM_HOUSE";
				sql  = sql + " where";
				sql  = sql + "     HOUSEID = ?";
				
				// queryForListメソッドでSQLを実行し、結果MapのListで受け取る。
				List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql,houseId);
				
				for (Map<String, Object> rs: rsList) {
					boxSumYTI = Double.parseDouble(rs.get(strBOXSUM_YTI_MONTH).toString());
				}
				
				int ret = 0;
				
				//------------------------------------------------
				// データが存在しなかった場合は登録処理
				//------------------------------------------------
				if (count == 0) {
					
					
					//------------------------------------------------
					//収穫ケース数集計を登録
					
					sql        = " insert into TT_SHUKAKU_AGGREGATE (";
					sql  = sql + "     HOUSEID";
					sql  = sql + "    ,AGGREGATEYEAR";
					sql  = sql + "    ,AGGREGATEMONTH";
					sql  = sql + "    ,BOXSUM";
					sql  = sql + "    ,BOXSUM_YTI";
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
					sql  = sql + "    ,0"; //収穫ケース数合計_実績 0で初期化
					sql  = sql + "    ,?"; //収穫ケース数合計_予定 マスタの値で初期化
					sql  = sql + "    ,?";
					sql  = sql + "    ,?";
					sql  = sql + "    ,current_timestamp(3)";
					sql  = sql + "    ,?";
					sql  = sql + "    ,?";
					sql  = sql + "    ,current_timestamp(3)";
					sql  = sql + " )";
					
					ret = this.jdbcTemplate.update(sql
							,houseId
							,aggregateTable.getList().get(index).getYear()
							,aggregateTable.getList().get(index).getMonth()
							,boxSumYTI
							,userName
							,registPgmId
							,userName
							,registPgmId);
					
					
				} else {
					
				//------------------------------------------------
				// データが存在する場合は更新処理
				//------------------------------------------------
					
					sql        = " update TT_SHUKAKU_AGGREGATE";
					sql  = sql + " set";
					sql  = sql + "     BOXSUM_YTI     = ?"; //収穫ケース数合計_予定 マスタの値で更新
					sql  = sql + "    ,SYSUPDUSERID   = ?";
					sql  = sql + "    ,SYSUPDPGMID    = ?";
					sql  = sql + "    ,SYSUPDYMDHMS   = current_timestamp(3)";
					sql  = sql + " where";
					sql  = sql + "     HOUSEID        = ?";
					sql  = sql + " and AGGREGATEYEAR  = ?";
					sql  = sql + " and AGGREGATEMONTH = ?";
					
					
					ret = this.jdbcTemplate.update(sql
							,boxSumYTI
							,userName
							,registPgmId
							,houseId
							,aggregateTable.getList().get(index).getYear()
							,aggregateTable.getList().get(index).getMonth()
							);
					
					
					// メモ：commitはjdbcTemplateが自動で行ってくれる
					
					log.info("【INF】" + pgmId + ":処理終了 ret=[" + ret + "]");
					
					
					
				}
				
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
