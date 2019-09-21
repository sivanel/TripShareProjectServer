package tripShareObjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Post implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue
	private long m_ID;
	private long m_userID;
	private String m_userFirstName;
	private String m_userLastName;
	private String m_postThumbnailString; 
	private String m_title;
	private String m_description;
	private Boolean m_isPrivatePost;
	private Date m_createdDate;
	@OneToOne
	private long m_routeID;
    private ArrayList<Long> m_usersWhichLikedID;
    @OneToMany(cascade=CascadeType.PERSIST)
    private ArrayList<Comment> m_postComments;
    private ArrayList<String> m_relatedTags;

    public Post(int i_userID, String i_title, String i_description)
    {
        m_userID = i_userID;
        setTitle(i_title);
        setDescription(i_description);
        m_usersWhichLikedID = new ArrayList<>();
        m_postComments = new ArrayList<>();
        m_relatedTags = new ArrayList<>();
        m_createdDate = new Date();
    }

    public long getID() { return m_ID; }

    public long getUserID() { return m_userID; }
    public void setUserID(long i_userID) { m_userID = i_userID; }

    public String getTitle() { return m_title; }
    public void setTitle(String i_title) { m_title = new String(i_title); }

    public String getDescription() { return m_description; }
    public void setDescription(String i_description) { m_description = new String(i_description); }

    public long getPostRoute() { return m_routeID; }

    public void setRouteID(long i_routeID)
    {
        m_routeID = i_routeID;
    }

    public void addLikedID(long i_userIDWhichLiked)
    {
        m_usersWhichLikedID.add(i_userIDWhichLiked);
    }

    public Boolean checkIfLikedByUser(Long i_userID)
    {
        Boolean returnValue = false;

        if(m_usersWhichLikedID.contains(i_userID))
            returnValue = true;

        return returnValue;
    }
    
    public void addComment(Comment i_comment)
    {
    	m_postComments.add(i_comment);
    }
    
    public ArrayList<Comment> getCommentsArray()
    {
    	return m_postComments;
    }
    
	public void addRelatedTag(String i_relatedTag)
	{
		if(m_relatedTags == null)
			m_relatedTags = new ArrayList<>();
		
		m_relatedTags.add(i_relatedTag);
	}
	
	public void intiallizeRelatedTags()
	{
		m_relatedTags = new ArrayList<>();
	}
	
	public ArrayList<String> getRelatedTags() { return m_relatedTags; }
	
	public String getUserFirstName()
	{
		return m_userFirstName;
	}
	
	public String getUserLastName()
	{
		return m_userLastName;
	}
	
	public void setUserFirstName(String i_userFirstName)
	{
		m_userFirstName = i_userFirstName;
	}
	
	public void setUserLastName(String i_userLastName)
	{
		m_userLastName = i_userLastName;
	}
	
	public void setThumbnailString(String i_thumbnailString)
	{
		m_postThumbnailString = i_thumbnailString;
	}
	
	public String getThumbnailString()
	{
		return m_postThumbnailString;
	}
	
    public void setTags(ArrayList<String> i_relatedTags)
    {
        m_relatedTags = i_relatedTags;
    }
    
    public Boolean getIsPrivatePost() {return m_isPrivatePost; }
    
    public void setIsPrivatePost(Boolean i_isPrivatePost) { m_isPrivatePost = i_isPrivatePost; }
    
    public void setCreatedDate(Date i_Date) { m_createdDate = i_Date; }
    
    public Date getCreatedDate() { return m_createdDate; }
}
