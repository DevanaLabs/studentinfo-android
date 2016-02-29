package rs.devana.labs.studentinfo.presentation.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rs.devana.labs.studentinfo.R;

public class YearlyCalendarFragment extends Fragment {

    public static YearlyCalendarFragment newInstance() {
        YearlyCalendarFragment fragment = new YearlyCalendarFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_yearly_calendar, container, false);
    }

}
