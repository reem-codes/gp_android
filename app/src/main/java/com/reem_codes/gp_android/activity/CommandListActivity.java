package com.reem_codes.gp_android.activity;

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
import com.reem_codes.gp_android.model.Command;
import com.reem_codes.gp_android.model.Created;
import com.reem_codes.gp_android.model.Hardware;
import com.reem_codes.gp_android.model.Schedule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CommandListActivity extends BaseActivity {

    public static List<Command> commands;
    final int LAUNCH_ADD_COMMAND = 1;
    Hardware hardware;
    ListView listView;
    Command eCommand;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command_list);
        // check if user is properly logged in, or redirect to login page
        checkUser(this);

        // get the parent hardware that was clicked in the previous activity
        Intent intent = getIntent();
        int hardware_index = intent.getIntExtra("hardware_index", -1);
        if(hardware_index != -1) {
            hardware = HardwareListActivity.hardwares.get(hardware_index);
            super.setToolbar(hardware.getName() + "'s Command List");
            ((TextView) findViewById(R.id.hw_name)).setText(hardware.getName());
            ((TextView) findViewById(R.id.hw_config)).setText(hardware.isStatus()? "ON": "OFF");

        }


        commands = new ArrayList<>();
        listView = (ListView) findViewById(R.id.commands);


        /** new command
        * when the add command button is clicked, the NewCommand activity starts returning the command and the scheduling info if any
        * **/
        ImageButton addCommand = (ImageButton) findViewById(R.id.add_command);
        addCommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), NewCommandActivity.class);
                i.putExtra("hardware", hardware.getName());
                i.putExtra("status", hardware.isStatus());
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
            Toast.makeText(this, "please check network and try again",Toast.LENGTH_SHORT).show();
        }
    }


    public void getCommandApi() throws IOException{
        /**
         * This method gets the command list from the api and updates the adapter
         */
        url = getString(R.string.api_url) + "/command?schedule_id=not_null&hardware_id="+ hardware.getId();
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
                        Toast.makeText(CommandListActivity.this, "please check network and try again", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                System.out.println("GPDEBUG results are " + result);

                // parse result from string into object
                Gson gson = new Gson();
                TypeToken<ArrayList<Command>> typeToken = new TypeToken<ArrayList<Command>>(){};
                commands = gson.fromJson(result, typeToken.getType());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // update array adapter
                        ArrayAdapter arrayAdapter = new CommandAdapter(CommandListActivity.this, commands, listView, hardware);
                        listView.setAdapter(arrayAdapter);

                    }
                });

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /** once the results are returned from the new command activity, parse those results accordingly
         * check if the user cancelled the operation. If not upload the command data to the server
         * if it was scheduled the schedule must be posted/put first. Then the command
         */
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_ADD_COMMAND || requestCode == CommandAdapter.LAUNCH_EDIT_COMMAND) {
            if (resultCode == Activity.RESULT_OK) {
                boolean config = data.getBooleanExtra("config", true);
                boolean isEdit = data.getBooleanExtra("isEdit", false);
                boolean isScheduled = data.getBooleanExtra("isScheduled", false);
                if(isEdit){
                    TypeToken<Command> token = new TypeToken<Command>(){};
                    eCommand = (new Gson()).fromJson(data.getStringExtra("command"), token.getType());
                    System.out.println("GPDEBUG IS EDIT");
                }

                if(isScheduled) {
                    boolean isAM = data.getBooleanExtra("isAM", true);
                    int hour = data.getIntExtra("hour", -1);
                    int minute = data.getIntExtra("minute", -1);
                    int daysInt = data.getIntExtra("days", -1);

                    String time = String.format("%d:%d %s", hour, minute, isAM? "AM":"PM");
                    String sJson = String.format("{\"%s\": \"%s\", \"%s\": %d}", "time", time, "days",daysInt);
                    try {
                        postPutScheduleApi(sJson, config, isEdit);
                    } catch (IOException e) {
                        Toast.makeText(this, "schedule was not added. please check network and try again",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    try {
                        postPutCommandApi(config, -1, isEdit);
                    } catch (IOException e) {
                        Toast.makeText(this, "schedule was not added. please check network and try again",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "no command created", Toast.LENGTH_SHORT).show();
            }
        }
    }//onActivityResult

    public void postPutScheduleApi(String json, final Boolean config, final boolean isEdit) throws IOException{
        url = getString(R.string.api_url) + "/schedule";
        RequestBody body = RequestBody.create(JSON, json);
        System.out.println("GPDEBUG json is " + json);

        Request request;

        if(isEdit){
            url += "/" + eCommand.getSchedule().getId();
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
                        Toast.makeText(CommandListActivity.this, "please check network and try again", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                System.out.println("GPDEBUG results are " + result);
                Gson gson = new Gson();
                TypeToken<Created<Schedule>> typeToken = new TypeToken<Created<Schedule>>(){};
                Created<Schedule> schedule = gson.fromJson(result, typeToken.getType());
                postPutCommandApi(config, schedule.getObject().getId(), isEdit);
            }
        });
    }

    public void postPutCommandApi(Boolean config, int schedule_id, final boolean isEdit) throws IOException{
        url = getString(R.string.api_url) + "/command";
        String json = String.format("{\"%s\": %d, \"%s\": %b", "hardware_id", hardware.getId(), "configuration", config);
        json += schedule_id != -1? String.format(",\"%s\": %d", "schedule_id", schedule_id) : "";
        json += "}";

        RequestBody body = RequestBody.create(JSON, json);
        System.out.println("GPDEBUG json is " + json);

        Request request;
        if(isEdit){
            url += "/" + eCommand.getId();
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
        System.out.println(url);

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CommandListActivity.this, "command not made, please check network and try again", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                System.out.println("GPDEBUG results are " + result);

                Gson gson = new Gson();
                TypeToken<Created<Command>> typeToken = new TypeToken<Created<Command>>(){};
                final Created<Command> command = gson.fromJson(result, typeToken.getType());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!isEdit){
                            if(command.getObject().getSchedule() != null){
                                commands.add(command.getObject());
                            }
                            ArrayAdapter arrayAdapter = new CommandAdapter(CommandListActivity.this, commands, listView, hardware);
                            listView.setAdapter(arrayAdapter);
                        } else {
                            Toast.makeText(CommandListActivity.this,command.getMessage() , Toast.LENGTH_SHORT).show();
                            loadActivity();
                        }
                    }
                });
            }
        });
    }
}
