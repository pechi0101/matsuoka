package com.example.DAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.example.counst.SpecialUser;
import com.example.form.FormKanriMainteEmployeeDetail;
import com.example.form.FormKanriMainteEmployeeList;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DaoFormKanriMainteEmployee {
	
	private JdbcTemplate  jdbcTemplate;
	private String classId = "DaoFormKanriMainteEmployee";
	
	// コンストラクタ
	public DaoFormKanriMainteEmployee(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	// 全件を取得
	public FormKanriMainteEmployeeList getAllEmployeeData() {
		
		String pgmId = classId + ".getAllEmployeeData";
		log.info("【INF】" + pgmId + ":処理開始");
		
		
		// 返却値
		FormKanriMainteEmployeeList retForm = new FormKanriMainteEmployeeList();
		
		try {
			
			String sql = " select";
			sql  = sql + "     TM_EMPLOYEE.EMPLOYEEID";
			sql  = sql + "    ,TM_EMPLOYEE.EMPLOYEENAME";
			sql  = sql + "    ,TM_EMPLOYEE.PHONE";
			sql  = sql + "    ,TM_EMPLOYEE.POSTCD";
			sql  = sql + "    ,TM_EMPLOYEE.ADDRESS";
			sql  = sql + "    ,TM_HOURLYWAGE.HOURLYWAGE";
			sql  = sql + "    ,TM_EMPLOYEE.BIKO";
			sql  = sql + "    ,TM_EMPLOYEE.AUTHORITYKBN";
			sql  = sql + " from";
			sql  = sql + "     TM_EMPLOYEE";
			sql  = sql + " left outer join";
			sql  = sql + "     TM_HOURLYWAGE";
			sql  = sql + " on  TM_HOURLYWAGE.EMPLOYEEID  = TM_EMPLOYEE.EMPLOYEEID";
			sql  = sql + " and TM_HOURLYWAGE.VALIDFLG  = True";
			sql  = sql + " where";
			sql  = sql + "     TM_EMPLOYEE.DELETEFLG   = False";
			sql  = sql + " and TM_EMPLOYEE.EMPLOYEEID  not in (?,?)"; // 管理者ユーザとテストユーザは除く
			sql  = sql + " order by";
			sql  = sql + "     TM_EMPLOYEE.EMPLOYEEID";
			
			
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
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql,SpecialUser.KANRI_USER,SpecialUser.TEST_USER);
			
			
			for (Map<String, Object> rs: rsList) {
				
				
				FormKanriMainteEmployeeDetail detail = new FormKanriMainteEmployeeDetail();
				
				// 社員ID
				detail.setEmployeeId(rs.get("EMPLOYEEID").toString());
				// 社員名
				detail.setEmployeeName(rs.get("EMPLOYEENAME").toString());
				
				
				// 電話番号
				if (rs.get("PHONE") != null) {
					detail.setPhone(rs.get("PHONE").toString());
				}else{
					detail.setPhone("");
				}
				// 郵便番号
				if (rs.get("POSTCD") != null) {
					detail.setPostCd(rs.get("POSTCD").toString());
				}else{
					detail.setPostCd("");
				}
				// 住所
				if (rs.get("ADDRESS") != null) {
					detail.setAddress(rs.get("ADDRESS").toString());
				}else{
					detail.setAddress("");
				}
				// 時給
				if (rs.get("HOURLYWAGE") != null) {
					detail.setHourlyWage(rs.get("HOURLYWAGE").toString());
				}else{
					detail.setHourlyWage("");
				}
				// 備考
				if (rs.get("BIKO") != null) {
					detail.setBiko(rs.get("BIKO").toString());
				}else{
					detail.setBiko("");
				}
				// 権限区分
				if (rs.get("AUTHORITYKBN") != null) {
					detail.setAuthorityKbn(rs.get("AUTHORITYKBN").toString());
				}else{
					detail.setAuthorityKbn("0");
				}
				// 権限区分名
				if (detail.getAuthorityKbn().equals("Z") == true) {
					detail.setAuthorityKbnName("管理者");
				} else if (detail.getAuthorityKbn().equals("9") == true) {
					detail.setAuthorityKbnName("特別");
				}else{
					detail.setAuthorityKbnName("一般");
				}
				
				
				retForm.addEmployee(detail);
				
			}
			
			// ------------------------------------------------
			// 同名の社員が存在しないかチェック
			
			ArrayList<FormKanriMainteEmployeeDetail> list = retForm.getEmployeeList();
			
			
			for (int index1 = 0 ; index1 < list.size(); index1++) {
				
				for (int index2 = index1+1 ; index2 < list.size(); index2++) {
					
					String employeeName1 = list.get(index1).getEmployeeName();
					String employeeName2 = list.get(index2).getEmployeeName();
					
					employeeName1 = employeeName1.replaceAll("[ 　]", ""); // 正規表現で半角スペースと全角スペースを除去
					employeeName2 = employeeName2.replaceAll("[ 　]", ""); // 正規表現で半角スペースと全角スペースを除去
					
					//log.info("【DBG】社員名１=[" + employeeName1 + "]社員名２=[" + employeeName2 + "]");
					
					if (employeeName1.equals(employeeName2) == true) {
						//同名の社員が存在する場合、メッセージをセット
						retForm.setMessage("[ " + employeeName1 + " ]同姓同名の社員情報が複数存在します。確認のうえ修正／削除してください。");
						//for文を脱出するようindexをセット
						index1 = list.size();
						break;
					}
				}
			}
			
			
			
			
			log.info("【INF】" + pgmId + ":処理終了 取得件数=[" + Integer.toString(retForm.getEmployeeList().size()) + "]件");
			return retForm;
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return null;
		}
	}
	
	
	// 詳細を取得(社員ID指定)
	public FormKanriMainteEmployeeDetail getTargetEmployeeData(String employeeId) {
		
		String pgmId = classId + ".getTargetEmployeeData";
		log.info("【INF】" + pgmId + ":処理開始 社員ID=[" + employeeId + "]");
		
		
		// 返却値
		FormKanriMainteEmployeeDetail retForm = new FormKanriMainteEmployeeDetail();
		
		try {
			
			String sql = " select";
			sql  = sql + "     TM_EMPLOYEE.EMPLOYEEID";
			sql  = sql + "    ,TM_EMPLOYEE.EMPLOYEENAME";
			sql  = sql + "    ,TM_EMPLOYEE.PHONE";
			sql  = sql + "    ,TM_EMPLOYEE.POSTCD";
			sql  = sql + "    ,TM_EMPLOYEE.ADDRESS";
			sql  = sql + "    ,TM_HOURLYWAGE.HOURLYWAGE";
			sql  = sql + "    ,TM_EMPLOYEE.BIKO";
			sql  = sql + "    ,TM_EMPLOYEE.AUTHORITYKBN";
			sql  = sql + " from";
			sql  = sql + "     TM_EMPLOYEE";
			sql  = sql + " left outer join";
			sql  = sql + "     TM_HOURLYWAGE";
			sql  = sql + " on  TM_HOURLYWAGE.EMPLOYEEID  = TM_EMPLOYEE.EMPLOYEEID";
			sql  = sql + " and TM_HOURLYWAGE.VALIDFLG  = True";
			sql  = sql + " where";
			sql  = sql + "     TM_EMPLOYEE.EMPLOYEEID  = ?";
			sql  = sql + " and TM_EMPLOYEE.DELETEFLG   = False";
			
			//１件のみ取得であるためソートは不要
			//sql  = sql + " order by";
			//sql  = sql + "     TM_EMPLOYEE.EMPLOYEEID";
			
			
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
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql,employeeId);
			
			
			for (Map<String, Object> rs: rsList) {
				
				
				// 社員ID
				retForm.setEmployeeId(rs.get("EMPLOYEEID").toString());
				// 社員名
				retForm.setEmployeeName(rs.get("EMPLOYEENAME").toString());
				
				
				// 電話番号
				if (rs.get("PHONE") != null) {
					retForm.setPhone(rs.get("PHONE").toString());
				}else{
					retForm.setPhone("");
				}
				// 郵便番号
				if (rs.get("POSTCD") != null) {
					retForm.setPostCd(rs.get("POSTCD").toString());
				}else{
					retForm.setPostCd("");
				}
				// 住所
				if (rs.get("ADDRESS") != null) {
					retForm.setAddress(rs.get("ADDRESS").toString());
				}else{
					retForm.setAddress("");
				}
				// 時給
				if (rs.get("HOURLYWAGE") != null) {
					retForm.setHourlyWage(rs.get("HOURLYWAGE").toString());
				}else{
					retForm.setHourlyWage("");
				}
				// 備考
				if (rs.get("BIKO") != null) {
					retForm.setBiko(rs.get("BIKO").toString());
				}else{
					retForm.setBiko("");
				}
				// 権限区分
				if (rs.get("AUTHORITYKBN") != null) {
					retForm.setAuthorityKbn(rs.get("AUTHORITYKBN").toString());
				}else{
					retForm.setAuthorityKbn("0");
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
	
	
	
	
	
	// 社員IDを採番
	private String getNewIdentifier() {
		
		String pgmId = classId + ".getNewIdentifier";
		log.info("【INF】" + pgmId + ":処理開始");
		
		
		// 返却値
		String strNewIdentifier = "";
		
		try {
			
			// 社員IDのMAX＋１を新規社員IDにする
			String sql = " select";
			sql  = sql + "     MAX(TM_EMPLOYEE.EMPLOYEEID) EMPLOYEEID";
			sql  = sql + " from";
			sql  = sql + "     TM_EMPLOYEE";
			sql  = sql + " where";
			sql  = sql + "     TM_EMPLOYEE.EMPLOYEEID  not in (?,?)"; // 管理者ユーザとテストユーザは除く
			
			
			
			// queryForListメソッドでSQLを実行し、結果MapのListで受け取る。
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql,SpecialUser.KANRI_USER,SpecialUser.TEST_USER);
			
			
			for (Map<String, Object> rs: rsList) {
				
				// MAX社員ID＋１
				strNewIdentifier = Integer.toString(Integer.parseInt(rs.get("EMPLOYEEID").toString()) + 1);
				
				break;
			}
			
			
			// １件も取得できない場合(登録済み社員が０人の場合)は社員IDの初期値を返却
			if (strNewIdentifier == null || "".equals(strNewIdentifier) == true) {
				strNewIdentifier = "1000000001";
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
	public boolean registEmployee(FormKanriMainteEmployeeDetail employeeDetail ,String userName,String registPgmId) {
		
		String pgmId = classId + ".registEmployee";
		log.info("【INF】" + pgmId + ":処理開始 □登録社員=[" + employeeDetail.getEmployeeName() + "]");
		
		
		//新規IDを採番
		String strNewIdentifier = this.getNewIdentifier();
		employeeDetail.setEmployeeId(strNewIdentifier);
		
		
		
		//社員情報を登録
		try {
			String sql = " insert into TM_EMPLOYEE (";
			sql  = sql + "     EMPLOYEEID";
			sql  = sql + "    ,EMPLOYEENAME";
			sql  = sql + "    ,BIRTHDAY";
			sql  = sql + "    ,POSTCD";
			sql  = sql + "    ,ADDRESS";
			sql  = sql + "    ,PHONE";
			sql  = sql + "    ,PHOTO";
			sql  = sql + "    ,BANKNAME";
			sql  = sql + "    ,BANK_BRANCHNAME";
			sql  = sql + "    ,BANK_ACCOUNTTYPE";
			sql  = sql + "    ,BANK_ACCOUNTNO";
			sql  = sql + "    ,BIKO";
			sql  = sql + "    ,AUTHORITYKBN";
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
			sql  = sql + "    ,null"; //誕生日は未使用
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,null"; //写真は未使用
			sql  = sql + "    ,null"; //銀行情報は未使用
			sql  = sql + "    ,null"; //銀行情報は未使用
			sql  = sql + "    ,null"; //銀行情報は未使用
			sql  = sql + "    ,null"; //銀行情報は未使用
			sql  = sql + "    ,?";    //備考
			sql  = sql + "    ,?";    //権限区分
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
					,employeeDetail.getEmployeeId()
					,employeeDetail.getEmployeeName()
					,employeeDetail.getPostCd()
					,employeeDetail.getAddress()
					,employeeDetail.getPhone()
					,employeeDetail.getBiko()
					,employeeDetail.getAuthorityKbn()
					,userName
					,registPgmId
					,userName
					,registPgmId);
			
			
			
			
			
			//時給情報を登録
			
			
			//時給が未入力である場合は0円で登録
			if (employeeDetail.getHourlyWage() == null || "".equals(employeeDetail.getHourlyWage()) == true) {
				employeeDetail.setHourlyWage("0");
			}
			
			sql        = " insert into TM_HOURLYWAGE (";
			sql  = sql + "     EMPLOYEEID";
			sql  = sql + "    ,STARTDATETIME";
			sql  = sql + "    ,ENDDATETIME";
			sql  = sql + "    ,HOURLYWAGE";
			sql  = sql + "    ,VALIDFLG";
			sql  = sql + "    ,SYSREGUSERID";
			sql  = sql + "    ,SYSREGPGMID";
			sql  = sql + "    ,SYSREGYMDHMS";
			sql  = sql + "    ,SYSUPDUSERID";
			sql  = sql + "    ,SYSUPDPGMID";
			sql  = sql + "    ,SYSUPDYMDHMS";
			sql  = sql + " )values(";
			sql  = sql + "     ?";
			sql  = sql + "    ,current_timestamp"; // 適用開始日時は現在日時
			sql  = sql + "    ,null";              // 適用終了日時はnull
			sql  = sql + "    ,?";
			sql  = sql + "    ,1"; // 適用中フラグは1(true)で登録
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,current_timestamp(3)";
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,current_timestamp(3)";
			sql  = sql + " )";
			
			ret = this.jdbcTemplate.update(sql
					,employeeDetail.getEmployeeId()
					,Integer.parseInt(employeeDetail.getHourlyWage())
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
	
	
	
	
	
	// データ更新
	public boolean updateEmployee(FormKanriMainteEmployeeDetail employeeDetail ,String userName,String registPgmId) {
		
		String pgmId = classId + ".updateEmployee";
		log.info("【INF】" + pgmId + ":処理開始");
		
		try {
			
			//------------------------------------------------
			// 社員情報の更新
			
			String sql = " update TM_EMPLOYEE";
			sql  = sql + " set";
			sql  = sql + "     EMPLOYEENAME     = ?";
			sql  = sql + "    ,BIRTHDAY         = null"; //誕生日は未使用
			sql  = sql + "    ,POSTCD           = ?";
			sql  = sql + "    ,ADDRESS          = ?";
			sql  = sql + "    ,PHONE            = ?";
			sql  = sql + "    ,PHOTO            = null"; //写真は未使用
			sql  = sql + "    ,BANKNAME         = null"; //銀行情報は未使用
			sql  = sql + "    ,BANK_BRANCHNAME  = null"; //銀行情報は未使用
			sql  = sql + "    ,BANK_ACCOUNTTYPE = null"; //銀行情報は未使用
			sql  = sql + "    ,BANK_ACCOUNTNO   = null"; //銀行情報は未使用
			sql  = sql + "    ,BIKO             = ?";
			sql  = sql + "    ,AUTHORITYKBN     = ?";
			sql  = sql + "    ,SYSUPDUSERID     = ?";
			sql  = sql + "    ,SYSUPDPGMID      = ?";
			sql  = sql + "    ,SYSUPDYMDHMS     = current_timestamp(3)";
			sql  = sql + " where";
			sql  = sql + "     EMPLOYEEID       = ?";
			
			int ret = this.jdbcTemplate.update(sql
					,employeeDetail.getEmployeeName()
					,employeeDetail.getPostCd()
					,employeeDetail.getAddress()
					,employeeDetail.getPhone()
					,employeeDetail.getBiko()
					,employeeDetail.getAuthorityKbn()
					,userName
					,registPgmId
					,employeeDetail.getEmployeeId()
					);
			
			
			
			//------------------------------------------------
			// 時給情報の更新
			
			//※適用中の時給から変更がある場合のみ時給を更新(登録)する
			
			
			
			sql        = " select count(1)";
			sql  = sql + " from";
			sql  = sql + "     TM_HOURLYWAGE";
			sql  = sql + " where";
			sql  = sql + "     EMPLOYEEID    = ?";
			sql  = sql + " and VALIDFLG      = 1"; // 適用中フラグ(1:true)のデータを検索
			sql  = sql + " and HOURLYWAGE    = ?"; // 時給が同じデータが存在するか検索
			
			// queryForListメソッドでSQLを実行
			int count = this.jdbcTemplate.queryForObject(sql
											,Integer.class
											,employeeDetail.getEmployeeId()
											,employeeDetail.getHourlyWage()
											);
			
			log.info("【DBG】" + pgmId + ":社員ID=[" + employeeDetail.getEmployeeId() + "]、時給の件数=[" + count + "]件");
			
			
			// 上記で検索結果0件＝時給に変更があった場合
			
			if (count == 0) {
				
				
				// 現在適用中の時給を未適用に更新
				
				
				sql        = " update TM_HOURLYWAGE";
				sql  = sql + " set";
				sql  = sql + "     ENDDATETIME   = current_timestamp";//適用終了日時をセット
				sql  = sql + "    ,VALIDFLG      = 0"; //適用中フラグを(0:false)に更新
				sql  = sql + "    ,SYSUPDUSERID  = ?";
				sql  = sql + "    ,SYSUPDPGMID   = ?";
				sql  = sql + "    ,SYSUPDYMDHMS  = current_timestamp(3)";
				sql  = sql + " where";
				sql  = sql + "     EMPLOYEEID    = ?";
				sql  = sql + " and VALIDFLG      = 1"; //適用中フラグ(1:true)のデータを更新
				
				
				ret = this.jdbcTemplate.update(sql
						,userName
						,registPgmId
						,employeeDetail.getEmployeeId()
						);
				
				
				
				// 新規適用中のデータを登録
				
				
				//時給が未入力である場合は0円で登録
				if (employeeDetail.getHourlyWage() == null || "".equals(employeeDetail.getHourlyWage()) == true) {
					employeeDetail.setHourlyWage("0");
				}
				
				sql        = " insert into TM_HOURLYWAGE (";
				sql  = sql + "     EMPLOYEEID";
				sql  = sql + "    ,STARTDATETIME";
				sql  = sql + "    ,ENDDATETIME";
				sql  = sql + "    ,HOURLYWAGE";
				sql  = sql + "    ,VALIDFLG";
				sql  = sql + "    ,SYSREGUSERID";
				sql  = sql + "    ,SYSREGPGMID";
				sql  = sql + "    ,SYSREGYMDHMS";
				sql  = sql + "    ,SYSUPDUSERID";
				sql  = sql + "    ,SYSUPDPGMID";
				sql  = sql + "    ,SYSUPDYMDHMS";
				sql  = sql + " )values(";
				sql  = sql + "     ?";
				sql  = sql + "    ,current_timestamp"; // 適用開始日時は現在日時
				sql  = sql + "    ,null";              // 適用終了日時はnull
				sql  = sql + "    ,?";
				sql  = sql + "    ,1"; // 適用中フラグは1(true)で登録
				sql  = sql + "    ,?";
				sql  = sql + "    ,?";
				sql  = sql + "    ,current_timestamp(3)";
				sql  = sql + "    ,?";
				sql  = sql + "    ,?";
				sql  = sql + "    ,current_timestamp(3)";
				sql  = sql + " )";
				
				ret = this.jdbcTemplate.update(sql
						,employeeDetail.getEmployeeId()
						,Integer.parseInt(employeeDetail.getHourlyWage())
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
	public boolean deleteEmployee(FormKanriMainteEmployeeDetail employeeDetail ,String userName,String registPgmId) {
		
		String pgmId = classId + ".deleteEmployee";
		log.info("【INF】" + pgmId + ":処理開始");
		
		try {
			
			//------------------------------------------------
			// 社員情報の削除
			
			String sql = " update TM_EMPLOYEE";
			sql  = sql + " set";
			sql  = sql + "     BIKO             = ?";
			sql  = sql + "    ,DELETEFLG        = 1";    //削除フラグは1(true)で更新
			sql  = sql + "    ,DELETEYMDHMS     = current_timestamp(3)"; //削除日時は現在日時
			sql  = sql + "    ,SYSUPDUSERID     = ?";
			sql  = sql + "    ,SYSUPDPGMID      = ?";
			sql  = sql + "    ,SYSUPDYMDHMS     = current_timestamp(3)";
			sql  = sql + " where";
			sql  = sql + "     EMPLOYEEID       = ?";
			
			int ret = this.jdbcTemplate.update(sql
					,employeeDetail.getBiko()
					,userName
					,registPgmId
					,employeeDetail.getEmployeeId()
					);
			
			
			
			//------------------------------------------------
			// 時給情報の削除
			
			
			
			// 現在適用中の時給を未適用に更新
			
			
			sql        = " update TM_HOURLYWAGE";
			sql  = sql + " set";
			sql  = sql + "     ENDDATETIME   = current_timestamp";//適用終了日時をセット
			sql  = sql + "    ,VALIDFLG      = 0"; //適用中フラグを(0:false)に更新
			sql  = sql + "    ,SYSUPDUSERID  = ?";
			sql  = sql + "    ,SYSUPDPGMID   = ?";
			sql  = sql + "    ,SYSUPDYMDHMS  = current_timestamp(3)";
			sql  = sql + " where";
			sql  = sql + "     EMPLOYEEID    = ?";
			sql  = sql + " and VALIDFLG      = 1"; //適用中フラグ(1:true)のデータを更新
			
			
			ret = this.jdbcTemplate.update(sql
					,userName
					,registPgmId
					,employeeDetail.getEmployeeId()
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
