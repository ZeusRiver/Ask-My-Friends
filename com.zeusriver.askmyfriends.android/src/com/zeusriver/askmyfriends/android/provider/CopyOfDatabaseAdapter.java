package com.zeusriver.askmyfriends.android.provider;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CopyOfDatabaseAdapter {

    private static final int DATABASE_VERSION = 3;
    private static final String TAG = "SocialReferralDbAdapter";
	private static final String DATABASE_NAME = "ritereferral.db";
    private static final String MESSAGE_TABLE = "messages";
    private static final String CONTACT_TABLE = "contacts";
    private static final String LOCATION_TABLE = "locations";
    private static final String REGISTRATION_TABLE = "registration";
    private static final String RATING_TABLE = "ratings";
	public static final String TYPE = "type";
    public static final String ACCOUNTID = "accountid";
    public static final String DATE_CREATED = "date_created";
    public static final String KEY_ROWID = "_id";
    public static final String REQUEST_ID = "request_id";
    public static final String REGISTRATIONFLAG = "registrationflag";
    public static final String REQUEST_CATEGORY = "request_category";
    public static final String REQUEST_MESSAGE = "request_message";
    public static final String OVERALL_RATING = "overall_rating";
    public static final String PRICE_RATING = "price_rating";
    public static final String QUALITY_RATING = "quality_rating";
    public static final String SERVICE_RATING = "service_rating";
    public static final String SPEED_RATING = "speed_rating";
    public static final String MESSAGE_VIEWED = "message_viewed";
    public static final String KNOWLEDGE_RATING = "knowledge_rating";
    public static final String REFERRAL_FULLNAME = "referral_fullname";
    public static final String CONTACT_DATATYPE = "contact_datatype";
    public static final String CONTACT_TYPE= "contact_type";
    public static final String CONTACT_DATA= "contact_data";
    public static final String LOCATION_CITY = "location_city";   
    public static final String LOCATION_STATE = "location_state";   
    public static final String LOCATION_COUNTRY = "location_country"; 
    public static final String LOCATION_POSTALCODE = "location_postalcode";

    private final Context mCtx;
    
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Database creation sql statement
     */
    private static final String CREATE_MESSAGE_TABLE =
    	"CREATE TABLE "+ MESSAGE_TABLE +" (" 
    	+ "  "+ KEY_ROWID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
       // + "  "+ REQUEST_ID +" TEXT NOT NULL,"
    	+ "  "+ TYPE +" TEXT,"
        + "  "+ ACCOUNTID +" TEXT,"
        + "  "+ REQUEST_CATEGORY +" TEXT,"
        + "  "+ REQUEST_MESSAGE +" TEXT,"
        + "  "+ MESSAGE_VIEWED +" INTEGER,"
        + "  "+ DATE_CREATED +" DATE NOT NULL);";    
    private static final String CREATE_CONTACT_TABLE = 
        "CREATE TABLE "+ CONTACT_TABLE +" ("
        + "  "+ KEY_ROWID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
        + "  "+ REQUEST_ID +" INTEGER NOT NULL,"
        + "  "+ REFERRAL_FULLNAME +" TEXT,"
        + "  "+ CONTACT_DATATYPE +" TEXT,"
        + "  "+ CONTACT_TYPE +" INTEGER,"
        + "  "+ CONTACT_DATA +" TEXT);";  
    private static final String CREATE_LOCATION_TABLE = 
        "CREATE TABLE "+ LOCATION_TABLE +" ("
        + "  "+ KEY_ROWID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
        + "  "+ REQUEST_ID +" INTEGER NOT NULL,"
        + "  "+ LOCATION_CITY +" TEXT,"
        + "  "+ LOCATION_STATE +" TEXT,"
        + "  "+ LOCATION_COUNTRY +" TEXT,"
        + "  "+ LOCATION_POSTALCODE +" TEXT);";
    private static final String CREATE_RATING_TABLE = 
        "CREATE TABLE "+ RATING_TABLE +" ("
        + "  "+ KEY_ROWID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
        + "  "+ REQUEST_ID +" INTEGER NOT NULL,"
        + "  "+ REFERRAL_FULLNAME +" TEXT,"
        + "  "+ OVERALL_RATING +" FLOAT,"
        + "  "+ PRICE_RATING +" INTEGER,"
        + "  "+ QUALITY_RATING +" INTEGER,"
        + "  "+ SERVICE_RATING +" INTEGER,"
        + "  "+ SPEED_RATING +" INTEGER,"
        + "  "+ KNOWLEDGE_RATING +" INTEGER);";
    private static final String CREATE_REGISTRATION_TABLE = 
        "CREATE TABLE "+ REGISTRATION_TABLE +" ("
        + "  "+ KEY_ROWID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
        + "  "+ REGISTRATIONFLAG +" INTEGER NOT NULL);";  
    
    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_MESSAGE_TABLE);     
            db.execSQL(CREATE_CONTACT_TABLE);         		
            db.execSQL(CREATE_LOCATION_TABLE);
            db.execSQL(CREATE_RATING_TABLE);
            db.execSQL(CREATE_REGISTRATION_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
    }

    public CopyOfDatabaseAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public CopyOfDatabaseAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }
    
    public long saveRegistration(int registrationFlag) {
   	    ContentValues initialValues = new ContentValues();
   	    initialValues.put(REGISTRATIONFLAG, registrationFlag);
        return mDb.insert(REGISTRATION_TABLE, null, initialValues);
    }
    
    public Cursor checkRegistration() {
    	return mDb.query(REGISTRATION_TABLE, 
            new String[] {KEY_ROWID, REGISTRATIONFLAG}, null, null, null, null, null);
    }
 
    public long createMessage(String type, String accountid, 
    		String request_category, String request_message, int message_viewed) {
    	
    	Date date = new Date();
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	    	
   	    ContentValues initialValues = new ContentValues();
          	    
        initialValues.put(TYPE, type);
        initialValues.put(ACCOUNTID, accountid);
        initialValues.put(REQUEST_CATEGORY, request_category);
        initialValues.put(REQUEST_MESSAGE, request_message);
        initialValues.put(MESSAGE_VIEWED, message_viewed);
        initialValues.put(DATE_CREATED, dateFormat.format(date));

        return mDb.insert(MESSAGE_TABLE, null, initialValues);      
    }

    public long createContact(Long requestID, String contact_fullname, 
    		String contact_datatype, String contact_data, Integer contact_type) {
    	         
   	    ContentValues initialValues = new ContentValues();

    	initialValues.put(REQUEST_ID, requestID);
    	initialValues.put(REFERRAL_FULLNAME, contact_fullname);
    	initialValues.put(CONTACT_DATATYPE, contact_datatype);
    	initialValues.put(CONTACT_TYPE, contact_type);    	
    	initialValues.put(CONTACT_DATA, contact_data);

    	return mDb.insert(CONTACT_TABLE, null, initialValues);      
    }
    
    public long enterRating(Long requestID, String contact_fullname, float overall_rating,
    		float price_rating, float quality_rating, float service_rating, float speed_rating,
    		float knowledge_rating) {
    	         
   	    ContentValues initialValues = new ContentValues();
   	    
   	    initialValues.put(REQUEST_ID, requestID);
   	    initialValues.put(REFERRAL_FULLNAME, contact_fullname);
    	initialValues.put(OVERALL_RATING, overall_rating);
    	initialValues.put(PRICE_RATING, price_rating);
    	initialValues.put(QUALITY_RATING, quality_rating);
    	initialValues.put(SERVICE_RATING, service_rating);
    	initialValues.put(SPEED_RATING, speed_rating);
    	initialValues.put(KNOWLEDGE_RATING, knowledge_rating);
   	    
        return mDb.insert(RATING_TABLE, null, initialValues);
    }
    
    public long createLocation(Long requestID, String location_city,
    		String location_state, String location_postalcode, String location_country) {
    	         
   	    ContentValues initialValues = new ContentValues();

    	initialValues.put(REQUEST_ID, requestID);
    	initialValues.put(LOCATION_CITY, location_city);
    	initialValues.put(LOCATION_STATE, location_state);
    	initialValues.put(LOCATION_POSTALCODE, location_postalcode);
    	initialValues.put(LOCATION_COUNTRY, location_country);

    	return mDb.insert(LOCATION_TABLE, LOCATION_CITY, initialValues);
    }
    
    public boolean deleteMessage(Long requestID) {

        mDb.delete(MESSAGE_TABLE, KEY_ROWID + "=" + requestID, null);
        mDb.delete(CONTACT_TABLE, REQUEST_ID + "=" + requestID, null);
        mDb.delete(RATING_TABLE, REQUEST_ID + "=" + requestID, null);
        return true;
    }

    /* Need to come back and redefine which columns to return in SELECT statement */
	/* Right now, returning all columns, should only return columns that are to be displayed */
    public Cursor fetchAllMessages(String type) {
    	Cursor mCursor;
    	
    	final String MY_QUERY = 
    		
    		"SELECT * FROM messages " +
    		"WHERE TYPE = ?" +
    		"ORDER BY _id DESC";
    	
    	mCursor = mDb.rawQuery(MY_QUERY, new String[]{String.valueOf(type)});
    	
    	if (mCursor != null) {
   			mCursor.moveToFirst();
   		}
		return mCursor;
    }
    
    public Cursor getPendingMessages(String type) {
    	Cursor mCursor;
    	
    	final String MY_QUERY = "SELECT * FROM messages WHERE message_viewed != 1 AND TYPE = ?";
   		mCursor = mDb.rawQuery(MY_QUERY, new String[]{String.valueOf(type)});
   	
   		if (mCursor != null) {
   			mCursor.moveToFirst();
   		}
		return mCursor;
    }
    
    public Cursor fetchAllNotes() {

        return mDb.query(MESSAGE_TABLE, 
        	new String[] {KEY_ROWID, TYPE, REQUEST_ID, REQUEST_CATEGORY}, null, null, null, null, null);
    }
       
    public Cursor fetchAllResponse(String type) throws SQLException {
	    	
    	final String MY_QUERY =
    		    		
    		"SELECT * FROM messages AS M INNER JOIN ratings AS C " +
    		"ON M._id=C.request_id " +
    		"WHERE M.type = ?" +
			"ORDER BY C.request_id DESC";

    		Cursor mCursor = mDb.rawQuery(MY_QUERY, new String[]{String.valueOf(type)});
    	
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public Cursor fetchContact(Long requestID) throws SQLException {
    	
    	final String MY_QUERY = "SELECT * FROM contacts WHERE request_id = ?";

    		Cursor mCursor = mDb.rawQuery(MY_QUERY, new String[]{String.valueOf(requestID)});
    	
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public Cursor fetchResponse(Long requestID) throws SQLException {
	    	
    	final String MY_QUERY =
    		    		
    		"SELECT * FROM ratings AS R INNER JOIN contacts AS C " +
    		"ON R.request_id=C.request_id " +
    		"WHERE C.request_id = ?";

    		Cursor mCursor = mDb.rawQuery(MY_QUERY, new String[]{String.valueOf(requestID)});
    	
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
        
    public Cursor fetchRequest(Long requestID) throws SQLException {
  	     	    	
    	final String MY_QUERY =
    		
    		"SELECT * FROM messages AS M INNER JOIN contacts AS C " +
    		"ON M._id=C.request_id " +
    		"WHERE C.request_id = ?";

    		Cursor mCursor = mDb.rawQuery(MY_QUERY, new String[]{String.valueOf(requestID)});
    	
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean setMessageViewed(Long requestID) {
        ContentValues args = new ContentValues();
        args.put(MESSAGE_VIEWED, 1);

        return mDb.update(MESSAGE_TABLE, args, KEY_ROWID + "=" + requestID, null) > 0;
    }
    
    public boolean getMessageViewed(Long requestID) {
    	
    	final String MY_QUERY =

    	"SELECT * FROM messages AS M INNER JOIN contacts AS C " +
		"ON M._id=C.request_id " +
		"WHERE C.request_id = ?";
		
    	return mDb.rawQuery(MY_QUERY, new String[]{String.valueOf(requestID)}) != null;
    }
}