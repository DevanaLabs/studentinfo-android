package rs.devana.labs.studentinfo.presentation.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import rs.devana.labs.studentinfo.R;

public class ScheduleFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;
    private Context context;

    public ScheduleFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments, Context context) {
        super(fm);
        this.fragments = fragments;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return translate(position);
    }

    private CharSequence translate(int position) {
        String day;
        switch (position) {
            case 0:
                day = context.getString(R.string.monday);
                break;
            case 1:
                day = context.getString(R.string.tuesday);
                break;
            case 2:
                day = context.getString(R.string.wednesday);
                break;
            case 3:
                day = context.getString(R.string.thursday);
                break;
            case 4:
                day = context.getString(R.string.friday);
                break;
            case 5:
                day = context.getString(R.string.saturday);
                break;
            default:
                day = context.getString(R.string.sunday);
        }
        return day;
    }
}
