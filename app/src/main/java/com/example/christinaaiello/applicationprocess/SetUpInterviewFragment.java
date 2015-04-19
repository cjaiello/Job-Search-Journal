package com.example.christinaaiello.applicationprocess;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.christinaaiello.R;
import com.example.christinaaiello.general.DatabaseContract;

/**
 * Created by Christina Aiello on 3/27/2015.
 */
public class SetUpInterviewFragment extends Fragment {
    private SQLiteDatabase db;
    String ID;
    String TAG;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TAG = "SetUpInterviewFragment";

        // Initialize Database objects
        DatabaseContract.DatabaseHelper databaseHelper = new DatabaseContract.DatabaseHelper(getActivity().getApplicationContext());
        db = databaseHelper.getWritableDatabase();

        // Getting bundle information
        Bundle bundle = getActivity().getIntent().getExtras();
        ID = bundle.getString("companyID"); // companyID for this particular company

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.set_up_interview_fragment, container, false);

        // Reading this company's initial contact data from the database
        readCompanyData(view, ID);

        return view;
    }

    /**
     * This method will read a company's data from the database, based on companyID #
     */
    public void readCompanyData(View view, String companyID) {
        String[] projection = {
                DatabaseContract.SetUpInterviewTable._ID,
                DatabaseContract.SetUpInterviewTable.COLUMN_NAME_COMPANYID,
                DatabaseContract.SetUpInterviewTable.COLUMN_NAME_DATE,
                DatabaseContract.SetUpInterviewTable.COLUMN_NAME_TIME,
                DatabaseContract.SetUpInterviewTable.COLUMN_NAME_INTERVIEWERS,
                DatabaseContract.SetUpInterviewTable.COLUMN_NAME_EMAIL,
                DatabaseContract.SetUpInterviewTable.COLUMN_NAME_MISC_NOTES,
        };

        // I only want a company whose companyID number matches the one passed to me in a bundle
        String[] selectionArgs = {String.valueOf(companyID)};

        // My cursor that I use to loop over query results
        Cursor cursor = db.query(
                DatabaseContract.SetUpInterviewTable.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                DatabaseContract.SetUpInterviewTable.COLUMN_NAME_COMPANYID + "=?",                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        // Getting each of the text boxes for this fragment:
        TextView dateOfInterview = (TextView) view.findViewById(R.id.date_of_interview);
        TextView timeOfInterview = (TextView) view.findViewById(R.id.time_of_interview);
        TextView interviewerNames = (TextView) view.findViewById(R.id.names_of_interviewers);
        TextView contactEmailAddress = (TextView) view.findViewById(R.id.contact_email_address);
        TextView miscNotes = (TextView) view.findViewById(R.id.miscellaneous_notes);

        if (!(cursor.getCount() == 0)) {
            Log.i(TAG, "Got results when searching database.");
            // Now, look into the result and get the data
            cursor.moveToFirst();
            dateOfInterview.setText(cursor.getString(2));
            timeOfInterview.setText(cursor.getString(3));
            interviewerNames.setText(cursor.getString(4));
            contactEmailAddress.setText(cursor.getString(5));
            miscNotes.setText(cursor.getString(6));
        } else {
            Log.i(TAG, "Could not find matches when searching database.");
        }

    }

}
