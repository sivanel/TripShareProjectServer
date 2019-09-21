package route;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tripShareObjects.Route;

@WebServlet("/RouteNameUpdateServlet")
public class RouteNameUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		 // Obtain a database connection:
        EntityManagerFactory emf =
           (EntityManagerFactory)getServletContext().getAttribute("emf");
        EntityManager em = emf.createEntityManager();
        
        try 
        {  	
        	// get the user id from the request
        	long routeID = Long.parseLong(request.getParameter("m_routeID"));
        	String routeName = new String(request.getParameter("m_newRouteName"));
        	
        	Route routeToUpdate = em.find(Route.class, routeID);
        	
        	em.getTransaction().begin();
        	routeToUpdate.setRouteName(routeName);
        	em.getTransaction().commit();
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
