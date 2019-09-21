package route;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.persistence.*;
import com.google.gson.*;

import tripShareObjects.Post;
import tripShareObjects.Route;

@WebServlet("/RouteServlet")
public class RouteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    Gson gson = new Gson();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
    		throws ServletException, IOException {   
    	 // Obtain a database connection:
        EntityManagerFactory emf =
           (EntityManagerFactory)getServletContext().getAttribute("emf");
        EntityManager em = emf.createEntityManager();
        
        try 
        {  	
        	// get the user id from the request
        	Long userID = Long.parseLong(request.getParameter("m_userID"));
        	// read the routes from the DB        
        	 List<Route> routeListToSend = em.createQuery(
                     "SELECT r FROM Route r WHERE r.m_userID = :userID ORDER BY r.m_createdDateObject DESC", Route.class).setParameter("userID", userID).getResultList();
        	 
        	 //remove any route which have been deleted but are used in posts
        	 for(int i=0; i < routeListToSend.size(); i++)
        	 {
        		 if(routeListToSend.get(i).getIsDeletedButUsedInPost())
        			 routeListToSend.remove(i);
        	 }
        	 
    		String routeListInJson = gson.toJson(routeListToSend);
    		PrintWriter out = response.getWriter();
    		response.setContentType("application/json");
    		response.setCharacterEncoding("UTF-8");
    		out.print(routeListInJson);
    		out.flush();
        }
        catch (Exception e) 
    	{
			response.sendError(500, e.getMessage());
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
    protected void doPost(
        HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	 // Obtain a database connection:
        EntityManagerFactory emf =
           (EntityManagerFactory)getServletContext().getAttribute("emf");
        EntityManager em = emf.createEntityManager();
        
    	try 
    	{
    		// read the object from the request
    		BufferedReader reader = request.getReader();
    		Route routeToAddToDB = gson.fromJson(reader, Route.class);
            
    		// insert the object into the DB
    		em.getTransaction().begin();
    		em.persist(routeToAddToDB);
    		em.getTransaction().commit();        
    		
    		// set the response with the route generated ID
    		PrintWriter writer = response.getWriter();
    		String routeIDToSendBack = String.valueOf(routeToAddToDB.getRouteID());
    		writer.print(routeIDToSendBack);		
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
        	long routeID = Long.parseLong(request.getParameter("m_routeID"));
        	Boolean shouldDelete = true;
        	
        	Route routeToDelete = em.find(Route.class, routeID);
        	long userID = routeToDelete.getUserID();
        	
        	// read the posts from the DB  
        	TypedQuery<Post> query = em.createQuery(
                    "SELECT p FROM Post p WHERE p.m_userID = :userID", Post.class).setParameter("userID", userID);
        	 List<Post> userPostList = query.getResultList();
        	 
        	 // if we find post that uses this route, we shall not delete it from the DB
        	 for(int i=0; i<userPostList.size(); i++)
        	 {
        		 if(userPostList.get(i).getPostRoute() == routeID)
        		 {
        			 shouldDelete = false;	 
        			 em.getTransaction().begin();
        			 routeToDelete.setIsDeletedButUsedInPost(true);
               	  	 em.getTransaction().commit();	
        			 break;
        		 }
        	 }

        	 if(shouldDelete)
        	 {
        		 em.getTransaction().begin();
           	  	 em.remove(routeToDelete);
           	  	 em.getTransaction().commit();	 
        	 }
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