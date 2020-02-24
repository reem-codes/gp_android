package com.reem_codes.gp_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.reem_codes.gp_android.R;
import com.reem_codes.gp_android.activity.ScheduleActivity;


public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {
    public static String[] days = {"Mon", "Tues", "Wed", "Thurs", "Fri", "Sat", "Sun"};
    private OnItemClickListener listener;
    private Context context;
    boolean isClicked;
    public DayAdapter(Context context) {
        this.context = context;
    }

    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.day_item, parent, false);
        return new DayViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DayViewHolder holder, int position) {
        String day = days[position];
        holder.dayName.setText(day);
        isClicked = ScheduleActivity.isSelected[position];
        if(isClicked) {
            holder.dayName.setBackground(context.getResources().getDrawable(R.drawable.days_on));
            holder.dayName.setTextColor(context.getResources().getColor(android.R.color.white));
        } else {
            holder.dayName.setBackground(context.getResources().getDrawable(R.drawable.days_off));
            holder.dayName.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
        }
    }

    @Override
    public int getItemCount() {
        return days.length;
    }


    class DayViewHolder extends RecyclerView.ViewHolder {
        private TextView dayName;
        public DayViewHolder(View itemView) {
            super(itemView);
            dayName = itemView.findViewById(R.id.day);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(days[position]);
                    }
                    if(!isClicked) {
                        dayName.setBackground(context.getResources().getDrawable(R.drawable.days_on));
                        dayName.setTextColor(context.getResources().getColor(android.R.color.white));
                        isClicked = true;
                        ScheduleActivity.isSelected[position] = true;
                    } else {
                        dayName.setBackground(context.getResources().getDrawable(R.drawable.days_off));
                        dayName.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
                        isClicked = false;
                        ScheduleActivity.isSelected[position] = false;
                    }

                }
            });
        }
    }


    public interface OnItemClickListener {
        void onItemClick(String day);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}