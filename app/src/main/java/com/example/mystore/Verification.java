package com.example.mystore;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.provider.Settings;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.mystore.Model.AppHelper;
import com.example.mystore.Model.ConnectionDetector;
import com.example.mystore.Model.HelpingMethods;
import com.example.mystore.Model.RequestHandlerSingleten;
import com.example.mystore.Model.VolleyMultipartRequest;
import com.example.mystore.Model.VolleySingleton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.security.ProviderInstaller;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hbb20.CountryCodePicker;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.kofigyan.stateprogressbar.StateProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class Verification extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    Task location;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Button mSendCode, mbutton_verify, mResend_button, mButton_selctLocation;
    private ProgressBar mProgressBar, proBbar;
    private ProgressDialog mProgressDialog;
    private EditText mPhoneNumber, codeText, mEdiText_address;
    private String mVerificationId;
    private TextView mTextView_phoneno, mTimer, message;
    private CountryCodePicker ccp;
    private FirebaseAuth mAuth;
    public static String mAddress = "";
    static String mylatlng = "";
    private String address = "", city = "";
    SignInButton signInButton;
    static Geocoder geocoder;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseUser user;
    static List<Address> addresses;
    private static final String MAP_VIEW_BUNDLE_KEY = "AIzaSyCQamg8g6ZMTjQqGnu4iFYLW4WrnTJZjNE";
    private Animation animBlink;
    StateProgressBar stateProgressBar;
    SweetAlertDialog mCreateAlertDialog;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private HelpingMethods helpingMethods;
    private CardView mPhoneContainer, mVerifyContainer, mSigninContainer;
    private ViewGroup viewGroup;
    boolean flag = false;
    private ConnectionDetector connectionDetector;
    private ImageView icon, tick;

    static LatLng Your_Location = new LatLng(23.81, 90.41);
    private FloatingActionButton mfbpic;
    private CircleImageView musercrimage;
    Uri imageuri;
    Button mbutton_create;
    EditText mmusername;
    Bitmap bitmap = null;
    private String get_user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        FirebaseDatabase.getInstance().getReference("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ImageView imageView = findViewById(R.id.bgHeader);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ColorStateList stateList = ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary));
            imageView.setBackgroundTintList(stateList);
        } else {
            imageView.getBackground().getCurrent().setColorFilter(
                    new PorterDuffColorFilter(getResources().getColor(R.color.colorPrimary),
                            PorterDuff.Mode.MULTIPLY));
        }
        mmusername = findViewById(R.id.username);
        mbutton_create = findViewById(R.id.button_create);
        musercrimage = findViewById(R.id.usercrimage);
        mfbpic = findViewById(R.id.fbpic);
        mProgressBar = findViewById(R.id.pbar);
        mSendCode = findViewById(R.id.button_sendcode);
        mTimer = findViewById(R.id.timer_TextView);
        mPhoneNumber = findViewById(R.id.editText_phoneNumber);
        proBbar = findViewById(R.id.progressBar);
        viewGroup = findViewById(R.id.group_layout);
        mButton_selctLocation = findViewById(R.id.button_selctLocation);
        mEdiText_address = findViewById(R.id.ediText_address);
        icon = findViewById(R.id.emailicon);
        tick = findViewById(R.id.tickicon);
        message = findViewById(R.id.sms);
        mPhoneContainer = findViewById(R.id.step1_container);
        //mEditText_location = findViewById(R.id.editText_location);
        stateProgressBar = findViewById(R.id.your_state_progress_bar_id);
        mVerifyContainer = findViewById(R.id.step2_container);
        connectionDetector = new ConnectionDetector(Verification.this);
        mSigninContainer = findViewById(R.id.step3_container);
        mResend_button = findViewById(R.id.button_resend);
        mCreateAlertDialog = new SweetAlertDialog(Verification.this, SweetAlertDialog.PROGRESS_TYPE);
        mCreateAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#008577"));
        mCreateAlertDialog.setTitleText("creating account....");
        mCreateAlertDialog.setCancelable(false);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mResend_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResendCode();
            }
        });
        codeText = findViewById(R.id.pinView);
        mTextView_phoneno = findViewById(R.id.textView_phoneno);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getInstance().getCurrentUser();
        signInButton = findViewById(R.id.sign_in_button);

        try {
            ProviderInstaller.installIfNeeded(getApplicationContext());
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLSv1.2");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            sslContext.init(null, null, null);
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }


        mfbpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galery = new Intent();
                galery.setType("image/*");
                galery.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galery, "Select image"), 102);


            }
        });


        mbutton_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionDetector con = new ConnectionDetector(Verification.this);
                if (bitmap == null) {
                    helpingMethods.SnackBar("Select your image", v);
                } else if (mmusername.getText().toString().trim().equals("")) {
                    helpingMethods.SnackBar("Enter your name", v);
                } else if (mEdiText_address.getText().toString().trim().equals("")) {
                    helpingMethods.SnackBar("Enter your address", v);
                } else if (con.isConnected()) {
                    mCreateAlertDialog.show();
                    ////////////////////////////////////////////////////////////////////////
                    String url = "https://chhatt.com/Cornstr/grocery/api/reg";
                    VolleyMultipartRequest multipartRequest = new
                            VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
                                @Override
                                public void onResponse(NetworkResponse response) {
                                    if (response.statusCode == 200) {
                                        String userImage = null;
                                        try {
                                            userImage = new String(response.data, "UTF-8");
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                        //junaid
                                        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(mAuth.getUid());
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("name", mmusername.getText().toString().trim());
                                        hashMap.put("picture", userImage);
                                        hashMap.put("phone", mPhoneNumber.getText().toString().replaceAll(" ", ""));
                                        hashMap.put("status", 0);
                                        hashMap.put("token", FirebaseInstanceId.getInstance().getToken());
                                        hashMap.put("address", mEdiText_address.getText().toString().trim());
                                        hashMap.put("search", mmusername.getText().toString().trim().toLowerCase());
                                        userReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Intent intent = new Intent(Verification.this, MainActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    mCreateAlertDialog.dismiss();
                                                    helpingMethods.SnackBar("" + task.getException().getMessage(), mPhoneNumber);
                                                }
                                            }
                                        });


                                        FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(mAuth.getUid());


                                    } else {
                                        Toast.makeText(Verification.this, "Error founded: " + response.statusCode, Toast.LENGTH_SHORT).show();
                                        mProgressDialog.dismiss();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    NetworkResponse networkResponse = error.networkResponse;
                                    String errorMessage = "Unknown error";
                                    if (networkResponse == null) {
                                        if (error.getClass().equals(TimeoutError.class)) {
                                            errorMessage = "Request timeout";
                                        } else if (error.getClass().equals(NoConnectionError.class)) {
                                            errorMessage = "Failed to connect server";
                                        }
                                    } else {
                                        String result = new String(networkResponse.data);
                                        try {
                                            JSONObject response = new JSONObject(result);
                                            String status = response.getString("status");
                                            String message = response.getString("message");

                                            Log.e("Error Status", status);
                                            Log.e("Error Message", message);

                                            if (networkResponse.statusCode == 404) {
                                                errorMessage = "Resource not found";
                                            } else if (networkResponse.statusCode == 401) {
                                                errorMessage = message + " Please login again";
                                            } else if (networkResponse.statusCode == 400) {
                                                errorMessage = message + " Check your inputs";
                                            } else if (networkResponse.statusCode == 500) {
                                                errorMessage = message + " Something is getting wrong";
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    Log.i("Error", errorMessage);
                                    error.printStackTrace();
                                }
                            }) {
                                @Override
                                protected Map<String, DataPart> getByteData() {
                                    Map<String, DataPart> params = new HashMap<>();
                                    params.put("image[" + 0 + "]", new DataPart("profileimage.jpg", AppHelper.getFileDataFromDrawable(getBaseContext(), bitmap), "image/jpeg"));

                                    return params;
                                }

                                @Override
                                protected Map<String, String> getParams() {

                                    HashMap<String, String> hashMap = new HashMap<>();
                                    hashMap.put("u_id", mAuth.getUid());
                                    hashMap.put("user_name", mmusername.getText().toString().trim());
                                    hashMap.put("phone",mPhoneNumber.getText().toString().replaceAll(" ", ""));
                                    hashMap.put("lat", String.valueOf(Your_Location.latitude));
                                    hashMap.put("lng", String.valueOf(Your_Location.longitude));
                                    hashMap.put("address", mEdiText_address.getText().toString().trim());

                                    return hashMap;
                                }
                            };
                    multipartRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(multipartRequest);

                } else {
                    helpingMethods.SnackBar("Check your Internet connection", v);
                }


                ////////////////////////////////////////////////////////////////////////


            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionDetector detector = new ConnectionDetector(Verification.this);
                if (mEdiText_address.getText().toString().trim().equals("")) {
                    helpingMethods.SnackBar("Please enter your address.", v);
                } else if (detector.isConnected()) {
                    parseJSON();
                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, 101);
                } else {
                    helpingMethods.SnackBar("Check your internet connection.", v);
                }
            }
        });

        TextView textView = (TextView) signInButton.getChildAt(0);
        textView.setText("Corner SignIn");
        textView.setTextColor(Color.parseColor("#008577"));

        viewGroup = findViewById(R.id.group_layout);
        ccp = findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(mPhoneNumber);
        animBlink = AnimationUtils.loadAnimation(Verification.this, R.anim.blink);
        helpingMethods = new HelpingMethods(Verification.this);

        mSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSendCode.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
                SendCode(v);


            }
        });

        mbutton_verify = findViewById(R.id.button_verify);

        mbutton_verify.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                ConnectionDetector detector = new ConnectionDetector(Verification.this);
                if (codeText.getText().toString().trim().equals("")) {
                    helpingMethods.SnackBar("Please enter verification code.", codeText);
                } else if (detector.isConnected()) {
                    VerifyPhone();

                } else {
                    helpingMethods.SnackBar("Check your internet connection.", v);
                }

            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                String code = phoneAuthCredential.getSmsCode();
                ConnectionDetector detector = new ConnectionDetector(Verification.this);
                if (code != null) {
                    codeText.setText(code);
                    if (detector.isConnected()) {
                        VerifyPhone();
                    } else {
                        helpingMethods.SnackBar("Check your internet connection.", mbutton_verify);
                    }
                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    mProgressBar.setVisibility(View.GONE);
                    mSendCode.setVisibility(View.VISIBLE);
                    helpingMethods.SnackBar("Invalid phone number.", mProgressBar);
                } else if (e instanceof FirebaseTooManyRequestsException) {

                }
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                mTextView_phoneno.setText(ccp.getFullNumberWithPlus());
                new CountDownTimer(60000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        mTimer.setText("" + millisUntilFinished / 1000);
                        mTimer.setAnimation(animBlink);
                    }

                    public void onFinish() {
                        mTimer.setVisibility(View.GONE);
                        mbutton_verify.setVisibility(View.GONE);
                        mResend_button.setVisibility(View.VISIBLE);
                    }

                }.start();
                mVerifyContainer.setVisibility(View.VISIBLE);
                mPhoneContainer.setVisibility(View.GONE);
                mVerificationId = s;
                mResendToken = forceResendingToken;
                mProgressBar.setVisibility(View.GONE);
                mSendCode.setVisibility(View.VISIBLE);

            }
        };

        mButton_selctLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Verification.this, MapActivity.class);
                flag = true;
                startActivity(intent);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                if (task.isSuccessful()) {
                    mCreateAlertDialog.show();
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account);
                } else {
                    mCreateAlertDialog.dismiss();
                    helpingMethods.SnackBar("" + task.getException().getMessage(), mPhoneNumber);
                }
            } catch (ApiException e) {
                mCreateAlertDialog.dismiss();
                helpingMethods.SnackBar("" + e.getMessage(), mPhoneNumber);
            }
        } else if (requestCode == 102) {
            imageuri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageuri);
                musercrimage.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //junaid
    private void firebaseAuthWithGoogle(final GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mylatlng = Your_Location.latitude + "," + Your_Location.longitude;
                            user = mAuth.getCurrentUser();
                            assert user != null;
                            final String email = account.getEmail();
                            final String user = account.getDisplayName();
                            final Uri photouri = account.getPhotoUrl();
                            final String photo = photouri.toString();
                            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users").child("Customers");
                            HashMap hashMap = new HashMap<>();
                            hashMap.put("name", user);
                            hashMap.put("picture", photo);
                            hashMap.put("email", email);
                            hashMap.put("phone", mPhoneNumber.getText().toString().replaceAll(" ", ""));
                            hashMap.put("latlong", mylatlng);
                            hashMap.put("address", mEdiText_address.getText().toString().trim());
                            hashMap.put("status", 0);
                            hashMap.put("token", FirebaseInstanceId.getInstance().getToken());
                            hashMap.put("search", user.toLowerCase());
                            userReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(Verification.this, MainActivity.class));
                                    } else {
                                        mCreateAlertDialog.dismiss();
                                        helpingMethods.SnackBar("" + task.getException().getMessage(), mPhoneNumber);
                                    }
                                }
                            });

                        } else {
                            mCreateAlertDialog.dismiss();
                            helpingMethods.SnackBar("" + task.getException().getMessage(), mPhoneNumber);
                        }
                    }
                });
    }

    private void ResendCode() {
        Toast.makeText(this, "Pending", Toast.LENGTH_SHORT).show();
//        mResend_button.setVisibility(View.GONE);
//        mbutton_verify.setVisibility(View.VISIBLE);
//        new CountDownTimer(60000, 1000) {
//
//            public void onTick(long millisUntilFinished) {
//                mTimer.setText("" + millisUntilFinished / 1000);
//                mTimer.setVisibility(View.VISIBLE);
//                mTimer.setAnimation(animBlink);
//
//            }
//
//            public void onFinish() {
//                mTimer.setVisibility(View.GONE);
//                mbutton_verify.setVisibility(View.GONE);
//                mResend_button.setVisibility(View.VISIBLE);
//            }
//
//        }.start();
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithPhoneAuthCredential(credential);
        } catch (Exception e) {
            String error = e.getMessage();
            proBbar.setVisibility(View.GONE);
            message.setText(error);
            message.setTextColor(getResources().getColor(R.color.colorRed));
            if (Build.VERSION.SDK_INT >= 19) {
                TransitionManager.beginDelayedTransition(viewGroup);
                message.setVisibility(View.VISIBLE);
            } else {
                message.setVisibility(View.VISIBLE);
            }
            mbutton_verify.setEnabled(true);
            proBbar.setVisibility(View.GONE);
            codeText.setEnabled(true);
            message.setVisibility(View.VISIBLE);
        }
    }
//
//    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
//
//        mAuth.signInWithCredential(phoneAuthCredential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//
//                            FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                    if (dataSnapshot.exists()) {
//                                        Intent intent = new Intent(Verification.this, MainActivity.class);
//                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                        startActivity(intent);
//                                        finish();
//                                    } else {
//
//
//                                        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0, 1, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                                        scaleAnimation.setDuration(100);
//                                        scaleAnimation.setInterpolator(new AccelerateInterpolator());
//                                        scaleAnimation.setRepeatMode(Animation.REVERSE);
//                                        scaleAnimation.setRepeatCount(1);
//                                        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
//                                            @Override
//                                            public void onAnimationStart(Animation animation) {
//                                                icon.setColorFilter(Verification.this.getResources().getColor(R.color.colorGreen));
//
//                                            }
//
//                                            @Override
//                                            public void onAnimationEnd(Animation animation) {
//
//                                            }
//
//                                            @Override
//                                            public void onAnimationRepeat(Animation animation) {
//
//                                            }
//                                        });
//                                        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
//                                        mSigninContainer.setVisibility(View.VISIBLE);
//                                        mVerifyContainer.setVisibility(View.GONE);
//                                        viewGroup.setVisibility(View.GONE);
//                                        proBbar.setVisibility(View.GONE);
//
//                                        if (ContextCompat.checkSelfPermission(Verification.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                                            kamkicheez();
//                                        } else {
//                                            getPermisssion();
//                                        }
//
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                }
//                            });
//
//
//
//
//                        } else {
//                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                                proBbar.setVisibility(View.GONE);
//                                message.setText("Invalid Code.");
//                                message.setTextColor(getResources().getColor(R.color.colorRed));
//                                if (Build.VERSION.SDK_INT >= 19) {
//                                    TransitionManager.beginDelayedTransition(viewGroup);
//                                    message.setVisibility(View.VISIBLE);
//                                } else {
//                                    message.setVisibility(View.VISIBLE);
//                                }
//                                mbutton_verify.setEnabled(true);
//                                proBbar.setVisibility(View.GONE);
//                                codeText.setEnabled(true);
//                                message.setVisibility(View.VISIBLE);
//                            }
//                        }
//                    }
//                });
//
//    }




    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {

        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            //Toast.makeText(Verification.this, "User created" + mAuth.getUid(), Toast.LENGTH_SHORT).show();


                            if (ContextCompat.checkSelfPermission(Verification.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                kamkicheez();
                            } else {
                                 getPermisssion();
                            }



                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                proBbar.setVisibility(View.GONE);
                                message.setText("Invalid Code.");
                                message.setTextColor(getResources().getColor(R.color.colorRed));
                                if (Build.VERSION.SDK_INT >= 19) {

                                    message.setVisibility(View.VISIBLE);
                                } else {
                                    message.setVisibility(View.VISIBLE);
                                }
                                mbutton_verify.setEnabled(true);
                                proBbar.setVisibility(View.GONE);
                                codeText.setEnabled(true);
                                message.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });

    }

    private void getPermisssion() {

        Dexter.withActivity(Verification.this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        kamkicheez();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Verification.this);
                            builder.setTitle("Permission Denied")
                                    .setMessage("Permission to access device location is permanently denied. you need to go to setting to allow the permission.")
                                    .setNegativeButton("Deny", null)
                                    .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            intent.setData(Uri.fromParts("package", getPackageName(), null));
                                        }
                                    })
                                    .show();
                        } else {
                            helpingMethods.SnackBar("Permission Denied", mPhoneNumber);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check();
    }


    private void resendVerificationCode(String fullNumberWithPlus, PhoneAuthProvider.ForceResendingToken mResendToken) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                fullNumberWithPlus, 60, TimeUnit.SECONDS, this, mCallbacks, mResendToken);
    }


    private void kamkicheez() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Verification.this);
        if (ActivityCompat.checkSelfPermission(Verification.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Verification.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        location = mFusedLocationProviderClient.getLastLocation();
        location.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    final android.location.Location currentLocation = (Location) task.getResult();
                    try {
                        ((SupportMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.mapview)).getMapAsync(new OnMapReadyCallback() {

                            @Override
                            public void onMapReady(GoogleMap googleMap) {

                                Your_Location = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                                mMap = googleMap;
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Your_Location, 15));  //move camera to location
                                mAddress = getAddress(currentLocation.getLatitude(), currentLocation.getLongitude());
                                mEdiText_address.setText(mAddress);
                                if (mMap != null) {
                                    Marker hamburg = mMap.addMarker(new MarkerOptions().position(Your_Location));
                                }
                                // Rest of the stuff you need to do with the map
                            }
                        });

                    } catch (Exception e) {

                    }

                }
            }
        });
    }

    public String getAddress(Double lat, Double lng) {

        geocoder = new Geocoder(this);
        try {
            address = null;
            addresses = geocoder.getFromLocation(lat, lng, 1);
            address = addresses.get(0).getAddressLine(0);
            city = addresses.get(0).getLocality();


        } catch (IOException e) {
            e.printStackTrace();
        }


        return address + " " + city;

    }

    private void checkApi() {
        if (Build.VERSION.SDK_INT >= 19) {
            TransitionManager.beginDelayedTransition(viewGroup);
        }
        icon.setVisibility(View.VISIBLE);
        proBbar.setVisibility(View.VISIBLE);
        mbutton_verify.setEnabled(false);
        codeText.setEnabled(false);
    }

    private void VerifyPhone() {
        String code = codeText.getText().toString();
        if (Build.VERSION.SDK_INT >= 19) {
            TransitionManager.beginDelayedTransition(viewGroup);
            icon.setVisibility(View.GONE);
            codeText.setEnabled(true);
            message.setVisibility(View.GONE);
        } else {
            codeText.setEnabled(true);
            icon.setVisibility(View.GONE);
            message.setVisibility(View.GONE);
        }
        checkApi();
        verifyPhoneNumberWithCode(mVerificationId, code);
        get_user = "https://chhatt.com/Cornstr/grocery/api/get/client/verified?mob="+mPhoneNumber.getText().toString().replaceAll(" ", "");
        parseJSON();
    }


    public void SendCode(View view) {
        String phoneno = mPhoneNumber.getText().toString().replaceAll(" ", "");
        if (phoneno.equals("")) {
            mProgressBar.setVisibility(View.GONE);
            mSendCode.setVisibility(View.VISIBLE);
            helpingMethods.SnackBar("Please enter your phone number.", view);
        } else if (connectionDetector.isConnected()) {
            startPhoneNumberVerification(ccp.getFullNumberWithPlus());
        } else {
            mProgressBar.setVisibility(View.GONE);
            mSendCode.setVisibility(View.VISIBLE);
            helpingMethods.SnackBar("Check your internet connection.", view);
        }

    }

    private void startPhoneNumberVerification(String fullNumberWithPlus) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                fullNumberWithPlus, 60, TimeUnit.SECONDS, this, mCallbacks);

    }

    @Override
    public void onResume() {
        super.onResume();


        if (flag) {
            String[] getvv = mylatlng.split(",");
            final double latitude = Double.parseDouble(getvv[0]);
            final double longitude = Double.parseDouble(getvv[1]);
            Your_Location = new LatLng(latitude, longitude);
            ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.mapview)).getMapAsync(new OnMapReadyCallback() {

                @Override
                public void onMapReady(GoogleMap googleMap) {

                    mMap = googleMap;
                    mMap = googleMap;
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Your_Location, 15));  //move camera to location
                    mAddress = getAddress(latitude, longitude);
                    mEdiText_address.setText(mAddress);
                    if (mMap != null) {
                        Marker hamburg = mMap.addMarker(new MarkerOptions().position(Your_Location));
                    }
                    // Rest of the stuff you need to do with the map
                }
            });
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapview);
            mapFragment.getMapAsync(Verification.this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    private void parseJSON() {


        FirebaseUser user = mAuth.getCurrentUser();
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(get_user, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response.isNull(0)) {

                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                    mSigninContainer.setVisibility(View.VISIBLE);
                    mVerifyContainer.setVisibility(View.GONE);

                } else {

                    mSigninContainer.setVisibility(View.GONE);
                    Intent intent = new Intent(Verification.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();


                        try {



                        } catch (Exception e) {

                        }



                           // helpingMethods.saveuser(u_name, u_image, u_nic, u_address, u_email, u_businessntn, u_number, u_role, null);






                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestHandlerSingleten.getInstance(Verification.this).addToRequestQueue(jsonArrayRequest);

    }
}

