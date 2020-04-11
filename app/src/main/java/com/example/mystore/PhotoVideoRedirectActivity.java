package com.example.mystore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.mystore.Model.APIService;
import com.example.mystore.Model.Client;
import com.example.mystore.Model.Data;
import com.example.mystore.Model.MyResponce;
import com.example.mystore.Model.Sender;
import com.example.mystore.Model.Token;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mystore.MessagingActivity.count;
import static com.example.mystore.MessagingActivity.countUnread;
import static com.example.mystore.MessagingActivity.fuserid;
import static com.example.mystore.MessagingActivity.getchatroot;
import static com.example.mystore.MessagingActivity.receiver_id;
import static com.example.mystore.MessagingActivity.receiver_image;
import static com.example.mystore.MessagingActivity.receiver_name;
import static com.example.mystore.MessagingActivity.unreadListenr;

public class PhotoVideoRedirectActivity extends AppCompatActivity {
    ImageView save, cancle,playvideobutton;
    Uri urivid;
    DatabaseReference reference;
    List mGChat, receiver;
    FirebaseUser fuser;
    APIService apiService;
    StorageReference imageuploadstrref;
    boolean checkvideoplay = false,newcheck = false;
    StorageReference storageReference, videoreference;
    String mFileName = null;
    int length = 0;
    ValueEventListener referenceListener;
    private DatabaseReference unreadReference, ConversionRef;
    private StorageTask uploadTask, uploadTask1, uploadTask3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_video_redirect);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        save = findViewById(R.id.save);
        cancle = findViewById(R.id.cancle);
        playvideobutton = findViewById(R.id.playvideobutton);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        imageuploadstrref = FirebaseStorage.getInstance().getReference("uploads");
        unreadReference = FirebaseDatabase.getInstance().getReference().child("Unread").child(FirebaseAuth.getInstance().getUid()).child(receiver_id);
        ConversionRef = FirebaseDatabase.getInstance().getReference().child("Chatlist");
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        videoreference = FirebaseStorage.getInstance().getReference("uploadsvideo");
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        init();
    }

    VideoView videoView;

    private void init() {


        ImageView imgshow = findViewById(R.id.imgShow);
        videoView = findViewById(R.id.vidShow);
        if (getIntent().getStringExtra("WHO").equalsIgnoreCase("Image")) {
            imgshow.setVisibility(View.VISIBLE);
            Glide.with(PhotoVideoRedirectActivity.this).load(Cameramethod.uri.getPath()).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(imgshow);

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MessagingActivity messagingActivity = new MessagingActivity();

                    uploadImage();
                }
            });

            cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();

                }
            });
        } else {

            videoView.setVisibility(View.VISIBLE);

            try {
                videoView.setMediaController(null);
                videoView.setVideoURI(Uri.parse(getIntent().getStringExtra("PATH")));
                urivid = Uri.parse(getIntent().getStringExtra("PATH"));


            } catch (Exception e) {
                e.printStackTrace();
            }
            videoView.requestFocus();
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    //videoView.start();
                    //umair
                    playvideobutton.setVisibility(View.VISIBLE);
                    videoView.seekTo(1);

                    playvideobutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!checkvideoplay){
                                if (!newcheck){
                                    videoView.start();
                                    checkvideoplay = true;
                                    Toast.makeText(PhotoVideoRedirectActivity.this, "start", Toast.LENGTH_SHORT).show();
                                    playvideobutton.setImageResource(R.drawable.ic_pause_black_24dp);
                                }
                                else{
                                    videoView.seekTo(length);
                                    Toast.makeText(PhotoVideoRedirectActivity.this, "resume", Toast.LENGTH_SHORT).show();
                                    videoView.start();
                                    checkvideoplay = true;
                                    playvideobutton.setImageResource(R.drawable.ic_pause_black_24dp);
                                }
                            }
                            else{
                                length=videoView.getCurrentPosition();
                                Toast.makeText(PhotoVideoRedirectActivity.this, "pause", Toast.LENGTH_SHORT).show();
                                playvideobutton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                                videoView.pause();
                                checkvideoplay = false;
                                newcheck = true;
                            }

                        }
                    });
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(PhotoVideoRedirectActivity.this, "Pending...", Toast.LENGTH_SHORT).show();
                            //   uploadVideo();
                        }
                    });
                    cancle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                }
            });
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    //videoView.start();
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(PhotoVideoRedirectActivity.this, "send clicked  "+urivid, Toast.LENGTH_SHORT).show();



                        }
                    });
                    cancle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();

                        }
                    });
                }
            });
        }
    }

    private String getFileExtension1(Uri uri) {
        ContentResolver contentResolver = PhotoVideoRedirectActivity.this.getContentResolver();
        MimeTypeMap mimeTypeMap1 = MimeTypeMap.getSingleton();
        return mimeTypeMap1.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void uploadVideo() {

        Toast.makeText(this, ""+urivid, Toast.LENGTH_SHORT).show();
//        final DateFormat df = new SimpleDateFormat("HH:mm");
//        final Calendar calobj = Calendar.getInstance();
//
//        String geturi = urivid.toString();
//        mchat.add(new Chats("video", df.format(calobj.getTime()), fuserid, false, geturi));
//        messageAdapter = new MessageAdapter(PhotoVideoRedirectActivity.this, mchat);
//        MessagingActivity.recyclerView.setAdapter(messageAdapter);
//        messageAdapter.notifyDataSetChanged();
//        finish();
//        //final ProgressDialog pd = new ProgressDialog(PhotoVideoRedirectActivity.this);
//        //pd.setMessage("Uploading");
//        //pd.show();
//
//        if (urivid != null) {
//
//            final StorageReference fileReference1 = videoreference.child(System.currentTimeMillis()
//                    + "." + getFileExtension1(urivid));
//
//
//            urivid = Uri.fromFile(new File(String.valueOf(urivid)));
//            uploadTask1 = fileReference1.putFile(urivid);
//            uploadTask1.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                @Override
//                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                    if (!task.isSuccessful()) {
//                        throw task.getException();
//                    }
//
//                    return fileReference1.getDownloadUrl();
//                }
//            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                @Override
//                public void onComplete(@NonNull Task<Uri> task) {
//                    if (task.isSuccessful()) {
//                        Uri downloadUri = task.getResult();
//                        String mUri1 = downloadUri.toString();
//                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//                        String id = reference.child("Chats").push().getKey();
//                        HashMap<String, Object> hashMap = new HashMap<>();
//                        hashMap.put("message", mUri1);
//                        hashMap.put("time", ServerValue.TIMESTAMP);
//                        hashMap.put("seen", false);
//                        hashMap.put("type", "video");
//                        hashMap.put("chatid", id);
//                        hashMap.put("sender", fuserid);
//                        hashMap.put("delivery", "not available");
//                        if (getchatroot.equals("")) {
//                            reference.child("Chats").child(fuserid + receiver_id).child(id).setValue(hashMap);
//                        } else {
//                            reference.child("Chats").child(getchatroot).child(id).setValue(hashMap);
//                        }
//                        UnreadMessage("\uD83C\uDFA5 New Video");
//                        sendNotifiaction(receiver_id, UserName, "\uD83C\uDFA5 New Video");
//                        finish();
//
//
//                    } else {
//                        Toast.makeText(PhotoVideoRedirectActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
//                        //pd.dismiss();
//                    }
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(PhotoVideoRedirectActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                   // pd.dismiss();
//                }
//            });
//        } else {
//            Toast.makeText(PhotoVideoRedirectActivity.this, "No video selected", Toast.LENGTH_SHORT).show();
//        }
    }


    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = PhotoVideoRedirectActivity.this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void uploadImage() {
        final String sender, rec;
        final DateFormat df = new SimpleDateFormat("HH:mm");
        final Calendar calobj = Calendar.getInstance();
        sender = getIntent().getStringExtra("sender");
        rec = getIntent().getStringExtra("rec");
        final ProgressDialog pd = new ProgressDialog(PhotoVideoRedirectActivity.this);
        pd.setMessage("Uploading");
        pd.show();

        if (Cameramethod.uri != null) {
            Uri uri = Cameramethod.uri;
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(uri));
            uri = Uri.fromFile(new File(String.valueOf(uri)));

            uploadTask = fileReference.putFile(uri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.


                            isSuccessful()) {
                        throw task.getException();
                    }

                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {

                        DateFormat df = new SimpleDateFormat("HH:mm");
                        Calendar calobj = Calendar.getInstance();
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        String id = reference.child("Chats").push().getKey();
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("message", mUri);
                        hashMap.put("time", ServerValue.TIMESTAMP);
                        hashMap.put("seen", false);
                        hashMap.put("type", "image");
                        hashMap.put("chatid", id);
                        hashMap.put("sender", fuserid);
                        hashMap.put("delivery", "not available");
                        if (getchatroot.equals("")) {
                            reference.child("Chats").child(fuserid + receiver_id).child(id).setValue(hashMap);
                        } else {
                            reference.child("Chats").child(getchatroot).child(id).setValue(hashMap);
                        }
                        UnreadMessage("\uD83D\uDCF7 New Image");
                        //sendNotifiaction(receiver_id, UserName, "\uD83D\uDCF7 New Image");
                        finish();



                    } else {
                        Toast.makeText(PhotoVideoRedirectActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    pd.dismiss();
                }
            });
        } else {
            Toast.makeText(PhotoVideoRedirectActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void UnreadMessage(String message) {
        Map concMap = new HashMap<>();
        concMap.put("last", message);
        concMap.put("time", ServerValue.TIMESTAMP);
        concMap.put("unread", count);
        concMap.put("name", receiver_name.toLowerCase());
        concMap.put("image", receiver_image);
        concMap.put("status", "sent");
        concMap.put("who", fuserid);

        Map concMap2 = new HashMap<>();
        concMap2.put("last", message);
        concMap2.put("time", ServerValue.TIMESTAMP);
        concMap2.put("unread", 0);
        concMap2.put("name", receiver_name.toLowerCase());
        concMap2.put("image", receiver_image);
        concMap2.put("status", "sent");
        concMap2.put("who", fuserid);

        Map conc1Map = new HashMap<>();
        conc1Map.put("last", message);
        conc1Map.put("time", ServerValue.TIMESTAMP);
        conc1Map.put("unread", 0);
        conc1Map.put("name", receiver_name.toLowerCase());
        conc1Map.put("image", receiver_image);
        conc1Map.put("status", "sent");
        conc1Map.put("who", fuserid);

        Map message1TextDetail = new HashMap<>();
        message1TextDetail.put(FirebaseAuth.getInstance().getUid() + "/" + receiver_id, conc1Map);
        if (MessagingActivity.status.equals("")) {
            message1TextDetail.put(receiver_id + "/" + FirebaseAuth.getInstance().getUid(), concMap);
        } else if (MessagingActivity.status.equals("no")) {
            message1TextDetail.put(receiver_id + "/" + FirebaseAuth.getInstance().getUid(), concMap);
        } else {
            message1TextDetail.put(receiver_id + "/" + FirebaseAuth.getInstance().getUid(), concMap2);
        }

        ConversionRef.updateChildren(message1TextDetail, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (MessagingActivity.status.equals("") || MessagingActivity.status.equals("no")) {
                    unreadReference.child("unread").setValue(countUnread + 1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            unreadReference.addValueEventListener(unreadListenr);

                        }
                    });
                }

            }
        });
        //  progressBar.setVisibility(View.GONE);
    }

    private void sendNotifiaction(String receiver, final String username, final String message) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(FirebaseAuth.getInstance().getUid(), R.mipmap.ic_launcher, username + ": " + message, "New Message",
                            receiver_id);

                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponce>() {
                                @Override
                                public void onResponse(Call<MyResponce> call, Response<MyResponce> response) {
                                    if (response.code() == 200) {
                                        if (response.body().success != 1) {
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponce> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}

