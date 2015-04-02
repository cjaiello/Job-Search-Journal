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
public class InterviewCompletedFragment extends Fragment {
    private DatabaseContract.DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    String ID;
    String TAG;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TAG = "InterviewCompletedFragment";

        // Initialize Database objects
        databaseHelper = new DatabaseContract.DatabaseHelper(getActivity().getApplicationContext());
        db = databaseHelper.getWritableDatabase();

        // Getting bundle information
        Bundle bundle = getActivity().getIntent().getExtras();
        ID = bundle.getString("ID"); // ID for this particular company

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.interview_completed_fragment, container, false);

        // Reading this company's initial contact data from the database
        readCompanyData(view, ID);

        return view;
    }

    /**
     * This method will read a company's data from the database, based on ID #
     */
    public void readCompanyData(View view, String companyID) {
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

        // Getting each of the text boxes for this fragment:
        TextView notesAboutInterview = (TextView) view.findViewById(R.id.notes_about_interview);

        if (!(cursor.getCount() == 0)) {
            Log.i(TAG, "Got results when searching database.");
            // Now, look into the result and get the data
            cursor.moveToFirst();
            notesAboutInterview.setText(cursor.getString(2));
        } else {
            Log.i(TAG, "Could not find matches when searching database.");
        }

    }

}
