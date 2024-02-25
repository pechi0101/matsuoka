package com.example.DAO;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DaoWork {
	
	private JdbcTemplate  jdbcTemplate;
	private String classId = "DaoWork";
	
	// コンストラクタ
	public DaoWork(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	
	
	// IDに対する名称を取得
	public String getNameFromId(String workId) {
		
		String pgmId = classId + ".getNameFromId";
		
		try {
			log.info("【INF】" + pgmId + ":処理開始 ID=[" + workId + "]");
			
			String sql = " select ";
			sql  = sql + "     WORKNAME";
			sql  = sql + " from";
			sql  = sql + "     TM_WORK";
			sql  = sql + " where";
			sql  = sql + "     WORKID   = ?";
			
			// queryForListメソッドでSQLを実行し、結果MapのListで受け取る。
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql,workId);
			
			String workName = "";
			
			for (Map<String, Object> rs: rsList) {
				
				workName = rs.get("WORKNAME").toString();
			}
			
			log.info("【DBG】" + pgmId + "名称=[" + workName + "]");
			log.info("【INF】" + pgmId + ":処理終了");
			return workName;
			
			
		}catch(Exception e){

			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return "";
		}
	}
	
	

}
