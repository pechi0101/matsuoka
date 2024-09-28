package com.example.DAO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.example.entity.ShukakuBoxSum;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DaoShukakuBoxSum {
	
	private JdbcTemplate  jdbcTemplate;
	private String classId = "DaoShukakuBoxSum";
	
	// コンストラクタ
	public DaoShukakuBoxSum(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	// 指定したハウス、収穫日の情報を取得
	public ShukakuBoxSum getData(String houseId,LocalDateTime shukakuDate) {
		
		String pgmId = classId + ".getData";
		log.info("【INF】" + pgmId + ":〇処理開始 ハウスID=[" + houseId + "]、収穫日=[" + shukakuDate.toString() + "]");
		
		log.info("【INF】" + pgmId + ":000-01");
		
		// 返却値
		ShukakuBoxSum shukakuBoxSum = new ShukakuBoxSum();
		
		log.info("【INF】" + pgmId + ":000-02");
		
		
		try {
			
			String sql = " select";
			sql  = sql + "     HOUSEID";
			sql  = sql + "    ,DATE_FORMAT(SHUKAKUDATE,'%Y%m%d') SHUKAKUDATE_STRING"; // YYYYMMDD形式で取得（時間は24時間制）
			sql  = sql + "    ,REGISTEMPLOYEEID";
			sql  = sql + "    ,DATE_FORMAT(REGISTDATETIME,'%Y%m%d%H%i%S') REGISTDATETIME_STRING"; // YYYYMMDDHHMMSS形式で取得（時間は24時間制）
			sql  = sql + "    ,BIKO";
			sql  = sql + "    ,BOXSUM";
			sql  = sql + " from";
			sql  = sql + "     TT_SHUKAKU_BOXSUM";
			sql  = sql + " where";
			sql  = sql + "     HOUSEID      = ?";
			sql  = sql + " and SHUKAKUDATE  = ?";
			sql  = sql + " and DELETEFLG    = false";
			
			log.info("【INF】" + pgmId + ":000-03");
			
			
			
			
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
			
			// 年月日までの日時フォーマットを準備
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			
			log.info("【INF】" + pgmId + ":001");
			log.info("【INF】" + pgmId + ":検索収穫日=[" + formatter.format(shukakuDate) + "]");
			
			// queryForListメソッドでSQLを実行し、結果MapのListで受け取る。
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(
														 sql
														,houseId
														,formatter.format(shukakuDate) // 日付は決められた書式(フォーマット)の文字列で検索する
														);
			
			log.info("【INF】" + pgmId + ":002");
			
			// 【重要】この変数に作業開始、終了日時を文字列でセットし、作業状況の判定に使用する。
			String shukakuDateString = "";
			String registDateTimeString = "";
			
			for (Map<String, Object> rs: rsList) {
				
				shukakuBoxSum.setHouseId(rs.get("HOUSEID").toString());
				
				// 年月日時分秒までの日時フォーマットを準備
				//formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
				formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
				
				log.info("【INF】" + pgmId + ":003");
				
				//収穫日(LocalDateTime型に合わせるため後ろに時分秒の000000を付ける)
				shukakuDateString = rs.get("SHUKAKUDATE_STRING").toString() + "000000";
				
				log.info("【INF】" + pgmId + ":収穫日=[" + shukakuDateString + "]");
				
				shukakuBoxSum.setShukakuDate(LocalDateTime.parse(shukakuDateString,formatter));
				
				
				log.info("【INF】" + pgmId + ":004");
				
				//登録社員ID
				shukakuBoxSum.setRegistEmployeeid(rs.get("REGISTEMPLOYEEID").toString());
				
				
				// 年月日時分秒までの日時フォーマットを準備
				formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
				
				//登録日時
				registDateTimeString = rs.get("REGISTDATETIME_STRING").toString();
				shukakuBoxSum.setRegistDatetime(LocalDateTime.parse(registDateTimeString,formatter));
				
				
				log.info("【INF】" + pgmId + ":005");
				
				
				// 備考
				if (rs.get("BIKO") != null) {
					shukakuBoxSum.setBiko(rs.get("BIKO").toString());
				}
				// 収穫ケース数合計
				shukakuBoxSum.setBoxSum(Double.parseDouble(rs.get("BOXSUM").toString()));
				
				// 値は１件のみ取得されるためここでLOOP終了
				break;
			}
			
			log.info("【DBG】" + pgmId + "ハウスID=[" + shukakuBoxSum.getHouseId() + "]、登録社員ID=[" + shukakuBoxSum.getRegistEmployeeid() + "]");
			
			
			if ("".equals(shukakuBoxSum.getRegistEmployeeid())) {
				//データを取得できなかった場合
				log.info("【INF】" + pgmId + ":処理終了 データなし");
				
			}else{
				//データを取得できた場合
				log.info("【INF】" + pgmId + ":処理終了 取得データの収穫日=[" + shukakuBoxSum.getShukakuDate().toString() + "]");
				
			}
			
			
			return shukakuBoxSum;
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return shukakuBoxSum;
		}
	}
	
	
	// データ登録
	public boolean regist(ShukakuBoxSum shukakuBoxSum ,String userName,String registPgmId) {
		
		// 年月日時分秒までの日時フォーマットを準備
		DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		String pgmId = classId + ".regist";
		log.info("【INF】" + pgmId + ":処理開始 □収穫日=[" + formatterDate.format(shukakuBoxSum.getShukakuDate()) + "]");
		
		try {
			
			String sql = " insert into TT_SHUKAKU_BOXSUM (";
			sql  = sql + "     HOUSEID";
			sql  = sql + "    ,SHUKAKUDATE";
			sql  = sql + "    ,REGISTEMPLOYEEID";
			sql  = sql + "    ,REGISTDATETIME";
			sql  = sql + "    ,BIKO";
			sql  = sql + "    ,BOXSUM";
			sql  = sql + "    ,DELETEFLG";
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
			sql  = sql + "    ,current_timestamp(3)"; //登録日時
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,current_timestamp(3)";
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,current_timestamp(3)";
			sql  = sql + " )";
			
			
			
			int ret = this.jdbcTemplate.update(sql
					,shukakuBoxSum.getHouseId()
					,formatterDate.format(shukakuBoxSum.getShukakuDate())
					,shukakuBoxSum.getRegistEmployeeid()
					,shukakuBoxSum.getBiko()
					,shukakuBoxSum.getBoxSum()
					,shukakuBoxSum.isDelflg()
					,userName
					,registPgmId
					,userName
					,registPgmId);
			
			
			// メモ：commitはjdbcTemplateが自動で行ってくれる
			
			log.info("【INF】" + pgmId + ":処理終了 ret=[" + ret + "]");
			
			return true;
			
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return false;
		}
	}
	
	
	
	// データ更新（指定されたハウス、収穫日の"未削除"データを”削除状態”に更新）
	public boolean updateDeleteState(String houseId, LocalDateTime shukakuDate ,String userName,String registPgmId) {
		
		String pgmId = classId + ".updateDeleteState";
		log.info("【INF】" + pgmId + ":処理開始");
		
		try {
			
			String sql = " update TT_SHUKAKU_BOXSUM";
			sql  = sql + " set";
			sql  = sql + "     DELETEFLG     = true";
			sql  = sql + "    ,SYSUPDUSERID  = ?";
			sql  = sql + "    ,SYSUPDPGMID   = ?";
			sql  = sql + "    ,SYSUPDYMDHMS  = current_timestamp(3)";
			sql  = sql + " where";
			sql  = sql + "     HOUSEID      = ?";
			sql  = sql + " and SHUKAKUDATE  = ?";
			sql  = sql + " and DELETEFLG    = false";
			
			
			// 年月日時分秒までの日時フォーマットを準備
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			
			
			int ret = this.jdbcTemplate.update(sql
					,userName
					,registPgmId
					,houseId
					,formatter.format(shukakuDate)
					);
			
			
			// メモ：commitはjdbcTemplateが自動で行ってくれる
			
			log.info("【INF】" + pgmId + ":処理終了 ret=[" + ret + "]");
			
			// 更新件数０件である場合もOKを返却
			if (ret == 0) {
				return true;
			}
			return true;
			
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return false;
		}
	}
	

}
