package rs.devana.labs.studentinfo.presentation.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import rs.devana.labs.studentinfo.R;
import rs.devana.labs.studentinfo.domain.models.lecture.Lecture;
import rs.devana.labs.studentinfo.presentation.views.LecturesView;

public class DayFragment extends Fragment {
    private int day;
    private List<Lecture> lectures;

    public DayFragment() {
    }

    public static DayFragment newInstance(int day, List<Lecture> lectures) {
        DayFragment fragment = new DayFragment();
        fragment.setDay(day);
        fragment.setLectures(lectures);

        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_day, container, false);
        LecturesView lecturesView = (LecturesView) view.findViewById(R.id.lecturesView);
        lecturesView.setLectures(lectures);
        lecturesView.setDay(day);

        return view;
    }


    public void setDay(int day) {
        this.day = day;
    }

    public void setLectures(List<Lecture> lectures) {
        this.lectures = lectures;
    }


}
