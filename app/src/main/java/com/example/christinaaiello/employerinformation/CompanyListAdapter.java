package com.example.christinaaiello.employerinformation;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.christinaaiello.R;

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
        TextView positionLabelView = (TextView) convertView.findViewById(R.id.position_label);
        TextView positionView = (TextView) convertView.findViewById(R.id.position);
        TextView stepView = (TextView) convertView.findViewById(R.id.step);
        Employer employer = employerList.get(position);

        Log.i("Inside Adapter name", "Position is: " + Integer.toString(position));
        // Now let's set the text on the screen for this employer:
        companyIDView.setText(employer.getID());
        Log.i("Inside Adapter, name:", employer.getName());
        companyNameView.setText(employer.getName());
        websiteView.setText(employer.getWebsite());

        // If they don't put in a website, hide this field
        if (employer.getWebsite() == null) {
            websiteView.setVisibility(View.GONE);
        } else if (employer.getWebsite().length() == 0) {
            websiteView.setVisibility(View.GONE);
        } else {
            // There is a website, so show this field:
            websiteView.setVisibility(View.VISIBLE);
        }

        positionView.setText(employer.getPosition());
        // If they don't put in a position, hide this field
        if (employer.getPosition().length() == 0) {
            positionView.setVisibility(View.GONE);
            positionLabelView.setVisibility(View.GONE);
        } else {
            // They did enter a position, so show this field:
            positionView.setVisibility(View.VISIBLE);
            positionLabelView.setVisibility(View.VISIBLE);
        }

        // If they have started, show most recent step, else don't
        if (employer.getStep() != null) {
            stepView.setText(employer.getStep());
        } else {
            stepView.setText("Not started");
        }

        return convertView;
    }
}
