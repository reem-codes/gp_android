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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.reem_codes.gp_android.R;

public abstract class BaseActivity extends AppCompatActivity {
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

    public abstract void loadActivity();
}
