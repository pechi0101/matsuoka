
/*
JQueryで各種イベント操作

*/
$(function() {
	
	//------------------------------------------------
	// ボタン押下イベント
	//
	
	$('.ymd-button a[name$="send"]').click(function() {
		
		//alert("変更");
		//console.log("■" + $(this).closest('div[name="employeeData"]').find('input[name$="employeeId"]').val());
		console.log("□□★");
		
		// 押したボタンのhidden項目より呼び出しプログラム名を取得
		var callPgmName = $(this).closest('.ymd-button').find('input[name$="callPgmName"]').val();
		
		console.log("□" + callPgmName);
		
		//画面内にformタグは１つしかないため０番目を固定で取得
		let form = document.getElementsByTagName('form')[0];
		
		//ボタンの隠し項目から取得した呼出しプログラム名をアクションにセットしてsubmit
		form.action="/matsuoka/" + callPgmName; 
    	form.method="post";
    	form.submit();
	});
	
});
