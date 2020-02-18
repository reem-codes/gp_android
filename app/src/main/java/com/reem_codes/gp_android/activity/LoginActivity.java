package com.reem_codes.gp_android.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.reem_codes.gp_android.R;

public class LoginActivity extends AppCompatActivity {

    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view) {
        /* CALL API */
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra("email", email.getText().toString());
//        intent.putExtra("password", password.getText().toString());
//        startActivity(intent);
    }
}
