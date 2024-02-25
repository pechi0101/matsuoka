package com.example.Controller;

import java.time.Duration;
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

import com.example.DAO.DaoFormDispQRInfoButton;
import com.example.DAO.DaoFormDispWorkStatus;
import com.example.DAO.DaoFormIndexQR;
import com.example.DAO.DaoHouse;
import com.example.DAO.DaoHouseWorkStatus;
import com.example.DAO.DaoWork;
import com.example.counst.ButtonKbn;
import com.example.counst.SpecialWork;
import com.example.entity.HouseWorkStatus;
import com.example.form.FormDispQRInfo;
import com.example.form.FormDispQRInfoButton;
import com.example.form.FormDispWorkStatus;
import com.example.form.FormIndexKanri;
import com.example.form.FormIndexQR;
import com.example.form.FormReadQRCode;
import com.example.form.FormReadQRStart;
import com.example.form.FormSelectQRReadDevice;
import com.example.form.FormSelectWorkStatusDate;
import com.example.form.FormSelectWorkStatusDateButton;

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
	
	
	// 表示作業期間選択画面への遷移
	@RequestMapping(value ="/matsuoka/TransitionSelectWorkStatusDate",method = RequestMethod.POST)
	public ModelAndView trunsition_SelectWorkStatusDate(@ModelAttribute FormIndexKanri formIndexKanri, ModelAndView mav) {

		String pgmId = classId + ".trunsition_SelectWorkStatusDate";
		
		log.info("【INF】" + pgmId + ":処理開始 社員ID=[" + formIndexKanri.getLoginEmployeeId() + "]、社員名=[" + formIndexKanri.getLoginEmployeeName() + "]");
		
		
		FormSelectWorkStatusDate formSelectWorkStatusDate = new FormSelectWorkStatusDate();
		
		formSelectWorkStatusDate.setLoginEmployeeId(formIndexKanri.getLoginEmployeeId());
		formSelectWorkStatusDate.setLoginEmployeeName(formIndexKanri.getLoginEmployeeName());
		
		// 表示期間の初期化
		formSelectWorkStatusDate.setSelectIntervalDate(0);
		
		// ボタンリストのセット
		ArrayList<FormSelectWorkStatusDateButton> buttonList = new ArrayList<FormSelectWorkStatusDateButton>();

		FormSelectWorkStatusDateButton btn000 = new FormSelectWorkStatusDateButton();
		FormSelectWorkStatusDateButton btn001 = new FormSelectWorkStatusDateButton();
		FormSelectWorkStatusDateButton btn002 = new FormSelectWorkStatusDateButton();
		FormSelectWorkStatusDateButton btn003 = new FormSelectWorkStatusDateButton();
		FormSelectWorkStatusDateButton btn007 = new FormSelectWorkStatusDateButton();
		FormSelectWorkStatusDateButton btn014 = new FormSelectWorkStatusDateButton();
		FormSelectWorkStatusDateButton btn030 = new FormSelectWorkStatusDateButton();

		btn000.setButtonData(0);
		btn001.setButtonData(1);
		btn002.setButtonData(2);
		btn003.setButtonData(3);
		btn007.setButtonData(7);
		btn014.setButtonData(14);
		btn030.setButtonData(30);
		
		buttonList.add(btn000); // 当日分
		buttonList.add(btn001); // 過去1日分
		buttonList.add(btn002); // 過去2日分
		buttonList.add(btn003); // 過去3日分
		buttonList.add(btn007); // 過去１週間分
		buttonList.add(btn014); // 過去２週間分
		buttonList.add(btn030); // 過去１か月分
		formSelectWorkStatusDate.setButtonList(buttonList);
		
		mav.addObject("formSelectWorkStatusDate", formSelectWorkStatusDate);
		
		
		log.info("【INF】" + pgmId + ":処理終了");
		mav.setViewName("scrSelectWorkStatusDate.html");
		return mav;
	}

	
	// 表示作業期間選択画面への遷移
	@RequestMapping(value ="/matsuoka/TransitionDispWorkStatus",method = RequestMethod.POST)
	public ModelAndView trunsition_DispWorkStatus(@ModelAttribute FormSelectWorkStatusDate formSelectWorkStatusDate, ModelAndView mav) {

		String pgmId = classId + ".trunsition_DispWorkStatus";
		
		log.info("【INF】" + pgmId + ":処理開始 社員ID=[" + formSelectWorkStatusDate.getLoginEmployeeId() + "]、社員名=[" + formSelectWorkStatusDate.getLoginEmployeeName() + "]");
		
		
		//------------------------------------------------
		//表示データを検索
		
		DaoFormDispWorkStatus dao = new DaoFormDispWorkStatus(jdbcTemplate);
		FormDispWorkStatus formDispWorkStatus = dao.getDispData(formSelectWorkStatusDate.getSelectIntervalDate());
		

		//------------------------------------------------
		//ログインユーザ情報のセット
		
		formDispWorkStatus.setLoginEmployeeId(formSelectWorkStatusDate.getLoginEmployeeId());
		formDispWorkStatus.setLoginEmployeeName(formSelectWorkStatusDate.getLoginEmployeeName());
		
		
		mav.addObject("formDispWorkStatus", formDispWorkStatus);
		
		
		log.info("【INF】" + pgmId + ":処理終了");
		mav.setViewName("scrDispWorkStatus.html");
		return mav;
	}
	

	// 表示作業期間選択画面への遷移
	@RequestMapping(value ="/matsuoka/Dummy",method = RequestMethod.POST)
	public ModelAndView trunsition_Dummy(@ModelAttribute FormSelectWorkStatusDate formSelectWorkStatusDate, ModelAndView mav) {
		
		return mav;
	}
	
	
	
	
	
	
	
	
	
	
	
	
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
			
			String strWorkId = qrDataList[4];
			String strHouseId = qrDataList[1];
			
			
			// ------------------------------------------------
			// ＱＲコードにセットされている情報をセット
			
			formReadQRStart.setLoginEmployeeId(formReadQRCode.getLoginEmployeeId());
			formReadQRStart.setLoginEmployeeName(formReadQRCode.getLoginEmployeeName());
			formReadQRStart.setSelectedDeviceLabel(formReadQRCode.getSelectedDeviceLabel());
			formReadQRStart.setMessage("");
			
			// ------------------------------------------------
			// 作業状況マスタを検索し、今のＱＲコード読取りが「作業開始」なのか
			// それとも「作業終了(または中断)」であるのかを判定する
			
			
			// ＱＲコードから取得した「作業ID、ハウスID、作業開始従業員ID」でハウス作業進捗テーブルを検索。
			// ※収穫であるため
			DaoHouseWorkStatus daoHouseWorkStatus = new DaoHouseWorkStatus(jdbcTemplate);
			HouseWorkStatus houseWorkStatus =  daoHouseWorkStatus.getLatestWorkStatusForShukaku(strWorkId, strHouseId, formReadQRStart.getLoginEmployeeId());
			
			log.info("【DBG】" + pgmId + ":最新の作業状況=[" + houseWorkStatus.getWorkStatus() + "]");
			
			// 取得・判定した作業状況が「エラー」である場合は異常終了
			if (houseWorkStatus.getWorkStatus() == DaoHouseWorkStatus.STATUS_ERROR) {
				log.error("【ERR】" + pgmId + " :作業状況の取得処理で異常が発生しました。作業ID=[" + strWorkId + "]");
				formReadQRStart.setMessage("【エラー】ＱＲコード読込処理で異常が発生しました（作業状況取得エラー）。もう一度ＱＲコードの読み取り行うかシステム担当者にご連絡ください。");
				
				mav.addObject("formReadQRStart", formReadQRStart);
				log.info("【INF】" + pgmId + ":処理終了");
				mav.setViewName("scrReadQRStart.html");
				return mav;
			}
			
			
			// ------------------------------------------------
			// 最新のデータが「作業未実施」「作業完了」状態である場合、「作業開始」データの登録
			if (houseWorkStatus.getWorkStatus() == DaoHouseWorkStatus.STATUS_NOT
			||  houseWorkStatus.getWorkStatus() == DaoHouseWorkStatus.STATUS_DONE) {
				
				HouseWorkStatus registHouseWorkStatus = new HouseWorkStatus();
				boolean ret = false;
				
				registHouseWorkStatus.setWorkId(strWorkId);
				registHouseWorkStatus.setHouseId(strHouseId);
				registHouseWorkStatus.setColNo("XX");
				registHouseWorkStatus.setPercent(0);
				registHouseWorkStatus.setBiko("");
				
				registHouseWorkStatus.setStartEmployeeId(formReadQRCode.getLoginEmployeeId());
				registHouseWorkStatus.setStartDateTime(LocalDateTime.now());
				//registHouseWorkStatus.setEndEmployeeId();  // ”作業開始”なので終了社員IDはセット不要
				//registHouseWorkStatus.setEndDateTime();    // ”作業開始”なので終了日時  はセット不要
				
				ret = daoHouseWorkStatus.registStartStatus(registHouseWorkStatus, formReadQRCode.getLoginEmployeeId(), "scrReadQRCode");
				
				// 取得・判定した作業状況が「エラー」である場合は異常終了
				if (ret == false) {
					log.error("【ERR】" + pgmId + " :作業状況の登録処理で異常が発生しました。");
					formReadQRStart.setMessage("【エラー】ＱＲコード読込処理で異常が発生しました（作業状況登録エラー）。もう一度ＱＲコードの読み取り行うかシステム担当者にご連絡ください。");
					
					mav.addObject("formReadQRStart", formReadQRStart);
					log.info("【INF】" + pgmId + ":処理終了");
					mav.setViewName("scrReadQRStart.html");
					return mav;
				}
				
				formReadQRStart.setMessage("【 収穫作業 】作業開始で登録しました。");
			}
			
			
			// ------------------------------------------------
			// 最新のデータが「作業中」状態である場合、「作業完了」にデータ更新
			if (houseWorkStatus.getWorkStatus() == DaoHouseWorkStatus.STATUS_WORKING) {
				
				HouseWorkStatus updateHouseWorkStatus = new HouseWorkStatus();
				boolean ret = false;
				
				updateHouseWorkStatus.setWorkId(houseWorkStatus.getWorkId());
				updateHouseWorkStatus.setHouseId(houseWorkStatus.getHouseId());
				updateHouseWorkStatus.setColNo("XX");
				updateHouseWorkStatus.setPercent(100);
				updateHouseWorkStatus.setBiko("");
				
				updateHouseWorkStatus.setStartEmployeeId(houseWorkStatus.getStartEmployeeId());
				updateHouseWorkStatus.setStartDateTime(houseWorkStatus.getStartDateTime());
				updateHouseWorkStatus.setEndEmployeeId(formReadQRCode.getLoginEmployeeId());
				updateHouseWorkStatus.setEndDateTime(LocalDateTime.now());
				
				ret = daoHouseWorkStatus.updateEndStatus(updateHouseWorkStatus, formReadQRCode.getLoginEmployeeId(), "scrReadQRCode");
				
				// 取得・判定した作業状況が「エラー」である場合は異常終了
				if (ret == false) {
					log.error("【ERR】" + pgmId + " :作業状況の更新処理で異常が発生しました。");
					formReadQRStart.setMessage("【エラー】ＱＲコード読込処理で異常が発生しました（作業状況更新エラー）。もう一度ＱＲコードの読み取り行うかシステム担当者にご連絡ください。");
					
					mav.addObject("formReadQRStart", formReadQRStart);
					log.info("【INF】" + pgmId + ":処理終了");
					mav.setViewName("scrReadQRStart.html");
					return mav;
				}
				
				
				formReadQRStart.setMessage("【 収穫作業 】作業完了(100%)で登録しました。");
			}
			
			
			mav.addObject("formReadQRStart", formReadQRStart);
			log.info("【INF】" + pgmId + ":処理終了");
			mav.setViewName("scrReadQRStart.html");
			// ★★★★★★★★★★★★★★★★★★★★★★★★★
			//
			//          収穫である場合はココで処理終了
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
		
		
		
		// キャンセルである場合は登録処理を行わない
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
}
