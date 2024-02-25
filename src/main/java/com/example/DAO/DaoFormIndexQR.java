package com.example.DAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.example.entity.Employee;
import com.example.form.FormIndexQR;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DaoFormIndexQR {
	
	private JdbcTemplate  jdbcTemplate;
	private String classId = "DaoFormIndexQR";
	
	// コンストラクタ
	public DaoFormIndexQR(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	
	
	// 会社で取得
	public FormIndexQR getAllValidEmployee() {
		
		String pgmId = classId + ".getAllValidEmployee";
		
		try {
			log.info("【INF】" + pgmId + ":処理開始");
			
			String sql = " select ";
			sql  = sql + "     EMPLOYEEID";
			sql  = sql + "    ,EMPLOYEENAME";
			sql  = sql + " from";
			sql  = sql + "     TM_EMPLOYEE";
			sql  = sql + " where";
			sql  = sql + "     DELETEFLG = false";
			sql  = sql + " order by";
			sql  = sql + "     EMPLOYEEID";
			
			// queryForListメソッドでSQLを実行し、結果MapのListで受け取る。
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql);
			

			FormIndexQR formIndexQR = new FormIndexQR();
			ArrayList<Employee> wkList = new ArrayList<Employee>();
			
			formIndexQR.setLoginEmployeeId("");
			formIndexQR.setLoginEmployeeName("");
			
			
			for (Map<String, Object> rs: rsList) {
				
				Employee wkrs = new Employee();
				
				wkrs.setEmployeeId(rs.get("EMPLOYEEID").toString());
				wkrs.setEmployeeName(rs.get("EMPLOYEENAME").toString());
				
				log.info("【DBG】" + pgmId + "社員ID=[" + wkrs.getEmployeeId() + "]");
				log.info("【DBG】" + pgmId + "社員名=[" + wkrs.getEmployeeName() + "]");
				
				wkList.add(wkrs);
				
			}
			
			formIndexQR.setEmployeeList(wkList);
			
			log.info("【INF】" + pgmId + ":処理終了");
			return formIndexQR;
			
			
		}catch(Exception e){

			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			//検索した結果を返却します
			return null;
		}
	}
	
	

}
