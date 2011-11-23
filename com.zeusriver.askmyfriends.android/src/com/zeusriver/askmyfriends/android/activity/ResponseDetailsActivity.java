package com.zeusriver.askmyfriends.android.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import com.zeusriver.askmyfriends.android.entity.ContactEntity;
import com.zeusriver.askmyfriends.android.entity.MessageEntity;
import com.zeusriver.askmyfriends.android.provider.CopyOfDatabaseAdapter;
import com.zeusriver.askmyfriends.android.provider.DatabaseAdapter;

public class ResponseDetailsActivity extends MenuActivity implements Serializable, 
	OnClickListener {
	
	private static final long serialVersionUID = 1L;
	private static final String TAG = "RiteReferral";

	public String categoryText;
	public String selectedCategory;
	public DatabaseAdapter mDbHelper;
    
	MessageEntity responseEntity;
	List<ContactEntity> selectedContacts;
	RatingBar priceRatingBar;
	RatingBar qualityRatingBar;
	RatingBar customerserviceRatingBar;
	RatingBar speedRatingBar;
	Button slideHandleButton;
	SlidingDrawer slidingDrawer;
	TextView contactName;
    ObjectMapper mapper = new ObjectMapper();
    
    ContactEntity referedContact;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.display_response);
		setContentView(R.layout.display_response);
		
		Bundle b = getIntent().getExtras();		
		referedContact = (ContactEntity)getIntent().getSerializableExtra("referedContact");

		TextView mBodyText = (TextView) findViewById(R.id.message_entrytxtbox);
		contactName = (TextView) findViewById(R.id.contact_fullname);
		TextView category =(TextView)findViewById(R.id.category);
														
		Button button = (Button)findViewById(R.id.btnaddcontact);
		button.setOnClickListener(this);
		
		priceRatingBar = (RatingBar)findViewById(R.id.pricerating);
		qualityRatingBar = (RatingBar)findViewById(R.id.qualityrating);
		customerserviceRatingBar = (RatingBar)findViewById(R.id.customerservicerating);
		speedRatingBar = (RatingBar)findViewById(R.id.speedrating);
		
		//contactName.setText(b.getString(CopyOfDatabaseAdapter.REFERRAL_FULLNAME));
		contactName.setText(referedContact.getFullName());
		mBodyText.setText(b.getString(CopyOfDatabaseAdapter.REQUEST_MESSAGE));
		category.setText(b.getString(CopyOfDatabaseAdapter.REQUEST_CATEGORY));
		
		/*
		priceRatingBar.setRating(b.getFloat(CopyOfDatabaseAdapter.PRICE_RATING));
		qualityRatingBar.setRating(b.getFloat(CopyOfDatabaseAdapter.QUALITY_RATING));
		speedRatingBar.setRating(b.getFloat(CopyOfDatabaseAdapter.SPEED_RATING));
		customerserviceRatingBar.setRating(b.getFloat(CopyOfDatabaseAdapter.SERVICE_RATING));	
		*/
		
		priceRatingBar.setRating(referedContact.getPriceRating());
		qualityRatingBar.setRating(referedContact.getQualityRating());
		speedRatingBar.setRating(referedContact.getEffeciencyRating());
		customerserviceRatingBar.setRating(referedContact.getCustomerServiceRating());
	}
		
	public void goHome(View v) {
		Intent i = new Intent(ResponseDetailsActivity.this, HomeActivity.class);	     
		startActivity(i);
	}

	@Override
	public void onClick(View v) {
			
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int rawContactInsertIndex = ops.size();
	 
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
           .withValue(RawContacts.ACCOUNT_TYPE, null)
           .withValue(RawContacts.ACCOUNT_NAME, null)
           .build());
	        
        //INSERT NAME
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
           .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,rawContactInsertIndex)
           .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
           .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contactName.getText().toString()) // Name of the person
           .build());
	                
        /*
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
	                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,   rawContactInsertIndex)
	                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
	                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, szPhone) // Number of the person
	                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
	                .build());
	        
	        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
	                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,   rawContactInsertIndex)
	                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
	                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, szMobile) // Number of the person
	                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
	                .build());
	        
	        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
	                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,   rawContactInsertIndex)
	                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
	                .withValue(ContactsContract.CommonDataKinds.Email.DATA, szEmail) 
	                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
	                .build());
	        */
	       //Add contact
			Uri newContactUri = null;

			try {
		        ContentProviderResult[] res = getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		        if (res!=null && res[0]!=null) {
		        	newContactUri = res[0].uri;
		        	//02-20 22:21:09 URI added contact:content://com.android.contacts/raw_contacts/612
		        	Log.d(TAG, "URI added contact:"+ newContactUri);
		        }
		        else Log.e(TAG, "Contact not added.");
	        }
	        catch (RemoteException e)
	        { 
	            // error
	        	Log.e(TAG, "Error (1) adding contact.");
	        	newContactUri = null;
	        }
	        catch (OperationApplicationException e) 
	        {
	            // error
	        	Log.e(TAG, "Error (2) adding contact.");
	        	newContactUri = null;
	        }  
	        Log.d(TAG, "Contact added to system contacts.");
	        
	        if (newContactUri == null) {
	        	Log.e(TAG, "Error creating contact");
	        }
	    
				
	}
}