package com.zeusriver.askmyfriends.android.shared;

import java.io.UnsupportedEncodingException;

import org.codehaus.jackson.map.ObjectMapper;

import android.os.AsyncTask;

public class WebServiceAsyncTask extends AsyncTask<Object, Boolean, String> {

    ObjectMapper mapper = new ObjectMapper();  
	
	@Override
	protected String doInBackground(Object... params) {
		String serviceUrl = (String) params[0];
		String jacksonString = (String)params[1];

		BasicWebService webService = new BasicWebService(serviceUrl);
				
		try {
			return webService.webPost(jacksonString);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	return jacksonString;
	}

	@Override
	protected void onPostExecute(String response) {
	}
	
	@Override
	protected void onPreExecute() {
	}
}