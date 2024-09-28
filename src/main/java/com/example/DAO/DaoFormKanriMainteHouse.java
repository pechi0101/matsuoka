package com.example.DAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.example.form.FormKanriMainteHouseDetail;
import com.example.form.FormKanriMainteHouseList;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DaoFormKanriMainteHouse {
	
	private JdbcTemplate  jdbcTemplate;
	private String classId = "DaoFormKanriMainteHouse";
	
	// コンストラクタ
	public DaoFormKanriMainteHouse(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	// 全件を取得
	public FormKanriMainteHouseList getAllHouseData() {
		
		String pgmId = classId + ".getAllHouseData";
		log.info("【INF】" + pgmId + ":処理開始");
		
		
		// 返却値
		FormKanriMainteHouseList retForm = new FormKanriMainteHouseList();
		
		try {
			
			String sql = " select";
			sql  = sql + "     TM_HOUSE.HOUSEID";
			sql  = sql + "    ,TM_HOUSE.HOUSENAME";
			sql  = sql + "    ,ifnull(TM_HOUSECOL.CNT,0) CNT";
			sql  = sql + "    ,TM_HOUSE.BOXSUM_YTI_01";
			sql  = sql + "    ,TM_HOUSE.BOXSUM_YTI_02";
			sql  = sql + "    ,TM_HOUSE.BOXSUM_YTI_03";
			sql  = sql + "    ,TM_HOUSE.BOXSUM_YTI_04";
			sql  = sql + "    ,TM_HOUSE.BOXSUM_YTI_05";
			sql  = sql + "    ,TM_HOUSE.BOXSUM_YTI_06";
			sql  = sql + "    ,TM_HOUSE.BOXSUM_YTI_07";
			sql  = sql + "    ,TM_HOUSE.BOXSUM_YTI_08";
			sql  = sql + "    ,TM_HOUSE.BOXSUM_YTI_09";
			sql  = sql + "    ,TM_HOUSE.BOXSUM_YTI_10";
			sql  = sql + "    ,TM_HOUSE.BOXSUM_YTI_11";
			sql  = sql + "    ,TM_HOUSE.BOXSUM_YTI_12";
			sql  = sql + "    ,TM_HOUSE.BIKO";
			sql  = sql + " from";
			sql  = sql + "     TM_HOUSE";
			sql  = sql + " left outer join";
			sql  = sql + "     (select HOUSEID,count(*) CNT from TM_HOUSECOL group by HOUSEID) TM_HOUSECOL";
			sql  = sql + " on  TM_HOUSECOL.HOUSEID  = TM_HOUSE.HOUSEID";
			sql  = sql + " where";
			sql  = sql + "     TM_HOUSE.DELETEFLG = False";
			sql  = sql + " order by";
			sql  = sql + "     TM_HOUSE.HOUSEID";
			
			
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
				
				
				FormKanriMainteHouseDetail detail = new FormKanriMainteHouseDetail();
				
				// ハウスID
				detail.setHouseId(rs.get("HOUSEID").toString());
				// ハウス名
				detail.setHouseName(rs.get("HOUSENAME").toString());
				
				// 列数
				detail.setColCount(rs.get("CNT").toString());
				
				// 収穫予定ケース数
				detail.setBoxSumYTI01(Double.parseDouble(rs.get("BOXSUM_YTI_01").toString()));
				detail.setBoxSumYTI02(Double.parseDouble(rs.get("BOXSUM_YTI_02").toString()));
				detail.setBoxSumYTI03(Double.parseDouble(rs.get("BOXSUM_YTI_03").toString()));
				detail.setBoxSumYTI04(Double.parseDouble(rs.get("BOXSUM_YTI_04").toString()));
				detail.setBoxSumYTI05(Double.parseDouble(rs.get("BOXSUM_YTI_05").toString()));
				detail.setBoxSumYTI06(Double.parseDouble(rs.get("BOXSUM_YTI_06").toString()));
				detail.setBoxSumYTI07(Double.parseDouble(rs.get("BOXSUM_YTI_07").toString()));
				detail.setBoxSumYTI08(Double.parseDouble(rs.get("BOXSUM_YTI_08").toString()));
				detail.setBoxSumYTI09(Double.parseDouble(rs.get("BOXSUM_YTI_09").toString()));
				detail.setBoxSumYTI10(Double.parseDouble(rs.get("BOXSUM_YTI_10").toString()));
				detail.setBoxSumYTI11(Double.parseDouble(rs.get("BOXSUM_YTI_11").toString()));
				detail.setBoxSumYTI12(Double.parseDouble(rs.get("BOXSUM_YTI_12").toString()));
				
				
				// 備考
				if (rs.get("BIKO") != null) {
					detail.setBiko(rs.get("BIKO").toString());
				}else{
					detail.setBiko("");
				}
				
				retForm.addHouse(detail);
				
			}
			
			// ------------------------------------------------
			// 同名のハウスが存在しないかチェック
			
			ArrayList<FormKanriMainteHouseDetail> list = retForm.getHouseList();
			
			
			for (int index1 = 0 ; index1 < list.size(); index1++) {
				
				for (int index2 = index1+1 ; index2 < list.size(); index2++) {
					
					String houseName1 = list.get(index1).getHouseName();
					String houseName2 = list.get(index2).getHouseName();
					
					houseName1 = houseName1.replaceAll("[ 　]", ""); // 正規表現で半角スペースと全角スペースを除去
					houseName2 = houseName2.replaceAll("[ 　]", ""); // 正規表現で半角スペースと全角スペースを除去
					
					//log.info("【DBG】ハウス名１=[" + houseName1 + "]ハウス名２=[" + houseName2 + "]");
					
					if (houseName1.equals(houseName2) == true) {
						//同名のハウスが存在する場合、メッセージをセット
						retForm.setMessage("[ " + houseName1 + " ]同名のハウス情報が複数存在します。確認のうえ修正／削除してください。");
						//for文を脱出するようindexをセット
						index1 = list.size();
						break;
					}
				}
			}
			
			
			
			
			log.info("【INF】" + pgmId + ":処理終了 取得件数=[" + Integer.toString(retForm.getHouseList().size()) + "]件");
			return retForm;
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return null;
		}
	}
	
	
	// 詳細を取得(ハウスID指定)
	public FormKanriMainteHouseDetail getTargetHouseData(String houseId) {
		
		String pgmId = classId + ".getTargetHouseData";
		log.info("【INF】" + pgmId + ":処理開始 ハウスID=[" + houseId + "]");
		
		
		// 返却値
		FormKanriMainteHouseDetail retForm = new FormKanriMainteHouseDetail();
		
		try {
			
			String sql = " select";
			sql  = sql + "     TM_HOUSE.HOUSEID";
			sql  = sql + "    ,TM_HOUSE.HOUSENAME";
			sql  = sql + "    ,ifnull(TM_HOUSECOL.CNT,0) CNT";
			sql  = sql + "    ,TM_HOUSE.BOXSUM_YTI_01";
			sql  = sql + "    ,TM_HOUSE.BOXSUM_YTI_02";
			sql  = sql + "    ,TM_HOUSE.BOXSUM_YTI_03";
			sql  = sql + "    ,TM_HOUSE.BOXSUM_YTI_04";
			sql  = sql + "    ,TM_HOUSE.BOXSUM_YTI_05";
			sql  = sql + "    ,TM_HOUSE.BOXSUM_YTI_06";
			sql  = sql + "    ,TM_HOUSE.BOXSUM_YTI_07";
			sql  = sql + "    ,TM_HOUSE.BOXSUM_YTI_08";
			sql  = sql + "    ,TM_HOUSE.BOXSUM_YTI_09";
			sql  = sql + "    ,TM_HOUSE.BOXSUM_YTI_10";
			sql  = sql + "    ,TM_HOUSE.BOXSUM_YTI_11";
			sql  = sql + "    ,TM_HOUSE.BOXSUM_YTI_12";
			sql  = sql + "    ,TM_HOUSE.BIKO";
			sql  = sql + " from";
			sql  = sql + "     TM_HOUSE";
			sql  = sql + " left outer join";
			sql  = sql + "     (select HOUSEID,count(*) CNT from TM_HOUSECOL group by HOUSEID) TM_HOUSECOL";
			sql  = sql + " on  TM_HOUSECOL.HOUSEID  = TM_HOUSE.HOUSEID";
			sql  = sql + " where";
			sql  = sql + "     TM_HOUSE.HOUSEID     = ?";
			sql  = sql + " and TM_HOUSE.DELETEFLG   = False";
			
			//１件のみ取得であるためソートは不要
			//sql  = sql + " order by";
			//sql  = sql + "     TM_HOUSE.HOUSEID";
			
			
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
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql,houseId);
			
			
			for (Map<String, Object> rs: rsList) {
				
				
				// ハウスID
				retForm.setHouseId(rs.get("HOUSEID").toString());
				// ハウス名
				retForm.setHouseName(rs.get("HOUSENAME").toString());
				
				// 列数
				retForm.setColCount(rs.get("CNT").toString());
				
				// 収穫予定ケース数
				retForm.setBoxSumYTI01(Double.parseDouble(rs.get("BOXSUM_YTI_01").toString()));
				retForm.setBoxSumYTI02(Double.parseDouble(rs.get("BOXSUM_YTI_02").toString()));
				retForm.setBoxSumYTI03(Double.parseDouble(rs.get("BOXSUM_YTI_03").toString()));
				retForm.setBoxSumYTI04(Double.parseDouble(rs.get("BOXSUM_YTI_04").toString()));
				retForm.setBoxSumYTI05(Double.parseDouble(rs.get("BOXSUM_YTI_05").toString()));
				retForm.setBoxSumYTI06(Double.parseDouble(rs.get("BOXSUM_YTI_06").toString()));
				retForm.setBoxSumYTI07(Double.parseDouble(rs.get("BOXSUM_YTI_07").toString()));
				retForm.setBoxSumYTI08(Double.parseDouble(rs.get("BOXSUM_YTI_08").toString()));
				retForm.setBoxSumYTI09(Double.parseDouble(rs.get("BOXSUM_YTI_09").toString()));
				retForm.setBoxSumYTI10(Double.parseDouble(rs.get("BOXSUM_YTI_10").toString()));
				retForm.setBoxSumYTI11(Double.parseDouble(rs.get("BOXSUM_YTI_11").toString()));
				retForm.setBoxSumYTI12(Double.parseDouble(rs.get("BOXSUM_YTI_12").toString()));
				
				
				// 備考
				if (rs.get("BIKO") != null) {
					retForm.setBiko(rs.get("BIKO").toString());
				}else{
					retForm.setBiko("");
				}
				
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
	
	
	
	
	
	// ハウスIDを採番
	private String getNewIdentifier() {
		
		String pgmId = classId + ".getNewIdentifier";
		log.info("【INF】" + pgmId + ":処理開始");
		
		
		// 返却値
		String strNewIdentifier = "";
		
		try {
			
			// ハウスIDのMAX＋１を新規ハウスIDにする
			String sql = " select";
			sql  = sql + "     MAX(TM_HOUSE.HOUSEID) HOUSEID";
			sql  = sql + " from";
			sql  = sql + "     TM_HOUSE";
			
			// queryForListメソッドでSQLを実行し、結果MapのListで受け取る。
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql);
			
			
			for (Map<String, Object> rs: rsList) {
				
				// MAXハウスID＋１
				strNewIdentifier = Integer.toString(Integer.parseInt(rs.get("HOUSEID").toString()) + 1);
				
				break;
			}
			
			
			// １件も取得できない場合(登録済みハウスが０個の場合)はハウスIDの初期値を返却
			if (strNewIdentifier == null || "".equals(strNewIdentifier) == true) {
				strNewIdentifier = "10001";
			}
			
			
			
			log.info("【INF】" + pgmId + ":処理終了 採番ID=[" + strNewIdentifier + "]");
			return strNewIdentifier;
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return null;
		}
	}
	
	
	
	
	
	// データ登録
	public boolean registHouse(FormKanriMainteHouseDetail houseDetail ,String userName,String registPgmId) {
		
		String pgmId = classId + ".registHouse";
		log.info("【INF】" + pgmId + ":処理開始 □登録ハウス=[" + houseDetail.getHouseName() + "]");
		
		
		//新規IDを採番
		String strNewIdentifier = this.getNewIdentifier();
		houseDetail.setHouseId(strNewIdentifier);
		
		
		
		//ハウス情報を登録
		try {
			String sql = " insert into TM_HOUSE (";
			sql  = sql + "     HOUSEID";
			sql  = sql + "    ,HOUSENAME";
			sql  = sql + "    ,BOXSUM_YTI_01";
			sql  = sql + "    ,BOXSUM_YTI_02";
			sql  = sql + "    ,BOXSUM_YTI_03";
			sql  = sql + "    ,BOXSUM_YTI_04";
			sql  = sql + "    ,BOXSUM_YTI_05";
			sql  = sql + "    ,BOXSUM_YTI_06";
			sql  = sql + "    ,BOXSUM_YTI_07";
			sql  = sql + "    ,BOXSUM_YTI_08";
			sql  = sql + "    ,BOXSUM_YTI_09";
			sql  = sql + "    ,BOXSUM_YTI_10";
			sql  = sql + "    ,BOXSUM_YTI_11";
			sql  = sql + "    ,BOXSUM_YTI_12";
			sql  = sql + "    ,BIKO";
			sql  = sql + "    ,DELETEFLG";
			sql  = sql + "    ,DELETEYMDHMS";
			sql  = sql + "    ,SYSREGUSERID";
			sql  = sql + "    ,SYSREGPGMID";
			sql  = sql + "    ,SYSREGYMDHMS";
			sql  = sql + "    ,SYSUPDUSERID";
			sql  = sql + "    ,SYSUPDPGMID";
			sql  = sql + "    ,SYSUPDYMDHMS";
			sql  = sql + " )values(";
			sql  = sql + "     ?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";    //収穫予定ケース数01
			sql  = sql + "    ,?";    //収穫予定ケース数02
			sql  = sql + "    ,?";    //収穫予定ケース数03
			sql  = sql + "    ,?";    //収穫予定ケース数04
			sql  = sql + "    ,?";    //収穫予定ケース数05
			sql  = sql + "    ,?";    //収穫予定ケース数06
			sql  = sql + "    ,?";    //収穫予定ケース数07
			sql  = sql + "    ,?";    //収穫予定ケース数08
			sql  = sql + "    ,?";    //収穫予定ケース数09
			sql  = sql + "    ,?";    //収穫予定ケース数10
			sql  = sql + "    ,?";    //収穫予定ケース数11
			sql  = sql + "    ,?";    //収穫予定ケース数12
			sql  = sql + "    ,?";    //備考
			sql  = sql + "    ,0";    //削除フラグは0(false)で登録
			sql  = sql + "    ,null"; //削除日時はnull
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,current_timestamp(3)";
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,current_timestamp(3)";
			sql  = sql + " )";
			
			int ret = this.jdbcTemplate.update(sql
					,houseDetail.getHouseId()
					,houseDetail.getHouseName()
					,houseDetail.getBoxSumYTI01()
					,houseDetail.getBoxSumYTI02()
					,houseDetail.getBoxSumYTI03()
					,houseDetail.getBoxSumYTI04()
					,houseDetail.getBoxSumYTI05()
					,houseDetail.getBoxSumYTI06()
					,houseDetail.getBoxSumYTI07()
					,houseDetail.getBoxSumYTI08()
					,houseDetail.getBoxSumYTI09()
					,houseDetail.getBoxSumYTI10()
					,houseDetail.getBoxSumYTI11()
					,houseDetail.getBoxSumYTI12()
					,houseDetail.getBiko()
					,userName
					,registPgmId
					,userName
					,registPgmId);
			
			
			
			
			
			//ハウス列情報を登録(入力された列数分登録)
			
			//未入力である場合は0とする
			if (houseDetail.getColCount() == null || "".equals(houseDetail.getColCount()) == true) {
				houseDetail.setColCount("0");
			}
			
			
			for (int index = 1; index <= Integer.parseInt(houseDetail.getColCount()); index++) {
				
				log.info("【INF】" + pgmId + ":ハウスID=[" + houseDetail.getHouseId() + "]、列No=[" + String.format("%02d", index) + "]を登録");
				
				sql        = " insert into TM_HOUSECOL (";
				sql  = sql + "     HOUSEID";
				sql  = sql + "    ,COLNO";
				sql  = sql + "    ,BIKO";
				sql  = sql + "    ,DELETEFLG";
				sql  = sql + "    ,DELETEYMDHMS";
				sql  = sql + "    ,SYSREGUSERID";
				sql  = sql + "    ,SYSREGPGMID";
				sql  = sql + "    ,SYSREGYMDHMS";
				sql  = sql + "    ,SYSUPDUSERID";
				sql  = sql + "    ,SYSUPDPGMID";
				sql  = sql + "    ,SYSUPDYMDHMS";
				sql  = sql + " )values(";
				sql  = sql + "     ?";
				sql  = sql + "    ,?"; // 列No
				sql  = sql + "    ,null"; // 備考(未使用のためnullで登録)
				sql  = sql + "    ,0"; // 削除フラグは0(false)で登録
				sql  = sql + "    ,null"; //削除日時はnullで登録
				sql  = sql + "    ,?";
				sql  = sql + "    ,?";
				sql  = sql + "    ,current_timestamp(3)";
				sql  = sql + "    ,?";
				sql  = sql + "    ,?";
				sql  = sql + "    ,current_timestamp(3)";
				sql  = sql + " )";
				
				ret = this.jdbcTemplate.update(sql
						,houseDetail.getHouseId()
						,String.format("%02d", index) //列Noはただの連番(頭0サプレスで２桁の文字列)
						,userName
						,registPgmId
						,userName
						,registPgmId);
				
			}
			
			
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
	
	
	
	
	
	// データ更新
	public boolean updateHouse(FormKanriMainteHouseDetail houseDetail ,String userName,String registPgmId) {
		
		String pgmId = classId + ".updateHouse";
		log.info("【INF】" + pgmId + ":処理開始");
		
		try {
			
			//------------------------------------------------
			// ハウス情報の更新
			
			String sql = " update TM_HOUSE";
			sql  = sql + " set";
			sql  = sql + "     HOUSENAME        = ?";
			sql  = sql + "    ,BOXSUM_YTI_01    = ?";
			sql  = sql + "    ,BOXSUM_YTI_02    = ?";
			sql  = sql + "    ,BOXSUM_YTI_03    = ?";
			sql  = sql + "    ,BOXSUM_YTI_04    = ?";
			sql  = sql + "    ,BOXSUM_YTI_05    = ?";
			sql  = sql + "    ,BOXSUM_YTI_06    = ?";
			sql  = sql + "    ,BOXSUM_YTI_07    = ?";
			sql  = sql + "    ,BOXSUM_YTI_08    = ?";
			sql  = sql + "    ,BOXSUM_YTI_09    = ?";
			sql  = sql + "    ,BOXSUM_YTI_10    = ?";
			sql  = sql + "    ,BOXSUM_YTI_11    = ?";
			sql  = sql + "    ,BOXSUM_YTI_12    = ?";
			sql  = sql + "    ,BIKO             = ?";
			sql  = sql + "    ,SYSUPDUSERID     = ?";
			sql  = sql + "    ,SYSUPDPGMID      = ?";
			sql  = sql + "    ,SYSUPDYMDHMS     = current_timestamp(3)";
			sql  = sql + " where";
			sql  = sql + "     HOUSEID          = ?";
			
			int ret = this.jdbcTemplate.update(sql
					,houseDetail.getHouseName()
					,houseDetail.getBoxSumYTI01()
					,houseDetail.getBoxSumYTI02()
					,houseDetail.getBoxSumYTI03()
					,houseDetail.getBoxSumYTI04()
					,houseDetail.getBoxSumYTI05()
					,houseDetail.getBoxSumYTI06()
					,houseDetail.getBoxSumYTI07()
					,houseDetail.getBoxSumYTI08()
					,houseDetail.getBoxSumYTI09()
					,houseDetail.getBoxSumYTI10()
					,houseDetail.getBoxSumYTI11()
					,houseDetail.getBoxSumYTI12()
					,houseDetail.getBiko()
					,userName
					,registPgmId
					,houseDetail.getHouseId()
					);
			
			
			
			//------------------------------------------------
			// 列情報の更新
			
			//※一旦全削除してから再登録
			
			
			sql        = " delete from TM_HOUSECOL";
			sql  = sql + " where";
			sql  = sql + "     HOUSEID    = ?";
			
			
			ret = this.jdbcTemplate.update(sql
					,houseDetail.getHouseId()
					);
			
			
			
			//ハウス列情報を登録(入力された列数分登録)
			
			//未入力である場合は0とする
			if (houseDetail.getColCount() == null || "".equals(houseDetail.getColCount()) == true) {
				houseDetail.setColCount("0");
			}
			
			
			for (int index = 1; index <= Integer.parseInt(houseDetail.getColCount()); index++) {
				
				log.info("【INF】" + pgmId + ":ハウスID=[" + houseDetail.getHouseId() + "]、列No=[" + String.format("%02d", index) + "]を登録");
				
				sql        = " insert into TM_HOUSECOL (";
				sql  = sql + "     HOUSEID";
				sql  = sql + "    ,COLNO";
				sql  = sql + "    ,BIKO";
				sql  = sql + "    ,DELETEFLG";
				sql  = sql + "    ,DELETEYMDHMS";
				sql  = sql + "    ,SYSREGUSERID";
				sql  = sql + "    ,SYSREGPGMID";
				sql  = sql + "    ,SYSREGYMDHMS";
				sql  = sql + "    ,SYSUPDUSERID";
				sql  = sql + "    ,SYSUPDPGMID";
				sql  = sql + "    ,SYSUPDYMDHMS";
				sql  = sql + " )values(";
				sql  = sql + "     ?";
				sql  = sql + "    ,?"; // 列No
				sql  = sql + "    ,null"; // 備考(未使用のためnullで登録)
				sql  = sql + "    ,0"; // 削除フラグは0(false)で登録
				sql  = sql + "    ,null"; //削除日時はnullで登録
				sql  = sql + "    ,?";
				sql  = sql + "    ,?";
				sql  = sql + "    ,current_timestamp(3)";
				sql  = sql + "    ,?";
				sql  = sql + "    ,?";
				sql  = sql + "    ,current_timestamp(3)";
				sql  = sql + " )";
				
				ret = this.jdbcTemplate.update(sql
						,houseDetail.getHouseId()
						,String.format("%02d", index) //列Noはただの連番(頭0サプレスで２桁の文字列)
						,userName
						,registPgmId
						,userName
						,registPgmId);
				
			}
			
			
			
			// メモ：commitはjdbcTemplateが自動で行ってくれる
			
			log.info("【INF】" + pgmId + ":処理終了 ret=[" + ret + "]");
			
			// 更新件数０件である場合はNGを返却
			if (ret == 0) {
				return false;
			}
			return true;
			
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return false;
		}
	}
	
	
	
	
	
	
	// データ削除（物理削除は行わない）
	public boolean deleteHouse(FormKanriMainteHouseDetail houseDetail ,String userName,String registPgmId) {
		
		String pgmId = classId + ".deleteHouse";
		log.info("【INF】" + pgmId + ":処理開始");
		
		try {
			
			//------------------------------------------------
			// ハウス情報の削除
			
			String sql = " update TM_HOUSE";
			sql  = sql + " set";
			sql  = sql + "     BIKO             = ?";
			sql  = sql + "    ,DELETEFLG        = 1";    //削除フラグは1(true)で更新
			sql  = sql + "    ,DELETEYMDHMS     = current_timestamp(3)"; //削除日時は現在日時
			sql  = sql + "    ,SYSUPDUSERID     = ?";
			sql  = sql + "    ,SYSUPDPGMID      = ?";
			sql  = sql + "    ,SYSUPDYMDHMS     = current_timestamp(3)";
			sql  = sql + " where";
			sql  = sql + "     HOUSEID       = ?";
			
			int ret = this.jdbcTemplate.update(sql
					,houseDetail.getBiko()
					,userName
					,registPgmId
					,houseDetail.getHouseId()
					);
			
			
			
			//------------------------------------------------
			// 列情報の更新
			
			//※一旦全削除してから再登録
			
			
			sql        = " delete from TM_HOUSECOL";
			sql  = sql + " where";
			sql  = sql + "     HOUSEID    = ?";
			
			
			ret = this.jdbcTemplate.update(sql
					,houseDetail.getHouseId()
					);
			
			
			
			// メモ：commitはjdbcTemplateが自動で行ってくれる
			
			log.info("【INF】" + pgmId + ":処理終了 ret=[" + ret + "]");
			
			// 更新件数０件である場合はNGを返却
			if (ret == 0) {
				return false;
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
