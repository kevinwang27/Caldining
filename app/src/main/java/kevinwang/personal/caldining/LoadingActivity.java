package kevinwang.personal.caldining;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Log;
import android.view.Window;
import android.widget.ProgressBar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class LoadingActivity extends AppCompatActivity {

    int dayOfYear;
    int currentYear;

    final ArrayList<String> cafe3Titles = new ArrayList<>();
    final List<List<String>> cafe3StationLists = new ArrayList<>();
    final ArrayList<String> clarkKerrTitles = new ArrayList<>();
    final List<List<String>> clarkKerrStationLists = new ArrayList<>();
    final ArrayList<String> crossroadsTitles = new ArrayList<>();
    final List<List<String>> crossroadsStationLists = new ArrayList<>();
    final ArrayList<String> foothillTitles = new ArrayList<>();
    final List<List<String>> foothillStationLists = new ArrayList<>();

    final HashMap<String, Integer> projectedPoints = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setExitTransition(new Fade());
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Calendar currentTime = Calendar.getInstance();
        dayOfYear = currentTime.get(Calendar.DAY_OF_YEAR);
        currentYear = currentTime.get(Calendar.YEAR);

        scrapeWebsite("https://caldining.berkeley.edu/menu.php", "http://caldining.berkeley.edu/meal-plans/how-it-works/budgeting-your-points");
    }

    private void scrapeWebsite(final String menuUrl, final String pointsUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect(menuUrl).get();
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

                try {
                    Document document = Jsoup.connect(pointsUrl).get();
                    Elements tables = document.getElementsByTag("table");
                    Elements rows;
                    int numDays;
                    if (dayOfYear >= 212) {
                        rows = tables.get(0).getElementsByTag("tr");
                        numDays = rows.size() * 7;
                    } else {
                        rows = tables.get(1).getElementsByTag("tr");
                        numDays = rows.size() * 7;
                    }
                    String startDay = rows.get(1).getElementsByTag("td").get(0).text();
                    int startMonthCal, startDayCal;
                    if (startDay.toLowerCase().contains("aug")) {
                        startMonthCal = 7;
                    } else {
                        startMonthCal = 0;
                    }
                    startDayCal = Integer.valueOf(startDay.replaceAll("[^0-9]", ""));

                    Calendar startDayCalendar = new GregorianCalendar(currentYear, startMonthCal, startDayCal);
                    int startDayOfYear = startDayCalendar.get(Calendar.DAY_OF_YEAR);
                    int difference = dayOfYear - startDayOfYear;
                    Log.d("diff", difference + "");
                    double multiplicationFactor = 1.0 - (1.0 * difference) / numDays;
                    projectedPoints.put("Standard", (int) Math.round(1250.0 * multiplicationFactor));
                    projectedPoints.put("Premium", (int) Math.round(1500.0 * multiplicationFactor));
                    projectedPoints.put("Blue", (int) Math.round(600.0 * multiplicationFactor));
                    projectedPoints.put("Gold", (int) Math.round(875.0 * multiplicationFactor));
                    projectedPoints.put("Platinum", (int) Math.round(1150.0 * multiplicationFactor));
                    projectedPoints.put("Ultimate", -1);

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
        intent.putExtra("projectedPoints", projectedPoints);

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
