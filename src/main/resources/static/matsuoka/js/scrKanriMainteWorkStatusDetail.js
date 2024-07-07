
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
	
	$('input[name$="colNo"]').on("input", function() {
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		//------------------------------------------------
		// 列の入力チェック
		//------------------------------------------------
		
		
		// 収穫である場合はチェックしない
		let inputWork = document.getElementById("dropDownWork");
		
		if (inputWork === "1000010") {
			msgArea.innerHTML = '';
			return;
		}
		
		let inputVal = document.getElementById("colNo");
		let msgArea  = document.getElementById("colNoMsg");
		
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
			
		} else if (inputVal.value.length != 2) {
			//桁数チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】2ケタで入力してください 例：03</p>';
			return;
			
		} else if (inputVal.value == "00") {
			//桁数チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】列00は不正な入力です。入力し直してください 例：03</p>';
			return;
			
		}else{
			msgArea.innerHTML = '';
		}
	});
	
	$('#dropDownWork').change(function(){
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		//------------------------------------------------
		// 作業の入力チェック
		//------------------------------------------------
		
		let inputVal = document.getElementById("dropDownWork");
		let msgArea  = document.getElementById("dropDownWorkMsg");
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
	
	//_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
	$('#dropDownStartEmployee').change(function(){
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		//------------------------------------------------
		// 作業開始社員の入力チェック
		//------------------------------------------------
		
		let inputVal = document.getElementById("dropDownStartEmployee");
		let msgArea  = document.getElementById("dropDownStartEmployeeMsg");
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
	
	$("#startDate").change(function(){
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		//------------------------------------------------
		// 作業開始日の入力チェック
		//------------------------------------------------
		
		let inputValDate = document.getElementById("startDate");
		let inputValTime = document.getElementById("startTime");
		let msgArea  = document.getElementById("startDateTimeMsg");
		
		if (inputValDate.value.length == 0
		||  inputValTime.value.length == 0) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】入力必須です</p>';
			return;
		
		}else if (
			(inputValDate.value.length != 0 && inputValTime.value.length == 0)
		||  (inputValDate.value.length == 0 && inputValTime.value.length != 0)
		) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】日付と時間はセットで入力してください</p>';
			return;
			
		}else{
			msgArea.innerHTML = '';
		}
	});
	
	$("#startTime").change(function(){
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		let inputValDate = document.getElementById("startDate");
		let inputValTime = document.getElementById("startTime");
		let msgArea  = document.getElementById("startDateTimeMsg");
		
		if (inputValDate.value.length == 0
		||  inputValTime.value.length == 0) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】入力必須です</p>';
			return;
		
		}else if (
			(inputValDate.value.length != 0 && inputValTime.value.length == 0)
		||  (inputValDate.value.length == 0 && inputValTime.value.length != 0)
		) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】日付と時間はセットで入力してください</p>';
			return;
			
		}else{
			msgArea.innerHTML = '';
		}
	});
	
	//_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
	
	$("#dropDownEndEmployee").change(function(){
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		//------------------------------------------------
		// 作業終了社員の入力チェック
		//------------------------------------------------
		
		let inputVal = document.getElementById("dropDownEndEmployee");
		let msgArea  = document.getElementById("dropDownEndEmployeeMsg");
		//チェック不要
		msgArea.innerHTML = '';
		
	});
	
	$("#endDate").change(function(){
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		//------------------------------------------------
		// 作業終了日の入力チェック
		//------------------------------------------------
		
		let inputValDate = document.getElementById("endDate");
		let inputValTime = document.getElementById("endTime");
		let msgArea  = document.getElementById("endDateTimeMsg");
		if (
			(inputValDate.value.length != 0 && inputValTime.value.length == 0)
		||  (inputValDate.value.length == 0 && inputValTime.value.length != 0)
		) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】日付と時間はセットで入力してください</p>';
			return;
			
		}else{
			msgArea.innerHTML = '';
		}
	});
	
	$("#endTime").change(function(){
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		//------------------------------------------------
		// 作業終了時分の入力チェック
		//------------------------------------------------
		
		let inputValDate = document.getElementById("endDate");
		let inputValTime = document.getElementById("endTime");
		let msgArea  = document.getElementById("endDateTimeMsg");
		if (
			(inputValDate.value.length != 0 && inputValTime.value.length == 0)
		||  (inputValDate.value.length == 0 && inputValTime.value.length != 0)
		) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】日付と時間はセットで入力してください</p>';
			return;
			
		}else{
			msgArea.innerHTML = '';
		}
	});
	
	//_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
	
	
	
	$('input[name$="percent"]').on("input", function() {
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		//------------------------------------------------
		// 進捗の入力チェック
		//------------------------------------------------
		
		let inputVal = document.getElementById("percent");
		let msgArea  = document.getElementById("percentMsg");
		
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
		
	
	$('input[name$="boxCount"]').on("input", function() {
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		//------------------------------------------------
		// ケース数の入力チェック
		//------------------------------------------------
		
		
		let inputVal  = document.getElementById("boxCount");
		let msgArea   = document.getElementById("boxCountMsg");
		let inputWork = document.getElementById("dropDownWork");
		
		console.log("□作業は: " + inputWork.value);
		
		// 収穫"以外"である場合はチェックしない
		if (inputWork.value != "1000010") {
			msgArea.innerHTML = '';
			return;
		}
		
		
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
	// 入力チェック関数
	//
	
	function inputCheck() {
		
		
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
		// 列の入力チェック
		//------------------------------------------------
		
		// 収穫である場合はチェックしない
		let inputWork = document.getElementById("dropDownWork");
		
		if (inputWork === "1000010") {
			msgArea.innerHTML = '';
			return;
		}
		
		inputVal = document.getElementById("colNo");
		msgArea  = document.getElementById("colNoMsg");
		
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
			
		} else if (inputVal.value.length != 2) {
			//桁数チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】2ケタで入力してください 例：03</p>';
			checkOKFlg = false;
			
		} else if (inputVal.value == "00") {
			//桁数チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】列00は不正な入力です。入力し直してください 例：03</p>';
			checkOKFlg = false;
			
		}else{
			msgArea.innerHTML = '';
		}
		
		
		//------------------------------------------------
		// 作業の入力チェック
		//------------------------------------------------
		
		inputVal = document.getElementById("dropDownWork");
		msgArea  = document.getElementById("dropDownWorkMsg");
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
		// 作業開始社員の入力チェック
		//------------------------------------------------
		
		inputVal = document.getElementById("dropDownStartEmployee");
		msgArea  = document.getElementById("dropDownStartEmployeeMsg");
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
		// 作業開始日・時分の入力チェック
		//------------------------------------------------
		
		let inputValDate = document.getElementById("startDate");
		let inputValTime = document.getElementById("startTime");
		msgArea  = document.getElementById("startDateTimeMsg");
		
		if (inputValDate.value.length == 0
		||  inputValTime.value.length == 0) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】入力必須です</p>';
			checkOKFlg = false;
		
		}else if (
			(inputValDate.value.length != 0 && inputValTime.value.length == 0)
		||  (inputValDate.value.length == 0 && inputValTime.value.length != 0)
		) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】日付と時間はセットで入力してください</p>';
			checkOKFlg = false;
			
		}else{
			msgArea.innerHTML = '';
		}
		
		
		//------------------------------------------------
		// 作業終了社員の入力チェック
		//------------------------------------------------
		
		inputVal = document.getElementById("dropDownEndEmployee");
		msgArea  = document.getElementById("dropDownEndEmployeeMsg");
		//チェック不要
		
		
		
		//------------------------------------------------
		// 作業終了日・時分の入力チェック
		//------------------------------------------------
		
		inputValDate = document.getElementById("endDate");
		inputValTime = document.getElementById("endTime");
		msgArea  = document.getElementById("endDateTimeMsg");
		if (
			(inputValDate.value.length != 0 && inputValTime.value.length == 0)
		||  (inputValDate.value.length == 0 && inputValTime.value.length != 0)
		) {
			//未入力チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】日付と時間はセットで入力してください</p>';
			checkOKFlg = false;
			
		}else{
			msgArea.innerHTML = '';
		}
		
		
		//------------------------------------------------
		// 進捗の入力チェック
		//------------------------------------------------
		
		inputVal = document.getElementById("percent");
		msgArea  = document.getElementById("percentMsg");
		
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
			
		} else if (inputVal.value > 300) {
			//範囲チェック
			msgArea.innerHTML = '<p class="msg-ng">【NG】0～300の範囲内で入力してください</p>';
			checkOKFlg = false;
			
		}else{
			msgArea.innerHTML = '';
		}
		
		//------------------------------------------------
		// ケース数の入力チェック
		//------------------------------------------------
		
		inputVal  = document.getElementById("boxCount");
		msgArea   = document.getElementById("boxCountMsg");
		inputWork = document.getElementById("dropDownWork");
		
		console.log("□作業は: " + inputWork.value);
		
		// 収穫"以外"である場合はチェックしない
		if (inputWork.value != "1000010") {
			msgArea.innerHTML = '';
			
		}else{
			
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
		document.getElementById("workId").value           = document.getElementById("dropDownWork").value;
		document.getElementById("startEmployeeId").value  = document.getElementById("dropDownStartEmployee").value;
		document.getElementById("startDateTime").value    = editDateTimeValue("startDate", "startTime"); //自作関数でyyyy/MM/dd hh-mm-ss 形式に変換
		
		document.getElementById("endEmployeeId").value    = document.getElementById("dropDownEndEmployee").value;
		document.getElementById("endDateTime").value      = editDateTimeValue("endDate", "endTime");     //自作関数でyyyy/MM/dd hh-mm-ss 形式に変換
		
		
		
	}
	
	
	//------------------------------------------------
	// 日時の編集
	//
	
	function editDateTimeValue(strDateInputAreaName,strTimeInputAreaName) {
		
		
		let dateInputVal   = document.getElementById(strDateInputAreaName).value
		let timeInputVal   = document.getElementById(strTimeInputAreaName).value;
		
		//日時のいずれかが入力されていない場合は空白を返却
		if (dateInputVal == "" || timeInputVal == "") {
			return "";
		}
		
		// 日付と時間を組み合わせてDateオブジェクトを作成
		var combinedDateTime = new Date(dateInputVal + "T" + timeInputVal);
		
		// yyyy/MM/dd hh:mm:ss 形式に整形
		//
		//【メモ】サーバ側で以下のように変数宣言されてるからその形式に合わせる
		//     @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
		//     private LocalDateTime startDateTime;
		//
		var formattedDateTime = combinedDateTime.getFullYear() + '/' + 
								 ('0' + (combinedDateTime.getMonth() + 1)).slice(-2) + '/' + 
								 ('0' + combinedDateTime.getDate()).slice(-2) + ' ' + 
								 ('0' + combinedDateTime.getHours()).slice(-2) + ':' + 
								 ('0' + combinedDateTime.getMinutes()).slice(-2) + ':' + 
								 ('0' + combinedDateTime.getSeconds()).slice(-2);
		
		
		
		console.log("□編集した日時=[" + formattedDateTime + "]");
		
		return formattedDateTime;
		
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
		
		form.action="/matsuoka/TransitionKanriMainteWorkStatusListSearch";
    	form.method="post";
    	form.submit();
	});
	
	//------------------------------------------------
	// 復旧ボタン押下イベント
	//
	
	$('.ymd-kanri-button a[name$="revival"]').click(function() {
		
		//alert("新規");
		//console.log("■" + $(this).closest('div[name="houseData"]').find('input[name$="houseId"]').val());
		
		//入力チェックNGの場合は処理終了
		if (inputCheck() == false) {
			alert('入力チェックでエラーがあります。画面赤字箇所の指示に従い入力を修正してください。');
			return;
		}
		
		// hidden項目に選択した内容をセットしフォームをPOST送信
		document.getElementById("buttonKbn").value   = "revival";
		
		// 入力項目をhidden領域に編集(自作関数)
		editInputValToHiddenArea();
		
		//画面内にformタグは１つしかないため０番目を固定で取得
		let form = document.getElementsByTagName('form')[0];
		
		form.action="/matsuoka/EditKanriWorkStatus";
    	form.method="post";
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
		
		// 入力項目をhidden領域に編集(自作関数)
		editInputValToHiddenArea();
		
		//画面内にformタグは１つしかないため０番目を固定で取得
		let form = document.getElementsByTagName('form')[0];
		
		form.action="/matsuoka/EditKanriWorkStatus";
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
		
		// 入力項目をhidden領域に編集(自作関数)
		editInputValToHiddenArea();
		
		//画面内にformタグは１つしかないため０番目を固定で取得
		let form = document.getElementsByTagName('form')[0];
		
		form.action="/matsuoka/EditKanriWorkStatus";
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
		
		// 入力項目をhidden領域に編集(自作関数)
		editInputValToHiddenArea();
		
		//画面内にformタグは１つしかないため０番目を固定で取得
		let form = document.getElementsByTagName('form')[0];
		
		form.action="/matsuoka/EditKanriWorkStatus";
    	form.method="post";
    	form.submit();
	});
	
	
});
