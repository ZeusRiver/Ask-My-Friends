package com.zeusriver.askmyfriends.android.activity;

import java.io.InputStream;
import java.io.Serializable;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeusriver.askmyfriends.android.entity.MessageEntity;
import com.zeusriver.askmyfriends.android.provider.CopyOfDatabaseAdapter;
import com.zeusriver.askmyfriends.android.provider.DatabaseAdapter;

public class RequestDetailsActivity extends Activity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final String TAG = "RiteReferral";
	public String categoryText;
	public String selectedCategory;
	public DatabaseAdapter mDbHelper;
    public TextView mBodyText;
    TextView headerText;
    TextView tvAccountID;
	String name;
	int id;
	String emailName;
	
	MessageEntity responseEntity = new MessageEntity();
		
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.request_details);
								
		Bundle b = getIntent().getExtras();
		
		headerText = (TextView)findViewById(R.id.message_headertxt);
		tvAccountID = (TextView) findViewById(R.id.field_accountID);
		TextView mBodyText= (TextView) findViewById(R.id.message_entrytxtbox);
		ImageView imageView =(ImageView) findViewById(R.id.contactphoto);

		emailName = b.getString(CopyOfDatabaseAdapter.ACCOUNTID);
		
		headerText.setText(b.getString(CopyOfDatabaseAdapter.REQUEST_CATEGORY));
		mBodyText.setText(b.getString(CopyOfDatabaseAdapter.REQUEST_MESSAGE));

		Uri uri = Uri.withAppendedPath(Email.CONTENT_LOOKUP_URI, Uri.encode(emailName));
		Cursor displayName = getContentResolver().query(uri,
		        new String[]{Email.CONTACT_ID, Email.DISPLAY_NAME, Email.DATA},
		        null, null, null);
    	if (displayName != null) {
    		if (displayName.moveToFirst()) {    				
    			id = displayName.getInt(displayName.
    				getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID));
    		}		
        } 
    	
        startManagingCursor(displayName);
        
        String contactID = Integer.toString(id);
        
        Cursor emailCur = getContentResolver().query(
        		ContactsContract.Contacts.CONTENT_URI,
				new String[] {Contacts.DISPLAY_NAME},
				ContactsContract.Contacts._ID + " = ?",    				
				new String[]{contactID},null);
        
        if (emailCur.moveToFirst()) {
        
        		name  = emailCur.getString(emailCur.
        				getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        		Log.v(TAG,"name is " + name);
        }
        
        Uri contactPhotoUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(), contactPhotoUri);
		Bitmap bitmap = BitmapFactory.decodeStream(input);
		
		imageView.setImageBitmap(bitmap);
		
        
        if (name== null) {
    		tvAccountID.setText(b.getString(CopyOfDatabaseAdapter.ACCOUNTID));
        } else {
    		tvAccountID.setText(name);
        }
	}
	
	public void referContact(View v) {
		
		responseEntity.setMessageType("Response");
		responseEntity.setCategoryID(headerText.getText().toString());
		responseEntity.setRequestorID(emailName);
		//responseEntity.setRequestorID(name);

		Intent i = new Intent(RequestDetailsActivity.this, ContactsForResponseActivity.class);	     
		Bundle b = new Bundle();  
		b.putSerializable("response",responseEntity);
		i.putExtras(b);
		startActivity(i);
	}
	
	public void goHome(View v) {
		Intent i = new Intent(RequestDetailsActivity.this, HomeActivity.class);	     
		startActivity(i);
	}
}