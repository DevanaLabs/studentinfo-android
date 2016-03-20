package rs.devana.labs.studentinfo.presentation.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import rs.devana.labs.studentinfo.R;
import rs.devana.labs.studentinfo.domain.models.lecture.Lecture;
import rs.devana.labs.studentinfo.infrastructure.dagger.Injector;
import rs.devana.labs.studentinfo.infrastructure.event_bus_events.ChooseGroupEvent;
import rs.devana.labs.studentinfo.infrastructure.json.parser.LectureParser;
import rs.devana.labs.studentinfo.presentation.adapters.ScheduleFragmentPagerAdapter;

public class WeeklyScheduleFragment extends Fragment {

    @Inject
    LectureParser lectureParser;

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    EventBus eventBus;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weekly_schedule, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);

        boolean sat = false, sun = false;
        String lectures = sharedPreferences.getString("lectures", "");
        fragments = new ArrayList<>();
        try {
            JSONArray jsonLectures = new JSONArray(lectures);
            fragments.add(DayFragment.newInstance(MONDAY, lectureParser.getLecturesForDay(MONDAY, jsonLectures)));
            fragments.add(DayFragment.newInstance(TUESDAY, lectureParser.getLecturesForDay(TUESDAY, jsonLectures)));
            fragments.add(DayFragment.newInstance(WEDNESDAY, lectureParser.getLecturesForDay(WEDNESDAY, jsonLectures)));
            fragments.add(DayFragment.newInstance(THURSDAY, lectureParser.getLecturesForDay(THURSDAY, jsonLectures)));
            fragments.add(DayFragment.newInstance(FRIDAY, lectureParser.getLecturesForDay(FRIDAY, jsonLectures)));
            List<Lecture> saturdayLectures, sundayLectures;
            if (!(saturdayLectures = lectureParser.getLecturesForDay(SATURDAY, jsonLectures)).isEmpty()) {
                fragments.add(DayFragment.newInstance(SATURDAY, saturdayLectures));
                sat = true;
            }
            if (!(sundayLectures = lectureParser.getLecturesForDay(SUNDAY, jsonLectures)).isEmpty()) {
                fragments.add(DayFragment.newInstance(SUNDAY, sundayLectures));
                sun = true;
            }
        } catch (JSONException e) {
            createChooseGroupDialog().show();
            e.printStackTrace();
        }

        scheduleFragmentPagerAdapter = new ScheduleFragmentPagerAdapter(this.getActivity().getSupportFragmentManager(), fragments, getContext());
        viewPager.setAdapter(scheduleFragmentPagerAdapter);
        scheduleFragmentPagerAdapter.notifyDataSetChanged();
        tabLayout.setTabTextColors(Color.LTGRAY, Color.WHITE);
        tabLayout.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.colorPrimary));
        tabLayout.setupWithViewPager(viewPager);
        setTab(viewPager, sat, sun);

        return view;
    }

    private AlertDialog createChooseGroupDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.chooseGroup);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                eventBus.post(new ChooseGroupEvent());
                dialog.dismiss();
            }
        });

        return builder.create();
    }


    private void setTab(ViewPager viewPager, boolean sat, boolean sun) {
        Calendar calendar = Calendar.getInstance();
        String dayLongName = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
        switch (dayLongName) {
            case "Mon":
                viewPager.setCurrentItem(0);
                break;
            case "Tue":
                viewPager.setCurrentItem(1);
                break;
            case "Wed":
                viewPager.setCurrentItem(2);
                break;
            case "Thu":
                viewPager.setCurrentItem(3);
                break;
            case "Fri":
                viewPager.setCurrentItem(4);
                break;
            case "Sat":
                if (sat) viewPager.setCurrentItem(5);
                break;
            case "Sun":
                if (sun) viewPager.setCurrentItem(6);
                break;
            default:
                viewPager.setCurrentItem(0);
                break;
        }
    }
}
