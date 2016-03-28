package rs.devana.labs.studentinfoapp.presentation.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rs.devana.labs.studentinfoapp.R;
import rs.devana.labs.studentinfoapp.domain.models.event.Event;

public class EventArrayAdapter extends BaseAdapter {

    List<Event> events;
    Context context;
    LayoutInflater inflater;
    private List<Integer> skipIndexes = new ArrayList<>();

    public EventArrayAdapter(List<Event> events, Context context) {
        this.events = events;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setSkipIndexes();
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return events.get(position).getId();
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_event_row_view, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.dayTextView = (TextView) convertView.findViewById(R.id.dayTextView);
            viewHolder.monthTextView = (TextView) convertView.findViewById(R.id.monthTextView);
            viewHolder.descriptionTextView = (TextView) convertView.findViewById(R.id.descriptionTextView);
            viewHolder.additionalInfoTextView = (TextView) convertView.findViewById(R.id.additionalInfoTextView);
            viewHolder.eventDescriptionParentLayout = (CardView) convertView.findViewById(R.id.eventDescriptionParentLayout);
            viewHolder.notificationsAlertImageView = (ImageView) convertView.findViewById(R.id.notificationsAlertImageView);
            Event event = events.get(position);

            if (skipIndexes.contains(position)) {
                viewHolder.dayTextView.setVisibility(View.INVISIBLE);
                viewHolder.monthTextView.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.dayTextView.setVisibility(View.VISIBLE);
                viewHolder.monthTextView.setVisibility(View.VISIBLE);
            }
            viewHolder.dayTextView.setText(event.getDayOfTheMonth());
            viewHolder.monthTextView.setText(translate(event.getMonth()));
            viewHolder.descriptionTextView.setText("" + event.getDescription());
            viewHolder.additionalInfoTextView.setText("" + event.getType());

            if (event.getNotifications() != null && !event.getNotifications().isEmpty()){
                viewHolder.notificationsAlertImageView.setVisibility(View.VISIBLE);
            }
            setColor(viewHolder.eventDescriptionParentLayout, event);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    private void setColor(CardView cardView, Event event) {
        int colorToParse;
        switch (event.getType()) {
            case "Испитни рок":
            case "Испит":
                colorToParse = R.color.eventBlue;
                break;
            case "Колоквијум":
            case "Колоквијумска недеља":
                colorToParse = R.color.eventOrange;
                break;
            case "Плаћање школарине":
                colorToParse = R.color.eventGreen;
                break;
            case "Нерадни дани":
                colorToParse = R.color.eventGray;
                break;
            default:
                colorToParse = R.color.eventYellow;
        }
        cardView.setCardBackgroundColor(ContextCompat.getColor(context, colorToParse));
    }


    private String translate(int month) {
        String temp;
        //it returns 0 for january and so on
        switch (month + 1) {
            case 1:
                temp = context.getResources().getString(R.string.january);
                break;
            case 2:
                temp = context.getResources().getString(R.string.february);
                break;
            case 3:
                temp = context.getResources().getString(R.string.march);
                break;
            case 4:
                temp = context.getResources().getString(R.string.april);
                break;
            case 5:
                temp = context.getResources().getString(R.string.may);
                break;
            case 6:
                temp = context.getResources().getString(R.string.june);
                break;
            case 7:
                temp = context.getResources().getString(R.string.july);
                break;
            case 8:
                temp = context.getResources().getString(R.string.august);
                break;
            case 9:
                temp = context.getResources().getString(R.string.september);
                break;
            case 10:
                temp = context.getResources().getString(R.string.october);
                break;
            case 11:
                temp = context.getResources().getString(R.string.november);
                break;
            default:
                temp = context.getResources().getString(R.string.december);
        }

        return temp;
    }

    private void setSkipIndexes() {
        int i = 0;
        Event previous = null;
        while (i < events.size()) {
            Event current = events.get(i);
            if (previous != null && previous.getMonth() == current.getMonth() && previous.getDayOfTheMonth().equals(current.getDayOfTheMonth())) {
                skipIndexes.add(i);
            }
            previous = current;
            i++;
        }
    }

    private static class ViewHolder {
        TextView dayTextView;
        TextView monthTextView;
        TextView descriptionTextView;
        TextView additionalInfoTextView;
        CardView eventDescriptionParentLayout;
        ImageView notificationsAlertImageView;
    }
}
