package com.reem_codes.gp_android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.reem_codes.gp_android.R;
import com.reem_codes.gp_android.adapter.RaspberryAdapter;
import com.reem_codes.gp_android.model.Hardware;
import com.reem_codes.gp_android.model.Raspberry;

import java.util.ArrayList;
import java.util.List;

public class RaspberryActivity extends BaseActivity {

    public static List<Raspberry> raspberries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raspberry);
        super.setToolbar("Raspberry List");

        /* create a mock list
        TODO: take the arraylist from the api
         */
        raspberries= new ArrayList<>();
        raspberries.add(new Raspberry(1, "raspberry 1", "9878y"));
        raspberries.add(new Raspberry(2, "my room", "9878y"));
        raspberries.add(new Raspberry(3, "family home", "9878y"));

        // take the listview
        ListView listView = (ListView) findViewById(R.id.raspberries);
        // make a raspberry adapter
        ArrayAdapter arrayAdapter = new RaspberryAdapter(this, raspberries, listView);
        // set the listview's adapter to the raspberry one
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), HardwareListActivity.class);
                intent.putExtra("raspberry_index", i);
                startActivity(intent);
            }
        });

        ImageButton add = (ImageButton) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQRScanner();
            }
        });
    }


    private void startQRScanner() {
        new IntentIntegrator(this).initiateScan();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result =   IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "user cancelled",Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, result.getContents(),Toast.LENGTH_LONG).show();

//                updateText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void updateText(String scanCode) {
//        Intent intent = new Intent(this, ItemDetailsActivity.class);
//        intent.putExtra(EXTRA_QR_RESULT, scanCode);
//        startActivity(intent);
    }

    @Override
    public void loadActivity() {
        Toast.makeText(this, "Reload Activity",Toast.LENGTH_LONG).show();

    }
}
