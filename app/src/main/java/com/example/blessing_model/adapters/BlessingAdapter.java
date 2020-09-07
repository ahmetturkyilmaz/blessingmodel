package com.example.blessing_model.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blessing_model.R;
import com.example.blessing_model.pojo.Blessing;

import java.util.ArrayList;

public class BlessingAdapter extends RecyclerView.Adapter<BlessingAdapter.BlessingViewHolder> {
    private ArrayList<Blessing> blessings;


    public static class BlessingViewHolder extends RecyclerView.ViewHolder {
        public TextView Id;
        public TextView blessingName;
        public TextView sure;

        public BlessingViewHolder(@NonNull View itemView) {
            super(itemView);
            Id = itemView.findViewById(R.id.itemId);
            blessingName = itemView.findViewById(R.id.parentText);
            sure = itemView.findViewById(R.id.childText);

        }
    }

    public BlessingAdapter(ArrayList<Blessing> blessings) {
        this.blessings = blessings;

    }

    @NonNull
    @Override
    public BlessingAdapter.BlessingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.prayer_item, parent, false);
        BlessingAdapter.BlessingViewHolder blessingViewHolder = new BlessingAdapter.BlessingViewHolder(view);
        return blessingViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BlessingAdapter.BlessingViewHolder holder, int position) {
        Blessing blessing = this.blessings.get(position);
        holder.Id.setText(blessing.getId());
        holder.blessingName.setText(blessing.getName());
        holder.sure.setText(blessing.getBlessing());
    }

    @Override
    public int getItemCount() {
        return blessings.size();
    }


    public void setFilter(ArrayList<Blessing> newList) {
        blessings= new ArrayList<>();
        blessings.addAll(newList);
        notifyDataSetChanged();
    }
}
