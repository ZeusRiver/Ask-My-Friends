package com.zeusriver.askmyfriends.android.entity;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;

@JsonAutoDetect
public class ContactEntity implements Serializable, Comparator<ContactEntity>{

	private static final long serialVersionUID = 1L;
	
	private String accountID;
	private String fullName;
	private List<String> mobileNumber;
	private List<String> emailAddress;
	private float overAllRating;
	private float priceRating;
	private float qualityRating;
	private float customerServiceRating;
	private float effeciencyRating;
	private float knowledgeRating;
	private long contactIndex;
	private LocationEntity locationInfo;
	private Map<String,Integer> emailType;
	private Map<String,Integer> phoneType;

	public ContactEntity() {
	}

	public ContactEntity(String accountID, String fullName,
			List<String> mobileNumber, List<String> emailAddress, LocationEntity locationInfo, 
			boolean receivedFlag, float overAllRating, float priceRating, float qualityRating, 
			float customerServiceRating, float effeciencyRating, float knowledgeRating, 
			Map<String,Integer> emailType,Map<String,Integer> phoneType) {	
		super();
		
		this.accountID = accountID;
		this.fullName = fullName;
		this.mobileNumber = mobileNumber;
		this.emailAddress = emailAddress;
		this.locationInfo = locationInfo;
		this.overAllRating = overAllRating;
		this.priceRating = priceRating;
		this.qualityRating = qualityRating;
		this.customerServiceRating = customerServiceRating;
		this.effeciencyRating = effeciencyRating;
		this.knowledgeRating = knowledgeRating;
		this.emailType = emailType;
		this.phoneType = phoneType;
	}
	
	public String getAccountID() {
		return accountID;
	}
	
	public String getFullName() {
		return fullName;
	}
		
	public List<String> getMobileNumber() {
		return mobileNumber;
	}
	
	public List<String> getEmailAddress() {
		return emailAddress;
	}
	
	public LocationEntity getLocationInfo() {
		return locationInfo;
	}
	
	public float getOverAllRating() {
		return overAllRating;
	}
	
	public float getPriceRating() {
		return priceRating;
	}

	public float getQualityRating() {
		return qualityRating;
	}

	public float getCustomerServiceRating() {
		return customerServiceRating;
	}

	public float getEffeciencyRating() {
		return effeciencyRating;
	}

	public float getKnowledgeRating() {
		return knowledgeRating;
	}
	
	@JsonIgnore
	public long getContactIndex() {
		return contactIndex;
	}
	
	public Map<String,Integer> getPhoneType() {
		return phoneType;
	}
	
	public Map<String,Integer> getEmailType() {
		return emailType;
	}
	
	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public void setMobileNumber(List<String> mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	
	public void setEmailAddress(List<String> emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public void setLocationInfo(LocationEntity locationInfo) {
		this.locationInfo = locationInfo;
	}
	
	public void setOverAllRating(float overAllRating) {
		this.overAllRating = overAllRating;
	}
	
	public void setPriceRating(float priceRating) {
		this.priceRating = priceRating;
	}
	
	public void setQualityRating(float qualityRating) {
		this.qualityRating = qualityRating;
	}
	
	public void setCustomerServiceRating(float customerServiceRating) {
		this.customerServiceRating = customerServiceRating;
	}
	
	public void setEffeciencyRating(float effeciencyRating) {
		this.effeciencyRating = effeciencyRating;
	}
	
	public void setKnowledgeRating(float knowledgeRating) {
		this.knowledgeRating = knowledgeRating;
	}
	
	@JsonIgnore
	public void setContactIndex(long contactIndex) {
		this.contactIndex = contactIndex;
	}
	
	public void setEmailType(Map<String,Integer> emailType) {
		this.emailType = emailType;
	}
	
	public void setPhoneType(Map<String,Integer> phoneType) {
		this.phoneType = phoneType;
	}

	@Override
	public int compare(ContactEntity arg0, ContactEntity arg1) {
		return arg0.getFullName().compareTo(arg1.getFullName()) ;
	}
}