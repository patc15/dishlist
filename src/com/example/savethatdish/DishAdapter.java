package com.example.savethatdish;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DishAdapter extends ArrayAdapter<Dish>
{
	//Adapter context
	Context mContext;

	//Adapter View layout
	int mLayoutResourceId;

	public DishAdapter(Context context, int layoutResourceId) {
		super(context, layoutResourceId);

		mContext = context;
		mLayoutResourceId = layoutResourceId;
	}	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;

		final Dish currentItem = getItem(position);

		if (row == null) {
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			row = inflater.inflate(mLayoutResourceId, parent, false);
		}

		row.setTag(currentItem);
		Log.w("TEST", currentItem.getName());
		//Update text fields to reflect dish's name, description, and price
		final TextView name = (TextView) row.findViewById(R.id.dish_name);
		name.setText(currentItem.getName());

		final TextView description = (TextView) row.findViewById(R.id.dish_descr);
		description.setText(currentItem.getDescription());
		
		final TextView price = (TextView) row.findViewById(R.id.dish_price);
		price.setText(currentItem.getPrice());

		return row;
	
	}
}