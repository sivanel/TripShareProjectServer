package user;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tripShareObjects.User;

@WebServlet("/UpdateFirstHomePageLaunchServlet")
public class UpdateFirstHomePageLaunchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Obtain a database connection:
	       EntityManagerFactory emf =
	          (EntityManagerFactory)getServletContext().getAttribute("emf");
	       EntityManager em = emf.createEntityManager();
	       
	       try 
	       {  	
	       	// get the user id from the request
	       	Long userID = Long.parseLong(request.getParameter("m_userID"));
	       	User user = em.find(User.class, userID);
	   		
	   		em.getTransaction().begin();
	   		user.setfirstHomePageLaunch(false);
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
