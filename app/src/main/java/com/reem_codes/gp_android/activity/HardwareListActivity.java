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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.reem_codes.gp_android.adapter.HardwareAdapter;
import com.reem_codes.gp_android.adapter.RaspberryAdapter;
import com.reem_codes.gp_android.model.Hardware;
import com.reem_codes.gp_android.model.Raspberry;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderView;

import com.reem_codes.gp_android.R;
import com.reem_codes.gp_android.adapter.SliderAdapterExample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;


public class HardwareListActivity extends BaseActivity {
    final int LAUNCH_ADD_HARDWARE = 3;

    SliderView sliderView;
    SliderAdapterExample adapter;
    public static List<Hardware> hardwares;
    Raspberry raspberry;
    GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hardware_list);
        checkUser(this);


        Intent intent = getIntent();
        final int raspberry_id = intent.getIntExtra("raspberry_index", -1);
        if(raspberry_id != -1) {
            raspberry = RaspberryActivity.raspberries.get(raspberry_id);
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
        gridView = (GridView) findViewById(R.id.grid_view);


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

        loadActivity();
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
        try {
            getHardwareApi();
        } catch (IOException e) {
            Toast.makeText(this, "please check network and try again",Toast.LENGTH_LONG).show();
        }
    }


    public void getHardwareApi() throws IOException{
        url = getString(R.string.api_url) + String.format("/hardware?user_id=%d&raspberry_id=%d",currentLoggedUser.getUser().getId(), raspberry.getId());

        // then, we build the request by provising the url, the method and the body
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", "Bearer " + currentLoggedUser.getAccess_token())
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(HardwareListActivity.this, "please check network and try again", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                System.out.println("GPDEBUG results are " + result);


                // use Gson to parse from a string to objects and lists
                // first create Gson object
                Gson gson = new Gson();
                // specify the object type: is the string a json representation of a command? a user? in our case: login
                TypeToken<ArrayList<Hardware>> typeToken = new TypeToken<ArrayList<Hardware>>(){};
                // create the login object using the response body string and gson parser
                hardwares = gson.fromJson(result, typeToken.getType());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(HardwareListActivity.this," Success" , Toast.LENGTH_LONG).show();

                        // make an array adapter for the gridview of an object of type array adabter
                        ArrayAdapter arrayAdapter = new HardwareAdapter(HardwareListActivity.this, hardwares);
                        // set the array adapter to the gridview
                        gridView.setAdapter(arrayAdapter);

                    }
                });

            }
        });
    }
}
