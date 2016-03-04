package rs.devana.labs.studentinfo.presentation.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ListView;

public class LectureView extends ListView {

    public LectureView(Context context) {
        super(context);
    }

    public LectureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LectureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LectureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    
}
