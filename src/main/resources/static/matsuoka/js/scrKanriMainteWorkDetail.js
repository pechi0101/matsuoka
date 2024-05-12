
/*
JQueryで各種イベント操作

*/
$(function() {
	
	
	
	//□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□
	//
	// 入力変更イベント
	
	
	
	$('input[name$="workName"]').on("input", function() {
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		//------------------------------------------------
		// 作業名の入力チェック
		//------------------------------------------------
		
		let inputVal = document.getElementById("workName");
		let msgArea  = document.getElementById("workNameMsg");
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
	
	$('input[name$="separatePersent"]').on("input", function() {
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		//------------------------------------------------
		// 進捗率区切の入力チェック
		//------------------------------------------------
		
		inputVal = document.getElementById("separatePersent");
		msgArea  = document.getElementById("separatePersentMsg");
		msgArea.innerHTML = '';
		
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
			
		} else if (
		   inputVal.value !=  0
		&& inputVal.value !=  5
		&& inputVal.value != 10
		&& inputVal.value != 20
		&& inputVal.value != 25
		&& inputVal.value != 50) {
			//桁数チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】0,5,10,20,50のいずれかで入力してください</p>';
			return;
		}
	});
	
	$('input[name$="resetSpan"]').on("input", function() {
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		//------------------------------------------------
		// リセット間隔の入力チェック
		//------------------------------------------------
		
		inputVal = document.getElementById("resetSpan");
		msgArea  = document.getElementById("resetSpanMsg");
		msgArea.innerHTML = '';
		
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
		// 作業名の入力チェック
		//------------------------------------------------
		
		let inputVal = document.getElementById("workName");
		let msgArea  = document.getElementById("workNameMsg");
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
		// 進捗率区切の入力チェック
		//------------------------------------------------
		
		inputVal = document.getElementById("separatePersent");
		msgArea  = document.getElementById("separatePersentMsg");
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
			
		} else if (
		   inputVal.value !=  0
		&& inputVal.value !=  5
		&& inputVal.value != 10
		&& inputVal.value != 20
		&& inputVal.value != 25
		&& inputVal.value != 50) {
			//桁数チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】0,5,10,20,50のいずれかで入力してください</p>';
			checkOKFlg = false;
		}
		
		
		//------------------------------------------------
		// リセット間隔の入力チェック
		//------------------------------------------------
		
		inputVal = document.getElementById("resetSpan");
		msgArea  = document.getElementById("resetSpanMsg");
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
		//console.log("■" + $(this).closest('div[name="workData"]').find('input[name$="workId"]').val());
		
		
		// hidden項目に選択した内容をセットしフォームをPOST送信
		document.getElementById("buttonKbn").value   = "";  //※不要
		
		//画面内にformタグは１つしかないため０番目を固定で取得
		let form = document.getElementsByTagName('form')[0];
		
		form.action="TransitionKanriMainteWorkList";
    	form.method="get";
    	form.submit();
	});
	
	//------------------------------------------------
	// 登録ボタン押下イベント
	//
	
	$('.ymd-kanri-button a[name$="regist"]').click(function() {
		
		//alert("新規");
		//console.log("■" + $(this).closest('div[name="workData"]').find('input[name$="workId"]').val());
		
		//入力チェックNGの場合は処理終了
		if (inputCheck() == false) {
			alert('入力チェックでエラーがあります。画面赤字箇所の指示に従い入力を修正してください。');
			return;
		}
		
		// hidden項目に選択した内容をセットしフォームをPOST送信
		document.getElementById("buttonKbn").value   = "regist";
		
		//画面内にformタグは１つしかないため０番目を固定で取得
		let form = document.getElementsByTagName('form')[0];
		
		form.action="/matsuoka/EditKanriWork";
    	form.method="post";
    	form.submit();
	});
	
	//------------------------------------------------
	// 更新ボタン押下イベント
	//
	
	$('.ymd-kanri-button a[name$="update"]').click(function() {
		
		//alert("新規");
		//console.log("■" + $(this).closest('div[name="workData"]').find('input[name$="workId"]').val());
		
		//入力チェックNGの場合は処理終了
		if (inputCheck() == false) {
			alert('入力チェックでエラーがあります。画面赤字箇所の指示に従い入力を修正してください。');
			return;
		}
		
		// hidden項目に選択した内容をセットしフォームをPOST送信
		document.getElementById("buttonKbn").value   = "update";
		
		//画面内にformタグは１つしかないため０番目を固定で取得
		let form = document.getElementsByTagName('form')[0];
		
		form.action="/matsuoka/EditKanriWork";
    	form.method="post";
    	form.submit();
	});
	
	//------------------------------------------------
	// 削除ボタン押下イベント
	//
	
	$('.ymd-kanri-button a[name$="delete"]').click(function() {
		
		//alert("新規");
		//console.log("■" + $(this).closest('div[name="workData"]').find('input[name$="workId"]').val());
		
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
		
		form.action="/matsuoka/EditKanriWork";
    	form.method="post";
    	form.submit();
	});
	
	
});
