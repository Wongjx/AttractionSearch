package com.example.searchbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends FragmentActivity implements OnMapLoadedCallback{
	public static String SearchQ ="SearchQ";
	public static GoogleMap gmap;
	public static float zoom= 15;
	public static ArrayList<String> words = new ArrayList<String>();
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
        		.findFragmentById(R.id.mapFrag);
        gmap = mapFragment.getMap();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
               (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        
        searchView.setOnQueryTextListener(new OnQueryTextListener() { 
        	 
            @Override 
            public boolean onQueryTextChange(String query) {
            	Log.d("ChangeTEXT", "Word");
                searchView.setSubmitButtonEnabled(true);
                return true; 
 
            }

			@Override
			public boolean onQueryTextSubmit(String query) {
				Log.d("SubmitedTEXT", "Worded");
				String QU=acorrect(query);
				SearchMap(QU);
				return true;
			} 
 
        });
        
        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_sat) {
        	gmap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            return true;
        }
        if (id == R.id.action_norm) {
        	gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void SearchMap(String search){
    	String myLocation =search;
    	Geocoder myGeoCoder = new Geocoder(this);
    	List<Address> matchedAddresses=null;
    	try{
    		matchedAddresses=myGeoCoder.getFromLocationName(myLocation,5);
    	}catch(IOException e){
    		Toast.makeText(this, "Cannot find a loaction", Toast.LENGTH_LONG).show();
    	}
    	
    	Intent myIntent = new Intent (Intent.ACTION_VIEW);
    	Double lat = null;
    	Double lon = null;
    	try{
    		lat= matchedAddresses.get(0).getLatitude();
    		lon= matchedAddresses.get(0).getLongitude();
    		LatLng coord= new LatLng(lat,lon);
    		gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(coord,zoom));
//        	myIntent.setData(Uri.parse("geo:"+lat+","+lon));    	
//        	Intent chooser =Intent.createChooser(myIntent, "Launch Maps");
//        	startActivity(chooser);
    	} catch(Exception e ){
    		Toast.makeText(this, "Could not resolve the requested loaction!", Toast.LENGTH_LONG);
    	}
    }
    
	public static String acorrect(String s) {
		String output = "";
		for (Place p: Place.values()) {
			String[] t = p.name.split(" ");
			for (String w: t) {
				if (!words.contains(w)) {
					words.add(w);
				}
			}
		}
		String[] splitted = s.split(" ");
		for (String msg: splitted) {
			HashMap<String, Integer> m = new HashMap<String, Integer>();
			int count = 4;
			String word = "";
			for (String w: words) {
				int cost = Lev(msg.toLowerCase(),w.toLowerCase());
				if (cost == 0) {
					output += msg + " ";
					continue;
				}
				else if (cost <= 3) {
					m.put(w, cost);
				}
			}
			for (String matched: m.keySet()) {
				if (m.get(matched) < count) {
					count = m.get(matched);
					word = matched;
				}
			}
			if (word != "") {
				output += word +" ";
			}
			else {
				output += msg + " ";
			}
		}
		return output;
	}
	
	public static int Lev(String a, String b) {
		if (a == b) {
			return 0;
		}
		if (a.length() == 0) {
			return b.length();
		}
		if (b.length() == 0) {
			return a.length();
		}
		int[] v1 = new int[b.length() +1];
		int[] v2 = new int[b.length() +1];
		
		for (int i = 0;i < v1.length; i++) {
			v1[i] = i;
		}
		for (int j = 0; j < a.length(); j++) {
			v2[0] = j + 1;
			for (int k = 0;k < b.length(); k++) {
				int cost = (a.charAt(j) == b.charAt(k)) ? 0 : 1;
				v2[k+1] = Math.min(Math.min(v2[k] + 1, v1[k+1] + 1), v1[k] + cost);
			}
			for (int l = 0;l < v1.length; l++) {
				v1[l] = v2[l];
			}
		}
		return v2[b.length()];
		
	}


	@Override
	public void onMapLoaded() {
	    gmap.addMarker(new MarkerOptions()
        .position(new LatLng(0, 0))
        .title("Marker"));
		
	} 
}
