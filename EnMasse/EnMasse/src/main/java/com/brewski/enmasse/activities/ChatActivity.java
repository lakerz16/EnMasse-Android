package com.brewski.enmasse.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.brewski.enmasse.R;

public class ChatActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getActionBar().setDisplayUseLogoEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        overridePendingTransition(R.anim.vine_right_left, R.anim.vine_pause_scale);
    }

    @Override
    public void finish() {
        super.finish();
        //overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
        overridePendingTransition(R.anim.vine_resume_scale, R.anim.vine_left_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_settings:
                return true;
            default:
                break;
        }

        return true;
    }
}
