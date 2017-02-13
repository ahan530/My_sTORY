package qf.com.my_story.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linke on 2017/1/17 0017.
 * email:linke0530@163.com.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter{
    private   List<Fragment> fragment_data;
    public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragment_data) {
        super(fm);
        this.fragment_data = fragment_data;
    }
    @Override
    public int getCount() {
        return fragment_data.size();
    }
    @Override
    public Fragment getItem(int position) {
        return fragment_data.get(position);
    }


}
