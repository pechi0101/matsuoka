


//console.log("■開始");
//console.log("text =[" + document.getElementById("startDatetime").text + "]");
//console.log("value=[" + document.getElementById("startDatetime").value + "]");


if (document.getElementById("startDatetime").value == "") {
	//------------------------------------------------
	// 開始日時が空白の場合(作業開始時)
	
	//ケース数の項目を非表示にする（divタグごと非表示にする）
	document.getElementById("boxCountContainer").style.display = 'none';
	document.getElementById("workEndMessageContainer").style.display = 'none';
}else{
	//------------------------------------------------
	// 開始日時が空白でない場合(作業完了時)
	
	// ケース数の初期値を空白にする
	document.getElementById("boxCount").value = "";
}


/**
 * 通常のJavaScriptで入力チェックを実施
 */
window.addEventListener('DOMContentLoaded',function() {
	
	let inputBoxCount = document.getElementById("boxCount");
	
	// 入力変更時に入力チェック
	inputBoxCount.addEventListener("input",function() {
		
		// 半角数字と小数点の正規表現（整数部は３桁、小数点以下は１桁）
		var regex = /^\d{1,3}(\.\d{1})?$/;
		
		if (this.value.length == 0) {
			//未入力チェック
			document.getElementById("boxCountMsg").innerHTML = '<p class="msg-ng">【NG】入力必須です</p>';
		} else if (regex.test(this.value) == false) {
			//半角数字チェック
			document.getElementById("boxCountMsg").innerHTML = '<p class="msg-ng">【NG】半角数字と小数点のみで入力してください。</p>';
		} else if (this.value == 0) {
			//0はNGチェック
			document.getElementById("boxCountMsg").innerHTML = '<p class="msg-ng">【NG】0以外の数値を入力してください</p>';
		}else{
			document.getElementById("boxCountMsg").innerHTML = '';
		}
	});
	
	
});


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
		
		let buttonKbn = $(this).closest('div[name="buttonDispArea"]').find('input[name$="buttonKbn"]').val();
		
		console.log("buttonKbn=[" + buttonKbn + "]");
		
		
		// ”作業完了 100%”ボタンを押された場合のみ入力チェックを実施
		
		// 【メモ】
		// 収穫である場合は以下ボタンのみ表示される
		//   ・start  作業開始
		//   ・end    作業完了 100%
		//   ・cancel 取消してもう一度
		
		if (buttonKbn == "end") {
			
			
			let inputBoxCount = document.getElementById("boxCount");
			
			
			// 半角数字と小数点の正規表現（整数部は３桁、小数点以下は１桁）
			var regex = /^\d{1,3}(\.\d{1})?$/;
			
			if (inputBoxCount.value.length == 0) {
				//未入力チェック
				document.getElementById("boxCountMsg").innerHTML = '<p class="msg-ng">【NG】入力必須です</p>';
				return;
				
			} else if (regex.test(inputBoxCount.value) == false) {
				//半角数字チェック
				document.getElementById("boxCountMsg").innerHTML = '<p class="msg-ng">【NG】半角数字と小数点のみで入力してください。</p>';
				return;
				
			} else if (inputBoxCount.value == 0) {
				//0はNGチェック
				document.getElementById("boxCountMsg").innerHTML = '<p class="msg-ng">【NG】0以外の数値を入力してください</p>';
				return;
				
			}else{
				document.getElementById("boxCountMsg").innerHTML = '';
			}
		}
		
		
		// ”取消してもう一度”ボタンを押された場合、ケース数は0で初期化
		//※ケース数を空白にして”取消してもう一度”ボタンを押下した場合、↑の入力チェックが行われない。その場合、数値型に空白がセットされ異常終了するため。
		if (buttonKbn == "cancel") {
			document.getElementById("boxCount").value = 0;
		}
		
		
		
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
