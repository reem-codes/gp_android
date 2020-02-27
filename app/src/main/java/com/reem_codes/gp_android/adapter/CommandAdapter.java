package com.reem_codes.gp_android.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.reem_codes.gp_android.R;
import com.reem_codes.gp_android.activity.BaseActivity;
import com.reem_codes.gp_android.activity.HardwareListActivity;
import com.reem_codes.gp_android.activity.NewCommandActivity;
import com.reem_codes.gp_android.model.Base;
import com.reem_codes.gp_android.model.Command;
import com.reem_codes.gp_android.model.Hardware;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;


public class CommandAdapter extends ArrayAdapter<Command> {
    public final static int LAUNCH_EDIT_COMMAND = 4;

    Context context;
    static Command command;
    ListView listView;
    Hardware hardware;
    public CommandAdapter(Context context, List<Command> commands, ListView listView) {
        super(context, R.layout.command_item_adapter, commands);
        this.context = context;
        this.listView = listView;
    }
    public CommandAdapter(Context context, List<Command> commands, ListView listView, Hardware hardware) {
        this(context, commands, listView);
        this.hardware = hardware;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        // specify each item's layout
        View item = inflater.inflate(R.layout.command_item_adapter, parent, false);

        // get the current command from the list passed to the adapter
        command = getItem(position);

        // take the views from the layout
        final TextView config = (TextView) item.findViewById(R.id.config);
        ImageButton delete = (ImageButton) item.findViewById(R.id.delete);
        ImageButton edit = (ImageButton) item.findViewById(R.id.edit);

        /* This is the code to turn the days integer value into binary,
        then into an array of on positions representing the days in which the command is to be executed
         */
        ArrayList<Integer> positions = intToBinaryPositions(command.getSchedule().getDays());
        String days = "";
        if(positions.size() == 7) {
            days = "everyday";
        } else {
            for(int i = 0; i < positions.size(); i++) {
                int day = positions.get(i);
                switch (day) {
                    case 0: days += "Mon";break;
                    case 1: days += "Tue";break;
                    case 2: days += "Wed";break;
                    case 3: days += "Thurs";break;
                    case 4: days += "Fri";break;
                    case 5: days += "Sat";break;
                    case 6: days += "Sun";break;
                }
                if(i != positions.size() -1 ) {
                    days += ", ";
                }
            }
        }

        // make the config string and set the view text to the string
        String configString = String.format("%s at %s on %s", command.isConfiguration()? "ON" : "OFF", command.getSchedule().getTime(), days);
        config.setText(configString);

        // if the user clicked on delete button, call the API to delete it
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command = getItem(position);
                try {

                    String url = context.getString(R.string.api_url) + "/command/" + command.getId();

                    // then, we build the request by provising the url, the method and the body
                    Request request = new Request.Builder()
                            .url(url)
                            .delete()
                            .addHeader("Authorization", "Bearer " + ((BaseActivity)context).currentLoggedUser.getAccess_token())
                            .build();

                    ((BaseActivity)context).client.newCall(request).enqueue(new Callback() {

                        @Override
                        public void onFailure(Call call, IOException e) {
                            ((Activity)context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "command not deleted, please check network and try again", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String result = response.body().string();
                            System.out.println("GPDEBUG results are " + result);


                            // use Gson to parse from a string to objects and lists
                            // first create Gson object
                            Gson gson = new Gson();
                            // specify the object type: is the string a json representation of a command? a user? in our case: login
                            TypeToken<Base> typeToken = new TypeToken<Base>(){};
                            // create the login object using the response body string and gson parser
                            final Base res = gson.fromJson(result, typeToken.getType());
                            ((Activity)context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context,res.getMessage() , Toast.LENGTH_LONG).show();
                                    ((BaseActivity)context).loadActivity();
                                }
                            });
                        }
                    });
                } catch (Exception e){
                    Toast.makeText(context, "error while deleting, please check your internet connection", Toast.LENGTH_LONG).show();

                }
            }
        });

        // if the user clicked on the edit button, open the NewCommandActivity
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command = getItem(position);
                Intent intent = new Intent(context, NewCommandActivity.class);
                intent.putExtra("isEdit", true);
                intent.putExtra("hardware", hardware.getName());
                intent.putExtra("status", hardware.isStatus());
                Gson gson = new Gson();
                intent.putExtra("command", gson.toJson(command));
                System.out.println("GPDEBUG " + gson.toJson(command));
                System.out.println("GPDEBUG " + command.isConfiguration());
                ((Activity)context).startActivityForResult(intent, LAUNCH_EDIT_COMMAND);
            }
        });
        return item;
    }

    public static ArrayList<Integer> intToBinaryPositions(int x){
        /**
         * This method takes an integer number,
         * converts it to binary
         * and then returns an array of on positions */
        String binary = toBinary(x, 7);
        ArrayList<Integer> positions = new ArrayList<>();
        for(int i = 0; i < binary.length(); i++){
            if(binary.charAt(i) == '1'){
                positions.add(i);
            }
        }

        return positions;
    }

    public static String toBinary(int x, int len) {
        StringBuilder result = new StringBuilder();

        for (int i = len - 1; i >= 0 ; i--) {
            int mask = 1 << i;
            result.append((x & mask) != 0 ? 1 : 0);
        }

        return result.toString();
    }

    public static boolean[] isSelected(int x){
        /**
         * This method takes an integer number,
         * converts it to binary
         * and then returns an array of on positions */
        String binary = toBinary(x, 7);
        boolean[] positions = new boolean[7];
        for(int i = 0; i < binary.length(); i++){
            if(binary.charAt(i) == '1'){
                positions[i] = true;
            }else {
                positions[i] = false;

            }
        }

        return positions;
    }

}