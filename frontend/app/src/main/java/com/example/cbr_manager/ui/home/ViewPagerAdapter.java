package com.example.cbr_manager.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;

import com.example.cbr_manager.R;
import com.example.cbr_manager.helper.Helper;
import com.example.cbr_manager.service.client.Client;
import com.example.cbr_manager.ui.clientdetails.ClientDetailsActivity;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    private final FragmentActivity activity;
    private List<Client> clients;
    private LayoutInflater layoutInflater;
    private Context context;

    public ViewPagerAdapter(Context context, FragmentActivity activity, List<Client> clients) {
        this.clients = clients;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return clients.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.card_item, container, false);

        Client client = clients.get(position);

        ImageView imageView = view.findViewById(R.id.imageClient);
        Helper.setImageViewFromURL(client.getPhotoURL(), imageView);

        TextView fullName = view.findViewById(R.id.textFullName);
        fullName.setText(client.getFullName());

        TextView location = view.findViewById(R.id.textLocation);
        location.setText(client.getLocation());

        TextView riskScore = view.findViewById(R.id.textScore);
        riskScore.setText(Integer.toString(client.getRiskScore()));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent clientInfoIntent = new Intent(context, ClientDetailsActivity.class);

                clientInfoIntent.putExtra("clientId", client.getId());

                activity.startActivity(clientInfoIntent);
            }
        });

        container.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
