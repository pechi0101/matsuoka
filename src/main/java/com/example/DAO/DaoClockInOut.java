package com.example.DAO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jdbc.core.JdbcTemplate;

import com.example.counst.SpecialUser;
import com.example.form.FormDispQRInfoClockInOut;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DaoClockInOut {
	
	private JdbcTemplate  jdbcTemplate;
	private String classId = "DaoClockInOut";
	
	// コンストラクタ
	public DaoClockInOut(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	
	
	
	// 指定の日付で「出勤」状態の情報があるか否かのチェック
	public boolean exsistsClockInData(String empliyeeId,String clockInYear,String clockInMonth,String clockInDay) {
		
		String pgmId = classId + ".exsistsClockInData";
		log.info("【INF】" + pgmId + ":処理開始 社員ID=[" + empliyeeId + "]、出勤年月日=[" + clockInYear + clockInMonth + clockInDay + "]");
		
		try {
			
			String sql = " select count(1)";
			sql  = sql + " from";
			sql  = sql + "     TT_CLOCKINOUT";
			sql  = sql + " where";
			sql  = sql + "     EMPLOYEEID       = ?";
			sql  = sql + " and CLOCKINYEAR      = ?";
			sql  = sql + " and CLOCKINMONTH     = ?";
			sql  = sql + " and CLOCKINDAY       = ?";
			sql  = sql + " and CLOCKOUTDATETIME is null"; // 退勤日時＝null→出勤状態の情報を検索してる
			
			// queryForListメソッドでSQLを実行
			int count = this.jdbcTemplate.queryForObject(sql
											,Integer.class
											,empliyeeId
											,clockInYear
											,clockInMonth
											,clockInDay
											);
			
			log.info("【INF】" + pgmId + ":処理終了 件数=[" + Integer.toString(count) + "]");
			
			// 件数が１件以上である場合Trueを返却
			return count >= 1;
			
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			//検索した結果を返却します
			return false;
		}
	}
	
	
	
	
	
	// 指定の出退勤日時が既存の出退勤情報と重複しているかをチェック
	public boolean isDuplicationClockInDataTime(String empliyeeId,String clockInYear,String clockInMonth,String clockInDay,LocalDateTime clockInDatetime,LocalDateTime clockOutDatetime) {
		
		// 年月日時分秒までの日時フォーマットを準備
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		String pgmId = classId + ".isDuplicationClockInDataTime";
		log.info("【INF】" + pgmId + ":処理開始 社員ID=[" + empliyeeId + "]、出勤年月日=[" + clockInYear + clockInMonth + clockInDay + "]、出勤日時=[" + clockInDatetime + "]、退勤日時=[" + clockOutDatetime + "]");
		
		try {
			
			int count = 0;
			
			if (clockOutDatetime == null) {
				
				
				String sql = " select count(1)";
				sql  = sql + " from";
				sql  = sql + "     TT_CLOCKINOUT";
				sql  = sql + " where";
				sql  = sql + "     EMPLOYEEID        = ?";
				sql  = sql + " and CLOCKINYEAR       = ?";
				sql  = sql + " and CLOCKINMONTH      = ?";
				sql  = sql + " and CLOCKINDAY        = ?";
				sql  = sql + " and CLOCKINDATETIME  <> ?";
				sql  = sql + " and (";
				sql  = sql + "     CLOCKINDATETIME  <= ? and ? <= CLOCKOUTDATETIME";
			  //sql  = sql + " or  CLOCKINDATETIME  <= ? and ? <= CLOCKOUTDATETIME";
				sql  = sql + "     )";
				
				// queryForListメソッドでSQLを実行
				count = this.jdbcTemplate.queryForObject(sql
												,Integer.class
												,empliyeeId
												,clockInYear
												,clockInMonth
												,clockInDay
												,formatter.format(clockInDatetime)
												,formatter.format(clockInDatetime)
												,formatter.format(clockInDatetime)
				//								,formatter.format(clockOutDatetime)
				//								,formatter.format(clockOutDatetime)
												);
			} else {
				
				
				String sql = " select count(1)";
				sql  = sql + " from";
				sql  = sql + "     TT_CLOCKINOUT";
				sql  = sql + " where";
				sql  = sql + "     EMPLOYEEID        = ?";
				sql  = sql + " and CLOCKINYEAR       = ?";
				sql  = sql + " and CLOCKINMONTH      = ?";
				sql  = sql + " and CLOCKINDAY        = ?";
				sql  = sql + " and CLOCKINDATETIME  <> ?";
				sql  = sql + " and (";
				sql  = sql + "     CLOCKINDATETIME  <= ? and ? <= CLOCKOUTDATETIME";
				sql  = sql + " or  CLOCKINDATETIME  <= ? and ? <= CLOCKOUTDATETIME";
				sql  = sql + "     )";
				
				// queryForListメソッドでSQLを実行
				count = this.jdbcTemplate.queryForObject(sql
												,Integer.class
												,empliyeeId
												,clockInYear
												,clockInMonth
												,clockInDay
												,formatter.format(clockInDatetime)
												,formatter.format(clockInDatetime)
												,formatter.format(clockInDatetime)
												,formatter.format(clockOutDatetime)
												,formatter.format(clockOutDatetime)
												);
				
				
				
				
			}
			
			log.info("【INF】" + pgmId + ":処理終了 件数=[" + Integer.toString(count) + "]");
			
			// 件数が１件以上である場合Trueを返却
			return count >= 1;
			
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			//検索した結果を返却します
			return false;
		}
	}
	
	
	
	
	// 指定の日付で「出勤」状態の情報を検索
	public FormDispQRInfoClockInOut getClockInData(String empliyeeId,String clockInYear,String clockInMonth,String clockInDay) {
		
		String pgmId = classId + ".getClockInData";
		log.info("【INF】" + pgmId + ":処理開始 社員ID=[" + empliyeeId + "]、出勤年月日=[" + clockInYear + clockInMonth + clockInDay + "]");
		
		FormDispQRInfoClockInOut formDispQRInfoClockInOut = new FormDispQRInfoClockInOut();
		
		formDispQRInfoClockInOut.setLoginEmployeeId(empliyeeId);
		formDispQRInfoClockInOut.setClockInYear(clockInYear);
		formDispQRInfoClockInOut.setClockInMonth(clockInMonth);
		formDispQRInfoClockInOut.setClockInDay(clockInDay);
		
		try {
			
			String sql = " select";
			sql  = sql + "     DATE_FORMAT(CLOCKINDATETIME ,'%Y%m%d%H%i%S') CLOCKINDATETIME_STRING";  // YYYYMMDDHHMMSS形式で取得（時間は24時間制）
			sql  = sql + "    ,DATE_FORMAT(CLOCKOUTDATETIME,'%Y%m%d%H%i%S') CLOCKOUTDATETIME_STRING"; // YYYYMMDDHHMMSS形式で取得（時間は24時間制）
			sql  = sql + "    ,HOURLYWAGE";
			sql  = sql + "    ,WORKING_HOUERS";
			sql  = sql + " from";
			sql  = sql + "     TT_CLOCKINOUT";
			sql  = sql + " where";
			sql  = sql + "     EMPLOYEEID       = ?";
			sql  = sql + " and CLOCKINYEAR      = ?";
			sql  = sql + " and CLOCKINMONTH     = ?";
			sql  = sql + " and CLOCKINDAY       = ?";
			sql  = sql + " and CLOCKOUTDATETIME is null"; // 退勤日時＝null→出勤状態の情報を検索してる
			
			
			
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
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(
														 sql
														,empliyeeId
														,clockInYear
														,clockInMonth
														,clockInDay
														);
			
			
			for (Map<String, Object> rs: rsList) {
				
				DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
				DateTimeFormatter formatterDate     = DateTimeFormatter.ofPattern("yyyyMMdd");
				DateTimeFormatter formatterTime     = DateTimeFormatter.ofPattern("HHmmss");
				
				String wkDate; //YYYYMMDDの文字列
				String wkTime; //HHMMSSの文字列
				
				//出勤日
				String clockInDateString = rs.get("CLOCKINDATETIME_STRING").toString();
				wkDate = clockInDateString.substring(0, 8);
				wkTime = clockInDateString.substring(8, 14);
				
				formDispQRInfoClockInOut.setClockInDate(LocalDate.parse(wkDate,formatterDate));
				formDispQRInfoClockInOut.setClockInTime(LocalTime.parse(wkTime,formatterTime));
				formDispQRInfoClockInOut.setClockInTimeString(formDispQRInfoClockInOut.getClockInTime().format(DateTimeFormatter.ofPattern("HH:mm")));
				formDispQRInfoClockInOut.setClockInDatetime(LocalDateTime.parse(clockInDateString, formatterDateTime));
				
				//退勤日
				if (rs.get("CLOCKOUTDATETIME_STRING") != null) {
					String clockOutDateString = rs.get("CLOCKOUTDATETIME_STRING").toString();
					wkDate = clockOutDateString.substring(0, 8);
					wkTime = clockOutDateString.substring(8, 14);
					
					formDispQRInfoClockInOut.setClockOutDate(LocalDate.parse(wkDate,formatterDate));
					formDispQRInfoClockInOut.setClockOutTime(LocalTime.parse(wkTime,formatterTime));
					formDispQRInfoClockInOut.setClockOutTimeString(formDispQRInfoClockInOut.getClockOutTime().format(DateTimeFormatter.ofPattern("HH:mm")));
					formDispQRInfoClockInOut.setClockOutDatetime(LocalDateTime.parse(clockOutDateString, formatterDateTime));
				}
				
				// 時給
				if (rs.get("HOURLYWAGE") != null) {
					formDispQRInfoClockInOut.setHourLywage(Integer.parseInt(rs.get("HOURLYWAGE").toString()));
				}
				
				// 勤務時間
				if (rs.get("WORKING_HOUERS") != null) {
					formDispQRInfoClockInOut.setWorkingHours((Double.parseDouble(rs.get("WORKING_HOUERS").toString())));
				}
				
				// 値は１件のみ取得されるためここでLOOP終了
				break;
			}
			
			log.info("【INF】" + pgmId + ":処理終了 取得データの出勤日=[" + formDispQRInfoClockInOut.getClockInDay().toString() + "]");
			
			
			return formDispQRInfoClockInOut;
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return null;
		}
	}
	
	
	// データ登録
	public boolean regist(FormDispQRInfoClockInOut formDispQRInfoClockInOut ,String userName,String registPgmId) {
		
		// 年月日時分秒までの日時フォーマットを準備
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		String pgmId = classId + ".regist";
		log.info("【INF】" + pgmId + ":処理開始 □出勤日時=[" + formDispQRInfoClockInOut.getClockInDatetime() + "]、退勤日時=[" + formDispQRInfoClockInOut.getClockOutDatetime() + "]、勤務時間=[" + formDispQRInfoClockInOut.getWorkingHours() + "]時間");
		
		try {
			
			// 出退勤日時をSQL登録・更新用に文字列編集
			String clockInDateTimeString = null;
			if (formDispQRInfoClockInOut.getClockInDatetime() != null) {
				clockInDateTimeString = formatter.format(formDispQRInfoClockInOut.getClockInDatetime());
			}
			String clockOutDateTimeString = null;
			if (formDispQRInfoClockInOut.getClockOutDatetime() != null) {
				clockOutDateTimeString = formatter.format(formDispQRInfoClockInOut.getClockOutDatetime());
			}
			
			String sql = " insert into TT_CLOCKINOUT (";
			sql  = sql + "     EMPLOYEEID";
			sql  = sql + "    ,CLOCKINYEAR";
			sql  = sql + "    ,CLOCKINMONTH";
			sql  = sql + "    ,CLOCKINDAY";
			sql  = sql + "    ,CLOCKINDATETIME";
			sql  = sql + "    ,CLOCKOUTDATETIME";
			sql  = sql + "    ,HOURLYWAGE";
			sql  = sql + "    ,WORKING_HOUERS";
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
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";//CLOCKINDATETIME
			sql  = sql + "    ,?";//CLOCKOUTDATETIME
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";//WORKING_HOUERS
			
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,current_timestamp(3)";
			sql  = sql + "    ,?";
			sql  = sql + "    ,?";
			sql  = sql + "    ,current_timestamp(3)";
			sql  = sql + " )";
			
			
			
			int ret = this.jdbcTemplate.update(sql
					,formDispQRInfoClockInOut.getLoginEmployeeId()
					,formDispQRInfoClockInOut.getClockInYear()
					,formDispQRInfoClockInOut.getClockInMonth()
					,formDispQRInfoClockInOut.getClockInDay()
					,clockInDateTimeString
					,clockOutDateTimeString
					,formDispQRInfoClockInOut.getHourLywage()
					,formDispQRInfoClockInOut.getWorkingHours()
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
	public boolean update(FormDispQRInfoClockInOut formDispQRInfoClockInOut ,String userName,String registPgmId) {
		
		// 年月日時分秒までの日時フォーマットを準備
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		String pgmId = classId + ".regist";
		log.info("【INF】" + pgmId + ":処理開始 □出勤日時=[" + formDispQRInfoClockInOut.getClockInDatetime() + "]、退勤日時=[" + formDispQRInfoClockInOut.getClockOutDatetime() + "]、勤務時間=[" + formDispQRInfoClockInOut.getWorkingHours() + "]時間");
		
		try {
			
			// 出退勤日時をSQL登録・更新用に文字列編集
			String clockInDateTimeString = null;
			if (formDispQRInfoClockInOut.getClockInDatetime() != null) {
				clockInDateTimeString = formatter.format(formDispQRInfoClockInOut.getClockInDatetime());
			}
			String clockOutDateTimeString = null;
			if (formDispQRInfoClockInOut.getClockOutDatetime() != null) {
				clockOutDateTimeString = formatter.format(formDispQRInfoClockInOut.getClockOutDatetime());
			}
			
			
			String sql = " update TT_CLOCKINOUT ";
			sql  = sql + " set";
			sql  = sql + "     CLOCKINYEAR      = ?";
			sql  = sql + "    ,CLOCKINMONTH     = ?";
			sql  = sql + "    ,CLOCKINDAY       = ?";
			sql  = sql + "    ,CLOCKOUTDATETIME = ?";
			sql  = sql + "    ,HOURLYWAGE       = ?";
			sql  = sql + "    ,WORKING_HOUERS   = ?";
			sql  = sql + "    ,SYSUPDUSERID     = ?";
			sql  = sql + "    ,SYSUPDPGMID      = ?";
			sql  = sql + "    ,SYSUPDYMDHMS     = current_timestamp(3)";
			sql  = sql + " where";
			sql  = sql + "     EMPLOYEEID       = ?";
			sql  = sql + " and CLOCKINDATETIME  = ?";
			
			int ret = this.jdbcTemplate.update(sql
					,formDispQRInfoClockInOut.getClockInYear()
					,formDispQRInfoClockInOut.getClockInMonth()
					,formDispQRInfoClockInOut.getClockInDay()
					,clockOutDateTimeString
					,formDispQRInfoClockInOut.getHourLywage()
					,formDispQRInfoClockInOut.getWorkingHours()
					,userName
					,registPgmId
					,formDispQRInfoClockInOut.getLoginEmployeeId()
					,clockInDateTimeString
					);
			
			
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
	
	
	
	
	//_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
	//
	// 給与明細作成関連
	//
	//_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
	

	// 汎用関数：シートをコピーする
	private Sheet copyExcelSheet(Workbook workbook,String origiunalSheetName, String newSheetName) {
		
		String pgmId = classId + ".copyExcelSheet";
		log.info("【INF】" + pgmId + ":処理開始 コピー元シート名=[" + origiunalSheetName + "]新シート名=[" + newSheetName + "]");
		
		try {
			
			// 元のシートを取得
			Sheet originalSheet = workbook.getSheet(origiunalSheetName);
			Sheet newSheet      = workbook.createSheet(newSheetName);
			
			
			//------------------------------------------------
			// 列幅をコピー(１行目に値が入ってる最後の列まで列幅をコピー)
			
			//log.info("【DBG】" + pgmId + ":最終列            =[" + (originalSheet.getRow(0).getLastCellNum() + 1) +"]列目");
			for (int colIndex = 0; colIndex < originalSheet.getRow(0).getLastCellNum(); colIndex++) {
			    newSheet.setColumnWidth(colIndex, originalSheet.getColumnWidth(colIndex));
			}
			
			
			//------------------------------------------------
			//目盛り線を非表示にする
			newSheet.setDisplayGridlines(false);
			
			//------------------------------------------------
			// 行をコピー
			for (int rowIndex = 0; rowIndex <= originalSheet.getLastRowNum(); rowIndex++) {
				Row originalRow = originalSheet.getRow(rowIndex);
				Row newRow      = newSheet.createRow(rowIndex);
				
				if (originalRow == null) {
					continue;
				}
				// 行の高さをコピー
				newRow.setHeight(originalRow.getHeight());
				
				// セルをコピー
				for (int colIndex = 0; colIndex < originalRow.getLastCellNum(); colIndex++) {
					Cell originalCell = originalRow.getCell(colIndex);
					Cell newCell      = newRow.createCell(colIndex);
					
					if (originalCell == null) {
						continue;
					}
					
					// スタイルをコピー
					CellStyle newCellStyle = workbook.createCellStyle();
					newCellStyle.cloneStyleFrom(originalCell.getCellStyle());
					newCell.setCellStyle(newCellStyle);
					
					// セルの値をnullで初期化
					String nullVal = null;
					newCell.setCellValue(nullVal);
					
					// 値をコピー
					switch (originalCell.getCellType()) {
						case STRING:
							newCell.setCellValue(originalCell.getStringCellValue());
						    break;
						case NUMERIC:
							newCell.setCellValue(originalCell.getNumericCellValue());
							break;
						case BOOLEAN:
							newCell.setCellValue(originalCell.getBooleanCellValue());
							break;
						case FORMULA: // 計算式である場合。例：「=SUM(A1:A10)」や「=A1+B1」
							newCell.setCellFormula(originalCell.getCellFormula());
							break;
						default:
							newCell.setCellValue(originalCell.toString());
							break;
					}
					
				}
			}
			
			//------------------------------------------------
			// 印刷範囲をコピー
			if (originalSheet.getPrintSetup() != null) {
				newSheet.setPrintGridlines(originalSheet.isPrintGridlines());
				
				int originalSheetIndex = workbook.getSheetIndex(origiunalSheetName);
				int newSheetIndex      = workbook.getSheetIndex(newSheetName);
				String printArea       = workbook.getPrintArea(originalSheetIndex);
				//log.info("【INF】" + pgmId + ":originalSheetIndex=[" + Integer.toString(originalSheetIndex) +"]");
				//log.info("【INF】" + pgmId + ":newSheetIndex     =[" + Integer.toString(newSheetIndex) +"]");
				//log.info("【INF】" + pgmId + ":printArea         =[" + printArea +"]");
				printArea = printArea.replace(origiunalSheetName, "").replace("'","").replace("!", ""); // prinAreaには「'元のシート名'!$A$1:$F$102」という文字列が入っているので、元のシート名とシングクォートと"!"を""に置き換えて「$A$1:$F$102」という文字列にする
				//log.info("【INF】" + pgmId + ":printArea 編集後  =[" + printArea +"]");
				if (printArea != null) {
					workbook.setPrintArea(newSheetIndex, printArea);
				}
			}
			
			
			
			
			// 出力ファイルの保存
			/*
			try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
			    workbook.write(fos);
			}
			*/
			
			log.info("【INF】" + pgmId + ":処理終了");
			
			return newSheet;
			
		}catch(Exception e){
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	// 汎用関数：行と列を指定して値をセットするメソッド
	private void setCellValue(Sheet sheet, int rowIndex, int columnIndex, String value) {
		try {
			Row row   = sheet.getRow(rowIndex);
			Cell cell = row.getCell(columnIndex);
			cell.setCellValue(value);
		}catch(Exception e){
			log.error("【ERR】setCellValue(String):異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	private void setCellValue(Sheet sheet, int rowIndex, int columnIndex, int value) {
		try {
			Row row   = sheet.getRow(rowIndex);
			Cell cell = row.getCell(columnIndex);
			cell.setCellValue(value);
		}catch(Exception e){
			log.error("【ERR】setCellValue(int):異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	private void setCellValue(Sheet sheet, int rowIndex, int columnIndex, double value) {
		try {
			Row row   = sheet.getRow(rowIndex);
			Cell cell = row.getCell(columnIndex);
			cell.setCellValue(value);
		}catch(Exception e){
			log.error("【ERR】setCellValue(double):異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	private void setCellValue(Sheet sheet, int rowIndex, int columnIndex, LocalDateTime value) {
		try {
			Row row   = sheet.getRow(rowIndex);
			Cell cell = row.getCell(columnIndex);
			cell.setCellValue(value);
		}catch(Exception e){
			log.error("【ERR】setCellValue(LocalDateTime):異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	private void setCellValue(Sheet sheet, int rowIndex, int columnIndex, LocalDate value) {
		try {
			Row row   = sheet.getRow(rowIndex);
			Cell cell = row.getCell(columnIndex);
			cell.setCellValue(value);
		}catch(Exception e){
			log.error("【ERR】setCellValue(LocalDate):異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	
	
	// 引数のExcel workBook に対し給与明細のExcelを作成しダウンロード
	public boolean createPaySlipExcel(String filterTargetYM, Workbook workbook) {
		
		String pgmId = classId + ".createPaySlip";
		log.info("【INF】" + pgmId + ":処理開始 対象年月(YYYY-MM形式の文字列)=[" + filterTargetYM + "]");
		
		try {
			
			// 社員マスタを検索
			
			//------------------------------------------------
			// 社員・時給情報を検索
			
			String sql = " select";
			sql  = sql + "     EMP.EMPLOYEEID";
			sql  = sql + "    ,EMP.EMPLOYEENAME";
			sql  = sql + " from";
			sql  = sql + "     TM_EMPLOYEE EMP";
			sql  = sql + " where";
			sql  = sql + "     EMP.DELETEFLG = false";
			sql  = sql + " and EMP.EMPLOYEEID not in (?, ?)"; // 管理者とテストユーザは除外
			sql  = sql + " order by";
			sql  = sql + "     EMP.EMPLOYEEID";
			
			// queryForListメソッドでSQLを実行し、結果MapのListで受け取る。
			List<Map<String, Object>> rsListEmpt = this.jdbcTemplate.queryForList(
														 sql
														,SpecialUser.KANRI_USER
														,SpecialUser.TEST_USER
														);
			
			// 社員マスタの件数分LOOP
			for (Map<String, Object> rs: rsListEmpt) {
				
				String employeeId   = rs.get("EMPLOYEEID").toString();
				String employeeName = rs.get("EMPLOYEENAME").toString();
				boolean ret = false;
				
				log.info("【INFO】" + pgmId + ":■------社員名=[" + employeeName + "]の給与明細出力処理 開始---------");
				
				//------------------------------------------------
				// シートをコピーして対象社員のシートを作成
				//
				//※社員１人につき２シート作成(出退勤記録、給与明細)
				//
				
				// シートを取得してコピー
				String originalSheetName = "松岡農園 出退勤記録原本";
				String newSheetName      = "出退勤記録_" + employeeName;
				
				Sheet newSheet = this.copyExcelSheet(workbook, originalSheetName, newSheetName);
				if (newSheet == null) {
					log.info("【ERR】" + pgmId + ":処理終了：給与明細Exclファイルの出力(シートのコピー処理)で異常が発生しました。");
					return false;
				}
				
				//------------------------------------------------
				// 改ページ位置の設定を行う(42行目と43行目の間で改ページ)
				workbook.getSheet(newSheetName).setRowBreak(42 - 1);
				
				
				//------------------------------------------------
				// 印刷設定を行う
				
				PrintSetup printSetup = workbook.getSheet(newSheetName).getPrintSetup();
				printSetup.setPaperSize(PrintSetup.A4_PAPERSIZE); // A4サイズに設定
				printSetup.setLandscape(false);      // false:印刷を縦方向に設定 true:印刷を横方向に設定
				printSetup.setFitWidth( (short) 1);  // 横1ページに合わせる
				printSetup.setFitHeight((short) 0);  // 高さは自動調整
				
				workbook.getSheet(newSheetName).setFitToPage(true); // 上記の「横1ページに合わせる」を有効にするために必要らしい
				
				// ヘッダとフッタの設定
				Header header = workbook.getSheet(newSheetName).getHeader();
				Footer footer = workbook.getSheet(newSheetName).getFooter();
				
				// 中央ヘッダにシート名を設定
				header.setCenter(HeaderFooter.tab());
				
				// 中央フッタにページ番号と総ページ数を設定
				footer.setCenter(HeaderFooter.page() + " / " + HeaderFooter.numPages());
				
				//------------------------------------------------
				// 出退勤の情報(タイムカードの情報)を取得
				
				
				
				
				//------------------------------------------------
				// 出退勤の情報(タイムカードの情報)をExcelに転記
				
				
				
				
				
				
				
				
				
				//------------------------------------------------
				// 出退勤の情報(登録データそのまま)を取得
				ExcelClockInOut clockInOutData =  this.getClockInOutData(employeeId, filterTargetYM);
				if (clockInOutData == null) {
					log.info("【ERR】" + pgmId + ":処理終了：給与明細Exclファイルの出力(出退勤情報のExcel出力で異常が発生しました。");
					return false;
				}
				
				
				
				//------------------------------------------------
				// 出退勤の情報(登録データそのまま)をExcelに転記
				ret = this.outputExcelClockInOutData(clockInOutData ,newSheet);
				if (ret == false) {
					log.info("【ERR】" + pgmId + ":処理終了：給与明細Exclファイルの出力(出退勤情報のExcel出力で異常が発生しました。");
					return false;
				}
				
				
				
				
				
				
				//------------------------------------------------
				// 給与明細の情報を取得
				
				
				
				
				//------------------------------------------------
				// 給与明細の情報をExcelに転記
				
				
				
				
				
				
				
				log.info("【INFO】" + pgmId + ":■------社員名=[" + employeeName + "]の給与明細出力処理 終了---------");
				
			}
			
			
			
			
			log.info("【INF】" + pgmId + ":処理終了");
			
			return true;
			
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			//検索した結果を返却します
			return false;
		}
	}
	
	
	
	// ------------------------------------------------
	// 出退勤記録（一覧）用クラス
	// ------------------------------------------------
	@Data
	private class ExcelClockInOut {
		
		//対象年月(YYYY/MM/01)と月初日を持つ
		@DateTimeFormat(pattern = "yyyy/MM/dd")
		private LocalDate targetYMfirstDate;
		
		//社員ID
		private String employeeId;
		//社員名
		private String employeeName;
		//時給
		private int hourwage; //時給
		//勤務時間合計
		private double workingHoursSum;
		//休憩時間合計
		private double breakOutTimeSum;
		ArrayList<ExcelClockInOutDetail> details = new ArrayList<ExcelClockInOutDetail>();
		
		public double getWorkingHoursSum() {
			
			workingHoursSum = 0;
			for (int index = 0 ; index < this.details.size(); index ++) {
				workingHoursSum = workingHoursSum + this.details.get(index).getWorkingHouers();
			}
			return workingHoursSum;
		}
		
	}
	@Data
	private class ExcelClockInOutDetail {
		
		//勤務日
		@DateTimeFormat(pattern = "yyyy/MM/dd")
		private LocalDate targetDate;
		
		//出勤日時
		@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
		private LocalDateTime clockInDateTime;
		
		//退勤日時
		@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
		private LocalDateTime clockOutDateTime;
		
		//勤務時間
		private double workingHouers;
		
		//休憩時間
		
	}
	
	
	
	// ------------------------------------------------
	// タームカード検索
	private ExcelClockInOut getTimeCardData(String employeeId, String filterTargetYM) {
		
		String pgmId = classId + ".getTimeCardData";
		log.info("【INF】" + pgmId + ":処理開始 社員ID=[" + employeeId + "]、対象年月(YYYY-MM形式の文字列)=[" + filterTargetYM + "]");
		
		ExcelClockInOut retObj = new ExcelClockInOut();
		
		try {
			
			//引数の対象年月の最初の日付をYYYY-MM-DD形式で取得
			String targetYMDfirstDay = filterTargetYM + "-01";
			
			//------------------------------------------------
			// 社員・時給情報を検索
			String sql = " select";
			sql  = sql + "     EMP.EMPLOYEENAME";
			sql  = sql + "    ,WAG.HOURLYWAGE";
			sql  = sql + " from";
			sql  = sql + "     TM_EMPLOYEE EMP";
			sql  = sql + " inner join TM_HOURLYWAGE WAG";
			sql  = sql + "     on  WAG.EMPLOYEEID      = EMP.EMPLOYEEID";
			sql  = sql + " where";
			sql  = sql + "     EMP.EMPLOYEEID      = ?";
			sql  = sql + " and WAG.STARTDATETIME  <= ?"; //時給は月初日が開始～終了内のものを参照する
			sql  = sql + " and WAG.ENDDATETIME    >  ?";
			
			// queryForListメソッドでSQLを実行し、結果MapのListで受け取る。
			List<Map<String, Object>> rsListEmpt = this.jdbcTemplate.queryForList(
														 sql
														,employeeId
														,targetYMDfirstDay
														,targetYMDfirstDay
														);
			
			for (Map<String, Object> rs: rsListEmpt) {
				
				retObj.setTargetYMfirstDate(LocalDate.parse(targetYMDfirstDay)); //対象年月の月初日をセット
				retObj.setEmployeeId(employeeId);
				retObj.setEmployeeName(rs.get("EMPLOYEENAME").toString());
				retObj.setHourwage(Integer.parseInt(rs.get("HOURLYWAGE").toString()));
				// 検索結果は１件のみ
				break;
			}
			
			
			
			// 引数.対象年月(YYYY-MM形式)から年と月を切出し
			
			
			
			// SQLで指定年月の全日付を取得
			
			
			
			
			// 全日付の件数分LOOP
			
			
			
			// 出退勤情報を検索
			// 複数レコード取得できる場合、その日付の最初の出勤時間、最後の退勤時間
			// 合計の勤務時間、休憩時間を取得
			
			
			//------------------------------------------------
			// 出退勤情報を検索
			
			sql        = " select";
			sql  = sql + "     (CONCAT_WS('-', CLOCKINYEAR, CLOCKINMONTH ,CLOCKINDAY)) CLOCKINDATE";  // YYYY-MM-DD形式で取得
			sql  = sql + "    ,DATE_FORMAT(CLOCKINDATETIME ,'%Y%m%d%H%i%S') CLOCKINDATETIME_STRING";  // YYYYMMDDHHMMSS形式で取得（時間は24時間制）
			sql  = sql + "    ,DATE_FORMAT(CLOCKOUTDATETIME,'%Y%m%d%H%i%S') CLOCKOUTDATETIME_STRING"; // YYYYMMDDHHMMSS形式で取得（時間は24時間制）
			sql  = sql + "    ,HOURLYWAGE";
			sql  = sql + "    ,WORKING_HOUERS";
			sql  = sql + " from";
			sql  = sql + "     TT_CLOCKINOUT";
			sql  = sql + " where";
			sql  = sql + "     EMPLOYEEID       = ?";
			sql  = sql + " and CLOCKINYEAR      = ?";
			sql  = sql + " and CLOCKINMONTH     = ?";
			sql  = sql + " order by";
			sql  = sql + "     CLOCKINDATETIME";
			
			
			
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
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(
														 sql
														,employeeId
														,filterTargetYM.substring(0, 4)
														,filterTargetYM.substring(5, 7)
														);
			
			
			for (Map<String, Object> rs: rsList) {
				
				ExcelClockInOutDetail detail = new ExcelClockInOutDetail();
				
				DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
				
				//出勤日
				detail.setTargetDate(LocalDate.parse(rs.get("CLOCKINDATE").toString()));
				//出勤日時
				String clockInDateString = rs.get("CLOCKINDATETIME_STRING").toString();
				detail.setClockInDateTime(LocalDateTime.parse(clockInDateString, formatterDateTime));
				//退勤日時
				if (rs.get("CLOCKOUTDATETIME_STRING") != null) {
					String clockOutDateString = rs.get("CLOCKOUTDATETIME_STRING").toString();
					detail.setClockOutDateTime(LocalDateTime.parse(clockOutDateString, formatterDateTime));
				}
				
				detail.setWorkingHouers(Double.parseDouble(rs.get("WORKING_HOUERS").toString()));
				
				
				retObj.getDetails().add(detail);
				
			}
			
			
			log.info("【INF】" + pgmId + ":処理終了");
			
			
			return retObj;
			
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			//検索した結果を返却します
			return null;
		}
	}
	
	
	
	
	// ------------------------------------------------
	// 出退勤記録検索
	private ExcelClockInOut getClockInOutData(String employeeId, String filterTargetYM) {
		
		String pgmId = classId + ".getClockInOutData";
		log.info("【INF】" + pgmId + ":処理開始 社員ID=[" + employeeId + "]、対象年月(YYYY-MM形式の文字列)=[" + filterTargetYM + "]");
		
		ExcelClockInOut retObj = new ExcelClockInOut();
		
		try {
			
			//引数の対象年月の最初の日付をYYYY-MM-DD形式で取得
			String targetYMDfirstDay = filterTargetYM + "-01";
			
			//------------------------------------------------
			// 社員・時給情報を検索
			String sql = " select";
			sql  = sql + "     EMP.EMPLOYEENAME";
			sql  = sql + "    ,WAG.HOURLYWAGE";
			sql  = sql + " from";
			sql  = sql + "     TM_EMPLOYEE EMP";
			sql  = sql + " inner join TM_HOURLYWAGE WAG";
			sql  = sql + "     on  WAG.EMPLOYEEID      = EMP.EMPLOYEEID";
			sql  = sql + " where";
			sql  = sql + "     EMP.EMPLOYEEID      = ?";
			sql  = sql + " and WAG.STARTDATETIME  <= ?"; //時給は月初日が開始～終了内のものを参照する
			sql  = sql + " and WAG.ENDDATETIME    >  ?";
			
			// queryForListメソッドでSQLを実行し、結果MapのListで受け取る。
			List<Map<String, Object>> rsListEmpt = this.jdbcTemplate.queryForList(
														 sql
														,employeeId
														,targetYMDfirstDay
														,targetYMDfirstDay
														);
			
			for (Map<String, Object> rs: rsListEmpt) {
				
				retObj.setTargetYMfirstDate(LocalDate.parse(targetYMDfirstDay)); //対象年月の月初日をセット
				retObj.setEmployeeId(employeeId);
				retObj.setEmployeeName(rs.get("EMPLOYEENAME").toString());
				retObj.setHourwage(Integer.parseInt(rs.get("HOURLYWAGE").toString()));
				// 検索結果は１件のみ
				break;
			}
			
			
			
			
			
			//------------------------------------------------
			// 出退勤情報を検索
			
			sql        = " select";
			sql  = sql + "     (CONCAT_WS('-', CLOCKINYEAR, CLOCKINMONTH ,CLOCKINDAY)) CLOCKINDATE";  // YYYY-MM-DD形式で取得
			sql  = sql + "    ,DATE_FORMAT(CLOCKINDATETIME ,'%Y%m%d%H%i%S') CLOCKINDATETIME_STRING";  // YYYYMMDDHHMMSS形式で取得（時間は24時間制）
			sql  = sql + "    ,DATE_FORMAT(CLOCKOUTDATETIME,'%Y%m%d%H%i%S') CLOCKOUTDATETIME_STRING"; // YYYYMMDDHHMMSS形式で取得（時間は24時間制）
			sql  = sql + "    ,HOURLYWAGE";
			sql  = sql + "    ,WORKING_HOUERS";
			sql  = sql + " from";
			sql  = sql + "     TT_CLOCKINOUT";
			sql  = sql + " where";
			sql  = sql + "     EMPLOYEEID       = ?";
			sql  = sql + " and CLOCKINYEAR      = ?";
			sql  = sql + " and CLOCKINMONTH     = ?";
			sql  = sql + " order by";
			sql  = sql + "     CLOCKINDATETIME";
			
			
			
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
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(
														 sql
														,employeeId
														,filterTargetYM.substring(0, 4)
														,filterTargetYM.substring(5, 7)
														);
			
			
			for (Map<String, Object> rs: rsList) {
				
				ExcelClockInOutDetail detail = new ExcelClockInOutDetail();
				
				DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
				
				//出勤日
				detail.setTargetDate(LocalDate.parse(rs.get("CLOCKINDATE").toString()));
				//出勤日時
				String clockInDateString = rs.get("CLOCKINDATETIME_STRING").toString();
				detail.setClockInDateTime(LocalDateTime.parse(clockInDateString, formatterDateTime));
				//退勤日時
				if (rs.get("CLOCKOUTDATETIME_STRING") != null) {
					String clockOutDateString = rs.get("CLOCKOUTDATETIME_STRING").toString();
					detail.setClockOutDateTime(LocalDateTime.parse(clockOutDateString, formatterDateTime));
				}
				
				detail.setWorkingHouers(Double.parseDouble(rs.get("WORKING_HOUERS").toString()));
				
				
				retObj.getDetails().add(detail);
				
			}
			
			
			log.info("【INF】" + pgmId + ":処理終了");
			
			
			return retObj;
			
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			//検索した結果を返却します
			return null;
		}
	}
	
	
	
	// ------------------------------------------------
	// 出退勤記録Excel出力
	private boolean outputExcelClockInOutData(ExcelClockInOut clockInOutData ,Sheet sheet) {
		
		String pgmId = classId + ".outputExcelClockInOutData";
		log.info("【INF】" + pgmId + ":処理開始 社員=[" + clockInOutData.getEmployeeName() + "]");
		
		try {
			
			
			// ------------------------------------------------
			// ヘッダ情報の出力
			
			//※Excel46行目、3列名が勤務年月をセットするセルの位置　→セルの位置は0スタートなので位置を-1する
			this.setCellValue(sheet, 46 - 1, 3 - 1, clockInOutData.getTargetYMfirstDate());  // 勤務年月
			this.setCellValue(sheet, 47 - 1, 3 - 1, clockInOutData.getEmployeeId());         // 社員ID
			this.setCellValue(sheet, 48 - 1, 3 - 1, clockInOutData.getEmployeeName());       // 社員名
			
			this.setCellValue(sheet, 46 - 1, 5 - 1, clockInOutData.getWorkingHoursSum());    // 勤務時間合計
			this.setCellValue(sheet, 47 - 1, 5 - 1, clockInOutData.getHourwage());           // 時給
			
			
			// ------------------------------------------------
			// 明細情報の出力
			
			int startRowIndex = 52 - 1; //52行が明細行の１行目
			int dataIndex     =  0 - 1;
			
			for (int rowIndex = startRowIndex; rowIndex < startRowIndex + clockInOutData.getDetails().size(); rowIndex++) {
				
				dataIndex = dataIndex + 1;
				//出勤日
				this.setCellValue(sheet, rowIndex, 2 - 1, clockInOutData.getDetails().get(dataIndex).getTargetDate());
				//出勤日時
				this.setCellValue(sheet, rowIndex, 3 - 1, clockInOutData.getDetails().get(dataIndex).getClockInDateTime());
				//退勤日時
				this.setCellValue(sheet, rowIndex, 4 - 1, clockInOutData.getDetails().get(dataIndex).getClockOutDateTime());
				//勤務時間
				this.setCellValue(sheet, rowIndex, 5 - 1, clockInOutData.getDetails().get(dataIndex).getWorkingHouers());
				
			}
			
			log.info("【INF】" + pgmId + ":処理終了");
			return true;
			
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			//検索した結果を返却します
			return false;
		}
	}
	
	
	
	
	// ------------------------------------------------
	// 給与明細用クラス
	// ------------------------------------------------
	@Data
	private class ExcelPaySlipDetail {
		
		//対象年月(YYYY/MM/01)と月初日を持つ
		@DateTimeFormat(pattern = "yyyy/MM/dd")
		private LocalDate targetYMfirstDate;
		
		// ------------------------------------------------
		// 勤怠項目
		
		//社員ID
		
		//社員名
		
		//出勤日数
		
		//午後出勤日数(基本は0でOK→要望があったら仕様を決めて入れる)
		
		
		//勤務時間合計(所定労働時間) →勤怠マスタから算出した勤務時間
		
		
		//完了列数(収穫以外の作業を行った場合)
		
		
		//作業時間→ハウス作業進捗、ハウス作業(収穫)進捗テーブルから算出した作業時間
		
		
		//箱数
		
		
		//平均作業時間
		
		
		// ------------------------------------------------
		// 支給項目
		
		
		//時給
		
		
		//追通勤手当
		
		
		// ------------------------------------------------
		// 控除項目
		
		//所得税→毎年求め方が変動するためシステムでは求めない
		
		
		
	}
	
	
	
	
}
