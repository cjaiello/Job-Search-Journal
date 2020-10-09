package jobsearchjournal.applicationprocess;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import jobsearchjournal.R;
import jobsearchjournal.general.DatabaseContract;

/**
 * Created by Christina Aiello on 3/27/2015.
 */
public class ReceivedResponseFragment extends Fragment {
    private SQLiteDatabase db;
    String ID;
    String TAG;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TAG = "ReceivedResponseFragment";

        // Initialize Database objects
        DatabaseContract.DatabaseHelper databaseHelper = new DatabaseContract.DatabaseHelper(getActivity().getApplicationContext());
        db = databaseHelper.getWritableDatabase();

        // Getting bundle information
        Bundle bundle = getActivity().getIntent().getExtras();
        ID = bundle.getString("ID"); // companyID for this particular company


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.received_response_fragment, container, false);

        // Reading this company's initial contact data from the database
        readCompanyData(view, ID);

        return view;
    }

    /**
     * This method will read a company's data from the database, based on companyID #
     */
    public void readCompanyData(View view, String companyID) {
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

        // Getting each of the text boxes for this fragment:
        TextView dateOfResponse = (TextView) view.findViewById(R.id.date_of_response);
        CheckBox acceptedBox = (CheckBox) view.findViewById(R.id.accepted_button);
        CheckBox rejectedBox = (CheckBox) view.findViewById(R.id.rejected_button);
        TextView offerAmount = (TextView) view.findViewById(R.id.offer_amount);
        RelativeLayout offerAmountLayout = (RelativeLayout) view.findViewById(R.id.offer_amount_layout);
        RelativeLayout offerDeadlineLayout = (RelativeLayout) view.findViewById(R.id.offer_deadline_layout);
        RelativeLayout offerResponseLayout = (RelativeLayout) view.findViewById(R.id.offer_response_layout);
        TextView offerDeadline = (TextView) view.findViewById(R.id.offer_deadline);
        TextView offerResponse = (TextView) view.findViewById(R.id.offer_response);
        TextView miscNotes = (TextView) view.findViewById(R.id.miscellaneous_notes);

        if (!(cursor.getCount() == 0)) {
            //Log.i(TAG, "Got results when searching database for recieved response information for this company.");
            // Now, look into the result and get the data
            cursor.moveToFirst();
            dateOfResponse.setText(cursor.getString(2));
            String receivedResponse = cursor.getString(3); // Seeing what the response actually was
            // If they did get an offer, display this information on the screen
            if (receivedResponse.equals("Yes")){
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
        }

    }

}
