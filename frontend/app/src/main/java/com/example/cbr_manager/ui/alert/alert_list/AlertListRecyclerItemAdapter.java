package com.example.cbr_manager.ui.alert.alert_list;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cbr_manager.R;
import com.example.cbr_manager.service.visit.Visit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlertListRecyclerItemAdapter extends RecyclerView.Adapter<AlertListRecyclerItemAdapter.AlertItemViewHolder> implements Filterable {

    private List<AlertListRecyclerItem> alertListRecyclerItems;
    private OnItemListener onItemListener;
    private List<AlertListRecyclerItem> filteredAlerts;
    private boolean unreadChecked = false;
    
    public AlertListRecyclerItem getAlert(int position) {
        return filteredAlerts.get(position);
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public Filter getFilterWithCheckBox(boolean unreadChecked){
        this.unreadChecked = unreadChecked;
        return newFilter;
    }

    public Filter newFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String searchString = constraint.toString().toLowerCase().trim();

            List<AlertListRecyclerItem> tempFilteredList = new ArrayList<>();

            if (searchString.isEmpty()&!unreadChecked) {
                tempFilteredList.addAll(alertListRecyclerItems);
            } else {
                for (AlertListRecyclerItem alertListRecyclerItem : alertListRecyclerItems) {
                    if (alertListRecyclerItem.getTitle().toLowerCase().trim().contains(searchString) | alertListRecyclerItem.getBody().toLowerCase().trim().contains(searchString)) {
                        if(!alertListRecyclerItem.getMarkedRead()|!unreadChecked){
                            tempFilteredList.add(alertListRecyclerItem);
                        }
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = tempFilteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredAlerts.clear();
            filteredAlerts.addAll((List) results.values);
            Collections.reverse(filteredAlerts);
            notifyDataSetChanged();
        }
    };

    public static class AlertItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textListTitle;
        public TextView textListBody;
        public TextView textListDate;
        OnItemListener onItemListener;

        public AlertItemViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);
            textListTitle = itemView.findViewById(R.id.textListTitle);
            textListBody = itemView.findViewById(R.id.textListBody);
            textListDate = itemView.findViewById(R.id.textListDate);
            this.onItemListener = onItemListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onItemListener.onItemClick(getAdapterPosition());
        }
    }

    public interface OnItemListener {
        void onItemClick(int position);
    }

    public AlertListRecyclerItemAdapter(List<AlertListRecyclerItem> alertListRecyclerItems, OnItemListener onItemListener) {
        this.alertListRecyclerItems = alertListRecyclerItems;
        this.filteredAlerts = new ArrayList<>();
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public AlertItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.alert_item, parent, false);
        AlertItemViewHolder evh = new AlertItemViewHolder(v, onItemListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull AlertItemViewHolder holder, int position) {
        AlertListRecyclerItem currentItem = filteredAlerts.get(position);

        holder.textListTitle.setText(currentItem.getTitle());
        holder.textListBody.setText(currentItem.getBody());
        holder.textListDate.setText(currentItem.getDate());
    }

    @Override
    public int getItemCount() {
        return filteredAlerts.size();
    }



}
