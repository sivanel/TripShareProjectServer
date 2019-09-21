package user;

import java.io.IOException;
import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import tripShareObjects.User;

@WebServlet("/UserTagsServlet")
public class UserTagsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Gson gson = new Gson();

    @Override
    protected void doPost(
        HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

		// Obtain a database connection:
		EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
		EntityManager em = emf.createEntityManager();

		try 
		{
			ArrayList<String> userPreferredTags = new ArrayList<>();
			// get the user id from the request
			Long userID = Long.parseLong(request.getParameter("m_userID"));
			// read the object from the request
			String preferredTagsString = request.getParameter("m_userPreferredTags");
			
			JsonParser jsonParser = new JsonParser();
			JsonArray jsonArr = (JsonArray)jsonParser.parse(preferredTagsString).getAsJsonArray();
			
			for (int i = 0; i < jsonArr.size(); i++) {
				JsonElement jsonElement = jsonArr.get(i);
				String preferredTag = new Gson().fromJson(jsonElement.toString(), String.class);
				userPreferredTags.add(preferredTag);
			}

			User user = em.find(User.class, userID);

			em.getTransaction().begin();
			user.setPreferredTagsArray(userPreferredTags);
			em.getTransaction().commit();
			
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
}
