package com.example.furniture_shop.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.furniture_shop.Classes.AppParams;
import com.example.furniture_shop.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

    TextView user_name, user_surname, user_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user_email = findViewById(R.id.textView_email_profile_activity);
        user_name = findViewById(R.id.textView_name_profile_activity);
        user_surname = findViewById(R.id.textView_surname_profile_activity);

        user_email.setText(MainActivity.this_user.getEmail());
        user_name.setText(MainActivity.this_user.getName());
        user_surname.setText(MainActivity.this_user.getSurname());


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
            case R.id.action_add_category:
                addCategory();
            case R.id.action_add_product:
                addProduct();
                return true;
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
    public void addCategory() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Создать категорию");

        final EditText edt_category_name = new EditText(ProfileActivity.this);
        edt_category_name.setInputType(InputType.TYPE_CLASS_TEXT);
        alertDialogBuilder.setView(edt_category_name);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialogBuilder.setPositiveButton(R.string.action_add_category, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               addCategoryRequest(edt_category_name.getText().toString());
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
    public void addProduct() {
        //
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
               Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);
    }
    public void addProductRequest() {
        //
    }
}