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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rs.devana.labs.studentinfo.R;
import rs.devana.labs.studentinfo.domain.models.notification.Notification;
import rs.devana.labs.studentinfo.domain.models.notification.NotificationRepositoryInterface;
import rs.devana.labs.studentinfo.infrastructure.dagger.Injector;
import rs.devana.labs.studentinfo.infrastructure.json.parser.NotificationParser;
import rs.devana.labs.studentinfo.presentation.adapters.NotificationArrayAdapter;

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
        NotificationArrayAdapter notificationArrayAdapter = new NotificationArrayAdapter(notificationsList, this.getActivity());
        notificationsListView.setAdapter(notificationArrayAdapter);
        notificationsListView.setEmptyView(this.getActivity().findViewById(R.id.notificationsEmptyView));
    }


}
