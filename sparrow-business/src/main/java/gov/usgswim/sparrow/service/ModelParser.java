package gov.usgswim.sparrow.service;

import gov.usgswim.sparrow.SparrowUnits;
import gov.usgswim.sparrow.domain.IPredefinedSession;
import gov.usgswim.sparrow.domain.PredefinedSession;
import gov.usgswim.sparrow.domain.Source;
import gov.usgswim.sparrow.domain.SparrowModel;
import gov.usgswim.sparrow.domain.SparrowModelBuilder;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;
import static gov.usgswim.sparrow.service.ModelSerializerConstants.*;

import java.util.ArrayList;

import gov.usgswim.sparrow.parser.XMLParseValidationException;
import gov.usgswim.sparrow.util.ParserHelper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.apache.log4j.Logger;

/**
 * This is a partial implementation.
 * TODO: support all model attributes (JIRA SPDSSII-81)
 */
public class ModelParser {

	private static final long serialVersionUID = 1L;
	protected static Logger log =
		Logger.getLogger(ModelParser.class);
	
	private final SimpleDateFormat dateFormat;
	
	public ModelParser(){
		String pattern = DATE_FORMAT.getPattern();
		dateFormat = new SimpleDateFormat(pattern);
	}
	
	// =============================
	// PUBLIC STATIC UTILITY METHODS
	// =============================
	public static boolean isTargetMatch(String tagName) {
		return MODELS.equals(tagName);
	}

	public List<SparrowModel> parse(XMLStreamReader in)
		throws XMLStreamException, XMLParseValidationException {
		List<SparrowModel> models = new ArrayList<>();
		
		ParserHelper.parseToStartTag(in);
		String localName = in.getLocalName();
		int eventCode = in.getEventType();
		assert (isTargetMatch(localName) && eventCode == START_ELEMENT) : this.getClass().getSimpleName()
		+ " can only parse " + MODELS + " elements.";

		while (in.hasNext()) {

			eventCode = in.next();
			// Main event loop -- parse until corresponding target end tag encountered.
			switch (eventCode) {
				case START_ELEMENT:
					localName = in.getLocalName();
					if(MODEL.equals(localName)){
						SparrowModel model = parseModel(in);
						models.add(model);
					} else {
						throw new RuntimeException("unrecognized child element of <" + localName + "> for " + MODELS);
					}
					break;
				case END_ELEMENT:
					localName = in.getLocalName();
					if (MODELS.equals(localName)) {
						return models; // we're done
					}
					break;
			}
		}
		throw new RuntimeException("tag <" + MODELS + "> not closed. Unexpected end of stream?");
	}
	
	public SparrowModel parseModel(XMLStreamReader in) throws XMLStreamException {
		SparrowModelBuilder smb = new SparrowModelBuilder();
		while (in.hasNext()) {
			int eventCode = in.next();
			// Main event loop -- parse until corresponding target end tag encountered.
			switch (eventCode) {
				case START_ELEMENT:
					String localName = in.getLocalName();
					switch(localName){
						case STATUS:
							parseStatus(in, smb);
							break;
						case SPATIAL_MEMBERSHIP:
							parseSpatialMembership(in, smb);
							break;
						case SESSIONS:
							List<IPredefinedSession> sessions = parseSessions(in);
							smb.setSessions(sessions);
							break;
						case SOURCES:
							List<Source> sources = parseSources(in);
							for(Source source : sources){
								smb.addSource(source);
							}
							break;
						case MODEL_NAME:
							smb.setName(ParserHelper.parseSimpleElementValue(in));
							break;
						case ALIAS:
							ParserHelper.ignoreElement(in);
							break;
						case MODEL_DESCRIPTION:
							smb.setDescription(ParserHelper.parseSimpleElementValue(in));
							break;
						case MODEL_URL:
							smb.setUrl(ParserHelper.parseSimpleElementValue(in));
							break;
						case DATE_ADDED:
							String dateStr = ParserHelper.parseSimpleElementValue(in);
							if(null != dateStr && 0 < dateStr.length()){
								Date date = null;
								try {
									date = dateFormat.parse(dateStr);
								} catch (ParseException e){
									throw new RuntimeException(e);
								}
								smb.setDateAdded(date);
							}
							break;
						case CONTACT_ID:
							smb.setContactId(ParserHelper.parseSimpleElementLong(in));
							break;
						case ENH_NETWORK_ID:
							smb.setEnhNetworkId(ParserHelper.parseSimpleElementLong(in));
							break;
						case ENH_NETWORK_NAME:
							smb.setEnhNetworkName(ParserHelper.parseSimpleElementValue(in));
							break;
						case ENH_NETWORK_URL:
							smb.setEnhNetworkUrl(ParserHelper.parseSimpleElementValue(in));
							break;
						case THEME_NAME:
							smb.setThemeName(ParserHelper.parseSimpleElementValue(in));
							break;
						case MODEL_CONSTITUENT:
							smb.setConstituent(ParserHelper.parseSimpleElementValue(in));
							break;
						case MODEL_UNITS:
							String unitsText = in.getElementText();
							SparrowUnits units = SparrowUnits.parse(unitsText);
							smb.setUnits(units);
							break;
						case BASE_YEAR:
							smb.setBaseYear(ParserHelper.parseSimpleElementInt(in));
							break;
						default:
							throw new RuntimeException("unrecognized child element of <" + localName + "> for " + MODEL);
					}
				case END_ELEMENT:
					localName = in.getLocalName();
					if (MODEL.equals(localName)) {
						return smb.toImmutable(); // we're done
					}
					break;
			}
		}
		throw new RuntimeException("tag <" + MODEL + "> not closed. Unexpected end of stream?");
	}
	
	public List<IPredefinedSession> parseSessions(XMLStreamReader in) {
		log.warn("Sessions parsing not yet supported");
		return null;
	}
	public PredefinedSession parseSession(XMLStreamReader in) {
		log.warn("Session parsing not yet supported");
		return null;
	}
	public List<Source> parseSources(XMLStreamReader in) {
		log.warn("Sources parsing not yet supported");
		return null;
	}
	public Source parseSource(XMLStreamReader in) {
		log.warn("Source parsing not yet supported");
		return null;
	}
	public void parseSpatialMembership(XMLStreamReader in, SparrowModelBuilder smb) {
		log.warn("Spatial membership parsing not yet supported");
	}
	
	public void parseStatus(XMLStreamReader in, SparrowModelBuilder smb) throws XMLStreamException {
		log.warn("Status parsing not yet supported");
	}
	
	public void parseBounds(XMLStreamReader in, SparrowModelBuilder smb) {
		log.warn("Bounds parsing not yet supported");
	}

}
