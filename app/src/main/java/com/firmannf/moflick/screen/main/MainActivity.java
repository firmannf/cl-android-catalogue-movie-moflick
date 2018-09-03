package com.firmannf.moflick.screen.main;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.firmannf.moflick.R;
import com.firmannf.moflick.data.source.local.MovieHelper;
import com.firmannf.moflick.screen.loved.LovedFragment;
import com.firmannf.moflick.screen.now_playing.NowPlayingFragment;
import com.firmannf.moflick.screen.popular.PopularFragment;
import com.firmannf.moflick.screen.search.SearchFragment;
import com.firmannf.moflick.screen.upcoming.UpcomingFragment;
import com.firmannf.moflick.util.AppPreference;
import com.firmannf.moflick.util.receiver.DailyReleaseNotificationReceiver;
import com.firmannf.moflick.util.receiver.DailyReminderNotificationReceiver;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.main_toolbar)
    Toolbar toolbar;
    @BindView(R.id.main_bottomnavigation)
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        new DailyReminderNotificationReceiver().setNotification(this);
        new DailyReleaseNotificationReceiver().setNotification(this);

        AppPreference appPreference = new AppPreference(this);
        Boolean firstRun = appPreference.getFirstRun();
        if (firstRun) {
            MovieHelper movieHelper = new MovieHelper(this);
            movieHelper.open();
            movieHelper.close();
            appPreference.setFirstRun(false);
        }

        setupToolbar();
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            getSupportActionBar().setTitle(getString(R.string.text_title_now_playing));
            replaceFragment(NowPlayingFragment.newInstance());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        final SearchView searchView =
                (SearchView) menu.findItem(R.id.main_menu_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                addFragment(SearchFragment.newInstance(query), SearchFragment.class.getSimpleName());

                // Hide Keyboard
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        MenuItem menuItemSearch = menu.findItem(R.id.main_menu_search);
        menuItemSearch.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                bottomNavigationView.setVisibility(View.VISIBLE);
                removeFragment(SearchFragment.class.getSimpleName());
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.main_menu_changelanguage:
                Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.navigation_nowplaying:
                setToolbarTitle(getString(R.string.text_title_now_playing));
                replaceFragment(NowPlayingFragment.newInstance());
                break;
            case R.id.navigation_popular:
                setToolbarTitle(getString(R.string.text_title_popular));
                replaceFragment(PopularFragment.newInstance());
                break;
            case R.id.navigation_upcoming:
                setToolbarTitle(getString(R.string.text_title_upcoming));
                replaceFragment(UpcomingFragment.newInstance());
                break;
            case R.id.navigation_loved:
                setToolbarTitle(getString(R.string.text_title_loved));
                replaceFragment(LovedFragment.newInstance());
                break;
        }

        return true;
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.app_name));
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_framelayout_container, fragment)
                .commit();
    }

    private void addFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.main_framelayout_container, fragment, tag)
                .commit();
    }

    private void removeFragment(String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment != null)
            fragmentManager.beginTransaction()
                    .remove(fragmentManager.findFragmentByTag(tag))
                    .commit();
    }

    public BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }

    private void setToolbarTitle(String title) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
    }
}
