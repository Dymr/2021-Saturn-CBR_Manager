package com.example.cbr_manager.ui.visits;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cbr_manager.R;
import com.example.cbr_manager.service.visit.Visit;
import com.example.cbr_manager.ui.VisitViewModel;
import com.example.cbr_manager.ui.clientdetails.ClientDetailsActivity;
import com.example.cbr_manager.ui.clientdetails.ClientDetailsFragment;
import com.example.cbr_manager.ui.visitdetails.VisitDetailsActivity;

import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.observers.DisposableObserver;

@AndroidEntryPoint
public class VisitsFragment extends Fragment implements VisitsRecyclerItemAdapter.OnItemListener {
    private static final String TAG = "VisitsFragment";
    private static final int NO_SPECIFIC_CLIENT = -1;
    public static final String KEY_CLIENT_ID = "KEY_CLIENT_ID";

    ArrayList<VisitsRecyclerItem> visitsRecyclerItems = new ArrayList<>();
    private VisitsRecyclerItemAdapter adapter;
    private int clientId = NO_SPECIFIC_CLIENT;
    private VisitViewModel visitViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        visitViewModel = new ViewModelProvider(this).get(VisitViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_visits, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        clientId = this.getArguments().getInt(KEY_CLIENT_ID, NO_SPECIFIC_CLIENT);

        Log.d(TAG, "onViewCreated: " + clientId);

        RecyclerView visitsRecyclerView = view.findViewById(R.id.recyclerView);
        visitsRecyclerView.setHasFixedSize(true); // if we know it won't change size.

        RecyclerView.LayoutManager visitsLayoutManager = new LinearLayoutManager(getContext());
        adapter = new VisitsRecyclerItemAdapter(visitsRecyclerItems, this);
        visitsRecyclerView.setLayoutManager(visitsLayoutManager);
        visitsRecyclerView.setAdapter(adapter);

        fetchVisitsToList(visitsRecyclerItems);

        SearchView visitSearch = view.findViewById(R.id.visitSearchView);
        visitSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    public void fetchVisitsToList(List<VisitsRecyclerItem> visitUIList) {
        visitViewModel.getVisits().subscribe(new DisposableObserver<Visit>() {
            @Override
            public void onNext(@io.reactivex.annotations.NonNull Visit visit) {
                if (clientId == NO_SPECIFIC_CLIENT || visit.getClientId() == clientId) {
                    VisitsRecyclerItem visitsRecyclerItem = createVisitRecycleItem(visit);
                    visitUIList.add(visitsRecyclerItem);
                }
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    private VisitsRecyclerItem createVisitRecycleItem(Visit visit) {
        Timestamp datetimeCreated = visit.getDatetimeCreated();
        Format formatter = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = formatter.format(datetimeCreated);
        String purpose = formatPurposeString(visit);
        return new VisitsRecyclerItem(formattedDate,
                visit.getClient().getFullName(), visit, purpose, visit.getLocationDropDown());
    }

    @NotNull
    private String formatPurposeString(@io.reactivex.annotations.NonNull Visit visit) {
        String purpose = "";
        if (visit.isCBRPurpose()) {
            purpose += "CBR ";
        }
        if (visit.isDisabilityReferralPurpose()) {
            purpose += "Disability Referral ";
        }
        if (visit.isDisabilityFollowUpPurpose()) {
            purpose += "Disability Follow up";
        }
        if (purpose.equals("")) {
            purpose = "No purpose indicated.";
        }
        return purpose;
    }


    @Override
    public void onItemClick(int position) {

        Intent visitInfoIntent = new Intent(getContext(), VisitDetailsActivity.class);
        VisitsRecyclerItem visitsRecyclerItem = adapter.getVisitItem(position);
        visitInfoIntent.putExtra(VisitDetailsActivity.KEY_VISIT_ID, visitsRecyclerItem.getId());

        startActivity(visitInfoIntent);
    }
}