package com.example.jobsearchjournal.applicationprocess;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jobsearchjournal.R;
import com.example.jobsearchjournal.general.DatabaseContract;

import java.util.ArrayList;


public class TrackYourProgressActivity extends AppCompatActivity {
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
    RelativeLayout interviewFollowupLayout;
    Bundle updateStepsBundle;
    ArrayList<Interview> listOfInterviews;
    InterviewAdapter adapter;

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
        //Log.i(TAG, "Company ID inside update is: " + ID);
        interviewNumber = 0;

        getLayoutItemsOnScreen(); // Getting the items on the screen and putting them into variables
        seeIfDataExists(ID); // Initializing the data, using the company companyID
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
        //Log.i("Activity result", "Activity result");

        if (aRequestCode == requestCode) {
            //Log.i("Activity result", "Got correct result code for initialcontact");
            seeIfDataExists(ID); // Initializing the data, using the company companyID
        }

        // First we clear the list of companies that the adapter is using:
        listOfInterviews.clear();
        // Now we update the list of companies:
        listOfInterviews.addAll(getAllInterviews());
        // And lastly we tell the adapter to get new data:
        adapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren((ListView) findViewById(R.id.listview));
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
     * This method will initialize clicking on the top-of-screen options in this layout,
     * which allow the user to add new "events"
     */

    public void initializeClicking() {
        // Getting the initial contact layout item
        ImageView initialContactImage = (ImageView) initialContactLayout.getChildAt(0);
        TextView initialContactTextView = (TextView) initialContactLayout.getChildAt(1);
        final Intent initialContactIntent = new Intent(TrackYourProgressActivity.this, InitialContactActivityEditMode.class);
        final Bundle initialContactBundle = new Bundle();
        step = "initial";
        editing = false;
        // Use this name when starting a new activity
        initialContactBundle.putString("Step", step);
        initialContactBundle.putBoolean("Editing", editing);
        initialContactBundle.putString("companyID", ID);
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
        final Intent setupIntent = new Intent(TrackYourProgressActivity.this, InterviewActivityEditMode.class);
        final Bundle setupBundle = new Bundle();
        step = "setup";
        editing = false;
        // Use this name when starting a new activity
        setupBundle.putString("Step", step);
        setupBundle.putBoolean("Editing", editing);
        setupBundle.putString("companyID", ID);
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

        // RECEIVED RESPONSE: Getting the set up contact layout item
        ImageView receivedResponseImage = (ImageView) interviewFollowupLayout.getChildAt(0);
        TextView receivedResponseTextView = (TextView) interviewFollowupLayout.getChildAt(1);
        // This will let the user edit this section:
        final Intent receivedResponseIntent = new Intent(TrackYourProgressActivity.this, ReceivedResponseActivityEditMode.class);
        final Bundle receivedResponseBundle = new Bundle();
        step = "completed";
        editing = false;
        // Use this name when starting a new activity
        receivedResponseBundle.putString("Step", step);
        receivedResponseBundle.putBoolean("Editing", editing);
        receivedResponseBundle.putString("companyID", ID);
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
     * @param companyID is the companyID of the company in the table
     */
    public void seeIfDataExists(String companyID) {
        String mostRecentStep; // This contains the most recent step taken with a company
        String[] projection = {
                DatabaseContract.InitialContactTable._ID,
                DatabaseContract.InitialContactTable.COLUMN_NAME_COMPANYID,
        };

        // I only want a company whose companyID number matches the one passed to me in a bundle
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

        // This contains the list of companies
        ListView listView = (ListView) findViewById(R.id.listview);

        // Adapter for the list of companies
        // This will display on the screen all of the interviews someone has had
        listOfInterviews = getAllInterviews();
        adapter = new InterviewAdapter(listOfInterviews, this);
        listView.setAdapter(adapter);
        setListViewHeightBasedOnChildren((ListView) findViewById(R.id.listview));

        // Seeing if the user has started recording the interview process:
        if (!(receivedResponseToInterviewCursor.getCount() == 0)) {
            // This is called if everything has been filled out
            //Log.i(TAG, "Got results when searching database - Received Response After Interview");
            displayReceivedResponseFragment(); // Showing response to interview
            mostRecentStep = "Received Response After Interview";
            displayInitialContactDataFragment(); // Show info from initial contact
            contactWithCompanyCompleted(); // Hide all boxes
        } else if (listOfInterviews.size() > 0) {
            // Display how many interviews have happened
            mostRecentStep = listOfInterviews.size() > 1 ? listOfInterviews.size() + " Interviews Recorded" : "1 Interview Recorded";
            // Show option to either add another interview or report results back
            displayInitialContactDataFragment(); // Show info from initial contact
            completedAtLeastOneInterview(); // Now the user can potentially record followup information
            // Have they recorded receiving a response?
        } else if (!(initialContactCursor.getCount() == 0)) {
            //Log.i(TAG, "Got results when searching database - Initially Contacted Company");
            mostRecentStep = "Initially Contacted Company";
            displayInitialContactDataFragment(); // Show info from initial contact
            // Lastly, we need to hide the "add initial contact" box
        } else {
            mostRecentStep = "Not Started";
            //Log.i(TAG, "Could not find initial contact matches when searching database.");
            userHastNotStarted(); // Hiding unnecessary items until the user will actually use them
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
        initialContactCompleted(); // Hide the box to add initial contact now
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
        final Intent initialContactIntent = new Intent(TrackYourProgressActivity.this, InitialContactActivityEditMode.class);
        editing = true; // This time, we are editing, not creating something new
        // Use this name when starting a new activity
        final Bundle initialContactBundle = new Bundle();
        initialContactBundle.putBoolean("Editing", editing);
        initialContactBundle.putString("companyID", ID);
        initialContactIntent.putExtras(initialContactBundle);
        // Creating an intent to start the window
        startActivityForResult(initialContactIntent, requestCode, initialContactBundle);
    }

    /**
     * This method is used when a user wants to edit received response.
     * This is coded into the XML, which is why it isn't called in this file.
     *
     * @param view is the button being clicked
     */
    public void receivedResponseEditClick(View view) {
        final Intent receivedResponseIntent = new Intent(TrackYourProgressActivity.this, ReceivedResponseActivityEditMode.class);
        editing = true; // This time, we are editing, not creating something new
        // Use this name when starting a new activity
        final Bundle receivedResponseBundle = new Bundle();
        receivedResponseBundle.putBoolean("Editing", editing);
        receivedResponseBundle.putString("companyID", ID);
        receivedResponseIntent.putExtras(receivedResponseBundle);
        // Creating an intent to start the window
        startActivityForResult(receivedResponseIntent, requestCode, receivedResponseBundle);
    }

    /**
     * This method will get all of the various layout items on the screen for us.
     */
    public void getLayoutItemsOnScreen() {
        initialContactLayout = (RelativeLayout) findViewById(R.id.ic_action_add_person_box);
        setUpInterviewLayout = (RelativeLayout) findViewById(R.id.ic_action_time_box);
        interviewFollowupLayout = (RelativeLayout) findViewById(R.id.ic_action_phone_box);
    }

    /**
     * This method will get all interviews from the database.
     */
    public ArrayList<Interview> getAllInterviews() {
        // This will contain the names of all of the companies
        ArrayList<Interview> interviewList = new ArrayList<Interview>();

        // Writing what columns we want from the table
        String[] projection = {
                DatabaseContract.SetUpInterviewTable._ID,
                DatabaseContract.SetUpInterviewTable.COLUMN_NAME_COMPANYID,
                DatabaseContract.SetUpInterviewTable.COLUMN_NAME_DATE,
                DatabaseContract.SetUpInterviewTable.COLUMN_NAME_TIME,
                DatabaseContract.SetUpInterviewTable.COLUMN_NAME_INTERVIEWERS,
                DatabaseContract.SetUpInterviewTable.COLUMN_NAME_EMAIL,
                DatabaseContract.SetUpInterviewTable.COLUMN_NAME_MISC_NOTES,
                DatabaseContract.SetUpInterviewTable.COLUMN_NAME_FOLLOWUP_NOTES
        };

        // Organize interview names in this order...
        String sortOrder = DatabaseContract.SetUpInterviewTable._ID + " ASC";
        // Selection arguments (using the company's id)
        String[] selectionArgs = {String.valueOf(ID)};

        // My cursor that I use to loop over query results
        Cursor cursor = db.query(
                DatabaseContract.SetUpInterviewTable.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                DatabaseContract.SetUpInterviewTable.COLUMN_NAME_COMPANYID + "=?",// The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // Thcce sort order
        );

        // Now, we make an employer object and put each row's data into it.
        // We then insert this into the ArrayList and move to the next row.
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            Interview interview = new Interview();
            interview.setInterviewID(cursor.getString(0));
            interview.setCompanyID(cursor.getString(1));
            interview.setDate(cursor.getString(2));
            interview.setTime(cursor.getString(3));
            interview.setInterviewerNames(cursor.getString(4));
            interview.setContactEmailAddress(cursor.getString(5));
            interview.setMiscNotes(cursor.getString(6));
            interview.setInterviewCompletedNotes(cursor.getString(7));
            interviewList.add(interview);
            cursor.moveToNext();
        }

        return interviewList;
    }

    /**
     * This method lets a user edit a scheduled interview's information.
     */
    public void setUpInterviewEditClick(View view) {
        // Relativelayout that this button is in:
        RelativeLayout relativeLayout = (RelativeLayout) view.getParent();
        TextView interviewIDText = (TextView) relativeLayout.getChildAt(3);
        // Lastly, this is the actual interview's ID number:
        String interviewID = interviewIDText.getText().toString();

        final Intent setUpInterviewIntent = new Intent(TrackYourProgressActivity.this, InterviewActivityEditMode.class);
        editing = true; // This time, we are editing, not creating something new
        // Use this name when starting a new activity
        final Bundle setUpInterviewBundle = new Bundle();
        setUpInterviewBundle.putBoolean("Editing", editing);
        setUpInterviewBundle.putString("companyID", ID);
        setUpInterviewBundle.putString("interviewID", interviewID);
        setUpInterviewIntent.putExtras(setUpInterviewBundle);
        // Creating an intent to start the window
        startActivityForResult(setUpInterviewIntent, requestCode, setUpInterviewBundle);
    }

    /**
     * This method is used to let me put a scrollivew inside of a listview.
     * Credit for this code goes to Nex:
     * http://nex-otaku-en.blogspot.com/2010/12/android-put-listview-in-scrollview.html
     * Thank you, Nex!!
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    /**
     * Once the user has initially contacted a company, they don't need to see that button again!
     */
    public void initialContactCompleted() {
        initialContactLayout.setVisibility(View.GONE);
        setUpInterviewLayout.setVisibility(View.VISIBLE);
        interviewFollowupLayout.setVisibility(View.GONE);
    }

    /**
     * Once the user has had at least one interview, display option to add more interviews or report results
     */
    public void completedAtLeastOneInterview() {
        initialContactLayout.setVisibility(View.GONE);
        setUpInterviewLayout.setVisibility(View.VISIBLE);
        interviewFollowupLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Once the user has gotten an offer (or a rejection), they don't need to see any of these buttons again!
     */
    public void contactWithCompanyCompleted() {
        initialContactLayout.setVisibility(View.GONE);
        setUpInterviewLayout.setVisibility(View.GONE);
        interviewFollowupLayout.setVisibility(View.GONE);
    }

    /**
     * User has not yet started tracking progress, so hide the necessary items.
     */
    public void userHastNotStarted() {
        initialContactLayout.setVisibility(View.VISIBLE);
        setUpInterviewLayout.setVisibility(View.GONE);
        interviewFollowupLayout.setVisibility(View.GONE);
    }


    /**
     * This method lets a user delete an interview
     *
     * @param view is the view a user clicked on
     */
    public void deleteInterview(final View view) {
        // Making a dialog box that will pop up for the user
        AlertDialog.Builder builder = new AlertDialog.Builder(TrackYourProgressActivity.this);
        builder.setTitle("Delete Interview?");
        builder.setMessage("Are you sure you want to delete this interview?");
        builder.setIcon(R.drawable.ic_launcher);
        builder.setPositiveButton("Yes, Delete It", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                // Getting the RelativeLayout holding this trash can and other things
                RelativeLayout parentView = (RelativeLayout) view.getParent();
                // Getting the textview holding the interview's ID number
                TextView idView = (TextView) parentView.getChildAt(3);
                // This is the actual ID number
                String idNumber = idView.getText().toString();
                // Deleting from the database:
                db.delete(DatabaseContract.SetUpInterviewTable.TABLE_NAME, DatabaseContract.SetUpInterviewTable._ID + "=?", new String[]{idNumber});
                // And refreshing the layout:
                // First we clear the list of companies that the adapter is using:
                listOfInterviews.clear();
                // Now we update the list of companies:
                listOfInterviews.addAll(getAllInterviews());
                // And lastly we tell the adapter to get new data:
                adapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren((ListView) findViewById(R.id.listview));
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


}