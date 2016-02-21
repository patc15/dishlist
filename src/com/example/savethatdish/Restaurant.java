package com.example.savethatdish;

import com.parse.ParseObject;

public class Restaurant {

	private String imageURL;
	private String name;
	private String address;
	//private String distance;
	private String rating;
	private String numReviews;
	//private String numFriends;
	private String objectId;
	
	public Restaurant(ParseObject o) {
		setImageURL((String) o.get("yelp_image_url"));
		setName((String) o.get("name"));
		setAddress((String) o.get("full_address"));
		setRating(String.valueOf(o.get("rating")));
		setNumReviews(String.valueOf(o.get("review_count")));
		setObjectId((String)o.getObjectId());
	}

	public String getObjectId() {
		return objectId;
	}
	
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	
	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getNumReviews() {
		return numReviews;
	}

	public void setNumReviews(String numReviews) {
		this.numReviews = numReviews;
	}
	
}
