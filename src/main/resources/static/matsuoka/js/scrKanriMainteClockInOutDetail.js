/*
JQueryで各種イベント操作

*/
$(function() {
	
	//□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□
	//
	// 画面描画直後の処理
	
	
	// 出勤日が入力されており、退勤日が未入力である場合、同じ日付を退勤日にセットする（入力補助）
	
	let clockInDate  = document.getElementById("startDate").value;
	let clockOutDate = document.getElementById("endDate").value;
	
	//console.log("■clockInDate=[" + clockInDate +"]、clockOutDate=[" + clockOutDate + "]");
	if (clockInDate != "" && clockOutDate == "") {
		//console.log("■セット！！");
		document.getElementById("endDate").value = clockInDate;
	}
	
	
	//□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□
	//
	// 入力変更イベント
	
	
	
	//_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
	$('#dropDownEmployee').change(function(){
		// ここに変更時の処理を記述します
		console.log("入力が変更されました。新しい値は: " + $(this).val());
		
		//------------------------------------------------
		// 社員の入力チェック
		//------------------------------------------------
		
		let inputVal = document.getElementById("dropDownEmployee");
		let msgArea  = document.getElementById("dropDownEmployeeMsg");
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
		// 社員の入力チェック
		//------------------------------------------------
		
		let inputVal = document.getElementById("dropDownEmployee");
		let msgArea  = document.getElementById("dropDownEmployeeMsg");
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
		
		
		document.getElementById("employeeId").value       = document.getElementById("dropDownEmployee").value;
		document.getElementById("startDateTime").value    = editDateTimeValue("startDate", "startTime"); //自作関数でyyyy/MM/dd hh-mm-ss 形式に変換
		document.getElementById("endDateTime").value      = editDateTimeValue("endDate"  , "endTime");   //自作関数でyyyy/MM/dd hh-mm-ss 形式に変換
		
		
		
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
		
		form.action="/matsuoka/TransitionKanriMainteClockInOutListSearch";
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
		
		form.action="/matsuoka/EditKanriClockInOut";
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
		
		form.action="/matsuoka/EditKanriClockInOut";
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
		
		form.action="/matsuoka/EditKanriClockInOut";
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
		
		form.action="/matsuoka/EditKanriClockInOut";
		form.method="post";
		form.submit();
	});
	
	
});
