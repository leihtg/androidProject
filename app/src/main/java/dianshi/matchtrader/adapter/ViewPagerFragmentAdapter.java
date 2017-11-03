package dianshi.matchtrader.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/9 0009.
 */
public class ViewPagerFragmentAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> array;
    private List<String> tagList;
    FragmentManager fragmentManager;

    public ViewPagerFragmentAdapter(FragmentManager fragmentManager, ArrayList<Fragment> array) {
        super(fragmentManager);
        this.array = array;
        this.fragmentManager = fragmentManager;
        tagList = new ArrayList<String>();

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public Fragment getItem(int position) {
        return array.get(position);
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        tagList.add(makeFragmentName(container.getId(), (int) getItemId(position)));
        return super.instantiateItem(container, position);
    }

    public static String makeFragmentName(int viewId, long index) {
        return "android:switcher:" + viewId + ":" + index;
    }


}
