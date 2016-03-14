package gov.usgswim.sparrow.domain;

import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SparrowModelBuilderTest {
	SparrowModelBuilder instance;

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

	/**
	 * Test of getSources method, of class SparrowModelBuilder.
	 */
	@Test
	public void testGetSources() {
		//test value at contstruction time
		assertEquals("Getter should return empty list, not null", Collections.emptyList(), instance.getSources());
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

}
