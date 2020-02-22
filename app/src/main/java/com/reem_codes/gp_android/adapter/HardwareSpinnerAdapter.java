package com.reem_codes.gp_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.reem_codes.gp_android.R;
import com.reem_codes.gp_android.activity.RaspberryActivity;
import com.reem_codes.gp_android.activity.ScheduleActivity;
import com.reem_codes.gp_android.model.Raspberry;

import java.util.List;


public class HardwareSpinnerAdapter extends ArrayAdapter<String>{

    private final LayoutInflater mInflater;
    private final Context mContext;
    private final String[] icons;
    private final int mResource;

    public HardwareSpinnerAdapter(@NonNull Context context, @LayoutRes int resource,
                              @NonNull String[] icons) {
        super(context, resource, 0, icons);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        this.icons = icons;
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent){
        final View view = mInflater.inflate(mResource, parent, false);

        LayoutInflater inflater = LayoutInflater.from(getContext());

        // specify each item's layout
        View item = inflater.inflate(R.layout.hardware_spinner, parent, false);

        // get the current icon name
        String iconName = icons[position];

        // take the views from the layout
        ImageView icon = (ImageView) item.findViewById(R.id.icon);

        // set the imageview
        int resID = mContext.getResources().getIdentifier(iconName, "drawable", mContext.getPackageName());
        icon.setImageResource(resID);
        return item;
    }
}