package com.example.christinaaiello;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.christinaaiello.employerinformation.Employer;

import static com.example.christinaaiello.DatabaseContract.DatabaseEntry;
import static com.example.christinaaiello.DatabaseContract.DatabaseHelper;


public class AddCompanyActivity extends ActionBarActivity {
    private EditText employerNameEditText; // Box where user types employer's name
    private Employer employer; // Will contain an employer's information
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_company_activity);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        db = databaseHelper.getWritableDatabase();

        employer = new Employer();

        // Getting the edit text box where user types employer's name
        employerNameEditText = (EditText) findViewById(R.id.company_name);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_company, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.save_company) {
            try {
                saveToDatabase();
            } catch (InterruptedException e) {
                Log.e("Database Error", "Error in saving to database: " + e.toString());
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method will save a company's information to the database
     */
    public void saveToDatabase() throws InterruptedException {
        // URL for the company's information:
        String URL = employer.constructAPIURL(employerNameEditText.getText().toString());
        // Fetching the company's information:
        employer.fetchCompanyInformation(URL);

        // Each of the textboxes the user typed into:
        EditText companyNameTextView = (EditText) findViewById(R.id.company_name);
        EditText companyPosition = (EditText) findViewById(R.id.company_position);
        EditText companySize = (EditText) findViewById(R.id.company_size);
        EditText companyLocation = (EditText) findViewById(R.id.company_location);
        EditText companyGoal = (EditText) findViewById(R.id.company_goal_mission_statement);
        EditText companyMisc = (EditText) findViewById(R.id.company_miscellaneous_notes);

        ContentValues values = new ContentValues();
        // These are retrieved from what the user typed in:
        values.put(DatabaseEntry.COLUMN_NAME_NAME, companyNameTextView.getText().toString());
        values.put(DatabaseEntry.COLUMN_NAME_POSITION, companyPosition.getText().toString());
        values.put(DatabaseEntry.COLUMN_NAME_SIZE, companySize.getText().toString());
        values.put(DatabaseEntry.COLUMN_NAME_LOCATION, companyLocation.getText().toString());
        values.put(DatabaseEntry.COLUMN_NAME_GOAL, companyGoal.getText().toString());
        values.put(DatabaseEntry.COLUMN_NAME_MISCELLANEOUS, companyMisc.getText().toString());
        // These are retrieved via the API:
        values.put(DatabaseEntry.COLUMN_NAME_WEBSITE, employer.getWebsite());
        values.put(DatabaseEntry.COLUMN_NAME_INDUSTRY, employer.getIndustry());
        values.put(DatabaseEntry.COLUMN_NAME_LOGO, employer.getSquareLogo());
        values.put(DatabaseEntry.COLUMN_NAME_OVERALL_RATING, employer.getOverallRating());
        values.put(DatabaseEntry.COLUMN_NAME_CULTURE, employer.getCultureAndValuesRating());
        values.put(DatabaseEntry.COLUMN_NAME_LEADERSHIP, employer.getSeniorLeadershipRating());
        values.put(DatabaseEntry.COLUMN_NAME_COMPENSATION, employer.getCompensationAndBenefitsRating());
        values.put(DatabaseEntry.COLUMN_NAME_OPPORTUNITIES, employer.getCareerOpportunitiesRating());
        values.put(DatabaseEntry.COLUMN_NAME_WORKLIFE, employer.getWorkLifeBalanceRating());

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                DatabaseEntry.TABLE_NAME,
                null,
                values);

        // Now, if we couldn't get info for this company from Glassdoor, let's let them know about that.
        if (!employer.getCouldRetrieveFromGlassdoor()) {
            Toast.makeText(getApplicationContext(), "Could not retrieve company data from Glassdoor.com",
                    Toast.LENGTH_LONG).show();
        }

        // Closing this activity
        finish();

    }
}
