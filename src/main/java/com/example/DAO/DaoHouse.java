package com.example.DAO;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DaoHouse {
	
	private JdbcTemplate  jdbcTemplate;
	private String classId = "DaoHouse";
	
	// コンストラクタ
	public DaoHouse(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	
	
	// IDに対する名称を取得
	public String getNameFromId(String houseId) {
		
		String pgmId = classId + ".getNameFromId";
		
		try {
			log.info("【INF】" + pgmId + ":処理開始 ID=[" + houseId + "]");
			
			String sql = " select ";
			sql  = sql + "     HOUSENAME";
			sql  = sql + " from";
			sql  = sql + "     TM_HOUSE";
			sql  = sql + " where";
			sql  = sql + "     HOUSEID   = ?";
			sql  = sql + " and DELETEFLG = false";
			
			// queryForListメソッドでSQLを実行し、結果MapのListで受け取る。
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql,houseId);
			
			String houseName = "";
			
			for (Map<String, Object> rs: rsList) {
				
				houseName = rs.get("HOUSENAME").toString();
			}

			log.info("【DBG】" + pgmId + "名称=[" + houseName + "]");
			log.info("【INF】" + pgmId + ":処理終了");
			return houseName;
			
			
		}catch(Exception e){

			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return "";
		}
	}
	
	

}
