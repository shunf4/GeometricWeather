package wangdaye.com.geometricweather.weather.apis;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import wangdaye.com.geometricweather.weather.json.caiyun.CaiYunForecastResult;
import wangdaye.com.geometricweather.weather.json.caiyun.CaiYunMainlyResult;
import wangdaye.com.geometricweather.weather.json.tencent.TencentGeocoderResult;

public interface TencentGeocoderApi {

    @GET("ws/geocoder/v1/")
    @Headers("Referer: https://apis.map.qq.com/tools/geolocation?key=4VQBZ-ZGO3G-VGSQE-ILN4G-LWFUK-5WB7H&referer=qqmap")
    Observable<TencentGeocoderResult> getStreetName(@Query("location") String latLong,
                                                       @Query("output") String output,
                                                       @Query("key") String key);

}
