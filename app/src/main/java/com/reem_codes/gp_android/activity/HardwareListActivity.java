package com.reem_codes.gp_android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.reem_codes.gp_android.adapter.HardwareAdapter;
import com.reem_codes.gp_android.model.Created;
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
import okhttp3.RequestBody;
import okhttp3.Response;


public class HardwareListActivity extends BaseActivity {
    final int LAUNCH_ADD_HARDWARE = 3;

    SliderView sliderView;
    SliderAdapterExample adapter;
    public static List<Hardware> hardwares;
    Raspberry raspberry;
    GridView gridView;
    Hardware eHardware;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hardware_list);
        checkUser(this);

        // get the parent raspberry previously clicked
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


        hardwares = new ArrayList<>();
        gridView = (GridView) findViewById(R.id.grid_view);

        /* open commandlist belonging to this hardware */
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
                i.putExtra("raspberry", raspberry.getName());
                startActivityForResult(i, LAUNCH_ADD_HARDWARE);
            }
        });

        loadActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_ADD_HARDWARE || requestCode == HardwareAdapter.LAUNCH_EDIT_HARDWARE) {
            if (resultCode == Activity.RESULT_OK) {
                int gpio = data.getIntExtra("gpio", -1);
                String name = data.getStringExtra("name");
                String desc = data.getStringExtra("desc");
                String icon = data.getStringExtra("icon");
                boolean isEdit = data.getBooleanExtra("isEdit", false);
                if(isEdit){
                    TypeToken<Hardware> token = new TypeToken<Hardware>(){};
                    eHardware = (new Gson()).fromJson(data.getStringExtra("hardware"), token.getType());
                    System.out.println("GPDEBUG IS EDIT");
                }

                String text = String.format("{\"name\":\"%s\", \"gpio\":%d,\"icon\": \"%s\", \"desc\": \"%s\", \"raspberry_id\": %d}", name, gpio, icon, desc, raspberry.getId());
                try {
                    postPutHardwareApi(text, isEdit);
                } catch (IOException e) {
                    Toast.makeText(this, "please check network and try again",Toast.LENGTH_LONG).show();
                }
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

                Gson gson = new Gson();
                TypeToken<ArrayList<Hardware>> typeToken = new TypeToken<ArrayList<Hardware>>(){};
                hardwares = gson.fromJson(result, typeToken.getType());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // once the results arrive, update the arrayadapter
                        ArrayAdapter arrayAdapter = new HardwareAdapter(HardwareListActivity.this, hardwares, gridView);
                        gridView.setAdapter(arrayAdapter);
                    }
                });
            }
        });
    }

    public void postPutHardwareApi(String json, final boolean isEdit) throws IOException{
        url = getString(R.string.api_url) + "/hardware";

        RequestBody body = RequestBody.create(JSON, json);
        System.out.println("GPDEBUG json is " + json);


        Request request;

        if(isEdit){
            url += "/" + eHardware.getId();
            request = new Request.Builder()
                    .url(url)
                    .put(body)
                    .addHeader("Authorization", "Bearer " + currentLoggedUser.getAccess_token())
                    .build();

        } else {
            request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Authorization", "Bearer " + currentLoggedUser.getAccess_token())
                    .build();
        }
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(HardwareListActivity.this, "hardware not made, please check network and try again", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                System.out.println("GPDEBUG results are " + result);


                Gson gson = new Gson();
                TypeToken<Created<Hardware>> typeToken = new TypeToken<Created<Hardware>>(){};
                final Created<Hardware> hardware = gson.fromJson(result, typeToken.getType());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!isEdit) {
                            if (hardware.getObject() != null) {
                                hardwares.add(hardware.getObject());
                            }
                            ArrayAdapter arrayAdapter = new HardwareAdapter(HardwareListActivity.this, hardwares, gridView);
                            gridView.setAdapter(arrayAdapter);
                        }
                        else {
                            Toast.makeText(HardwareListActivity.this,hardware.getMessage() , Toast.LENGTH_SHORT).show();
                            loadActivity();
                        }
                    }
                });
            }
        });
    }
}
