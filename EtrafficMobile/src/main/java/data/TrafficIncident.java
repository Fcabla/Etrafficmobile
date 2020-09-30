package data;
import java.util.ArrayList;

import com.google.gson.annotations.Expose;

/**
 * Class for representing traffic incidents
 * @author fer_n
 *
 */
public class TrafficIncident {
	@Expose
	private String incidentId;
	@Expose
	private ArrayList<TrafficEvent> events;
	
	public TrafficIncident (String incidentId, ArrayList<TrafficEvent> events) {
		this.incidentId = incidentId;
		this.events = events;
	}
	
	public TrafficIncident () {
		incidentId = "";
		events = new ArrayList<TrafficEvent>();
	}
	
	/**
	 * @return the incidentId
	 */
	public String getIncidentId() {
		return incidentId;
	}

	/**
	 * @param incidentId the incidentId to set
	 */
	public void setIncidentId(String incidentId) {
		this.incidentId = incidentId;
	}

	/**
	 * @return the events
	 */
	public ArrayList<TrafficEvent> getEvents() {
		return events;
	}

	/**
	 * @param events the events to set
	 */
	public void setEvents(ArrayList<TrafficEvent> events) {
		this.events = events;
	}
}
