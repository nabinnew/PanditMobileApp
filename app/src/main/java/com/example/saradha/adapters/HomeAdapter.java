package com.example.saradha.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.saradha.R;
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
        PanditcardBinding binding = PanditcardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PanditModel m = list.get(position);

        holder.binding.panditName.setText(m.name);
        holder.binding.panditPrice.setText("Rs." + m.price);

        Glide.with(ctx).load(m.photo).into(holder.binding.panditImage);

        // Dynamic service tags
        holder.binding.tagContainer.removeAllViews();
        if (m.services != null) {
            for (String s : m.services) {
                TextView tv = new TextView(ctx);
                tv.setText(s);
                tv.setTextSize(10f);
                tv.setTextColor(ctx.getResources().getColor(R.color.black));
                tv.setPadding(12, 4, 12, 4);
                tv.setBackgroundResource(R.drawable.rating_bg);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0,0,8,0);
                tv.setLayoutParams(params);

                holder.binding.tagContainer.addView(tv);
            }
        }

        holder.binding.card.setOnClickListener(v -> {
            Intent intent = new Intent(ctx, PanditDetailsActivity.class);
            intent.putExtra("id", m.userid);
            intent.putExtra("name", m.name);
            intent.putExtra("price", m.price);
            intent.putExtra("photo", m.photo);
            intent.putExtra("services", m.services != null ? String.join(",", m.services) : "");
            intent.putExtra("experience", m.experience);
            intent.putExtra("location", m.location);
            intent.putExtra("phone", m.phone);
            ctx.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateList(List<PanditModel> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }
}
