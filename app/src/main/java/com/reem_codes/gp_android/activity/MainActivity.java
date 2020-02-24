package com.reem_codes.gp_android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat.Style;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.reem_codes.gp_android.R;
import com.reem_codes.gp_android.adapter.RaspberryAdapter;
import com.reem_codes.gp_android.model.Raspberry;
import com.reem_codes.gp_android.model.Schedule;
import com.reem_codes.gp_android.model._Response;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends BaseActivity {
    public static String CHANNEL_ID = "TESTING";
    ArrayList<_Response> responses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkUser(this);

        createNotificationChannel();
        Button login = (Button) findViewById(R.id.login);
        Button raspberry = (Button) findViewById(R.id.raspberry);
        Button command = (Button) findViewById(R.id.command);
        Button hardware = (Button) findViewById(R.id.hardware);
        Button addCommand = (Button) findViewById(R.id.add_command);
        Button schedule = (Button) findViewById(R.id.schedule);
        Button addHardware = (Button) findViewById(R.id.add_hardware);
        Button notification = (Button) findViewById(R.id.notify);

        try{
            getResponseApi();
        }
        catch (Exception e){
            System.out.println("GPDEBUG "+ e.getMessage());
            Toast.makeText(this, "failed first catch", Toast.LENGTH_SHORT).show();
        }
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
    }
    private void addNotification(_Response response) {
        System.out.println("GPDEBUG I am notifying");
        String message = response.getMessage() + "\n" + response.getExecutionTime();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.other);
        builder.setContentTitle(response.getIsDone() ? "Command executed successfully" : "an error occured");
        builder.setContentText(message);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setStyle(new NotificationCompat.BigTextStyle());
    //        Intent notificationIntent = new Intent(this, MainActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(contentIntent);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(response.getId(), builder.build());
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(response.getId(), builder.build());

        createNotificationChannel();

    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CHANNEL_ID;
            String description = "I am desc";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }

    public void getResponseApi() throws IOException {
        url = getString(R.string.api_url) + "/response";

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", "Bearer " + currentLoggedUser.getAccess_token())
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                System.out.println("GPDEBUG results are " + result);


                Gson gson = new Gson();
                TypeToken<ArrayList<_Response>> typeToken = new TypeToken<ArrayList<_Response>>(){};
                responses = gson.fromJson(result, typeToken.getType());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for(_Response res : responses) {
                            addNotification(res);
                        }
                    }
                });
            }
        });
    }


    @Override
    public void loadActivity() {

    }
}
