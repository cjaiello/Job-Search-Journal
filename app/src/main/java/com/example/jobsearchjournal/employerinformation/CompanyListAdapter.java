package com.example.jobsearchjournal.employerinformation;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jobsearchjournal.R;

import java.util.List;

public class CompanyListAdapter extends RecyclerView.Adapter<CompanyListAdapter.ViewHolder> {
    private List<Employer> employerList;

    public CompanyListAdapter(List<Employer> employerList) {
        this.employerList = employerList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout innerLayout;
        TextView companyIDView;
        TextView companyNameView;
        TextView websiteView;
        TextView positionLabelView;
        TextView positionView;
        ImageView positionImageView;
        TextView stepView;
        ImageView companyLogo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            innerLayout = (RelativeLayout) itemView.findViewById(R.id.relative_layout);
            companyIDView = (TextView) itemView.findViewById(R.id.id);
            companyNameView = (TextView) itemView.findViewById(R.id.name);
            websiteView = (TextView) itemView.findViewById(R.id.website);
            positionLabelView = (TextView) itemView.findViewById(R.id.position_label);
            positionView = (TextView) itemView.findViewById(R.id.position);
            positionImageView = (ImageView) itemView.findViewById(R.id.position_image);
            stepView = (TextView) itemView.findViewById(R.id.step);
            companyLogo = (ImageView) itemView.findViewById(R.id.companyLogo);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_main_listitem, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        //Getting current employer from this position
        Employer employer = employerList.get(position);

        // Now we can fill the layout with the right values
        // Logo for company:
        if (employer.getLogoByteArray() != null) {
            holder.companyLogo.setImageBitmap(BitmapFactory.decodeByteArray(employer.getLogoByteArray(), 0, employer.getLogoByteArray().length));
            holder.companyLogo.setVisibility(View.VISIBLE);
        } else holder.companyLogo.setVisibility(View.INVISIBLE);
        // Now let's set the text on the screen for this employer:
        holder.companyIDView.setText(employer.getID());
        holder.companyNameView.setText(employer.getName());
        holder.websiteView.setText(employer.getWebsite());

        // If they don't put in a website, hide this field
        if (employer.getWebsite() == null) {
            holder.websiteView.setVisibility(View.GONE);
        } else if (employer.getWebsite().length() == 0) {
            holder.websiteView.setVisibility(View.GONE);
        } else {
            // There is a website, so show this field:
            holder.websiteView.setVisibility(View.VISIBLE);
        }

        holder.positionView.setText(employer.getPosition());
        // If they don't put in a position, hide this field
        if (employer.getPosition().length() == 0) {
            holder.positionView.setVisibility(View.GONE);
            holder.positionLabelView.setVisibility(View.GONE);
            holder.positionImageView.setVisibility(View.GONE);
        } else {
            // They did enter a position, so show this field:
            holder.positionView.setVisibility(View.VISIBLE);
            holder.positionLabelView.setVisibility(View.VISIBLE);
            holder.positionImageView.setVisibility(View.VISIBLE);
        }

        // If they have started, show most recent step, else don't
        if (employer.getStep() != null) {
            holder.stepView.setText(employer.getStep());
        } else {
            holder.stepView.setText("Not started");
        }

        // Using a setOnClickListener here since it's easier to get all needed info
        // With that, we don't need to use a callback and it makes our Activity less messy
        holder.innerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Making an intent to open ViewCompanyActivity
                Intent startViewCompanyIntent = new Intent(v.getContext(), ViewCompanyActivity.class);
                // Bundle to store things in
                Bundle bundle = new Bundle();
                // Use this name when starting a new activity
                bundle.putString("ID", holder.companyIDView.getText().toString());
                startViewCompanyIntent.putExtras(bundle);
                // Creating an intent to start the window
                v.getContext().startActivity(startViewCompanyIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return employerList != null ? employerList.size() : 0;
    }
}
