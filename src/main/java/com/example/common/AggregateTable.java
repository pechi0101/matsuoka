package com.example.common;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class AggregateTable {
	
	public ArrayList<AggregateDetail> list = null;
	
	
	private JdbcTemplate  jdbcTemplate;
	private String classId = "AggregateTable";
	
	// コンストラクタ
	public AggregateTable(JdbcTemplate jdbcTemplate,LocalDate baseDate) {
		String pgmId = classId + ".NEW";
		
		log.info("【INF】" + pgmId + ":処理開始");
		
		this.jdbcTemplate = jdbcTemplate;
		
		
		// ■集計初月が09月である場合( →09月がリストの先頭になる )
		//
		//	    基準年月
		//	      ▼     |   09    10    11    12  |  01    02    03    04    05    06    07    08
		//	    2024/08  |  2023  2023  2023  2023 | 2024  2024  2024  2024  2024  2024  2024  2024
		//	    2024/09  |  2024  2024  2024  2024 | 2025  2025  2025  2025  2025  2025  2025  2025
		//	    2024/10  |  2024  2024  2024  2024 | 2025  2025  2025  2025  2025  2025  2025  2025
		//	    2024/11  |  2024  2024  2024  2024 | 2025  2025  2025  2025  2025  2025  2025  2025
		//	    2024/12  |  2024  2024  2024  2024 | 2025  2025  2025  2025  2025  2025  2025  2025
		//	    2025/01  |  2024  2024  2024  2024 | 2025  2025  2025  2025  2025  2025  2025  2025
		//	    2025/02  |  2024  2024  2024  2024 | 2025  2025  2025  2025  2025  2025  2025  2025
		//	    2025/03  |  2024  2024  2024  2024 | 2025  2025  2025  2025  2025  2025  2025  2025
		//	    2025/04  |  2024  2024  2024  2024 | 2025  2025  2025  2025  2025  2025  2025  2025
		//	    2025/05  |  2024  2024  2024  2024 | 2025  2025  2025  2025  2025  2025  2025  2025
		//	    2025/06  |  2024  2024  2024  2024 | 2025  2025  2025  2025  2025  2025  2025  2025
		//	    2025/07  |  2024  2024  2024  2024 | 2025  2025  2025  2025  2025  2025  2025  2025
		//	    2025/08  |  2024  2024  2024  2024 | 2025  2025  2025  2025  2025  2025  2025  2025
		//	    2025/09  |  2025  2025  2025  2025 | 2026  2026  2026  2026  2026  2026  2026  2026
		
		// ■集計初月が12月である場合( →12月がリストの先頭になる )
		//
		//	    基準年月
		//	      ▼     |   12  |  01    02    03    04    05    06    07    08    09    10    11 
		//	    2024/08  |  2023 | 2024  2024  2024  2024  2024  2024  2024  2024  2024  2024  2024
		//	    2024/09  |  2023 | 2024  2024  2024  2024  2024  2024  2024  2024  2024  2024  2024
		//	    2024/10  |  2023 | 2024  2024  2024  2024  2024  2024  2024  2024  2024  2024  2024
		//	    2024/11  |  2023 | 2024  2024  2024  2024  2024  2024  2024  2024  2024  2024  2024
		//	    2024/12  |  2024 | 2025  2025  2025  2025  2025  2025  2025  2025  2025  2025  2025
		//	    2025/01  |  2024 | 2025  2025  2025  2025  2025  2025  2025  2025  2025  2025  2025
		//	    2025/02  |  2024 | 2025  2025  2025  2025  2025  2025  2025  2025  2025  2025  2025
		//	    2025/03  |  2024 | 2025  2025  2025  2025  2025  2025  2025  2025  2025  2025  2025
		//	    2025/04  |  2024 | 2025  2025  2025  2025  2025  2025  2025  2025  2025  2025  2025
		//	    2025/05  |  2024 | 2025  2025  2025  2025  2025  2025  2025  2025  2025  2025  2025
		//	    2025/06  |  2024 | 2025  2025  2025  2025  2025  2025  2025  2025  2025  2025  2025
		//	    2025/07  |  2024 | 2025  2025  2025  2025  2025  2025  2025  2025  2025  2025  2025
		//	    2025/08  |  2024 | 2025  2025  2025  2025  2025  2025  2025  2025  2025  2025  2025
		//	    2025/09  |  2024 | 2025  2025  2025  2025  2025  2025  2025  2025  2025  2025  2025
		
		
		// ■集計初月が01月である場合( →01月がリストの先頭になる )
		//
		//	    基準年月
		//	      ▼     |   01    02    03    04    05    06    07    08    09    10    11    12 
		//	    2024/08  |  2024  2024  2024  2024  2024  2024  2024  2024  2024  2024  2024  2024
		//	    2024/09  |  2024  2024  2024  2024  2024  2024  2024  2024  2024  2024  2024  2024
		//	    2024/10  |  2024  2024  2024  2024  2024  2024  2024  2024  2024  2024  2024  2024
		//	    2024/11  |  2024  2024  2024  2024  2024  2024  2024  2024  2024  2024  2024  2024
		//	    2024/12  |  2024  2024  2024  2024  2024  2024  2024  2024  2024  2024  2024  2024
		//	    2025/01  |  2025  2025  2025  2025  2025  2025  2025  2025  2025  2025  2025  2025
		//	    2025/02  |  2025  2025  2025  2025  2025  2025  2025  2025  2025  2025  2025  2025
		//	    2025/03  |  2025  2025  2025  2025  2025  2025  2025  2025  2025  2025  2025  2025
		//	    2025/04  |  2025  2025  2025  2025  2025  2025  2025  2025  2025  2025  2025  2025
		//	    2025/05  |  2025  2025  2025  2025  2025  2025  2025  2025  2025  2025  2025  2025
		//	    2025/06  |  2025  2025  2025  2025  2025  2025  2025  2025  2025  2025  2025  2025
		//	    2025/07  |  2025  2025  2025  2025  2025  2025  2025  2025  2025  2025  2025  2025
		//	    2025/08  |  2025  2025  2025  2025  2025  2025  2025  2025  2025  2025  2025  2025
		//	    2025/09  |  2025  2025  2025  2025  2025  2025  2025  2025  2025  2025  2025  2025
		//
		
		
		// 集計初月を取得
		int aggregateStartMonth = Integer.parseInt(this.getAggregateStartMonth());
		
		
		// 基準日から年を切出し
		DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("yyyy");
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM");
		int baseYYYY = Integer.parseInt(baseDate.format(yearFormatter));
		int baseMM   = Integer.parseInt(baseDate.format(monthFormatter));
		
		log.info("【INF】" + classId + " 基準年=[" + baseYYYY + "]基準月=[" + baseMM + "]集計初月=[" + aggregateStartMonth + "]");
		
		// 基準月が集計初月より小さい場合、基準年を前年にする
		if (baseMM < aggregateStartMonth) {
			baseYYYY = baseYYYY - 1;
		}
		
		
		// 集計用年月テーブルを作成(12か月分作成)
		int indexMonth = aggregateStartMonth;
		
		this.list = new ArrayList<AggregateDetail>();
		
		for (int index = 0 ; index < 12; index++) {
			
			if (indexMonth >=aggregateStartMonth) {
				AggregateDetail detail = new AggregateDetail(
												 Integer.toString(baseYYYY)
												,String.format("%02d", indexMonth)//頭0サプレスの文字列
												,0);
				
				this.list.add(detail);
				
				
				
			} else if (indexMonth < aggregateStartMonth) { 
				
				AggregateDetail detail = new AggregateDetail(
												 Integer.toString(baseYYYY + 1)   //★集計初月より小さい月である場合は翌年になる
												,String.format("%02d", indexMonth)//頭0サプレスの文字列
												,0);
				
				this.list.add(detail);
			}
			
			indexMonth = indexMonth + 1;
			
			// 12月を超えたら1月に戻す
			if (indexMonth > 12) {
				indexMonth = 1;
			}
			
		}
		
		
		
		
		// 検証用にログ出力
		log.info("【DBG】" + classId + "----------★集計年月テーブル★------------------");
		for (int index = 0; index < this.list.size() -1; index ++) {
			
			log.info("【DBG】" + classId + "[" + this.list.get(index).year + "]年[" + this.list.get(index).month + "]月");
		}
		log.info("【DBG】" + classId + "------------------------------------------------");
		
		log.info("【INF】" + pgmId + ":処理終了");
		
		
	}
	
	
	
	
	// 集計初月を取得
	public String getAggregateStartMonth() {
		
		String pgmId = classId + ".getAggregateStartMonth";
		
		try {
			log.info("【INF】" + pgmId + ":処理開始");
			
			String sql = " select ";
			sql  = sql + "     AGGREGATEMONTH";
			sql  = sql + " from";
			sql  = sql + "     TM_AGGREGATE_START_MONTH";
			
			// queryForListメソッドでSQLを実行し、結果MapのListで受け取る。
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql);
			
			String aggregateStartMonth = "";
			
			for (Map<String, Object> rs: rsList) {
				
				aggregateStartMonth = rs.get("AGGREGATEMONTH").toString();
			}
			
			log.info("【DBG】" + pgmId + "集計初月=[" + aggregateStartMonth + "]");
			log.info("【INF】" + pgmId + ":処理終了");
			return aggregateStartMonth;
			
			
		}catch(Exception e){

			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return null;
		}
	}
	
	@Data
	public class AggregateDetail {
		
		public String year;
		public String month;
		//public int boxSumYTI;
		
		public AggregateDetail(String year, String month, int boxSumYTI) {
			
			this.year      = year;
			this.month     = month;
			//this.boxSumYTI = boxSumYTI;
		}
		
	}

}
