package com.example.DAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.example.counst.SpecialWork;
import com.example.form.FormKanriMainteWorkDetail;
import com.example.form.FormKanriMainteWorkList;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DaoFormKanriMainteWork {
	
	private JdbcTemplate  jdbcTemplate;
	private String classId = "DaoFormKanriMainteWork";
	
	// コンストラクタ
	public DaoFormKanriMainteWork(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	// 全件を取得
	public FormKanriMainteWorkList getAllWorkData() {
		
		String pgmId = classId + ".getAllWorkData";
		log.info("【INF】" + pgmId + ":処理開始");
		
		
		// 返却値
		FormKanriMainteWorkList retForm = new FormKanriMainteWorkList();
		
		try {
			
			String sql = " select";
			sql  = sql + "     TM_WORK.WORKID";
			sql  = sql + "    ,TM_WORK.WORKNAME";
			sql  = sql + "    ,TM_WORK.WORKKBN";
			sql  = sql + "    ,ifnull(TM_WORK.SEPARATE_PERCENT,0) SEPARATE_PERCENT";
			sql  = sql + "    ,ifnull(TM_WORK.RESET_SPAN,0) RESET_SPAN";
			sql  = sql + "    ,TM_WORK.BIKO";
			sql  = sql + " from";
			sql  = sql + "     TM_WORK";
			sql  = sql + " where";
			sql  = sql + "     TM_WORK.WORKID not in (?, ?)"; // 収穫と収穫数合計入力はメンテナンス対象外
			sql  = sql + " order by";
			sql  = sql + "     TM_WORK.WORKID";
			
			
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
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql,SpecialWork.SHUKAKU ,SpecialWork.SHUKAKU_SUM);
			
			
			for (Map<String, Object> rs: rsList) {
				
				
				FormKanriMainteWorkDetail detail = new FormKanriMainteWorkDetail();
				
				// 作業ID
				detail.setWorkId(rs.get("WORKID").toString());
				// 作業名
				detail.setWorkName(rs.get("WORKNAME").toString());
				// 作業区分 
				detail.setWorkKbn(rs.get("WORKKBN").toString());
				// 作業進捗区切
				detail.setSeparatePersent(rs.get("SEPARATE_PERCENT").toString());
				// リセット間隔
				detail.setResetSpan(rs.get("RESET_SPAN").toString());
				
				// 備考
				if (rs.get("BIKO") != null) {
					detail.setBiko(rs.get("BIKO").toString());
				}else{
					detail.setBiko("");
				}
				
				retForm.addWork(detail);
				
			}
			
			// ------------------------------------------------
			// 同名の作業が存在しないかチェック
			
			ArrayList<FormKanriMainteWorkDetail> list = retForm.getWorkList();
			
			
			for (int index1 = 0 ; index1 < list.size(); index1++) {
				
				for (int index2 = index1+1 ; index2 < list.size(); index2++) {
					
					String workName1 = list.get(index1).getWorkName();
					String workName2 = list.get(index2).getWorkName();
					
					workName1 = workName1.replaceAll("[ 　]", ""); // 正規表現で半角スペースと全角スペースを除去
					workName2 = workName2.replaceAll("[ 　]", ""); // 正規表現で半角スペースと全角スペースを除去
					
					//log.info("【DBG】作業名１=[" + workName1 + "]作業名２=[" + workName2 + "]");
					
					if (workName1.equals(workName2) == true) {
						//同名の作業が存在する場合、メッセージをセット
						retForm.setMessage("[ " + workName1 + " ]同名の作業情報が複数存在します。確認のうえ修正／削除してください。");
						//for文を脱出するようindexをセット
						index1 = list.size();
						break;
					}
				}
			}
			
			
			
			
			log.info("【INF】" + pgmId + ":処理終了 取得件数=[" + Integer.toString(retForm.getWorkList().size()) + "]件");
			return retForm;
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return null;
		}
	}
	
	
	// 詳細を取得(作業ID指定)
	public FormKanriMainteWorkDetail getTargetWorkData(String workId) {
		
		String pgmId = classId + ".getTargetWorkData";
		log.info("【INF】" + pgmId + ":処理開始 作業ID=[" + workId + "]");
		
		
		// 返却値
		FormKanriMainteWorkDetail retForm = new FormKanriMainteWorkDetail();
		
		try {
			
			String sql = " select";
			sql  = sql + "     TM_WORK.WORKID";
			sql  = sql + "    ,TM_WORK.WORKNAME";
			sql  = sql + "    ,TM_WORK.WORKKBN";
			sql  = sql + "    ,ifnull(TM_WORK.SEPARATE_PERCENT,0) SEPARATE_PERCENT";
			sql  = sql + "    ,ifnull(TM_WORK.RESET_SPAN,0) RESET_SPAN";
			sql  = sql + "    ,TM_WORK.BIKO";
			sql  = sql + " from";
			sql  = sql + "     TM_WORK";
			sql  = sql + " where";
			sql  = sql + "     TM_WORK.WORKID     = ?";
			
			//１件のみ取得であるためソートは不要
			//sql  = sql + " order by";
			//sql  = sql + "     TM_WORK.WORKID";
			
			
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
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql,workId);
			
			
			for (Map<String, Object> rs: rsList) {
				
				// 作業ID
				retForm.setWorkId(rs.get("WORKID").toString());
				// 作業名
				retForm.setWorkName(rs.get("WORKNAME").toString());
				// 作業区分 
				retForm.setWorkKbn(rs.get("WORKKBN").toString());
				// 作業進捗区切
				retForm.setSeparatePersent(rs.get("SEPARATE_PERCENT").toString());
				// リセット間隔
				retForm.setResetSpan(rs.get("RESET_SPAN").toString());
				
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
	
	
	
	
	
	// 作業IDを採番
	private String getNewIdentifier() {
		
		String pgmId = classId + ".getNewIdentifier";
		log.info("【INF】" + pgmId + ":処理開始");
		
		
		// 返却値
		String strNewIdentifier = "";
		
		try {
			
			// 作業IDのMAX＋１を新規作業IDにする
			String sql = " select";
			sql  = sql + "     MAX(TM_WORK.WORKID) WORKID";
			sql  = sql + " from";
			sql  = sql + "     TM_WORK";
			
			// queryForListメソッドでSQLを実行し、結果MapのListで受け取る。
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql);
			
			
			for (Map<String, Object> rs: rsList) {
				
				// MAX作業ID＋１
				strNewIdentifier = Integer.toString(Integer.parseInt(rs.get("WORKID").toString()) + 1);
				
				break;
			}
			
			
			// １件も取得できない場合(登録済み作業が０個の場合)は作業IDの初期値を返却
			if (strNewIdentifier == null || "".equals(strNewIdentifier) == true) {
				strNewIdentifier = "1000001";
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
	public boolean registWork(FormKanriMainteWorkDetail workDetail ,String userName,String registPgmId) {
		
		String pgmId = classId + ".registWork";
		log.info("【INF】" + pgmId + ":処理開始 □登録作業=[" + workDetail.getWorkName() + "]");
		
		
		//新規IDを採番
		String strNewIdentifier = this.getNewIdentifier();
		workDetail.setWorkId(strNewIdentifier);
		
		
		
		//作業情報を登録
		try {
			String sql = " insert into TM_WORK (";
			sql  = sql + "     WORKID";
			sql  = sql + "    ,WORKNAME";
			sql  = sql + "    ,WORKKBN";
			sql  = sql + "    ,SEPARATE_PERCENT";
			sql  = sql + "    ,RESET_SPAN";
			sql  = sql + "    ,SYSREGUSERID";
			sql  = sql + "    ,SYSREGPGMID";
			sql  = sql + "    ,SYSREGYMDHMS";
			sql  = sql + "    ,SYSUPDUSERID";
			sql  = sql + "    ,SYSUPDPGMID";
			sql  = sql + "    ,SYSUPDYMDHMS";
			sql  = sql + " )values(";
			sql  = sql + "     ?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,'1'"; //作業区分は1:ハウス作業固定
			sql  = sql + "    ,?"; //進捗率間隔
			sql  = sql + "    ,?"; //リセット間隔
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,current_timestamp(3)";
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,current_timestamp(3)";
			sql  = sql + " )";
			
			int ret = this.jdbcTemplate.update(sql
					,workDetail.getWorkId()
					,workDetail.getWorkName()
					,workDetail.getSeparatePersent()
					,workDetail.getResetSpan()
					,userName
					,registPgmId
					,userName
					,registPgmId);
			
			
			
			
			// ------------------------------------------------
			// 作業区切情報を登録
			
			
			//未入力である場合は0とする
			if (workDetail.getSeparatePersent() == null || "".equals(workDetail.getSeparatePersent()) == true) {
				workDetail.setSeparatePersent("0");
			}
			
			
			// workDetail.getSeparatePersent()は0、5、10、20、25、50、100、のいずれか
			// 0、100以外である場合は登録処理を行う
			
			if (workDetail.getSeparatePersent().equals("0")   == false
			||  workDetail.getSeparatePersent().equals("100") == false
			) {
				
				// この時点でworkDetail.getSeparatePersent()は 5、10、20、25、50、のいずれか
				int maxIndex = 100 / Integer.parseInt(workDetail.getSeparatePersent());
				
				for (int index = 1; index < maxIndex; index++) { // 100%のレコードは登録しないため比較演算子は<
					
					log.info("【INF】" + pgmId + ":作業ID=[" + workDetail.getWorkId() + "]、区切=[" + Integer.toString(Integer.parseInt(workDetail.getSeparatePersent()) * index) + "]%を登録");
					
					sql        = " insert into TM_HOUSE_WORKSTATUS_SEP (";
					sql  = sql + "     WORKID";
					sql  = sql + "    ,SEQNO";
					sql  = sql + "    ,PERCENT";
					sql  = sql + "    ,SYSREGUSERID";
					sql  = sql + "    ,SYSREGPGMID";
					sql  = sql + "    ,SYSREGYMDHMS";
					sql  = sql + "    ,SYSUPDUSERID";
					sql  = sql + "    ,SYSUPDPGMID";
					sql  = sql + "    ,SYSUPDYMDHMS";
					sql  = sql + " )values(";
					sql  = sql + "     ?";
					sql  = sql + "    ,?";//SEQNo
					sql  = sql + "    ,?";//進捗率
					sql  = sql + "    ,?";
					sql  = sql + "    ,?";
					sql  = sql + "    ,current_timestamp(3)";
					sql  = sql + "    ,?";
					sql  = sql + "    ,?";
					sql  = sql + "    ,current_timestamp(3)";
					sql  = sql + " )";
					
					ret = this.jdbcTemplate.update(sql
							,workDetail.getWorkId()
							,index
							,Integer.parseInt(workDetail.getSeparatePersent()) * index
							,userName
							,registPgmId
							,userName
							,registPgmId);
				}
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
	public boolean updateWork(FormKanriMainteWorkDetail workDetail ,String userName,String registPgmId) {
		
		String pgmId = classId + ".updateWork";
		log.info("【INF】" + pgmId + ":処理開始");
		
		try {
			
			//------------------------------------------------
			// 作業情報の更新
			
			String sql = " update TM_WORK";
			sql  = sql + " set";
			sql  = sql + "     WORKNAME         = ?";
			sql  = sql + "    ,SEPARATE_PERCENT = ?";
			sql  = sql + "    ,RESET_SPAN       = ?";
			sql  = sql + "    ,SYSUPDUSERID     = ?";
			sql  = sql + "    ,SYSUPDPGMID      = ?";
			sql  = sql + "    ,SYSUPDYMDHMS     = current_timestamp(3)";
			sql  = sql + " where";
			sql  = sql + "     WORKID           = ?";
			
			int ret = this.jdbcTemplate.update(sql
					,workDetail.getWorkName()
					,workDetail.getSeparatePersent()
					,workDetail.getResetSpan()
					,userName
					,registPgmId
					,workDetail.getWorkId()
					);
			
			
			
			
			// ------------------------------------------------
			// 作業区切情報を登録
			
			//※一旦全削除してから再登録
			
			
			sql        = " delete from TM_HOUSE_WORKSTATUS_SEP";
			sql  = sql + " where";
			sql  = sql + "     WORKID    = ?";
			
			
			ret = this.jdbcTemplate.update(sql
					,workDetail.getWorkId()
					);
			
			
			
			
			//未入力である場合は0とする
			if (workDetail.getSeparatePersent() == null || "".equals(workDetail.getSeparatePersent()) == true) {
				workDetail.setSeparatePersent("0");
			}
			
			
			// workDetail.getSeparatePersent()は0、5、10、20、25、50、100、のいずれか
			// 0、100以外である場合は登録処理を行う
			
			if (workDetail.getSeparatePersent().equals("0")   == false
			||  workDetail.getSeparatePersent().equals("100") == false
			) {
				
				// この時点でworkDetail.getSeparatePersent()は 5、10、20、25、50、のいずれか
				int maxIndex = 100 / Integer.parseInt(workDetail.getSeparatePersent());
				
				for (int index = 1; index < maxIndex; index++) { // 100%のレコードは登録しないため比較演算子は<
					
					log.info("【INF】" + pgmId + ":作業ID=[" + workDetail.getWorkId() + "]、区切=[" + Integer.toString(Integer.parseInt(workDetail.getSeparatePersent()) * index) + "]%を登録");
					
					sql        = " insert into TM_HOUSE_WORKSTATUS_SEP (";
					sql  = sql + "     WORKID";
					sql  = sql + "    ,SEQNO";
					sql  = sql + "    ,PERCENT";
					sql  = sql + "    ,SYSREGUSERID";
					sql  = sql + "    ,SYSREGPGMID";
					sql  = sql + "    ,SYSREGYMDHMS";
					sql  = sql + "    ,SYSUPDUSERID";
					sql  = sql + "    ,SYSUPDPGMID";
					sql  = sql + "    ,SYSUPDYMDHMS";
					sql  = sql + " )values(";
					sql  = sql + "     ?";
					sql  = sql + "    ,?";//SEQNo
					sql  = sql + "    ,?";//進捗率
					sql  = sql + "    ,?";
					sql  = sql + "    ,?";
					sql  = sql + "    ,current_timestamp(3)";
					sql  = sql + "    ,?";
					sql  = sql + "    ,?";
					sql  = sql + "    ,current_timestamp(3)";
					sql  = sql + " )";
					
					ret = this.jdbcTemplate.update(sql
							,workDetail.getWorkId()
							,index
							,Integer.parseInt(workDetail.getSeparatePersent()) * index
							,userName
							,registPgmId
							,userName
							,registPgmId);
				}
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
	
	
	
	
	
	
	// データ削除（物理削除）
	public boolean deleteWork(FormKanriMainteWorkDetail workDetail ,String userName,String registPgmId) {
		
		String pgmId = classId + ".deleteWork";
		log.info("【INF】" + pgmId + ":処理開始");
		
		try {
			
			//------------------------------------------------
			// 作業情報の削除
			
			
			String sql = " delete from TM_WORK";
			sql  = sql + " where";
			sql  = sql + "     WORKID    = ?";
			
			
			int ret = this.jdbcTemplate.update(sql
					,workDetail.getWorkId()
					);
			
			
			
			
			// ------------------------------------------------
			// 作業区切情報を削除
			
			sql        = " delete from TM_HOUSE_WORKSTATUS_SEP";
			sql  = sql + " where";
			sql  = sql + "     WORKID    = ?";
			
			
			ret = this.jdbcTemplate.update(sql
					,workDetail.getWorkId()
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
