package gov.usgswim.sparrow.domain;

import gov.usgswim.sparrow.SparrowUnits;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class SparrowModelImmTest {

	SparrowModelImm instance;
	List<IPredefinedSession> sessions;
	List<Source> sources;
	List<String> states;
	List<String> regions;
	Date date;
	public SparrowModelImmTest() {
		
		
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
		sessions = new ArrayList<>(Arrays.asList(new IPredefinedSession[]{
			new PredefinedSession(Long.MIN_VALUE, null, Long.MIN_VALUE, PredefinedSessionType.LISTED, Boolean.TRUE, null, null, Integer.MIN_VALUE, null, null, null, null, null, null, PredefinedSessionTopic.SCENARIO),
			new PredefinedSession(Long.MIN_VALUE, null, Long.MIN_VALUE, PredefinedSessionType.LISTED, Boolean.TRUE, null, null, Integer.MIN_VALUE, null, null, null, null, null, null, PredefinedSessionTopic.SCENARIO)
		}));
		sources = new ArrayList<>(Arrays.asList(new Source[]{
			new SourceImm(
				Long.MIN_VALUE, Integer.MAX_VALUE, "my name 1", "my display name 1", "", 1, Long.MIN_VALUE, "tn", SparrowUnits.CFS
			),
			new SourceImm(
				Long.MIN_VALUE, Integer.MAX_VALUE, "my name 1", "my display name 2", "", 1, Long.MIN_VALUE, "tp", SparrowUnits.FPS
			)
		}));
		states = new ArrayList<>(Arrays.asList(
			"WI",
			"MN"
		));
		regions = new ArrayList<>(Arrays.asList(
			"MRB0",
			"MRB1"
		));
		
		date = new Date();

		instance = new SparrowModelImm(
			Long.MIN_VALUE,
			true,
			true,
			true,
			null,
			null,
			null,
			date,
			Long.MIN_VALUE,
			Long.MIN_VALUE,
			null,
			null,
			null,
			null,
			Double.NaN,
			Double.NaN,
			Double.NaN,
			Double.NaN,
			null,
			true,
			SparrowUnits.CFS,
			sessions,
			sources,
			false,
			1990,
			states,
			regions
		);
	}
	
	public final String LIST_COPY_ERROR_MESSAGE = "starting and ending lengths should be equal, else the constructor is not copying parameterized lists";

	/**
	 * Test of getSessions method, of class SparrowModelImm.
	 */
	@Test(expected=UnsupportedOperationException.class)
	public void testGetSessionsIsImmutable() {
		List<IPredefinedSession> result = instance.getSessions();
		result.remove(0);
	}
	
	@Test
	public void testConstructorCopiesSessions() {
		int startingLength = instance.getSessions().size();
		sessions.remove(0);
		int endingLength = instance.getSessions().size();
		
		assertEquals(LIST_COPY_ERROR_MESSAGE, startingLength, endingLength);
	}
	
	/**
	 * Test of getSources method, of class SparrowModelImm.
	 */
	@Test(expected=UnsupportedOperationException.class)
	public void testGetSources() {
		List<Source> result = instance.getSources();
		result.remove(0);
	}

	@Test
	public void testConstructorCopiesSources() {
		int startingLength = instance.getSources().size();
		sources.remove(0);
		int endingLength = instance.getSources().size();
		
		assertEquals(LIST_COPY_ERROR_MESSAGE, startingLength, endingLength);
	}
	
	/**
	 * Test of getStates method, of class SparrowModelImm.
	 */
	@Test(expected=UnsupportedOperationException.class)
	public void testGetStates() {
		List<String> result = instance.getStates();
		result.remove(0);
	}

	@Test
	public void testConstructorCopiesStates() {
		int startingLength = instance.getStates().size();
		states.remove(0);
		int endingLength = instance.getStates().size();
		
		assertEquals(LIST_COPY_ERROR_MESSAGE, startingLength, endingLength);
	}
	
	/**
	 * Test of getRegions method, of class SparrowModelImm.
	 */
	@Test(expected=UnsupportedOperationException.class)
	public void testGetRegions() {
		List<String> result = instance.getRegions();
		result.remove(0);
	}
	
	@Test
	public void testConstructorCopiesRegions() {
		int startingLength = instance.getRegions().size();
		regions.remove(0);
		int endingLength = instance.getRegions().size();
		
		assertEquals(LIST_COPY_ERROR_MESSAGE, startingLength, endingLength);
	}
	
	@Test
	public void testConstructorCopiesDate() {
		Date expectedDate = (Date) date.clone();
		
		date.setYear(date.getYear() + 1);
		
		Date subsequentDate = instance.getDateAdded();
		
		assertEquals("the caller of the constructor was able to modify the date that the immutable class presented", expectedDate, subsequentDate);
	}

}
