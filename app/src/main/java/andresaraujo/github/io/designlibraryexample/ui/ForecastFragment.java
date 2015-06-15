package andresaraujo.github.io.designlibraryexample.ui;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONException;
import org.parceler.Parcel;
import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import andresaraujo.github.io.designlibraryexample.R;
import andresaraujo.github.io.designlibraryexample.domain.ForecastDTO;

public class ForecastFragment extends Fragment implements MainActivity.OnPrefLocationListener {

    private final String LOG_TAG = ForecastFragment.class.getSimpleName();

    private ForecastAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_dummyfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_refresh:
                updateWeather();
                return true;
            case R.id.action_map:
                openPreferredLocationInMap();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openPreferredLocationInMap() {
        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location = sharedPrefs.getString(
                getString(R.string.pref_location_key),
                getString(R.string.pref_location_default));

        // Using the URI scheme for showing a location found on a map.  This super-handy
        // intent can is detailed in the "Common Intents" page of Android's developer site:
        // http://developer.android.com/guide/components/intents-common.html#Maps
        Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
                .appendQueryParameter("q", location)
                .build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d(LOG_TAG, "Couldn't call " + location + ", no receiving apps installed!");
        }
    }

    private void updateWeather() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location = preferences.getString(getString(R.string.pref_location_key),
                getString(R.string.pref_location_default));
        String format = "json";
        String units = "metric";
        final int numDays = 7;

        final String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily";
        final String QUERY_PARAM = "q";
        final String FORMAT_PARAM = "mode";
        final String UNITS_PARAM = "units";
        final String DAYS_PARAM = "cnt";

        final Context context = getActivity();

        final Uri uri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, location)
                .appendQueryParameter(FORMAT_PARAM, format)
                .appendQueryParameter(UNITS_PARAM, units)
                .appendQueryParameter(DAYS_PARAM, String.valueOf(numDays))
                .build();

        Ion.with(context)
                .load(uri.toString())
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>> () {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        if(e != null) {
                            Toast.makeText(context, "Error loading weather", Toast.LENGTH_SHORT).show();
                            Log.e("Forecast", "Error loading weather: " + uri.toString(), e);
                            return;
                        }
                        Log.d("Forecast", "Request forecast URL: " + result.getRequest().getUri().toString());

                        ForecastParserTask forecastParserTask = new ForecastParserTask(numDays);
                        forecastParserTask.execute(result.getResult());
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.fragment_forecast_list, container, false);

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        mAdapter = new ForecastAdapter(getActivity(), new ArrayList<ForecastDTO>());
        recyclerView.setAdapter(mAdapter);

        return recyclerView;
    }

    @Override
    public void onPrefLocationChanged() {
        updateWeather();
    }

    public  static class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {

        //private final TypedValue mTypedValue = new TypedValue();
        //private int mBackground;
        private List<ForecastDTO> mValues;

        public ForecastAdapter(Context context, List<ForecastDTO> items) {
            mValues =  items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_chat_item, parent, false);
            //view.setBackgroundResource(mBackground);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mForecastData = mValues.get(position);
            holder.mTextView.setText(mValues.get(position).toString());

            final Parcelable wrapped = Parcels.wrap(holder.mForecastData);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ForecastDetailsActivity.class);
                    intent.putExtra(ForecastDetailsActivity.EXTRA_FORECAST, wrapped);
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public ForecastDTO mForecastData;

            public final View mView;
            public final ImageView mImageView;
            public final TextView mTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                mView = itemView;

                mImageView = (ImageView) itemView.findViewById(R.id.profile_pic);
                mTextView = (TextView) itemView.findViewById(android.R.id.text1);
            }
        }
    }

    public class ForecastParserTask extends AsyncTask<JsonObject, Void, ForecastDTO[]> {

        private final String LOG_TAG = ForecastParserTask.class.getSimpleName();

        private final int mNumDays;

        public ForecastParserTask(int numDays) {
            this.mNumDays = numDays;
        }

        /* The date/time conversion code is going to be moved outside the asynctask later,
         * so for convenience we're breaking it out into its own method now.
         */
        private String getReadableDateString(long time){
            // Because the API returns a unix timestamp (measured in seconds),
            // it must be converted to milliseconds in order to be converted to valid date.
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
            return shortenedDateFormat.format(time);
        }

        private ForecastDTO[] getWeatherDataFromJson(JsonObject forecastJson, int numDays)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_LIST = "list";
            final String OWM_CITY = "city";
            final String OWM_CITY_NAME = "name";
            final String OWM_CITY_COUNTRY = "country";
            final String OWM_WEATHER = "weather";
            final String OWM_TEMPERATURE = "temp";
            final String OWM_MAX = "max";
            final String OWM_MIN = "min";
            final String OWM_DESCRIPTION = "main";
            final String OWM_HUMIDITY = "humidity";
            final String OWM_PRESSURE = "pressure";
            final String OWM_WIND = "speed";

            //JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JsonArray weatherArray = forecastJson.getAsJsonArray(OWM_LIST);
            String cityName = forecastJson.getAsJsonObject(OWM_CITY).get(OWM_CITY_NAME).getAsString();
            String countryName = forecastJson.getAsJsonObject(OWM_CITY).get(OWM_CITY_COUNTRY).getAsString();

            // OWM returns daily forecasts based upon the local time of the city that is being
            // asked for, which means that we need to know the GMT offset to translate this data
            // properly.

            // Since this data is also sent in-order and the first day is always the
            // current day, we're going to take advantage of that to get a nice
            // normalized UTC date for all of our weather.

            Time dayTime = new Time();
            dayTime.setToNow();

            // we start at the day returned by local time. Otherwise this is a mess.
            int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

            // now we work exclusively in UTC
            dayTime = new Time();

            ForecastDTO[] resultDto = new ForecastDTO[numDays];

            for(int i = 0; i < weatherArray.size(); i++) {
                // For now, using the format "Day, description, hi/low"
                String day;
                String description;

                // Get the JSON object representing the day
                JsonObject dayForecast = weatherArray.get(i).getAsJsonObject();

                // The date/time is returned as a long.  We need to convert that
                // into something human-readable, since most people won't read "1400356800" as
                // "this saturday".
                long dateTime;
                // Cheating to convert this to UTC time, which is what we want anyhow
                dateTime = dayTime.setJulianDay(julianStartDay+i);
                day = getReadableDateString(dateTime);

                // description is in a child array called "weather", which is 1 element long.
                JsonObject weatherObject = dayForecast.getAsJsonArray(OWM_WEATHER).get(0).getAsJsonObject();
                description = weatherObject.get(OWM_DESCRIPTION).getAsString();

                // Temperatures are in a child object called "temp".  Try not to name variables
                // "temp" when working with temperature.  It confuses everybody.
                JsonObject temperatureObject = dayForecast.getAsJsonObject(OWM_TEMPERATURE);
                double high = temperatureObject.get(OWM_MAX).getAsDouble();
                double low = temperatureObject.get(OWM_MIN).getAsDouble();

                ForecastDTO forecastDTO = new ForecastDTO(cityName, countryName,
                        description, day);

                forecastDTO.setHigh((int) Math.round(high));
                forecastDTO.setLow((int) Math.round(low));
                forecastDTO.setHumidity(dayForecast.get(OWM_HUMIDITY).getAsInt());
                forecastDTO.setPressure(dayForecast.get(OWM_PRESSURE).getAsDouble());
                forecastDTO.setWind(dayForecast.get(OWM_WIND).getAsDouble());

                resultDto[i] = forecastDTO;
            }

            for (ForecastDTO o : resultDto) {
                Log.v(LOG_TAG, "Forecast entry: " + o);
            }
            return resultDto;

        }

        @Override
        protected ForecastDTO[] doInBackground(JsonObject... params) {
            if(params.length == 0) return null;

            JsonObject forecastJson = params[0];

            try {
                return getWeatherDataFromJson(forecastJson, mNumDays);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ForecastDTO[] result) {
            if(result == null) return;

            mAdapter.mValues.clear();
            Collections.addAll(mAdapter.mValues, result);
            mAdapter.notifyDataSetChanged();
        }
    }
}
