package com.example.xtc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private CardView cardSA, cardIS;

    private Toolbar mainToolbar;

    private FloatingActionButton addReportBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainToolbar = (Toolbar) findViewById(R.id.toolbarid);
        setSupportActionBar(mainToolbar);

        getSupportActionBar().setTitle(" Llaporkan Jalan Rusak");

         CardView cardSA = findViewById(R.id.card_SA);
        cardSA.setOnClickListener(this);

        CardView cardIS = findViewById(R.id.card_IS);
        cardIS.setOnClickListener(this);

        addReportBtn = findViewById(R.id.add_report_btn);
        addReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newReportIntent = new Intent(MainActivity.this, NewReportActivity.class);
                startActivity(newReportIntent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.card_SA:
                Intent showSA = new Intent(MainActivity.this, PictureSourceActivity.class);
                startActivity(showSA);
                break;
        }

        switch (view.getId()){
            case R.id.card_IS:
                Intent showSA = new Intent(MainActivity.this, ReportListActivity.class);
                startActivity(showSA);
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_my_account_btn:

                Intent settingsIntent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(settingsIntent);

                return true;

            default:
                return false;

        }
    }
}
