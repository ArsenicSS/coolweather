package com.example.swong.myweather;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.swong.myweather.db.City;
import com.example.swong.myweather.db.County;
import com.example.swong.myweather.db.Province;
import com.example.swong.myweather.gson.Weather;
import com.example.swong.myweather.gson_db.GsonCity;
import com.example.swong.myweather.gson_db.GsonCounty;
import com.example.swong.myweather.gson_db.GsonProvince;
import com.example.swong.myweather.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class ChooseAreaFragment extends Fragment {

    private final static String TAG = "my";
    public static final String LEVEL_PROVINCE = "LEVEL_PROVINCE";
    public static final String LEVEL_CITY = "LEVEL_CITY";
    public static final String LEVEL_COUNTY = "LEVEL_COUNTY";
    public static final String LEVEL_WEATHER = "LEVEL_WEATHER";

    private String currentLevel;

    private int provinceId;
    private int cityId;
    private int countyId;
    private String weatherId;


    private List<String> datas = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);

        final TextView titleRank = view.findViewById(R.id.title_rank);
        final ListView listView = view.findViewById(R.id.list_view);

        currentLevel = LEVEL_PROVINCE;
        try {
            queryProvinceData();
        }catch (Exception e){e.printStackTrace();}

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity() ,android.R.layout.simple_list_item_1, datas);
        //listView = view.findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              if(currentLevel == LEVEL_PROVINCE) {
                  try {
                      currentLevel = LEVEL_CITY;
                      List<Province> mProvince = DataSupport.select("provinceId").where("name = ?", datas.get(i)).find(Province.class);
                      String string = datas.get(i);
                      for(Province procince : mProvince){
                          provinceId = procince.getId();
                      }

                      DataSupport.deleteAll(City.class);
                      queryCityData("/" + provinceId);

                      adapter.notifyDataSetChanged();
                      listView.setSelection(0);
                      titleRank.setText(string);

                  }catch (Exception e){e.printStackTrace();}

              }else if(currentLevel == LEVEL_CITY){
                  try {
                      currentLevel = LEVEL_COUNTY;
                      List<City> mCity = DataSupport.select("cityId").where("name = ?", datas.get(i)).find(City.class);
                      String string = datas.get(i);
                      for(City city : mCity){
                          cityId = city.getCityId();
                      }

                      DataSupport.deleteAll(County.class);
                      queryCountyData("/" + cityId);

                      adapter.notifyDataSetChanged();
                      listView.setSelection(0);
                      titleRank.setText(string);
                  }catch (Exception e){e.printStackTrace();}

              }else if(currentLevel == LEVEL_COUNTY){
                  try {
                      //currentLevel = LEVEL_WEATHER;
                      //List<County> mCounty = DataSupport.select("weatherId").where("name = ?", datas.get(i)).find(County.class);
                      //for(County county : mCounty){
                      //    weatherId = county.getWeatherId();
                      //}
                      //queryWeatherData("cityid=" + weatherId);
                      Intent intent = new Intent(getActivity(), WeatherActivity.class);
                      Log.d(TAG, "onItemClick: Intent intent = new Intent(getActivity(), WeatherActivity.class");

                      startActivity(intent);

                  }catch (Exception e){e.printStackTrace();}
              }

          }
        });

        return view;
    }


    public void queryProvinceData() throws InterruptedException {
        List<Province> provinceList = DataSupport.findAll(Province.class);

        //从数据库读取
        if(provinceList.size() > 0) {
            datas.clear();
            for(Province procince : provinceList){
                datas.add(procince.getName());
            }
        }
        //从网络读取
        else {
            sendRequest("http://guolin.tech/api/china");
            Thread.sleep(500);
            queryProvinceData();
        }
    }

    public void queryCityData(String id) throws InterruptedException {
        List<City> cityList = DataSupport.findAll(City.class);

        //从数据库读取
        if(cityList.size() > 0) {
            datas.clear();
            for(City city : cityList){
                datas.add(city.getName());
            }
        }
        //从网络读取
        else {
            sendRequest("http://guolin.tech/api/china" + id);
            Thread.sleep(500);
            queryCityData(" ");
        }
    }

    public void queryCountyData(String id) throws InterruptedException {
        List<County> countyList = DataSupport.findAll(County.class);

        //从数据库读取
        if(countyList.size() > 0) {
            datas.clear();
            for(County county : countyList){
                datas.add(county.getName());
            }
        }
        //从网络读取
        else {
            sendRequest("http://guolin.tech/api/china/" + cityId + id);
            Thread.sleep(500);
            queryCountyData(" ");
        }
    }

    //public void queryWeatherData(String id) throws InterruptedException{
    //    sendRequest("http://guolin.tech/api/weather?" + id + "&key=b1b5c44edc644354a7316756023a4e95");
    //    Thread.sleep(500);
    //}


    public void sendRequest(String url){
        HttpUtil.sendOkHttpRequest(url, new okhttp3.Callback(){
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                parseJSONWithGSON(responseData);
            }
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, currentLevel + "sendRequest onFailure");
            }
        });
    }


    private void parseJSONWithGSON(String jsonData) {
        Gson gson = new Gson();

        if(currentLevel == LEVEL_PROVINCE) {
            List<GsonProvince> gsonProvinceList = gson.fromJson(jsonData, new TypeToken<List<GsonProvince>>() {
            }.getType());
            for (GsonProvince gsonProvince : gsonProvinceList) {
                Province province = new Province();
                province.setProvinceId(gsonProvince.getId());
                province.setName(gsonProvince.getName());
                province.save();
            }
        }else if(currentLevel == LEVEL_CITY){
            List<GsonCity> gsonCityList = gson.fromJson(jsonData, new TypeToken<List<GsonCity>>() {
            }.getType());
            for (GsonCity gsonCity : gsonCityList) {
                City city = new City();
                city.setCityId(gsonCity.getId());
                city.setName(gsonCity.getName());
                city.save();
            }
        }else if(currentLevel == LEVEL_COUNTY){
            List<GsonCounty> gsonCountyList = gson.fromJson(jsonData, new TypeToken<List<GsonCounty>>() {
            }.getType());
            for (GsonCounty gsonCounty : gsonCountyList) {
                County county = new County();
                county.setCountyId(gsonCounty.getId());
                county.setWeatherId(gsonCounty.getWeatherId());
                county.setName(gsonCounty.getName());
                county.save();
            }
        }
        //else if(currentLevel == LEVEL_WEATHER){
        //    Log.d(TAG, "parseJSONWithGSON: Intent intent = new Intent(getActivity(), WeatherActivity.class);");
        //    Intent intent = new Intent(getActivity(), WeatherActivity.class);
        //    startActivity(intent);
        //}
    }

}
