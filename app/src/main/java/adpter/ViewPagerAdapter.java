package adpter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.suvlas.CustomMenuFragment;
import com.suvlas.MenuFragment;
/**
 * Created by hp on 11-Aug-17.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    String restaurant_name,restaurant_id;
    public ViewPagerAdapter(FragmentManager fm,String restaurant_name,String restaurant_id) {
        super(fm);
        this.restaurant_name = restaurant_name;
        this.restaurant_id = restaurant_id;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MenuFragment.newInstance(restaurant_name,restaurant_id);
            case 1:
                return CustomMenuFragment.newInstance(restaurant_name,restaurant_id);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "MENU";
            case 1:
                return "COMBO";
            default:
                return null;
        }
    }
}