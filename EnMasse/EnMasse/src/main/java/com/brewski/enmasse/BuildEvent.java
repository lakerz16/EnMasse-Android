package com.brewski.enmasse;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;

import com.parse.ParseObject;

public class BuildEvent extends Activity {
	
	EditText edit0;
	EditText edit1;
	EditText edit2;
	
	@SuppressLint("NewApi")
	public void onCreate(Bundle state) {
		super.onCreate(state);
		setContentView(R.layout.activity_buildevent);
		
		
		edit0 = (EditText) findViewById(R.id.edit0);
		//edit1 = (EditText) findViewById(R.id.edit1);
		//edit2 = (EditText) findViewById(R.id.edit2);
		
		if(Build.VERSION.SDK_INT >= 11) {
		    android.app.ActionBar ab = getActionBar();
	        ab.setDisplayUseLogoEnabled(true);
	        ab.setDisplayShowTitleEnabled(false);
	        ab.setDisplayHomeAsUpEnabled(true);
	        //ab.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_buildevent, menu);
		return true;
	}
	
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
    	
    	switch(item.getItemId()) {
    	case R.id.menu_buildDone:
    		ParseObject testObject = new ParseObject("Events");
			testObject.put("name", edit0.getText().toString());
			testObject.saveInBackground();
			finish();
    		break;
    	default:
    		break;	
    	}

        return true;
    }
	
}