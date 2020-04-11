package com.example.mystore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.mystore.Model.HelpingMethods;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import static com.example.mystore.ProfileActivity.mImage;

public class videoView extends AppCompatActivity {
    Intent intent;
    String viewimage, viewvideo;
    VideoView video;
    ImageView imageView, mbackBtn;
    private Bitmap bitmapImage;
    private Button mCancel, mChoose;
    private HelpingMethods helpingMethods;
    ProgressBar progressBar;
    private StorageReference storageReference;
    MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

        progressBar = findViewById(R.id.video_progressBar);
        video = findViewById(R.id.video);
        mediaController = new MediaController(this);
        imageView = findViewById(R.id.image);
        mbackBtn = findViewById(R.id.backBtn);
        mCancel = findViewById(R.id.cancelBtn);
        storageReference = FirebaseStorage.getInstance().getReference("Profile Images");
        mChoose = findViewById(R.id.chooseBtn);
        intent = getIntent();
        viewvideo = intent.getStringExtra("video");
        viewimage = intent.getStringExtra("image");
        helpingMethods = new HelpingMethods(videoView.this);
        if (intent.getStringExtra("from") != null) {
            Glide.with(getApplicationContext()).asBitmap().load(viewimage).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(imageView);
            mCancel.setVisibility(View.VISIBLE);
            mChoose.setVisibility(View.VISIBLE);
            try {
                bitmapImage = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), Uri.parse(viewimage));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mChoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mImage.setImageBitmap(bitmapImage);
                    ProfileActivity.check = false;
                    finish();
                    uploadImage(Uri.parse(viewimage), v);
                }
            });

            mCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

        }

        if (viewimage != null) {
            Glide.with(getApplicationContext()).asBitmap().load(viewimage).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(imageView);
            imageView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

        }


        if (viewvideo != null) {
            video.setVideoURI(Uri.parse(viewvideo));
            video.setMediaController(mediaController);
            mediaController.setAnchorView(video);

            video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    progressBar.setVisibility(View.GONE);
                    video.start();
                }
            });

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                video.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
                        if (MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START == i) {
                            progressBar.setVisibility(View.GONE);
                        }
                        if (MediaPlayer.MEDIA_INFO_BUFFERING_START == i) {
                            progressBar.setVisibility(View.VISIBLE);
                        }
                        if (MediaPlayer.MEDIA_INFO_BUFFERING_END == i) {
                            progressBar.setVisibility(View.VISIBLE);
                        }
                        return false;
                    }
                });
            }
        }

        mbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void uploadImage(Uri imageUri, final View view) {
        final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                + "." + getFileExtension(imageUri));

        StorageTask uploadTask = fileReference.putFile(imageUri);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return fileReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull final Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    String mUri = downloadUri.toString();
                    FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(FirebaseAuth.getInstance().getUid()).child("picture").setValue(mUri);
                } else {
                    helpingMethods.SnackBar("" + task.getException().getMessage(), view);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                helpingMethods.SnackBar("" + e.getMessage(), view);
            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = videoView.this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (video.isPlaying()) {
            video.stopPlayback();
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (video.isPlaying()) {
            video.stopPlayback();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (video.isPlaying()) {
            video.stopPlayback();
        }
    }

}

