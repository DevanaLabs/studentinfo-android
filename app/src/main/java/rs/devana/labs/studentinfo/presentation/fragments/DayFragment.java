package rs.devana.labs.studentinfo.presentation.fragments;


import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rs.devana.labs.studentinfo.R;
import rs.devana.labs.studentinfo.domain.models.lecture.Lecture;
import rs.devana.labs.studentinfo.presentation.views.LectureView;
import rs.devana.labs.studentinfo.presentation.views.LecturesView;
import rs.devana.labs.studentinfo.presentation.weekview.MonthLoader;
import rs.devana.labs.studentinfo.presentation.weekview.WeekView;
import rs.devana.labs.studentinfo.presentation.weekview.WeekViewEvent;

public class DayFragment extends Fragment implements WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener {
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

//        if (!lectures.isEmpty()) {
//            Collections.sort(lectures, new Comparator<Lecture>() {
//                @Override
//                public int compare(Lecture lhs, Lecture rhs) {
//                    return lhs.getStartsAt() - rhs.getStartsAt();
//                }
//            });
//            LectureArrayAdapter lectureArrayAdapter = new LectureArrayAdapter(lectures, this.getActivity());
//            lecturesListView.setAdapter(lectureArrayAdapter);
//        }
//        else {
//            TextView noClassesToday = (TextView) view.findViewById(R.id.listEmptyView);
//            noClassesToday.setVisibility(View.VISIBLE);
//            lecturesListView.setEmptyView(noClassesToday);
//        }

        return view;
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


    @Override
    public void onEmptyViewLongPress(Calendar time) {

    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {

    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {

    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        List<WeekViewEvent> events = new ArrayList<>();

        for (int i = 0; i < lectures.size(); i++) {
            LectureView lectureView = new LectureView(lectures.get(i));
            if (lectures.get(i).getType() == 0) {
                lectureView.setColor(R.color.green);
            } else {
                lectureView.setColor(R.color.orange);
            }
            events.add(lectureView);
        }
        return events;
    }
}
