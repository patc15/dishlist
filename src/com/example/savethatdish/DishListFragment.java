package com.example.savethatdish;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.facebook.Session;
import com.parse.ParseUser;

public class DishListFragment extends Fragment implements OnClickListener {
		
	private boolean isCurrentlyInDishlist; //flag that indicates whether this is currently in dishlist (false if history)
	private ImageButton dishListTextButton, historyTextButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		Log.w("TEST", "DishFragment onCreateView()");
		return inflater.inflate(R.layout.dishlist, container, false);
	}
	
	/** Called when the activity is first created. */
	@Override
	public void onStart() {
	    super.onStart();
	    Log.w("TEST", "DishFragment onStart()");
	    /* Set up button, listeners */
	    ImageButton hamburger = (ImageButton) getView().findViewById(R.id.hamburger_dishlist);  //hamburgerButton declared in dishlist.xml
	    ImageButton addButton = (ImageButton) getView().findViewById(R.id.add_dishlist);
	    //ImageButton sortButton = (ImageButton) getView().findViewById(R.id.sortButton);
	    //ImageButton mapButton = (ImageButton) getView().findViewById(R.id.mapButton);
	    dishListTextButton = (ImageButton) getView().findViewById(R.id.left_dishlist_tab);
	    historyTextButton = (ImageButton) getView().findViewById(R.id.right_history_tab);
	    
	    if (hamburger != null)
	    	hamburger.setOnClickListener(this);
	    if (addButton != null)
	    	addButton.setOnClickListener(this);
	    //if (sortButton != null)
	    //	sortButton.setOnClickListener(this);
	    //if (mapButton != null)
	    //	mapButton.setOnClickListener(this);
	    if (dishListTextButton != null)
	    	dishListTextButton.setOnClickListener(this);
	    if (historyTextButton != null)
	    	historyTextButton.setOnClickListener(this);

        isCurrentlyInDishlist = true; //start off in dishList mode.
	}
	
    // Implement the OnClickListener callback
    public void onClick(View v) {
    	if (v.getId() == R.id.hamburger_dishlist) {
			Intent hamburger = new Intent(getActivity(), HamburgerActivity.class);
			startActivity(hamburger);
    	}
    	else if (v.getId() == R.id.add_dishlist) {
			Intent search = new Intent(getActivity(), SearchActivity.class);
			startActivity(search);
       	
	        // Log the user out. ----------------------TEMPORARY FOR TESTING-------------------------
	        //ParseUser.logOut();
	        //Session session = Session.getActiveSession();
	        //if (session != null){
	        //     session.closeAndClearTokenInformation();
	        //     session = null;
	        //     Log.w("TEST", "logged out and cleared session");
	        //}
	        // Go to the login view
	        //((MainActivity)getActivity()).showFragment(MainActivity.SPLASH, false);
	        // --------------------------------------------------------------------------------------
    	}
      
		//else if (v.getId() == R.id.sortButton) {
		//  Log.w("TEST", "Sort Button pressed");
		//}
		  
		//else if (v.getId() == R.id.mapButton) {
		//	Log.w("TEST", "Map button pressed");
		//}
		  
		else if (v.getId() == R.id.left_dishlist_tab) {
			Log.w("TEST", "Dishlist button pressed");
			changeToDishlist();
		}
		  
		else if (v.getId() == R.id.right_history_tab) {
			Log.w("TEST", "History button pressed");
			changeToHistory();
		}
    }
	
    /*
     * Change the stuff on the screen to go from History to Dishlist
     * Parameters: None
     * Return: Boolean that indicates whether it successfully is in Dishlist now
     * Side Effects: Changes the screen UI to reflect the new mode it is in; shows the dishlist entries
     */
    
    private boolean changeToDishlist() {
    	if (isCurrentlyInDishlist)
    		return true; //already in dishlist, don't have to change anything so don't requery
    	
    	/* CHANGE THE UI */
    	if (dishListTextButton != null)
    		dishListTextButton.setImageResource(R.drawable.left_dishlist_tab_filled);
    	
    	if (historyTextButton != null)
    		historyTextButton.setImageResource(R.drawable.right_history_tab_empty);		
    	/* END CHANGING THE UI */
    	   	
    	/* CHANGE THE LIST OF RESTAURANTS */
    	
    	/* END CHANGING THE LIST OF RESTAURANTS */
    	
    	isCurrentlyInDishlist = true;
    	return true;
    	
    }
    
    /*
     * Change the stuff on the screen to go from Dishlist to History 
     * Parameters: None
     * Return: Boolean that indicates whether it is successfully in History
     * Side Effects: Changes the screen UI to reflect the new mode it is in; shows the History entries
     */
    
    private boolean changeToHistory() {
    	if (!isCurrentlyInDishlist)
    		return true; //already in history, don't have to change anything.
    	
    	/* CHANGE THE UI */
    	if (dishListTextButton != null)
    		dishListTextButton.setImageResource(R.drawable.left_dishlist_tab_empty);
    	
    	if (historyTextButton != null)
    		historyTextButton.setImageResource(R.drawable.right_history_tab_filled);	   	
    	/* END CHANGING THE UI */
    	
    	/* CHANGE THE LIST OF RESTAURANTS */
    	
    	/* END CHANGING THE LIST OF RESTAURANTS */
    	
    	isCurrentlyInDishlist = false;
    	return true;
    }
    
    
    /* 
     * Android LifeCycle
     * See: http://stackoverflow.com/questions/6812003/difference-between-oncreate-and-onstart
     * Also, see: http://developer.android.com/guide/components/activities.html#Lifecycle
     */
    
    @Override
	public void onStop() {
		super.onStop();
		Log.w("TEST", "DishFragment onStop()");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Log.w("TEST", "DishFragment onPause()");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.w("TEST", "DishFragment onResume()");
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.w("TEST", "DishFragment onDestroyView()");
	}
}
