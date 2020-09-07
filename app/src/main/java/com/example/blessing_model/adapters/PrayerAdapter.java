package com.example.blessing_model.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blessing_model.R;
import com.example.blessing_model.pojo.Prayer;

import java.util.ArrayList;

public class PrayerAdapter extends RecyclerView.Adapter<PrayerAdapter.PrayerViewHolder> {

    private ArrayList<Prayer> prayers;


    public static class PrayerViewHolder extends RecyclerView.ViewHolder {
        public TextView sureId;
        public TextView blessingName;
        public TextView sure;

        public PrayerViewHolder(@NonNull View itemView) {
            super(itemView);
            sureId = itemView.findViewById(R.id.itemId);
            blessingName = itemView.findViewById(R.id.parentText);
            sure = itemView.findViewById(R.id.childText);

        }
    }

    public PrayerAdapter() {
        this.prayers = new ArrayList<>();
    }

    public PrayerAdapter(ArrayList<Prayer> prayers) {
        this.prayers = prayers;
    }

    public void setPrayers(ArrayList<Prayer> prayers) {
        this.prayers = prayers;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PrayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.prayer_item, parent, false);
        PrayerViewHolder prayerViewHolder = new PrayerViewHolder(view);
        return prayerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PrayerViewHolder holder, int position) {
        Prayer prayer = this.prayers.get(position);
        holder.sureId.setText(prayer.getSureId());
        holder.blessingName.setText(prayer.getPrayerName());
        holder.sure.setText(prayer.getBlessing());
    }

    @Override
    public int getItemCount() {
        return prayers.size();
    }


    public void setFilter(ArrayList<Prayer> newList) {
        prayers = new ArrayList<>();
        prayers.addAll(newList);
        notifyDataSetChanged();
    }

}
