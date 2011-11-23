package com.zeusriver.askmyfriends.android.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.zeusriver.askmyfriends.android.entity.ContactEntity;
import com.zeusriver.askmyfriends.android.entity.MessageEntity;
import com.zeusriver.askmyfriends.android.provider.CopyOfDatabaseAdapter;
import com.zeusriver.askmyfriends.android.provider.CustomContactsAdapter;
import com.zeusriver.askmyfriends.android.shared.WebServiceAsyncTask;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactsActivity extends ListActivity { 
    /** Called when the activity is first created. */ 
	
	private static final String TAG = "RiteReferral";
	CustomContactsAdapter customContactsAdapter = null;
	MessageEntity requestEntity;
	private ListView mainListView = null;
	public CopyOfDatabaseAdapter mDbHelper;
    ObjectMapper mapper = new ObjectMapper();  

	//private static final String WEB_SERVICE_POST_URL = "http://ritereferral.appspot.com/testrequest";
	private static final String WEB_SERVICE_POST_URL = "http://askmyfriends-qa.appspot.com/testrequest";
	
    public void onCreate(Bundle savedInstanceState) {
    	    	
        super.onCreate(savedInstanceState);
		setContentView(R.layout.contact);
					
		mDbHelper = new CopyOfDatabaseAdapter(this);
		mDbHelper.open();
		        
        this.mainListView = getListView();
        this.customContactsAdapter = new CustomContactsAdapter(ContactsActivity.this,
        		R.layout.single_contact, this.PrepareListOfContacts());
        mainListView.setAdapter(this.customContactsAdapter);
        mainListView.setItemsCanFocus(false);
        mainListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }  
        
    private ArrayList<ContactEntity> PrepareListOfContacts() {
    	
    	final ArrayList<ContactEntity> listOfContacts = new ArrayList<ContactEntity>();
    	
    	String[] PROJECTION=new String[] { 
    			Contacts._ID,
                Contacts.DISPLAY_NAME,
                Contacts.HAS_PHONE_NUMBER,
        };
    	
    	ContentResolver cr = getContentResolver();
    	Cursor contactCursor = cr.query(ContactsContract.Contacts.
    			CONTENT_URI,PROJECTION,null,null,null);   	
		
    	while(contactCursor.moveToNext()) {
    		
    		ArrayList<String> contactEmails = new ArrayList<String>();
    		ArrayList<String> listOfMobileNumbers = new ArrayList<String>();
    		
    		Map<String,Integer> emailMapping = new HashMap<String,Integer>();
    		Map<String,Integer> phoneMapping = new HashMap<String,Integer>();
    		
    		String id = contactCursor.getString(contactCursor.
    				getColumnIndex(ContactsContract.Contacts._ID));
    		
			String name = contactCursor.getString(contactCursor.
    				getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));		
     		
    		if (Integer.parseInt(contactCursor.getString(contactCursor.
    				getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
    		    			        			
    			ContactEntity contact = new ContactEntity();	
	    		String contactName = null;
	    		Long contactId = null;
	    		
				Cursor emailCur = cr.query(
					ContactsContract.CommonDataKinds.Email.CONTENT_URI,
    				new String[] {Email.DATA, Email.TYPE},
    				ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",    				
    				new String[]{id},null);	
				
				Cursor phoneCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
    				new String[] {Phone.NUMBER, Email.TYPE},
    				ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
    				new String[]{id},null);
				
    			while (emailCur.moveToNext()) {

    				int emailType = emailCur.getInt(emailCur.
            			getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
    				
    				String emailAddress = emailCur.getString(emailCur.
    					getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));   				
    			    				
    				if(contactEmails.contains(emailAddress)==false) {
            			contactEmails.add(emailAddress);
            			contactName = name;
            			contactId = Long.valueOf(id);
            			emailMapping.put(emailAddress,emailType);
    				}
    			}		
    			emailCur.close();
    			
    			while (phoneCur.moveToNext()) {
    			
    				int mobileType = phoneCur.getInt(phoneCur.
                	        getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
    				
    				String mobileNumber = phoneCur.getString(phoneCur.
        				getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA));
    					
						listOfMobileNumbers.add(mobileNumber);   
            			phoneMapping.put(mobileNumber,mobileType);
    			}
    			phoneCur.close();

    			if (contactName != null & !listOfContacts.contains(contactName)) {
    				contact.setFullName(contactName);
    				contact.setContactIndex(contactId);
    				contact.setEmailAddress(contactEmails);
    				contact.setMobileNumber(listOfMobileNumbers);
    				contact.setEmailType(emailMapping);
    				contact.setPhoneType(phoneMapping);
    				listOfContacts.add(contact);
    			}
    		}
    	}
    	contactCursor.close();
	   	Collections.sort(listOfContacts, new ContactEntity());
    return listOfContacts;
    }
        
    public void clearSelections(View v) {
    	for ( int i=0; i< this.customContactsAdapter.getCount(); i++ ) {
    		mainListView.setItemChecked(i, false);
        } this.customContactsAdapter.clearSelections();
    }
        
	public void goHome(View v) {
		Intent i = new Intent(ContactsActivity.this, HomeActivity.class);	     
		startActivity(i);
	}
        
    public void submitMessage(View v) throws JsonGenerationException, JsonMappingException, IOException {
		
    	ArrayList<ContactEntity> selectedContacts = customContactsAdapter.selectedContacts;
    	
    	if (selectedContacts.isEmpty()) {
    		
    		String error = "Select one or more friends";
    		
			LayoutInflater inflater = getLayoutInflater();
			View layout = inflater.inflate(R.layout.toast_layout,
					(ViewGroup)findViewById(R.id.toast_layout));
			
			//ImageView image = (ImageView) layout.findViewById(R.id.image);
			//image.setImageResource(R.drawable.icon);
			TextView text = (TextView) layout.findViewById(R.id.text);
			text.setText(error);
			Toast toast = new Toast(getApplicationContext());
			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			toast.setDuration(Toast.LENGTH_SHORT);
			toast.setView(layout);
			toast.show();
			return;
	    		
    	} else {
    		
    		if (isConnected()) {
    		
	    		requestEntity = (MessageEntity)getIntent().getSerializableExtra("message");
	    		requestEntity.setContactInfo(selectedContacts);
	
				new Thread(new Runnable() {
						public void run() {
							mDbHelper.createMessage("RequestOut",null,
								requestEntity.getCategoryID(),
								requestEntity.getMessageText(),0);
						}
				}).start();
									
	    		String JacksonRepresentation = mapper.writeValueAsString(requestEntity);
	    		WebServiceAsyncTask webServiceTask = new WebServiceAsyncTask();
	    		webServiceTask.execute(WEB_SERVICE_POST_URL,JacksonRepresentation);
	    		
	    		String error = "Message Sent";
	    		
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.toast_layout,
						(ViewGroup)findViewById(R.id.toast_layout));
				
				ImageView image = (ImageView) layout.findViewById(R.id.image);
				image.setImageResource(R.drawable.okicon);
				TextView text = (TextView) layout.findViewById(R.id.text);
				text.setText(error);
				Toast toast = new Toast(getApplicationContext());
				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(layout);
				toast.show();
    		    		
				Intent i = new Intent(ContactsActivity.this, HomeActivity.class);
				startActivity(i);
    		}
    		else {
    			
	    		String error = "No network connection, message can not be sent";
    			
    			LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.toast_layout,
						(ViewGroup)findViewById(R.id.toast_layout));
				
				//ImageView image = (ImageView) layout.findViewById(R.id.image);
				//image.setImageResource(R.drawable.icon);
				TextView text = (TextView) layout.findViewById(R.id.text);
				text.setText(error);
				Toast toast = new Toast(getApplicationContext());
				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toast.setDuration(Toast.LENGTH_SHORT);
				toast.setView(layout);
				toast.show();	
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
           
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mDbHelper.close();
		Log.i(TAG,"On Destroy");
	}
}