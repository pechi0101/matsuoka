package com.example.DAO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.example.counst.SpecialUser;
import com.example.form.FormKanriMainteShukakuSumDetail;
import com.example.form.FormKanriMainteShukakuSumList;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DaoFormKanriShukakuSum {
	
	private JdbcTemplate  jdbcTemplate;
	private String classId = "DaoFormKanriShukakuSum";
	
	// コンストラクタ
	public DaoFormKanriShukakuSum(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	// リストを取得
	/*
		ハウス名     日付     収穫ケース数   収穫ケース数(QR)
		------------------------------------------------------
		３号隔離   2024/5/1    10  ケース     10  ケース
		３号隔離   2024/5/2                   10  ケース
		３号隔離   2024/5/3    10  ケース               
		３号隔離   2024/5/4    10  ケース     10  ケース
		３号隔離   2024/5/5                             	←このケースは表示しない（検索対象外）
	 */
	public FormKanriMainteShukakuSumList getShukakuSumList(String houseId,LocalDate shukakuDateFr,LocalDate shukakuDateTo) {
		
		// 年月日時分秒までの日時フォーマットを準備
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		String pgmId = classId + ".getShukakuSumList";
		log.info("【INF】" + pgmId + ":処理開始 ハウスID=[" + houseId + "]、収穫日From=[" + shukakuDateFr + "]、収穫日From=[" + shukakuDateTo + "]");
		
		
		// 返却値
		FormKanriMainteShukakuSumList retForm = new FormKanriMainteShukakuSumList();
		try {
			
			// ★★★★★★★★★★★★★★★★★★★★★★★★★★★
			//
			// 動的SQLであるためSQL中に直接引数の検索条件を埋め込む
			//
			// ★★★★★★★★★★★★★★★★★★★★★★★★★★★
			
			
			//【メモ】：画面に削除した作業をグレーアウトして表示するために削除済みの作業も検索対象にする
			
			
			String sql = " select";
			sql  = sql + "     TT_SHUKAKU_BASE.HOUSEID";
			sql  = sql + "    ,TM_HOUSE.HOUSENAME";
			sql  = sql + "    ,DATE_FORMAT(TT_SHUKAKU_BASE.SHUKAKUDATE,'%Y%m%d') SHUKAKUDATE_STRING";// YYYYMMDD形式で取得
			sql  = sql + "    ,TT_SHUKAKU.BOXSUM BOXSUM";
			sql  = sql + "    ,TT_SHUKAKU_QR.BOXSUM BOXSUM_QR";
			sql  = sql + "    ,TT_SHUKAKU.BIKO";
			sql  = sql + " from";
			// 表示対象のハウスIDと収穫日のみを取得するSQL :パートさんによる収穫入力または松岡さんによる収穫ケース数合計入力のいづれかが存在するハウス・収穫日を検索
			sql  = sql + "     (";
			sql  = sql + "     select   distinct";
			sql  = sql + "              HOUSEID";
			sql  = sql + "             ,date(ENDDATETIME) SHUKAKUDATE";
			sql  = sql + "     from     TT_HOUSE_WORKSTATUS_SHUKAKU";
			sql  = sql + "     where    STARTEMPLOYEEID <> '" + SpecialUser.TEST_USER + "'";//テストユーザは対象にしない
			sql  = sql + "     and      DELETEFLG <> 1";
			sql  = sql + "     and      ENDDATETIME is not null";
			sql  = sql + "           union";   // 「union all」ではなく「union」にすることでハウス・収穫日の重複は除去する
			sql  = sql + "     select   HOUSEID";
			sql  = sql + "             ,SHUKAKUDATE";
			sql  = sql + "     from     TT_SHUKAKU_BOXSUM";
			sql  = sql + "     where    DELETEFLG <> 1";
			sql  = sql + "     ) TT_SHUKAKU_BASE";
			// ハウス名を取得するSQL
			sql  = sql + " left join";
			sql  = sql + "     TM_HOUSE";
			sql  = sql + "     on";
			sql  = sql + "         TT_SHUKAKU_BASE.HOUSEID     = TM_HOUSE.HOUSEID";
			// ハウス作業”後”に松岡さんが最終確認のためQRコードで入力した、又は収穫状況確認画面で入力した収穫数を取得するSQL
			sql  = sql + " left join";
			sql  = sql + "     (";
			sql  = sql + "     select   HOUSEID";
			sql  = sql + "             ,SHUKAKUDATE";
			sql  = sql + "             ,BOXSUM";
			sql  = sql + "             ,BIKO";
			sql  = sql + "     from     TT_SHUKAKU_BOXSUM";
			sql  = sql + "     where    DELETEFLG <> 1"; //ハウス・収穫日ごとに削除されてないデータは１件のみ
			sql  = sql + "     ) TT_SHUKAKU";
			sql  = sql + "     on";
			sql  = sql + "         TT_SHUKAKU_BASE.HOUSEID     = TT_SHUKAKU.HOUSEID";
			sql  = sql + "     and TT_SHUKAKU_BASE.SHUKAKUDATE = TT_SHUKAKU.SHUKAKUDATE";
			// ハウス作業で松岡さんやパートの方がQRコードで入力した収穫数を取得するSQL
			sql  = sql + " left join";
			sql  = sql + "     (";
			sql  = sql + "     select   HOUSEID";
			sql  = sql + "             ,date(ENDDATETIME) SHUKAKUDATE";
			sql  = sql + "             ,sum(BOXCOUNT) BOXSUM";
			sql  = sql + "     from     TT_HOUSE_WORKSTATUS_SHUKAKU";
			sql  = sql + "     where    STARTEMPLOYEEID <> '" + SpecialUser.TEST_USER + "'";//テストユーザは対象にしない
			sql  = sql + "     and      DELETEFLG <> 1";
			sql  = sql + "     and      ENDDATETIME is not null";
			sql  = sql + "     group by HOUSEID";
			sql  = sql + "             ,date(ENDDATETIME)";
			sql  = sql + "     ) TT_SHUKAKU_QR";
			sql  = sql + "     on";
			sql  = sql + "         TT_SHUKAKU_BASE.HOUSEID     = TT_SHUKAKU_QR.HOUSEID";
			sql  = sql + "     and TT_SHUKAKU_BASE.SHUKAKUDATE = TT_SHUKAKU_QR.SHUKAKUDATE";
			sql  = sql + " where";
			sql  = sql + "     TT_SHUKAKU_BASE.HOUSEID  is not null"; //ダミーのwhere条件
			
			if (houseId != null && "".equals(houseId) == false) {
			sql  = sql + " and TT_SHUKAKU_BASE.HOUSEID          = '" + houseId + "'";
			}
			if (shukakuDateFr != null) {
			sql  = sql + " and TT_SHUKAKU_BASE.SHUKAKUDATE   >= '" + formatter.format(shukakuDateFr) + "'";
			}
			if (shukakuDateTo != null) {
			sql  = sql + " and TT_SHUKAKU_BASE.SHUKAKUDATE   <= '" + formatter.format(shukakuDateTo) + "'";
			}
			sql  = sql + " order by";
			sql  = sql + "     TT_SHUKAKU_BASE.HOUSEID";
			sql  = sql + "    ,TT_SHUKAKU_BASE.SHUKAKUDATE";
			
			
			
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
			
			
			// 【重要】この変数に作業開始、終了日時を文字列でセットし、作業状況の判定に使用する。
			//String startDateTimeString = "";
			//String endDateTimeString = "";
			//String deleteDateTimeString = "";
			
			
			for (Map<String, Object> rs: rsList) {
				
				
				FormKanriMainteShukakuSumDetail detail = new FormKanriMainteShukakuSumDetail();
				
				
				
				// ハウスID
				detail.setHouseId(rs.get("HOUSEID").toString());
				
				//ハウス名 ※ハウス名がマスタから取得できない場合は"未登録"と一覧表示
				if (rs.get("HOUSENAME") != null) {
					detail.setHouseName(rs.get("HOUSENAME").toString());
				} else {
					detail.setHouseName("未登録");
				}
				
				
				
				// 年月日時分秒までの日時フォーマットを準備
				//DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
				DateTimeFormatter formatterDate     = DateTimeFormatter.ofPattern("yyyyMMdd");
				//DateTimeFormatter formatterTime     = DateTimeFormatter.ofPattern("HHmmss");
				
				String wkDate; //YYYYMMDDの文字列
				
				// 収穫日
				wkDate = rs.get("SHUKAKUDATE_STRING").toString();
				detail.setShukakuDate(LocalDate.parse(wkDate,formatterDate));
				
				
				// ケース数
				if (rs.get("BOXSUM") != null) {
					detail.setBoxSum(rs.get("BOXSUM").toString());
				}
				
				// ケース数(QR)
				if (rs.get("BOXSUM_QR") != null) {
					detail.setBoxSumQR(rs.get("BOXSUM_QR").toString());
				}
				
				// 備考
				if (rs.get("BIKO") != null) {
					detail.setBiko(rs.get("BIKO").toString());
				}
				
				retForm.getShukakuList().add(detail);
				
			}
			
			
			log.info("【INF】" + pgmId + ":処理終了 取得件数=[" + Integer.toString(retForm.getShukakuList().size()) + "]件");
			return retForm;
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return null;
		}
	}
	
	
	
	// 詳細を取得(ハウスID、収穫日時指定)：ハウス収穫後に入力する収穫ケース数が存在し、それを更新するために詳細画面を表示する場合
	/*
		ハウス名     日付     収穫ケース数   収穫ケース数(QR)
		------------------------------------------------------
		３号隔離   2024/5/1    10  ケース     10  ケース	←入り欄画面でこいつを選択して詳細画面に遷移する際に、詳細画面に表示する情報を検索するメソッド
		３号隔離   2024/5/2                   10  ケース
		３号隔離   2024/5/3    10  ケース               	←入り欄画面でこいつを選択して詳細画面に遷移する際に、詳細画面に表示する情報を検索するメソッド
		３号隔離   2024/5/4    10  ケース     10  ケース	←入り欄画面でこいつを選択して詳細画面に遷移する際に、詳細画面に表示する情報を検索するメソッド
	 */
	public FormKanriMainteShukakuSumDetail getShukakuSumDetail(String houseId,LocalDate shukakuDate) {
		
		// 年月日での日付フォーマットを準備
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		
		String pgmId = classId + ".getShukakuSumDetail";
		log.info("【INF】" + pgmId + ":処理開始 ハウスID=[" + houseId + "]、収穫日=[" + shukakuDate + "]");
		
		
		// 返却値
		FormKanriMainteShukakuSumDetail retForm = new FormKanriMainteShukakuSumDetail();
		
		try {
			
			String sql = " select";
			sql  = sql + "     TT_SHUKAKU.HOUSEID";
			sql  = sql + "    ,TM_HOUSE.HOUSENAME";
			sql  = sql + "    ,DATE_FORMAT(TT_SHUKAKU.SHUKAKUDATE,'%Y%m%d') SHUKAKUDATE_STRING";
			sql  = sql + "    ,TT_SHUKAKU.BOXSUM";
			sql  = sql + "    ,TT_SHUKAKU_QR.BOXSUM BOXSUM_QR";
			sql  = sql + "    ,TT_SHUKAKU.BIKO";
			sql  = sql + " from";
			sql  = sql + "     TT_SHUKAKU_BOXSUM TT_SHUKAKU";
			// ハウス名を取得するSQL
			sql  = sql + " left join";
			sql  = sql + "     TM_HOUSE";
			sql  = sql + "     on";
			sql  = sql + "         TT_SHUKAKU.HOUSEID     = TM_HOUSE.HOUSEID";
			// ハウス作業で松岡さんやパートの方がQRコードで入力した収穫数を取得するSQL
			sql  = sql + " left join";
			sql  = sql + "     (";
			sql  = sql + "     select   HOUSEID";
			sql  = sql + "             ,date(ENDDATETIME) SHUKAKUDATE";
			sql  = sql + "             ,sum(BOXCOUNT) BOXSUM";
			sql  = sql + "     from     TT_HOUSE_WORKSTATUS_SHUKAKU";
			sql  = sql + "     where    STARTEMPLOYEEID <> '" + SpecialUser.TEST_USER + "'";
			sql  = sql + "     and      DELETEFLG <> 1";
			sql  = sql + "     and      ENDDATETIME is not null";
			sql  = sql + "     group by HOUSEID";
			sql  = sql + "             ,date(ENDDATETIME)";
			sql  = sql + "     ) TT_SHUKAKU_QR";
			sql  = sql + "     on";
			sql  = sql + "         TT_SHUKAKU.HOUSEID     = TT_SHUKAKU_QR.HOUSEID";
			sql  = sql + "     and TT_SHUKAKU.SHUKAKUDATE = TT_SHUKAKU_QR.SHUKAKUDATE";
			sql  = sql + " where";
			sql  = sql + "     TT_SHUKAKU.DELETEFLG <> 1";
			sql  = sql + " and TT_SHUKAKU.HOUSEID     = ?";
			sql  = sql + " and TT_SHUKAKU.SHUKAKUDATE = ?";
			
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
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql
																,houseId
																,formatter.format(shukakuDate)
																);
			
			
			// 【重要】この変数に作業開始、終了日時を文字列でセットし、作業状況の判定に使用する。
			String shukakuDateString = "";
			
			
			for (Map<String, Object> rs: rsList) {
				
				// 年月日時分秒までの日時フォーマットを準備
				//DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
				DateTimeFormatter formatterDate     = DateTimeFormatter.ofPattern("yyyyMMdd");
				//DateTimeFormatter formatterTime     = DateTimeFormatter.ofPattern("HHmmss");
				
				//String wkDate; //YYYYMMDDの文字列
				//String wkTime; //HHMMSSの文字列
				
				
				//------------------------------------------------
				// 表示情報
				
				// ハウスID
				retForm.setHouseId(rs.get("HOUSEID").toString());
				
				//ハウス名 ※ハウス名がマスタから取得できない場合は"未登録"と一覧表示
				if (rs.get("HOUSENAME") != null) {
					retForm.setHouseName(rs.get("HOUSENAME").toString());
				} else {
					retForm.setHouseName("未登録");
				}
				
				// 収穫日
				shukakuDateString = rs.get("SHUKAKUDATE_STRING").toString();
				retForm.setShukakuDate(LocalDate.parse(shukakuDateString,formatterDate));
				
				
				// 収穫ケース数 ※収穫作業”以外”である場合は収穫箱数はnullにしてる。その場合は空白表示
				if (rs.get("BOXSUM") != null) {
					retForm.setBoxSum(rs.get("BOXSUM").toString());
				}
				
				if (rs.get("BOXSUM_QR") != null) {
					retForm.setBoxSumQR(rs.get("BOXSUM_QR").toString());
				}
				
				// 備考
				if (rs.get("BIKO") != null) {
					retForm.setBiko(rs.get("BIKO").toString());
				}
				
				//1件のみ取得するためLOOPしない
				break;
			}
			
			
			//------------------------------------------------
			// データが取得できない場合、QRコードでパートさん／松岡さんが入力した収穫情報のみ取得
			if (retForm.getHouseId().equals("") == true) {
				
				retForm = this.getShukakuSumDetailQR(houseId, shukakuDate);
				
			}
			
			
			log.info("【INF】" + pgmId + ":処理終了");
			return retForm;
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return null;
		}
	}
	
	
	
	// 詳細を取得(ハウスID、作業開始日時指定)：ハウス収穫後に入力する収穫ケース数は存在しないが、ハウス作業で入力した収穫ケース数の情報は存在する場合
	/*
		ハウス名     日付     収穫ケース数   収穫ケース数(QR)
		------------------------------------------------------
		３号隔離   2024/5/1    10  ケース     10  ケース
		３号隔離   2024/5/2                   10  ケース	←入り欄画面でこいつを選択して詳細画面に遷移する際に、詳細画面に表示する情報を検索するメソッド
		３号隔離   2024/5/3    10  ケース               
		３号隔離   2024/5/4    10  ケース     10  ケース
	 */
	public FormKanriMainteShukakuSumDetail getShukakuSumDetailQR(String houseId,LocalDate shukakuDate) {
		
		// 年月日での日付フォーマットを準備
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		
		String pgmId = classId + ".getShukakuSumDetailQR";
		log.info("【INF】" + pgmId + ":処理開始 ハウスID=[" + houseId + "]、収穫日=[" + shukakuDate + "]");
		
		
		// 返却値
		FormKanriMainteShukakuSumDetail retForm = new FormKanriMainteShukakuSumDetail();
		
		try {
			
			
			//------------------------------------------------
			// QRコードでパートさん／松岡さんが入力した収穫情報のみ取得
			
			
			
			String sql = " select";
			sql  = sql + "    TT_SHUKAKU_QR.HOUSEID";
			sql  = sql + "   ,TM_HOUSE.HOUSENAME";
			sql  = sql + "   ,DATE_FORMAT(TT_SHUKAKU_QR.SHUKAKUDATE,'%Y%m%d') SHUKAKUDATE_STRING";
			sql  = sql + "   ,null BOXSUM";
			sql  = sql + "   ,TT_SHUKAKU_QR.BOXSUM_QR";
			sql  = sql + "   ,null BIKO";
			sql  = sql + " from";
			sql  = sql + "     (";
			sql  = sql + "     select";
			sql  = sql + "        HOUSEID";
			sql  = sql + "       ,date(ENDDATETIME) SHUKAKUDATE";
			sql  = sql + "       ,sum(BOXCOUNT) BOXSUM_QR";
			sql  = sql + "     from";
			sql  = sql + "        TT_HOUSE_WORKSTATUS_SHUKAKU";
			sql  = sql + "     where";
			sql  = sql + "         STARTEMPLOYEEID <> '" + SpecialUser.TEST_USER + "'";
			sql  = sql + "     and DELETEFLG <> 1";
			sql  = sql + "     and ENDDATETIME is not null";
			sql  = sql + "     and HOUSEID           = ?";
			sql  = sql + "     and date(ENDDATETIME) = ?";
			sql  = sql + "     group by";
			sql  = sql + "        HOUSEID";
			sql  = sql + "       ,date(ENDDATETIME)";
			sql  = sql + "     ) TT_SHUKAKU_QR";
			sql  = sql + " left join TM_HOUSE";
			sql  = sql + " on";
			sql  = sql + "     TT_SHUKAKU_QR.HOUSEID = TM_HOUSE.HOUSEID";
			
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
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql
																,houseId
																,formatter.format(shukakuDate)
																);
			
			
			// 【重要】この変数に作業開始、終了日時を文字列でセットし、作業状況の判定に使用する。
			String shukakuDateString = "";
			
			
			
			for (Map<String, Object> rs: rsList) {
				
				// 年月日時分秒までの日時フォーマットを準備
				//DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
				DateTimeFormatter formatterDate     = DateTimeFormatter.ofPattern("yyyyMMdd");
				//DateTimeFormatter formatterTime     = DateTimeFormatter.ofPattern("HHmmss");
				
				//String wkDate; //YYYYMMDDの文字列
				//String wkTime; //HHMMSSの文字列
				
				//------------------------------------------------
				// 表示情報
				
				// ハウスID
				retForm.setHouseId(rs.get("HOUSEID").toString());
				
				//ハウス名 ※ハウス名がマスタから取得できない場合は"未登録"と一覧表示
				if (rs.get("HOUSENAME") != null) {
					retForm.setHouseName(rs.get("HOUSENAME").toString());
				} else {
					retForm.setHouseName("未登録");
				}
				
				// 収穫日
				shukakuDateString = rs.get("SHUKAKUDATE_STRING").toString();
				retForm.setShukakuDate(LocalDate.parse(shukakuDateString,formatterDate));
				
				// 収穫ケース数 ※収穫作業”以外”である場合は収穫箱数はnullにしてる。その場合は空白表示
				if (rs.get("BOXSUM") != null) {
					retForm.setBoxSum(rs.get("BOXSUM").toString());
				}
				
				if (rs.get("BOXSUM_QR") != null) {
					retForm.setBoxSumQR(rs.get("BOXSUM_QR").toString());
				}
				
				// 備考
				if (rs.get("BIKO") != null) {
					retForm.setBiko(rs.get("BIKO").toString());
				}
				
				//1件のみ取得するためLOOPしない
				break;
			}
			
			
			
			log.info("【INF】" + pgmId + ":処理終了");
			return retForm;
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return null;
		}
	}
	
	
	
	// ハウス収穫後に入力する収穫ケース数の情報が存在するか否かを返却する
	/*
		ハウス名     日付     収穫ケース数   収穫ケース数(QR)
		------------------------------------------------------
		３号隔離   2024/5/1    10  ケース     10  ケース	←★収穫ケース数列に表示する情報が存在するか
		３号隔離   2024/5/2                   10  ケース
		３号隔離   2024/5/3    10  ケース               
		３号隔離   2024/5/4    10  ケース     10  ケース
	 */
	public boolean isExistsShukakuSum(String houseId,LocalDate shukakuDate) {
		
		String pgmId = classId + ".isExistsShukakuSum";
		log.info("【INF】" + pgmId + ":処理開始 ハウスID=[" + houseId + "]、収穫日=[" + shukakuDate + "]");
		
		try {
			
			String sql = " select";
			sql  = sql + "    count(1) CNT";
			sql  = sql + " from";
			sql  = sql + "     TT_SHUKAKU_BOXSUM";
			sql  = sql + " where";
			sql  = sql + "     DELETEFLG  <> 1";
			sql  = sql + " and HOUSEID     = ?";
			sql  = sql + " and SHUKAKUDATE = ?";
			
			
			// queryForListメソッドでSQLを実行
			int count = this.jdbcTemplate.queryForObject(sql
											,Integer.class
											,houseId
											,shukakuDate
											);
			
			log.info("【INF】" + pgmId + ":処理終了 件数=[" + Integer.toString(count) + "]");
			
			return count > 0;
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了[" + e.toString() + "]");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return false;
		}
	}
	
	
	
	// ハウス収穫時に入力する収穫ケース数(QR)の情報が存在するか否かを返却する
	/*
		ハウス名     日付     収穫ケース数   収穫ケース数(QR)
		------------------------------------------------------
		３号隔離   2024/5/1    10  ケース     10  ケース	←収穫ケース数(QR)列に表示する情報が存在するか
		３号隔離   2024/5/2                   10  ケース
		３号隔離   2024/5/3    10  ケース               
		３号隔離   2024/5/4    10  ケース     10  ケース
	 */
	public boolean isExistsShukakuSumQR(String houseId,LocalDate shukakuDate) {
		
		String pgmId = classId + ".isExistsShukakuSumQR";
		log.info("【INF】" + pgmId + ":処理開始 ハウスID=[" + houseId + "]、収穫日=[" + shukakuDate + "]");
		
		
		try {
			
			
			//------------------------------------------------
			// QRコードでパートさん／松岡さんが入力した収穫情報のみ取得
			
			String sql = " select";
			sql  = sql + "    count(1) CNT";
			sql  = sql + " from";
			sql  = sql + "     TT_HOUSE_WORKSTATUS_SHUKAKU";
			sql  = sql + " where";
			sql  = sql + "     STARTEMPLOYEEID  <> '" + SpecialUser.TEST_USER + "'";
			sql  = sql + " and DELETEFLG        <> 1";
			sql  = sql + " and HOUSEID           = ?";
			sql  = sql + " and ENDDATETIME is not null";
			sql  = sql + " and date(ENDDATETIME) = ?";
			
			
			// queryForListメソッドでSQLを実行
			int count = this.jdbcTemplate.queryForObject(sql
											,Integer.class
											,houseId
											,shukakuDate
											);
			
			log.info("【INF】" + pgmId + ":処理終了 件数=[" + Integer.toString(count) + "]");
			
			return count > 0;
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了[" + e.toString() + "]");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return false;
		}
	}
	
	// データ登録
	public boolean registShukakuSum(FormKanriMainteShukakuSumDetail detail ,String userName,String registPgmId) {
		
		// 年月日までのフォーマットを準備
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		String pgmId = classId + ".registShukakuSum";
		log.info("【INF】" + pgmId + ":処理開始 ハウスID=[" + detail.getHouseId() + "]、収穫日=[" + detail.getShukakuDate() + "]、収穫ケース数=[" + detail.getBoxSum() + "]");
		
		try {
			
			int ret = 0;
			
			// すでにデータが存在する場合、過去のデータを論理削除してから登録を行う
			//
			// ※新規登録ボタンを押下、すでにデータの存在するハウス・収穫日を入力して登録ボタンを押下した場合に必要な処理
			//
			if (isExistsShukakuSum(detail.getHouseId(), detail.getShukakuDate()) == true) {
				
				deleteShukakuSum(detail ,userName,registPgmId);
			}
			
			
			//------------------------------------------------
			// 収穫ケース数合計テーブルの登録
			
			
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
			sql  = sql + "     ?";     //HOUSEID
			sql  = sql + "    ,?";     //SHUKAKUDATE
			sql  = sql + "    ,?";     //REGISTEMPLOYEEID
			sql  = sql + "    ,current_timestamp(3)"; //REGISTDATETIME
			sql  = sql + "    ,?";     //BIKO
			sql  = sql + "    ,?";     //BOXSUM
			sql  = sql + "    ,0";     //DELETEFLG
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,current_timestamp(3)";
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,current_timestamp(3)";
			sql  = sql + " )";
			
			ret = this.jdbcTemplate.update(sql
					,detail.getHouseId()
					,formatter.format(detail.getShukakuDate())
					,SpecialUser.KANRI_USER
					,detail.getBiko()
					,detail.getBoxSum()
					,userName
					,registPgmId
					,userName
					,registPgmId);
			
			// メモ：commitはjdbcTemplateが自動で行ってくれる
			
			log.info("【INF】" + pgmId + ":処理終了 ret=[" + ret + "]");
			
			return true;
			
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了[" + e.toString() + "]");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return false;
		}
	}
	
	// データ更新
	public boolean updateShukakuSum(FormKanriMainteShukakuSumDetail detail ,String userName,String registPgmId) {
		
		// 年月日までのフォーマットを準備
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		String pgmId = classId + ".updateShukakuSum";
		log.info("【INF】" + pgmId + ":処理開始 ハウスID=[" + detail.getHouseId() + "]、収穫日=[" + detail.getShukakuDate() + "]、収穫ケース数=[" + detail.getBoxSum() + "]");
		
		try {
			
			int ret = 0;
			
			
			//------------------------------------------------
			// 収穫ケース数合計テーブルの更新
			
			String sql = " update TT_SHUKAKU_BOXSUM";
			sql  = sql + " set";
			sql  = sql + "     BOXSUM           = ?";
			sql  = sql + "    ,BIKO             = ?";
			sql  = sql + "    ,SYSUPDUSERID     = ?";
			sql  = sql + "    ,SYSUPDPGMID      = ?";
			sql  = sql + "    ,SYSUPDYMDHMS     = current_timestamp(3)";
			sql  = sql + " where";
			sql  = sql + "     HOUSEID          = ?";
			sql  = sql + " and SHUKAKUDATE      = ?";
			sql  = sql + " and DELETEFLG        = 0"; //同じハウス、収穫日で削除されていないデータは最新の1件のみ。その最新データを更新する
			
			ret = this.jdbcTemplate.update(sql
					,detail.getBoxSum()
					,detail.getBiko()
					,userName
					,registPgmId
					,detail.getHouseId()
					,formatter.format(detail.getShukakuDate())
					);
			
			
			
			// メモ：commitはjdbcTemplateが自動で行ってくれる
			
			log.info("【INF】" + pgmId + ":処理終了 ret=[" + ret + "]");
			
			// 更新件数０件である場合はNGを返却
			if (ret == 0) {
				return false;
			}
			return true;
			
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了[" + e.toString() + "]");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return false;
		}
	}
	
	
	
	
	// データゼロクリア
	public boolean zeroClearShukakuSum(FormKanriMainteShukakuSumDetail detail ,String userName,String registPgmId) {
		
		// 年月日までのフォーマットを準備
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		String pgmId = classId + ".zeroClearShukakuSum";
		log.info("【INF】" + pgmId + ":処理開始 ハウスID=[" + detail.getHouseId() + "]、収穫日=[" + detail.getShukakuDate() + "]");
		
		try {
			
			int ret = 0;
			
			
			//------------------------------------------------
			// 収穫ケース数合計テーブルの更新
			
			String sql = " update TT_SHUKAKU_BOXSUM";
			sql  = sql + " set";
			sql  = sql + "     BOXSUM           = 0";  //収穫ケース数0件で更新＝0クリア
			sql  = sql + "    ,BIKO             = ?";
			sql  = sql + "    ,SYSUPDUSERID     = ?";
			sql  = sql + "    ,SYSUPDPGMID      = ?";
			sql  = sql + "    ,SYSUPDYMDHMS     = current_timestamp(3)";
			sql  = sql + " where";
			sql  = sql + "     HOUSEID          = ?";
			sql  = sql + " and SHUKAKUDATE      = ?";
			sql  = sql + " and DELETEFLG        = 0"; //同じハウス、収穫日で削除されていないデータは最新の1件のみ。その最新データを更新する
			
			ret = this.jdbcTemplate.update(sql
					,detail.getBiko()
					,userName
					,registPgmId
					,detail.getHouseId()
					,formatter.format(detail.getShukakuDate())
					);
			
			
			
			// メモ：commitはjdbcTemplateが自動で行ってくれる
			
			log.info("【INF】" + pgmId + ":処理終了 ret=[" + ret + "]");
			
			// 更新件数０件である場合はNGを返却
			if (ret == 0) {
				return false;
			}
			return true;
			
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了[" + e.toString() + "]");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return false;
		}
	}
	
	
	
	
	// データ削除(物理削除はしない)
	public boolean deleteShukakuSum(FormKanriMainteShukakuSumDetail detail ,String userName,String registPgmId) {
		
		// 年月日までのフォーマットを準備
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		String pgmId = classId + ".deleteShukakuSum";
		log.info("【INF】" + pgmId + ":処理開始 ハウスID=[" + detail.getHouseId() + "]、収穫日=[" + detail.getShukakuDate() + "]");
		
		try {
			
			int ret = 0;
			
			
			//------------------------------------------------
			// 収穫ケース数合計テーブルの更新
			
			String sql = " update TT_SHUKAKU_BOXSUM";
			sql  = sql + " set";
			sql  = sql + "     DELETEFLG        = 1";  //収穫ケース数論理削除
			sql  = sql + "    ,SYSUPDUSERID     = ?";
			sql  = sql + "    ,SYSUPDPGMID      = ?";
			sql  = sql + "    ,SYSUPDYMDHMS     = current_timestamp(3)";
			sql  = sql + " where";
			sql  = sql + "     HOUSEID          = ?";
			sql  = sql + " and SHUKAKUDATE      = ?";
			sql  = sql + " and DELETEFLG        = 0"; //同じハウス、収穫日で削除されていないデータは最新の1件のみ。その最新データを更新する
			
			ret = this.jdbcTemplate.update(sql
					,userName
					,registPgmId
					,detail.getHouseId()
					,formatter.format(detail.getShukakuDate())
					);
			
			
			
			// メモ：commitはjdbcTemplateが自動で行ってくれる
			
			log.info("【INF】" + pgmId + ":処理終了 ret=[" + ret + "]");
			
			// 更新件数０件である場合はNGを返却
			if (ret == 0) {
				return false;
			}
			return true;
			
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了[" + e.toString() + "]");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return false;
		}
	}
	
}
