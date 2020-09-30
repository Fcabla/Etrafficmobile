package utilities;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import data.IncidentLocation;
import data.IncidentPoint;
import data.TrafficEvent;
import data.TrafficIncident;
import eu.datex2.schema.x10.x10.D2LogicalModelDocument;
import eu.datex2.schema.x10.x10.Linear;
import eu.datex2.schema.x10.x10.Location;
import eu.datex2.schema.x10.x10.PayloadPublication;
import eu.datex2.schema.x10.x10.Point;
import eu.datex2.schema.x10.x10.ReferencePoint;
import eu.datex2.schema.x10.x10.ReferencePointLinear;
import eu.datex2.schema.x10.x10.Situation;
import eu.datex2.schema.x10.x10.SituationPublication;
import eu.datex2.schema.x10.x10.SituationRecord;
import eu.datex2.schema.x10.x10.TPEGJunction;

public class IncidentDataManager {
	private ArrayList<TrafficIncident> incidents;
	private D2LogicalModelDocument document;
	
	/**
	 * IncidentDataManager constructor
	 * @param document parsed DATEX2 file
	 */
	public IncidentDataManager(D2LogicalModelDocument document) {
		this.incidents = new ArrayList<TrafficIncident>();
		this.document = document;
	}
	
	/**
	 * IncidentDataManager default constructor
	 */
	public IncidentDataManager() {
		this.incidents = new ArrayList<TrafficIncident>();
		//this.document = new D2LogicalModelDocument();
	}

	/**
	 * @return the incidents
	 */
	public ArrayList<TrafficIncident> getIncidents() {
		return incidents;
	}

	/**
	 * @param incidents the incidents to set
	 */
	public void setIncidents(ArrayList<TrafficIncident> incidents) {
		this.incidents = incidents;
	}

	/**
	 * @return the document
	 */
	public D2LogicalModelDocument getDocument() {
		return document;
	}

	/**
	 * @param document the document to set
	 */
	public void setDocument(D2LogicalModelDocument document) {
		this.document = document;
	}
	
	/**
	 * Function that reload the incidents array
	 */
	public void reloadIncidents() {
		incidents.clear();
		loadIncidents();
	}
	
	/**
	 * Function that loads the data from the document into the incidents array
	 */
	public void loadIncidents() {
		PayloadPublication payload = document.getD2LogicalModel().getPayloadPublication();

		SituationPublication sitPub = (SituationPublication) payload.changeType(SituationPublication.type);
		List<Situation> situations = sitPub.getSituationList();
		
		for (Situation sit : situations) {
			TrafficIncident incident = new TrafficIncident();
			ArrayList<TrafficEvent> events = new ArrayList<TrafficEvent>();
			incident.setIncidentId(sit.getId());
			List<SituationRecord> situationRecords = sit.getSituationRecordList();
			
			for(SituationRecord record : situationRecords) {
				TrafficEvent event = new TrafficEvent();
				event.setEventId(record.getId());
				event.setTypeEvent(record.schemaType().getShortJavaName());
				//event.setStartDate(record.getSituationRecordCreationTime().getStringValue());
				event.setStartDate(
						parseDate(record.getValidity().getValidityTimeSpecification().getOverallStartTime().getStringValue()));
				try {
					event.setEndDate(
							parseDate(record.getValidity().getValidityTimeSpecification().getOverallEndTime().getStringValue()));
				}catch (NullPointerException e) {
					// There is not end date so its not a planned event
					event.setEndDate("");
				}
				
				event.setProbabilityOccurrence(record.getProbabilityOfOccurrence().toString());
				event.setDescription("");	
				event.setImpact(record.getImpact().getImpactOnTraffic().toString());	
				if(record.getImpact().getDelays()!= null) {
					event.setDelays(record.getImpact().getDelays().getDelaysType().toString());
				}
				
				IncidentLocation loc = getLocationInfo(record.getGroupOfLocations().getLocationContainedInGroupList());
				event.setLocationEvent(loc);
				event.setDescription(loadDescription (event));
				events.add(event);
			}
			incident.setEvents(events);
			incidents.add(incident);
		}
	}
	
	/**
	 * Function to extract the location from the incident records 
	 * @param locationsList
	 * @return
	 */
	private IncidentLocation getLocationInfo(List<Location> locationsList) {
		IncidentLocation iLoc = new IncidentLocation();
		
		for (Location d2location : locationsList) {
			iLoc.setType(d2location.schemaType().getShortJavaName());
			
			if(d2location.schemaType().equals(Point.type)) {	//if its a point
				
				Point d2Point = ((Point) d2location.changeType(Point.type));
				ReferencePoint refPoint = d2Point.getReferencePoint();
				IncidentPoint iPoint = loadRefPoint(refPoint);
				
				iPoint.setLatitude(d2Point.getPointByCoordinates().getPointCoordinates().getLatitude());
				iPoint.setLongitude(d2Point.getPointByCoordinates().getPointCoordinates().getLongitude());
				iLoc.setPoint(iLoc.new IncidentLocationPoint(iPoint));
				
			}else if(d2location.schemaType().equals(Linear.type)) {		// if its a segment
				//IncidentLocationSegment iLocSegment = iLoc.new IncidentLocationSegment();
				
				Linear d2Segment = (Linear) d2location.changeType(Linear.type);
				ReferencePointLinear refLinear = d2Segment.getReferencePointLinear();
				ReferencePoint refPrimPoint = refLinear.getReferencePointPrimaryLocation().getReferencePoint();
				IncidentPoint iPointPrimary = loadRefPoint(refPrimPoint);
				TPEGJunction junPrim = (TPEGJunction) d2Segment.getTpeglinearLocation().getFrom().changeType(TPEGJunction.type);
				iPointPrimary.setLatitude(junPrim.getPointCoordinates().getLatitude());
				iPointPrimary.setLongitude(junPrim.getPointCoordinates().getLongitude());
				
				
				ReferencePoint refSecPoint = refLinear.getReferencePointSecondaryLocation().getReferencePoint();
				IncidentPoint iPointSecondary = loadRefPoint(refSecPoint);
				TPEGJunction junSec = (TPEGJunction) d2Segment.getTpeglinearLocation().getFrom().changeType(TPEGJunction.type);
				iPointSecondary.setLatitude(junSec.getPointCoordinates().getLatitude());
				iPointSecondary.setLongitude(junSec.getPointCoordinates().getLongitude());
				String towards = "";
				try {
					towards = refPrimPoint.getReferencePointExtension().getExtendedReferencePoint().getDirectionNamed();
				}catch (NullPointerException e) {
					towards = "unknown";
				}
				//System.out.println(towards + " // " + iPointPrimary.getDirection()); //********************************
				iLoc.setSegment(iLoc.new IncidentLocationSegment(iPointPrimary, iPointSecondary, towards));
			}else {
				System.out.println("Data type: " + d2location.schemaType() + " is not supported");
				//System.out.println(d2location);
			}
			
		}
		return iLoc;
	}
	
	/**
	 * Function that loads turns the location data from the document into a iPoint
	 * @param refPoint the data from a point extracted from the document
	 * @return an incidentPoint
	 */
	private IncidentPoint loadRefPoint(ReferencePoint refPoint) {
		
		IncidentPoint iPoint = new IncidentPoint();
		iPoint.setPointId(refPoint.getReferencePointIdentifier());
		iPoint.setAdministrativeArea(refPoint.getAdministrativeArea().getValueList().get(0).getStringValue());
		iPoint.setRoad(refPoint.getRoadNumber());
		iPoint.setKp(String.valueOf(refPoint.getReferencePointDistance()/1000));
		try {
			iPoint.setDirection(refPoint.getDirectionRelative().toString());
		}catch (NullPointerException e) {
			iPoint.setDirection("unknown");
		}
		
		return iPoint;
	}
	
	/**
	 * Function to make a description in HTML format based in the loaded data of a traffic event
	 * @param event the object with the data
	 * @return a string with the description of the event.
	 */
	private String loadDescription (TrafficEvent event) {
		String title = "";
		String road = "";
		String impact = "Impacto en el trafico: " + event.getImpact();
		if(!event.getDelays().equals("")) {
			impact += " con retrasos: " + event.getDelays();
		}
		
		String dates = "Desde: " + event.getStartDate();
		if(!event.getEndDate().equals("")) {
			dates += "<br>Hasta: " + event.getEndDate();
		}
		
		if(event.getLocationEvent().getType().equals(Point.type.getShortJavaName())) {
			
			title = event.getTypeEvent() + " / " + event.getLocationEvent().getPoint().getPoint().getRoad() +
					"(" + event.getLocationEvent().getPoint().getPoint().getKp() + ")";
			
			road = "La carretera " + event.getLocationEvent().getPoint().getPoint().getRoad() + 
					" en el km " + event.getLocationEvent().getPoint().getPoint().getKp() + " en " +
					event.getLocationEvent().getPoint().getPoint().getAdministrativeArea() + " en sentido " +
					event.getLocationEvent().getPoint().getPoint().getDirection();
			
		}else if(event.getLocationEvent().getType().equals(Linear.type.getShortJavaName())) {
			title = event.getTypeEvent() + " / " + event.getLocationEvent().getSegment().getPrimaryPoint().getRoad() +
					"(" + event.getLocationEvent().getSegment().getPrimaryPoint().getKp() + " - " +
					event.getLocationEvent().getSegment().getSecondaryPoint().getKp() + ")";
			
			road = "La carretera " + event.getLocationEvent().getSegment().getPrimaryPoint().getRoad() + " en " +
					event.getLocationEvent().getSegment().getPrimaryPoint().getAdministrativeArea() + " desde el km " +
					event.getLocationEvent().getSegment().getPrimaryPoint().getKp() + " al km " + 
					event.getLocationEvent().getSegment().getSecondaryPoint().getKp() + " en sentido " +
					event.getLocationEvent().getSegment().getPrimaryPoint().getDirection() + " hacia " + 
					event.getLocationEvent().getSegment().getTowards();
		}
		
		String description = "<h4 style=\"display: inline;\">"+title+"</h4><br>"+dates+"<br>"+"<h5>"+impact+"</h5><br>"+""+road+"";
		return description;
	}
	
	/**
	 * Function to convert the date format
	 * @param date from the document 
	 * @return date formatted
	 */
	private String parseDate(String date) {
		String splittedDate [] = date.split("T");

		String partsDate [] = splittedDate[0].split("-");
		splittedDate[1] = splittedDate[1].split("\\.")[0];

		String partsHour [] = splittedDate[1].split(":");
		
		String resultDate = partsHour[0] + ":" + partsHour[1] + "H - " + partsDate[2] + 
				"/" + partsDate[1] + "/" + partsDate[0];
		return resultDate;
	}
	
	/**
	 * Function that returns an event from the incidents array
	 * @param incID the incident that is being searched
	 * @return
	 */
	public TrafficEvent getIncidentbyID(String incID) {
		TrafficEvent te = null;
		for (TrafficIncident i : incidents) {
			if(i.getIncidentId().equals(incID)) {
				te = i.getEvents().get(0);
				return te;
			}
		}
		System.out.println(incID + " not found");
		return te;
	}
	
	public static void main(final String[] args) throws MalformedURLException, IOException{
		DataDownloader dd = new DataDownloader("src/main/java/content.xml","http://infocar.dgt.es/datex2/dgt/SituationPublication/all/content.xml");
		dd.downloadFile();
		dd.loadFile();
		IncidentDataManager idm = new IncidentDataManager(dd.getDocument());
		idm.loadIncidents();
		/*DataLoader dl = new DataLoader("src/main/java/content.xml");
		try {
			dl.loadFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		D2LogicalModelDocument d = dl.getDocument();
		IncidentDataManager idm = new IncidentDataManager(d);
		idm.loadIncidents();
		System.out.println(idm.getIncidents());*/
	}

	
	
}
