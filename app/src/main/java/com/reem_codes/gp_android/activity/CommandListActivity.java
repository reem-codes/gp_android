package com.reem_codes.gp_android.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.reem_codes.gp_android.R;
import com.reem_codes.gp_android.adapter.CommandAdapter;
import com.reem_codes.gp_android.adapter.HardwareAdapter;
import com.reem_codes.gp_android.model.Command;
import com.reem_codes.gp_android.model.Created;
import com.reem_codes.gp_android.model.Hardware;
import com.reem_codes.gp_android.model.Schedule;
import com.reem_codes.gp_android.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.reem_codes.gp_android.adapter.DayAdapter.days;

public class CommandListActivity extends BaseActivity {

    public static List<Command> commands;
    final int LAUNCH_ADD_COMMAND = 1;
    Hardware hardware;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command_list);
        checkUser(this);

        Intent intent = getIntent();
        int hardware_index = intent.getIntExtra("hardware_index", -1);
        if(hardware_index != -1) {
            hardware = HardwareListActivity.hardwares.get(hardware_index);
            super.setToolbar(hardware.getName() + "'s Command List");
            ((TextView) findViewById(R.id.hw_name)).setText(hardware.getName());
            ((TextView) findViewById(R.id.hw_config)).setText(hardware.isStatus()? "ON": "OFF");

        }

        /* list of commands
        TODO: get list from server
         */
        commands = new ArrayList<>();
        // get the listview
        listView = (ListView) findViewById(R.id.commands);
        // make an array adapter for the listview of an object of type array adabter


        /* new command **/
        ImageButton addCommand = (ImageButton) findViewById(R.id.add_command);
        addCommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), NewCommandActivity.class);
                i.putExtra("hardware", hardware.getName());
                startActivityForResult(i, LAUNCH_ADD_COMMAND);
            }
        });


        loadActivity();

    }

    @Override
    public void loadActivity() {
        try {
            getCommandApi();
        } catch (IOException e) {
            Toast.makeText(this, "please check network and try again",Toast.LENGTH_LONG).show();
        }
    }


    public void getCommandApi() throws IOException{
        url = getString(R.string.api_url) + "/command?schedule_id=not_null&hardware_id="+ hardware.getId();
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
                        Toast.makeText(CommandListActivity.this, "please check network and try again", Toast.LENGTH_LONG).show();
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
                TypeToken<ArrayList<Command>> typeToken = new TypeToken<ArrayList<Command>>(){};
                // create the login object using the response body string and gson parser
                commands = gson.fromJson(result, typeToken.getType());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CommandListActivity.this," Success" , Toast.LENGTH_LONG).show();

                        ArrayAdapter arrayAdapter = new CommandAdapter(CommandListActivity.this, commands, listView);
                        // set the array adapter to the listview
                        listView.setAdapter(arrayAdapter);

                    }
                });

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_ADD_COMMAND) {
            if (resultCode == Activity.RESULT_OK) {
                boolean config = data.getBooleanExtra("config", true);
                boolean isScheduled = data.getBooleanExtra("isScheduled", false);

                if(isScheduled) {
                    boolean isAM = data.getBooleanExtra("isAM", true);
                    int hour = data.getIntExtra("hour", -1);
                    int minute = data.getIntExtra("minute", -1);
                    int daysInt = data.getIntExtra("days", -1);

                    String time = String.format("%d:%d %s", hour, minute, isAM? "AM":"PM");
                    String sJson = String.format("{\"%s\": \"%s\", \"%s\": %d}", "time", time, "days",daysInt);
                    try {
                        postScheduleApi(sJson, config);
                    } catch (IOException e) {
                        Toast.makeText(this, "schedule was not added. please check network and try again",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    try {
                        postCommandApi(config, -1);
                    } catch (IOException e) {
                        Toast.makeText(this, "schedule was not added. please check network and try again",Toast.LENGTH_LONG).show();
                    }
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "no command created", Toast.LENGTH_LONG).show();
            }
        }
    }//onActivityResult

    public void postScheduleApi(String json, final Boolean config) throws IOException{
        url = getString(R.string.api_url) + "/schedule";
        RequestBody body = RequestBody.create(JSON, json);
        System.out.println("GPDEBUG json is " + json);

        // then, we build the request by provising the url, the method and the body
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization", "Bearer " + currentLoggedUser.getAccess_token())
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CommandListActivity.this, "please check network and try again", Toast.LENGTH_LONG).show();
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
                TypeToken<Created<Schedule>> typeToken = new TypeToken<Created<Schedule>>(){};
                // create the login object using the response body string and gson parser
                Created<Schedule> schedule = gson.fromJson(result, typeToken.getType());
                postCommandApi(config, schedule.getObject().getId());
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                });

            }
        });
    }

    public void postCommandApi(Boolean config, int schedule_id) throws IOException{
        url = getString(R.string.api_url) + "/command";
        String json = String.format("{\"%s\": %d, \"%s\": %b", "hardware_id", hardware.getId(), "configuration", config);
        json += schedule_id != -1? String.format(",\"%s\": %d", "schedule_id", schedule_id) : "";
        json += "}";

        RequestBody body = RequestBody.create(JSON, json);
        System.out.println("GPDEBUG json is " + json);

        // then, we build the request by provising the url, the method and the body
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization", "Bearer " + currentLoggedUser.getAccess_token())
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CommandListActivity.this, "command not made, please check network and try again", Toast.LENGTH_LONG).show();
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
                TypeToken<Created<Command>> typeToken = new TypeToken<Created<Command>>(){};
                // create the login object using the response body string and gson parser
                final Created<Command> command = gson.fromJson(result, typeToken.getType());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CommandListActivity.this," Success" , Toast.LENGTH_LONG).show();
                        if(command.getObject().getSchedule() != null){
                            commands.add(command.getObject());
                        }
                        ArrayAdapter arrayAdapter = new CommandAdapter(CommandListActivity.this, commands, listView);
                        // set the array adapter to the listview
                        listView.setAdapter(arrayAdapter);
                    }
                });
            }
        });
    }

}
