package kevinwang.personal.caldining;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import MenuFragments.DisplayMenuFragment;

public class MenusFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public MenusFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_menus, container, false);
        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);

        viewPager.setAdapter(new MenusAdapter(getChildFragmentManager()));
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        return v;
    }

    private class MenusAdapter extends FragmentPagerAdapter {

        MenusAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return DisplayMenuFragment.newInstance(Location.CROSSROADS);
                case 1:
                    return DisplayMenuFragment.newInstance(Location.CAFE3);
                case 2:
                    return DisplayMenuFragment.newInstance(Location.FOOTHILL);
                case 3:
                    return DisplayMenuFragment.newInstance(Location.CLARKKERR);
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "Crossroads";
                case 1:
                    return "Cafe 3";
                case 2:
                    return "Foothill";
                case 3:
                    return "Clark Kerr";
            }
            return null;
        }

    }

    public enum Location {
        CROSSROADS, CAFE3, FOOTHILL, CLARKKERR;

        @Override
        public String toString() {
            switch (this) {
                case CROSSROADS:
                    return "Crossroads";
                case CAFE3:
                    return "Cafe_3";
                case FOOTHILL:
                    return "Foothill";
                case CLARKKERR:
                    return "Clark_Kerr_Campus";
                default:
                    return "Crossroads";
            }
        }
    }
}
