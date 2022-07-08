package com.example.furniture_shop.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.furniture_shop.Classes.AppParams;
import com.example.furniture_shop.Classes.CartProductAdapter;
import com.example.furniture_shop.Classes.CartProducts;
import com.example.furniture_shop.Classes.ProductAdapter;
import com.example.furniture_shop.Classes.Products;
import com.example.furniture_shop.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    ListView listViewCart;
    Button btn_createOrder;
    TextView txt_totalPrice;
    public static ArrayList<CartProducts> cartProducts = new ArrayList<CartProducts>();
    public static boolean requestHasBeenSent = false;
    public static boolean isActionMode = false;
    public static ArrayList<CartProducts> UserSelection = new ArrayList<>();
    public static ActionMode actionMode = null;
    public static int totalPrice = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        listViewCart = findViewById(R.id.listView_cart);
        btn_createOrder = findViewById(R.id.button_create_order_cart);
        txt_totalPrice = findViewById(R.id.total_price_card);
        listViewCart.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);

        txt_totalPrice.setText(totalPrice+" руб.");

        if(!requestHasBeenSent) {
            getCartProductRequest();
        }

        CartProductAdapter adapter = new CartProductAdapter(getApplicationContext(), R.layout.listview_cart_products, cartProducts);
        listViewCart.setAdapter(adapter);


        listViewCart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               //
            }
        });

        btn_createOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBeforeCreateOrder();
            }
        });

        AbsListView.MultiChoiceModeListener modeListener = new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.topbar_cart, menu);
                isActionMode = true;
                actionMode = mode;
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_delete:
                        removeCartProducts();
                        return true;

                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                isActionMode = false;
                actionMode = null;
                UserSelection.clear();
            }
        };
        listViewCart.setMultiChoiceModeListener(modeListener);



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.cart);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        isActionMode = false;
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return true;
                    case R.id.catalog:
                        isActionMode = false;
                        startActivity(new Intent(getApplicationContext(), CatalogActivity.class));
                        finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return true;
                    case R.id.cart:
                        return true;
                    case R.id.profile:
                        isActionMode = false;
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


    void getCartProductRequest() {
        totalPrice = 0;

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        String url = AppParams.API_GET_CART_PRODUCTS;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listViewCart.setVisibility(View.VISIBLE);
                cartProducts.clear();
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject cartproduct = array.getJSONObject(i);
                        String id = cartproduct.getString("id");
                        String product_id = cartproduct.getString("product_id");
                        String name = cartproduct.getString("name");
                        String description = cartproduct.getString("description");
                        String price = cartproduct.getString("price");
                        String count = "Кол-во: "+cartproduct.getString("count");
                        String image = cartproduct.getString("image");

                        cartProducts.add(new CartProducts(id, product_id, count, name, description, image, price));

                        totalPrice += Integer.parseInt(cartproduct.getString("price")) * Integer.parseInt(cartproduct.getString("count"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(getApplicationContext(), CartActivity.class));
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", MainActivity.this_user.getId());
                return params;
            }
        };
        listViewCart.setVisibility(View.INVISIBLE);
        queue.add(stringRequest);
        requestHasBeenSent = true;
    }
    void removeCartProducts() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = AppParams.API_REMOVE_CART_PRODUCT;

        JSONArray array = new JSONArray();

            try {
                for (int i = 0; i < UserSelection.size(); i++) {
                    JSONObject object = new JSONObject();
                    object.put("id", i );
                    object.put("product_id", UserSelection.get(i).getId());
                    array.put(i, object);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }



        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, array, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                getCartProductRequest();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                Log.e("EEEROR" , error.toString());
        }
        });
        queue.add(jsonArrayRequest);
    }
    void addOrderRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = AppParams.API_ORDER;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CartActivity.this);
                alertDialogBuilder.setTitle("Оформление заказа");
                alertDialogBuilder.setMessage("Заказ успешно оформлен!");
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialogBuilder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getCartProductRequest();
                        ProfileActivity.requestOrdersHasBeenSent = false;
                        alertDialog.dismiss();
                    }
                });
                alertDialogBuilder.show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CartActivity.this);
                alertDialogBuilder.setTitle("Оформление заказа");
                alertDialogBuilder.setMessage("При оформлении заказа произошла ошибка, попробуйте позже!");
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialogBuilder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alertDialogBuilder.show();
            }
        }){
            public Map<String,String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("api_token", AppParams.getUserToken());
                return params;
            }
        };
        queue.add(stringRequest);
    }
    void checkBeforeCreateOrder() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Оформление заказа");
        alertDialogBuilder.setMessage("Вы действительно хотите оформить заказ!");
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialogBuilder.setPositiveButton(R.string.create_order, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addOrderRequest();
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