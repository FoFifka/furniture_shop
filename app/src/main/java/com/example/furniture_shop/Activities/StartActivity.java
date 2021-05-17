package com.example.furniture_shop.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.furniture_shop.Classes.AppParams;
import com.example.furniture_shop.R;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        loadData();

        if(AppParams.getUserIsLogined()) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else {
            startActivity(new Intent(getApplicationContext(), AuthActivity.class));
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

    }
    void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(AppParams.SHARED_PREFS, MODE_PRIVATE);
        AppParams.setUserIsLogined(sharedPreferences.getBoolean(AppParams.SP_USER_IS_LOGINED, false));
        AppParams.setUserToken(sharedPreferences.getString(AppParams.SP_BEARER_TOKEN, ""));
    }
}