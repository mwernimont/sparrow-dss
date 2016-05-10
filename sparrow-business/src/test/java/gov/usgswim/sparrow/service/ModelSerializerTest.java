package gov.usgswim.sparrow.service;

import gov.usgs.webservices.framework.formatter.IFormatter;
import gov.usgs.webservices.framework.formatter.XMLPassThroughFormatter;
import gov.usgswim.sparrow.SparrowUnits;
import gov.usgswim.sparrow.domain.IPredefinedSession;
import gov.usgswim.sparrow.domain.PredefinedSessionBuilder;
import gov.usgswim.sparrow.domain.PredefinedSessionTopic;
import gov.usgswim.sparrow.domain.PredefinedSessionType;
import gov.usgswim.sparrow.domain.SourceBuilder;
import gov.usgswim.sparrow.domain.SparrowModel;
import gov.usgswim.sparrow.domain.SparrowModelBuilder;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class DomainSerializerTest {
	ModelSerializer instance;
	SparrowModelBuilder mb;
	SourceBuilder srcb;
	PredefinedSessionBuilder psb;
	
	public static final long MOCK_MODEL_ID = 42;
	public static final String MOCK_MODEL_URL = "http://www.usgs.gov/MOCK_MODEL";
	public static final String MOCK_NETWORK_NAME = "Clever Network Name!";
	public static final String MOCK_NETWORK_URL = "http://www.usgs.gov/MOCK_NETWORK";
	
	public DomainSerializerTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
		mb = new SparrowModelBuilder();
		mb.setId(MOCK_MODEL_ID);
		mb.setApproved(true);
		mb.setPublic(true);
		mb.setArchived(false);
		mb.setName("CLEVER MODEL NAME");
		mb.setDescription("Clever model description here");
		mb.setUrl(MOCK_MODEL_URL);
		mb.setDateAdded(new Date());
		mb.setContactId(12L);
		mb.setEnhNetworkId(123L);
		mb.setEnhNetworkName(MOCK_NETWORK_NAME);
		mb.setEnhNetworkUrl(MOCK_NETWORK_URL);
		mb.setEnhNetworkIdColumn("clever network id column name");
		mb.setThemeName("something");
		mb.setNorthBound(42.0);
		mb.setEastBound(42.0);
		mb.setWestBound(42.0);
		mb.setSouthBound(42.0);
		mb.setConstituent("nitrogen");
		mb.setUsingSimpleReachIds(true);
		mb.setUnits(SparrowUnits.KG_PER_YEAR);
		mb.setStates(Arrays.asList(new String[] {
			"AL",
			"AK"
		}));
		mb.setRegions(Arrays.asList(new String[] {
			"MRB1"
		}));
		mb.setIsNational(false);
		mb.setBaseYear(1990);
		
		srcb = new SourceBuilder();
		srcb.setConstituent("nitrogen");
		srcb.setDescription("N fertilizer");
		srcb.setDisplayName("fertilizer");
		srcb.setId(234L);
		srcb.setIdentifier(98);
		srcb.setModelId(MOCK_MODEL_ID);
		srcb.setName("Nitrogen Fertilizer");
		srcb.setSortOrder(0);
		srcb.setUnits(SparrowUnits.KG_PER_YEAR);
		
		psb = new PredefinedSessionBuilder();
		psb.setAddBy("add by");
		psb.setAddContactInfo("123 fake st");
		psb.setAddDate(new java.sql.Date(3151861));
		psb.setAddNote("notable note");
		psb.setApproved(true);
		psb.setContextString("{\"PredictionContext\":{\"@xmlns\":\"http://www.usgs.gov/sparrow/prediction-schema/v0_2\",\"@xmlns:xsi\":\"http://www.w3.org/2001/XMLSchema-instance\",\"@model-id\":\"50\",\"adjustmentGroups\":{\"@conflicts\":\"accumulate\",\"reachGroup\":[],\"individualGroup\":{\"@enabled\":\"true\",\"reach\":[]}},\"analysis\":{\"dataSeries\":{\"#text\":\"incremental_delivered_yield\"},\"groupBy\":{\"#text\":\"\",\"@aggFunction\":\"avg\"}},\"terminalReaches\":{\"reach\":[{\"@id\":\"5573\",\"@name\":\"PEE DEE R\"}]},\"areaOfInterest\":\"\",\"nominalComparison\":{\"@type\":\"none\"}},\"PermanentMapState\":{\"what_to_map\":\"catch\",\"binData\":null,\"binAuto\":true,\"binCount\":5,\"binType\":\"EQUAL_COUNT\",\"calibSites\":-75,\"reachOverlay\":19,\"huc8Overlay\":40,\"dataLayerOpacity\":75,\"lat\":35.068359375,\"lon\":-78.9422607421875,\"zoom\":6,\"mapLayers\":{\"-253\":100,\"-13\":100,\"-14\":100,\"4952\":100,\"-2538\":72}}}");
		psb.setDescription("descriptive description");
		psb.setGroupName("great group");
		psb.setId(22L);
		psb.setModelId(MOCK_MODEL_ID);
		psb.setName("session name");
		psb.setPredefinedSessionType(PredefinedSessionType.FEATURED);
		psb.setSortOrder(0);
		psb.setTopic(PredefinedSessionTopic.SCENARIO);
		psb.setUniqueCode("jackieeee5682");
		
		
	}
	
	private String serialize(){
		//get immutables
		List<SparrowModel> models= new ArrayList<>();
		mb.addSource(srcb.toImmutable());
		mb.setSessions(Arrays.asList(new IPredefinedSession[]{
			psb.toImmutable()
		}));
		models.add(mb.toImmutable());
		instance = new ModelSerializer(models);
		
		IFormatter formatter = new XMLPassThroughFormatter();
		Writer writer = new StringWriter();
		
		try {
			formatter.dispatch(instance, writer);
			writer.flush();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		
		String result = writer.toString();
		return result;
	}
	
	@Test
	public void testThatNonNationalModelOmitsNationalTag() {
		//scaffolded objects already have national = false
		String result = serialize();
		int idx = result.indexOf("<national>");
		assertEquals("serialization should exclude a <national> element when the object has is_national set to false", -1, idx);
		idx = result.indexOf("<states>");
		assertNotEquals("serialization should include a <states> element when the object has is_national set to false", -1, idx);
		idx = result.indexOf("<regions>");
		assertNotEquals("serialization should include a <regions> element when the object has is_national set to false", -1, idx);
	}
	
	@Test
	public void testThatNationalModelIncludesNationalTag() {
		//scaffolded objects already have national = false, so set to true
		mb.setIsNational(true);
		String result = serialize();
		int idx = result.indexOf("<national>");
		assertNotEquals("serialization should include a <national> element when the object has is_national set to false", -1, idx);
		
	}
	
	@Test
	public void testThatSerializationWorksOnNullPredefinedSessionTopic() {
		psb.setTopic(null);
		boolean exceptionThrown = false;
		try{
			serialize();
		}
		catch(Exception ex){
			exceptionThrown = true;
		}
		
		assertFalse("Serialization failed while PredefinedSession topic was set to null", exceptionThrown);
	}
	
	@Test
	public void testThatNetworkUrlAndIdAreSerialized() {
		String result = serialize();
		
		int idx = result.indexOf(MOCK_NETWORK_NAME);
		assertNotEquals("network name should be serialized", -1, idx);
		
		idx = result.indexOf(MOCK_NETWORK_URL);
		assertNotEquals("network url should be serialized", -1, idx);
	}
}
