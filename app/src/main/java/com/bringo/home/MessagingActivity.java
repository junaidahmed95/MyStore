package com.bringo.home;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.asksira.bsimagepicker.BSImagePicker;
import com.asksira.bsimagepicker.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bringo.home.Adapter.MessagingAdapter;
import com.bringo.home.Model.APIService;
import com.bringo.home.Model.Chats;
import com.bringo.home.Model.Client;
import com.bringo.home.Model.ConnectionDetector;
import com.bringo.home.Model.Data;
import com.bringo.home.Model.GetTimeAgo;
import com.bringo.home.Model.MyResponce;
import com.bringo.home.Model.Sender;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static com.bringo.home.Adapter.MessagingAdapter.checkflagforme;
import static com.bringo.home.Adapter.MessagingAdapter.myplayer;



public class MessagingActivity extends AppCompatActivity implements View.OnClickListener, BSImagePicker.OnMultiImageSelectedListener, BSImagePicker.ImageLoaderDelegate {

    @Override
    public void onMultiImageSelected(List<Uri> uriList, String tag) {

        for (int i = 0; i < uriList.size(); i++) {
            if (i >= 25) return;
            mimagesList.add(uriList.get(i));
        }
        startActivity(new Intent(MessagingActivity.this, MuImagesActivity.class));
    }

    @Override
    public void loadImage(File imageFile, ImageView ivImage) {
        Glide.with(MessagingActivity.this).load(imageFile).into(ivImage);
    }

    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;
    private BottomSheetDialog mbottomSheetDialog;
    private final int read = 1;
    private final int write = 1;
    public static List<Uri> mimagesList;
    public static ProgressBar msgProgressBar;
    public static int propertycheck = 0;
    public static String getpropimage, getprice;
    public static String flag = "false";
    CircleImageView profileimage;
    public static boolean lastcheck = false;
    ConnectionDetector cd;
    private Toolbar mChatToolbar;
    private DatabaseReference unreadReference, checkReference, ConversionRef;
    private View layoutSlideCancel, layoutLock;
    private boolean isLocked = false;
    public static int count;
    private static final int REQUEST_LOCATION = 8;
    private String sender_id;
    private String msg = "";
    DatabaseReference userRef;
    LocationManager locationManager;
    private boolean thisflag = false;
    String lattitude, longitude;
    TextView username, cancelvoicebtn;
    private static final int REQUEST_CODE_PERMISSION = 2;
    public static String fuserid;
    private boolean isDeleting;
    final DateFormat df = new SimpleDateFormat("HH:mm");
    final Calendar calobj = Calendar.getInstance();
    DatabaseReference seeRef;
    public static Uri contactData;
    public static boolean checkflag = false;
    Button play, pause;
    public static int countUnread;
    public static String getchatroot = "";


    ///////permissioncheck
    private final int CAMERA_PERMISSION_CODE = 3;
    private final int CONTACT_PERMISSION_CODE = 2;
    private final int STORAGE_PERMISSION_CODE = 4;
///////permissioncheck


    FirebaseAuth mAuth;
    DatabaseReference dbref;

    private ValueEventListener userDataListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
if(dataSnapshot.exists()){
    Long lastTime = Long.valueOf(dataSnapshot.child("status").getValue().toString());


    String Time = GetTimeAgo.getTimeAgo(lastTime, MessagingActivity.this);
    if (lastTime == 0) {
        LastSeen.setText("Online");
    } else {
        LastSeen.setText(Time);
    }

}


        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public static ValueEventListener unreadListenr = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            countUnread = Integer.parseInt(dataSnapshot.child("unread").getValue().toString());
            count = countUnread + 1;
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private ValueEventListener setSeen = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.hasChild("status")) {
                FirebaseDatabase.getInstance().getReference().child("Chatlist").child(receiver_id).child(sender_id).child("status").setValue("seen");

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private ValueEventListener setUnreadListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                if (dataSnapshot.hasChild(receiver_id)) {
                    FirebaseDatabase.getInstance().getReference().child("Chatlist").child(sender_id).child(receiver_id).child("unread").setValue(0);

                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


    @Override
    public void onClick(View view) {
        hideRevealView();
        switch (view.getId()) {

            case R.id.ImgAudio:
                openAudio();

                break;
            case R.id.Imglocation:
                openLocation();

                break;
            case R.id.Imgphoto:
                if (ContextCompat.checkSelfPermission(MessagingActivity.this,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    opencamera();
                } else {
                    requestCameraPermission();

                }
                break;

            case R.id.imgvideo:
                if (ContextCompat.checkSelfPermission(MessagingActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    openVideo();
                } else {
                    requestStoragePermission();

                }
                break;
            case R.id.imgcontact:
                if (ContextCompat.checkSelfPermission(MessagingActivity.this,
                        Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {

                    openContact();
                } else {
                    requestContactPermission();

                }
                break;
            case R.id.imggallery:
                if (ContextCompat.checkSelfPermission(MessagingActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    openImage();
                } else {
                    requestStoragePermission();

                }
                break;

        }
    }

    public enum UserBehaviour {
        CANCELING,
        LOCKING,
        NONE
    }

    public enum RecordingBehaviour {
        CANCELED,
        LOCKED,
        LOCK_DONE,
        RELEASED
    }

    private float directionOffset, cancelOffset, lockOffset;
    DatabaseReference reference;
    Intent intent;
    private View select_item;
    private View btn_send, btn_voice;

    EditText txt_send;
    private TextView timeText;
    public static MessagingAdapter messageAdapter;
    public static List<Chats> mchat;
    DatabaseReference setNoRef;
    private int audioTotalTime;
    private TimerTask timerTask;
    private Timer audioTimer;
    private SimpleDateFormat timeFormatter = new SimpleDateFormat("m:ss", Locale.getDefault());
    private static final int RC_VIDEO_PICKER = 1000;
    private View layoutDustin, layoutMessage;
    private View imageViewLockArrow, imageViewLock, imageViewMic, dustin, dustin_cover, imageViewStop;
    private float lastX, lastY;
    private float firstX, firstY;
    public static RecyclerView recyclerView;
    private String mFileName = null;
    private Animation animBlink, animJump, animJumpFast;
    private MediaRecorder mRecorder = null;
    private boolean stopTrackingAction;
    public static String status = "";
    private Handler handler;
    public static final int RECORD_AUDIO = 0;
    private float dp = 0;
    String sender_name;
    /////////////////attachment
    private ImageView gallery_btn, photo_btn, video_btn, audio_btn, location_btn, contact_btn;
    private LinearLayout backImage;
    private LinearLayout mRevealView;
    private boolean hidden = true;
    private static final int CAMERA_REQUEST = 1888;
    public static String receiver_id;
    public static String receiver_image, receiver_name, Status;
    private StorageReference mfirebaseVoice;
    APIService apiService;
    private UserBehaviour userBehaviour = UserBehaviour.NONE;
    private RecordingListener recordingListener;
    boolean notify = false;
    DatabaseReference newUnreadReference;
    StorageReference storageReference, videoreference;
    DatabaseReference checkStatusRef;
    private TextView LastSeen;
    private static final int IMAGE_REQUEST = 234;
    ProgressBar progressBar;

    Button btn;
    /*private static final int Voice_REQUEST = 1000;*/
    private Uri imageUri, videouri, voiceuri, camerauri;
    private StorageTask uploadTask, uploadTask1, uploadTask2;

    ValueEventListener seenListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    try {
                        Chats chat = snapshot.getValue(Chats.class);
                        if (!chat.getSender().equals(FirebaseAuth.getInstance().getUid())) {
                            FirebaseDatabase.getInstance().getReference("Chats").child(getchatroot).child(chat.getChatid()).child("seen").setValue(true);

                        }


//                            if (chat.getReciver().equals(sender_id) && chat.getSender().equals(userid)) {
//                                HashMap<String, Object> hashMap = new HashMap<>();
//                                hashMap.put("seen", true);
//                                snapshot.getRef().updateChildren(hashMap);
//
//                            }

                    } catch (Exception e) {

                    }

                }
            }
        }


        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }

    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        setupUI(findViewById(R.id.messagerelative));
//        if (ContextCompat.checkSelfPermission(MessagingActivity.this,
//                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
//            requestAudioPermission();
//
//        }

        //progressBar  = findViewById(R.id.pregressbarmessage);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("");
//        getSupportActionBar().setDisplayHomeAsUpEnabledfsearch(true);
//        cd = new ConnectionDetector(this);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//                //startActivity(new Intent(MessagingActivity.this, MessagingActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//
////                    friends fragment = new friends();
////                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
////                    transaction.replace(R.id.messagerelative, fragment);
////                    transaction.commit();
//
//            }
//
//
//        });



        mChatToolbar = findViewById(R.id.chat_app_bar);
        cancelvoicebtn = findViewById(R.id.cancel_voice);
        setSupportActionBar(mChatToolbar);
        getSupportActionBar().setTitle("");
        mimagesList = new ArrayList<Uri>();
        ActionBar actionBar = getSupportActionBar();
        final String[] mPermission = {Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        actionBar.setDisplayShowCustomEnabled(true);
        mChatToolbar.findViewById(R.id.chat_app_bar);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view = inflater.inflate(R.layout.chat_bar_layout, null);
        actionBar.setCustomView(action_bar_view);

        LinearLayout mUserLayout = action_bar_view.findViewById(R.id.userLayout);
        mUserLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent send = new Intent(MessagingActivity.this, Userdetails.class);
//                send.putExtra("user_id", receiver_id);
//                send.putExtra("user", receiver_name);
//                send.putExtra("photo", receiver_image);
//                send.putExtra("email", receiver_email);
//                send.putExtra("phone", phone);
//
//                startActivity(send);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });

        receiver_id = getIntent().getStringExtra("user_id");


//        FirebaseDatabase.getInstance().getReference().child("Users").child(receiver_id).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                Long time = (Long) dataSnapshot.child("status").getValue();
//
//                Date date = new Date(time);
//                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
//                String posTime = dateFormat.format(date);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


        username = findViewById(R.id.custom_bar_title);
        //msgProgressBar = findViewById(R.id.bar_msg);
        profileimage = findViewById(R.id.custom_bar_image);

        receiver_image = getIntent().getStringExtra("uImage");
        receiver_name = getIntent().getStringExtra("uName");
        if(receiver_name!=null && receiver_image!=null){
            username.setText(receiver_name);
            Glide.with(getApplicationContext()).asBitmap().load(receiver_image).apply(new RequestOptions().placeholder(R.drawable.avatar)).into(profileimage);
        }



        final EditText searchEdit = findViewById(R.id.search_editText);
        LastSeen = findViewById(R.id.custom_bar_seen);
        ImageView searcImageView = findViewById(R.id.search);
//        cancelvoicebtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //umair
//            }
//        });
        searcImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchEdit.setVisibility(View.VISIBLE);
                mChatToolbar.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.equals("")) {

                } else {
                    try {
                        messageAdapter.getFilter().filter(editable.toString().toLowerCase());
                    } catch (Exception e) {
                    }
                }

            }
        });


        searchEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (event.getRawX() >= (searchEdit.getRight() - searchEdit.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        searchEdit.setVisibility(View.GONE);
                        mChatToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        searchEdit.setText("");
                    }
                }
                return false;
            }
        });


        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Owners").child(receiver_id);
        userRef.keepSynced(true);
        userRef.addValueEventListener(userDataListener);


        mAuth = FirebaseAuth.getInstance();
        sender_id = mAuth.getUid();
        fuserid = sender_id;
        seeRef = FirebaseDatabase.getInstance().getReference().child("Chatlist").child(receiver_id).child(sender_id);

        if (sender_id.equals(mAuth.getUid())) {
            unreadReference = FirebaseDatabase.getInstance().getReference().child("Unread").child(sender_id).child(receiver_id);
            setNoRef = FirebaseDatabase.getInstance().getReference().child("Unread").child(receiver_id).child(sender_id);
            setNoRef.child("unread").setValue(0);
            Map unreadMap = new HashMap<>();
            unreadMap.put("unread", 0);
            unreadMap.put("online", "yes");

            unreadReference.setValue(unreadMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    checkStatusRef = FirebaseDatabase.getInstance().getReference().child("Unread").child(receiver_id).child(sender_id);
                    checkStatusRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild("online")) {
                                status = dataSnapshot.child("online").getValue().toString();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    if (status.equals("")) {
                        unreadReference.addValueEventListener(unreadListenr);
                    } else if (status.equals("no")) {
                        unreadReference.addValueEventListener(unreadListenr);
                    }
                }
            });
            checkReference = FirebaseDatabase.getInstance().getReference().child("Chatlist").child(sender_id);

            checkReference.addValueEventListener(setUnreadListener);
            seeRef.addValueEventListener(setSeen);

        }


        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        mchat = new ArrayList<>();

        btn_send = findViewById(R.id.message_btn_send);
        txt_send = findViewById(R.id.txt_send);
        imageViewLock = findViewById(R.id.imageViewLock);
        imageViewStop = findViewById(R.id.imageViewStop);
        imageViewLockArrow = findViewById(R.id.imageViewLockArrow);
        layoutDustin = findViewById(R.id.layoutDustin);
        layoutMessage = findViewById(R.id.layoutMessage);
        timeText = findViewById(R.id.textViewTime);
        layoutSlideCancel = findViewById(R.id.layoutSlideCancel);
        layoutLock = findViewById(R.id.layoutLock);
        backImage = findViewById(R.id.backlayout);


        /////////////////////attachment anim
        mRevealView = (LinearLayout) findViewById(R.id.reveal_items);
        mRevealView.setVisibility(View.GONE);
        gallery_btn = (ImageView) findViewById(R.id.imggallery);
        photo_btn = (ImageView) findViewById(R.id.Imgphoto);
        video_btn = (ImageView) findViewById(R.id.imgvideo);
        audio_btn = (ImageView) findViewById(R.id.ImgAudio);
        location_btn = (ImageView) findViewById(R.id.Imglocation);
        contact_btn = (ImageView) findViewById(R.id.imgcontact);
        gallery_btn.setOnClickListener(this);
        photo_btn.setOnClickListener(this);
        video_btn.setOnClickListener(this);
        audio_btn.setOnClickListener(this);
        location_btn.setOnClickListener(this);
        contact_btn.setOnClickListener(this);

        /////////////////////attachment anim


        handler = new Handler(Looper.getMainLooper());

        animBlink = AnimationUtils.loadAnimation(MessagingActivity.this,
                R.anim.blink);
        animJump = AnimationUtils.loadAnimation(MessagingActivity.this,
                R.anim.jump);
        animJumpFast = AnimationUtils.loadAnimation(MessagingActivity.this,
                R.anim.jump_fast);
        imageViewMic = findViewById(R.id.imageViewMic);
        dustin = findViewById(R.id.dustin);
        dustin_cover = findViewById(R.id.dustin_cover);

        btn_voice = findViewById(R.id.message_btn_voice);
        select_item = findViewById(R.id.select_item);
        layoutSlideCancel = findViewById(R.id.layoutSlideCancel);
        dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, MessagingActivity.this.getResources().getDisplayMetrics());

        layoutLock = findViewById(R.id.layoutLock);
        //btn=(Button) findViewById(R.id.call);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        Context context = this.getApplicationContext();

        //sinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());


        txt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRevealView.setVisibility(View.GONE);
            }
        });
        txt_send.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    if (btn_send.getVisibility() != View.GONE) {
                        btn_send.setVisibility(View.GONE);
                        btn_send.animate().scaleX(0f).scaleY(0f).setDuration(100).setInterpolator(new LinearInterpolator()).start();
                    }
                } else {
                    if (btn_send.getVisibility() != View.VISIBLE && !isLocked) {
                        btn_send.setVisibility(View.VISIBLE);
                        btn_send.animate().scaleX(1f).scaleY(1f).setDuration(100).setInterpolator(new LinearInterpolator()).start();
                        //umair
                    }
                    if (thisflag) {
                        btn_send.setVisibility(View.VISIBLE);
                        btn_send.animate().scaleX(1f).scaleY(1f).setDuration(100).setInterpolator(new LinearInterpolator()).start();
                    }
                }
            }
        });


        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seenListener != null) {
                    reference.removeEventListener(seenListener);
                }if (userRef != null) {
                    userRef.removeEventListener(userDataListener);
                } if (getIntent().getStringExtra("for") != null) {
                    Intent intent = new Intent(MessagingActivity.this, BringoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        videoreference = FirebaseStorage.getInstance().getReference("uploadsvideo");
        mfirebaseVoice = FirebaseStorage.getInstance().getReference();
        //mfirebaseVoice= FirebaseStorage.getInstance().getReference();
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/recorded_audio.3gp";


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg = txt_send.getText().toString().trim();
                txt_send.setText("");
                sendMessage(sender_id, receiver_id, msg, "", "text");


            }
        });
        ConversionRef = FirebaseDatabase.getInstance().getReference().child("Chatlist");



        select_item.setOnClickListener(new View.OnClickListener() {
            //            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                View mView = LayoutInflater.from(MessagingActivity.this).inflate(R.layout.attachment_sheet, null);

                RelativeLayout mCameraLayout, mGalleryLayout, mVideoLayout, mLocationLayout, mContactLayout;
                LinearLayout mCancelLayout;

                mCameraLayout = mView.findViewById(R.id.cameraLayout);
                mGalleryLayout = mView.findViewById(R.id.galleryLayout);
                mVideoLayout = mView.findViewById(R.id.videoLayout);
                mLocationLayout = mView.findViewById(R.id.locationLayout);
                mContactLayout = mView.findViewById(R.id.contactLayout);
                mCancelLayout = mView.findViewById(R.id.cancelLayout);

                mCameraLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ContextCompat.checkSelfPermission(MessagingActivity.this,
                                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            opencamera();
                        } else {
                            requestCameraPermission();

                        }
                        mbottomSheetDialog.cancel();
                    }
                });

                mGalleryLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ContextCompat.checkSelfPermission(MessagingActivity.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            openImage();
                        } else {
                            requestStoragePermission();

                        }
                        mbottomSheetDialog.cancel();
                    }
                });

                mVideoLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ContextCompat.checkSelfPermission(MessagingActivity.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            openVideo();
                        } else {
                            requestStoragePermission();

                        }
                        mbottomSheetDialog.cancel();
                    }
                });

                mLocationLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openLocation();
                        mbottomSheetDialog.cancel();
                    }
                });

                mContactLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ContextCompat.checkSelfPermission(MessagingActivity.this,
                                Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {

                            openContact();
                        } else {
                            requestContactPermission();

                        }
                        mbottomSheetDialog.cancel();
                    }
                });

                mCancelLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mbottomSheetDialog.cancel();
                    }
                });

                mbottomSheetDialog = new BottomSheetDialog(mView.getContext());
                mbottomSheetDialog.setContentView(mView);
                mbottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mbottomSheetDialog.show();


//                int cx = (mRevealView.getRight() + mRevealView.getLeft());
//                int cy = mRevealView.getBottom();
//                int radius = Math.max(mRevealView.getWidth(), mRevealView.getHeight());
//
//                if (hidden) {
//                    Animator anim = android.view.ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, 0, radius);
//                    mRevealView.setVisibility(View.VISIBLE);
//                    anim.start();
//                    txt_send.onEditorAction(EditorInfo.IME_ACTION_DONE);
//                    hidden = false;
//                } else {
//                    Animator anim = android.view.ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, radius, 0);
//                    anim.addListener(new AnimatorListenerAdapter() {
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//                            super.onAnimationEnd(animation);
//                            mRevealView.setVisibility(View.INVISIBLE);
//                            hidden = true;
//                        }
//                    });
//                    anim.start();
//                }
            }
        });


        imageViewStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLocked = false;
                stopRecording(RecordingBehaviour.LOCK_DONE);

            }
        });

        btn_voice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, final MotionEvent motionEvent) {
                try {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), mPermission[0])
                            != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(getApplicationContext(), mPermission[1])
                                    != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(getApplicationContext(), mPermission[2])
                                    != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(MessagingActivity.this,
                                mPermission, REQUEST_CODE_PERMISSION);
                        return false;

                        // If any permission aboe not allowed by user, this condition will execute every tim, else your else part will work
                    } else {
                        if (checkflagforme) {
                            myplayer.pause();
                        }
                        MessagingAdapter.mPrePos = -1;
                        if (isDeleting) {
                            return true;
                        }

                        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                            cancelOffset = (float) (btn_voice.getX() / 2.8);
                            lockOffset = (float) (btn_voice.getX() / 2.5);

                            if (firstX == 0) {
                                firstX = motionEvent.getRawX();
                            }

                            if (firstY == 0) {
                                firstY = motionEvent.getRawY();
                            }

                            try {
                                if (ActivityCompat.checkSelfPermission(getApplicationContext(), mPermission[0])
                                        != PackageManager.PERMISSION_GRANTED ||
                                        ActivityCompat.checkSelfPermission(getApplicationContext(), mPermission[1])
                                                != PackageManager.PERMISSION_GRANTED ||
                                        ActivityCompat.checkSelfPermission(getApplicationContext(), mPermission[2])
                                                != PackageManager.PERMISSION_GRANTED) {

                                    ActivityCompat.requestPermissions(MessagingActivity.this,
                                            mPermission, REQUEST_CODE_PERMISSION);

                                    // If any permission aboe not allowed by user, this condition will execute every tim, else your else part will work
                                } else {
                                    startRecording();
                                    notify = true;


                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {

                            } catch (Exception e) {
                            }
                            //requestAudioPermissions();

                            //startRecording();

                        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP
                                || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {

                            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                                stopRecording(RecordingBehaviour.RELEASED);
                                //umair
                            }

                        } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {

                            if (stopTrackingAction) {
                                return true;
                            }

                            UserBehaviour direction = UserBehaviour.NONE;

                            float motionX = Math.abs(firstX - motionEvent.getRawX());
                            float motionY = Math.abs(firstY - motionEvent.getRawY());

                            if (motionX > directionOffset &&
                                    motionX > directionOffset &&
                                    lastX < firstX && lastY < firstY) {

                                if (motionX > motionY && lastX < firstX) {
                                    //umair
                                    direction = UserBehaviour.CANCELING;

                                } else if (motionY > motionX && lastY < firstY) {

                                    direction = UserBehaviour.LOCKING;
                                }

                            } else if (motionX > motionY && motionX > directionOffset && lastX < firstX) {
                                direction = UserBehaviour.CANCELING;
                            } else if (motionY > motionX && motionY > directionOffset && lastY < firstY) {
                                direction = UserBehaviour.LOCKING;
                            }

                            if (direction == UserBehaviour.CANCELING) {
                                if (userBehaviour == UserBehaviour.NONE || motionEvent.getRawY() + btn_voice.getWidth() / 2 > firstY) {
                                    userBehaviour = UserBehaviour.CANCELING;
                                }

                                if (userBehaviour == UserBehaviour.CANCELING) {

                                    translateX(-(firstX - motionEvent.getRawX()));
                                }
                            } else if (direction == UserBehaviour.LOCKING) {


                                if (userBehaviour == UserBehaviour.NONE || motionEvent.getRawX() + btn_voice.getWidth() / 2 > firstX) {
                                    userBehaviour = UserBehaviour.LOCKING;
                                }

                                if (userBehaviour == UserBehaviour.LOCKING) {

                                    translateY(-(firstY - motionEvent.getRawY()));
                                }
                            }
                            cancelvoicebtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    thisflag = true;
                                    stopTrackingAction = true;
                                    imageViewStop.setVisibility(View.GONE);
                                    stopRecording(RecordingBehaviour.CANCELED);

                                }
                            });

                            lastX = motionEvent.getRawX();
                            lastY = motionEvent.getRawY();
                        }
                        view.onTouchEvent(motionEvent);
                        return true;


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }

            }
        });


        FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(sender_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sender_name = dataSnapshot.child("name").getValue().toString().toUpperCase();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        readMessage(sender_id, receiver_id);

        FirebaseDatabase.getInstance().getReference("Chats").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.exists()) {
                            if (snapshot.getKey().contains(receiver_id) && snapshot.getKey().contains(sender_id)) {
                                if (receiver_id.equals(sender_id) && snapshot.getKey().equals(receiver_id + "" + sender_id)) {
                                    getchatroot = snapshot.getKey();
                                    seenMessage(receiver_id);
                                    return;
                                } else {
                                    if (receiver_id.equals(sender_id)) {
                                        getchatroot = receiver_id + "" + sender_id;
                                        seenMessage(receiver_id);
                                        return;
                                    } else {
                                        getchatroot = snapshot.getKey();
                                        seenMessage(receiver_id);
                                        return;
                                    }
                                }

                            } else {
                                getchatroot = "";
                            }

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    protected void onStop() {
        super.onStop();
        unreadReference.removeEventListener(unreadListenr);
        checkReference.removeEventListener(setUnreadListener);
        seeRef.removeEventListener(setSeen);
        FirebaseDatabase.getInstance().getReference().child("Unread").child(sender_id).child(receiver_id).child("online").setValue("no").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                }else {

                }
            }
        });
    }


    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    //hideSoftKeyboard(MessagingActivity.this);
                    return false;
                }
            });
        }


        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    //umair
    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            //When permission is not granted by user, show them message why this permission is needed.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {

                //Give user option to still opt-in the permissions
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);

            } else {
                // Show user dialog to grant permission to record audio
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);
            }
        }
//       else if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            //When permission is not granted by user, show them message why this permission is needed.
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                Toast.makeText(this, "Please grant permissions to record audio", Toast.LENGTH_LONG).show();
//
//                //Give user option to still opt-in the permissions
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                        read);
//
//            } else {
//                // Show user dialog to grant permission to record audio
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.RECORD_AUDIO},
//                        read);
//            }
//        }
        else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            //When permission is not granted by user, show them message why this permission is needed.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "Please grant permissions to record audio", Toast.LENGTH_LONG).show();

                //Give user option to still opt-in the permissions
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        write);

            } else {
                // Show user dialog to grant permission to record audio
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        write);
            }
        }

        //If permission is granted, then go ahead recording audio
        else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {

            //Go ahead with recording audio now
            startRecording();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case CAMERA_PERMISSION_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    opencamera();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permissions Denied to record audio", Toast.LENGTH_LONG).show();
                }
                return;

            }

            case STORAGE_PERMISSION_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    openImage();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permissions Denied to record audio", Toast.LENGTH_LONG).show();
                }
                return;


            }
            case REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    openLocation();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permissions Denied to record audio", Toast.LENGTH_LONG).show();
                }
                return;


            }
        }
    }


    private void translateY(float y) {

        if (y < -lockOffset) {
            locked();
            btn_voice.setTranslationY(0);
            return;
        }

        if (layoutLock.getVisibility() != View.VISIBLE) {
            layoutLock.setVisibility(View.VISIBLE);
        }

        btn_voice.setTranslationY(y);
        layoutLock.setTranslationY(y / 2);
        btn_voice.setTranslationX(0);
    }

    private void translateX(float x) {
        if (x < -cancelOffset) {
            canceled();
            btn_voice.setTranslationX(0);
            layoutSlideCancel.setTranslationX(0);
            return;
        }

        btn_voice.setTranslationX(x);
        layoutSlideCancel.setTranslationX(x);
        layoutLock.setTranslationY(0);
        btn_voice.setTranslationY(0);

        if (Math.abs(x) < imageViewMic.getWidth() / 2) {
            if (layoutLock.getVisibility() != View.VISIBLE) {
                layoutLock.setVisibility(View.VISIBLE);
            }
        } else {
            if (layoutLock.getVisibility() != View.GONE) {
                layoutLock.setVisibility(View.GONE);
            }
        }
    }


    private void openLocation() {
        cd = new ConnectionDetector(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            GetLocation();
        }
        if (cd.isConnected()) {

        } else {
            Toast.makeText(getApplicationContext(), "error: " + "The Connection could not be found. Please Check your Connection!!", Toast.LENGTH_SHORT).show();

        }

    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MessagingActivity.this,
                                    new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_CODE
                            );
                            ActivityCompat.requestPermissions(MessagingActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, write
                            );
                            ActivityCompat.requestPermissions(MessagingActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, read
                            );
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, write);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, read);

        }
    }

    private void requestAudioPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.RECORD_AUDIO)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MessagingActivity.this,
                                    new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_RECORD_AUDIO);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_RECORD_AUDIO);

        }
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MessagingActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    private void requestContactPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_CONTACTS)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MessagingActivity.this,
                                    new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.CALL_PHONE}, CONTACT_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, CONTACT_PERMISSION_CODE);

        }
    }


    //////
    public void GetLocation() {
        if (ActivityCompat.checkSelfPermission(MessagingActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (MessagingActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MessagingActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                String locat1 = GetExactLocation(Double.parseDouble(lattitude), Double.parseDouble(longitude));
                String locat = "http://maps.google.com/maps?" + locat1;
                //String locat = " "+Double.parseDouble(lattitude)+","+Double.parseDouble(longitude);
                sendMessage(sender_id, receiver_id, lattitude + "," + longitude, "location", "location");


            } else if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                String locat1 = GetExactLocation(Double.parseDouble(lattitude), Double.parseDouble(longitude));
                String locat = "http://maps.google.com/maps?" + locat1;
                //String locat = "http://maps.google.com/maps?"+Double.parseDouble(lattitude)+","+Double.parseDouble(longitude);
                sendMessage(sender_id, receiver_id, lattitude + "," + longitude, "location", "location");

            } else if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                String locat1 = GetExactLocation(Double.parseDouble(lattitude), Double.parseDouble(longitude));
                String locat = "http://maps.google.com/maps?" + locat1;
                //String locat = "http://maps.google.com/maps?"+Double.parseDouble(lattitude)+","+Double.parseDouble(longitude);
                sendMessage(sender_id, receiver_id, lattitude + "," + longitude, "location", "location");
            } else {
                Toast.makeText(this, "Unble to Trace your location", Toast.LENGTH_SHORT).show();


            }
        }
    }

    private void locked() {
        stopTrackingAction = true;
        stopRecording(RecordingBehaviour.LOCKED);
        cancelvoicebtn.setVisibility(View.VISIBLE);
        imageViewStop.setVisibility(View.VISIBLE);
        isLocked = true;
//        Animator animator = ViewAnimationUtils.createCircularReveal()
    }

    private void canceled() {
        stopTrackingAction = true;
        stopRecording(RecordingBehaviour.CANCELED);
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void hideRevealView() {
        if (mRevealView.getVisibility() == View.VISIBLE) {
            mRevealView.setVisibility(View.GONE);
            hidden = true;
        }
    }


    private void openContact() {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
//        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(intent, REQUEST_SELECT_CONTACT);
//        }

        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, 320);

    }


    private void opencamera() {
        int Permission_ALL = 1;
        Intent photo = new Intent(MessagingActivity.this, Cameramethod.class);
        photo.putExtra("activity", "messsage");
//        String[] permissions = {Manifest.permission.CAMERA};
//        if (!haspermission(this, permissions)) {
//            ActivityCompat.requestPermissions(this, permissions, Permission_ALL);
//        }
//        Toast.makeText(MessagingActivity.this, "asda", Toast.LENGTH_SHORT).show();

        startActivity(photo);

    }

    private void openAudio() {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER);
        startActivity(intent);
    }

    public String GetExactLocation(double lati, double longi) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lati, longi, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            } else {
                Toast.makeText(this, "Error ", Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();

        }
        return strAdd;
    }


    private void seenMessage(final String userid) {
        reference = FirebaseDatabase.getInstance().getReference("Chats").child(getchatroot);
        reference.addValueEventListener(seenListener);
    }

    private void openImage() {
        try {
            BSImagePicker pickerDialog = new BSImagePicker.Builder("com.example.mystore.fileprovider")
                    .isMultiSelect()
                    .setMinimumMultiSelectCount(1)
                    .setMaximumMultiSelectCount(25)
                    .setPeekHeight(Utils.dp2px(550))
                    .setMultiSelectDoneTextColor(R.color.colorAccent)
                    .build();
            pickerDialog.show(getSupportFragmentManager(), "picker");
        }catch (Exception e){

        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = MessagingActivity.this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        final ProgressDialog pd = new ProgressDialog(MessagingActivity.this);
        pd.setMessage("Uploading");
        pd.show();

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
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("sender", sender_id);
                        hashMap.put("reciver", receiver_id);
                        hashMap.put("image", mUri);
                        hashMap.put("video", "default");
                        hashMap.put("prop", "default");
                        hashMap.put("voice", "default");
                        hashMap.put("status", "not available");
                        hashMap.put("message", "default");
                        hashMap.put("chatid", id);
                        hashMap.put("isseen", false);
                        hashMap.put("timestamp", ServerValue.TIMESTAMP);
                        reference.child("Chats").child(id).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    UnreadMessage("\uD83D\uDCF7 New Image");
                                    sendNotifiaction(receiver_id, sender_name, "\uD83D\uDCF7 New Image");
                                    pd.dismiss();
                                } else {
                                    Toast.makeText(MessagingActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    pd.dismiss();
                                }
                            }
                        });


                    } else {
                        Toast.makeText(MessagingActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MessagingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } else {
            Toast.makeText(MessagingActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void openVideo() {
        Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent1.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(intent1, RC_VIDEO_PICKER);
    }

    private String getFileExtension1(Uri uri) {
        ContentResolver contentResolver = MessagingActivity.this.getContentResolver();
        MimeTypeMap mimeTypeMap1 = MimeTypeMap.getSingleton();
        return mimeTypeMap1.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {
            case (320):
                if (resultCode == Activity.RESULT_OK) {
                    contactData = data.getData();
                    Cursor c = getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        String hasNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String num = "", name = "";
                        ContentResolver cr = getContentResolver();
                        Cursor c1 = cr.query(contactData, null, null, null, null);
                        if (c1.moveToFirst()) {
                            name = c1.getString(c1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                        }
                        Cursor numbers = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                        while (numbers.moveToNext()) {
                            num = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        }
                        sendMessage(sender_id, receiver_id, "Name: " + name + "\n" + "Phone No: " + num, "contact", "text");

                    }
                    break;
                }
        }


        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK) {

            if (data != null) {

                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();

                    for (int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        mimagesList.add(imageUri);
                    }
                    Log.d("Gallery url ", String.valueOf(imageUri));
                    startActivity(new Intent(MessagingActivity.this, MuImagesActivity.class));
                } else {

                    Uri uri = data.getData();
                    mimagesList.add(uri);
                    Log.d("Gallery url ", String.valueOf(imageUri));
                    startActivity(new Intent(MessagingActivity.this, MuImagesActivity.class));
                }
            }
        }

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageUri = getImageUri(getApplicationContext(), bitmap);
            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(MessagingActivity.this, "Upload in preogress", Toast.LENGTH_SHORT).show();
            } else {
                Log.d("Gallery url ", String.valueOf(imageUri));
                uploadImage();
            }
        }
        if (requestCode == RC_VIDEO_PICKER && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            videouri = data.getData();

            if (uploadTask1 != null && uploadTask1.isInProgress()) {
                Toast.makeText(MessagingActivity.this, "Upload in preogress", Toast.LENGTH_SHORT).show();
            } else {
                notify = true;
                uploadVideo();
            }
        }


    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void uploadVideo() {

        String geturitovideo = videouri.toString();
        mchat.add(new Chats("video", ServerValue.TIMESTAMP, fuserid, false, geturitovideo));
        messageAdapter = new MessagingAdapter(MessagingActivity.this, mchat);
        recyclerView.setAdapter(messageAdapter);
        messageAdapter.notifyDataSetChanged();

        if (videouri != null) {
            final StorageReference fileReference1 = videoreference.child(System.currentTimeMillis()
                    + "." + getFileExtension1(videouri));

            uploadTask1 = fileReference1.putFile(videouri);
            uploadTask1.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return fileReference1.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull final Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String mUri1 = downloadUri.toString();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        String id = reference.child("Chats").push().getKey();
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("message", mUri1);
                        hashMap.put("time", ServerValue.TIMESTAMP);
                        hashMap.put("seen", false);
                        hashMap.put("type", "video");
                        hashMap.put("chatid", id);
                        hashMap.put("sender", sender_id);
                        hashMap.put("delivery", "not available");
                        if (getchatroot.equals("")) {
                            reference.child("Chats").child(sender_id + receiver_id).child(id).setValue(hashMap);
                        } else {
                            reference.child("Chats").child(getchatroot).child(id).setValue(hashMap);
                        }
                        UnreadMessage("\uD83C\uDFA5 New Video");
                        sendNotifiaction(receiver_id, sender_name, "\uD83C\uDFA5 New Video");
                        //pd.dismiss();

                    } else {
                        Toast.makeText(MessagingActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        //pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //pd.dismiss();

                }
            });
        } else {
            Toast.makeText(MessagingActivity.this, "No video selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void upload() {
        mchat.add(new Chats("voice", ServerValue.TIMESTAMP, sender_id, false, ""));
        recyclerView.smoothScrollToPosition(mchat.size() - 1);

        final StorageReference fileparth = mfirebaseVoice.child("Audio").child("new_audio " + System.currentTimeMillis() + " .3gp");
        voiceuri = Uri.fromFile(new File(mFileName));
        uploadTask2 = fileparth.putFile(voiceuri);
        uploadTask2.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    Toast.makeText(MessagingActivity.this, task.toString(), Toast.LENGTH_SHORT).show();
                    throw task.getException();
                }

                return fileparth.getDownloadUrl();
            }
        }).addOnCompleteListener(
                new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {

                            Uri downloadUri = task.getResult();
                            String mUri2 = downloadUri.toString();
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                            String id = reference.child("Chats").push().getKey();
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("message", mUri2);
                            hashMap.put("time", ServerValue.TIMESTAMP);
                            hashMap.put("seen", false);
                            hashMap.put("type", "voice");
                            hashMap.put("chatid", id);
                            hashMap.put("sender", sender_id);
                            hashMap.put("delivery", "not available");

                            if (getchatroot.equals("")) {
                                reference.child("Chats").child(sender_id + receiver_id).child(id).setValue(hashMap);
                            } else {
                                reference.child("Chats").child(getchatroot).child(id).setValue(hashMap);
                            }

                            UnreadMessage("\uD83C\uDFA7 New Voice Message");
                            sendNotifiaction(receiver_id, sender_name, "\uD83C\uDFA7 New Voice Message");


                        } else {
                            Toast.makeText(MessagingActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MessagingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        //mchat.add(new Chat(sender_id,receiver_id,"default","default","default",false,"",))
    }


    private void startRecording() {
        txt_send.setVisibility(View.GONE);
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        if (recordingListener != null)
            recordingListener.onRecordingStarted();

        stopTrackingAction = false;

        select_item.setVisibility(View.INVISIBLE);
        btn_voice.animate().scaleXBy(1f).scaleYBy(1f).setDuration(200).setInterpolator(new OvershootInterpolator()).start();
        timeText.setVisibility(View.VISIBLE);
        layoutLock.setVisibility(View.VISIBLE);
        layoutSlideCancel.setVisibility(View.VISIBLE);
        imageViewMic.setVisibility(View.VISIBLE);
        timeText.startAnimation(animBlink);
        imageViewLockArrow.clearAnimation();
        imageViewLock.clearAnimation();
        imageViewLockArrow.startAnimation(animJumpFast);
        imageViewLock.startAnimation(animJump);


        try {
            mRecorder.prepare();
        } catch (IOException e) {

        }

        mRecorder.start();

        if (audioTimer == null) {
            audioTimer = new Timer();
            timeFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        }

        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        timeText.setText(timeFormatter.format(new Date(audioTotalTime * 1000)));
                        audioTotalTime++;
                    }
                });
            }
        };

        audioTotalTime = 0;
        audioTimer.schedule(timerTask, 0, 1000);

    }


    private void stopRecording(RecordingBehaviour recordingBehaviour) {

        MessagingAdapter.playingStatus = "true";
        stopTrackingAction = true;
        firstX = 0;
        firstY = 0;
        lastX = 0;
        lastY = 0;

        userBehaviour = UserBehaviour.NONE;

        btn_voice.animate().scaleX(1f).scaleY(1f).translationX(0).translationY(0).setDuration(100).setInterpolator(new LinearInterpolator()).start();
        layoutSlideCancel.setTranslationX(0);
        layoutSlideCancel.setVisibility(View.GONE);

        layoutLock.setVisibility(View.GONE);
        layoutLock.setTranslationY(0);
        imageViewLockArrow.clearAnimation();
        imageViewLock.clearAnimation();

        if (thisflag) {
            cancelvoicebtn.setVisibility(View.GONE);
            timeText.clearAnimation();
            timeText.setVisibility(View.INVISIBLE);
            imageViewMic.setVisibility(View.INVISIBLE);


            timerTask.cancel();
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
            delete();
            if (recordingListener != null)
                recordingListener.onRecordingCanceled();
            thisflag = false;
        }


        if (isLocked) {
            return;
        }

        if (recordingBehaviour == RecordingBehaviour.LOCKED) {


            if (recordingListener != null)
                recordingListener.onRecordingLocked();

        } else if (thisflag || recordingBehaviour == RecordingBehaviour.CANCELED) {
            timeText.clearAnimation();
            timeText.setVisibility(View.INVISIBLE);
            imageViewMic.setVisibility(View.INVISIBLE);


            timerTask.cancel();
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
            delete();

            if (recordingListener != null)
                recordingListener.onRecordingCanceled();

        } else if (recordingBehaviour == RecordingBehaviour.RELEASED || recordingBehaviour == RecordingBehaviour.LOCK_DONE) {
            cancelvoicebtn.setVisibility(View.GONE);
            timeText.clearAnimation();
            timeText.setVisibility(View.INVISIBLE);
            imageViewMic.setVisibility(View.INVISIBLE);
            txt_send.setVisibility(View.VISIBLE);
            select_item.setVisibility(View.VISIBLE);
            imageViewStop.setVisibility(View.GONE);
            try {
                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;
                upload();
            } catch (Exception e) {
            }
            try {
                timerTask.cancel();
            } catch (Exception e) {
                //Toast.makeText(this, "umair" + e.getMessage(), Toast.LENGTH_SHORT).show();
                requestPermissions();
            }

            if (recordingListener != null)
                recordingListener.onRecordingCompleted();
        }


    }


    private void sendMessage(String sender, final String reciver, String message, String forContact, String type) {

        String msg = "";
        /*System.out.println(df.format(calobj.getTime()));*/
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        final String id = reference.child("Chats").push().getKey();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", message);
        hashMap.put("time", ServerValue.TIMESTAMP);
        hashMap.put("seen", false);
        hashMap.put("type", type);
        hashMap.put("chatid", id);
        hashMap.put("sender", sender);
        hashMap.put("delivery", "not available");
        if (getchatroot.equals("")) {
            reference.child("Chats").child(sender + reciver).child(id).setValue(hashMap);
        } else {
            reference.child("Chats").child(getchatroot).child(id).setValue(hashMap);
        }


        if (forContact.equals("")) {
            msg = message;
            UnreadMessage(message);
        } else if (forContact.equals("location")) {
            msg = "\uD83D\uDCCD Location";
            UnreadMessage("\uD83D\uDCCD Location");
        } else if (forContact.equals("contact")) {
            msg = "\uD83D\uDC64 New Contact";
            UnreadMessage("\uD83D\uDC64 New Contact");
        }
        sendNotifiaction(reciver, sender_name, msg);

    }

    private void UnreadMessage(String message) {
        Map concMap = new HashMap<>();
        concMap.put("last", message);
        concMap.put("time", ServerValue.TIMESTAMP);
        concMap.put("unread", count);
        concMap.put("name", receiver_name.toLowerCase());
        concMap.put("image", receiver_image);
        concMap.put("status", "sent");
        concMap.put("who", sender_id);

        Map concMap2 = new HashMap<>();
        concMap2.put("last", message);
        concMap2.put("time", ServerValue.TIMESTAMP);
        concMap2.put("unread", 0);
        concMap2.put("name", receiver_name.toLowerCase());
        concMap2.put("image", receiver_image);
        concMap2.put("status", "sent");
        concMap2.put("who", sender_id);

        Map conc1Map = new HashMap<>();
        conc1Map.put("last", message);
        conc1Map.put("time", ServerValue.TIMESTAMP);
        conc1Map.put("unread", 0);
        conc1Map.put("name", receiver_name.toLowerCase());
        conc1Map.put("image", receiver_image);
        conc1Map.put("status", "sent");
        conc1Map.put("who", sender_id);

        Map message1TextDetail = new HashMap<>();
        message1TextDetail.put(sender_id + "/" + receiver_id, conc1Map);
        if (status.equals("")) {
            message1TextDetail.put(receiver_id + "/" + sender_id, concMap);
        } else if (status.equals("no")) {
            message1TextDetail.put(receiver_id + "/" + sender_id, concMap);
        } else {
            message1TextDetail.put(receiver_id + "/" + sender_id, concMap2);
        }
        ConversionRef.updateChildren(message1TextDetail, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (status.equals("") || status.equals("no")) {
                    unreadReference.child("unread").setValue(count).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Users").child("Owners").child(receiver);
        tokens.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Data data = new Data(sender_id, R.mipmap.ic_launcher, username + ": " + message, "New Message",
                            receiver_id);
                    String mToken = dataSnapshot.child("token").getValue().toString();
                    Toast.makeText(MessagingActivity.this, ""+mToken, Toast.LENGTH_SHORT).show();
                    Sender sender = new Sender(data, mToken);

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

    private void readMessage(final String id, final String userid) {

        //////////////////////


        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mchat.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.exists()) {
                            if (snapshot.getKey().contains(FirebaseAuth.getInstance().getUid() + userid) || snapshot.getKey().contains(userid + FirebaseAuth.getInstance().getUid())) {
                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                    if (snapshot1.hasChild("deleted") && snapshot1.child("deleted").getValue().toString().equals(FirebaseAuth.getInstance().getUid())) {

                                    } else {
                                        Chats chat = snapshot1.getValue(Chats.class);
                                        mchat.add(chat);
                                    }
                                    //mchat.add(new Chats(snapshot1.child("chatid").getValue().toString(), snapshot1.child("message").getValue().toString(), snapshot1.child("type").getValue().toString(), snapshot1.child("time").getValue().toString(), snapshot1.child("delivery").getValue().toString(), snapshot1.child("sender").getValue().toString(), (Boolean) snapshot1.child("seen").getValue()));
                                }
                            }

                            Collections.sort(mchat, new Comparator<Chats>() {
                                @Override
                                public int compare(Chats lhs, Chats rhs) {
                                    // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                                    return lhs.getTime() < rhs.getTime() ? -1 : (lhs.getTime() < rhs.getTime() ) ? 1 : 0;
                                }
                            });

//

                            messageAdapter = new MessagingAdapter(MessagingActivity.this, mchat);
                            recyclerView.setAdapter(messageAdapter);
                            messageAdapter.notifyDataSetChanged();
                        }

                    }


//                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
//                        if (snapshot.getKey().contains(receiver_id) && snapshot.getKey().contains(sender_id)){
//                            for (DataSnapshot snapshot1: snapshot.getChildren()){
//                                if (snapshot1.exists()){
//                                    try {
//                                        Chat chat = snapshot1.getValue(Chat.class);
//                                        if (!userid.isEmpty() && !id.isEmpty()) {
//                                            if (chat.getReciver().equals
//                                                    (FirebaseAuth.getInstance().getCurrentUser().getUid())
//                                                    && chat.getSender().equals(userid) ||
//                                                    chat.getReciver().equals(userid)
//                                                            && chat.getSender().equals(id)) {
////                                if (snapshot.hasChild("delete")) {
////                                    if (!chat.getDelete().equals(FirebaseAuth.getInstance().getUid())) {
////                                        mchat.add(chat);
////                                    }
////                                } else {
////                                    mchat.add(chat);
////                                }
//                                                mchat.add(chat);
//                                            }
//                                        }
//
//                                        messageAdapter = new MessageAdapter(MessagingActivity.this, mchat);
//                                        recyclerView.setAdapter(messageAdapter);
//                                        messageAdapter.notifyDataSetChanged();
//                                        flag = "true";
//                                    } catch (Exception e) {
//
//                                    }
//                                }
//
//                            }
//                            //umair
//                        }
//                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //////////////////////


//        mchat = new ArrayList<>();
//        reference = FirebaseDatabase.getInstance().getReference("Chats");
//        reference.keepSynced(true);
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                mchat.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    if (dataSnapshot.exists()) {
//                        try {
//                            Chat chat = snapshot.getValue(Chat.class);
//                            if (!userid.isEmpty() && !id.isEmpty()) {
//                                if (chat.getReciver().equals
//                                        (FirebaseAuth.getInstance().getCurrentUser().getUid())
//                                        && chat.getSender().equals(userid) ||
//                                        chat.getReciver().equals(userid)
//                                                && chat.getSender().equals(id)) {
////                                if (snapshot.hasChild("delete")) {
////                                    if (!chat.getDelete().equals(FirebaseAuth.getInstance().getUid())) {
////                                        mchat.add(chat);
////                                    }
////                                } else {
////                                    mchat.add(chat);
////                                }
//                                    mchat.add(chat);
//                                }
//                            }
//
//                            messageAdapter = new MessageAdapter(MessagingActivity.this, mchat);
//                            recyclerView.setAdapter(messageAdapter);
//                            messageAdapter.notifyDataSetChanged();
//                            flag = "true";
//                        } catch (Exception e) {
//
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        //progressBar.setVisibility(View.GONE);
//        Toast.makeText(MessagingActivity.this, "done", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(mAuth.getUid()).child("status").setValue(0);
        /*   currentUser(userid);*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (seenListener != null) {
            reference.removeEventListener(seenListener);
        }
        FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(mAuth.getUid()).child("status").setValue(ServerValue.TIMESTAMP);
        /*currentUser("none");*/
    }


    public interface RecordingListener {

        void onRecordingStarted();

        void onRecordingLocked();

        void onRecordingCompleted();

        void onRecordingCanceled();

    }

    private void delete() {
        //umair
        cancelvoicebtn.setVisibility(View.GONE);
        imageViewMic.setVisibility(View.VISIBLE);
        imageViewMic.setRotation(0);
        isDeleting = true;
        btn_voice.setEnabled(false);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isDeleting = false;
                btn_voice.setEnabled(true);
                select_item.setVisibility(View.VISIBLE);
            }
        }, 1250);

        imageViewMic.animate().translationY(-dp * 150).rotation(180).scaleXBy(0.6f).scaleYBy(0.6f).setDuration(500).setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                dustin.setTranslationX(-dp * 40);
                dustin_cover.setTranslationX(-dp * 40);

                dustin_cover.animate().translationX(0).rotation(-120).setDuration(350).setInterpolator(new DecelerateInterpolator()).start();

                dustin.animate().translationX(0).setDuration(350).setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        dustin.setVisibility(View.VISIBLE);
                        dustin_cover.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                imageViewMic.animate().translationY(0).scaleX(1).scaleY(1).setDuration(350).setInterpolator(new LinearInterpolator()).setListener(
                        new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                imageViewMic.setVisibility(View.INVISIBLE);
                                imageViewMic.setRotation(0);

                                dustin_cover.animate().rotation(0).setDuration(150).setStartDelay(50).start();
                                dustin.animate().translationX(-dp * 40).setDuration(200).setStartDelay(250).setInterpolator(new DecelerateInterpolator()).start();
                                dustin_cover.animate().translationX(-dp * 40).setDuration(200).setStartDelay(250).setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        txt_send.setVisibility(View.VISIBLE);
                                        //chee
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                }).start();
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        }
                ).start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();
    }

    @Override
    public void onBackPressed() {

        if (checkflagforme) {
            myplayer.pause();
        }
        MessagingAdapter.mPrePos = -1;
        if (seenListener != null) {
            reference.removeEventListener(seenListener);
        }
        if (userRef != null) {
            userRef.removeEventListener(userDataListener);
        }if (getIntent().getStringExtra("for") != null) {
            Intent intent = new Intent(MessagingActivity.this, BringoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

finish();
        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
        if (seenListener != null) {
            reference.removeEventListener(seenListener);
        }
        MessagingAdapter.playingStatus = "true";
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }


}