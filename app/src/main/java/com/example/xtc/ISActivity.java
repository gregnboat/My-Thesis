package com.example.xtc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ISActivity extends AppCompatActivity implements View.OnClickListener {
    private CardView cardMap, cardAcknowledge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_is);

        CardView cardMap = findViewById(R.id.card_map);
        cardMap.setOnClickListener(this);

        CardView cardAcknowledge = findViewById(R.id.card_acknowledge);
        cardAcknowledge.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.card_map:
                Intent showMap = new Intent(ISActivity.this, UploadImageActivity.class);
                startActivity(showMap);
                break;
        }

        switch (view.getId()) {
            case R.id.card_acknowledge:
                Intent showLogin = new Intent(ISActivity.this, LoginActivity.class);
                startActivity(showLogin);
                break;
        }
    }
}
