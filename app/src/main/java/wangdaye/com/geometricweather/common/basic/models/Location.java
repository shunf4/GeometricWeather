package wangdaye.com.geometricweather.common.basic.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import wangdaye.com.geometricweather.R;
import wangdaye.com.geometricweather.common.basic.models.options.provider.WeatherSource;
import wangdaye.com.geometricweather.common.basic.models.weather.Weather;
import wangdaye.com.geometricweather.common.utils.DisplayUtils;
import wangdaye.com.geometricweather.common.utils.LanguageUtils;

/**
 * Location.
 * */

public class Location
        implements Parcelable {

    private final String cityId;

    private final float latitude;
    private final float longitude;
    private final TimeZone timeZone;

    private final String country;
    private final String province;
    private final String city;
    private final String district;

    private final String street;

    @Nullable private Weather weather;
    private final WeatherSource weatherSource;

    private final boolean currentPosition;
    private final boolean residentPosition;
    private final boolean china;

    private static final String NULL_ID = "NULL_ID";
    public static final String CURRENT_POSITION_ID = "CURRENT_POSITION";

    public Location(Location src, WeatherSource weatherSource) {
        this(src.cityId, src.latitude, src.longitude, src.timeZone, src.country, src.province,
                src.city, src.district, src.weather, weatherSource, src.currentPosition,
                src.residentPosition, src.china, src.street);
    }

    public Location(Location src, boolean currentPosition, boolean residentPosition) {
        this(src.cityId, src.latitude, src.longitude, src.timeZone, src.country, src.province,
                src.city, src.district, src.weather, src.weatherSource,
                currentPosition, residentPosition, src.china, src.street);
    }

    public Location(Location src,
                    float latitude, float longitude, TimeZone timeZone,
                    String country, String province, String city, String district, boolean china, String street) {
        this(src.cityId, latitude, longitude, timeZone, country, province, city, district,
                src.weather, src.weatherSource, src.currentPosition, src.residentPosition, china, street);
    }

    public Location(String cityId, float latitude, float longitude, TimeZone timeZone,
                    String country, String province, String city, String district,
                    @Nullable Weather weather, WeatherSource weatherSource,
                    boolean currentPosition, boolean residentPosition, boolean china, String street) {
        this.cityId = cityId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timeZone = timeZone;
        this.country = country;
        this.province = province;
        this.city = city;
        this.district = district;
        this.weather = weather;
        this.weatherSource = weatherSource;
        this.currentPosition = currentPosition;
        this.residentPosition = residentPosition;
        this.china = china;
        this.street = street;
    }

    public static Location buildLocal() {
        return new Location(
                NULL_ID,
                0, 0, TimeZone.getDefault(),
                "", "", "", "",
                null, WeatherSource.ACCU,
                true, false, false, null
        );
    }

    public static Location buildDefaultLocation() {
        return new Location(
                "101924",
                39.904000f, 116.391000f, TimeZone.getTimeZone("Asia/Shanghai"),
                "中国", "直辖市", "北京", "",
                null, WeatherSource.ACCU,
                false, false, true, null
        );
    }

    public boolean equals(@Nullable Location location) {
        if (location == null) {
            return false;
        } else {
            return equals(location.getFormattedId());
        }
    }

    public boolean equals(@Nullable String formattedId) {
        if (TextUtils.isEmpty(formattedId)) {
            return false;
        }
        if (CURRENT_POSITION_ID.equals(formattedId)) {
            return isCurrentPosition();
        }
        try {
            assert formattedId != null;
            String[] keys = formattedId.split("&");
            return !isCurrentPosition()
                    && cityId.equals(keys[0])
                    && weatherSource.name().equals(keys[1]);
        } catch (Exception e) {
            return false;
        }
    }

    public String getFormattedId() {
        return isCurrentPosition() ? CURRENT_POSITION_ID : (cityId + "&" + weatherSource.name());
    }

    public boolean isCurrentPosition() {
        return currentPosition;
    }

    public boolean isResidentPosition() {
        return residentPosition;
    }

    public boolean isUsable() {
        return !cityId.equals(NULL_ID);
    }

    public boolean canUseChineseSource() {
        return LanguageUtils.isChinese(city) && china;
    }

    public String getCityId() {
        return cityId;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public String getCountry() {
        return country;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public String getCityName(Context context) {
        String fallbackName;

        if (!TextUtils.isEmpty(district) && !district.equals("市辖区") && !district.equals("无")) {
            fallbackName = district;
        } else if (!TextUtils.isEmpty(city) && !city.equals("市辖区")) {
            fallbackName = city;
        } else if (!TextUtils.isEmpty(province)) {
            fallbackName = province;
        } else if (currentPosition) {
            fallbackName = context.getString(R.string.current_location);
        } else {
            fallbackName = "";
        }

        if (street == null) {
            return fallbackName;
        }

        return street.replace("%district", fallbackName);
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(getCountry() + " " + getProvince());
        if (!getProvince().equals(getCity())
                && !TextUtils.isEmpty(getCity())) {
            builder.append(" ").append(getCity());
        }
        if (!getCity().equals(getDistrict())
                && !TextUtils.isEmpty(getDistrict())) {
            builder.append(" ").append(getDistrict());
        }
        String fallbackName = builder.toString();

        if (street == null) {
            return fallbackName;
        }

        return street.replace("%district", fallbackName);
    }

    public boolean hasGeocodeInformation() {
        return !TextUtils.isEmpty(country)
                || !TextUtils.isEmpty(province)
                || !TextUtils.isEmpty(city)
                || !TextUtils.isEmpty(district)
                || !TextUtils.isEmpty(street);
    }

    public boolean hasStreetInformation() {
        return !TextUtils.isEmpty(street);
    }

    @Nullable
    public Weather getWeather() {
        return weather;
    }

    public void setWeather(@Nullable Weather weather) {
        this.weather = weather;
    }

    public WeatherSource getWeatherSource() {
        return weatherSource;
    }

    public boolean isChina() {
        return china;
    }

    public String getStreetRaw() {
        return street;
    }

    private static boolean isEquals(@Nullable String a, @Nullable String b) {
        if (TextUtils.isEmpty(a) && TextUtils.isEmpty(b)) {
            return true;
        } else if (!TextUtils.isEmpty(a) && !TextUtils.isEmpty(b)) {
            return a.equals(b);
        } else {
            return false;
        }
    }

    public static List<Location> excludeInvalidResidentLocation(Context context, List<Location> list) {
        Location currentLocation = null;
        for (Location l : list) {
            if (l.isCurrentPosition()) {
                currentLocation = l;
                break;
            }
        }

        List<Location> result = new ArrayList<>(list.size());
        if (currentLocation == null) {
            result.addAll(list);
        } else {
            for (Location l : list) {
                if (!l.isResidentPosition() || !l.isCloseTo(context, currentLocation)) {
                    result.add(l);
                }
            }
        }
        return result;
    }

    private boolean isCloseTo(Context c, Location location) {
        if (cityId.equals(location.getCityId())) {
            return true;
        }
        if (isEquals(province, location.province)
                && isEquals(city, location.city)) {
            return true;
        }
        if (isEquals(province, location.province)
                && getCityName(c).equals(location.getCityName(c))) {
            return true;
        }
        return Math.abs(latitude - location.latitude) < 0.8
                && Math.abs(longitude - location.longitude) < 0.8;
    }

    public boolean isDaylight() {
        if (weather != null ) {
            return weather.isDaylight(getTimeZone());
        }

        return DisplayUtils.isDaylight(getTimeZone());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cityId);
        dest.writeFloat(this.latitude);
        dest.writeFloat(this.longitude);
        dest.writeSerializable(this.timeZone);
        dest.writeString(this.country);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.district);
        dest.writeString(this.street);
        dest.writeInt(this.weatherSource == null ? -1 : this.weatherSource.ordinal());
        dest.writeByte(this.currentPosition ? (byte) 1 : (byte) 0);
        dest.writeByte(this.residentPosition ? (byte) 1 : (byte) 0);
        dest.writeByte(this.china ? (byte) 1 : (byte) 0);
    }

    protected Location(Parcel in) {
        this.cityId = in.readString();
        this.latitude = in.readFloat();
        this.longitude = in.readFloat();
        this.timeZone = (TimeZone) in.readSerializable();
        this.country = in.readString();
        this.province = in.readString();
        this.city = in.readString();
        this.district = in.readString();
        this.street = in.readString();
        int tmpWeatherSource = in.readInt();
        this.weatherSource = tmpWeatherSource == -1 ? null : WeatherSource.values()[tmpWeatherSource];
        this.currentPosition = in.readByte() != 0;
        this.residentPosition = in.readByte() != 0;
        this.china = in.readByte() != 0;
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel source) {
            return new Location(source);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };


}
