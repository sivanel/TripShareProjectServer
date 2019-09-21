package user;

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

import tripShareObjects.User;

@WebServlet("/UploadProfileImageServlet")
public class UploadProfileImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Gson gson = new Gson();
		// Obtain a database connection:
        EntityManagerFactory emf =
           (EntityManagerFactory)getServletContext().getAttribute("emf");
        EntityManager em = emf.createEntityManager();
		
		try 
    	{
			// read the object from the request
    		BufferedReader reader = request.getReader();
    		User userToUpdate = gson.fromJson(reader, User.class);
    		
    		Long userID = userToUpdate.getID();
    		String imageString = userToUpdate.getImageString();
    		
    		em.getTransaction().begin();
    		User userToAddImageTo = em.find( User.class,userID);
    		userToAddImageTo.setImageString(imageString);
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
