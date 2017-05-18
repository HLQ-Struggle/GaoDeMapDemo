package cn.hlq.gaodemapdemo.map;

import android.annotation.TargetApi;
import android.app.Activity;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;

import java.util.Date;

import cn.hlq.gaodemapdemo.R;

/**
 * create by heliquan at 2017年5月4日23:26:59
 * 基于高德地图实现显示以及定位
 */
public class GaoDeLocationActivity extends Activity implements LocationSource, AMapLocationListener {

    private MapView mapView;

    private AMap aMap;

    public static AMapLocationClient mLocationClient = null;
    public static AMapLocationClientOption mLocationOption = null;

    private OnLocationChangedListener mListener;

    // 标识首次定位
    private boolean isFirstLocation = true;

    @Override
    protected void onResume() {
        super.onResume();
        // 重新绘制加载地图
        mapView.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gao_de_location);
        initView();
        // 创建地图
        mapView.onCreate(savedInstanceState);
        initGaoDeMap();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 暂停地图的绘制
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 销毁地图
        mapView.onDestroy();
    }

    /**
     * 重写此方法，在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private void initView() {
        // 实例化地图控件
        mapView = (MapView) findViewById(R.id.id_gaode_location_map);
        if (aMap == null) {
            // 显示地图
            aMap = mapView.getMap();
        }
        aMap.getUiSettings().setZoomControlsEnabled(false);
        // 设置地图默认的指南针是否显示
        aMap.getUiSettings().setCompassEnabled(true);
        // 设置定位监听
        aMap.setLocationSource(this);
        // 设置默认定位按钮是否显示
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
    }

    /**
     * 初始化高德地图
     */
    public void initGaoDeMap() {
        // 初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        // 设置高德地图定位回调监听
        mLocationClient.setLocationListener(this);
        // 初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        // 高精度定位模式：会同时使用网络定位和GPS定位，优先返回最高精度的定位结果，以及对应的地址描述信息
        // 设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 低功耗定位模式：不会使用GPS和其他传感器，只会使用网络定位（Wi-Fi和基站定位）；
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        // 仅用设备定位模式：不需要连接网络，只使用GPS进行定位，这种模式下不支持室内环境的定位，自 v2.9.0 版本支持返回地址描述信息。
        // 设置定位模式为AMapLocationMode.Device_Sensors，仅设备模式。
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
        // SDK默认采用连续定位模式，时间间隔2000ms
        // 设置定位间隔，单位毫秒，默认为2000ms，最低1000ms。
        mLocationOption.setInterval(3000);
        // 设置定位同时是否需要返回地址描述
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        // 设置是否强制刷新WIFI，默认为强制刷新。每次定位主动刷新WIFI模块会提升WIFI定位精度，但相应的会多付出一些电量消耗。
        // 设置是否强制刷新WIFI，默认为true，强制刷新。
        mLocationOption.setWifiActiveScan(true);
        // 设置是否允许模拟软件Mock位置结果，多为模拟GPS定位结果，默认为false，不允许模拟位置。
        // 设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        // 设置定位请求超时时间，默认为30秒
        // 单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(50000);
        // 设置是否开启定位缓存机制
        // 缓存机制默认开启，可以通过以下接口进行关闭。
        // 当开启定位缓存功能，在高精度模式和低功耗模式下进行的网络定位结果均会生成本地缓存，不区分单次定位还是连续定位。GPS定位结果不会被缓存。
        // 关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        // 设置是否只定位一次，默认为false
        mLocationOption.setOnceLocation(false);
        // 给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        // 启动高德地图定位
        mLocationClient.startLocation();
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        // 解析AMapLocation对象
        // 判断AMapLocation对象不为空，当定位错误码类型为0时定位成功
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                aMapLocation.getLocationType(); // 获取当前定位结果来源，如网络定位结果，详见定位类型表
                aMapLocation.getLatitude(); // 获取纬度
                aMapLocation.getLongitude(); // 获取经度
                aMapLocation.getAccuracy(); // 获取精度信息
                aMapLocation.getAddress(); // 地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                aMapLocation.getCountry(); // 国家信息
                aMapLocation.getProvince(); // 省信息
                aMapLocation.getCity(); // 城市信息
                aMapLocation.getDistrict(); // 城区信息
                aMapLocation.getStreet(); // 街道信息
                aMapLocation.getStreetNum(); // 街道门牌号信息
                aMapLocation.getCityCode(); // 城市编码
                aMapLocation.getAdCode(); // 地区编码
                aMapLocation.getAoiName(); // 获取当前定位点的AOI信息
                aMapLocation.getBuildingId(); // 获取当前室内定位的建筑物Id
                aMapLocation.getFloor(); // 获取当前室内定位的楼层
                aMapLocation.getGpsAccuracyStatus(); // 获取GPS的当前状态
                // 获取定位时间
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                df.format(date);
                // 如果不设置标志位，拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLocation) {
                    // 设置缩放级别
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                    // 将地图移动到定位点
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                    // 点击定位按钮 能够将地图的中心移动到定位点
                    mListener.onLocationChanged(aMapLocation);
                    isFirstLocation = false;
                }
            } else {
                // 定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("HLQ_Struggle", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        mListener = null;
    }
}
