package com.reem_codes.gp_android.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.reem_codes.gp_android.R;
import com.reem_codes.gp_android.adapter.HardwareSpinnerAdapter;
import com.reem_codes.gp_android.model.Hardware;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewHardwareActivity extends AppCompatActivity {

    String[] icons = {"light", "electric", "security", "weather", "plumbing", "other"};
    int imageSelectedId, gpio;
    String name, desc, raspberry;
    EditText nameEdit, gpioEdit, descEdit;
    boolean isEdit;
    Hardware hardware;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_hardware);
        TextView toolbarText = (TextView) findViewById(R.id.toolbar_title);
        toolbarText.setText("Add Hardware");
        gson = new Gson();

        Intent intent = getIntent();
        raspberry = intent.getStringExtra("raspberry");
        isEdit = intent.getBooleanExtra("isEdit", false);


        nameEdit = (EditText) findViewById(R.id.name);
        gpioEdit = (EditText) findViewById(R.id.gpio);
        descEdit = (EditText) findViewById(R.id.desc);

        if(raspberry != null){
            ((TextView) findViewById(R.id.raspberry_name)).setText(raspberry);
        }
        if(isEdit) {
            ((TextView) findViewById(R.id.textView)).setText("Edit Hardware");
            toolbarText.setText("Edit Hardware");
            TypeToken<Hardware> token = new TypeToken<Hardware>(){};
            hardware = gson.fromJson(intent.getStringExtra("hardware"), token.getType());
            gpioEdit.setText(""+ hardware.getGpio());
            nameEdit.setText(hardware.getName());
            descEdit.setText(hardware.getDesc());
        }

        // Get reference of widgets from XML layout
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Initializing an ArrayAdapter
        final HardwareSpinnerAdapter spinnerArrayAdapter = new HardwareSpinnerAdapter(this, R.layout.hardware_spinner,icons);
        spinner.setAdapter(spinnerArrayAdapter);

        // set click listener to options
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                // Notify the selected item text

                imageSelectedId = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if(isEdit){
            spinner.setSelection(spinnerArrayAdapter.getPosition(hardware.getIcon()));

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

                name = nameEdit.getText().toString();
                desc = descEdit.getText().toString();

                if(name.equals("")) {
                    Toast.makeText(getApplicationContext(), "Name mustn't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                String sgpio = gpioEdit.getText().toString();

                if(sgpio.length() == 0) {
                    Toast.makeText(getApplicationContext(), "GPIO cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                gpio = Integer.valueOf(sgpio);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("name", name);
                returnIntent.putExtra("desc", desc);
                returnIntent.putExtra("gpio", gpio);
                returnIntent.putExtra("icon", icons[imageSelectedId]);
                if(isEdit){
                    returnIntent.putExtra("hardware", gson.toJson(hardware));
                    returnIntent.putExtra("isEdit", true);

                }

                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

    }
}
