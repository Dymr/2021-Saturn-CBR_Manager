package com.example.cbr_manager.ui.createreferral;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cbr_manager.R;


public class CreateReferralFragment extends Fragment {

    public CreateReferralFragment() {
        // Required empty public constructor
    }

    public static CreateReferralFragment newInstance(String param1, String param2) {
        CreateReferralFragment fragment = new CreateReferralFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_referral, container, false);
    }
}