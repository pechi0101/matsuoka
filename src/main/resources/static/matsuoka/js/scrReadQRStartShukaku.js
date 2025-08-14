document.getElementById('dispWorkStatusList').addEventListener('click', function() {

	//------------------------------------------------
	
	//画面内にformタグは１つしかないため０番目を固定で取得
	let form = document.getElementsByTagName('form')[0];
	
	form.action="/matsuoka/DispWorkStatusList"; // 作業状況一覧画面へ遷移するためのコントローラメソッドを指定
	form.method="post";
	form.submit();
});

