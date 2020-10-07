package com.example.jobsearchjournal.applicationprocess;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.jobsearchjournal.R;
import com.example.jobsearchjournal.general.DatabaseContract;

/**
 * Created by Christina Aiello on 3/27/2015.
 */
public class InitialContactActivityEditMode extends AppCompatActivity {
    private DatabaseContract.DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    Boolean editing; // Help us tell the difference between adding a new step and editing a current one
    String ID; // This is the companyID for this particular company
    //static Integer requestCode;
    String TAG; // Used for debugging
    // UI Components:
    EditText dateOfContact;
    EditText contactName;
    EditText contactEmailAddress;
    EditText contactPhoneNumber;
    EditText method;
    EditText whatWasDiscussed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initial_contact_activity_editmode);
        getLayoutItemsOnScreen();
         // Used for logging:
        TAG = "InitialContactActivityEditMode";

        // Initialize Database objects
        databaseHelper = new DatabaseContract.DatabaseHelper(getApplicationContext());
        db = databaseHelper.getWritableDatabase();

        // Getting bundle information
        Bundle bundle = getIntent().getExtras();
        editing = bundle.getBoolean("Editing"); // Whether we're editing (or adding)
        ID = bundle.getString("companyID"); // companyID for this particular company
        //Log.i(TAG, "Within initial contact (edit mode), company ID is: " + ID);

        // If I chose to edit this, show the old data
        if (editing) {
            try {
                displayCompanyData(ID);
            } catch (InterruptedException e) {

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
                    ContentValues initialContactValues = getTextFromScreen(); // Get info from the screen

                    // Calling method to decide if we're adding a new "Initial Contact" or updating old one:
                    addOrUpdate(initialContactValues); // This updates the database
                    //Log.i(TAG, "Done adding initial contact values");
                    Intent intent = new Intent();
                    // Closing this activity
                    setResult(RESULT_OK, intent); //add this
                    finish();
                } catch (InterruptedException e) {

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
        // If we're editing old data, do an update
        if (editing) {
            //Log.i(TAG, "Initial contact values are being edited");
            updateData(ID, initialContactValues); // Updating data, based on this company's companyID
        } else {
            // We're adding something new, so do an insert
            //Log.i(TAG, "Initial contact values are being INSERTED");
            db.insert(
                    DatabaseContract.InitialContactTable.TABLE_NAME,
                    null,
                    initialContactValues);
        }
    }

    /**
     * This method will acquire the new information from the screen, putting the information
     * into a ContentValues that is used to insert into the database
     *
     * @return a ContentValues with the information from the screen
     */
    public ContentValues getTextFromScreen() throws InterruptedException {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.InitialContactTable.COLUMN_NAME_COMPANYID, ID); // Using the companyID from the bundle
        // These are retrieved from what the user typed in:
        values.put(DatabaseContract.InitialContactTable.COLUMN_NAME_DATE, dateOfContact.getText().toString());
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
    public void updateData(String companyID, ContentValues initialContactValues) throws InterruptedException {
        // Updating the row, returning the primary key value of the new row
        String strFilter = "company_id=" + companyID;

        // Make the query
        db.update(
                DatabaseContract.InitialContactTable.TABLE_NAME,
                initialContactValues,
                strFilter,
                null);

        // Closing this activity
        finish();

    }

    /**
     * This will display the data for the chosen company on the screen
     */
    public void displayCompanyData(String companyID) throws InterruptedException {
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

        // I only want a company whose companyID number matches the one passed to me in a bundle
        String[] selectionArgs = {String.valueOf(companyID)};

        // My cursor that I use to loop over query results
        Cursor cursor = db.query(
                DatabaseContract.InitialContactTable.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                DatabaseContract.InitialContactTable.COLUMN_NAME_COMPANYID + "=?",// The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        // If we got results:
        if (!(cursor.getCount() == 0)) {
            cursor.moveToFirst();
            //Log.i(TAG, "Got \"initial contact\" results when searching database: " + Integer.toString(cursor.getCount()));
            dateOfContact.setText(cursor.getString(2));
            contactName.setText(cursor.getString(3));
            contactEmailAddress.setText(cursor.getString(4));
            contactPhoneNumber.setText(cursor.getString(5));
            method.setText(cursor.getString(6));
            whatWasDiscussed.setText(cursor.getString(7));
        } else {
            //Log.i(TAG, "Could not find matches when searching database.");
            // We won't show any data, because we don't have it.
        }


    }

    /**
     * This method will get all of the various layout items on the screen for us.
     */
    public void getLayoutItemsOnScreen() {
        // Each of the textboxes the user types into:
        dateOfContact = (EditText) findViewById(R.id.date_of_contact);
        contactName = (EditText) findViewById(R.id.contact_name);
        contactEmailAddress = (EditText) findViewById(R.id.contact_email_address);
        contactPhoneNumber = (EditText) findViewById(R.id.contact_phone_number);
        method = (EditText) findViewById(R.id.method_of_interaction);
        whatWasDiscussed = (EditText) findViewById(R.id.what_was_discussed_with_contact);
    }

}
