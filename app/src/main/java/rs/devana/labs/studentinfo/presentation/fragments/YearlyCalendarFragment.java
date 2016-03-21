package rs.devana.labs.studentinfo.presentation.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import rs.devana.labs.studentinfo.R;
import rs.devana.labs.studentinfo.domain.models.event.Event;
import rs.devana.labs.studentinfo.infrastructure.dagger.Injector;
import rs.devana.labs.studentinfo.infrastructure.json.parser.EventParser;
import rs.devana.labs.studentinfo.presentation.adapters.EventArrayAdapter;

public class YearlyCalendarFragment extends Fragment {

    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    EventParser eventParser;

    public static YearlyCalendarFragment newInstance() {
        YearlyCalendarFragment fragment = new YearlyCalendarFragment();
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
        return inflater.inflate(R.layout.fragment_yearly_calendar, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        ListView eventsListView = (ListView) this.getActivity().findViewById(R.id.eventsListView);
        List<Event> eventsList;
        try {
            eventsList = eventParser.parse(new JSONArray(sharedPreferences.getString("allEvents", "")));
            Collections.sort(eventsList, new EventComparator());
            EventArrayAdapter eventArrayAdapter = new EventArrayAdapter(eventsList, this.getActivity());
            eventsListView.setAdapter(eventArrayAdapter);
            eventsListView.setSelection(findPositionOfTodayDate(eventsList));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private int findPositionOfTodayDate(List<Event> eventList){
        Calendar currentDate = Calendar.getInstance();
        for (int i = 0; i < eventList.size(); i++) {
            if (currentDate.compareTo(eventList.get(i).getStartsAt()) > 0) continue;
            return i;
        }
        return eventList.size();
    }

    private class EventComparator implements Comparator<Event> {

        @Override
        public int compare(Event lhs, Event rhs) {
            return lhs.getStartsAt().compareTo(rhs.getStartsAt());

        }
    }
}
