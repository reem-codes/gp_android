package com.reem_codes.gp_android.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.reem_codes.gp_android.R;
import com.reem_codes.gp_android.adapter.CommandAdapter;
import com.reem_codes.gp_android.model.Command;
import com.reem_codes.gp_android.model.Schedule;
import com.reem_codes.gp_android.model.User;

import java.util.ArrayList;
import java.util.List;

public class CommandListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command_list);

        List<Command> commands = new ArrayList<>();
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

        ListView listView = (ListView) findViewById(R.id.commands);
        ArrayAdapter arrayAdapter = new CommandAdapter(this, commands);
        listView.setAdapter(arrayAdapter);
    }
}
