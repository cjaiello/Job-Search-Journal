package com.example.christinaaiello.applicationprocess;

import android.content.ContentValues;
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
    public static Integer interviewNumber;
    String step;
    Boolean editing;
    String ID;
    static Integer requestCode;
    String TAG;
    private SQLiteDatabase db;
    // These are the options that show up at the top of the screen that let you create various events related to interacting with a company
    RelativeLayout initialContactLayout;
    RelativeLayout setUpInterviewLayout;
    RelativeLayout interviewOneCompletedLayout;
    RelativeLayout interviewTwoCompletedLayout;
    RelativeLayout interviewFollowupLayout;
    Bundle updateStepsBundle;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_steps_in_application_process_activity);
        requestCode = 4;
        TAG = "UpdateStepsInApplicationProcessActivity";
        // Initialize Database objects
        DatabaseContract.DatabaseHelper databaseHelper = new DatabaseContract.DatabaseHelper(getApplicationContext());
        db = databaseHelper.getReadableDatabase();

        // Getting bundle information
        updateStepsBundle = getIntent().getExtras();
        ID = updateStepsBundle.getString("ID");
        interviewNumber = 0;

        getLayoutItemsOnScreen(); // Getting the items on the screen and putting them into variables
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
        Log.i("Activity result", "Activity result");

        if (aRequestCode == requestCode) {
            Log.i("Activity result", "Got correct result code for initialcontact");
            seeIfDataExists(ID); // Initializing the data, using the company ID
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method will initalize clicking on the top-of-screen options in this layout,
     * which allow the user to add new "events"
     */

    public void initializeClicking() {

        Log.i(TAG + "Clicking initialized?", "Yes");

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
        // This will let the user edit this section:
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
        ImageView interviewOneCompletedImage = (ImageView) interviewOneCompletedLayout.getChildAt(0);
        TextView interviewOneCompletedTextView = (TextView) interviewOneCompletedLayout.getChildAt(1);
        ImageView interviewTwoCompletedImage = (ImageView) interviewTwoCompletedLayout.getChildAt(0);
        TextView interviewTwoCompletedTextView = (TextView) interviewTwoCompletedLayout.getChildAt(1);
        // This will let the user edit this section:
        final Intent interviewCompletedIntent = new Intent(UpdateStepsInApplicationProcessActivity.this, InterviewCompletedActivityEditMode.class);
        final Bundle interviewCompletedBundle = new Bundle();
        step = "completed";
        editing = false;
        // Use this name when starting a new activity
        interviewCompletedBundle.putString("Step", step);
        interviewCompletedBundle.putBoolean("Editing", editing);
        interviewCompletedBundle.putString("ID", ID);

        interviewOneCompletedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interviewCompletedBundle.putInt("interviewNumber", 1);
                interviewCompletedIntent.putExtras(interviewCompletedBundle);
                // Creating an intent to start the window
                startActivityForResult(interviewCompletedIntent, requestCode, interviewCompletedBundle);
            }
        });
        interviewOneCompletedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interviewCompletedBundle.putInt("interviewNumber", 1);
                interviewCompletedIntent.putExtras(interviewCompletedBundle);
                // Creating an intent to start the window
                startActivityForResult(interviewCompletedIntent, requestCode, interviewCompletedBundle);
            }
        });
        interviewTwoCompletedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interviewCompletedBundle.putInt("interviewNumber", 2);
                interviewCompletedIntent.putExtras(interviewCompletedBundle);
                // Creating an intent to start the window
                startActivityForResult(interviewCompletedIntent, requestCode, interviewCompletedBundle);
            }
        });
        interviewTwoCompletedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interviewCompletedBundle.putInt("interviewNumber", 2);
                interviewCompletedIntent.putExtras(interviewCompletedBundle);
                // Creating an intent to start the window
                startActivityForResult(interviewCompletedIntent, requestCode, interviewCompletedBundle);
            }
        });

        // RECEIVED RESPONSE: Getting the set up contact layout item
        ImageView receivedResponseImage = (ImageView) interviewFollowupLayout.getChildAt(0);
        TextView receivedResponseTextView = (TextView) interviewFollowupLayout.getChildAt(1);
        // This will let the user edit this section:
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
        String mostRecentStep; // This contains the most recent step taken with a company
        String[] projection = {
                DatabaseContract.InitialContactTable._ID,
                DatabaseContract.InitialContactTable.COLUMN_NAME_COMPANYID,
        };

        // I only want a company whose ID number matches the one passed to me in a bundle
        String[] selectionArgs = {String.valueOf(companyID)};

        // Get any information about initial contact
        Cursor initialContactCursor = db.query(
                DatabaseContract.InitialContactTable.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                DatabaseContract.InitialContactTable.COLUMN_NAME_COMPANYID + "=?",                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        // Get any information about setting up an interview
        Cursor setUpInterviewCursor = db.query(
                DatabaseContract.SetUpInterviewTable.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                DatabaseContract.SetUpInterviewTable.COLUMN_NAME_COMPANYID + "=?",                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        // Get any information about completing an interview
        Cursor interviewCompletedCursor = db.query(
                DatabaseContract.InterviewCompletedTable.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                DatabaseContract.InterviewCompletedTable.COLUMN_NAME_COMPANYID + "=?",                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        // Get any information about receiving a response after an interview
        Cursor receivedResponseToInterviewCursor = db.query(
                DatabaseContract.ReceivedResponseTable.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                DatabaseContract.ReceivedResponseTable.COLUMN_NAME_COMPANYID + "=?",                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        // Seeing if the user has started recording the interview process:
        if (!(initialContactCursor.getCount() == 0)) {
            Log.i(TAG, "Got results when searching database - Initially Contacted Company");
            mostRecentStep = "Initially Contacted Company";
            displayInitialContactDataFragment();
            // Lastly, we need to hide the "add initial contact" box
            initialContactFilledOut();
        } else {
            mostRecentStep = "Not started";
            Log.i(TAG, "Could not find matches when searching database.");
            noOptionsFilledOut(); // Hide all options other than the first one
        }

        // Have they recorded setting up an interview?
        if (!(setUpInterviewCursor.getCount() == 0)) {
            Log.i(TAG, "Got results when searching database - Set Up Interview");
            displaySetUpInterviewDataFragment(); // Show set up interview info
            mostRecentStep = "Set Up Interview";
            // Lastly, we need to hide the unnecessary boxes
            scheduledInterviewFilledOut();
        }

        // Have they recorded having completed an interview?
        if (!(interviewCompletedCursor.getCount() == 0)) {
            Log.e("Count was", Integer.toString(interviewCompletedCursor.getCount()));
            if ((interviewCompletedCursor.getCount() == 2)) {
                Log.i(TAG, "Got results when searching database - Completed Interview");
                Log.e("Count", "Count was still two");
                interviewNumber = 2; // Used to show/hide certain buttons on screen
                Log.e("Number is", interviewNumber.toString());
                displayInterviewTwoCompletedFragment(); // If they've done two interviews
            } else interviewNumber = 1; // Used to show/hide certain buttons on screen
            displayInterviewOneCompletedFragment(); // Show interview completed info
            mostRecentStep = "Completed Interview";
            // Lastly, we need to hide the unnecessary boxes
            interviewDocumentationFilledOut();
        }

        // Have they recorded receiving a response?
        if (!(receivedResponseToInterviewCursor.getCount() == 0)) {
            // This is called if everything has been filled out
            Log.i(TAG, "Got results when searching database - Received Response After Interview");
            displayReceivedResponseFragment(); // Showing response to interview
            mostRecentStep = "Received Response After Interview";
            allFilledOut();
        }

        // Lastly, we now will mark the column in the database saying what the most recent step
        // that has been taken with this company
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.CompanyDataTable.COLUMN_NAME_STEP, mostRecentStep);

        // Updating the row, returning the primary key value of the new row
        String strFilter = "_id=" + companyID;

        // Update the column in the main company table to say what the most recent step is
        db.update(
                DatabaseContract.CompanyDataTable.TABLE_NAME,
                values,
                strFilter,
                null);

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
    public void displayInterviewOneCompletedFragment() {
        Bundle bundle = new Bundle();
        bundle.putInt("Number", 1);
        // If we just edited the initial contact fragment, show it now:
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // Initializing the fragment for initial contact with a company
        InterviewCompletedFragment interviewCompletedFragment = new InterviewCompletedFragment();
        interviewCompletedFragment.setArguments(bundle);
        transaction.add(R.id.interview_one_completed_fragment, interviewCompletedFragment);
        transaction.commit();
    }

    /**
     * Display our interview completed
     */
    public void displayInterviewTwoCompletedFragment() {
        Bundle bundle = new Bundle();
        bundle.putInt("Number", 2);
        // If we just edited the initial contact fragment, show it now:
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // Initializing the fragment for initial contact with a company
        InterviewCompletedFragment interviewCompletedFragment = new InterviewCompletedFragment();
        interviewCompletedFragment.setArguments(bundle);
        transaction.add(R.id.interview_two_completed_fragment, interviewCompletedFragment);
        transaction.commit();
    }

    /**
     * Display that we received a response
     */
    public void displayReceivedResponseFragment() {
        // If we just edited the initial contact fragment, show it now:
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // Initializing the fragment for initial contact with a company
        ReceivedResponseFragment receivedResponseFragment = new ReceivedResponseFragment();
        transaction.add(R.id.received_response_fragment, receivedResponseFragment);
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
        editing = true; // This time, we are editing, not creating something new
        // Use this name when starting a new activity
        final Bundle initialContactBundle = new Bundle();
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
        editing = true; // This time, we are editing, not creating something new
        // Use this name when starting a new activity
        final Bundle setUpInterviewBundle = new Bundle();
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
        editing = true; // This time, we are editing, not creating something new
        // Use this name when starting a new activity
        final Bundle interviewCompletedBundle = new Bundle();
        interviewCompletedBundle.putBoolean("Editing", editing);
        interviewCompletedBundle.putString("ID", ID);
        if (view.getParent().getParent().getParent() == findViewById(R.id.interview_two_completed_fragment)) {
            interviewCompletedBundle.putInt("interviewNumber", 2);
        } else interviewCompletedBundle.putInt("interviewNumber", 1);
        interviewCompletedIntent.putExtras(interviewCompletedBundle);
        // Creating an intent to start the window
        startActivityForResult(interviewCompletedIntent, requestCode, interviewCompletedBundle);
    }

    /**
     * This method is used when a user wants to edit received response.
     * This is coded into the XML, which is why it isn't called in this file.
     *
     * @param view is the button being clicked
     */
    public void receivedResponseEditClick(View view) {
        final Intent receivedResponseIntent = new Intent(UpdateStepsInApplicationProcessActivity.this, ReceivedResponseActivityEditMode.class);
        editing = true; // This time, we are editing, not creating something new
        // Use this name when starting a new activity
        final Bundle receivedResponseBundle = new Bundle();
        receivedResponseBundle.putBoolean("Editing", editing);
        receivedResponseBundle.putString("ID", ID);
        receivedResponseIntent.putExtras(receivedResponseBundle);
        // Creating an intent to start the window
        startActivityForResult(receivedResponseIntent, requestCode, receivedResponseBundle);
    }

    /**
     * This method is used to hide the "Add Initial Contact" box when that's already been clicked,
     * and the steps after the next step
     */
    public void initialContactFilledOut() {
        // The first box, the add initial contact box
        initialContactLayout.setVisibility(View.GONE);
        setUpInterviewLayout.setVisibility(View.VISIBLE);
        interviewOneCompletedLayout.setVisibility(View.GONE);
        interviewTwoCompletedLayout.setVisibility(View.GONE);
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
        interviewOneCompletedLayout.setVisibility(View.VISIBLE);
        interviewTwoCompletedLayout.setVisibility(View.GONE);
        interviewFollowupLayout.setVisibility(View.GONE);
    }

    /**
     * This method is used to hide the "Document how your interview went" box when that's already been clicked,
     * and the steps after the next step
     */
    public void interviewDocumentationFilledOut() {
        initialContactLayout.setVisibility(View.GONE);
        setUpInterviewLayout.setVisibility(View.GONE);
        interviewOneCompletedLayout.setVisibility(View.GONE);
        // If they've only done one interview so far
        if (interviewNumber == 1) {
            // Two options: second interview, or write about followup:
            interviewTwoCompletedLayout.setVisibility(View.VISIBLE);
        } else interviewTwoCompletedLayout.setVisibility(View.GONE);
        interviewFollowupLayout.setVisibility(View.VISIBLE);
    }

    /**
     * This method is used to hide the "Add Initial Contact" box when that's already been clicked,
     * and the steps after the next step
     */
    public void allFilledOut() {
        initialContactLayout.setVisibility(View.GONE);
        setUpInterviewLayout.setVisibility(View.GONE);
        interviewOneCompletedLayout.setVisibility(View.GONE);
        interviewTwoCompletedLayout.setVisibility(View.GONE);
        interviewFollowupLayout.setVisibility(View.GONE);
        // Hiding the text that says "Next steps"
        TextView nextStepText = (TextView) findViewById(R.id.next_step_text);
        nextStepText.setVisibility(View.GONE);
    }

    /**
     * This method is used to hide all options other than the first,
     * and the steps after the next step
     */
    public void noOptionsFilledOut() {
        setUpInterviewLayout.setVisibility(View.GONE);
        interviewOneCompletedLayout.setVisibility(View.GONE);
        interviewTwoCompletedLayout.setVisibility(View.GONE);
        interviewFollowupLayout.setVisibility(View.GONE);
    }

    /**
     * This method will get all of the various layout items on the screen for us.
     */
    public void getLayoutItemsOnScreen() {
        initialContactLayout = (RelativeLayout) findViewById(R.id.ic_action_add_person_box);
        setUpInterviewLayout = (RelativeLayout) findViewById(R.id.ic_action_time_box);
        interviewOneCompletedLayout = (RelativeLayout) findViewById(R.id.ic_action_chat_box);
        interviewTwoCompletedLayout = (RelativeLayout) findViewById(R.id.ic_action_chat_box_two);
        interviewFollowupLayout = (RelativeLayout) findViewById(R.id.ic_action_phone_box);
    }


}
