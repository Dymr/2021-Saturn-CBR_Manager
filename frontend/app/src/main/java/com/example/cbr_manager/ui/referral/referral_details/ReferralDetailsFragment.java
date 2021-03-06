package com.example.cbr_manager.ui.referral.referral_details;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.preference.PreferenceManager;
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
import com.example.cbr_manager.ui.referral.referral_details.ReferralDetailsEditFragment;
import com.example.cbr_manager.utils.Helper;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.material.snackbar.Snackbar;

import org.threeten.bp.format.FormatStyle;

import java.sql.Ref;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableCompletableObserver;
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
    private Referral localReferral;
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
        referralId = intent.getIntExtra("referralId", -1);
        resolveButton = root.findViewById(R.id.referralDetailsResolveButton);
        getReferralInfo(referralId);


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
        referralViewModel.getReferral(referralId).observe(getViewLifecycleOwner(), referral -> {
            localReferral = referral;
            setUpTextView(R.id.referralDetailsTypeTextView, referral.getServiceType());
            setUpTextView(R.id.referralDetailsReferToTextView, referral.getRefer_to());
            setUpTextView(R.id.referralDetailsStatusTextView, referral.getStatus());
            setUpTextView(R.id.referralDetailsOutcomeTextView, referral.getOutcome());
            setUpTextView(R.id.referralDetailsServiceDetailTextView, referral.getServiceDetail().getInfo(referral.getServiceType()));
            setUpTextView(R.id.referralDetailsDateCreatedTextView, Helper.formatDateTimeToLocalString(referral.getCreatedAt(), FormatStyle.SHORT));
            setUpTextView(R.id.referralDetailsClientTextView, referral.getFullName());
            if (referral.getStatus().equals("CREATED")) {
                resolveButton.setVisibility(View.VISIBLE);
                setupTapTarget();
            }
            setupImageViews(referral.getPhotoURL());
        });
    }

    private void setupTapTarget() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (!preferences.getBoolean("firstTimeResolve", false)) {
            TapTargetView.showFor(getActivity(),
                    TapTarget.forView(getView().findViewById(R.id.referralDetailsResolveButton), "Ready to resolve?", "If the client's referral has been resolved, tap the button to change the status to 'resolved' and to record an outcome.")
                            .outerCircleAlpha(0.96f)
                            .titleTextSize(20)
                            .drawShadow(true)
                            .transparentTarget(true)
                            .targetRadius(90)
                            .dimColor(R.color.black));

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("firstTimeResolve", true);
            editor.apply();
        }
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
                Bundle bundle = new Bundle();
                bundle.putInt("referralId", referralId);
                ReferralDetailsEditFragment referralDetailsEditFragment = new ReferralDetailsEditFragment();
                referralDetailsEditFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_referral_details, referralDetailsEditFragment, null)
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
        localReferral.setStatus("RESOLVED");
        localReferral.setOutcome(outcome);
        referralViewModel.modifyReferral(localReferral).subscribe(new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
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
            @Override
            public void onError(@NonNull Throwable e) {
            }
        });
    }

    private int getPixelValue(int dp) {
        Resources resources = getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, resources.getDisplayMetrics());
    }

}