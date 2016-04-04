package rs.devana.labs.studentinfoapp.presentation.fragments;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import rs.devana.labs.studentinfoapp.R;
import rs.devana.labs.studentinfoapp.domain.models.event.Event;
import rs.devana.labs.studentinfoapp.infrastructure.dagger.Injector;
import rs.devana.labs.studentinfoapp.infrastructure.event_bus_events.OpenEventActivityEvent;
import rs.devana.labs.studentinfoapp.infrastructure.json.parser.EventParser;
import rs.devana.labs.studentinfoapp.infrastructure.repository.EventRepository;
import rs.devana.labs.studentinfoapp.presentation.adapters.EventArrayAdapter;

public class YearlyCalendarFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    EventParser eventParser;

    @Inject
    EventBus eventBus;

    @Inject
    EventRepository eventRepository;

    private SwipeRefreshLayout swipeRefreshLayout;
    private EventArrayAdapter eventArrayAdapter;
    private ListView eventsListView;
    private List<Event> eventsList;

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
        String events = sharedPreferences.getString("allEvents", "");
        try {
            eventsList = eventParser.parse(new JSONArray(events));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Collections.sort(eventsList, new EventComparator());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yearly_calendar, container, false);
        eventsListView = (ListView) view.findViewById(R.id.eventsListView);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        eventArrayAdapter = new EventArrayAdapter(eventsList, this.getActivity());
        eventsListView.setAdapter(eventArrayAdapter);
        eventsListView.setSelection(findPositionOfTodayDate(eventsList));
        eventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                postEvent(position);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private int findPositionOfTodayDate(List<Event> eventList){
        Calendar currentDate = Calendar.getInstance();
        for (int i = 0; i < eventList.size(); i++) {
            if (currentDate.getTimeInMillis() > eventList.get(i).getStartsAt().getTimeInMillis() + 86400) continue;
            return i;
        }
        return eventList.size();
    }

    private void postEvent(int position){
        eventBus.post(new OpenEventActivityEvent(eventsList.get(position)));
    }

    @Override
    public void onRefresh() {
         new Thread(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                JSONArray events = eventRepository.getEvents();
                try {
                    JSONArray eventsBefore = new JSONArray(sharedPreferences.getString("allEvents", "[]"));
                    for (int i = 0; i < eventsBefore.length(); i++){
                        for (int j = 0; j < events.length(); j++) {
                            if (events.getJSONObject(i).getJSONArray("notifications").length() > 0) {
                                if (eventsBefore.getJSONObject(i).getInt("id") == events.getJSONObject(j).getInt("id")) {
                                    for (int k = 0; k < eventsBefore.getJSONObject(i).getJSONArray("notifications").length(); k++){
                                        for (int l = 0; l < events.getJSONObject(j).getJSONArray("notifications").length(); l++){
                                            if ((eventsBefore.getJSONObject(i).getJSONArray("notifications").getJSONObject(k).getInt("id") == events.getJSONObject(j).getJSONArray("notifications").getJSONObject(l).getInt("id")) && (eventsBefore.getJSONObject(i).getJSONArray("notifications").getJSONObject(k).has("arrived"))){
                                                events.getJSONObject(j).getJSONArray("notifications").getJSONObject(l).put("arrived", eventsBefore.getJSONObject(i).getJSONArray("notifications").getJSONObject(k).getString("arrived"));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                addArrivedToEventNotifications(events);
                eventsList = eventParser.parse(events);
                Collections.sort(eventsList, new EventComparator());
                eventArrayAdapter.setEvents(eventsList);
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            eventsListView.setAdapter(eventArrayAdapter);
                            eventsListView.setSelection(findPositionOfTodayDate(eventsList));
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("allEvents", events.toString());
                editor.apply();
            }
        }).start();
    }

    private JSONArray addArrivedToEventNotifications(JSONArray jsonEvents){
        for (int i = 0; i < jsonEvents.length(); i++){
                try {
                    for (int j = 0; j < jsonEvents.getJSONObject(i).getJSONArray("notifications").length();  j++) {
                        if (!jsonEvents.getJSONObject(i).getJSONArray("notifications").getJSONObject(j).has("arrived")) {
                            String arrived = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault()).format(Calendar.getInstance().getTime());
                            jsonEvents.getJSONObject(i).getJSONArray("notifications").getJSONObject(j).put("arrived", arrived);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return jsonEvents;
        }

    private class EventComparator implements Comparator<Event> {

        @Override
        public int compare(Event lhs, Event rhs) {
            return lhs.getStartsAt().compareTo(rhs.getStartsAt());
        }
    }
}
