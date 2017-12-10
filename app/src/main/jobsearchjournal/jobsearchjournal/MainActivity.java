package jobsearchjournal.jobsearchjournal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import jobsearchjournal.employerinformation.AddCompanyActivity;
import jobsearchjournal.employerinformation.CompanyListAdapter;
import jobsearchjournal.employerinformation.Employer;
import jobsearchjournal.employerinformation.ViewCompanyActivity;
import jobsearchjournal.general.DatabaseContract;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity "; // Used for log files
    private ListView listView; // Contains all companies' names and urls
    public CompanyListAdapter adapter;
    private DatabaseContract.DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private ArrayList<Employer> listOfCompanies;
    private String MY_PREFS_NAME = "preferences";
    private Menu mainActivityMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = (FloatingActionButton) findViewById (R.id.fab);
        fab.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                startActivity(new Intent(MainActivity.this, AddCompanyActivity.class));
            }
        });
        databaseHelper = new DatabaseContract.DatabaseHelper(getApplicationContext());
        db = databaseHelper.getWritableDatabase();
        try {
            listOfCompanies = getAllCompanies("name"); // Will contain all companies
        } catch (IOException e) {

        }

        // Putting view settings in preferences
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("view", "name");
        editor.commit();

        // This contains the list of companies
        listView = (ListView) findViewById(R.id.listview);

        // Adapter for the list of companies
        adapter = new CompanyListAdapter(listOfCompanies, this);
        listView.setAdapter(adapter);

        // Setting actions that occur when pressing an item in the listview
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // Making an intent to open ViewCompanyActivity
                Intent startViewCompanyIntent = new Intent(MainActivity.this, ViewCompanyActivity.class);
                // Bundle to store things in
                Bundle bundle = new Bundle();
                // Getting the clicked view
                RelativeLayout relativeLayout = (RelativeLayout) view;
                // Getting the first child, which is the textview with a company's name
                TextView idTextView = (TextView) relativeLayout.getChildAt(1);
                // Use this name when starting a new activity
                bundle.putString("ID", idTextView.getText().toString());
                startViewCompanyIntent.putExtras(bundle);
                // Creating an intent to start the window
                startActivity(startViewCompanyIntent);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mainActivityMenu = menu;
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            refreshListOfCompanies();
        } catch (IOException e) {

        }
    }

    /**
     * This method can be called from anywhere to refresh the list of companies in this activity
     */
    private void refreshListOfCompanies() throws IOException {
        // Checking user's preferences to see what way to display data
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String viewOrder = prefs.getString("view", null);
        // First we clear the list of companies that the adapter is using:
        listOfCompanies.clear();
        // Now we update the list of companies:
        listOfCompanies.addAll(getAllCompanies(viewOrder));
        // And lastly we tell the adapter to get new data:
        adapter.notifyDataSetChanged();
    }

    /**
     * This method will let a user flip his or her view settings in this main activity - either
     * organize the list of companies by name or by step in the application process
     *
     */
    public void setViewPreferences(MenuItem viewOrderItem) throws IOException {
        // Checking user's preferences to see what way to display data
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String viewOrder = prefs.getString("view", null);
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        View view = (View) findViewById(R.id.listview);
        // Flipping view settings
        if (viewOrder.equals("name")) {
            editor.putString("view", "step");
            viewOrderItem.setTitle("Organize by Company Name");
            Snackbar.make (view, "Now in order by application step!", Snackbar.LENGTH_LONG).show ();
        } else {
            editor.putString("view", "name");
            viewOrderItem.setTitle("Organize by Application Step");
            Snackbar.make (view, "Now in order by company name!", Snackbar.LENGTH_LONG).show ();
        }
        editor.commit();

        // Finally, refresh the layout:
        refreshListOfCompanies();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method will get all company names from the database.
     */
    public ArrayList<Employer> getAllCompanies(String orderBy) throws IOException {
        // This will contain the names of all of the companies
        ArrayList<Employer> employerList = new ArrayList<Employer>();

        // Writing what columns we want from the table
        String[] projection = {
                DatabaseContract.CompanyDataTable._ID,
                DatabaseContract.CompanyDataTable.COLUMN_NAME_NAME,
                DatabaseContract.CompanyDataTable.COLUMN_NAME_POSITION,
                DatabaseContract.CompanyDataTable.COLUMN_NAME_WEBSITE,
                DatabaseContract.CompanyDataTable.COLUMN_NAME_STEP,
                DatabaseContract.CompanyDataTable.COLUMN_NAME_LOGO,
        };

        // Organize company names in this order...
        String sortOrder;

        // Either do it alphabetically
        if (orderBy.equals("name")) {
            sortOrder =
                    DatabaseContract.CompanyDataTable.COLUMN_NAME_NAME + " ASC";
        } else {
            // Or order them by step
            sortOrder =
                    DatabaseContract.CompanyDataTable.COLUMN_NAME_STEP + " ASC";
        }

        // My cursor that I use to loop over query results
        Cursor cursor = db.query(
                DatabaseContract.CompanyDataTable.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // Thcce sort order
        );

        // Now, we make an employer object and put each row's data into it.
        // We then insert this into the ArrayList and move to the next row.
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            Employer employer = new Employer();
            //Log.i(TAG, "Company name is: " + cursor.getString(1));
            employer.setID(Long.toString(cursor.getLong(0)));
            employer.setName(cursor.getString(1));
            employer.setPosition(cursor.getString(2));
            employer.setWebsite(cursor.getString(3));
            employer.setStep(cursor.getString(4));
            employer.setLogoByteArray(cursor.getBlob(5));
            employerList.add(employer);
            cursor.moveToNext();
        }

        //Log.i(TAG, "Companynameslist: " + employerList.toString());
        return employerList;
    }

    /**
     * This method lets a user delete a company
     *
     * @param view is the view a user clicked on
     */
    public void deleteCompany(final View view) {
        // Making a dialog box that will pop up for the user
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Delete Company?");
        builder.setMessage("Are you sure you want to delete this company?");
        builder.setIcon(R.drawable.ic_launcher);
        builder.setPositiveButton("Yes, Delete It", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                // Getting the relativelayout holding this trash can and other things
                RelativeLayout parentView = (RelativeLayout) view.getParent();
                // Getting the textview holding the company's ID number
                TextView idView = (TextView) parentView.getChildAt(1);
                // This is the actual ID number
                String idNumber = idView.getText().toString();
                // Deleting from the database:
                db.delete(DatabaseContract.CompanyDataTable.TABLE_NAME, DatabaseContract.CompanyDataTable._ID + "=?", new String[]{idNumber});
                db.delete(DatabaseContract.InitialContactTable.TABLE_NAME, DatabaseContract.InitialContactTable.COLUMN_NAME_COMPANYID + "=?", new String[]{idNumber});
                db.delete(DatabaseContract.SetUpInterviewTable.TABLE_NAME, DatabaseContract.SetUpInterviewTable.COLUMN_NAME_COMPANYID + "=?", new String[]{idNumber});
                db.delete(DatabaseContract.ReceivedResponseTable.TABLE_NAME, DatabaseContract.ReceivedResponseTable.COLUMN_NAME_COMPANYID + "=?", new String[]{idNumber});
                // And refreshing the layout
                try {
                    refreshListOfCompanies();
                } catch (IOException e) {

                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
