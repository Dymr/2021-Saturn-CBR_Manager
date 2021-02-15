package com.example.cbr_manager.ui.visits;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cbr_manager.R;

import java.util.ArrayList;

public class VisitsRecyclerItemAdapter extends RecyclerView.Adapter<VisitsRecyclerItemAdapter.VisitItemViewHolder> {

    private ArrayList<VisitsRecyclerItem> visitsRecyclerItems;
    private OnItemListener onItemListener;

    public static class VisitItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageView;
        public TextView textView1;
        public TextView textView2;
        OnItemListener onItemListener;

        public VisitItemViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView1 = itemView.findViewById(R.id.textView6);
            textView2 = itemView.findViewById(R.id.textView7);
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

    public VisitsRecyclerItemAdapter(ArrayList<VisitsRecyclerItem> visitsRecyclerItems, OnItemListener onItemListener) {
        this.visitsRecyclerItems = visitsRecyclerItems;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public VisitItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.visit_item, parent, false);
        VisitItemViewHolder evh = new VisitItemViewHolder(v, onItemListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull VisitItemViewHolder holder, int position) {
        VisitsRecyclerItem currentItem = visitsRecyclerItems.get(position);

        holder.imageView.setImageResource(currentItem.getmImageResource());
        holder.textView1.setText(currentItem.getmText1());
        holder.textView2.setText(currentItem.getmText2());
    }

    @Override
    public int getItemCount() {
        return visitsRecyclerItems.size();
    }
}
