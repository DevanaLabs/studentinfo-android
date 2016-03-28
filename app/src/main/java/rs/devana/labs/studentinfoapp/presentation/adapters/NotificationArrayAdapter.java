package rs.devana.labs.studentinfoapp.presentation.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import rs.devana.labs.studentinfoapp.R;
import rs.devana.labs.studentinfoapp.domain.models.notification.Notification;

public class NotificationArrayAdapter extends BaseAdapter {

    List<Notification> notifications;
    Context context;
    LayoutInflater inflater;

    public NotificationArrayAdapter(List<Notification> notifications, Context context) {
        this.notifications = notifications;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return notifications.size();
    }

    @Override
    public Object getItem(int position) {
        return notifications.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_notification_card_view, parent, false);

            CardView cardView = (CardView) convertView.findViewById(R.id.card_view_notifications);
            setBackgroundColor(cardView, position);

            Notification notification = notifications.get(position);
            TextView additionalInfoNotificationTextView = (TextView) convertView.findViewById(R.id.additionalInfoNotificationTextView);
            additionalInfoNotificationTextView.setText(notification.getAdditionalInfo());
            TextView timeOfNotification = (TextView) convertView.findViewById(R.id.timeOfNotification);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
            timeOfNotification.setText(simpleDateFormat.format(notification.getExpiresAt().getTime()));
            TextView notificationDescriptionTextView = (TextView) convertView.findViewById(R.id.notificationDescriptionTextView);
            notificationDescriptionTextView.setText(notification.getDescription());
        }
        return convertView;
    }

    private void setBackgroundColor(CardView cardView, int position){
        cardView.setCardBackgroundColor(position % 2 == 0 ? Color.parseColor("#eeeeee") : Color.parseColor("#ffffff"));
    }
}