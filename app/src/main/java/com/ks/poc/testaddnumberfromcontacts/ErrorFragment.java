package com.ks.poc.testaddnumberfromcontacts;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Krit on 7/6/2016.
 */
public class ErrorFragment extends Fragment {
    public TextView tv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.error_view, container, false);
        tv = (TextView) view.findViewById(R.id.errorTextView);
        return view;
    }
}
