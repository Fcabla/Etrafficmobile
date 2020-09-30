package webServlets;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Main.MainEtraffic;
import data.TrafficEvent;
import webPush.PushController;
import webPush.Subscription;

/**
 * Servlet implementation class PushNotificationServlet
 */
public class PushNotificationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PushController pc;
	private MainEtraffic m;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PushNotificationServlet() {
        super();
        pc = PushController.getInstance();
        m = MainEtraffic.getInstance();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Not supported
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String subscriptionRequest = new String(); // this is your data sent from client
		try {
			String line = "";
		    BufferedReader reader = request.getReader();
		    while ((line = reader.readLine()) != null)
		    	subscriptionRequest += line;
		  } catch (Exception e) { 
		    e.printStackTrace();
		  }
		
		JsonObject jsonObject = JsonParser.parseString(subscriptionRequest).getAsJsonObject();
		m = MainEtraffic.getInstance();
		pc = PushController.getInstance();
		Subscription sub = pc.getUser(jsonObject.get("endpoint").getAsString());
		
		TrafficEvent nearEvent = m.getIncidentbyID(jsonObject.get("incidentID").getAsString());
		if(nearEvent != null) {
			String titleNot = nearEvent.getTypeEvent() + " a " + jsonObject.get("distance").getAsString() + " metros";
			String typeNot = nearEvent.getTypeEvent();
			String bodyNot = nearEvent.getNotDescription();
			String payload = "{\"title\":\""+titleNot+"\" , \"body\":\""+bodyNot+"\", \"inType\":\""+typeNot+"\"}";
			pc.sendPushMessage(sub, payload.getBytes());
		}
		
	}

}
