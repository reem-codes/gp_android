package com.reem_codes.gp_android.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import androidx.core.widget.TextViewCompat;
import android.widget.TextView;
import android.widget.Toast;
import com.reem_codes.gp_android.R;

import java.util.ArrayList;

public class ScheduleActivity extends AppCompatActivity {

    EditText hourEdit, minuteEdit;
    RadioButton am;
    int hour, minute;
    boolean isAM;

    static boolean[] isSelected = {false, false, false, false, false, false, false};
    boolean[] days = new boolean[7];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        hourEdit = (EditText) findViewById(R.id.hour);
        minuteEdit = (EditText) findViewById(R.id.min);
        am = (RadioButton) findViewById(R.id.am);

        TextView mon = (TextView) findViewById(R.id.mon);
        mon.setBackground(getResources().getDrawable(R.drawable.days_off));
        mon.setTextColor(getResources().getColor(android.R.color.darker_gray));


        TextView tues = (TextView) findViewById(R.id.tues);
        tues.setBackground(getResources().getDrawable(R.drawable.days_on));
        tues.setTextColor(getResources().getColor(R.color.white));


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

                String h = hourEdit.getText().toString();
                String m = minuteEdit.getText().toString();

                if(h.length() == 0 || m.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Hours and Minutes cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                hour = Integer.valueOf(h);
                minute = Integer.valueOf(m);

                if(hour > 12 || hour < 1) {
                    Toast.makeText(getApplicationContext(), "Hours must be between 1 and 12", Toast.LENGTH_LONG).show();
                    return;

                }
                if(minute > 59 || minute < 0) {
                    Toast.makeText(getApplicationContext(), "Minutes must be between 0 and 59", Toast.LENGTH_LONG).show();
                    return;

                }

                isAM = am.isChecked();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("hour", hour);
                returnIntent.putExtra("minute", minute);
                returnIntent.putExtra("isAM", isAM);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

    }
}
