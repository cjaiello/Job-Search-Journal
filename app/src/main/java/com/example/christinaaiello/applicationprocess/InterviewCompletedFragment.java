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
    private SQLiteDatabase db;
    String ID;
    String TAG;
    Integer interviewNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TAG = "InterviewCompletedFragment";

        // Initialize Database objects
        DatabaseContract.DatabaseHelper databaseHelper = new DatabaseContract.DatabaseHelper(getActivity().getApplicationContext());
        db = databaseHelper.getWritableDatabase();

        // Getting bundle information
        Bundle updateStepsBundle = getActivity().getIntent().getExtras();
        ID = updateStepsBundle.getString("ID"); // ID for this particular company
        Log.e(TAG, "ID is: " + ID);
        Bundle interviewCompletedBundle = getArguments();
        interviewNumber = interviewCompletedBundle.getInt("Number");
        Log.e(TAG, "InterviewNumber is: " + interviewNumber);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.interview_completed_fragment, container, false);

        // Reading this company's initial contact data from the database
        readCompanyData(view);

        return view;
    }

    /**
     * This method will read a company's data from the database, based on ID #
     */
    public void readCompanyData(View view) {
        String[] projection = {
                DatabaseContract.InterviewCompletedTable._ID,
                DatabaseContract.InterviewCompletedTable.COLUMN_NAME_COMPANYID,
                DatabaseContract.InterviewCompletedTable.COLUMN_NAME_NOTES_ABOUT_INTERVIEW,
                DatabaseContract.InterviewCompletedTable.COLUMN_NAME_INTERVIEW_NUMBER,
        };

        // I only want a company whose ID number matches the one passed to me in a bundle
        String[] selectionArgs = {String.valueOf(ID), String.valueOf(interviewNumber)};

        // My cursor that I use to loop over query results
        Cursor cursor = db.query(
                DatabaseContract.InterviewCompletedTable.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                DatabaseContract.InterviewCompletedTable.COLUMN_NAME_COMPANYID + "=? AND " + DatabaseContract.InterviewCompletedTable.COLUMN_NAME_INTERVIEW_NUMBER + "=?",                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        // Getting each of the text boxes for this fragment:
        TextView notesAboutInterview = (TextView) view.findViewById(R.id.notes_about_interview);
        TextView interviewNumberView = (TextView) view.findViewById(R.id.interview_number);

        if (!(cursor.getCount() == 0)) {
            Log.i(TAG, "Got results when searching database.");
            // Now, look into the result and get the data
            cursor.moveToFirst();
            Log.e(TAG, cursor.getString(2));
            Log.e(TAG, cursor.getString(3));
            notesAboutInterview.setText(cursor.getString(2));
            interviewNumberView.setText(cursor.getString(3));
        } else {
            Log.i(TAG, "Could not find matches when searching database.");
            Log.e("no", "no matches");
        }

    }

}
