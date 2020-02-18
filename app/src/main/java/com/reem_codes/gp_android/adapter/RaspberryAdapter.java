package com.reem_codes.gp_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.reem_codes.gp_android.R;
import com.reem_codes.gp_android.model.Command;
import com.reem_codes.gp_android.model.Raspberry;

import java.util.ArrayList;
import java.util.List;

public class RaspberryAdapter extends ArrayAdapter<Raspberry> {
    Context context;
    public RaspberryAdapter(Context context, List<Raspberry> raspberries) {
        super(context, R.layout.raspberry_item_adapter, raspberries);
        this.context = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        View item = inflater.inflate(R.layout.raspberry_item_adapter, parent, false);


        final Raspberry raspberry = getItem(position);
        final TextView name = (TextView) item.findViewById(R.id.raspberry);

        ImageButton delete = (ImageButton) item.findViewById(R.id.delete_raspberry);

        name.setText(raspberry.getName());

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Are you sure?", Toast.LENGTH_LONG).show();
            }
        });


        return item;
    }
}