package com.example.christinaaiello.applicationprocess;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.example.christinaaiello.employerinformation.DatabaseContract;


public class UpdateStepsInApplicationProcessActivity extends ActionBarActivity {
    String step;
    Boolean editing;
    String ID;
    static Integer requestCode;
    String TAG;
    private DatabaseContract.DatabaseHelper databaseHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_steps_in_application_process_activity);
        requestCode = 4;
        TAG = "UpdateStepsInApplicationProcessActivity";
        // Initialize Database objects
        databaseHelper = new DatabaseContract.DatabaseHelper(getApplicationContext());
        db = databaseHelper.getReadableDatabase();

        // Getting bundle information
        Bundle bundle = getIntent().getExtras();
        ID = bundle.getString("ID");

        displayData(ID); // Initializing the data, using the company ID
        initializeClicking(); // Initialize the bundle to be used for edit mode, and the onclicks
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.update_steps, menu);
        return true;
    }

    @Override
    public void onActivityResult(int aRequestCode, int resultCode, Intent data) {
        super.onActivityResult(aRequestCode, resultCode, data);
        Log.e("Activity result", "Activity result");

        if (aRequestCode == requestCode) {
            Log.e("Activity result", "Got correct result code for initialcontact");
            displayInitialContactData();
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

    /**
     * This method will initialize all of the data in the list of steps.
     *
     * @param companyID is the ID of the company in the table
     */
    public void displayData(String companyID) {
        String[] projection = {
                DatabaseContract.InitialContactTable._ID,
                DatabaseContract.InitialContactTable.COLUMN_NAME_COMPANYID,
        };

        // I only want a company whose ID number matches the one passed to me in a bundle
        String[] selectionArgs = {String.valueOf(companyID)};

        // My cursor that I use to loop over query results
        Cursor cursor = db.query(
                DatabaseContract.InitialContactTable.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                DatabaseContract.InitialContactTable._ID + "=?",                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        if (!(cursor.getCount() == 0)) {
            Log.i(TAG, "Got results when searching database.");
            // Since we have had initial contact with this company, show the data on the screen.
            displayInitialContactData();
        } else {
            Log.i(TAG, "Could not find matches when searching database.");
            // We won't show any data, because we don't have it.
        }

    }

    /**
     * Display our initial contact data
     */
    public void displayInitialContactData() {
        // If we just edited the initial contact fragment, show it now:
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // Initializing the fragment for initial contact with a company
        InitialContactFragment initialContactFragment = new InitialContactFragment();
        transaction.add(R.id.initial_contact_fragment, initialContactFragment);
        transaction.commit();
    }

    /**
     * This method is used when a user wants to edit initial contact info
     *
     * @param view is the button being clicked
     */
    public void initialContactEditClick(View view) {
        final Intent initialContactIntent = new Intent(UpdateStepsInApplicationProcessActivity.this, InitialContactActivityEditMode.class);

        step = "initial";
        editing = true; // This time, we are editing, not creating something new
        // Use this name when starting a new activity
        final Bundle initialContactBundle = new Bundle();
        initialContactBundle.putString("Step", step);
        initialContactBundle.putBoolean("Editing", editing);
        initialContactBundle.putString("ID", ID);
        initialContactIntent.putExtras(initialContactBundle);
        // Creating an intent to start the window
        startActivityForResult(initialContactIntent, requestCode, initialContactBundle);
    }

}
