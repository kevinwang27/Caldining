package MenuFragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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

import kevinwang.personal.caldining.MenusFragment;
import kevinwang.personal.caldining.R;

public class DisplayMenuFragment extends Fragment {

    private LinearLayout mMenuContainer;
    private LinearLayout mThirdMenu;
    private LinearLayout mFirstContainer;
    private LinearLayout mSecondContainer;
    private LinearLayout mThirdContainer;
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
        mMenuContainer = v.findViewById(R.id.menu_container);
        mThirdMenu = v.findViewById(R.id.third_menu);
        mFirstContainer = v.findViewById(R.id.first_container);
        mSecondContainer = v.findViewById(R.id.second_container);
        mThirdContainer = v.findViewById(R.id.third_container);
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
                try {
                    Document document = Jsoup.connect(url).get();
                    Element todayMenus = document.getElementsByClass("menu_wrap_overall").first();
                    Elements mealTitles = todayMenus.getElementsByClass("location_period");
                    if (mealTitles.size() == 3) {
                        for (Element title : mealTitles) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    
                                }
                            });
                        }
                    }
                    Elements menus = todayMenus.getElementsByClass("desc_wrap_ck3");
                    for (Element menu : menus) {
                        Elements stations = menu.getElementsByClass("station_wrap");

                    }
                } catch (IOException e) {
                    buildErrorAlert();
                }
            }
        }).start();
    }

}
