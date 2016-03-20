package rs.devana.labs.studentinfo.presentation.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;

import rs.devana.labs.studentinfo.R;
import rs.devana.labs.studentinfo.domain.models.lecture.Lecture;
import rs.devana.labs.studentinfo.infrastructure.dagger.Injector;
import rs.devana.labs.studentinfo.infrastructure.event_bus_events.OpenLectureFragmentEvent;

public class LecturesView extends View {

    @Inject
    EventBus eventBus;

    private final static int SECONDS_IN_DAY = 86400;
    private final static int SECONDS_IN_HALF_DAY = 43200;
    private final static int START_DELAY_SECONDS = 900;
    private final static int SECONDS_IN_HOUR = 3600;
    private final static int LECTURES_IN_DAY = 12;
    private final static int START_DELAY_HOURS = 9;
    private final static float TEXT_SEPARATOR = 1 / 3f;
    private float lecturePaddingLeft;
    private float lecturePaddingTop;
    private float hourHeight;
    private int[] ind;
    private List<Lecture> lectures;
    private int day;
    private Context context;
    private float mDownX;
    private float mDownY;
    private boolean isOnClick;

    public LecturesView(Context context, List<Lecture> lectures) {
        super(context);
        this.context = context;
        this.lectures = lectures;
    }

    public LecturesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public LecturesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LecturesView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Injector.INSTANCE.getApplicationComponent().inject(this);

        lecturePaddingLeft = this.context.getResources().getDimensionPixelSize(R.dimen.lecturePaddingLeft);
        lecturePaddingTop = this.context.getResources().getDimensionPixelSize(R.dimen.lecturePaddingTop);
        ind = new int[13];
        for (int i = 0; i < 13; i++) {
            ind[i] = -1;
        }

        TextView textView = new TextView(context);
        float margin = textView.getTextSize() / 2;
        float height = getHeight();
        hourHeight = (height - 2 * margin) / LECTURES_IN_DAY;
        float width = getWidth();

        Paint paint = new Paint();
        if (lectures.size() > 0) {
            for (int i = 0; i < lectures.size(); i++) {
                if (lectures.get(i).getType() == 0) {
                    paint.setColor(ContextCompat.getColor(context, R.color.blue));
                } else {
                    paint.setColor(ContextCompat.getColor(context, R.color.orange));
                }
                int startHour = ((lectures.get(i).getStartsAt() % SECONDS_IN_DAY) - START_DELAY_SECONDS) / SECONDS_IN_HOUR;
                int endHour = (lectures.get(i).getEndsAt() % SECONDS_IN_DAY) / SECONDS_IN_HOUR;
                paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                paint.setTextSize(width / 25);
                paint.setTextAlign(Paint.Align.LEFT);
                canvas.drawRect(0, hourHeight * (startHour - START_DELAY_HOURS) + margin, width, hourHeight * (endHour - START_DELAY_HOURS) + margin, paint);
                paint.setColor(Color.parseColor("#FFFFFF"));

                canvas.drawLine(0, hourHeight * (endHour - START_DELAY_HOURS) + margin, width, hourHeight * (endHour - START_DELAY_HOURS) + margin, paint);

                int hours = endHour - startHour;

                drawClassroomName(canvas, lectures.get(i).getClassroom().getName(), hourHeight * (startHour - START_DELAY_HOURS + TEXT_SEPARATOR) + getCourseTextSize(hours) + getTeacherTextSize(hours) + margin, hours);
                drawTeacherName(canvas, lectures.get(i).getTeacher(), hourHeight * (startHour - START_DELAY_HOURS + TEXT_SEPARATOR) + getCourseTextSize(hours) + margin, hours);
                drawCourseName(canvas, lectures.get(i).getCourse().getName(), hourHeight * (startHour - START_DELAY_HOURS + TEXT_SEPARATOR) + margin, hours);

                for (int j = startHour; j < endHour; j++) {
                    ind[j - 9] = i;
                }
            }
        }
        paint.setColor(ContextCompat.getColor(context, R.color.grey));
        canvas.drawRect(0, 0, width, margin, paint);

        canvas.drawRect(0, height - margin, width, height, paint);

        drawTimeLine(canvas, height + margin, width);

        for (int i = 1; i < 12; i++) {
            if ((ind[i] < 0) && (ind[i - 1] < 0)) {
                paint.setColor(ContextCompat.getColor(context, R.color.black));
                canvas.drawLine(0, hourHeight * i + margin, width, hourHeight * i + margin, paint);
            }
        }
    }

    private void drawCourseName(Canvas canvas, String name, float height, int hours) {
        Paint paint = getPaint();
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextSize(getCourseTextSize(hours));

        canvas.drawText(name, lecturePaddingLeft, height + lecturePaddingTop, paint);
    }

    private void drawTeacherName(Canvas canvas, String name, float height, int hours) {
        Paint paint = getPaint();
        paint.setTextSize(getTeacherTextSize(hours));

        canvas.drawText(name, lecturePaddingLeft, height + lecturePaddingTop, paint);
    }

    private void drawClassroomName(Canvas canvas, String name, float height, int hours) {
        Paint paint = getPaint();
        paint.setTextSize(getClassroomTextSize(hours));

        canvas.drawText(name, lecturePaddingLeft, height + lecturePaddingTop, paint);
    }

    private Paint getPaint() {
        Paint paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.white));
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        return paint;
    }

    private void drawTimeLine(Canvas canvas, float height, float width) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int today = calendar.get(Calendar.DAY_OF_WEEK) - 2;
        if (today == -1) {
            today = 6;
        }
        if (today == day) {
            Paint paint = new Paint();
            paint.setColor(ContextCompat.getColor(context, R.color.silver));
            paint.setAlpha(127);

            long seconds = ((calendar.getTimeInMillis() + calendar.getTimeZone().getRawOffset()) / 1000 % SECONDS_IN_DAY) - 9 * SECONDS_IN_HOUR;
            canvas.drawRect(0, height * seconds / SECONDS_IN_HALF_DAY - lecturePaddingLeft, width, height * seconds / SECONDS_IN_HALF_DAY + lecturePaddingLeft, paint);
        }
    }

    private float getCourseTextSize(int hours) {
        if (hours == 1) {
            return context.getResources().getDimensionPixelSize(R.dimen.courseNameFor1);
        }
        if (hours == 2) {
            return context.getResources().getDimensionPixelSize(R.dimen.courseNameFor2);
        } else {
            return context.getResources().getDimensionPixelSize(R.dimen.courseNameFor3);
        }
    }

    private float getTeacherTextSize(int hours) {
        if (hours == 1) {
            return context.getResources().getDimensionPixelSize(R.dimen.teacherNameFor1);
        }
        if (hours == 2) {
            return context.getResources().getDimensionPixelSize(R.dimen.teacherNameFor2);
        } else {
            return context.getResources().getDimensionPixelSize(R.dimen.teacherNameFor3);
        }
    }

    private float getClassroomTextSize(int hours) {
        if (hours == 1) {
            return context.getResources().getDimensionPixelSize(R.dimen.classroomNameFor1);
        }
        if (hours == 2) {
            return context.getResources().getDimensionPixelSize(R.dimen.classroomNameFor2);
        } else {
            return context.getResources().getDimensionPixelSize(R.dimen.classroomNameFor3);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                isOnClick = true;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (isOnClick) {
                    if (ind[(int) (event.getY() / hourHeight)] >= 0) {
                        playSoundEffect(SoundEffectConstants.CLICK);
                        eventBus.post(new OpenLectureFragmentEvent(lectures.get(ind[(int) (event.getY() / hourHeight)])));
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float SCROLL_THRESHOLD = 3;
                if (isOnClick && (Math.abs(mDownX - event.getX()) > SCROLL_THRESHOLD || Math.abs(mDownY - event.getY()) > SCROLL_THRESHOLD)) {
                    isOnClick = false;
                }
                break;
            default:
                break;
        }
        return true;
    }

    public void setLectures(List<Lecture> lectures) {
        this.lectures = lectures;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
