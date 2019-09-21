package user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import tripShareObjects.User;

@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Gson gson = new Gson();
		// Obtain a database connection:
        EntityManagerFactory emf =
           (EntityManagerFactory)getServletContext().getAttribute("emf");
        EntityManager em = emf.createEntityManager();
		User user;
		Boolean isUserExist = false;
		PrintWriter writer = response.getWriter();
		
		try 
    	{
    		// read the object from the request
    		BufferedReader reader = request.getReader();
    		 User userToAdd = gson.fromJson(reader, User.class);
            
    		 try
    			{	  
    				Query query = em.createQuery("SELECT p FROM User p WHERE p.m_userName =:userName", User.class).setParameter("userName", userToAdd.getStringUserName());
    				user = (User)query.getSingleResult();
    				if(user != null)
    					isUserExist = true;
    			}
    			catch (NoResultException e) 
    			{
    				isUserExist = false;
    			}
    		 
    		 if(isUserExist)
    			 writer.print("-1"); // means user already exists
    		 else
    		 {
    				// insert the object into the DB
    	    		em.getTransaction().begin();
    	    		em.persist(userToAdd);
    	    		em.getTransaction().commit(); 
    	    		
    	    		// set the response with the route generated ID
    	    		String userIDToSendBack = String.valueOf(userToAdd.getID());
    	    		writer.print(userIDToSendBack);	
    		 }
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
