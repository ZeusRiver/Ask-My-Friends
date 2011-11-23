package com.zeusriver.askmyfriends.android.provider;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import com.zeusriver.askmyfriends.android.activity.R;
import com.zeusriver.askmyfriends.android.entity.ContactEntity;
import com.zeusriver.askmyfriends.android.shared.BitmapManager;

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

public class CustomContactsAdapter extends ArrayAdapter<ContactEntity> {
	private Context context;
	public ArrayList<ContactEntity> selectedContacts = new ArrayList<ContactEntity>();
	private ArrayList<ContactEntity> listOfContacts;
	private ArrayList<Boolean> itemChecked = new ArrayList<Boolean>();
    private final LayoutInflater inflator;
	private static final String TAG = "RiteReferral";
	
	public CustomContactsAdapter(Context context, int textViewResourceId,
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
			v = inflator.inflate(R.layout.single_contact, null);
		
			mHolder = new MainListHolder();
			mHolder.bTitle = (TextView) v.findViewById(R.id.txtTitle);  
			mHolder.cBox = (CheckBox) v.findViewById(R.id.bcheck);
			mHolder.photo = (ImageView) v.findViewById(R.id.contactphoto); 
			v.setTag(mHolder);
		}
		else {
			mHolder = (MainListHolder)v.getTag();
		}
    	ContactEntity singleContact = listOfContacts.get(pos);
        Uri contactPhotoUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, singleContact.getContactIndex());
   
		mHolder.bTitle.setText(singleContact.getFullName().toString());
		mHolder.cBox.setTag(singleContact);
		mHolder.photo.setTag(contactPhotoUri);
        mHolder.bTitle.setTextSize(20);
        mHolder.bTitle.setTextColor(Color.DKGRAY);
        
  		BitmapManager.INSTANCE.loadBitmap(context, contactPhotoUri, mHolder.photo, 40, 40);  

  		v.setOnClickListener (new OnClickListener() {

			@Override
			public void onClick(View v) {
				CheckBox cb = (CheckBox) v.findViewById(R.id.bcheck);
				Log.i("TAG", "Contact Was Clicked");
				
				if (cb.isChecked()) {
					cb.setChecked(false);
		        	itemChecked.set(pos, false);
		        	selectedContacts.remove((ContactEntity) cb.getTag());
					Log.i("TAG", "Checkbox unchecked");

				} else if (!cb.isChecked()) {
					cb.setChecked(true);
		        	itemChecked.set(pos, true);
		        	selectedContacts.add((ContactEntity) cb.getTag());
					Log.i("TAG", "Checkbox checked");
				}
			}		
        });
  		mHolder.cBox.setChecked(itemChecked.get(pos));
		return v;
	}
	
	class MainListHolder 
	{
	    private TextView bTitle;
	    private CheckBox cBox;
	    private ImageView photo;
	}
				
	public void clearSelections() {
				
		for (int i = 0; i < this.getCount(); i++) {
			itemChecked.add(i, false); // initializes all items value with false
			selectedContacts.clear();
	    }
	}
}