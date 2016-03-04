package rs.devana.labs.studentinfo.presentation.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;

    public ScheduleFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
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
                day = "PON";
                break;
            case 1:
                day = "UTO";
                break;
            case 2:
                day = "SRE";
                break;
            case 3:
                day = "ČET";
                break;
            case 4:
                day = "PET";
                break;
            case 5:
                day = "SUB";
                break;
            default: day = "NED";
        }
        return day;
    }
}
