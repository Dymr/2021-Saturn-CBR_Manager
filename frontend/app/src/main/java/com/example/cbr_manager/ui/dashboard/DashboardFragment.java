package com.example.cbr_manager.ui.dashboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager.widget.ViewPager;

import com.example.cbr_manager.R;
import com.example.cbr_manager.service.APIService;
import com.example.cbr_manager.service.alert.Alert;
import com.example.cbr_manager.service.client.Client;
import com.example.cbr_manager.service.client.ClientRiskScoreComparator;
import com.example.cbr_manager.service.referral.Referral;
import com.example.cbr_manager.service.visit.Visit;
import com.example.cbr_manager.ui.AlertViewModel;
import com.example.cbr_manager.ui.ClientViewModel;
import com.example.cbr_manager.ui.ReferralViewModel;
import com.example.cbr_manager.ui.VisitViewModel;
import com.example.cbr_manager.ui.alert.alert_details.AlertDetailsActivity;
import com.example.cbr_manager.ui.clientselector.ClientSelectorActivity;
import com.example.cbr_manager.ui.create_client.CreateClientStepperActivity;
import com.example.cbr_manager.utils.Helper;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;

import org.threeten.bp.format.FormatStyle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class DashboardFragment extends Fragment {

    private static final int NEW_VISIT_CODE = 100;
    private final APIService apiService = APIService.getInstance();
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    List<Client> clientViewPagerList;
    Alert newestAlert;
    TextView seeMoreTextView;
    TextView dateAlertTextView;
    TextView titleTextView;
    int homeAlertId = -1;

    private VisitViewModel visitViewModel;
    private AlertViewModel alertViewModel;
    private ClientViewModel clientViewModel;
    private ReferralViewModel referralViewModel;

    public DashboardFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        visitViewModel = new ViewModelProvider(this).get(VisitViewModel.class);
        alertViewModel = new ViewModelProvider(this).get(AlertViewModel.class);
        clientViewModel = new ViewModelProvider(this).get(ClientViewModel.class);
        referralViewModel = new ViewModelProvider(this).get(ReferralViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchNewestAlert();
        setupViewPager(view);
        setupButtons(view);
        setAlertButtons();

        fetchTopFiveRiskiestClients(clientViewPagerList);

        setupVisitStats(view);
        setupOutstandingReferralStats(view);
        setupTapTarget(view);

    }

    private void setupTapTarget(View view) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (!preferences.getBoolean("firstTimeDashboardAlert", false)) {
            TapTargetView.showFor(getActivity(),
                    TapTarget.forView(view.findViewById(R.id.seeAllTextView), "Never miss an alert.",
                            "Click here to see all the alerts. Be sure to check often to always stay updated.")
                            .outerCircleAlpha(0.96f)
                            .targetCircleColor(R.color.white)
                            .titleTextSize(20)
                            .drawShadow(true)
                            .tintTarget(true)
                            .dimColor(R.color.black));
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("firstTimeDashboardAlert", true);
            editor.apply();
        }
    }

    private void setupOutstandingReferralStats(View root) {
        referralViewModel.getReferralsAsLiveData().observe(getViewLifecycleOwner(), referrals -> {
            int createdReferrals = 0;
            int completedReferrals = 0;
            String status;
            for (Referral referral : referrals) {
                status = referral.getStatus();
                if (status.toLowerCase().trim().equals("created")) {
                    createdReferrals++;
                } else if (status.toLowerCase().trim().equals("resolved")) {
                    completedReferrals++;
                }
            }
//                        fillTopThreeOutstandingReferrals(referrals);
            TextView createdReferralsTextView = root.findViewById(R.id.dashboardOutstandingReferralsNumTextView);
            createdReferralsTextView.setText(Integer.toString(createdReferrals));
            TextView completedReferralsTextView = root.findViewById(R.id.dashboardCompletedReferralsNumTextView);
            completedReferralsTextView.setText(Integer.toString(completedReferrals));
        });
    }

    private void fillTopThreeOutstandingReferrals(List<Referral> referrals) {
        if (referrals.size() == 0) {
            // Fill nothing.
            return;
        } else if (referrals.size() == 1) {
            LinearLayout outStandingReferral1 = getView().findViewById(R.id.outstandingReferralPerson1);
            outStandingReferral1.setVisibility(View.VISIBLE);
        } else if (referrals.size() == 2) {
            LinearLayout outstandingReferral1 = getView().findViewById(R.id.outstandingReferralPerson1);
            LinearLayout outstandingReferral2 = getView().findViewById(R.id.outstandingReferralPerson2);
            outstandingReferral1.setVisibility(View.VISIBLE);
            outstandingReferral2.setVisibility(View.VISIBLE);
        } else {
            LinearLayout outstandingReferral1 = getView().findViewById(R.id.outstandingReferralPerson1);
            LinearLayout outstandingReferral2 = getView().findViewById(R.id.outstandingReferralPerson2);
            LinearLayout outstandingReferral3 = getView().findViewById(R.id.outstandingReferralPerson3);
            outstandingReferral1.setVisibility(View.VISIBLE);
            outstandingReferral2.setVisibility(View.VISIBLE);
            outstandingReferral3.setVisibility(View.VISIBLE);
        }
    }

    private void setupOutstandingReferralCard(int imageId, int nameTextViewId, int serviceTextViewId, int dateTextViewId, String name, String service, String date) {
        TextView nameTextView = getView().findViewById(nameTextViewId);
        nameTextView.setText(name);
        TextView serviceTextView = getView().findViewById(serviceTextViewId);
        serviceTextView.setText(service);
    }

    private void setupVisitStats(View root) {
        visitViewModel.getVisitsAsLiveData().observe(getViewLifecycleOwner(), visits -> {
            int totalVisits = visits.size();

            TextView totalNumberVisits = root.findViewById(R.id.dashboardTotalVisitsNum);
            totalNumberVisits.setText(Integer.toString(totalVisits));
            List<String> differentLocations = new ArrayList<>();
            List<Integer> differentClients = new ArrayList<>();
            for (Visit eachVisit : visits) {
                if (!differentClients.contains(eachVisit.getClientId())) {
                    differentClients.add(eachVisit.getClientId());
                }
                if (!differentLocations.contains(eachVisit.getLocationDropDown())) {
                    differentLocations.add(eachVisit.getLocationDropDown());
                }
            }

            TextView totalClientsVisited = root.findViewById(R.id.dashboardClientsVisitedNum);
            totalClientsVisited.setText(Integer.toString(differentClients.size()));

            TextView totalLocationsVisited = root.findViewById(R.id.dashboardLocationsNum);
            totalLocationsVisited.setText(Integer.toString(differentLocations.size()));
        });
    }

    public void fetchNewestAlert() {
        dateAlertTextView = getView().findViewById(R.id.dateAlertTextView);
        titleTextView = getView().findViewById(R.id.announcementTextView);
        alertViewModel.getAllAlerts().observe(getViewLifecycleOwner(), retrivedAlerts -> {
            List<Alert> alerts = retrivedAlerts;
            if (alerts != null & !alerts.isEmpty()) {
                newestAlert = alerts.get(0);
                dateAlertTextView.setText("Date posted:  " + Helper.formatDateTimeToLocalString(newestAlert.getCreatedAt(), FormatStyle.SHORT));
                titleTextView.setText(newestAlert.getTitle());
                homeAlertId = newestAlert.getId();
            }
        });
    }

    public void setAlertButtons() {
        seeMoreTextView = getView().findViewById(R.id.seeAllTextView);
        TextView moreTextView = getView().findViewById(R.id.dashboardAlertsMoreTextView);
        moreTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AlertDetailsActivity.class);
                intent.putExtra("alertId", homeAlertId);
                startActivity(intent);
            }
        });
        seeMoreTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(DashboardFragment.this)
                        .navigate(R.id.action_nav_dashboard_to_nav_alert_list);
            }
        });
    }

    public void fetchTopFiveRiskiestClients(List<Client> clientList) {
        clientViewModel.getAllClients().observe(getViewLifecycleOwner(), clients -> {
            clientList.clear();
            clients.sort(new ClientRiskScoreComparator(ClientRiskScoreComparator.SortOrder.DESCENDING));
            List<Client> topFiveClients;
            if (clients.size() > 5) {
                topFiveClients = clients.subList(0, 5);
            } else {
                topFiveClients = clients;
            }
            clientList.addAll(topFiveClients);
            adapter.notifyDataSetChanged();
        });
    }

    private void setupButtons(View root) {
        TextView allClientsTextView = root.findViewById(R.id.dashboardAllClientsTextView);
        allClientsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(DashboardFragment.this)
                        .navigate(R.id.action_nav_dashboard_to_nav_client_list);
            }
        });

        TextView addClientTextView = root.findViewById(R.id.dashaboardAddClientTextView);
        addClientTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreateClientStepperActivity.class);
                startActivity(intent);
            }
        });

        TextView newVisitTextView = root.findViewById(R.id.dashboardNewVisitTextView);
        newVisitTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ClientSelectorActivity.class);
                intent.putExtra("CODE", NEW_VISIT_CODE);
                startActivity(intent);
            }
        });

        TextView allOutstandingReferralsTextView = root.findViewById(R.id.allOutstandingReferralsTextView);
        allOutstandingReferralsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(DashboardFragment.this)
                        .navigate(R.id.action_nav_dashboard_to_nav_referral_list);
            }
        });
    }

    private void setupViewPager(View root) {
        clientViewPagerList = new ArrayList<>();
        adapter = new ViewPagerAdapter(getContext(), this.getActivity(), clientViewPagerList);
        viewPager = root.findViewById(R.id.clientPriorityList);
        viewPager.setAdapter(adapter);
        viewPager.setClipToPadding(false);
        viewPager.setPadding(220, 0, 220, 0);
    }


}