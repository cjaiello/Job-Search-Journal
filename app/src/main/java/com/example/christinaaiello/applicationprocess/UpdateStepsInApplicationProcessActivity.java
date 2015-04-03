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
import com.example.christinaaiello.general.DatabaseContract;


public class UpdateStepsInApplicationProcessActivity extends ActionBarActivity {
    String step;
    Boolean editing;
    String ID;
    static Integer requestCode;
    String TAG;
    private DatabaseContract.DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    // These are the options that show up at the top of the screen that let you create various events related to interacting with a company
    RelativeLayout initialContactLayout;
    RelativeLayout setUpInterviewLayout;
    RelativeLayout interviewCompletedLayout;
    RelativeLayout interviewFollowupLayout;

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
        setUpInterviewLayout = (RelativeLayout) findViewById(R.id.ic_action_time_box);
        interviewCompletedLayout = (RelativeLayout) findViewById(R.id.ic_action_chat_box);
        interviewFollowupLayout = (RelativeLayout) findViewById(R.id.ic_action_phone_box);

        // Getting bundle information
        Bundle bundle = getIntent().getExtras();
        ID = bundle.getString("ID");
        Log.e(TAG, "Company ID in update is... " + ID);

        seeIfDataExists(ID); // Initializing the data, using the company ID
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
            seeIfDataExists(ID); // Initializing the data, using the company ID
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
     * This method will initalize clicking on the top-of-screen options in this layout,
     * which allow the user to add new "events"
     */

    public void initializeClicking() {

        Log.e("Clicking initialized?", "Yes");

        // Getting the inital contact layout item
        ImageView initialContactImage = (ImageView) initialContactLayout.getChildAt(0);
        TextView initialContactTextView = (TextView) initialContactLayout.getChildAt(1);
        final Intent initialContactIntent = new Intent(UpdateStepsInApplicationProcessActivity.this, InitialContactActivityEditMode.class);
        final Bundle initialContactBundle = new Bundle();
        step = "initial";
        editing = false;
        // Use this name when starting a new activity
        initialContactBundle.putString("Step", step);
        initialContactBundle.putBoolean("Editing", editing);
        initialContactBundle.putString("ID", ID);
        initialContactIntent.putExtras(initialContactBundle);

        initialContactImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creating an intent to start the window
                startActivityForResult(initialContactIntent, requestCode, initialContactBundle);
            }
        });
        initialContactTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creating an intent to start the window
                startActivityForResult(initialContactIntent, requestCode, initialContactBundle);
            }
        });

        // SET UP INTERVIEW: Getting the set up contact layout item
        ImageView setUpContactImage = (ImageView) setUpInterviewLayout.getChildAt(0);
        TextView setUpContactLayoutTextView = (TextView) setUpInterviewLayout.getChildAt(1);
        final Intent setupIntent = new Intent(UpdateStepsInApplicationProcessActivity.this, SetUpInterviewActivityEditMode.class);
        final Bundle setupBundle = new Bundle();
        step = "setup";
        editing = false;
        // Use this name when starting a new activity
        setupBundle.putString("Step", step);
        setupBundle.putBoolean("Editing", editing);
        setupBundle.putString("ID", ID);
        setupIntent.putExtras(setupBundle);
        setUpContactImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creating an intent to start the window
                startActivityForResult(setupIntent, requestCode, setupBundle);
            }
        });
        setUpContactLayoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creating an intent to start the window
                startActivityForResult(setupIntent, requestCode, setupBundle);
            }
        });

        // INTERVIEW COMPLETED: Getting the set up contact layout item
        ImageView interviewCompletedImage = (ImageView) interviewCompletedLayout.getChildAt(0);
        TextView interviewCompletedTextView = (TextView) interviewCompletedLayout.getChildAt(1);
        final Intent interviewCompletedIntent = new Intent(UpdateStepsInApplicationProcessActivity.this, InterviewCompletedActivityEditMode.class);
        final Bundle interviewCompletedBundle = new Bundle();
        step = "completed";
        editing = false;
        // Use this name when starting a new activity
        interviewCompletedBundle.putString("Step", step);
        interviewCompletedBundle.putBoolean("Editing", editing);
        interviewCompletedBundle.putString("ID", ID);
        interviewCompletedIntent.putExtras(interviewCompletedBundle);

        interviewCompletedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creating an intent to start the window
                startActivityForResult(interviewCompletedIntent, requestCode, interviewCompletedBundle);
            }
        });
        interviewCompletedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creating an intent to start the window
                startActivityForResult(interviewCompletedIntent, requestCode, interviewCompletedBundle);
            }
        });

        // RECEIVED RESPONSE: Getting the set up contact layout item
        ImageView receivedResponseImage = (ImageView) interviewFollowupLayout.getChildAt(0);
        TextView receivedResponseTextView = (TextView) interviewFollowupLayout.getChildAt(1);
        final Intent receivedResponseIntent = new Intent(UpdateStepsInApplicationProcessActivity.this, ReceivedResponseActivityEditMode.class);
        final Bundle receivedResponseBundle = new Bundle();
        step = "completed";
        editing = false;
        // Use this name when starting a new activity
        receivedResponseBundle.putString("Step", step);
        receivedResponseBundle.putBoolean("Editing", editing);
        receivedResponseBundle.putString("ID", ID);
        receivedResponseIntent.putExtras(receivedResponseBundle);

        receivedResponseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creating an intent to start the window
                startActivityForResult(receivedResponseIntent, requestCode, receivedResponseBundle);
            }
        });
        receivedResponseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creating an intent to start the window
                startActivityForResult(receivedResponseIntent, requestCode, receivedResponseBundle);
            }
        });
    }

    /**
     * This method will initialize all of the data in the list of steps.
     *
     * @param companyID is the ID of the company in the table
     */
    public void seeIfDataExists(String companyID) {
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
                DatabaseContract.SetUpInterviewTable.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                DatabaseContract.SetUpInterviewTable.COLUMN_NAME_COMPANYID + "=?",                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        Cursor interviewCompletedCursor = db.query(
                DatabaseContract.InterviewCompletedTable.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                DatabaseContract.SetUpInterviewTable.COLUMN_NAME_COMPANYID + "=?",                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        if (!(interviewCompletedCursor.getCount() == 0)) {
            Log.i(TAG, "Got results when searching database.");
            displayInitialContactDataFragment(); // Show initial contact info
            displaySetUpInterviewDataFragment(); // Show set up interview info
            displayInterviewCompletedFragment(); // Show interview completed info
            // Lastly, we need to hide the unnecessary boxes
            interviewDocumentationFilledOut();
        } else if (!(setUpInterviewCursor.getCount() == 0)) {
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
        } else {
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
     * Display our interview completed
     */
    public void displayInterviewCompletedFragment() {
        // If we just edited the initial contact fragment, show it now:
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // Initializing the fragment for initial contact with a company
        InterviewCompletedFragment interviewCompletedFragment = new InterviewCompletedFragment();
        transaction.add(R.id.interview_completed_fragment, interviewCompletedFragment);
        transaction.commit();
    }

    /**
     * This method is used when a user wants to edit initial contact info.
     * This is coded into the XML, which is why it isn't called in this file.
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
     * This method is used when a user wants to edit initial contact info.
     * This is coded into the XML, which is why it isn't called in this file.
     *
     * @param view is the button being clicked
     */
    public void setUpInterviewEditClick(View view) {
        final Intent setUpInterviewIntent = new Intent(UpdateStepsInApplicationProcessActivity.this, SetUpInterviewActivityEditMode.class);

        step = "setup";
        editing = true; // This time, we are editing, not creating something new
        // Use this name when starting a new activity
        final Bundle setUpInterviewBundle = new Bundle();
        setUpInterviewBundle.putString("Step", step);
        setUpInterviewBundle.putBoolean("Editing", editing);
        setUpInterviewBundle.putString("ID", ID);
        setUpInterviewIntent.putExtras(setUpInterviewBundle);
        // Creating an intent to start the window
        startActivityForResult(setUpInterviewIntent, requestCode, setUpInterviewBundle);
    }


    /**
     * This method is used when a user wants to edit interview completed info.
     * This is coded into the XML, which is why it isn't called in this file.
     *
     * @param view is the button being clicked
     */
    public void interviewCompletedEditClick(View view) {
        final Intent interviewCompletedIntent = new Intent(UpdateStepsInApplicationProcessActivity.this, InterviewCompletedActivityEditMode.class);

        step = "completed";
        editing = true; // This time, we are editing, not creating something new
        // Use this name when starting a new activity
        final Bundle interviewCompletedBundle = new Bundle();
        interviewCompletedBundle.putString("Step", step);
        interviewCompletedBundle.putBoolean("Editing", editing);
        interviewCompletedBundle.putString("ID", ID);
        interviewCompletedIntent.putExtras(interviewCompletedBundle);
        // Creating an intent to start the window
        startActivityForResult(interviewCompletedIntent, requestCode, interviewCompletedBundle);
    }

    /**
     * This method is used to hide the "Add Initial Contact" box when that's already been clicked,
     * and the steps after the next step
     */
    public void initialContactFilledOut() {
        // The first box, the add initial contact box
        initialContactLayout.setVisibility(View.GONE);
        setUpInterviewLayout.setVisibility(View.VISIBLE);
        interviewCompletedLayout.setVisibility(View.GONE);
        interviewFollowupLayout.setVisibility(View.GONE);
    }

    /**
     * This method is used to hide the "When did you schedule your interview" box when that's already been clicked,
     * and the steps after the next step
     */
    public void scheduledInterviewFilledOut() {
        // The first box, the add initial contact box
        initialContactLayout.setVisibility(View.GONE);
        setUpInterviewLayout.setVisibility(View.GONE);
        interviewCompletedLayout.setVisibility(View.VISIBLE);
        interviewFollowupLayout.setVisibility(View.GONE);
    }

    /**
     * This method is used to hide the "Document how your interview went" box when that's already been clicked,
     * and the steps after the next step
     */
    public void interviewDocumentationFilledOut() {
        // The first box, the add initial contact box
        initialContactLayout.setVisibility(View.GONE);
        setUpInterviewLayout.setVisibility(View.GONE);
        interviewCompletedLayout.setVisibility(View.GONE);
        interviewFollowupLayout.setVisibility(View.VISIBLE);
    }

    /**
     * This method is used to hide the "Add Initial Contact" box when that's already been clicked,
     * and the steps after the next step
     */
    public void interviewFollowupFilledOut() {
        // The first box, the add initial contact box
        initialContactLayout.setVisibility(View.GONE);
        setUpInterviewLayout.setVisibility(View.GONE);
        interviewCompletedLayout.setVisibility(View.GONE);
        interviewFollowupLayout.setVisibility(View.GONE);
    }

    /**
     * This method is used to hide the "Add Initial Contact" box when that's already been clicked,
     * and the steps after the next step
     */
    public void allFilledOut() {
        // The first box, the add initial contact box
        initialContactLayout.setVisibility(View.GONE);
        setUpInterviewLayout.setVisibility(View.GONE);
        interviewCompletedLayout.setVisibility(View.GONE);
        interviewFollowupLayout.setVisibility(View.GONE);
    }

    /**
     * This method is used to hide all options other than the first,
     * and the steps after the next step
     */
    public void noOptionsFilledOut() {
        // The first box, the add initial contact box
        setUpInterviewLayout.setVisibility(View.GONE);
        interviewCompletedLayout.setVisibility(View.GONE);
        interviewFollowupLayout.setVisibility(View.GONE);
    }

}
