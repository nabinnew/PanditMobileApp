package com.example.saradha.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saradha.R;
import com.example.saradha.model.NotificationModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserNotificationAdapter extends RecyclerView.Adapter<UserNotificationAdapter.ViewHolder> {

    Context context;
    List<NotificationModel> list;

    public UserNotificationAdapter(Context context, List<NotificationModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationModel model = list.get(position);

        holder.title.setText(model.getTitle());
        holder.body.setText(model.getBody());
        holder.time.setText(getTimeAgo(model.getCreated_at()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, body, time;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notificationTitle);
            body = itemView.findViewById(R.id.notificationBody);
            time = itemView.findViewById(R.id.notificationTime);
        }
    }

    // ✅ Auto Time Formatting Logic
    private String getTimeAgo(String dateTime) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date past = format.parse(dateTime);
            Date now = new Date();

            long diff = now.getTime() - past.getTime();

            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

            if (seconds < 60) return "Just now";
            if (minutes < 60) return minutes + " minutes ago";
            if (hours < 24) return hours + " hours ago";
            return days + " days ago";

        } catch (Exception e) {
            return "";
        }
    }
}
