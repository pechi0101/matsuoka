package com.example.Controller;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.DAO.DaoDropDownList;
import com.example.DAO.DaoFormDispQRInfoButton;
import com.example.DAO.DaoFormIndexQR;
import com.example.DAO.DaoFormKanriDispWorkStatus;
import com.example.DAO.DaoFormKanriMainteEmployee;
import com.example.DAO.DaoFormKanriMainteHouse;
import com.example.DAO.DaoFormKanriMainteWork;
import com.example.DAO.DaoFormKanriMainteWorkStatus;
import com.example.DAO.DaoFormKanriShukakuSum;
import com.example.DAO.DaoHouse;
import com.example.DAO.DaoHouseWorkStatus;
import com.example.DAO.DaoHouseWorkStatusShukaku;
import com.example.DAO.DaoShukakuBoxSum;
import com.example.DAO.DaoWork;
import com.example.counst.ButtonKbn;
import com.example.counst.SpecialUser;
import com.example.counst.SpecialWork;
import com.example.entity.HouseWorkStatus;
import com.example.entity.HouseWorkStatusShukaku;
import com.example.entity.ShukakuBoxSum;
import com.example.form.FormDispQRInfo;
import com.example.form.FormDispQRInfoButton;
import com.example.form.FormDispQRInfoShukaku;
import com.example.form.FormDispQRInfoShukakuSum;
import com.example.form.FormIndexKanri;
import com.example.form.FormIndexQR;
import com.example.form.FormKanriDispWorkStatus;
import com.example.form.FormKanriMainteEmployeeDetail;
import com.example.form.FormKanriMainteEmployeeList;
import com.example.form.FormKanriMainteHouseDetail;
import com.example.form.FormKanriMainteHouseList;
import com.example.form.FormKanriMainteShukakuSumDetail;
import com.example.form.FormKanriMainteShukakuSumList;
import com.example.form.FormKanriMainteWorkDetail;
import com.example.form.FormKanriMainteWorkList;
import com.example.form.FormKanriMainteWorkStatusDetail;
import com.example.form.FormKanriMainteWorkStatusList;
import com.example.form.FormReadQRCode;
import com.example.form.FormReadQRStart;
import com.example.form.FormReadQRStartShukaku;
import com.example.form.FormReadQRStartShukakuSum;
import com.example.form.FormSelectQRReadDevice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MatsuokaWebController {

	private String classId = "MatsuokaWebController";
	
	public MatsuokaWebController() {
		System.out.println("★MatsuokaWebController★");
	}
	
	@Autowired
	ResourceLoader resourceLoader;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	
	//システムの初期処理→作業者一覧画面の表示
	@RequestMapping(value ="/matsuoka/indexQR",method = RequestMethod.GET)
	public ModelAndView indexQR(ModelAndView mav) {
		
		String pgmId = classId + ".indexQR";
		
		log.info("【INF】" + pgmId + ":処理開始");
		
		DaoFormIndexQR dao = new DaoFormIndexQR(jdbcTemplate);
		FormIndexQR formIndexQR = dao.getAllValidEmployee();
		
		mav.addObject("formIndexQR",formIndexQR);
		
		log.info("【INF】" + pgmId + ":処理終了");
		

		mav.setViewName("scrIndexQR.html");
		return mav;
	}
	

	
	// ＱＲコード読取カメラデバイス選択画面への遷移
	@RequestMapping(value ="/matsuoka/scrSelectQRReadDevice",method = RequestMethod.POST)
	public ModelAndView trunsition_SelectQRReadDevice(@ModelAttribute FormIndexQR formIndexQR, ModelAndView mav) {

		String pgmId = classId + ".trunsition_SelectQRReadDevice";
		
		log.info("【INF】" + pgmId + ":処理開始 社員ID=[" + formIndexQR.getLoginEmployeeId() + "]、社員名=[" + formIndexQR.getLoginEmployeeName() + "]");
		
		
		FormSelectQRReadDevice formSelectQRReadDevice = new FormSelectQRReadDevice();
		
		formSelectQRReadDevice.setLoginEmployeeId(formIndexQR.getLoginEmployeeId());
		formSelectQRReadDevice.setLoginEmployeeName(formIndexQR.getLoginEmployeeName());
		
		// 選択デバイスの初期化
		formSelectQRReadDevice.setSelectedDeviceLabel("");
		
		
		mav.addObject("formSelectQRReadDevice", formSelectQRReadDevice);
		
		log.info("【INF】" + pgmId + ":処理終了");
		mav.setViewName("scrSelectQRReadDevice.html");
		return mav;
	}
	
	
	// ＱＲコード読取開始画面への遷移
	@RequestMapping(value ="/matsuoka/TransitionReadQRStart",method = RequestMethod.POST)
	public ModelAndView trunsition_ReadQRStart(@ModelAttribute FormSelectQRReadDevice formSelectQRReadDevice, ModelAndView mav) {

		String pgmId = classId + ".trunsition_ReadQRStart";
		
		log.info("【INF】" + pgmId + ":処理開始 社員ID=[" + formSelectQRReadDevice.getLoginEmployeeId() + "]、社員名=[" + formSelectQRReadDevice.getLoginEmployeeName() + "]");
		log.info("【INF】" + pgmId + ":選択デバイス名=[" + formSelectQRReadDevice.getSelectedDeviceLabel() + "]");
		
		
		FormReadQRStart formReadQRStart = new FormReadQRStart();
		
		formReadQRStart.setLoginEmployeeId(formSelectQRReadDevice.getLoginEmployeeId());
		formReadQRStart.setLoginEmployeeName(formSelectQRReadDevice.getLoginEmployeeName());
		formReadQRStart.setSelectedDeviceLabel(formSelectQRReadDevice.getSelectedDeviceLabel());
		
		
		// 初期メッセージ
		formReadQRStart.setMessage("ＱＲコードの読み取り準備完了");
		
		
		mav.addObject("formReadQRStart", formReadQRStart);
		
		log.info("【INF】" + pgmId + ":処理終了");
		mav.setViewName("scrReadQRStart.html");
		return mav;
	}
	
	
	
	// ＱＲコード読取画面への遷移
	@RequestMapping(value ="/matsuoka/TransitionReadQR",method = RequestMethod.POST)
	public ModelAndView trunsition_ReadQR(@ModelAttribute FormReadQRStart formReadQRStart, ModelAndView mav) {
		
		String pgmId = classId + ".trunsition_ReadQR";
		
		log.info("【INF】" + pgmId + ":処理開始 社員ID=[" + formReadQRStart.getLoginEmployeeId() + "]、社員名=[" + formReadQRStart.getLoginEmployeeName() + "]");
		log.info("【INF】" + pgmId + ":選択デバイス名=[" + formReadQRStart.getSelectedDeviceLabel() + "]");
		
		
		FormReadQRCode formReadQRCode = new FormReadQRCode();
		
		formReadQRCode.setLoginEmployeeId(formReadQRStart.getLoginEmployeeId());
		formReadQRCode.setLoginEmployeeName(formReadQRStart.getLoginEmployeeName());
		formReadQRCode.setSelectedDeviceLabel(formReadQRStart.getSelectedDeviceLabel());
		
		mav.addObject("formReadQRCode", formReadQRCode);
		
		log.info("【INF】" + pgmId + ":処理終了");
		mav.setViewName("scrReadQRCode.html");
		return mav;
	}
	
	
	
	@RequestMapping(value ="/matsuoka/TransitionDispQRInfo",method = RequestMethod.POST)
	public ModelAndView trunsition_DispQRInfo(@ModelAttribute FormReadQRCode formReadQRCode, ModelAndView mav) {
		
		String pgmId = classId + ".trunsition_DispQRInfo";
		
		log.info("【INF】" + pgmId + " :処理開始 社員ID=[" + formReadQRCode.getLoginEmployeeId() + "]、社員名=[" + formReadQRCode.getLoginEmployeeName() + "]");
		log.info("【INF】" + pgmId + " :選択デバイス名=[" + formReadQRCode.getSelectedDeviceLabel() + "]、読取QRコード=[" + formReadQRCode.getQrcode() + "]");
		
		
		// ------------------------------------------------
		// ＱＲコードの情報をカンマで分解する
		String[] qrDataList = formReadQRCode.getQrcode().split(",");
		
		log.info("【DBG】" + pgmId + " :QRコードの要素数=[" + Integer.toString(qrDataList.length) + "]件");
		
		
		
		// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
		// 
		// ＱＲコードの入力チェック
		// 
		// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
		
		
		
		// ------------------------------------------------
		// 誤ったＱＲコードやバーコードを読み取った場合(※)ＮＧ
		// 
		// ※エラーである場合
		// ①ＱＲコードで読み取った情報内にカンマが存在しない
		// ②ＱＲコードをカンマで分解し、分解した要素が５以外
		// ③分解したＱＲコードの最初の情報が適切でない：要素１つ目(接頭文字列)が文字列"MatsuokaQRData"であること
		// ④分解したＱＲコードの最初の情報が適切でない：要素２つ目(ハウスID  )が５ケタであること
		// ④分解したＱＲコードの最初の情報が適切でない：要素３つ目(列№      )が２ケタであること
		// ④分解したＱＲコードの最初の情報が適切でない：要素４つ目(作業区分  )が１ケタであること
		// ④分解したＱＲコードの最初の情報が適切でない：要素５つ目(作業ID    )が７ケタであること
		// ------------------------------------------------
		
		FormReadQRStart formReadQRStart = new FormReadQRStart();
		
		formReadQRStart.setLoginEmployeeId(formReadQRCode.getLoginEmployeeId());
		formReadQRStart.setLoginEmployeeName(formReadQRCode.getLoginEmployeeName());
		
		if (qrDataList.length < 1) {
			log.error("【ERR】" + pgmId + " :誤ったＱＲコードが読込まれました。QR=[" + formReadQRCode.getQrcode() + "]");
			formReadQRStart.setMessage("【エラー】誤ったＱＲコードが読込まれました。再度ＱＲコードの読み取りをお願いいたします。");
			
			mav.addObject("formReadQRStart", formReadQRStart);
			log.info("【INF】" + pgmId + ":処理終了");
			mav.setViewName("scrReadQRStart.html");
			return mav;
		}
		if (qrDataList.length != 5) {
			log.error("【ERR】" + pgmId + " :誤ったＱＲコードが読込まれました。QR=[" + formReadQRCode.getQrcode() + "]");
			formReadQRStart.setMessage("【エラー】誤ったＱＲコードが読込まれました。再度ＱＲコードの読み取りをお願いいたします。");
			
			mav.addObject("formReadQRStart", formReadQRStart);
			log.info("【INF】" + pgmId + ":処理終了");
			mav.setViewName("scrReadQRStart.html");
			return mav;
		}
		if (
			qrDataList[0].equals("MatsuokaQRData") == false
		||  qrDataList[1].length() != 5
		||  qrDataList[2].length() != 2
		||  qrDataList[3].length() != 1
		||  qrDataList[4].length() != 7
		) {
			log.error("【ERR】" + pgmId + " :誤ったＱＲコードが読込まれました。QR=[" + formReadQRCode.getQrcode() + "]");
			formReadQRStart.setMessage("【エラー】誤ったＱＲコードが読込まれました。再度ＱＲコードの読み取りをお願いいたします。");
			
			mav.addObject("formReadQRStart", formReadQRStart);
			log.info("【INF】" + pgmId + ":処理終了");
			mav.setViewName("scrReadQRStart.html");
			return mav;
		}
		
		
		// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
		// 
		// 作業が「収穫」作業である場合
		// 
		// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
		
		
		if (qrDataList[4].equals(SpecialWork.SHUKAKU) == true) {
			
			
			// ------------------------------------------------
			// ＱＲコードにセットされている情報をセット
			FormDispQRInfoShukaku formDispQRInfoShukaku = new FormDispQRInfoShukaku();

			formDispQRInfoShukaku.setLoginEmployeeId(formReadQRCode.getLoginEmployeeId());
			formDispQRInfoShukaku.setLoginEmployeeName(formReadQRCode.getLoginEmployeeName());
			formDispQRInfoShukaku.setSelectedDeviceLabel(formReadQRCode.getSelectedDeviceLabel());
			
			formDispQRInfoShukaku.setWorkId(qrDataList[4]);
			formDispQRInfoShukaku.setHouseId(qrDataList[1]);
			formDispQRInfoShukaku.setColNo("XX"); //収穫である場合、列は無関係であるためXXとしておく
			formDispQRInfoShukaku.setBiko("");
			formDispQRInfoShukaku.setBoxCount(1);
			
			
			
			// ------------------------------------------------
			// 作業IDに対する作業名を取得
			
			DaoWork daoWork = new DaoWork(jdbcTemplate);
			String workName = daoWork.getNameFromId(formDispQRInfoShukaku.getWorkId());
			
			
			
			// 名称取得エラー
			if (workName.trim().equals("") == true) {
				log.error("【ERR】" + pgmId + " :未登録な作業のＱＲコードが読込まれました。QR=[" + formReadQRCode.getQrcode() + "]");
				formReadQRStart.setMessage("【エラー】未登録な作業のＱＲコードが読込まれました。別のＱＲコードの読み取りをお願いいたします。 ");
				
				mav.addObject("formReadQRStart", formReadQRStart);
				log.info("【INF】" + pgmId + ":処理終了");
				mav.setViewName("scrReadQRStart.html");
				return mav;
			}
			
			formDispQRInfoShukaku.setWorkName(workName);
			
			
			
			// ------------------------------------------------
			// ハウスIDに対するハウス名を取得
			
			DaoHouse daoHouse = new DaoHouse(jdbcTemplate);
			String houseName = daoHouse.getNameFromId(formDispQRInfoShukaku.getHouseId());
			
			
			if (houseName.trim().equals("") == true) {
				log.error("【ERR】" + pgmId + " :未登録なハウスのＱＲコードが読込まれました。QR=[" + formReadQRCode.getQrcode() + "]");
				formReadQRStart.setMessage("【エラー】未登録なハウスのＱＲコードが読込まれました。別のＱＲコードの読み取りをお願いいたします。 ");
				
				mav.addObject("formReadQRStart", formReadQRStart);
				log.info("【INF】" + pgmId + ":処理終了");
				mav.setViewName("scrReadQRStart.html");
				return mav;
			}
			
			formDispQRInfoShukaku.setHouseName(houseName);
			
			
			
			
			// ------------------------------------------------
			// 作業状況マスタを検索し、今のＱＲコード読取りが「作業開始」なのか
			// それとも「作業終了(または中断)」であるのかを判定する
			
			
			// ＱＲコードから取得した「作業ID、ハウスID、列№」でハウス作業進捗テーブルを検索。
			DaoHouseWorkStatusShukaku daoHouseWorkStatusShukaku = new DaoHouseWorkStatusShukaku(jdbcTemplate);
			HouseWorkStatusShukaku houseWorkStatusShukaku =  daoHouseWorkStatusShukaku.getLatestWorkStatus(formDispQRInfoShukaku.getWorkId(), formDispQRInfoShukaku.getHouseId(), formDispQRInfoShukaku.getColNo());

			log.info("【DBG】" + pgmId + ":最新の作業状況=[" + houseWorkStatusShukaku.getWorkStatus() + "]");
			
			// 取得・判定した作業状況が「エラー」である場合は異常終了
			if (houseWorkStatusShukaku.getWorkStatus() == DaoHouseWorkStatusShukaku.STATUS_ERROR) {
				log.error("【ERR】" + pgmId + " :作業状況の取得処理で異常が発生しました。作業ID=[" + formDispQRInfoShukaku.getWorkId() + "]");
				formReadQRStart.setMessage("【エラー】ＱＲコード読込処理で異常が発生しました（作業状況取得エラー）。もう一度ＱＲコードの読み取り行うかシステム担当者にご連絡ください。");
				
				mav.addObject("formReadQRStart", formReadQRStart);
				log.info("【INF】" + pgmId + ":処理終了");
				mav.setViewName("scrReadQRStart.html");
				return mav;
			}
			
			// 作業状況と進捗率（％）と開始日時、備考、収穫箱数を画面表示用にセット
			formDispQRInfoShukaku.setWorkStatus(houseWorkStatusShukaku.getWorkStatus());
			formDispQRInfoShukaku.setPercent(houseWorkStatusShukaku.getPercent());
			formDispQRInfoShukaku.setStartDatetime(houseWorkStatusShukaku.getStartDateTime());
			formDispQRInfoShukaku.setBiko(houseWorkStatusShukaku.getBiko());
			formDispQRInfoShukaku.setBoxCount(houseWorkStatusShukaku.getBoxCount());
			
			
			
			
			// ------------------------------------------------
			// 作業進捗区切マスタを検索しこの作業がどの％で区切られてるかを検証し
			// 次画面に表示する「作業開始」「作業中断」などのボタンの情報を取得
			
			DaoFormDispQRInfoButton daoButton = new DaoFormDispQRInfoButton(jdbcTemplate);
			ArrayList<FormDispQRInfoButton> buttonDispInfoList = daoButton.getDispQRInfoButtonList(formDispQRInfoShukaku.getWorkId(),houseWorkStatusShukaku.getPercent());
			
			if (buttonDispInfoList == null) {
				log.error("【ERR】" + pgmId + " :未登録なハウス作業のＱＲコード読込まれました。作業ID=[" + formDispQRInfoShukaku.getWorkId() + "]");
				formReadQRStart.setMessage("【エラー】未登録なハウス作業のＱＲコード読込まれました。別のＱＲコードの読み取りをお願いいたします。 ");
				
				mav.addObject("formReadQRStart", formReadQRStart);
				log.info("【INF】" + pgmId + ":処理終了");
				mav.setViewName("scrReadQRStart.html");
				return mav;
			}
			
			formDispQRInfoShukaku.setButtonDispInfoList(buttonDispInfoList);
			
			
			
			// ------------------------------------------------
			// 最新のデータが「作業未実施状態」か「作業完了状態」である場合、画面に表示する作業開始日時、進捗（％）、備考、収穫箱数を初期化

			if (formDispQRInfoShukaku.getWorkStatus() == DaoHouseWorkStatusShukaku.STATUS_NOT
			||  formDispQRInfoShukaku.getWorkStatus() == DaoHouseWorkStatusShukaku.STATUS_DONE) {
				
				formDispQRInfoShukaku.setStartDatetime(null);
				formDispQRInfoShukaku.setStartEmployeeid("");
				formDispQRInfoShukaku.setPercent(0);
				formDispQRInfoShukaku.setBiko("");
				formDispQRInfoShukaku.setBoxCount(0);
			}
			
			
			
			
			// ------------------------------------------------
			// 最新のデータが「作業未実施状態」か「作業完了状態」である場合は「作業開始」「取消」ボタンのみ有効にする
			
			if (formDispQRInfoShukaku.getWorkStatus() == DaoHouseWorkStatusShukaku.STATUS_NOT
			||  formDispQRInfoShukaku.getWorkStatus() == DaoHouseWorkStatusShukaku.STATUS_DONE) {
				
				for (int index = 0 ; index < formDispQRInfoShukaku.getButtonDispInfoList().size(); index ++) {
					
					log.info("【DBG】" + pgmId + ":□index=[" + index + "]ボタン区分=[" + formDispQRInfoShukaku.getButtonDispInfoList().get(index).getButtonKbn() + "]");
					if (
					   formDispQRInfoShukaku.getButtonDispInfoList().get(index).getButtonKbn().equals(ButtonKbn.START) == true   //作業開始ボタン
					|| formDispQRInfoShukaku.getButtonDispInfoList().get(index).getButtonKbn().equals(ButtonKbn.CANCEL) == true  //取消ボタン
					) {
						
						formDispQRInfoShukaku.getButtonDispInfoList().get(index).setButtonEnabledFlg(true);
						
					} else {
						formDispQRInfoShukaku.getButtonDispInfoList().get(index).setButtonEnabledFlg(false);
						
					}
					
				}
				
			}
			
			
			
			// ------------------------------------------------
			// 最新のデータが「作業中状態」である場合は「作業完了」「取消」ボタンのみ有効にする
			//
			
			if (formDispQRInfoShukaku.getWorkStatus() == DaoHouseWorkStatusShukaku.STATUS_WORKING) {
				
				for (int index = 0 ; index < formDispQRInfoShukaku.getButtonDispInfoList().size(); index ++) {
					
					log.info("【DBG】" + pgmId + ":■index=[" + index + "]ボタン区分=[" + formDispQRInfoShukaku.getButtonDispInfoList().get(index).getButtonKbn() + "]");
					if (
					   formDispQRInfoShukaku.getButtonDispInfoList().get(index).getButtonKbn().equals(ButtonKbn.END) == true     //作業完了ボタン
					|| formDispQRInfoShukaku.getButtonDispInfoList().get(index).getButtonKbn().equals(ButtonKbn.CANCEL) == true //取消ボタン
					) {
						
						formDispQRInfoShukaku.getButtonDispInfoList().get(index).setButtonEnabledFlg(true);
						
					} else {
						formDispQRInfoShukaku.getButtonDispInfoList().get(index).setButtonEnabledFlg(false);
						
					}
				}
			}
			
			
			
			// ------------------------------------------------
			
			mav.addObject("formDispQRInfoShukaku", formDispQRInfoShukaku);
			
			
			log.info("【INF】" + pgmId + ":処理終了!!!");
			mav.setViewName("scrDispQRInfoShukaku.html");
			// ★★★★★★★★★★★★★★★★★★★★★★★★★
			//
			//          収穫である場合はココで処理終了
			//
			// ★★★★★★★★★★★★★★★★★★★★★★★★★
			return mav;
			
		}
		
		
		
		
		
		
		// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
		// 
		// 作業が「収穫(合計入力)」作業である場合
		// 
		// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
		
		
		if (qrDataList[4].equals(SpecialWork.SHUKAKU_SUM) == true) {
			
			
			// ------------------------------------------------
			// ＱＲコードにセットされている情報をセット
			FormDispQRInfoShukakuSum formDispQRInfoShukakuSum = new FormDispQRInfoShukakuSum();
			
			formDispQRInfoShukakuSum.setLoginEmployeeId(formReadQRCode.getLoginEmployeeId());
			formDispQRInfoShukakuSum.setLoginEmployeeName(formReadQRCode.getLoginEmployeeName());
			formDispQRInfoShukakuSum.setSelectedDeviceLabel(formReadQRCode.getSelectedDeviceLabel());
			
			formDispQRInfoShukakuSum.setHouseId(qrDataList[1]);
			formDispQRInfoShukakuSum.setShukakuDate(LocalDateTime.now());
			formDispQRInfoShukakuSum.setRegistEmployeeid(formReadQRCode.getLoginEmployeeId());
			formDispQRInfoShukakuSum.setRegistDatetime(LocalDateTime.now());
			formDispQRInfoShukakuSum.setBiko("");
			formDispQRInfoShukakuSum.setBoxSum(0);
			
			
			// ------------------------------------------------
			// ハウスIDに対するハウス名を取得
			
			DaoHouse daoHouse = new DaoHouse(jdbcTemplate);
			String houseName = daoHouse.getNameFromId(formDispQRInfoShukakuSum.getHouseId());
			
			
			if (houseName.trim().equals("") == true) {
				log.error("【ERR】" + pgmId + " :未登録なハウスのＱＲコードが読込まれました。QR=[" + formReadQRCode.getQrcode() + "]");
				formReadQRStart.setMessage("【エラー】未登録なハウスのＱＲコードが読込まれました。別のＱＲコードの読み取りをお願いいたします。 ");
				
				mav.addObject("formReadQRStart", formReadQRStart);
				log.info("【INF】" + pgmId + ":処理終了");
				mav.setViewName("scrReadQRStart.html");
				return mav;
			}
			
			formDispQRInfoShukakuSum.setHouseName(houseName);
			
			
			
			
			// ------------------------------------------------
			// 収穫ケース数合計マスタを検索し、現在登録されているケース数合計を取得
			
			
			// ＱＲコードから取得した「作業ID、ハウスID、列№」でハウス作業進捗テーブルを検索。
			DaoShukakuBoxSum daoShukakuBoxSum = new DaoShukakuBoxSum(jdbcTemplate);
			ShukakuBoxSum ShukakuBoxSum =  daoShukakuBoxSum.getData(formDispQRInfoShukakuSum.getHouseId(),formDispQRInfoShukakuSum.getShukakuDate());
			
			
			// 備考と収穫ケース数を画面表示用にセット
			formDispQRInfoShukakuSum.setRegistEmployeeid(ShukakuBoxSum.getRegistEmployeeid()); // ココが空白の場合は登録データなしと判断
			formDispQRInfoShukakuSum.setBiko(ShukakuBoxSum.getBiko());
			formDispQRInfoShukakuSum.setBoxSum(ShukakuBoxSum.getBoxSum());
			
			
			
			
			// ------------------------------------------------
			// 次画面に表示する「登録」「取消してもう一度」のボタンの情報を取得
			
			DaoFormDispQRInfoButton daoButton = new DaoFormDispQRInfoButton(jdbcTemplate);
			ArrayList<FormDispQRInfoButton> buttonDispInfoList = daoButton.getShukakuBoxSumButtonList();
			
			if (buttonDispInfoList == null) {
				log.error("【ERR】" + pgmId + " :ボタン情報のセットでエラーが発生しました。");
				formReadQRStart.setMessage("【エラー】ボタン情報のセットでエラーが発生しました。システム管理者にご連絡ください。");
				
				mav.addObject("formReadQRStart", formReadQRStart);
				log.info("【INF】" + pgmId + ":処理終了");
				mav.setViewName("scrReadQRStart.html");
				return mav;
			}
			
			formDispQRInfoShukakuSum.setButtonDispInfoList(buttonDispInfoList);
			
			// ------------------------------------------------
			
			mav.addObject("formDispQRInfoShukakuSum", formDispQRInfoShukakuSum);
			
			
			log.info("【INF】" + pgmId + ":処理終了!!!");
			mav.setViewName("scrDispQRInfoShukakuSum.html");
			// ★★★★★★★★★★★★★★★★★★★★★★★★★
			//
			//          収穫(合計入力)である場合はココで処理終了
			//
			// ★★★★★★★★★★★★★★★★★★★★★★★★★
			return mav;
			
		}
		
		
		
		
		
		
		// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
		// 
		// 作業が一般作業である場合
		// 
		// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
		
		
		
		// ------------------------------------------------
		// ＱＲコードにセットされている情報をセット
		
		FormDispQRInfo formDispQRInfo = new FormDispQRInfo();
		
		formDispQRInfo.setLoginEmployeeId(formReadQRCode.getLoginEmployeeId());
		formDispQRInfo.setLoginEmployeeName(formReadQRCode.getLoginEmployeeName());
		formDispQRInfo.setSelectedDeviceLabel(formReadQRCode.getSelectedDeviceLabel());
		
		formDispQRInfo.setWorkId(qrDataList[4]);
		formDispQRInfo.setHouseId(qrDataList[1]);
		formDispQRInfo.setColNo(qrDataList[2]);
		formDispQRInfo.setBiko("");
		
		
		
		// ------------------------------------------------
		// 作業IDに対する作業名を取得
		
		DaoWork daoWork = new DaoWork(jdbcTemplate);
		String workName = daoWork.getNameFromId(formDispQRInfo.getWorkId());
		
		
		
		// 名称取得エラー
		if (workName.trim().equals("") == true) {
			log.error("【ERR】" + pgmId + " :未登録な作業のＱＲコードが読込まれました。QR=[" + formReadQRCode.getQrcode() + "]");
			formReadQRStart.setMessage("【エラー】未登録な作業のＱＲコードが読込まれました。別のＱＲコードの読み取りをお願いいたします。 ");
			
			mav.addObject("formReadQRStart", formReadQRStart);
			log.info("【INF】" + pgmId + ":処理終了");
			mav.setViewName("scrReadQRStart.html");
			return mav;
		}
		
		formDispQRInfo.setWorkName(workName);
		
		
		
		// ------------------------------------------------
		// ハウスIDに対するハウス名を取得
		
		DaoHouse daoHouse = new DaoHouse(jdbcTemplate);
		String houseName = daoHouse.getNameFromId(formDispQRInfo.getHouseId());
		
		
		if (houseName.trim().equals("") == true) {
			log.error("【ERR】" + pgmId + " :未登録なハウスのＱＲコードが読込まれました。QR=[" + formReadQRCode.getQrcode() + "]");
			formReadQRStart.setMessage("【エラー】未登録なハウスのＱＲコードが読込まれました。別のＱＲコードの読み取りをお願いいたします。 ");
			
			mav.addObject("formReadQRStart", formReadQRStart);
			log.info("【INF】" + pgmId + ":処理終了");
			mav.setViewName("scrReadQRStart.html");
			return mav;
		}
		
		formDispQRInfo.setHouseName(houseName);
		
		
		
		
		// ------------------------------------------------
		// 作業状況マスタを検索し、今のＱＲコード読取りが「作業開始」なのか
		// それとも「作業終了(または中断)」であるのかを判定する
		
		
		// ＱＲコードから取得した「作業ID、ハウスID、列№」でハウス作業進捗テーブルを検索。
		DaoHouseWorkStatus daoHouseWorkStatus = new DaoHouseWorkStatus(jdbcTemplate);
		HouseWorkStatus houseWorkStatus =  daoHouseWorkStatus.getLatestWorkStatus(formDispQRInfo.getWorkId(), formDispQRInfo.getHouseId(), formDispQRInfo.getColNo());

		log.info("【DBG】" + pgmId + ":最新の作業状況=[" + houseWorkStatus.getWorkStatus() + "]");
		
		// 取得・判定した作業状況が「エラー」である場合は異常終了
		if (houseWorkStatus.getWorkStatus() == DaoHouseWorkStatus.STATUS_ERROR) {
			log.error("【ERR】" + pgmId + " :作業状況の取得処理で異常が発生しました。作業ID=[" + formDispQRInfo.getWorkId() + "]");
			formReadQRStart.setMessage("【エラー】ＱＲコード読込処理で異常が発生しました（作業状況取得エラー）。もう一度ＱＲコードの読み取り行うかシステム担当者にご連絡ください。");
			
			mav.addObject("formReadQRStart", formReadQRStart);
			log.info("【INF】" + pgmId + ":処理終了");
			mav.setViewName("scrReadQRStart.html");
			return mav;
		}
		
		// 作業状況と進捗率（％）と開始日時、備考を画面表示用にセット
		formDispQRInfo.setWorkStatus(houseWorkStatus.getWorkStatus());
		formDispQRInfo.setPercent(houseWorkStatus.getPercent());
		formDispQRInfo.setStartDatetime(houseWorkStatus.getStartDateTime());
		formDispQRInfo.setBiko(houseWorkStatus.getBiko());
		
		
		
		
		// ------------------------------------------------
		// 作業進捗区切マスタを検索しこの作業がどの％で区切られてるかを検証し
		// 次画面に表示する「作業開始」「作業中断」などのボタンの情報を取得
		
		DaoFormDispQRInfoButton daoButton = new DaoFormDispQRInfoButton(jdbcTemplate);
		ArrayList<FormDispQRInfoButton> buttonDispInfoList = daoButton.getDispQRInfoButtonList(formDispQRInfo.getWorkId(),houseWorkStatus.getPercent());
		
		if (buttonDispInfoList == null) {
			log.error("【ERR】" + pgmId + " :未登録なハウス作業のＱＲコード読込まれました。作業ID=[" + formDispQRInfo.getWorkId() + "]");
			formReadQRStart.setMessage("【エラー】未登録なハウス作業のＱＲコード読込まれました。別のＱＲコードの読み取りをお願いいたします。 ");
			
			mav.addObject("formReadQRStart", formReadQRStart);
			log.info("【INF】" + pgmId + ":処理終了");
			mav.setViewName("scrReadQRStart.html");
			return mav;
		}
		
		formDispQRInfo.setButtonDispInfoList(buttonDispInfoList);
		
		
		
		// ------------------------------------------------
		// 最新のデータが「作業未実施状態」か「作業完了状態」である場合、画面に表示する作業開始日時、進捗（％）、備考を初期化

		if (formDispQRInfo.getWorkStatus() == DaoHouseWorkStatus.STATUS_NOT
		||  formDispQRInfo.getWorkStatus() == DaoHouseWorkStatus.STATUS_DONE) {
			
			formDispQRInfo.setStartDatetime(null);
			formDispQRInfo.setStartEmployeeid("");
			formDispQRInfo.setPercent(0);
			formDispQRInfo.setBiko("");
		}
		
		
		
		
		// ------------------------------------------------
		// 最新のデータが「作業未実施状態」か「作業完了状態」である場合は「作業開始」「取消」ボタンのみ有効にする
		
		if (formDispQRInfo.getWorkStatus() == DaoHouseWorkStatus.STATUS_NOT
		||  formDispQRInfo.getWorkStatus() == DaoHouseWorkStatus.STATUS_DONE) {
			
			for (int index = 0 ; index < formDispQRInfo.getButtonDispInfoList().size(); index ++) {
				
				log.info("【DBG】" + pgmId + ":□index=[" + index + "]ボタン区分=[" + formDispQRInfo.getButtonDispInfoList().get(index).getButtonKbn() + "]");
				if (
				   formDispQRInfo.getButtonDispInfoList().get(index).getButtonKbn().equals(ButtonKbn.START) == true   //作業開始ボタン
				|| formDispQRInfo.getButtonDispInfoList().get(index).getButtonKbn().equals(ButtonKbn.CANCEL) == true  //取消ボタン
				) {
					
					formDispQRInfo.getButtonDispInfoList().get(index).setButtonEnabledFlg(true);
					
				} else {
					formDispQRInfo.getButtonDispInfoList().get(index).setButtonEnabledFlg(false);
					
				}
				
			}
			
		}
		
		
		
		// ------------------------------------------------
		// 最新のデータが「作業中状態」である場合は「作業開始」「作業完了」「作業中断」「取消」ボタンのみ有効にする
		// 【メモ】
		//「作業開始」ボタンは前回作業終了を入力し忘れた場合の救済措置として表示しておく。
		//
		
		if (formDispQRInfo.getWorkStatus() == DaoHouseWorkStatus.STATUS_WORKING) {
			
			for (int index = 0 ; index < formDispQRInfo.getButtonDispInfoList().size(); index ++) {
				
				log.info("【DBG】" + pgmId + ":■index=[" + index + "]ボタン区分=[" + formDispQRInfo.getButtonDispInfoList().get(index).getButtonKbn() + "]");
				if (
				   formDispQRInfo.getButtonDispInfoList().get(index).getButtonKbn().equals(ButtonKbn.START) == true   //作業開始ボタン
				|| formDispQRInfo.getButtonDispInfoList().get(index).getButtonKbn().equals(ButtonKbn.END) == true     //作業完了ボタン
				|| formDispQRInfo.getButtonDispInfoList().get(index).getButtonKbn().equals(ButtonKbn.HALFWAY) == true//作業中断ボタン
				|| formDispQRInfo.getButtonDispInfoList().get(index).getButtonKbn().equals(ButtonKbn.CANCEL) == true //取消ボタン
				) {
					
					formDispQRInfo.getButtonDispInfoList().get(index).setButtonEnabledFlg(true);
					
				} else {
					formDispQRInfo.getButtonDispInfoList().get(index).setButtonEnabledFlg(false);
					
				}
			}
		}
		
		
		
		// ------------------------------------------------
		
		mav.addObject("formDispQRInfo", formDispQRInfo);
		
		
		log.info("【INF】" + pgmId + ":処理終了!!!");
		mav.setViewName("scrDispQRInfo.html");
		return mav;
	}
	
	
	
	
	@RequestMapping(value ="/matsuoka/RegistQRInfo",method = RequestMethod.POST)
	public ModelAndView registQRInfo(@ModelAttribute FormDispQRInfo formDispQRInfo, ModelAndView mav) {
		
		String pgmId = classId + ".registQRInfo";
		
		log.info("【INF】" + pgmId + " :★処理開始 押したボタンのボタン区分=[" + formDispQRInfo.getPushedButtunKbn() + "]、進捗率=[" + formDispQRInfo.getPushedButtunPercent() + "]、備考=[" + formDispQRInfo.getBiko() + "]、社員ID=[" + formDispQRInfo.getLoginEmployeeId() + "]");
		log.info("【INF】" + pgmId + " :選択デバイス名=[" + formDispQRInfo.getSelectedDeviceLabel() + "]");
		log.info("【INF】" + pgmId + " :ハウスID=[" + formDispQRInfo.getHouseId() + "]、列№=[" + formDispQRInfo.getColNo() + "]、作業ID=[" + formDispQRInfo.getWorkId() + "]、開始日時=[" + formDispQRInfo.getStartDatetime() + "]");
		
		FormReadQRStart formReadQRStart = new FormReadQRStart();
		
		// ログイン社員ID、社員名をセット
		formReadQRStart.setLoginEmployeeId(formDispQRInfo.getLoginEmployeeId());
		formReadQRStart.setLoginEmployeeName(formDispQRInfo.getLoginEmployeeName());
		formReadQRStart.setSelectedDeviceLabel(formDispQRInfo.getSelectedDeviceLabel());
		
		
		
		// キャンセル(取消してもう一度)である場合は登録処理を行わない
		if (formDispQRInfo.getPushedButtunKbn().equals(ButtonKbn.CANCEL)) {
			
			formReadQRStart.setMessage("ＱＲコードの読み取りが取消されました。");
			
			mav.addObject("formReadQRStart", formReadQRStart);
			
			log.info("【INF】" + pgmId + ":処理終了");
			mav.setViewName("scrReadQRStart.html");
			return mav;
			
			
		}
		
		
		DaoHouseWorkStatus daoHouseWorkStatus = new DaoHouseWorkStatus(jdbcTemplate);
		HouseWorkStatus houseWorkStatus = new HouseWorkStatus();
		boolean ret = false;
		
		
		
		
		
		
		// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
		// 
		// 作業が「消毒」か「その他」である場合（全列の作業進捗として登録・更新する）
		// 
		// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
		
		
		if (formDispQRInfo.getWorkId().equals(SpecialWork.SHODOKU)
		||  formDispQRInfo.getWorkId().equals(SpecialWork.OTHER)
		) {
			
			
			houseWorkStatus.setWorkId(formDispQRInfo.getWorkId());
			houseWorkStatus.setHouseId(formDispQRInfo.getHouseId());
			houseWorkStatus.setColNo(formDispQRInfo.getColNo());
			houseWorkStatus.setPercent(formDispQRInfo.getPushedButtunPercent());
			houseWorkStatus.setBiko(formDispQRInfo.getBiko());
			
			
			
			// ------------------------------------------------
			// 作業状況のデータを登録・更新し遷移先に表示するメッセージの設定
			
			
			// 作業開始
			if (formDispQRInfo.getPushedButtunKbn().equals(ButtonKbn.START)) {
				
				houseWorkStatus.setStartEmployeeId(formDispQRInfo.getLoginEmployeeId());
				houseWorkStatus.setStartDateTime(LocalDateTime.now());
				//houseWorkStatus.setEndEmployeeId();  // ”作業開始”なので終了社員IDはセット不要
				//houseWorkStatus.setEndDateTime();    // ”作業開始”なので終了日時  はセット不要
				
				ret = daoHouseWorkStatus.registStartStatusAllCol(houseWorkStatus, formDispQRInfo.getLoginEmployeeId(), "scrDispQRInfo");
				
				formReadQRStart.setMessage("作業開始で登録しました。");
				
			}
			
			
			// 作業完了
			if (formDispQRInfo.getPushedButtunKbn().equals(ButtonKbn.END)) {
				
				houseWorkStatus.setStartEmployeeId(formDispQRInfo.getStartEmployeeid());
				houseWorkStatus.setStartDateTime(formDispQRInfo.getStartDatetime());
				houseWorkStatus.setEndEmployeeId(formDispQRInfo.getLoginEmployeeId());
				houseWorkStatus.setEndDateTime(LocalDateTime.now());
				
				ret = daoHouseWorkStatus.updateEndStatusAllCol(houseWorkStatus, formDispQRInfo.getLoginEmployeeId(), "scrDispQRInfo");

				// 作業開始日時
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
				String startDateTime = formatter.format(houseWorkStatus.getStartDateTime());
				String endDateTime = formatter.format(houseWorkStatus.getEndDateTime());
				// 作業時間をｎ時間ｍ分で表示
				Duration duration = Duration.between(houseWorkStatus.getStartDateTime(), houseWorkStatus.getEndDateTime());
				long hours = duration.toHours();
				long minutes = duration.minusHours(hours).toMinutes();
				
				formReadQRStart.setMessage("☆作業完了(100%)で登録しました。\n作業開始：" + startDateTime + "\n作業終了：" + endDateTime + "\n作業時間：" + hours + " 時間 " + minutes + " 分");
				
			}
			
			
			
			// 作業中断
			if (formDispQRInfo.getPushedButtunKbn().equals(ButtonKbn.HALFWAY)) {
				
				houseWorkStatus.setStartEmployeeId(formDispQRInfo.getStartEmployeeid());
				houseWorkStatus.setStartDateTime(formDispQRInfo.getStartDatetime());
				//houseWorkStatus.setEndEmployeeId();  // ”作業中断”なので終了社員IDはセット不要
				//houseWorkStatus.setEndDateTime();    // ”作業中断”なので終了日時  はセット不要
				
				ret = daoHouseWorkStatus.updateHalfWayStatusAllCol(houseWorkStatus, formDispQRInfo.getLoginEmployeeId(), "scrDispQRInfo");
				
				formReadQRStart.setMessage("作業中断(" + Integer.toString(formDispQRInfo.getPushedButtunPercent()) + "%)で登録しました。");
			}
			
			
			
			// 登録・更新処理結果判定
			if (ret == false) {
				
				log.error("【ERR】" + pgmId + " :作業状況の登録・更新処理で異常が発生しました。※消毒／その他作業");
				formReadQRStart.setMessage("【エラー】登録処理で異常が発生しました（作業状況登録・更新エラー）。もう一度ＱＲコードの読み取り行うかシステム担当者にご連絡ください。");
				
				mav.addObject("formReadQRStart", formReadQRStart);
				
				log.info("【INF】" + pgmId + ":処理終了");
				mav.setViewName("scrReadQRStart.html");
				return mav;
				
				
			}
			
			
			mav.addObject("formReadQRStart", formReadQRStart);
			
			log.info("【INF】" + pgmId + ":処理終了");
			mav.setViewName("scrReadQRStart.html");
			// ★★★★★★★★★★★★★★★★★★★★★★★★★
			//
			//      消毒・その他である場合はココで処理終了
			//
			// ★★★★★★★★★★★★★★★★★★★★★★★★★
			return mav;
			
		}
		
		
		
		// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
		// 
		// 作業が一般作業である場合（QRコードの列の作業進捗として登録・更新する）
		// 
		// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
		
		
		
		
		houseWorkStatus.setWorkId(formDispQRInfo.getWorkId());
		houseWorkStatus.setHouseId(formDispQRInfo.getHouseId());
		houseWorkStatus.setColNo(formDispQRInfo.getColNo());
		houseWorkStatus.setPercent(formDispQRInfo.getPushedButtunPercent());
		houseWorkStatus.setBiko(formDispQRInfo.getBiko());
		
		
		
		// ------------------------------------------------
		// 作業状況のデータを登録・更新し遷移先に表示するメッセージの設定
		
		
		// 作業開始
		if (formDispQRInfo.getPushedButtunKbn().equals(ButtonKbn.START)) {
			
			houseWorkStatus.setStartEmployeeId(formDispQRInfo.getLoginEmployeeId());
			houseWorkStatus.setStartDateTime(LocalDateTime.now());
			//houseWorkStatus.setEndEmployeeId();  // ”作業開始”なので終了社員IDはセット不要
			//houseWorkStatus.setEndDateTime();    // ”作業開始”なので終了日時  はセット不要
			
			ret = daoHouseWorkStatus.registStartStatus(houseWorkStatus, formDispQRInfo.getLoginEmployeeId(), "scrDispQRInfo");
			
			formReadQRStart.setMessage("作業開始で登録しました。");
			
		}
		
		
		// 作業完了
		if (formDispQRInfo.getPushedButtunKbn().equals(ButtonKbn.END)) {
			
			houseWorkStatus.setStartEmployeeId(formDispQRInfo.getStartEmployeeid());
			houseWorkStatus.setStartDateTime(formDispQRInfo.getStartDatetime());
			houseWorkStatus.setEndEmployeeId(formDispQRInfo.getLoginEmployeeId());
			houseWorkStatus.setEndDateTime(LocalDateTime.now());
			
			ret = daoHouseWorkStatus.updateEndStatus(houseWorkStatus, formDispQRInfo.getLoginEmployeeId(), "scrDispQRInfo");
			
			// 作業開始日時
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
			String startDateTime = formatter.format(houseWorkStatus.getStartDateTime());
			String endDateTime = formatter.format(houseWorkStatus.getEndDateTime());
			// 作業時間をｎ時間ｍ分で表示
			Duration duration = Duration.between(houseWorkStatus.getStartDateTime(), houseWorkStatus.getEndDateTime());
			long hours = duration.toHours();
			long minutes = duration.minusHours(hours).toMinutes();
			
			formReadQRStart.setMessage("☆作業完了(100%)で登録しました。\n作業開始：" + startDateTime + "\n作業終了：" + endDateTime + "\n作業時間：" + hours + " 時間 " + minutes + " 分");
			
		}
		
		
		
		// 作業中断
		if (formDispQRInfo.getPushedButtunKbn().equals(ButtonKbn.HALFWAY)) {
			
			houseWorkStatus.setStartEmployeeId(formDispQRInfo.getStartEmployeeid());
			houseWorkStatus.setStartDateTime(formDispQRInfo.getStartDatetime());
			//houseWorkStatus.setEndEmployeeId();  // ”作業中断”なので終了社員IDはセット不要
			//houseWorkStatus.setEndDateTime();    // ”作業中断”なので終了日時  はセット不要
			
			ret = daoHouseWorkStatus.updateHalfWayStatus(houseWorkStatus, formDispQRInfo.getLoginEmployeeId(), "scrDispQRInfo");
			
			formReadQRStart.setMessage("作業中断(" + Integer.toString(formDispQRInfo.getPushedButtunPercent()) + "%)で登録しました。");
		}
		
		
		
		// 登録・更新処理結果判定
		if (ret == false) {
			
			log.error("【ERR】" + pgmId + " :作業状況の登録・更新処理で異常が発生しました。");
			formReadQRStart.setMessage("【エラー】登録処理で異常が発生しました（作業状況登録・更新エラー）。もう一度ＱＲコードの読み取り行うかシステム担当者にご連絡ください。");
			
			mav.addObject("formReadQRStart", formReadQRStart);
			
			log.info("【INF】" + pgmId + ":処理終了");
			mav.setViewName("scrReadQRStart.html");
			return mav;
			
			
		}
		
		
		mav.addObject("formReadQRStart", formReadQRStart);
		
		log.info("【INF】" + pgmId + ":処理終了");
		mav.setViewName("scrReadQRStart.html");
		return mav;
	}
	
	
	
	
	
	
	
	@RequestMapping(value ="/matsuoka/RegistQRInfoShukaku",method = RequestMethod.POST)
	public ModelAndView registQRInfoShukaku(@ModelAttribute FormDispQRInfoShukaku formDispQRInfoShukaku, ModelAndView mav) {
		
		String pgmId = classId + ".registQRInfoShukaku";
		
		log.info("【INF】" + pgmId + " :★処理開始 押したボタンのボタン区分=[" + formDispQRInfoShukaku.getPushedButtunKbn() + "]、進捗率=[" + formDispQRInfoShukaku.getPushedButtunPercent() + "]、備考=[" + formDispQRInfoShukaku.getBiko() + "]、社員ID=[" + formDispQRInfoShukaku.getLoginEmployeeId() + "]");
		log.info("【INF】" + pgmId + " :選択デバイス名=[" + formDispQRInfoShukaku.getSelectedDeviceLabel() + "]");
		log.info("【INF】" + pgmId + " :ハウスID=[" + formDispQRInfoShukaku.getHouseId() + "]、列№=[" + formDispQRInfoShukaku.getColNo() + "]、作業ID=[" + formDispQRInfoShukaku.getWorkId() + "]、開始日時=[" + formDispQRInfoShukaku.getStartDatetime() + "]");
		
		FormReadQRStartShukaku formReadQRStartShukaku = new FormReadQRStartShukaku();
		
		// ログイン社員ID、社員名をセット
		formReadQRStartShukaku.setLoginEmployeeId(formDispQRInfoShukaku.getLoginEmployeeId());
		formReadQRStartShukaku.setLoginEmployeeName(formDispQRInfoShukaku.getLoginEmployeeName());
		formReadQRStartShukaku.setSelectedDeviceLabel(formDispQRInfoShukaku.getSelectedDeviceLabel());
		
		
		
		// キャンセル(取消してもう一度)である場合は登録処理を行わない
		if (formDispQRInfoShukaku.getPushedButtunKbn().equals(ButtonKbn.CANCEL)) {
			
			formReadQRStartShukaku.setMessage("ＱＲコードの読み取りが取消されました。");
			
			mav.addObject("formReadQRStartShukaku", formReadQRStartShukaku);
			
			log.info("【INF】" + pgmId + ":処理終了");
			mav.setViewName("scrReadQRStartShukaku.html");
			return mav;
			
			
		}
		
		
		DaoHouseWorkStatusShukaku daoHouseWorkStatusShukaku = new DaoHouseWorkStatusShukaku(jdbcTemplate);
		HouseWorkStatusShukaku houseWorkStatusShukaku = new HouseWorkStatusShukaku();
		boolean ret = false;
		
		
		
		
		// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
		// 
		// QRコードの列の作業進捗として登録・更新する
		// 
		// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
		
		
		
		
		houseWorkStatusShukaku.setWorkId(formDispQRInfoShukaku.getWorkId());
		houseWorkStatusShukaku.setHouseId(formDispQRInfoShukaku.getHouseId());
		houseWorkStatusShukaku.setColNo(formDispQRInfoShukaku.getColNo());
		houseWorkStatusShukaku.setPercent(formDispQRInfoShukaku.getPushedButtunPercent());
		houseWorkStatusShukaku.setBiko(formDispQRInfoShukaku.getBiko());
		houseWorkStatusShukaku.setBoxCount(formDispQRInfoShukaku.getBoxCount());
		
		
		
		// ------------------------------------------------
		// 作業状況のデータを登録・更新し遷移先に表示するメッセージの設定
		
		
		// 作業開始
		if (formDispQRInfoShukaku.getPushedButtunKbn().equals(ButtonKbn.START)) {
			
			houseWorkStatusShukaku.setStartEmployeeId(formDispQRInfoShukaku.getLoginEmployeeId());
			houseWorkStatusShukaku.setStartDateTime(LocalDateTime.now());
			//houseWorkStatusShukaku.setEndEmployeeId();  // ”作業開始”なので終了社員IDはセット不要
			//houseWorkStatusShukaku.setEndDateTime();    // ”作業開始”なので終了日時  はセット不要
			
			ret = daoHouseWorkStatusShukaku.registStartStatus(houseWorkStatusShukaku, formDispQRInfoShukaku.getLoginEmployeeId(), "scrDispQRInfo");
			
			formReadQRStartShukaku.setMessage("作業開始で登録しました。");
			
		}
		
		
		// 作業完了
		if (formDispQRInfoShukaku.getPushedButtunKbn().equals(ButtonKbn.END)) {
			
			houseWorkStatusShukaku.setStartEmployeeId(formDispQRInfoShukaku.getStartEmployeeid());
			houseWorkStatusShukaku.setStartDateTime(formDispQRInfoShukaku.getStartDatetime());
			houseWorkStatusShukaku.setEndEmployeeId(formDispQRInfoShukaku.getLoginEmployeeId());
			houseWorkStatusShukaku.setEndDateTime(LocalDateTime.now());
			
			ret = daoHouseWorkStatusShukaku.updateEndStatus(houseWorkStatusShukaku, formDispQRInfoShukaku.getLoginEmployeeId(), "scrDispQRInfo");
			
			// 作業開始日時
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
			String startDateTime = formatter.format(houseWorkStatusShukaku.getStartDateTime());
			String endDateTime = formatter.format(houseWorkStatusShukaku.getEndDateTime());
			// 作業時間をｎ時間ｍ分で表示
			Duration duration = Duration.between(houseWorkStatusShukaku.getStartDateTime(), houseWorkStatusShukaku.getEndDateTime());
			long hours = duration.toHours();
			long minutes = duration.minusHours(hours).toMinutes();
			//【メモ】\nで改行して表示させてる
			formReadQRStartShukaku.setMessage("☆作業完了(100%)で登録しました。\n作業開始：" + startDateTime + "\n作業終了：" + endDateTime + "\n作業時間：" + hours + " 時間 " + minutes + " 分");
			
		}
		
		
		
		// 登録・更新処理結果判定
		if (ret == false) {
			
			log.error("【ERR】" + pgmId + " :作業状況の登録・更新処理で異常が発生しました。");
			formReadQRStartShukaku.setMessage("【エラー】登録処理で異常が発生しました（作業状況登録・更新エラー）。もう一度ＱＲコードの読み取り行うかシステム担当者にご連絡ください。");
			
			mav.addObject("formReadQRStartShukaku", formReadQRStartShukaku);
			
			log.info("【INF】" + pgmId + ":処理終了");
			mav.setViewName("scrReadQRStartShukaku.html");
			return mav;
			
			
		}
		
		
		mav.addObject("formReadQRStartShukaku", formReadQRStartShukaku);
		
		log.info("【INF】" + pgmId + ":処理終了");
		mav.setViewName("scrReadQRStartShukaku.html");
		return mav;
	}
	
	
	
	
	
	
	@RequestMapping(value ="/matsuoka/RegistQRInfoShukakuSum",method = RequestMethod.POST)
	public ModelAndView registQRInfoShukakuSum(@ModelAttribute FormDispQRInfoShukakuSum formDispQRInfoShukakuSum, ModelAndView mav) {
		
		String pgmId = classId + ".registQRInfoShukakuSum";
		
		log.info("【INF】" + pgmId + " :★処理開始 押したボタンのボタン区分=[" + formDispQRInfoShukakuSum.getPushedButtunKbn() + "]");
		log.info("【INF】" + pgmId + " :収穫日=[" + formDispQRInfoShukakuSum.getShukakuDate() + "]、ケース数=[" + formDispQRInfoShukakuSum.getBoxSum() + "]");
		log.info("【INF】" + pgmId + " :備考=[" + formDispQRInfoShukakuSum.getBiko() + "]、社員ID=[" + formDispQRInfoShukakuSum.getLoginEmployeeId() + "]");
		log.info("【INF】" + pgmId + " :選択デバイス名=[" + formDispQRInfoShukakuSum.getSelectedDeviceLabel() + "]");
		log.info("【INF】" + pgmId + " :ハウスID=[" + formDispQRInfoShukakuSum.getHouseId() + "]");
		log.info("【INF】" + pgmId + " :登録社員ID=[" + formDispQRInfoShukakuSum.getRegistEmployeeid() + "]、登録日時=[" + formDispQRInfoShukakuSum.getRegistDatetime() + "]");
		
		
		FormReadQRStartShukakuSum formReadQRStartShukakuSum = new FormReadQRStartShukakuSum();
		
		// ログイン社員ID、社員名をセット
		formReadQRStartShukakuSum.setLoginEmployeeId(formDispQRInfoShukakuSum.getLoginEmployeeId());
		formReadQRStartShukakuSum.setLoginEmployeeName(formDispQRInfoShukakuSum.getLoginEmployeeName());
		formReadQRStartShukakuSum.setSelectedDeviceLabel(formDispQRInfoShukakuSum.getSelectedDeviceLabel());
		formReadQRStartShukakuSum.setBoxSum(formDispQRInfoShukakuSum.getBoxSum());
		
		
		// キャンセル(取消してもう一度)である場合は登録処理を行わない
		if (formDispQRInfoShukakuSum.getPushedButtunKbn().equals(ButtonKbn.CANCEL)) {
			
			formReadQRStartShukakuSum.setMessage("ＱＲコードの読み取りが取消されました。");
			
			mav.addObject("formReadQRStartShukakuSum", formReadQRStartShukakuSum);
			
			log.info("【INF】" + pgmId + ":処理終了");
			mav.setViewName("scrReadQRStartShukakuSum.html");
			return mav;
			
			
		}
		
		
		DaoShukakuBoxSum daoShukakuBoxSum = new DaoShukakuBoxSum(jdbcTemplate);
		boolean ret = false;
		
		
		
		
		// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
		// 
		// 収穫ケース数合計の登録
		// 
		// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
		
		
		
		// ------------------------------------------------
		// 指定されたハウス、収穫日の"未削除"データを”削除状態”に更新
		ret = daoShukakuBoxSum.updateDeleteState(formDispQRInfoShukakuSum.getHouseId()
												,formDispQRInfoShukakuSum.getRegistDatetime()
												,formDispQRInfoShukakuSum.getLoginEmployeeId()
												,"scrDispQRInfoShukakuSum"
												);
		
		// 更新処理結果判定
		if (ret == false) {
			
			log.error("【ERR】" + pgmId + " :収穫ケース数合計の更新処理で異常が発生しました。");
			formReadQRStartShukakuSum.setMessage("【エラー】登録処理で異常が発生しました（収穫ケース数登録エラー）。もう一度ＱＲコードの読み取り行うかシステム担当者にご連絡ください。");
			
			mav.addObject("formReadQRStartShukakuSum", formReadQRStartShukakuSum);
			
			log.info("【INF】" + pgmId + ":処理終了");
			mav.setViewName("scrReadQRStartShukakuSum.html");
			return mav;
			
			
		}
		
		
		// ------------------------------------------------
		// 指定されたハウス、収穫日の最新の収穫ケース数合計の情報を登録
		
		
		ShukakuBoxSum shukakuBoxSum = new ShukakuBoxSum();
		
		shukakuBoxSum.setHouseId(formDispQRInfoShukakuSum.getHouseId());
		shukakuBoxSum.setShukakuDate(formDispQRInfoShukakuSum.getRegistDatetime());
		shukakuBoxSum.setRegistDatetime(LocalDateTime.now());
		shukakuBoxSum.setRegistEmployeeid(formReadQRStartShukakuSum.getLoginEmployeeId());
		shukakuBoxSum.setBiko(formDispQRInfoShukakuSum.getBiko());
		shukakuBoxSum.setBoxSum(formDispQRInfoShukakuSum.getBoxSum());
		
		
		ret = daoShukakuBoxSum.regist(shukakuBoxSum
									, formDispQRInfoShukakuSum.getLoginEmployeeId()
									,"scrDispQRInfoShukakuSum"
									);
		
		// 登録処理結果判定
		if (ret == false) {
			
			log.error("【ERR】" + pgmId + " :収穫ケース数合計の登録処理で異常が発生しました。");
			formReadQRStartShukakuSum.setMessage("【エラー】登録処理で異常が発生しました（収穫ケース数登録エラー）。もう一度ＱＲコードの読み取り行うかシステム担当者にご連絡ください。");
			
			mav.addObject("formReadQRStartShukakuSum", formReadQRStartShukakuSum);
			
			log.info("【INF】" + pgmId + ":処理終了");
			mav.setViewName("scrReadQRStartShukakuSum.html");
			return mav;
			
			
		}
		
		
		
		
		// ------------------------------------------------
		// ハウスIDに対するハウス名を取得
		
		DaoHouse daoHouse = new DaoHouse(jdbcTemplate);
		String houseName = daoHouse.getNameFromId(formDispQRInfoShukakuSum.getHouseId());
		
		
		if (houseName.trim().equals("") == true) {
			log.error("【ERR】" + pgmId + " :ハウス名の取得で異常終了 ハウスID=[" + formDispQRInfoShukakuSum.getHouseId() + "]");
			formReadQRStartShukakuSum.setMessage("【エラー】登録処理で異常が発生しました（収穫ケース数登録エラー）。もう一度ＱＲコードの読み取り行うかシステム担当者にご連絡ください。");
			
			mav.addObject("formReadQRStartShukakuSum", formReadQRStartShukakuSum);
			
			log.info("【INF】" + pgmId + ":処理終了");
			mav.setViewName("scrReadQRStartShukakuSum.html");
			return mav;
		}
		
		
		
		// ------------------------------------------------
		// 正常終了メッセージのセット
		
		
		// 作業開始日時
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		String shukakuDateString = formatter.format(shukakuBoxSum.getShukakuDate());
		
		// 【メモ】\nで改行して表示させている
		formReadQRStartShukakuSum.setMessage("☆収穫ケース数合計を登録しました。\nハウス：" + houseName + "\n収穫日：" + shukakuDateString + "\n収穫数：" + shukakuBoxSum.getBoxSum());
		
		
		
		mav.addObject("formReadQRStartShukakuSum", formReadQRStartShukakuSum);
		
		log.info("【INF】" + pgmId + ":処理終了");
		mav.setViewName("scrReadQRStartShukakuSum.html");
		return mav;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	//★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
	//
	//  管理システムＴＯＰへの遷移
	//
	//★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
	
	
	//管理者システムのindex画面の起動
	@RequestMapping(value ="/matsuoka/indexKanri",method = RequestMethod.GET)
	public ModelAndView indexKanri(ModelAndView mav) {
		
		String pgmId = classId + ".indexKanri";
		
		log.info("【INF】" + pgmId + ":処理開始");
		
		// ※ログインユーザはFormIndexKanriのコンストラクタで「管理者ユーザ固定」にしている
		FormIndexKanri formIndexKanri = new FormIndexKanri();
		
		mav.addObject("formIndexKanri",formIndexKanri);
		
		log.info("【INF】" + pgmId + ":処理終了");
		
		
		mav.setViewName("scrIndexKanri.html");
		return mav;
	}
	
	
	// ダミー
	@RequestMapping(value ="/matsuoka/TransitionKanriDummy",method = RequestMethod.GET)
	public ModelAndView trunsition_WorkDummy(ModelAndView mav) {

		String pgmId = classId + ".trunsition_WorkDummy";
		
		log.info("【INF】" + pgmId + ":処理開始");

		// ※ログインユーザはFormIndexKanriのコンストラクタで「管理者ユーザ固定」にしている
		FormIndexKanri formIndexKanri = new FormIndexKanri();
		
		mav.addObject("formIndexKanri",formIndexKanri);
		
		log.info("【INF】" + pgmId + ":処理終了");
		mav.setViewName("scrIndexKanri.html");
		return mav;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	//★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
	//
	//  社員情報メンテナンス
	//
	//★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
	
	
	// 一覧画面への遷移
	@RequestMapping(value ="/matsuoka/TransitionKanriMainteEmployeeList",method = RequestMethod.GET)
	public ModelAndView trunsition_KanriMainteEmployeeList(ModelAndView mav) {

		String pgmId = classId + ".trunsition_KanriMainteEmployeeList";
		
		log.info("【INF】" + pgmId + ":処理開始");
		
		DaoFormKanriMainteEmployee dao = new DaoFormKanriMainteEmployee(jdbcTemplate);
		
		//------------------------------------------------
		// 一覧表示用にデータを取得
		FormKanriMainteEmployeeList formKanriMainteEmployeeList;
		
		formKanriMainteEmployeeList = dao.getAllEmployeeData();
		
		if (formKanriMainteEmployeeList == null) {
			
			formKanriMainteEmployeeList = new FormKanriMainteEmployeeList();
			formKanriMainteEmployeeList.setMessage("検索処理で異常が発生しました。システム管理者にご連絡ください。");
			log.info("【ERR】" + pgmId + ":検索処理で異常終了");
		
		} else if (formKanriMainteEmployeeList.getEmployeeList().size() == 0) {
		
			formKanriMainteEmployeeList.setMessage("データが0件でした。");
			log.info("【INF】" + pgmId + ":データが0件でした。");
		}
		
		
		
		mav.addObject("formKanriMainteEmployeeList",formKanriMainteEmployeeList);
		
		log.info("【INF】" + pgmId + ":処理終了");
		mav.setViewName("scrKanriMainteEmployeeList.html");
		return mav;
	}
	
	//詳細画面への遷移
	@RequestMapping(value ="/matsuoka/TransitionKanriMainteEmployeeDetail",method = RequestMethod.POST)
	public ModelAndView trunsition_KanriMainteEmployeeDetail(@ModelAttribute FormKanriMainteEmployeeList formKanriMainteEmployeeList, ModelAndView mav) {
		
		String pgmId = classId + ".trunsition_KanriMainteEmployeeDetail";
		
		log.info("【INF】" + pgmId + " :処理開始 社員ID=[" + formKanriMainteEmployeeList.getSelectEmployeeId() + "]");
		
		String targetEmployeeId = formKanriMainteEmployeeList.getSelectEmployeeId();
		
		
		FormKanriMainteEmployeeDetail formKanriMainteEmployeeDetail = new FormKanriMainteEmployeeDetail();
		
		
		if (targetEmployeeId.equals("") == true) {
			
			//------------------------------------------------
			//社員IDが指定されていない場合(新規登録)：次画面を空表示
			
			// 処理なし
			
		}else{
			//------------------------------------------------
			//社員IDが指定されている  場合(更新削除)：社員情報を検索して次画面に表示
			
			DaoFormKanriMainteEmployee dao = new DaoFormKanriMainteEmployee(jdbcTemplate);
			formKanriMainteEmployeeDetail = dao.getTargetEmployeeData(targetEmployeeId);
			
		}
		
		
		mav.addObject("formKanriMainteEmployeeDetail",formKanriMainteEmployeeDetail);
		
		log.info("【INF】" + pgmId + ":処理終了");
		mav.setViewName("scrKanriMainteEmployeeDetail.html");
		return mav;
	}
	
	
	@RequestMapping(value ="/matsuoka/EditKanriEmployee",method = RequestMethod.POST)
	public ModelAndView editKanriEmployee(@ModelAttribute FormKanriMainteEmployeeDetail formKanriMainteEmployeeDetail, ModelAndView mav) {
		
		String pgmId = classId + ".editKanriEmployee";
		
		log.info("【INF】" + pgmId + " :処理開始");
		log.info("【INF】" + pgmId + " :ﾎﾞﾀﾝ区分=[" + formKanriMainteEmployeeDetail.getButtonKbn() + "]");
		log.info("【INF】" + pgmId + " :社員ＩＤ=[" + formKanriMainteEmployeeDetail.getEmployeeId() + "]");
		log.info("【INF】" + pgmId + " :社員氏名=[" + formKanriMainteEmployeeDetail.getEmployeeName() + "]");

		DaoFormKanriMainteEmployee dao = new DaoFormKanriMainteEmployee(jdbcTemplate);
		
		
		//------------------------------------------------
		// 登録・更新・削除
		
		
		if (formKanriMainteEmployeeDetail.getButtonKbn().equals("regist") == true) {
			
			//------------------------------------------------
			//登録処理を実施
			dao.registEmployee(formKanriMainteEmployeeDetail, SpecialUser.KANRI_USER, "scrKanriMainteEmployeeDetail");
			
			
		} else if (formKanriMainteEmployeeDetail.getButtonKbn().equals("update") == true) {
			
			//------------------------------------------------
			//更新処理を実施
			dao.updateEmployee(formKanriMainteEmployeeDetail, SpecialUser.KANRI_USER, "scrKanriMainteEmployeeDetail");
			
			
		} else if (formKanriMainteEmployeeDetail.getButtonKbn().equals("delete") == true) {
			
			//------------------------------------------------
			//削除処理を実施
			dao.deleteEmployee(formKanriMainteEmployeeDetail, SpecialUser.KANRI_USER, "scrKanriMainteEmployeeDetail");
			
			
		}
		
		
		//------------------------------------------------
		// 一覧表示用にデータを取得
		FormKanriMainteEmployeeList formKanriMainteEmployeeList;
		
		formKanriMainteEmployeeList = dao.getAllEmployeeData();

		if (formKanriMainteEmployeeList == null) {
			
			formKanriMainteEmployeeList = new FormKanriMainteEmployeeList();
			formKanriMainteEmployeeList.setMessage("検索処理で異常が発生しました。システム管理者にご連絡ください。");
			log.info("【ERR】" + pgmId + ":検索処理で異常終了");
		
		} else if (formKanriMainteEmployeeList.getEmployeeList().size() == 0) {
		
			formKanriMainteEmployeeList.setMessage("データが0件でした。");
			log.info("【INF】" + pgmId + ":データが0件でした。");
		}
		
		
		
		
		mav.addObject("formKanriMainteEmployeeList",formKanriMainteEmployeeList);
		
		log.info("【INF】" + pgmId + ":処理終了");
		mav.setViewName("scrKanriMainteEmployeeList.html");
		return mav;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	//★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
	//
	//  ハウス情報メンテナンス
	//
	//★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
	
	
	// 一覧画面への遷移
	@RequestMapping(value ="/matsuoka/TransitionKanriMainteHouseList",method = RequestMethod.GET)
	public ModelAndView trunsition_KanriMainteHouseList(ModelAndView mav) {

		String pgmId = classId + ".trunsition_KanriMainteHouseList";
		
		log.info("【INF】" + pgmId + ":処理開始");
		
		DaoFormKanriMainteHouse dao = new DaoFormKanriMainteHouse(jdbcTemplate);
		
		//------------------------------------------------
		// 一覧表示用にデータを取得
		FormKanriMainteHouseList formKanriMainteHouseList;
		
		formKanriMainteHouseList = dao.getAllHouseData();
		
		if (formKanriMainteHouseList == null) {
			
			formKanriMainteHouseList = new FormKanriMainteHouseList();
			formKanriMainteHouseList.setMessage("検索処理で異常が発生しました。システム管理者にご連絡ください。");
			log.info("【ERR】" + pgmId + ":検索処理で異常終了");
		
		} else if (formKanriMainteHouseList.getHouseList().size() == 0) {
		
			formKanriMainteHouseList.setMessage("データが0件でした。");
			log.info("【INF】" + pgmId + ":データが0件でした。");
		}
		
		
		
		mav.addObject("formKanriMainteHouseList",formKanriMainteHouseList);
		
		log.info("【INF】" + pgmId + ":処理終了");
		mav.setViewName("scrKanriMainteHouseList.html");
		return mav;
	}
	
	//詳細画面への遷移
	@RequestMapping(value ="/matsuoka/TransitionKanriMainteHouseDetail",method = RequestMethod.POST)
	public ModelAndView trunsition_KanriMainteHouseDetail(@ModelAttribute FormKanriMainteHouseList formKanriMainteHouseList, ModelAndView mav) {
		
		String pgmId = classId + ".trunsition_KanriMainteHouseDetail";
		
		log.info("【INF】" + pgmId + " :処理開始 ハウスID=[" + formKanriMainteHouseList.getSelectHouseId() + "]");
		
		String targetHouseId = formKanriMainteHouseList.getSelectHouseId();
		
		
		FormKanriMainteHouseDetail formKanriMainteHouseDetail = new FormKanriMainteHouseDetail();
		
		
		if (targetHouseId.equals("") == true) {
			
			//------------------------------------------------
			//ハウスIDが指定されていない場合(新規登録)：次画面を空表示
			
			// 処理なし
			
		}else{
			//------------------------------------------------
			//ハウスIDが指定されている  場合(更新削除)：ハウス情報を検索して次画面に表示
			
			DaoFormKanriMainteHouse dao = new DaoFormKanriMainteHouse(jdbcTemplate);
			formKanriMainteHouseDetail = dao.getTargetHouseData(targetHouseId);
			
		}
		
		
		mav.addObject("formKanriMainteHouseDetail",formKanriMainteHouseDetail);
		
		log.info("【INF】" + pgmId + ":処理終了");
		mav.setViewName("scrKanriMainteHouseDetail.html");
		return mav;
	}
	
	
	@RequestMapping(value ="/matsuoka/EditKanriHouse",method = RequestMethod.POST)
	public ModelAndView editKanriHouse(@ModelAttribute FormKanriMainteHouseDetail formKanriMainteHouseDetail, ModelAndView mav) {
		
		String pgmId = classId + ".editKanriHouse";
		
		log.info("【INF】" + pgmId + " :処理開始");
		log.info("【INF】" + pgmId + " :ﾎﾞﾀﾝ区分=[" + formKanriMainteHouseDetail.getButtonKbn() + "]");
		log.info("【INF】" + pgmId + " :ハウスID=[" + formKanriMainteHouseDetail.getHouseId() + "]");
		log.info("【INF】" + pgmId + " :ハウス名=[" + formKanriMainteHouseDetail.getHouseName() + "]");

		DaoFormKanriMainteHouse dao = new DaoFormKanriMainteHouse(jdbcTemplate);
		
		
		//------------------------------------------------
		// 登録・更新・削除
		
		
		if (formKanriMainteHouseDetail.getButtonKbn().equals("regist") == true) {
			
			//------------------------------------------------
			//登録処理を実施
			dao.registHouse(formKanriMainteHouseDetail, SpecialUser.KANRI_USER, "scrKanriMainteHouseDetail");
			
			
		} else if (formKanriMainteHouseDetail.getButtonKbn().equals("update") == true) {
			
			//------------------------------------------------
			//更新処理を実施
			dao.updateHouse(formKanriMainteHouseDetail, SpecialUser.KANRI_USER, "scrKanriMainteHouseDetail");
			
			
		} else if (formKanriMainteHouseDetail.getButtonKbn().equals("delete") == true) {
			
			//------------------------------------------------
			//削除処理を実施
			dao.deleteHouse(formKanriMainteHouseDetail, SpecialUser.KANRI_USER, "scrKanriMainteHouseDetail");
			
			
		}
		
		
		//------------------------------------------------
		// 一覧表示用にデータを取得
		FormKanriMainteHouseList formKanriMainteHouseList;
		
		formKanriMainteHouseList = dao.getAllHouseData();

		if (formKanriMainteHouseList == null) {
			
			formKanriMainteHouseList = new FormKanriMainteHouseList();
			formKanriMainteHouseList.setMessage("検索処理で異常が発生しました。システム管理者にご連絡ください。");
			log.info("【ERR】" + pgmId + ":検索処理で異常終了");
		
		} else if (formKanriMainteHouseList.getHouseList().size() == 0) {
		
			formKanriMainteHouseList.setMessage("データが0件でした。");
			log.info("【INF】" + pgmId + ":データが0件でした。");
		}
		
		
		
		
		mav.addObject("formKanriMainteHouseList",formKanriMainteHouseList);
		
		log.info("【INF】" + pgmId + ":処理終了");
		mav.setViewName("scrKanriMainteHouseList.html");
		return mav;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	//★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
	//
	//  作業情報メンテナンス
	//
	//★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
	
	
	// 一覧画面への遷移
	@RequestMapping(value ="/matsuoka/TransitionKanriMainteWorkList",method = RequestMethod.GET)
	public ModelAndView trunsition_KanriMainteWorkList(ModelAndView mav) {

		String pgmId = classId + ".trunsition_KanriMainteWorkList";
		
		log.info("【INF】" + pgmId + ":処理開始");
		
		DaoFormKanriMainteWork dao = new DaoFormKanriMainteWork(jdbcTemplate);
		
		//------------------------------------------------
		// 一覧表示用にデータを取得
		FormKanriMainteWorkList formKanriMainteWorkList;
		
		formKanriMainteWorkList = dao.getAllWorkData();
		
		if (formKanriMainteWorkList == null) {
			
			formKanriMainteWorkList = new FormKanriMainteWorkList();
			formKanriMainteWorkList.setMessage("検索処理で異常が発生しました。システム管理者にご連絡ください。");
			log.info("【ERR】" + pgmId + ":検索処理で異常終了");
		
		} else if (formKanriMainteWorkList.getWorkList().size() == 0) {
		
			formKanriMainteWorkList.setMessage("データが0件でした。");
			log.info("【INF】" + pgmId + ":データが0件でした。");
		}
		
		
		
		mav.addObject("formKanriMainteWorkList",formKanriMainteWorkList);
		
		log.info("【INF】" + pgmId + ":処理終了");
		mav.setViewName("scrKanriMainteWorkList.html");
		return mav;
	}
	
	//詳細画面への遷移
	@RequestMapping(value ="/matsuoka/TransitionKanriMainteWorkDetail",method = RequestMethod.POST)
	public ModelAndView trunsition_KanriMainteWorkDetail(@ModelAttribute FormKanriMainteWorkList formKanriMainteWorkList, ModelAndView mav) {
		
		String pgmId = classId + ".trunsition_KanriMainteWorkDetail";
		
		log.info("【INF】" + pgmId + " :処理開始 作業ID=[" + formKanriMainteWorkList.getSelectWorkId() + "]");
		
		String targetWorkId = formKanriMainteWorkList.getSelectWorkId();
		
		
		FormKanriMainteWorkDetail formKanriMainteWorkDetail = new FormKanriMainteWorkDetail();
		
		
		if (targetWorkId.equals("") == true) {
			
			//------------------------------------------------
			//作業IDが指定されていない場合(新規登録)：次画面を空表示
			
			// 処理なし
			
		}else{
			//------------------------------------------------
			//作業IDが指定されている  場合(更新削除)：作業情報を検索して次画面に表示
			
			DaoFormKanriMainteWork dao = new DaoFormKanriMainteWork(jdbcTemplate);
			formKanriMainteWorkDetail = dao.getTargetWorkData(targetWorkId);
			
		}
		
		
		mav.addObject("formKanriMainteWorkDetail",formKanriMainteWorkDetail);
		
		log.info("【INF】" + pgmId + ":処理終了");
		mav.setViewName("scrKanriMainteWorkDetail.html");
		return mav;
	}
	
	//登録処理
	@RequestMapping(value ="/matsuoka/EditKanriWork",method = RequestMethod.POST)
	public ModelAndView editKanriWork(@ModelAttribute FormKanriMainteWorkDetail formKanriMainteWorkDetail, ModelAndView mav) {
		
		String pgmId = classId + ".editKanriWork";
		
		log.info("【INF】" + pgmId + " :処理開始");
		log.info("【INF】" + pgmId + " :ﾎﾞﾀﾝ区分=[" + formKanriMainteWorkDetail.getButtonKbn() + "]");
		log.info("【INF】" + pgmId + " :作業ID  =[" + formKanriMainteWorkDetail.getWorkId() + "]");
		log.info("【INF】" + pgmId + " :作業名  =[" + formKanriMainteWorkDetail.getWorkName() + "]");

		DaoFormKanriMainteWork dao = new DaoFormKanriMainteWork(jdbcTemplate);
		
		
		//------------------------------------------------
		// 登録・更新・削除
		
		
		if (formKanriMainteWorkDetail.getButtonKbn().equals("regist") == true) {
			
			//------------------------------------------------
			//登録処理を実施
			dao.registWork(formKanriMainteWorkDetail, SpecialUser.KANRI_USER, "scrKanriMainteWorkDetail");
			
			
		} else if (formKanriMainteWorkDetail.getButtonKbn().equals("update") == true) {
			
			//------------------------------------------------
			//更新処理を実施
			dao.updateWork(formKanriMainteWorkDetail, SpecialUser.KANRI_USER, "scrKanriMainteWorkDetail");
			
			
		} else if (formKanriMainteWorkDetail.getButtonKbn().equals("delete") == true) {
			
			//------------------------------------------------
			//削除処理を実施
			dao.deleteWork(formKanriMainteWorkDetail, SpecialUser.KANRI_USER, "scrKanriMainteWorkDetail");
			
			
		}
		
		
		//------------------------------------------------
		// 一覧表示用にデータを取得
		FormKanriMainteWorkList formKanriMainteWorkList;
		
		formKanriMainteWorkList = dao.getAllWorkData();

		if (formKanriMainteWorkList == null) {
			
			formKanriMainteWorkList = new FormKanriMainteWorkList();
			formKanriMainteWorkList.setMessage("検索処理で異常が発生しました。システム管理者にご連絡ください。");
			log.info("【ERR】" + pgmId + ":検索処理で異常終了");
		
		} else if (formKanriMainteWorkList.getWorkList().size() == 0) {
		
			formKanriMainteWorkList.setMessage("データが0件でした。");
			log.info("【INF】" + pgmId + ":データが0件でした。");
		}
		
		
		
		
		mav.addObject("formKanriMainteWorkList",formKanriMainteWorkList);
		
		log.info("【INF】" + pgmId + ":処理終了");
		mav.setViewName("scrKanriMainteWorkList.html");
		return mav;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	//★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
	//
	//  作業内容情報メンテナンス
	//
	//★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
	
	
	// 一覧画面への遷移
	@RequestMapping(value ="/matsuoka/TransitionKanriMainteWorkStatusList",method = RequestMethod.GET)
	public ModelAndView trunsition_KanriMainteWorkStatusList(ModelAndView mav) {

		String pgmId = classId + ".trunsition_KanriMainteWorkStatusList";
		
		log.info("【INF】" + pgmId + ":処理開始");
		
		DaoFormKanriMainteWorkStatus dao = new DaoFormKanriMainteWorkStatus(jdbcTemplate);
		
		//------------------------------------------------
		// 一覧表示用にデータを取得
		FormKanriMainteWorkStatusList formKanriMainteWorkStatusList;
		
		// ※最初は検索条件なしで全て一覧表示するため引数は全て空白かnull
		formKanriMainteWorkStatusList = dao.getWorkStatusList("", "", "", null,null);
		
		if (formKanriMainteWorkStatusList == null) {
			
			formKanriMainteWorkStatusList = new FormKanriMainteWorkStatusList();
			formKanriMainteWorkStatusList.setMessage("検索処理で異常が発生しました。システム管理者にご連絡ください。");
			log.info("【ERR】" + pgmId + ":検索処理で異常終了");
		
		} else if (formKanriMainteWorkStatusList.getWorkStatusList().size() == 0) {
		
			formKanriMainteWorkStatusList.setMessage("データが0件でした。");
			log.info("【INF】" + pgmId + ":データが0件でした。");
		}
		
		
		// ------------------------------------------------
		// 絞込み条件欄のドロップダウンリストにセットする値を検索しセット
		
		DaoDropDownList daoDropDown = new DaoDropDownList(jdbcTemplate);
		
		formKanriMainteWorkStatusList.setDropDownHouseList(   daoDropDown.getHouseList());
		formKanriMainteWorkStatusList.setDropDownWorkList(    daoDropDown.getWorkList());
		formKanriMainteWorkStatusList.setDropDownEmployeeList(daoDropDown.getEmployeeList());
		
		
		
		mav.addObject("formKanriMainteWorkStatusList",formKanriMainteWorkStatusList);
		
		log.info("【INF】" + pgmId + ":処理終了");
		mav.setViewName("scrKanriMainteWorkStatusList.html");
		return mav;
	}
	
	
	
	//詳細画面への遷移
	@RequestMapping(value ="/matsuoka/TransitionKanriMainteWorkStatusDetail",method = RequestMethod.POST)
	public ModelAndView trunsition_KanriMainteWorkStatusDetail(@ModelAttribute FormKanriMainteWorkStatusList formKanriMainteWorkStatusList, ModelAndView mav) {
		
		String pgmId = classId + ".trunsition_KanriMainteWorkStatusDetail";
		
		log.info("【INF】" + pgmId + " :処理開始");
		log.info("【INF】" + pgmId + " :ハウスID    =[" + formKanriMainteWorkStatusList.getSelectHouseId() + "]");
		log.info("【INF】" + pgmId + " :列No        =[" + formKanriMainteWorkStatusList.getSelectColNo() + "]");
		log.info("【INF】" + pgmId + " :作業ID      =[" + formKanriMainteWorkStatusList.getSelectWorkId() + "]");
		log.info("【INF】" + pgmId + " :作業開始日時=[" + formKanriMainteWorkStatusList.getSelectStartDateTime() + "]");
		log.info("【INF】" + pgmId + " :▼フィルタリング条件------------------------------------------------");
		log.info("【INF】" + pgmId + " :ハウスID    =[" + formKanriMainteWorkStatusList.getFilterHouseId() + "]");
		log.info("【INF】" + pgmId + " :作業ID      =[" + formKanriMainteWorkStatusList.getFilterWorkId() + "]");
		log.info("【INF】" + pgmId + " :社員ID      =[" + formKanriMainteWorkStatusList.getFilterStartEmployeeId() + "]");
		log.info("【INF】" + pgmId + " :作業開始日Fr=[" + formKanriMainteWorkStatusList.getFilterDateFr() + "]");
		log.info("【INF】" + pgmId + " :作業開始日To=[" + formKanriMainteWorkStatusList.getFilterDateTo() + "]");
		log.info("【INF】" + pgmId + " :▲フィルタリング条件------------------------------------------------");
		
		
		String targetHouseId              = formKanriMainteWorkStatusList.getSelectHouseId();
		String targetColNo                = formKanriMainteWorkStatusList.getSelectColNo();
		String targetWorkId               = formKanriMainteWorkStatusList.getSelectWorkId();
		LocalDateTime targetStartDateTime = formKanriMainteWorkStatusList.getSelectStartDateTime();
		
		
		FormKanriMainteWorkStatusDetail formKanriMainteWorkStatusDetail = new FormKanriMainteWorkStatusDetail();
		
		if (targetHouseId.equals("") == true) {
			
			//------------------------------------------------
			//ハウスIDが指定されていない場合(新規登録)：次画面を空表示
			
			// 処理なし
			
		}else{
			//------------------------------------------------
			//ハウスIDが指定されている  場合(更新削除)：ハウス情報を検索して次画面に表示
			
			DaoFormKanriMainteWorkStatus dao = new DaoFormKanriMainteWorkStatus(jdbcTemplate);
			formKanriMainteWorkStatusDetail = dao.getWorkStatusDatail(targetHouseId, targetColNo, targetWorkId, targetStartDateTime);
		}
		
		
		// ------------------------------------------------
		// 一覧画面で選択・入力したフィルタリング条件を引継ぎ
		
		formKanriMainteWorkStatusDetail.setFilterHouseId(        formKanriMainteWorkStatusList.getFilterHouseId());
		formKanriMainteWorkStatusDetail.setFilterWorkId(         formKanriMainteWorkStatusList.getFilterWorkId());
		formKanriMainteWorkStatusDetail.setFilterStartEmployeeId(formKanriMainteWorkStatusList.getFilterStartEmployeeId());
		formKanriMainteWorkStatusDetail.setFilterDateFr(         formKanriMainteWorkStatusList.getFilterDateFr());
		formKanriMainteWorkStatusDetail.setFilterDateTo(         formKanriMainteWorkStatusList.getFilterDateTo());
		
		
		// ------------------------------------------------
		// 入力欄のドロップダウンリストにセットする値を検索しセット
		
		DaoDropDownList daoDropDown = new DaoDropDownList(jdbcTemplate);
		
		formKanriMainteWorkStatusDetail.setDropDownHouseList(        daoDropDown.getHouseList());
		formKanriMainteWorkStatusDetail.setDropDownWorkList(         daoDropDown.getWorkList());
		formKanriMainteWorkStatusDetail.setDropDownStartEmployeeList(daoDropDown.getEmployeeList());
		formKanriMainteWorkStatusDetail.setDropDownEndEmployeeList(  daoDropDown.getEmployeeList());
		
		
		
		mav.addObject("formKanriMainteWorkStatusDetail",formKanriMainteWorkStatusDetail);
		
		log.info("【INF】" + pgmId + ":処理終了 ■■■開始社員ID＝" + formKanriMainteWorkStatusDetail.getStartEmployeeId());
		mav.setViewName("scrKanriMainteWorkStatusDetail.html");
		return mav;
	}
	
	
	
	//登録処理
	@RequestMapping(value ="/matsuoka/EditKanriWorkStatus",method = RequestMethod.POST)
	public ModelAndView editKanriWorkStatus(@ModelAttribute FormKanriMainteWorkStatusDetail formKanriMainteWorkStatusDetail, ModelAndView mav) {
		
		String pgmId = classId + ".editKanriWorkStatus";
		
		log.info("【INF】" + pgmId + " :処理開始");
		log.info("【INF】" + pgmId + " :ﾎﾞﾀﾝ区分    =[" + formKanriMainteWorkStatusDetail.getButtonKbn() + "]");
		log.info("【INF】" + pgmId + " :ハウスID    =[" + formKanriMainteWorkStatusDetail.getHouseId() + "]");
		log.info("【INF】" + pgmId + " :列No        =[" + formKanriMainteWorkStatusDetail.getColNo() + "]");
		log.info("【INF】" + pgmId + " :作業ID      =[" + formKanriMainteWorkStatusDetail.getWorkId() + "]");
		log.info("【INF】" + pgmId + " :作業開始社員=[" + formKanriMainteWorkStatusDetail.getStartEmployeeId() + "]");
		log.info("【INF】" + pgmId + " :作業開始日時=[" + formKanriMainteWorkStatusDetail.getStartDateTime() + "]");
		log.info("【INF】" + pgmId + " :作業終了社員=[" + formKanriMainteWorkStatusDetail.getEndEmployeeId() + "]");
		log.info("【INF】" + pgmId + " :作業終了日時=[" + formKanriMainteWorkStatusDetail.getEndDateTime() + "]");
		log.info("【INF】" + pgmId + " :ケース数    =[" + formKanriMainteWorkStatusDetail.getBoxCount() + "]");
		log.info("【INF】" + pgmId + " :進捗        =[" + formKanriMainteWorkStatusDetail.getPercent() + "]");
		log.info("【INF】" + pgmId + " :備考        =[" + formKanriMainteWorkStatusDetail.getBiko() + "]");
		log.info("【INF】" + pgmId + " :▼フィルタリング条件------------------------------------------------");
		log.info("【INF】" + pgmId + " :ハウスID    =[" + formKanriMainteWorkStatusDetail.getFilterHouseId() + "]");
		log.info("【INF】" + pgmId + " :作業ID      =[" + formKanriMainteWorkStatusDetail.getFilterWorkId() + "]");
		log.info("【INF】" + pgmId + " :社員ID      =[" + formKanriMainteWorkStatusDetail.getFilterStartEmployeeId() + "]");
		log.info("【INF】" + pgmId + " :作業開始日Fr=[" + formKanriMainteWorkStatusDetail.getFilterDateFr() + "]");
		log.info("【INF】" + pgmId + " :作業開始日To=[" + formKanriMainteWorkStatusDetail.getFilterDateTo() + "]");
		log.info("【INF】" + pgmId + " :▲フィルタリング条件------------------------------------------------");
		
		
		DaoFormKanriMainteWorkStatus dao = new DaoFormKanriMainteWorkStatus(jdbcTemplate);
		
		
		//------------------------------------------------
		// 登録・更新・削除
		
		
		if (formKanriMainteWorkStatusDetail.getButtonKbn().equals("regist") == true) {
			
			//------------------------------------------------
			//登録処理を実施
			dao.registWorkStatus(formKanriMainteWorkStatusDetail, SpecialUser.KANRI_USER, "scrKanriMainteWorkStatusDetail");
			
			
		} else if (formKanriMainteWorkStatusDetail.getButtonKbn().equals("update") == true) {
			
			//------------------------------------------------
			//更新処理を実施
			dao.updateWorkStatus(formKanriMainteWorkStatusDetail, SpecialUser.KANRI_USER, "scrKanriMainteWorkStatusDetail");
			
			
		} else if (formKanriMainteWorkStatusDetail.getButtonKbn().equals("delete") == true) {
			
			//------------------------------------------------
			//削除処理を実施
			dao.deleteWorkStatus(formKanriMainteWorkStatusDetail, SpecialUser.KANRI_USER, "scrKanriMainteWorkStatusDetail");
			
			
		} else if (formKanriMainteWorkStatusDetail.getButtonKbn().equals("revival") == true) {
			
			//------------------------------------------------
			//復旧処理を実施
			dao.revivalWorkStatus(formKanriMainteWorkStatusDetail, SpecialUser.KANRI_USER, "scrKanriMainteWorkStatusDetail");
			
			
		}
		
		
		//------------------------------------------------
		// 一覧表示用にデータを取得
		FormKanriMainteWorkStatusList formKanriMainteWorkStatusList;
		
		formKanriMainteWorkStatusList = dao.getWorkStatusList("", "", "", null,null);
		
		if (formKanriMainteWorkStatusList == null) {
			
			formKanriMainteWorkStatusList = new FormKanriMainteWorkStatusList();
			formKanriMainteWorkStatusList.setMessage("検索処理で異常が発生しました。システム管理者にご連絡ください。");
			log.info("【ERR】" + pgmId + ":検索処理で異常終了");
		
		} else if (formKanriMainteWorkStatusList.getWorkStatusList().size() == 0) {
		
			formKanriMainteWorkStatusList.setMessage("データが0件でした。");
			log.info("【INF】" + pgmId + ":データが0件でした。");
		}

		
		
		// ------------------------------------------------
		// 一覧画面で選択・入力したフィルタリング条件を引継ぎ
		
		formKanriMainteWorkStatusList.setFilterHouseId(        formKanriMainteWorkStatusDetail.getFilterHouseId());
		formKanriMainteWorkStatusList.setFilterWorkId(         formKanriMainteWorkStatusDetail.getFilterWorkId());
		formKanriMainteWorkStatusList.setFilterStartEmployeeId(formKanriMainteWorkStatusDetail.getFilterStartEmployeeId());
		formKanriMainteWorkStatusList.setFilterDateFr(         formKanriMainteWorkStatusDetail.getFilterDateFr());
		formKanriMainteWorkStatusList.setFilterDateTo(         formKanriMainteWorkStatusDetail.getFilterDateTo());
		
		
		// ------------------------------------------------
		// 絞込み条件欄のドロップダウンリストにセットする値を検索しセット
		
		DaoDropDownList daoDropDown = new DaoDropDownList(jdbcTemplate);
		
		formKanriMainteWorkStatusList.setDropDownHouseList(   daoDropDown.getHouseList());
		formKanriMainteWorkStatusList.setDropDownWorkList(    daoDropDown.getWorkList());
		formKanriMainteWorkStatusList.setDropDownEmployeeList(daoDropDown.getEmployeeList());
		
		
		mav.addObject("formKanriMainteWorkStatusList",formKanriMainteWorkStatusList);
		
		log.info("【INF】" + pgmId + ":処理終了");
		mav.setViewName("scrKanriMainteWorkStatusList.html");
		return mav;
	}
	
	
	
	
	//戻るボタン押下処理
	@RequestMapping(value ="/matsuoka/TransitionKanriMainteWorkStatusListSearch",method = RequestMethod.POST)
	public ModelAndView transition_KanriMainteWorkStatusListSearch(@ModelAttribute FormKanriMainteWorkStatusDetail formKanriMainteWorkStatusDetail, ModelAndView mav) {
		
		String pgmId = classId + ".transition_KanriMainteWorkStatusListSearch";
		
		log.info("【INF】" + pgmId + " :処理開始");
		log.info("【INF】" + pgmId + " :ﾎﾞﾀﾝ区分    =[" + formKanriMainteWorkStatusDetail.getButtonKbn() + "]");
		log.info("【INF】" + pgmId + " :ハウスID    =[" + formKanriMainteWorkStatusDetail.getHouseId() + "]");
		log.info("【INF】" + pgmId + " :列No        =[" + formKanriMainteWorkStatusDetail.getColNo() + "]");
		log.info("【INF】" + pgmId + " :作業ID      =[" + formKanriMainteWorkStatusDetail.getWorkId() + "]");
		log.info("【INF】" + pgmId + " :作業開始日付=[" + formKanriMainteWorkStatusDetail.getStartDate() + "]");
		log.info("【INF】" + pgmId + " :作業開始時間=[" + formKanriMainteWorkStatusDetail.getStartTime() + "]");
		log.info("【INF】" + pgmId + " :▼フィルタリング条件------------------------------------------------");
		log.info("【INF】" + pgmId + " :ハウスID    =[" + formKanriMainteWorkStatusDetail.getFilterHouseId() + "]");
		log.info("【INF】" + pgmId + " :作業ID      =[" + formKanriMainteWorkStatusDetail.getFilterWorkId() + "]");
		log.info("【INF】" + pgmId + " :社員ID      =[" + formKanriMainteWorkStatusDetail.getFilterStartEmployeeId() + "]");
		log.info("【INF】" + pgmId + " :作業開始日Fr=[" + formKanriMainteWorkStatusDetail.getFilterDateFr() + "]");
		log.info("【INF】" + pgmId + " :作業開始日To=[" + formKanriMainteWorkStatusDetail.getFilterDateTo() + "]");
		log.info("【INF】" + pgmId + " :▲フィルタリング条件------------------------------------------------");
		
		
		DaoFormKanriMainteWorkStatus dao = new DaoFormKanriMainteWorkStatus(jdbcTemplate);
		
		
		//------------------------------------------------
		// 一覧表示用にデータを取得
		FormKanriMainteWorkStatusList formKanriMainteWorkStatusList;
		
		formKanriMainteWorkStatusList = dao.getWorkStatusList("", "", "", null,null);
		
		if (formKanriMainteWorkStatusList == null) {
			
			formKanriMainteWorkStatusList = new FormKanriMainteWorkStatusList();
			formKanriMainteWorkStatusList.setMessage("検索処理で異常が発生しました。システム管理者にご連絡ください。");
			log.info("【ERR】" + pgmId + ":検索処理で異常終了");
		
		} else if (formKanriMainteWorkStatusList.getWorkStatusList().size() == 0) {
		
			formKanriMainteWorkStatusList.setMessage("データが0件でした。");
			log.info("【INF】" + pgmId + ":データが0件でした。");
		}
		
		
		
		// ------------------------------------------------
		// 一覧画面で選択・入力したフィルタリング条件を引継ぎ
		
		formKanriMainteWorkStatusList.setFilterHouseId(        formKanriMainteWorkStatusDetail.getFilterHouseId());
		formKanriMainteWorkStatusList.setFilterWorkId(         formKanriMainteWorkStatusDetail.getFilterWorkId());
		formKanriMainteWorkStatusList.setFilterStartEmployeeId(formKanriMainteWorkStatusDetail.getFilterStartEmployeeId());
		formKanriMainteWorkStatusList.setFilterDateFr(         formKanriMainteWorkStatusDetail.getFilterDateFr());
		formKanriMainteWorkStatusList.setFilterDateTo(         formKanriMainteWorkStatusDetail.getFilterDateTo());
		
		
		
		// ------------------------------------------------
		// 絞込み条件欄のドロップダウンリストにセットする値を検索しセット
		
		DaoDropDownList daoDropDown = new DaoDropDownList(jdbcTemplate);
		
		formKanriMainteWorkStatusList.setDropDownHouseList(   daoDropDown.getHouseList());
		formKanriMainteWorkStatusList.setDropDownWorkList(    daoDropDown.getWorkList());
		formKanriMainteWorkStatusList.setDropDownEmployeeList(daoDropDown.getEmployeeList());
		
		
		
		
		mav.addObject("formKanriMainteWorkStatusList",formKanriMainteWorkStatusList);
		
		log.info("【INF】" + pgmId + ":処理終了");
		mav.setViewName("scrKanriMainteWorkStatusList.html");
		return mav;
	}
	
	
	
	
	
	
	
	
	//★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
	//
	//  作業状況確認
	//
	//★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
	
	
	
	//システムの初期処理→作業者一覧画面の表示
	@RequestMapping(value ="/matsuoka/TransitionKanriDispWorkStatus",method = RequestMethod.GET)
	public ModelAndView transition_KanriDispWorkStatus(ModelAndView mav) {
		
		String pgmId = classId + ".transition_KanriDispWorkStatus";
		
		log.info("【INF】" + pgmId + ":処理開始");
		
		// 画面に表示する情報の取得
		DaoFormKanriDispWorkStatus dao = new DaoFormKanriDispWorkStatus(jdbcTemplate);
		FormKanriDispWorkStatus formKanriDispWorkStatus = dao.getDispData();
		
		
		//------------------------------------------------
		//デバッグ用ログ出力
		if (formKanriDispWorkStatus == null) {
			log.info("■■nullだ！！！！");
		}
		if (formKanriDispWorkStatus.getActiveWorkLists() == null) {
			log.info("■□nullだ！！！！");
		}
		for (int index = 0 ; index < formKanriDispWorkStatus.getActiveWorkLists().size() ;index++) {
			if (formKanriDispWorkStatus.getActiveWorkLists().get(index) == null) {
				log.info("□□nullだ！！！！");
			}else{
				log.info("ハウス=[" +  formKanriDispWorkStatus.getActiveWorkLists().get(index).getHouseId() + "]");
			}
		}
		//------------------------------------------------
		
		
		
		mav.addObject("formKanriDispWorkStatus",formKanriDispWorkStatus);
		
		log.info("【INF】" + pgmId + ":処理終了■■■");
		

		mav.setViewName("scrKanriDispWorkStatus.html");
		return mav;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	//★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
	//
	//  収穫状況確認メンテナンス
	//
	//★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
	
	
	// 一覧画面への遷移
	@RequestMapping(value ="/matsuoka/TransitionKanriMainteShukakuSumList",method = RequestMethod.GET)
	public ModelAndView trunsition_KanriMainteShukakuSumList(ModelAndView mav) {

		String pgmId = classId + ".trunsition_KanriMainteShukakuSumList";
		
		log.info("【INF】" + pgmId + ":処理開始");
		
		DaoFormKanriShukakuSum dao = new DaoFormKanriShukakuSum(jdbcTemplate);
		
		//------------------------------------------------
		// 一覧表示用にデータを取得
		FormKanriMainteShukakuSumList formKanriMainteShukakuSumList;
		
		// ※最初は検索条件なしで全て一覧表示するため引数は全て空白かnull
		formKanriMainteShukakuSumList = dao.getShukakuSumList("",  null,null);
		
		if (formKanriMainteShukakuSumList == null) {
			
			formKanriMainteShukakuSumList = new FormKanriMainteShukakuSumList();
			formKanriMainteShukakuSumList.setMessage("検索処理で異常が発生しました。システム管理者にご連絡ください。");
			log.info("【ERR】" + pgmId + ":検索処理で異常終了");
		
		} else if (formKanriMainteShukakuSumList.getShukakuList().size() == 0) {
		
			formKanriMainteShukakuSumList.setMessage("データが0件でした。");
			log.info("【INF】" + pgmId + ":データが0件でした。");
		}
		
		
		// ------------------------------------------------
		// 絞込み条件欄のドロップダウンリストにセットする値を検索しセット
		
		DaoDropDownList daoDropDown = new DaoDropDownList(jdbcTemplate);
		
		formKanriMainteShukakuSumList.setDropDownHouseList(daoDropDown.getHouseList());
		
		
		mav.addObject("formKanriMainteShukakuSumList",formKanriMainteShukakuSumList);
		
		log.info("【INF】" + pgmId + ":処理終了");
		mav.setViewName("scrKanriMainteShukakuSumList.html");
		return mav;
	}
	
	
	
	//詳細画面への遷移
	@RequestMapping(value ="/matsuoka/TransitionKanriMainteShukakuSumDetail",method = RequestMethod.POST)
	public ModelAndView trunsition_KanriMainteShukakuSumDetail(@ModelAttribute FormKanriMainteShukakuSumList formKanriMainteShukakuSumList, ModelAndView mav) {
		
		String pgmId = classId + ".trunsition_KanriMainteShukakuSumDetail";
		
		log.info("【INF】" + pgmId + " :処理開始");
		log.info("【INF】" + pgmId + " :ハウスID    =[" + formKanriMainteShukakuSumList.getSelectHouseId() + "]");
		log.info("【INF】" + pgmId + " :収穫日      =[" + formKanriMainteShukakuSumList.getSelectShukakuDate() + "]");
		log.info("【INF】" + pgmId + " :▼フィルタリング条件------------------------------------------------");
		log.info("【INF】" + pgmId + " :ハウスID    =[" + formKanriMainteShukakuSumList.getFilterHouseId() + "]");
		log.info("【INF】" + pgmId + " :作業開始日Fr=[" + formKanriMainteShukakuSumList.getFilterDateFr() + "]");
		log.info("【INF】" + pgmId + " :作業開始日To=[" + formKanriMainteShukakuSumList.getFilterDateTo() + "]");
		log.info("【INF】" + pgmId + " :▲フィルタリング条件------------------------------------------------");
		
		
		String targetHouseId              = formKanriMainteShukakuSumList.getSelectHouseId();
		LocalDate targetShukakuDate   = formKanriMainteShukakuSumList.getSelectShukakuDate();
		
		// 返却値
		FormKanriMainteShukakuSumDetail formKanriMainteShukakuSumDetail = new FormKanriMainteShukakuSumDetail();
		
		if (targetHouseId.equals("") == true) {
			
			//------------------------------------------------
			//ハウスIDが指定されていない場合(新規登録)：次画面を空表示
			
			// 処理なし
			
		}else{
			//------------------------------------------------
			//ハウスIDが指定されている  場合(更新削除)：収穫合計情報を検索して次画面に表示
			
			DaoFormKanriShukakuSum dao = new DaoFormKanriShukakuSum(jdbcTemplate);
			
			
			//boolean blExistsShukakuSum   = dao.isExistsShukakuSum(  targetHouseId, targetShukakuDate);
			//boolean blExistsShukakuSumQR = dao.isExistsShukakuSumQR(targetHouseId, targetShukakuDate);
			
			
			formKanriMainteShukakuSumDetail = dao.getShukakuSumDetail(targetHouseId, targetShukakuDate);
		}
		
		
		// ------------------------------------------------
		// 一覧画面で選択・入力したフィルタリング条件を引継ぎ
		
		formKanriMainteShukakuSumDetail.setFilterHouseId(    formKanriMainteShukakuSumList.getFilterHouseId());
		formKanriMainteShukakuSumDetail.setFilterDateFr(     formKanriMainteShukakuSumList.getFilterDateFr());
		formKanriMainteShukakuSumDetail.setFilterDateTo(     formKanriMainteShukakuSumList.getFilterDateTo());
		
		
		// ------------------------------------------------
		// 入力欄のドロップダウンリストにセットする値を検索しセット
		
		DaoDropDownList daoDropDown = new DaoDropDownList(jdbcTemplate);
		
		formKanriMainteShukakuSumDetail.setDropDownHouseList( daoDropDown.getHouseList());
		
		
		
		mav.addObject("formKanriMainteShukakuSumDetail",formKanriMainteShukakuSumDetail);
		
		log.info("【INF】" + pgmId + ":処理終了");
		mav.setViewName("scrKanriMainteShukakuSumDetail.html");
		return mav;
	}
	

	//戻るボタン押下処理
	@RequestMapping(value ="/matsuoka/TransitionKanriMainteShukakuSumListSearch",method = RequestMethod.POST)
	public ModelAndView transition_KanriMainteMainteShukakuSumListSearch(@ModelAttribute FormKanriMainteShukakuSumDetail formKanriMainteShukakuSumDetail, ModelAndView mav) {

		String pgmId = classId + ".transition_KanriMainteMainteShukakuSumListSearch";
		
		log.info("【INF】" + pgmId + " :処理開始");
		log.info("【INF】" + pgmId + " :ﾎﾞﾀﾝ区分    =[" + formKanriMainteShukakuSumDetail.getButtonKbn() + "]");
		log.info("【INF】" + pgmId + " :ハウスID    =[" + formKanriMainteShukakuSumDetail.getHouseId() + "]");
		log.info("【INF】" + pgmId + " :収穫日      =[" + formKanriMainteShukakuSumDetail.getShukakuDate() + "]");
		log.info("【INF】" + pgmId + " :▼フィルタリング条件------------------------------------------------");
		log.info("【INF】" + pgmId + " :ハウスID    =[" + formKanriMainteShukakuSumDetail.getFilterHouseId() + "]");
		log.info("【INF】" + pgmId + " :収穫日Fr    =[" + formKanriMainteShukakuSumDetail.getFilterDateFr() + "]");
		log.info("【INF】" + pgmId + " :収穫日To    =[" + formKanriMainteShukakuSumDetail.getFilterDateTo() + "]");
		log.info("【INF】" + pgmId + " :▲フィルタリング条件------------------------------------------------");
		
		
		
		log.info("【INF】" + pgmId + ":処理開始");
		
		DaoFormKanriShukakuSum dao = new DaoFormKanriShukakuSum(jdbcTemplate);
		
		//------------------------------------------------
		// 一覧表示用にデータを取得
		FormKanriMainteShukakuSumList formKanriMainteShukakuSumList;
		
		formKanriMainteShukakuSumList = dao.getShukakuSumList("",  null,null);
		
		if (formKanriMainteShukakuSumList == null) {
			
			formKanriMainteShukakuSumList = new FormKanriMainteShukakuSumList();
			formKanriMainteShukakuSumList.setMessage("検索処理で異常が発生しました。システム管理者にご連絡ください。");
			log.info("【ERR】" + pgmId + ":検索処理で異常終了");
		
		} else if (formKanriMainteShukakuSumList.getShukakuList().size() == 0) {
		
			formKanriMainteShukakuSumList.setMessage("データが0件でした。");
			log.info("【INF】" + pgmId + ":データが0件でした。");
		}
		
		
		
		// ------------------------------------------------
		// 一覧画面で選択・入力したフィルタリング条件を引継ぎ
		
		formKanriMainteShukakuSumList.setFilterHouseId(        formKanriMainteShukakuSumDetail.getFilterHouseId());
		formKanriMainteShukakuSumList.setFilterDateFr(         formKanriMainteShukakuSumDetail.getFilterDateFr());
		formKanriMainteShukakuSumList.setFilterDateTo(         formKanriMainteShukakuSumDetail.getFilterDateTo());
		
		
		// ------------------------------------------------
		// 絞込み条件欄のドロップダウンリストにセットする値を検索しセット
		
		DaoDropDownList daoDropDown = new DaoDropDownList(jdbcTemplate);
		
		formKanriMainteShukakuSumList.setDropDownHouseList(daoDropDown.getHouseList());
		
		
		mav.addObject("formKanriMainteShukakuSumList",formKanriMainteShukakuSumList);
		
		log.info("【INF】" + pgmId + ":処理終了");
		mav.setViewName("scrKanriMainteShukakuSumList.html");
		return mav;
	}
	
	
	
	//登録処理
	@RequestMapping(value ="/matsuoka/TransitionKanriEditShukakuSum",method = RequestMethod.POST)
	public ModelAndView editKanriShukakuSum(@ModelAttribute FormKanriMainteShukakuSumDetail formKanriMainteShukakuSumDetail, ModelAndView mav) {
		
		String pgmId = classId + ".editKanriShukakuSum";
		
		log.info("【INF】" + pgmId + " :処理開始");
		log.info("【INF】" + pgmId + " :ﾎﾞﾀﾝ区分    =[" + formKanriMainteShukakuSumDetail.getButtonKbn() + "]");
		log.info("【INF】" + pgmId + " :ハウスID    =[" + formKanriMainteShukakuSumDetail.getHouseId() + "]");;
		log.info("【INF】" + pgmId + " :作業開始日時=[" + formKanriMainteShukakuSumDetail.getShukakuDate() + "]");
		log.info("【INF】" + pgmId + " :ケース数    =[" + formKanriMainteShukakuSumDetail.getBoxSum() + "]");
		log.info("【INF】" + pgmId + " :備考        =[" + formKanriMainteShukakuSumDetail.getBiko() + "]");
		log.info("【INF】" + pgmId + " :▼フィルタリング条件------------------------------------------------");
		log.info("【INF】" + pgmId + " :ハウスID    =[" + formKanriMainteShukakuSumDetail.getFilterHouseId() + "]");
		log.info("【INF】" + pgmId + " :作業開始日Fr=[" + formKanriMainteShukakuSumDetail.getFilterDateFr() + "]");
		log.info("【INF】" + pgmId + " :作業開始日To=[" + formKanriMainteShukakuSumDetail.getFilterDateTo() + "]");
		log.info("【INF】" + pgmId + " :▲フィルタリング条件------------------------------------------------");
		
		
		DaoFormKanriShukakuSum dao = new DaoFormKanriShukakuSum(jdbcTemplate);
		
		
		//------------------------------------------------
		// 登録・更新・削除
		
		
		if (formKanriMainteShukakuSumDetail.getButtonKbn().equals("regist") == true) {
			
			//------------------------------------------------
			//登録処理を実施
			dao.registShukakuSum(formKanriMainteShukakuSumDetail, SpecialUser.KANRI_USER, "scrKanriMainteShukakuSumDetail");
			
			
		} else if (formKanriMainteShukakuSumDetail.getButtonKbn().equals("update") == true) {
			
			//------------------------------------------------
			//更新処理を実施
			dao.updateShukakuSum(formKanriMainteShukakuSumDetail, SpecialUser.KANRI_USER, "scrKanriMainteShukakuSumDetail");
			
			
		} else if (formKanriMainteShukakuSumDetail.getButtonKbn().equals("delete") == true) {
			
			//------------------------------------------------
			//削除処理を実施(収穫ケース数を0クリア)
			dao.zeroClearShukakuSum(formKanriMainteShukakuSumDetail, SpecialUser.KANRI_USER, "scrKanriMainteShukakuSumDetail");
			
		}
		
		
		//------------------------------------------------
		// 一覧表示用にデータを取得
		FormKanriMainteShukakuSumList formKanriMainteShukakuSumList;
		
		formKanriMainteShukakuSumList = dao.getShukakuSumList("", null,null);
		
		if (formKanriMainteShukakuSumList == null) {
			
			formKanriMainteShukakuSumList = new FormKanriMainteShukakuSumList();
			formKanriMainteShukakuSumList.setMessage("検索処理で異常が発生しました。システム管理者にご連絡ください。");
			log.info("【ERR】" + pgmId + ":検索処理で異常終了");
		
		} else if (formKanriMainteShukakuSumList.getShukakuList().size() == 0) {
		
			formKanriMainteShukakuSumList.setMessage("データが0件でした。");
			log.info("【INF】" + pgmId + ":データが0件でした。");
		}

		
		
		// ------------------------------------------------
		// 一覧画面で選択・入力したフィルタリング条件を引継ぎ
		
		formKanriMainteShukakuSumList.setFilterHouseId(        formKanriMainteShukakuSumDetail.getFilterHouseId());
		formKanriMainteShukakuSumList.setFilterDateFr(         formKanriMainteShukakuSumDetail.getFilterDateFr());
		formKanriMainteShukakuSumList.setFilterDateTo(         formKanriMainteShukakuSumDetail.getFilterDateTo());
		
		
		// ------------------------------------------------
		// 絞込み条件欄のドロップダウンリストにセットする値を検索しセット
		
		DaoDropDownList daoDropDown = new DaoDropDownList(jdbcTemplate);
		
		formKanriMainteShukakuSumList.setDropDownHouseList(   daoDropDown.getHouseList());
		
		
		mav.addObject("formKanriMainteShukakuSumList",formKanriMainteShukakuSumList);
		
		log.info("【INF】" + pgmId + ":処理終了");
		mav.setViewName("scrKanriMainteShukakuSumList.html");
		return mav;
	}
	
}
