
/*
JQueryで各種イベント操作

*/
$(function() {
	
	
	
	// □□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□
	// ユーザ操作に関するイベント
	
	
	//------------------------------------------------
	// 強制リセット押下イベント
	//
	$('.ymd-red-button a[name$="reset"]').click(function() {
		
		//alert("変更");
		//console.log("■" + $(this).closest('.ymd-list-detail-container[name$="detail-parent"]').find('input[name$="houseId"]').val());
		
		
		// 権限がない場合は作業情報の編集は不可 ★管理者権限がないとボタンが表示されないためこの制御は不要
		//let editAuthority           = document.getElementById("editAuthority").value;
		//
		//if (editAuthority == "false") {
		//	return;
		//}
		
		// 送信情報をhiddenn領域にセット
		document.getElementById("selectHouseId").value = $(this).closest('.ymd-red-button').find('input[name$="houseId"]').val();
		document.getElementById("selectWorkId").value  = $(this).closest('.ymd-red-button').find('input[name$="workId"]').val();
		
		$('#ymd-confirmOverlay').fadeIn(150); // 確認ダイアログ表示
		
		
		/*
		// hidden項目に選択した行の情報を、新規フォームにセットし送信
		let loginEmployeeId    = document.getElementById("loginEmployeeId").value;
		let loginEmployeeName  = document.getElementById("loginEmployeeName").value;
		let selectedDeviceLabel= document.getElementById("selectedDeviceLabel").value;
		let selectHouseId         = $(this).closest('.ymd-list-detail-container[name$="detail-workStatus-parent"]').find('input[name$="houseId"]').val();
		let selectWorkId          = $(this).closest('.ymd-list-detail-container[name$="detail-workStatus-parent"]').find('input[name$="workId"]').val();
		
		
		// 新しいフォームを一時的に作成
		const form = document.createElement("form");
		form.method = "POST";
		form.action = "/matsuoka/DispWorkStatusMobileExecReset";
		
		// 送信したい hidden 要素だけ追加
		const params = {
			loginEmployeeId,
			loginEmployeeName,
			selectedDeviceLabel,
			selectHouseId,
			selectWorkId
		};
		
		for (const key in params) {
			const input = document.createElement("input");
			input.type = "hidden";
			input.name = key;
			input.value = params[key];
			form.appendChild(input);
		}
		
		// フォームをbodyに追加して送信
		document.body.appendChild(form);
		form.submit();
		
		*/
	});
	
		// 強制リセット確認ダイアログ「はい」押下時
	$('#btnYes').click(function() {
		
		$('#ymd-confirmOverlay').fadeOut(150); // ダイアログを閉じる
		
		
		
		// hidden項目に選択した行の情報を、新規フォームにセットし送信
		let loginEmployeeId       = document.getElementById("loginEmployeeId").value;
		let loginEmployeeName     = document.getElementById("loginEmployeeName").value;
		let selectedDeviceLabel   = document.getElementById("selectedDeviceLabel").value;
		let selectHouseId         = document.getElementById("selectHouseId").value
		let selectWorkId          = document.getElementById("selectWorkId").value
		
		
		// 新しいフォームを一時的に作成
		const form = document.createElement("form");
		form.method = "POST";
		form.action = "/matsuoka/DispWorkStatusMobileExecReset";
		
		// 送信したい hidden 要素だけ追加
		const params = {
			loginEmployeeId,
			loginEmployeeName,
			selectedDeviceLabel,
			selectHouseId,
			selectWorkId
		};
		
		for (const key in params) {
			const input = document.createElement("input");
			input.type = "hidden";
			input.name = key;
			input.value = params[key];
			form.appendChild(input);
		}
		
		// フォームをbodyに追加して送信
		document.body.appendChild(form);
		form.submit();
		
		
		
	});
	
	// 強制リセット確認ダイアログ「いいえ」押下時
	$('#btnNo').click(function() {
		$('#ymd-confirmOverlay').fadeOut(150); // ダイアログを閉じる
		
		// hidden領域をクリア
		document.getElementById("selectHouseId").value = "";
		document.getElementById("selectWorkId").value  = "";
		
	});
	
	
	
	
	//------------------------------------------------
	// 一覧の特定行押下イベント ※稼働中の作業
	//
	$('.ymd-list-detail-container[name$="detail-workStatus-row"]').click(function() {
		
		//alert("変更");
		//console.log("■" + $(this).closest('.ymd-list-detail-container[name$="detail-parent"]').find('input[name$="houseId"]').val());
		
		
		// 権限がない場合は作業情報の編集は不可
		let editAuthority           = document.getElementById("editAuthority").value;
		
		if (editAuthority == "false") {
			return;
		}
		
		
		
		// hidden項目に選択した行の情報をセットしフォームをPOST送信
		let selectHouseId         = $(this).closest('.ymd-list-detail-container[name$="detail-workStatus-parent"]').find('input[name$="houseId"]').val();
		let selectWorkId          = $(this).closest('.ymd-list-detail-container[name$="detail-workStatus-parent"]').find('input[name$="workId"]').val();
		let selectColNo           = $(this).closest('.ymd-list-detail-container[name$="detail-workStatus-parent"]').find('input[name$="colNo"]').val();
		//let selectStartEmployeeId = $(this).closest('.ymd-list-detail-container[name$="detail-workStatus-parent"]').find('input[name$="startEmployeeId"]').val();
		//let selectStartDateTime   = $(this).closest('.ymd-list-detail-container[name$="detail-workStatus-parent"]').find('input[name$="startDateTime"]').val();
		
		// QRコードの情報を組み立てる 例：MatsuokaQRData,10001,01,1,1000001
		document.getElementById("qrcode").value   = "MatsuokaQRData," + selectHouseId + "," + selectColNo + ",1," + selectWorkId;
		document.getElementById("scrName").value  = "scrDispWorkStatusMobile";
		
		//alert("QRコード＝" + document.getElementById("qrcode").value);
		
		//画面内にformタグは１つしかないため０番目を固定で取得
		let form = document.getElementsByTagName('form')[0];
		
		// QRコードを読み取った後の画面に遷移する
		form.action="/matsuoka/TransitionDispQRInfo";
    	form.method="post";
    	form.submit();
	});
	
	
	//------------------------------------------------
	// 一覧の特定行押下イベント ※全ハウスの収穫状況
	//
	$('.ymd-list-detail-container[name$="detail-shukakuSum-row"]').click(function() {
		
		//alert("変更");
		//console.log("■" + $(this).closest('.ymd-list-detail-container[name$="detail-parent"]').find('input[name$="houseId"]').val());
		
		
		// 権限がない場合は作業情報の編集は不可
		let editAuthority        = document.getElementById("editAuthority").value;
		
		if (editAuthority == "false") {
			return;
		}
		
		
		
		// hidden項目に選択した行の情報をセットしフォームをPOST送信
		let selectHouseId         = $(this).closest('.ymd-list-detail-container[name$="detail-shukakuSum-parent"]').find('input[name$="houseId"]').val();
		let selectWorkId          = "1000010"; // 作業は"収穫" 固定
		let selectColNo           = "01";      // 列Noは"01"   固定
		
		// QRコードの情報を組み立てる 例：MatsuokaQRData,10001,01,1,1000010
		document.getElementById("qrcode").value   = "MatsuokaQRData," + selectHouseId + "," + selectColNo + ",1," + selectWorkId;
		document.getElementById("scrName").value  = "scrDispWorkStatusMobile";
		
		//alert("QRコード＝" + document.getElementById("qrcode").value);
		
		//画面内にformタグは１つしかないため０番目を固定で取得
		let form = document.getElementsByTagName('form')[0];
		
		// QRコードを読み取った後の画面に遷移する
		form.action="/matsuoka/TransitionDispQRInfo";
    	form.method="post";
    	form.submit();
	});
	
	
	
	//------------------------------------------------
	// 閉じるボタン押下イベント
	//
	
	$('.ymd-button a[name$="close"]').click(function() {
		
		//------------------------------------------------
		
		//画面内にformタグは１つしかないため０番目を固定で取得
		let form = document.getElementsByTagName('form')[0];
		
		//form.action="";     // HTML内で直接記載されているためココでのセットは不要
    	//form.method="post"; // HTML内で直接記載されているためココでのセットは不要
    	form.submit();
	});
	
	
});
