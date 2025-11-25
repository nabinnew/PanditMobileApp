package com.example.saradha.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.saradha.R;
import com.example.saradha.model.Pandit;
import com.example.saradha.model.PanditBooked;

import java.util.List;

public class PanditBookedAdapter extends RecyclerView.Adapter<PanditBookedAdapter.ViewHolder> {

    Context context;
    List<PanditBooked> panditList;

    public PanditBookedAdapter(Context context, List<PanditBooked> panditList) {
        this.context = context;
        this.panditList = panditList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pandit, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PanditBooked pandit = panditList.get(position);

        holder.name.setText(pandit.getName());
        holder.location.setText("📍 " + pandit.getLocation());
        holder.phone.setText("📞 " + pandit.getPhone());

        Glide.with(context)
                .load(pandit.getPhoto())
                .placeholder(R.drawable.pandit)
                .into(holder.image);

        holder.btnCall.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + pandit.getPhone()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return panditList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, location, phone;
        ImageView image;
        Button btnCall;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.textPanditName);
            location = itemView.findViewById(R.id.textAddress);
            phone = itemView.findViewById(R.id.textPhone);
            image = itemView.findViewById(R.id.panditImage);
            btnCall = itemView.findViewById(R.id.btnCallNow);
        }
    }
}
