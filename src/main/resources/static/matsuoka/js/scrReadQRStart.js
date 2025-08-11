document.getElementById('openPopup').addEventListener('click', function() {
  document.getElementById('popupOverlay').style.display = 'block';
});

document.getElementById('closePopup').addEventListener('click', function() {
	event.stopPropagation(); // ★ イベント伝播を止める(ポップアップの閉じるボタンをタップした時に、ポップアップ表示元の画面のボタンも自動的にタップされちゃうのを防ぐ)
	document.getElementById('popupOverlay').style.display = 'none';
});
