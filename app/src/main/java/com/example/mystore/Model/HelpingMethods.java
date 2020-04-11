package com.example.mystore.Model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.mystore.MessagingActivity;
import com.example.mystore.R;
import com.example.mystore.videoView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HelpingMethods {

    private Activity activity;
    private String mNam, mImage, mEmail, mPhone;

    public HelpingMethods(Activity activity) {
        this.activity = activity;
    }

    public void SnackBar(String msg, View v) {
        Snackbar sb = Snackbar.make(v, msg, Snackbar.LENGTH_SHORT);
        sb.show();
    }

    public void alertmututual(final String u_id, final Context context) {

        final AlertDialog.Builder alertadd = new AlertDialog.Builder(activity);
        LayoutInflater factory = LayoutInflater.from(activity);
        final View view = factory.inflate(R.layout.profile_dialog, null);
        final ImageView a = view.findViewById(R.id.dialog_imageview);
        final TextView b = view.findViewById(R.id.usernamecatalog);

        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Owners").child(u_id);
        userDb.keepSynced(true);
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    mNam = dataSnapshot.child("full_name").getValue().toString();
                    mImage = dataSnapshot.child("picture").getValue().toString();
                    mPhone = dataSnapshot.child("phone").getValue().toString();
                    b.setText(mNam);
                    Glide.with(activity).asBitmap().load(mImage).apply(new RequestOptions().placeholder(R.drawable.avatar)).into(a);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, videoView.class);
                intent.putExtra("image", mImage);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

        //Picasso.with(mContext).load(user.getPhoto()).fit().into(a);
        Button call, chat, info;
        call = view.findViewById(R.id.callcatag);
        chat = view.findViewById(R.id.chatcatag);
        info = view.findViewById(R.id.infocatag);
        alertadd.setView(view);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri numberuri = Uri.parse("tel:" + "0" + mPhone);
                Intent dial = new Intent(Intent.ACTION_DIAL);
                dial.setData(numberuri);
                activity.startActivity(dial);
                ((Activity) context).finish();


            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent send = new Intent(activity, Userdetails.class);
//                send.putExtra("user_id", u_id);
//                send.putExtra("user", mNam);
//                send.putExtra("photo", mImage);
//                send.putExtra("email", mEmail);
//                activity.startActivity(send);
//                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(activity, MessagingActivity.class);
                a.putExtra("user_id", u_id);
                //a.putExtra("sms",forward);
                activity.startActivity(a);
                ((Activity) context).finish();


            }
        });

        alertadd.show();
    }

}
