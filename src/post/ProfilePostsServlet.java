package post;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import tripShareObjects.Post;

@WebServlet("/ProfilePostServlet")
public class ProfilePostsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 Gson gson = new Gson();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Obtain a database connection:
        EntityManagerFactory emf =
           (EntityManagerFactory)getServletContext().getAttribute("emf");
        EntityManager em = emf.createEntityManager();
        
        try 
        {  	
        	// get the user id from the request
        	int userID = Integer.parseInt(request.getParameter("m_userID"));
        	int m_firstPositionToRetrieve = Integer.parseInt(request.getParameter("m_firstPositionToRetrieve"));
        	
        	// read the posts from the DB  
        	TypedQuery<Post> query = em.createQuery(
                    "SELECT p FROM Post p WHERE p.m_userID = :userID ORDER BY p.m_createdDate DESC", Post.class).setParameter("userID", userID).setFirstResult(m_firstPositionToRetrieve).setMaxResults(5);
        	 List<Post> postListToSend = query.getResultList();
    		String postListInJson = gson.toJson(postListToSend);
    		PrintWriter out = response.getWriter();
    		response.setContentType("application/json");
    		response.setCharacterEncoding("UTF-8");
    		out.print(postListInJson);
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
        // Obtain a database connection:
        EntityManagerFactory emf =
           (EntityManagerFactory)getServletContext().getAttribute("emf");
        EntityManager em = emf.createEntityManager();
        
    	try 
    	{
    		// read the object from the request
    		BufferedReader reader = request.getReader();
    		Post m_PostToAddToDB = gson.fromJson(reader, Post.class);
            
    		// insert the object into the DB
    		em.getTransaction().begin();
    		em.persist(m_PostToAddToDB);
    		em.getTransaction().commit();        
    		
    		// set the response with the route generated ID
    		PrintWriter writer = response.getWriter();
    		String postIDToSendBack = String.valueOf(m_PostToAddToDB.getID());
    		writer.print(postIDToSendBack);
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
	
	 @Override
	    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
	    		throws ServletException, IOException {
	    	
	    	 // Obtain a database connection:
	        EntityManagerFactory emf =
	           (EntityManagerFactory)getServletContext().getAttribute("emf");
	        EntityManager em = emf.createEntityManager();
	        
	    	try
	    	{
	        	// get the route id from the request
	        	long postID = Long.parseLong(request.getParameter("m_postID"));
	        	
	        	Post postToDelete = em.find(Post.class, postID);

	        	  em.getTransaction().begin();
	        	  em.remove(postToDelete);
	        	  em.getTransaction().commit();
	    	}
	    	catch(Exception e)
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
