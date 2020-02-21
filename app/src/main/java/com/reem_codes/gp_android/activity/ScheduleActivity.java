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
import android.widget.Toast;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.reem_codes.gp_android.R;
import com.reem_codes.gp_android.adapter.DayAdapter;

public class ScheduleActivity extends AppCompatActivity {

    EditText hourEdit, minuteEdit;
    RadioButton am;
    int hour, minute;
    boolean isAM;

    public static boolean[] isSelected = {false, false, false, false, false, false, false};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        hourEdit = (EditText) findViewById(R.id.hour);
        minuteEdit = (EditText) findViewById(R.id.min);
        am = (RadioButton) findViewById(R.id.am);


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
                int countOn = 0;
                for(boolean day : isSelected) {
                    if(day){
                        countOn++;
                    }
                }
                if(countOn == 0){
                    Toast.makeText(getApplicationContext(), "You must select one day at east", Toast.LENGTH_LONG).show();
                    return;
                }


                isAM = am.isChecked();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("hour", hour);
                returnIntent.putExtra("minute", minute);
                returnIntent.putExtra("isAM", isAM);
                returnIntent.putExtra("days", isSelected);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

    }
}
