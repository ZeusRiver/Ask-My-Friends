package com.zeusriver.askmyfriends.android.entity;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;

@JsonAutoDetect
public class AccountEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String accountID;
    private String accountType;
    private String accountStatus;
    private String accountCreationDate;
    private String accountBillingEnabled;
	private List<ContactEntity> contactInfo;
	
	public AccountEntity(String accountID) {
		this.accountID = accountID;
	}
		
	public String getAccountID() {
		return accountID;
	}

	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	public String getAccountCreationDate() {
		return accountCreationDate;
	}

	public void setAccountCreationDate(String accountCreationDate) {
		this.accountCreationDate = accountCreationDate;
	}

	public String getAccountBillingEnabled() {
		return accountBillingEnabled;
	}

	public void setAccountBillingEnabled(String accountBillingEnabled) {
		this.accountBillingEnabled = accountBillingEnabled;
	}

	public List<ContactEntity> getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(List<ContactEntity> contactInfo) {
		this.contactInfo = contactInfo;
	}
}