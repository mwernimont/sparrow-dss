var CONTROLLER;
Ext.onReady(function() {

	//A requirement of using Ext.History
	Ext.History.init();
	
	loadModels(false, false, false, function(response, request){
		var models = Ext.util.JSON.decode(response.responseText);
		CONTROLLER = new Sparrow.index.Controller({region : 'Any', parameter : 'Any', models: models.models.model});
		CONTROLLER.readStateFromHistory();
		Ext.each(Screencasts, function(screencast){
			var opt = document.createElement('option');
			opt.text = screencast.name;
			opt.value = screencast.videoId;

			document.getElementById('tutorial-video-select').appendChild(opt);
		});

		//Instrument the go button for the videos
		var videoGoBtn = document.getElementById('tutorial-video-go-button');
		videoGoBtn.onclick = function() {
			var videoId = document.getElementById('tutorial-video-select').value;

			if (videoId != '') {
				Screencast.open(videoId);
			}
		};
	});
});