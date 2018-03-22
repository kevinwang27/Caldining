package kevinwang.personal.caldining;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import MenuFragments.Cafe3Fragment;
import MenuFragments.ClarkKerrFragment;
import MenuFragments.CrossroadsFragment;
import MenuFragments.FoothillFragment;

public class MenusFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public MenusFragment() {}

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
                    return new CrossroadsFragment();
                case 1:
                    return new ClarkKerrFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "Crossroads";
                case 1:
                    return "Clark Kerr";
            }
            return null;
        }

    }

}
