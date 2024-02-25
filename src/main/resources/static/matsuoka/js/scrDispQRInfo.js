
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
		
		
		// hidden項目に押下したボタンの情報セットしフォームをPOST送信
		document.getElementById("pushedButtunKbn").value     = $(this).closest('div[name="buttonDispArea"]').find('input[name$="buttonKbn"]').val();
		document.getElementById("pushedButtunPercent").value = $(this).closest('div[name="buttonDispArea"]').find('input[name$="percent"]').val();
		
		//画面内にformタグは１つしかないため０番目を固定で取得
		let form = document.getElementsByTagName('form')[0];
		
		//form.action="";     // HTML内で直接記載されているためココでのセットは不要
    	//form.method="post"; // HTML内で直接記載されているためココでのセットは不要
    	form.submit();
	});
	
});
