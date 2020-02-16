package com.reem_codes.gp_android.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.reem_codes.gp_android.R;
import com.reem_codes.gp_android.adapter.CommandAdapter;
import com.reem_codes.gp_android.model.User;

import java.util.ArrayList;
import java.util.List;

public class CommandListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command_list);

        List<User> commands = new ArrayList<>();
        commands.add(new User("reem@reem.com", "123425", 1));
        commands.add(new User("nouf@reem.com", "3grty", 2));
        commands.add(new User("sara@reem.com", "etyr5ge", 3));
        commands.add(new User("abeer@reem.com", "545", 4));
        commands.add(new User("mona@reem.com", "12etrhv3425", 5));
        commands.add(new User("doha@reem.com", "dfhvh", 6));
        ListView listView = (ListView) findViewById(R.id.commands);
        ArrayAdapter arrayAdapter = new CommandAdapter(this, commands);
        listView.setAdapter(arrayAdapter);
    }
}
