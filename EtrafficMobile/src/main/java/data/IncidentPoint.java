package data;

import com.google.gson.annotations.Expose;

public class IncidentPoint {
	@Expose
	private String pointId;
	private String administrativeArea;	//provincia
	private String road;
	private String kp;
	@Expose
	private float latitude;
	@Expose
	private float longitude;
	private String direction;
	
	public IncidentPoint(String pointId, String administrativeArea, String road, String kp, float latitude, float longitude,
			String direction) {
		
		this.pointId = pointId;
		this.administrativeArea = administrativeArea;
		this.road = road;
		this.kp = kp;
		this.latitude = latitude;
		this.longitude = longitude;
		this.direction = direction;
	}
	
	public IncidentPoint() {
		this.pointId = "";
		this.administrativeArea = "";
		this.road = "";
		this.kp = "";
		this.latitude = 0;
		this.longitude = 0;
		this.direction = "";
	}
	
	
	/**
	 * @return the pointId
	 */
	public String getPointId() {
		return pointId;
	}

	/**
	 * @param pointId the pointId to set
	 */
	public void setPointId(String pointId) {
		this.pointId = pointId;
	}

	/**
	 * @return the administrativeArea
	 */
	public String getAdministrativeArea() {
		return administrativeArea;
	}

	/**
	 * @param administrativeArea the administrativeArea to set
	 */
	public void setAdministrativeArea(String administrativeArea) {
		this.administrativeArea = administrativeArea;
	}

	/**
	 * @return the road
	 */
	public String getRoad() {
		return road;
	}

	/**
	 * @param road the road to set
	 */
	public void setRoad(String road) {
		this.road = road;
	}

	/**
	 * @return the kp
	 */
	public String getKp() {
		return kp;
	}

	/**
	 * @param kp the kp to set
	 */
	public void setKp(String kp) {
		this.kp = kp;
	}

	/**
	 * @return the latitude
	 */
	public float getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public float getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the direction
	 */
	public String getDirection() {
		return direction;
	}

	/**
	 * @param direction the direction to set
	 */
	public void setDirection(String direction) {
		this.direction = direction;
	}
	
	/**
	 * Function that returns the location description for a notification
	 * @return description
	 */
	public String getNotDescription() {
		return "En " + road + " ("+ kp+") en direccion "+ direction;
	}

}
