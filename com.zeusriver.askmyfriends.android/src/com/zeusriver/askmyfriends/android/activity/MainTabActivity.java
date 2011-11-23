package com.zeusriver.askmyfriends.android.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class MainTabActivity extends TabActivity {
    /** Called when the activity is first created. */
	
	int tab;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
		Bundle b = getIntent().getExtras();

		if (b!=null){
			tab = b.getInt("TAB", 0);		
		}
		else
			tab = 0;
		
        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Reusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, CategoryActivity.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("categories").setIndicator("Categories",
                          res.getDrawable(R.drawable.ic_tab_category))
                      .setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, ReferralActivity.class);
        spec = tabHost.newTabSpec("referrals").setIndicator("Requests",
                          res.getDrawable(R.drawable.ic_tab_referrals))
                      .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, RequestActivity.class);
        spec = tabHost.newTabSpec("requests").setIndicator("Responses",
                          res.getDrawable(R.drawable.ic_tab_requests))
                      .setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(tab);
    }
}  