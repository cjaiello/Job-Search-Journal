package com.example.christinaaiello.applicationprocess;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.christinaaiello.R;


public class UpdateStepsInApplicationProcessActivity extends ActionBarActivity {
    String step;
    Boolean editing;
    String ID;
    static Integer requestCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_steps_in_application_process_activity);
        requestCode = 4;

        // Getting bundle information
        Bundle bundle = getIntent().getExtras();
        ID = bundle.getString("ID");

        initializeClicking(); // Initialize the bundle to be used for edit mode
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("Activity result", "Activity result");

        if (requestCode == requestCode) {
            Log.e("Activity result", "Got correct result code");
            // If we just edited the initial contact fragment, show it now:
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            // Initializing the fragment for initial contact with a company
            InitialContactFragment initialContactFragment = new InitialContactFragment();
            transaction.add(R.id.initial_contact_fragment, initialContactFragment);
            transaction.commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method will initalize the fragments in this layout
     */

    public void initializeClicking() {

        Log.e("Clicking initialized?", "Yes");

        // Getting the inital contact layout item
        RelativeLayout initialContactLayout = (RelativeLayout) findViewById(R.id.ic_action_add_person_box);
        ImageView initialContactImage = (ImageView) initialContactLayout.getChildAt(0);
        TextView initialContactTextView = (TextView) initialContactLayout.getChildAt(1);
        final Intent initialContactIntent = new Intent(UpdateStepsInApplicationProcessActivity.this, InitialContactActivityEditMode.class);
        final Bundle initialContactBundle = new Bundle();

        initialContactImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                step = "initial";
                editing = false;
                // Use this name when starting a new activity
                initialContactBundle.putString("Step", step);
                initialContactBundle.putBoolean("Editing", editing);
                initialContactBundle.putString("ID", ID);
                initialContactIntent.putExtras(initialContactBundle);
                // Creating an intent to start the window
                startActivityForResult(initialContactIntent, requestCode, initialContactBundle);
            }
        });
        initialContactTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                step = "initial";
                editing = false;
                // Use this name when starting a new activity
                initialContactBundle.putString("Step", step);
                initialContactBundle.putBoolean("Editing", editing);
                initialContactBundle.putString("ID", ID);
                initialContactIntent.putExtras(initialContactBundle);
                // Creating an intent to start the window
                startActivityForResult(initialContactIntent, requestCode, initialContactBundle);
            }
        });

        // Getting the set up contact layout item
        RelativeLayout setUpContactLayout = (RelativeLayout) findViewById(R.id.ic_action_time_box);
        ImageView setUpContactImage = (ImageView) setUpContactLayout.getChildAt(0);
        TextView setUpContactLayoutTextView = (TextView) setUpContactLayout.getChildAt(1);
        final Intent setupIntent = new Intent(UpdateStepsInApplicationProcessActivity.this, InitialContactActivityEditMode.class);
        final Bundle setupBundle = new Bundle();
        setUpContactImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                step = "setup";
                editing = false;
                // Use this name when starting a new activity
                setupBundle.putString("Step", step);
                setupBundle.putBoolean("Editing", editing);
                setupBundle.putString("ID", ID);
                setupIntent.putExtras(setupBundle);
                // Creating an intent to start the window
                startActivityForResult(setupIntent, requestCode, initialContactBundle);
            }
        });
        setUpContactLayoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                step = "setup";
                editing = false;
                // Use this name when starting a new activity
                setupBundle.putString("Step", step);
                setupBundle.putBoolean("Editing", editing);
                setupBundle.putString("ID", ID);
                setupIntent.putExtras(setupBundle);
                // Creating an intent to start the window
                startActivityForResult(setupIntent, requestCode, initialContactBundle);
            }
        });


    }
}
