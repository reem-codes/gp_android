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
import com.reem_codes.gp_android.model.User;

import java.util.ArrayList;
import java.util.List;

public class CommandAdapter extends ArrayAdapter<Command> {
    Context context;
    public CommandAdapter(Context context, List<Command> commands) {
        super(context, R.layout.command_item_adapter, commands);
        this.context = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        View item = inflater.inflate(R.layout.command_item_adapter, parent, false);


        final Command command = getItem(position);
        final TextView config = (TextView) item.findViewById(R.id.config);

        ImageButton delete = (ImageButton) item.findViewById(R.id.delete);
        ImageButton edit = (ImageButton) item.findViewById(R.id.edit);
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

        String configString = String.format("%s at %s on %s", command.isOn()? "ON" : "Off", command.getSchedule().getTime(), days);
        config.setText(configString);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Are you sure?", Toast.LENGTH_LONG).show();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, String.format("You are id num %d", command.getId()), Toast.LENGTH_LONG).show();
            }
        });
        return item;
    }
    public static ArrayList<Integer> intToBinaryPositions(int x){
        String binary = Integer.toBinaryString(x);
        ArrayList<Integer> positions = new ArrayList<>();
        for(int i = 0; i < binary.length(); i++){
            if(binary.charAt(i) == '1'){
                positions.add(i);
            }
        }

        return positions;
    }
}