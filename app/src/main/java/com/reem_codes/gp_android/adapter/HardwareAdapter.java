package com.reem_codes.gp_android.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.reem_codes.gp_android.R;
import com.reem_codes.gp_android.activity.BaseActivity;
import com.reem_codes.gp_android.activity.NewHardwareActivity;
import com.reem_codes.gp_android.activity.RaspberryActivity;
import com.reem_codes.gp_android.model.Base;
import com.reem_codes.gp_android.model.Hardware;
import com.reem_codes.gp_android.model.Raspberry;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;


public class HardwareAdapter extends ArrayAdapter<Hardware> {
    public final static int LAUNCH_EDIT_HARDWARE = 7;

    Context context;
    static Hardware hardware;
    GridView gridView;
    Raspberry raspberry;

    public HardwareAdapter(Context context, List<Hardware> hardwares, GridView gridView) {
        super(context, R.layout.hardware_item_adapter, hardwares);
        this.context = context;
        this.gridView = gridView;
    }

    public HardwareAdapter(Context context, List<Hardware> hardwares, GridView gridView, Raspberry raspberry) {
        this(context, hardwares, gridView);
        this.raspberry = raspberry;
    }

        @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        // specify each item's layout
        View item = inflater.inflate(R.layout.hardware_item_adapter, parent, false);

        // get the current hardware from the list passed to the adapter
        hardware = getItem(position);

        // take the views from the layout
        final TextView name = (TextView) item.findViewById(R.id.name);
        ImageView image = (ImageView) item.findViewById(R.id.icon);
        ImageButton delete = (ImageButton) item.findViewById(R.id.delete);
        ImageButton edit = (ImageButton) item.findViewById(R.id.edit);

        name.setText(hardware.getName());

        // set the icon
        int resID = context.getResources().getIdentifier( "other", "drawable", context.getPackageName());

        if(hardware.getIcon() != null) {
            resID = context.getResources().getIdentifier( hardware.getIcon(), "drawable", context.getPackageName());
            System.out.println("GPDEBUG " + resID);
            if(resID == 0) {
                resID = context.getResources().getIdentifier( "other", "drawable", context.getPackageName());

            }
        }
        image.setImageResource(resID);


        // if the user clicked on delete button, call the API to delete it
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hardware = getItem(position);
                try {

                    String url = context.getString(R.string.api_url) + "/hardware/" + hardware.getId();

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
                                    Toast.makeText(context, "hardware not deleted, please check network and try again", Toast.LENGTH_LONG).show();
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
                            // specify the object type: is the string a json representation of a command? a user? in our case: base
                            TypeToken<Base> typeToken = new TypeToken<Base>(){};
                            // create the login object using the response body string and gson parser
                            final Base res = gson.fromJson(result, typeToken.getType());
                            ((Activity)context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context,res.getMessage() , Toast.LENGTH_SHORT).show();
                                    ((BaseActivity)context).loadActivity();
                                }
                            });
                        }
                    });
                } catch (Exception e){
                    Toast.makeText(context, "error while deleting, please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            }
        });

        // if the user clicked on the edit button, open the NewHardwareActivity
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hardware = getItem(position);
                Intent intent = new Intent(context, NewHardwareActivity.class);
                intent.putExtra("isEdit", true);
                intent.putExtra("raspberry", raspberry.getName());
                Gson gson = new Gson();
                intent.putExtra("hardware", gson.toJson(hardware));
                System.out.println("GPDEBUG " + gson.toJson(hardware));
                ((Activity)context).startActivityForResult(intent, LAUNCH_EDIT_HARDWARE);
            }
        });

        return item;
    }
}