package com.example.blessing_model.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blessing_model.R;
import com.example.blessing_model.pojo.Names;

import java.util.ArrayList;

public class NamesAdapter extends RecyclerView.Adapter<NamesAdapter.NameViewHolder> {


    private ArrayList<Names> names;


    public static class NameViewHolder extends RecyclerView.ViewHolder {
        public TextView itemId;
        public TextView parentText;
        public TextView childText;

        public NameViewHolder(@NonNull View itemView) {
            super(itemView);
            itemId = itemView.findViewById(R.id.itemId);
            parentText = itemView.findViewById(R.id.parentText);
            childText = itemView.findViewById(R.id.childText);

        }
    }

    public NamesAdapter(ArrayList<Names> names) {
        this.names = names;

    }

    @NonNull
    @Override
    public NamesAdapter.NameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.prayer_item, parent, false);
        NamesAdapter.NameViewHolder namesViewHolder = new NamesAdapter.NameViewHolder(view);
        return namesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NamesAdapter.NameViewHolder holder, int position) {
        Names names = this.names.get(position);
        holder.itemId.setText(names.getId());
        holder.parentText.setText(names.getNames());
        holder.childText.setText(names.getExplanation());
    }

    @Override
    public int getItemCount() {
        return names.size();
    }


    public void setFilter(ArrayList<Names> newList) {
        names = new ArrayList<>();
        names.addAll(newList);
        notifyDataSetChanged();
    }

}
