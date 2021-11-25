package com.example.xtc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ReportListActivity extends AppCompatActivity {

    private Toolbar arlMainToolbar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    private BottomNavigationView mainBottomNav;

    private ReportListFragment reportListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        arlMainToolbar = (Toolbar) findViewById(R.id.arl_main_toolbar);
        setSupportActionBar(arlMainToolbar);

        getSupportActionBar().setTitle(" ");

        if (mAuth.getCurrentUser() != null) {

            mainBottomNav = findViewById(R.id.arl_main_bottom_nav);

            // Fragments
            reportListFragment = new ReportListFragment();

            replaceFragment(reportListFragment);

            mainBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {

                        case R.id.bottom_action_report_list:
                            replaceFragment(reportListFragment);
                            return true;

                        default:
                            return false;
                    }
                }
            });


        }
    }

    private void replaceFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.arl_main_container, fragment);
        fragmentTransaction.commit();
    }
}