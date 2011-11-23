package com.zeusriver.askmyfriends.android.shared;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class BasicWebService {

	DefaultHttpClient httpClient;
	String returnedValue=null;
	String webServiceUrl;
	HttpResponse response=null;
	HttpGet httpGet=null;
	HttpPost httpPost;

	public BasicWebService(String serviceName){
		
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpConnectionParams.setConnectionTimeout(params, 10000);
		HttpProtocolParams.setContentCharset(params, "UTF-8");
		params.setBooleanParameter("http.protocol.expect-continue", false);
		
		httpClient = new DefaultHttpClient(params);
		webServiceUrl = serviceName;
	}

	public String webGet() {
			
		httpGet = new HttpGet(webServiceUrl);

		try {
			response = httpClient.execute(httpGet);
			
			if(response.getStatusLine().getStatusCode()==200) {			
				returnedValue = EntityUtils.toString(response.getEntity(),HTTP.UTF_8);
			} else {
				returnedValue = null;
			}
		} catch (IOException e) {
			Log.e("WebService:", e.getMessage());
		} catch (Exception e) {
			Log.e("WebService:", e.getMessage());
		} 
	return returnedValue;
	}
	
	public String webPost(String JacksonRepresentation) throws UnsupportedEncodingException {
		
		String jacksonString = JacksonRepresentation;

		StringEntity se = new StringEntity(jacksonString);  
		se.setContentType("application/json");
	        		
		httpPost = new HttpPost(webServiceUrl);	  
		httpPost.setEntity(se);
				
		try {
			response = httpClient.execute(httpPost);
			
			if(response.getStatusLine().getStatusCode()==200) {
				returnedValue = EntityUtils.toString(response.getEntity(),HTTP.UTF_8);
			} else {
				returnedValue = null;
			}
			
		} catch (ClientProtocolException e) {
			Log.e("WebService:", e.getMessage());
		} catch (IOException e) {
			Log.e("WebService:", e.getMessage());
		}		
	return returnedValue;
	}
}