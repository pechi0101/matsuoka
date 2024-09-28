
/*
JQueryで各種イベント操作

*/
$(function() {
	
	
	
	//□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□
	//
	// 入力変更イベント
	
	
	
	$('input[name$="employeeName"]').on("input", function() {
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		//------------------------------------------------
		// 社員名の入力チェック
		//------------------------------------------------
		
		let inputVal = document.getElementById("employeeName");
		let msgArea  = document.getElementById("employeeNameMsg");
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
	
	$('input[name$="phone"]').on("input", function() {
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		//------------------------------------------------
		// 電話番号の入力チェック
		//------------------------------------------------
		
		let inputVal = document.getElementById("phone");
		let msgArea  = document.getElementById("phoneMsg");
		
		// 半角数字とハイフンの正規表現
		var regex = /^[0-9-]+$/;
		
		if (inputVal.value.length > 0) {
				
			if (regex.test(inputVal.value) == false) {
				//半角数字チェック
				msgArea.innerHTML = '<p class="msg-ng">【NG】半角数字とハイフンのみで入力してください</p>';
				return;
				
			} else if (inputVal.value.length > 13) {
				//桁数チェック
				msgArea.innerHTML = '<p class="msg-ng">【NG】13文字以内で入力してください</p>';
				return;
				
			}else{
				msgArea.innerHTML = '';
			}
		}
	});
	
	$('input[name$="postCd"]').on("input", function() {
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		//------------------------------------------------
		// 郵便番号の入力チェック
		//------------------------------------------------
		
		let inputVal = document.getElementById("postCd");
		let msgArea  = document.getElementById("postCdMsg");
		
		// 半角数字とハイフンの正規表現
		var regex = /^[0-9-]+$/;
		
		if (inputVal.value.length > 0) {
				
			if (regex.test(inputVal.value) == false) {
				//半角数字チェック
				msgArea.innerHTML = '<p class="msg-ng">【NG】半角数字とハイフンのみで入力してください</p>';
				return;
				
			} else if (inputVal.value.length > 8) {
				//桁数チェック
				msgArea.innerHTML = '<p class="msg-ng">【NG】8文字以内で入力してください</p>';
				return;
				
			}else{
				msgArea.innerHTML = '';
			}
			
		}
	});
	
	$('input[name$="address"]').on("input", function() {
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		//------------------------------------------------
		// 住所の入力チェック
		//------------------------------------------------
		
		let inputVal = document.getElementById("address");
		let msgArea  = document.getElementById("addressMsg");
		
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
	
	$('input[name$="hourlyWage"]').on("input", function() {
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		//------------------------------------------------
		// 時給の入力チェック
		//------------------------------------------------
		
		let inputVal = document.getElementById("hourlyWage");
		let msgArea  = document.getElementById("hourlyWageMsg");
		
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
			
		} else if (inputVal.value.length > 4) {
			//桁数チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】4ケタ以内で入力してください</p>';
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
		// 社員名の入力チェック
		//------------------------------------------------
		
		let inputVal = document.getElementById("employeeName");
		let msgArea  = document.getElementById("employeeNameMsg");
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
		// 電話番号の入力チェック
		//------------------------------------------------
		
		inputVal = document.getElementById("phone");
		msgArea  = document.getElementById("phoneMsg");
		msgArea.innerHTML = '';
		
		// 半角数字とハイフンの正規表現
		var regex = /^[0-9-]+$/;
		
		if (inputVal.value.length > 0) {
				
			if (regex.test(inputVal.value) == false) {
				//半角数字チェック
				msgArea.innerHTML = '<p class="msg-ng">【NG】半角数字とハイフンのみで入力してください</p>';
				checkOKFlg = false;
				
			} else if (inputVal.value.length > 13) {
				//桁数チェック
				msgArea.innerHTML = '<p class="msg-ng">【NG】13文字以内で入力してください</p>';
				checkOKFlg = false;
			}
		}
		
		
		//------------------------------------------------
		// 郵便番号の入力チェック
		//------------------------------------------------
		
		inputVal = document.getElementById("postCd");
		msgArea  = document.getElementById("postCdMsg");
		msgArea.innerHTML = '';
		
		// 半角数字とハイフンの正規表現
		var regex = /^[0-9-]+$/;
		
		if (inputVal.value.length > 0) {
				
			if (regex.test(inputVal.value) == false) {
				//半角数字チェック
				msgArea.innerHTML = '<p class="msg-ng">【NG】半角数字とハイフンのみで入力してください</p>';
				checkOKFlg = false;
				
			} else if (inputVal.value.length > 8) {
				//桁数チェック
				msgArea.innerHTML = '<p class="msg-ng">【NG】8文字以内で入力してください</p>';
				checkOKFlg = false;
			}
			
		}
		
		
		//------------------------------------------------
		// 住所の入力チェック
		//------------------------------------------------
		
		inputVal = document.getElementById("address");
		msgArea  = document.getElementById("addressMsg");
		msgArea.innerHTML = '';
		
		if (inputVal.value.length > 0) {
				
			if (inputVal.value.length > 50) {
				//桁数チェック
				msgArea.innerHTML = '<p class="msg-ng">【NG】50文字以内で入力してください</p>';
				checkOKFlg = false;
			}
		}
		
		
		//------------------------------------------------
		// 時給の入力チェック
		//------------------------------------------------
		
		inputVal = document.getElementById("hourlyWage");
		msgArea  = document.getElementById("hourlyWageMsg");
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
			
		} else if (inputVal.value.length > 4) {
			//桁数チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】4ケタ以内で入力してください</p>';
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
		//console.log("■" + $(this).closest('div[name="employeeData"]').find('input[name$="employeeId"]').val());
		
		
		// hidden項目に選択した内容をセットしフォームをPOST送信
		document.getElementById("buttonKbn").value   = "";  //※不要
		
		//画面内にformタグは１つしかないため０番目を固定で取得
		let form = document.getElementsByTagName('form')[0];
		
		form.action="TransitionKanriMainteEmployeeList";
    	form.method="get";
    	form.submit();
	});
	
	//------------------------------------------------
	// 登録ボタン押下イベント
	//
	
	$('.ymd-kanri-button a[name$="regist"]').click(function() {
		
		//alert("新規");
		//console.log("■" + $(this).closest('div[name="employeeData"]').find('input[name$="employeeId"]').val());
		
		//入力チェックNGの場合は処理終了
		if (inputCheck() == false) {
			alert('入力チェックでエラーがあります。画面赤字箇所の指示に従い入力を修正してください。');
			return;
		}
		
		// hidden項目に選択した内容をセットしフォームをPOST送信
		document.getElementById("buttonKbn").value   = "regist";
		
		//画面内にformタグは１つしかないため０番目を固定で取得
		let form = document.getElementsByTagName('form')[0];
		
		form.action="/matsuoka/EditKanriEmployee";
    	form.method="post";
    	form.submit();
	});
	
	//------------------------------------------------
	// 更新ボタン押下イベント
	//
	
	$('.ymd-kanri-button a[name$="update"]').click(function(event) {
		
		//alert("新規");
		//console.log("■" + $(this).closest('div[name="employeeData"]').find('input[name$="employeeId"]').val());
		
		// 入力チェックエラー時(return後)のデフォルトの動作（formの送信処理）を防ぐ
		event.preventDefault();
		
		
		//入力チェックNGの場合は処理終了
		if (inputCheck() == false) {
			alert('入力チェックでエラーがあります。画面赤字箇所の指示に従い入力を修正してください。');
			return;
		}
		
		// hidden項目に選択した内容をセットしフォームをPOST送信
		document.getElementById("buttonKbn").value   = "update";
		
		//画面内にformタグは１つしかないため０番目を固定で取得
		let form = document.getElementsByTagName('form')[0];
		
		form.action="/matsuoka/EditKanriEmployee";
    	form.method="post";
    	form.submit();
	});
	
	//------------------------------------------------
	// 削除ボタン押下イベント
	//
	
	$('.ymd-kanri-button a[name$="delete"]').click(function() {
		
		//alert("新規");
		//console.log("■" + $(this).closest('div[name="employeeData"]').find('input[name$="employeeId"]').val());
		
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
		
		form.action="/matsuoka/EditKanriEmployee";
    	form.method="post";
    	form.submit();
	});
	
	
});
