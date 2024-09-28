

//収穫日の初期値を本日にする
var today = new Date();
today.setDate(today.getDate());
var yyyy = today.getFullYear();
var mm   = ("0"+(today.getMonth()+1)).slice(-2);
var dd   = ("0"+today.getDate()).slice(-2);
document.getElementById("inputShukakuDate").value=yyyy+'-'+mm+'-'+dd;
document.getElementById("shukakuDate").value=yyyy + '-' + mm + '-' + dd + 'T00:00:00';


// ケース数の初期値を空白にする
document.getElementById("boxSum").value = "";





/**
 * 通常のJavaScriptで入力チェックを実施
 */
window.addEventListener('DOMContentLoaded',function() {
	
	let inputBoxSum = document.getElementById("boxSum");
	
	// 入力変更時に入力チェック
	inputBoxSum.addEventListener("input",function() {
		
		// 半角数字と小数点の正規表現（整数部は３桁、小数点以下は１桁）
		var regex = /^\d{1,3}(\.\d{1})?$/;
		
		if (this.value.length == 0) {
			//未入力チェック
			document.getElementById("boxSumMsg").innerHTML = '<p class="msg-ng">【NG】入力必須です</p>';
		} else if (regex.test(this.value) == false) {
			//半角数字チェック
			document.getElementById("boxSumMsg").innerHTML = '<p class="msg-ng">【NG】半角数字と小数点のみで入力してください。</p>';
		} else if (this.value == 0) {
			//0はNGチェック
			document.getElementById("boxSumMsg").innerHTML = '<p class="msg-ng">【NG】0以外の数値を入力してください</p>';
		}else{
			document.getElementById("boxSumMsg").innerHTML = '';
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
		
		
		// ”登録/更新”ボタンを押された場合のみ入力チェックを実施
		
		// 【メモ】
		// 収穫である場合は以下ボタンのみ表示される
		//   ・regist 登録/更新
		//   ・cancel 取消してもう一度
		
		if (buttonKbn == "regist") {
			
			
			let inputBoxSum = document.getElementById("boxSum");
			
			
			// 半角数字と小数点の正規表現（整数部は３桁、小数点以下は１桁）
			var regex = /^\d{1,3}(\.\d{1})?$/;
			
			if (inputBoxSum.value.length == 0) {
				//未入力チェック
				document.getElementById("boxSumMsg").innerHTML = '<p class="msg-ng">【NG】入力必須です</p>';
				return;
				
			} else if (regex.test(inputBoxSum.value) == false) {
				//半角数字チェック
				document.getElementById("boxSumMsg").innerHTML = '<p class="msg-ng">【NG】半角数字と小数点のみで入力してください。</p>';
				return;
				
			} else if (inputBoxSum.value == 0) {
				//0はNGチェック
				document.getElementById("boxSumMsg").innerHTML = '<p class="msg-ng">【NG】0以外の数値を入力してください</p>';
				return;
				
			}else{
				document.getElementById("boxSumMsg").innerHTML = '';
			}
		}
		
		
		// ”取消してもう一度”ボタンを押された場合、ケース数は0で初期化
		//※ケース数を空白にして”取消してもう一度”ボタンを押下した場合、↑の入力チェックが行われない。その場合、数値型に空白がセットされ異常終了するため。
		if (buttonKbn == "cancel") {
			document.getElementById("boxSum").value = 0;
		}
		
		
		
		// hidden項目に押下したボタンの情報セットしフォームをPOST送信
		document.getElementById("pushedButtunKbn").value     = $(this).closest('div[name="buttonDispArea"]').find('input[name$="buttonKbn"]').val();
		
		
		
		
		// 画面の収穫日を登録日にセット
		var shukakuDateValue = document.getElementById("inputShukakuDate").value;
		
		console.log("shukakuDateValue=[" + shukakuDateValue + "]");
		
		
		// LocalDateTime型をDate型に変換
		var date = new Date(shukakuDateValue);
		var year = date.getFullYear();
		var month = date.getMonth() + 1; // 月は0から始まるため+1する
		var day = date.getDate();
		
		console.log("month      =[" + month + "]");
		console.log("繋げると...=[" + year + '-' + (month < 10 ? '0' : '') + month + '-' + (day < 10 ? '0' : '') + day + "]");
		
		
		//月や日の値が10未満の場合に、一桁の場合に前に0を追加。例えば、1月の場合は '01'に変換して文字列結合。
		//LocalDateTime型であることを考慮し時分秒(T00:00:00)も末尾につける。
		document.getElementById("registDatetime").value = year + '-' + (month < 10 ? '0' : '') + month + '-' + (day < 10 ? '0' : '') + day + 'T00:00:00';
		
		
		//画面内にformタグは１つしかないため０番目を固定で取得
		let form = document.getElementsByTagName('form')[0];
		
		//form.action="";     // HTML内で直接記載されているためココでのセットは不要
    	//form.method="post"; // HTML内で直接記載されているためココでのセットは不要
    	form.submit();
    	
	});
	
});
