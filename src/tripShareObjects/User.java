package tripShareObjects;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	    private long m_ID;
	    private String m_userName;
	    private String m_name;
	    private String m_lastName;
	    private	String m_password;
	    private ArrayList<String> m_preferredTags;
	    private String m_profileImageString;
	    private boolean m_firstHomePageLaunch = true;

	    public long getID() { return m_ID; }

	    public String getStringUserName() { return m_userName; }

	    public String getPassword()
	    {
	        return m_password;
	    }

	    public String getuserRealName()
	    {
	        return m_name;
	    }

	    public String getLastName()
	    {
	        return m_lastName;
	    }

	    public void setuserRealName(String i_Name)
	    {
	        m_name = i_Name;
	    }

	    public void setLastName(String i_LastName)
	    {
	        m_lastName = i_LastName;
	    }

	    public void setUserID(long i_ID)
	    {
	        m_ID = i_ID;
	    }

	    public void setUserName(String i_userName) { m_userName = i_userName; }

	    public void setPassword(String i_password)
	    {
	        m_password = i_password;
	    }

	    public void addPreferredTag(String i_preferredTag)
	    {
	        if(m_preferredTags == null)
	            m_preferredTags = new ArrayList<>();

	        m_preferredTags.add(i_preferredTag);
	    }
	    
	    public void setPreferredTagsArray(ArrayList<String> i_preferredTags)
	    {
	    	if(m_preferredTags == null)
	    		m_preferredTags = new ArrayList<>();
	    	
	    	m_preferredTags = i_preferredTags;
	    }

	    public void intiallizePreferredTags()
	    {
	        m_preferredTags = new ArrayList<>();
	    }

	    public ArrayList<String> getPreferredTags(){ return m_preferredTags; }
	    
	    public String getImageString() { return m_profileImageString; }

	    public void setImageString(String i_imageString)
	    {
	    	if(m_profileImageString == null)
	    		m_profileImageString = new String();

	    	m_profileImageString = i_imageString; 
	    }
	    
	    public void setfirstHomePageLaunch(Boolean i_firstHomePageLaunch) { m_firstHomePageLaunch = i_firstHomePageLaunch; }
	    
	    public Boolean getfirstHomePageLaunch() { return m_firstHomePageLaunch; }
}
