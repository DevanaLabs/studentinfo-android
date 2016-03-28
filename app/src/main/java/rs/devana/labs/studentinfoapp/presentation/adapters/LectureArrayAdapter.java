package rs.devana.labs.studentinfoapp.presentation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import rs.devana.labs.studentinfoapp.R;
import rs.devana.labs.studentinfoapp.domain.models.lecture.Lecture;

public class LectureArrayAdapter extends BaseAdapter {
    List<Lecture> lectures;
    Context context;
    LayoutInflater inflater;

    public LectureArrayAdapter(List<Lecture> lectures, Context context) {
        this.lectures = lectures;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return lectures.size();
    }

    @Override
    public Object getItem(int position) {
        return lectures.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            TextView lecture = (TextView)convertView.findViewById(android.R.id.text1);
            lecture.setText(context.getString(R.string.start)+lectures.get(position).getConvertedStartsAt()+"h\n"
                    +context.getString(R.string.end)+lectures.get(position).getConvertedEndsAt()+"h\n"
                    +lectures.get(position).getLectureName()+"\n"
                        +lectures.get(position).getLectureClassroom());
        }
        return convertView;
    }
}