package rs.devana.labs.studentinfo.presentation.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import rs.devana.labs.studentinfo.R;
import rs.devana.labs.studentinfo.domain.models.lecture.Lecture;
import rs.devana.labs.studentinfo.presentation.adapters.LectureArrayAdapter;
import rs.devana.labs.studentinfo.presentation.adapters.ScheduleFragmentPagerAdapter;

public class WeeklyScheduleFragment extends Fragment {

    ViewPager viewPager;

    public static WeeklyScheduleFragment newInstance() {
        WeeklyScheduleFragment fragment = new WeeklyScheduleFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_weekly_schedule, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        List<Fragment> fragments = new ArrayList<>();
        List<Lecture> lectures1 = new ArrayList<>();
        lectures1.add(new Lecture(1, "Predavanje", null, 124214, 421444, "pera", null, null));
        List<Lecture> lectures2 = new ArrayList<>();
        lectures2.add(new Lecture(2, "Predavanje", null, 124214, 421444, "pera", null, null));
        List<Lecture> lectures3 = new ArrayList<>();
        lectures3.add(new Lecture(3, "Predavanje", null, 124214, 421444, "pera", null, null));
        List<Lecture> lectures4 = new ArrayList<>();
        lectures4.add(new Lecture(4, "Predavanje", null, 124214, 421444, "pera", null, null));
        List<Lecture> lectures5 = new ArrayList<>();
        lectures5.add(new Lecture(5, "Predavanje", null, 124214, 421444, "pera", null, null));
        List<Lecture> lectures6 = new ArrayList<>();
        lectures6.add(new Lecture(6, "Predavanje", null, 124214, 421444, "pera", null, null));
        List<Lecture> lectures7 = new ArrayList<>();
        lectures7.add(new Lecture(7, "Predavanje", null, 124214, 421444, "pera", null, null));
        DayFragment mon = DayFragment.newInstance(1, lectures1);
        DayFragment tue = DayFragment.newInstance(2, lectures2);
        DayFragment wen = DayFragment.newInstance(3, lectures3);
        DayFragment thu = DayFragment.newInstance(4, lectures4);
        DayFragment fri = DayFragment.newInstance(5, lectures5);
        DayFragment sat = DayFragment.newInstance(6, lectures6);
        DayFragment sun = DayFragment.newInstance(7, lectures7);
        fragments.add(mon);
        fragments.add(tue);
        fragments.add(wen);
        fragments.add(thu);
        fragments.add(fri);
        fragments.add(sat);
        fragments.add(sun);
        fragments.add(NotificationsFragment.newInstance());


        ScheduleFragmentPagerAdapter scheduleFragmentPagerAdapter = new ScheduleFragmentPagerAdapter(this.getActivity().getSupportFragmentManager(), fragments);
        viewPager.setAdapter(scheduleFragmentPagerAdapter);

        scheduleFragmentPagerAdapter.notifyDataSetChanged();

        return view;
    }
}
