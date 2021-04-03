package com.example.cbr_manager.ui.visitdetails;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cbr_manager.R;
import com.example.cbr_manager.service.client.Client;
import com.example.cbr_manager.service.visit.Visit;
import com.example.cbr_manager.ui.VisitViewModel;
import com.example.cbr_manager.utils.Helper;
import com.google.android.material.snackbar.Snackbar;

import org.threeten.bp.format.FormatStyle;

import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.observers.DisposableSingleObserver;

@AndroidEntryPoint
public class VisitDetailsFragment extends Fragment {

    public static String KEY_VISIT_ID = "KEY_VISIT_ID";
    private int visitId;
    private VisitViewModel visitViewModel;

    public static VisitDetailsFragment newInstance(String param1, String param2) {
        VisitDetailsFragment fragment = new VisitDetailsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static VisitDetailsFragment newInstance(int visitId) {
        VisitDetailsFragment fragment = new VisitDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_VISIT_ID, visitId);
        fragment.setArguments(args);
        return fragment;
    }

    public VisitDetailsFragment() {
        super(R.layout.fragment_visit_details);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        visitViewModel = new ViewModelProvider(this).get(VisitViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpToolBar();

        visitId = requireArguments().getInt(KEY_VISIT_ID, -1);

        getVisitInfo(visitId);
    }

    public void setUpToolBar() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.client_details, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.edit_client) {
            startVisitDetailsEditFragment();
        }
        return super.onOptionsItemSelected(item);
    }

    private void startVisitDetailsEditFragment() {
        Bundle bundle = new Bundle();
        bundle.putInt("visitId", visitId);
        VisitDetailsEditFragment visitDetailsEditFragment = new VisitDetailsEditFragment();
        visitDetailsEditFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_visit_details, visitDetailsEditFragment, null)
                .addToBackStack(null)
                .commit();
    }

    private void getVisitInfo(int visitId) {
        visitViewModel.getVisit(visitId).subscribe(new DisposableSingleObserver<Visit>() {
            @Override
            public void onSuccess(@io.reactivex.annotations.NonNull Visit visit) {
                String datetimeCreated = visit.getCreatedAt();
                String formattedDate = Helper.formatDateTimeToLocalString(datetimeCreated, FormatStyle.SHORT);

                setupDateTextView(formattedDate);

                Client client = visit.getClient();
                setupNameTextView(client.getFullName());
                setupImageViews(client.getPhotoURL());

                setupLocationTextView(visit.getLocationDropDown());
                setupVillageNumTextView(visit.getVillageNoVisit().toString());
                setupHealthTextViews(visit);
                setupEducationTextViews(visit);
                setupSocialTextViews(visit);
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                Snackbar.make(getView(), "Failed to get the client. Please try again", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    private void setUpTextView(int textViewID, String textValue) {
        TextView textView = (TextView) getView().findViewById(textViewID);
        textView.setText(textValue);
    }

    private void setupImageViews(String imageURL) {
        ImageView displayPicture = (ImageView) getView().findViewById(R.id.visitDetailsDisplayPictureImageView);
        Helper.setImageViewFromURL(imageURL, displayPicture, R.drawable.client_details_placeholder);
    }

    private void setupNameTextView(String fullName) {
        setUpTextView(R.id.visitDetailsNameTextView, fullName);
    }

    private void setupLocationTextView(String location) {
        setUpTextView(R.id.visitDetailsLocationTextView, location);
    }

    private void setupDateTextView(String date) {
        setUpTextView(R.id.visitDetailsDateTextView, date);
    }

    private void setupVillageNumTextView(String villageNum) {
        setUpTextView(R.id.visitDetailsVillageNumTextView, villageNum);
    }

    private void setupHealthTextViews(Visit visit) {
        boolean notEmpty;

        setUpTextView(R.id.visitDetailsWheelchairHealthTextView, visit.getWheelchairHealthProvisionText());
        notEmpty = setVisibility(R.id.visitDetailsWheelchairHealthLinearLayout, visit.getWheelchairHealthProvisionText(), false);

        setUpTextView(R.id.visitDetailsProstheticHealthTextView, visit.getProstheticHealthProvisionText());
        notEmpty = setVisibility(R.id.visitDetailsProstheticHealthLinearLayout, visit.getProstheticHealthProvisionText(), notEmpty);

        setUpTextView(R.id.visitDetailsOrthoticHealthTextView, visit.getOrthoticHealthProvisionText());
        notEmpty = setVisibility(R.id.visitDetailsOrthoticHealthLinearLayout, visit.getOrthoticHealthProvisionText(), notEmpty);

        setUpTextView(R.id.visitDetailsRepairsHealthTextView, visit.getRepairsHealthProvisionText());
        notEmpty = setVisibility(R.id.visitDetailsRepairsHealthLinearLayout, visit.getRepairsHealthProvisionText(), notEmpty);

        setUpTextView(R.id.visitDetailsReferralHealthTextView, visit.getReferralHealthProvisionText());
        notEmpty = setVisibility(R.id.visitDetailsReferralHealthLinearLayout, visit.getReferralHealthProvisionText(), notEmpty);

        setUpTextView(R.id.visitDetailsAdviceHealthTextView, visit.getAdviceHealthProvisionText());
        notEmpty = setVisibility(R.id.visitDetailsAdviceHealthLinearLayout, visit.getAdviceHealthProvisionText(), notEmpty);

        setUpTextView(R.id.visitDetailsAdvocacyHealthTextView, visit.getAdvocacyHealthProvisionText());
        notEmpty = setVisibility(R.id.visitDetailsAdvocacyHealthLinearLayout, visit.getAdvocacyHealthProvisionText(), notEmpty);

        setUpTextView(R.id.visitDetailsEncouragementHealthTextView, visit.getEncouragementHealthProvisionText());
        notEmpty = setVisibility(R.id.visitDetailsEncouragementHealthLinearLayout, visit.getEncouragementHealthProvisionText(), notEmpty);

        setUpTextView(R.id.visitDetailsConclusionHealthTextView, visit.getConclusionHealthProvision());
        notEmpty = setVisibility(R.id.visitDetailsConclusionHealthLinearLayout, visit.getConclusionHealthProvision(), notEmpty);

        setCardVisibility(R.id.healthDetailsCard, notEmpty);
    }

    private void setupEducationTextViews(Visit visit) {
        boolean notEmpty;

        setUpTextView(R.id.visitDetailsReferralEducationTextView, visit.getReferralEducationProvisionText());
        notEmpty = setVisibility(R.id.visitDetailsReferralEducationLinearLayout, visit.getReferralEducationProvisionText(), false);

        setUpTextView(R.id.visitDetailsAdviceEducationTextView, visit.getAdviceEducationProvisionText());
        notEmpty = setVisibility(R.id.visitDetailsAdviceEducationLinearLayout, visit.getAdviceEducationProvisionText(), notEmpty);

        setUpTextView(R.id.visitDetailsAdvocacyEducationTextView, visit.getAdvocacyEducationProvisionText());
        notEmpty = setVisibility(R.id.visitDetailsAdvocacyEducationLinearLayout, visit.getAdvocacyEducationProvisionText(), notEmpty);

        setUpTextView(R.id.visitDetailsEncouragementEducationTextView, visit.getEncouragementEducationProvisionText());
        notEmpty = setVisibility(R.id.visitDetailsEncouragementEducationLinearLayout, visit.getEncouragementEducationProvisionText(), notEmpty);

        setUpTextView(R.id.visitDetailsConclusionEducationTextView, visit.getConclusionEducationProvision());
        notEmpty = setVisibility(R.id.visitDetailsConclusionEducationLinearLayout, visit.getConclusionEducationProvision(), notEmpty);

        setCardVisibility(R.id.educationDetailsCard, notEmpty);
    }

    private void setupSocialTextViews(Visit visit) {
        boolean notEmpty;

        setUpTextView(R.id.visitDetailsReferralSocialTextView, visit.getReferralSocialProvisionText());
        notEmpty = setVisibility(R.id.visitDetailsReferralSocialLinearLayout, visit.getReferralSocialProvisionText(), false);

        setUpTextView(R.id.visitDetailsAdviceSocialTextView, visit.getAdviceSocialProvisionText());
        notEmpty = setVisibility(R.id.visitDetailsAdviceSocialLinearLayout, visit.getAdviceSocialProvisionText(), notEmpty);

        setUpTextView(R.id.visitDetailsAdvocacySocialTextView, visit.getAdvocacySocialProvisionText());
        notEmpty = setVisibility(R.id.visitDetailsAdvocacySocialLinearLayout, visit.getAdvocacySocialProvisionText(), notEmpty);

        setUpTextView(R.id.visitDetailsEncouragementSocialTextView, visit.getEncouragementSocialProvisionText());
        notEmpty = setVisibility(R.id.visitDetailsEncouragementSocialLinearLayout, visit.getEncouragementSocialProvisionText(), notEmpty);

        setUpTextView(R.id.visitDetailsConclusionSocialTextView, visit.getConclusionSocialProvision());
        notEmpty = setVisibility(R.id.visitDetailsConclusionSocialLinearLayout, visit.getConclusionSocialProvision(), notEmpty);

        setCardVisibility(R.id.socialDetailsCard, notEmpty);
    }

    private void setCardVisibility(int id, boolean notEmpty) {
        View view = getView();
        CardView card = (CardView) view.findViewById(id);
        if(notEmpty) {
            card.setVisibility(View.VISIBLE);
        } else{
            card.setVisibility(View.GONE);
        }
    }

    private boolean setVisibility(int id, String data, boolean notEmpty) {
        View view = getView();
        LinearLayout layout = view.findViewById(id);
        if(data.length()==0) {
            layout.setVisibility(View.GONE);
            return (notEmpty || false);
        } else{
            layout.setVisibility(View.VISIBLE);
            return (notEmpty || true);
        }
    }
}
