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

        // specify each item's layout
        View item = inflater.inflate(R.layout.command_item_adapter, parent, false);

        // get the current command from the list passed to the adapter
        final Command command = getItem(position);

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
                Toast.makeText(context, "Are you sure?", Toast.LENGTH_LONG).show();
            }
        });

        // if the user clicked on the edit button, open the commandActivity
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, String.format("You are id num %d", command.getId()), Toast.LENGTH_LONG).show();
            }
        });
        return item;
    }

    public static ArrayList<Integer> intToBinaryPositions(int x){
        /**
         * This method takes an integer number,
         * converts it to binary
         * and then returns an array of on positions */
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