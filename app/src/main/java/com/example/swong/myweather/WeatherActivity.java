package com.example.swong.myweather;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class WeatherActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btnBack = findViewById(R.id.btn_back);
        drawerLayout = findViewById(R.id.drawer_layout);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });
    }
}
