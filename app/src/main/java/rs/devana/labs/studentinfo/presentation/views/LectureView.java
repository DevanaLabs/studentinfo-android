package rs.devana.labs.studentinfo.presentation.views;

import java.util.Calendar;

import rs.devana.labs.studentinfo.domain.models.lecture.Lecture;
import rs.devana.labs.studentinfo.presentation.weekview.WeekViewEvent;

public class LectureView extends WeekViewEvent {

    protected Lecture lecture;

    public LectureView(Lecture lecture) {
        super(lecture.getId(), lecture.getLectureName(), convert(lecture.getStartsAt()), convert(lecture.getEndsAt()));
        this.lecture = lecture;
    }

    private static Calendar convert(int time) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);

        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());

        calendar.add(Calendar.SECOND, time);
        return calendar;
    }
}
