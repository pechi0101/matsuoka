package com.example.DAO;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.example.form.FormKanriDispShukakuAggregateDetail;
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
			
			// ★★★★★★★★★★★★★★★★★★★★★★★★★★★
			//
			// 動的SQLであるためSQL中に直接引数の検索条件を埋め込む
			//
			// ★★★★★★★★★★★★★★★★★★★★★★★★★★★
			
			
			//【メモ】：画面に削除した作業をグレーアウトして表示するために削除済みの作業も検索対象にする
			
			
			String sql = " select";
			sql  = sql + "     AGGRE.HOUSEID";
			sql  = sql + "    ,HOUSE.HOUSENAME";
			sql  = sql + "    ,AGGRE.AGGREGATEYEAR";
			sql  = sql + "    ,AGGRE.AGGREGATEMONTH";
			sql  = sql + "    ,AGGRE.BOXSUM";
			sql  = sql + " from";
			sql  = sql + "     TT_SHUKAKU_AGGREGATE AGGRE";
			sql  = sql + " inner join";
			sql  = sql + "     TM_HOUSE HOUSE";
			sql  = sql + "     on HOUSE.HOUSEID = AGGRE.HOUSEID";
			sql  = sql + " order by";
			sql  = sql + "     AGGRE.HOUSEID";
			sql  = sql + "    ,AGGRE.AGGREGATEYEAR";
			sql  = sql + "    ,AGGRE.AGGREGATEMONTH";
			/*
			sql  = sql + " where";
			sql  = sql + "     TT_HOUSE_WORKSTATUS.STARTEMPLOYEEID <> '" + SpecialUser.TEST_USER + "'";//テストユーザは対象にしない
			if (houseId != null && "".equals(houseId) == false) {
			sql  = sql + " and TT_HOUSE_WORKSTATUS.HOUSEID          = '" + houseId + "'";
			}
			if (colNo != null   && "".equals(colNo)   == false) {
			sql  = sql + " and TT_HOUSE_WORKSTATUS.COLNO            = '" + colNo + "'";
			}
			if (workId != null  && "".equals(workId)  == false) {
			sql  = sql + " and TT_HOUSE_WORKSTATUS.WORKID           = '" + workId + "'";
			}
			if (startDateTimeFr != null) {
			sql  = sql + " and TT_HOUSE_WORKSTATUS.STARTDATETIME   >= '" + formatter.format(startDateTimeFr) + "'";
			}
			if (startDateTimeTo != null) {
			sql  = sql + " and TT_HOUSE_WORKSTATUS.STARTDATETIME   <= '" + formatter.format(startDateTimeTo) + "'";
			}
			*/
			
			
			//log.info("【INF】" + pgmId + ":検索SQL=[" + sql + "]");
			
			
			
			/*
			 * 【メモ】：MySQLの日付フォーマット
			 * %Y    4 桁の年               例：2024
			 * %y    2 桁の年               例：24
			 * %c    月                     例：0 ~ 12
			 * %m    2 桁の月               例：00 ~ 12
			 * %e    日                     例：0 ~ 31
			 * %d    2 桁の日               例：00 ~ 31
			 * %H    24時制の時間           例：00 ~ 23
			 * %h    12時制の時間           例：01 ~ 12
			 * %p    午前・午後             例：AM か PM
			 * %i    分                     例：00 ~ 59
			 * %S,%s 秒                     例：00 ~ 59
			 * %f    ミリ秒                 例：000000 ~ 999999
			 * %M    月名                   例：January ~ December
			 * %b    簡略月名               例：Jan ~ Dec
			 * %W    曜日名                 例：Sunday ~ Saturday
			 * %b    簡略曜日名             例：Sun ~ Sat
			 * %a    12時制の時間・分・秒。 例：21:40:13
			 * %T    24時制の時間・分・秒。 例：21:40:13
			 */
			
			
			
			// queryForListメソッドでSQLを実行し、結果MapのListで受け取る。
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql);
			
			
			for (Map<String, Object> rs: rsList) {
				
				
				FormKanriDispShukakuAggregateDetail detail = new FormKanriDispShukakuAggregateDetail();
				
				
				
				// ハウスID
				detail.setHouseId(rs.get("HOUSEID").toString());
				
				//ハウス名 ※ハウス名がマスタから取得できない場合は"未登録"と一覧表示
				if (rs.get("HOUSENAME") != null) {
					detail.setHouseName(rs.get("HOUSENAME").toString());
				} else {
					detail.setHouseName("未登録");
				}
				
				// 集計年月
				detail.setAggregateYear(rs.get("AGGREGATEYEAR").toString());
				detail.setAggregateMonth(rs.get("AGGREGATEMONTH").toString());
				
				
				// 収穫ケース合計
				detail.setBoxSum(rs.get("BOXSUM").toString());
				
				
				retForm.getDetailList().add(detail);
				
			}
			
			
			log.info("【INF】" + pgmId + ":処理終了 取得件数=[" + Integer.toString(retForm.getDetailList().size()) + "]件");
			return retForm;
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了[" + e.toString() + "]");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return null;
		}
	}
	
}
