package andresaraujo.github.io.designlibraryexample.domain;

import org.parceler.Parcel;

@Parcel
public class ForecastDTO {
    private String cityName;
    private String countryName;

    private String weatherDescription;
    private int high;
    private int low;
    private String day;

    private int humidity;
    private double pressure;
    private double wind;

    public ForecastDTO() {}

    public ForecastDTO(String cityName, String countryName,
                       String weatherDescription, String day) {
        this.cityName = cityName;
        this.countryName = countryName;
        this.weatherDescription = weatherDescription;
        this.day = day;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public String getHighAndLow() {
        return high + "/" +low;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getWind() {
        return wind;
    }

    public void setWind(double wind) {
        this.wind = wind;
    }

    @Override
    public String toString() {
        return day + " - " + weatherDescription + " - " + getHighAndLow();
    }
}