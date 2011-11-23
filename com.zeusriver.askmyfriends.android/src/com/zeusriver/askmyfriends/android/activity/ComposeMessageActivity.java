package com.zeusriver.askmyfriends.android.activity;

import java.io.Serializable;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.app.ProgressDialog;

import com.zeusriver.askmyfriends.android.provider.DatabaseAdapter;
import com.zeusriver.askmyfriends.android.entity.MessageEntity;

public class ComposeMessageActivity extends MenuActivity implements Serializable, OnClickListener {
	
	private static final long serialVersionUID = 1L;
	public String categoryText;
	public String selectedCategory;
	public DatabaseAdapter mDbHelper;
    public EditText mBodyText;
    public TextView tvMobileNumber;
    public EditText etCity;
    //public EditText etPostalCode;
	private static ProgressDialog dialog;
    
	MessageEntity requestEntity = new MessageEntity();
	
	private static final String PREF = "RiteReferralPrefences";
	int Length;
	private TextView mTextView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.compose);
		mBodyText = (EditText)findViewById(R.id.message_entrytxtbox);
		mTextView = (TextView)findViewById(R.id.messagecount);
	
	/*	final Drawable x = getResources().getDrawable(R.drawable.ic_tab_grey);
		
		
		x.setBounds(0, 0, x.getIntrinsicWidth(), x.getIntrinsicHeight());
		
		//mBodyText.setCompoundDrawables(null, null, value.equals("") ? null : x, null);
		mBodyText.setOnTouchListener(new OnTouchListener() {
		    
			@Override
		    public boolean onTouch(View v, MotionEvent event) {
		        if (mBodyText.getCompoundDrawables()[2] == null) {
		            return false;
		        }
		        if (event.getAction() != MotionEvent.ACTION_UP) {
		            return false;
		        }
		        if (event.getX() > mBodyText.getWidth() - mBodyText.getPaddingRight() - x.getIntrinsicWidth()) {
		        	mBodyText.setText("");
		        	mBodyText.setCompoundDrawables(null, null, null, null);
		        }
		        return false;
		    }	
		});*/
	
		InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		// only will trigger it if no physical keyboard is open
		mgr.showSoftInput(mBodyText, InputMethodManager.SHOW_FORCED);
    	mTextView.setText(String.valueOf(250));
    	
		final TextWatcher mTextEditorWatcher = new TextWatcher() {
		        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		        }

		        public void onTextChanged(CharSequence s, int start, int before, int count) {
		        	mTextView.setText(String.valueOf(250-s.length()));
		        	//mBodyText.setCompoundDrawables(null, null, mBodyText.getText().toString().equals("") ? null : x, null);
	            	mTextView.setTextColor(Color.DKGRAY);
		        }

		        public void afterTextChanged(Editable s) {
		        	Length = s.length();	      		        	
		        }
		};				
	/*	LocationManager locManager = (LocationManager) getSystemService(LOCATION_SERVICE); 
		if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			this.findLocation();
		}*/
		mBodyText.addTextChangedListener(mTextEditorWatcher);
		Bundle b = getIntent().getExtras();
		selectedCategory= b.getString(categoryText);
		
		//EditText tvAccountID = (EditText) findViewById(R.id.field_accountID);
		//EditText headerText = (EditText)findViewById(R.id.message_headertxt);
		//tvMobileNumber = (TextView) findViewById(R.id.field_ha_line1);
		//etCity = (EditText) findViewById(R.id.city_entrytxtbox);
		//etPostalCode = (EditText) findViewById(R.id.postalcode_entrytxtbox);

		Button button = (Button)findViewById(R.id.btnconfirm);
        button.setOnClickListener(this);
       /* if (Length < 0) {    
			button.setClickable(false);
			button.setFocusable(false);
        }*/
		       	
		//headerText.setText(selectedCategory);
		
		//etCity.setText(requestEntity.getCity());
        //etPostalCode.setText(messageEntity.getPostalCode());
				
		SharedPreferences settings = getSharedPreferences(PREF, 0);
		if (settings.contains("accountName")){
			String accountID = settings.getString("accountName", "Please Maintain phone Account");
			//tvAccountID.setText(accountID);
			requestEntity.setAccountID(accountID);
		}
	}
	
	@Override
	public void onClick(View v) {
		
		dialog = ProgressDialog.show(ComposeMessageActivity.this,
                null, "Loading. Please wait...", true);
		
		dialog.setOwnerActivity(ComposeMessageActivity.this);
		
		new Thread() {
	           public void run() {
	                   try{
	                           // Do some Fake-Work
	                           sleep(5000);
	                   } catch (Exception e) { }
	                   // Dismiss the Dialog
	                   dialog.dismiss();
	           }
			}.start();
											
		requestEntity.setCategoryID(selectedCategory);
		requestEntity.setMessageType("Request");
		requestEntity.setMessageText(mBodyText.getText().toString());	
		//requestEntity.setCity(etCity.getText().toString());
		//messageEntity.setPostalCode(etPostalCode.getText().toString());
	
		Intent i = new Intent(ComposeMessageActivity.this, ContactsActivity.class);	     
		Bundle b = new Bundle();  
		b.putSerializable("message",requestEntity);
		i.putExtras(b);
		startActivity(i);
	}
	
	public void goHome(View v) {
		Intent i = new Intent(ComposeMessageActivity.this, HomeActivity.class);	     
		startActivity(i);
	}
}