package com.example.christinaaiello.applicationprocess;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.christinaaiello.R;

/**
 * Created by Christina Aiello on 3/27/2015.
 */
public class InitialContactFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.initial_contact_fragment, container, false);
    }

}
