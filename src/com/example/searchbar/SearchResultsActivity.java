package com.example.searchbar;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

public class SearchResultsActivity extends Activity {
	TextView text=(TextView) findViewById(R.id.textView1);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.result);
        handleIntent(getIntent());
    }
    
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        // Associate searchable configuration with the SearchView
//        SearchManager searchManager =
//               (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        final SearchView searchView =
//                (SearchView) menu.findItem(R.id.search).getActionView();
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));
//        
//        searchView.setOnQueryTextListener(new OnQueryTextListener() { 
//        	 
//            @Override 
//            public boolean onQueryTextChange(String query) {
//            	Log.d("ChangeTEXT", "Word");
//                searchView.setSubmitButtonEnabled(true);
//                return true; 
// 
//            }
//
//			@Override
//			public boolean onQueryTextSubmit(String query) {
//				Log.d("SubmitedTEXT", "Worded");
//				return true;
//			} 
// 
//        });
//        
//        
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    protected void onNewIntent(Intent intent) {
        
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
    	String query = intent.getStringExtra(SearchManager.QUERY);
//    	String query = intent.getStringExtra(MainActivity.SearchQ);
    	text.setText(query);
            //use the query to search your data somehow
    }
}