

// □□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□
// 共通関数


//------------------------------------------------
// 行を表示するか否かの判定
//

function showOrHideRow() {
	
	
	//------------------------------------------------
	// フィルタリング条件の各値を取得
	
	var filterHouseId    = $("#dropDownHouse").val();
	var filterWorkId     = $("#dropDownWork").val();
	var filterEmployeeId = $("#dropDownEmployee").val();
	
	// 【メモ】日付は「2024-06-12」の形式で取得できるみたい
	// 【メモ】日付が初期状態(yyyy/mm/ddと画面に表示されてる状態)である場合は空白("")が取得できる
	var filterDateFr     = $("#searchDateFr").val();
	var filterDateTo     = $("#searchDateTo").val();
	
	filterDateFr = filterDateFr.replace(/-/g,"");//「2024-06-12」のハイフンを正規表現で全て除去しとく
	filterDateTo = filterDateTo.replace(/-/g,"");//「2024-06-12」のハイフンを正規表現で全て除去しとく
	
	var chechedVal = $("#checkBoxDispAllData").prop("checked");
	
	
	
	console.log("□選択されたハウスID=[" + filterHouseId + "]");
	console.log("□選択された作業ID  =[" + filterWorkId + "]");
	console.log("□選択された社員ID  =[" + filterEmployeeId + "]");
	console.log("□選択された日付From=[" + filterDateFr + "]");
	console.log("□選択された日付To  =[" + filterDateTo + "]");
	console.log("□削除行表示？      =[" + chechedVal + "]");
	
	
	
	//------------------------------------------------
	// 一覧の行を取得
	var rows = $('[name="detail-row"]');
	
	console.log("□□□LOOP開始------------------------------------------------");
	
	// 各行をループ処理
	rows.each(function() {
		
		// 行内のハウスIDを取得
		var houseId            = $(this).closest('.ymd-list-detail-container[name$="detail-parent"]').find('[name$="houseId"]').val();
		// 行内の作業IDを取得
		var workId             = $(this).closest('.ymd-list-detail-container[name$="detail-parent"]').find('[name$="workId"]').val();
		// 行内の社員IDを取得
		var startEmployeeId    = $(this).closest('.ymd-list-detail-container[name$="detail-parent"]').find('[name$="startEmployeeId"]').val();
		// 行内の開始日Fromを取得
		var startDate          = $(this).closest('.ymd-list-detail-container[name$="detail-parent"]').find('[name$="startDate"]').val();
		startDate = startDate.replace(/\//g,"");//「2024/06/12」のスラッシュを正規表現で全て除去しとく
		
		var deleteFlg = $(this).closest('.ymd-list-detail-container[name$="detail-parent"]').find('[name$="deleteFlg"]').val();
		
		
		console.log("□ハウスID  =[" + houseId + "]");
		console.log("□作業ID    =[" + workId + "]");
		console.log("□社員ID    =[" + startEmployeeId + "]");
		console.log("□日付From  =[" + startDate + "]");
		console.log("□削除フラグ=[" + deleteFlg + "]");
		
		
		//★★★★★★★★★★★★★★★★★★
		// 結果その行を表示するか否かのフラグ
		var showFld = true;
		//★★★★★★★★★★★★★★★★★★
		
		
		//★以下で「非表示」の条件に当てはまるか否かを判定★
		
		
		//------------------------------------------------
		//フィルタリング条件が入力されている時、フィルタリング条件に当てはまらない場合非表示
		
		//ハウスID
		if (filterHouseId    != "" && houseId         != filterHouseId) {
			showFld = false;
		}
		//作業ID
		if (filterWorkId     != "" && workId          != filterWorkId) {
			showFld = false;
		}
		//社員ID
		if (filterEmployeeId != "" && startEmployeeId != filterEmployeeId) {
			showFld = false;
		}
		
		
		//開始日From～To両方とも入力されてる場合→開始日がその範囲外である行は非表示にする
		if (filterDateFr     != "" && filterDateTo  != "") {
			if (startDate      < filterDateFr
			||  filterDateTo < startDate) {
				showFld = false;
			}
		}
		//開始日From        のみ入力されてる場合→開始日がそれより小さい行は非表示にする
		if (filterDateFr     != "" && filterDateTo  == "") {
			if (startDate      < filterDateFr) {
				showFld = false;
			}
		}
		//開始日To          のみ入力されてる場合→開始日がそれより大きい行は非表示にする
		if (filterDateFr     == "" && filterDateTo  != "") {
			if (filterDateTo < startDate) {
				showFld = false;
			}
		}
		
		
		//------------------------------------------------
		//「削除した行は表示しない」と 「削除済み」の作業か否かで非表示判定
		
		
		//「削除した行は表示しない」and 「削除済み」 →その行は非表示にする
		if (chechedVal == false  && deleteFlg === "true") {
			showFld = false;
		}
		
		
		//------------------------------------------------
		//結果判定
		
		if (showFld == true) {
			// 行を表示する
			$(this).closest('.ymd-list-detail-container[name$="detail-parent"]').show();
		} else {
			// 行を非表示にする
			$(this).closest('.ymd-list-detail-container[name$="detail-parent"]').hide();
		}
		
	});
	
}



// □□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□
// 初期表示処理イベント

/*
削除された作業はグレー表示する
*/
$(document).ready(function() {
	// 一覧の行を取得
	var rows = $('[name="detail-row"]');
	
	console.log("□□□LOOP開始------------------------------------------------");
	
	//------------------------------------------------
	// 各行をループ処理して削除された行である場合、背景色をグレーにする
	rows.each(function() {
		// 行内のdeleteFlgを取得
		var deleteFlg = $(this).closest('.ymd-list-detail-container[name$="detail-parent"]').find('[name$="deleteFlg"]').val();
		//console.log("□削除フラグ=[" + deleteFlg + "]");
		if (deleteFlg === "true") {
			// 行の背景色をグレーにする
			$(this).removeClass().addClass('ymd-list-detail-container-deleted');
		}
	});
	
	//------------------------------------------------
	//上で定義した自作の共通関数で行の表示・非表示を切り替える
	showOrHideRow();
	
 });




/*
JQueryで各種イベント操作

*/
$(function() {
	
	
	
	
	// □□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□
	// フィルタリング条件入力エリアに関するイベント
	
	
	//------------------------------------------------
	// 「ハウス」のドロップダウンが変更されたとき
	//
	$('#dropDownHouse').change(function(){
		
		//上で定義した自作の共通関数で行の表示・非表示を切り替える
		showOrHideRow();
	});
	
	//------------------------------------------------
	// 「作業」のドロップダウンが変更されたとき
	//
	$('#dropDownWork').change(function(){
		
		//上で定義した自作の共通関数で行の表示・非表示を切り替える
		showOrHideRow();
	});
	
	//------------------------------------------------
	// 「社員」のドロップダウンが変更されたとき
	//
	$('#dropDownEmployee').change(function(){
		
		//上で定義した自作の共通関数で行の表示・非表示を切り替える
		showOrHideRow();
	});
	
	//------------------------------------------------
	// 「日付From」の入力が変更されたとき
	//
	$("#searchDateFr").change(function(){
		
		//上で定義した自作の共通関数で行の表示・非表示を切り替える
		showOrHideRow();
	});
	
	//------------------------------------------------
	// 「日付To」  の入力が変更されたとき
	//
	$("#searchDateTo").change(function(){
		
		//上で定義した自作の共通関数で行の表示・非表示を切り替える
		showOrHideRow();
	});
	
	
	
	
	
	// □□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□
	// 新規登録ボタン・削除作業表示チェックボックス表示エリアに関するイベント
	
	
	//------------------------------------------------
	// 「削除した行も表示する」のチェックが変更されたとき
	//
	
	$('#checkBoxDispAllData').change(function(){
		
		//上で定義した自作の共通関数で行の表示・非表示を切り替える
		showOrHideRow();
		
	});
	
	
	//------------------------------------------------
	// 新規追加ボタン押下イベント
	//
	
	$('.ymd-kanri-button a[name$="new"]').click(function() {
		
		
		//alert("新規");
		//console.log("■" + $(this).closest('div[name="houseData"]').find('input[name$="houseId"]').val());
		
		
		
		
		//------------------------------------------------
		// hidden項目に「選択したフィルタリング条件の情報」をセット
		document.getElementById("filterHouseId").value         = $("#dropDownHouse").val();
		document.getElementById("filterWorkId").value          = $("#dropDownWork").val();
		document.getElementById("filterStartEmployeeId").value = $("#dropDownEmployee").val();
		// 【メモ】日付は「2024-06-12」の形式で取得できるみたい
		// 【メモ】日付が初期状態(yyyy/mm/ddと画面に表示されてる状態)である場合は空白("")が取得できる
		document.getElementById("filterDateFr").value          = $("#searchDateFr").val();
		document.getElementById("filterDateTo").value          = $("#searchDateTo").val();
		
		
		//------------------------------------------------
		// hidden項目に「選択した一覧の情報」をセット
		document.getElementById("selectHouseId").value         = "";
		document.getElementById("selectColNo").value           = "";
		document.getElementById("selectWorkId").value          = "";
		document.getElementById("selectStartDateTime").value   = "";
		
		
		
		// ------------------------------------------------
		// hidden項目に設定した情報を「新規」で作成したフォームにセットし送信(submit)
		//
		// ※既存のformをそのままsubmitしてしまうと、画面に一覧表示されてる全ての情報をサーバに送信してしまう。
		//   そのためsubmit用に必要最低限の情報を新規で作成したformにセットし、それをsubmitする。
		//
		
		let filterHouseId          = document.getElementById("filterHouseId").value;
		let filterWorkId           = document.getElementById("filterWorkId").value;
		let filterStartEmployeeId  = document.getElementById("filterStartEmployeeId").value
		let filterDateFr           = document.getElementById("filterDateFr").value;
		let filterDateTo           = document.getElementById("filterDateTo").value;
		let selectHouseId          = document.getElementById("selectHouseId").value;
		let selectColNo            = document.getElementById("selectColNo").value;
		let selectWorkId           = document.getElementById("selectWorkId").value;
		let selectStartDateTime    = document.getElementById("selectStartDateTime").value;
		
		
		// 新しいフォームを一時的に作成
		const form = document.createElement("form");
		form.method = "POST";
		form.action = "/matsuoka/TransitionKanriMainteWorkStatusDetail";
		
		// 送信したい hidden 要素だけ追加
		const params = {
			 filterHouseId
			,filterWorkId
			,filterStartEmployeeId
			,filterDateFr
			,filterDateTo
			,selectHouseId
			,selectColNo
			,selectWorkId
			,selectStartDateTime
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
	
	
	// □□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□
	// 一覧表に関するイベント
	
	
	//------------------------------------------------
	// 一覧の特定行押下イベント(通常行)
	//
	
	$('.ymd-list-detail-container[name$="detail-row"]').click(function() {
		
		//alert("変更");
		//console.log("■" + $(this).closest('.ymd-list-detail-container[name$="detail-parent"]').find('input[name$="houseId"]').val());
		
		
		//------------------------------------------------
		// hidden項目に「選択したフィルタリング条件の情報」をセット
		document.getElementById("filterHouseId").value         = $("#dropDownHouse").val();
		document.getElementById("filterWorkId").value          = $("#dropDownWork").val();
		document.getElementById("filterStartEmployeeId").value = $("#dropDownEmployee").val();
		// 【メモ】日付は「2024-06-12」の形式で取得できるみたい
		// 【メモ】日付が初期状態(yyyy/mm/ddと画面に表示されてる状態)である場合は空白("")が取得できる
		document.getElementById("filterDateFr").value          = $("#searchDateFr").val();
		document.getElementById("filterDateTo").value          = $("#searchDateTo").val();
		
		
		//------------------------------------------------
		// hidden項目に「選択した一覧の情報」をセット
		document.getElementById("selectHouseId").value         = $(this).closest('.ymd-list-detail-container[name$="detail-parent"]').find('input[name$="houseId"]').val();
		document.getElementById("selectColNo").value           = $(this).closest('.ymd-list-detail-container[name$="detail-parent"]').find('input[name$="colNo"]').val();
		document.getElementById("selectWorkId").value          = $(this).closest('.ymd-list-detail-container[name$="detail-parent"]').find('input[name$="workId"]').val();
		document.getElementById("selectStartDateTime").value   = $(this).closest('.ymd-list-detail-container[name$="detail-parent"]').find('input[name$="startDateTime"]').val();
		
		
		// ------------------------------------------------
		// hidden項目に設定した情報を「新規」で作成したフォームにセットし送信(submit)
		//
		// ※既存のformをそのままsubmitしてしまうと、画面に一覧表示されてる全ての情報をサーバに送信してしまう。
		//   そのためsubmit用に必要最低限の情報を新規で作成したformにセットし、それをsubmitする。
		//
		
		let filterHouseId          = document.getElementById("filterHouseId").value;
		let filterWorkId           = document.getElementById("filterWorkId").value;
		let filterStartEmployeeId  = document.getElementById("filterStartEmployeeId").value
		let filterDateFr           = document.getElementById("filterDateFr").value;
		let filterDateTo           = document.getElementById("filterDateTo").value;
		let selectHouseId          = document.getElementById("selectHouseId").value;
		let selectColNo            = document.getElementById("selectColNo").value;
		let selectWorkId           = document.getElementById("selectWorkId").value;
		let selectStartDateTime    = document.getElementById("selectStartDateTime").value;
		
		
		// 新しいフォームを一時的に作成
		const form = document.createElement("form");
		form.method = "POST";
		form.action = "/matsuoka/TransitionKanriMainteWorkStatusDetail";
		
		// 送信したい hidden 要素だけ追加
		const params = {
			 filterHouseId
			,filterWorkId
			,filterStartEmployeeId
			,filterDateFr
			,filterDateTo
			,selectHouseId
			,selectColNo
			,selectWorkId
			,selectStartDateTime
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
	
	//------------------------------------------------
	// 一覧の特定行押下イベント(削除行)
	//
	
	$('.ymd-list-detail-container-deleted[name$="detail-row"]').click(function() {
		
		//alert("変更");
		//console.log("■" + $(this).closest('.ymd-list-detail-container-deleted[name$="detail-parent"]').find('input[name$="houseId"]').val());
		
		
		//------------------------------------------------
		// hidden項目に「選択したフィルタリング条件の情報」をセット
		document.getElementById("filterHouseId").value         = $("#dropDownHouse").val();
		document.getElementById("filterWorkId").value          = $("#dropDownWork").val();
		document.getElementById("filterStartEmployeeId").value = $("#dropDownEmployee").val();
		// 【メモ】日付は「2024-06-12」の形式で取得できるみたい
		// 【メモ】日付が初期状態(yyyy/mm/ddと画面に表示されてる状態)である場合は空白("")が取得できる
		document.getElementById("filterDateFr").value          = $("#searchDateFr").val();
		document.getElementById("filterDateTo").value          = $("#searchDateTo").val();
		
		
		//------------------------------------------------
		// hidden項目に「選択した一覧の情報」をセット
		document.getElementById("selectHouseId").value         = $(this).closest('.ymd-list-detail-container[name$="detail-parent"]').find('input[name$="houseId"]').val();
		document.getElementById("selectColNo").value           = $(this).closest('.ymd-list-detail-container[name$="detail-parent"]').find('input[name$="colNo"]').val();
		document.getElementById("selectWorkId").value          = $(this).closest('.ymd-list-detail-container[name$="detail-parent"]').find('input[name$="workId"]').val();
		document.getElementById("selectStartDateTime").value   = $(this).closest('.ymd-list-detail-container[name$="detail-parent"]').find('input[name$="startDateTime"]').val();
		
		
		// ------------------------------------------------
		// hidden項目に設定した情報を「新規」で作成したフォームにセットし送信(submit)
		//
		// ※既存のformをそのままsubmitしてしまうと、画面に一覧表示されてる全ての情報をサーバに送信してしまう。
		//   そのためsubmit用に必要最低限の情報を新規で作成したformにセットし、それをsubmitする。
		//
		
		let filterHouseId          = document.getElementById("filterHouseId").value;
		let filterWorkId           = document.getElementById("filterWorkId").value;
		let filterStartEmployeeId  = document.getElementById("filterStartEmployeeId").value
		let filterDateFr           = document.getElementById("filterDateFr").value;
		let filterDateTo           = document.getElementById("filterDateTo").value;
		let selectHouseId          = document.getElementById("selectHouseId").value;
		let selectColNo            = document.getElementById("selectColNo").value;
		let selectWorkId           = document.getElementById("selectWorkId").value;
		let selectStartDateTime    = document.getElementById("selectStartDateTime").value;
		
		
		// 新しいフォームを一時的に作成
		const form = document.createElement("form");
		form.method = "POST";
		form.action = "/matsuoka/TransitionKanriMainteWorkStatusDetail";
		
		// 送信したい hidden 要素だけ追加
		const params = {
			 filterHouseId
			,filterWorkId
			,filterStartEmployeeId
			,filterDateFr
			,filterDateTo
			,selectHouseId
			,selectColNo
			,selectWorkId
			,selectStartDateTime
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
	
});
