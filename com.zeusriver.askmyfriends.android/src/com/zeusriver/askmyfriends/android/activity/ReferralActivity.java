package com.zeusriver.askmyfriends.android.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
import org.codehaus.jackson.map.ObjectMapper;

import com.zeusriver.askmyfriends.android.entity.ContactEntity;
import com.zeusriver.askmyfriends.android.entity.MessageEntity;
import com.zeusriver.askmyfriends.android.provider.CopyOfDatabaseAdapter;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
 
public class ReferralActivity extends ListActivity {
 
	ObjectMapper mapper = new ObjectMapper();
	List<ContactEntity> contactEntity = null;
	List<MessageEntity> referralEntity;
	List<String> mobileNumber;
	List<String> emailAddress;
	private static final String TAG = "RiteReferral";
	
	public CopyOfDatabaseAdapter mDbHelper;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.referral);
        mDbHelper = new CopyOfDatabaseAdapter(this);
        mDbHelper.open();
        fetchData();
        getListView();
    }
	 	
	private void fetchData() {
		Cursor c = mDbHelper.fetchAllMessages("Request");
        startManagingCursor(c);
        //String date = c.getString(c.getColumnIndexOrThrow("date_created"));
    	 
       // String emailAddress = c.getString(c.getColumnIndexOrThrow("accountid"));

        String[] from = new String[] {
        		CopyOfDatabaseAdapter.ACCOUNTID,
        		CopyOfDatabaseAdapter.DATE_CREATED,
        		CopyOfDatabaseAdapter.REQUEST_CATEGORY,
        		CopyOfDatabaseAdapter.REQUEST_MESSAGE,
        		};
        int[] to = new int[] { R.id.text1, R.id.text2, R.id.text3, R.id.text4};
        
        SimpleCursorAdapter notes = new SimpleCursorAdapter(this, R.layout.referral_row, c, from, to);
        setListAdapter(notes);    
	}
	
    public void onListItemClick(ListView parent, View view, int position, long id) {
    	
    	Intent i = new Intent(this, RequestDetailsActivity.class);
		
		Cursor c1 = mDbHelper.fetchRequest(id);
		mDbHelper.setMessageViewed(id);
		startManagingCursor(c1);
				
		i.putExtra(CopyOfDatabaseAdapter.ACCOUNTID, c1.getString(
		        c1.getColumnIndexOrThrow(CopyOfDatabaseAdapter.ACCOUNTID)));
		i.putExtra(CopyOfDatabaseAdapter.REQUEST_CATEGORY, c1.getString(
		        c1.getColumnIndexOrThrow(CopyOfDatabaseAdapter.REQUEST_CATEGORY)));
		i.putExtra(CopyOfDatabaseAdapter.REQUEST_MESSAGE, c1.getString(
		        c1.getColumnIndexOrThrow(CopyOfDatabaseAdapter.REQUEST_MESSAGE)));
		
		startActivity(i);    	
    }
    
    public void goHome(View v) {
		Intent i = new Intent(ReferralActivity.this, HomeActivity.class);	     
		startActivity(i);
	}
    
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mDbHelper.close();
		Log.i(TAG,"On Destroy");
	}
}