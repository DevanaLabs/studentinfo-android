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
import android.view.View;

import java.util.List;

import rs.devana.labs.studentinfo.R;
import rs.devana.labs.studentinfo.domain.models.lecture.Lecture;

public class LecturesView extends View {

    private final static int SECONDS_IN_DAY = 86400;
    private final static int START_DELAY_SECONDS = 900;
    private final static int SECONDS_IN_HOUR = 3600;
    private final static int LECTURES_IN_DAY = 12;
    private final static int START_DELAY_HOURS = 9;
    private final static float TEXT_SEPARATOR = 1 / 3f;
    private List<Lecture> lectures;
    private Context context;

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

        boolean[] ind = new boolean[13];

        float height = getHeight();
        float hourHeight = height / LECTURES_IN_DAY;
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
                canvas.drawRect(0, hourHeight * (startHour - START_DELAY_HOURS), width, hourHeight * (endHour - START_DELAY_HOURS), paint);
                paint.setColor(Color.parseColor("#FFFFFF"));

                float heightCourseName = Math.min(3 * width / lectures.get(i).getCourse().getName().length(), height / 25);
                canvas.drawLine(0, hourHeight * (endHour - START_DELAY_HOURS), width, hourHeight * (endHour - START_DELAY_HOURS), paint);

                canvas.drawText(lectures.get(i).getTypeString(), 0, hourHeight * (startHour - START_DELAY_HOURS + TEXT_SEPARATOR) + heightCourseName + width / 25, paint);
                canvas.drawText(lectures.get(i).getTeacher(), 0, hourHeight * (startHour - START_DELAY_HOURS + TEXT_SEPARATOR) + heightCourseName, paint);

                drawCourseName(canvas, lectures.get(i).getCourse().getName(), hourHeight * (startHour - START_DELAY_HOURS + TEXT_SEPARATOR), heightCourseName);


                for (int j = startHour; j <= endHour; j++) {
                    ind[j - 9] = true;
                }
            }
        }
        for (int i = 1; i < 12; i++) {
            if (!ind[i]) {
                paint.setColor(Color.parseColor("#000000"));
                canvas.drawLine(0, hourHeight * i, width, hourHeight * i, paint);
            }
        }
    }

    private void drawCourseName(Canvas canvas, String name, float height, float size) {
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#FFFFFF"));
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextSize(size);
        canvas.drawText(name, 0, height, paint);
    }


    public void setLectures(List<Lecture> lectures) {
        this.lectures = lectures;
    }
}
