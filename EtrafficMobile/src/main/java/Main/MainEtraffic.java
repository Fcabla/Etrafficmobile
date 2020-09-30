package Main;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import data.TrafficEvent;
import data.TrafficIncident;
import utilities.DataDownloader;
import utilities.IncidentDataManager;



public final class MainEtraffic {
	
	private static MainEtraffic etrafficInstance = null; 
	private static DataDownloader dd;
	private static IncidentDataManager idm;
	private ScheduledExecutorService executorService;
	
	/**
	 * MainEtraffic constructor
	 * Follows singleton pattern
	 * Sets a timer
	 */
	private MainEtraffic() {
		dd = new DataDownloader();
		idm = new IncidentDataManager();
		executorService = Executors.newSingleThreadScheduledExecutor();
    	executorService.scheduleAtFixedRate(MainEtraffic::loadData, 0, 5, TimeUnit.MINUTES);
	}
	
	/**
	 * Function that returns the current instance of the class (singleton)
	 * @return current instance of the class
	 */
	public static MainEtraffic getInstance(){ 
        if (etrafficInstance == null) {
        	etrafficInstance = new MainEtraffic(); 
        }
        
    	return etrafficInstance; 
    }
	
	/**
	 * Function to download a process the incidents data
	 */
	public static void loadData() {
		try {
			dd.downloadFile();
			dd.loadFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		idm.setDocument(dd.getDocument());
		idm.reloadIncidents();
		System.out.println("data downloaded");
	}

	/**
	 * Function that returns the incidents from the incidentDataManager class
	 * @return the traffic incidents
	 */
	public ArrayList<TrafficIncident> getIncidents(){
		return idm.getIncidents();
	}
	/**
	 * Function to get a incident by its id
	 * @param incID id of the incident searched
	 * @return traffic incident
	 */
	public TrafficEvent getIncidentbyID(String incID) {
		return idm.getIncidentbyID(incID);
	}
	
	public static void main(final String[] args) throws MalformedURLException, IOException {
		//MainEtraffic m = MainEtraffic.getInstance();
	}

	
}
