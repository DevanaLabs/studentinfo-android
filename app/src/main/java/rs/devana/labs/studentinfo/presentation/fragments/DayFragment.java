package rs.devana.labs.studentinfo.presentation.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import rs.devana.labs.studentinfo.R;
import rs.devana.labs.studentinfo.domain.models.lecture.Lecture;
import rs.devana.labs.studentinfo.domain.models.notification.Notification;
import rs.devana.labs.studentinfo.presentation.adapters.LectureArrayAdapter;
import rs.devana.labs.studentinfo.presentation.adapters.NotificationArrayAdapter;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_day, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        ListView lecturesListView = (ListView) this.getActivity().findViewById(R.id.lecturesListView);
        LectureArrayAdapter lectureArrayAdapter = new LectureArrayAdapter(lectures, this.getActivity());
        lecturesListView.setAdapter(lectureArrayAdapter);
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public List<Lecture> getLectures() {
        return lectures;
    }

    public void setLectures(List<Lecture> lectures) {
        this.lectures = lectures;
    }


}
