package com.ks.poc.testaddnumberfromcontacts;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by Krit on 7/6/2016.
 */
public class ListFragment extends Fragment {

    public ListView lvResult;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.list_view, container, false);
        lvResult = (ListView) view.findViewById(R.id.listSearchResult);
        return view;
    }
}
