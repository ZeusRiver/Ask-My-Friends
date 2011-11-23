package com.zeusriver.askmyfriends.android.activity;

import java.io.IOException;
import java.util.ArrayList;

import org.codehaus.jackson.map.ObjectMapper;
import org.xmlpull.v1.XmlPullParserException;

import com.zeusriver.askmyfriends.android.activity.CategoryActivity;
import com.zeusriver.askmyfriends.android.activity.MenuActivity;
import com.zeusriver.askmyfriends.android.provider.CopyOfDatabaseAdapter;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;

public class CategoryActivity extends ExpandableListActivity {
	  
    public static final int menu1 = 1;
	public CopyOfDatabaseAdapter mDbHelper;
    ObjectMapper mapper = new ObjectMapper();  
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category);
		
		ExpandableListAdapter mAdapter = new MyExpandableListAdapter();
		((MyExpandableListAdapter) mAdapter).readCategoriesFromXml();
		setListAdapter(mAdapter);
		registerForContextMenu(getExpandableListView());
		getExpandableListView().setOnChildClickListener(this);	
	}
	
	public void goHome(View v) {
		Intent i = new Intent(this, HomeActivity.class);
		startActivity(i);
	}
	
/*	@Override 
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
	}*/
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
    	switch (item.getItemId()) {
    		case R.id.close:
    			finish();	
    			return true;
    		case R.id.search:
    			MenuActivity ma = new MenuActivity();
    			ma.search();
    			return true;
    		default:
    			return super.onOptionsItemSelected(item);
    	}
	}
	
/*	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("Category Options");
		menu.add(0, menu1, 0, "Request Referral");
		menu.add(0, 0, 0, "View Existing Referrals");
		menu.add(0, 0, 0, "Assign a Contact");
		menu.add(0, 0, 0, "Refer a Contact");
	}*/	
	
	@Override
	public boolean onChildClick (ExpandableListView parent, 
		View v, int groupPosition, int childPosition, long id) {
								
		ExpandableListAdapter mAdapter = parent.getExpandableListAdapter();
		String selectedCategory = (String) mAdapter.getChild(groupPosition, childPosition);	
		
		Intent i = new Intent(CategoryActivity.this, ComposeMessageActivity.class);
	    Bundle b = new Bundle();
			b.putString(null,selectedCategory);
			i.putExtras(b);
		startActivity(i);
	return true;
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) item.getMenuInfo();

		String title = ((TextView) info.targetView).getText().toString();

		int type = ExpandableListView.getPackedPositionType(info.packedPosition);
		if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) { 
			switch (item.getItemId()) {
				case menu1:
			    	Intent i = new Intent(CategoryActivity.this, ComposeMessageActivity.class);	     
			    	Bundle b = new Bundle();  
			    		b.putString(null,title);
			    		i.putExtras(b);
			    		startActivity(i); 
				    return true;				    
			    case R.id.search:
			        return true;
			    default:
			        return super.onOptionsItemSelected(item);		
			}
		}
		else if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {	
			
			int groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition); 
			Toast.makeText(this, title + ": Group " + groupPos + " clicked", Toast.LENGTH_SHORT).show();
		
		return true;
		}
    return false;
	}
	
public class MyExpandableListAdapter extends BaseExpandableListAdapter {

	ArrayList<String> groups;
	ArrayList<ArrayList<String>> children;

    Context mContext;

	public MyExpandableListAdapter(Context context) {
        mContext = context;
    }
	
	public MyExpandableListAdapter() {
    }
	
    private void readCategoriesFromXml() {    	   	
    	
    	int eventType = -1;
		int i = -1; //Keep track of main categories row position
		int j = 0;  //keep track of sub categories column position
			
		ArrayList<String> mainCategory = new ArrayList<String>();
		ArrayList<ArrayList<String>> subCategory = new ArrayList<ArrayList<String>>();
		XmlResourceParser categoryXml = getResources().getXml(R.xml.categories);
		
		while (eventType != XmlResourceParser.END_DOCUMENT) {
			if (eventType == XmlResourceParser.START_TAG) {	
				String strNode = categoryXml.getName();					
				if (strNode.equals("category")) {
					mainCategory.add(categoryXml.getAttributeValue(null, "maincategory").toUpperCase());
					i++;
				}
				if (strNode.equals("sub")) {	
					subCategory.add(new ArrayList<String>());
					subCategory.get(i).add(categoryXml.getAttributeValue(null, "subcategory").toUpperCase());
					j++;
				}		
		    }	
			try {
				eventType = categoryXml.next();	
		    } catch (XmlPullParserException e) {
		    	e.printStackTrace();
			  } catch (IOException e) {
				e.printStackTrace();
			  }
		}		
		groups = mainCategory;
		children = subCategory;
	}
		
    public Object getChild(int groupPosition, int childPosition) {
    	return children.get(groupPosition).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
    	return childPosition;
    }

    public int getChildrenCount(int groupPosition) {
    	return children.get(groupPosition).size();
    }

    public TextView getGenericView() {
        // Layout parameters for the ExpandableListView
    	AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 75);
      	
    	TextView textView = new TextView(CategoryActivity.this);
        textView.setLayoutParams(lp);
        
        // Center the text vertically
        textView.setGravity(Gravity.CENTER_VERTICAL| Gravity.LEFT);
        
        // Set the text starting position
        textView.setPadding(57, 0, 0, 0);
        textView.setTextSize(20);
        textView.setTextColor(Color.DKGRAY);
    return textView;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
           View convertView, ViewGroup parent) {
    	TextView textView = getGenericView();
        textView.setText(getChild(groupPosition, childPosition).toString());
                        
    return textView;
    }

    public Object getGroup(int groupPosition) {
    	return groups.get(groupPosition);
    }

    public int getGroupCount() {
    	return groups.size();
    }

    public long getGroupId(int groupPosition) {
    	return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, 
    	   ViewGroup parent) {
        TextView textView = getGenericView();
        textView.setText(getGroup(groupPosition).toString());
    return textView;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
    	return true;
    }

    public boolean hasStableIds() {
    	return true;
    }
}
}