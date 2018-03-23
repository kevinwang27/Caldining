package MenuFragments;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
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
    private LinearLayout[] mContainers;
    private TextView mFirstTitle;
    private TextView mSecondTitle;
    private TextView mThirdTitle;

    public DisplayMenuFragment() {}

    public static DisplayMenuFragment newInstance(MenusFragment.Location location) {
        DisplayMenuFragment displayMenuFragment = new DisplayMenuFragment();

        Bundle args = new Bundle();
        args.putSerializable("location", location);
        displayMenuFragment.setArguments(args);

        return displayMenuFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_display_menu, container, false);
        mContainers = new LinearLayout[3];
        mMenuContainer = v.findViewById(R.id.menu_container);
        mSecondMenu = v.findViewById(R.id.second_menu);
        mThirdMenu = v.findViewById(R.id.third_menu);
        mContainers[0] = v.findViewById(R.id.first_container);
        mContainers[1] = v.findViewById(R.id.second_container);
        mContainers[2] = v.findViewById(R.id.third_container);
        mFirstTitle = v.findViewById(R.id.first_title);
        mSecondTitle = v.findViewById(R.id.second_title);
        mThirdTitle = v.findViewById(R.id.third_title);

        MenusFragment.Location mLocation = (MenusFragment.Location) getArguments().getSerializable("location");
        String locationAbbr;
        switch (mLocation) {
            case CROSSROADS:
                locationAbbr = "xr";
                break;
            case CAFE3:
                locationAbbr = "c3";
                break;
            case FOOTHILL:
                locationAbbr = "fh";
                break;
            case CLARKKERR:
                locationAbbr = "ckc";
                break;
            default:
                locationAbbr = "xr";
        }
        String url = "https://caldining.berkeley.edu/menu_" + locationAbbr + ".php";
        scrapeWebsite(url, mLocation);

        return v;
    }

    private void buildErrorAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Unable to connect to Caldining")
                .setMessage("Please make sure you have internet connection and try again.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create();
    }

    private void scrapeWebsite(final String url, final MenusFragment.Location mLocation) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<String> titles = new ArrayList<>();
                final List<List<String>> stationLists = new ArrayList<>();
                final List<List<List<String>>> foodListofLists = new ArrayList<>();
                try {
                    Document document = Jsoup.connect(url).get();
                    Element todayMenus = document.getElementsByClass("menu_wrap_overall").first();
                    Elements mealTitles = todayMenus.getElementsByClass("location_period");
                    for (Element title : mealTitles) {
                        titles.add(title.text());
                    }
                    Elements menus = todayMenus.getElementsByClass("desc_wrap_ck3");
                    for (int numMenus = 0; numMenus < menus.size(); numMenus++) {
                        stationLists.add(new ArrayList<String>());
                        foodListofLists.add(new ArrayList<List<String>>());
                        Elements mealStations = menus.get(numMenus).getElementsByClass("station_wrap");
                        for (int numStations = 0; numStations < mealStations.size(); numStations++) {
                            stationLists.get(numMenus).add(mealStations.get(numStations).text());
                            foodListofLists.get(numMenus).add(new ArrayList<String>());
                            Elements mealFoods = mealStations.get(numStations).siblingElements();
                            for (Element food : mealFoods) {
                                foodListofLists.get(numMenus).get(numStations).add(food.text());
                            }
                        }
                    }
                } catch (IOException e) {
                    buildErrorAlert();
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setTitles(titles);
                        for (int i = 0; i < stationLists.size(); i++) {
                            createStations(mContainers[i], stationLists.get(i), foodListofLists.get(i));
                        }
                    }
                });
            }
        }).start();
    }

    private void setTitles(ArrayList<String> titles) {
        switch (titles.size()) {
            case 1:
                mFirstTitle.setText(titles.get(0));
                mMenuContainer.removeView(mSecondMenu);
                mMenuContainer.removeView(mThirdMenu);
                break;
            case 2:
                mFirstTitle.setText(titles.get(0));
                mSecondTitle.setText(titles.get(1));
                mMenuContainer.removeView(mThirdMenu);
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

    private void createStations(LinearLayout container, List<String> stations, List<List<String>> foodList) {
        for (int i = 0; i < stations.size(); i++) {
            LinearLayout menuStation = new LinearLayout(getContext());
            menuStation.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            menuStation.setLayoutParams(params);

            TextView stationTitle = new TextView(getContext());
            stationTitle.setTextColor(Color.BLACK);
            stationTitle.setText(stations.get(i));
            container.addView(stationTitle);

            setFoods(menuStation, foodList.get(i));
        }
    }

    private void setFoods(LinearLayout container, List<String> foods) {
        for (String food : foods) {
            TextView foodText = new TextView(getContext());
            foodText.setTextColor(Color.BLACK);
            foodText.setText(food);
            container.addView(foodText);
        }
    }

}
