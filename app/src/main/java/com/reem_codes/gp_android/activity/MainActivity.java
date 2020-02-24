package com.reem_codes.gp_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.reem_codes.gp_android.R;
import com.reem_codes.gp_android.service.ResponseService;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        checkUser(this);

        Button login = (Button) findViewById(R.id.login);
        Button raspberry = (Button) findViewById(R.id.raspberry);
        Button command = (Button) findViewById(R.id.command);
        Button hardware = (Button) findViewById(R.id.hardware);
        Button addCommand = (Button) findViewById(R.id.add_command);
        Button schedule = (Button) findViewById(R.id.schedule);
        Button addHardware = (Button) findViewById(R.id.add_hardware);
        Button notification = (Button) findViewById(R.id.notify);
        Button start = (Button) findViewById(R.id.start);
        Button stop = (Button) findViewById(R.id.stop);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        raspberry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RaspberryActivity.class);
                startActivity(intent);
            }
        });
        command.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CommandListActivity.class);
                startActivity(intent);
            }
        });
        hardware.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), HardwareListActivity.class);
                startActivity(intent);
            }
        });
        addCommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NewCommandActivity.class);
                startActivity(intent);
            }
        });
        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ScheduleActivity.class);
                startActivity(intent);
            }
        });

        addHardware.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NewHardwareActivity.class);
                startActivity(intent);
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(view.getContext(), ResponseService.class));
            }
        });
    }


    @Override
    public void loadActivity() {

    }
}
