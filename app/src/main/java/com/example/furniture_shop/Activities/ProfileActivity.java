package com.example.furniture_shop.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.furniture_shop.Activities.admin.AddCategoryActivity;
import com.example.furniture_shop.Activities.admin.AddProductActivity;
import com.example.furniture_shop.Classes.AppParams;
import com.example.furniture_shop.Classes.CartProducts;
import com.example.furniture_shop.Classes.News;
import com.example.furniture_shop.Classes.Order;
import com.example.furniture_shop.Classes.OrderAdapter;
import com.example.furniture_shop.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    TextView user_name, user_surname, user_email, textView_profile_no_orders;
    ProgressBar progressBarProfileOrders;
    ListView listViewOrders;
    public static ArrayList<Order> orders = new ArrayList<Order>();

    public static boolean requestOrdersHasBeenSent = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user_email = findViewById(R.id.textView_email_profile_activity);
        user_name = findViewById(R.id.textView_name_profile_activity);
        user_surname = findViewById(R.id.textView_surname_profile_activity);
        textView_profile_no_orders = findViewById(R.id. textView_profile_no_orders);
        listViewOrders = findViewById(R.id.listView_profile_orders);

        progressBarProfileOrders = findViewById(R.id.progressBarProfileOrders);

        try {
            user_email.setText(MainActivity.this_user.getEmail());
            user_name.setText(MainActivity.this_user.getName());
            user_surname.setText(MainActivity.this_user.getSurname());
        } catch (Exception e) {
            logout();
            Toast.makeText(getApplicationContext(), "При загрузке профиля произошла ошибка", Toast.LENGTH_LONG).show();
        }

        if(!requestOrdersHasBeenSent) {
            textView_profile_no_orders.setVisibility(View.INVISIBLE);
            progressBarProfileOrders.setVisibility(View.VISIBLE);
            getOrdersRequest();
        } else {
            progressBarProfileOrders.setVisibility(View.GONE);
        }


        OrderAdapter orderAdapter = new OrderAdapter(getApplicationContext(), R.layout.listview_order, orders);
        listViewOrders.setAdapter(orderAdapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.profile);
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
                        return true;
                    case R.id.cart:
                        startActivity(new Intent(getApplicationContext(), CartActivity.class));
                        finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return true;
                    case R.id.profile:
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.topbar_profile, menu);
        MenuItem action_add_category = menu.findItem(R.id.action_add_category);
        MenuItem action_add_product = menu.findItem(R.id.action_add_product);
        if(MainActivity.this_user.getPermission().equals("1")) {
            action_add_category.setVisible(false);
            action_add_product.setVisible(false);
        } else {
            action_add_category.setVisible(true);
            action_add_product.setVisible(true);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                break;
            case R.id.action_add_category:
                startActivity(new Intent(getApplicationContext(), AddCategoryActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.action_add_product:
                startActivity(new Intent(getApplicationContext(), AddProductActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onBackPressed();
    }
    public void getOrdersRequest() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        String url = AppParams.API_GET_ORDERS;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                orders.clear();
                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject order = array.getJSONObject(i);

                        String order_id = order.getString("id");
                        String order_price = order.getString("total_price");
                        String order_data = order.getString("created_at");
                        String order_status = order.getString("status_id");

                        orders.add(new Order(order_id, order_price, order_data, order_status));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                requestOrdersHasBeenSent = true;
                progressBarProfileOrders.setVisibility(View.GONE);
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBarProfileOrders.setVisibility(View.GONE);
                textView_profile_no_orders.setText(error.toString());
                textView_profile_no_orders.setVisibility(View.VISIBLE);
            }
        }) {
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("api_token", AppParams.getUserToken());
                return params;
            }
        };
        queue.add(stringRequest);
    }
    public void logout() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Выйти из аккаунта");
        alertDialogBuilder.setMessage("Вы действительно хотите выйти!");
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialogBuilder.setPositiveButton(R.string.action_signout, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences sharedPreferences = getSharedPreferences(AppParams.SHARED_PREFS, MODE_PRIVATE);
                sharedPreferences.edit()
                        .putBoolean(AppParams.SP_USER_IS_LOGINED, false)
                        .putString(AppParams.SP_BEARER_TOKEN, "")
                        .apply();
                MainActivity.requestNewsHasBeenSent = false;
                MainActivity.requestGetUserDataHasBeenSent = false;
                startActivity(new Intent(getApplicationContext(), AuthActivity.class));
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialogBuilder.show();
    }
}