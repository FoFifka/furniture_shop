package com.example.furniture_shop.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.furniture_shop.Classes.AppParams;
import com.example.furniture_shop.Classes.CatalogCategories;
import com.example.furniture_shop.Classes.CatalogCategoryAdapter;
import com.example.furniture_shop.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CatalogActivity extends AppCompatActivity {

    ListView listViewCategories;
    ProgressBar progressBar;
    static boolean requestHasBeenSent = false;

    static public ArrayList<CatalogCategories> categories = new ArrayList<CatalogCategories>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        listViewCategories = findViewById(R.id.listViewCategories);
        progressBar = findViewById(R.id.progressBar_catalog_activity);

        if (!requestHasBeenSent) {
            progressBar.setVisibility(View.VISIBLE);
            getCategoriesRequest();
        }


        CatalogCategoryAdapter adapter = new CatalogCategoryAdapter(getApplicationContext(), R.layout.listview_catalog_categories, categories);
        listViewCategories.setAdapter(adapter);

        listViewCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductsActivity.requestHasBeenSent = false;
                CatalogCategories catalog = (CatalogCategories)parent.getItemAtPosition((int)id);
                ProductsActivity.category = catalog;
                startActivity(new Intent(getApplicationContext(), ProductsActivity.class));
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.catalog);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return true;
                    case R.id.catalog:
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onBackPressed();
    }

    void getCategoriesRequest() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = AppParams.API_CATEGORIES;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject category = response.getJSONObject(i);

                        String category_id = category.getString("id");
                        String category_name = category.getString("name");
                        String category_image = category.getString("image");


                        categories.add(new CatalogCategories(category_id, category_name, category_image));
                    } catch (Exception e) {
                        Log.e("ERROR!", e.toString());
                    }
                }
                requestHasBeenSent = true;
                progressBar.setVisibility(View.INVISIBLE);
                startActivity(new Intent(getApplicationContext(), CatalogActivity.class));
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
}