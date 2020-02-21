package com.reem_codes.gp_android.activity;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.app.Activity;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.reem_codes.gp_android.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class NewCommandActivity extends Activity {
    final int LAUNCH_ADD_SCHEDULE = 2;

    boolean isOn = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_command);

        // Get reference of widgets from XML layout
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Initializing a String Array
        String[] configs = new String[]{
                "ON",
                "OFF"
        };

        final List<String> plantsList = new ArrayList<>(Arrays.asList(configs));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,R.layout.spinner,plantsList);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner);
        spinner.setAdapter(spinnerArrayAdapter);

        // set click listener to options
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                // Notify the selected item text
                Toast.makeText
                        (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                        .show();
                isOn = position == 0;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
                returnIntent.putExtra("config", isOn);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        Button schedule = (Button) findViewById(R.id.schedule);
        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ScheduleActivity.class);
                startActivityForResult(intent, LAUNCH_ADD_SCHEDULE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_ADD_SCHEDULE) {
            if (resultCode == Activity.RESULT_OK) {
                boolean isAM = data.getBooleanExtra("isAM", true);
                int hour = data.getIntExtra("hour", -1);
                int minute = data.getIntExtra("minute", -1);
                Toast.makeText(this, String.format("%s. %d:%d", isAM? "AM":"PM", hour, minute), Toast.LENGTH_LONG).show();

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "no schedule added", Toast.LENGTH_LONG).show();
            }
        }
    }//onActivityResult

}
