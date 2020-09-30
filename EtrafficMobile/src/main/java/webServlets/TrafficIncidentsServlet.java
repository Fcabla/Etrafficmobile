package webServlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Main.MainEtraffic;

/**
 * Servlet implementation class TrafficIncidentsServlet
 */
@WebServlet(name = "TrafficIncidentsServlet", urlPatterns = {"/TrafficIncidentsServlet"})
public class TrafficIncidentsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private MainEtraffic m;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TrafficIncidentsServlet() {
        super();
        m = MainEtraffic.getInstance(); 
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
        	m = MainEtraffic.getInstance();
        	Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    		String jsonIncidents = gson.toJson(m.getIncidents());
    	    out.print(jsonIncidents);
    	    out.flush();
        } 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Not supported
		//doGet(request, response);
		
	}

}
