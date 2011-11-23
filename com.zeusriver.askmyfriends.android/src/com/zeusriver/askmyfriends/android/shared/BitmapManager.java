package com.zeusriver.askmyfriends.android.shared;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ImageView;

public enum BitmapManager {  
    INSTANCE;  
	
    private static final String TAG = "RiteReferral";

    private final Map<Uri, SoftReference<Bitmap>> cache;  
    private final ExecutorService pool;  
	private Context ctx;
    private Map<ImageView, Uri> imageViews = Collections  
            .synchronizedMap(new WeakHashMap<ImageView, Uri>());  
    private Bitmap placeholder;  
  
    BitmapManager() {  
        cache = new HashMap<Uri, SoftReference<Bitmap>>();  
        pool = Executors.newFixedThreadPool(5); 
    }  
  
    public void setPlaceholder(Bitmap bmp) {  
        placeholder = bmp;  
    }  
  
    public Bitmap getBitmapFromCache(Uri uri) {  
        if (cache.containsKey(uri)) {  
            return cache.get(uri).get();  
        }  
  
        return null;  
    }  
  
    public void queueJob(final Uri uri, final ImageView imageView,  
            final int width, final int height) {  
        /* Create handler in UI thread. */  
        final Handler handler = new Handler() {  
            @Override  
            public void handleMessage(Message msg) {  
                Uri tag = imageViews.get(imageView);  
                if (tag != null && tag.equals(uri)) {  
                    if (msg.obj != null) {  
                        imageView.setImageBitmap((Bitmap) msg.obj);  
                    } else {  
                        imageView.setImageBitmap(placeholder);  
                        Log.d(TAG, "fail " + uri);  
                    }  
                }  
            }  
        };
  
        pool.submit(new Runnable() {  
            @Override  
            public void run() {  
                final Bitmap bmp = downloadBitmap(uri, width, height);  
                Message message = Message.obtain();
                message.obj = bmp;  
                Log.d(TAG, "Item downloaded: " + uri);  
                
                handler.sendMessage(message);  
            }  
        });  
    }  
  
    public void loadBitmap(Context context, final Uri uri, final ImageView imageView,  
            final int width, final int height) {  
    	
    	this.ctx = context;
    	
        imageViews.put(imageView, uri);  
        Bitmap bitmap = getBitmapFromCache(uri);  
  
        // check in UI thread, so no concurrency issues  
        if (bitmap != null) {  
            Log.d(TAG, "Item loaded from cache: " + uri); 
            imageView.setImageBitmap(bitmap);  
        } else {  
            imageView.setImageBitmap(placeholder);  
            queueJob(uri, imageView, width, height);  
        }  
    }  
  
    public Bitmap downloadBitmap(Uri uri, int width, int height) {  
    	
    	ContentResolver cr = ctx.getContentResolver();
        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
		Bitmap bitmap = BitmapFactory.decodeStream(input);
		bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);  
		cache.put(uri, new SoftReference<Bitmap>(bitmap));  
		return bitmap;  
    }  
}  