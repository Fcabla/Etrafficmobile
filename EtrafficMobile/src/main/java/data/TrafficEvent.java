package data;

import com.google.gson.annotations.Expose;

public class TrafficEvent {
	@Expose
	private String eventId;
	@Expose
	private String typeEvent;
	private String startDate;
	private String endDate;
	private String probabilityOccurrence;
	@Expose
	private String description;
	@Expose
	private String impact;
	private String delays;
	@Expose
	private IncidentLocation locationEvent;
	
	
	public TrafficEvent(String eventId, String typeEvent, String startDate, String endDate,
			String probabilityOccurrence, String description, String impact, String delays, IncidentLocation locationEvent) {
		this.eventId = eventId;
		this.typeEvent = typeEvent;
		this.startDate = startDate;
		this.endDate = endDate;
		this.probabilityOccurrence = probabilityOccurrence;
		this.description = description;
		this.impact = impact;
		this.delays = delays;
		this.locationEvent = locationEvent;
	}
	
	public TrafficEvent() {
		this.eventId = "";
		this.typeEvent = "";
		this.startDate = "";
		this.endDate = "";
		this.probabilityOccurrence = "";
		this.description = "";
		this.impact = "";
		this.delays = "";
		this.locationEvent = new IncidentLocation();
	}
	
	/**
	 * @return the eventId
	 */
	public String getEventId() {
		return eventId;
	}
	/**
	 * @param eventId the eventId to set
	 */
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	/**
	 * @return the typeEvent
	 */
	public String getTypeEvent() {
		return typeEvent;
	}
	/**
	 * @param typeEvent the typeEvent to set
	 */
	public void setTypeEvent(String typeEvent) {
		this.typeEvent = typeEvent;
	}
	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the probabilityOccurrence
	 */
	public String getProbabilityOccurrence() {
		return probabilityOccurrence;
	}
	/**
	 * @param probabilityOccurrence the probabilityOccurrence to set
	 */
	public void setProbabilityOccurrence(String probabilityOccurrence) {
		this.probabilityOccurrence = probabilityOccurrence;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the impact
	 */
	public String getImpact() {
		return impact;
	}
	/**
	 * @param impact the impact to set
	 */
	public void setImpact(String impact) {
		this.impact = impact;
	}
	
	/**
	 * @return the delays
	 */
	public String getDelays() {
		return delays;
	}

	/**
	 * @param delays the delays to set
	 */
	public void setDelays(String delays) {
		this.delays = delays;
	}

	/**
	 * @return the Location
	 */
	public IncidentLocation getLocationEvent() {
		return locationEvent;
	}
	/**
	 * @param locationEvent the Location to set
	 */
	public void setLocationEvent(IncidentLocation locationEvent) {
		this.locationEvent = locationEvent;
	}
	
	/**
	 * Function that returns the location description for a notification
	 * @return description
	 */
	public String getNotDescription() {
		return locationEvent.getNotDescription();
	}
	
	
}
