package com.example.savethatdish;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

public class MainActivity extends FragmentActivity {
	
	public static final int SPLASH = 0;
	public static final int DISHLIST = 1;
	public static final int SETTINGS = 2;
	public static final int LOAD = 3;
	private static final int FRAGMENT_COUNT = LOAD + 1;

	private boolean loading;
	private boolean isResumed = false;
	private MenuItem settings;	// Menu item in settings
	private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];
	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = 
		new Session.StatusCallback() {
		@Override
        public void call(Session session, 
        	SessionState state, Exception exception) {
		   	Session session2 = ParseFacebookUtils.getSession();
	        onSessionStateChange(session2, state, exception);
	    }
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		    
	    Log.w("TEST", "Mainactivity onCreate()");
	    
	    /*
	     * Get rid of the title bar for all FragmentActivities associated with MainActivity
	     */
	    
	    //Remove title bar
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);

	    //Remove notification bar
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		
	    // ---------------UNCOMMENT TO TEST APP WITHOUT NEEDING TO LOGIN------------------
		//Intent intent = new Intent(this.SearchActivity.class);//Large Map Activity
		//startActivity(intent);
		// -------------------------------------------------------------------------------	
	    
	    uiHelper = new UiLifecycleHelper(this, callback);
	    uiHelper.onCreate(savedInstanceState);		    
		    
		// Initialize parse
	    Parse.initialize(this, "dmq07tEG39xubkof59l2UyXnZJcojifl3jlYQ0af", 
	    		"U0Lsnx5qHCdXTzPBtb8NMlInEApUUFEDq1q0gW83");
		  
	    ParseFacebookUtils.initialize("311859808965504");
		    
	    setContentView(R.layout.activity_main);
		   

		/* MANAGE FRAGMENTS */
	    FragmentManager fm = getSupportFragmentManager();
	    fragments[SPLASH] = fm.findFragmentById(R.id.splashFragment);
	    fragments[DISHLIST] = fm.findFragmentById(R.id.dishListFragment);
	    fragments[SETTINGS] = fm.findFragmentById(R.id.userSettingsFragment);
	    fragments[LOAD] = fm.findFragmentById(R.id.loadFragment);

	    FragmentTransaction transaction = fm.beginTransaction();
	    for(int i = 0; i < fragments.length; i++) {
	        transaction.hide(fragments[i]);	
	    }
	    transaction.commit();
	}
	
	public void showFragment(int fragmentIndex, boolean addToBackStack) {
		Log.w("TEST", "Mainactivity showFragment() with fragmentIndex: " + fragmentIndex);
		FragmentManager fm = getSupportFragmentManager();
	    FragmentTransaction transaction = fm.beginTransaction();
        for (int i = 0; i < fragments.length; i++) {
        	if (i == fragmentIndex) {
	            transaction.show(fragments[i]);
	        } else {
	        	transaction.hide(fragments[i]);
	        }
	    }
	    if (addToBackStack) {
	        transaction.addToBackStack(null);
	    }
	    transaction.commit();
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    Log.w("TEST", "Mainactivity onResume()");
	    uiHelper.onResume();
	    isResumed = true;
	}

	@Override
	public void onPause() {
	    super.onPause();
	    Log.w("TEST", "Mainactivity onPause()");
	    uiHelper.onPause();
	    isResumed = false;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	    Log.w("TEST", "Mainactivity onActivityResult called");
	    loading = true;
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    Log.w("TEST", "Mainactivity onDestroy()");
	    uiHelper.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		Log.w("TEST", "Mainactivity session state changing");
	    // Only make changes if the activity is visible
	    if (isResumed) {
	    	Log.w("TEST", "Mainactivity is resumed");
	        FragmentManager manager = getSupportFragmentManager();
	        // Get the number of entries in the back stack
	        int backStackSize = manager.getBackStackEntryCount();
	        // Clear the back stack
	        for (int i = 0; i < backStackSize; i++) {
	            manager.popBackStack();
	        }
	        if (session != null && session.isOpened()) {
	        	Log.w("TEST", "Session is open");
	            // If the session state is open:
	            // Show the authenticated fragment
	            showFragment(DISHLIST, false);
	        } else if (session != null && session.isClosed()) {
	        	Log.w("TEST", "Session is closed");
	        	if(loading) {
	        	   loading = false;
	        	   showFragment(LOAD, false);
	        	} else {
	               // If the session state is closed:
	               // Show the login fragment
	               showFragment(SPLASH, false);
	        	}
	        }
	    }
	}
	
	@Override
	protected void onResumeFragments() {
	    super.onResumeFragments();
	    Log.w("TEST", "Mainactivity onResumeFragments()");
	    Session session = Session.getActiveSession();

	    if (session != null && session.isOpened()) {
	    	Log.w("TEST", "resuming dishFragment");
	        // if the session is already open,
	        // try to show the DISHLIST fragment
	        showFragment(DISHLIST, false);
	    } else {
	    	if(loading) {
	    	   Log.w("TEST", "resuming loadFragment");
	    	   loading = false;
	    	   showFragment(LOAD, false);
	    	} else {
	    	   Log.w("TEST", "resuming splashFragment");
	           // otherwise present the splash screen
	           // and ask the person to login.
	           showFragment(SPLASH, false);
	    	}
	    }
	}
	
	
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
	    // only add the menu when the DISHLIST fragment is showing
	    if (fragments[DISHLIST].isVisible()) {
	        if (menu.size() == 0) {
	            settings = menu.add(R.string.settings);
	        }
	        return true;
	    } else {
	        menu.clear();
	        settings = null;
	    }
	    return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    if (item.equals(settings)) {
	        showFragment(SETTINGS, true);
	        return true;
	    }
	    return false;
	}
}
