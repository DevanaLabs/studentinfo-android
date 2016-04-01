package rs.devana.labs.studentinfoapp.presentation.fragments;


import android.content.SharedPreferences;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ListView;
import android.widget.OverScroller;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
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
import rs.devana.labs.studentinfoapp.infrastructure.event_bus_events.RefreshNotificationsEvent;
import rs.devana.labs.studentinfoapp.infrastructure.json.parser.NotificationParser;
import rs.devana.labs.studentinfoapp.presentation.adapters.NotificationArrayAdapter;
import rs.devana.labs.studentinfoapp.presentation.views.NotificationListView;

public class NotificationsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    NotificationParser notificationParser;

    @Inject
    NotificationRepositoryInterface notificationRepository;

    @Inject
    EventBus eventBus;


    private SwipeRefreshLayout swipeRefreshLayout;
    private NotificationArrayAdapter notificationArrayAdapter;
    private List<Notification> notificationsList;
    private NotificationListView notificationsListView;
    boolean mSwiping = false;
    boolean mItemPressed = false;
    HashMap<Long, Integer> mItemIdTopMap = new HashMap<>();
    Set<String> removedNotifications;

    private Direction mCurrentScrollDirection = Direction.NONE;
    private Direction mCurrentFlingDirection = Direction.NONE;
    private boolean mHorizontalFlingEnabled = true;
    private boolean mVerticalFlingEnabled = true;
    private int mScaledTouchSlop = 0;
    private OverScroller mScroller;
    private PointF mCurrentOrigin = new PointF(0f, 0f);
    private float mXScrollingSpeed = 1f;

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
        eventBus.register(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        notificationsListView = (NotificationListView) this.getActivity().findViewById(R.id.notificationsListView);
        TextView emptyTextView = (TextView) this.getActivity().findViewById(R.id.empty);
        notificationsListView.setEmptyView(emptyTextView);
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
        eventBus.unregister(this);
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

    @Subscribe
    public void onRefreshNotificationsEvent(RefreshNotificationsEvent refreshNotificationsEvent){
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
        notificationArrayAdapter.setNotifications(notificationsList);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notificationsListView.setAdapter(notificationArrayAdapter);
            }
        });
    }
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {

        float mDownX;
        float mDownY;
        private int mSwipeSlop = -1;
        private boolean calcOnce = false;

        @Override
        public boolean onTouch(final View view, MotionEvent event) {
            if (mSwipeSlop < 0) {
                mSwipeSlop = ViewConfiguration.get(getActivity()).
                        getScaledTouchSlop();
            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    if (mItemPressed) {
                        // Multi-item swipes not handled
                        return false;
                    }
                    swipeRefreshLayout.setEnabled(false);
                    calcOnce = true;
                    mItemPressed = true;
                    mDownX = event.getX();
                    mDownY = event.getY();
                    break;
                }
                case MotionEvent.ACTION_CANCEL: {
                    view.setAlpha(1);
                    view.setTranslationX(0);
                    mItemPressed = false;
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    float x = event.getX() + view.getTranslationX();
                    float y = event.getY() + view.getTranslationY();
                    float deltaX = Math.abs(x - mDownX);
                    float deltaY = Math.abs(y - mDownY);
                    if (((deltaY > 10) || (deltaX > 10)) && calcOnce){
                        if(deltaY > deltaX) {
                            swipeRefreshLayout.setEnabled(true);
                        }
                        calcOnce = false;
                    }
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
                case MotionEvent.ACTION_UP: {
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
                            view.setAlpha(1);
                            view.setTranslationX(0);
                            mItemPressed = false;
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
                swipeRefreshLayout.setEnabled(true);
                mItemPressed = false;
                break;
                default:
                    return false;
            }
            return true;
        }
    };

    private void animateRemoval(final ListView listview, View viewToRemove) {

        HashSet<String> temp = new HashSet<>(sharedPreferences.getStringSet("removedNotifications", new HashSet<String>()));
        temp.add(String.valueOf(viewToRemove.getId()));
        SharedPreferences.Editor editor =  sharedPreferences.edit();
        editor.putStringSet("removedNotifications", temp);
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

    private final GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {


        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            switch (mCurrentScrollDirection) {
                case NONE: {
                    // Allow scrolling only in one direction.
                    if (Math.abs(distanceX) > Math.abs(distanceY)) {
                        if (distanceX > 0) {
                            mCurrentScrollDirection = Direction.LEFT;
                        } else {
                            mCurrentScrollDirection = Direction.RIGHT;
                        }
                    } else {
                        mCurrentScrollDirection = Direction.VERTICAL;
                    }
                    break;
                }
                case LEFT: {
                    // Change direction if there was enough change.
                    if (Math.abs(distanceX) > Math.abs(distanceY) && (distanceX < -mScaledTouchSlop)) {
                        mCurrentScrollDirection = Direction.RIGHT;
                    }
                    break;
                }
                case RIGHT: {
                    // Change direction if there was enough change.
                    if (Math.abs(distanceX) > Math.abs(distanceY) && (distanceX > mScaledTouchSlop)) {
                        mCurrentScrollDirection = Direction.LEFT;
                    }
                    break;
                }
            }

            // Calculate the new origin after scroll.
            switch (mCurrentScrollDirection) {
                case LEFT:
                case RIGHT:
                    mCurrentOrigin.x -= distanceX * mXScrollingSpeed;
                    ViewCompat.postInvalidateOnAnimation(notificationsListView);
                    break;
                case VERTICAL:
                    mCurrentOrigin.y -= distanceY;
                    ViewCompat.postInvalidateOnAnimation(notificationsListView);
                    break;
            }
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if ((mCurrentFlingDirection == Direction.LEFT && !mHorizontalFlingEnabled) ||
                    (mCurrentFlingDirection == Direction.RIGHT && !mHorizontalFlingEnabled) ||
                    (mCurrentFlingDirection == Direction.VERTICAL && !mVerticalFlingEnabled)) {
                return true;
            }

            mScroller.forceFinished(true);

            mCurrentFlingDirection = mCurrentScrollDirection;
            switch (mCurrentFlingDirection) {
                case LEFT:
                case RIGHT:
                    mScroller.fling((int) mCurrentOrigin.x, (int) mCurrentOrigin.y, (int) (velocityX * mXScrollingSpeed), 0, Integer.MIN_VALUE, Integer.MAX_VALUE, (int) -(notificationsListView.getHeight()/notificationsListView.getCount()- notificationsListView.getHeight()), 0);
                    break;
                case VERTICAL:
                    mScroller.fling((int) mCurrentOrigin.x, (int) mCurrentOrigin.y, 0, (int) velocityY, Integer.MIN_VALUE, Integer.MAX_VALUE, (int) -(notificationsListView.getHeight()/notificationsListView.getCount() - notificationsListView.getHeight()), 0);
                    break;
            }

            ViewCompat.postInvalidateOnAnimation(notificationsListView);
            return true;
        }
    };

    private enum Direction {
        NONE, LEFT, RIGHT, VERTICAL
    }
}
