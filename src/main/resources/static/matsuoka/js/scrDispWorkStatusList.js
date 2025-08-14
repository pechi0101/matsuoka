

// 引数のミリ秒間スリープする関数
function sleep(ms) {
	return new Promise(resolve => setTimeout(resolve, ms));
}


/*
JQueryで各種イベント操作

*/
$(function() {
	
	
	
	// □□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□
	// ボタン押下・チェックボックス表示エリアに関するイベント
	
	
	//------------------------------------------------
	// チェックを終了ボタン押下イベント
	//
	
	$('.ymd-button a[name$="checkedWorEnd"]').click(function() {
		
		//------------------------------------------------
		// hidden項目に「どのボタンが押されたか」の情報をセットしフォームをPOST送信
		document.getElementById("pushButtonKbn").value         = "1";
		
		//------------------------------------------------
		
		//画面内にformタグは１つしかないため０番目を固定で取得
		let form = document.getElementsByTagName('form')[0];
		
		//form.action="";     // HTML内で直接記載されているためココでのセットは不要
    	//form.method="post"; // HTML内で直接記載されているためココでのセットは不要
    	form.submit();
	});
	
	
	//------------------------------------------------
	// 全て終了ボタン押下イベント
	//
	
	$('.ymd-button a[name$="allWorkEnd"]').click(async function() {
		
		
		//------------------------------------------------
		// 画面上の全てのチェックボックスをONにする
		
		var rows = $('[name="detail-row"]');
		
		console.log("□□□LOOP開始------------------------------------------------");
		
		// 各行をループ処理
		for (const row of rows) {
			
			await sleep(100);// 0.1秒待つ　演出
			// チェックボックスをONにする
			$(row).find('input[type="checkbox"]').prop('checked', true);
			
		}
		
		await sleep(300);// 0.3秒待つ　演出
		
		if (confirm("全ての作業を強制的に「完了」状態にさせます。ホントに大丈夫ですか？")) {
			// 「はい」が押されたとき
			//console.log("処理継続");
		} else {
			// 「いいえ」が押されたとき
			//console.log("処理中断");
			
			//全てのチェックを一旦外す
			for (const row of rows) {
				// チェックボックスをOFFにする
				$(row).find('input[type="checkbox"]').prop('checked', false);
			}
			return;
		}
		
		
		//------------------------------------------------
		// hidden項目に「どのボタンが押されたか」の情報をセットしフォームをPOST送信
		document.getElementById("pushButtonKbn").value         = "2";
		
		//------------------------------------------------
		
		//画面内にformタグは１つしかないため０番目を固定で取得
		let form = document.getElementsByTagName('form')[0];
		
		//form.action="";     // HTML内で直接記載されているためココでのセットは不要
    	//form.method="post"; // HTML内で直接記載されているためココでのセットは不要
    	form.submit();
	});
	
	
	//------------------------------------------------
	// 閉じるボタン押下イベント
	//
	
	$('.ymd-button a[name$="close"]').click(function() {
		
		//------------------------------------------------
		// hidden項目に「どのボタンが押されたか」の情報をセットしフォームをPOST送信
		document.getElementById("pushButtonKbn").value         = "9";
		
		//------------------------------------------------
		
		//画面内にformタグは１つしかないため０番目を固定で取得
		let form = document.getElementsByTagName('form')[0];
		
		//form.action="";     // HTML内で直接記載されているためココでのセットは不要
    	//form.method="post"; // HTML内で直接記載されているためココでのセットは不要
    	form.submit();
	});
	
	
});
