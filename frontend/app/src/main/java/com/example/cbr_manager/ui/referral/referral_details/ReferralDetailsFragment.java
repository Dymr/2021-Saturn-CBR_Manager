package com.example.cbr_manager.ui.referral.referral_details;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cbr_manager.R;
import com.example.cbr_manager.service.APIService;
import com.example.cbr_manager.service.client.Client;
import com.example.cbr_manager.service.referral.Referral;
import com.example.cbr_manager.service.referral.ServiceDetails.PhysiotherapyServiceDetail;
import com.example.cbr_manager.ui.ReferralViewModel;
import com.example.cbr_manager.ui.clientdetails.ClientDetailsEditFragment;
import com.example.cbr_manager.ui.createreferral.CreateReferralActivity;
import com.example.cbr_manager.ui.createvisit.CreateVisitActivity;
import com.example.cbr_manager.ui.referral.referral_list.ReferralListFragment;
import com.example.cbr_manager.ui.visits.VisitsFragment;
import com.example.cbr_manager.utils.Helper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
@AndroidEntryPoint
public class ReferralDetailsFragment extends Fragment {

    private int referralId = -1;
    private View parentLayout;
    private ReferralViewModel referralViewModel;
    private static final String TAG = "ReferralDetailsFragment";
    public ReferralDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        referralViewModel = new ViewModelProvider(this).get(ReferralViewModel.class);

        View root = inflater.inflate(R.layout.fragment_referral_details, container, false);

        parentLayout = root.findViewById(android.R.id.content);

        Intent intent = getActivity().getIntent();
        int referralId = intent.getIntExtra("referralId", -1);
        getReferralInfo(referralId);

        this.referralId = referralId;

        setupButtons(root);

        return root;
    }

    private void getReferralInfo(int referralId) {
        referralViewModel.getReferral(referralId).subscribe(new DisposableSingleObserver<Referral>() {
            @Override
            public void onSuccess(@NonNull Referral referral) {
                setUpTextView(R.id.referralDetailsTypeTextView, referral.getServiceType());
                setUpTextView(R.id.referralDetailsReferToTextView, referral.getRefer_to());
                setUpTextView(R.id.referralDetailsStatusTextView, referral.getStatus());
                setUpTextView(R.id.referralDetailsOutcomeTextView, referral.getOutcome());
                setUpTextView(R.id.referralDetailsServiceDetailTextView, referral.getServiceDetail().getInfo());
                setUpTextView(R.id.referralDetailsDateCreatedTextView, referral.getFormattedDate());
                setUpTextView(R.id.referralDetailsClientTextView, referral.getFullName());
                setupImageViews(referral.getPhotoURL());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Snackbar.make(parentLayout, "Failed to get the referral. Please try again", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setUpTextView(int textViewId, String text) {
        TextView textView = (TextView)getView().findViewById(textViewId);
        if(text.equals("")){
            text="None";
        }
        textView.setText(text);
    }

    private void setupImageViews(String imageURL) {
        ImageView displayPicture = (ImageView)getView().findViewById(R.id.referralDetailsDisplayPictureImageView);
        Helper.setImageViewFromURL(imageURL, displayPicture, R.drawable.client_details_placeholder);
    }

    private void setupButtons(View root) {
        setupBackButton(root);
        setupEditButton(root);
    }

    private void setupEditButton(View root) {

        ImageView editButtonImageView = root.findViewById(R.id.referralDetailsEditImageView);

        editButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_referral_details, ReferralDetailsEditFragment.class, null)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void setupBackButton(View root) {
        ImageView backImageView = root.findViewById(R.id.referralDetailsBackImageView);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    public int getReferralId() {
        return referralId;
    }



}