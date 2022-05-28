package com.example.furniture_shop.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.furniture_shop.Classes.AppParams;
import com.example.furniture_shop.Classes.CatalogCategories;
import com.example.furniture_shop.Classes.News;
import com.example.furniture_shop.Classes.NewsAdapter;
import com.example.furniture_shop.Classes.User;
import com.example.furniture_shop.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static User this_user = new User();
    public static boolean requestGetUserDataHasBeenSent = false;
    public static boolean requestNewsHasBeenSent = false;

    ProgressBar newsProgressBar;
    ListView listViewNews;


    public static ArrayList<News> news = new ArrayList<News>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsProgressBar = findViewById(R.id.newsProgressBar);
        listViewNews = findViewById(R.id.listViewNews);


        if(!requestGetUserDataHasBeenSent) {
            getUserDataRequest();
        }
        if(!requestNewsHasBeenSent) {
            newsProgressBar.setVisibility(View.VISIBLE);
            getNewsRequest();
        }

        NewsAdapter newsAdapter = new NewsAdapter(getApplicationContext(), R.layout.listview_news, news);
        listViewNews.setAdapter(newsAdapter);



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        return true;
                    case R.id.catalog:
                        startActivity(new Intent(getApplicationContext(), CatalogActivity.class));
                        finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return true;
                    case R.id.cart:
                        startActivity(new Intent(getApplicationContext(), CartActivity.class));
                        finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return true;
                }
                return false;
            }
        });
    }

    private void getNewsRequest() {
        news.clear();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = AppParams.API_GET_NEWS;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject news1 = response.getJSONObject(i);

                        String news_id = news1.getString("id");
                        String news_title = news1.getString("title");
                        String news_text = news1.getString("text");


                        news.add(new News(news_id, news_title, news_text));
                    } catch (Exception e) {
                        Log.e("ERROR!", e.toString());
                    }
                }
                requestNewsHasBeenSent = true;
                newsProgressBar.setVisibility(View.GONE);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(jsonArrayRequest);

    }

    void getUserDataRequest() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        String url = AppParams.API_USER;

        JSONObject obj = new JSONObject();
        try {
            obj.put("token", AppParams.getUserToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.has("id")) {
                    requestGetUserDataHasBeenSent = true;
                    try {
                        MainActivity.this_user.setId(response.getString("id"));
                        MainActivity.this_user.setEmail(response.getString("email"));
                        MainActivity.this_user.setName(response.getString("name"));
                        MainActivity.this_user.setSurname(response.getString("surname"));
                        MainActivity.this_user.setPermission(response.getString("permission_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    logout();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                logout();
            }
        });
        queue.add(jsonObjectRequest);
    }
    void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences(AppParams.SHARED_PREFS, MODE_PRIVATE);
        sharedPreferences.edit()
                .putBoolean(AppParams.SP_USER_IS_LOGINED, false)
                .putString(AppParams.SP_BEARER_TOKEN, "")
                .apply();
        startActivity(new Intent(getApplicationContext(), AuthActivity.class));
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}