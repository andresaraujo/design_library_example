package andresaraujo.github.io.designlibraryexample.data.models;


import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = WeatherDb.NAME, version = WeatherDb.VERSION)
public class WeatherDb {
    public static final String NAME = "weather_db";

    public static final int VERSION = 1;
}
