package webServlets;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import webPush.PushController;
import webPush.Subscription;

/**
 * Servlet implementation class PushSubscriptionServlet
 */
public class PushSubscriptionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private PushController pc;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PushSubscriptionServlet() {
        super();
        pc = PushController.getInstance();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Not supported
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String subscriptionRequest = new String(); 
		try {
			String line = "";
		    BufferedReader reader = request.getReader();
		    while ((line = reader.readLine()) != null)
		    	subscriptionRequest += line;
		    
	    } catch (Exception e) { 
		    e.printStackTrace();
	    }

		//System.out.println(subscriptionRequest);
		JsonObject jsonObject = JsonParser.parseString(subscriptionRequest).getAsJsonObject();

		Subscription sub = new Subscription(
				jsonObject.get("auth").getAsString(),
				jsonObject.get("key").getAsString(),
				jsonObject.get("endpoint").getAsString()
		);
		
		pc = PushController.getInstance();
		pc.subscribeUser(sub);
	}
	

}
