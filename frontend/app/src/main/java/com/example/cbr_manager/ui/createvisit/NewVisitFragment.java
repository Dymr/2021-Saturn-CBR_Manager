package com.example.cbr_manager.ui.createvisit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cbr_manager.R;
import com.example.cbr_manager.service.APIService;
import com.example.cbr_manager.service.client.Client;
import com.example.cbr_manager.service.user.User;
import com.example.cbr_manager.service.visit.Visit;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class NewVisitFragment extends Fragment {
    private int clientId = -1;
    private Integer userId = -1;
    private APIService apiService = APIService.getInstance();
    private Client client = new Client();

    public NewVisitFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_visit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        clientId = ((CreateVisitActivity) getActivity()).clientId;

        apiService.userService.getCurrentUser().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    userId = user.getId();
                } else {
                    Toast.makeText(getContext(), "User response error.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getContext(), "User failure error.", Toast.LENGTH_SHORT).show();
            }
        });

        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String todayString = format.format(today);

        EditText textView = (EditText) view.findViewById(R.id.fragmentPreambleEditTextDate);
        textView.setText(todayString);

        setupLocationSpinner(view);
        setupProvisionLayoutVisibility(view);

        setupVisibilityHealthProvisions(view);
        setupVisibilityEducationProvisions(view);
        setupVisibilitySocialProvisions(view);

        setupRadioGroupProvisionListeners(view);

        view.findViewById(R.id.preambleSubmitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gatherAllData(view);
                ((CreateVisitActivity) getActivity()).onBackPressed();
            }
        });
    }

    private void gatherAllData(View view) {
        if (apiService.isAuthenticated()) {
            apiService.clientService.getClient(clientId).enqueue(new Callback<Client>() {
                @Override
                public void onResponse(Call<Client> call, Response<Client> response) {
                    if (response.isSuccessful()) {
                        client = response.body();
                        fillClientWithVisitData(client, view);
                        Visit visit = new Visit("", clientId, userId, client);

                        Call<Visit> call1 = apiService.visitService.createVisit(visit);
                        call1.enqueue(new Callback<Visit>() {
                            @Override
                            public void onResponse(Call<Visit> call, Response<Visit> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(getContext(), "Visit creation successful!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Response error creating visit.", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Visit> call, Throwable t) {
                                Toast.makeText(getContext(), "Error in creating new visit.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "Response error finding client.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Client> call, Throwable t) {
                    Toast.makeText(getContext(), "Error finding client.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void fillClientWithVisitData(Client client, View view) {
        ChipGroup purposeChipGroup = view.findViewById(R.id.purposeChipGroup);
        purposeChipGroup.getCheckedChipIds();

        Chip cbrChip = view.findViewById(R.id.cbrChip);
        boolean isCBR = cbrChip.isChecked();

        Chip disabilityReferralChip = view.findViewById(R.id.purposeReferralChip);
        boolean isDisabilityReferral = disabilityReferralChip.isChecked();

        Chip disabilityFollowupChip = view.findViewById(R.id.purposeFollowUpChip);
        boolean isDisabilityFollowUp = disabilityFollowupChip.isChecked();

        Chip healthProvisionChip = view.findViewById(R.id.healthProvisionChip);
        boolean isHealthProvision = healthProvisionChip.isChecked();

        Chip educationProvisionChip = view.findViewById(R.id.educationProvisionChip);
        boolean isEducationProvision = educationProvisionChip.isChecked();

        Chip socialProvisionChip = view.findViewById(R.id.socialProvisionChip);
        boolean isSocialProvision = socialProvisionChip.isChecked();

        EditText cbrWorkerName = view.findViewById(R.id.fragmentPreambleCBRNameEditText);
        String name = cbrWorkerName.getText().toString();

        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String todayString = format.format(today);

        // Location
        EditText locationOfVisit = view.findViewById(R.id.editTextTextLocation);
        String locationVisit = locationOfVisit.getText().toString();

        Spinner locationSpinner = view.findViewById(R.id.locationFragmentSpinner);
        String locationDDL = locationSpinner.getSelectedItem().toString();

        EditText villageNumber = view.findViewById(R.id.villageNumberEditText);
        String villageNumberString = villageNumber.getText().toString();

        // Health provision
        Chip wheelchairHealth = view.findViewById(R.id.wheelchairChip);
        boolean isWheelChairHealth = wheelchairHealth.isChecked();
        EditText wheelchairEditText = view.findViewById(R.id.wheelchairTextMultiLine);
        String wheelchairDescription = wheelchairEditText.getText().toString();

        Chip prostheticHealth = view.findViewById(R.id.prostheticChip);
        boolean isProtheticHealth = prostheticHealth.isChecked();
        EditText protheticEditText = view.findViewById(R.id.protheticTextMultiLine);
        String protheticDescription = protheticEditText.getText().toString();

        Chip ortheticHealth = view.findViewById(R.id.orthoticChip);
        boolean isOrtheticHealth = ortheticHealth.isChecked();
        EditText ortheticEditText = view.findViewById(R.id.orthoticTextMultiLine);
        String ortheticDescription = ortheticEditText.getText().toString();

        Chip repairsHealth = view.findViewById(R.id.wheelchairRepairsChip);
        boolean isRepairHealth = repairsHealth.isChecked();
        EditText repairEditText = view.findViewById(R.id.wheelchairRepairsTextMultiLine);
        String repairDescription = repairEditText.getText().toString();

        Chip referralHealth = view.findViewById(R.id.referralChip);
        boolean isReferralHealth = referralHealth.isChecked();
        EditText referralEditText = view.findViewById(R.id.referralTextMultiLine5);
        String referralDescription = referralEditText.getText().toString();

        Chip adviceHealth = view.findViewById(R.id.adviceChip);
        boolean isAdviceHealth = adviceHealth.isChecked();
        EditText adviceEditText = view.findViewById(R.id.adviceTextMultiLine);
        String adviceDescription = adviceEditText.getText().toString();

        Chip advocacyHealth = view.findViewById(R.id.advocacyChip);
        boolean isAdvocacyHealth = advocacyHealth.isChecked();
        EditText advocacyEditText = view.findViewById(R.id.advocacyTextMultiLine);
        String advocacyDescription = advocacyEditText.getText().toString();

        Chip encouragementHealth = view.findViewById(R.id.encouragementChip);
        boolean isEncouragementHealth = encouragementHealth.isChecked();
        EditText encouragementEditText = view.findViewById(R.id.encouragementTextMultiLine);
        String encouragementDescription = encouragementEditText.getText().toString();

        RadioGroup healthGoalMet = view.findViewById(R.id.healthProvisionsRadioGroup);
        int id = healthGoalMet.getCheckedRadioButtonId();
        String healthGoalText = "";
        if (id != -1) {
            RadioButton selectedHealthGoal = (RadioButton) view.findViewById(id);
            healthGoalText = selectedHealthGoal.getText().toString();
        }

        EditText healthConclusionEditText = view.findViewById(R.id.healthProvisionConclusionTextMultiLine);
        String healthConclusionDescription = healthConclusionEditText.getText().toString();

        // Education
        Chip educationAdviceChip = view.findViewById(R.id.educationProvisionsAdviceChip);
        boolean isEducationAdvice = educationAdviceChip.isChecked();
        EditText educationAdviceText = view.findViewById(R.id.educationProvisionAdviceTextMultiLine);
        String educationAdviceDescription = educationAdviceText.getText().toString();

        Chip educationAdvocacyChip = view.findViewById(R.id.educationProvisionsAdvocacyChip);
        boolean isEducationAdvocacy = educationAdvocacyChip.isChecked();
        EditText educationAdvocacyEditText = view.findViewById(R.id.educationProvisionAdvocacyTextMultiLine);
        String educationAdvocacyDescription = educationAdvocacyEditText.getText().toString();

        Chip educationReferralChip = view.findViewById(R.id.educationProvisionsReferralChip);
        boolean isEducationReferral = educationReferralChip.isChecked();
        EditText educationReferralEditText = view.findViewById(R.id.educationProvisionReferralTextMultiLine);
        String educationReferralDescription = educationReferralEditText.getText().toString();

        Chip educationEncouragementChip = view.findViewById(R.id.educationProvisionsEncouragementChip);
        boolean isEducationEncouragement = educationEncouragementChip.isChecked();
        EditText educationEncouragementEditText = view.findViewById(R.id.encouragementTextMultiLine);
        String educationEncouragementDescription = educationEncouragementEditText.getText().toString();

        RadioGroup educationGoalMet = view.findViewById(R.id.educationProvisionRadioGroup);
        int idEducationRadio = educationGoalMet.getCheckedRadioButtonId();
        String educationGoalText = "";
        if (idEducationRadio != -1) {
            RadioButton selectedEducationGoal = (RadioButton) view.findViewById(idEducationRadio);
            educationGoalText = selectedEducationGoal.getText().toString();
        }
        EditText educationConclusionEditText = view.findViewById(R.id.educationProvisionConclusionTextMultiLine);
        String educationConclusion = educationConclusionEditText.getText().toString();

        // Social
        Chip socialAdviceChip = view.findViewById(R.id.socialProvisionAdviceChip);
        boolean isSocialAdvice = socialAdviceChip.isChecked();
        EditText socialAdviceEditText = view.findViewById(R.id.socialProvisionAdviceTextMultiLine);
        String socialAdviceDescription = socialAdviceEditText.getText().toString();

        Chip socialAdvocacyChip = view.findViewById(R.id.socialProvisionAdvocacyChip);
        boolean isSocialAdvocacy = socialAdvocacyChip.isChecked();
        EditText socialAdvocacyEditText = view.findViewById(R.id.socialProvisionAdvocacyMultiLineText);
        String socialAdvocacyDescription = socialAdvocacyEditText.getText().toString();

        Chip socialReferralChip = view.findViewById(R.id.socialProvisionReferralChip);
        boolean isSocialReferral = socialReferralChip.isChecked();
        EditText socialReferralEditText = view.findViewById(R.id.socialProvisionReferralTextMultiLine);
        String socialReferralDescription = socialReferralEditText.getText().toString();

        Chip socialEncouragementChip = view.findViewById(R.id.socialProvisionEncouragementChip);
        boolean isSocialEncouragement = socialEncouragementChip.isChecked();
        EditText socialEncouragementEditText = view.findViewById(R.id.socialProvisionEncouragementTextMultiLine);
        String socialEncouragementDescription = socialEncouragementEditText.getText().toString();

        RadioGroup socialGoalMet = view.findViewById(R.id.socialProvisionRadioGroup);
        int idSocialRadio = socialGoalMet.getCheckedRadioButtonId();
        String socialGoalText = "";
        if (idSocialRadio != -1) {
            RadioButton selectedSocialGoal = (RadioButton) view.findViewById(idSocialRadio);
            socialGoalText = selectedSocialGoal.getText().toString();
        }
        EditText socialConclusionEditText = view.findViewById(R.id.socialProvisionConclusionTextMultiLine);
        String socialConclusion = socialConclusionEditText.getText().toString();

        // Setting Preamble
        client.setCBRPurpose(isCBR);
        client.setDisabilityFollowUpPurpose(isDisabilityFollowUp);
        client.setDisabilityReferralPurpose(isDisabilityReferral);

        client.setHealthProvision(isHealthProvision);
        client.setEducationProvision(isEducationProvision);
        client.setSocialProvision(isSocialProvision);

        client.setCbrWorkerName(name);

        // Setting locations
        client.setLocationVisitGPS(locationVisit);
        client.setLocationDropDown(locationDDL);
        if (!villageNumberString.isEmpty()) {
            client.setVillageNoVisit(Integer.parseInt(villageNumberString));
        }
        // Health provision
        client.setWheelchairHealthProvision(isWheelChairHealth);
        client.setProstheticHealthProvision(isProtheticHealth);
        client.setOrthoticHealthProvision(isOrtheticHealth);
        client.setRepairsHealthProvision(isRepairHealth);
        client.setReferralHealthProvision(isReferralHealth);
        client.setAdviceHealthProvision(isAdviceHealth);
        client.setAdvocacyHealthProvision(isAdvocacyHealth);
        client.setEncouragementHealthProvision(isEncouragementHealth);

        client.setWheelchairHealthProvisionText(wheelchairDescription);
        client.setProstheticHealthProvisionText(protheticDescription);
        client.setOrthoticHealthProvisionText(ortheticDescription);
        client.setRepairsHealthProvisionText(repairDescription);
        client.setReferralHealthProvisionText(referralDescription);
        client.setAdviceHealthProvisionText(adviceDescription);
        client.setAdvocacyHealthProvisionText(advocacyDescription);
        client.setEncouragementHealthProvisionText(encouragementDescription);

        client.setGoalMetHealthProvision(healthGoalText);
        client.setConclusionHealthProvision(healthConclusionDescription);

        // Education provision
        client.setAdviceEducationProvision(isEducationAdvice);
        client.setAdvocacyEducationProvision(isEducationAdvocacy);
        client.setReferralEducationProvision(isEducationReferral);
        client.setEncouragementEducationProvision(isEducationEncouragement);

        client.setAdviceEducationProvisionText(educationAdviceDescription);
        client.setAdvocacyEducationProvisionText(educationAdvocacyDescription);
        client.setReferralEducationProvisionText(educationReferralDescription);
        client.setEncouragementEducationProvisionText(educationEncouragementDescription);
        client.setGoalMetEducationProvision(educationGoalText);
        client.setConclusionEducationProvision(educationConclusion);

        // Social provision
        client.setAdviceSocialProvision(isSocialAdvice);
        client.setAdvocacySocialProvision(isSocialAdvocacy);
        client.setReferralSocialProvision(isSocialReferral);
        client.setEncouragementSocialProvision(isSocialEncouragement);

        client.setAdviceSocialProvisionText(socialAdviceDescription);
        client.setAdvocacyHealthProvisionText(socialAdvocacyDescription);
        client.setReferralSocialProvisionText(socialReferralDescription);
        client.setEncouragementSocialProvisionText(socialEncouragementDescription);

        client.setGoalMetSocialProvision(socialGoalText);
        client.setConclusionEducationProvision(socialConclusion);
    }

    private void setupLocationSpinner(View view) {
        Spinner spinner = (Spinner) view.findViewById(R.id.locationFragmentSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.locationsArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void setupProvisionLayoutVisibility(View view) {

        ChipGroup cbrChipGroup = view.findViewById(R.id.cbrTypeChipGroup);
        TextView cbrTypeTextView = view.findViewById(R.id.cbrTypeTextView);
        cbrChipGroup.setVisibility(GONE);
        cbrTypeTextView.setVisibility(GONE);
        LinearLayout healthProvisionLayout = view.findViewById(R.id.healthProvisionsLayout);
        LinearLayout educationProvisionLayout = view.findViewById(R.id.educationProvisionsLayout);
        LinearLayout socialProvisionLayout = view.findViewById(R.id.socialProvisionsLayout);

        Chip healthProvisionChip = view.findViewById(R.id.healthProvisionChip);
        Chip educationProvisionChip = view.findViewById(R.id.educationProvisionChip);
        Chip socialProvisionChip = view.findViewById(R.id.socialProvisionChip);

        Chip cbrChip = view.findViewById(R.id.cbrChip);
        cbrChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbrChip.isChecked()) {
                    healthProvisionLayout.setVisibility(GONE);
                    educationProvisionLayout.setVisibility(GONE);
                    socialProvisionLayout.setVisibility(GONE);
                    cbrChipGroup.setVisibility(View.VISIBLE);
                    cbrTypeTextView.setVisibility(View.VISIBLE);

                    setupProvisionLayoutChipListeners(healthProvisionChip, healthProvisionLayout);
                    setupProvisionLayoutChipListeners(educationProvisionChip, educationProvisionLayout);
                    setupProvisionLayoutChipListeners(socialProvisionChip, socialProvisionLayout);
                } else {
                    healthProvisionLayout.setVisibility(View.VISIBLE);
                    educationProvisionLayout.setVisibility(View.VISIBLE);
                    socialProvisionLayout.setVisibility(View.VISIBLE);
                    cbrChipGroup.setVisibility(GONE);
                    cbrTypeTextView.setVisibility(GONE);

                    healthProvisionChip.setChecked(false);
                    educationProvisionChip.setChecked(false);
                    socialProvisionChip.setChecked(false);
                }
            }

            private void setupProvisionLayoutChipListeners(Chip chip, LinearLayout linearLayout) {
                chip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (chip.isChecked()) {
                            linearLayout.setVisibility(View.VISIBLE);
                        } else {
                            linearLayout.setVisibility(GONE);
                        }
                    }
                });
            }
        });
    }

    private void setupVisibilityHealthProvisions(View view) {
        EditText wheelchairMT = view.findViewById(R.id.wheelchairTextMultiLine);
        Chip wheelchairChip = view.findViewById(R.id.wheelchairChip);
        TextView wheelchairTextView = view.findViewById(R.id.healthProvisionsWheelChairTextView);
        setChipListener(wheelchairChip, wheelchairMT, wheelchairTextView);

        EditText prostheticMultiTextView = view.findViewById(R.id.protheticTextMultiLine);
        Chip c2 = view.findViewById(R.id.prostheticChip);
        TextView protheticTextView = view.findViewById(R.id.protheticTextView);
        setChipListener(c2, prostheticMultiTextView, protheticTextView);

        EditText t3 = view.findViewById(R.id.orthoticTextMultiLine);
        Chip c3 = view.findViewById(R.id.orthoticChip);
        TextView orthoticTextView = view.findViewById(R.id.orthoticTextView);
        setChipListener(c3, t3, orthoticTextView);

        EditText t4 = view.findViewById(R.id.wheelchairRepairsTextMultiLine);
        Chip c4 = view.findViewById(R.id.wheelchairRepairsChip);
        TextView wheelchairRepairsTextView = view.findViewById(R.id.wheelchairRepairsTextView);
        setChipListener(c4, t4, wheelchairRepairsTextView);

        EditText t5 = view.findViewById(R.id.referralTextMultiLine5);
        Chip c5 = view.findViewById(R.id.referralChip);
        TextView referralTextView = view.findViewById(R.id.referralTextView);
        setChipListener(c5, t5, referralTextView);

        EditText t6 = view.findViewById(R.id.adviceTextMultiLine);
        Chip c6 = view.findViewById(R.id.adviceChip);
        TextView adviceTextView = view.findViewById(R.id.adviceTextView);
        setChipListener(c6, t6, adviceTextView);

        EditText t7 = view.findViewById(R.id.advocacyTextMultiLine);
        Chip c7 = view.findViewById(R.id.advocacyChip);
        TextView advocacyTextView = view.findViewById(R.id.advocacyTextView);
        setChipListener(c7, t7, advocacyTextView);

        EditText t8 = view.findViewById(R.id.encouragementTextMultiLine);
        Chip c8 = view.findViewById(R.id.encouragementChip);
        TextView encouragementTextView = view.findViewById(R.id.encouragementTextView);
        setChipListener(c8, t8, encouragementTextView);
    }

    private void setupVisibilityEducationProvisions(View view) {
        Chip adviceChip = view.findViewById(R.id.educationProvisionsAdviceChip);
        TextView adviceTV = view.findViewById(R.id.educationProvisionAdviceTextView);
        EditText adviceET = view.findViewById(R.id.educationProvisionAdviceTextMultiLine);
        setChipListener(adviceChip,adviceET,adviceTV);

        Chip advocacyChip = view.findViewById(R.id.educationProvisionsAdvocacyChip);
        TextView advocacyTV = view.findViewById(R.id.educationProvisionAdvocacyTextView);
        EditText advocacyET = view.findViewById(R.id.educationProvisionAdvocacyTextMultiLine);
        setChipListener(advocacyChip,advocacyET,advocacyTV);

        Chip referralChip = view.findViewById(R.id.educationProvisionsReferralChip);
        TextView referralTV = view.findViewById(R.id.educationProvisionReferralTextView);
        EditText referralET = view.findViewById(R.id.educationProvisionReferralTextMultiLine);
        setChipListener(referralChip,referralET,referralTV);

        Chip encourageChip = view.findViewById(R.id.educationProvisionsEncouragementChip);
        TextView encourageTV = view.findViewById(R.id.educationProvisionEncouragementTextView);
        EditText encourageET = view.findViewById(R.id.educationProvisionEncouragementTextMultiLine);
        setChipListener(encourageChip,encourageET,encourageTV);
    }

    private void setupVisibilitySocialProvisions(View view) {
        Chip adviceChip = view.findViewById(R.id.socialProvisionAdviceChip);
        TextView adviceTV = view.findViewById(R.id.socialProvisionAdviceTextView);
        EditText adviceET = view.findViewById(R.id.socialProvisionAdviceTextMultiLine);
        setChipListener(adviceChip,adviceET,adviceTV);

        Chip advocacyChip = view.findViewById(R.id.socialProvisionAdvocacyChip);
        TextView advocacyTV = view.findViewById(R.id.socialProvisionAdvocacyTextView);
        EditText advocacyET = view.findViewById(R.id.socialProvisionAdvocacyMultiLineText);
        setChipListener(advocacyChip,advocacyET,advocacyTV);

        Chip referralChip = view.findViewById(R.id.socialProvisionReferralChip);
        TextView referralTV = view.findViewById(R.id.socialProvisionReferralTextView);
        EditText referralET = view.findViewById(R.id.socialProvisionReferralTextMultiLine);
        setChipListener(referralChip,referralET,referralTV);

        Chip encourageChip = view.findViewById(R.id.socialProvisionEncouragementChip);
        TextView encourageTV = view.findViewById(R.id.socialProvisionEncouragementTextView);
        EditText encourageET = view.findViewById(R.id.socialProvisionEncouragementTextMultiLine);
        setChipListener(encourageChip,encourageET,encourageTV);
    }

    private void setChipListener(Chip chip, EditText editText, TextView textView) {
        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chip.isChecked()) {
                    editText.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                } else {
                    editText.setVisibility(GONE);
                    textView.setVisibility(GONE);
                }
            }
        });
    }

    private void setupRadioGroupProvisionListeners(View view) {
        RadioGroup healthRadioGroup = view.findViewById(R.id.healthProvisionsRadioGroup);
        RadioGroup educationRadioGroup = view.findViewById(R.id.educationProvisionRadioGroup);
        RadioGroup socialRadioGroup = view.findViewById(R.id.socialProvisionRadioGroup);

        healthRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.concludedHPRadioButton) {
                    view.findViewById(R.id.healthProvisionConclusionTextView).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.healthProvisionConclusionTextMultiLine).setVisibility(View.VISIBLE);
                } else {
                    view.findViewById(R.id.healthProvisionConclusionTextView).setVisibility(GONE);
                    view.findViewById(R.id.healthProvisionConclusionTextMultiLine).setVisibility(GONE);
                }
            }
        });

        educationRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.educationProvisionConcludedRadioButton) {
                    view.findViewById(R.id.educationProvisionConclusionTextView).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.educationProvisionConclusionTextMultiLine).setVisibility(View.VISIBLE);
                } else {
                    view.findViewById(R.id.educationProvisionConclusionTextView).setVisibility(GONE);
                    view.findViewById(R.id.educationProvisionConclusionTextMultiLine).setVisibility(GONE);
                }
            }
        });

        socialRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.socialProvisionConcludedRadioButton) {
                    view.findViewById(R.id.socialProvisionConclusionTextView).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.socialProvisionConclusionTextMultiLine).setVisibility(View.VISIBLE);
                } else {
                    view.findViewById(R.id.socialProvisionConclusionTextView).setVisibility(GONE);
                    view.findViewById(R.id.socialProvisionConclusionTextMultiLine).setVisibility(GONE);
                }
            }
        });
    }
}