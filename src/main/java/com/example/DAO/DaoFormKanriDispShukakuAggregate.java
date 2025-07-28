package com.example.DAO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.example.common.AggregateTable;
import com.example.form.FormKanriDispShukakuAggregateDetail;
import com.example.form.FormKanriDispShukakuAggregateDetail.AggregateDispDataType;
import com.example.form.FormKanriDispShukakuAggregateList;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DaoFormKanriDispShukakuAggregate {
	
	private JdbcTemplate  jdbcTemplate;
	private String classId = "DaoFormKanriDispWorkStatus";
	
	// コンストラクタ
	public DaoFormKanriDispShukakuAggregate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	
	
	// リストを取得
	public FormKanriDispShukakuAggregateList getAggregateData() {
		
		
		// 年月日時分秒までの日時フォーマットを準備
		//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		String pgmId = classId + ".getAggregateData";
		log.info("【INF】" + pgmId + ":処理開始");
		
		
		// 返却値
		FormKanriDispShukakuAggregateList retForm = new FormKanriDispShukakuAggregateList();
		try {
			
			//------------------------------------------------
			// 収穫ケース数集計テーブルの作成（本年度のデータ取得用）
			
			LocalDate nowDate = LocalDate.now();
			AggregateTable aggregateTableNowYear = new AggregateTable(jdbcTemplate, nowDate);
			
			
			//------------------------------------------------
			// 収穫ケース数集計テーブルの作成（前年度のデータ取得用）
			
			LocalDate prvDate = LocalDate.now().minusYears(1);
			AggregateTable aggregateTablePrvYear = new AggregateTable(jdbcTemplate, prvDate);
			
			
			//------------------------------------------------
			// 全ハウス合計をグラフ表示するデータを取得
			
			retForm.setDetailList_All(      this.getAggregateDataDetailList(""    ,aggregateTableNowYear,aggregateTablePrvYear)  );
			
			
			//------------------------------------------------
			// ハウス毎にグラフ表示するデータを取得
			
			retForm.setDetailList_10001(    this.getAggregateDataDetailList("10001",aggregateTableNowYear,aggregateTablePrvYear)  );
			retForm.setDetailList_10002(    this.getAggregateDataDetailList("10002",aggregateTableNowYear,aggregateTablePrvYear)  );
			retForm.setDetailList_10003(    this.getAggregateDataDetailList("10003",aggregateTableNowYear,aggregateTablePrvYear)  );
			retForm.setDetailList_10004(    this.getAggregateDataDetailList("10004",aggregateTableNowYear,aggregateTablePrvYear)  );
			retForm.setDetailList_10005(    this.getAggregateDataDetailList("10005",aggregateTableNowYear,aggregateTablePrvYear)  );
			retForm.setDetailList_10006(    this.getAggregateDataDetailList("10006",aggregateTableNowYear,aggregateTablePrvYear)  );
			retForm.setDetailList_10007(    this.getAggregateDataDetailList("10007",aggregateTableNowYear,aggregateTablePrvYear)  );
			
			
			
			
			
			log.info("【INF】" + pgmId + ":処理終了");
			return retForm;
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了[" + e.toString() + "]");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return null;
		}
	}
	
	
	// リストを取得
	public ArrayList<FormKanriDispShukakuAggregateDetail> getAggregateDataDetailList(
															 String houseId
															,AggregateTable aggregateTableNowYear
															,AggregateTable aggregateTablePrvYear) {
		
		
		// 年月日時分秒までの日時フォーマットを準備
		//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		String pgmId = classId + ".getAggregateDataDetailList";
		log.info("【INF】" + pgmId + ":処理開始");
		
		
		// 返却値
		ArrayList<FormKanriDispShukakuAggregateDetail> retList = new ArrayList<FormKanriDispShukakuAggregateDetail>();
		
		try {
			
			// ------------------------------------------------
			// 前年度実績のデータを取得
			
			for (int index = 0 ;index < aggregateTablePrvYear.list.size(); index++) {
				
				FormKanriDispShukakuAggregateDetail detail = this.getAggregateDataDetail(
																	  AggregateDispDataType.PRV_YEAR
																	, houseId
																	, aggregateTablePrvYear.list.get(index).getYear()
																	, aggregateTablePrvYear.list.get(index).getMonth()
																	);
				retList.add(detail);
			}
			
			// ------------------------------------------------
			// 本年度予定のデータを取得
			
			for (int index = 0 ;index < aggregateTableNowYear.list.size(); index++) {
				
				FormKanriDispShukakuAggregateDetail detail = this.getAggregateDataDetail(
																	  AggregateDispDataType.NOW_YEAR_YTI
																	, houseId
																	, aggregateTableNowYear.list.get(index).getYear()
																	, aggregateTableNowYear.list.get(index).getMonth()
																	);
				retList.add(detail);
			}
			
			// ------------------------------------------------
			// 本年度実績のデータを取得
			
			for (int index = 0 ;index < aggregateTableNowYear.list.size(); index++) {
				
				FormKanriDispShukakuAggregateDetail detail = this.getAggregateDataDetail(
																	  AggregateDispDataType.NOW_YEAR
																	, houseId
																	, aggregateTableNowYear.list.get(index).getYear()
																	, aggregateTableNowYear.list.get(index).getMonth()
																	);
				retList.add(detail);
			}
			
			
			log.info("【INF】" + pgmId + ":処理終了 取得件数=[" + Integer.toString(retList.size()) + "]件");
			return retList;
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了[" + e.toString() + "]");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return null;
		}
	}
	
	
	// リストを取得
	public FormKanriDispShukakuAggregateDetail getAggregateDataDetail(String dataType, String houseId,String aggregateYear, String aggregateMonth) {
		
		
		// 年月日時分秒までの日時フォーマットを準備
		//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		String pgmId = classId + ".getAggregateDataDetail";
		log.info("【INF】" + pgmId + ":処理開始 データタイプ=[" + dataType + "]ハウス=[" + houseId + "]集計年=[" + aggregateYear + "]集計月=[" + aggregateMonth + "]");
		
		
		// 返却値
		FormKanriDispShukakuAggregateDetail detail = new FormKanriDispShukakuAggregateDetail();
		
		try {
			
			String sql = "";
			List<Map<String, Object>> rsList = null;
			
			
			// ハウスIDの指定がない場合は、その年月で”全ハウスの合計”を取得
			if (houseId.equals("") == true) {
				
				sql        = " select";
				sql  = sql + "     'XXXXXX' HOUSEID";
				sql  = sql + "    ,AGGREGATEYEAR";
				sql  = sql + "    ,AGGREGATEMONTH";
				sql  = sql + "    ,SUM(BOXSUM) BOXSUM";
				sql  = sql + "    ,SUM(BOXSUM_YTI) BOXSUM_YTI";
				sql  = sql + " from";
				sql  = sql + "     TT_SHUKAKU_AGGREGATE";
				sql  = sql + " where";
				sql  = sql + "     AGGREGATEYEAR  = ?";
				sql  = sql + " and AGGREGATEMONTH = ?";
				sql  = sql + " group by";
				sql  = sql + "     AGGREGATEYEAR";
				sql  = sql + "    ,AGGREGATEMONTH";
				
				
				// queryForListメソッドでSQLを実行し、結果MapのListで受け取る。
				rsList = this.jdbcTemplate.queryForList(sql
												,aggregateYear
												,aggregateMonth
												);
				
			} else {
				
				sql        = " select";
				sql  = sql + "     HOUSEID";
				sql  = sql + "    ,AGGREGATEYEAR";
				sql  = sql + "    ,AGGREGATEMONTH";
				sql  = sql + "    ,BOXSUM";
				sql  = sql + "    ,BOXSUM_YTI";
				sql  = sql + " from";
				sql  = sql + "     TT_SHUKAKU_AGGREGATE";
				sql  = sql + " where";
				sql  = sql + "     HOUSEID        = ?";
				sql  = sql + " and AGGREGATEYEAR  = ?";
				sql  = sql + " and AGGREGATEMONTH = ?";
				
				
				// queryForListメソッドでSQLを実行し、結果MapのListで受け取る。
				rsList = this.jdbcTemplate.queryForList(sql
												,houseId
												,aggregateYear
												,aggregateMonth
												);
				
			}
			
			int cnt = 0;
			for (Map<String, Object> rs: rsList) {
				
				
				//データタイプ
				detail.setDataType(dataType);
				
				// ハウスID
				detail.setHouseId(rs.get("HOUSEID").toString());
				// 集計年月
				detail.setAggregateYear( rs.get("AGGREGATEYEAR").toString());
				detail.setAggregateMonth(rs.get("AGGREGATEMONTH").toString());
				
				
				if (dataType.equals(AggregateDispDataType.PRV_YEAR)) {
					// 収穫ケース合計
					detail.setBoxSum(rs.get("BOXSUM").toString());
				}
				if (dataType.equals(AggregateDispDataType.NOW_YEAR_YTI)) {
					// 収穫ケース合計＿予定
					detail.setBoxSum(rs.get("BOXSUM_YTI").toString());
				}
				if (dataType.equals(AggregateDispDataType.NOW_YEAR)) {
					// 収穫ケース合計
					detail.setBoxSum(rs.get("BOXSUM").toString());
				}
				
				break;
			}
			
			
			log.info("【INF】" + pgmId + ":処理終了 取得件数=[" + Integer.toString(cnt) + "]件");
			return detail;
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了[" + e.toString() + "]");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return null;
		}
	}
	
	
}
