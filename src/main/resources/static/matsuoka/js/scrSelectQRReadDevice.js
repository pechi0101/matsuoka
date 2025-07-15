window.SQR = window.SQR || {}

var s_time = new Date();
var e_time = null;
var diff   = null;

$(function() {
    
    if (!navigator.mediaDevices) {
        console.log("■エラー■");
        return
    }
	
	
    /**
     * デバイスのカメラを起動
     */
    
	
	
	/*
	e_time = new Date();
	diff = e_time.getTime() - s_time.getTime();
	console.log("■" +  diff +  "■initCamera★★★");
	*/
	
	var deviceListArea = document.querySelector("#device-List");
	console.log("★開始★");
	
	// このメソッドを呼び出すことでユーザーにブラウザがカメラを使用することを許可するかの確認ダイアログが表示され、
	// 許可されれば thenの処理が行われる
	
	
	//deviceListArea.innerHTML = deviceListArea.innerHTML + "<br><span>■■001</span>"
	//const appVersion = navigator.appVersion;
	//deviceListArea.innerHTML = deviceListArea.innerHTML + "<br><span>■■002</span>"
	
	// ブラウザが利用可能であるプロパティをチェックし、サポート対象であるかを確認。
	const supports = navigator.mediaDevices.getSupportedConstraints();
	
	if (!supports.facingMode) {
		deviceListArea.innerHTML = deviceListArea.innerHTML + "<br>"
		deviceListArea.innerHTML = deviceListArea.innerHTML + "<span>【エラー】facingModeが指定できない！□</span><br>"
	}
	
	if (!supports.deviceId) {
		deviceListArea.innerHTML = deviceListArea.innerHTML + "<br>"
		deviceListArea.innerHTML = deviceListArea.innerHTML + "<span>【エラー】deviceIdが指定できない！□</span><br>"
	}
	
	
	
	
	if (!navigator.mediaDevices?.enumerateDevices) {
		console.log("enumerateDevices() not supported.");
		
		deviceListArea.innerHTML = deviceListArea.innerHTML + "<br>"
		deviceListArea.innerHTML = deviceListArea.innerHTML + "<span>【エラー】enumerateDevicesがサポートされてない！□</span><br>"
		deviceListArea.innerHTML = deviceListArea.innerHTML + "<br>"
		
	} else {
		
		
		// 最初の「デバイスへのアクセスを許可しますか？」をするためにデバイスＩＤを指定せずバックカメラを起動。
		// その後”標準”バックカメラのデバイスＩＤを取得し、そのデバイスＩＤで改めてカメラを起動する。
		// 【メモ】Andoroidで試したところ、デバイスＩＤを指定しないと「何か暗いレンズ」でカメラが起動してしまった。
		navigator.mediaDevices
		.getUserMedia({
			audio: false,   // マイクデバイスの使用許可を求めない
			video: {
				facingMode: {exact: 'environment'},
				}
			})
			.then((stream) => {
				
				
				// List cameras and microphones.
				navigator.mediaDevices.enumerateDevices()
				.then((devices) => {
					
					//console.log(("背面超広角カメラ".indexOf("カメラ")) != -1 && ("背面超広角カメラ".indexOf("背面")) != -1);
					
					devices.forEach((device) => {
						//
						if (
							device.kind == "videoinput" && device.label.indexOf("camera") != -1  && device.label.indexOf("back") != -1 //Android
						||  device.kind == "videoinput" && device.label.indexOf("カメラ") != -1  && device.label.indexOf("背面") != -1 //iPhoneの背面カメラ、背面超広角カメラなど
						||  device.kind == "videoinput" && device.label.indexOf("デスクビューカメラ") != -1    //iPhoneの背面広角カメラを利用したカメラ機能
						||  device.kind == "videoinput" && device.label.indexOf("トリプル") != -1              //iPhoneの背面にある３つのカメラ
						){
							deviceListArea.innerHTML = deviceListArea.innerHTML + "<div name='device-info' style='width: 100%;'>"
							                                                    + "<input type='hidden' name='deviceLabel' value='" + device.label + "'/>"
							                                                    + "<div class='ymd-button' style='width: 100%;'>"
							                                                    + "<a name='send'><span>"  + device.label + "</span></a>"
							                                                    + "</div>"
							                                                    + "</div>";
							deviceListArea.innerHTML = deviceListArea.innerHTML + "<br>";
							
							
						}
						
						console.log(`${device.kind}: □${device.label}□ id = ${device.deviceId}`);
					});
					
					//▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
					// ボタンにイベントをセット
					// ※JavaScriptで追加したボタンに対してイベントをセットする場合、ココにイベント処理を記述する必要あり
					
					$(function() {
						
						console.log("☆☆☆☆☆開始☆☆☆☆☆");
						
						//------------------------------------------------
						// ボタン押下イベント
						//
						
						$('.ymd-button a[name$="send"]').click(function() {
							
							
							console.log("JavaScript開始 send☆☆☆☆");
							//alert("変更");
							
							// hidden項目にボタンを押下したデバイスのラベルをセットしフォームをPOST送信
							document.getElementById("selectedDeviceLabel").value   = $(this).closest('div[name="device-info"]').find('input[name$="deviceLabel"]').val();
							
							// ローカルストレージから社員名のラベルを取得
							var varLoginEmployeeName     = localStorage.getItem('localStorage_LoginEmployeeName');
							document.getElementById("loginEmployeeName").value = varLoginEmployeeName;
							
							
							//document.getElementById("loginEmployeeName").value     = $(this).closest('div[name="employeeData"]').find('input[name$="employeeName"]').val();
							
							// ローカルストレージにカメラデバイスのラベルを保持
							localStorage.setItem("localStorage_SelectedDeviceLabel"  ,document.getElementById("selectedDeviceLabel").value);
							
							//画面内にformタグは１つしかないため０番目を固定で取得
							let form = document.getElementsByTagName('form')[0];
							
							//form.action="";     // HTML内で直接記載されているためココでのセットは不要
							//form.method="post"; // HTML内で直接記載されているためココでのセットは不要
							form.submit();
						});
						
					});
					//▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲
					
					
				})
				.catch((err) => {
					deviceListArea.innerHTML = deviceListArea.innerHTML + "<span>【エラー】エラー００１</span><br>"
					deviceListArea.innerHTML = deviceListArea.innerHTML + "<br>"
					console.error(`${err.name}: ${err.message}`);
				});
				
				
				
			})
			.catch((err) => {
				deviceListArea.innerHTML = deviceListArea.innerHTML + "<span>【エラー】エラー００２</span><br>"
				deviceListArea.innerHTML = deviceListArea.innerHTML + "<br>"
				console.error(`${err.name}: ${err.message}`);
			})
		
	}
	
	
});







