package com.reem_codes.gp_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.reem_codes.gp_android.R;
import com.reem_codes.gp_android.model.Hardware;

import java.util.List;


public class HardwareAdapter extends ArrayAdapter<Hardware> {
    Context context;
    public HardwareAdapter(Context context, List<Hardware> hardwares) {
        super(context, R.layout.hardware_item_adapter, hardwares);
        this.context = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        // specify each item's layout
        View item = inflater.inflate(R.layout.hardware_item_adapter, parent, false);

        // get the current hardware from the list passed to the adapter
        final Hardware hardware = getItem(position);

        // take the views from the layout
        final TextView name = (TextView) item.findViewById(R.id.name);
        ImageView image = (ImageView) item.findViewById(R.id.icon);

        name.setText(hardware.getName());

        // set the icon
        int resID = context.getResources().getIdentifier( "other", "drawable", context.getPackageName());

        if( hardware.getIcon() != null) {
            resID = context.getResources().getIdentifier( hardware.getIcon(), "drawable", context.getPackageName());
            System.out.println("GPDEBUG " + resID);
            if(resID == 0) {
                resID = context.getResources().getIdentifier( "other", "drawable", context.getPackageName());

            }
        }
        image.setImageResource(resID);


        return item;
    }
}