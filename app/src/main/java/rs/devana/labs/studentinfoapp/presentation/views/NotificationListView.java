package rs.devana.labs.studentinfoapp.presentation.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class NotificationListView extends ListView {
    public NotificationListView(Context context) {
        super(context);
    }

    public NotificationListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NotificationListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NotificationListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
//        int mDiffX = 0;
//        int mDiffY = 0;
//        float mLastX = 0;
//        float mLastY = 0;
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                // reset difference values
//                mDiffX = 0;
//                mDiffY = 0;
//
//                mLastX = ev.getX();
//                mLastY = ev.getY();
//                break;
//
//            case MotionEvent.ACTION_MOVE:
////                final float curX = ev.getX();
////                final float curY = ev.getY();
////                mDiffX += Math.abs(curX - mLastX);
////                mDiffY += Math.abs(curY - mLastY);
////                mLastX = curX;
////                mLastY = curY;
////
////                // don't intercept event, when user tries to scroll vertically
////                if (mDiffX > mDiffY) {
////                    return false; // do not react to horizontal touch events, these events will be passed to your list item view
////                }
//                return true;
//        }
//
//        return super.onInterceptTouchEvent(ev);
    }
}