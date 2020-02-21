package com.reem_codes.gp_android.adapter;

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

import com.reem_codes.gp_android.R;
import com.reem_codes.gp_android.activity.RaspberryActivity;
import com.reem_codes.gp_android.model.Command;
import com.reem_codes.gp_android.model.Raspberry;

import java.util.ArrayList;
import java.util.List;

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
                Toast.makeText(context, raspberry.getName() + " was successfully deleted", Toast.LENGTH_LONG).show();
                RaspberryActivity.raspberries.remove(raspberry);
                remove(raspberry);
                listView.invalidate();
            }
        });


        return item;
    }
}