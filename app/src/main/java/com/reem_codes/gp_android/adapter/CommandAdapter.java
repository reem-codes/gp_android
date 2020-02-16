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
import com.reem_codes.gp_android.model.User;

import java.util.List;

public class CommandAdapter extends ArrayAdapter<User> {
    Context context;
    public CommandAdapter(Context context, List<User> users) {
        super(context, R.layout.command_item_adapter, users);
        this.context = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        View item = inflater.inflate(R.layout.command_item_adapter, parent, false);


        final User user = getItem(position);
        final TextView config = (TextView) item.findViewById(R.id.config);
        TextView time = (TextView) item.findViewById(R.id.time);
        TextView days = (TextView) item.findViewById(R.id.days);
        ImageButton delete = (ImageButton) item.findViewById(R.id.delete);
        ImageButton edit = (ImageButton) item.findViewById(R.id.edit);
        config.setText(user.getEmail());
        time.setText(user.getUpdateAt());
        days.setText(Integer.toString(user.getId()));

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Are you sure?", Toast.LENGTH_LONG).show();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, String.format("You are id num %d", user.getId()), Toast.LENGTH_LONG).show();
            }
        });
        return item;
    }
}