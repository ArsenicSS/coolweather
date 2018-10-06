package com.example.swong.myweather;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.swong.myweather.util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    
    ScrollView weatherLayout;
    DrawerLayout drawerLayout;

    static String mTxtUpdatetime;

    @Bind(R.id.btn_home)
    Button btnHome;
    @Bind(R.id.txt_cityname)
    TextView txtCityname;
    @Bind(R.id.txt_updatetime)
    TextView txtUpdatetime;
    @Bind(R.id.txt_degree)
    TextView txtDegree;
    @Bind(R.id.txt_weatherInfo)
    TextView txtWeatherInfo;
    @Bind(R.id.txt_date_1)
    TextView txtDate1;
    @Bind(R.id.txt_weather_1)
    TextView txtWeather1;
    @Bind(R.id.txt_max_1)
    TextView txtMax1;
    @Bind(R.id.txt_min_1)
    TextView txtMin1;
    @Bind(R.id.txt_date_2)
    TextView txtDate2;
    @Bind(R.id.txt_weather_2)
    TextView txtWeather2;
    @Bind(R.id.txt_max_2)
    TextView txtMax2;
    @Bind(R.id.txt_min_2)
    TextView txtMin2;
    @Bind(R.id.txt_date_3)
    TextView txtDate3;
    @Bind(R.id.txt_weather_3)
    TextView txtWeather3;
    @Bind(R.id.txt_max_3)
    TextView txtMax3;
    @Bind(R.id.txt_min_3)
    TextView txtMin3;
    @Bind(R.id.txt_api)
    TextView txtApi;
    @Bind(R.id.txt_pm25)
    TextView txtPm25;
    @Bind(R.id.txt_comfort)
    TextView txtComfort;
    @Bind(R.id.txt_car_washing)
    TextView txtCarWashing;
    @Bind(R.id.txt_sport)
    TextView txtSport;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        weatherLayout = findViewById(R.id.weather_layout);
        weatherLayout.setVisibility(View.INVISIBLE);
        drawerLayout = findViewById(R.id.drawer_layout);

        Intent intent = getIntent();
        String weatherId = intent.getStringExtra("weatherId");
        String link = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=b1b5c44edc644354a7316756023a4e95";
        sendRequest(link);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });
    }

    public void sendRequest(String url) {
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                parseJSONWithGSON(responseData);
            }

            @Override
            public void onFailure(Call call, IOException e) {
            }
        });
    }

    public void parseJSONWithGSON(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray HeWeather = jsonObject.getJSONArray("HeWeather");
            JSONObject Object = HeWeather.getJSONObject(0);

            JSONObject basic = Object.getJSONObject("basic");
            final String location = basic.getString("location");//地区
            final int tz = basic.getInt("tz");//温度

            JSONObject update = Object.getJSONObject("update");
            String loc = update.getString("loc");
            loc = loc.split(" ")[1];
            mTxtUpdatetime = loc;//更新时间

            JSONObject now = Object.getJSONObject("now");
            final String cond_txt = now.getString("cond_txt");//天气

            JSONArray daily_forecast = Object.getJSONArray("daily_forecast");
            JSONObject forecast_1 = daily_forecast.getJSONObject(0);
            final String date_1 = forecast_1.getString("date");//今天日期
            JSONObject cond_1 = forecast_1.getJSONObject("cond");
            final String mCond_1 = cond_1.getString("txt_d");//今天天气
            JSONObject tmp_1 = forecast_1.getJSONObject("tmp");
            final String max_1 = tmp_1.getString("max");//今天最高温
            final String min_1 = tmp_1.getString("min");//今天最低温

            JSONObject forecast_2 = daily_forecast.getJSONObject(1);
            final String date_2 = forecast_2.getString("date");//明天日期
            JSONObject cond_2 = forecast_2.getJSONObject("cond");
            final String mCond_2 = cond_2.getString("txt_d");//明天天气
            JSONObject tmp_2 = forecast_1.getJSONObject("tmp");
            final String max_2 = tmp_2.getString("max");//明天最高温
            final String min_2 = tmp_2.getString("min");//明天最低温

            JSONObject forecast_3 = daily_forecast.getJSONObject(2);
            final String date_3 = forecast_3.getString("date");//后天日期
            JSONObject cond_3 = forecast_2.getJSONObject("cond");
            final String mCond_3 = cond_3.getString("txt_d");//后天天气
            JSONObject tmp_3 = forecast_1.getJSONObject("tmp");
            final String max_3 = tmp_3.getString("max");//后天最高温
            final String min_3 = tmp_3.getString("min");//后天最低温

            JSONObject aqi = Object.getJSONObject("aqi");
            JSONObject city = aqi.getJSONObject("city");
            final String mAqi = city.getString("aqi");//aqi
            final String pm25 = city.getString("pm25");//pm2.5

            JSONObject suggestion = Object.getJSONObject("suggestion");
            JSONObject comf = suggestion.getJSONObject("comf");
            final String comf_txt = comf.getString("txt");//舒适度
            JSONObject cw = suggestion.getJSONObject("cw");
            final String cw_txt = cw.getString("txt");//运动指数
            JSONObject sport = suggestion.getJSONObject("sport");
            final String sport_txt = sport.getString("txt");//洗车指数


            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    txtCityname.setText(location);//地区
                    txtUpdatetime.setText(mTxtUpdatetime);//更新时间
                    txtDegree.setText(tz + "℃");//温度
                    txtWeatherInfo.setText(cond_txt);//天气
                    txtDate1.setText(date_1);
                    txtWeather1.setText(mCond_1);
                    txtMax1.setText(max_1);
                    txtMin1.setText(min_1);
                    txtDate2.setText(date_2);
                    txtWeather2.setText(mCond_2);
                    txtMax2.setText(max_2);
                    txtMin2.setText(min_2);
                    txtDate3.setText(date_3);
                    txtWeather3.setText(mCond_3);
                    txtMax3.setText(max_3);
                    txtMin3.setText(min_3);
                    txtApi.setText(mAqi);//aqi指数
                    txtPm25.setText(pm25);//pm2.5
                    txtComfort.setText("舒适度: " + comf_txt);//舒适度
                    txtCarWashing.setText("\n洗车指数: " + cw_txt);//洗车指数
                    txtSport.setText("\n运动指数: " + sport_txt);//运动指数
                    weatherLayout.setVisibility(View.VISIBLE);

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
