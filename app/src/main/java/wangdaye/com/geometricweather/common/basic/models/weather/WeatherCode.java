package wangdaye.com.geometricweather.common.basic.models.weather;

public enum WeatherCode {
    CLEAR,
    PARTLY_CLOUDY,
    CLOUDY,
    RAIN,
    SNOW,
    WIND,
    FOG,
    HAZE,
    SLEET,
    HAIL,
    THUNDER,
    THUNDERSTORM,
    UNKNOWN;

    public boolean isPrecipitation() {
        return this == RAIN || this == SNOW || this == SLEET || this == HAIL || this == THUNDERSTORM;
    }

    public boolean isRain() {
        return this == RAIN || this == SLEET || this == THUNDERSTORM;
    }

    public boolean isSnow() {
        return this == SNOW || this == SLEET;
    }

    public boolean isIce() {
        return this == HAIL;
    }
}
