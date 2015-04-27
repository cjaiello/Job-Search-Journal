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
    // Views on screen:
    TextView idView;
    EditText nameView;
    EditText positionView;
    EditText positionPostingView;
    EditText sizeView;
    EditText locationView;
    EditText goalView;
    EditText miscView;
    EditText websiteView;
    EditText industryView;
    EditText overallRatingView;
    EditText cultureView;
    EditText leadershipView;
    EditText compensationView;
    EditText opportunitiesView;
    EditText worklifeView;

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
        if (id == R.id.save_company) {
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
        idView = (TextView) findViewById(R.id.company_id);
        nameView = (EditText) findViewById(R.id.company_name);
        positionView = (EditText) findViewById(R.id.company_position);
        positionPostingView = (EditText) findViewById(R.id.company_position_posting);
        sizeView = (EditText) findViewById(R.id.company_size);
        locationView = (EditText) findViewById(R.id.company_location);
        goalView = (EditText) findViewById(R.id.company_goal_mission_statement);
        miscView = (EditText) findViewById(R.id.company_miscellaneous_notes);
        websiteView = (EditText) findViewById(R.id.company_website);
        industryView = (EditText) findViewById(R.id.company_industry);
        overallRatingView = (EditText) findViewById(R.id.company_overall_rating);
        cultureView = (EditText) findViewById(R.id.company_culture_and_values_rating);
        leadershipView = (EditText) findViewById(R.id.company_senior_leadership_rating);
        compensationView = (EditText) findViewById(R.id.company_compensation_rating);
        opportunitiesView = (EditText) findViewById(R.id.company_career_opportunities_rating);
        worklifeView = (EditText) findViewById(R.id.company_work_life_balance_rating);

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
        positionPostingView.setText(bundle.getString("LinkToJobPosting"));
    }

    /**
     * This method will save a company's information to the database
     */
    public void saveToDatabase() throws InterruptedException {

        ContentValues values = new ContentValues();
        // These are retrieved from what the user typed in:
        values.put(CompanyDataTable.COLUMN_NAME_NAME, nameView.getText().toString());
        values.put(CompanyDataTable.COLUMN_NAME_POSITION, positionView.getText().toString());
        values.put(CompanyDataTable.COLUMN_NAME_SIZE, sizeView.getText().toString());
        values.put(CompanyDataTable.COLUMN_NAME_LOCATION, locationView.getText().toString());
        values.put(CompanyDataTable.COLUMN_NAME_GOAL, goalView.getText().toString());
        values.put(CompanyDataTable.COLUMN_NAME_MISCELLANEOUS, miscView.getText().toString());
        // These are retrieved via the API:
        values.put(CompanyDataTable.COLUMN_NAME_WEBSITE, websiteView.getText().toString());
        values.put(CompanyDataTable.COLUMN_NAME_INDUSTRY, industryView.getText().toString());
        values.put(CompanyDataTable.COLUMN_NAME_OVERALL_RATING, overallRatingView.getText().toString());
        values.put(CompanyDataTable.COLUMN_NAME_CULTURE, cultureView.getText().toString());
        values.put(CompanyDataTable.COLUMN_NAME_LEADERSHIP, leadershipView.getText().toString());
        values.put(CompanyDataTable.COLUMN_NAME_COMPENSATION, compensationView.getText().toString());
        values.put(CompanyDataTable.COLUMN_NAME_OPPORTUNITIES, opportunitiesView.getText().toString());
        values.put(CompanyDataTable.COLUMN_NAME_WORKLIFE, worklifeView.getText().toString());
        values.put(CompanyDataTable.COLUMN_NAME_LINK_TO_JOB_POSTING, positionPostingView.getText().toString());

        // Updating the row, returning the primary key value of the new row
        String strFilter = "_id=" + idView.getText().toString();

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
