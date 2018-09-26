package com.example.swong.myweather.util;

public interface HttpCallbackListener {
    void OnFinish(String response);
    void onError(Exception e);
}
