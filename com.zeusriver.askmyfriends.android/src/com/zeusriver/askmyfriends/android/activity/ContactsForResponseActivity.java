package com.zeusriver.askmyfriends.android.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import com.zeusriver.askmyfriends.android.entity.ContactEntity;
import com.zeusriver.askmyfriends.android.entity.MessageEntity;
import com.zeusriver.askmyfriends.android.provider.CopyOfCustomContactsAdapter;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ContactsForResponseActivity extends ListActivity implements OnItemClickListener { 
    /** Called when the activity is first created. */ 
	
	private static final String TAG = "RiteReferral";
	CopyOfCustomContactsAdapter CopyOfcustomContactsAdapter = null;
	MessageEntity responseEntity;
	private ListView mainListView = null;
    ObjectMapper mapper = new ObjectMapper();  
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.contactsforresponse);
		
		responseEntity = (MessageEntity)getIntent().getSerializableExtra("response");
		
		this.mainListView = getListView();
        this.CopyOfcustomContactsAdapter = new CopyOfCustomContactsAdapter(ContactsForResponseActivity.this,
        		R.layout.contact_row, this.PrepareListOfContacts());
        mainListView.setAdapter(this.CopyOfcustomContactsAdapter);
        mainListView.setItemsCanFocus(false);
       // mainListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mainListView.setOnItemClickListener(this);

       // mainListView.setOnItemClickListener(contactSelected);
                
        mainListView.setTextFilterEnabled(true);
		//Button button = (Button)findViewById(R.id.next);
		
		//button.setOnClickListener(this);
		
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
    			//String contactName = null;
	    		//Long contactId = null;
    			
				Cursor emailCur = cr.query(
					ContactsContract.CommonDataKinds.Email.CONTENT_URI,
    				new String[] {Email.DATA, Email.TYPE},
    				ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",    				
    				new String[]{id},null);	
				
				Cursor phoneCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
	    			new String[] {Phone.NUMBER, Email.TYPE},
    				ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
    				new String[]{id,},null);
		
    			while (emailCur.moveToNext()) {
    				
    				int emailType = emailCur.getInt(emailCur.
                			getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
    				    				
    				String emailAddress = emailCur.getString(emailCur.
    					getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
    			    				
    				if(contactEmails.contains(emailAddress)==false) {
            			contactEmails.add(emailAddress);
            			//contactName = name;
            			//contactId = Long.valueOf(id);
            			emailMapping.put(emailAddress,emailType);
    				}
    			}		
    			emailCur.close();
    			
    			while (phoneCur.moveToNext()) {
    				String mobileNumber = phoneCur.getString(phoneCur.
        				getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA));
    					listOfMobileNumbers.add(mobileNumber);
    			}
    			phoneCur.close();

    			contact.setFullName(name);
				//contact.setContactIndex(contactId);
    			contact.setEmailAddress(contactEmails);
    			contact.setMobileNumber(listOfMobileNumbers);
    			contact.setEmailType(emailMapping);
				contact.setPhoneType(phoneMapping);
    			listOfContacts.add(contact);
    		}
    	}
	   	Collections.sort(listOfContacts, new ContactEntity());
    return listOfContacts;
    }

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {		
	     ArrayList<ContactEntity> selectedContacts = new ArrayList<ContactEntity>();
		 ContactEntity singleContact = PrepareListOfContacts().get(pos);	
		 selectedContacts.add(singleContact);	 
		 responseEntity.setContactInfo(selectedContacts);
         
		 Intent i = new Intent(ContactsForResponseActivity.this, ResponseMessageActivity.class);
		 Bundle b = new Bundle();  
		 b.putSerializable("resposeMessageWithContacts",responseEntity);
		 i.putExtras(b);
		 startActivity(i);
		 
		// Toast.makeText(this, "You selected: " + Integer.toString(pos) + singleContact.getFullName(), Toast.LENGTH_LONG).show();

	}
	
	public void goHome(View v) {
		Intent i = new Intent(ContactsForResponseActivity.this, HomeActivity.class);	     
		startActivity(i);
	}
}
