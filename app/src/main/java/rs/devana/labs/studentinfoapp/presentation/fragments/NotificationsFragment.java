package rs.devana.labs.studentinfoapp.presentation.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import rs.devana.labs.studentinfoapp.R;
import rs.devana.labs.studentinfoapp.domain.models.notification.Notification;
import rs.devana.labs.studentinfoapp.domain.models.notification.NotificationRepositoryInterface;
import rs.devana.labs.studentinfoapp.infrastructure.dagger.Injector;
import rs.devana.labs.studentinfoapp.infrastructure.json.parser.NotificationParser;
import rs.devana.labs.studentinfoapp.presentation.adapters.NotificationArrayAdapter;

public class NotificationsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    NotificationParser notificationParser;

    @Inject
    NotificationRepositoryInterface notificationRepository;


    private SwipeRefreshLayout swipeRefreshLayout;
    private NotificationArrayAdapter notificationArrayAdapter;
    private List<Notification> notificationsList;
    private ListView notificationsListView;

    public static NotificationsFragment newInstance() {
        NotificationsFragment fragment = new NotificationsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.INSTANCE.getApplicationComponent().inject(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        notificationsListView = (ListView) this.getActivity().findViewById(R.id.notificationsListView);
        swipeRefreshLayout = (SwipeRefreshLayout) this.getActivity().findViewById(R.id.swipeRefreshLayout);
        notificationsList = new ArrayList<>();

        String notifications = sharedPreferences.getString("notifications", "");
        if (!notifications.isEmpty()) {
            try {
                notificationsList = notificationParser.parse(new JSONArray(notifications));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(notificationsList, new NotificationComparator());
        notificationArrayAdapter = new NotificationArrayAdapter(notificationsList, this.getActivity());
        if (notificationsList.size() > 0) {
            notificationsListView.setAdapter(notificationArrayAdapter);
            notificationsListView.setEmptyView(this.getActivity().findViewById(R.id.notificationsEmptyView));
        }
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                JSONArray notifications = notificationRepository.getAllNotifications();
                try {
                    JSONArray notificationsBefore = new JSONArray(sharedPreferences.getString("notifications", "[]"));
                    for (int i = 0; i < notificationsBefore.length(); i++) {
                        for (int j = 0; j < notifications.length(); j++) {
                            if ((notificationsBefore.getJSONObject(i).getInt("id") == notifications.getJSONObject(j).getInt("id")) && (notificationsBefore.getJSONObject(i).has("arrived"))) {
                                notifications.getJSONObject(i).put("arrived", notificationsBefore.getJSONObject(i).getString("arrived"));
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                addArrivedToNotifications(notifications);
                notificationsList = notificationParser.parse(notifications);
                Collections.sort(notificationsList, new NotificationComparator());
                notificationArrayAdapter.setNotifications(notificationsList);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notificationsListView.setAdapter(notificationArrayAdapter);
                        notificationsListView.setEmptyView(getActivity().findViewById(R.id.notificationsEmptyView));
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
                if (notifications.length() > 0) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("notifications", notifications.toString());
                    editor.apply();
                }
            }
        }).start();
    }

    private JSONArray addArrivedToNotifications(JSONArray jsonNotifications) {
        for (int i = 0; i < jsonNotifications.length(); i++) {
            try {
                if (!jsonNotifications.getJSONObject(i).has("arrived")) {
                    String arrived = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault()).format(Calendar.getInstance().getTime());
                    jsonNotifications.getJSONObject(i).put("arrived", arrived);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonNotifications;
    }

    private class NotificationComparator implements Comparator<Notification> {

        @Override
        public int compare(Notification lhs, Notification rhs) {
            return rhs.getArrived().compareTo(lhs.getArrived());
        }
    }
}
