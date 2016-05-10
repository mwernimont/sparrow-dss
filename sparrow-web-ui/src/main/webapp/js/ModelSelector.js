function loadModels(public, approved, archived, callback){
	    var xmlreq = ''
        + '<?xml version="1.0" encoding="ISO-8859-1" ?>'
        + '<sparrow-meta-request '
        + '  xmlns="http://www.usgs.gov/sparrow/meta_request/v0_1" '
        + '  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">'
        + '  <model public="$public$" archived="$archived$" approved="$approved$" />'
        + '</sparrow-meta-request>'
        ;
    xmlreq = xmlreq.replace("$public$", public);
    xmlreq = xmlreq.replace("$approved$", approved);
    xmlreq = xmlreq.replace("$archived$", archived);
    
    
    // Send a request to the model service for a list of public models
    Ext.Ajax.request({
        url: 'getModels',
        params: 'xmlreq=' + xmlreq + '&mimetype=json',
        success: callback,
        failure: function(response, options) {
            Ext.MessageBox.alert('Failed', 'Unable to connect to SPARROW.');
        }
    });
};