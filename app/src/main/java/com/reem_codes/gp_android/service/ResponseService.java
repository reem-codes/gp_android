package com.reem_codes.gp_android.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.reem_codes.gp_android.R;
import com.reem_codes.gp_android.activity.LoginActivity;
import com.reem_codes.gp_android.model.Login;
import com.reem_codes.gp_android.model._Response;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ResponseService extends Service {
    public static String CHANNEL_ID = "TESTING";
    public OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public String url;
    public Login currentLoggedUser;
    public static int DELAYED_TIME = 5 * 60 * 1000; // 5 minutes * 60 seconds * 1000 millisecond
    ArrayList<_Response> responses;

    private MediaPlayer player;
    public ResponseService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        createNotificationChannel();
        checkUser(this);

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        checkUser(this);
        if(currentLoggedUser != null) {
            final Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try{
                        getResponseApi();
                    }
                    catch (Exception e){
                        System.out.println("GPDEBUG "+ e.getMessage());
                    }
                    handler.postDelayed(this, DELAYED_TIME);
                }
            };
            handler.post(runnable);

        }
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //stopping the player when service is destroyed
    }
    public void checkUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.reem_codes.gp_android", Context.MODE_PRIVATE);
        String login = sharedPreferences.getString("login", null);
        if(login != null) {
            Gson gson = new Gson();
            TypeToken<Login> token = new TypeToken<Login>(){};
            currentLoggedUser = gson.fromJson(login, token.getType());

        }
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

                for(_Response res : responses) {
                    addNotification(res);
                }
            }
        });
    }

}
