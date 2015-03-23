package com.example.christinaaiello;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.christinaaiello.employerinformation.Employer;


public class AddCompanyActivity extends FragmentActivity {
    EditText employerNameEditText; // Box where user types employer's name
    Employer employer; // Will contain an employer's information

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_company_activity);

        employer = new Employer();

        // Getting the edit text box where user types employer's name
        employerNameEditText = (EditText) findViewById(R.id.company_name);

        // Button used to save changes made
        Button saveButton = (Button) findViewById(R.id.save_button);

        // Save button's onClickListener
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    saveToDatabase();
                } catch (InterruptedException e) {
                    Log.e("Database Error", "Error in saving to database: " + e.toString());
                }
            }
        });


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
     * This method will save a company's information to the database
     */
    public void saveToDatabase() throws InterruptedException {
        // URL for the company's information:
        String URL = constructAPIURL(employerNameEditText.getText().toString());
        Log.e("String", "String is: " + URL);
        // Fetching the company's information:
        employer.fetchCompanyInformation(URL);
        Log.e("Company website", "Company website is: " + employer.getWebsite());
    }

    /**
     * This method will construct the necessary API URL for a specific company, by name.
     *
     * @param companyName is the name of the company whose information we want
     * @return the URL to get the company's information
     */
    public String constructAPIURL(String companyName) {
        // First, we trim any leading or trailing spaces off the name
        String trimmedName = companyName.trim();
        // Next, we need to replace any middle spaces with %20's
        String noSpacesInName = trimmedName.replaceAll(" ", "%20");
        // Lastly, return the URL for this company name
        return "http://api.glassdoor.com/api/api.htm?v=1&format=json&t.p=31746&t.k=f6EKkHN4wb9&action=employers&q=" + noSpacesInName + "&userip=192.168.43.42&useragent=Mozilla/%2F4.0";
    }
}
