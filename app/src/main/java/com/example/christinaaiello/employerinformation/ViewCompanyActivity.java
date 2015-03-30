package com.example.christinaaiello.employerinformation;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.christinaaiello.R;
import com.example.christinaaiello.applicationprocess.UpdateStepsInApplicationProcessActivity;

import static com.example.christinaaiello.employerinformation.DatabaseContract.CompanyDataTable;
import static com.example.christinaaiello.employerinformation.DatabaseContract.DatabaseHelper;


public class ViewCompanyActivity extends ActionBarActivity {
    String TAG = "ViewCompanyActivity "; // Used for log printing
    Employer employer; // Contains employer's information
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private String companyID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_company_activity);

        // Prepping database
        databaseHelper = new DatabaseHelper(getApplicationContext());
        db = databaseHelper.getWritableDatabase();
        employer = new Employer();

        // Getting the company's Name number from the bundle
        Bundle bundle = getIntent().getExtras();
        companyID = bundle.getString("ID");

        // When this textview is clicked, it opens up the steps in the process
        // that a user is (regarding applying to a job)
        TextView positionView = (TextView) findViewById(R.id.company_position);
        positionView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ViewCompanyActivity.this, UpdateStepsInApplicationProcessActivity.class);
                intent.putExtras(createBundleForEditing()); // Putting company info into bundle
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_company, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Intent used for editing mode
        Log.e(TAG, "Making intent for editing");
        Intent intent = new Intent(ViewCompanyActivity.this, EditCompanyActivity.class);
        intent.putExtras(createBundleForEditing()); // Putting company info into bundle
        Log.e(TAG, "Created bundle for editing");

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Log.e(TAG, "Clicked action settings");
            return true;
        } else if (id == R.id.edit_company) {
            Log.e(TAG, "Clicked Edit Company Button");
            startActivity(intent);
        } else {
            Log.e(TAG, "Didn't click anything");
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This will display the data for the chosen company
     */
    public void displayCompanyData() {
        // First we need to get the view objects that are on the screen
        TextView idView = (TextView) findViewById(R.id.company_id);
        TextView nameView = (TextView) findViewById(R.id.company_name);
        TextView positionView = (TextView) findViewById(R.id.company_position);
        TextView sizeView = (TextView) findViewById(R.id.company_size);
        TextView locationView = (TextView) findViewById(R.id.company_location);
        TextView goalView = (TextView) findViewById(R.id.company_goal_mission_statement);
        TextView miscView = (TextView) findViewById(R.id.company_miscellaneous_notes);
        TextView websiteView = (TextView) findViewById(R.id.company_website);
        TextView industryView = (TextView) findViewById(R.id.company_industry);
        TextView overallRatingView = (TextView) findViewById(R.id.company_overall_rating);
        TextView cultureView = (TextView) findViewById(R.id.company_culture_and_values_rating);
        TextView leadershipView = (TextView) findViewById(R.id.company_senior_leadership_rating);
        TextView compensationView = (TextView) findViewById(R.id.company_compensation_rating);
        TextView opportunitiesView = (TextView) findViewById(R.id.company_career_opportunities_rating);
        TextView worklifeView = (TextView) findViewById(R.id.company_work_life_balance_rating);

        // Now we need to set the content of the views
        idView.setText(employer.getID());
        nameView.setText(employer.getName());
        positionView.setText(employer.getPosition());
        sizeView.setText(employer.getSize());
        locationView.setText(employer.getLocation());
        goalView.setText(employer.getGoal());
        websiteView.setText(employer.getWebsite());
        industryView.setText(employer.getIndustry());
        overallRatingView.setText(employer.getOverallRating());
        cultureView.setText(employer.getCultureAndValuesRating());
        leadershipView.setText(employer.getSeniorLeadershipRating());
        compensationView.setText(employer.getCompensationAndBenefitsRating());
        opportunitiesView.setText(employer.getCareerOpportunitiesRating());
        worklifeView.setText(employer.getWorkLifeBalanceRating());
        miscView.setText(employer.getMisc());
    }

    /**
     * This method will read a company's data from the database, based on ID #
     */
    public void readCompanyData(String companyID) {
        String[] projection = {
                CompanyDataTable._ID,
                CompanyDataTable.COLUMN_NAME_NAME,
                CompanyDataTable.COLUMN_NAME_POSITION,
                CompanyDataTable.COLUMN_NAME_SIZE,
                CompanyDataTable.COLUMN_NAME_LOCATION,
                DatabaseContract.CompanyDataTable.COLUMN_NAME_GOAL,
                CompanyDataTable.COLUMN_NAME_MISCELLANEOUS,
                CompanyDataTable.COLUMN_NAME_WEBSITE,
                CompanyDataTable.COLUMN_NAME_INDUSTRY,
                CompanyDataTable.COLUMN_NAME_LOGO,
                CompanyDataTable.COLUMN_NAME_OVERALL_RATING,
                CompanyDataTable.COLUMN_NAME_CULTURE,
                CompanyDataTable.COLUMN_NAME_LEADERSHIP,
                CompanyDataTable.COLUMN_NAME_COMPENSATION,
                CompanyDataTable.COLUMN_NAME_OPPORTUNITIES,
                CompanyDataTable.COLUMN_NAME_WORKLIFE,
        };

        // I only want a company whose ID number matches the one passed to me in a bundle
        String[] selectionArgs = {String.valueOf(companyID)};

        // My cursor that I use to loop over query results
        Cursor cursor = db.query(
                CompanyDataTable.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                CompanyDataTable._ID + "=?",                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        if (!(cursor.getCount() == 0)) {
            Log.i(TAG, "Got results when searching database for companies user has entered.");
            // Now, look into the result and get the data
            cursor.moveToFirst();
            employer.setID(cursor.getString(0));
            employer.setName(cursor.getString(1));
            employer.setPosition(cursor.getString(2));
            employer.setSize(cursor.getString(3));
            employer.setLocation(cursor.getString(4));
            employer.setGoal(cursor.getString(5));
            employer.setMisc(cursor.getString(6));
            employer.setWebsite(cursor.getString(7));
            employer.setIndustry(cursor.getString(8));
            employer.setSquareLogo(cursor.getString(9));
            employer.setOverallRating(cursor.getString(10));
            employer.setCultureAndValuesRating(cursor.getString(11));
            employer.setSeniorLeadershipRating(cursor.getString(12));
            employer.setCompensationAndBenefitsRating(cursor.getString(13));
            employer.setCareerOpportunitiesRating(cursor.getString(14));
            employer.setWorkLifeBalanceRating(cursor.getString(15));
        } else {
            Log.i(TAG, "Could not find matches when searching database for companies user has entered.");
        }

    }

    /**
     * This method will set the links for Glassdoor.com and for the actual company's website.
     */
    public void setLinks() {
        // Getting the textview containing a company's website
        TextView websiteTextView = (TextView) findViewById(R.id.company_website);
        // Getting the text in this textview
        String websiteLink = websiteTextView.getText().toString();
        // Turning the content of this textview into a clickable link:
        websiteTextView.setText(
                Html.fromHtml(
                        "<a href=\"http://" + websiteLink + "\">" + websiteLink + "</a>"));
        websiteTextView.setMovementMethod(LinkMovementMethod.getInstance());

        // Getting the textview containing "courtesy of glassdoor.com"
        TextView subtitleTextView = (TextView) findViewById(R.id.ratings_subtitle);
        // Getting the text in this textview
        String subtitleTextViewContent = subtitleTextView.getText().toString();
        // Turning the content of this textview into a clickable link:
        subtitleTextView.setText(
                Html.fromHtml(
                        "<a href=\"http://glassdoor.com\">" + subtitleTextViewContent + "</a>"));
        subtitleTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * This will create a bundle of information that can be sent to the editing activity.
     */
    public Bundle createBundleForEditing() {
        Bundle bundle = new Bundle();
        Log.e(TAG, "Making bundle for editing!");
        bundle.putString("ID", employer.getID());
        bundle.putString("Name", employer.getName());
        bundle.putString("Position", employer.getPosition());
        bundle.putString("Size", employer.getSize());
        bundle.putString("Location", employer.getLocation());
        bundle.putString("Goal", employer.getGoal());
        bundle.putString("Website", employer.getWebsite());
        bundle.putString("Industry", employer.getIndustry());
        bundle.putString("Overall", employer.getOverallRating());
        bundle.putString("Culture", employer.getCultureAndValuesRating());
        bundle.putString("Leadership", employer.getSeniorLeadershipRating());
        bundle.putString("Compensation", employer.getCompensationAndBenefitsRating());
        bundle.putString("Opportunities", employer.getCareerOpportunitiesRating());
        bundle.putString("Worklife", employer.getWorkLifeBalanceRating());
        bundle.putString("Misc", employer.getMisc());
        Log.e(TAG, "Made bundle for editing!");
        return bundle;
    }

    @Override
    public void onResume() {
        // Displaying the company's data on the screen
        readCompanyData(companyID);
        displayCompanyData();
        setLinks(); // Setting links in activity
        super.onResume();
    }


}
