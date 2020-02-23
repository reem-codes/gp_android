package com.reem_codes.gp_android.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.reem_codes.gp_android.R;
import com.reem_codes.gp_android.model.Login;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public abstract class BaseActivity extends AppCompatActivity {
    public OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public String url;
    public Login currentLoggedUser;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.refresh) {
            System.out.println("GPDEBUG refresh");
            loadActivity();
            return true;
        }
        if(item.getItemId() == R.id.logout) {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.reem_codes.gp_android", Context.MODE_PRIVATE);
            sharedPreferences.edit().remove("login").apply();
            /* Logout api */
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    public void setToolbar(String text) {
        TextView toolbarText = (TextView) findViewById(R.id.toolbar_title);
        toolbarText.setText(text);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_top);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public void checkUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.reem_codes.gp_android", Context.MODE_PRIVATE);
        String login = sharedPreferences.getString("login", null);
        if(login != null) {
            Gson gson = new Gson();
            TypeToken<Login> token = new TypeToken<Login>(){};
            currentLoggedUser = gson.fromJson(login, token.getType());

        } else {
            Intent intent = new Intent(context, LoginActivity.class);
            startActivity(intent);
        }
    }

    public abstract void loadActivity();
}
