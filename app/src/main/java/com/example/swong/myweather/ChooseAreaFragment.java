package com.example.swong.myweather;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.swong.myweather.db.Province;
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

    private List<String> provinces = new ArrayList<>();
    private final static String TAG = "my";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);

        try {
            queryProvinceData();
        }catch (Exception e){e.printStackTrace();}
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity() ,android.R.layout.simple_list_item_1, provinces);
        ListView listView = view.findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        //listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        //  @Override
        //  public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //      provinces.get(i);
        //      System.out.println(provinces.get(i));
        //  }
        //});

        return view;
    }


    public void queryProvinceData() throws InterruptedException {
        List<Province> provinceList = DataSupport.findAll(Province.class);

        //从数据库读取
        if(provinceList.size() > 0) {
            Log.d("my", "从数据库读取:"+provinceList.size());
            for(Province procince : provinceList){
                provinces.add(procince.getName());
            }
        }
        //从网络读取
        else {
            Log.d("my", "从网络读取");
            sendRequest("http://guolin.tech/api/china");
            Thread.sleep(2000);
            queryProvinceData();
        }
    }


    public void sendRequest(String url){
        HttpUtil.sendOkHttpRequest(url, new okhttp3.Callback(){
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "sendRequest onResponse: ");
                String responseData = response.body().string();
                parseJSONWithGSON(responseData);
            }
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "sendRequest onFailure");
            }
        });
    }


    private void parseJSONWithGSON(String jsonData) {
        Log.d(TAG, "parseJSONWithGSON");
        Gson gson = new Gson();

        List<GsonProvince> gsonProvinceList = gson.fromJson(jsonData, new TypeToken<List<GsonProvince>>() {}.getType());
        for(GsonProvince gsonProvince: gsonProvinceList){
            Province province = new Province();
            province.setName(gsonProvince.getName());
            province.save();
        }

    }

}
