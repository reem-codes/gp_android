package com.reem_codes.gp_android.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.reem_codes.gp_android.R;
import com.reem_codes.gp_android.adapter.RaspberryAdapter;
import com.reem_codes.gp_android.model.Hardware;
import com.reem_codes.gp_android.model.Raspberry;

import java.util.ArrayList;
import java.util.List;

public class RaspberryActivity extends AppCompatActivity {

    public static List<Raspberry> raspberries;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raspberry);
        TextView toolbarText = (TextView) findViewById(R.id.toolbar_title);
        toolbarText.setText("Raspberry List");

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
    }
}
