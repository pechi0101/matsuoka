
/*
JQueryで各種イベント操作

*/
$(function() {
	
	//------------------------------------------------
	// 新規追加ボタン押下イベント
	//
	
	$('.ymd-kanri-button a[name$="new"]').click(function() {
		
		//alert("新規");
		//console.log("■" + $(this).closest('div[name="workData"]').find('input[name$="workId"]').val());
		
		
		// hidden項目に選択した作業の作業IDをセットしフォームをPOST送信(新規追加時は作業IDは空白)
		document.getElementById("selectWorkId").value   = "";
		
		//画面内にformタグは１つしかないため０番目を固定で取得
		let form = document.getElementsByTagName('form')[0];
		
		//form.action="";     // HTML内で直接記載されているためココでのセットは不要
    	//form.method="post"; // HTML内で直接記載されているためココでのセットは不要
    	form.submit();
	});
	
	
	//------------------------------------------------
	// 一覧の特定行押下イベント
	//
	
	$('.ymd-list-detail-container[name$="detail-row"]').click(function() {
		
		//alert("変更");
		//console.log("■" + $(this).closest('.ymd-list-detail-container[name$="detail-parent"]').find('input[name$="workId"]').val());
		
		// hidden項目に選択した作業の作業IDをセットしフォームをPOST送信(新規追加時は作業IDは空白)
		document.getElementById("selectWorkId").value   = $(this).closest('.ymd-list-detail-container[name$="detail-parent"]').find('input[name$="workId"]').val();
		
		//画面内にformタグは１つしかないため０番目を固定で取得
		let form = document.getElementsByTagName('form')[0];
		
		//form.action="";     // HTML内で直接記載されているためココでのセットは不要
    	//form.method="post"; // HTML内で直接記載されているためココでのセットは不要
    	form.submit();
	});
	
});
