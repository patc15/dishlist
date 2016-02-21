package com.example.savethatdish;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

public class Yelp {
	public static final String CONSUMER_KEY = "PYJ9fp4Zs357x8GKEcc2OA";
	public static String CONSUMER_SECRET = "Svw5yWnPK26_WYOrbkcvsC4PMNU";
	public static final String TOKEN = "_o14KmTq969arSh-BdJBIHIBLanS3h2J";
	public static final String TOKEN_SECRET = "MLFvYopfLQVy8YWpN7WObb8u_EA";
	
	OAuthService service;
	Token accessToken;

	public Yelp(String consumerKey, String consumerSecret, String token, String tokenSecret) {
		this.service = new ServiceBuilder().provider(YelpApi2.class)
				.apiKey(consumerKey).apiSecret(consumerSecret).build();
		this.accessToken = new Token(token, tokenSecret);
	}
	
	public String search(String term, String sort, String location) {
		OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
		request.addQuerystringParameter("term", term);
		request.addQuerystringParameter("sort", sort);
		request.addQuerystringParameter("location", location);
		this.service.signRequest(this.accessToken, request);
		Response response = request.send();
	    return response.getBody();    
	}
	   
	public String regionSearch(String latitude, String longitude) {
		OAuthRequest request = new OAuthRequest(Verb.GET,
	              "http://api.yelp.com/v2/search");
		request.addQuerystringParameter("ll", latitude+","+longitude);
		this.service.signRequest(this.accessToken, request);
	    Response response = request.send();
	    return response.getBody();    
	}
	   
	/**public static void main(String[] args) throws JSONException {
		Yelp yelp = new Yelp(CONSUMER_KEY, CONSUMER_SECRET, TOKEN, TOKEN_SECRET);
		//String response = yelp.search("Tacos", "0", "La Jolla");
		String response = yelp.regionSearch("37.77493", "-122.419415");
		JSONObject obj = new JSONObject(response);
		JSONArray businesses = obj.getJSONArray("businesses");
		
		int n = businesses.length();
		for(int i = 0; i < n; i++) {
			JSONObject location = (JSONObject) businesses.getJSONObject(i).get("location");
			JSONArray address = (JSONArray) location.get("display_address");
			System.out.println(address.get(0) + ", " + address.get(1));
		}
	}*/
}