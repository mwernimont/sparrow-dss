/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.usgswim.sparrow.service.maplayer;

import static gov.usgs.cida.sparrow.service.util.ServiceResponseMimeType.XML;
import gov.usgs.cida.sparrow.service.util.ServiceResponseOperation;
import gov.usgs.cida.sparrow.service.util.ServiceResponseStatus;
import static gov.usgs.cida.sparrow.service.util.ServiceResponseStatus.FAIL;
import gov.usgs.cida.sparrow.service.util.ServiceResponseWrapper;
import gov.usgswim.sparrow.action.CreateGeoserverLayer;
import gov.usgswim.sparrow.action.WriteDbfFileForContext;
import gov.usgswim.sparrow.domain.PredictionContext;
import gov.usgswim.sparrow.service.AbstractSparrowServlet;
import gov.usgswim.sparrow.service.SharedApplication;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.jndi.JndiTemplate;

/**
 *
 * @author eeverman
 */
public class GeoServerWMSEndPointService extends AbstractSparrowServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doActualGet(HttpServletRequest httpReq, HttpServletResponse resp)
			throws ServletException, IOException {

		//Since the WPS returns its own wrapper in all cases except an error,
		//this wrapper is only used for errors.
		ServiceResponseWrapper wrap = new ServiceResponseWrapper(
				String.class, ServiceResponseOperation.GET);
		wrap.setStatus(ServiceResponseStatus.FAIL);
		wrap.setMimeType(XML);
		
		String geoserverHost;
		int geoserverPort;
		String geoserverPath;
		String geoserverScheme;
		//We need to access these params to check if they exist, so we'll
		//do this initiation here.
		JndiTemplate template = new JndiTemplate();
		
		try {
			geoserverScheme = (String)template.lookup("java:comp/env/geoserver-scheme");
			geoserverHost = (String)template.lookup("java:comp/env/geoserver-host");
			geoserverPort = (Integer)template.lookup("java:comp/env/geoserver-port");
			geoserverPath = (String)template.lookup("java:comp/env/geoserver-path");
			
			URL url =  new URL(geoserverScheme, geoserverHost, geoserverPort, geoserverPath);
			wrap.setStatus(ServiceResponseStatus.OK);
			wrap.setEntityClass(String.class);
			wrap.addEntity(url.toExternalForm());
			
		} catch(Exception exception){
			wrap.setError(exception);
			wrap.setMessage("Maybe the JNDI configuration is unset for the GeoServer Location.");
		}



		
		//This only happens if there is an error
		sendResponse(resp, wrap);

	}

	@Override
	protected void doActualPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doActualGet(req, resp);
	}
	
}
