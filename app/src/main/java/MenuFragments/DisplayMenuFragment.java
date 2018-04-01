package MenuFragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kevinwang.personal.caldining.MenusFragment;
import kevinwang.personal.caldining.R;

public class DisplayMenuFragment extends Fragment {

    private LinearLayout mMenuContainer;
    private LinearLayout mSecondMenu;
    private LinearLayout mThirdMenu;
    private ArrayList<LinearLayout> mContainers;
    private TextView mFirstTitle;
    private TextView mSecondTitle;
    private TextView mThirdTitle;

    public DisplayMenuFragment() {}

    public static DisplayMenuFragment newInstance(ArrayList<String> titles, List<List<String>> stations) {
        DisplayMenuFragment displayMenuFragment = new DisplayMenuFragment();

        Bundle args = new Bundle();
        args.putSerializable("titles", titles);
        args.putSerializable("stations", (ArrayList) stations);
        displayMenuFragment.setArguments(args);

        return displayMenuFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_display_menu, container, false);
        mContainers = new ArrayList<>();
        mMenuContainer = v.findViewById(R.id.menu_container);
        mSecondMenu = v.findViewById(R.id.second_menu);
        mThirdMenu = v.findViewById(R.id.third_menu);
        mContainers.add((LinearLayout) v.findViewById(R.id.first_container));
        mContainers.add((LinearLayout) v.findViewById(R.id.second_container));
        mContainers.add((LinearLayout) v.findViewById(R.id.third_container));
        mFirstTitle = v.findViewById(R.id.first_title);
        mSecondTitle = v.findViewById(R.id.second_title);
        mThirdTitle = v.findViewById(R.id.third_title);

        ArrayList<String> titles = (ArrayList<String>) getArguments().getSerializable("titles");
        List<List<String>> stations = (List<List<String>>) getArguments().getSerializable("stations");

        setTitles(titles);

        for (int i = 0; i < mContainers.size(); i++) {
            createStations(mContainers.get(i), stations.get(i));
        }
        return v;
    }

    private void setTitles(ArrayList<String> titles) {
        switch (titles.size()) {
            case 1:
                mFirstTitle.setText(titles.get(0));
                mMenuContainer.removeView(mSecondMenu);
                mMenuContainer.removeView(mThirdMenu);
                mContainers.remove(2);
                mContainers.remove(1);
                break;
            case 2:
                mFirstTitle.setText(titles.get(0));
                mSecondTitle.setText(titles.get(1));
                mMenuContainer.removeView(mThirdMenu);
                mContainers.remove(2);
                break;
            case 3:
                mFirstTitle.setText(titles.get(0));
                mSecondTitle.setText(titles.get(1));
                mThirdTitle.setText(titles.get(2));
                break;
            default:
                //numMenus = 0;
                //weird menu??
        }
    }

    private void createStations(LinearLayout container, List<String> stations) {
        for (String station : stations) {
            final LinearLayout menuStation = new LinearLayout(getContext());
            menuStation.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 5, 0 ,5);
            menuStation.setLayoutParams(params);

            final TextView stationEntry = new TextView(getContext());
            stationEntry.setTextColor(Color.BLACK);
            stationEntry.setText(station);
            if (isUpperCase(station)) {
                stationEntry.setTextSize(17);
                stationEntry.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params1.setMargins(0, 10, 0, 10);
                stationEntry.setLayoutParams(params1);
            }
            menuStation.addView(stationEntry);

            container.addView(menuStation);
        }
    }

    private boolean isUpperCase(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != ' ' && !Character.isUpperCase(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

}
