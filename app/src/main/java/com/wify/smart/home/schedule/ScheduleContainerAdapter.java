package com.wify.smart.home.schedule;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.wify.smart.home.R;
import com.wify.smart.home.dto.ScheduleObject;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.List;

public class ScheduleContainerAdapter extends RecyclerView.Adapter<ScheduleContainerAdapter.RecyclerViewHolder> {

    private final Context context;

    private List<ScheduleObject> schedules;

    public ScheduleContainerAdapter(Context context, List<ScheduleObject> schedules) {
        this.context = context;
        this.schedules = schedules;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.schedule_container_adapter, viewGroup, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int i) {

        try {

            ScheduleObject scheduleObject = schedules.get(i);

            holder.schedule_name.setText(scheduleObject.getName());

            if (scheduleObject.getTime().length() > 0) {

                String ScheduleSplit[] = scheduleObject.getTime().split(":");

                boolean isPM = (Integer.parseInt(ScheduleSplit[0]) >= 12);

                holder.schedule_time.setText(String.format("%02d:%02d %s", (Integer.parseInt(ScheduleSplit[0]) == 12 || Integer.parseInt(ScheduleSplit[0]) == 0) ? 12 : Integer.parseInt(ScheduleSplit[0]) % 12, Integer.parseInt(ScheduleSplit[1]), isPM ? "PM" : "AM"));

            }

            for (int index = 0; index < scheduleObject.getDays().length(); index++) {

                int c = Integer.parseInt("" + scheduleObject.getDays().charAt(index)) + 1;

                switch (c) {

                    case 1:
                        holder.schedule_day_s.setTextColor(context.getResources().getColor(R.color.bold_text_color));
                        break;

                    case 2:
                        holder.schedule_day_m.setTextColor(context.getResources().getColor(R.color.bold_text_color));
                        break;

                    case 3:
                        holder.schedule_day_t.setTextColor(context.getResources().getColor(R.color.bold_text_color));
                        break;

                    case 4:
                        holder.schedule_day_w.setTextColor(context.getResources().getColor(R.color.bold_text_color));
                        break;

                    case 5:
                        holder.schedule_day_th.setTextColor(context.getResources().getColor(R.color.bold_text_color));
                        break;

                    case 6:
                        holder.schedule_day_f.setTextColor(context.getResources().getColor(R.color.bold_text_color));
                        break;

                    case 7:
                        holder.schedule_day_sat.setTextColor(context.getResources().getColor(R.color.bold_text_color));
                        break;
                }
            }

            holder.view_schedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(context, AddScheduleActivity.class);

                    i.putExtra(UtilityConstants.SCHEDULE, scheduleObject);

                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(i);

                }
            });


        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return (null != schedules ? schedules.size() : 0);
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView schedule_name, schedule_time;

        ImageView view_schedule;

        TextView schedule_day_s, schedule_day_m, schedule_day_t, schedule_day_w, schedule_day_th, schedule_day_f, schedule_day_sat;

        RecyclerViewHolder(View view) {
            super(view);

            schedule_name = view.findViewById(R.id.schedule_name);

            schedule_time = view.findViewById(R.id.schedule_time);

            view_schedule = view.findViewById(R.id.view_schedule);

            schedule_day_s = view.findViewById(R.id.schedule_day_s);

            schedule_day_m = view.findViewById(R.id.schedule_day_m);

            schedule_day_t = view.findViewById(R.id.schedule_day_t);

            schedule_day_w = view.findViewById(R.id.schedule_day_w);

            schedule_day_th = view.findViewById(R.id.schedule_day_th);

            schedule_day_f = view.findViewById(R.id.schedule_day_f);

            schedule_day_sat = view.findViewById(R.id.schedule_day_sat);

        }

    }
}
