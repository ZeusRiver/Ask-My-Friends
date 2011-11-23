package com.zeusriver.askmyfriends.android.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {
    
    protected boolean _active = true;
    protected int _splashTime = 3000; // show splash for 3 secs
     
        
    @Override
       public void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           setContentView(R.layout.splash);   
           
        // thread for displaying the SplashScreen
            Thread splashThread = new Thread() {
                @Override
                public void run() {
                    try {
                        int waited = 0;
                        while(_active && (waited < _splashTime)) {
                            sleep(100);
                            if(_active) {
                                waited += 100;
                            }
                        }
                    } catch(InterruptedException e) {
                        // do nothing
                    } finally {
                        finish();
                                                
                        Intent intent = new Intent();
                        intent.setComponent(new ComponentName(getBaseContext(), HomeActivity.class));
                        startActivity(intent);    
                    }
                }
            };
            splashThread.start();            
    }
}
