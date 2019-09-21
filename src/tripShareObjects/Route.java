package tripShareObjects;

import java.io.Serializable;
import javax.persistence.*;

import java.util.*;

@Entity
public class Route implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue
	private long m_ID;
	private long m_userID;
	private String m_routeName;
	private String m_createdDate;
	private Date m_createdDateObject;
    private Boolean m_isDeletedButUsedInPost;
	@OneToMany(cascade=CascadeType.PERSIST)
	private List<Coordinate> m_routeCoordinates= new ArrayList<>(); 
	
	public Route(int i_userID) { m_userID = i_userID; m_isDeletedButUsedInPost = false; m_createdDateObject = new Date();}
	
	public long getUserID() { return m_userID; }
	
	public long getRouteID() { return m_ID; }
	
	public String getRouteName() { return m_routeName; }
	
	public List<Coordinate> getRouteCoordinates() { return m_routeCoordinates; }
	
	public void addCoordinateToRoute(Coordinate i_newCoordinate) { m_routeCoordinates.add(i_newCoordinate); }
	
	public void setRouteCoordinates(List<Coordinate> i_routeCoordinates) { m_routeCoordinates = i_routeCoordinates; }
	
	public void setRouteName(String i_routeName) { m_routeName = new String(i_routeName); }
	
	public String getCreatedDate() { return m_createdDate; }
	
	public void setCreatedDate(String i_createdDate) { m_createdDate = i_createdDate; }

	 public Boolean getIsDeletedButUsedInPost() { return m_isDeletedButUsedInPost; }

	 public void setIsDeletedButUsedInPost(Boolean i_isDeletedButUsedInPost) { m_isDeletedButUsedInPost = i_isDeletedButUsedInPost; }
	 
	 public void setCreatedDateObject(Date i_Date) { m_createdDateObject = i_Date; }
	    
	 public Date getCreatedDateObject() { return m_createdDateObject; }
}
