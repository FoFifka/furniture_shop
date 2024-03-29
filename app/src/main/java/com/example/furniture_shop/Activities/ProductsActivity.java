package com.example.furniture_shop.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.furniture_shop.Classes.AppParams;
import com.example.furniture_shop.Classes.CatalogCategories;
import com.example.furniture_shop.Classes.ProductAdapter;
import com.example.furniture_shop.Classes.Products;
import com.example.furniture_shop.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductsActivity extends AppCompatActivity {

    ListView listViewProducts;
    ProgressBar progressBarProducts;
    Button buttonDeleteCategory;
    public static ArrayList<Products> products = new ArrayList<Products>();

    public static boolean requestHasBeenSent = false;
    public static CatalogCategories category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        listViewProducts = findViewById(R.id.listViewProducts);
        progressBarProducts = findViewById(R.id.progressBarProducts);
        buttonDeleteCategory = findViewById(R.id.button_delete_category);



        if (!requestHasBeenSent) {
            requestHasBeenSent = true;
            getProductsRequest(category.getCategory_id());
        }

        if (MainActivity.this_user.getPermission().equals("1")) {
            buttonDeleteCategory.setVisibility(View.GONE);
        } else {
            buttonDeleteCategory.setVisibility(View.VISIBLE);
        }


        ProductAdapter adapter = new ProductAdapter(getApplicationContext(), R.layout.listview_products, products);
        listViewProducts.setAdapter(adapter);


        listViewProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Products product = (Products)parent.getItemAtPosition((int)id);
                ProductActivity.requestHasBeenSent = false;
                ProductActivity.product_id = product.getId();

                startActivity(new Intent(getApplicationContext(), ProductActivity.class));
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        buttonDeleteCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listViewProducts.setVisibility(View.GONE);
                buttonDeleteCategory.setVisibility(View.GONE);
                progressBarProducts.setVisibility(View.VISIBLE);
                deleteCategoryRequest();
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
                        startActivity(new Intent(getApplicationContext(), CatalogActivity.class));
                        finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
    void getProductsRequest(String id) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = AppParams.API_PRODUCTS;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listViewProducts.setVisibility(View.VISIBLE);
                try {
                    JSONArray array = new JSONArray(response);

                    products.clear();

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject product = array.getJSONObject(i);

                        String product_id = product.getString("id");
                        String product_name = product.getString("name");
                        String product_description = product.getString("description");
                        String product_image = product.getString("image");
                        int product_price =  product.getInt("price");

                        products.add(new Products(product_id, product_name,product_description,product_image,product_price));
                    }
                    startActivity(new Intent(getApplicationContext(), ProductsActivity.class));
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("category_id", id);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void deleteCategoryRequest() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        String url = AppParams.API_DELETE_CATEGORY;

        JSONObject obj = new JSONObject();
        try {
            obj.put("api_token", AppParams.getUserToken());
            obj.put("category_id", category.getCategory_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getApplicationContext(), "Категория успешно удалёна", Toast.LENGTH_SHORT).show();
                CatalogActivity.requestHasBeenSent = false;
                startActivity(new Intent(getApplicationContext(), CatalogActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "При удалении категории произошла ошибка", Toast.LENGTH_SHORT).show();
                progressBarProducts.setVisibility(View.GONE);
                listViewProducts.setVisibility(View.VISIBLE);
            }
        });
        queue.add(jsonObjectRequest);
    }
}