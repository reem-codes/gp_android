package com.reem_codes.gp_android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.reem_codes.gp_android.adapter.HardwareAdapter;
import com.reem_codes.gp_android.model.Hardware;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderView;

import com.reem_codes.gp_android.R;
import com.reem_codes.gp_android.adapter.SliderAdapterExample;

import java.util.ArrayList;
import java.util.List;



public class HardwareListActivity extends BaseActivity {
    final int LAUNCH_ADD_HARDWARE = 3;

    SliderView sliderView;
    SliderAdapterExample adapter;
    public static List<Hardware> hardwares;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hardware_list);


        Intent intent = getIntent();
        final int raspberry_id = intent.getIntExtra("raspberry_index", -1);
        if(raspberry_id != -1) {
            Toast.makeText(this, "the raspberry clicked is " + RaspberryActivity.raspberries.get(raspberry_id).getName(), Toast.LENGTH_LONG).show();
            super.setToolbar(RaspberryActivity.raspberries.get(raspberry_id).getName() + "'s Hardware List");

        }

        /* Slider view configuration **/
        sliderView = findViewById(R.id.imageSlider);

        adapter = new SliderAdapterExample(this);
        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimations.FILL); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(2); //set scroll delay in seconds :
        sliderView.startAutoCycle();

        // make mock hardware list
        hardwares = new ArrayList<>();
        hardwares.add(new Hardware(1, "et", "led", "light", "led on my room", 11, 1, true));
        hardwares.add(new Hardware(2, "et", "solenoid", "electric", "AC on my room", 13, 1, false));
        hardwares.add(new Hardware(3, "et", "camera", "security", "garage camera", 13, 2, true));
        hardwares.add(new Hardware(4, "et", "weather station", "weather", "checking garden's weather", 13, 2, true));
        hardwares.add(new Hardware(5, "et", "washing machine", "plumbing", "turn on washing machine", 13, 2, true));
        hardwares.add(new Hardware(6, "et", "my cat's food", "other", "feed cat", 13, 2, false));

        GridView gridView = (GridView) findViewById(R.id.grid_view);
        // make an array adapter for the gridview of an object of type array adabter
        ArrayAdapter arrayAdapter = new HardwareAdapter(this, hardwares);
        // set the array adapter to the gridview
        gridView.setAdapter(arrayAdapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), CommandListActivity.class);
                intent.putExtra("hardware_index", i);
                startActivity(intent);
            }
        });

        /* new hardware **/
        ImageButton add = (ImageButton) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), NewHardwareActivity.class);
                i.putExtra("raspberry_index", raspberry_id);
                startActivityForResult(i, LAUNCH_ADD_HARDWARE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_ADD_HARDWARE) {
            if (resultCode == Activity.RESULT_OK) {
                int gpio = data.getIntExtra("gpio", -1);
                String name = data.getStringExtra("name");
                String desc = data.getStringExtra("desc");
                String icon = data.getStringExtra("icon");

                String text = String.format("%s %d %s\n %s", name, gpio, icon, desc);
                Toast.makeText(this, text, Toast.LENGTH_LONG).show();

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "no hardware created", Toast.LENGTH_LONG).show();
            }
        }
    }//onActivityResult

    @Override
    public void loadActivity() {
        Toast.makeText(this, "Reload Activity",Toast.LENGTH_LONG).show();

    }
}
