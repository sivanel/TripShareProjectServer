package user;

import java.io.IOException;
import java.io.PrintWriter;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import tripShareObjects.User;


@WebServlet("/UserImageByIDServlet")
public class UserImageByIDServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Obtain a database connection:
        EntityManagerFactory emf =
           (EntityManagerFactory)getServletContext().getAttribute("emf");
        EntityManager em = emf.createEntityManager();
        
        try 
        {  
        	Long userID = Long.valueOf(request.getParameter("m_userID"));
        	User user = em.find(User.class, userID);
        	String userProfileImageString = user.getImageString();
        	
        	response.setContentType("application/json");
    		response.setCharacterEncoding("UTF-8");
    		
    		PrintWriter out = response.getWriter();
    		out.print(userProfileImageString);
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
