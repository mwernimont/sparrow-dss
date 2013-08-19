package gov.usgswim.sparrow.service;

import static gov.usgswim.sparrow.service.ServiceResponseMimeType.*;


import gov.usgswim.sparrow.action.Action;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class ServletResponseParser {
	protected static Logger log =
		Logger.getLogger(ServletResponseParser.class); //logging for this class
	
	
	HttpServletRequest req;
	String content;
	ServiceResponseMimeType type;
	Object parsedObject;
	String errorMessage;
	
	boolean contentParsed = false;
	boolean objectParsed = false;
	
	public ServletResponseParser(HttpServletRequest req) {
		this.req = req;
	}
	
	public String getContent() {
		
		if (!contentParsed) parse();
		
		return content;
	}
	
	public ServiceResponseMimeType getType() {
		
		if (!contentParsed) parse();
		
		return type;
	}
	
	public Object getAsObject() {
		
		if (!objectParsed) parseObject();
		return parsedObject;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	private void parseObject() {
		if (!contentParsed) parse();
		
		Object entity = null;

		
		try {
			if (! UNKNOWN.equals(type) && content != null) {
				switch (type) {
				case XML:
					entity = getXMLXStream().fromXML(content);
					break;
				case JSON:
					entity = getJSONXStreamReader().fromXML(content);
					break;
				default:
					errorMessage = "Unrecognized content type.";
				}
			}
		} catch (Throwable e) {
			log.warn("Error while attempting to parse content to an object", e);
			log.debug(type.toString() + " content that caused the error was: " + content);
		}
		
		parsedObject = entity;
	}
	
	private void parse() {
		
		try {
			//First try to detect the content type to see if XML or JSON were
			//passed in the content of the post.
			String myType = req.getContentType();
			
			//Remove the encoding portion of the mimetype
			//This is likely only an issue during testing where we are using
			//a HTTPUnit test request, which seems to return type and encoding
			//together.
			if (myType.indexOf(';') > -1) {
				myType = myType.substring(0, myType.indexOf(';'));
			}
			
			ServiceResponseMimeType parsedType = ServiceResponseMimeType.parse(myType);
			if (! UNKNOWN.equals(parsedType)) {
				type = parsedType;
				content = contentAsString(req);
			} else {
				
				//couldn't find it in the body of the post, so check for explicit
				//set param names
				String xmlParam = StringUtils.trimToNull(req.getHeader(AbstractSparrowServlet.XML_SUBMIT_HEADER_NAME));
				String jsonParam = StringUtils.trimToNull(req.getHeader(AbstractSparrowServlet.JSON_SUBMIT_HEADER_NAME));
				
				if (xmlParam == null) xmlParam = AbstractSparrowServlet.XML_SUBMIT_DEFAULT_PARAM_NAME;
				if (jsonParam == null) jsonParam = AbstractSparrowServlet.JSON_SUBMIT_DEFAULT_PARAM_NAME;
				

				content = StringUtils.trimToNull(req.getParameter(xmlParam));
				
				if (content != null) {
					type = XML;
				} else {
					content = StringUtils.trimToNull(req.getParameter(jsonParam));
					
					if (content != null) {
						type = JSON;
					} else {
						errorMessage = "Unable to find content.";
					}
				}
			}
		} catch (IOException e) {
			//Basically unrecoverable
		}
		
		contentParsed = true;
	}
	

	
	private String contentAsString(HttpServletRequest req) throws IOException {
	    BufferedReader reader = req.getReader();
	    StringBuilder sb = new StringBuilder();
	    String line = reader.readLine();
	    while (line != null) {
	        sb.append(line + "\n");
	        line = reader.readLine();
	    }
	    reader.close();
	    String data = sb.toString();

	    return data;
	}
	
	
	public static XStream getXMLXStream() {
		XStream xs = new XStream(new StaxDriver());
	    xs.setMode(XStream.NO_REFERENCES);
	    xs.processAnnotations(ServiceResponseWrapper.class);
	    return xs;
	}
	
	/**
	 * To read JSON, we need to use the Jettison driver.
	 * @return
	 */
	public static XStream getJSONXStreamReader() {
		HierarchicalStreamDriver driver = new JettisonMappedXmlDriver();
		XStream xs = new XStream(driver);
	    xs.setMode(XStream.NO_REFERENCES);
	    xs.processAnnotations(ServiceResponseWrapper.class);
	    return xs;
	}
	
	/**
	 * The Jettison driver works to write JSON, but it does it all
	 * as a single line.  This driver makes it more readable when
	 * serializing, but does not support reading.
	 * @return
	 */
	public static XStream getJSONXStreamWriter() {
		XStream xs = new XStream(new JsonHierarchicalStreamDriver());
	    xs.setMode(XStream.NO_REFERENCES);
	    xs.processAnnotations(ServiceResponseWrapper.class);
	    return xs;
	}
}
