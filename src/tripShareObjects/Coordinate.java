package tripShareObjects;

import java.io.Serializable;
import javax.persistence.*;

@Entity
public class Coordinate implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue
	private long m_ID;
	private String m_latitude;
	private String m_longitude;
	@OneToOne(cascade=CascadeType.PERSIST)
	private Addition m_addition;

	public Coordinate() { }

	Coordinate(String i_Latitude, String i_Longitude) 
	{
	        this.m_latitude = new String(i_Latitude);
	        this.m_longitude = new String(i_Longitude);
	}
	
	public long getID()
	{
		return m_ID;
	}
	
	public String getLatitude()
	{
		return m_latitude;
	}
	
	public String getLongitude()
	{
		return m_longitude;
	}
	
	public Addition getCoordinateAddition()
	{
		return m_addition;
	}
	
	public void setAddition(Addition i_additionToSet)
	{
		if(m_addition == null)
			m_addition = new Addition();
		
		m_addition.setImageString(i_additionToSet.getImageString());
		m_addition.setNote(i_additionToSet.getNote());
	}
	
	public Addition getAddition()
	{
		return m_addition;
	}
	
	@Override
    public String toString() {
        return String.format("%d, %d", this.m_latitude, this.m_longitude);
    }
}