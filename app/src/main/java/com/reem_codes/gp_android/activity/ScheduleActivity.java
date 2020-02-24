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
import androidx.recyclerview.widget.RecyclerView;

import android.widget.TextView;
import android.widget.Toast;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.reem_codes.gp_android.R;
import com.reem_codes.gp_android.adapter.CommandAdapter;
import com.reem_codes.gp_android.adapter.DayAdapter;

public class ScheduleActivity extends AppCompatActivity {

    EditText hourEdit, minuteEdit;
    RadioButton am, pm;
    int hour, minute;
    boolean isAM, isEdit;

    public static boolean[] isSelected = {false, false, false, false, false, false, false};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        TextView toolbarText = (TextView) findViewById(R.id.toolbar_title);
        toolbarText.setText("Add Schedule");
        hourEdit = (EditText) findViewById(R.id.hour);
        minuteEdit = (EditText) findViewById(R.id.min);
        am = (RadioButton) findViewById(R.id.am);
        pm = (RadioButton) findViewById(R.id.pm);

        Intent intent = getIntent();
        isEdit = intent.getBooleanExtra("isEdit", false);
        if(isEdit) {
            toolbarText.setText("Edit Schedule");
            String time = intent.getStringExtra("time");
            int days = intent.getIntExtra("days", 0);
            String[] split = time.split(":");
            hour = Integer.valueOf(split[0]);
            String[] secondSplit= split[1].split(" ");
            minute = Integer.valueOf(secondSplit[0]);
            isAM = secondSplit[1].equals("AM")? true: false;

            hourEdit.setText(""+hour);
            minuteEdit.setText(""+minute);
            am.setChecked(isAM);
            pm.setChecked(!isAM);

            isSelected = CommandAdapter.isSelected(days);
        }
        /*
        app:flexWrap="wrap"
            app:justifyContent="space_around"
            app:alignItems="center"
            app:alignContent="center"
         */
        FlexboxLayoutManager manager = new FlexboxLayoutManager(this, FlexDirection.ROW);
        manager.setJustifyContent(JustifyContent.SPACE_AROUND);
        manager.setFlexWrap(FlexWrap.WRAP);
        manager.setAlignItems(AlignItems.CENTER);

        RecyclerView recycler = (RecyclerView) findViewById(R.id.days);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(new DayAdapter(this));


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
                    Toast.makeText(getApplicationContext(), "Hours and Minutes cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                hour = Integer.valueOf(h);
                minute = Integer.valueOf(m);

                if(hour > 12 || hour < 1) {
                    Toast.makeText(getApplicationContext(), "Hours must be between 1 and 12", Toast.LENGTH_SHORT).show();
                    return;

                }
                if(minute > 59 || minute < 0) {
                    Toast.makeText(getApplicationContext(), "Minutes must be between 0 and 59", Toast.LENGTH_SHORT).show();
                    return;

                }
                int countOn = 0;
                for(boolean day : isSelected) {
                    if(day){
                        countOn++;
                    }
                }
                if(countOn == 0){
                    Toast.makeText(getApplicationContext(), "You must select one day at east", Toast.LENGTH_SHORT).show();
                    return;
                }
                int daysInt = 0;
                for(int i = 0; i < isSelected.length; i++) {
                    if(isSelected[i]){
                        daysInt += Math.pow(2, isSelected.length - i - 1);
                    }
                }

                isAM = am.isChecked();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("hour", hour);
                returnIntent.putExtra("minute", minute);
                returnIntent.putExtra("isAM", isAM);
                returnIntent.putExtra("days", daysInt);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

    }
}
