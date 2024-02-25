
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
		
		console.log("001");
		
		// 押したボタンのhidden項目より検索期間を取得
		document.getElementById("selectIntervalDate").value   = $(this).closest('div[name$="intervalButtonData"]').find('input[name$="buttonData"]').val();
		
		console.log("002");
		
		
		console.log("□" + document.getElementById("selectIntervalDate").value);
		
		console.log("003");
		
		
		//画面内にformタグは１つしかないため０番目を固定で取得
		let form = document.getElementsByTagName('form')[0];
		
		console.log("004a");
		
		//form.action="";     // HTML内で直接記載されているためココでのセットは不要
    	//form.method="post"; // HTML内で直接記載されているためココでのセットは不要
    	form.submit();
	});
	
});
