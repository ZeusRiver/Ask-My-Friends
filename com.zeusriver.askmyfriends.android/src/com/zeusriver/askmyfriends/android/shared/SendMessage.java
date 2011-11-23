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
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.zeusriver.askmyfriends.android.entity.MessageEntity;

import android.util.Log;

public class SendMessage {
	
	MessageEntity messageEntity;
	String url;
    ObjectMapper mapper = new ObjectMapper();  
	
	public SendMessage(MessageEntity messsage, String url) throws JsonGenerationException, JsonMappingException, IOException {
		
		this.writeToDb();
		this.sendMessageToServer();
		this.displaySuccessMessage();
	}

	public boolean writeToDb() {
		return true;	
	}
	
	public boolean sendMessageToServer() throws JsonGenerationException, JsonMappingException, IOException {
		
		String JacksonRepresentation = mapper.writeValueAsString(messageEntity); 
		WebServiceAsyncTask webServiceTask = new WebServiceAsyncTask();
		webServiceTask.execute(url,JacksonRepresentation,this);
		
		return true;	
	}
	
	public void displaySuccessMessage() {
		
	}
}