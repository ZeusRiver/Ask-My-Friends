package com.zeusriver.askmyfriends.android.activity;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RatingBar.OnRatingBarChangeListener;

import com.zeusriver.askmyfriends.android.entity.ContactEntity;
import com.zeusriver.askmyfriends.android.entity.MessageEntity;
import com.zeusriver.askmyfriends.android.provider.DatabaseAdapter;
import com.zeusriver.askmyfriends.android.shared.WebServiceAsyncTask;

public class ResponseMessageActivity extends Activity implements Serializable, 
	OnClickListener, OnRatingBarChangeListener {
	
	private static final long serialVersionUID = 1L;
    private static int contactCount=0; /* keep track of selected contacts */

	public String categoryText;
	public String selectedCategory;
	public DatabaseAdapter mDbHelper;
    public EditText mBodyText;
    public EditText contactName;
    public EditText etCity;
    int numberOfSelectedContacts;
    
    static MessageEntity responseEntity;
	List<ContactEntity> selectedContacts;
	RatingBar priceRatingBar;
	RatingBar qualityRatingBar;
	RatingBar customerserviceRatingBar;
	RatingBar speedRatingBar;
    ObjectMapper mapper = new ObjectMapper();
    
	
	private static final String PREF = "RiteReferralPrefences";
	//private static final String WEB_SERVICE_POST_URL = "http://ritereferral.appspot.com/testresponse";
	private static final String WEB_SERVICE_POST_URL = "http://askmyfriends-qa.appspot.com/testresponse";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.response);
	    this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		/*LocationManager locManager = (LocationManager) getSystemService(LOCATION_SERVICE); 
		if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			this.findLocation();
		}*/
	 
		responseEntity = (MessageEntity)getIntent().getSerializableExtra("resposeMessageWithContacts");
	    selectedContacts = (List<ContactEntity>) responseEntity.getContactInfo();
		numberOfSelectedContacts = selectedContacts.size();   
	    
	    /*if (getLastNonConfigurationInstance() != null)
	    {
	    	responseEntity = (MessageEntity)getLastNonConfigurationInstance();
	    } else {
			responseEntity = (MessageEntity)getIntent().getSerializableExtra("resposeMessageWithContacts");
			selectedContacts = (List<ContactEntity>) responseEntity.getContactInfo();
			numberOfSelectedContacts = selectedContacts.size();
	    }*/
		
		mBodyText = (EditText) findViewById(R.id.message_entrytxtbox);
		TextView contactName = (TextView) findViewById(R.id.contact_fullname);
		TextView category = (TextView) findViewById(R.id.category);

		contactName.setText(selectedContacts.get(contactCount).getFullName());
		category.setText(responseEntity.getCategoryID());
		
		Button button = (Button)findViewById(R.id.btnsendresponse);
		priceRatingBar = (RatingBar)findViewById(R.id.pricerating);
		qualityRatingBar = (RatingBar)findViewById(R.id.qualityrating);
		customerserviceRatingBar = (RatingBar)findViewById(R.id.customerservicerating);
		speedRatingBar = (RatingBar)findViewById(R.id.speedrating);
		
	    button.setOnClickListener(this);
	    
        priceRatingBar.setOnRatingBarChangeListener(this);
        qualityRatingBar.setOnRatingBarChangeListener(this);
        customerserviceRatingBar.setOnRatingBarChangeListener(this);
        speedRatingBar.setOnRatingBarChangeListener(this);
				       					
		SharedPreferences settings = getSharedPreferences(PREF, 0);
		if (settings.contains("accountName")) {
			String accountID = settings.getString("accountName", "Please Maintain phone Account");
			responseEntity.setAccountID(accountID);
		}
	}
	
	@Override
	public void onRatingChanged(RatingBar arg0, float rating, boolean arg2) {
	
		selectedContacts.get(contactCount).setPriceRating(priceRatingBar.getRating());	
		selectedContacts.get(contactCount).setQualityRating(qualityRatingBar.getRating());	
		selectedContacts.get(contactCount).setCustomerServiceRating(customerserviceRatingBar.getRating());	
		selectedContacts.get(contactCount).setEffeciencyRating(speedRatingBar.getRating());
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() 
	{
	  if (responseEntity != null) // Check that the object exists
	      return(responseEntity);
	  return super.onRetainNonConfigurationInstance();
	} 
	
	@Override
	public void onClick(View v) {
		responseEntity.setMessageText(mBodyText.getText().toString());
		responseEntity.setContactInfo(selectedContacts);
		contactCount++;			
		
		if (numberOfSelectedContacts==contactCount) {
			
			if (isConnected()) {
			
				try {
					String JacksonRepresentation = mapper.writeValueAsString(responseEntity);
					WebServiceAsyncTask webServiceTask = new WebServiceAsyncTask();
					webServiceTask.execute(WEB_SERVICE_POST_URL,JacksonRepresentation);
				} catch (JsonGenerationException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
					
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
				
				Intent i = new Intent(ResponseMessageActivity.this, HomeActivity.class);	     
				startActivity(i);
			}
			else {
				String error = "No network connection, message can not be sent";
    			
    			LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.toast_layout,
						(ViewGroup)findViewById(R.id.toast_layout));
				
				ImageView image = (ImageView) layout.findViewById(R.id.image);
				image.setImageResource(R.drawable.icon);
				TextView text = (TextView) layout.findViewById(R.id.text);
				text.setText(error);
				Toast toast = new Toast(getApplicationContext());
				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toast.setDuration(Toast.LENGTH_SHORT);
				toast.setView(layout);
				toast.show();	
			}
			}
		else {
			Intent i = new Intent(ResponseMessageActivity.this, ResponseMessageActivity.class);	     
			startActivity(i);
		}
	}
	
	public void goHome(View v) {
		Intent i = new Intent(ResponseMessageActivity.this, HomeActivity.class);	     
		startActivity(i);
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
}