package rs.devana.labs.studentinfoapp.presentation.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import rs.devana.labs.studentinfoapp.R;
import rs.devana.labs.studentinfoapp.domain.models.lecture.Lecture;
import rs.devana.labs.studentinfoapp.infrastructure.dagger.Injector;
import rs.devana.labs.studentinfoapp.infrastructure.json.parser.LectureParser;


public class LectureFragment extends Fragment {

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    LectureParser lectureParser;

    private Lecture lecture;

    public static LectureFragment newInstance(int lectureId) {
        LectureFragment fragment = new LectureFragment();
        Bundle args = new Bundle();
        args.putInt("lectureId", lectureId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.INSTANCE.getApplicationComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lecture, container, false);
        Lecture lecture = lectureParser.getLecture(getArguments().getInt("lectureId"));

        TextView teacherName = (TextView) view.findViewById(R.id.teacherName);
        TextView lectureType = (TextView) view.findViewById(R.id.lectureType);
        TextView lectureTime = (TextView) view.findViewById(R.id.lectureTime);
        TextView classroomName = (TextView) view.findViewById(R.id.classroomName);

        teacherName.setText(lecture.getTeacher());
        lectureType.setText(lecture.getTypeString());
        lectureTime.setText(lecture.getConvertedStartsAt());
        classroomName.setText(lecture.getClassroom().getName());

        return view;
    }


}
