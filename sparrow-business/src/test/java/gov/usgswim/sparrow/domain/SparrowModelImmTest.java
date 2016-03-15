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
		instance = (SparrowModelImm) (new SparrowModelBuilder().toImmutable());
	}
	

	/**
	 * Test of getSessions method, of class SparrowModelImm.
	 */
	@Test(expected=UnsupportedOperationException.class)
	public void testSessionsAreImmutable() {
		List<IPredefinedSession> result = instance.getSessions();
		result.remove(0);
	}
	

	/**
	 * Test of getSources method, of class SparrowModelImm.
	 */
	@Test(expected=UnsupportedOperationException.class)
	public void testSourcesAreImmutable() {
		List<Source> result = instance.getSources();
		result.remove(0);
	}

	/**
	 * Test of getStates method, of class SparrowModelImm.
	 */
	@Test(expected=UnsupportedOperationException.class)
	public void testStatesAreImmutable() {
		List<String> result = instance.getStates();
		result.remove(0);
	}

	/**
	 * Test of getRegions method, of class SparrowModelImm.
	 */
	@Test(expected=UnsupportedOperationException.class)
	public void testGetRegions() {
		List<String> result = instance.getRegions();
		result.remove(0);
	}

}
