package kevinwang.personal.caldining;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mBottomNavigationView;
    private Toolbar toolbar;
    private Bundle bundle;
    private HashMap<String, Integer> projectedPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        bundle = intent.getBundleExtra("data");
        projectedPoints = (HashMap<String, Integer>) intent.getSerializableExtra("projectedPoints");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            startMenusFragment();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_menus:
                    startMenusFragment();
                    return true;
                case R.id.navigation_points:
                    startPointsFragment();
                    return true;
            }
            return false;
        }
    };

    private void startMenusFragment() {
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.Fragment fragment = manager.findFragmentByTag("MenusFragment");
        FragmentTransaction transaction = manager.beginTransaction();
        if (fragment == null) {
            fragment = MenusFragment.newInstance(bundle);
            transaction.replace(R.id.fragmentContainer, fragment, "MenusFragment");
            transaction.commit();
        } else {
            transaction.show(fragment);
            Fragment pointsFragment = manager.findFragmentByTag("PointsFragment");
            if (pointsFragment != null) {
                transaction.hide(pointsFragment);
            }
            transaction.commit();
        }
    }

    private void startPointsFragment() {
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.Fragment fragment = manager.findFragmentByTag("PointsFragment");
        FragmentTransaction transaction = manager.beginTransaction();
        if (fragment == null) {
            fragment = PointsFragment.newInstance(projectedPoints);
            transaction.replace(R.id.fragmentContainer, fragment, "PointsFragment");
            transaction.commit();
        } else {
            transaction.show(fragment);
            Fragment menusFragment = manager.findFragmentByTag("MenusFragment");
            if (menusFragment != null) {
                transaction.hide(menusFragment);
            }
            transaction.commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);

        return true;
    }

}
