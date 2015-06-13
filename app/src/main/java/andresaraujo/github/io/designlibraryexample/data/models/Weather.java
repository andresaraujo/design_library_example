package andresaraujo.github.io.designlibraryexample.data.models;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(databaseName = WeatherDb.NAME)
public class Weather extends BaseModel {

    @Column @PrimaryKey(autoincrement = true)
    long id;

    @Column long date;
    @Column(name = "weather_id") long weatherId;
    @Column String short_desc;
    @Column float min;
    @Column float max;
    @Column float humidity;
    @Column float pressure;
    @Column float wind;
    @Column float degrees;

    @Column
    @ForeignKey(
            references = {@ForeignKeyReference(columnName = "location_id",
                    columnType = Long.class,
                    foreignColumnName = "id")},
            saveForeignKeyModel = false)
    Location location;

    @Override
    public String toString() {
        return "Weather{" +
                "id=" + id +
                ", date=" + date +
                ", weatherId=" + weatherId +
                ", short_desc='" + short_desc + '\'' +
                ", min=" + min +
                ", max=" + max +
                ", humidity=" + humidity +
                ", pressure=" + pressure +
                ", wind=" + wind +
                ", degrees=" + degrees +
                ", location=" + location +
                '}';
    }
}
