package rs.devana.labs.studentinfoapp.presentation.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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
    boolean mSwiping = false;
    boolean mItemPressed = false;
    HashMap<Long, Integer> mItemIdTopMap = new HashMap<>();
    Set<String> removedNotifications;


    private static final int SWIPE_DURATION = 250;
    private static final int MOVE_DURATION = 150;

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
        removedNotifications = sharedPreferences.getStringSet("removedNotifications", null);
        if (removedNotifications == null){
            removedNotifications = new HashSet<>();
        }
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
        notificationArrayAdapter = new NotificationArrayAdapter(notificationsList, this.getActivity(), R.layout.custom_notification_card_view, mTouchListener);
        if (notificationsList.size() > 0) {
            notificationsListView.setAdapter(notificationArrayAdapter);
        }
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onDetach() {

        super.onDetach();
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

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {

        float mDownX;
        private int mSwipeSlop = -1;

        @Override
        public boolean onTouch(final View view, MotionEvent event) {

            if (mSwipeSlop < 0) {
                mSwipeSlop = ViewConfiguration.get(getActivity()).
                        getScaledTouchSlop();
            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (mItemPressed) {
                        // Multi-item swipes not handled
                        return false;
                    }
                    mItemPressed = true;
                    mDownX = event.getX();
                    break;
                case MotionEvent.ACTION_CANCEL:
                    view.setAlpha(1);
                    view.setTranslationX(0);
                    mItemPressed = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                {
                    float x = event.getX() + view.getTranslationX();
                    float deltaX = x - mDownX;
                    float deltaXAbs = Math.abs(deltaX);
                    if (!mSwiping) {
                        if (deltaXAbs > mSwipeSlop) {
                            mSwiping = true;
                            notificationsListView.requestDisallowInterceptTouchEvent(true);
                        }
                    }
                    if (mSwiping) {
                        view.setTranslationX((x - mDownX));
                        view.setAlpha(1 - deltaXAbs / view.getWidth());
                    }
                }
                break;
                case MotionEvent.ACTION_UP:
                {
                    // User let go - figure out whether to animate the view out, or back into place
                    if (mSwiping) {
                        float x = event.getX() + view.getTranslationX();
                        float deltaX = x - mDownX;
                        float deltaXAbs = Math.abs(deltaX);
                        float fractionCovered;
                        float endX;
                        float endAlpha;
                        final boolean remove;
                        if (deltaXAbs > view.getWidth() / 4) {
                            // Greater than a quarter of the width - animate it out
                            fractionCovered = deltaXAbs / view.getWidth();
                            endX = deltaX < 0 ? -view.getWidth() : view.getWidth();
                            endAlpha = 0;
                            remove = true;
                        } else {
                            // Not far enough - animate it back
                            fractionCovered = 1 - (deltaXAbs / view.getWidth());
                            endX = 0;
                            endAlpha = 1;
                            remove = false;
                        }
                        long duration = (int) ((1 - fractionCovered) * SWIPE_DURATION);
                        notificationsListView.setEnabled(false);
                        view.animate().setDuration(duration).
                                alpha(endAlpha).translationX(endX).
                                withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Restore animated values
                                        view.setAlpha(1);
                                        view.setTranslationX(0);
                                        if (remove) {
                                            animateRemoval(notificationsListView, view);
                                        } else {
                                            mSwiping = false;
                                            notificationsListView.setEnabled(true);
                                        }
                                    }
                                });
                    }
                }
                mItemPressed = false;
                break;
                default:
                    return false;
            }
            return true;
        }
    };

    private void animateRemoval(final ListView listview, View viewToRemove) {

        removedNotifications.add(String.valueOf(viewToRemove.getId()));
        SharedPreferences.Editor editor =  sharedPreferences.edit();
        editor.putStringSet("removedNotifications", removedNotifications);
        editor.apply();

        int firstVisiblePosition = listview.getFirstVisiblePosition();
        for (int i = 0; i < listview.getChildCount(); ++i) {
            View child = listview.getChildAt(i);
            if (child != viewToRemove) {
                int position = firstVisiblePosition + i;
                long itemId = notificationArrayAdapter.getItemId(position);
                mItemIdTopMap.put(itemId, child.getTop());
            }
        }
        int position = notificationsListView.getPositionForView(viewToRemove);
        notificationsList.remove(position);
        notificationArrayAdapter.notifyDataSetChanged();
        listview.setAdapter(notificationArrayAdapter);

        final ViewTreeObserver observer = listview.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                observer.removeOnPreDrawListener(this);
                boolean firstAnimation = true;
                int firstVisiblePosition = listview.getFirstVisiblePosition();
                for (int i = 0; i < listview.getChildCount(); ++i) {
                    final View child = listview.getChildAt(i);
                    int position = firstVisiblePosition + i;
                    long itemId = notificationArrayAdapter.getItemId(position);
                    Integer startTop = mItemIdTopMap.get(itemId);
                    int top = child.getTop();
                    if (startTop != null) {
                        if (startTop != top) {
                            int delta = startTop - top;
                            child.setTranslationY(delta);
                            child.animate().setDuration(MOVE_DURATION).translationY(0);
                            if (firstAnimation) {
                                child.animate().withEndAction(new Runnable() {
                                    public void run() {
                                        mSwiping = false;
                                        notificationsListView.setEnabled(true);
                                    }
                                });
                                firstAnimation = false;
                            }
                        }
                    } else {
                        int childHeight = child.getHeight() + listview.getDividerHeight();
                        startTop = top + (i > 0 ? childHeight : -childHeight);
                        int delta = startTop - top;
                        child.setTranslationY(delta);
                        child.animate().setDuration(MOVE_DURATION).translationY(0);
                        if (firstAnimation) {
                            child.animate().withEndAction(new Runnable() {
                                public void run() {
                                    mSwiping = false;
                                    notificationsListView.setEnabled(true);
                                }
                            });
                            firstAnimation = false;
                        }
                    }
                }
                mItemIdTopMap.clear();
                return true;
            }
        });
    }
}
