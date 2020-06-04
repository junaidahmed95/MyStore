package com.bringo.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.bringo.home.Model.AppHelper;
import com.bringo.home.Model.ConnectionDetector;
import com.bringo.home.Model.HelpingMethods;
import com.bringo.home.Model.RequestHandlerSingleten;
import com.bringo.home.Model.VolleyMultipartRequest;
import com.bringo.home.Model.VolleySingleton;
import com.bringo.home.ui.home.HomeFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
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
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
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
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;


public class Verification extends AppCompatActivity implements OnMapReadyCallback {
    private static final String MAP_VIEW_BUNDLE_KEY = "AIzaSyCQamg8g6ZMTjQqGnu4iFYLW4WrnTJZjNE";
    public static String mAddress = "";
    static String mylatlng = "";
    static Geocoder geocoder;
    private String hasImage = "0";
    private String signInOption = "Main";
    private String userImage = null;
    static List<Address> addresses;
    static LatLng Your_Location = new LatLng(23.81, 90.41);
    Task location;
    SignInButton signInButton;
    int PERMISSION_ID = 44;
    StateProgressBar stateProgressBar;
    boolean flag = false;
    private GoogleMap mMap;
    private TextView mbtnPrivacyPolicy;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Button mSendCode, mbutton_verify, mResend_button, mButton_selctLocation;
    private ProgressDialog mProgressDialog;
    private EditText mPhoneNumber, codeText, mEdiText_address;
    private String mVerificationId;
    private TextView mTextView_phoneno, mTimer;
    private CountryCodePicker ccp;
    private FirebaseAuth mAuth;
    private String address = "", city = "";
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseUser user;
    private FusedLocationProviderClient mFusedLocationClient;
    private Animation animBlink;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private HelpingMethods helpingMethods;
    private CardView mPhoneContainer, mVerifyContainer, mSigninContainer;
    private ConnectionDetector connectionDetector;
    private FloatingActionButton mfbpic;
    private CircleImageView musercrimage;
    private Uri imageuri;
    private Button mbutton_create, signup;
    private EditText mmusername, moptional_number, moptional_email;
    private Bitmap bitmap = null;
    private String get_user;
    private PhoneAuthCredential credential;
    private LinearLayout mmainLayout, mphoneLayout, moptionalPhoneLayout, moptionalEmailLayout;
    private ImageView imageView;
    private CardView mcardVew1;
    private LoginButton fb_login;
    String photoUrl;
    private int RC_SIGN_IN = 1;

    private CallbackManager mcallbackManager;
    private FirebaseAuth.AuthStateListener authStateListener;
    private AccessTokenTracker accessTokenTracker;
    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            SetMap(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.verification_activity);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mbtnPrivacyPolicy = findViewById(R.id.btnPrivacyPolicy);
        mAuth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.getApplicationSignature(getApplicationContext());
        FirebaseDatabase.getInstance().getReference("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        moptional_number = findViewById(R.id.optional_number);
        moptional_email = findViewById(R.id.optional_email);
        moptionalEmailLayout = findViewById(R.id.optionalEmailLayout);
        moptionalPhoneLayout = findViewById(R.id.optionalPhoneLayout);
        mcardVew1 = findViewById(R.id.cardVew1);
        mphoneLayout = findViewById(R.id.phoneLayout);
        mmainLayout = findViewById(R.id.mainLayout);
        imageView = findViewById(R.id.bgHeader);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mbtnPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://bringo.biz/privacy.policy"));
                startActivity(browserIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        signup = findViewById(R.id.signup);
        mmusername = findViewById(R.id.username);
        mbutton_create = findViewById(R.id.button_create);
        musercrimage = findViewById(R.id.usercrimage);
        mfbpic = findViewById(R.id.fbpic);

        mSendCode = findViewById(R.id.button_sendcode);
        mTimer = findViewById(R.id.timer_TextView);
        mPhoneNumber = findViewById(R.id.editText_phoneNumber);
        mButton_selctLocation = findViewById(R.id.button_selctLocation);
        mEdiText_address = findViewById(R.id.ediText_address);
        mPhoneContainer = findViewById(R.id.step1_container);
        //mEditText_location = findViewById(R.id.editText_location);
        stateProgressBar = findViewById(R.id.your_state_progress_bar_id);
        mVerifyContainer = findViewById(R.id.step2_container);
        connectionDetector = new ConnectionDetector(Verification.this);
        mSigninContainer = findViewById(R.id.step3_container);
        mResend_button = findViewById(R.id.button_resend);
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
        //google sigin code
        signInButton = findViewById(R.id.sign_in_button);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        fb_login = findViewById(R.id.login_button);
        SetUpFB();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    mProgressDialog.setMessage("Please wait...");
//                    mProgressDialog.show();
//                    signInOption = "FB";
//                    get_user = "https://bringo.biz/api/get/client/verified?mob=" + FirebaseAuth.getInstance().getUid();
//                    parseJSON();
//                } else {
//                    //Toast.makeText(Verification.this, "Failed!", Toast.LENGTH_SHORT).show();
//                }
            }
        };
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
//                if (currentAccessToken == null) {
//                    mAuth.signOut();
//                }
            }
        };
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.setMessage("Please wait...");
                mProgressDialog.show();
                signInOption = "Google";
                signIn();
            }
        });


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

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mmainLayout.setVisibility(View.GONE);
                mphoneLayout.setVisibility(View.VISIBLE);
                mcardVew1.setVisibility(View.VISIBLE);
                mPhoneContainer.setVisibility(View.VISIBLE);
                signInOption = "Phone";

            }
        });


        mfbpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galery = new Intent();
                galery.setType("image/*");
                galery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galery, "Select image"), 102);
            }
        });

//create button
        mbutton_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionDetector con = new ConnectionDetector(Verification.this);
                if (hasImage.equals("0") && bitmap == null) {
                    helpingMethods.SnackBar("Select your image", v);
                } else if (mmusername.getText().toString().trim().equals("")) {
                    helpingMethods.SnackBar("Enter your name", v);
                } else if (moptionalPhoneLayout.getVisibility() == View.VISIBLE && moptional_number.getText().toString().trim().equals("")) {
                    helpingMethods.SnackBar("Enter your phone number", v);
                } else if (moptionalPhoneLayout.getVisibility() == View.VISIBLE && !moptional_number.getText().toString().trim().equals("") && moptional_number.getText().toString().length() < 11) {
                    helpingMethods.SnackBar("Invalid phone number", v);
                } else if (moptionalEmailLayout.getVisibility() == View.VISIBLE && moptional_email.getText().toString().trim().equals("")) {
                    helpingMethods.SnackBar("Enter your email", v);
                } else if (mEdiText_address.getText().toString().trim().equals("")) {
                    helpingMethods.SnackBar("Enter your address", v);
                } else if (con.isConnected()) {
                    mProgressDialog.setMessage("Creating account...");
                    mProgressDialog.show();
                    String url = "https://bringo.biz/api/reg";
                    VolleyMultipartRequest multipartRequest = new
                            VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
                                @Override
                                public void onResponse(NetworkResponse response) {
                                    if (response.statusCode == 200) {
                                        try {
                                            userImage = new String(response.data, "UTF-8");
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }

                                        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(mAuth.getUid());
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("name", mmusername.getText().toString());

                                        hashMap.put("picture", userImage);

                                        if (signInOption.equals("Phone")) {
                                            hashMap.put("phone", mPhoneNumber.getText().toString().replaceAll(" ", "").replaceFirst("^[0]+|^[+92]+", ""));
                                        } else {
                                            hashMap.put("phone", moptional_number.getText().toString());
                                        }
                                        hashMap.put("email", moptional_email.getText().toString());
                                        hashMap.put("status", 0);
                                        hashMap.put("token", FirebaseInstanceId.getInstance().getToken());
                                        hashMap.put("address", mEdiText_address.getText().toString().trim());
                                        hashMap.put("search", mmusername.getText().toString().trim().toLowerCase());

                                        userReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    String email = null;
                                                    if (!moptional_email.getText().toString().trim().equals("")) {
                                                        email = moptional_email.getText().toString();
                                                    }

                                                    if (signInOption.equals("Phone")) {
                                                        helpingMethods.saveuser(mmusername.getText().toString(), userImage, mPhoneNumber.getText().toString().replaceAll(" ", "").replaceFirst("^[0]+|^[+92]+", ""), email);
                                                    } else {
                                                        helpingMethods.saveuser(mmusername.getText().toString(), userImage, moptional_number.getText().toString(), email);
                                                    }


                                                    Intent intent = new Intent(Verification.this, MainActivity.class);
                                                    if (getIntent().getStringExtra("for") != null) {
                                                        intent.putExtra("cart", "open");
                                                    }
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                                    finish();
                                                } else {
                                                    mProgressDialog.cancel();
                                                    helpingMethods.SnackBar("" + task.getException().getMessage(), mPhoneNumber);
                                                }
                                            }
                                        });


                                        FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(mAuth.getUid());


                                    } else {
                                        mProgressDialog.cancel();
                                        Toast.makeText(Verification.this, "Error founded: " + response.statusCode, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    NetworkResponse networkResponse = error.networkResponse;
                                    String errorMessage = "Unknown error";
                                    mProgressDialog.cancel();
                                    Toast.makeText(Verification.this, "" + errorMessage, Toast.LENGTH_SHORT).show();
                                    if (networkResponse == null) {
                                        if (error.getClass().equals(TimeoutError.class)) {
                                            errorMessage = "Request timeout";
                                        } else if (error.getClass().equals(NoConnectionError.class)) {
                                            errorMessage = "Failed to connect server";
                                        }
                                        mProgressDialog.cancel();
                                        Toast.makeText(Verification.this, "" + errorMessage, Toast.LENGTH_SHORT).show();
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
                                            mProgressDialog.cancel();
                                            Toast.makeText(Verification.this, "" + errorMessage, Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            mProgressDialog.cancel();
                                            Toast.makeText(Verification.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    Log.i("Error", errorMessage);
                                    error.printStackTrace();
                                }
                            }) {
                                @Override
                                protected Map<String, DataPart> getByteData() {
                                    Map<String, DataPart> params = new HashMap<>();
                                    if (hasImage.equals("0")) {
                                        params.put("image[" + 0 + "]", new DataPart("profileimage.jpg", AppHelper.getFileDataFromDrawable(getBaseContext(), bitmap), "image/jpeg"));
                                    }

                                    return params;
                                }

                                @Override
                                protected Map<String, String> getParams() {

                                    HashMap<String, String> hashMap = new HashMap<>();
                                    OSPermissionSubscriptionState status1 = OneSignal.getPermissionSubscriptionState();
                                    status1.getPermissionStatus().getEnabled();
                                    status1.getSubscriptionStatus().getSubscribed();
                                    status1.getSubscriptionStatus().getUserSubscriptionSetting();
                                    status1.getSubscriptionStatus().getUserId();
                                    status1.getSubscriptionStatus().getPushToken();
                                    JSONObject mainObj = new JSONObject();


                                    try {
                                        mainObj.put("permissionStatus", status1.getPermissionStatus().toJSONObject());
                                        mainObj.put("subscriptionStatus", status1.getSubscriptionStatus().toJSONObject());
                                        mainObj.put("emailSubscriptionStatus", status1.getEmailSubscriptionStatus().toJSONObject());
                                        JSONObject jsonObject1 = mainObj.getJSONObject("subscriptionStatus");
                                        hashMap.put("play_id", String.valueOf(jsonObject1.get("userId")));
                                    } catch (Throwable t) {
                                        t.printStackTrace();
                                    }


                                    if (hasImage.equals("1")) {
                                        hashMap.put("url", photoUrl);
                                    }

                                    hashMap.put("auth", hasImage);


                                    hashMap.put("email", moptional_email.getText().toString());
                                    hashMap.put("u_id", mAuth.getUid());
                                    hashMap.put("image", userImage);
                                    hashMap.put("user_name", mmusername.getText().toString().trim());
                                    if (signInOption.equals("Phone")) {
                                        hashMap.put("phone", mPhoneNumber.getText().toString().replaceAll(" ", "").replaceFirst("^[0]+|^[+92]+", ""));
                                    } else {
                                        hashMap.put("phone", moptional_number.getText().toString());
                                    }
                                    hashMap.put("lat", String.valueOf(Your_Location.latitude));
                                    hashMap.put("lng", String.valueOf(Your_Location.longitude));
                                    hashMap.put("address", mEdiText_address.getText().toString().trim());

                                    return hashMap;
                                }
                            };
                    multipartRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(multipartRequest);

                } else {
                    mProgressDialog.cancel();
                    helpingMethods.SnackBar("Check your Internet connection", v);
                }


                ////////////////////////////////////////////////////////////////////////


            }
        });
//google sigin button

        TextView textView = (TextView) signInButton.getChildAt(0);
        textView.setText("Continue with Google");
        textView.setTextColor(Color.parseColor("#FFE00D0D"));
        textView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER | Gravity.CENTER_VERTICAL);

        ccp = findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(mPhoneNumber);
        animBlink = AnimationUtils.loadAnimation(Verification.this, R.anim.blink);
        helpingMethods = new HelpingMethods(Verification.this);

        mSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPhoneNumber.getText().toString().trim().equals("")) {
                    helpingMethods.SnackBar("Please enter your phone number.", v);
                } else if (connectionDetector.isConnected()) {
                    mProgressDialog.setMessage("Sending code...");
                    mProgressDialog.show();
                    startPhoneNumberVerification(ccp.getFullNumberWithPlus());
                } else {
                    helpingMethods.SnackBar("Check your internet connection.", v);
                }


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
                    mProgressDialog.setMessage("Verifying code...");
                    mProgressDialog.show();
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
                        mProgressDialog.setMessage("Verifying code...");
                        mProgressDialog.show();
                        VerifyPhone();
                    } else {
                        helpingMethods.SnackBar("Check your internet connection.", mbutton_verify);
                    }
                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    mProgressDialog.cancel();
                    Toast.makeText(Verification.this, "Invalid phone number", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    mProgressDialog.cancel();
                    Toast.makeText(Verification.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                mPhoneContainer.setVisibility(View.GONE);
                mVerifyContainer.setVisibility(View.VISIBLE);
                mProgressDialog.cancel();
                mVerificationId = s;
                mResendToken = forceResendingToken;

            }
        };

        mButton_selctLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Verification.this, MapActivity.class);
                intent.putExtra("activity", "verification");
                flag = true;
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    private void SetUpFB() {
        fb_login.setReadPermissions("email", "public_profile");
        mcallbackManager = CallbackManager.Factory.create();
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        fb_login.registerCallback(mcallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mProgressDialog.setMessage("Please wait...");
                mProgressDialog.show();
                signInOption = "FB";
                handleFacebookToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                mProgressDialog.cancel();
                Toast.makeText(Verification.this, "Canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                mProgressDialog.cancel();
                Toast.makeText(Verification.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mcallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            mProgressDialog.setMessage("Signing In...");
            mProgressDialog.show();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
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

    private void ResendCode() {
        //Toast.makeText(this, "Pending", Toast.LENGTH_SHORT).show();
        mResend_button.setVisibility(View.GONE);
        mbutton_verify.setVisibility(View.VISIBLE);
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                mTimer.setText("" + millisUntilFinished / 1000);
                mTimer.setVisibility(View.VISIBLE);
                mTimer.setAnimation(animBlink);

            }

            public void onFinish() {
                mTimer.setVisibility(View.GONE);
                mbutton_verify.setVisibility(View.GONE);
                mResend_button.setVisibility(View.VISIBLE);
            }

        }.start();
    }


    //kam
    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        try {
            credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithPhoneAuthCredential(credential);
        } catch (Exception e) {
            mProgressDialog.cancel();
            String error = e.getMessage();
            Toast.makeText(Verification.this, "" + error, Toast.LENGTH_SHORT).show();

        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            get_user = "https://bringo.biz/api/get/client/verified?mob=" + FirebaseAuth.getInstance().getUid();
                            ConnectionDetector detector = new ConnectionDetector(Verification.this);
                            if (detector.isConnected()) {
                                parseJSON();
                            } else {
                                mProgressDialog.cancel();
                                Toast.makeText(Verification.this, "Check your ineternet,", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                mProgressDialog.cancel();
                                Toast.makeText(Verification.this, "Invalid code!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(Verification.this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(Verification.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(Verification.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                final Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    SetMap(location.getLatitude(), location.getLongitude());
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(Verification.this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        } else {
            requestPermissions();
        }
    }

    private void SetMap(final double latitude, final double longitude) {
        try {
            ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.mapview)).getMapAsync(new OnMapReadyCallback() {

                @Override
                public void onMapReady(GoogleMap googleMap) {

                    Your_Location = new LatLng(latitude, longitude);
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

        } catch (Exception e) {

        }

    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                Verification.this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private void CheckLocationPermission() {
        Dexter.withActivity(Verification.this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        getLastLocation();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Verification.this);
                            builder.setTitle("Permission Denied")
                                    .setMessage("Permission to access device location is permanently denied. you need to go to setting to allow the permission.")
                                    .setNegativeButton("Cancel", null)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            intent.setData(Uri.fromParts("package", getPackageName(), null));
                                        }
                                    })
                                    .show();
                        } else {
                            Toast.makeText(Verification.this, "Permission Denied.", Toast.LENGTH_SHORT).show();
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


    private void VerifyPhone() {
        String code = codeText.getText().toString();
        verifyPhoneNumberWithCode(mVerificationId, code);


    }


    private void startPhoneNumberVerification(String fullNumberWithPlus) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                fullNumberWithPlus, 60, TimeUnit.SECONDS, this, mCallbacks);

    }


    @Override
    public void onResume() {
        super.onResume();


        CheckLocationPermission();

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
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(get_user, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                if (response.isNull(0)) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (signInOption.equals("Google")) {
                        mfbpic.hide();
                        updateUI();
                    } else if (signInOption.equals("FB")) {
                        mfbpic.hide();
                        UpdateUi(user);
                    } else if (signInOption.equals("Phone")) {
                        mfbpic.show();
                        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                        hasImage = "0";
                        mVerifyContainer.setVisibility(View.GONE);
                        mSigninContainer.setVisibility(View.VISIBLE);
                        mProgressDialog.cancel();
                    }


                } else {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            jsonObject = response.getJSONObject(i);
                            String u_name = jsonObject.getString("user_name");
                            String u_image = jsonObject.getString("user_image");
                            String u_phone = jsonObject.getString("phone");
                            String u_email = jsonObject.getString("email");


                            helpingMethods.saveuser(u_name, u_image, u_phone, u_email);

                        } catch (Exception e) {
                            Toast.makeText(Verification.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    Intent intent = new Intent(Verification.this, MainActivity.class);
                    if (getIntent().getStringExtra("for") != null) {
                        intent.putExtra("cart", "open");
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    mProgressDialog.cancel();
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.cancel();
                Toast.makeText(Verification.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestHandlerSingleten.getInstance(Verification.this).addToRequestQueue(jsonArrayRequest);

    }

    @Override
    public void onBackPressed() {
        if (mPhoneContainer.getVisibility() == View.VISIBLE) {
            mphoneLayout.setVisibility(View.GONE);
            mcardVew1.setVisibility(View.GONE);
            mPhoneContainer.setVisibility(View.GONE);
            mmainLayout.setVisibility(View.VISIBLE);
            signInOption = "Main";
        } else if (mVerifyContainer.getVisibility() == View.VISIBLE) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                            mPhoneNumber.setText("");
                            codeText.setText("");
                            mVerifyContainer.setVisibility(View.GONE);
                            mPhoneContainer.setVisibility(View.VISIBLE);
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(Verification.this);
            builder.setMessage("Do you want to go back?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();

        } else if (signInOption.equals("Phone") && mSigninContainer.getVisibility() == View.VISIBLE) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                            mPhoneNumber.setText("");
                            codeText.setText("");
                            mSigninContainer.setVisibility(View.GONE);
                            mPhoneContainer.setVisibility(View.VISIBLE);
                            signInOption = "Main";
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(Verification.this);
            builder.setMessage("Do you want to go back?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();

        } else if (mSigninContainer.getVisibility() == View.VISIBLE) {
            if (signInOption.equals("Google") || signInOption.equals("FB")) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                if (signInOption.equals("FB")) {
                                    SetUpFB();
                                }
                                moptional_number.setText("");
                                moptionalPhoneLayout.setVisibility(View.GONE);
                                mSigninContainer.setVisibility(View.GONE);
                                mmainLayout.setVisibility(View.VISIBLE);
                                signInOption = "Main";
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(Verification.this);
                builder.setMessage("Do you want to go back?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }


        } else if (mmainLayout.getVisibility() == View.VISIBLE) {
            finish();
        }


    }

    private void handleFacebookToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    get_user = "https://bringo.biz/api/get/client/verified?mob=" + FirebaseAuth.getInstance().getUid();
                    parseJSON();
                } else {
                    mProgressDialog.cancel();
                    Toast.makeText(Verification.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void UpdateUi(FirebaseUser user) {
        if (user != null) {
            mmusername.setText(user.getDisplayName());
            photoUrl = String.valueOf(user.getPhotoUrl());
            moptional_number.setText(user.getPhoneNumber());
            moptional_email.setText(user.getEmail());
            Glide.with(this).load(photoUrl).apply(new RequestOptions().placeholder(R.drawable.avatar)).into(musercrimage);
            mmainLayout.setVisibility(View.GONE);
            hasImage = "1";
            moptionalPhoneLayout.setVisibility(View.VISIBLE);
            mSigninContainer.setVisibility(View.VISIBLE);
            mProgressDialog.cancel();

        }


    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
            FirebaseGoogleAuth(acc);
        } catch (ApiException e) {
            mProgressDialog.cancel();
            Toast.makeText(Verification.this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acct) {
        if (acct != null) {
            AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        get_user = "https://bringo.biz/api/get/client/verified?mob=" + mAuth.getCurrentUser().getUid();
                        parseJSON();
                    } else {
                        mProgressDialog.cancel();
                        Toast.makeText(Verification.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            mProgressDialog.cancel();
            Toast.makeText(Verification.this, "acc failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (account != null) {
            String personName = account.getDisplayName();
            String personEmail = account.getEmail();
            photoUrl = String.valueOf(account.getPhotoUrl());
            moptional_email.setText(personEmail);
            mmusername.setText(personName);
            Glide.with(this).load(photoUrl).apply(new RequestOptions().placeholder(R.drawable.avatar)).into(musercrimage);
            hasImage = "1";
            mmainLayout.setVisibility(View.GONE);
            moptionalPhoneLayout.setVisibility(View.VISIBLE);
            mSigninContainer.setVisibility(View.VISIBLE);
            mProgressDialog.cancel();

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            mAuth.removeAuthStateListener(authStateListener);
        }
    }
}