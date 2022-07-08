package com.example.furniture_shop.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import org.json.JSONException;
import org.json.JSONObject;

public class AuthActivity extends AppCompatActivity {

    ImageView logo;
    TextView textView_signup;
    Button btn_signin;
    EditText edt_email, edt_password;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        logo = findViewById(R.id.imageView_logo_auth_activity);
        textView_signup = findViewById(R.id.textView_signup_auth_activity);
        edt_email = findViewById(R.id.editText_email_auth_activity);
        edt_password = findViewById(R.id.editText_password_auth_activity);
        btn_signin = findViewById(R.id.button_signin_auth_activity);
        progressBar = findViewById(R.id.progressBar_auth_activity);

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                logo.setVisibility(View.INVISIBLE);
                textView_signup.setVisibility(View.INVISIBLE);
                edt_email.setVisibility(View.INVISIBLE);
                edt_password.setVisibility(View.INVISIBLE);
                btn_signin.setVisibility(View.INVISIBLE);
                loginRequest();

            }
        });
        textView_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        ProfileActivity.requestOrdersHasBeenSent = false;
    }
    private void loginRequest() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = AppParams.API_SIGNIN;

        JSONObject object = new JSONObject();
        try {
            object.put("email", edt_email.getText().toString());
            object.put("password", edt_password.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.has("token")) {
                        saveData(response.getString("token"));
                        AppParams.setUserToken(response.getString("token"));
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        logo.setVisibility(View.VISIBLE);
                        textView_signup.setVisibility(View.VISIBLE);
                        edt_email.setVisibility(View.VISIBLE);
                        edt_password.setVisibility(View.VISIBLE);
                        btn_signin.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                btn_signin.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Ошибка авторизации", Toast.LENGTH_SHORT).show();

                progressBar.setVisibility(View.INVISIBLE);
                logo.setVisibility(View.VISIBLE);
                textView_signup.setVisibility(View.VISIBLE);
                edt_email.setVisibility(View.VISIBLE);
                edt_password.setVisibility(View.VISIBLE);
                btn_signin.setVisibility(View.VISIBLE);
            }
        });
        queue.add(jsonObjectRequest);
    }
    void saveData(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences(AppParams.SHARED_PREFS, MODE_PRIVATE);
        sharedPreferences.edit()
                .putString(AppParams.SP_BEARER_TOKEN, token)
                .putBoolean(AppParams.SP_USER_IS_LOGINED, true)
                .apply();

    }
    void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(AppParams.SHARED_PREFS, MODE_PRIVATE);
    }
}