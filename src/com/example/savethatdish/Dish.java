package com.example.savethatdish;

import com.parse.ParseObject;

public class Dish {

   private String name;
   private String objectId;
   private String description;
   private String restaurantId;
   private String price;
   //listed
   //liked
   //disliked
   
   public Dish(ParseObject o)
   {
	   setName((String) o.get("name"));
	   setObjectId((String) o.getObjectId());
	   setDescription((String) o.get("description"));
	   setPrice((String) o.get("price"));
	   setRestaurantId((String) o.get("restaurantId"));
   }
   
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the objectId
	 */
	public String getObjectId() {
		return objectId;
	}
	
	/**
	 * @param objectId the objectId to set
	 */
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return the restaurantId
	 */
	public String getRestaurantId() {
		return restaurantId;
	}
	
	/**
	 * @param restaurantId the restaurantId to set
	 */
	public void setRestaurantId(String restaurantId) {
		this.restaurantId = restaurantId;
	}
	
	/**
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}
	
	/**
	 * @param price the price to set
	 */
	public void setPrice(String price) {
		this.price = price;
	}
	
	//---------------------------USER INTERACTION METHODS---------------------------------
   public boolean addToParse() {
	   ParseObject dish = new ParseObject("Dish"); 
	   dish.add("name", name);
	   
	   //PULL INFORMATION (LOCATION, RESTAURANT NAME...) FROM YELP
	   //LAWRENCE HELP
	   
	   dish.saveInBackground();//Adds to database
	   
	   //Associated info to add: restaurant name, location. Anything else?
	   return true; //need to do error handling before returning
   }

   /*
    * Purpose: delete local copy, connect to parse?
    * @Return: Boolean value indicating whether remove was successful
    * Other: Error Handling
    */
   
   public boolean removeDish() {
	   return true;
   }

   /* 
    * @param: rating
    * @return: Boolean value letting caller know if it was successful.
    * Other: Error handling with Parse should be done in this method. Use that
    * info to return the proper Boolean
    */
   public boolean rateDish(int rating) // integer or boolean param?
   {
	   return true;
   }

   /* 
    * @return: returns the number of total likes for a dish
    */
   public int allLikes() {
      return 0;
   }

   /* 
    * @param: userName
    * @return: returns true if dish is on bucketlist,
    *  	    returns false if dish is not on bucketlist
    */
   public boolean onBucketlist(String userName) {
      return false;
   }

   /* 
    * @param: userName
    * @return: returns true if successfully added to bucketlist,
    *  	    returns false if already on bucketlist
    */
   public boolean addToBucketlist(String userName) {
      if (onBucketlist(userName))
        return false;
      //else add to Parse
      return true;
   }

}
