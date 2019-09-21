package user;

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
import com.google.gson.JsonArray;

import tripShareObjects.User;

@WebServlet("/UserInfoValidation")
public class UserInfoValidation extends HttpServlet 
{
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Gson gson = new Gson();
		// Obtain a database connection:
        EntityManagerFactory emf =
           (EntityManagerFactory)getServletContext().getAttribute("emf");
        EntityManager em = emf.createEntityManager();
        
		String userName;
		String password; 
		String userToReturnInJson = "";
		User user = null;
		boolean isValid = false;
		PrintWriter writer = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		userName = gson.fromJson(request.getParameter("m_userName"),String.class);
		password = gson.fromJson(request.getParameter("m_password"), String.class);
		
		try
		{	  
			Query query = em.createQuery("SELECT p FROM User p WHERE p.m_userName =:userName", User.class).setParameter("userName", userName);
			user = (User)query.getSingleResult();
			if(password.equals(user.getPassword())) 
			{
				isValid = true;
				userToReturnInJson = gson.toJson(user);
			}	
		}
		catch (NoResultException e) 
		{
			isValid = false;
		}
		
		JsonArray jsonArray = new JsonArray();
		jsonArray.add(userToReturnInJson);
		jsonArray.add(gson.toJson(isValid));

		writer.print(jsonArray);
		writer.flush();
	}
}

