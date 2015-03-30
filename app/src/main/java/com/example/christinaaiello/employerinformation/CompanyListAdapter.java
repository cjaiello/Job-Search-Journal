package com.example.christinaaiello.employerinformation;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.christinaaiello.R;
import com.example.christinaaiello.employerinformation.Employer;

import java.util.List;

/**
 * Created by Christina Aiello on 3/23/2015.
 */
public class CompanyListAdapter extends ArrayAdapter<Employer> {
    private List<Employer> employerList;
    private Context context;

    public CompanyListAdapter(List<Employer> employerList, Context ctx) {
        super(ctx, R.layout.activity_main_listitem, employerList);
        this.employerList = employerList;
        this.context = ctx;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // First let's verify the convertView is not null
        if (convertView == null) {
            // This a new view we inflate the new layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_main_listitem, parent, false);
        }
        // Now we can fill the layout with the right values
        TextView companyIDView = (TextView) convertView.findViewById(R.id.id);
        TextView companyNameView = (TextView) convertView.findViewById(R.id.name);
        TextView websiteView = (TextView) convertView.findViewById(R.id.website);
        TextView positionView = (TextView) convertView.findViewById(R.id.position);
        Employer employer = employerList.get(position);
        Log.e("Employerlist", employerList.toString());
        Log.e("Inside Adapter name", "Position is: " + Integer.toString(position));

        companyIDView.setText(employer.getID());
        Log.e("Inside Adapter, name:", employer.getName());
        companyNameView.setText(employer.getName());
        websiteView.setText(employer.getWebsite());
        positionView.setText("Position Applied For: " + employer.getPosition());

        return convertView;
    }
}
