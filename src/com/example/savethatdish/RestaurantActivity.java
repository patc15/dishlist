package com.example.savethatdish;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class RestaurantActivity extends Activity{
	
	private DishAdapter dishAdapter;
	private Restaurant restaurant;
	private SquareImageView imageView;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.restaurant);
		
		// Create an adapter to bind the items with the view
		dishAdapter = new DishAdapter(this, R.layout.menu_item);
		ListView dishlist = (ListView) findViewById(R.id.dishesList);
		dishlist.setAdapter(dishAdapter);		

		//Find the ID of the restaurant to display
		Intent intent = getIntent();
		String restaurantId = intent.getStringExtra("restaurant_id");
		
		//Retrieve the restaurant ParseObject
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Restaurant");
		query.getInBackground(restaurantId, new GetCallback<ParseObject>() {
		  public void done(ParseObject object, ParseException e) {
		    if (e == null) {
		    	setInfo(object);
				//setNumFriends(object);
				//setDistance(object);
				listDishes(object);
		        new ImageTask().execute(restaurant);
		    } else {
		    	Log.w("TEST", "Failed :(");
		    	e.printStackTrace();
		    }
		  }
		});
				
	}
	
	private void setInfo(ParseObject restaurant) {
		TextView nameView, addressView, reviewsView, ratingView;
		
		this.restaurant = new Restaurant(restaurant);
		imageView = (SquareImageView) findViewById(R.id.label_logo);
        imageView.setImageDrawable(null);
        nameView = (TextView) findViewById(R.id.label_name);
        addressView = (TextView) findViewById(R.id.label_address);
        reviewsView = (TextView) findViewById(R.id.reviews_value);
        //milesView = (TextView) findViewById(R.id.distance_value);
        //friendView = (TextView) findViewById(R.id.friends_value);
        ratingView = (TextView) findViewById(R.id.rating_value);
        
        nameView.setText(this.restaurant.getName());
        addressView.setText(this.restaurant.getAddress());
        reviewsView.setText("in " + this.restaurant.getNumReviews() + " reviews");
        ratingView.setText(this.restaurant.getRating() + "/5");

	}

	/**private void setDistance(ParseObject object) {
		// TODO calculate distance from current location to the restaurant
		// set TestView on R.id.label_mile
	}
	
	private void setNumFriends(ParseObject object) {
		// TODO calculate number of friends who also want to visit this restaurant
		// set TextView on R.id.label_friend
	}*/
	
	/*Need to put this code whenever you click on a restaurant:
	Intent intent = new Intent("com.example.savethatdish.RestaurantActivity");
	intent.putExtra("restaurant_id", restaurant.getObjectId()); <-- restaurant is the parse object that was clicked
	startActivity(intent);
*/
	public class ImageTask extends AsyncTask<Restaurant, Void, Bitmap> {
		@Override
		protected Bitmap doInBackground(Restaurant... arg0) {
			Bitmap mIcon = null;
			InputStream newurl;
			try {
				newurl = (InputStream) new URL(arg0[0].getImageURL()).openStream();
				mIcon = BitmapFactory.decodeStream(newurl);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return mIcon;
		}
		
		protected void onPostExecute(Bitmap result) {
			imageView.setImageBitmap(result);
		}
	}
	
	private void listDishes(ParseObject object) {
		
		//Query all dishes in the database and choose the ones belonging to the right restaurant
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Dish");
		query.whereEqualTo("restaurant", object);

		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> dishList, ParseException e) {
		        if (e == null) {
		        	//Add the results to the adapter to be displayed
		        	for(ParseObject a : dishList)
		        	{
		        		dishAdapter.add(new Dish(a));
		        	}
		        } else {
		        	e.printStackTrace();
		        }
		    }
		});
	}
	
	@Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
	}
}