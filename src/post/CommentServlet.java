package post;

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

import tripShareObjects.Comment;
import tripShareObjects.Post;

@WebServlet("/CommentServlet")
public class CommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 Gson gson = new Gson();
	
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
		{
			// Obtain a database connection:
       EntityManagerFactory emf =
          (EntityManagerFactory)getServletContext().getAttribute("emf");
       EntityManager em = emf.createEntityManager();
       
       try 
       {  	
       	// get the user id from the request
       	Long postID = Long.parseLong(request.getParameter("m_PostID"));
       	// read the object from the request
		BufferedReader reader = request.getReader();
       	Comment comment = gson.fromJson(reader, Comment.class);

       	Post post = em.find(Post.class, postID);
   		
   		em.getTransaction().begin();
   		post.addComment(comment);
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
