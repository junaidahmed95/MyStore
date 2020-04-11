package com.example.mystore.ui.account;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.mystore.MainActivity;
import com.example.mystore.OrderTrackActivity;
import com.example.mystore.ProfileActivity;
import com.example.mystore.R;
import com.example.mystore.VaultActivity;
import com.example.mystore.Verification;
import com.example.mystore.WishActivity;
import com.example.mystore.ui.HistoryActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment {

    private CircleImageView mProfile;
    private FirebaseAuth mAuth;
    private CardView mLogOut, mProfileCard;
    private String image, name;
    private TextView mName, mEmail;
    private ProgressBar mProgressBar;
    private RelativeLayout mVaultLayout, mwishlayout,mlayout_history,mlayout_history1;




    public View onCreateView(@NonNull LayoutInflater inflater,

                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.account_fragment, container, false);

        mlayout_history1= root.findViewById(R.id.layout_history1);
        mProfile = root.findViewById(R.id.userProfile);
        mEmail = root.findViewById(R.id.userEmail);
        mName = root.findViewById(R.id.userName);
        mProgressBar = root.findViewById(R.id.pBar);
        mLogOut = root.findViewById(R.id.logOutLayout);
        mVaultLayout = root.findViewById(R.id.vaultLayout);
        mProfileCard = root.findViewById(R.id.profileCard);
        mwishlayout = root.findViewById(R.id.wishlayout);
        mlayout_history = root.findViewById(R.id.layout_history);


        mAuth = FirebaseAuth.getInstance();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(mAuth.getUid());
        userRef.keepSynced(true);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    try {
                        image = dataSnapshot.child("picture").getValue().toString();
                        name = dataSnapshot.child("name").getValue().toString();
                        Glide.with(getActivity()).asBitmap().load(image).apply(new RequestOptions().placeholder(R.drawable.avatar)).into(mProfile);
                        mName.setText(name);
                        mEmail.setText(dataSnapshot.child("phone").getValue().toString());
                    } catch (Exception e) {

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mlayout_history1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), OrderTrackActivity.class));
            }
        });
        mlayout_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), HistoryActivity.class);
                startActivity(intent);
            }
        });

        mwishlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WishActivity.class);
                startActivity(intent);

            }
        });

        mLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), Verification.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });

        mVaultLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), VaultActivity.class);
                intent.putExtra("image", image);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });

        mProfileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }


}
