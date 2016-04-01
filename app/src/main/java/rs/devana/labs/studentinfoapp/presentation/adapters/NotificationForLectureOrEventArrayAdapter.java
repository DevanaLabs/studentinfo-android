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
import java.util.List;
import java.util.Locale;

import rs.devana.labs.studentinfoapp.R;
import rs.devana.labs.studentinfoapp.domain.models.notification.Notification;

public class NotificationForLectureOrEventArrayAdapter  extends ArrayAdapter<Notification> {
    List<? extends Notification> notifications;
    Context context;
    LayoutInflater inflater;
    int customLayoutId;

    View.OnTouchListener mOnTouchListener;

    public NotificationForLectureOrEventArrayAdapter(List<Notification> notifications, Context context, int customLayoutId, View.OnTouchListener mOnTouchListener) {
        super(context, customLayoutId, notifications);
        this.customLayoutId = customLayoutId;
        this.notifications = notifications;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mOnTouchListener = mOnTouchListener;

    }

    public NotificationForLectureOrEventArrayAdapter(List<? extends Notification> notifications, Context context, int customLayoutId) {
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


    public void setNotifications(List<? extends Notification> notifications) {
        this.notifications = notifications;
    }
}

