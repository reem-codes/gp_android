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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.reem_codes.gp_android.R;
import com.reem_codes.gp_android.adapter.CommandAdapter;
import com.reem_codes.gp_android.adapter.RaspberryAdapter;
import com.reem_codes.gp_android.model.Created;
import com.reem_codes.gp_android.model.Hardware;
import com.reem_codes.gp_android.model.Login;
import com.reem_codes.gp_android.model.Raspberry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RaspberryActivity extends BaseActivity {

    public static List<Raspberry> raspberries;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raspberry);
        super.setToolbar("Raspberry List");
        checkUser(this);


        /* create a mock list
        TODO: take the arraylist from the api
         */
        raspberries= new ArrayList<>();

        // take the listview
        listView = (ListView) findViewById(R.id.raspberries);
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

        loadActivity();

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
                String results = result.getContents();

                int raspberry_id = -1;
                try {
                    raspberry_id = Integer.valueOf(results);
                } catch (Exception e) {
                    Toast.makeText(this, "an invalid code is scanned",Toast.LENGTH_LONG).show();
                }
                if(raspberry_id != -1) {
                    try {
                        putRaspberryUser(raspberry_id);
                    } catch (IOException e) {
                        Toast.makeText(this, "please check network and try again",Toast.LENGTH_LONG).show();
                    }
                }
                
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void loadActivity() {
        try {
            getRaspberryApi();
        } catch (IOException e) {
            Toast.makeText(this, "please check network and try again",Toast.LENGTH_LONG).show();
        }
    }

    public void getRaspberryApi() throws IOException{
        url = getString(R.string.api_url) + "/raspberry?user_id="+ currentLoggedUser.getUser().getId();

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
                        Toast.makeText(RaspberryActivity.this, "please check network and try again", Toast.LENGTH_LONG).show();
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
                TypeToken<ArrayList<Raspberry>> typeToken = new TypeToken<ArrayList<Raspberry>>(){};
                // create the login object using the response body string and gson parser
                raspberries = gson.fromJson(result, typeToken.getType());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RaspberryActivity.this," Success" , Toast.LENGTH_LONG).show();

                        // make a raspberry adapter
                        ArrayAdapter arrayAdapter = new RaspberryAdapter(RaspberryActivity.this, raspberries, listView);
                        // set the listview's adapter to the raspberry one
                        listView.setAdapter(arrayAdapter);
                    }
                });

            }
        });
    }

    private void putRaspberryUser(int raspberry_id) throws IOException{
        url = getString(R.string.api_url) + "/raspberry_user";
        String json = String.format("{\"raspberry_id\": %d}", raspberry_id);

        RequestBody body = RequestBody.create(JSON, json);
        System.out.println("GPDEBUG json is " + json);

        // then, we build the request by provising the url, the method and the body
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .addHeader("Authorization", "Bearer " + currentLoggedUser.getAccess_token())
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RaspberryActivity.this, "please check network and try again", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                System.out.println("GPDEBUG results are " + result);
                final int code = response.code();

                // use Gson to parse from a string to objects and lists
                // first create Gson object
                Gson gson = new Gson();
                // specify the object type: is the string a json representation of a command? a user? in our case: login
                TypeToken<Created<Raspberry>> typeToken = new TypeToken<Created<Raspberry>>(){};
                // create the login object using the response body string and gson parser
                final Created<Raspberry> raspberry = gson.fromJson(result, typeToken.getType());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(raspberry.getObject() != null){

                            Toast.makeText(RaspberryActivity.this,"Success" , Toast.LENGTH_LONG).show();
                            raspberries.add(raspberry.getObject());

                            // make a raspberry adapter
                            ArrayAdapter arrayAdapter = new RaspberryAdapter(RaspberryActivity.this, raspberries, listView);
                            // set the listview's adapter to the raspberry one
                            listView.setAdapter(arrayAdapter);
                        } else if(code == 500){
                            Toast.makeText(RaspberryActivity.this,"Already added", Toast.LENGTH_LONG).show();

                        } else if (raspberry.getMessage() != null){
                            Toast.makeText(RaspberryActivity.this,raspberry.getMessage(), Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(RaspberryActivity.this, "an error occurred. Please check your network and try again", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
    }

}
