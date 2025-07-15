
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
		
		
		// hidden項目にボタンを押下した社員の社員IDをセットしフォームをPOST送信
		document.getElementById("loginEmployeeId").value   = $(this).closest('div[name="employeeData"]').find('input[name$="employeeId"]').val();
		document.getElementById("loginEmployeeName").value = $(this).closest('div[name="employeeData"]').find('input[name$="employeeName"]').val();
		
		// ローカルストレージにログイン社員ID、社員名を保持
		localStorage.setItem("localStorage_LoginEmployeeId"  ,document.getElementById("loginEmployeeId").value);
		localStorage.setItem("localStorage_LoginEmployeeName",document.getElementById("loginEmployeeName").value);
		
		
		//画面内にformタグは１つしかないため０番目を固定で取得
		let form = document.getElementsByTagName('form')[0];
		
		//form.action="";     // HTML内で直接記載されているためココでのセットは不要
    	//form.method="post"; // HTML内で直接記載されているためココでのセットは不要
    	form.submit();
	});
	
});
