(function(){
	Ext.ns('Sparrow.index');

	var templater = {};
	Sparrow.index.ModelTemplater = templater;
	
	/**
	 * given a list of models, return an html list of all of them
	 */
	templater.listOfModels = function(models){
		var modelList = '<div class="captioneddiv" xmlns:dct="http://purl.org/dc/terms/" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:gmd="http://www.isotc211.org/2005/gmd" xmlns:gco="http://www.isotc211.org/2005/gco">' +
			'<ol start="">'
		
		Ext.each(models, function(model){
			modelList += '<li><a href="javascript:(CONTROLLER.selectUUID("' + model['@id'] + '"))">' +
				model.name + '</a><hr>';
		});


		modelList += '</ol></div>';
			
		return modelList;
	};
	
	/**
	 * given a model, return an html div displaying the model details
	 */
	templater.modelDetails = function(model){
		return '<div>test details</div>';
	};
	
	

}());