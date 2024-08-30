package com.example.DAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.example.counst.ButtonKbn;
import com.example.form.FormDispQRInfoButton;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DaoFormDispQRInfoButton {
	
	private JdbcTemplate  jdbcTemplate;
	private String classId = "DaoFormDispQRInfoButton";
	
	// コンストラクタ
	public DaoFormDispQRInfoButton(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	
	
	// 作業IDに対する情報を取得
	public ArrayList<FormDispQRInfoButton> getDispQRInfoButtonList(String workId,int currentParcent) {
		
		String pgmId = classId + ".getDispQRInfoButtonList";
		
		try {
			log.info("【INF】" + pgmId + ":処理開始 ID=[" + workId + "]");
			
			
			// ------------------------------------------------
			// ボタン表示内容をセットしていく
			//
			// 押下可能フラグはココでは固定で（true）にしておき
			// 別処理で押下可能・負荷を切り替える
			
			
			ArrayList<FormDispQRInfoButton> buttonDispInfoList = new ArrayList<FormDispQRInfoButton>();
			
			
			// １件目は作業完了ボタンをセット
			FormDispQRInfoButton endButton = new FormDispQRInfoButton();
			endButton.setButtonEnabledFlg(true);
			endButton.setButtonKbn(ButtonKbn.END);
			endButton.setPercent(100);
			//endButton.setButtonString("作業完了 100%"); //【メモ】ボタン表示文字列の編集はFormDispQRInfoButtonクラスのgetterに記載
			//リストに追加
			buttonDispInfoList.add(endButton);
			
			
			//------------------------------------------------
			// 処理中断 n%のボタンをセット
			
			
			String sql = " select ";
			sql  = sql + "     PERCENT";
			sql  = sql + " from";
			sql  = sql + "     TM_HOUSE_WORKSTATUS_SEP";
			sql  = sql + " where";
			sql  = sql + "     WORKID  = ?";
			sql  = sql + " and PERCENT > ?";   //※現在の新緑率より大きいパーセントのみ画面表示する
			sql  = sql + " order by";
			sql  = sql + "     PERCENT";
			
			// queryForListメソッドでSQLを実行し、結果MapのListで受け取る。
			List<Map<String, Object>> rsList = this.jdbcTemplate.queryForList(sql,workId,currentParcent);
			
			
			for (Map<String, Object> rs: rsList) {
				
				FormDispQRInfoButton wkrs = new FormDispQRInfoButton();
				wkrs.setButtonEnabledFlg(true);
				wkrs.setButtonKbn(ButtonKbn.HALFWAY);
				wkrs.setPercent(Integer.parseInt(rs.get("PERCENT").toString()));
				//wkrs.setButtonString("作業中断 " + wkrs.getPercent() + "%"); //【メモ】ボタン表示文字列の編集はFormDispQRInfoButtonクラスのgetterに記載
				//リストに追加
				buttonDispInfoList.add(wkrs);
			}
			
			// 作業開始ボタンをセット
			FormDispQRInfoButton startButton = new FormDispQRInfoButton();
			startButton.setButtonEnabledFlg(true);
			startButton.setButtonKbn(ButtonKbn.START);
			startButton.setPercent(0);
			//startButton.setButtonString("作業開始"); //【メモ】ボタン表示文字列の編集はFormDispQRInfoButtonクラスのgetterに記載
			//リストに追加
			buttonDispInfoList.add(startButton);
			
			// 作業再開ボタンをセット
			FormDispQRInfoButton restartButton = new FormDispQRInfoButton();
			restartButton.setButtonEnabledFlg(true);
			restartButton.setButtonKbn(ButtonKbn.RESTART);
			restartButton.setPercent(0);
			//restartButton.setButtonString("作業開始"); //【メモ】ボタン表示文字列の編集はFormDispQRInfoButtonクラスのgetterに記載
			//リストに追加
			buttonDispInfoList.add(restartButton);
			
			
			//------------------------------------------------
			// 最後は取消ボタンをセット
			FormDispQRInfoButton cancelButton = new FormDispQRInfoButton();
			cancelButton.setButtonEnabledFlg(true);
			cancelButton.setButtonKbn(ButtonKbn.CANCEL);
			cancelButton.setPercent(0);
			//cancelButton.setButtonString("取消してもう一度"); //【メモ】ボタン表示文字列の編集はFormDispQRInfoButtonクラスのgetterに記載
			//リストに追加
			buttonDispInfoList.add(cancelButton);
			
			
			
			log.info("【INF】" + pgmId + ":処理終了");
			return buttonDispInfoList;
			
			
		}catch(Exception e){

			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return null;
		}
	}
	
	
	
	
	
	
	// 収穫ケース数合計機能用のボタンをセット
	public ArrayList<FormDispQRInfoButton> getShukakuBoxSumButtonList() {
		
		String pgmId = classId + ".getShukakuBoxSumButtonList";
		
		try {
			log.info("【INF】" + pgmId + ":処理開始");
			
			
			// ------------------------------------------------
			// ボタン表示内容をセットしていく
			//
			// 押下可能フラグはココでは固定で（true）にセット
			
			
			ArrayList<FormDispQRInfoButton> buttonDispInfoList = new ArrayList<FormDispQRInfoButton>();
			
			
			//------------------------------------------------
			// １件目は登録ボタンをセット
			FormDispQRInfoButton endButton = new FormDispQRInfoButton();
			endButton.setButtonEnabledFlg(true);
			endButton.setButtonKbn(ButtonKbn.REGIST);
			endButton.setPercent(0);//未使用項目
			//endButton.setButtonString("登録"); //【メモ】ボタン表示文字列の編集はFormDispQRInfoButtonクラスのgetterに記載
			//リストに追加
			buttonDispInfoList.add(endButton);
			
			
			//------------------------------------------------
			// 最後は取消ボタンをセット
			FormDispQRInfoButton cancelButton = new FormDispQRInfoButton();
			cancelButton.setButtonEnabledFlg(true);
			cancelButton.setButtonKbn(ButtonKbn.CANCEL);
			cancelButton.setPercent(0);
			//cancelButton.setButtonString("取消してもう一度"); //【メモ】ボタン表示文字列の編集はFormDispQRInfoButtonクラスのgetterに記載
			//リストに追加
			buttonDispInfoList.add(cancelButton);
			
			
			
			log.info("【INF】" + pgmId + ":処理終了");
			return buttonDispInfoList;
			
			
		}catch(Exception e){

			log.error("【ERR】" + pgmId + ":異常終了");
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return null;
		}
	}

}
