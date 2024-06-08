package com.example.DAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.example.counst.SpecialUser;
import com.example.counst.SpecialWork;
import com.example.dropDownList.DropDownEmployee;
import com.example.dropDownList.DropDownHouse;
import com.example.dropDownList.DropDownWork;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DaoDropDownList {
	
	private JdbcTemplate  jdbcTemplate;
	private String classId = "DaoHouse";
	
	// コンストラクタ
	public DaoDropDownList(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	//------------------------------------------------
	//ハウスのドロップダウンリストの情報を取得
	
	public ArrayList<DropDownHouse> getHouseList() {
		
		String pgmId = classId + ".getHouseList";
		
		try {
			log.info("【INF】" + pgmId + ":処理開始");
			
			String sql = " select ";
			sql  = sql + "     HOUSEID";
			sql  = sql + "    ,HOUSENAME";
			sql  = sql + " from";
			sql  = sql + "     TM_HOUSE";
			sql  = sql + " where";
			sql  = sql + "     DELETEFLG = false";
			sql  = sql + " order by";
			sql  = sql + "     HOUSEID";
			
			// queryForListメソッドでSQLを実行し、結果MapのListで受け取る。
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql);
			
			
			ArrayList<DropDownHouse> houseList = new ArrayList<DropDownHouse>();
			
			// 空のリストを１つ目にセットする
			houseList.add( new DropDownHouse("",""));
			
			
			for (Map<String, Object> rs: rsList) {
				
				houseList.add( new DropDownHouse(
									 rs.get("HOUSEID").toString()
									,rs.get("HOUSENAME").toString()
						)
				);
				
			}
			log.info("【INF】" + pgmId + ":処理終了");
			return houseList;
			
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return null;
		}
	}
	
	
	//------------------------------------------------
	//社員のドロップダウンリストの情報を取得
	
	public ArrayList<DropDownEmployee> getEmployeeList() {
		
		String pgmId = classId + ".getEmployeeList";
		
		try {
			log.info("【INF】" + pgmId + ":処理開始");
			
			String sql = " select ";
			sql  = sql + "     EMPLOYEEID";
			sql  = sql + "    ,EMPLOYEENAME";
			sql  = sql + " from";
			sql  = sql + "     TM_EMPLOYEE";
			sql  = sql + " where";
			sql  = sql + "     DELETEFLG  = false";
			sql  = sql + " and EMPLOYEEID not in (?,?)";
			sql  = sql + " order by";
			sql  = sql + "     EMPLOYEEID";
			
			// queryForListメソッドでSQLを実行し、結果MapのListで受け取る。
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql,SpecialUser.KANRI_USER,SpecialUser.TEST_USER);
			
			
			ArrayList<DropDownEmployee> employeeList = new ArrayList<DropDownEmployee>();
			
			// 空のリストを１つ目にセットする
			employeeList.add( new DropDownEmployee("",""));
			
			
			for (Map<String, Object> rs: rsList) {
				
				employeeList.add( new DropDownEmployee(
									 rs.get("EMPLOYEEID").toString()
									,rs.get("EMPLOYEENAME").toString()
						)
				);
				
			}
			log.info("【INF】" + pgmId + ":処理終了");
			return employeeList;
			
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return null;
		}
	}
	
	
	//------------------------------------------------
	//作業のドロップダウンリストの情報を取得
	
	public ArrayList<DropDownWork> getWorkList() {
		
		String pgmId = classId + ".getWorkList";
		
		try {
			log.info("【INF】" + pgmId + ":処理開始");
			
			String sql = " select ";
			sql  = sql + "     WORKID";
			sql  = sql + "    ,WORKNAME";
			sql  = sql + " from";
			sql  = sql + "     TM_WORK";
			sql  = sql + " where";
			sql  = sql + "     WORKID not in (?)";
			sql  = sql + " order by";
			sql  = sql + "     WORKID";
			
			// queryForListメソッドでSQLを実行し、結果MapのListで受け取る。
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql,SpecialWork.SHUKAKU_SUM);
			
			
			ArrayList<DropDownWork> workList = new ArrayList<DropDownWork>();
			
			// 空のリストを１つ目にセットする
			workList.add( new DropDownWork("",""));
			
			
			for (Map<String, Object> rs: rsList) {
				
				workList.add( new DropDownWork(
									 rs.get("WORKID").toString()
									,rs.get("WORKNAME").toString()
						)
				);
				
			}
			log.info("【INF】" + pgmId + ":処理終了");
			return workList;
			
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return null;
		}
	}
	
	

}
