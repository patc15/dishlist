package com.example.savethatdish;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ResultsActivity extends Activity {
	 
	private ListView listView;
	private ImageView listButton, mapButton;
	private List<JSONObject> results;
	private List<String> addresses;
	private RestaurantAdapter restaurantAdapter;
	
	float x1, x2, y1, y2;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.results);
		
		ImageButton hamburgerButton = (ImageButton)findViewById(R.id.hamburger_results);
		hamburgerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent hamburger = new Intent(ResultsActivity.this, HamburgerActivity.class);
				startActivity(hamburger);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
			}
		});
		
		ImageButton addButton = (ImageButton)findViewById(R.id.add_results);
		addButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent search = new Intent(ResultsActivity.this, SearchActivity.class);
				startActivity(search);
			}
		});
		
		listView = (ListView)findViewById(R.id.searchResults);
		results = SearchActivity.returnResults();
		addresses = new ArrayList<String>();
		listButton = (ImageView)findViewById(R.id.left_list_tab);
		mapButton = (ImageView)findViewById(R.id.right_map_tab);
		mapButton.setDrawingCacheEnabled(true);
		mapButton.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				Bitmap bmp = Bitmap.createBitmap(arg0.getDrawingCache());
				int color = bmp.getPixel((int)arg1.getX(), (int)arg1.getY());
				if(color == Color.TRANSPARENT)
					return false;
				else {
					Intent map = new Intent(ResultsActivity.this, LargeMapActivity.class);
					startActivity(map);
					return true;
				}
			}
		});
		
		restaurantAdapter = new RestaurantAdapter(ResultsActivity.this, R.layout.label);

		for(JSONObject j : results) {
			try {
				JSONObject location = (JSONObject) j.get("location");
				JSONArray address = (JSONArray) location.get("display_address");
				String city = (String) location.get("city");
				addresses.add(address.get(0) + ", " + city);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		checkParseRestaurant(addresses);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {	
				Intent restaurant = new Intent(ResultsActivity.this, RestaurantActivity.class);
 				Restaurant currRestaurant = (Restaurant) arg0.getItemAtPosition(arg2);
 				restaurant.putExtra("restaurant_id", currRestaurant.getObjectId());
 				startActivity(restaurant);
			}
		});
		
		listButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//do nothing
			}
		});
		
		mapButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent map = new Intent(ResultsActivity.this, LargeMapActivity.class);
				startActivity(map);
			}
		});
	}
	
	public void checkParseRestaurant(List<String> addresses) {		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Restaurant");
		query.whereContainedIn("short_address", addresses);
		query.findInBackground(new FindCallback<ParseObject>() {
			@SuppressWarnings("unchecked")
			public void done(List<ParseObject> restaurants, ParseException e) {
				if (e == null) {
					new ResultsTask().execute(restaurants);
				} 
				else {
				}
            }
		});
	}
	
	 /* SWIPE YOUR FINGER LEFT TO RIGHT AND HAMBURGER MENU WILL OPEN 
 	  * SWIPE YOUR FINGER RIGHT TO LEFT AND SEARCH WILL OPEN
 	  */
 	 public boolean onTouchEvent(MotionEvent touchevent) 
     {
       switch (touchevent.getAction())
       {
         case MotionEvent.ACTION_DOWN: 
         {
           x1 = touchevent.getX(); y1 = touchevent.getY();
           break;
         }
         case MotionEvent.ACTION_UP: 
         {
           x2 = touchevent.getX(); y2 = touchevent.getY(); 
           if (x1 < x2)//L to R swipe 
           {
        	      Intent intent = new Intent(ResultsActivity.this, HamburgerActivity.class);
           	  startActivity(intent);     
       	      overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
           }
           else if(x1 > x2)
           {
        	      Intent intent = new Intent(ResultsActivity.this, SearchActivity.class);
           	  startActivity(intent);     
       	      overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
           }
         }
       }
     return false;
    }
	
	public void listThings(List<ParseObject> restaurants) {
		for(ParseObject o : restaurants) {
			restaurantAdapter.add(new Restaurant(o));
		}
	}
	
	public class ResultsTask extends AsyncTask<List<ParseObject>, Void, Void> {
		@Override
		protected Void doInBackground(List<ParseObject>... params) {
			listThings(params[0]);
			return null;
		}
		
		protected void onPostExecute(Void result) {
			listView.setAdapter(restaurantAdapter);
		}
	}
}