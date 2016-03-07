(function(){
	Ext.ns('Sparrow.index');

	var templater = {};
	Sparrow.index.ModelTemplater = templater;
	
	/**
	 * given a list of models, return an html list of all of them
	 */
	templater.listOfModels = function(models){
		return '<div>test list</div>';
	};
	
	/**
	 * given a model, return an html div displaying the model details
	 */
	templater.modelDetails = function(model){
		return '<div>test details</div>';
	};
	
	

}());