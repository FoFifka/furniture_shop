package com.example.furniture_shop.Activities.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.furniture_shop.Activities.CatalogActivity;
import com.example.furniture_shop.Activities.MainActivity;
import com.example.furniture_shop.Classes.AppParams;
import com.example.furniture_shop.R;

import org.json.JSONException;
import org.json.JSONObject;

public class AddCategoryActivity extends AppCompatActivity {

    EditText categoryNameEditText;
    Button addCategoryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        categoryNameEditText = findViewById(R.id.categoryNameEditText);
        addCategoryButton = findViewById(R.id.addCategoryButton);

        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCategoryRequest(categoryNameEditText.getText().toString().trim());
            }
        });
    }

    public void addCategoryRequest(String category_name) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = AppParams.API_ADD_CATEGORY;

        JSONObject obj = new JSONObject();
        try {
            obj.put("api_token", AppParams.getUserToken());
            obj.put("name", category_name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getApplicationContext(), "Категория успешно создана", Toast.LENGTH_LONG).show();
                Log.d("RESPONSE", response.toString());

                CatalogActivity.requestHasBeenSent = false;
                startActivity(new Intent(getApplicationContext(), CatalogActivity.class));
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "При создании категории произошла ошибка", Toast.LENGTH_SHORT).show();
                Log.e("ERROR", error.toString());
            }
        });
        queue.add(jsonObjectRequest);
    }
}