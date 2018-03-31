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

import java.util.ArrayList;
import java.util.List;

import MenuFragments.DisplayMenuFragment;

public class MenusFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private ArrayList<String> cafe3Titles;
    private List<List<String>> cafe3StationLists;
    private ArrayList<String> clarkKerrTitles;
    private List<List<String>> clarkKerrStationLists;
    private ArrayList<String> crossroadsTitles;
    private List<List<String>> crossroadsStationLists;
    private ArrayList<String> foothillTitles;
    private List<List<String>> foothillStationLists;

    public MenusFragment() {}

    public static MenusFragment newInstance(Bundle bundle) {
        MenusFragment menusFragment = new MenusFragment();
        menusFragment.setArguments(bundle);
        return menusFragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        Bundle data = getArguments();

        cafe3Titles = (ArrayList<String>) data.getSerializable("cafe3Titles");
        crossroadsTitles = (ArrayList<String>) data.getSerializable("crossroadsTitles");
        foothillTitles = (ArrayList<String>) data.getSerializable("foothillTitles");
        clarkKerrTitles = (ArrayList<String>) data.getSerializable("clarkKerrTitles");
        cafe3StationLists = (List<List<String>>) data.getSerializable("cafe3StationLists");
        crossroadsStationLists = (List<List<String>>) data.getSerializable("crossroadsStationLists");
        foothillStationLists = (List<List<String>>) data.getSerializable("foothillStationLists");
        clarkKerrStationLists = (List<List<String>>) data.getSerializable("clarkKerrStationLists");
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
                    return DisplayMenuFragment.newInstance(crossroadsTitles, crossroadsStationLists);
                case 1:
                    return DisplayMenuFragment.newInstance(cafe3Titles, cafe3StationLists);
                case 2:
                    return DisplayMenuFragment.newInstance(foothillTitles, foothillStationLists);
                case 3:
                    return DisplayMenuFragment.newInstance(clarkKerrTitles, clarkKerrStationLists);
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
