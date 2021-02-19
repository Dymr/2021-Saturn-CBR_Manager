package com.example.cbr_manager.ui.clientlist;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cbr_manager.R;
import com.example.cbr_manager.helper.Helper;
import com.example.cbr_manager.service.client.Client;

import java.util.List;

public class ClientListRecyclerItemAdapter extends RecyclerView.Adapter<ClientListRecyclerItemAdapter.ClientItemViewHolder> {

    private List<Client> clients;
    private OnItemListener onItemListener;

    public ClientListRecyclerItemAdapter(List<Client> clientList, OnItemListener onItemListener) {
        this.clients = clientList;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public ClientItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_item, parent, false);
        ClientItemViewHolder evh = new ClientItemViewHolder(v, onItemListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ClientItemViewHolder holder, int position) {
        Client currentClient = clients.get(position);
        Helper.setImageViewFromURL(currentClient.getPhotoURL(), holder.imageView);

        holder.textView1.setText(currentClient.getFullName());
        holder.textView2.setText(currentClient.getLocation());
        holder.riskTextView.setText(Integer.toString(currentClient.getRiskScore()));
        int intRiskScore = currentClient.getRiskScore();
        if (intRiskScore >= 10) {
            holder.riskTextView.setTextColor(Color.parseColor("#b02323"));
        } else if (intRiskScore < 10 && intRiskScore >= 5) {
            holder.riskTextView.setTextColor(Color.parseColor("#c45404"));
        } else {
            holder.riskTextView.setTextColor(Color.parseColor("#c49704"));
        }
    }

    @Override
    public int getItemCount() {
        return clients.size();
    }

    public interface OnItemListener {
        void onItemClick(int position);
    }

    public static class ClientItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageView;
        public TextView textView1;
        public TextView textView2;
        public TextView riskTextView;
        OnItemListener onItemListener;

        public ClientItemViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView1 = itemView.findViewById(R.id.textListTitle);
            textView2 = itemView.findViewById(R.id.textListBody);
            riskTextView = itemView.findViewById(R.id.textListDate);
            this.onItemListener = onItemListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onItemListener.onItemClick(getAdapterPosition());
        }
    }


}
