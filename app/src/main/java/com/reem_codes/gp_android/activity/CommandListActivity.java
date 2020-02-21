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

import com.reem_codes.gp_android.R;
import com.reem_codes.gp_android.adapter.CommandAdapter;
import com.reem_codes.gp_android.model.Command;
import com.reem_codes.gp_android.model.Schedule;
import com.reem_codes.gp_android.model.User;

import java.util.ArrayList;
import java.util.List;

public class CommandListActivity extends AppCompatActivity {

    public static List<Command> commands;
    final int LAUNCH_ADD_COMMAND = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command_list);
        Intent intent = getIntent();
        int raspberry_id = intent.getIntExtra("hardware_index", -1);
        if(raspberry_id != -1) {
            Toast.makeText(this, "the hardware clicked is " + HardwareListActivity.hardwares.get(raspberry_id).getName(), Toast.LENGTH_LONG).show();
            TextView toolbarText = (TextView) findViewById(R.id.toolbar_title);
            toolbarText.setText(HardwareListActivity.hardwares.get(raspberry_id).getName() + "'s Command List");

        }

        /* list of commands
        TODO: get list from server
         */
        commands = new ArrayList<>();
        commands.add(new Command(1, "2020-01-01", new Schedule(1, 102, "9:30 AM", "345"), true));
        commands.add(new Command(2, "2020-01-01", new Schedule(2, 127, "10:45 PM", "345"), true));
        commands.add(new Command(3, "2020-01-01", new Schedule(3, 1, "1:00 AM", "345"), false));
        commands.add(new Command(4, "2020-01-01", new Schedule(4, 105, "5:15 AM", "345"), true));
        commands.add(new Command(5, "2020-01-01", new Schedule(5, 86, "6:20 AM", "345"), false));
        commands.add(new Command(5, "2020-01-01", new Schedule(6, 85, "2:13 AM", "345"), false));
        commands.add(new Command(5, "2020-01-01", new Schedule(7, 87, "7:55 AM", "345"), false));
        commands.add(new Command(5, "2020-01-01", new Schedule(8, 33, "8:35 AM", "345"), false));
        commands.add(new Command(5, "2020-01-01", new Schedule(9, 13, "3:40 AM", "345"), false));
        commands.add(new Command(5, "2020-01-01", new Schedule(10, 16, "11:17 AM", "345"), false));
        commands.add(new Command(5, "2020-01-01", new Schedule(1, 18, "12:44 AM", "345"), false));

        // get the listview
        ListView listView = (ListView) findViewById(R.id.commands);
        // make an array adapter for the listview of an object of type array adabter
        ArrayAdapter arrayAdapter = new CommandAdapter(this, commands, listView);
        // set the array adapter to the listview
        listView.setAdapter(arrayAdapter);


        /* new command **/
        ImageButton addCommand = (ImageButton) findViewById(R.id.add_command);
        addCommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), NewCommandActivity.class);
                startActivityForResult(i, LAUNCH_ADD_COMMAND);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_ADD_COMMAND) {
            if (resultCode == Activity.RESULT_OK) {
                boolean config = data.getBooleanExtra("config", true);
                Toast.makeText(this, config? "ON":"OFF", Toast.LENGTH_LONG).show();

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "no command created", Toast.LENGTH_LONG).show();
            }
        }
    }//onActivityResult
}
