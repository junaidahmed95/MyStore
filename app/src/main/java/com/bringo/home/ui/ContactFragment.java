package com.bringo.home.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bringo.home.MessagingActivity;
import com.bringo.home.Model.ConnectionDetector;
import com.bringo.home.Model.HelpingMethods;
import com.bringo.home.R;
import com.bringo.home.Verification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {

    private ImageButton mcallBtn, memailBtn,mchatBtn;
    private Button mProceedBtn;
    private String sName, sImage;
    private HelpingMethods helpingMethods;
    private EditText msubEditText, mmessEditText;
    private TextView mphoneNumb, memailText;

    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_contact, container, false);

        helpingMethods = new HelpingMethods(getActivity());

        if (FirebaseAuth.getInstance().getUid() != null && helpingMethods.GetUName()!=null) {

            FirebaseDatabase.getInstance().getReference("Users").child("Owners").child("BrwELMpWZEa057zF77OZdTx63k23").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        sName = dataSnapshot.child("full_name").getValue().toString();
                        sImage = dataSnapshot.child("picture").getValue().toString();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }



        mphoneNumb = rootView.findViewById(R.id.phoneNumb);
        memailText = rootView.findViewById(R.id.emailText);
        msubEditText = rootView.findViewById(R.id.subEditText);
        mchatBtn = rootView.findViewById(R.id.chatBtn);
        mmessEditText = rootView.findViewById(R.id.messEditText);
        mProceedBtn = rootView.findViewById(R.id.proceedBtn);
        memailBtn = rootView.findViewById(R.id.emailBtn);
        memailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendEmail();
            }
        });

        mchatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FirebaseAuth.getInstance().getUid()!=null && helpingMethods.GetUName()!=null){
                    ConnectionDetector connectionDetector = new ConnectionDetector(getActivity());
                    if(connectionDetector.isConnected()){
                        Intent mintent = new Intent(getActivity(), MessagingActivity.class);
                        mintent.putExtra("user_id", "BrwELMpWZEa057zF77OZdTx63k23");
                        mintent.putExtra("uName", sName);
                        mintent.putExtra("uImage", sImage);
                        mintent.putExtra("check", "one");
                        mintent.putExtra("forward", "one");
                        startActivity(mintent);
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                }else {
                    startActivity(new Intent(getActivity(), Verification.class));
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }

            }
        });

        mProceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mmessEditText.getText().toString().trim().equals("")) {
                    SendEmail();
                } else {
                    Toast.makeText(getActivity(), "Enter your message.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mcallBtn = rootView.findViewById(R.id.callBtn);
        mcallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MakeACall();
            }
        });

        return rootView;
    }

    private void SendEmail() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        String[] strTo = {memailText.getText().toString()};
        intent.putExtra(Intent.EXTRA_EMAIL, strTo);
        intent.putExtra(Intent.EXTRA_SUBJECT, msubEditText.getText().toString());
        intent.putExtra(Intent.EXTRA_TEXT, mmessEditText.getText().toString());
        intent.setType("message/rfc822");
        intent.setPackage("com.google.android.gm");

        try {
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "There is no any email app.", Toast.LENGTH_LONG).show();
        }
    }


    private void MakeACall() {
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.CALL_PHONE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + mphoneNumb.getText().toString()));
                        startActivity(callIntent);
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                            builder.setTitle("Permission Denied")
                                    .setMessage("Permission to make a phone call is permanently denied. you need to go to setting to allow the permission.")
                                    .setNegativeButton("Cancel", null)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            intent.setData(Uri.fromParts("package", getActivity().getPackageName(), null));
                                        }
                                    })
                                    .show();
                        } else {
                            Toast.makeText(getContext(), "Permission Denied.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check();
    }

}
