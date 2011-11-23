package com.zeusriver.askmyfriends.android.entity;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;

@JsonAutoDetect
public class LocationEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
	private String messageID;   
    private String postalCode;
    private String city;
    private String state;
    private String country;

    public LocationEntity() {
    }

    public LocationEntity(String postalCode, String city, String state, String country) {
        super();
        
        this.postalCode = postalCode;
        this.city = city;
        this.state = state;
        this.country = country;
    }

	@JsonIgnore
    public String getMessageID() {
		return messageID;
	}
    
    public String getPostalCode() {
        return postalCode;
    }
    
    public String getCity() {
        return city;
    }
    
    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
}