package com.example.saradha.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.saradha.R;
import com.example.saradha.model.Pandit;
import java.util.List;

public class PanditAdapter extends RecyclerView.Adapter<PanditAdapter.PanditViewHolder> {

    private Context context;
    private List<Pandit> list;

    public PanditAdapter(Context context, List<Pandit> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PanditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.book_pandit_items, parent, false);
        return new PanditViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PanditViewHolder holder, int position) {
        Pandit pandit = list.get(position);

        holder.name.setText(pandit.getName());
        holder.address.setText("📍 " + pandit.getLocation());
        holder.phone.setText("📞 " + pandit.getPhone());
        holder.date.setText("Date - " + pandit.getDate() + " At " + pandit.getTime());

        holder.btnCall.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + pandit.getPhone()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class PanditViewHolder extends RecyclerView.ViewHolder {
        TextView name, address, phone, date;
        Button btnCall;

        public PanditViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textPanditName);
            address = itemView.findViewById(R.id.textAddress);
            phone = itemView.findViewById(R.id.textPhone);
            date = itemView.findViewById(R.id.textdate);
            btnCall = itemView.findViewById(R.id.btncall);
        }
    }
}
