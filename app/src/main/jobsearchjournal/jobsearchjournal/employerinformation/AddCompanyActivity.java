package jobsearchjournal.employerinformation;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import jobsearchjournal.R;
import jobsearchjournal.general.DatabaseContract;

import static jobsearchjournal.general.DatabaseContract.CompanyDataTable;
import static jobsearchjournal.general.DatabaseContract.DatabaseHelper;


public class AddCompanyActivity extends ActionBarActivity {
    private EditText employerNameEditText; // Box where user types employer's name
    private Employer employer; // Will contain an employer's information
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_company_activity);

        // Initialize Database objects
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.save_company) {
            try {
                saveToDatabase();
            } catch (InterruptedException e) {

            }
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method will save a company's information to the database
     */
    public void saveToDatabase() throws InterruptedException {
        // Each of the textboxes the user typed into:
        EditText companyNameTextView = (EditText) findViewById(R.id.company_name);
        EditText companyPosition = (EditText) findViewById(R.id.company_position);
        EditText companyPositionPosting = (EditText) findViewById(R.id.company_position_posting);
        EditText companySize = (EditText) findViewById(R.id.company_size);
        EditText companyLocation = (EditText) findViewById(R.id.company_location);
        EditText companyGoal = (EditText) findViewById(R.id.company_goal_mission_statement);
        EditText companyMisc = (EditText) findViewById(R.id.company_miscellaneous_notes);

        ContentValues values = new ContentValues();
        // These are retrieved from what the user typed in:
        values.put(DatabaseContract.CompanyDataTable.COLUMN_NAME_NAME, companyNameTextView.getText().toString());
        values.put(CompanyDataTable.COLUMN_NAME_POSITION, companyPosition.getText().toString());
        values.put(CompanyDataTable.COLUMN_NAME_SIZE, companySize.getText().toString());
        values.put(CompanyDataTable.COLUMN_NAME_LOCATION, companyLocation.getText().toString());
        values.put(CompanyDataTable.COLUMN_NAME_GOAL, companyGoal.getText().toString());
        values.put(CompanyDataTable.COLUMN_NAME_MISCELLANEOUS, companyMisc.getText().toString());
        values.put(CompanyDataTable.COLUMN_NAME_LINK_TO_JOB_POSTING, companyPositionPosting.getText().toString());

        // URL for the company's information:
        String URL = employer.constructAPIURL(employerNameEditText.getText().toString());
        // Fetching the company's information:
        employer.fetchCompanyInformation(URL);

        // These are retrieved via the API:
        values.put(CompanyDataTable.COLUMN_NAME_WEBSITE, employer.getWebsite());
        values.put(CompanyDataTable.COLUMN_NAME_INDUSTRY, employer.getIndustry());
        values.put(CompanyDataTable.COLUMN_NAME_OVERALL_RATING, employer.getOverallRating());
        values.put(CompanyDataTable.COLUMN_NAME_CULTURE, employer.getCultureAndValuesRating());
        values.put(CompanyDataTable.COLUMN_NAME_LEADERSHIP, employer.getSeniorLeadershipRating());
        values.put(CompanyDataTable.COLUMN_NAME_COMPENSATION, employer.getCompensationAndBenefitsRating());
        values.put(CompanyDataTable.COLUMN_NAME_OPPORTUNITIES, employer.getCareerOpportunitiesRating());
        values.put(CompanyDataTable.COLUMN_NAME_WORKLIFE, employer.getWorkLifeBalanceRating());
        values.put(CompanyDataTable.COLUMN_NAME_LOGO, employer.getLogoByteArray());

        string companyLocation = companyLocation.getText().toString();
        if (companyLocation != null && companyLocation != "") {
            String streetViewUrl = ("https://maps.googleapis.com/maps/api/streetview?size=400x400&location=" +).replaceAll(" ", "%20");
            employer.fetchCompanyStreetView(streetViewUrl);
        }

        // Lastly, add the streetview image to the database based on the address the user typed in:
        values.put(CompanyDataTable.COLUMN_NAME_STREET_VIEW, employer.getStreetByteArray());

        // Insert the new row, returning the primary key value of the new row
        db.insert(
                CompanyDataTable.TABLE_NAME,
                null,
                values);

        // Now, if we couldn't get info for this company from Glassdoor, let's let them know about that.
        if (!employer.getCouldRetrieveFromGlassdoor()) {
            Toast.makeText(getApplicationContext(), "Sorry! We could not retrieve company data from Glassdoor.com for this company.",
                    Toast.LENGTH_LONG).show();
        }

        // Closing this activity
        finish();

    }
}
