package com.reem_codes.gp_android.adapter;

import android.app.Activity;
import android.content.Context;
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
import com.reem_codes.gp_android.activity.RaspberryActivity;
import com.reem_codes.gp_android.model.Base;
import com.reem_codes.gp_android.model.Command;
import com.reem_codes.gp_android.model.Raspberry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RaspberryAdapter extends ArrayAdapter<Raspberry> {
    Context context;
    static Raspberry raspberry;
    ListView listView;
    public RaspberryAdapter(Context context, List<Raspberry> raspberries, ListView listView) {
        super(context, R.layout.raspberry_item_adapter, raspberries);
        this.context = context;
        this.listView = listView;

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        // specify each item's layout
        View item = inflater.inflate(R.layout.raspberry_item_adapter, parent, false);

        // get the current raspberry object
        raspberry = getItem(position);

        // take the views from the layout
        final TextView name = (TextView) item.findViewById(R.id.raspberry);
        ImageButton delete = (ImageButton) item.findViewById(R.id.delete_raspberry);

        // set the nameview to take the current raspberry's name
        name.setText(raspberry.getName());

        // in case the user clicks the delete button: delete from the web server
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                raspberry = getItem(position);
                try {

                    String url = context.getString(R.string.api_url) + "/raspberry_user";
                    String json = String.format("{\"raspberry_id\": %d}", raspberry.getId());

                    RequestBody body = RequestBody.create(BaseActivity.JSON, json);
                    System.out.println("GPDEBUG json is " + json);

                    // then, we build the request by provising the url, the method and the body
                    Request request = new Request.Builder()
                            .url(url)
                            .delete(body)
                            .addHeader("Authorization", "Bearer " + ((BaseActivity)context).currentLoggedUser.getAccess_token())
                            .build();

                    ((BaseActivity)context).client.newCall(request).enqueue(new Callback() {

                        @Override
                        public void onFailure(Call call, IOException e) {
                            ((Activity)context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "raspberry not deleted, please check network and try again", Toast.LENGTH_LONG).show();
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



        return item;
    }
}