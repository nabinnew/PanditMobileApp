package com.example.saradha.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.saradha.databinding.PanditcardBinding;
import com.example.saradha.model.PanditModel;
import com.example.saradha.view.PanditDetailsActivity;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private Context ctx;
    private List<PanditModel> list;

    public HomeAdapter(Context ctx, List<PanditModel> list) {
        this.ctx = ctx;
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        PanditcardBinding binding;

        public ViewHolder(PanditcardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PanditcardBinding binding = PanditcardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PanditModel m = list.get(position);
        Log.i("TAG", "desc == "+m.description);

        // Name & Price
        holder.binding.panditName.setText(m.name);
        holder.binding.panditPrice.setText("Rs." + m.price);
        holder.binding.card.setOnClickListener(view -> {
            Intent intent = new Intent(ctx, PanditDetailsActivity.class);

            // Put all necessary details
            intent.putExtra("id", m.userid);
            intent.putExtra("name", m.name);
            intent.putExtra("price", m.price);
            intent.putExtra("photo", m.photo);
            intent.putExtra("services", m.services != null ? String.join(",", m.services) : "");
            intent.putExtra("experience", m.experience != null ? m.experience : "");
            intent.putExtra("location", m.location != null ? m.location : "");
            intent.putExtra("verified", m.verified);
            intent.putExtra("phone", m.phone != null ? m.phone : "");
            intent.putExtra("status", m.status != null ? m.status : "");
            intent.putExtra("address", m.address != null ? m.address : "");
            intent.putExtra("description", m.description != null ? m.description : "");

            ctx.startActivity(intent);
        });
        // Photo/Profile
        String imageUrl = m.photo != null ? m.photo : m.profile;
        Glide.with(ctx)
                .load(imageUrl)
                .into(holder.binding.panditImage);

        // Services (tags)
        if (m.services != null) {
            if (m.services.size() > 0) holder.binding.tag1.setText(m.services.get(0));

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
