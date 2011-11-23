package com.zeusriver.askmyfriends.android.provider;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeusriver.askmyfriends.android.activity.R;
import com.zeusriver.askmyfriends.android.entity.ContactEntity;
import com.zeusriver.askmyfriends.android.provider.CustomContactsAdapter.MainListHolder;
import com.zeusriver.askmyfriends.android.shared.BitmapManager;

public class CopyOfCustomContactsAdapter extends ArrayAdapter<ContactEntity> {
	private Context context;
	public ArrayList<ContactEntity> selectedContacts = new ArrayList<ContactEntity>();
	private ArrayList<ContactEntity> listOfContacts;
	private ArrayList<Boolean> itemChecked = new ArrayList<Boolean>();
    private final LayoutInflater inflator;
	private static final String TAG = "RiteReferral";
	
	public CopyOfCustomContactsAdapter(Context context, int textViewResourceId,
			ArrayList<ContactEntity> arrayList) {
		super(context, textViewResourceId, arrayList);
		this.context = context;
        this.listOfContacts = arrayList;
        inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        clearSelections();
        BitmapManager.INSTANCE.setPlaceholder(BitmapFactory.decodeResource(  
                context.getResources(), R.drawable.rricon48));   
	}
		
	@Override
	public View getView(final int pos, View v, ViewGroup parent) {
		
        MainListHolder mHolder;
        
		if (v == null) {
			v = inflator.inflate(R.layout.contact_row, null);
		
			mHolder = new MainListHolder();
			mHolder.bTitle = (TextView) v.findViewById(R.id.txtTitle);  
			mHolder.photo = (ImageView) v.findViewById(R.id.contactphoto); 
			v.setTag(mHolder);
		}
		else {
			mHolder = (MainListHolder)v.getTag();
		}
    	ContactEntity singleContact = listOfContacts.get(pos);
        Uri contactPhotoUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, singleContact.getContactIndex());
   
		mHolder.bTitle.setText(singleContact.getFullName().toString());
		mHolder.photo.setTag(contactPhotoUri);
        mHolder.bTitle.setTextSize(20);
        mHolder.bTitle.setTextColor(Color.DKGRAY);
        
  		BitmapManager.INSTANCE.loadBitmap(context, contactPhotoUri, mHolder.photo, 40, 40);  

		return v;
	}
	
	class MainListHolder 
	{
	    private TextView bTitle;
	    private ImageView photo;
	}
	
	public void clearSelections() {
		for (int i = 0; i < this.getCount(); i++) {
			itemChecked.add(i, false); // initializes all items value with false
			selectedContacts.clear();
	    }
	}
}