package com.reem_codes.gp_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.reem_codes.gp_android.R;
import com.reem_codes.gp_android.model.Command;
import com.reem_codes.gp_android.model.Hardware;
import com.reem_codes.gp_android.model.Login;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class NewCommandActivity extends Activity {
    final int LAUNCH_ADD_SCHEDULE = 2;

    boolean isOn = true;
    boolean isAM, isScheduled;
    int hour, minute, days;
    boolean isEdit;
    Command command;
    Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_command);
        gson = new Gson();

        TextView toolbarText = (TextView) findViewById(R.id.toolbar_title);
        toolbarText.setText("Add Command");

        Intent intent = getIntent();
        String name = intent.getStringExtra("hardware");
        isEdit = intent.getBooleanExtra("isEdit", false);
        if(name != null){
            ((TextView) findViewById(R.id.hardware_name)).setText(name);
        }
        if(isEdit) {
            ((TextView) findViewById(R.id.textView)).setText("Edit Command");
            toolbarText.setText("Edit Command");
            TypeToken<Command> token = new TypeToken<Command>(){};
            command = gson.fromJson(intent.getStringExtra("command"), token.getType());
        }
        // Get reference of widgets from XML layout
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Initializing a String Array
        String[] configs = new String[]{
                "ON",
                "OFF"
        };

        final List<String> plantsList = new ArrayList<>(Arrays.asList(configs));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,R.layout.command_spinner,plantsList);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.command_spinner);
        spinner.setAdapter(spinnerArrayAdapter);

        // set click listener to options
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                // Notify the selected item text
//                Toast.makeText
//                        (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
//                        .show();
                isOn = position == 0;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if(isEdit){
            System.out.println("GPDEBUG " + command.isConfiguration());
            spinner.setSelection(command.isConfiguration()? spinnerArrayAdapter.getPosition(configs[0]) : spinnerArrayAdapter.getPosition(configs[1]));

        }
        ImageButton close = (ImageButton) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });

        Button done = (Button) findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("config", spinner.getSelectedItemPosition() == 0);
                if(isEdit){
                    returnIntent.putExtra("command", gson.toJson(command));
                    returnIntent.putExtra("isEdit", true);

                }
                if(isScheduled){
                    returnIntent.putExtra("isScheduled", isScheduled);
                    returnIntent.putExtra("isAM", isAM);
                    returnIntent.putExtra("hour", hour);
                    returnIntent.putExtra("minute", minute);
                    returnIntent.putExtra("days", days);
                }

                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        Button schedule = (Button) findViewById(R.id.schedule);
        if(isEdit) {
            schedule.setText("Edit Schedule");
        }
        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ScheduleActivity.class);
                if(isEdit) {
                    intent.putExtra("isEdit", true);
                    intent.putExtra("days", command.getSchedule().getDays());
                    intent.putExtra("time", command.getSchedule().getTime());
                }
                startActivityForResult(intent, LAUNCH_ADD_SCHEDULE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_ADD_SCHEDULE) {
            if (resultCode == Activity.RESULT_OK) {
                isScheduled = true;
                isAM = data.getBooleanExtra("isAM", true);
                hour = data.getIntExtra("hour", -1);
                minute = data.getIntExtra("minute", -1);
                days = data.getIntExtra("days", -1);

                Toast.makeText(this, isEdit ? "Schedule edited" : "Schedule added", Toast.LENGTH_LONG).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "no schedule added", Toast.LENGTH_LONG).show();
            }
        }
    }//onActivityResult

}
