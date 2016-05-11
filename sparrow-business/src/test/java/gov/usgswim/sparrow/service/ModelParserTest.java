package gov.usgswim.sparrow.service;

import com.sun.org.apache.xerces.internal.impl.PropertyManager;
import com.sun.org.apache.xerces.internal.impl.XMLStreamReaderImpl;
import gov.usgswim.sparrow.SparrowUnits;
import gov.usgswim.sparrow.domain.SparrowModel;
import gov.usgswim.sparrow.parser.XMLParseValidationException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ModelParserTest {
	ModelParser parser;
	public ModelParserTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
		parser = new ModelParser();
	}

	@After
	public void tearDown() {
	}

	private XMLStreamReader getReader(String string){
		try {
			return new XMLStreamReaderImpl(new StringReader(string), new PropertyManager(PropertyManager.CONTEXT_READER));
		} catch (XMLStreamException ex) {
			throw new RuntimeException(ex);
		}
	}
	/**
	 * Test of isTargetMatch method, of class ModelParser.
	 */
	@Test

	public void testIsTargetMatch() {
	}

	/**
	 * Test of parse method, of class ModelParser.
	 */
	@Test
	public void testParseWithoutComplicatedElements() throws Exception {
		String example = "<?xml version=\"1.0\"?>\n"
			+ "<models xmlns=\"http://www.usgs.gov/sparrow/meta_response/v0_1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.usgs.gov/sparrow/meta_response/v0_1 http://www.usgs.gov/sparrow/meta_response.xsd\">\n"
			+ "  <model id=\"42\">\n"
			+ "    <name>CLEVER MODEL NAME</name>\n"
			+ "    <description>Clever model description here</description>\n"
			+ "    <url>http://www.usgs.gov/MOCK_MODEL</url>\n"
			+ "    <dateAdded>2016-05-10</dateAdded>\n"
			+ "    <contactId>12</contactId>\n"
			+ "    <enhNetworkId>123</enhNetworkId>\n"
			+ "    <enhNetworkName>Clever Network Name!</enhNetworkName>\n"
			+ "    <enhNetworkUrl>http://www.usgs.gov/MOCK_NETWORK</enhNetworkUrl>\n"
			+ "    <themeName>something</themeName>\n"
			+ "    <constituent>nitrogen</constituent>\n"
			+ "    <units>kg&#x25CF;year&#x2C9;&#xB9;</units>\n"
			+ "    <baseYear>1990</baseYear>\n"
			+ "  </model>\n"
			+ "</models>";

		XMLStreamReader reader;
		reader = getReader(example);
		List<SparrowModel> models = parser.parse(reader);
		SparrowModel m = models.get(0);

		assertEquals("CLEVER MODEL NAME", m.getName());
		assertEquals("Clever model description here", m.getDescription());
		assertEquals("http://www.usgs.gov/MOCK_MODEL", m.getUrl());
		
		String dateStr = "2016-05-10";
		SimpleDateFormat df = new SimpleDateFormat(ModelSerializerConstants.DATE_FORMAT.getPattern());
		Date expectedDate = df.parse(dateStr);
		
		assertEquals(expectedDate, m.getDateAdded());
		assertEquals(12L, (long)m.getContactId());
		assertEquals(123L, (long)m.getEnhNetworkId());
		assertEquals("Clever Network Name!", m.getEnhNetworkName());
		assertEquals("something", m.getThemeName());
		assertEquals("nitrogen", m.getConstituent());
		assertEquals(SparrowUnits.KG_PER_YEAR, m.getUnits());
		assertEquals(1990, m.getBaseYear());

	}

	/**
	 * Test of parseModel method, of class ModelParser.
	 */
	@Test
	public void testParseModel() throws Exception {
	}

	/**
	 * Test of parseSessions method, of class ModelParser.
	 */
	@Test
	public void testParseSessions() {
	}

	/**
	 * Test of parseSession method, of class ModelParser.
	 */
	@Test
	public void testParseSession() {
	}

	/**
	 * Test of parseSources method, of class ModelParser.
	 */
	@Test
	public void testParseSources() {
	}

	/**
	 * Test of parseSource method, of class ModelParser.
	 */
	@Test
	public void testParseSource() {
	}

	/**
	 * Test of parseSpatialMembership method, of class ModelParser.
	 */
	@Test
	public void testParseSpatialMembership() {
	}

	/**
	 * Test of parseStatus method, of class ModelParser.
	 */
	@Test
	public void testParseStatus() throws XMLStreamException, XMLParseValidationException {
	}

	/**
	 * Test of parseBounds method, of class ModelParser.
	 */
	@Test
	public void testParseBounds() {
	}

}
