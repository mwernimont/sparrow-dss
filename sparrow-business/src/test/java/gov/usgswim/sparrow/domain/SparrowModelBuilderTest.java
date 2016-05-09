package gov.usgswim.sparrow.domain;

import gov.usgswim.sparrow.SparrowUnits;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SparrowModelBuilderTest {
	SparrowModelBuilder instance;
	final static String LIST_COPY_ERROR_MESSAGE = "starting and ending lengths should be equal, else the constructor is not copying parameterized lists";

	@Before
	public void setUp() {
		instance = new SparrowModelBuilder();
	}

	/**
	 * Test of getSessions method, of class SparrowModelBuilder.
	 */
	@Test
	public void testGetSessions() {
		//test value at construction time
		assertNull("Getter should return null, not an empty list", instance.getSessions());
		instance.setSessions(null);
		assertNull("Getter should return null, not an empty list", instance.getSessions());
	}
	@Test
	public void testBuilderCopiesSessions() {
		List<IPredefinedSession> sessions = new ArrayList<>(Arrays.asList(new IPredefinedSession[]{
			new PredefinedSession(Long.MIN_VALUE, null, Long.MIN_VALUE, PredefinedSessionType.LISTED, Boolean.TRUE, null, null, Integer.MIN_VALUE, null, null, null, null, null, null, PredefinedSessionTopic.SCENARIO),
			new PredefinedSession(Long.MIN_VALUE, null, Long.MIN_VALUE, PredefinedSessionType.LISTED, Boolean.TRUE, null, null, Integer.MIN_VALUE, null, null, null, null, null, null, PredefinedSessionTopic.SCENARIO)
		}));
		instance.setSessions(sessions);
		
		SparrowModel imm = instance.toImmutable();
		int startingLength = imm.getSessions().size();
		sessions.remove(0);
		int endingLength = imm.getSessions().size();
		
		assertEquals(LIST_COPY_ERROR_MESSAGE, startingLength, endingLength);
	}
	/**
	 * Test of getSources method, of class SparrowModelBuilder.
	 */
	@Test
	public void testGetSources() {
		//test value at contstruction time
		assertEquals("Getter should return empty list, not null", Collections.emptyList(), instance.getSources());
	}

	@Test
	public void testBuilderCopiesSources() {
		List<Source> sources = new ArrayList<>(Arrays.asList(new Source[]{
			new SourceImm(
				Long.MIN_VALUE, Integer.MAX_VALUE, "my name 1", "my display name 1", "", 1, Long.MIN_VALUE, "tn", SparrowUnits.CFS
			),
			new SourceImm(
				Long.MIN_VALUE, Integer.MAX_VALUE, "my name 1", "my display name 2", "", 1, Long.MIN_VALUE, "tp", SparrowUnits.FPS
			)
		}));
		
		for(Source src : sources){
			instance.addSource(src);
		}
		
		SparrowModel imm = instance.toImmutable();
		int startingLength = imm.getSources().size();
		sources.remove(0);
		int endingLength = imm.getSources().size();
		
		assertEquals(LIST_COPY_ERROR_MESSAGE, startingLength, endingLength);
	}
	
	/**
	 * Test of getStates method, of class SparrowModelBuilder.
	 */
	@Test
	public void testGetStates() {
		//test value at contstruction time
		assertEquals("Getter should return empty list, not null", Collections.emptyList(), instance.getStates());
		instance.setStates(null);
		assertEquals("Getter should return empty list, not null", Collections.emptyList(), instance.getStates());
	}
	@Test
	public void testBuilderCopiesStates() {
		List<String> states = new ArrayList<>(Arrays.asList(
			"WI",
			"MN"
		));
		instance.setStates(states);
		SparrowModel imm = instance.toImmutable();
		int startingLength = imm.getStates().size();
		states.remove(0);
		int endingLength = imm.getStates().size();
		
		assertEquals(LIST_COPY_ERROR_MESSAGE, startingLength, endingLength);
	}
	
	/**
	 * Test of getRegions method, of class SparrowModelBuilder.
	 */
	@Test
	public void testGetRegions() {
		//test value at contstruction time
		assertEquals("Getter should return empty list, not null", Collections.emptyList(), instance.getRegions());
		instance.setRegions(null);
		assertEquals("Getter should return empty list, not null", Collections.emptyList(), instance.getRegions());
	}
	
	@Test
	public void testBuilderCopiesRegions() {
		List<String> regions = new ArrayList<>(Arrays.asList(
			"MRB0",
			"MRB1"
		));
		instance.setRegions(regions);
		SparrowModel imm = instance.toImmutable();
		int startingLength = imm.getRegions().size();
		regions.remove(0);
		int endingLength = imm.getRegions().size();
		
		assertEquals(LIST_COPY_ERROR_MESSAGE, startingLength, endingLength);
	}
	@Test
	public void testConstructorCopiesDate() {
		Date date = new Date();
		Date expectedDate = (Date) date.clone();
		instance.setDateAdded(date);
		SparrowModel imm = instance.toImmutable();
		
		date.setYear(date.getYear() + 1);
		Date subsequentDate = imm.getDateAdded();
		assertEquals("the caller of the constructor was able to modify the date that the immutable class presented", expectedDate, subsequentDate);
	}

}
