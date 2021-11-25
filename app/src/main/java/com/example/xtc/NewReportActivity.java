package com.example.xtc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import id.zelory.compressor.Compressor;

public class NewReportActivity extends AppCompatActivity {

    private Toolbar newPostReportToolbar;

    private ImageView newPostReportImage;
    private EditText newPostReportDesc;
    private Button newPostReportBtn;

    private Uri postReportImageUri = null;

    private ProgressBar newPostReportProgress;

    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private Bitmap compressedImageFile;

    private String current_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_report);

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        current_user_id = firebaseAuth.getCurrentUser().getUid();

        newPostReportToolbar = findViewById(R.id.new_report_toolbar);
        setSupportActionBar(newPostReportToolbar);
        getSupportActionBar().setTitle("Add New Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        newPostReportImage = findViewById(R.id.new_report_image);
        newPostReportDesc = findViewById(R.id.new_report_desc);
        newPostReportBtn = findViewById(R.id.report_btn);
        newPostReportProgress = findViewById(R.id.new_report_progress);

        newPostReportImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(512, 512)
                        .setAspectRatio(1, 1)
                        .start(NewReportActivity.this);

            }
        });

        newPostReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String desc = newPostReportDesc.getText().toString();
                if (!TextUtils.isEmpty(desc) && postReportImageUri != null); {

                    newPostReportProgress.setVisibility(View.VISIBLE);

                    final String randomName = UUID.randomUUID().toString();

                    final StorageReference filePath = storageReference.child("post_images").child(randomName + ".jpg");
                    Task<Uri>urlTask = filePath.putFile(postReportImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return filePath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull final Task<Uri> task) {

                            final String downloadUri = task.getResult().toString();

                            if (task.isSuccessful()) {

                                File newImageFile = new File(postReportImageUri.getPath());
                                try {
                                    compressedImageFile = new Compressor(NewReportActivity.this)
                                            .setMaxHeight(100)
                                            .setMaxWidth(100)
                                            .setQuality(2)
                                            .compressToBitmap(newImageFile);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                final byte[] thumbData = baos.toByteArray();

                                final StorageReference uploadThumbImage = storageReference.child("post_images/thumbs").child(randomName + ".jpg");

                                Task<Uri> urlTask = uploadThumbImage.putBytes(thumbData).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                        if (!task.isSuccessful()) {
                                            throw task.getException();
                                        }
                                        return uploadThumbImage.getDownloadUrl();
                                    }
                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            String downloadThumbUri = task.getResult().toString();

                                            Map<String, Object> postMap = new HashMap<>();
                                            postMap.put("image_url",downloadUri);
                                            postMap.put("image_thumb", downloadThumbUri);
                                            postMap.put("desc", desc);
                                            postMap.put("user_id", current_user_id);
                                            postMap.put("timestamp", FieldValue.serverTimestamp());

                                            firebaseFirestore.collection("Posts").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(NewReportActivity.this, "Your Report is Succesfully posted", Toast.LENGTH_LONG).show();
                                                        Intent mainIntent = new Intent(NewReportActivity.this, ReportListActivity.class);
                                                        startActivity(mainIntent);
                                                    } else {
                                                        String tempMessage = task.getException().getMessage();
                                                        Toast.makeText(NewReportActivity.this, "Error: " + tempMessage, Toast.LENGTH_LONG).show();
                                                    }
                                                    newPostReportProgress.setVisibility(View.INVISIBLE);
                                                }
                                            });
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        // Error Handling

                                    }
                                });
                            } else {
                                // Handle failures
                                newPostReportProgress.setVisibility(View.INVISIBLE);
                                String tempMessage = task.getException().getMessage();
                                Toast.makeText(NewReportActivity.this, "Error" + tempMessage, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                postReportImageUri = result.getUri();
                newPostReportImage.setImageURI(postReportImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }
        }
    }
}