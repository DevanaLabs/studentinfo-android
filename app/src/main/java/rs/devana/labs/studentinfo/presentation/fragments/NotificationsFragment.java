package rs.devana.labs.studentinfo.presentation.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rs.devana.labs.studentinfo.R;
import rs.devana.labs.studentinfo.domain.models.notification.Notification;
import rs.devana.labs.studentinfo.presentation.adapters.NotificationArrayAdapter;

public class NotificationsFragment extends Fragment {


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
    public void onStart() {
        super.onStart();
        ListView notificationsListView = (ListView) this.getActivity().findViewById(R.id.notificationsListView);
        List<Notification> dummyData = generateDummyData();
        NotificationArrayAdapter notificationArrayAdapter = new NotificationArrayAdapter(dummyData, this.getActivity());
        notificationsListView.setAdapter(notificationArrayAdapter);
    }

    private List<Notification> generateDummyData() {
        List<Notification> stringArrayList = new ArrayList<Notification>();
        stringArrayList.add(new Notification(1, "Vazno obavestenje 1", Calendar.getInstance()));
        stringArrayList.add(new Notification(1, "Vrlo bitna stvar 2", Calendar.getInstance()));
        stringArrayList.add(new Notification(1, "Bas jako bitna stvar 3", Calendar.getInstance()));
        stringArrayList.add(new Notification(1, "Vrlo kul obavestenje 4", Calendar.getInstance()));
        stringArrayList.add(new Notification(1, "Nesto 5", Calendar.getInstance()));
        stringArrayList.add(new Notification(1, "Odlaze se cas 6", Calendar.getInstance()));
        stringArrayList.add(new Notification(1, "Novi svet otkriven 7", Calendar.getInstance()));
        stringArrayList.add(new Notification(1, "Nema vise skole 8", Calendar.getInstance()));
        stringArrayList.add(new Notification(1, "ETF izgoreo do temelja 9", Calendar.getInstance()));
        return stringArrayList;
    }

}
