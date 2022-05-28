package com.example.furniture_shop.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.furniture_shop.Classes.AppParams;
import com.example.furniture_shop.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductActivity extends AppCompatActivity {

    ProgressBar progressBar_product;
    ImageView product_image;
    TextView product_name, product_description, product_price;
    EditText product_count;
    Button btn_add_cart, btn_delete_product;
    ScrollView product;

    public static String product_id;

    public static boolean requestHasBeenSent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        progressBar_product = findViewById(R.id.progressBar_product);
        product_image = findViewById(R.id.imageView_product);
        product_name = findViewById(R.id.textView_product_name);
        product_description = findViewById(R.id.textView_product_description);
        product_price = findViewById(R.id.textView_product_price);
        btn_add_cart = findViewById(R.id.button_product_add_cart);
        product_count = findViewById(R.id.editText_product_count);
        product =findViewById(R.id.product);
        btn_delete_product = findViewById(R.id.button_product_delete);

        if(!requestHasBeenSent) {
            requestHasBeenSent = true;
            progressBar_product.setVisibility(View.VISIBLE);
            product.setVisibility(View.GONE);
            getProductRequest(product_id);
        }


        if (MainActivity.this_user.getPermission().equals("1")) {
            btn_delete_product.setVisibility(View.GONE);
        } else {
            btn_delete_product.setVisibility(View.VISIBLE);
        }

        btn_add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCartRequest();
                CartActivity.requestHasBeenSent = false;
            }
        });

        btn_delete_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar_product.setVisibility(View.VISIBLE);
                product.setVisibility(View.GONE);
                btn_delete_product.setVisibility(View.GONE);
                deleteProductRequest();
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
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), ProductsActivity.class));
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    void getProductRequest(String id) {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = AppParams.API_PRODUCT;

        JSONObject obj = new JSONObject();
        try {
            obj.put("product_id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar_product.setVisibility(View.GONE);
                product.setVisibility(View.VISIBLE);

                try {
                    getProductImage(response.getString("image"));
                    product_name.setText(response.getString("name"));
                    product_description.setText(response.getString("description"));
                    product_price.setText(response.getString("price")+" руб.");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);
    }
    void getProductImage(String url) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                product_image.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", error.toString());
            }
        });
        queue.add(imageRequest);
    }
    void addToCartRequest() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        String url = AppParams.API_ADD_CART_PRODUCT;

        JSONObject obj = new JSONObject();
        try {
            obj.put("user_id", MainActivity.this_user.getId());
            obj.put("product_id", product_id);
            obj.put("count", product_count.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String message = response.getString("message");
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(jsonObjectRequest);
    }
    void deleteProductRequest() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        String url = AppParams.API_DELETE_PRODUCT;

        JSONObject obj = new JSONObject();
        try {
            obj.put("api_token", AppParams.getUserToken());
            obj.put("product_id", product_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getApplicationContext(), "Продукт успешно удалён", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), CatalogActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "При удалении продукта произошла ошибка", Toast.LENGTH_SHORT).show();
                progressBar_product.setVisibility(View.GONE);
                btn_delete_product.setVisibility(View.VISIBLE);
                product.setVisibility(View.VISIBLE);

            }
        });
        queue.add(jsonObjectRequest);
    }
}
