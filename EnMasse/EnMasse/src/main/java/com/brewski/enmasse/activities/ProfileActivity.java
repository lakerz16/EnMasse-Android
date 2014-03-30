package com.brewski.enmasse.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.brewski.enmasse.R;
import com.brewski.enmasse.models.DeveloperProfile;
import com.brewski.enmasse.util.Utilities;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProfileActivity extends Activity {

    ArrayList<DeveloperProfile> developerProfiles;
    ListView profileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        developerProfiles = new ArrayList<DeveloperProfile>();

        profileList = (ListView) findViewById(R.id.user_list);

        getActionBar().setDisplayUseLogoEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Developer Profile</font>"));

        profileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                Utilities.SetDeveloperProfile(ProfileActivity.this, developerProfiles.get(position));
                Toast.makeText(ProfileActivity.this, developerProfiles.get(position).getName(), Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        });

        //overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
        overridePendingTransition(R.anim.vine_right_left, R.anim.vine_pause_scale);
    }

    @Override
    public void onResume() {
        super.onResume();

        refreshProfileList();
    }

    private void refreshProfileList() {

        ParseQuery query = new ParseQuery("DeveloperProfiles");
        query.findInBackground(new FindCallback() {
            @Override
            public void done(List profileQuery, ParseException e) {
                if (e == null) {

                    developerProfiles.clear();
                    for (ParseObject p : (ArrayList<ParseObject>) profileQuery) {
                        developerProfiles.add(new DeveloperProfile(p));
                    }

                    List<String> developerNames = new ArrayList<String>();
                    for(DeveloperProfile dp : developerProfiles) {
                        developerNames.add(dp.getName());
                    }

                    final TextAdapter adapter = new TextAdapter(ProfileActivity.this, developerNames);
                    profileList.setAdapter(adapter);

                } else {
                    Log.e("developer", "Error: " + e.getMessage());
                }

                //mPullToRefreshAttacher.setRefreshComplete();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //if (id == R.id.action_settings) {
        //    return true;
        //}
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        //overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
        overridePendingTransition(R.anim.vine_resume_scale, R.anim.vine_left_right);
    }

    private class TextAdapter extends BaseAdapter {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        List<String> features;

        private Context context;

        public TextAdapter(Context context, List<String> objects) {
            //super(context, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
            features = objects;
            this.context = context;
        }

        @Override
        public int getCount() {
            return features.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View vi=convertView;
            if(convertView==null) {
                vi = inflater.inflate(R.layout.developer_list, null);
            }

            TextView text = (TextView)vi.findViewById(R.id.developer_name);
            text.setText(features.get(position));

            return vi;
        }
    }

}
