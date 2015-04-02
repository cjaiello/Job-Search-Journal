package com.example.christinaaiello.applicationprocess;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.christinaaiello.R;
import com.example.christinaaiello.general.DatabaseContract;

/**
 * Created by Christina Aiello on 3/27/2015.
 */
public class InterviewCompletedActivityEditMode extends ActionBarActivity {
    private DatabaseContract.DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    Boolean editing; // Help us tell the difference between adding a new step and editing a current one
    String ID;
    static Integer requestCode;
    String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interview_completed_fragment_editmode);
        requestCode = 4;
        TAG = "InterviewCompletedActivityEditMode";
        // Initialize Database objects
        databaseHelper = new DatabaseContract.DatabaseHelper(getApplicationContext());
        db = databaseHelper.getWritableDatabase();

        // Getting bundle information
        Bundle bundle = getIntent().getExtras();
        editing = bundle.getBoolean("Editing"); // Whether we're editing (or adding)
        ID = bundle.getString("ID"); // ID for this particular company

        // If I chose to edit this, show the old data
        if (editing) {
            try {
                displayPreviouslyEnteredInterviewData(ID);
            } catch (InterruptedException e) {
                Log.e(TAG, "Error in displaying company data.");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_step, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.save_data:
                try {
                    ContentValues initialContactValues = getSetUpInterviewInformation();// Get info from the screen
                    // Calling method to decide if we're adding a new thing or updating old:
                    addOrUpdate(initialContactValues); // This updates the database
                    Intent intent = new Intent();
                    Log.e("Fuck", "Fuck fuck fuck");
                    // Closing this activity
                    setResult(RESULT_OK, intent); //add this
                    finish();
                } catch (InterruptedException e) {
                    Log.e("Exception", "Exception in set up interview fragment edit mode, " + e.toString());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This method decides if you are adding a new row to the table or updating it
     *
     * @param setUpValues are the values on the screen for setting up an interview
     */
    public void addOrUpdate(ContentValues setUpValues) throws InterruptedException {
        if (editing) {
            Log.e(TAG, "editing");
            updateData(ID, setUpValues); // Updating data, based on this company's ID
        } else {
            long newRowId;
            newRowId = db.insert(
                    DatabaseContract.InterviewCompletedTable.TABLE_NAME,
                    null,
                    setUpValues);
        }
    }

    /**
     * This method will acquire the new information from the screen
     */
    public ContentValues getSetUpInterviewInformation() throws InterruptedException {
        // Each of the textboxes the user typed into:
        EditText notesAboutInterview = (EditText) findViewById(R.id.notes_about_interview);

        ContentValues values = new ContentValues();
        // These are retrieved from what the user typed in:
        values.put(DatabaseContract.InterviewCompletedTable.COLUMN_NAME_COMPANYID, ID); // Using the ID from the bundle
        // These are retrieved from what the user typed in:
        values.put(DatabaseContract.InterviewCompletedTable.COLUMN_NAME_NOTES_ABOUT_INTERVIEW, notesAboutInterview.getText().toString());

        return values;
    }

    /**
     * This method will save a company's information to the database
     */
    public void updateData(String companyID, ContentValues interviewInfo) throws InterruptedException {

        // Updating the row, returning the primary key value of the new row
        String strFilter = "company_id=" + companyID;

        long newRowId;
        newRowId = db.update(
                DatabaseContract.InterviewCompletedTable.TABLE_NAME,
                interviewInfo,
                strFilter,
                null);

        // Closing this activity
        finish();

    }

    /**
     * This will display the data for the chosen company
     */
    public void displayPreviouslyEnteredInterviewData(String companyID) throws InterruptedException {
        // Each of the textboxes the user typed into:
        EditText notesAboutInterview = (EditText) findViewById(R.id.notes_about_interview);

        String[] projection = {
                DatabaseContract.InterviewCompletedTable._ID,
                DatabaseContract.InterviewCompletedTable.COLUMN_NAME_COMPANYID,
                DatabaseContract.InterviewCompletedTable.COLUMN_NAME_NOTES_ABOUT_INTERVIEW,
        };

        // I only want a company whose ID number matches the one passed to me in a bundle
        String[] selectionArgs = {String.valueOf(companyID)};

        // My cursor that I use to loop over query results
        Cursor cursor = db.query(
                DatabaseContract.InterviewCompletedTable.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                DatabaseContract.InterviewCompletedTable.COLUMN_NAME_COMPANYID + "=?",                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        if (!(cursor.getCount() == 0)) {
            cursor.moveToFirst();
            Log.e(TAG, "Got results when searching database: " + Integer.toString(cursor.getCount()));
            notesAboutInterview.setText(cursor.getString(2));
        } else {
            Log.i(TAG, "Could not find matches when searching database.");
            // We won't show any data, because we don't have it.
        }


    }

}
