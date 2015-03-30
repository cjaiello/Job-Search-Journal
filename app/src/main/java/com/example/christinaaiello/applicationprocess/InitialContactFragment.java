package com.example.christinaaiello.applicationprocess;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.christinaaiello.R;
import com.example.christinaaiello.employerinformation.DatabaseContract;

/**
 * Created by Christina Aiello on 3/27/2015.
 */
public class InitialContactFragment extends Fragment {
    private DatabaseContract.DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    String ID;
    String TAG;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TAG = "InitialContactFragment";

        // Initialize Database objects
        databaseHelper = new DatabaseContract.DatabaseHelper(getActivity().getApplicationContext());
        db = databaseHelper.getWritableDatabase();

        // Getting bundle information
        Bundle bundle = getActivity().getIntent().getExtras();
        ID = bundle.getString("ID"); // ID for this particular company

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.initial_contact_fragment, container, false);

        // Reading this company's initial contact data from the database
        readCompanyData(view, ID);

        return view;
    }

    /**
     * This method will read a company's data from the database, based on ID #
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

        // Getting each of the text boxes for this fragment:
        TextView dateOfInterview = (TextView) view.findViewById(R.id.date_of_interview);
        TextView contactName = (TextView) view.findViewById(R.id.contact_name);
        TextView contactEmailAddress = (TextView) view.findViewById(R.id.contact_email_address);
        TextView contactPhoneNumber = (TextView) view.findViewById(R.id.contact_phone_number);
        TextView method = (TextView) view.findViewById(R.id.method_of_interaction);
        TextView whatWasDiscussed = (TextView) view.findViewById(R.id.what_was_discussed_with_contact);

        if (!(cursor.getCount() == 0)) {
            Log.i(TAG, "Got results when searching database.");
            // Now, look into the result and get the data
            cursor.moveToFirst();
            dateOfInterview.setText(cursor.getString(2));
            contactName.setText(cursor.getString(3));
            contactEmailAddress.setText(cursor.getString(4));
            contactPhoneNumber.setText(cursor.getString(5));
            method.setText(cursor.getString(6));
            whatWasDiscussed.setText(cursor.getString(7));
        } else {
            Log.i(TAG, "Could not find matches when searching database.");
        }

    }

}
