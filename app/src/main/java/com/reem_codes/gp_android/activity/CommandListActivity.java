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
import com.reem_codes.gp_android.model.Hardware;
import com.reem_codes.gp_android.model.Schedule;
import com.reem_codes.gp_android.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
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
                startActivityForResult(i, LAUNCH_ADD_COMMAND);
            }
        });


        loadActivity();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_ADD_COMMAND) {
            if (resultCode == Activity.RESULT_OK) {
                boolean config = data.getBooleanExtra("config", true);
                boolean isScheduled = data.getBooleanExtra("isScheduled", false);

                String text = config? "ON":"OFF";
                if(isScheduled) {
                    boolean isAM = data.getBooleanExtra("isAM", true);
                    int hour = data.getIntExtra("hour", -1);
                    int minute = data.getIntExtra("minute", -1);
                    boolean[] isSelected = data.getBooleanArrayExtra("days");

                    String daysString ="";
                    for(int i = 0; i < isSelected.length; i++){
                        if(ScheduleActivity.isSelected[i]){
                            daysString += days[i] + " ";
                        }
                    }
                    text += String.format(" %s. %d:%d\n%s", isAM? "AM":"PM", hour, minute, daysString);
                }
                Toast.makeText(this, text, Toast.LENGTH_LONG).show();

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "no command created", Toast.LENGTH_LONG).show();
            }
        }
    }//onActivityResult

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
}
