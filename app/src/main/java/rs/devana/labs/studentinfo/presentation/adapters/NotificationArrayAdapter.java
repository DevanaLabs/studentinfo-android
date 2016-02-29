package rs.devana.labs.studentinfo.presentation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import rs.devana.labs.studentinfo.domain.models.notification.Notification;

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
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            TextView lecture = (TextView) convertView.findViewById(android.R.id.text1);
            lecture.setText(notifications.get(position).toString());
        }
        return convertView;
    }
}