package com.example.savethatdish;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;


public class HamburgerActivity extends Activity{
    float x1,x2;
    float y1, y2;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
		
		
		setContentView(R.layout.hamburger);
		
		
		/* DishList */
        ImageView dishlist = (ImageView) findViewById(R.id.dishList);
        dishlist.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        	startActivity(new Intent(HamburgerActivity.this, DishListFragment.class));
        	overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
        }
        });
        
        /* History */
        ImageView history = (ImageView) findViewById(R.id.history);
        history.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        	startActivity(new Intent(HamburgerActivity.this, MainActivity.class));
        	overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
        }
        });
		
		
		/* Profile */
        ImageView profile = (ImageView) findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        	startActivity(new Intent(HamburgerActivity.this, MainActivity.class));//CHANGE THIS TO PROFILE
        	overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
        }
        });
        
        
		/* Friends */
        ImageView friends = (ImageView) findViewById(R.id.friends);
        friends.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        	startActivity(new Intent(HamburgerActivity.this, MainActivity.class));//CHANGE THIS TO FRIENDS
        	overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
        }
        });
        
        
		/* Settings */
        ImageView settings = (ImageView) findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        	startActivity(new Intent(HamburgerActivity.this, SettingsActivity.class));
        	overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
        }
        });
        
        
		/* Logout */
        ImageView logout = (ImageView) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        	startActivity(new Intent(HamburgerActivity.this, MainActivity.class));//CHANGE THIS TO LOGOUT
        	overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
        }
        });
        
        
		/* Back Arrow */
        ImageView back = (ImageView) findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        	finish();
        	}
        });	
        
	}
        
   	 /* SWIPE YOUR FINGER RIGHT TO LEFT AND HAMBURGER MENU WILL CLOSE */
   	 public boolean onTouchEvent(MotionEvent touchevent) 
        {
          switch (touchevent.getAction())
          {
            case MotionEvent.ACTION_DOWN: 
            {
              x1 = touchevent.getX();
              y1 = touchevent.getY();
              break;
            }
            case MotionEvent.ACTION_UP: 
            {
              x2 = touchevent.getX();
              y2 = touchevent.getY(); 

              if (x1 > x2)//R to L swipe 
              {
            	 finish();
              }
            }
          }
          return false;
        }
}
