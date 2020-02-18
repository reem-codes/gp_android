package com.reem_codes.gp_android.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.reem_codes.gp_android.R;
import com.reem_codes.gp_android.adapter.RaspberryAdapter;
import com.reem_codes.gp_android.model.Raspberry;

import java.util.ArrayList;
import java.util.List;

public class RaspberryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raspberry);

        /* create a mock list
        TODO: take the arraylist from the api
         */
        List<Raspberry> raspberries= new ArrayList<>();
        raspberries.add(new Raspberry(1, "raspberry 1", "9878y"));
        raspberries.add(new Raspberry(2, "my room", "9878y"));
        raspberries.add(new Raspberry(3, "family home", "9878y"));

        // take the listview
        ListView listView = (ListView) findViewById(R.id.raspberries);
        // make a raspberry adapter
        ArrayAdapter arrayAdapter = new RaspberryAdapter(this, raspberries);
        // set the listview's adapter to the raspberry one
        listView.setAdapter(arrayAdapter);
    }
}
