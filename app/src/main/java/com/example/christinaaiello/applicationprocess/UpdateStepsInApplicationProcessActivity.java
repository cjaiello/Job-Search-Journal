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
    RelativeLayout initialContactLayout;
    RelativeLayout scheduleInterviewLayout;
    RelativeLayout interviewDocumentationLayout;
    RelativeLayout interviewFollowupLayout;
    RelativeLayout offerResponseLayout;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_steps_in_application_process_activity);
        requestCode = 4;
        TAG = "UpdateStepsInApplicationProcessActivity";
        // Initialize Database objects
        databaseHelper = new DatabaseContract.DatabaseHelper(getApplicationContext());
        db = databaseHelper.getReadableDatabase();

        initialContactLayout = (RelativeLayout) findViewById(R.id.ic_action_add_person_box);
        scheduleInterviewLayout = (RelativeLayout) findViewById(R.id.ic_action_time_box);
        interviewDocumentationLayout = (RelativeLayout) findViewById(R.id.ic_action_chat_box);
        interviewFollowupLayout = (RelativeLayout) findViewById(R.id.ic_action_phone_box);
        offerResponseLayout = (RelativeLayout) findViewById(R.id.ic_action_email_box);

        // Getting bundle information
        Bundle bundle = getIntent().getExtras();
        ID = bundle.getString("ID");
        Log.e(TAG, "Company ID in update is... " + ID);

        seeIfInitialContactDataExists(ID); // Initializing the data, using the company ID
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
            displayInitialContactDataFragment();
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
        final Intent setupIntent = new Intent(UpdateStepsInApplicationProcessActivity.this, SetUpInterviewActivityEditMode.class);
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
    public void seeIfInitialContactDataExists(String companyID) {
        String[] projection = {
                DatabaseContract.InitialContactTable._ID,
                DatabaseContract.InitialContactTable.COLUMN_NAME_COMPANYID,
        };

        // I only want a company whose ID number matches the one passed to me in a bundle
        String[] selectionArgs = {String.valueOf(companyID)};

        // My cursor that I use to loop over query results
        Cursor initialContactCursor = db.query(
                DatabaseContract.InitialContactTable.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                DatabaseContract.InitialContactTable.COLUMN_NAME_COMPANYID + "=?",                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        Cursor setUpInterviewCursor = db.query(
                DatabaseContract.InitialContactTable.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                DatabaseContract.InitialContactTable.COLUMN_NAME_COMPANYID + "=?",                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        if (!(setUpInterviewCursor.getCount() == 0)) {
            Log.i(TAG, "Got results when searching database.");
            displayInitialContactDataFragment(); // Show initial contact info
            displaySetUpInterviewDataFragment(); // Show set up interview info
            // Lastly, we need to hide the unnecessary boxes
            scheduledInterviewFilledOut();
        } else if (!(initialContactCursor.getCount() == 0)) {
            Log.i(TAG, "Got results when searching database.");
            displayInitialContactDataFragment();
            // Lastly, we need to hide the "add initial contact" box
            initialContactFilledOut();
        }else {
            Log.i(TAG, "Could not find matches when searching database.");
            noOptionsFilledOut(); // Hide all options other than the first one
        }

    }

    /**
     * Display our initial contact data
     */
    public void displayInitialContactDataFragment() {
        // If we just edited the initial contact fragment, show it now:
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // Initializing the fragment for initial contact with a company
        InitialContactFragment initialContactFragment = new InitialContactFragment();
        transaction.add(R.id.initial_contact_fragment, initialContactFragment);
        transaction.commit();
    }

    /**
     * Display our initial contact data
     */
    public void displaySetUpInterviewDataFragment() {
        // If we just edited the initial contact fragment, show it now:
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // Initializing the fragment for initial contact with a company
        SetUpInterviewFragment setUpInterviewFragment = new SetUpInterviewFragment();
        transaction.add(R.id.set_up_interview_fragment, setUpInterviewFragment);
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

    /**
     * This method is used to hide the "Add Initial Contact" box when that's already been clicked,
     * and the steps after the next step
     */
    public void initialContactFilledOut() {
        // The first box, the add initial contact box
        initialContactLayout.setVisibility(View.GONE);
        scheduleInterviewLayout.setVisibility(View.VISIBLE);
        interviewDocumentationLayout.setVisibility(View.GONE);
        interviewFollowupLayout.setVisibility(View.GONE);
        offerResponseLayout.setVisibility(View.GONE);
    }

    /**
     * This method is used to hide the "When did you schedule your interview" box when that's already been clicked,
     * and the steps after the next step
     */
    public void scheduledInterviewFilledOut() {
        // The first box, the add initial contact box
        initialContactLayout.setVisibility(View.GONE);
        scheduleInterviewLayout.setVisibility(View.GONE);
        interviewDocumentationLayout.setVisibility(View.VISIBLE);
        interviewFollowupLayout.setVisibility(View.GONE);
        offerResponseLayout.setVisibility(View.GONE);
    }

    /**
     * This method is used to hide the "Document how your interview went" box when that's already been clicked,
     * and the steps after the next step
     */
    public void interviewDocumentationFilledOut() {
        // The first box, the add initial contact box
        initialContactLayout.setVisibility(View.GONE);
        scheduleInterviewLayout.setVisibility(View.GONE);
        interviewDocumentationLayout.setVisibility(View.GONE);
        interviewFollowupLayout.setVisibility(View.VISIBLE);
        offerResponseLayout.setVisibility(View.GONE);
    }

    /**
     * This method is used to hide the "Add Initial Contact" box when that's already been clicked,
     * and the steps after the next step
     */
    public void interviewFollowupFilledOut() {
        // The first box, the add initial contact box
        initialContactLayout.setVisibility(View.GONE);
        scheduleInterviewLayout.setVisibility(View.GONE);
        interviewDocumentationLayout.setVisibility(View.GONE);
        interviewFollowupLayout.setVisibility(View.GONE);
        offerResponseLayout.setVisibility(View.VISIBLE);
    }

    /**
     * This method is used to hide the "Add Initial Contact" box when that's already been clicked,
     * and the steps after the next step
     */
    public void allFilledOut() {
        // The first box, the add initial contact box
        initialContactLayout.setVisibility(View.GONE);
        scheduleInterviewLayout.setVisibility(View.GONE);
        interviewDocumentationLayout.setVisibility(View.GONE);
        interviewFollowupLayout.setVisibility(View.GONE);
        offerResponseLayout.setVisibility(View.GONE);
    }

    /**
     * This method is used to hide all options other than the first,
     * and the steps after the next step
     */
    public void noOptionsFilledOut() {
        // The first box, the add initial contact box
        scheduleInterviewLayout.setVisibility(View.GONE);
        interviewDocumentationLayout.setVisibility(View.GONE);
        interviewFollowupLayout.setVisibility(View.GONE);
        offerResponseLayout.setVisibility(View.GONE);
    }

}
