package com.example.jobsearchjournal.applicationprocess;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.jobsearchjournal.R;
import com.example.jobsearchjournal.general.DatabaseContract;

/**
 * Created by Christina Aiello on 3/27/2015.
 */
public class ReceivedResponseActivityEditMode extends AppCompatActivity {
    private SQLiteDatabase db;
    Boolean editing; // Help us tell the difference between adding a new step and editing a current one
    String ID;
    static Integer requestCode;
    String TAG;
    Integer acceptedBoxFlag; // Used to tell whether the accepted box is checked or not
    // Items on the sreen:
    RelativeLayout offerAmountLayout;
    RelativeLayout offerDeadlineLayout;
    RelativeLayout offerResponseLayout;
    CheckBox acceptedBox;
    CheckBox rejectedBox;
    EditText dateOfResponse;
    EditText offerAmount;
    EditText offerDeadline;
    EditText offerResponse;
    EditText miscNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.received_response_fragment_editmode);
        requestCode = 4;
        TAG = "ReceivedResponseActivityEditMode";
        // Initialize Database objects
        DatabaseContract.DatabaseHelper databaseHelper = new DatabaseContract.DatabaseHelper(getApplicationContext());
        db = databaseHelper.getWritableDatabase();
        acceptedBoxFlag = 0; // Initialize to zero
        getLayoutItemsOnScreen(); // Getting all the items on the screen and putting them into variables

        // Getting bundle information
        Bundle bundle = getIntent().getExtras();
        editing = bundle.getBoolean("Editing"); // Whether we're editing (or adding)
        ID = bundle.getString("companyID"); // companyID for this particular company

        // If I chose to edit this, show the old data
        if (editing) {
            try {
                displayPreviouslyEnteredData(ID);
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
                    ContentValues receivedResponseValues = getReceivedResponseInformation();// Get info from the screen
                    // Calling method to decide if we're adding a new thing or updating old:
                    addOrUpdate(receivedResponseValues); // This updates the database
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
     * @param receivedResponseValues are the values on the screen for setting up an interview
     */
    public void addOrUpdate(ContentValues receivedResponseValues) throws InterruptedException {
        if (editing) {
            //Log.i(TAG, "editing");
            updateData(ID, receivedResponseValues); // Updating data, based on this company's companyID
        } else {
            db.insert(
                    DatabaseContract.ReceivedResponseTable.TABLE_NAME,
                    null,
                    receivedResponseValues);
        }
    }

    /**
     * This method will acquire the new information from the screen
     */
    public ContentValues getReceivedResponseInformation() throws InterruptedException {
        Boolean gotOffer = false; // Tells if user got an offer or not

        // If user got an offer, set this value to true (If this was only clicked once)
        if (acceptedBox.isChecked()) {
            gotOffer = true;
        }

        ContentValues values = new ContentValues();
        // These are retrieved from what the user typed in:
        values.put(DatabaseContract.ReceivedResponseTable.COLUMN_NAME_COMPANYID, ID); // Using the companyID from the bundle
        // These are retrieved from what the user typed in:
        values.put(DatabaseContract.ReceivedResponseTable.COLUMN_NAME_DATE_OF_RESPONSE, dateOfResponse.getText().toString());
        // If the user got an offer, store this information:
        if (gotOffer) {
            values.put(DatabaseContract.ReceivedResponseTable.COLUMN_NAME_RECEIVED_OFFER, "Yes");
        } else {
            // Else say they didn't get one:
            values.put(DatabaseContract.ReceivedResponseTable.COLUMN_NAME_RECEIVED_OFFER, "No");
        }
        values.put(DatabaseContract.ReceivedResponseTable.COLUMN_NAME_OFFER_AMOUNT, offerAmount.getText().toString());
        values.put(DatabaseContract.ReceivedResponseTable.COLUMN_NAME_OFFER_DEADLINE, offerDeadline.getText().toString());
        values.put(DatabaseContract.ReceivedResponseTable.COLUMN_NAME_OFFER_RESPONSE, offerResponse.getText().toString());
        values.put(DatabaseContract.ReceivedResponseTable.COLUMN_NAME_MISC_NOTES, miscNotes.getText().toString());

        return values;
    }

    /**
     * This method will save a company's information to the database
     */
    public void updateData(String companyID, ContentValues setUpInfo) throws InterruptedException {

        // Updating the row, returning the primary key value of the new row
        String strFilter = "company_id=" + companyID;

        long newRowId;
        newRowId = db.update(
                DatabaseContract.ReceivedResponseTable.TABLE_NAME,
                setUpInfo,
                strFilter,
                null);

        // Closing this activity
        finish();

    }

    /**
     * This will display the data for the chosen company
     */
    public void displayPreviouslyEnteredData(String companyID) throws InterruptedException {
        Boolean gotOffer = false; // Tells if user got an offer or not
        // If user got an offer, set this value to true
        if (acceptedBox.isChecked()) {
            gotOffer = true;
        }

        String[] projection = {
                DatabaseContract.ReceivedResponseTable._ID,
                DatabaseContract.ReceivedResponseTable.COLUMN_NAME_COMPANYID,
                DatabaseContract.ReceivedResponseTable.COLUMN_NAME_DATE_OF_RESPONSE,
                DatabaseContract.ReceivedResponseTable.COLUMN_NAME_RECEIVED_OFFER,
                DatabaseContract.ReceivedResponseTable.COLUMN_NAME_OFFER_AMOUNT,
                DatabaseContract.ReceivedResponseTable.COLUMN_NAME_OFFER_DEADLINE,
                DatabaseContract.ReceivedResponseTable.COLUMN_NAME_OFFER_RESPONSE,
                DatabaseContract.ReceivedResponseTable.COLUMN_NAME_MISC_NOTES,
        };

        // I only want a company whose companyID number matches the one passed to me in a bundle
        String[] selectionArgs = {String.valueOf(companyID)};

        // My cursor that I use to loop over query results
        Cursor cursor = db.query(
                DatabaseContract.ReceivedResponseTable.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                DatabaseContract.ReceivedResponseTable.COLUMN_NAME_COMPANYID + "=?",                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        if (!(cursor.getCount() == 0)) {
            cursor.moveToFirst();
            //Log.i(TAG, "Got results when searching database for " + "\received response\" information: " + Integer.toString(cursor.getCount()));
            dateOfResponse.setText(cursor.getString(2));
            String receivedResponse = cursor.getString(3); // Seeing what the response actually was
            // If they did get an offer, display this information on the screen
            if (receivedResponse.equals("Yes")) {
                acceptedBox.setChecked(true);
                rejectedBox.setChecked(false);
                offerAmountLayout.setVisibility(View.VISIBLE);
                offerDeadlineLayout.setVisibility(View.VISIBLE);
                offerResponseLayout.setVisibility(View.VISIBLE);
                offerAmount.setText(cursor.getString(4));
                offerDeadline.setText(cursor.getString(5));
                offerResponse.setText(cursor.getString(6));
            } else {
                // Have rejected box be checked
                rejectedBox.setChecked(true);
                acceptedBox.setChecked(false);
            }
            // Either way, set misc notes
            miscNotes.setText(cursor.getString(7));
        } else {
            //Log.i(TAG, "Could not find matches when searching database.");
            // We won't show any data, because we don't have it.
        }


    }

    /**
     * This method is used by the "Yes I Got An Offer" button in this layout. If the user checks this box, more details show up for the user to enter.
     *
     * @param v
     */
    public void receivedOfferMethod(View v) {
        acceptedBoxFlag++; // Increase the counter
        //Log.i(TAG, "Clicked received offer, and counter is: " + Integer.toString(acceptedBoxFlag));

        if (((acceptedBoxFlag % 2) == 0) && (acceptedBoxFlag != 0)) {
            //Log.i(TAG, "Counter is an odd number, and counter is: " + Integer.toString(acceptedBoxFlag));
            // This happens for unchecking this box
            offerAmountLayout.setVisibility(View.GONE);
            offerDeadlineLayout.setVisibility(View.GONE);
            offerResponseLayout.setVisibility(View.GONE);
        } else {
            offerAmountLayout.setVisibility(View.VISIBLE);
            offerDeadlineLayout.setVisibility(View.VISIBLE);
            offerResponseLayout.setVisibility(View.VISIBLE);
            rejectedBox.setChecked(false);
        }
    }

    /**
     * This method is used by the "No I Didn't Get Offer" button in this layout. If the user checks this box, more details show up for the user to enter.
     *
     * @param v
     */
    public void receivedNoOfferMethod(View v) {
        //Log.i(TAG, "No, didn't get an offer - was clicked");
        // Hide what we don't need to see
        offerAmountLayout.setVisibility(View.GONE);
        offerDeadlineLayout.setVisibility(View.GONE);
        offerResponseLayout.setVisibility(View.GONE);
        acceptedBox.setChecked(false);
        // If the number is odd for clicking, that means we need to increase the count
        if (acceptedBoxFlag % 2 != 0) {
            acceptedBoxFlag++;
        }
    }

    /**
     * This method will get all of the various layout items on the screen for us.
     */
    public void getLayoutItemsOnScreen() {
        // Boxes that the user types into on the screen:
        dateOfResponse = (EditText) findViewById(R.id.date_of_response);
        offerAmountLayout = (RelativeLayout) findViewById(R.id.offer_amount_layout);
        offerDeadlineLayout = (RelativeLayout) findViewById(R.id.offer_deadline_layout);
        offerResponseLayout = (RelativeLayout) findViewById(R.id.offer_response_layout);
        acceptedBox = (CheckBox) findViewById(R.id.accepted_button);
        rejectedBox = (CheckBox) findViewById(R.id.rejected_button);
        offerAmount = (EditText) findViewById(R.id.offer_amount);
        offerDeadline = (EditText) findViewById(R.id.offer_deadline);
        offerResponse = (EditText) findViewById(R.id.offer_response);
        miscNotes = (EditText) findViewById(R.id.miscellaneous_notes);
    }
}
