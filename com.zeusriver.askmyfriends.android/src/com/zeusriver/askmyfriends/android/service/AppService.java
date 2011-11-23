package com.zeusriver.askmyfriends.android.service;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.zeusriver.askmyfriends.android.activity.HomeActivity;
import com.zeusriver.askmyfriends.android.activity.R;
import com.zeusriver.askmyfriends.android.entity.ContactEntity;
import com.zeusriver.askmyfriends.android.entity.MessageEntity;
import com.zeusriver.askmyfriends.android.provider.CopyOfDatabaseAdapter;
import com.zeusriver.askmyfriends.android.service.WakefulIntentService;
import com.zeusriver.askmyfriends.android.shared.BasicWebService;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

public class AppService extends WakefulIntentService {
 
	private static final String PREF = "RiteReferralPrefences";
	private static final String TAG = "RiteReferral";
	public CopyOfDatabaseAdapter mDbHelper;
	List<MessageEntity> messageEntity = null; 

	public AppService() {
		super("AppService");
	}

	@Override
	protected void doWakefulWork(Intent intent) {
	
			if (isConnected()) {
				if (pollServer(getPreferences()) != null) {
					if (writeMessageToDB()) {
						notifyUser();
					}
				}
			}
	}
	
	public boolean isConnected() {
		String cs = Context.CONNECTIVITY_SERVICE;		
	    ConnectivityManager cm = (ConnectivityManager) getSystemService(cs);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnected()) {
	        return true;
	    }
	    return false;
	}
	
	private String getPreferences() {
		SharedPreferences preferences = getSharedPreferences(PREF, 0);  
		String accountID;	
		/*Check if there is a preference set for RiteReferral */
		if (preferences.contains("accountName")) {
			accountID = preferences.getString("accountName", "No accountID found");
		}
				
		/*If no preferences were set (i.e. app was just downloaded) 
		 * then select first google account if there are multiple */
		else {
			AccountManager manager = AccountManager.get(this);
		    Account[] accounts = manager.getAccountsByType("com.google");
		    accountID = accounts[0].toString();
		}
	return accountID;	
	}
	
	private List<MessageEntity> pollServer(String accountID) {
		
		ObjectMapper mapper = new ObjectMapper();
		
		Uri uri = new Uri.Builder()
			.scheme("http")
			//.authority("ritereferral.appspot.com")
			//.authority("askmycontacts.appspot.com")
			.authority("askmyfriends-qa.appspot.com")
			.path("/testrequest")
			//.appendQueryParameter("accountIDParam", accountID)
			.appendQueryParameter("accountIDParam", "anitha.kunathasan@gmail.com")
			.build();
		 		 		
		BasicWebService webService = new BasicWebService(uri.toString()); 
		try {
			String jsonMessage = webService.webGet();
			if (jsonMessage !=null) {
				messageEntity = mapper.readValue(jsonMessage,new TypeReference<List<MessageEntity>>(){});
			} else {
				messageEntity = null;
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return messageEntity;
	}
	
	public boolean writeMessageToDB() {
		
		if (!messageEntity.isEmpty()) {
			for(MessageEntity message: messageEntity) {
				mDbHelper = new CopyOfDatabaseAdapter(this);
				mDbHelper.open();
			 
				long rowID = mDbHelper.createMessage(
					message.getMessageType(),
					message.getAccountID(),
					message.getCategoryID(),
					message.getMessageText(),
					0);
				
				for(ContactEntity contact: message.getContactInfo()) {
					
					Iterator iterator = contact.getEmailType().keySet().iterator();

					while (iterator.hasNext()) {
						
						String type = "EMAIL";

						String email = (String) iterator.next();
						int i = contact.getEmailType().get(email);
						mDbHelper.createContact(rowID,contact.getFullName(),type,email,i);
					}
					
					Iterator iterator2 = contact.getPhoneType().keySet().iterator();

					while (iterator2.hasNext()) {
						
						String type = "PHONE";

						String phone = (String) iterator2.next();
						int i = contact.getPhoneType().get(phone);
						mDbHelper.createContact(rowID,contact.getFullName(),type,phone,i);
					}
					
					/*while (contact.getEmailType().keySet().iterator().hasNext()) {
						String type = "EMAIL";

						int i = contact.getEmailType().keySet().iterator().next();
						String email = contact.getEmailType().get(i);
						mDbHelper.createContact(rowID,contact.getFullName(),type,email);	
					}
					while (contact.getPhoneType().keySet().iterator().hasNext()) {
						String type = "PHONE";
						
						int i = contact.getPhoneType().keySet().iterator().next();
						String phone = contact.getPhoneType().get(i);
						mDbHelper.createContact(rowID,contact.getFullName(),type,phone);

					}*/
							
				/*for (ContactEntity contact: message.getContactInfo()) {
					for (String mobile: contact.getMobileNumber()) {
						String type = "PHONE";
						mDbHelper.createContact(rowID,contact.getFullName(),type,mobile);	
					}
					for (String email: contact.getEmailAddress()) {
						String type = "EMAIL";
						mDbHelper.createContact(rowID,contact.getFullName(),type,email);
					}*/
					mDbHelper.enterRating(rowID, contact.getFullName(),
						contact.getOverAllRating(),contact.getPriceRating(),
						contact.getQualityRating(),contact.getCustomerServiceRating(),
						contact.getEffeciencyRating(),contact.getKnowledgeRating());
				}
			} return true;
		}
		else {
			return false;
		}
	}
				
	private void notifyUser(){
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		
		int icon = R.drawable.rricon64;
		CharSequence tickerText = "You've Received a Message";
		long when = System.currentTimeMillis();
		
		Notification notification = new Notification(icon, tickerText, when);
		
		Context context = getApplicationContext();
		CharSequence contentTitle = "Ask My Friends Message";
		CharSequence contentText = "You have received a new message";
		
		Intent notificationIntent;
		notificationIntent = new Intent(this, HomeActivity.class);		
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
	
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		
		final int HELLO_ID = 1;

		mNotificationManager.notify(HELLO_ID, notification);
	}
}