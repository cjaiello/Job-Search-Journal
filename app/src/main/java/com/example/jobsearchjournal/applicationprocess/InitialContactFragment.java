package com.example.jobsearchjournal.applicationprocess;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jobsearchjournal.R;
import com.example.jobsearchjournal.general.DatabaseContract;
import com.example.jobsearchjournal.general.PhoneCallCounter;

/**
 * Created by Christina Aiello on 3/27/2015.
 */
public class InitialContactFragment extends Fragment {
    private SQLiteDatabase db;
    String ID;
    String TAG;
    TextView contactPhoneNumber;
    TextView numberOfCallsExchangedTextView;
    TextView contactEmailAddress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TAG = "InitialContactFragment";

        // Initialize Database objects
        DatabaseContract.DatabaseHelper databaseHelper = new DatabaseContract.DatabaseHelper(getActivity().getApplicationContext());
        db = databaseHelper.getWritableDatabase();

        // Getting bundle information
        Bundle bundle = getActivity().getIntent().getExtras();
        ID = bundle.getString("ID"); // companyID for this particular company

        //Log.i(TAG, "Bundle inside initial contact fragment says my company id is: " + ID);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.initial_contact_fragment, container, false);
        contactPhoneNumber = (TextView) view.findViewById(R.id.contact_phone_number);
        // Getting the number of calls box on the screen
        numberOfCallsExchangedTextView = (TextView) view.findViewById(R.id.contact_phone_number_count);

        // Reading this company's initial contact data from the database
        readCompanyData(view, ID);

        // Setting it so that phone numbers can be clicked
        setPhoneNumberClicking(view);
        hidePhoneCallCount(); // Hides phonecall count if user doesn't enter phone number

        // Letting a user email someone:
        TextView emailAddress = (TextView)view.findViewById(R.id.contact_email_address);
        emailAddress.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{contactEmailAddress.getText().toString()});
                intent.setType("message/rfc822");
                startActivity(intent);
            }
        });

        return view;
    }

    /**
     * This method will read a company's data from the database, based on companyID #
     *
     * @param view      is the inital contact fragment view
     * @param companyID is the companyID# of the company
     */
    public void readCompanyData(View view, String companyID) {
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
                DatabaseContract.InitialContactTable.COLUMN_NAME_COMPANYID + "=?",                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        // Getting each of the text boxes for this fragment:
        TextView dateOfInterview = (TextView) view.findViewById(R.id.date_of_interview);
        TextView contactName = (TextView) view.findViewById(R.id.contact_name);
        contactEmailAddress = (TextView) view.findViewById(R.id.contact_email_address);
        TextView method = (TextView) view.findViewById(R.id.method_of_interaction);
        TextView whatWasDiscussed = (TextView) view.findViewById(R.id.what_was_discussed_with_contact);

        // Display initial contact information if it exists:
        if (!(cursor.getCount() == 0)) {
            //Log.i(TAG, "Got results when searching database for initial contact info.");
            // Now, look into the result and get the data
            cursor.moveToFirst();
            dateOfInterview.setText(cursor.getString(2));
            contactName.setText(cursor.getString(3));
            contactEmailAddress.setText(Html.fromHtml("<u>" + cursor.getString(4) + "</u>"));
            contactPhoneNumber.setText(cursor.getString(5));
            method.setText(cursor.getString(6));
            whatWasDiscussed.setText(cursor.getString(7));
        } else {
            //Log.i(TAG, "Could not find matches when searching database.");
        }

        // Calling a method to count the # of times we've exchanged calls with this individual
        Integer numberOfCalls = PhoneCallCounter.getNumberOfPhoneCalls(this.getActivity(), cursor.getString(5));
        // Setting the value in that box
        numberOfCallsExchangedTextView.setText(Integer.toString(numberOfCalls));
    }

    /**
     * This method will let a user click a company's phone number and call them.
     * @param view is the view containing the phone number
     */
    public void setPhoneNumberClicking(View view) {
        final TextView contactPhoneNumber = (TextView) view.findViewById(R.id.contact_phone_number);
        if(contactPhoneNumber.getText().toString().length() != 0) {
            contactPhoneNumber.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent callCompanyIntent = new Intent(Intent.ACTION_CALL);
                    callCompanyIntent.setData(Uri.parse("tel:" + contactPhoneNumber.getText().toString()));
                    startActivity(callCompanyIntent);
                }
            });

            // Setting text to be underlined
            String phoneNumberText = "<u>" + contactPhoneNumber.getText().toString() + "</u>";
            contactPhoneNumber.setText(Html.fromHtml(phoneNumberText));

            // Also, display the phone icon!
            ImageView phoneIcon = (ImageView) view.findViewById(R.id.phone_icon);
            phoneIcon.setVisibility(View.VISIBLE);

            // And let's make this phone icon clickable, so the user can use this as well to call the given phone number
            phoneIcon.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent callCompanyIntent = new Intent(Intent.ACTION_CALL);
                    callCompanyIntent.setData(Uri.parse("tel:" + contactPhoneNumber.getText().toString()));
                    startActivity(callCompanyIntent);
                }
            });
        }
    }

    /**
     * This will hide the phonecall count if user hasn't given a phone number
     */
    public void hidePhoneCallCount(){
        if(contactPhoneNumber.getText().toString().length() == 0){
            numberOfCallsExchangedTextView.setText("");
        } else {
            numberOfCallsExchangedTextView.setVisibility(View.VISIBLE);
        }
    }

}
