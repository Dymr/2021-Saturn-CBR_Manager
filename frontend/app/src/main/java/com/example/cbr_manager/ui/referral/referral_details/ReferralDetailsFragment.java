package com.example.cbr_manager.ui.referral.referral_details;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cbr_manager.R;
import com.example.cbr_manager.service.APIService;
import com.example.cbr_manager.service.referral.Referral;
import com.example.cbr_manager.ui.ReferralViewModel;
import com.example.cbr_manager.utils.Helper;
import com.google.android.material.snackbar.Snackbar;

import java.sql.Ref;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.annotations.NonNull;
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
    private Referral myReferral;
    private View parentLayout;
    private Button resolveButton;
    private ReferralViewModel referralViewModel;
    private APIService apiService = APIService.getInstance();
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
        resolveButton = root.findViewById(R.id.referralDetailsResolveButton);
        getReferralInfo(referralId);

        this.referralId = referralId;

        resolveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupResolveConfirmation();
            }
        });

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
                if (referral.getStatus().equals("CREATED")) {
                    resolveButton.setVisibility(View.VISIBLE);
                }
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

    private void setupResolveConfirmation() {
        EditText editText = new EditText(this.getContext());
        editText.setHint("Outcome");
        LinearLayout container = new LinearLayout(this.getContext());
        container.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(getPixelValue(16),getPixelValue(16),getPixelValue(16),getPixelValue(16));
        editText.setLayoutParams(lp);
        container.addView(editText);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Confirm Resolve");
        alertDialogBuilder.setView(container);
        alertDialogBuilder.setMessage("Please confirm that this referral is ready to be resolved.");
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getReferral(editText.getText().toString());
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void getReferral(String outcome) {
        apiService.referralService.getReferral(referralId).enqueue(new Callback<Referral>() {
            @Override
            public void onResponse(Call<Referral> call, Response<Referral> response) {
                Referral referral = response.body();
                referral.setStatus("RESOLVED");

                referral.setOutcome(outcome);
                apiService.referralService.updateReferral(referral).enqueue(new Callback<Referral>() {
                    @Override
                    public void onResponse(Call<Referral> call, Response<Referral> response) {
                        if (response.isSuccessful()) {
                            TextView statusTextView = getView().findViewById(R.id.referralDetailsStatusTextView);
                            statusTextView.setText("RESOLVED");
                            TextView outcomeTextView = getView().findViewById(R.id.referralDetailsOutcomeTextView);
                            if (outcome.isEmpty()) {
                                outcomeTextView.setText("None");
                            } else {
                                outcomeTextView.setText(outcome);
                            }
                            resolveButton.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Update successful!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Referral> call, Throwable t) {
                    }
                });
            }
            @Override
            public void onFailure(Call<Referral> call, Throwable t) {

            }
        });
    }

    private int getPixelValue(int dp) {
        Resources resources = getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, resources.getDisplayMetrics());
    }


}