package data;

import com.google.gson.annotations.Expose;

public class IncidentLocation {
	@Expose
	private String type;
	@Expose
	private IncidentLocationPoint point;
	@Expose
	private IncidentLocationSegment segment;
	
	/**
	 * IncidentLocation constructor
	 */
	public IncidentLocation() {
		this.type = "";
		this.point = new IncidentLocationPoint();
		this.segment = new IncidentLocationSegment();
	}
	
	/**
	 * Function that returns the location description for a notification
	 * @return
	 */
	public String getNotDescription() {
		if(this.type.equals("Point")) {
			return point.getNotDescription();
		}else if (this.type.equals("Linear")) {
			return segment.getNotDescription();
		}
		return "";
	}
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the point
	 */
	public IncidentLocationPoint getPoint() {
		return point;
	}

	/**
	 * @param point the point to set
	 */
	public void setPoint(IncidentLocationPoint point) {
		this.point = point;
	}

	/**
	 * @return the segment
	 */
	public IncidentLocationSegment getSegment() {
		return segment;
	}

	/**
	 * @param segment the segment to set
	 */
	public void setSegment(IncidentLocationSegment segment) {
		this.segment = segment;
	}
	
	public class IncidentLocationPoint{
		@Expose
		private IncidentPoint point;
		
		/**
		 * IncidentLocationPoint default constructor 
		 */
		public IncidentLocationPoint() {
			point = new IncidentPoint();
		}
		
		/**
		 * Function that returns the location description for a notification
		 * @return description
		 */
		public String getNotDescription() {
			return point.getNotDescription();
		}

		/**
		 * IncidentLocationPoint constructor 
		 * @param point IncidentPoint
		 */
		public IncidentLocationPoint(IncidentPoint point) {
			this.point = point;
		}

		/**
		 * @return the point
		 */
		public IncidentPoint getPoint() {
			return point;
		}

		/**
		 * @param point the point to set
		 */
		public void setPoint(IncidentPoint point) {
			this.point = point;
		}
	
	}
	
	public class IncidentLocationSegment{
		@Expose
		private IncidentPoint primaryPoint;
		@Expose
		private IncidentPoint secondaryPoint;
		private String towards;
		
		/**
		 * IncidentLocationSegment constructor 
		 * @param primaryPoint IncidentPoint
		 * @param secondaryPoint IncidentPoint
		 * @param towards direction 
		 */
		public IncidentLocationSegment(IncidentPoint primaryPoint, IncidentPoint secondaryPoint, String towards) {
			this.primaryPoint = primaryPoint;
			this.secondaryPoint = secondaryPoint;
			this.towards = towards;
		}
		
		/**
		 * IncidentLocationSegment default constructor
		 */
		public IncidentLocationSegment() {
			this.primaryPoint = new IncidentPoint();
			this.secondaryPoint = new IncidentPoint();
			this.towards = "";
		}
		
		/**
		 * Function that returns the location description for a notification
		 * @return description
		 */
		public String getNotDescription() {
			return primaryPoint.getNotDescription();
		}

		

		/**
		 * @return the primaryPoint
		 */
		public IncidentPoint getPrimaryPoint() {
			return primaryPoint;
		}

		/**
		 * @param primaryPoint the primaryPoint to set
		 */
		public void setPrimaryPoint(IncidentPoint primaryPoint) {
			this.primaryPoint = primaryPoint;
		}

		/**
		 * @return the secondaryPoint
		 */
		public IncidentPoint getSecondaryPoint() {
			return secondaryPoint;
		}

		/**
		 * @param secondaryPoint the secondaryPoint to set
		 */
		public void setSecondaryPoint(IncidentPoint secondaryPoint) {
			this.secondaryPoint = secondaryPoint;
		}

		/**
		 * @return the towards
		 */
		public String getTowards() {
			return towards;
		}

		/**
		 * @param towards the towards to set
		 */
		public void setTowards(String towards) {
			this.towards = towards;
		}
		
		
	}
}
