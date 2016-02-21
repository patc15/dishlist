package com.example.savethatdish;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class RestaurantAdapter extends ArrayAdapter<Restaurant>
{
	private int mLayoutResourceId;
	private SquareImageView imageView;
	private TextView nameView, addressView, reviewsView, ratingView; //friendView, milesView;
	private Restaurant currentRestaurant;
	private View rowView;

	public RestaurantAdapter(Context context, int layoutResourceId) {
		super(context, layoutResourceId);

		mLayoutResourceId = layoutResourceId;
	}	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        rowView = convertView;
        
        currentRestaurant = getItem(position);
        if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(mLayoutResourceId, null);
		}

        rowView.setTag(currentRestaurant);
       
        imageView = (SquareImageView) rowView.findViewById(R.id.label_logo);
        imageView.setImageDrawable(null);
        nameView = (TextView) rowView.findViewById(R.id.label_name);
        addressView = (TextView) rowView.findViewById(R.id.label_address);
        reviewsView = (TextView) rowView.findViewById(R.id.reviews_value);
        //milesView = (TextView) rowView.findViewById(R.id.distance_value);
        //friendView = (TextView) rowView.findViewById(R.id.friends_value);
        ratingView = (TextView) rowView.findViewById(R.id.rating_value);
        
        nameView.setText(currentRestaurant.getName());
        addressView.setText(currentRestaurant.getAddress());
        reviewsView.setText("in " + currentRestaurant.getNumReviews() + " reviews");
        ratingView.setText(currentRestaurant.getRating() + "/5");
        
        new ImageTask().execute(currentRestaurant);

        return rowView;
	}
	
	@Override
    public boolean areAllItemsEnabled() 
    {
        return true;
    }

    @Override
    public boolean isEnabled(int arg0) 
    {
        return true;
    }
	
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
			if (rowView.getTag() == currentRestaurant) 
				imageView.setImageBitmap(result);
		}
	}
}