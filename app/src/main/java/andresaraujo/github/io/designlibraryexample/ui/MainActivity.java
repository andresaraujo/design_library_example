package andresaraujo.github.io.designlibraryexample.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import andresaraujo.github.io.designlibraryexample.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout mDrawerLayout;
    private FloatingActionButton mFab;
    private TabLayout mTabLayout;

    Fragment mForecastFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        setupToolbar();
        setupViewPager(viewPager);
        setupTableLayout(viewPager);
        setupDrawerContent(navigationView);
        setupFab();

    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupTableLayout(ViewPager viewPager) {
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(viewPager);
    }

    private void setupFab() {
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(this);
    }

    private void setupDrawerContent(final NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        Snackbar.make(navigationView, menuItem.getTitle(), Snackbar.LENGTH_SHORT).show();
                        return true;
                    }
                });
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        mForecastFragment = new ForecastFragment();

        adapter.addFragment(mForecastFragment, "Forecast");
        adapter.addFragment(new ChatListFragment(), "Octocats");
        //adapter.addFragment(new ForecastFragment(), "...");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }



        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.fab:
                final SharedPreferences sharedPrefs =
                        PreferenceManager.getDefaultSharedPreferences(this);
                String location = sharedPrefs.getString(
                        getString(R.string.pref_location_key),
                        getString(R.string.pref_location_default));

                new MaterialDialog.Builder(this)
                        .title(R.string.pref_location_label)
                        //.content("Content")
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input("Location", location, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                SharedPreferences.Editor edit = sharedPrefs.edit();
                                edit.putString(getString(R.string.pref_location_key), input.toString());
                                edit.commit();
                                ((OnPrefLocationListener)mForecastFragment).onPrefLocationChanged();

                                Snackbar.make(view, "Location updated", Snackbar.LENGTH_LONG)
                                        .setAction("Dismiss", MainActivity.this).show();
                            }
                        }).show();
                break;
        }
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    public interface OnPrefLocationListener {
        void onPrefLocationChanged();
    }
}
