package jobsearchjournal.employerinformation;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import jobsearchjournal.R;
import jobsearchjournal.applicationprocess.TrackYourProgressActivity;
import jobsearchjournal.general.DatabaseContract;

import java.io.IOException;

import static jobsearchjournal.general.DatabaseContract.CompanyDataTable;
import static jobsearchjournal.general.DatabaseContract.DatabaseHelper;


public class ViewCompanyActivity extends AppCompatActivity {
    String TAG = "ViewCompanyActivity "; // Used for log printing
    Employer employer; // Contains employer's information
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private String companyID; // in-app ID # for this company
    private double latitude; // User's current latitude
    private double longitude; // User's current longitude
    private LocationManager locationManager; // Used to get user's location
    // Views on screen:
    TextView idView;
    TextView positionView;
    TextView sizeView;
    TextView goalView;
    TextView miscView;
    TextView websiteView;
    TextView industryView;
    TextView overallRatingView;
    TextView cultureView;
    TextView leadershipView;
    TextView compensationView;
    TextView opportunitiesView;
    TextView worklifeView;
    ImageView companyLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_company_activity);

        // Initializing our location manager, letting us access device's location
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Prepping database
        databaseHelper = new DatabaseHelper(getApplicationContext());
        db = databaseHelper.getWritableDatabase();
        employer = new Employer();

        // Getting the company's Name number from the bundle
        Bundle bundle = getIntent().getExtras();
        companyID = bundle.getString("ID");
        //Log.i(TAG, "Company ID is... " + companyID);

        // When this textview is clicked, it opens up the steps in the process
        // that a user is (regarding applying to a job)
        TextView trackProgressText = (TextView) findViewById(R.id.track_progress_text);
        trackProgressText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewCompanyProgress();
            }
        });
    }

    public void viewCompanyProgress(final View view) {
        Intent intent = new Intent(ViewCompanyActivity.this, TrackYourProgressActivity.class);
        intent.putExtras(createBundleForEditing()); // Putting company info into bundle
        startActivity(intent);
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
        //Log.i(TAG, "Making intent for editing");
        Intent intent = new Intent(ViewCompanyActivity.this, EditCompanyActivity.class);
        intent.putExtras(createBundleForEditing()); // Putting company info into bundle
        //Log.i(TAG, "Created bundle for editing");

        //noinspection SimplifiableIfStatement
        if (id == R.id.edit_company) {
            //Log.i(TAG, "Clicked Edit Company Button");
            startActivity(intent);
        } else {
            //Log.i(TAG, "Didn't click anything");
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This will display the data for the chosen company
     */
    public void displayCompanyData() {
        // First we need to get the view objects that are on the screen
        idView = (TextView) findViewById(R.id.company_id);
        positionView = (TextView) findViewById(R.id.company_position);
        sizeView = (TextView) findViewById(R.id.company_size);
        goalView = (TextView) findViewById(R.id.company_goal_mission_statement);
        miscView = (TextView) findViewById(R.id.company_miscellaneous_notes);
        websiteView = (TextView) findViewById(R.id.company_website);
        industryView = (TextView) findViewById(R.id.company_industry);
        overallRatingView = (TextView) findViewById(R.id.company_overall_rating);
        cultureView = (TextView) findViewById(R.id.company_culture_and_values_rating);
        leadershipView = (TextView) findViewById(R.id.company_senior_leadership_rating);
        compensationView = (TextView) findViewById(R.id.company_compensation_rating);
        opportunitiesView = (TextView) findViewById(R.id.company_career_opportunities_rating);
        worklifeView = (TextView) findViewById(R.id.company_work_life_balance_rating);
        companyLogo = (ImageView) findViewById(R.id.company_logo);

        // Now we need to set the content of the views
        idView.setText(employer.getID());
        sizeView.setText(employer.getSize());
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
        if (employer.getLogoByteArray() != null) {
            companyLogo.setImageBitmap(BitmapFactory.decodeByteArray(employer.getLogoByteArray(), 0, employer.getLogoByteArray().length));
        }

        // Giving this activity a new action bar title:
        setTitle(employer.getName());
    }

    /**
     * This method will read a company's data from the database, based on ID #
     */
    public void readCompanyData(String companyID) throws IOException {
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
                CompanyDataTable.COLUMN_NAME_STREET_VIEW,
                CompanyDataTable.COLUMN_NAME_LINK_TO_JOB_POSTING,
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
            //Log.i(TAG, "Got results when searching database for companies user has entered.");
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
            employer.setLogoByteArray(cursor.getBlob(9));
            employer.setOverallRating(cursor.getString(10));
            employer.setCultureAndValuesRating(cursor.getString(11));
            employer.setSeniorLeadershipRating(cursor.getString(12));
            employer.setCompensationAndBenefitsRating(cursor.getString(13));
            employer.setCareerOpportunitiesRating(cursor.getString(14));
            employer.setWorkLifeBalanceRating(cursor.getString(15));
            employer.setStreetByteArray(cursor.getBlob(16));
            employer.setLinkToJobPosting(cursor.getString(17));
        } else {
            //Log.i(TAG, "Could not find matches when searching database for companies user has entered.");
        }

    }

    /**
     * This method will set the links for Glassdoor.com and for the actual company's website.
     */
    public void setLinks() {
        if ((employer.getWebsite() != null) && (employer.getWebsite().length() != 0)) {
            // Getting the textview containing a company's website
            TextView websiteTextView = (TextView) findViewById(R.id.company_website);
            // Getting the text in this textview
            String websiteLink = websiteTextView.getText().toString();
            // Turning the content of this textview into a clickable link:
            websiteTextView.setText(
                    Html.fromHtml(
                            "<a href=\"http://" + websiteLink + "\">" + websiteLink + "</a>"));
            websiteTextView.setMovementMethod(LinkMovementMethod.getInstance());
            // Lastly, let's display the internet icon image:
            ImageView internetIcon = (ImageView) findViewById(R.id.internet_icon);
            internetIcon.setVisibility(View.VISIBLE);
        }

        // Getting the textview containing "courtesy of glassdoor.com"
        TextView subtitleTextView = (TextView) findViewById(R.id.ratings_subtitle);
        // Getting the text in this textview
        String subtitleTextViewContent = subtitleTextView.getText().toString();
        // Turning the content of this textview into a clickable link:
        subtitleTextView.setText(
                Html.fromHtml(
                        "<a href=\"http://glassdoor.com\">" + subtitleTextViewContent + "</a>"));
        subtitleTextView.setMovementMethod(LinkMovementMethod.getInstance());

        // Lastly, let's make the location look like a clickable link, so people will click it
        if (employer.getLocation().length() != 0) {
            String locationText = "<u>" + employer.getLocation() + "</u>";
            TextView locationView = (TextView) findViewById(R.id.company_location);
            locationView.setText(Html.fromHtml(locationText));

            // Making this box larger and clickable
            RelativeLayout locationLayout = (RelativeLayout) findViewById(R.id.company_location_layout);
            ViewGroup.LayoutParams params = locationLayout.getLayoutParams();
            params.height = 600;
            locationLayout.setLayoutParams(params);
            // And this needs to actually be clickable:
            locationLayout.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    openMap(view);
                }
            });

            // If we have a streetview image, use that, else show the map icon:
            ImageView companyStreetView = (ImageView) findViewById(R.id.company_street_view);
            if (employer.getStreetByteArray() != null) {
                companyStreetView.setImageBitmap(BitmapFactory.decodeByteArray(employer.getStreetByteArray(), 0, employer.getStreetByteArray().length));

            } else {
                // Making the map icon visible:
                ImageView mapIcon = (ImageView) findViewById(R.id.map_icon);
                mapIcon.setVisibility(View.VISIBLE);
            }
        }
        
        // Turning the position applied for into a link to a specific position
        if(employer.getLinkToJobPosting().length() > 0){
            //Log.i(TAG, "Link to job posting exists! Let's make this a link now.");
            //Log.i(TAG, employer.getLinkToJobPosting());
            positionView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (!employer.getLinkToJobPosting().startsWith("http://") && !employer.getLinkToJobPosting().startsWith("https://")){
                        employer.setLinkToJobPosting("http://" + employer.getLinkToJobPosting());
                    }
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(employer.getLinkToJobPosting()));
                    startActivity(intent);
                }
            });
            positionView.setText(employer.getPosition());
            // Give this link the proper formatting:
            positionView.setTextColor(getResources().getColorStateList(R.color.link_color));
            positionView.setTypeface(null, Typeface.BOLD);
            positionView.setPaintFlags(positionView.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        } else {
            // Else just have it list the position without a link to anywhere
            positionView.setText(employer.getPosition());
            //Log.i(TAG, "Link to job posting was null");
        }
    }

    /**
     * This method will open up a map with this company's location, when the "location" is clicked in the layout.
     */
    public void openMap(View view) {
        Uri uri = Uri.parse("http://maps.google.com/maps?saddr=" + latitude + "," + longitude + "&daddr=" + employer.getLocation());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    /**
     * This will create a bundle of information that can be sent to the editing activity.
     */
    public Bundle createBundleForEditing() {
        Bundle bundle = new Bundle();
        //Log.i(TAG, "Making bundle for editing!");
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
        bundle.putString("LinkToJobPosting", employer.getLinkToJobPosting());
        //Log.i(TAG, "Made bundle for editing!");
        return bundle;
    }

    @Override
    public void onResume() {
        // Displaying the company's data on the screen
        try {
            readCompanyData(companyID);
        } catch (IOException e) {

        }
        displayCompanyData();
        setLinks(); // Setting links in activity

        // Telling the location manager to start listening for location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // Moving the "camera" (what google maps is showing) to the location's current latitude and longitude
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
                // TODO Auto-generated method stub
            }
        });

        super.onResume();
    }

    @Override
    // When we get destroyed we should clean up after ourselves
    protected void onDestroy() {
        super.onDestroy();
    }


}
