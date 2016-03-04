package rs.devana.labs.studentinfo.presentation.fragments;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rs.devana.labs.studentinfo.R;
import rs.devana.labs.studentinfo.infrastructure.dagger.Injector;
import rs.devana.labs.studentinfo.infrastructure.json.parser.LectureParser;
import rs.devana.labs.studentinfo.presentation.adapters.ScheduleFragmentPagerAdapter;

public class WeeklyScheduleFragment extends Fragment {

    @Inject
    LectureParser lectureParser;

    @Inject
    SharedPreferences sharedPreferences;

    ViewPager viewPager;
    List<Fragment> fragments;
    ScheduleFragmentPagerAdapter scheduleFragmentPagerAdapter;

    private static final int MONDAY = 0;
    private static final int TUESDAY = 1;
    private static final int WEDNESDAY = 2;
    private static final int THURSDAY = 3;
    private static final int FRIDAY = 4;
    private static final int SATURDAY = 5;
    private static final int SUNDAY = 6;

    public static WeeklyScheduleFragment newInstance() {
        WeeklyScheduleFragment fragment = new WeeklyScheduleFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.INSTANCE.getApplicationComponent().inject(this);
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
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);

        String lectures = sharedPreferences.getString("lectures", "");
        fragments = new ArrayList<>();

        try {
            fragments.add(DayFragment.newInstance(MONDAY , lectureParser.getLecturesForDay(MONDAY, new JSONArray(lectures))));
            fragments.add(DayFragment.newInstance(TUESDAY , lectureParser.getLecturesForDay(TUESDAY, new JSONArray(lectures))));
            fragments.add(DayFragment.newInstance(WEDNESDAY , lectureParser.getLecturesForDay(WEDNESDAY, new JSONArray(lectures))));
            fragments.add(DayFragment.newInstance(THURSDAY , lectureParser.getLecturesForDay(THURSDAY, new JSONArray(lectures))));
            fragments.add(DayFragment.newInstance(FRIDAY , lectureParser.getLecturesForDay(FRIDAY, new JSONArray(lectures))));
            fragments.add(DayFragment.newInstance(SATURDAY , lectureParser.getLecturesForDay(SATURDAY, new JSONArray(lectures))));
            fragments.add(DayFragment.newInstance(SUNDAY , lectureParser.getLecturesForDay(SUNDAY, new JSONArray(lectures))));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        scheduleFragmentPagerAdapter = new ScheduleFragmentPagerAdapter(this.getActivity().getSupportFragmentManager(), fragments);
        viewPager.setAdapter(scheduleFragmentPagerAdapter);
        scheduleFragmentPagerAdapter.notifyDataSetChanged();
        tabLayout.setTabTextColors(Color.LTGRAY, Color.WHITE);
        tabLayout.setBackgroundColor(Color.parseColor("#3F51B5"));
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}
