package post;

import tripShareObjects.User;
import tripShareObjects.Post;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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

@WebServlet("/HomePagePostsServlet")
public class HomePagePostsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 Gson gson = new Gson();
		
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Obtain a database connection:
       EntityManagerFactory emf =
          (EntityManagerFactory)getServletContext().getAttribute("emf");
       EntityManager em = emf.createEntityManager();
       
       try 
       {  
    	   List<Post> m_postsToSend = new ArrayList<>();
    	   Long userID = Long.valueOf(request.getParameter("m_userID"));
       	   int m_firstPositionToRetrieve = Integer.parseInt(request.getParameter("m_firstPositionToRetrieve"));
    	   User user = em.find(User.class, userID);
    	   ArrayList<String> preferredTag = user.getPreferredTags();
    	   ArrayList<String> relatedTags;
       	// TODO: get all posts from DB as well as user preferred Tags and get into a list all the posts that the user 
    	// might be interested in.
    	 TypedQuery<Post> query = em.createQuery("SELECT p FROM Post p ORDER BY p.m_createdDate DESC", Post.class).setFirstResult(m_firstPositionToRetrieve).setMaxResults(5);
       	 List<Post> postListToSearchIn = query.getResultList();
       	 
       	 // Get all the relevant posts for the user preferences
       	 for (Post post : postListToSearchIn) 
       	 {
       		relatedTags = post.getRelatedTags();
			for(String tag : preferredTag)
			{
				if(relatedTags != null && relatedTags.contains(tag))
				{
					if(m_postsToSend != null && m_postsToSend.contains(post))
						continue;
					if(post.getIsPrivatePost())
					{
						if(post.getUserID() == userID)
							m_postsToSend.add(post);
					}
					else
					{
						m_postsToSend.add(post);
					}
							
				}
			}
       	 }
       	 
       	String postListInJson = gson.toJson(m_postsToSend);
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
}
