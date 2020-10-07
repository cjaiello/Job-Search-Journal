package jobsearchjournal.applicationprocess;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import jobsearchjournal.R;

import java.util.List;

/**
 * Created by Christina Aiello on 3/23/2015.
 */
public class InterviewAdapter extends ArrayAdapter<Interview> {
    private List<Interview> interviewList;
    private Context context;
    private String TAG;

    public InterviewAdapter(List<Interview> interviewList, Context ctx) {
        super(ctx, R.layout.set_up_interview_fragment, interviewList);
        this.interviewList = interviewList;
        this.context = ctx;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TAG = "InterviewAdapter";
        // First let's verify the convertView is not null
        if (convertView == null) {
            // This a new view we inflate the new layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.set_up_interview_fragment, parent, false);
        }
        // Now we can fill the layout with the right values
        // Getting each of the text boxes for this fragment:
        TextView interviewID = (TextView) convertView.findViewById(R.id.interview_id);
        TextView dateOfInterview = (TextView) convertView.findViewById(R.id.date_of_interview);
        TextView timeOfInterview = (TextView) convertView.findViewById(R.id.time_of_interview);
        TextView interviewerNames = (TextView) convertView.findViewById(R.id.names_of_interviewers);
        final TextView contactEmailAddress = (TextView) convertView.findViewById(R.id.contact_email_address);
        TextView miscNotes = (TextView) convertView.findViewById(R.id.miscellaneous_notes);
        TextView followupNotes = (TextView) convertView.findViewById(R.id.followup_notes);

        // Getting the current interview object:
        Interview interview = interviewList.get(position);
        // And setting info on screen based on this interview's information:
        interviewID.setText(interview.getInterviewID());
        dateOfInterview.setText(interview.getDate());
        timeOfInterview.setText(interview.getTime());
        interviewerNames.setText(interview.getInterviewerNames());
        contactEmailAddress.setText(Html.fromHtml("<u>" + interview.getContactEmailAddress() + "</u>"));
        TextView emailAddress = (TextView)convertView.findViewById(R.id.contact_email_address);
        // Letting the user immediately email this individual:
        emailAddress.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{contactEmailAddress.getText().toString()});
                emailIntent.setType("message/rfc822");
                context.startActivity(emailIntent);
            }
        });
        miscNotes.setText(interview.getMiscNotes());
        followupNotes.setText(interview.getInterviewCompletedNotes());

        return convertView;
    }
}
