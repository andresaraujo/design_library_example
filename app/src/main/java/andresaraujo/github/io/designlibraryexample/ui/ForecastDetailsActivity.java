package andresaraujo.github.io.designlibraryexample.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.parceler.Parcels;

import andresaraujo.github.io.designlibraryexample.R;
import andresaraujo.github.io.designlibraryexample.domain.ForecastDTO;

public class ForecastDetailsActivity extends AppCompatActivity {
    public static final String EXTRA_FORECAST = "forecast";
    private static final String FORECAST_SHARE_HASHTAG = " #WeatherApp";

    private ForecastDTO mForecastData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast_detail);

        Intent intent = getIntent();
        mForecastData = Parcels.unwrap(intent.getExtras().getParcelable(EXTRA_FORECAST));

        setupToolbar();
        setupCollapsingToolbar(mForecastData.getDay());

        loadToolbarBg();
        setupFab();
        setupViewData();
    }

    private void setupViewData() {
        TextView location = (TextView) findViewById(R.id.weather_location);
        TextView high = (TextView) findViewById(R.id.weather_high);
        TextView low = (TextView) findViewById(R.id.weather_low);
        TextView description = (TextView) findViewById(R.id.weather_description);

        TextView humidity = (TextView) findViewById(R.id.weather_humidity);
        TextView pressure = (TextView) findViewById(R.id.weather_pressure);
        TextView wind = (TextView) findViewById(R.id.weather_wind);

        location.setText(mForecastData.getCityName() + ", "+mForecastData.getCountryName());
        high.setText(mForecastData.getHigh() + "°");
        low.setText(mForecastData.getLow() + "°");
        description.setText(mForecastData.getWeatherDescription());

        humidity.setText(mForecastData.getHumidity() + " %");
        pressure.setText(mForecastData.getPressure() + " %");
        wind.setText(mForecastData.getWind()+ " km/h W");
    }

    private void setupFab() {
        View fab = findViewById(R.id.fab_share);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareForecastIntent = createShareForecastIntent();
                if (shareForecastIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(shareForecastIntent);
                }
            }
        });
    }

    private void setupToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupCollapsingToolbar(String name) {
        final CollapsingToolbarLayout collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(name);
    }

    private void loadToolbarBg() {
        ImageView imageView = (ImageView) findViewById(R.id.toolbar_bg);
        Glide.with(this)
                .load(R.drawable.octocat2)
                .centerCrop()
                .into(imageView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mForecastData.getCityName() + ", " +
                mForecastData.getCountryName() + " " +
                mForecastData.toString() + FORECAST_SHARE_HASHTAG);
        return shareIntent;
    }
}
