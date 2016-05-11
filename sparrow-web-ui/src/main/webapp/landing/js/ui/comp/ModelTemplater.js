(function(){
	Ext.ns('Sparrow.index');

	var templater = {};
	Sparrow.index.ModelTemplater = templater;
	var templateCache = {};
	
	Ext.onReady(function(){
		templateCache.modelList = Handlebars.compile(document.getElementById('model-list-template').innerHTML);
		templateCache.modelDetails = Handlebars.compile(document.getElementById('model-details-template').innerHTML);
	});
	
	/**
	 * given a list of models, return an html list of all of them
	 */
	templater.listOfModels = function(models){
		var output = templateCache.modelList(models);
		return output;
	};
	
	/**
	 * given a model, return an html div displaying the model details
	 */
	templater.modelDetails = function(model){
		var output = templateCache.modelDetails(model);
		return output;
	};

}());