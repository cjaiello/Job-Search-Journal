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
import com.example.christinaaiello.employerinformation.DatabaseContract;

/**
 * Created by Christina Aiello on 3/27/2015.
 */
public class InitialContactActivityEditMode extends ActionBarActivity {
    private DatabaseContract.DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    Boolean editing; // Help us tell the difference between adding a new step and editing a current one
    String ID;
    static Integer requestCode;
    String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initial_contact_activity_editmode);
        requestCode = 4;
        TAG = "InitialContactActivityEditMode";
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
                displayCompanyData(ID);
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
                    ContentValues initialContactValues = getInitialContactInfo(); // Get info from the screen
                    // Calling method to decide if we're adding a new thing or updating old:
                    addOrUpdate(initialContactValues); // This updates the database
                    Intent intent = new Intent();
                    Log.e("Fuck", "Fuck fuck fuck");
                    // Closing this activity
                    setResult(RESULT_OK, intent); //add this
                    finish();
                } catch (InterruptedException e) {
                    Log.e("Exception", "Exception in initial contact fragment edit mode, " + e.toString());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This method decides if you are adding a new row to the table or updating it
     *
     * @param initialContactValues
     */
    public void addOrUpdate(ContentValues initialContactValues) throws InterruptedException {
        if (editing) {
            Log.e(TAG, "editing");
            updateData(ID); // Updating data, based on this company's ID
        } else {
            long newRowId;
            newRowId = db.insert(
                    DatabaseContract.InitialContactTable.TABLE_NAME,
                    null,
                    initialContactValues);
        }
    }

    /**
     * This method will acquire the new information from the screen
     */
    public ContentValues getInitialContactInfo() throws InterruptedException {
        // Each of the textboxes the user typed into:
        EditText dateOfInterview = (EditText) findViewById(R.id.date_of_interview);
        EditText contactName = (EditText) findViewById(R.id.contact_name);
        EditText contactEmailAddress = (EditText) findViewById(R.id.contact_email_address);
        EditText contactPhoneNumber = (EditText) findViewById(R.id.contact_phone_number);
        EditText method = (EditText) findViewById(R.id.method_of_interaction);
        EditText whatWasDiscussed = (EditText) findViewById(R.id.what_was_discussed_with_contact);

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.InitialContactTable.COLUMN_NAME_COMPANYID, ID); // Using the ID from the bundle
        // These are retrieved from what the user typed in:
        values.put(DatabaseContract.InitialContactTable.COLUMN_NAME_DATE, dateOfInterview.getText().toString());
        values.put(DatabaseContract.InitialContactTable.COLUMN_NAME_CONTACT, contactName.getText().toString());
        values.put(DatabaseContract.InitialContactTable.COLUMN_NAME_EMAIL, contactEmailAddress.getText().toString());
        values.put(DatabaseContract.InitialContactTable.COLUMN_NAME_PHONE, contactPhoneNumber.getText().toString());
        values.put(DatabaseContract.InitialContactTable.COLUMN_NAME_METHOD, method.getText().toString());
        values.put(DatabaseContract.InitialContactTable.COLUMN_NAME_DISCUSSION, whatWasDiscussed.getText().toString());

        return values;
    }

    /**
     * This method will save a company's information to the database
     */
    public void updateData(String companyID) throws InterruptedException {
        // Each of the textboxes the user typed into:
        EditText dateOfInterview = (EditText) findViewById(R.id.date_of_interview);
        EditText contactName = (EditText) findViewById(R.id.contact_name);
        EditText contactEmailAddress = (EditText) findViewById(R.id.contact_email_address);
        EditText contactPhoneNumber = (EditText) findViewById(R.id.contact_phone_number);
        EditText method = (EditText) findViewById(R.id.method_of_interaction);
        EditText whatWasDiscussed = (EditText) findViewById(R.id.what_was_discussed_with_contact);

        ContentValues values = new ContentValues();
        // These are retrieved from what the user typed in:
        values.put(DatabaseContract.InitialContactTable.COLUMN_NAME_COMPANYID, ID); // Using the ID from the bundle
        // These are retrieved from what the user typed in:
        values.put(DatabaseContract.InitialContactTable.COLUMN_NAME_DATE, dateOfInterview.getText().toString());
        values.put(DatabaseContract.InitialContactTable.COLUMN_NAME_CONTACT, contactName.getText().toString());
        values.put(DatabaseContract.InitialContactTable.COLUMN_NAME_EMAIL, contactEmailAddress.getText().toString());
        values.put(DatabaseContract.InitialContactTable.COLUMN_NAME_PHONE, contactPhoneNumber.getText().toString());
        values.put(DatabaseContract.InitialContactTable.COLUMN_NAME_METHOD, method.getText().toString());
        values.put(DatabaseContract.InitialContactTable.COLUMN_NAME_DISCUSSION, whatWasDiscussed.getText().toString());

        // Updating the row, returning the primary key value of the new row
        String strFilter = "company_id=" + companyID;

        long newRowId;
        newRowId = db.update(
                DatabaseContract.InitialContactTable.TABLE_NAME,
                values,
                strFilter,
                null);

        // Closing this activity
        finish();

    }

    /**
     * This will display the data for the chosen company
     */
    public void displayCompanyData(String companyID) throws InterruptedException {
        // Each of the textboxes the user typed into:
        EditText dateOfInterview = (EditText) findViewById(R.id.date_of_interview);
        EditText contactName = (EditText) findViewById(R.id.contact_name);
        EditText contactEmailAddress = (EditText) findViewById(R.id.contact_email_address);
        EditText contactPhoneNumber = (EditText) findViewById(R.id.contact_phone_number);
        EditText method = (EditText) findViewById(R.id.method_of_interaction);
        EditText whatWasDiscussed = (EditText) findViewById(R.id.what_was_discussed_with_contact);

        String[] projection = {
                DatabaseContract.InitialContactTable._ID,
                DatabaseContract.InitialContactTable.COLUMN_NAME_COMPANYID,
                DatabaseContract.InitialContactTable.COLUMN_NAME_DATE,
                DatabaseContract.InitialContactTable.COLUMN_NAME_CONTACT,
                DatabaseContract.InitialContactTable.COLUMN_NAME_EMAIL,
                DatabaseContract.InitialContactTable.COLUMN_NAME_PHONE,
                DatabaseContract.InitialContactTable.COLUMN_NAME_METHOD,
                DatabaseContract.InitialContactTable.COLUMN_NAME_DISCUSSION,
        };

        // I only want a company whose ID number matches the one passed to me in a bundle
        String[] selectionArgs = {String.valueOf(companyID)};

        // My cursor that I use to loop over query results
        Cursor cursor = db.query(
                DatabaseContract.InitialContactTable.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                DatabaseContract.InitialContactTable.COLUMN_NAME_COMPANYID + "=?",                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        if (!(cursor.getCount() == 0)) {
            cursor.moveToFirst();
            Log.e(TAG, "Got results when searching database: " + Integer.toString(cursor.getCount()));
            dateOfInterview.setText(cursor.getString(2));
            contactName.setText(cursor.getString(3));
            contactEmailAddress.setText(cursor.getString(4));
            contactPhoneNumber.setText(cursor.getString(5));
            method.setText(cursor.getString(6));
            whatWasDiscussed.setText(cursor.getString(7));
        } else {
            Log.i(TAG, "Could not find matches when searching database.");
            // We won't show any data, because we don't have it.
        }


    }

}
