package com.example.cbr_manager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.Group;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.cbr_manager.service.APIService;
import com.example.cbr_manager.service.alert.Alert;
import com.example.cbr_manager.service.user.User;
import com.example.cbr_manager.ui.AlertViewModel;
import com.example.cbr_manager.ui.AuthViewModel;
import com.example.cbr_manager.ui.create_client.CreateClientStepperActivity;
import com.example.cbr_manager.ui.user.UserActivity;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class NavigationActivity extends AppCompatActivity implements DrawerLayout.DrawerListener {
    public static String KEY_SNACK_BAR_MESSAGE = "KEY_SNACK_BAR_MESSAGE";
    private final String TAG = "NavigationActivity";
    NavigationView navigationView;
    AuthViewModel authViewModel;
    AlertViewModel alertViewModel;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        alertViewModel = new ViewModelProvider(this).get(AlertViewModel.class);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.nav_view);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.addDrawerListener(this);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        appBarConfiguration = new AppBarConfiguration.Builder(
                // each id passed is treated as top level fragment
                R.id.nav_home, R.id.nav_user_creation, R.id.nav_alert_creation, R.id.nav_statistics
        )
                .setDrawerLayout(drawer)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        handleIncomingSnackBarMessage(navigationView);

        View headerView = navigationView.getHeaderView(0);

        setUpHeaderView(headerView);
        setupTapTarget(toolbar);




        hideAdminOnlyMenuItems();
    }

    private void setupTapTarget(Toolbar toolbar) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(!preferences.getBoolean("firstTimeNav", false)) {
            TapTargetView.showFor(this,
                    TapTarget.forToolbarNavigationIcon(toolbar,
                            "One tap away.", "Tap the menu icon to navigate to the dashboard, view client, visit, and referral lists, and any alerts.")
                            .outerCircleAlpha(0.96f)
                            .targetCircleColor(R.color.white)
                            .titleTextSize(20)
                            .drawShadow(true)
                            .tintTarget(true)
                            .dimColor(R.color.black)
//                        .transparentTarget(true)

            );
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("firstTimeNav", true);
            editor.apply();
        }
    }

    private void hideAdminOnlyMenuItems(){
        authViewModel.getUser().subscribe(new DisposableSingleObserver<User>() {
            @Override
            public void onSuccess(@io.reactivex.annotations.NonNull User user) {
                if(!user.isAdmin()){
                    MenuItem itemNavUserCreation = navigationView.getMenu().findItem(R.id.nav_user_creation);
                    itemNavUserCreation.setEnabled(user.isAdmin());
                    MenuItem itemNavAlertCreation = navigationView.getMenu().findItem(R.id.nav_alert_creation);
                    itemNavAlertCreation.setEnabled(user.isAdmin());
                    MenuItem itemNavStats = navigationView.getMenu().findItem(R.id.nav_statistics);
                    itemNavStats.setEnabled(user.isAdmin());
                }
            }
            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {

            }
        });
    }

    private void setupAlertsBadge(NavigationView navigationView) {
        alertViewModel.getUnreadAlerts().observe(this,retrievedAlerts ->{
            TextView alertsTV = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_alert_list));
            alertsTV.setGravity(Gravity.CENTER_VERTICAL);
            alertsTV.setTypeface(null, Typeface.BOLD);
            alertsTV.setTextColor(getResources().getColor(R.color.purple_700));

            if(retrievedAlerts!=null){
                int size = retrievedAlerts.size();
                if (size > 0) {
                    alertsTV.setText(Integer.toString(size));
                }
            }
        });
    }

    private void setUpHeaderView(View headerView) {
        TextView navFirstName = headerView.findViewById(R.id.nav_first_name);
        TextView navEmail = headerView.findViewById(R.id.nav_email);
        Group userInfoGroup = headerView.findViewById(R.id.user_info_group);

        authViewModel.getUser().subscribe(new SingleObserver<User>() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
            }

            @Override
            public void onSuccess(@io.reactivex.annotations.NonNull User user) {
                navFirstName.setText(user.getFirstName());
                navEmail.setText(user.getEmail());

                for(int id: userInfoGroup.getReferencedIds()){
                    headerView.findViewById(id).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(NavigationActivity.this, UserActivity.class);
                            intent.putExtra(UserActivity.KEY_USER_ID, user.getId());
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (view instanceof EditText) {
                Rect outRect = new Rect();
                view.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    view.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    public void handleIncomingSnackBarMessage(View view) {
        String message = getIntent().getStringExtra(KEY_SNACK_BAR_MESSAGE);
        if (message != null && !message.isEmpty()) {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {
        setupAlertsBadge(navigationView);
    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}

