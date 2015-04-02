package com.example.christinaaiello.employerinformation;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.example.christinaaiello.R;

import static com.example.christinaaiello.general.DatabaseContract.CompanyDataTable;
import static com.example.christinaaiello.general.DatabaseContract.DatabaseHelper;


public class EditCompanyActivity extends ActionBarActivity {
    String TAG = "EditCompanyActivity "; // Used for log printing
    Employer employer; // Contains employer's information
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private String companyID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_company_activity);

        // Prepping database
        databaseHelper = new DatabaseHelper(getApplicationContext());
        db = databaseHelper.getWritableDatabase();

        // Getting the company's Name number from the bundle
        Bundle bundle = getIntent().getExtras();
        // Displaying the company's data on the screen
        displayCompanyData(bundle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_company, menu);
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
            // First, save to database:
            try {
                saveToDatabase();
            } catch (InterruptedException e) {
                Log.e(TAG, "Could not save to database: " + e.toString());
            }
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This will display the data for the chosen company
     */
    public void displayCompanyData(Bundle bundle) {
        // First we need to get the view objects that are on the screen
        TextView idView = (TextView) findViewById(R.id.company_id);
        EditText nameView = (EditText) findViewById(R.id.company_name);
        EditText positionView = (EditText) findViewById(R.id.company_position);
        EditText sizeView = (EditText) findViewById(R.id.company_size);
        EditText locationView = (EditText) findViewById(R.id.company_location);
        EditText goalView = (EditText) findViewById(R.id.company_goal_mission_statement);
        EditText miscView = (EditText) findViewById(R.id.company_miscellaneous_notes);
        EditText websiteView = (EditText) findViewById(R.id.company_website);
        EditText industryView = (EditText) findViewById(R.id.company_industry);
        EditText overallRatingView = (EditText) findViewById(R.id.company_overall_rating);
        EditText cultureView = (EditText) findViewById(R.id.company_culture_and_values_rating);
        EditText leadershipView = (EditText) findViewById(R.id.company_senior_leadership_rating);
        EditText compensationView = (EditText) findViewById(R.id.company_compensation_rating);
        EditText opportunitiesView = (EditText) findViewById(R.id.company_career_opportunities_rating);
        EditText worklifeView = (EditText) findViewById(R.id.company_work_life_balance_rating);

        // Now we need to set the content of the views
        idView.setText(bundle.getString("ID"));
        nameView.setText(bundle.getString("Name"));
        positionView.setText(bundle.getString("Position"));
        sizeView.setText(bundle.getString("Size"));
        locationView.setText(bundle.getString("Location"));
        goalView.setText(bundle.getString("Goal"));
        websiteView.setText(bundle.getString("Website"));
        industryView.setText(bundle.getString("Industry"));
        overallRatingView.setText(bundle.getString("Overall"));
        cultureView.setText(bundle.getString("Culture"));
        leadershipView.setText(bundle.getString("Leadership"));
        compensationView.setText(bundle.getString("Compensation"));
        opportunitiesView.setText(bundle.getString("Opportunities"));
        worklifeView.setText(bundle.getString("Worklife"));
        miscView.setText(bundle.getString("Misc"));
    }

    /**
     * This method will save a company's information to the database
     */
    public void saveToDatabase() throws InterruptedException {
        // Each of the textboxes the user typed into:
        TextView companyID = (TextView) findViewById(R.id.company_id);
        EditText companyName = (EditText) findViewById(R.id.company_name);
        EditText companyPosition = (EditText) findViewById(R.id.company_position);
        EditText companySize = (EditText) findViewById(R.id.company_size);
        EditText companyLocation = (EditText) findViewById(R.id.company_location);
        EditText companyGoal = (EditText) findViewById(R.id.company_goal_mission_statement);
        EditText companyMisc = (EditText) findViewById(R.id.company_miscellaneous_notes);
        EditText companyWebsite = (EditText) findViewById(R.id.company_website);
        EditText companyIndustry = (EditText) findViewById(R.id.company_industry);
        EditText companyOverall = (EditText) findViewById(R.id.company_overall_rating);
        EditText companyCulture = (EditText) findViewById(R.id.company_culture_and_values_rating);
        EditText companyLeadership = (EditText) findViewById(R.id.company_senior_leadership_rating);
        EditText companyCompensation = (EditText) findViewById(R.id.company_compensation_rating);
        EditText companyOpportunities = (EditText) findViewById(R.id.company_career_opportunities_rating);
        EditText companyWorklife = (EditText) findViewById(R.id.company_work_life_balance_rating);

        ContentValues values = new ContentValues();
        // These are retrieved from what the user typed in:
        values.put(CompanyDataTable.COLUMN_NAME_NAME, companyName.getText().toString());
        values.put(CompanyDataTable.COLUMN_NAME_POSITION, companyPosition.getText().toString());
        values.put(CompanyDataTable.COLUMN_NAME_SIZE, companySize.getText().toString());
        values.put(CompanyDataTable.COLUMN_NAME_LOCATION, companyLocation.getText().toString());
        values.put(CompanyDataTable.COLUMN_NAME_GOAL, companyGoal.getText().toString());
        values.put(CompanyDataTable.COLUMN_NAME_MISCELLANEOUS, companyMisc.getText().toString());
        // These are retrieved via the API:
        values.put(CompanyDataTable.COLUMN_NAME_WEBSITE, companyWebsite.getText().toString());
        values.put(CompanyDataTable.COLUMN_NAME_INDUSTRY, companyIndustry.getText().toString());
        values.put(CompanyDataTable.COLUMN_NAME_OVERALL_RATING, companyOverall.getText().toString());
        values.put(CompanyDataTable.COLUMN_NAME_CULTURE, companyCulture.getText().toString());
        values.put(CompanyDataTable.COLUMN_NAME_LEADERSHIP, companyLeadership.getText().toString());
        values.put(CompanyDataTable.COLUMN_NAME_COMPENSATION, companyCompensation.getText().toString());
        values.put(CompanyDataTable.COLUMN_NAME_OPPORTUNITIES, companyOpportunities.getText().toString());
        values.put(CompanyDataTable.COLUMN_NAME_WORKLIFE, companyWorklife.getText().toString());

        // Updating the row, returning the primary key value of the new row
        String strFilter = "_id=" + companyID.getText().toString();

        long newRowId;
        newRowId = db.update(
                CompanyDataTable.TABLE_NAME,
                values,
                strFilter,
                null);

        // Closing this activity
        finish();

    }
}
