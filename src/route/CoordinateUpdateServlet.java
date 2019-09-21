package route;

import java.io.BufferedReader;
import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import tripShareObjects.Coordinate;


@WebServlet("/CoordinateUpdateServlet")
public class CoordinateUpdateServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		// Obtain a database connection:
        EntityManagerFactory emf =
           (EntityManagerFactory)getServletContext().getAttribute("emf");
        EntityManager em = emf.createEntityManager();
        
        try 
        {  	
        	// read the object from the request
    		BufferedReader reader = request.getReader();
    		Coordinate newCoordinateInfo = new Gson().fromJson(reader, Coordinate.class);
        	        	
    		// Update Additions if needed.
        		em.getTransaction().begin();
				Coordinate coordInDB = em.find(Coordinate.class, newCoordinateInfo.getID());
				coordInDB.setAddition(newCoordinateInfo.getAddition());
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
