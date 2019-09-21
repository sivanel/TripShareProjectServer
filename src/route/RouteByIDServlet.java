package route;

import java.io.IOException;
import java.io.PrintWriter;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import tripShareObjects.Route;

@WebServlet("/RouteByIDServlet")
public class RouteByIDServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
      Gson gson = new Gson();
      
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		 // Obtain a database connection:
        EntityManagerFactory emf =
           (EntityManagerFactory)getServletContext().getAttribute("emf");
        EntityManager em = emf.createEntityManager();
        
        try 
        {  	
        	// get the user id from the request
        	int routeID = Integer.parseInt(request.getParameter("m_routeID"));
        	// read the routes from the DB        
        	 Route routeToSend = em.createQuery(
                     "SELECT r FROM Route r WHERE r.m_ID = :routeID", Route.class).setParameter("routeID", routeID).getSingleResult();
    		String routeInJson = gson.toJson(routeToSend);
    		PrintWriter out = response.getWriter();
    		response.setContentType("application/json");
    		response.setCharacterEncoding("UTF-8");
    		out.print(routeInJson);
    		out.flush();
        }
        catch (Exception e) 
    	{
			response.sendError(500, e.toString());
		}
        finally 
        {
            // Close the database connection:
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            em.close();
        }
	}


}
