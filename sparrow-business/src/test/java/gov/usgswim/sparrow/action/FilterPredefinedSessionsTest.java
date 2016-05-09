package gov.usgswim.sparrow.action;

import gov.usgswim.sparrow.domain.IPredefinedSession;
import gov.usgswim.sparrow.domain.PredefinedSession;
import gov.usgswim.sparrow.domain.PredefinedSessionBuilder;
import gov.usgswim.sparrow.domain.PredefinedSessionTopic;
import gov.usgswim.sparrow.request.PredefinedSessionRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FilterPredefinedSessionsTest {

	PredefinedSession watershedSession;
	PredefinedSession scenarioSession;
	List<IPredefinedSession> oneOfEachTopic;

	@Before
	public void setUp() {
		PredefinedSessionBuilder builder = new PredefinedSessionBuilder();
		
		builder.setTopic(PredefinedSessionTopic.WATERSHED);
		
		watershedSession = builder.toImmutable();

		builder = new PredefinedSessionBuilder();
		builder.setTopic(PredefinedSessionTopic.SCENARIO);
		scenarioSession = builder.toImmutable();
		
		oneOfEachTopic = Arrays.asList(new IPredefinedSession[]{
			scenarioSession,
			watershedSession
		});
		
	}
	
	/**
	 * Test of filter method, on Topic of class FilterPredefinedSessions.
	 */
	@Test
	public void testFilterOnSessionTopic() throws Exception {
		FilterPredefinedSessions instance = new FilterPredefinedSessions(
			new PredefinedSessionRequest(null, null, null, null, null)
			//nulls are ok, the request is not used by filterOnTopic ,
		);
		
		List<IPredefinedSession> filtered = new ArrayList<>(oneOfEachTopic);
		List<IPredefinedSession> expResult = Arrays.asList(new IPredefinedSession[]{
			scenarioSession
		});
		
		instance.filterOnTopic(PredefinedSessionTopic.SCENARIO, filtered);
		
		assertEquals(expResult, filtered);
		
		instance.filterOnTopic(PredefinedSessionTopic.WATERSHED, filtered);
		
		expResult = new ArrayList<>();
		
		assertEquals(expResult, filtered);
	}
	

}