package wangdaye.com.geometricweather.weather.json.tencent;

import com.google.gson.annotations.SerializedName;

public class TencentGeocoderResult {
    public static class Result {
        public static class AddressComponent {
            public String city;
            public String district;
            public String nation;
            public String province;
            public String street;
            @SerializedName("street_number")
            public String streetNumber;
        }

        @SerializedName("address_component")
        public AddressComponent addressComponent;
    }
    public Result result;

    public int status;
}
