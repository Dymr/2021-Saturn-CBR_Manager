package com.example.cbr_manager.ui.create_client;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.fragment.app.Fragment;

import com.example.cbr_manager.R;

public class DisabilityFragment extends Fragment {

    CheckBox[] checkBoxes = new CheckBox[10];
    String[] disability_list = {"Amputee", "Polio", "Spinal Cord Injury", "Cerebral Palsy", "Spina Bifada",
                                "Hydrocephalus", "Visual Impairment", "Hearing Impairment", "Don't Know", "Other"};
    String disabilities="";

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.activity_create_client_disability, container, false);
        String txt;

        for(int i=0 ; i<10 ; i++) {
            txt = "checkBox" + i;
            int resourceId = this.getResources().getIdentifier(txt, "id", getActivity().getPackageName());
            checkBoxes[i] = view.findViewById(resourceId);
        }

        Button nextButton = view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInfo(v);
                ((CreateClientActivity) getActivity()).setViewPager(CreateClientActivity.CreateClientFragments.CAREGIVER_INFO.ordinal());
            }
        });
        Button prevButton = view.findViewById(R.id.prevButton);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CreateClientActivity) getActivity()).setViewPager(CreateClientActivity.CreateClientFragments.PERSONAL_INFO.ordinal());
            }
        });

        return view;
    }

    public void updateInfo(View v) {
        disabilities = "";
        for(int i=0 ; i<10 ; i++) {
            if(checkBoxes[i].isChecked()) {
                disabilities = disabilities.concat(disability_list[i] + "/");
            }
        }
        ((CreateClientActivity) getActivity()).setDisabilityInfo(disabilities);
    }
}
