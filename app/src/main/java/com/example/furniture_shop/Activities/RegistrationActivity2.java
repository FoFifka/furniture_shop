package com.example.furniture_shop.Activities;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class RegistrationActivity2 extends AppCompatActivity {

    ImageView logo;
    TextView txt_have_acc_yet, txt_back;
    EditText edt_password, edt_repeat_password;
    Button btn_create_acc;
    ProgressBar progressBar;

    public static String username = "";
    public static String usersurname = "";
    public static String useremail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration2);

        logo = findViewById(R.id.imageView_logo_reg_activity2);
        txt_have_acc_yet = findViewById(R.id.textView_already_have_account_reg_activity2);
        txt_back = findViewById(R.id.textView_back_reg_activity2);
        edt_password = findViewById(R.id.editText_password_reg_activity2);
        edt_repeat_password = findViewById(R.id.editText_repeat_password_reg_activity2);
        btn_create_acc = findViewById(R.id.button_create_acc_reg_activity2);
        progressBar = findViewById(R.id.progressBar_reg_activity2);

        btn_create_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_password.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Поля не могут быть пустыми!", Toast.LENGTH_SHORT).show();
                } else {
                    if(edt_password.getText().toString().equals(edt_repeat_password.getText().toString())) {
                        progressBar.setVisibility(View.VISIBLE);
                        logo.setVisibility(View.INVISIBLE);
                        txt_back.setVisibility(View.INVISIBLE);
                        txt_have_acc_yet.setVisibility(View.INVISIBLE);
                        edt_password.setVisibility(View.INVISIBLE);
                        edt_repeat_password.setVisibility(View.INVISIBLE);
                        btn_create_acc.setVisibility(View.INVISIBLE);
                        signUpRequest();
                    } else {
                        Toast.makeText(getApplicationContext(), "Пароли не совпадают", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

        txt_have_acc_yet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AuthActivity.class));
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }
    private void signUpRequest() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = AppParams.API_SIGNUP;

        JSONObject object = new JSONObject();
        try {
            object.put("name", username);
            object.put("surname", usersurname);
            object.put("email", useremail);
            object.put("password", edt_password.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getApplicationContext(), "Регистрация прошла успешно!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), AuthActivity.class));
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Ошибка регистрации", Toast.LENGTH_SHORT).show();

                progressBar.setVisibility(View.INVISIBLE);
                logo.setVisibility(View.VISIBLE);
                txt_back.setVisibility(View.VISIBLE);
                txt_have_acc_yet.setVisibility(View.VISIBLE);
                edt_password.setVisibility(View.VISIBLE);
                edt_repeat_password.setVisibility(View.VISIBLE);
                btn_create_acc.setVisibility(View.VISIBLE);
            }
        });
        queue.add(jsonObjectRequest);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}