

// □□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□
// 共通関数


//------------------------------------------------
// 行を表示するか否かの判定
//

function showOrHideRow() {
	
	
	//------------------------------------------------
	// フィルタリング条件の各値を取得
	
	var filterHouseId    = $("#dropDownHouse").val();
	
	// 【メモ】日付は「2024-06-12」の形式で取得できるみたい
	// 【メモ】日付が初期状態(yyyy/mm/ddと画面に表示されてる状態)である場合は空白("")が取得できる
	var filterDateFr     = $("#searchDateFr").val();
	var filterDateTo     = $("#searchDateTo").val();
	
	filterDateFr = filterDateFr.replace(/-/g,"");//「2024-06-12」のハイフンを正規表現で全て除去しとく
	filterDateTo = filterDateTo.replace(/-/g,"");//「2024-06-12」のハイフンを正規表現で全て除去しとく
	
	
	
	console.log("□選択されたハウスID=[" + filterHouseId + "]");
	console.log("□選択された日付From=[" + filterDateFr + "]");
	console.log("□選択された日付To  =[" + filterDateTo + "]");
	
	
	
	//------------------------------------------------
	// 一覧の行を取得
	var rows = $('[name="detail-row"]');
	
	console.log("□□□LOOP開始------------------------------------------------");
	
	// 各行をループ処理
	rows.each(function() {
		
		// 行内のハウスIDを取得
		var houseId            = $(this).closest('.ymd-list-detail-container[name$="detail-parent"]').find('[name$="houseId"]').val();
		// 行内の収穫日を取得
		var shukakuDate         = $(this).closest('.ymd-list-detail-container[name$="detail-parent"]').find('[name$="shukakuDate"]').val();
		shukakuDate = shukakuDate.replace(/\//g,"");//「2024/06/12」のスラッシュを正規表現で全て除去しとく
		
		console.log("□ハウスID  =[" + houseId + "]");
		console.log("□収穫日    =[" + shukakuDate + "]");
		
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
		
		//収穫日From～To両方とも入力されてる場合→開始日がその範囲外である行は非表示にする
		if (filterDateFr     != "" && filterDateTo  != "") {
			if (shukakuDate    < filterDateFr
			||  filterDateTo < shukakuDate) {
				showFld = false;
			}
		}
		//収穫日From        のみ入力されてる場合→開始日がそれより小さい行は非表示にする
		if (filterDateFr     != "" && filterDateTo  == "") {
			if (shukakuDate    < filterDateFr) {
				showFld = false;
			}
		}
		//収穫日To          のみ入力されてる場合→開始日がそれより大きい行は非表示にする
		if (filterDateFr     == "" && filterDateTo  != "") {
			if (filterDateTo < shukakuDate) {
				showFld = false;
			}
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


$(document).ready(function() {
	
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
	// 新規登録ボタン表示エリアに関するイベント
	
	
	//------------------------------------------------
	// 新規追加ボタン押下イベント
	//
	
	$('.ymd-kanri-button a[name$="new"]').click(function() {
		
		
		//alert("新規");
		//console.log("■" + $(this).closest('div[name="houseData"]').find('input[name$="houseId"]').val());
		
		
		
		
		//------------------------------------------------
		// hidden項目に「選択したフィルタリング条件の情報」をセットしフォームをPOST送信
		document.getElementById("filterHouseId").value         = $("#dropDownHouse").val();
		// 【メモ】日付は「2024-06-12」の形式で取得できるみたい
		// 【メモ】日付が初期状態(yyyy/mm/ddと画面に表示されてる状態)である場合は空白("")が取得できる
		document.getElementById("filterDateFr").value          = $("#searchDateFr").val();
		document.getElementById("filterDateTo").value          = $("#searchDateTo").val();
		
		
		//------------------------------------------------
		// hidden項目に「選択した一覧の情報」をセットしフォームをPOST送信(新規追加時は空白)
		document.getElementById("selectHouseId").value         = "";
		document.getElementById("selectShukakuDate").value 　  = "";
		
		//------------------------------------------------
		
		//画面内にformタグは１つしかないため０番目を固定で取得
		let form = document.getElementsByTagName('form')[0];
		
		//form.action="";     // HTML内で直接記載されているためココでのセットは不要
    	//form.method="post"; // HTML内で直接記載されているためココでのセットは不要
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
		// hidden項目に「選択したフィルタリング条件の情報」をセットしフォームをPOST送信
		document.getElementById("filterHouseId").value         = $("#dropDownHouse").val();
		// 【メモ】日付は「2024-06-12」の形式で取得できるみたい
		// 【メモ】日付が初期状態(yyyy/mm/ddと画面に表示されてる状態)である場合は空白("")が取得できる
		document.getElementById("filterDateFr").value          = $("#searchDateFr").val();
		document.getElementById("filterDateTo").value          = $("#searchDateTo").val();
		
		
		//------------------------------------------------
		// hidden項目に「選択した一覧の情報」をセットしフォームをPOST送信(新規追加時は空白)
		document.getElementById("selectHouseId").value         = $(this).closest('.ymd-list-detail-container[name$="detail-parent"]').find('input[name$="houseId"]').val();
		document.getElementById("selectShukakuDate").value     = $(this).closest('.ymd-list-detail-container[name$="detail-parent"]').find('input[name$="shukakuDate"]').val();
		
		//------------------------------------------------
		
		//画面内にformタグは１つしかないため０番目を固定で取得
		let form = document.getElementsByTagName('form')[0];
		
		//form.action="";     // HTML内で直接記載されているためココでのセットは不要
		//form.method="post"; // HTML内で直接記載されているためココでのセットは不要
		form.submit();
	});
	
	
});
