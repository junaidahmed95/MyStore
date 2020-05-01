package com.bringo.home.ui.messages;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bringo.home.MessagingActivity;
import com.bringo.home.Model.NewUsers;
import com.bringo.home.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesFragment extends Fragment {

    private RecyclerView mConvList;
    private DatabaseReference mConvDatabase, mUsersDatabase;
    private FirebaseAuth mAuth;
    private TextView mName;
    String mNam, mImage, mEmail;
    private String getkey;
    ValueEventListener newListener;
    private final int CONTACT_PERMISSION_CODE = 2;
    boolean isImageFitToScreen;
    DatabaseReference reference;
    private boolean myownflag = false;
    public static MenuItem searchitem;
    public static boolean checkingtheqactivity = false;
    String name, image;
    private LinearLayoutManager linearLayoutManager;
    FirebaseRecyclerAdapter<NewUsers, ConvViewHolder> mAdapter;
    FirebaseRecyclerOptions<NewUsers> options;
    private String user_id;
    private List<NewUsers> usersList;
    private ProgressBar msmsPBar;

    public MessagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_messages, container, false);

        msmsPBar = mView.findViewById(R.id.smsPBar);
        mConvList = mView.findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mAuth = FirebaseAuth.getInstance();
        usersList = new ArrayList<>();
        user_id = mAuth.getUid();
        if (user_id != null) {
            msmsPBar.setVisibility(View.VISIBLE);
            mConvDatabase = FirebaseDatabase.getInstance().getReference().child("Chatlist");
            mConvDatabase.keepSynced(true);
            mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
            mUsersDatabase.keepSynced(true);
            reference = FirebaseDatabase.getInstance().getReference().child("Chats");

            FirebaseDatabase.getInstance().getReference().child("Chatlist").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    usersList.clear();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Long time = Long.parseLong(snapshot.child("time").getValue().toString());
                            usersList.add(new NewUsers(snapshot.child("name").getValue().toString(), snapshot.child("image").getValue().toString(), time));
                        }

                        Collections.sort(usersList, new Comparator<NewUsers>() {
                            @Override
                            public int compare(NewUsers lhs, NewUsers rhs) {
                                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                                return lhs.getTime() < rhs.getTime() ? -1 : (lhs.getTime() < rhs.getTime()) ? 1 : 0;
                            }
                        });

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }


        setHasOptionsMenu(true);

        return mView;

    }

    @Override
    public void onStart() {
        super.onStart();
        AllChats();
    }

    private void AllChats() {
        if (user_id != null) {
            options = new FirebaseRecyclerOptions.Builder<NewUsers>().setQuery(mConvDatabase.child(user_id).orderByChild("time"), NewUsers.class).build();
            mAdapter = new FirebaseRecyclerAdapter<NewUsers, ConvViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull final ConvViewHolder holder, final int position, @NonNull NewUsers model) {
                    final String u_id = getRef(position).getKey();

                    mConvDatabase.child(user_id).child(u_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChildren()) {
                                try {
                                    String sms = dataSnapshot.child("last").getValue().toString();
                                    Long time = Long.parseLong(dataSnapshot.child("time").getValue().toString());
                                    int unread = Integer.parseInt(dataSnapshot.child("unread").getValue().toString());
                                    if (dataSnapshot.hasChild("image") && dataSnapshot.hasChild("image")) {
                                        name = dataSnapshot.child("name").getValue().toString();
                                        image = dataSnapshot.child("image").getValue().toString();
                                        holder.setName(name);
                                        holder.setUserImage(image, getContext());
                                    }

                                    holder.setMessage(sms);
                                    holder.setTime(time);
                                    holder.setUnread(unread);

                                    if (dataSnapshot.hasChild("status")) {
                                        if (dataSnapshot.child("who").getValue().toString().equals(user_id)) {
                                            String smsStatus = dataSnapshot.child("status").getValue().toString();
                                            holder.setMessageStatus(smsStatus);
                                            holder.stImageView.setVisibility(View.VISIBLE);
                                        } else {
                                            holder.stImageView.setVisibility(View.GONE);
                                        }
                                    }


                                    //mConvList.smoothScrollToPosition(mConvList.getAdapter().getItemCount());


                                } catch (Exception e) {
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    mUsersDatabase.child("Owners").child(u_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if ((dataSnapshot.exists() && dataSnapshot.hasChild("full_name") && dataSnapshot.hasChild("picture"))) {
                                if (dataSnapshot.hasChild("status")) {
                                    Long st = Long.valueOf(dataSnapshot.child("status").getValue().toString());
                                    holder.setOnlineStatus(st, getContext());
                                }
                                msmsPBar.setVisibility(View.GONE);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    newListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    if (snapshot.child("reciver").getValue().toString().equals(u_id) && snapshot.child("sender").getValue().toString().equals(user_id)) {
                                        FirebaseDatabase.getInstance().getReference().child("Chats").child(snapshot.getKey()).child("delete").setValue(user_id);
                                    }
                                    if (snapshot.child("sender").getValue().toString().equals(u_id) && snapshot.child("reciver").getValue().toString().equals(user_id)) {
                                        FirebaseDatabase.getInstance().getReference().child("Chats").child(snapshot.getKey()).child("delete").setValue(user_id);
                                    }
                                }
                            }
                            //reference.removeEventListener(newListener);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }

                    };

                    holder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent mintent = new Intent(getActivity(), MessagingActivity.class);
                            mintent.putExtra("user_id", u_id);
                            mintent.putExtra("uName", usersList.get(position).getName());
                            mintent.putExtra("uImage", usersList.get(position).getImage());
                            mintent.putExtra("check", "one");
                            mintent.putExtra("forward", "one");
                            startActivity(mintent);
                            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    });

                    holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Are you sure?")
                                    .setContentText("Do you want to delete this chat permanently!")
                                    .setConfirmText("Delete")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(final SweetAlertDialog sDialog) {
                                            mConvDatabase.child(user_id).child(u_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    //umair
                                                    if (task.isSuccessful()) {
                                                        sDialog
                                                                .setTitleText("Deleted!")
                                                                .setContentText("Chat has been deleted!")
                                                                .setConfirmText("OK")
                                                                .setConfirmClickListener(null)
                                                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                                        FirebaseDatabase.getInstance().getReference("Chats").addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                //recevieerid = uid
                                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                    if (snapshot.getKey().contains(FirebaseAuth.getInstance().getUid() + u_id) || snapshot.getKey().contains(u_id + FirebaseAuth.getInstance().getUid())) {
                                                                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                                                            if (snapshot1.hasChild("deleted")) {
                                                                                FirebaseDatabase.getInstance().getReference("Chats").child(snapshot.getKey()).child(snapshot1.getKey()).removeValue();

                                                                            } else {
                                                                                FirebaseDatabase.getInstance().getReference().child("Chats").child(snapshot.getKey()).child(snapshot1.getKey()).child("deleted").setValue(user_id);
                                                                            }
                                                                        }

                                                                    }

                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                            }
                                                        });
                                                        ;


                                                    }
                                                }
                                            });
                                        }
                                    })
                                    .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                            return true;
                        }
                    });


                }

                @NonNull
                @Override
                public ConvViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_item, viewGroup, false);
                    return new ConvViewHolder(view);
                }

//                @Override
//                public int getItemCount() {
//                    return super.getItemCount();
//
//                }
            };
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
            mConvList.setLayoutManager(linearLayoutManager);
            mAdapter.startListening();
            mConvList.setAdapter(mAdapter);

//            mConvList.smoothScrollToPosition();

        }

    }


    @Override
    public void onStop() {
        super.onStop();
        if (mAuth.getUid() != null)
            mAdapter.stopListening();
    }


    public static class ConvViewHolder extends RecyclerView.ViewHolder {

        View mView;
        TextView unraedTet, convTime;
        ImageView mConv_image;
        ImageView stImageView;

        public ConvViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            unraedTet = mView.findViewById(R.id.unread_sms);
            convTime = mView.findViewById(R.id.conv_time);
            mConv_image = mView.findViewById(R.id.conv_image);
        }

        public void setMessage(String message) {
            TextView userStatusView = mView.findViewById(R.id.conv_msg);
            userStatusView.setText(message);
        }

        public void setUnread(int un) {
            if (un > 0) {
                unraedTet.setText(String.valueOf(un));
                unraedTet.setVisibility(View.VISIBLE);
            } else {
                unraedTet.setVisibility(View.GONE);
            }
        }

        private void setMessageStatus(String status) {
            stImageView = mView.findViewById(R.id.stus);
            if (status.equals("seen")) {
                stImageView.setImageResource(R.drawable.ic_action_seen);
            } else if (status.equals("sent")) {
                stImageView.setImageResource(R.drawable.ic_action_send);
            }
        }

        public void setTime(Long time) {
            Date currentDate = new Date();
            SimpleDateFormat currentFormat = new SimpleDateFormat("dd/MM/yyyy");
            String currentString = currentFormat.format(currentDate);

            Date dbDate = new Date(time);
            SimpleDateFormat dbFormat = new SimpleDateFormat("dd/MM/yyyy");
            String dbString = dbFormat.format(dbDate);

            Date dayDate = new Date(time);
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
            String dayString = dayFormat.format(dayDate);

            Calendar cal1 = Calendar.getInstance();
            cal1.add(Calendar.DATE, -1);

            Calendar cal2 = Calendar.getInstance();
            cal2.add(Calendar.DATE, -2);

            Calendar cal3 = Calendar.getInstance();
            cal3.add(Calendar.DATE, -3);

            Calendar cal4 = Calendar.getInstance();
            cal4.add(Calendar.DATE, -4);

            Calendar cal5 = Calendar.getInstance();
            cal5.add(Calendar.DATE, -5);

            Calendar cal6 = Calendar.getInstance();
            cal6.add(Calendar.DATE, -6);

            Calendar cal7 = Calendar.getInstance();
            cal7.add(Calendar.DATE, -7);

            SimpleDateFormat cal1Format = new SimpleDateFormat("dd/MM/yyyy");
            String cal1String = cal1Format.format(cal1.getTime());

            SimpleDateFormat cal2Format = new SimpleDateFormat("dd/MM/yyyy");
            String cal2String = cal2Format.format(cal2.getTime());

            SimpleDateFormat cal3Format = new SimpleDateFormat("dd/MM/yyyy");
            String cal3String = cal3Format.format(cal3.getTime());

            SimpleDateFormat cal4Format = new SimpleDateFormat("dd/MM/yyyy");
            String cal4String = cal4Format.format(cal4.getTime());

            SimpleDateFormat cal5Format = new SimpleDateFormat("dd/MM/yyyy");
            String cal5String = cal5Format.format(cal5.getTime());

            SimpleDateFormat cal6Format = new SimpleDateFormat("dd/MM/yyyy");
            String cal6String = cal6Format.format(cal6.getTime());

            SimpleDateFormat cal7Format = new SimpleDateFormat("dd/MM/yyyy");
            String cal7String = cal7Format.format(cal7.getTime());


            if (dbString.equals(currentString)) {
                Date date = new Date(time);
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                String tim = dateFormat.format(date).toUpperCase();
                setCovDateTime(tim);
            } else if (dbString.equals(cal1String)) {
                setCovDateTime("Yesterday");
            } else if (dbString.equals(cal2String) | dbString.equals(cal3String) | dbString.equals(cal4String) | dbString.equals(cal5String) | dbString.equals(cal6String) | dbString.equals(cal7String)) {
                setCovDateTime(dayString);
            } else {
                Date timeDate = new Date(time);
                SimpleDateFormat timeDateFormat = new SimpleDateFormat("E dd/MM/yyyy");
                String timeDateDtring = timeDateFormat.format(timeDate);
                setCovDateTime(timeDateDtring);
            }


        }

        private void setCovDateTime(String dateTime) {
            convTime.setText(dateTime);
        }

        public void setName(String name) {
            TextView userNameView = mView.findViewById(R.id.conv_name);
            userNameView.setText(name);
        }

        public void setUserImage(final String thumb_image, final Context ctx) {
            final CircleImageView userImageView = mView.findViewById(R.id.conv_image);
            if (ctx != null) {
                userImageView.setVisibility(View.VISIBLE);
                Glide.with(ctx).asBitmap().load(thumb_image).apply(new RequestOptions().placeholder(R.drawable.avatar)).into(userImageView);
            }
        }

        public void setOnlineStatus(Long status, Context context) {
            ImageView mStatusi = mView.findViewById(R.id.online_icon);
            if (status == 0) {
                mStatusi.setVisibility(View.VISIBLE);
            } else {
                mStatusi.setVisibility(View.GONE);
            }
        }

    }
}