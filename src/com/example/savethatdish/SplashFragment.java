package com.example.savethatdish;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.facebook.Request;
import com.facebook.Session;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ParseException;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class SplashFragment extends Fragment {
	
	private ImageButton loginButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, 
	        ViewGroup container, Bundle savedInstanceState) {
		Log.w("TEST", "SplashFragment onCreateView()");
	    View view = inflater.inflate(R.layout.splash, 
	            container, false);
	    return view;
	}
	
	@Override
	public void onStart() {
	    super.onStart();
	    Log.w("TEST", "SplashFragment onStart()");
	    loginButton = (ImageButton) getView().findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		onLoginButtonClicked();
        	}
        });
        
	}
	
	private void makeUserRequest() {
		Log.w("TEST", "SplashFragment making user request");
		Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
	            new Request.GraphUserCallback() {
	                @Override
	                public void onCompleted(GraphUser user, Response response) {
	                    // handle response
	                	if (user != null) {
	                       Log.w("TEST", "Setting user parseobject stuff");
	                       // Now add the data to the UI elements
	                       // Test add user email to parse
	                       ParseUser pUser = ParseUser.getCurrentUser();
	                       pUser.put("email", user.getProperty("email"));
	                       pUser.put("username", user.getName());
	                       pUser.put("fbId", user.getId());
	                       try {
	                          pUser.save();
						   } catch (ParseException e) {
				     	      Log.w("TEST", "Coulddn't save ParseUser (1)");
						   }
	                    } else if (response.getError() != null) {
	                        // handle error
	                    	Log.w("TEST", "Some error with response");
	                    }            
	                }
	            });
	    request.executeAsync();
	}
	
	private void makeFriendsRequest() {
		Log.w("TEST", "SplashFragment making friends request");
		Request request = Request.newMyFriendsRequest(ParseFacebookUtils.getSession(),
				new Request.GraphUserListCallback() {
					@Override
					public void onCompleted(List<GraphUser> friends, Response response) {
						if (friends != null && friends.size() > 0) {
							Log.w("TEST", "Setting user friends");
							List<String> friendIds = new ArrayList<String>(friends.size());
							//String[] friendNames = new String[friends.size()];
							GraphUser next;
							for (int i=0; i<friends.size(); i++) {
								next = friends.get(i);
								friendIds.add(i,next.getId());
								//friendNames[i] = next.getName();
							}
							getFriendObjectIds(friendIds);
							//ParseUser pUser = ParseUser.getCurrentUser();
						    //pUser.addAllUnique("friends", Arrays.asList(friendObjectIds));
						    //pUser.addAllUnique("friendnames", Arrays.asList(friendNames));
							//pUser.saveInBackground();
						} else if (friends != null && friends.size() == 0) {
							Log.w("TEST", "No friends are using app");
						} else if (response.getError() != null) {
							Log.w("TEST", "Some error with response");
							Log.w("TEST", response.getError().getErrorMessage());
						}
					}
				});
		request.executeAsync();
	}
	
	private void getFriendObjectIds(List<String> friendFbIds) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
		query.whereContainedIn("fbId", friendFbIds);
	    query.findInBackground(new FindCallback<ParseObject>() {
	    	@Override
	    	public void done(List<ParseObject> objects, ParseException e) {
	    		if(e == null) {
	    			if(objects.size() == 0) {
	    				Log.w("TEST", "No friends found...");
	    			} else {
	    			   Log.d("TEST", "Setting friends' objectids");
	    			   Log.d("TEST", "objects size is: " + objects.size());
	    			   String[] friendObjectIds = new String[objects.size()];
	    			   String[] friendNames = new String[objects.size()];
	    			   ParseObject next;
	    			   for(int i=0; i<objects.size(); i++) {
	    			      next = objects.get(i);
	    				  Log.d("TEST", "next is: " + next);
	    				  friendObjectIds[i] = next.getObjectId();
	    				  friendNames[i] = (String) next.get("username");
	    				  Log.d("TEST", "name is: " + friendNames[i]);
	    			   }
	    			   ParseUser pUser = ParseUser.getCurrentUser();
	    			   pUser.addAllUnique("friends", Arrays.asList(friendObjectIds));
	    			   pUser.addAllUnique("friendnames", Arrays.asList(friendNames));
	    			   pUser.saveInBackground();
	    			}
	    		} else {
	    			Log.d("TEST", "getFriendObjectIds Error: " + e.getMessage());
	    		}
	    	}
	    });
	}
	
	private void onLoginButtonClicked() {
		Log.w("TEST", "Login button pressed");
    	List<String> permissions = Arrays.asList("email", "user_friends");
    	ParseFacebookUtils.logIn(permissions, getActivity(), new LogInCallback() {
    		  @Override
    		  public void done(ParseUser user, ParseException err) {
    		    if (user == null) {
    		      Log.d("TEST", "Uh oh. The user cancelled the Facebook login.");
    		    } else if (user.isNew()) {
    		      Log.d("TEST", "User signed up and logged in through Facebook!");
    		      // Fetch Facebook user info if the session is active
    		        Session session = ParseFacebookUtils.getSession();
    		        if (session != null && session.isOpened()) {
    		        	Log.w("TEST", "Session is open, therefore open dishlsit");
    		        	((MainActivity)getActivity()).showFragment(MainActivity.DISHLIST, false);
    		        	makeUserRequest();
    		        	makeFriendsRequest();
    		        }
    		        if(ParseFacebookUtils.isLinked(user)) {
	    		    	  Log.w("TEST", "User is linked with parse");
	    		      } else {
	    		    	  Log.w("TEST", "User not linked with parse");
	    		      }

    		    } else {
    		      Log.d("TEST", "User logged in through Facebook!");
    		      //ParseFacebookUtils.saveLatestSessionData(user);
    		      // Fetch Facebook user info if the session is active
    		     
    		        Session session = ParseFacebookUtils.getSession();
    		        if (session != null && session.isOpened()) {
    		        	Log.w("TEST", "Session is open, therefore open dishlsit");
    		        	((MainActivity)getActivity()).showFragment(MainActivity.DISHLIST, false);
    		        	makeFriendsRequest();
    		        }
    		      if(ParseFacebookUtils.isLinked(user)) {
    		    	  Log.w("TEST", "User is linked with parse");
    		      } else {
    		    	  Log.w("TEST", "User not linked with parse");
    		      }
    		    }
    		  }
    		});
	}
	
	@Override
	public void onStop() {
		super.onStop();
		Log.w("TEST", "SplashFragment onStop()");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Log.w("TEST", "SplashFragment onPause()");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.w("TEST", "SplashFragment onResume()");
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.w("TEST", "SplashFragment onDestroyView()");
	}
	
}
