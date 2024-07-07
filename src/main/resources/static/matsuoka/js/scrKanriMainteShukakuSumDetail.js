
/*
JQueryで各種イベント操作

*/
$(function() {
	
	
	
	//□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□
	//
	// 入力変更イベント
	
	
	$('#dropDownHouse').change(function(){
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		//------------------------------------------------
		// ハウスの入力チェック
		//------------------------------------------------
		
		let inputVal = document.getElementById("dropDownHouse");
		let msgArea  = document.getElementById("dropDownHouseMsg");
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
	
	$("#inputShukakuDate").change(function(){
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		//------------------------------------------------
		// 作業開始日の入力チェック
		//------------------------------------------------
		
		let inputVal = document.getElementById("inputShukakuDate");
		let msgArea  = document.getElementById("inputShukakuDateMsg");
		
		if (inputVal.value.length == 0) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】入力必須です</p>';
			return;
		}else{
			msgArea.innerHTML = '';
		}
	});
	
	
	$('input[name$="boxSum"]').on("input", function() {
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		//------------------------------------------------
		// 収穫ケース数の入力チェック
		//------------------------------------------------
		
		let inputVal  = document.getElementById("boxSum");
		let msgArea   = document.getElementById("boxSumMsg");
		
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
			
		} else if (inputVal.value > 300) {
			//範囲チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】0～300の範囲内で入力してください</p>';
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
	// 入力チェック関数(登録用)
	//
	
	function inputCheck_regist() {
		
		
		let checkOKFlg = true;
		
		//------------------------------------------------
		// ハウスの入力チェック
		//------------------------------------------------
		
		let inputVal = document.getElementById("dropDownHouse");
		let msgArea  = document.getElementById("dropDownHouseMsg");
		if (inputVal.value.length == 0) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】入力必須です</p>';
			checkOKFlg = false;
			
		} else if (inputVal.value.length > 20) {
			//桁数チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】20文字以内で入力してください</p>';
			checkOKFlg = false;
			
		}else{
			msgArea.innerHTML = '';
		}
		
		
		//------------------------------------------------
		// 収穫日の入力チェック
		//------------------------------------------------
		
		inputVal = document.getElementById("inputShukakuDate");
		msgArea  = document.getElementById("inputShukakuDateMsg");
		
		if (inputVal.value.length == 0) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】入力必須です</p>';
			checkOKFlg = false;
		}else{
			msgArea.innerHTML = '';
		}
		
		
		//------------------------------------------------
		// ケース数の入力チェック
		//------------------------------------------------
		
		inputVal  = document.getElementById("boxSum");
		msgArea   = document.getElementById("boxSumMsg");
		
		
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
			
		} else if (inputVal.value > 300) {
			//範囲チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】0～300の範囲内で入力してください</p>';
			return;
			
		}else{
			msgArea.innerHTML = '';
		}
		
		
		//------------------------------------------------
		// 備考の入力チェック
		//------------------------------------------------
		
		inputVal = document.getElementById("biko");
		msgArea  = document.getElementById("bikoMsg");
		
		if (inputVal.value.length > 0) {
				
			if (inputVal.value.length > 50) {
				//桁数チェック
				msgArea.innerHTML = '<p class="msg-ng">【NG】50文字以内で入力してください</p>';
				return;
				
			}else{
				msgArea.innerHTML = '';
			}
		}
		
		
		
		return checkOKFlg;
		
	}
	//------------------------------------------------
	// 入力チェック関数(更新用)：ハウスと収穫日が入力項目ではないため登録用とチェックを分ける
	//
	
	function inputCheck_update() {
		
		
		let checkOKFlg = true;
		
		
		//------------------------------------------------
		// ケース数の入力チェック
		//------------------------------------------------
		
		inputVal  = document.getElementById("boxSum");
		msgArea   = document.getElementById("boxSumMsg");
		
		
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
			
		} else if (inputVal.value > 300) {
			//範囲チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】0～300の範囲内で入力してください</p>';
			return;
			
		}else{
			msgArea.innerHTML = '';
		}
		
		
		//------------------------------------------------
		// 備考の入力チェック
		//------------------------------------------------
		
		inputVal = document.getElementById("biko");
		msgArea  = document.getElementById("bikoMsg");
		
		if (inputVal.value.length > 0) {
				
			if (inputVal.value.length > 50) {
				//桁数チェック
				msgArea.innerHTML = '<p class="msg-ng">【NG】50文字以内で入力してください</p>';
				return;
				
			}else{
				msgArea.innerHTML = '';
			}
		}
		
		
		
		return checkOKFlg;
		
	}
	
	
	//------------------------------------------------
	// 入力項目をhidden領域に編集
	//
	
	function editInputValToHiddenArea() {
		
		
		document.getElementById("houseId").value          = document.getElementById("dropDownHouse").value;
		document.getElementById("shukakuDate").value      = editDateValue("inputShukakuDate"); //自作関数でyyyy/MM/dd 形式に変換
		
	}
	
	
	//------------------------------------------------
	// 日付の編集
	//
	
	function editDateValue(strDateInputAreaName) {
		
		
		let dateInputVal   = document.getElementById(strDateInputAreaName).value
		
		//日付が入力されていない場合は空白を返却
		if (dateInputVal == "") {
			return "";
		}
		
		// 日付と時間を組み合わせてDateオブジェクトを作成
		var combinedDate = new Date(dateInputVal);
		
		// yyyy/MM/dd hh 形式に整形
		//
		//【メモ】サーバ側で以下のように変数宣言されてるからその形式に合わせる
		//     @DateTimeFormat(pattern = "yyyy/MM/dd")
		//     private LocalDate shukakuDate;
		//
		var formattedDate = combinedDate.getFullYear() + '/' + 
								 ('0' + (combinedDate.getMonth() + 1)).slice(-2) + '/' + 
								 ('0' + combinedDate.getDate()).slice(-2);
		
		
		
		console.log("□編集した日付=[" + formattedDate + "]");
		
		return formattedDate;
		
	}
	
	
	//------------------------------------------------
	// 戻るボタン押下イベント
	//
	
	$('.ymd-kanri-button a[name$="back"]').click(function() {
		
		//alert("新規");
		//console.log("■" + $(this).closest('div[name="houseData"]').find('input[name$="houseId"]').val());
		
		
		// hidden項目に選択した内容をセットしフォームをPOST送信
		document.getElementById("buttonKbn").value   = "back";  //※不要
		
		//画面内にformタグは１つしかないため０番目を固定で取得
		let form = document.getElementsByTagName('form')[0];
		
		form.action="/matsuoka/TransitionKanriMainteShukakuSumListSearch";
    	form.method="post";
    	form.submit();
	});
	
	//------------------------------------------------
	// 登録ボタン押下イベント
	//
	
	$('.ymd-kanri-button a[name$="regist"]').click(function() {
		
		//alert("新規★★★★★★★★★★★★★");
		//console.log("■" + $(this).closest('div[name="houseData"]').find('input[name$="houseId"]').val());
		
		//入力チェックNGの場合は処理終了
		if (inputCheck_regist() == false) {
			alert('入力チェックでエラーがあります。画面赤字箇所の指示に従い入力を修正してください。');
			return;
		}
		
		// hidden項目に選択した内容をセットしフォームをPOST送信
		document.getElementById("buttonKbn").value   = "regist";
		
		// 入力項目をhidden領域に編集(自作関数)
		editInputValToHiddenArea();
		
		//画面内にformタグは１つしかないため０番目を固定で取得
		let form = document.getElementsByTagName('form')[0];
		
		form.action="/matsuoka/TransitionKanriEditShukakuSum";
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
		if (inputCheck_update() == false) {
			alert('入力チェックでエラーがあります。画面赤字箇所の指示に従い入力を修正してください。');
			return;
		}
		
		// hidden項目に選択した内容をセットしフォームをPOST送信
		document.getElementById("buttonKbn").value   = "update";
		
		//画面内にformタグは１つしかないため０番目を固定で取得
		let form = document.getElementsByTagName('form')[0];
		
		form.action="/matsuoka/TransitionKanriEditShukakuSum";
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
		
		form.action="/matsuoka/TransitionKanriEditShukakuSum";
    	form.method="post";
    	form.submit();
	});
	
	
});
