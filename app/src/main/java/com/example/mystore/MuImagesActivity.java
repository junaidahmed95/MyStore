package com.example.mystore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.asksira.bsimagepicker.BSImagePicker;
import com.asksira.bsimagepicker.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mystore.Adapter.PostImageAdapter;
import com.example.mystore.Adapter.multiviewpageradapter;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
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

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mystore.MessagingActivity.countUnread;
import static com.example.mystore.MessagingActivity.getchatroot;
import static com.example.mystore.MessagingActivity.mimagesList;
import static com.example.mystore.MessagingActivity.receiver_id;
import static com.example.mystore.MessagingActivity.receiver_image;
import static com.example.mystore.MessagingActivity.receiver_name;
import static com.example.mystore.MessagingActivity.unreadListenr;

public class MuImagesActivity extends AppCompatActivity implements BSImagePicker.OnMultiImageSelectedListener, BSImagePicker.ImageLoaderDelegate {

    RecyclerView mAttachRecyclerView;
    StorageReference storageReference;
    private DatabaseReference unreadReference, ConversionRef;
    private StorageTask uploadTask;
    int a;
    LinearLayout mBck;
    DatabaseReference reference;
    public static ViewPager myviewPager;
    APIService apiService;
    public static boolean checknotifychange = false;
    private multiviewpageradapter viewpagerAdapter;
    SweetAlertDialog pDialog;
    public static TextView mSendTotatl;
    public static ImageView imageView;
    private FloatingActionButton mSend, mCancel;
    private PostImageAdapter postImageAdapter;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mu_images);
        mAttachRecyclerView = findViewById(R.id.attach_recyclerView);
        floatingActionButton = findViewById(R.id.addFab);
        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        CircleImageView mImage = findViewById(R.id.uImage);
        Glide.with(MuImagesActivity.this).asBitmap().load(receiver_image).apply(new RequestOptions().placeholder(R.drawable.avatar)).into(mImage);
        myviewPager = findViewById(R.id.myviewpagerindetails);
        pDialog = new SweetAlertDialog(MuImagesActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#0288D1"));
        pDialog.setTitleText("Sending...");
        pDialog.setCancelable(false);
        mBck = findViewById(R.id.backBtn);
        mBck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mimagesList.clear();
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        mSendTotatl = findViewById(R.id.sendTotatl);
        unreadReference = FirebaseDatabase.getInstance().getReference().child("Unread").child(FirebaseAuth.getInstance().getUid()).child(receiver_id);
        ConversionRef = FirebaseDatabase.getInstance().getReference().child("Chatlist");
        reference = FirebaseDatabase.getInstance().getReference();
        mSend = findViewById(R.id.senImages);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mAttachRecyclerView.setLayoutManager(linearLayoutManager);
        postImageAdapter = new PostImageAdapter(this, mimagesList);
        mAttachRecyclerView.setAdapter(postImageAdapter);
        viewpagerAdapter = new multiviewpageradapter(mimagesList);
        myviewPager.setAdapter(viewpagerAdapter);
        mSendTotatl.setText("" + mimagesList.size());
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mimagesList.size() == 25) {
                    Toast.makeText(MuImagesActivity.this, "You can send only 25 images at a time", Toast.LENGTH_LONG).show();
                } else {
                    BSImagePicker pickerDialog = new BSImagePicker.Builder("com.example.mystore.fileprovider")
                            .isMultiSelect()
                            .setMinimumMultiSelectCount(1)
                            .setMaximumMultiSelectCount(25 - mimagesList.size())
                            .setPeekHeight(Utils.dp2px(550))
                            .setMultiSelectDoneTextColor(R.color.colorAccent)
                            .build();
                    pickerDialog.show(getSupportFragmentManager(), "picker");
                }


            }
        });
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < mimagesList.size(); i++) {
                    a++;
                    uploadImage(mimagesList.get(i));

                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mimagesList.clear();
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mimagesList.clear();
    }

    @Override
    public void finish() {
        mimagesList.clear();
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = MuImagesActivity.this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(Uri imageUri) {
        pDialog.show();

        if (imageUri != null) {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
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
                        String id = reference.child("Chats").push().getKey();
                        DateFormat df = new SimpleDateFormat("HH:mm");
                        Calendar calobj = Calendar.getInstance();
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("message", mUri);
                        hashMap.put("time", ServerValue.TIMESTAMP);
                        hashMap.put("seen", false);
                        hashMap.put("type", "image");
                        hashMap.put("chatid", id);
                        hashMap.put("sender", FirebaseAuth.getInstance().getUid());
                        hashMap.put("delivery", "not available");

                        if (getchatroot.equals("")) {
                            reference.child("Chats").child(FirebaseAuth.getInstance().getUid() + receiver_id).child(id).setValue(hashMap);
                        } else {
                            reference.child("Chats").child(getchatroot).child(id).setValue(hashMap);
                        }
                        if (a == mimagesList.size()) {
                            //sendNotifiaction(receiver_id, UserName, "\uD83D\uDCF7 New Image");
                            UnreadMessage("\uD83D\uDCF7 New Image");
                            mimagesList.clear();
                            finish();
                        }
                    } else {
                        Toast.makeText(MuImagesActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MuImagesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }
            });
        } else {
            Toast.makeText(MuImagesActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void UnreadMessage(String message) {
        Map concMap = new HashMap<>();
        concMap.put("last", message);
        concMap.put("time", ServerValue.TIMESTAMP);
        concMap.put("unread", countUnread + a);
        concMap.put("name", receiver_name.toLowerCase());
        concMap.put("image", receiver_image);
        concMap.put("status", "sent");
        concMap.put("who", FirebaseAuth.getInstance().getUid());

        Map concMap2 = new HashMap<>();
        concMap2.put("last", message);
        concMap2.put("time", ServerValue.TIMESTAMP);
        concMap2.put("unread", 0);
        concMap2.put("name", receiver_name.toLowerCase());
        concMap2.put("image", receiver_image);
        concMap2.put("status", "sent");
        concMap2.put("who", FirebaseAuth.getInstance().getUid());

        Map conc1Map = new HashMap<>();
        conc1Map.put("last", message);
        conc1Map.put("time", ServerValue.TIMESTAMP);
        conc1Map.put("unread", 0);
        conc1Map.put("name", receiver_name.toLowerCase());
        conc1Map.put("image", receiver_image);
        conc1Map.put("status", "sent");
        conc1Map.put("who", FirebaseAuth.getInstance().getUid());

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
                    unreadReference.child("unread").setValue(countUnread + a).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                                            Toast.makeText(MuImagesActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
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


    @Override
    public void loadImage(File imageFile, ImageView ivImage) {
        Glide.with(MuImagesActivity.this).asBitmap().load(imageFile).into(ivImage);
    }

    @Override
    public void onMultiImageSelected(List<Uri> uriList, String tag) {

        for (int i = 0; i < uriList.size(); i++) {
            if (i >= 25) return;
            mimagesList.add(uriList.get(i));
        }
        postImageAdapter = new PostImageAdapter(this, mimagesList);
        mAttachRecyclerView.setAdapter(postImageAdapter);
        viewpagerAdapter = new multiviewpageradapter(mimagesList);
        myviewPager.setAdapter(viewpagerAdapter);
        mSendTotatl.setText("" + mimagesList.size());
    }



}