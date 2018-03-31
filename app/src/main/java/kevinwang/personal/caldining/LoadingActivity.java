package kevinwang.personal.caldining;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.view.Window;
import android.widget.ProgressBar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoadingActivity extends AppCompatActivity {

    final ArrayList<String> cafe3Titles = new ArrayList<>();
    final List<List<String>> cafe3StationLists = new ArrayList<>();
    final ArrayList<String> clarkKerrTitles = new ArrayList<>();
    final List<List<String>> clarkKerrStationLists = new ArrayList<>();
    final ArrayList<String> crossroadsTitles = new ArrayList<>();
    final List<List<String>> crossroadsStationLists = new ArrayList<>();
    final ArrayList<String> foothillTitles = new ArrayList<>();
    final List<List<String>> foothillStationLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setExitTransition(new Fade());
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        scrapeWebsite("https://caldining.berkeley.edu/menu.php");
    }

    private void scrapeWebsite(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect(url).get();
                    Elements locationMenus = document.getElementsByClass("menu_wrap_overall");
                    for (Element locationMenu : locationMenus) {
                        String location = locationMenu.getElementsByClass("location2").first().text().trim();
                        ArrayList<String> titles;
                        List<List<String>> stationLists;
                        if (location.equals(MenusFragment.Location.CAFE3.toString())) {
                            titles = cafe3Titles;
                            stationLists = cafe3StationLists;
                        } else if (location.equals(MenusFragment.Location.CROSSROADS.toString())) {
                            titles = crossroadsTitles;
                            stationLists = crossroadsStationLists;
                        } else if (location.equals(MenusFragment.Location.FOOTHILL.toString())) {
                            titles = foothillTitles;
                            stationLists = foothillStationLists;
                        } else if (location.equals(MenusFragment.Location.CLARKKERR.toString())) {
                            titles = clarkKerrTitles;
                            stationLists = clarkKerrStationLists;
                        } else {
                            titles = crossroadsTitles;
                            stationLists = crossroadsStationLists;
                        }
                        Elements mealTitles = locationMenu.getElementsByClass("location_period");
                        for (Element title : mealTitles) {
                            titles.add(title.text());
                        }
                        Elements menus = locationMenu.getElementsByClass("desc_wrap_ck3");
                        for (int numMenus = 0; numMenus < menus.size(); numMenus++) {
                            stationLists.add(new ArrayList<String>());
                            Elements mealStations = menus.get(numMenus).select("p.station_wrap, p:not([class])");
                            for (Element station : mealStations) {
                                stationLists.get(numMenus).add(station.text());
                            }
                        }
                    }
                } catch (IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            buildErrorAlert();
                        }
                    });
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dataLoaded();
                    }
                });
            }
        }).start();
    }

    private void dataLoaded() {
        Intent intent = new Intent(this, MainActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("cafe3Titles", cafe3Titles);
        bundle.putSerializable("crossroadsTitles", crossroadsTitles);
        bundle.putSerializable("foothillTitles", foothillTitles);
        bundle.putSerializable("clarkKerrTitles", clarkKerrTitles);
        bundle.putSerializable("cafe3StationLists", (ArrayList) cafe3StationLists);
        bundle.putSerializable("crossroadsStationLists", (ArrayList) crossroadsStationLists);
        bundle.putSerializable("foothillStationLists", (ArrayList) foothillStationLists);
        bundle.putSerializable("clarkKerrStationLists", (ArrayList) clarkKerrStationLists);

        intent.putExtra("data", bundle);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivity(intent);
        }

    }

    private void buildErrorAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Unable to connect to Caldining")
                .setMessage("Please make sure you have internet connection and try again.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        startActivity(getIntent());
                    }
                });
        builder.create();
    }
}
