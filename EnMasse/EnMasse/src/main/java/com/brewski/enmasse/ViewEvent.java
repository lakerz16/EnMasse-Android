package com.brewski.enmasse;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class ViewEvent extends Activity {

	LinearLayout peopleList;
	LayoutInflater inflater;
	
	Globals globals;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewevent);
		
		peopleList = (LinearLayout) findViewById(R.id.peopleList);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		globals = (Globals) getApplicationContext();
		
		if(Build.VERSION.SDK_INT >= 11) {
		    android.app.ActionBar ab = getActionBar();
	        ab.setDisplayUseLogoEnabled(false);
	        ab.setDisplayShowHomeEnabled(false);
	        ab.setDisplayShowTitleEnabled(true);
	        ab.setTitle(globals.eventName);
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		fillPeopleList(((Globals)getApplicationContext()).currentEvent);
		
		refreshQuery(); // we should use this, then call fillPeopleList()
	}
	
	private void setOwnStatus() {
		ImageView statusIcon = (ImageView) findViewById(R.id.myStatusIcon);
		TextView statusText = (TextView) findViewById(R.id.myStatusText);
		TextView statusDescription = (TextView) findViewById(R.id.myStatusNote);
		
		statusIcon.setImageResource(R.drawable.state_blue);
		statusText.setText("Call Me Maybe");
		statusDescription.setText("Im thinking about it");
	}
	
	private void fillPeopleList(ParseObject ob) {
		peopleList.removeAllViews();
		
		if(ob.getString("going") == null)
			return;
		
		setOwnStatus();
    	
    	int i=0;
    	for(String peep : ob.getString("going").split("\\|")) {
    		
    		inflater.inflate(R.layout.people_row, peopleList);
    		View x = peopleList.getChildAt(i);
    		x.setBackgroundResource(listBacks[2]);
    		i++;
    		
    		try {
    			String[] nameState = peep.split("\\*");
        		((TextView)(x.findViewById(R.id.personName))).setText(nameState[0]);
        		((ImageButton)(x.findViewById(R.id.personIcon))).setImageResource(stateIcons[(Integer.parseInt(nameState[1]))]);
    		} catch (Exception e) {
    			
    		}
    	}
	}
	
	int[] listBacks = {R.drawable.list1,
					R.drawable.list2,
					R.drawable.list3,
					R.drawable.list4,
					R.drawable.list5,
					R.drawable.list6};
	
	int[] stateIcons = {R.drawable.state_gray,
					R.drawable.state_red,
					R.drawable.state_blue,
					R.drawable.state_green};
	
	private void refreshQuery() {
		
		// need to do this correctly
		
		ParseQuery query = new ParseQuery("Events");
        query.findInBackground(new FindCallback() {
            @Override
            public void done(List list, ParseException e) {
                if (e == null) {
                    for(ParseObject po : (List<ParseObject>) list) {
                        if(po.getString("name").equals(globals.eventName)) {
                            globals.currentEvent = po;
                            fillPeopleList(po);
                        }
                    }
                } else {
                    Log.e("score", "Error: " + e.getMessage());
                }
            }
        });
		/*query.findInBackground(new FindCallback() {
			@Override
		    public void done(List<ParseObject> peopleQuery, ParseException e) {
		        if (e == null) {
		        	for(ParseObject po : peopleQuery) {
		        		if(po.getString("name").equals(globals.eventName)) {
		        			globals.currentEvent = po;
		        			fillPeopleList(po);
		        		}
		        	}
		        } else {
		            Log.e("score", "Error: " + e.getMessage());
		        }
		    }
		});*/
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_viewevent, menu);
		return true;
	}
	
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
    	
    	switch(item.getItemId()) {
    	case R.id.menu_addPerson:
    		//startActivity(new Intent(this, BuildEvent.class));
    		break;
    	case R.id.menu_showMap:
    		refreshQuery();
    		break;
    	default:
    		break;	
    	}

        return true;
    }

}
