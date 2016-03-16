package rs.devana.labs.studentinfo.presentation.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
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
        List<Event> eventsList = new ArrayList<>();

//        String notifications = sharedPreferences.getString("notifications", "");
//        if (!notifications.isEmpty()) {
//            try {
//                eventsList = eventParser.parse(new JSONArray(notifications));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
        eventsList.add(new Event(1, "Испит", "Испит из неког небитног уопште предмета типа АОР. Умри АОР-е.", Calendar.getInstance(), Calendar.getInstance(), null, null));
        eventsList.add(new Event(2, "Колоквијум", "Јако важан и крајње битан догађај се догађа", Calendar.getInstance(), Calendar.getInstance(), null, null));
        eventsList.add(new Event(3, "Специјалан догађај", "Никола Стерлу ништа не ради и није вредан уопште лик оно у глобалу", Calendar.getInstance(), Calendar.getInstance(), null, null));
        Calendar c = Calendar.getInstance();
        c.set(2016, 4, 3);
        eventsList.add(new Event(3, "Нерадни дани", "Никола Нинковић је јако добар и паметан дечак", c, Calendar.getInstance(), null, null));
        Collections.sort(eventsList, new EventComparator());
        EventArrayAdapter eventArrayAdapter = new EventArrayAdapter(eventsList, this.getActivity());
        eventsListView.setAdapter(eventArrayAdapter);
    }

    private class EventComparator implements Comparator<Event> {

        @Override
        public int compare(Event lhs, Event rhs) {
            return lhs.getStartsAt().compareTo(rhs.getStartsAt());

        }
    }
}
