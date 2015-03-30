package com.example.christinaaiello.applicationprocess;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.christinaaiello.R;
import com.example.christinaaiello.employerinformation.DatabaseContract;


public class UpdateStepsInApplicationProcessEditModeActivity extends ActionBarActivity {
    String step; // The step we're currently on
    String TAG;
    private DatabaseContract.DatabaseHelper databaseHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = "UpdateStepsInApplicationProcessEditModeActivity";
        setContentView(R.layout.update_steps_in_application_process_editmode_activity);

        // Initialize Database objects
        databaseHelper = new DatabaseContract.DatabaseHelper(getApplicationContext());
        db = databaseHelper.getWritableDatabase();

        // Getting the step being modified from the bundle
        Bundle bundle = getIntent().getExtras();
        step = bundle.getString("Step");

        //initializeFragments(step);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_step, menu);
        super.onCreateOptionsMenu(menu);
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
     * This method will initialize the fragments in this layout
     */
    /*public void initializeFragments(String step) {

        Log.e("Did we get here?", "Yes");

        // Initializing the fragment for initial contact with a company
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // If it's the first step in the process:
        if (step.equals("initial")) {
            // Initializing the fragment for initial contact with a company
            InitialContactFragmentEditMode initialContactFragmentEditMode = new InitialContactFragmentEditMode();
            transaction.add(R.id.initial_contact_fragment, initialContactFragment);
            transaction.commit();
        } else if (step.equals("setup")) {
            // Initializing the fragment for scheduling an interview with a company
            InitialContactFragmentEditMode setUpInterviewFragmentEditMode = new SetUpContactFragmentEditMode();
            transaction.add(R.id.set_up_interview_fragment, setUpInterviewFragment);
            transaction.commit();
        }

    }*/
}
