package com.zeusriver.askmyfriends.android.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MenuActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
}

    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.main_menu, menu);
    	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    // Handle item selection
    	switch (item.getItemId()) {
    	case R.id.close:
    		finish();	
    	return true;
    	case R.id.search:
    		search();
        return true;
    	default:
        return super.onOptionsItemSelected(item);
    }
}

    public void search() {

    	Context context = getApplicationContext();
    	CharSequence text = "Hello toast!";
    	int duration = Toast.LENGTH_SHORT;

    	Toast toast = Toast.makeText(context, text, duration);
    	toast.show();
    }
}