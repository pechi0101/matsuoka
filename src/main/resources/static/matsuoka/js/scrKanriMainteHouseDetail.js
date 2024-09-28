
/*
JQueryで各種イベント操作

*/
$(function() {
	
	
	
	//□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□
	//
	// 入力変更イベント
	
	
	
	$('input[name$="houseName"]').on("input", function() {
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		//------------------------------------------------
		// ハウス名の入力チェック
		//------------------------------------------------
		
		let inputVal = document.getElementById("houseName");
		let msgArea  = document.getElementById("houseNameMsg");
		if (inputVal.value.length == 0) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】入力必須です</p>';
			return;
			
		} else if (inputVal.value.length > 20) {
			//桁数チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】20文字以内で入力してください</p>';
			return;
			
		}else{
			msgArea.innerHTML = '';
		}
	});
	
	$('input[name$="colCount"]').on("input", function() {
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		//------------------------------------------------
		// 列数の入力チェック
		//------------------------------------------------
		
		let inputVal = document.getElementById("colCount");
		let msgArea  = document.getElementById("colCountMsg");
		
		// 半角数字の正規表現
		var regex = /^[0-9]+$/;
		
		if (inputVal.value.length == 0) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】入力必須です</p>';
			return;
		
		} else if (regex.test(inputVal.value) == false) {
			//半角数字チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】半角数字のみで入力してください</p>';
			return;
			
		} else if (inputVal.value.length > 2) {
			//桁数チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】2ケタ以内で入力してください</p>';
			return;
			
		}else{
			msgArea.innerHTML = '';
		}
	});
	
	
	
	
	
	
	$('input[name$="boxSumYTI01"]').on("input", function() {
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		//------------------------------------------------
		// 収穫予定ケース数の入力チェック
		//------------------------------------------------
		
		let inputVal = document.getElementById("boxSumYTI01");
		let msgArea  = document.getElementById("boxSumYTIMsg");
		
		// 半角数字と小数点の正規表現（整数部は４桁、小数点以下は１桁）
		var regex = /^\d{1,4}(\.\d{1})?$/;
		
		if (inputVal.value.length == 0) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】入力必須です</p>';
			return;
		
		} else if (regex.test(inputVal.value) == false) {
			//半角数字チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】半角数字と小数点のみで入力してください。</p>';
			return;
			
		}else{
			msgArea.innerHTML = '';
		}
	});
	
	$('input[name$="boxSumYTI02"]').on("input", function() {
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		//------------------------------------------------
		// 収穫予定ケース数の入力チェック
		//------------------------------------------------
		
		let inputVal = document.getElementById("boxSumYTI02");
		let msgArea  = document.getElementById("boxSumYTIMsg");
		
		// 半角数字と小数点の正規表現（整数部は４桁、小数点以下は１桁）
		var regex = /^\d{1,4}(\.\d{1})?$/;
		
		if (inputVal.value.length == 0) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】入力必須です</p>';
			return;
		
		} else if (regex.test(inputVal.value) == false) {
			//半角数字チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】半角数字と小数点のみで入力してください。</p>';
			return;
			
		}else{
			msgArea.innerHTML = '';
		}
	});
	
	$('input[name$="boxSumYTI03"]').on("input", function() {
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		//------------------------------------------------
		// 収穫予定ケース数の入力チェック
		//------------------------------------------------
		
		let inputVal = document.getElementById("boxSumYTI03");
		let msgArea  = document.getElementById("boxSumYTIMsg");
		
		// 半角数字と小数点の正規表現（整数部は４桁、小数点以下は１桁）
		var regex = /^\d{1,4}(\.\d{1})?$/;
		
		if (inputVal.value.length == 0) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】入力必須です</p>';
			return;
		
		} else if (regex.test(inputVal.value) == false) {
			//半角数字チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】半角数字と小数点のみで入力してください。</p>';
			return;
			
		}else{
			msgArea.innerHTML = '';
		}
	});
	
	$('input[name$="boxSumYTI04"]').on("input", function() {
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		//------------------------------------------------
		// 収穫予定ケース数の入力チェック
		//------------------------------------------------
		
		let inputVal = document.getElementById("boxSumYTI04");
		let msgArea  = document.getElementById("boxSumYTIMsg");
		
		// 半角数字と小数点の正規表現（整数部は４桁、小数点以下は１桁）
		var regex = /^\d{1,4}(\.\d{1})?$/;
		
		if (inputVal.value.length == 0) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】入力必須です</p>';
			return;
		
		} else if (regex.test(inputVal.value) == false) {
			//半角数字チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】半角数字と小数点のみで入力してください。</p>';
			return;
			
		}else{
			msgArea.innerHTML = '';
		}
	});
	
	$('input[name$="boxSumYTI05"]').on("input", function() {
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		//------------------------------------------------
		// 収穫予定ケース数の入力チェック
		//------------------------------------------------
		
		let inputVal = document.getElementById("boxSumYTI05");
		let msgArea  = document.getElementById("boxSumYTIMsg");
		
		// 半角数字と小数点の正規表現（整数部は４桁、小数点以下は１桁）
		var regex = /^\d{1,4}(\.\d{1})?$/;
		
		if (inputVal.value.length == 0) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】入力必須です</p>';
			return;
		
		} else if (regex.test(inputVal.value) == false) {
			//半角数字チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】半角数字と小数点のみで入力してください。</p>';
			return;
			
		}else{
			msgArea.innerHTML = '';
		}
	});
	
	$('input[name$="boxSumYTI06"]').on("input", function() {
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		//------------------------------------------------
		// 収穫予定ケース数の入力チェック
		//------------------------------------------------
		
		let inputVal = document.getElementById("boxSumYTI06");
		let msgArea  = document.getElementById("boxSumYTIMsg");
		
		// 半角数字と小数点の正規表現（整数部は４桁、小数点以下は１桁）
		var regex = /^\d{1,4}(\.\d{1})?$/;
		
		if (inputVal.value.length == 0) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】入力必須です</p>';
			return;
		
		} else if (regex.test(inputVal.value) == false) {
			//半角数字チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】半角数字と小数点のみで入力してください。</p>';
			return;
			
		}else{
			msgArea.innerHTML = '';
		}
	});
	
	$('input[name$="boxSumYTI07"]').on("input", function() {
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		//------------------------------------------------
		// 収穫予定ケース数の入力チェック
		//------------------------------------------------
		
		let inputVal = document.getElementById("boxSumYTI07");
		let msgArea  = document.getElementById("boxSumYTIMsg");
		
		// 半角数字と小数点の正規表現（整数部は４桁、小数点以下は１桁）
		var regex = /^\d{1,4}(\.\d{1})?$/;
		
		if (inputVal.value.length == 0) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】入力必須です</p>';
			return;
		
		} else if (regex.test(inputVal.value) == false) {
			//半角数字チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】半角数字と小数点のみで入力してください。</p>';
			return;
			
		}else{
			msgArea.innerHTML = '';
		}
	});
	
	$('input[name$="boxSumYTI08"]').on("input", function() {
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		//------------------------------------------------
		// 収穫予定ケース数の入力チェック
		//------------------------------------------------
		
		let inputVal = document.getElementById("boxSumYTI08");
		let msgArea  = document.getElementById("boxSumYTIMsg");
		
		// 半角数字と小数点の正規表現（整数部は４桁、小数点以下は１桁）
		var regex = /^\d{1,4}(\.\d{1})?$/;
		
		if (inputVal.value.length == 0) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】入力必須です</p>';
			return;
		
		} else if (regex.test(inputVal.value) == false) {
			//半角数字チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】半角数字と小数点のみで入力してください。</p>';
			return;
			
		}else{
			msgArea.innerHTML = '';
		}
	});
	
	$('input[name$="boxSumYTI09"]').on("input", function() {
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		//------------------------------------------------
		// 収穫予定ケース数の入力チェック
		//------------------------------------------------
		
		let inputVal = document.getElementById("boxSumYTI09");
		let msgArea  = document.getElementById("boxSumYTIMsg");
		
		// 半角数字と小数点の正規表現（整数部は４桁、小数点以下は１桁）
		var regex = /^\d{1,4}(\.\d{1})?$/;
		
		if (inputVal.value.length == 0) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】入力必須です</p>';
			return;
		
		} else if (regex.test(inputVal.value) == false) {
			//半角数字チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】半角数字と小数点のみで入力してください。</p>';
			return;
			
		}else{
			msgArea.innerHTML = '';
		}
	});
	
	$('input[name$="boxSumYTI10"]').on("input", function() {
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		//------------------------------------------------
		// 収穫予定ケース数の入力チェック
		//------------------------------------------------
		
		let inputVal = document.getElementById("boxSumYTI10");
		let msgArea  = document.getElementById("boxSumYTIMsg");
		
		// 半角数字と小数点の正規表現（整数部は４桁、小数点以下は１桁）
		var regex = /^\d{1,4}(\.\d{1})?$/;
		
		if (inputVal.value.length == 0) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】入力必須です</p>';
			return;
		
		} else if (regex.test(inputVal.value) == false) {
			//半角数字チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】半角数字と小数点のみで入力してください。</p>';
			return;
			
		}else{
			msgArea.innerHTML = '';
		}
	});
	
	$('input[name$="boxSumYTI11"]').on("input", function() {
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		//------------------------------------------------
		// 収穫予定ケース数の入力チェック
		//------------------------------------------------
		
		let inputVal = document.getElementById("boxSumYTI11");
		let msgArea  = document.getElementById("boxSumYTIMsg");
		
		// 半角数字と小数点の正規表現（整数部は４桁、小数点以下は１桁）
		var regex = /^\d{1,4}(\.\d{1})?$/;
		
		if (inputVal.value.length == 0) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】入力必須です</p>';
			return;
		
		} else if (regex.test(inputVal.value) == false) {
			//半角数字チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】半角数字と小数点のみで入力してください。</p>';
			return;
			
		}else{
			msgArea.innerHTML = '';
		}
	});
	
	$('input[name$="boxSumYTI12"]').on("input", function() {
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		//------------------------------------------------
		// 収穫予定ケース数の入力チェック
		//------------------------------------------------
		
		let inputVal = document.getElementById("boxSumYTI12");
		let msgArea  = document.getElementById("boxSumYTIMsg");
		
		// 半角数字と小数点の正規表現（整数部は４桁、小数点以下は１桁）
		var regex = /^\d{1,4}(\.\d{1})?$/;
		
		if (inputVal.value.length == 0) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】入力必須です</p>';
			return;
		
		} else if (regex.test(inputVal.value) == false) {
			//半角数字チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】半角数字と小数点のみで入力してください。</p>';
			return;
			
		}else{
			msgArea.innerHTML = '';
		}
	});
	
	
	
	
	
	
	$('input[name$="biko"]').on("input", function() {
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		//------------------------------------------------
		// 備考の入力チェック
		//------------------------------------------------
		
		let inputVal = document.getElementById("biko");
		let msgArea  = document.getElementById("bikoMsg");
		
		if (inputVal.value.length > 0) {
				
			if (inputVal.value.length > 50) {
				//桁数チェック
				msgArea.innerHTML = '<p class="msg-ng">【NG】50文字以内で入力してください</p>';
				return;
				
			}else{
				msgArea.innerHTML = '';
			}
		}
	});
	
	
	
	//□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□
	//
	// ボタン押下イベント
	
	
	
	//------------------------------------------------
	// 入力チェック関数
	//
	
	function inputCheck() {
		
		
		let checkOKFlg = true;
		
		//------------------------------------------------
		// ハウス名の入力チェック
		//------------------------------------------------
		
		let inputVal = document.getElementById("houseName");
		let msgArea  = document.getElementById("houseNameMsg");
		msgArea.innerHTML = '';
		
		if (inputVal.value.length == 0) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】入力必須です</p>';
			checkOKFlg = false;
			
		} else if (inputVal.value.length > 20) {
			//桁数チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】20文字以内で入力してください</p>';
			checkOKFlg = false;
		}
		
		
		//------------------------------------------------
		// 列数の入力チェック
		//------------------------------------------------
		
		inputVal = document.getElementById("colCount");
		msgArea  = document.getElementById("colCountMsg");
		msgArea.innerHTML = '';
		
		// 半角数字の正規表現
		var regex = /^[0-9]+$/;
		
		if (inputVal.value.length == 0) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】入力必須です</p>';
			checkOKFlg = false;
		
		} else if (regex.test(inputVal.value) == false) {
			//半角数字チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】半角数字のみで入力してください</p>';
			checkOKFlg = false;
			
		} else if (inputVal.value.length > 2) {
			//桁数チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】2ケタ以内で入力してください</p>';
			checkOKFlg = false;
		}
		
		
		//------------------------------------------------
		// 収穫予定ケース数の入力チェック
		//------------------------------------------------
		
		inputVal = document.getElementById("boxSumYTI01");
		msgArea  = document.getElementById("boxSumYTIMsg");
		msgArea.innerHTML = '';
		
		// 半角数字と小数点の正規表現（整数部は４桁、小数点以下は１桁）
		var regex = /^\d{1,4}(\.\d{1})?$/;
		
		if (inputVal.value.length == 0) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】入力必須です</p>';
			checkOKFlg = false;
		
		} else if (regex.test(inputVal.value) == false) {
			//半角数字チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】半角数字と小数点のみで入力してください。</p>';
			checkOKFlg = false;
		}
		
		inputVal = document.getElementById("boxSumYTI02");
		msgArea  = document.getElementById("boxSumYTIMsg");
		msgArea.innerHTML = '';
		
		// 半角数字と小数点の正規表現（整数部は４桁、小数点以下は１桁）
		var regex = /^\d{1,4}(\.\d{1})?$/;
		
		if (inputVal.value.length == 0) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】入力必須です</p>';
			checkOKFlg = false;
		
		} else if (regex.test(inputVal.value) == false) {
			//半角数字チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】半角数字と小数点のみで入力してください。</p>';
			checkOKFlg = false;
		}
		
		inputVal = document.getElementById("boxSumYTI03");
		msgArea  = document.getElementById("boxSumYTIMsg");
		msgArea.innerHTML = '';
		
		// 半角数字と小数点の正規表現（整数部は４桁、小数点以下は１桁）
		var regex = /^\d{1,4}(\.\d{1})?$/;
		
		if (inputVal.value.length == 0) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】入力必須です</p>';
			checkOKFlg = false;
		
		} else if (regex.test(inputVal.value) == false) {
			//半角数字チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】半角数字と小数点のみで入力してください。</p>';
			checkOKFlg = false;
		}
		
		inputVal = document.getElementById("boxSumYTI04");
		msgArea  = document.getElementById("boxSumYTIMsg");
		msgArea.innerHTML = '';
		
		// 半角数字と小数点の正規表現（整数部は４桁、小数点以下は１桁）
		var regex = /^\d{1,4}(\.\d{1})?$/;
		
		if (inputVal.value.length == 0) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】入力必須です</p>';
			checkOKFlg = false;
		
		} else if (regex.test(inputVal.value) == false) {
			//半角数字チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】半角数字と小数点のみで入力してください。</p>';
			checkOKFlg = false;
		}
		
		inputVal = document.getElementById("boxSumYTI05");
		msgArea  = document.getElementById("boxSumYTIMsg");
		msgArea.innerHTML = '';
		
		// 半角数字と小数点の正規表現（整数部は４桁、小数点以下は１桁）
		var regex = /^\d{1,4}(\.\d{1})?$/;
		
		if (inputVal.value.length == 0) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】入力必須です</p>';
			checkOKFlg = false;
		
		} else if (regex.test(inputVal.value) == false) {
			//半角数字チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】半角数字と小数点のみで入力してください。</p>';
			checkOKFlg = false;
		}
		
		inputVal = document.getElementById("boxSumYTI06");
		msgArea  = document.getElementById("boxSumYTIMsg");
		msgArea.innerHTML = '';
		
		// 半角数字と小数点の正規表現（整数部は４桁、小数点以下は１桁）
		var regex = /^\d{1,4}(\.\d{1})?$/;
		
		if (inputVal.value.length == 0) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】入力必須です</p>';
			checkOKFlg = false;
		
		} else if (regex.test(inputVal.value) == false) {
			//半角数字チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】半角数字と小数点のみで入力してください。</p>';
			checkOKFlg = false;
		}
		
		inputVal = document.getElementById("boxSumYTI07");
		msgArea  = document.getElementById("boxSumYTIMsg");
		msgArea.innerHTML = '';
		
		// 半角数字と小数点の正規表現（整数部は４桁、小数点以下は１桁）
		var regex = /^\d{1,4}(\.\d{1})?$/;
		
		if (inputVal.value.length == 0) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】入力必須です</p>';
			checkOKFlg = false;
		
		} else if (regex.test(inputVal.value) == false) {
			//半角数字チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】半角数字と小数点のみで入力してください。</p>';
			checkOKFlg = false;
		}
		
		inputVal = document.getElementById("boxSumYTI08");
		msgArea  = document.getElementById("boxSumYTIMsg");
		msgArea.innerHTML = '';
		
		// 半角数字と小数点の正規表現（整数部は４桁、小数点以下は１桁）
		var regex = /^\d{1,4}(\.\d{1})?$/;
		
		if (inputVal.value.length == 0) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】入力必須です</p>';
			checkOKFlg = false;
		
		} else if (regex.test(inputVal.value) == false) {
			//半角数字チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】半角数字と小数点のみで入力してください。</p>';
			checkOKFlg = false;
		}
		
		inputVal = document.getElementById("boxSumYTI09");
		msgArea  = document.getElementById("boxSumYTIMsg");
		msgArea.innerHTML = '';
		
		// 半角数字と小数点の正規表現（整数部は４桁、小数点以下は１桁）
		var regex = /^\d{1,4}(\.\d{1})?$/;
		
		if (inputVal.value.length == 0) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】入力必須です</p>';
			checkOKFlg = false;
		
		} else if (regex.test(inputVal.value) == false) {
			//半角数字チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】半角数字と小数点のみで入力してください。</p>';
			checkOKFlg = false;
		}
		
		inputVal = document.getElementById("boxSumYTI10");
		msgArea  = document.getElementById("boxSumYTIMsg");
		msgArea.innerHTML = '';
		
		// 半角数字と小数点の正規表現（整数部は４桁、小数点以下は１桁）
		var regex = /^\d{1,4}(\.\d{1})?$/;
		
		if (inputVal.value.length == 0) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】入力必須です</p>';
			checkOKFlg = false;
		
		} else if (regex.test(inputVal.value) == false) {
			//半角数字チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】半角数字と小数点のみで入力してください。</p>';
			checkOKFlg = false;
		}
		
		inputVal = document.getElementById("boxSumYTI11");
		msgArea  = document.getElementById("boxSumYTIMsg");
		msgArea.innerHTML = '';
		
		// 半角数字と小数点の正規表現（整数部は４桁、小数点以下は１桁）
		var regex = /^\d{1,4}(\.\d{1})?$/;
		
		if (inputVal.value.length == 0) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】入力必須です</p>';
			checkOKFlg = false;
		
		} else if (regex.test(inputVal.value) == false) {
			//半角数字チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】半角数字と小数点のみで入力してください。</p>';
			checkOKFlg = false;
		}
		
		inputVal = document.getElementById("boxSumYTI12");
		msgArea  = document.getElementById("boxSumYTIMsg");
		msgArea.innerHTML = '';
		
		// 半角数字と小数点の正規表現（整数部は４桁、小数点以下は１桁）
		var regex = /^\d{1,4}(\.\d{1})?$/;
		
		if (inputVal.value.length == 0) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】入力必須です</p>';
			checkOKFlg = false;
		
		} else if (regex.test(inputVal.value) == false) {
			//半角数字チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】半角数字と小数点のみで入力してください。</p>';
			checkOKFlg = false;
		}
		
		
		
		//------------------------------------------------
		// 備考の入力チェック
		//------------------------------------------------
		
		inputVal = document.getElementById("biko");
		msgArea  = document.getElementById("bikoMsg");
		msgArea.innerHTML = '';
		
		if (inputVal.value.length > 0) {
				
			if (inputVal.value.length > 50) {
				//桁数チェック
				msgArea.innerHTML = '<p class="msg-ng">【NG】50文字以内で入力してください</p>';
				checkOKFlg = false;
			}
		}
		
		return checkOKFlg;
		
	}
	
	
	
	//------------------------------------------------
	// 戻るボタン押下イベント
	//
	
	$('.ymd-kanri-button a[name$="back"]').click(function() {
		
		//alert("新規");
		//console.log("■" + $(this).closest('div[name="houseData"]').find('input[name$="houseId"]').val());
		
		
		// hidden項目に選択した内容をセットしフォームをPOST送信
		document.getElementById("buttonKbn").value   = "";  //※不要
		
		//画面内にformタグは１つしかないため０番目を固定で取得
		let form = document.getElementsByTagName('form')[0];
		
		form.action="TransitionKanriMainteHouseList";
    	form.method="get";
    	form.submit();
	});
	
	//------------------------------------------------
	// 登録ボタン押下イベント
	//
	
	$('.ymd-kanri-button a[name$="regist"]').click(function() {
		
		//alert("新規");
		//console.log("■" + $(this).closest('div[name="houseData"]').find('input[name$="houseId"]').val());
		
		//入力チェックNGの場合は処理終了
		if (inputCheck() == false) {
			alert('入力チェックでエラーがあります。画面赤字箇所の指示に従い入力を修正してください。');
			return;
		}
		
		// hidden項目に選択した内容をセットしフォームをPOST送信
		document.getElementById("buttonKbn").value   = "regist";
		
		//画面内にformタグは１つしかないため０番目を固定で取得
		let form = document.getElementsByTagName('form')[0];
		
		form.action="/matsuoka/EditKanriHouse";
    	form.method="post";
    	form.submit();
	});
	
	//------------------------------------------------
	// 更新ボタン押下イベント
	//
	
	$('.ymd-kanri-button a[name$="update"]').click(function() {
		
		//alert("新規");
		//console.log("■" + $(this).closest('div[name="houseData"]').find('input[name$="houseId"]').val());
		
		//入力チェックNGの場合は処理終了
		if (inputCheck() == false) {
			alert('入力チェックでエラーがあります。画面赤字箇所の指示に従い入力を修正してください。');
			return;
		}
		
		// hidden項目に選択した内容をセットしフォームをPOST送信
		document.getElementById("buttonKbn").value   = "update";
		
		//画面内にformタグは１つしかないため０番目を固定で取得
		let form = document.getElementsByTagName('form')[0];
		
		form.action="/matsuoka/EditKanriHouse";
    	form.method="post";
    	form.submit();
	});
	
	//------------------------------------------------
	// 削除ボタン押下イベント
	//
	
	$('.ymd-kanri-button a[name$="delete"]').click(function() {
		
		//alert("新規");
		//console.log("■" + $(this).closest('div[name="houseData"]').find('input[name$="houseId"]').val());
		
		var confirmation = confirm('本当に削除しますか？');
		
		// OKが選択された場合
		if (confirmation) {
			// ここに処理を記述
			
		} else {
			// キャンセルが選択された場合
			alert('処理をキャンセルしました。');
			return;
		}
		
		// hidden項目に選択した内容をセットしフォームをPOST送信
		document.getElementById("buttonKbn").value   = "delete";
		
		//画面内にformタグは１つしかないため０番目を固定で取得
		let form = document.getElementsByTagName('form')[0];
		
		form.action="/matsuoka/EditKanriHouse";
    	form.method="post";
    	form.submit();
	});
	
	
});
