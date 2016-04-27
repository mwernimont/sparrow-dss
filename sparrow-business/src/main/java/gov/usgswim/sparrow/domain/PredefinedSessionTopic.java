
package gov.usgswim.sparrow.domain;

/**
 * Categorizes predefined sessions
 */
public enum PredefinedSessionTopic {
	WATERSHED("WATERSHED"), //a hydrologic spatial extent
	SCENARIO("SCENARIO"); //adjustments that are not specific to a spatial extent
	private final String text;

	PredefinedSessionTopic(String text) {
		this.text = text;
	}
	
	public String toString(){
		return this.text;
	}
	
	public static PredefinedSessionTopic fromString(String text) {
		if (text != null) {

			for (PredefinedSessionTopic b : PredefinedSessionTopic.values()) {
				if (text.equalsIgnoreCase(b.toString())) {
					return b;
				}
			}
		}
		return null;
	}
}
