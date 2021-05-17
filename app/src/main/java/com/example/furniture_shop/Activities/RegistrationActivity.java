package com.example.furniture_shop.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.furniture_shop.R;

public class RegistrationActivity extends AppCompatActivity {

    TextView txt_have_acc_yet;
    EditText edt_name, edt_surname, edt_email;
    Button btn_continue_reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        txt_have_acc_yet = findViewById(R.id.textView_already_have_account_reg_activity);
        edt_name = findViewById(R.id.editText_name_reg_activity);
        edt_email = findViewById(R.id.editText_email_reg_activity);
        edt_surname = findViewById(R.id.editText_surname_reg_activity);
        btn_continue_reg = findViewById(R.id.button_continue_registration_reg_activity);

        btn_continue_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_name.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Имя не может быть пустым", Toast.LENGTH_SHORT).show();
                } else if (edt_surname.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Фамилия не может быть пустым", Toast.LENGTH_SHORT).show();
                } else if (edt_email.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Почта не может быть пустым", Toast.LENGTH_SHORT).show();
                } else {
                    RegistrationActivity2.username = edt_name.getText().toString();
                    RegistrationActivity2.usersurname = edt_surname.getText().toString();
                    RegistrationActivity2.useremail = edt_email.getText().toString();
                    startActivity(new Intent(getApplicationContext(), RegistrationActivity2.class));
                    finish();
                }
            }
        });
    }
}