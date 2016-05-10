package gov.usgswim.sparrow.service;

import static gov.usgswim.sparrow.service.AbstractSerializer.XMLSCHEMA_NAMESPACE;
import static gov.usgswim.sparrow.service.AbstractSerializer.XMLSCHEMA_PREFIX;
import static gov.usgswim.sparrow.service.ModelSerializerConstants.*;
import gov.usgs.webservices.framework.dataaccess.BasicTagEvent;
import gov.usgs.webservices.framework.dataaccess.BasicXMLStreamReader;
import gov.usgswim.sparrow.domain.IPredefinedSession;
import gov.usgswim.sparrow.domain.SparrowModel;
import gov.usgswim.sparrow.domain.Source;
import gov.usgswim.sparrow.util.SparrowResourceUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
public class ModelSerializer extends BasicXMLStreamReader {
	

	private List<SparrowModel> models;
	private Iterator<SparrowModel> mIter;
	private boolean isOutputCompleteFirstRow;
	private boolean isSessionFirstRowOutput;
	private boolean isSourcesFirstRowOutput;

	public ModelSerializer(List<SparrowModel> models) {
		this.models = models;
	}


	/* Override because there's no resultset
	 * @see gov.usgs.webservices.framework.dataaccess.BasicXMLStreamReader#readNext()
	 */
	@Override
	public void readNext() throws XMLStreamException {
		try {
			if (!isStarted) {
				documentStartAction();
			}
			readModel();
			if (isDataDone && isStarted && !isEnded) {
				// Only output footer if the data is finished, the document was
				// actually started,
				// and the footer has not been output.
				documentEndAction();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new XMLStreamException(e);
		}
	}

	@Override
	protected BasicTagEvent documentStartAction() {
		super.documentStartAction();
		// add the namespaces
		this.setDefaultNamespace(TARGET_NAMESPACE);
		addNamespace(XMLSCHEMA_NAMESPACE, XMLSCHEMA_PREFIX);
		// opening element
		events.add(new BasicTagEvent(START_DOCUMENT));
		// TODO need to add encoding and version xw.add( evtFact.createStartDocument(ENCODING, XML_VERSION) );

		events.add(new BasicTagEvent(START_ELEMENT, MODELS)
			.addAttribute(XMLSCHEMA_PREFIX, XMLSCHEMA_NAMESPACE, "schemaLocation", TARGET_NAMESPACE + " " + TARGET_NAMESPACE_LOCATION));

		mIter = models.iterator();
		return null;
	}


	private void readModel() {
		if (mIter.hasNext()) {
			SparrowModel model = mIter.next();
			events.add(new BasicTagEvent(START_ELEMENT, MODEL).addAttribute(MODEL_ID_ATTRIBUTE, model.getId().toString()));
			{
                addOpenTag(STATUS);
                {
                    addNonNullBasicTag(MODEL_APPROVED, Boolean.toString(model.isApproved()));
                    addNonNullBasicTag(MODEL_PUBLIC, Boolean.toString(model.isPublic()));
                    addNonNullBasicTag(MODEL_ARCHIVED, Boolean.toString(model.isArchived()));
                }
                addCloseTag(STATUS);
				addNonNullBasicTag(MODEL_NAME, model.getName());
				addNonNullBasicTag(ALIAS, SparrowResourceUtils.lookupModelName(model.getId().toString()));
				addNonNullBasicTag(MODEL_DESCRIPTION, StringUtils.trimToNull(model.getDescription()));
				addNonNullBasicTag(MODEL_URL, StringUtils.trimToNull(model.getUrl()));
				addNonNullBasicTag(DATE_ADDED, DATE_FORMAT.format(model.getDateAdded()));
				addNonNullBasicTag(CONTACT_ID, model.getContactId().toString());
				addNonNullBasicTag(ENH_NETWORK_ID, model.getEnhNetworkId().toString());
				addNonNullBasicTag(ENH_NETWORK_NAME, model.getEnhNetworkName());
				addNonNullBasicTag(ENH_NETWORK_URL, model.getEnhNetworkUrl());
				addNonNullBasicTag(THEME_NAME, model.getThemeName());
				addNonNullBasicTag(MODEL_CONSTITUENT, model.getConstituent());
				addNonNullBasicTag(MODEL_UNITS, model.getUnits().getUserName());
				addNonNullBasicTag(BASE_YEAR, Integer.toString(model.getBaseYear()));
				addOpenTag(SPATIAL_MEMBERSHIP);
					if(model.isNational()) {
						//If model is national then it pertains to all states and regions. 
						//Declare model as national. Do not enumerate other memberships.
						addNonNullBasicTag(NATIONAL, Boolean.toString(model.isNational()));
					} else {
						//model is not national, so the subset of pertinent states and regions
						//must be enumerated
						addOpenTag(STATES);
							for(String state : model.getStates()){
								addNonNullBasicTag(STATE, state);
							}
						addCloseTag(STATES);
						addOpenTag(REGIONS);
							for(String region : model.getRegions()){
								addNonNullBasicTag(REGION, region);
							}
						addCloseTag(REGIONS);
					}
				addCloseTag(SPATIAL_MEMBERSHIP);
				events.add(new BasicTagEvent(BOUNDS, null)
					.addAttribute(NORTH_BOUNDS_ATTRIBUTE, model.getNorthBound().toString())
					.addAttribute(WEST_BOUNDS_ATTRIBUTE, model.getWestBound().toString())
					.addAttribute(SOUTH_BOUNDS_ATTRIBUTE, model.getSouthBound().toString())
					.addAttribute(EAST_BOUNDS_ATTRIBUTE, model.getEastBound().toString()));
				addOpenTag(SESSIONS);
				{
					for ( IPredefinedSession session: model.getSessions()) {
						if (isOutputCompleteFirstRow && !isSessionFirstRowOutput) {
							outputEmptySessionsForHeaders();
							isSessionFirstRowOutput = true;
						}
						
						BasicTagEvent tagEvent = new BasicTagEvent(SESSION, null)
							.addAttribute(SESSION_KEY, session.getUniqueCode())
							.addAttribute(SESSION_NAME, session.getName())
							.addAttribute(SESSION_DESCRIPTION, session.getDescription())
							.addAttribute(SESSION_GROUP_NAME, session.getGroupName())
							.addAttribute(SESSION_TYPE, session.getPredefinedSessionType().name())
							.addAttribute(SESSION_APPROVED, session.getApproved()?"T":"F")
							.addAttribute(SESSION_SORT_ORDER, Integer.toString(session.getSortOrder()))
							.addAttribute(SESSION_ADD_BY, session.getAddBy())
							.addAttribute(SESSION_ADD_DATE, session.getAddDate().toString())
							.addAttribute(SESSION_ADD_NOTE, session.getAddNote());
							
							if(null != session.getTopic()){
								tagEvent.addAttribute(SESSION_TOPIC, session.getTopic().name());
							}
							
						events.add(tagEvent);
					}
				}
				addCloseTag(SESSIONS);
				addOpenTag(SOURCES);
				{
					for (Source src : model.getSources()) {
						if (isOutputCompleteFirstRow && !isSourcesFirstRowOutput) {
							outputEmptySourceForHeaders();
							isSourcesFirstRowOutput = true;
							isOutputCompleteFirstRow = false; // Not sure if this is proper. Must rethink;
						}
						events.add(new BasicTagEvent(START_ELEMENT, SOURCE)
							.addAttribute(SOURCE_ID, src.getId().toString())
							.addAttribute(SOURCE_IDENTIFIER, Integer.toString(src.getIdentifier()))
							.addAttribute(SOURCE_SORT_ORDER, Integer.toString(src.getSortOrder()))
						);
						{
							addNonNullBasicTag(SOURCE_NAME, src.getName());
							addNonNullBasicTag(SOURCE_DISPLAY_NAME, src.getDisplayName());
							addNonNullBasicTag(SOURCE_DESCRIPTION,StringUtils.trimToNull(src.getDescription()));
							addNonNullBasicTag(SOURCE_CONSTITUENT, src.getConstituent());
							addNonNullBasicTag(SOURCE_UNITS, src.getUnits().toString());
						}
						addCloseTag(SOURCE);
						// add a carriage return to break up long text line
						events.add(new BasicTagEvent(SPACE));
					}
				}
				addCloseTag(SOURCES);
			}
			addCloseTag(MODEL);
		} else {
			isDataDone = true;
		}
	}


	@Override
	protected void documentEndAction() {
		super.documentEndAction();
		addCloseTag(MODELS);
		events.add(new BasicTagEvent(END_DOCUMENT));
	}


	private void outputEmptySourceForHeaders() {
		events.add(new BasicTagEvent(START_ELEMENT, SOURCE)
				.addAttribute(SOURCE_ID, "")
				.addAttribute(SOURCE_IDENTIFIER, "")
				.addAttribute(SOURCE_SORT_ORDER, ""));
		{
			addNonNullBasicTag(SOURCE_NAME, "");
			addNonNullBasicTag(SOURCE_DISPLAY_NAME, "");
			addNonNullBasicTag(SOURCE_DESCRIPTION, "");
			addNonNullBasicTag(SOURCE_CONSTITUENT, "");
			addNonNullBasicTag(SOURCE_UNITS, "");
		}
		addCloseTag(SOURCE);
		// isOutputCompleteFirstRow = false; [IK] This side effect is bad and should be no longer needed.
	}

	private void outputEmptySessionsForHeaders() {
		events.add(new BasicTagEvent(START_ELEMENT, SESSION)
				.addAttribute(SESSION_KEY, ""));
		addCloseTag(SESSION);
	}

	public void setOutputCompleteFirstRow() {
		isOutputCompleteFirstRow = true;
	}

	@Override
	public void close() throws XMLStreamException {
		// TODO Auto-generated method stub
	}

}
