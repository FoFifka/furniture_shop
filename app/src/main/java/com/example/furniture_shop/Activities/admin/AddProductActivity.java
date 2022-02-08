package com.example.furniture_shop.Activities.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.furniture_shop.Activities.CatalogActivity;
import com.example.furniture_shop.Classes.AppParams;
import com.example.furniture_shop.Classes.CatalogCategories;
import com.example.furniture_shop.Classes.CatalogSpinnerAdapter;
import com.example.furniture_shop.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddProductActivity extends AppCompatActivity {

    EditText productNameEditText, productDescriptionEditText, productPriceEditText;
    Button addProductButton;
    Spinner categoriesSpinner;
    ArrayList<CatalogCategories> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        productNameEditText = findViewById(R.id.productNameEditText);
        productDescriptionEditText = findViewById(R.id.productDescriptionEditText);
        productPriceEditText = findViewById(R.id.productPriceEditText);
        addProductButton = findViewById(R.id.addProductButton);
        categoriesSpinner = findViewById(R.id.categoriesSpinner);

        categories = new ArrayList<>();

        if(CatalogActivity.categories.size() < 1) {
            startActivity(new Intent(getApplicationContext(), CatalogActivity.class));
            finish();
            overridePendingTransition(0, 0);
        } else {
            categories = CatalogActivity.categories;
        }

        CatalogSpinnerAdapter catalogSpinnerAdapter = new CatalogSpinnerAdapter(this, categories);
        categoriesSpinner.setAdapter(catalogSpinnerAdapter);

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProductRequest();
            }
        });
    }

    void addProductRequest() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = AppParams.API_ADD_PRODUCT;

        JSONObject obj = new JSONObject();
        try {
            obj.put("api_token", AppParams.getUserToken());
            obj.put("name", productNameEditText.getText().toString().trim());
            obj.put("description", productDescriptionEditText.getText().toString().trim());
            obj.put("price", productPriceEditText.getText().toString().trim());
            obj.put("category_id", categories.get((int)categoriesSpinner.getSelectedItemId()).getCategory_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Toast.makeText(getApplicationContext(), "Категория успешно создана", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
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