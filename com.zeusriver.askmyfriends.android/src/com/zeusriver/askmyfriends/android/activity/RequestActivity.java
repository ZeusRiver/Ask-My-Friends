package com.zeusriver.askmyfriends.android.activity;

import java.util.HashMap;
import java.util.Map;

import com.zeusriver.askmyfriends.android.entity.ContactEntity;
import com.zeusriver.askmyfriends.android.entity.MessageEntity;
import com.zeusriver.askmyfriends.android.provider.CopyOfDatabaseAdapter;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class RequestActivity extends ListActivity {
  
	//public DatabaseAdapter mDbHelper;
	public CopyOfDatabaseAdapter mDbHelper;

    public static final int menu1 = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.request);
		mDbHelper = new CopyOfDatabaseAdapter(this);
        mDbHelper.open();
        fetchData();
        getListView();
	}
	
	private void fetchData() {
		Cursor c3 = mDbHelper.fetchAllMessages("Response");
        startManagingCursor(c3);
        //String date = c.getString(c.getColumnIndexOrThrow("date_created"));
        
        String[] from = new String[] {
        		CopyOfDatabaseAdapter.ACCOUNTID,
        		CopyOfDatabaseAdapter.DATE_CREATED,
        		CopyOfDatabaseAdapter.REQUEST_CATEGORY,
        		CopyOfDatabaseAdapter.REQUEST_MESSAGE,
        		};
        int[] to = new int[] { R.id.text1, R.id.text2, R.id.text3, R.id.text4};
        
        SimpleCursorAdapter notes = new SimpleCursorAdapter(this, R.layout.referral_row, c3, from, to);
        setListAdapter(notes);
        //c3.close();
	}
	
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("Request Options");
		menu.add(0, menu1, 0, "Delete Request");
		menu.add(0, 0, 0, "Resend Request");
	}
	
	public void onListItemClick(ListView parent, View view, int position, long id) {
	    	
	    Intent i = new Intent(this, ResponseDetailsActivity.class);								
		Cursor c = mDbHelper.fetchResponse(id);
		mDbHelper.setMessageViewed(id);
		startManagingCursor(c);
		
		MessageEntity receivedResposne = new MessageEntity();
		ContactEntity referedContact = new ContactEntity();
		
		String fullName = c.getString(
				c.getColumnIndexOrThrow(CopyOfDatabaseAdapter.REFERRAL_FULLNAME));
		
		Float priceRating = c.getFloat(
				c.getColumnIndexOrThrow(CopyOfDatabaseAdapter.PRICE_RATING));
		
		Float serviceRating = c.getFloat(
				c.getColumnIndexOrThrow(CopyOfDatabaseAdapter.SERVICE_RATING));
		
		Float speedRating = c.getFloat(
				c.getColumnIndexOrThrow(CopyOfDatabaseAdapter.SPEED_RATING));
		
		Float qualityRating = c.getFloat(
				c.getColumnIndexOrThrow(CopyOfDatabaseAdapter.QUALITY_RATING));
		
		i.putExtra(CopyOfDatabaseAdapter.REFERRAL_FULLNAME, c.getString(
				c.getColumnIndexOrThrow(CopyOfDatabaseAdapter.REFERRAL_FULLNAME)));
		i.putExtra(CopyOfDatabaseAdapter.PRICE_RATING, c.getFloat(
				c.getColumnIndexOrThrow(CopyOfDatabaseAdapter.PRICE_RATING)));
		i.putExtra(CopyOfDatabaseAdapter.SERVICE_RATING, c.getFloat(
				c.getColumnIndexOrThrow(CopyOfDatabaseAdapter.SERVICE_RATING)));	
		i.putExtra(CopyOfDatabaseAdapter.SPEED_RATING, c.getFloat(
				c.getColumnIndexOrThrow(CopyOfDatabaseAdapter.SPEED_RATING)));
		i.putExtra(CopyOfDatabaseAdapter.QUALITY_RATING, c.getFloat(
				c.getColumnIndexOrThrow(CopyOfDatabaseAdapter.QUALITY_RATING)));
				
		Cursor c2 = mDbHelper.fetchContact(id);
		startManagingCursor(c2);

		String datatype = c2.getString(c2.getColumnIndexOrThrow(CopyOfDatabaseAdapter.CONTACT_DATATYPE));
    	Map<String,Integer> phoneMapping = new HashMap<String,Integer>();
    	Map<String,Integer> emailMapping = new HashMap<String,Integer>();
    		
    	int row = c2.getCount();
    	    	
    	while(!c2.isLast()) {
    		    		
			if (datatype.equalsIgnoreCase("EMAIL")) {
				String data = c2.getString(c2.getColumnIndexOrThrow(CopyOfDatabaseAdapter.CONTACT_DATA));
				int type = c2.getInt(c2.getColumnIndexOrThrow(CopyOfDatabaseAdapter.CONTACT_TYPE));		
				phoneMapping.put(data,type);			
				referedContact.setPhoneType(phoneMapping);
			}
			
			else if (datatype.equalsIgnoreCase("PHONE")) {
				String data = c2.getString(c2.getColumnIndexOrThrow(CopyOfDatabaseAdapter.CONTACT_DATA));
				int type = c2.getInt(c2.getColumnIndexOrThrow(CopyOfDatabaseAdapter.CONTACT_TYPE));		
				emailMapping.put(data,type);			
				referedContact.setPhoneType(emailMapping);
			}
			c2.moveToNext();
    	}
			
		//c.close();
		
		Cursor c1 = mDbHelper.fetchRequest(id);
		startManagingCursor(c1);

		String messageText = c1.getString(
				c1.getColumnIndexOrThrow(CopyOfDatabaseAdapter.REQUEST_MESSAGE));
		
		String categoryID = c1.getString(
				c1.getColumnIndexOrThrow(CopyOfDatabaseAdapter.REQUEST_CATEGORY));
		
		i.putExtra(CopyOfDatabaseAdapter.REQUEST_MESSAGE, c1.getString(
				c1.getColumnIndexOrThrow(CopyOfDatabaseAdapter.REQUEST_MESSAGE)));
		i.putExtra(CopyOfDatabaseAdapter.REQUEST_CATEGORY, c1.getString(
				c1.getColumnIndexOrThrow(CopyOfDatabaseAdapter.REQUEST_CATEGORY)));
		
		//c1.close();
		
		referedContact.setFullName(fullName);
		referedContact.setPriceRating(priceRating);
		referedContact.setQualityRating(qualityRating);
		referedContact.setCustomerServiceRating(serviceRating);
		referedContact.setEffeciencyRating(speedRating);
		
		//receivedResposne.setCategoryID(categoryID);
		//receivedResposne.setMessageText(messageText);
		
		Bundle b = new Bundle();  
		b.putSerializable("referedContact",referedContact);
		i.putExtras(b);
		
		startActivity(i);
	}
		
/*	public boolean onContextItemSelected(MenuItem item) {
	    switch(item.getItemId()) {
	    	case menu1:
	    		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	            mDbHelper.deleteNote(info.id);
	            fillData();
	            return true;
	        }
	        return super.onContextItemSelected(item);
	}*/
	  
	  public void goHome(View v) {
		Intent i = new Intent(RequestActivity.this, HomeActivity.class);	     
		startActivity(i);
	  }
}