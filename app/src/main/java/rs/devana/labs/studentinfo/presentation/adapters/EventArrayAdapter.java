package rs.devana.labs.studentinfo.presentation.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rs.devana.labs.studentinfo.R;
import rs.devana.labs.studentinfo.domain.models.event.Event;

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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_event_row_view, parent, false);
            TextView dayTextView = (TextView) convertView.findViewById(R.id.dayTextView);
            TextView monthTextView = (TextView) convertView.findViewById(R.id.monthTextView);
            TextView descriptionTextView = (TextView) convertView.findViewById(R.id.descriptionTextView);
            TextView additionalInfoTextView = (TextView) convertView.findViewById(R.id.additionalInfoTextView);
            LinearLayout eventDescriptionParentLayout = (LinearLayout) convertView.findViewById(R.id.eventDescriptionParentLayout);
            Event event = events.get(position);

            if (skipIndexes.contains(position)) {
                dayTextView.setVisibility(View.INVISIBLE);
                monthTextView.setVisibility(View.INVISIBLE);
            } else {
                dayTextView.setVisibility(View.VISIBLE);
                monthTextView.setVisibility(View.VISIBLE);
            }
            dayTextView.setText(event.getDayOfTheMonth());
            monthTextView.setText(translate(event.getMonth()));
            descriptionTextView.setText("" + event.getDescription());
            additionalInfoTextView.setText("" + event.getType());
            setColor(eventDescriptionParentLayout, event);

        }
        return convertView;
    }

    private void setColor(LinearLayout eventDescriptionParentLayout, Event event) {
        String colorToParse;
        switch (event.getType()) {
            //blue
            case "Испитни рок":
            case "Испит":
                colorToParse = "#1080CC";
                break;
            //orange
            case "Колоквијум":
            case "Колоквијумска недеља":
                colorToParse = "#F07D52";
                break;
            //green
            case "Плаћање школарине":
                colorToParse = "#66BB6A";
                break;
            case "Нерадни дани":
                colorToParse = "#808080";
                break;
            //yellow
            default:
                colorToParse = "#FFE082";
        }
        eventDescriptionParentLayout.setBackgroundColor(Color.parseColor(colorToParse));
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
}
