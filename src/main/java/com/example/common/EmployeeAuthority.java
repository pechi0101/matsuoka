package com.example.common;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmployeeAuthority {
	
	private JdbcTemplate  jdbcTemplate;
	private String classId = "EmployeeAuthority";
	
	// コンストラクタ
	public EmployeeAuthority(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	
	// 操作権限があるか否かを判定
	public boolean IsEditAuthority(String employeeId) {
		
		String pgmId = classId + ".IsEditAuthority";
		
		try {
			
			String sql = " select";
			sql  = sql + "     TM_EMPLOYEE.AUTHORITYKBN";
			sql  = sql + " from";
			sql  = sql + "     TM_EMPLOYEE";
			sql  = sql + " where";
			sql  = sql + "     TM_EMPLOYEE.EMPLOYEEID  = ?";
			sql  = sql + " and TM_EMPLOYEE.DELETEFLG   = False";
			
			
			// queryForListメソッドでSQLを実行し、結果MapのListで受け取る。
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql,employeeId);
			
			
			for (Map<String, Object> rs: rsList) {
				
				// 権限区分
				if (rs.get("AUTHORITYKBN") == null) {
					
					
				} else {
					
					log.info("【INF】" + pgmId + ":ユーザ権限=[" + rs.get("AUTHORITYKBN").toString() + "]");
					
					// 9:特別ユーザ、Z:管理者ユーザである場合、編集を認める
					if (rs.get("AUTHORITYKBN").toString().equals("9") == true
					||  rs.get("AUTHORITYKBN").toString().equals("Z") == true) {
						return true;
					}
				}
				
				// LOOPは１回のみ
				break;
			}
			
			return false;
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了[" + e.toString() + "]");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return false;
		}
	}
	
	
	// 管理者権限があるか否かを判定
	public boolean IsAdministrationAuthority(String employeeId) {
		
		String pgmId = classId + ".IsAdministrationAuthority";
		
		try {
			
			String sql = " select";
			sql  = sql + "     TM_EMPLOYEE.AUTHORITYKBN";
			sql  = sql + " from";
			sql  = sql + "     TM_EMPLOYEE";
			sql  = sql + " where";
			sql  = sql + "     TM_EMPLOYEE.EMPLOYEEID  = ?";
			sql  = sql + " and TM_EMPLOYEE.DELETEFLG   = False";
			
			
			// queryForListメソッドでSQLを実行し、結果MapのListで受け取る。
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql,employeeId);
			
			
			for (Map<String, Object> rs: rsList) {
				
				// 権限区分
				if (rs.get("AUTHORITYKBN") == null) {
					
					
				} else {
					
					log.info("【INF】" + pgmId + ":ユーザ権限=[" + rs.get("AUTHORITYKBN").toString() + "]");
					
					// Z:管理者ユーザである場合、編集を認める
					if (rs.get("AUTHORITYKBN").toString().equals("Z") == true) {
						return true;
					}
				}
				
				// LOOPは１回のみ
				break;
			}
			
			return false;
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了[" + e.toString() + "]");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return false;
		}
	}
	
}
