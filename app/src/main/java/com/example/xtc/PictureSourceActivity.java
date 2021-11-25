package com.example.xtc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PictureSourceActivity extends AppCompatActivity implements View.OnClickListener{
    private CardView cardCamera, cardGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_source);

        CardView cardCamera = findViewById(R.id.card_camera);
        cardCamera.setOnClickListener(this);

        CardView cardGalleryUpload = findViewById(R.id.card_gallery_upload);
        cardGalleryUpload.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.card_camera:
                Intent showCam = new Intent(PictureSourceActivity.this, AnalysisActivity.class);
                startActivity(showCam);
                break;
        }
        switch (view.getId()){
            case R.id.card_gallery_upload:
                Intent showGalUpload = new Intent(PictureSourceActivity.this, GalleryUploadActivity.class);
                startActivity(showGalUpload);
                break;
        }

    }
}
