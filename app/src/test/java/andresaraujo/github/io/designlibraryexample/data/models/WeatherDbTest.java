package andresaraujo.github.io.designlibraryexample.data.models;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.util.List;
//import static org.assertj.android.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.*;

import andresaraujo.github.io.designlibraryexample.data.models.Location;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", sdk =  21)
public class WeatherDbTest {

    @Before
    public void setup() {
        FlowManager.init(ShadowApplication.getInstance().getApplicationContext());
        Location location =  new Location();
        location.city_name = "Test";
        location.coord_lat = 12.56f;
        location.coord_long = 45.56f;
        location.location_setting = "sd";

        location.save();

        Weather weather = new Weather();
        weather.date = System.currentTimeMillis();
        weather.degrees = 10;
        weather.humidity = 18.5f;
        weather.location = location;
        weather.max = 100;
        weather.min = 80;
        weather.pressure = 120f;
        weather.short_desc = "Rain";
        weather.weatherId = 12345;
        weather.wind = 18;

        weather.save();
    }

    @After
    public void destroy() {
        Delete.tables(Location.class);
        FlowManager.destroy();
    }

    @Test
    public void testData() {
        List<Location> locations = new Select().from(Location.class).queryList();
        List<Weather> weathers = new Select().from(Weather.class).queryList();

        assertThat(locations).hasSize(1);
        assertThat(weathers).hasSize(1);

        System.out.println(weathers.get(0));

    }
}
