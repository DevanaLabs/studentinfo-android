package rs.devana.labs.studentinfoapp.presentation.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import rs.devana.labs.studentinfoapp.R;
import rs.devana.labs.studentinfoapp.domain.models.notification.Notification;

public class NotificationArrayAdapter extends ArrayAdapter<Notification> {

    private static final int SECONDS_IN_MINUTES = 60;
    private static final int MINUTES_IN_HOURS = 60;
    private static final int HOURS_IN_DAY = 24;
    private static final int DAYS_IN_WEEK = 7;
    private static final int DAYS_IN_MONTH = 30;
    private static final int DAYS_IN_YEAR = 365;

    List<? extends Notification> notifications;
    Context context;
    LayoutInflater inflater;
    int customLayoutId;

    View.OnTouchListener mOnTouchListener;

    public NotificationArrayAdapter(List<Notification> notifications, Context context, int customLayoutId, View.OnTouchListener mOnTouchListener) {
        super(context, customLayoutId, notifications);
        this.customLayoutId = customLayoutId;
        this.notifications = notifications;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mOnTouchListener = mOnTouchListener;

    }

    public NotificationArrayAdapter(List<? extends Notification> notifications, Context context, int customLayoutId) {
        super(context, customLayoutId, (List<Notification>) notifications);
        this.customLayoutId = customLayoutId;
        this.notifications = notifications;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return notifications.size();
    }

    @Override
    public Notification getItem(int position) {
        return notifications.get(position);
    }

    @Override
    public long getItemId(int position) {
        Notification notification = getItem(position);
        return notification.getId();
    }

    @Override
    public int getViewTypeCount() {
        return getCount() < 1 ? 1 : getCount();
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }

        @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(customLayoutId, parent, false);

            CardView cardView = (CardView) convertView.findViewById(R.id.card_view_notifications);
            setBackgroundColor(cardView, position);

            Notification notification = notifications.get(position);
            convertView.setId(notification.getId());
            TextView additionalInfoNotificationTextView = (TextView) convertView.findViewById(R.id.additionalInfoNotificationTextView);
            additionalInfoNotificationTextView.setText(notification.getAdditionalInfo());

            TextView timeOfNotification = (TextView) convertView.findViewById(R.id.timeOfNotification);
            String time = context.getString(R.string.before) + " " + calculateTimeDiff(notification.getArrived());
            timeOfNotification.setText(time);

            TextView notificationDescriptionTextView = (TextView) convertView.findViewById(R.id.notificationDescriptionTextView);
            TextView notificationExpiresAt = (TextView) convertView.findViewById(R.id.notificationExpiresAt);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
            notificationExpiresAt.setText(simpleDateFormat.format(notification.getExpiresAt().getTime()));
            notificationDescriptionTextView.setText(notification.getDescription());
        }
        convertView.setOnTouchListener(mOnTouchListener);
        return convertView;
    }

    private void setBackgroundColor(CardView cardView, int position) {
        cardView.setCardBackgroundColor(position % 2 == 0 ? Color.parseColor("#eeeeee") : Color.parseColor("#ffffff"));
    }

    private String calculateTimeDiff(Calendar calendar) {
        long secondsNow = Calendar.getInstance().getTimeInMillis();
        long diff = (secondsNow - calendar.getTimeInMillis()) / 1000;

        if (diff > 2 * SECONDS_IN_MINUTES * MINUTES_IN_HOURS * HOURS_IN_DAY * DAYS_IN_YEAR) {
            return String.valueOf(diff / (SECONDS_IN_MINUTES * MINUTES_IN_HOURS * HOURS_IN_DAY * DAYS_IN_YEAR)) + " " + context.getString(R.string.years);
        }
        if (diff > SECONDS_IN_MINUTES * MINUTES_IN_HOURS * HOURS_IN_DAY * DAYS_IN_YEAR) {
            return String.valueOf(diff / (SECONDS_IN_MINUTES * MINUTES_IN_HOURS * HOURS_IN_DAY * DAYS_IN_YEAR)) + " " + context.getString(R.string.year);
        }
        if (diff > 5 * SECONDS_IN_MINUTES * MINUTES_IN_HOURS * HOURS_IN_DAY * DAYS_IN_MONTH) {
            return String.valueOf(diff / (SECONDS_IN_MINUTES * MINUTES_IN_HOURS * HOURS_IN_DAY * DAYS_IN_MONTH)) + " " + context.getString(R.string.monthsFive);
        }
        if (diff > 2 * SECONDS_IN_MINUTES * MINUTES_IN_HOURS * HOURS_IN_DAY * DAYS_IN_MONTH) {
            return String.valueOf(diff / (SECONDS_IN_MINUTES * MINUTES_IN_HOURS * HOURS_IN_DAY * DAYS_IN_MONTH)) + " " + context.getString(R.string.monthsTwo);
        }
        if (diff > SECONDS_IN_MINUTES * MINUTES_IN_HOURS * HOURS_IN_DAY * DAYS_IN_MONTH) {
            return String.valueOf(diff / (SECONDS_IN_MINUTES * MINUTES_IN_HOURS * HOURS_IN_DAY * DAYS_IN_MONTH)) + " " + context.getString(R.string.month);
        }
        if (diff > 2 * SECONDS_IN_MINUTES * MINUTES_IN_HOURS * HOURS_IN_DAY * DAYS_IN_WEEK) {
            return String.valueOf(diff / (SECONDS_IN_MINUTES * MINUTES_IN_HOURS * HOURS_IN_DAY * DAYS_IN_WEEK)) + " " + context.getString(R.string.weeks);
        }
        if (diff > SECONDS_IN_MINUTES * MINUTES_IN_HOURS * HOURS_IN_DAY * DAYS_IN_WEEK) {
            return String.valueOf(diff / (SECONDS_IN_MINUTES * MINUTES_IN_HOURS * HOURS_IN_DAY * DAYS_IN_WEEK)) + " " + context.getString(R.string.week);
        }
        if (diff > 2 * SECONDS_IN_MINUTES * MINUTES_IN_HOURS * HOURS_IN_DAY) {
            return String.valueOf(diff / (SECONDS_IN_MINUTES * MINUTES_IN_HOURS * HOURS_IN_DAY)) + " " + context.getString(R.string.days);
        }
        if (diff > SECONDS_IN_MINUTES * MINUTES_IN_HOURS * HOURS_IN_DAY) {
            return String.valueOf(diff / (SECONDS_IN_MINUTES * MINUTES_IN_HOURS * HOURS_IN_DAY)) + " " + context.getString(R.string.day);
        }
        if (diff > 5 * SECONDS_IN_MINUTES * MINUTES_IN_HOURS) {
            return String.valueOf(diff / (SECONDS_IN_MINUTES * MINUTES_IN_HOURS)) + " " + context.getString(R.string.hoursFive);
        }
        if (diff > 2 * SECONDS_IN_MINUTES * MINUTES_IN_HOURS) {
            return String.valueOf(diff / (SECONDS_IN_MINUTES * MINUTES_IN_HOURS)) + " " + context.getString(R.string.hoursTwo);
        }
        if (diff > SECONDS_IN_MINUTES * MINUTES_IN_HOURS) {
            return String.valueOf(diff / (SECONDS_IN_MINUTES * MINUTES_IN_HOURS)) + " " + context.getString(R.string.hour);
        }
        if ((diff > 2 * SECONDS_IN_MINUTES && (diff / SECONDS_IN_MINUTES) % 10 != 1) || diff / SECONDS_IN_MINUTES == 11) {
            return String.valueOf(diff / (SECONDS_IN_MINUTES)) + " " + context.getString(R.string.minutes);
        }
        if (diff > SECONDS_IN_MINUTES) {
            return String.valueOf(diff / (SECONDS_IN_MINUTES)) + " " + context.getString(R.string.minute);
        }
        if ((diff > 5 && diff % 10 != 1) || diff == 11 || diff < 1) {
            return String.valueOf(diff) + " " + context.getString(R.string.secondsFive);
        }
        if (diff > 2) {
            return String.valueOf(diff) + " " + context.getString(R.string.secondsTwo);
        }
        return String.valueOf(diff) + " " + context.getString(R.string.second);
    }

    public void setNotifications(List<? extends Notification> notifications) {
        this.notifications = notifications;
    }
}