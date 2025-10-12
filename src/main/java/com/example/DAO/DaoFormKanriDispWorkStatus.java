package com.example.DAO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.example.counst.SpecialUser;
import com.example.counst.SpecialWork;
import com.example.form.FormKanriDispWorkStatus;
import com.example.form.FormKanriDispWorkStatus.ActiveWorkList;
import com.example.form.FormKanriDispWorkStatus.ActiveWorkRow;
import com.example.form.FormKanriDispWorkStatus.NonActiveWorkCol;
import com.example.form.FormKanriDispWorkStatus.NonActiveWorkRow;
import com.example.form.FormKanriDispWorkStatus.ShukakuStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DaoFormKanriDispWorkStatus {
	
	private JdbcTemplate  jdbcTemplate;
	private String classId = "DaoFormKanriDispWorkStatus";
	
	// コンストラクタ
	public DaoFormKanriDispWorkStatus(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	// リストを取得
	public FormKanriDispWorkStatus getDispData() {
		
		// 年月日時分秒までの日時フォーマットを準備
		//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		String pgmId = classId + ".getDispData";
		log.info("【INF】" + pgmId + ":処理開始");
		
		
		// 返却値
		FormKanriDispWorkStatus retForm = new FormKanriDispWorkStatus();
		try {
			
			
			//------------------------------------------------------------------------------------------------
			// 画面上部に表示する”稼働中の作業”を取得
			
			retForm.setActiveWorkLists(this.getActiveWorkLists());
			
			
			//------------------------------------------------
			// 画面中部に表示する”収穫状況”を取得
			retForm.setShukakuStatusList(this.getShukakuStatusList());
			
			
			//------------------------------------------------------------------------------------------------
			// 画面下部に表示する”全ハウス・全作業の稼働状況”を取得
			
			retForm.setNonActiveWorkList(this.getNonActiveWorkList());
			
			
			//------------------------------------------------------------------------------------------------
			// 画面下部に表示する”全ハウス・全作業の稼働状況”表の「ヘッダ」を編集
			
			for (int index = 0 ; index < retForm.getNonActiveWorkList().get(0).getNonActiveWorkCol().size(); index ++) {
				//一覧表の１行目の”列の数”分LOOPして、表示する作業名をヘッダとしてセット
				String wkWorkName = retForm.getNonActiveWorkList().get(0).getNonActiveWorkCol().get(index).getWorkName();
				retForm.getNonActiveWorkListHeader().add(wkWorkName);
			}
			
			
			
			log.info("【INF】" + pgmId + ":処理終了 作業中の作業=[" + Integer.toString(retForm.getActiveWorkLists().size()) + "]件");
			return retForm;
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了[" + e.toString() + "]");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return null;
		}
	}
	
	
	
	
	
	
	//------------------------------------------------------------------------------------------------
	// 画面上部に表示する”稼働中の作業”を取得
	
	private ArrayList<ActiveWorkList> getActiveWorkLists() {
		
		// 年月日時分秒までの日時フォーマットを準備
		//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		String pgmId = classId + ".getActiveWorkList";
		log.info("【INF】" + pgmId + ":処理開始");
		
		
		// 返却値
		ArrayList<ActiveWorkList> retLists = new ArrayList<ActiveWorkList>();
		try {
			
			//①ハウスマスタ-②列マスタ-③作業情報を結合して検索
			//③はハウス、列、作業毎にグループ化。作業開始日時が最も大きいものを表示対象にする
			
			// ■以下のようにleft joinで結合検索
			//
			// +------------------------+   +------------------------+   +------------------------------------+   +----------------------------------------+
			// |  ①全ハウスと全作業    |   |  ②左記①のすべての列  |   |  ③左記①②に対する作業(日時MAX)   |   |  ④左記①②③に対する作業(作業状況)    |
			// |                        |   |                        |   |                                    |   |                                        |
			// |  ３号隔離 牽引         |-->|  ３号隔離 牽引   01    |-->|  ３号隔離 牽引   01 開始日時 MAX   |-->|  ３号隔離 牽引   01 開始/終了日時 進捗 |
			// |  ３号隔離 葉かき       |   |                  02    |   |                  02 開始日時 MAX   |   |                  02 開始/終了日時 進捗 |
			// |  ３号隔離 摘花         |   |                  03    |   |                  03 開始日時 MAX   |   |                  03 開始/終了日時 進捗 |
			// |  ４号隔離 牽引         |   |                  04    |   |                  04 開始日時 MAX   |   |                  04 開始/終了日時 進捗 |
			// |  ４号隔離 葉かき       |   |                  05    |   |                  05 開始日時 MAX   |   |                  05 開始/終了日時 進捗 |
			// |  ４号隔離 摘花         |   |                  06    |   |                  06 開始日時 MAX   |   |                  06 開始/終了日時 進捗 |
			// |  11号隔離 牽引         |   |                  07    |   |                  07 開始日時 MAX   |   |                  07 開始/終了日時 進捗 |
			// |  11号隔離 葉かき       |   |                  08    |   |                  08 開始日時 MAX   |   |                  08 開始/終了日時 進捗 |
			// |  11号隔離 摘花         |   |                  ..    |   |                  ..                |   |                  ..                    |
			// |  ８号土耕 牽引         |   |                        |   |                                    |   |                                        |
			// |  ８号土耕 葉かき       |   |  ３号隔離 葉かき 01    |   |  ３号隔離 葉かき 01 開始日時 MAX   |   |  ３号隔離 葉かき 01 開始/終了日時 進捗 |
			// |  ８号土耕 摘花         |   |                  02    |   |                  02 開始日時 MAX   |   |                  02 開始/終了日時 進捗 |
			// |       ...              |   |                  03    |   |                  03 開始日時 MAX   |   |                  03 開始/終了日時 進捗 |
			// |                        |   |                  04    |   |                  04 開始日時 MAX   |   |                  04 開始/終了日時 進捗 |
			// |                        |   |                  05    |   |                  05 開始日時 MAX   |   |                  05 開始/終了日時 進捗 |
			// |                        |   |                  06    |   |                  06 開始日時 MAX   |   |                  06 開始/終了日時 進捗 |
			// |                        |   |                  ..    |   |                  ..                |   |                  ..                    |
			// |                        |   |                        |   |                                    |   |                                        |
			// +------------------------+   +------------------------+   +------------------------------------+   +----------------------------------------+
			//
			//	                                                                   ▼
			//	                                                                   ▼
			// ■上位検索結果に対して、各ハウス、各作業のMAX開始/終了日時をinner joinする（MAX日時、ハウスID、作業IDをくっつけたものをソートキーにするため）
			// 
			// +-------------------------------------------------------------------------------------------------------------------------------------------+
			// |  ⑤全ハウスと全作業                                                                                                                       |
			// |                                                                                                                                           |
			// |  ３号隔離 牽引            MAX開始/終了日時                                                                                                |
			// |  ３号隔離 葉かき          MAX開始/終了日時                                                                                                |
			// |  ３号隔離 摘花            MAX開始/終了日時                                                                                                |
			// |  ４号隔離 牽引            MAX開始/終了日時                                                                                                |
			// |  ４号隔離 葉かき          MAX開始/終了日時                                                                                                |
			// |  ４号隔離 摘花            MAX開始/終了日時                                                                                                |
			// |  11号隔離 牽引            MAX開始/終了日時                                                                                                |
			// |  11号隔離 葉かき          MAX開始/終了日時                                                                                                |
			// |  11号隔離 摘花            MAX開始/終了日時                                                                                                |
			// |  ８号土耕 牽引            MAX開始/終了日時                                                                                                |
			// |  ８号土耕 葉かき          MAX開始/終了日時                                                                                                |
			// |  ８号土耕 摘花            MAX開始/終了日時                                                                                                |
			// |       ...                                                                                                                                 |
			// +-------------------------------------------------------------------------------------------------------------------------------------------+
			//
			
			// ------------------------------------------------
			// 【メモ】
			// 画面表示に使用する進捗率について
			// 下の通り最新の進捗率を画面表示に使用する
			//
			// ■例１：作業未実施状態である場合
			// 検索結果なし
			//			              ▼
			//			          画面表示しない
			// 
			// ■例２：作業中である場合
			// ３号隔離  01   葉かき  2024/08/25 15:00 ～ 2024/08/25 18:00   0% ～ 20%
			// ３号隔離  01   葉かき  2024/08/26 09:00 ～ 2024/08/25 12:00  20% ～ 80% ★コレを検索
			//			              ▼
			//			          80%(進捗率_終了)を表示
			// 
			// ■例３：作業中である場合
			// ３号隔離  01   葉かき  2024/08/25 15:00 ～ 2024/08/25 18:00   0% ～ 20%
			// ３号隔離  01   葉かき  2024/08/26 09:00 ～ 2024/08/25 12:00  20% ～ 80%
			// ３号隔離  01   葉かき  2024/08/26 09:00 ～                   80% ～  0% ★コレを検索
			//			              ▼
			//			          80%(進捗率_開始)を表示
			// 
			// ■例３：作業完了状態である場合
			// ３号隔離  01   葉かき  2024/08/25 15:00 ～ 2024/08/25 18:00   0% ～ 20%
			// ３号隔離  01   葉かき  2024/08/26 09:00 ～ 2024/08/25 12:00  20% ～ 80%
			// ３号隔離  01   葉かき  2024/08/26 09:00 ～ 2024/08/26 15:00  80% ～100% ★コレを検索
			//			              ▼
			//			         100%(進捗率_終了)を表示
			//
			// ------------------------------------------------
			
			String sql = " select";
			sql  = sql + "     VT_WORK01.HOUSEID";
			sql  = sql + "    ,VT_WORK01.HOUSENAME";
			sql  = sql + "    ,VT_WORK01.WORKID";
			sql  = sql + "    ,VT_WORK01.WORKNAME";
			sql  = sql + "    ,VT_WORK01.COLNO";
			sql  = sql + "    ,VT_WORK01.STARTDATETIME_STRING";
			sql  = sql + "    ,VT_WORK01.STARTEMPLOYEEID";
			sql  = sql + "    ,VT_WORK01.STARTEMPLOYEENAME";
			sql  = sql + "    ,VT_WORK01.ENDDATETIME_STRING";
			sql  = sql + "    ,VT_WORK01.ENDEMPLOYEEID";
			sql  = sql + "    ,VT_WORK01.PERCENT";
			sql  = sql + "    ,VT_WORK02.MAX_DATETIME_STRING";
			sql  = sql + "    ,VT_WORK03.FIRST_STARTDATETIME_STRING";
			sql  = sql + " from";
			sql  = sql + " (";
			sql  = sql + "     select";
			sql  = sql + "         VM_HOUSEWORK.HOUSEID";
			sql  = sql + "        ,VM_HOUSEWORK.HOUSENAME";
			sql  = sql + "        ,VM_HOUSEWORK.WORKID";
			sql  = sql + "        ,VM_HOUSEWORK.WORKNAME";
			sql  = sql + "        ,TM_HOUSECOL.COLNO";
			sql  = sql + "        ,DATE_FORMAT(TT_WORK.STARTDATETIME,'%Y%m%d%H%i%S') STARTDATETIME_STRING";
			sql  = sql + "        ,TT_WORK.STARTEMPLOYEEID";
			sql  = sql + "        ,TM_EMPLOYEE.EMPLOYEENAME STARTEMPLOYEENAME";
			sql  = sql + "        ,DATE_FORMAT(TT_WORK.ENDDATETIME,'%Y%m%d%H%i%S') ENDDATETIME_STRING";
			sql  = sql + "        ,TT_WORK.ENDEMPLOYEEID";
			sql  = sql + "        ,CASE WHEN TT_WORK.PERCENT = 0 THEN TT_WORK.PERCENT_START ELSE TT_WORK.PERCENT END AS PERCENT";//★進捗率は最新のモノを使用する
			sql  = sql + "     from";
			// ①：ハウスと作業を「全て」列挙する。ただし作業状況が１つもないハウスと作業は除く。
			sql  = sql + "        (";
			sql  = sql + "         select distinct";
			sql  = sql + "             TM_HOUSE.HOUSEID";
			sql  = sql + "            ,TM_HOUSE.HOUSENAME";
			sql  = sql + "            ,TM_WORK.WORKID";
			sql  = sql + "            ,TM_WORK.WORKNAME";
			sql  = sql + "         from TM_HOUSE";
			sql  = sql + "         cross join TM_WORK";
			sql  = sql + "         inner join TT_HOUSE_WORKSTATUS TT_WORK";
			sql  = sql + "         on  TM_HOUSE.HOUSEID        = TT_WORK.HOUSEID";
			sql  = sql + "         and TM_WORK.WORKID          = TT_WORK.WORKID";
			sql  = sql + "         and TT_WORK.DELETEFLG       = 0";
			sql  = sql + "         and TT_WORK.STARTEMPLOYEEID <> '" + SpecialUser.TEST_USER + "'";//テストユーザは対象にしない
			sql  = sql + "        )VM_HOUSEWORK";
			// ②：上記①に対して全ての列（列マスタ）を列挙する
			sql  = sql + "     inner join";
			sql  = sql + "         TM_HOUSECOL";
			sql  = sql + "         on";
			sql  = sql + "             VM_HOUSEWORK.HOUSEID  = TM_HOUSECOL.HOUSEID";
			// ③：上記①②に対して作業状況が「あれば」ヒモ付ける。ただし①②に対して作業が複数存在する場合は作業開始日時がMAXのものとヒモ付ける
			sql  = sql + "     left join";
			sql  = sql + "         (";
			sql  = sql + "         select HOUSEID,COLNO,WORKID,MAX(STARTDATETIME) STARTDATETIME";
			sql  = sql + "         from TT_HOUSE_WORKSTATUS";
			sql  = sql + "         where";
			sql  = sql + "             TT_HOUSE_WORKSTATUS.STARTEMPLOYEEID <> '" + SpecialUser.TEST_USER + "'";//テストユーザは対象にしない
			sql  = sql + "         and TT_HOUSE_WORKSTATUS.DELETEFLG       = 0";//未削除のデータのみ扱う 
			sql  = sql + "         group by HOUSEID,COLNO,WORKID";
			sql  = sql + "         )VT_WORK";
			sql  = sql + "         on";
			sql  = sql + "             VM_HOUSEWORK.HOUSEID    = VT_WORK.HOUSEID";
			sql  = sql + "         and TM_HOUSECOL.COLNO       = VT_WORK.COLNO";
			sql  = sql + "         and VM_HOUSEWORK.WORKID     = VT_WORK.WORKID";
			// ④：上記①②③に対して作業状況が「あれば」ヒモ付けてる。※作業の進捗状況(%)や作業開始・終了情報などを取得するため
			sql  = sql + "     left join";
			sql  = sql + "         TT_HOUSE_WORKSTATUS TT_WORK";
			sql  = sql + "         on";
			sql  = sql + "             VT_WORK.HOUSEID         = TT_WORK.HOUSEID";
			sql  = sql + "         and VT_WORK.COLNO           = TT_WORK.COLNO";
			sql  = sql + "         and VT_WORK.WORKID          = TT_WORK.WORKID";
			sql  = sql + "         and VT_WORK.STARTDATETIME   = TT_WORK.STARTDATETIME";
			sql  = sql + "         and TT_WORK.DELETEFLG       = 0";
			sql  = sql + "         and TT_WORK.STARTEMPLOYEEID <> '" + SpecialUser.TEST_USER + "'";//テストユーザは対象にしない
			sql  = sql + "     left join";
			sql  = sql + "         TM_EMPLOYEE";
			sql  = sql + "         on";
			sql  = sql + "             TT_WORK.STARTEMPLOYEEID = TM_EMPLOYEE.EMPLOYEEID";
			sql  = sql + " ) VT_WORK01";
			// ⑤：上記検索結果に対し▼のハウス、作業ごとのMAX日時テーブルと紐付ける（最後に作業を行った作業を画面最上部に表示するため）
			sql  = sql + " inner join";
			sql  = sql + " (";
			sql  = sql + "     select";
			sql  = sql + "         VT_WORK.HOUSEID";
			sql  = sql + "        ,VT_WORK.WORKID";
			sql  = sql + "        ,DATE_FORMAT(MAX(VT_WORK.DATETIME),'%Y%m%d%H%i%S') MAX_DATETIME_STRING";
			sql  = sql + "        ,CONCAT(DATE_FORMAT(MAX(VT_WORK.DATETIME),'%Y%m%d%H%i%S') ,VT_WORK.HOUSEID ,VT_WORK.WORKID) SORT_KEY"; //MAX日時、ハウスID、作業IDをくっつけたものをソートキーにする
			sql  = sql + "     from (";
			sql  = sql + "         select  HOUSEID";
			sql  = sql + "                ,WORKID";
			sql  = sql + "                ,STARTDATETIME DATETIME";
			sql  = sql + "         from    TT_HOUSE_WORKSTATUS";
			sql  = sql + "         where   STARTEMPLOYEEID <> '" + SpecialUser.TEST_USER + "'";//テストユーザは対象にしない
			sql  = sql + "         and     DELETEFLG        = 0";
			sql  = sql + "              union all";
			sql  = sql + "         select  HOUSEID";
			sql  = sql + "                ,WORKID";
			sql  = sql + "                ,ENDDATETIME DATETIME";
			sql  = sql + "         from    TT_HOUSE_WORKSTATUS";
			sql  = sql + "         where   STARTEMPLOYEEID <> '" + SpecialUser.TEST_USER + "'";//テストユーザは対象にしない
			sql  = sql + "         and     DELETEFLG        = 0";
			sql  = sql + "         and     ENDDATETIME is not null";
			sql  = sql + "         ) VT_WORK";
			sql  = sql + "         group by";
			sql  = sql + "             VT_WORK.HOUSEID";
			sql  = sql + "            ,VT_WORK.WORKID";
			sql  = sql + " ) VT_WORK02";
			sql  = sql + " on";
			sql  = sql + "     VT_WORK01.HOUSEID = VT_WORK02.HOUSEID";
			sql  = sql + " and VT_WORK01.WORKID  = VT_WORK02.WORKID";
			// ⑥：上記検索結果に対し▼のハウス、作業ごとのMIN開始日時テーブルと紐付ける（最後に作業を行った作業を画面最上部に表示するため）
			sql  = sql + " inner join";
			sql  = sql + " (";
			sql  = sql + "     select";
			sql  = sql + "         VT_WORK.HOUSEID";
			sql  = sql + "        ,VT_WORK.WORKID";
			sql  = sql + "        ,DATE_FORMAT(MIN(VT_WORK.DATETIME),'%Y%m%d%H%i%S') FIRST_STARTDATETIME_STRING";
			sql  = sql + "     from (";
			sql  = sql + "         select  HOUSEID";
			sql  = sql + "                ,WORKID";
			sql  = sql + "                ,STARTDATETIME DATETIME";
			sql  = sql + "         from    TT_HOUSE_WORKSTATUS";
			sql  = sql + "         where   STARTEMPLOYEEID <> '" + SpecialUser.TEST_USER + "'";//テストユーザは対象にしない
			sql  = sql + "         and     DELETEFLG        = 0";
			sql  = sql + "         ) VT_WORK";
			sql  = sql + "         group by";
			sql  = sql + "             VT_WORK.HOUSEID";
			sql  = sql + "            ,VT_WORK.WORKID";
			sql  = sql + " ) VT_WORK03";
			sql  = sql + " on";
			sql  = sql + "     VT_WORK01.HOUSEID = VT_WORK03.HOUSEID";
			sql  = sql + " and VT_WORK01.WORKID  = VT_WORK03.WORKID";
			sql  = sql + " order by";
			sql  = sql + "     VT_WORK02.SORT_KEY desc"; //MAX日時、ハウスID、作業IDをくっつけたものをソートキーにする
			sql  = sql + "    ,VT_WORK01.HOUSEID";
			sql  = sql + "    ,VT_WORK01.HOUSENAME";
			sql  = sql + "    ,VT_WORK01.WORKID";
			sql  = sql + "    ,VT_WORK01.COLNO";
			
			
			
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
			String startDateTimeString = "";
			String endDateTimeString = "";
			
			
			
			String wkHouseId = "";
			String wkWorkId  = "";
			
			int count = 0;
			
			FormKanriDispWorkStatus wkForm = new FormKanriDispWorkStatus();
			ActiveWorkList list = null; 
			
			
			for (Map<String, Object> rs: rsList) {
				
				count = count + 1;
				
				// 年月日時分秒までの日時フォーマットを準備
				DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
				
				
				if (wkHouseId.equals("") == true
				&&   wkWorkId.equals("") == true) {
					
					// 初回LOOPである場合、１件目のリスト（表）を作成
					
					//１件目の表を準備
					list = wkForm.new ActiveWorkList();
					
					list.setHouseId(    rs.get("HOUSEID").toString()    );
					list.setHouseName(  rs.get("HOUSENAME").toString()  );
					list.setWorkId(     rs.get("WORKID" ).toString()    );
					list.setWorkName(   rs.get("WORKNAME").toString()   );
					
					String firstStartDateString = rs.get("FIRST_STARTDATETIME_STRING").toString();
					list.setFirstStartDateTime(LocalDateTime.parse(firstStartDateString,formatterDateTime));
				
				} else  if (
					wkHouseId.equals(rs.get("HOUSEID").toString()) == false
				||   wkWorkId.equals(rs.get("WORKID").toString())  == false) {
					
					// 初回LOOPでなく、ハウスID・作業IDが異なる場合、新しいリスト（表）を作成
					
					// 返却する表を追加
					retLists.add(list);
					// 次の表を準備
					list = wkForm.new ActiveWorkList();
					
					list.setHouseId(    rs.get("HOUSEID").toString()    );
					list.setHouseName(  rs.get("HOUSENAME").toString()  );
					list.setWorkId(     rs.get("WORKID" ).toString()    );
					list.setWorkName(   rs.get("WORKNAME").toString()   );
					
					String firstStartDateString = rs.get("FIRST_STARTDATETIME_STRING").toString();
					list.setFirstStartDateTime(LocalDateTime.parse(firstStartDateString,formatterDateTime));
				}
				
				//ブレイクアウト用の変数をセット
				wkHouseId = rs.get("HOUSEID").toString();
				wkWorkId  = rs.get("WORKID").toString();
				
				
				
				ActiveWorkRow detail = wkForm.new ActiveWorkRow();
				
				
				
				// ハウスID
				detail.setHouseId(rs.get("HOUSEID").toString());
				// 作業ID
				detail.setWorkId(rs.get("WORKID").toString());
				//列No
				detail.setColNo(rs.get("COLNO").toString());
				
				
				
				// 作業開始日時
				if (rs.get("STARTDATETIME_STRING") != null) {
					startDateTimeString = rs.get("STARTDATETIME_STRING").toString();
					detail.setStartDateTime(LocalDateTime.parse(startDateTimeString,formatterDateTime));
				}
				// 作業開始社員ID
				if (rs.get("STARTEMPLOYEEID") != null) {
					detail.setStartEmployeeId(rs.get("STARTEMPLOYEEID").toString());
				}
				// 作業開始社員名
				if (rs.get("STARTEMPLOYEENAME") != null) {
					detail.setStartEmployeeName(rs.get("STARTEMPLOYEENAME").toString());
				}
				
				
				// 作業終了日時
				if (rs.get("ENDDATETIME_STRING") != null) {
					endDateTimeString   = rs.get("ENDDATETIME_STRING").toString();
					detail.setEndDateTime(LocalDateTime.parse(endDateTimeString,formatterDateTime));
				}
				// 作業終了社員ID
				if (rs.get("ENDEMPLOYEEID") != null) {
					detail.setEndEmployeeId(rs.get("ENDEMPLOYEEID").toString());
				}
				// 作業終了社員名  ※不要であるためSQLで取得してない
				//if (rs.get("ENDEMPLOYEENAME") != null) {
				//	detail.setEndEmployeeName(rs.get("ENDEMPLOYEENAME").toString());
				//}
				
				
				// 進捗率  ★最新のモノ
				if (rs.get("PERCENT") != null) {
					detail.setPercent(rs.get("PERCENT").toString());
				}
				
				
				
				// リストに追加
				list.getActiveWorKRows().add(detail);
				
			}
			
			// 検索件数が０件でなければ、最後の表を返却値に追加
			if (count > 0) {
				retLists.add(list);
			}
			
			
			
			log.info("【INF】" + pgmId + ":処理終了 作業中の作業=[" + Integer.toString(retLists.size()) + "]件");
			return retLists;
			
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了[" + e.toString() + "]");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return null;
		}
	}
	
	
	
	
	
	
	//------------------------------------------------------------------------------------------------
	// 画面中部に表示する”収穫状況”を取得
	
	private ArrayList<ShukakuStatus> getShukakuStatusList() {
		
		// 年月日時分秒までの日時フォーマットを準備
		//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		String pgmId = classId + ".getShukakuStatusList";
		log.info("【INF】" + pgmId + ":処理開始");
		
		
		try {
			
			String sql = " select";
			sql  = sql + "     VM_HOUSE.HOUSEID";
			sql  = sql + "    ,VM_HOUSE.HOUSENAME";
			sql  = sql + "    ,coalesce(VT_WORKS.BOXCOUNT_SUM,0) BOXCOUNT_SUM";  // COALESCE関数は、SQLで複数の引数の中から最初に非NULLの値を返す関数→ココではOracleでいうNVLの代わりに使ってる
			sql  = sql + " from";
			sql  = sql + "     (";
			sql  = sql + "     select    TM_HOUSE.HOUSEID";
			sql  = sql + "              ,TM_HOUSE.HOUSENAME";
			sql  = sql + "     from      TM_HOUSE";
			sql  = sql + "     where     DELETEFLG = False";
			sql  = sql + "     ) VM_HOUSE";
			sql  = sql + " left join";
			sql  = sql + "     (";
			sql  = sql + "     select    HOUSEID";
			sql  = sql + "              ,SUM(BOXCOUNT) BOXCOUNT_SUM";
			sql  = sql + "     from      TT_HOUSE_WORKSTATUS_SHUKAKU";
			sql  = sql + "     where     DELETEFLG = False";
			sql  = sql + "     and       STARTEMPLOYEEID <> '" + SpecialUser.TEST_USER + "'";
			sql  = sql + "     group by  HOUSEID";
			sql  = sql + "     ) VT_WORKS";
			sql  = sql + "     on  VM_HOUSE.HOUSEID  = VT_WORKS.HOUSEID";
			sql  = sql + " order by";
			sql  = sql + "     VM_HOUSE.HOUSEID";
			
			
			
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
			
			//インナークラスをNEWするために必要なクラス
			FormKanriDispWorkStatus wkForm = new FormKanriDispWorkStatus();
			
			// 返却値
			ArrayList<ShukakuStatus> retList = new ArrayList<ShukakuStatus>();
			
			
			for (Map<String, Object> rs: rsList) {
				
				ShukakuStatus row = wkForm.new ShukakuStatus();
				
				// ハウスID
				row.setHouseId(rs.get("HOUSEID").toString());
				// ハウス名
				row.setHouseName(rs.get("HOUSENAME").toString());
				//収穫ケース数
				row.setCaseCount(Double.parseDouble(rs.get("BOXCOUNT_SUM").toString()));
				
				
				//返却値のリストに追加
				retList.add(row);
				
			}
			
			
			log.info("【INF】" + pgmId + ":処理終了 表示するハウスの件数(行数)=[" + Integer.toString(retList.size()) + "]件");
			return retList;
			
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了[" + e.toString() + "]");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return null;
		}
	}
	
	
	
	
	
	
	//------------------------------------------------------------------------------------------------
	// 画面下部に表示する”稼働中/非稼働中の作業”を取得
	
	private ArrayList<NonActiveWorkRow> getNonActiveWorkList() {
		
		// 年月日時分秒までの日時フォーマットを準備
		//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		String pgmId = classId + ".getNonActiveWorkList";
		log.info("【INF】" + pgmId + ":処理開始!");
		
		
		try {
			// ------------------------------------------------------------------------------------------------
			// ■以下のようにleft joinで結合検索
			//
			// +------------------------+   +--------------------------------+
			// |  ①全ハウスと全作業    |   |  ②稼働中の作業                |
			// |                        |   |                                |
			// |  ３号隔離 牽引         |-->|  ３号隔離 牽引   開始日時 MAX  | ※②が存在したら”稼働中”と画面表示
			// |  ３号隔離 葉かき       |   |  ３号隔離 葉かき 開始日時 MAX  |
			// |  ３号隔離 摘花         |   |  ３号隔離 摘花   開始日時 MAX  |
			// |  ４号隔離 牽引         |   |  ４号隔離 牽引   開始日時 MAX  |
			// |  ４号隔離 葉かき       |   |  ４号隔離 葉かき 開始日時 MAX  |
			// |  ４号隔離 摘花         |   |  ４号隔離 摘花   開始日時 MAX  |
			// |  11号隔離 牽引         |   |       ...                      |
			// |  11号隔離 葉かき       |   +--------------------------------+
			// |  11号隔離 摘花         |   +--------------------------------+
			// |  ８号土耕 牽引         |   |  ③リセット済みの作業          |
			// |  ８号土耕 葉かき       |   |                                |
			// |  ８号土耕 摘花         |-->|  ３号隔離 牽引   ﾘｾｯﾄ日時 MAX  | ※②が存在しないが③が存在する場合は”
			// |       ...              |   |  ３号隔離 葉かき ﾘｾｯﾄ日時 MAX  |   リセットからの日数”を画面表示
			// |                        |   |  ３号隔離 摘花   ﾘｾｯﾄ日時 MAX  |
			// |                        |   |  ４号隔離 牽引   ﾘｾｯﾄ日時 MAX  |
			// |                        |   |  ４号隔離 葉かき ﾘｾｯﾄ日時 MAX  |
			// |                        |   |  ４号隔離 摘花   ﾘｾｯﾄ日時 MAX  |
			// |                        |   |       ...                      |
			// +------------------------+   +--------------------------------+
			//
			// ------------------------------------------------------------------------------------------------
			
			String sql = " select";
			sql  = sql + "     VM_HOUSE.HOUSEID";
			sql  = sql + "    ,VM_HOUSE.HOUSENAME";
			sql  = sql + "    ,VM_HOUSE.WORKID";
			sql  = sql + "    ,VM_HOUSE.WORKNAME";
			sql  = sql + "    ,DATE_FORMAT(VT_WORKS.STARTDATETIME,'%Y%m%d%H%i%S') STARTDATETIME_STRING";
			sql  = sql + "    ,DATE_FORMAT(VT_RESET.RESETYMDHMS,'%Y%m%d%H%i%S') RESETDATETIME_STRING";
			// 下記②「稼働中」    の作業が存在  する場合＝「稼働中」  と表示
			// 下記③「リセット済」の作業が存在  する場合＝「ｎ日経過」と表示 ※最後にリセットしてからｎ日経過してるよ～ってこと
			// 下記②、③          の両方が存在しない場合＝「－」      と表示
			sql  = sql + "    ,case";
			sql  = sql + "     when VT_WORKS.STARTDATETIME is not null then";
			sql  = sql + "         '稼働中'";
			sql  = sql + "     when VT_WORKS.STARTDATETIME is null and VT_RESET.RESETYMDHMS is not null then";
			sql  = sql + "         concat(DATEDIFF(now() , VT_RESET.RESETYMDHMS), '日経過')";
			sql  = sql + "     when VT_WORKS.STARTDATETIME is null and VT_RESET.RESETYMDHMS is null then";
			sql  = sql + "         '－'";
			sql  = sql + "     end PROGRESSDAYS";
			sql  = sql + " from";
			// ①：ハウスと作業を「全て」列挙する。ただし収穫作業は除く。
			sql  = sql + "     (";
			sql  = sql + "     select    TM_HOUSE.HOUSEID";
			sql  = sql + "              ,TM_HOUSE.HOUSENAME";
			sql  = sql + "              ,TM_WORK.WORKID";
			sql  = sql + "              ,TM_WORK.WORKNAME";
			sql  = sql + "     from      TM_HOUSE";
			sql  = sql + "     cross join";
			sql  = sql + "               (";
			sql  = sql + "               select * from TM_WORK";
			sql  = sql + "               where WORKID not in ('" + SpecialWork.SHUKAKU_SUM + "')";//収穫(合計)登録は対象にしない
			sql  = sql + "               ) TM_WORK";
			sql  = sql + "     ) VM_HOUSE";
			// ②：上記①のハウス・作業に対して、稼働中の作業(収穫作業も含むため union all)が存在するかを調べる
			sql  = sql + " left join";
			sql  = sql + "     (";
			sql  = sql + "     select    HOUSEID";
			sql  = sql + "              ,WORKID";
			sql  = sql + "              ,MAX(STARTDATETIME) STARTDATETIME";
			sql  = sql + "      from     TT_HOUSE_WORKSTATUS";
			sql  = sql + "      where    DELETEFLG = False"; //削除済みの作業状況は対象にしない
			sql  = sql + "      and      STARTEMPLOYEEID <> '" + SpecialUser.TEST_USER + "'";//テストユーザは対象にしない
			sql  = sql + "      group by HOUSEID";
			sql  = sql + "              ,WORKID";
			sql  = sql + "         union all";
			sql  = sql + "     select    HOUSEID";
			sql  = sql + "              ,WORKID";
			sql  = sql + "              ,MAX(STARTDATETIME) STARTDATETIME";
			sql  = sql + "      from     TT_HOUSE_WORKSTATUS_SHUKAKU";
			sql  = sql + "      where    DELETEFLG = False"; //削除済みの作業状況は対象にしない
			sql  = sql + "      and      STARTEMPLOYEEID <> '" + SpecialUser.TEST_USER + "'";//テストユーザは対象にしない
			sql  = sql + "      group by HOUSEID";
			sql  = sql + "              ,WORKID";
			sql  = sql + "     ) VT_WORKS";
			sql  = sql + "     on  VM_HOUSE.HOUSEID  = VT_WORKS.HOUSEID";
			sql  = sql + "     and VM_HOUSE.WORKID   = VT_WORKS.WORKID";
			// ③：上記①のハウス・作業に対して、リセット済みの作業が存在するかを調べる（リセット日時のMAXを取得）
			sql  = sql + " left join";
			sql  = sql + "     (";
			sql  = sql + "     select    HOUSEID";
			sql  = sql + "              ,WORKID";
			sql  = sql + "              ,MAX(RESETYMDHMS) RESETYMDHMS";
			sql  = sql + "      from     TT_HOUSE_WORKSTATUS_RESET";
			sql  = sql + "      where    STARTEMPLOYEEID <> '" + SpecialUser.TEST_USER + "'";//テストユーザは対象にしない
			sql  = sql + "      group by HOUSEID";
			sql  = sql + "              ,WORKID";
			sql  = sql + "     ) VT_RESET";
			sql  = sql + "     on  VM_HOUSE.HOUSEID  = VT_RESET.HOUSEID";
			sql  = sql + "     and VM_HOUSE.WORKID   = VT_RESET.WORKID";
			sql  = sql + " order by";
			sql  = sql + "     VM_HOUSE.HOUSEID";
			sql  = sql + "    ,VM_HOUSE.WORKID";
			
			
			
			
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
			
			//インナークラスをNEWするために必要なクラス
			FormKanriDispWorkStatus wkForm = new FormKanriDispWorkStatus();
			
			// 返却値
			ArrayList<NonActiveWorkRow> retList = new ArrayList<NonActiveWorkRow>();
			
			NonActiveWorkRow row = null;
			
			String wkHouseId = "";
			String startDateTimeString = "";
			String resetDateTimeString = "";
			
			for (Map<String, Object> rs: rsList) {
				
				// ハウスが変更したら一覧表の次の行を作成
				if (wkHouseId.equals(rs.get("HOUSEID").toString()) == false) {
					
					wkHouseId = rs.get("HOUSEID").toString();
					
					row = wkForm.new NonActiveWorkRow();
					// ハウスID
					row.setHouseId(rs.get("HOUSEID").toString());
					// ハウス名
					row.setHouseName(rs.get("HOUSENAME").toString());
					log.info("【DBG】" + pgmId + ":■------------------------------------------------");
					log.info("【DBG】" + pgmId + ":■ハウス=[" + row.getHouseName() + "]");
					
					retList.add(row);
				}
				
				
				NonActiveWorkCol col = wkForm.new NonActiveWorkCol();
				
				// 1000005(横ひも巻き)、1000007(その他)、1000008(空き作業１)、1000009(空き作業２)、1000010(収穫)は表示しない
				String workId = rs.get("WORKID").toString();
				
				if (("1000005").equals(workId) == true || ("1000007").equals(workId) == true || ("1000008").equals(workId) == true || ("1000009").equals(workId) == true || ("1000010").equals(workId) == true) {
					continue;
				}
				
				
				//作業ID、作業名
				col.setWorkId(rs.get("WORKID").toString());
				col.setWorkName(rs.get("WORKNAME").toString());
				
				
				// 年月日時分秒までの日時フォーマットを準備
				DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
				
				
				// 作業開始日時
				if (rs.get("STARTDATETIME_STRING") != null) {
					startDateTimeString = rs.get("STARTDATETIME_STRING").toString();
					col.setStartDateTime(LocalDateTime.parse(startDateTimeString,formatterDateTime));
				}
				// リセット日時
				if (rs.get("RESETDATETIME_STRING") != null) {
					resetDateTimeString = rs.get("RESETDATETIME_STRING").toString();
					col.setResetDateTime(LocalDateTime.parse(resetDateTimeString,formatterDateTime));
				}
				
				//リセットから経過した日数  n日経過／稼働中／－
				col.setProgressDays(rs.get("PROGRESSDAYS").toString());
				
				log.info("【DBG】" + pgmId + ":□作業名　=[" + col.getWorkName() + "]");
				log.info("【DBG】" + pgmId + ":□開始日時=[" + col.getStartDateTime() + "]");
				log.info("【DBG】" + pgmId + ":□リセット=[" + col.getWorkName() + "]");
				log.info("【DBG】" + pgmId + ":□作業状況=[" + col.getProgressDays() + "]");
				
				//行に列を追加
				row.getNonActiveWorkCol().add(col);
				
			}
			
			
			log.info("【INF】" + pgmId + ":処理終了 表示するハウスの件数(行数)=[" + Integer.toString(retList.size()) + "]件");
			return retList;
			
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了[" + e.toString() + "]");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return null;
		}
	}
	
	
	
	
	public boolean execReset(String houseId,String workId) {
		
		String pgmId = classId + ".execReset";
		log.info("【INF】" + pgmId + ":処理開始");
		
		try {
			LocalDateTime nowDateTime =  LocalDateTime.now();
			
			// ------------------------------------------------
			// 作業進捗情報をリセットテーブルに移行
			
			
			String sql = " insert into TT_HOUSE_WORKSTATUS_RESET";
			sql  = sql + " (";
			sql  = sql + "     HOUSEID";
			sql  = sql + "    ,COLNO";
			sql  = sql + "    ,WORKID";
			sql  = sql + "    ,STARTDATETIME";
			sql  = sql + "    ,STARTEMPLOYEEID";
			sql  = sql + "    ,ENDDATETIME";
			sql  = sql + "    ,ENDEMPLOYEEID";
			sql  = sql + "    ,PERCENT_START";
			sql  = sql + "    ,PERCENT";
			sql  = sql + "    ,FORCE_RESETFLG";
			sql  = sql + "    ,RESETYMDHMS";
			sql  = sql + "    ,BIKO";
			sql  = sql + "    ,SYSREGUSERID";
			sql  = sql + "    ,SYSREGPGMID";
			sql  = sql + "    ,SYSREGYMDHMS";
			sql  = sql + "    ,SYSUPDUSERID";
			sql  = sql + "    ,SYSUPDPGMID";
			sql  = sql + "    ,SYSUPDYMDHMS";
			sql  = sql + " )";
			sql  = sql + " select";
			sql  = sql + "     HOUSEID";
			sql  = sql + "    ,COLNO";
			sql  = sql + "    ,WORKID";
			sql  = sql + "    ,STARTDATETIME";
			sql  = sql + "    ,STARTEMPLOYEEID";
			sql  = sql + "    ,ENDDATETIME";
			sql  = sql + "    ,ENDEMPLOYEEID";
			sql  = sql + "    ,PERCENT_START";
			sql  = sql + "    ,PERCENT";
			sql  = sql + "    ,?";             // FORCE_RESETFLG ※強制リセットフラグ
			sql  = sql + "    ,?";             // RESETYMDHMS
			sql  = sql + "    ,BIKO";
			sql  = sql + "    ,SYSREGUSERID";
			sql  = sql + "    ,SYSREGPGMID";
			sql  = sql + "    ,SYSREGYMDHMS";
			sql  = sql + "    ,SYSUPDUSERID";
			sql  = sql + "    ,SYSUPDPGMID";
			sql  = sql + "    ,SYSUPDYMDHMS";
			sql  = sql + " from";
			sql  = sql + "     TT_HOUSE_WORKSTATUS";
			sql  = sql + " where";
			sql  = sql + "     HOUSEID   = ?";
			sql  = sql + " and WORKID    = ?";
			
			
			int ret = this.jdbcTemplate.update(sql
					,true   // 強制リセットフラグ
					,nowDateTime
					,houseId
					,workId
					);
			
			log.info("【INF】" + pgmId + ":処理終了 登録件数=[" + ret + "]件");
			
			
			// ------------------------------------------------
			// 移行した作業進捗情報を削除
			
			sql        = " delete from TT_HOUSE_WORKSTATUS";
			sql  = sql + " where";
			sql  = sql + "     HOUSEID = ?";
			sql  = sql + " and WORKID  = ?";
			
			
			ret = this.jdbcTemplate.update(sql
					,houseId
					,workId
					);
			
			// メモ：commitはjdbcTemplateが自動で行ってくれる
			
			log.info("【INF】" + pgmId + ":処理終了 削除件数=[" + ret + "]件");
			
			return true;
			
		}catch(Exception e){
			
			log.error("【ERR】" + pgmId + ":異常終了[" + e.toString() + "]");
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}
