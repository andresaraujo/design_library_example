package andresaraujo.github.io.designlibraryexample.data.models;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(databaseName = WeatherDb.NAME)
public class Location extends BaseModel {
    @Column
    @PrimaryKey(autoincrement = true)
    long id;

    @Column String location_setting;
    @Column String city_name;
    @Column float coord_lat;
    @Column float coord_long;

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", location_setting='" + location_setting + '\'' +
                ", city_name='" + city_name + '\'' +
                ", coord_lat=" + coord_lat +
                ", coord_long=" + coord_long +
                '}';
    }
}
