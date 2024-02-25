window.SQR = window.SQR || {}

var s_time = new Date();
var e_time = null;
var diff   = null;

SQR.reader = (() => {
	/**
	 * getUserMedia()に非対応の場合は非対応の表示をする
	 */
	const showUnsuportedScreen = () => {
		document.querySelector('#js-unsupported').classList.add('is-show')
	}
	if (!navigator.mediaDevices) {
		showUnsuportedScreen()
		return
	}
	
	// 背景用videoタグ
	const video2 = document.querySelector('#js-video2')
	// QRコード読取用videoタグ
	const video = document.querySelector('#js-video')
	
	 /**
	 * videoの出力をCanvasに描画して画像化 jsQRを使用してQR解析
	 */
	const checkQRUseLibrary = () => {
		
		/*
		e_time = new Date();
		diff = e_time.getTime() - s_time.getTime();
		console.log("■" +  diff +  "■checkQRUseLibrary");
		*/
		
		const canvas = document.querySelector('#js-canvas')
		// 引数"2d"を渡して実行すると、2Dグラフィックを描画するためのメソッドやプロパティをもつオブジェクトを返却
		const ctx = canvas.getContext('2d');
		
		//【参考】https://developer.mozilla.org/ja/docs/Web/API/CanvasRenderingContext2D/drawImage
		//キャンバスにvideoから切取った画像を描画する
		ctx.drawImage(video, 0, 0, canvas.width, canvas.height)
		
		//【参考】
		//キャンバスの指定の部分に対応するピクセルデータを表す ImageData オブジェクトを返却
		const imageData = ctx.getImageData(0, 0, canvas.width, canvas.height)
		//const imageData = ctx.getImageData(50, 100, canvas.width - 50, canvas.height-100);
		
		
		//★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
		//jsQRに定義されたisQR関数を使用して画像の中からQRコードから読み取った情報を取得する
		const code = jsQR(imageData.data, canvas.width, canvas.height)
		
		if (code) {
			
			// ★★★★★当JavaScriptの↓に記載してるSQR.modal.open関数をコール★★★★★
			// ※code.dataがQRコードから読み取った情報（URL等）
			SQR.modal.open(code.data)
		} else {
			setTimeout(checkQRUseLibrary, 10)
		}
		
	}
	
	
	
	
	
	/**
	 * videoの出力をBarcodeDetectorを使用してQR解析
	 * ※androidの場合はこっちに来るみたい...
	 */
	const checkQRUseBarcodeDetector = () => {
		
		/*
		e_time = new Date();
		diff = e_time.getTime() - s_time.getTime();
		console.log("■" +  diff +  "■checkQRUseBarcodeDetector");
		*/
		
		
		/*
		const barcodeDetector = new BarcodeDetector();
		const canvas = document.querySelector('#js-canvas')
		
		// 引数"2d"を渡して実行すると、2Dグラフィックを描画するためのメソッドやプロパティをもつオブジェクトを返却
		const ctx = canvas.getContext('2d');
		
		//【参考】https://developer.mozilla.org/ja/docs/Web/API/CanvasRenderingContext2D/drawImage
		//キャンバスにvideoから切取った画像を描画する
		ctx.drawImage(video, 0, 0, canvas.width, canvas.height);
		
		//【参考】
		//キャンバスの指定の部分に対応するピクセルデータを表す ImageData オブジェクトを返却
		const imageData = ctx.getImageData(0, 0, canvas.width, canvas.height);
		//const imageData = ctx.getImageData(50, 50, canvas.width-50, canvas.height-50);
		
		barcodeDetector
			.detect(imageData, canvas.width, canvas.height)
			.then((barcodes) => {
				
				console.log("★★006 [" + barcodes.length + "]個だよ～～");
				
				if (barcodes.length > 0) {
					//console.log("★★007");
				
					for (let barcode of barcodes) {
						//★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
						// ★★★★★当JavaScriptの↓に記載してるSQR.modal.open関数をコール★★★★★
						// ※barcode.rawValueがQRコードから読み取った情報（URL等）
						SQR.modal.open(barcode.rawValue);
					}
				} else {
					setTimeout(checkQRUseBarcodeDetector, 10);
				}
			})
			.catch(() => {
				console.log("★★★ERROR★★★");
				console.error('Barcode Detection failed, boo.');
			})
		*/
		
		
		
		const barcodeDetector = new BarcodeDetector();
		
		barcodeDetector
			.detect(video)
			.then((barcodes) => {
				
				//console.log("★★002 [" + barcodes.length + "]");
				if (barcodes.length > 0) {
					for (let barcode of barcodes) {
						//★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
						// ★★★★★当JavaScriptの↓に記載してるSQR.modal.open関数をコール★★★★★
						// ※barcode.rawValueがQRコードから読み取った情報（URL等）
						SQR.modal.open(barcode.rawValue)
					}
				} else {
					setTimeout(checkQRUseBarcodeDetector, 10)
				}
			})
			.catch(() => {
				//console.log("★001");
				console.error('Barcode Detection failed, boo.')
			})
		
	}

	/**
	 * BarcodeDetector APIを使えるかどうかで処理を分岐
	 */
	const findQR = () => {
		
		
		window.BarcodeDetector
			? checkQRUseBarcodeDetector()
			: checkQRUseLibrary()
		
	}

	/**
	 * デバイスのカメラを起動
	 */
	const initCamera = () => {
		
		
		var useDeviceLabel = '';
		var useDeviceId = '';
		
		/*
		e_time = new Date();
		diff = e_time.getTime() - s_time.getTime();
		console.log("■" +  diff +  "■initCamera★★★");
		*/
		
		console.log("■開始■");
		
		// このメソッドを呼び出すことでユーザーにブラウザがカメラを使用することを許可するかの確認ダイアログが表示され、
		// 許可されれば thenの処理が行われる
		
		
		if (!navigator.mediaDevices?.enumerateDevices) {
			console.log("enumerateDevices() not supported.");
			showUnsuportedScreen();
			return;
		}
		
		
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
				
				const os = getMobileOS();
				
				console.log("■デバイスの数=[" + devices.length + "]件、OS=[" + os + "]");
				
				devices.forEach((device) => {
					
					// 標準レンズのデバイスＩＤを取得
					// ------------------------------------------------
					// ■Android(山田の３眼スマホ)
					// camera2 1, facing front前面カメラ
					// camera2 4, facing back 何か暗いレンズ
					// camera2 3, facing back 望遠レンズ
					// camera2 2, facing back 接眼レンズ
					// camera2 0, facing back 標準レンズ★
					// ------------------------------------------------
					// ■Android(山田の１眼スマホ)
					// camera2 1, facing front前面カメラ
					// camera2 0, facing back 標準レンズ★
					// ------------------------------------------------
					// ■iPhone(小大さんのヤツ)
					// 前面カメラ
					// 背面デュアル広角カメラ
					// 背面超広角カメラ
					// 背面カメラ             標準レンズ★
					// ------------------------------------------------
					// ■iPhone(松岡さんのヤツ)
					// 前面カメラ
					// 背面カメラ             標準レンズ★
					// 背面望遠カメラ
					// 背面超広角カメラ
					// デスクビューカメラ     Mac BookにiPhoneを付けた状態でタイプしてる手元を映すことができるカメラ
					// ------------------------------------------------
					if (os == 'Android') {
						
						if (device.kind == 'videoinput' && device.label.indexOf('camera2 0, facing back') === 0) {
							useDeviceLabel = device.label; 
							useDeviceId    = device.deviceId;
						}
						
					} else if (os == 'iOS') {
						
						if (device.kind == 'videoinput' && device.label.indexOf('背面カメラ') === 0) {
							useDeviceLabel = device.label; 
							useDeviceId    = device.deviceId;
						}
						
					} else {
						
						
						
					}
					
					
					
					console.log(`${device.kind}: □${device.label}□ id = ${device.deviceId}`);
				});
				
				
				console.log("■OK■利用カメラﾗﾍﾞﾙ＝[" + useDeviceLabel + "]");
				console.log("■OK■利用カメラＩＤ＝[" + useDeviceId    + "]");
				
				
				//正しいデバイスＩＤでデバイスを起動するために、一旦カメラデバイスのストリームを解放
				stream.getTracks().forEach(track => track.stop());
				
				
				//正しいデバイスＩＤでカメラデバイスを起動
				navigator.mediaDevices
				.getUserMedia({
					audio: false,   // マイクデバイスの使用許可は求めない
					video: {
						deviceId: {exact: useDeviceId}
						}
					})
				.then((stream) => {
					
					// 背景用videoタグ（モザイク）撮影開始
					video2.srcObject = stream
					video2.onloadedmetadata = () => {
						video2.play()
					}
					// QRコード読取用videoタグ撮影開始
					video.srcObject = stream
					video.onloadedmetadata = () => {
						video.play()
						findQR()
					}
				})
				.catch(() => {
					showUnsuportedScreen()
				})
				
			})
			.catch((err) => {
				console.error(`${err.name}: ${err.message}`);
			});
			
			
			
		})
		.catch(() => {
			showUnsuportedScreen()
		})
		
	}
	
	const getMobileOS = () => {
		/*
		説明した関数の中で使用している/android/i.test(ua)を例に説明
		
		/android/ が正規表現の本体。
		/android/i のiは正規表現のフラグで大文字小文字を判定しない。
		AndroidやANDROID,aNdRoIdに対応することになる。
		
		/android/iでは検証する文字列のどこかに大文字小文字を気にしないで
		androidを含む文字列があるかどうかを判定する。
		
		/android/i.test(ua) のtestメソッドは引数の文字列uaに含まれる文字列
		が/android/iの正規表現にヒットするかどうかをtrue/falseで返す
		*/
		const ua = navigator.userAgent
		if (/android/i.test(ua)) {
			
			return "Android";
		
		} else if (
		   (/iPad|iPhone|iPod/.test(ua)) 
		|| (navigator.platform === 'MacIntel' && navigator.maxTouchPoints > 1)
		){
			
			return "iOS";
		}
	
		return "Other";
	}

	// initCamera関数とfindQR関数はSQR.readerの外から「SQR.reader.initCamera(....)」「SQR.reader.findQR(....)」の形式でコールしたいためreturnで返却している
    return {
        initCamera,
        findQR,
    }
})()


SQR.modal = (() => {
	const qrcode             = document.querySelector('#qrcode');
	const formRegist       = document.querySelector('form[name="registQRForm"]');
	
	
	const open = (url) => {
		
		
		
		//loginEmployeeId.value   = "10001"
		//loginEmployeeName.value = "山田"
		//取得したＱＲコードの文字列
		qrcode.value = url
		
		e_time = new Date();
		diff = e_time.getTime() - s_time.getTime();
		console.log("■" +  diff +  "■ＱＲコード読んだ！=[" + qrcode.value + "]");
		
		//alert("読取った!!" + formRegist.action);
		
		
		//データをサーバのSpringBootにPOST送信
		formRegist.submit();
		
	}
	
	// open関数はSQR.modalの外から「SQR.modal.open(....)」の形式でコールしたいためreturnで返却している
	return {
	open,
	}
})()



/*
SQR.modal = (() => {
    const modal = document.querySelector('#js-modal')            // モーダルで開くdivタグ領域
    const result = document.querySelector('#js-result')          // 読み取り結果のtextarea領域
    const modalClose = document.querySelector('#js-modal-close') // 閉じるボタン

    
	// 取得した文字列を「読み取り結果」領域に表示(←テスト用)
	// 取得した文字列をAjaxでサーバ処理にPOST送信。（データをDBに登録する）
	// 送信結果の「成功／失敗」を伝える文言をセットしてモーダル画面を開く
    const open = (url) => {
        
        //取得した文字列を「読み取り結果」領域に表示
        result.value = url
        modal.classList.add('is-show')
        
        
        //------------------------------------------------
        //Ajax通信
        var data = {
			companyId: '10001'
			,registEmployeeId: '11001'
			,qrcode: url
		};
		
		
		$.ajax({
			url: '/test02/RegistQR', //←サーバ側コントローラメソッドのurl：@PostMapping("/test02/customerAjaxTest")のカッコ内の文字列をセット
			method: 'post',
			data: JSON.stringify(data),
			contentType: 'application/json', 
			dataType: "json",
			cache: false
		}).done(function(data, status, jqxhr) {
			
			//ajax通信による非同期処理正常終了時の処理
			result.value = "成功！" + url;
			
		}).fail(function(data, status, jqxhr, errorThrown) {
			//ajax通信による非同期処理異常終了時の処理
			result.value = "失敗！" + url;
			alert('送信エラー' + status + '■' + jqxhr+ '■' + errorThrown);
		});
    	
        
    }
	
    // 閉じるボタン押下時の処理
    // モーダルを閉じてQR読み込みを再開
    const close = () => {
        modal.classList.remove('is-show')
        SQR.reader.findQR()
    }

	//モーダル画面の「閉じるボタン」に押下イベントを設定
    modalClose.addEventListener('click', () => close())
	
	// open関数はSQR.modalの外から「SQR.modal.open(....)」の形式でコールしたいためreturnで返却している
    return {
        open,
    }
})()
*/


/*
SQR.modal = (() => {
    const modal = document.querySelector('#js-modal')            // モーダルで開くdivタグ領域
    const result = document.querySelector('#js-result')          // 読み取り結果のtextarea領域
    const link = document.querySelector('#js-link')              // 開くボタン
    const copyBtn = document.querySelector('#js-copy')           // コピーボタン
    const modalClose = document.querySelector('#js-modal-close') // 閉じるボタン

    
	// 取得した文字列を「読み取り結果」領域に表示してモーダルを開く
    const open = (url) => {
        result.value = url
        link.setAttribute('href', url)
        modal.classList.add('is-show')
    }

    // 閉じるボタン押下時の処理
    // モーダルを閉じてQR読み込みを再開
    const close = () => {
        modal.classList.remove('is-show')
        SQR.reader.findQR()
    }

    // コピーボタン押下時の処理
    const copyResultText = () => {
        result.select()
        document.execCommand('copy')
    }
	
	//モーダル画面の「コピーボタン」「閉じるボタン」に押下イベントを設定
    copyBtn.addEventListener('click', copyResultText)
    modalClose.addEventListener('click', () => close())
	
	// open関数はSQR.modalの外から「SQR.modal.open(....)」の形式でコールしたいためreturnで返却している
    return {
        open,
    }
})()
*/

// カメラの初期化処理を起動
if (SQR.reader) SQR.reader.initCamera()
