package rs.devana.labs.studentinfoapp.presentation.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import rs.devana.labs.studentinfoapp.R;
import rs.devana.labs.studentinfoapp.domain.models.notification.Notification;
import rs.devana.labs.studentinfoapp.domain.models.notification.NotificationRepositoryInterface;
import rs.devana.labs.studentinfoapp.infrastructure.dagger.Injector;
import rs.devana.labs.studentinfoapp.infrastructure.json.parser.NotificationParser;
import rs.devana.labs.studentinfoapp.presentation.adapters.NotificationArrayAdapter;

public class NotificationsFragment extends Fragment {

    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    NotificationParser notificationParser;
    @Inject
    NotificationRepositoryInterface notificationRepository;

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
        ListView notificationsListView = (ListView) this.getActivity().findViewById(R.id.notificationsListView);
        List<Notification> notificationsList = new ArrayList<>();

        String notifications = sharedPreferences.getString("notifications", "");
        if (!notifications.isEmpty()) {
            try {
                notificationsList = notificationParser.parse(new JSONArray(notifications));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(notificationsList, new NotificationComparator());
        NotificationArrayAdapter notificationArrayAdapter = new NotificationArrayAdapter(notificationsList, this.getActivity());
        notificationsListView.setAdapter(notificationArrayAdapter);
        notificationsListView.setEmptyView(this.getActivity().findViewById(R.id.notificationsEmptyView));
    }

    private class NotificationComparator implements Comparator<Notification>{

        @Override
        public int compare(Notification lhs, Notification rhs) {
            return lhs.getExpiresAt().compareTo(rhs.getExpiresAt());
        }
    }

}
