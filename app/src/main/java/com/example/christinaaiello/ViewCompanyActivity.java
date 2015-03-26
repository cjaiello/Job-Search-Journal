package com.example.christinaaiello;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.christinaaiello.employerinformation.Employer;

import static com.example.christinaaiello.DatabaseContract.DatabaseEntry;
import static com.example.christinaaiello.DatabaseContract.DatabaseHelper;


public class ViewCompanyActivity extends FragmentActivity {
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
        // Displaying the company's data on the screen
        readCompanyData(companyID);
        displayCompanyData();

        setLinks(); // Setting links in activity
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
                DatabaseEntry._ID,
                DatabaseEntry.COLUMN_NAME_NAME,
                DatabaseEntry.COLUMN_NAME_POSITION,
                DatabaseEntry.COLUMN_NAME_SIZE,
                DatabaseEntry.COLUMN_NAME_LOCATION,
                DatabaseEntry.COLUMN_NAME_GOAL,
                DatabaseEntry.COLUMN_NAME_MISCELLANEOUS,
                DatabaseEntry.COLUMN_NAME_WEBSITE,
                DatabaseEntry.COLUMN_NAME_INDUSTRY,
                DatabaseEntry.COLUMN_NAME_LOGO,
                DatabaseEntry.COLUMN_NAME_OVERALL_RATING,
                DatabaseEntry.COLUMN_NAME_CULTURE,
                DatabaseEntry.COLUMN_NAME_LEADERSHIP,
                DatabaseEntry.COLUMN_NAME_COMPENSATION,
                DatabaseEntry.COLUMN_NAME_OPPORTUNITIES,
                DatabaseEntry.COLUMN_NAME_WORKLIFE,
        };

        // I only want a company whose ID number matches the one passed to me in a bundle
        String[] selectionArgs = {String.valueOf(companyID)};

        // My cursor that I use to loop over query results
        Cursor cursor = db.query(
                DatabaseEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                DatabaseEntry._ID + "=?",                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        if (!(cursor.getCount() == 0)) {
            Log.e("results", "HAD RESULTS!!!!!!");
            // Now, look into the result and get the data
            cursor.moveToFirst();
            for (int i = 0; i < 16; i++) {
                Log.e("Cursor", "Value is: " + cursor.getString(i));
            }
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
            Log.e("results", "NO RESULTs ahhhhhhhhhhhhh");
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


}
