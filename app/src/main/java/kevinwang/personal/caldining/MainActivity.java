package kevinwang.personal.caldining;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mBottomNavigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                case R.id.navigation_hours:
                    startHoursFragment();
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
            fragment = new MenusFragment();
            transaction.replace(R.id.fragmentContainer, fragment, "MenusFragment");
            transaction.commit();
        } else {
            transaction.show(fragment);
            Fragment pointsFragment = manager.findFragmentByTag("PointsFragment");
            if (pointsFragment != null) {
                transaction.hide(pointsFragment);
            }
            Fragment hoursFragment = manager.findFragmentByTag("HoursFragment");
            if (hoursFragment != null) {
                transaction.hide(hoursFragment);
            }
            transaction.commit();
        }
    }

    private void startHoursFragment() {
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.Fragment fragment = manager.findFragmentByTag("HoursFragment");
        FragmentTransaction transaction = manager.beginTransaction();
        if (fragment == null) {
            fragment = new HoursFragment();
            transaction.replace(R.id.fragmentContainer, fragment, "HoursFragment");
            transaction.commit();
        } else {
            transaction.show(fragment);
            Fragment menusFragment = manager.findFragmentByTag("MenusFragment");
            if (menusFragment != null) {
                transaction.hide(menusFragment);
            }
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
            fragment = new PointsFragment();
            transaction.replace(R.id.fragmentContainer, fragment, "PointsFragment");
            transaction.commit();
        } else {
            transaction.show(fragment);
            Fragment menusFragment = manager.findFragmentByTag("MenusFragment");
            if (menusFragment != null) {
                transaction.hide(menusFragment);
            }
            Fragment hoursFragment = manager.findFragmentByTag("HoursFragment");
            if (hoursFragment != null) {
                transaction.hide(hoursFragment);
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
