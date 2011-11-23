package com.zeusriver.askmyfriends.android.entity;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.zeusriver.askmyfriends.android.entity.ContactEntity;

@JsonAutoDetect
public class MessageEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String messageID;
	private String accountID;
	private String requestorID;
	private String messageType;
	private String categoryID;
	private String messageText;
	private String neededByDate; /* Change this to Date format after */
	private String respondedFlag;
	private List<ContactEntity> contactInfo;
	private String postalCode;
    private String city;
    		
	public MessageEntity() {
	}

	public MessageEntity(String messageID, String accountID, String requestorID,
			String messageType, String categoryID,
			String messageText, String neededByDate,
			String respondedFlag, List<ContactEntity> contactInfo,
			String postalCode, String city) {
		super();
		this.messageID = messageID;
		this.accountID = accountID;
		this.requestorID = requestorID;
		this.messageType = messageType;
		this.categoryID = categoryID;
		this.messageText = messageText;
		this.neededByDate = neededByDate;
		this.respondedFlag = respondedFlag;
		this.contactInfo = contactInfo;
		this.postalCode = postalCode;
		this.city = city;
	}
		
	public String getMessageID() {
		return messageID;
	}
	
	public String getAccountID() {
		return accountID;
	}
	
	public String getRequestorID() {
		return requestorID;
	}
		
	public String getMessageType() {
		return messageType;
	}
	
	public String getCategoryID() {
		return categoryID;
	}
	
	public String getMessageText() {
		return messageText;
	}
	
	public String getNeededByDate() {
		return neededByDate;
	}
	
	@JsonIgnore
	public String getRespondedFlag() {
		return respondedFlag;
	}
	
	public List<ContactEntity> getContactInfo() {
		return contactInfo;
	}

	public String getPostalCode() {
		return postalCode;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}
	
	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}
	
	public void setRequestorID(String requestorID) {
		this.requestorID = requestorID;
	}
		
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	
	public void setCategoryID(String categoryID) {
		this.categoryID = categoryID;
	}
	
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
	
	public void setNeededByDate(String neededByDate) {
		this.neededByDate = neededByDate;
	}
	
	@JsonIgnore
	public void setRespondedFlag(String respondedFlag) {
		this.respondedFlag = respondedFlag;
	}
	
	public void setContactInfo(List<ContactEntity> contactInfo) {
		this.contactInfo = contactInfo;
	}
	
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public void setCity(String city) {
		this.city = city;
	}
}