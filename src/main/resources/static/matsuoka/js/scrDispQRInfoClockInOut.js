
/*
JQueryで各種イベント操作
*/
$(function() {
	
	
	//□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□
	//
	// 入力変更イベント
	
	
	$("#inputClockInDate").change(function(){
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は...: " + $(this).val());
		
		//------------------------------------------------
		// 出勤日の入力チェック
		//------------------------------------------------
		
		let inputValDate = document.getElementById("inputClockInDate");
		let inputValTime = document.getElementById("inputClockInTime");
		let msgArea  = document.getElementById("inputClockInDateTimeMsg");
		
		console.log("■inputValTime: " + inputValTime.value);
		
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
	
	$("#inputClockInTime").change(function(){
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		//------------------------------------------------
		// 出勤時分の入力チェック
		//------------------------------------------------
		
		let inputValDate = document.getElementById("inputClockInDate");
		let inputValTime = document.getElementById("inputClockInTime");
		let msgArea  = document.getElementById("inputClockInDateTimeMsg");
		
		console.log("■inputValTime: " + inputValTime.value);
		
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
	
	
	
	$("#inputClockOutDate").change(function(){
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		//------------------------------------------------
		// 退勤日の入力チェック
		//------------------------------------------------
		
		let inputValDate = document.getElementById("inputClockOutDate");
		let inputValTime = document.getElementById("inputClockOutTime");
		let msgArea  = document.getElementById("inputClockOutDateTimeMsg");
		
		console.log("■inputValTime: " + inputValTime.value);
		
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
	
	$("#inputClockOutTime").change(function(){
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		//------------------------------------------------
		// 退勤時分の入力チェック
		//------------------------------------------------
		
		let inputValDate = document.getElementById("inputClockOutDate");
		let inputValTime = document.getElementById("inputClockOutTime");
		let msgArea  = document.getElementById("inputClockOutDateTimeMsg");
		
		console.log("■inputValTime: " + inputValTime.value);
		
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
	
	
	
	
	//□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□
	//
	// ボタン押下イベント
	
	
	
	//------------------------------------------------
	// 入力チェック関数
	//
	
	function inputCheck() {
		
		
		let checkOKFlg = true;
		
		//------------------------------------------------
		// 出勤日・時分の入力チェック
		//------------------------------------------------
		
		let inputValDate = document.getElementById("inputClockInDate");
		let inputValTime = document.getElementById("inputClockInTime");
		msgArea  = document.getElementById("inputClockInDateTimeMsg");
		
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
		// 作業終了日・時分の入力チェック
		//------------------------------------------------
		
		inputValDate = document.getElementById("inputClockOutDate");
		inputValTime = document.getElementById("inputClockOutTime");
		msgArea  = document.getElementById("inputClockOutDateTimeMsg");
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
		
		
		
		return checkOKFlg;
		
	}
	
	
	//------------------------------------------------
	// 日時の編集
	//
	
	function editDateTimeValue(strDateInputAreaName,strTimeInputAreaName) {
		
		
		let dateInputVal   = document.getElementById(strDateInputAreaName).value
		let timeInputVal   = document.getElementById(strTimeInputAreaName).value;
		
		console.log("□日時の編集処理--------------");
		console.log("□editDateTimeValue.dateInputVal: " + dateInputVal);
		console.log("□editDateTimeValue.timeInputVal: " + timeInputVal);
		
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
	// ボタン押下イベント
	//
	
	$('.ymd-button a[name$="regist"]').click(function() {
		
		//alert("登録");
		//console.log("■" + $(this).closest('div[name="employeeData"]').find('input[name$="employeeId"]').val());
		
		
		//入力チェックNGの場合は処理終了
		if (inputCheck() == false) {
			alert('入力チェックでエラーがあります。画面赤字箇所の指示に従い入力を修正してください。');
			return;
		}
		
		
		// hidden項目に押下したボタンの情報セットしフォームをPOST送信
		document.getElementById("pushedButtunKbn").value  = "regist";
		document.getElementById("clockInDatetime").value  = editDateTimeValue("inputClockInDate" , "inputClockInTime");  //自作関数でyyyy/MM/dd hh-mm-ss 形式に変換
		document.getElementById("clockOutDatetime").value = editDateTimeValue("inputClockOutDate", "inputClockOutTime"); //自作関数でyyyy/MM/dd hh-mm-ss 形式に変換
		
		//画面内にformタグは１つしかないため０番目を固定で取得
		let form = document.getElementsByTagName('form')[0];
		
		//console.log("★編集後の日時＝" + document.getElementById("clockInDatetime").value)
		
		form.action="/matsuoka/RegistQRInfoClockInOut";
    	form.method="post";
    	form.submit();
    	
	});
	
	
	
	
	$('.ymd-button a[name$="cancel"]').click(function() {
		
		//alert("キャンセル");
		//console.log("■" + $(this).closest('div[name="employeeData"]').find('input[name$="employeeId"]').val());
		
		
		// hidden項目に押下したボタンの情報セットしフォームをPOST送信
		document.getElementById("pushedButtunKbn").value  = "cancel";
		
		
		//画面内にformタグは１つしかないため０番目を固定で取得
		let form = document.getElementsByTagName('form')[0];
		
		form.action="/matsuoka/RegistQRInfoClockInOut";
    	form.method="post";
    	form.submit();
    	
	});
	
});
