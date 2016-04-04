package rs.devana.labs.studentinfoapp.presentation.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;

import rs.devana.labs.studentinfoapp.R;
import rs.devana.labs.studentinfoapp.domain.models.lecture.Lecture;
import rs.devana.labs.studentinfoapp.domain.models.notification.lecture.LectureNotification;
import rs.devana.labs.studentinfoapp.infrastructure.dagger.Injector;
import rs.devana.labs.studentinfoapp.infrastructure.event_bus_events.OpenLectureFragmentEvent;

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
    private float hourHeight;
    private int[] lectureViews;
    private GestureDetectorCompat mGestureDetector;
    private float margin;

    private List<Lecture> lectures;
    private int day;
    private Context context;

    public LecturesView(Context context) {
        super(context);
        this.context = context;
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
        mGestureDetector = new GestureDetectorCompat(context, mGestureListener);
        lecturePaddingLeft = getResources().getDimensionPixelSize(R.dimen.lecturePaddingLeft);
        lectureViews = new int[13];

        for (int i = 0; i < 13; i++) {
            lectureViews[i] = -1;
        }

        margin = new TextView(context).getTextSize() / 2;

        float height = getHeight();
        hourHeight = (height - 2 * margin) / LECTURES_IN_DAY;
        float width = getWidth();

        Paint paint = new Paint();
        if (lectures.size() > 0) {
            for (int i = 0; i < lectures.size(); i++) {
                int startHour = ((lectures.get(i).getStartsAt() % SECONDS_IN_DAY) - START_DELAY_SECONDS) / SECONDS_IN_HOUR;
                int endHour = (lectures.get(i).getEndsAt() % SECONDS_IN_DAY) / SECONDS_IN_HOUR;
                int hours = endHour - startHour;

                paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                paint.setTextAlign(Paint.Align.LEFT);

                if (lectures.get(i).getType() == 0) {
                    paint.setColor(ContextCompat.getColor(context, R.color.blue));
                } else {
                    paint.setColor(ContextCompat.getColor(context, R.color.orange));
                }

                canvas.drawRect(0, hourHeight * (startHour - START_DELAY_HOURS) + margin, width, hourHeight * (endHour - START_DELAY_HOURS) + margin, paint);
                drawNames(canvas, lectures.get(i).getCourse().getName(), lectures.get(i).getTeacher(), lectures.get(i).getClassroom().getName(), hourHeight * (startHour - START_DELAY_HOURS + TEXT_SEPARATOR) + margin, hours);
                drawNotificationCircle(canvas, lectures.get(i), hourHeight * (endHour - START_DELAY_HOURS) + margin, width);

                for (int j = startHour; j < endHour; j++) {
                    lectureViews[j - 9] = i;
                }
            }
        }

        paint.setColor(ContextCompat.getColor(context, R.color.grey));

        canvas.drawRect(0, 0, width, margin, paint); //top margin

        canvas.drawRect(0, height - margin, width, height, paint); //bottom margin

        drawTimeLine(canvas, height, width); //time line

        drawLines(canvas, paint, margin, width); //lines between lectures and in empty part of view
    }

    private void drawNotificationCircle(Canvas canvas, Lecture lecture, float height, float width) {
        List<LectureNotification> lectureNotifications = lecture.getLectureNotifications();
        if (lectureNotifications!= null && !lectureNotifications.isEmpty()){
            for (int i = 0; i < lectureNotifications.size(); i++){
                if (lectureNotifications.get(i).getExpiresAt().getTimeInMillis() > Calendar.getInstance().getTimeInMillis()){
                    Paint paint = getPaint();
                    Drawable iconDrawable = ContextCompat.getDrawable(context, R.drawable.notification_icon);
                    Bitmap icon = drawableToBitmap(iconDrawable, context);
                    canvas.drawBitmap(icon, width - icon.getWidth() - getResources().getDimensionPixelSize(R.dimen.Margin4dp), height - icon.getHeight() - getResources().getDimensionPixelSize(R.dimen.Margin4dp), paint);
                    break;
                }
            }
        }
    }

    private void drawLines(Canvas canvas, Paint paint, float margin, float width) {
        for (int i = 1; i < 12; i++) {
            if ((lectureViews[i] < 0) && (lectureViews[i - 1] < 0)) {
                paint.setColor(ContextCompat.getColor(context, R.color.black));
                canvas.drawLine(0, hourHeight * i + margin, width, hourHeight * i + margin, paint);
            }
            if ((lectureViews[i - 1] >= 0) && (lectureViews[i - 1] != lectureViews[i])) {
                paint.setColor(ContextCompat.getColor(context, R.color.white));
                canvas.drawLine(0, hourHeight * i + margin, width, hourHeight * i + margin, paint);
            }

        }
    }

    private void drawNames(Canvas canvas, String courseName, String teacherName, String classroomName, float height, int hours) {
        Paint paint = getPaint();
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextSize(getCourseTextSize(hours));

        TextPaint textPaint = getTextPaint();
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        textPaint.setTextSize(getCourseTextSize(hours));

        StaticLayout staticLayoutCourseName = new StaticLayout(courseName, textPaint, getWidth(), Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        StaticLayout staticLayoutTeacherName = new StaticLayout(teacherName, textPaint, getWidth(), Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        StaticLayout staticLayoutClassroomName = new StaticLayout(classroomName, textPaint, getWidth(), Layout.Alignment.ALIGN_NORMAL, 1, 1, false);

        canvas.save();
        canvas.translate(lecturePaddingLeft, height + getLecturePaddingTop(hours) - margin - getResources().getDimensionPixelSize(R.dimen.paddingScheduleHourLeft));
        staticLayoutCourseName.draw(canvas);
        canvas.restore();

        canvas.save();
        textPaint.setTextSize(getTeacherTextSize(hours));
        textPaint.setTextSize(getClassroomTextSize(hours));
        textPaint.setAlpha(255 * 7 / 10);
        canvas.translate(lecturePaddingLeft, height + getLecturePaddingTop(hours) - margin - getResources().getDimensionPixelSize(R.dimen.paddingScheduleHourLeft) + staticLayoutCourseName.getHeight());
        staticLayoutTeacherName.draw(canvas);
        canvas.restore();

        canvas.save();
        textPaint.setTextSize(getClassroomTextSize(hours));
        textPaint.setAlpha(255 * 7 / 10);
        canvas.translate(lecturePaddingLeft, height + getLecturePaddingTop(hours) - margin - getResources().getDimensionPixelSize(R.dimen.paddingScheduleHourLeft) + staticLayoutCourseName.getHeight() + staticLayoutTeacherName.getHeight());
        staticLayoutClassroomName.draw(canvas);
        canvas.restore();
    }

    private Paint getPaint() {
        Paint paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.white));
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        return paint;
    }

    private TextPaint getTextPaint() {
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(ContextCompat.getColor(context, R.color.white));
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        return textPaint;
    }

    private void drawTimeLine(Canvas canvas, float height, float width) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Belgrade"));
        float timeLineMargin = getResources().getDimensionPixelSize(R.dimen.timeLineMargin);
        int today = calendar.get(Calendar.DAY_OF_WEEK) - 2;
        if (today == -1) {
            today = 6;
        }
        if (today == day) {
            Paint paint = new Paint();
            paint.setColor(ContextCompat.getColor(context, R.color.black));
            paint.setAlpha(255 * 2 / 10);

            long seconds = ((calendar.getTimeInMillis() + calendar.getTimeZone().getRawOffset() + calendar.getTimeZone().getDSTSavings()) / 1000 % SECONDS_IN_DAY) - 9 * SECONDS_IN_HOUR;
            canvas.drawRect(0, height * seconds / SECONDS_IN_HALF_DAY - timeLineMargin, width, height * seconds / SECONDS_IN_HALF_DAY + timeLineMargin, paint);
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

    private float getLecturePaddingTop(int hours) {
        if (hours == 1) {
            return context.getResources().getDimensionPixelSize(R.dimen.lecturePaddingTopFor1);
        }
        if (hours == 2) {
            return context.getResources().getDimensionPixelSize(R.dimen.lecturePaddingTopFor2);
        } else {
            return context.getResources().getDimensionPixelSize(R.dimen.lecturePaddingTopFor3);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean click = mGestureDetector.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_UP && click) {
            return true;
        }
        return true;
    }

    public void setLectures(List<Lecture> lectures) {
        this.lectures = lectures;
    }

    public void setDay(int day) {
        this.day = day;
    }

    private final GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {

            if (lectureViews[(int) (event.getY() / hourHeight)] >= 0) {
                playSoundEffect(SoundEffectConstants.CLICK);
                eventBus.post(new OpenLectureFragmentEvent(lectures.get(lectureViews[(int) (event.getY() / hourHeight)])));

                return true;
            }

            return super.onSingleTapUp(event);
        }
    };

    public static Bitmap drawableToBitmap (Drawable drawable, Context context) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, context.getResources().getDimensionPixelOffset(R.dimen.notificationIconSize), context.getResources().getDimensionPixelOffset(R.dimen.notificationIconSize), true);
        Canvas canvas = new Canvas(scaledBitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return scaledBitmap;
    }
}
