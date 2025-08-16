document.getElementById('dispWorkStatusList').addEventListener('click', function() {

	//------------------------------------------------
	
	//画面内にformタグは１つしかないため０番目を固定で取得
	let form = document.getElementsByTagName('form')[0];
	
	form.action="/matsuoka/DispWorkStatusList"; // 作業状況一覧画面へ遷移するためのコントローラメソッドを指定
	form.method="post";
	form.submit();
});




/*
JQueryで各種イベント操作

*/
$(function() {
	
	
	
	// □□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□
	// ボタン押下に関するイベント
	
	//------------------------------------------------
	// 作業状況表示ボタン押下イベント
	//
	
	$('.ymd-button a[name$="dispWorkStatusButton"]').click(function() {
		
		//------------------------------------------------
		
		//画面内にformタグは１つしかないため０番目を固定で取得
		let form = document.getElementsByTagName('form')[0];
		
		form.action="/matsuoka/DispWorkStatusMobile";
    	form.method="post";
    	form.submit();
	});
	
	
});
