package com.bringo.home.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.content.ClipboardManager;
import android.location.Geocoder;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bringo.home.MessagingActivity;
import com.bringo.home.Model.CatLvlItemList;
import com.bringo.home.Model.Chats;
import com.bringo.home.Model.ConnectionDetector;
import com.bringo.home.Model.HelpingMethods;
import com.bringo.home.R;
import com.bringo.home.ViewImage;
import com.bringo.home.videoView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.bringo.home.MessagingActivity.getchatroot;
import static com.bringo.home.MessagingActivity.receiver_name;

public class MessagingAdapter extends RecyclerView.Adapter<MessagingAdapter.ViewHolder> implements Filterable {

    HelpingMethods helpingMethods;
    public static int MSG_TYPE_LEFT = 0;
    public static int MSG_TYPE_RIGHT = 1;
    boolean gett = true;
    Handler seekHandler = new Handler();
    private String mTitle = "";
    Runnable run;
    public static int mPrePos = -1;
    private Context mContext;
    List<Chats> mChat;
    public static List<Chats> mUserFull;
    SeekBar mSeekBar;
    int getchatsize = 0;
    public static String playingStatus = "true";
    MediaPlayer mediaPlayer;
    private BottomSheetDialog orderdetailSheetDialog;
    public static boolean checkflagforme = false;
    public static MediaPlayer myplayer;
    MediaPlayer mMediaPlayer;
    FirebaseUser fuser;
    int getval = 0;
    TextView mTvAudioLength;
    DatabaseReference picRef;

    public MessagingAdapter(Context mContext, List<Chats> mChat) {
        this.mContext = mContext;
        this.mChat = mChat;
        mUserFull = new ArrayList<>(mChat);
    }

    public MessagingAdapter() {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        final Activity activity = (Activity) mContext;
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        helpingMethods = new HelpingMethods(activity);
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, viewGroup, false);
            ViewHolder vh = new ViewHolder(view);
            return vh;
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, viewGroup, false);
            ViewHolder vh = new ViewHolder(view);
            return vh;
        }


    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        final Chats chat = mChat.get(i);
        final MediaPlayer mediaPlayer = new MediaPlayer();
        chat.setChatMediaPlayer(mediaPlayer);
        if (chat.getType() != null) {
            if (chat.getType().equals("text")) {
                viewHolder.videoCard.setVisibility(View.GONE);

                viewHolder.voicecrd.setVisibility(View.GONE);
                viewHolder.morder_CardView.setVisibility(View.GONE);
                viewHolder.image.setVisibility(View.GONE);
                viewHolder.Map_link_card.setVisibility(View.GONE);
                Date date = new Date(chat.getTime());
                SimpleDateFormat dateFormat = new SimpleDateFormat(" hh:mm a  dd/MM/yyyy");

                String tim = dateFormat.format(date).toUpperCase();
                viewHolder.show_message.setText(chat.getMessage());
                viewHolder.txt_time.setText(tim);
                viewHolder.message.setVisibility(View.VISIBLE);
//            if (chat.getStatus().equals("available") && !chat.isIsseen()) {
//                viewHolder.txt_seen.setImageResource(R.drawable.ic_tickdelivery);
//            } else
                if (!chat.isSeen()) {
                    viewHolder.txt_seen.setImageResource(R.drawable.ic_done_black_24dp);
                } else {
                    viewHolder.txt_seen.setImageResource(R.drawable.ic_done_all_black_24dp);
                }

                viewHolder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        final String[] options = {"Copy", "Delete", "Share"};
                        final String[] optionforreceiver = {"Copy", "Delete for me", "Share"};
                        //umair
                        AlertDialog.Builder bb = new AlertDialog.Builder(mContext);
                        bb.setTitle("Select");
                        if (chat.getSender().equals(MessagingActivity.receiver_id)) {
                            bb.setItems(optionforreceiver, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if ("Share".equals(optionforreceiver[which])) {
                                        Intent i = new Intent();
                                        i.setAction(Intent.ACTION_SEND);
                                        i.putExtra(Intent.EXTRA_TEXT, chat.getMessage());
                                        i.setType("text/plain");
                                        mContext.startActivity(i);
                                        return;
                                    } else {
                                        if ("Copy".equals(optionforreceiver[which])) {
                                            Toast.makeText(mContext, "Message copied", Toast.LENGTH_SHORT).show();
                                            ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                                            ClipData clip = ClipData.newPlainText("message", chat.getMessage());
                                            clipboard.setPrimaryClip(clip);
                                        } else if ("Delete for me".equals(optionforreceiver[which])) {
                                            deletemessageforme(chat.getChatid());
                                        }
                                    }
                                }
                            });
                            bb.show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setTitle("Select your choice");
                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if ("Delete".equals(options[which])) {
                                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                switch (which) {
                                                    case DialogInterface.BUTTON_POSITIVE:
                                                        FirebaseDatabase.getInstance().getReference("Chats").child(getchatroot).child(chat.getChatid()).child("message").setValue("\uD835\uDE1B\uD835\uDE29\uD835\uDE2A\uD835\uDE34 \uD835\uDE2E\uD835\uDE26\uD835\uDE34\uD835\uDE34\uD835\uDE22\uD835\uDE28\uD835\uDE26 \uD835\uDE38\uD835\uDE22\uD835\uDE34 \uD835\uDE25\uD835\uDE26\uD835\uDE2D\uD835\uDE26\uD835\uDE35\uD835\uDE26\uD835\uDE25").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task
                                                                        .isSuccessful()) {
                                                                    Toast toast = Toast.makeText(mContext, "Deleted", Toast.LENGTH_LONG);
                                                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                                                    toast.show();
                                                                } else {
                                                                    Toast.makeText(mContext, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                                        //FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("status").setValue(status);


                                                        break;

                                                    case DialogInterface.BUTTON_NEGATIVE:
                                                        //No button clicked
                                                        break;
                                                }
                                            }
                                        };

                                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                                                .setNegativeButton("No", dialogClickListener).show();

                                    } else if ("Share".equals(options[which])) {
                                        Intent i = new Intent();
                                        i.setAction(Intent.ACTION_SEND);
                                        i.putExtra(Intent.EXTRA_TEXT, chat.getMessage());
                                        i.setType("text/plain");
                                        mContext.startActivity(i);
                                    } else if ("Copy".equals(options[which])) {
                                        Toast.makeText(mContext, "Message copied", Toast.LENGTH_SHORT).show();
                                        ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                                        ClipData clip = ClipData.newPlainText("message", chat.getMessage());
                                        clipboard.setPrimaryClip(clip);
                                    }
                                }
                            });

                            builder.show();
                        }

                        return false;
                    }
                });


            } else if (chat.getType().equals("voice")) {
                Date date = new Date(chat.getTime());
                SimpleDateFormat dateFormat = new SimpleDateFormat(" hh:mm a  dd/MM/yyyy");
                String tim = dateFormat.format(date).toUpperCase();
                viewHolder.morder_CardView.setVisibility(View.GONE);
                viewHolder.image.setVisibility(View.GONE);
                viewHolder.Map_link_card.setVisibility(View.GONE);
                viewHolder.message.setVisibility(View.GONE);
                viewHolder.videoCard.setVisibility(View.GONE);
                viewHolder.mvo_time.setText(tim);


                if (chat.isUpload()) {
                    viewHolder.mLoad.setVisibility(View.GONE);

                } else {
                    viewHolder.mLoad.setVisibility(View.VISIBLE);
                }
                viewHolder.voicecrd.setVisibility(View.VISIBLE);

                if (chat.isSeen()) {
                    viewHolder.mvo_seen.setImageResource(R.drawable.ic_done_all_black_24dp);
                } else {
                    viewHolder.mvo_seen.setImageResource(R.drawable.ic_done_black_24dp);
                }

                mChat.get(i).setPause(true);

                if (mChat.get(i).isPause()) {
                    viewHolder.btn_play.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
                } else {
                    viewHolder.btn_play.setImageResource(R.drawable.ic_pause_black_24dp);
                }

                try {
                    chat.getChatMediaPlayer().setDataSource(chat.getMessage());
                    chat.getChatMediaPlayer().prepareAsync();

                    chat.getChatMediaPlayer().setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            viewHolder.tvAudioLength.setText(calculateDuration(chat.getChatMediaPlayer().getDuration()));
                            mMediaPlayer = chat.getChatMediaPlayer();
                            mTvAudioLength = viewHolder.tvAudioLength;
                            mSeekBar = viewHolder.seekBar;
                            viewHolder.seekBar.setMax(chat.getChatMediaPlayer().getDuration());
                            //run.run();
                            viewHolder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                    if (chat.getChatMediaPlayer() != null && fromUser) {
                                        chat.getChatMediaPlayer().seekTo(progress);
                                    }
                                }

                                @Override
                                public void onStartTrackingTouch(SeekBar seekBar) {

                                }

                                @Override
                                public void onStopTrackingTouch(SeekBar seekBar) {

                                }
                            });

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

                viewHolder.btn_play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!chat.getChatMediaPlayer().isPlaying()) {
                            checkflagforme = true;
                            if (mPrePos == -1) {
                                mPrePos = i;
                            } else {
                                mChat.get(mPrePos).getChatMediaPlayer().pause();
                                mPrePos = i;

                            }
                            myplayer = chat.getChatMediaPlayer();
                            myplayer.start();
                            viewHolder.btn_play.setImageResource(R.drawable.ic_pause_black_24dp);
                            run = new Runnable() {
                                @Override
                                public void run() {
                                    // Updateing SeekBar every 100 miliseconds
                                    viewHolder.seekBar.setProgress(chat.getChatMediaPlayer().getCurrentPosition());
                                    seekHandler.postDelayed(run, 100);
                                    //For Showing time of audio(inside runnable)
                                    int miliSeconds = chat.getChatMediaPlayer().getCurrentPosition();
                                    if (miliSeconds != 0) {
                                        //if audio is playing, showing current time;
                                        long minutes = TimeUnit.MILLISECONDS.toMinutes(miliSeconds);
                                        long seconds = TimeUnit.MILLISECONDS.toSeconds(miliSeconds);
                                        if (minutes == 0) {
                                            viewHolder.tvAudioLength.setText("0:" + seconds + "/" + calculateDuration(chat.getChatMediaPlayer().getDuration()));
                                        } else {
                                            if (seconds >= 60) {
                                                long sec = seconds - (minutes * 60);
                                                viewHolder.tvAudioLength.setText(minutes + ":" + sec + "/" + calculateDuration(chat.getChatMediaPlayer().getDuration()));
                                            }
                                        }
                                    } else {
                                        //Displaying total time if audio not playing
                                        int totalTime = chat.getChatMediaPlayer().getDuration();
                                        long minutes = TimeUnit.MILLISECONDS.toMinutes(totalTime);
                                        long seconds = TimeUnit.MILLISECONDS.toSeconds(totalTime);
                                        if (minutes == 0) {
                                            viewHolder.tvAudioLength.setText("0:" + seconds);
                                        } else {
                                            if (seconds >= 60) {
                                                long sec = seconds - (minutes * 60);
                                                viewHolder.tvAudioLength.setText(minutes + ":" + sec);
                                            }
                                        }
                                    }
                                }

                            };
                            run.run();

                        } else {
                            checkflagforme = false;
                            myplayer = chat.getChatMediaPlayer();
                            myplayer.pause();
                            viewHolder.btn_play.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
                        }
                        //stopplaying();
//                        if (!mediaPlayer.isPlaying()) {
//                            if (playingStatus.equals("true")) {
//                                playingStatus = "false";
//                                myplayer = mediaPlayer;
//                                myplayer.start();
//                                checkflagforme = true;
//                                viewHolder.btn_play.setImageResource(R.drawable.ic_pause);
//                                run = new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        // Updateing SeekBar every 100 miliseconds
//                                        viewHolder.seekBar.setProgress(mediaPlayer.getCurrentPosition());
//                                        seekHandler.postDelayed(run, 100);
//                                        //For Showing time of audio(inside runnable)
//                                        int miliSeconds = mediaPlayer.getCurrentPosition();
//                                        if (miliSeconds != 0) {
//                                            //if audio is playing, showing current time;
//                                            long minutes = TimeUnit.MILLISECONDS.toMinutes(miliSeconds);
//                                            long seconds = TimeUnit.MILLISECONDS.toSeconds(miliSeconds);
//                                            if (minutes == 0) {
//                                                viewHolder.tvAudioLength.setText("0:" + seconds + "/" + calculateDuration(mediaPlayer.getDuration()));
//                                            } else {
//                                                if (seconds >= 60) {
//                                                    long sec = seconds - (minutes * 60);
//                                                    viewHolder.tvAudioLength.setText(minutes + ":" + sec + "/" + calculateDuration(mediaPlayer.getDuration()));
//                                                }
//                                            }
//                                        } else {
//                                            //Displaying total time if audio not playing
//                                            int totalTime = mediaPlayer.getDuration();
//                                            long minutes = TimeUnit.MILLISECONDS.toMinutes(totalTime);
//                                            long seconds = TimeUnit.MILLISECONDS.toSeconds(totalTime);
//                                            if (minutes == 0) {
//                                                viewHolder.tvAudioLength.setText("0:" + seconds);
//                                            } else {
//                                                if (seconds >= 60) {
//                                                    long sec = seconds - (minutes * 60);
//                                                    viewHolder.tvAudioLength.setText(minutes + ":" + sec);
//                                                }
//                                            }
//                                        }
//                                    }
//
//                                };
//                                run.run();
//                            }
//
//                        } else {
//                            if (playingStatus.equals("false")) {
//                                playingStatus = "true";
//                                myplayer = mediaPlayer;
//                                myplayer.pause();
//                                viewHolder.btn_play.setImageResource(R.drawable.ic_play);
//                            }
//                        }
                    }
                });

                if (mediaPlayer != null) {
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            playingStatus = "true";
                            viewHolder.btn_play.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
                        }
                    });

                }

                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        final String[] options = {"Delete", "Share"};
                        final String[] optionforreceiver = {"Share", "Delete for me"};

                        AlertDialog.Builder bb = new AlertDialog.Builder(mContext);
                        bb.setTitle("Select");
                        if (chat.getSender().equals(MessagingActivity.receiver_id)) {
                            bb.setItems(optionforreceiver, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if ("Share".equals(optionforreceiver[which])) {
                                        Intent i = new Intent();
                                        i.setAction(Intent.ACTION_SEND);
                                        i.putExtra(Intent.EXTRA_TEXT, chat.getMessage());
                                        i.setType("Audio/*");
                                        mContext.startActivity(i);
                                        return;
                                    } else {
                                        if ("Delete for me".equals(optionforreceiver[which])) {
                                            deletemessageforme(chat.getChatid());
                                        }

                                    }
                                }
                            });
                            bb.show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setTitle("Select your choice");
                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if ("Delete".equals(options[which])) {
                                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                switch (which) {
                                                    case DialogInterface.BUTTON_POSITIVE:
                                                        deletemessage(chat.getChatid());

                                                        //FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("status").setValue(status);


                                                        break;

                                                    case DialogInterface.BUTTON_NEGATIVE:
                                                        //No button clicked
                                                        break;
                                                }
                                            }
                                        };

                                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                                                .setNegativeButton("No", dialogClickListener).show();

                                    } else if ("Share".equals(options[which])) {
                                        Intent i = new Intent();
                                        i.setAction(Intent.ACTION_SEND);
                                        i.putExtra(Intent.EXTRA_TEXT, chat.getMessage());
                                        i.setType("Audio/*");
                                        mContext.startActivity(i);
                                    }
                                }
                            });

                            builder.show();
                        }

                        return false;
                    }
                });


            } else if (chat.getType().equals("image")) {
                Date date = new Date(chat.getTime());
                SimpleDateFormat dateFormat = new SimpleDateFormat(" hh:mm a  dd/MM/yyyy");
                String tim = dateFormat.format(date).toUpperCase();
                viewHolder.videoCard.setVisibility(View.GONE);

                viewHolder.morder_CardView.setVisibility(View.GONE);
                viewHolder.voicecrd.setVisibility(View.GONE);
                viewHolder.Map_link_card.setVisibility(View.GONE);
                viewHolder.message.setVisibility(View.GONE);
                viewHolder.txt_time1.setText(tim);

                Glide.with(mContext).asBitmap().load(chat.getMessage()).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(viewHolder.show_Image);
                viewHolder.image.setVisibility(View.VISIBLE);

                if (chat.isSeen()) {
                    viewHolder.txt_seen1.setImageResource(R.drawable.ic_done_all_black_24dp);
                } else {
                    viewHolder.txt_seen1.setImageResource(R.drawable.ic_done_black_24dp);
                }


                viewHolder.show_Image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (chat.getSender().equals(FirebaseAuth.getInstance().getUid())) {
                            mTitle = "You";
                        } else {
                            mTitle = receiver_name;
                        }
                        Intent iView = new Intent(mContext, ViewImage.class);
                        iView.putExtra("imageview", chat.getMessage());
                        iView.putExtra("for", "image");
                        iView.putExtra("title", mTitle);
                        mContext.startActivity(iView);
                    }
                });


                viewHolder.show_Image.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        final String[] options = {"Delete", "Share"};
                        final String[] optionforreceiver = {"Share", "Delete for me"};


                        AlertDialog.Builder bb = new AlertDialog.Builder(mContext);
                        bb.setTitle("Select");
                        if (chat.getSender().equals(MessagingActivity.receiver_id)) {
                            bb.setItems(optionforreceiver, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if ("Share".equals(optionforreceiver[which])) {
                                        Intent i = new Intent();
                                        i.setAction(Intent.ACTION_SEND);
                                        i.putExtra(Intent.EXTRA_TEXT, chat.getMessage());
                                        i.setType("image/jpg");
                                        mContext.startActivity(i);
                                        return;
                                    } else {
                                        if ("Delete for me".equals(optionforreceiver[which])) {
                                            deletemessageforme(chat.getChatid());

                                        }
                                    }
                                }
                            });
                            bb.show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setTitle("Select your choice");
                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if ("Delete".equals(options[which])) {
                                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                switch (which) {
                                                    case DialogInterface.BUTTON_POSITIVE:
                                                        HashMap hashMap = new HashMap();
                                                        hashMap.put("message", "\uD835\uDE1B\uD835\uDE29\uD835\uDE2A\uD835\uDE34 \uD835\uDE2E\uD835\uDE26\uD835\uDE34\uD835\uDE34\uD835\uDE22\uD835\uDE28\uD835\uDE26 \uD835\uDE38\uD835\uDE22\uD835\uDE34 \uD835\uDE25\uD835\uDE26\uD835\uDE2D\uD835\uDE26\uD835\uDE35\uD835\uDE26\uD835\uDE25");
                                                        hashMap.put("type", "text");
                                                        FirebaseDatabase.getInstance().getReference("Chats").child(getchatroot).child(chat.getChatid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                                            @Override
                                                            public void onComplete(@NonNull Task task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast toast = Toast.makeText(mContext, "Deleted", Toast.LENGTH_LONG);
                                                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                                                    toast.show();
                                                                }
                                                            }
                                                        });
                                                        //FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("status").setValue(status);


                                                        break;

                                                    case DialogInterface.BUTTON_NEGATIVE:
                                                        //No button clicked
                                                        break;
                                                }
                                            }
                                        };

                                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                                                .setNegativeButton("No", dialogClickListener).show();

                                    } else if ("Share".equals(options[which])) {
                                        Intent i = new Intent();
                                        i.setAction(Intent.ACTION_SEND);
                                        i.putExtra(Intent.EXTRA_TEXT, chat.getMessage());
                                        i.setType("image/jpg");
                                        mContext.startActivity(i);
                                    }
                                }
                            });

                            builder.show();
                        }
                        return false;
                    }
                });


            } else if (chat.getType().equals("video")) {
                Date date = new Date(chat.getTime());
                SimpleDateFormat dateFormat = new SimpleDateFormat(" hh:mm a  dd/MM/yyyy");
                String tim = dateFormat.format(date).toUpperCase();
                Glide.with(mContext).asBitmap().load(chat.getMessage()).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(viewHolder.show_video);
                viewHolder.videoCard.setVisibility(View.VISIBLE);


                viewHolder.voicecrd.setVisibility(View.GONE);
                viewHolder.morder_CardView.setVisibility(View.GONE);
                viewHolder.Map_link_card.setVisibility(View.GONE);
                viewHolder.message.setVisibility(View.GONE);
                viewHolder.image.setVisibility(View.GONE);
                viewHolder.mv_time.setText(tim);

                if (chat.isUpload()) {
                    viewHolder.vidload.setVisibility(View.GONE);

                } else {
                    viewHolder.vidload.setVisibility(View.VISIBLE);
                }

                if (chat.isSeen()) {
                    viewHolder.mv_seen.setImageResource(R.drawable.ic_done_all_black_24dp);
                } else {
                    viewHolder.mv_seen.setImageResource(R.drawable.ic_done_black_24dp);
                }


                viewHolder.show_video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ConnectionDetector connectionDetector = new ConnectionDetector(mContext);
                        if (connectionDetector.isConnected()) {
                            Intent i = new Intent(mContext, videoView.class);
                            i.putExtra("video", chat.getMessage());
                            mContext.startActivity(i);
                        } else {
                            Toast.makeText(mContext, "Check your internet connection", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                viewHolder.show_video.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {


                        final String[] options = {"Delete", "Share"};
                        final String[] optionforreceiver = {"Share", "Delete for me"};
                        //umair


                        AlertDialog.Builder bb = new AlertDialog.Builder(mContext);
                        bb.setTitle("Select");
                        if (chat.getSender().equals(MessagingActivity.receiver_id)) {
                            bb.setItems(optionforreceiver, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if ("Share".equals(optionforreceiver[which])) {
                                        Intent i = new Intent();
                                        i.setAction(Intent.ACTION_SEND);
                                        i.putExtra(Intent.EXTRA_TEXT, chat.getMessage());
                                        i.setType("video/*");
                                        mContext.startActivity(i);
                                        return;
                                    } else {
                                        if ("Delete for me".equals(optionforreceiver[which])) {
                                            deletemessageforme(chat.getChatid());
                                        }

                                    }
                                }
                            });
                            bb.show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setTitle("Select your choice");
                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if ("Delete".equals(options[which])) {
                                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                switch (which) {
                                                    case DialogInterface.BUTTON_POSITIVE:
                                                        deletemessage(chat.getChatid());

                                                        //FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("status").setValue(status);


                                                        break;

                                                    case DialogInterface.BUTTON_NEGATIVE:
                                                        //No button clicked
                                                        break;
                                                }
                                            }
                                        };

                                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                                                .setNegativeButton("No", dialogClickListener).show();

                                    } else if ("Share".equals(options[which])) {
                                        Intent i = new Intent();
                                        i.setAction(Intent.ACTION_SEND);
                                        i.putExtra(Intent.EXTRA_TEXT, chat.getMessage());
                                        i.setType("video/*");
                                        mContext.startActivity(i);
                                    }
                                }
                            });

                            builder.show();
                        }

                        return false;
                    }
                });

            } else if (chat.getType().equals("location")) {
                Date date = new Date(chat.getTime());
                SimpleDateFormat dateFormat = new SimpleDateFormat(" hh:mm a  dd/MM/yyyy");
                String tim = dateFormat.format(date).toUpperCase();

                viewHolder.voicecrd.setVisibility(View.GONE);
                viewHolder.message.setVisibility(View.GONE);
                viewHolder.image.setVisibility(View.GONE);
                viewHolder.morder_CardView.setVisibility(View.GONE);
                viewHolder.videoCard.setVisibility(View.GONE);
                String[] getvv = chat.getMessage().split(",");
                String lat = getvv[0];
                String lang = getvv[1];


                String url = "https://maps.googleapis.com/maps/api/staticmap?center=" + lat + "," + lang + "&zoom=15&size=640x450&maptype=roadmap&markers=color:red%7Clabel:Property%7C" + lat + "," + lang + "" +
                        "&key=AIzaSyAAdMS03mAk6qDSf4HUmZmcjvSkiSN7jIU";
                Glide.with(mContext).asBitmap().load(url).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(viewHolder.mMap_icon);
                viewHolder.txt_time2.setText(tim);

                viewHolder.Map_link_card.setVisibility(View.VISIBLE);

                if (chat.isSeen()) {
                    viewHolder.txt_seen2.setImageResource(R.drawable.ic_done_all_black_24dp);
                } else {
                    viewHolder.txt_seen2.setImageResource(R.drawable.ic_done_black_24dp);
                }

                String[] location = chat.getMessage().split(",");
                String locat1 = GetExactLocation(Double.parseDouble(location[0]), Double.parseDouble(location[1]));
                String uriBegin = "geo:" + Double.parseDouble(location[0]) + "," + Double.parseDouble(location[1]);
                String query = Double.parseDouble(location[0]) + "," + Double.parseDouble(location[1]) + "(" + locat1 + ")";
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                final Uri uri = Uri.parse(uriString);

                viewHolder.Map_link_card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_VIEW, uri);
                        i.setPackage("com.google.android.apps.maps");
                        mContext.startActivity(i);
                    }
                });

            } else if (chat.getType().equals("Order")) {
                Date date = new Date(chat.getTime());
                SimpleDateFormat dateFormat = new SimpleDateFormat(" hh:mm a  dd/MM/yyyy");
                String tim = dateFormat.format(date).toUpperCase();
                viewHolder.voicecrd.setVisibility(View.GONE);
                viewHolder.message.setVisibility(View.GONE);
                viewHolder.image.setVisibility(View.GONE);
                viewHolder.videoCard.setVisibility(View.GONE);
                viewHolder.Map_link_card.setVisibility(View.GONE);
                viewHolder.morder_CardView.setVisibility(View.VISIBLE);
                viewHolder.mtxt_address.setText(chat.getAddress());
                viewHolder.mtxt_total_Price.setText(chat.getTotalPrice());
                viewHolder.mtxt_total_product.setText("" + chat.getTotalProduct());

                viewHolder.morder_time.setText(tim);

                if (chat.isSeen()) {
                    viewHolder.morder_seen.setImageResource(R.drawable.ic_done_all_black_24dp);
                } else {
                    viewHolder.morder_seen.setImageResource(R.drawable.ic_done_black_24dp);
                }

                viewHolder.mbtn_order_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ProgressDialog progressDialog = new ProgressDialog(mContext);
                        progressDialog.setMessage("Please wait...");
                        progressDialog.setCancelable(false);
                        View mView = LayoutInflater.from(mContext).inflate(R.layout.orderdetail_sheet, null);
                        Toolbar mappBar = mView.findViewById(R.id.appBar);
                        mappBar.setTitle("Order Detail");
                        final RecyclerView recyclerView = mView.findViewById(R.id.oorderdetail_gd);
                        TextView textView = mView.findViewById(R.id.totalitemprice);
                        TextView txt_addresss = mView.findViewById(R.id.txt_addresss);
                        TextView txt_totalproductss = mView.findViewById(R.id.txt_totalproductss);
                        TextView txt_total_qtys = mView.findViewById(R.id.txt_total_qtys);
                        textView.setText("" + chat.getTotalPrice());
                        txt_addresss.setText("" + chat.getAddress());
                        txt_totalproductss.setText("" + chat.getTotalProduct());
                        txt_total_qtys.setText("" + chat.getOrderID());
                        FloatingActionButton mfabClose = mView.findViewById(R.id.fabClose);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        final List<CatLvlItemList> mCatLvlItemList = new ArrayList<>();

                        String url = "https://chhatt.com/Cornstr/grocery/api/get/stores/orders?str_id=" + chat.getStoreID() + "&ord_id=" + chat.getOrderID();
                        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
                        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try {

                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject storeObject = response.getJSONObject(i);

                                        String pname = storeObject.getString("sp_name");
                                        String actprice = storeObject.getString("act_prc");
                                        String proimage = storeObject.getString("sp_image");
                                        String pqty = storeObject.getString("ord_qty");


                                        mCatLvlItemList.add(new CatLvlItemList(pname, actprice, pqty, proimage, chat.getStoreID()));

                                    }
                                    OrderAdapter orderAdapter = new OrderAdapter(mCatLvlItemList, mContext, true, false);
                                    recyclerView.setAdapter(orderAdapter);
                                    notifyDataSetChanged();
                                    progressDialog.cancel();

                                } catch (JSONException e) {
                                    orderdetailSheetDialog.cancel();
                                    progressDialog.cancel();
                                    Toast.makeText(mContext, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        orderdetailSheetDialog.cancel();
                                        progressDialog.cancel();
                                        Toast.makeText(mContext, "" + error.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                }
                        );
                        requestQueue.add(jsonArrayRequest);


                        try {
                            orderdetailSheetDialog = new BottomSheetDialog(mView.getContext());
                            orderdetailSheetDialog.setContentView(mView);
                            orderdetailSheetDialog.show();
                            setupFullHeight(orderdetailSheetDialog);
                            progressDialog.show();
                        } catch (Exception e) {

                        }


                        mfabClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                orderdetailSheetDialog.cancel();
                            }
                        });


                    }

                });

                final HashMap<String, Object> cancelMap = new HashMap<>();
                cancelMap.put("message", "This Order is Cancelled.");
                cancelMap.put("type", "text");
                final ProgressDialog progressDialog;
                progressDialog = new ProgressDialog(mContext);
                progressDialog.setMessage("Wait");
                progressDialog.setCancelable(false);

                viewHolder.mbtn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //progressDialog.show();
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        FirebaseDatabase.getInstance().getReference().child("Chats").child(FirebaseAuth.getInstance().getUid() + "6xK1UwZ2fNT6xYVVcLahuXObXll1").child(chat.getChatid()).updateChildren(cancelMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    FirebaseDatabase.getInstance().getReference().child("OrderDetail").child(chat.getOrderID()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                progressDialog.dismiss();
                                                            } else {
                                                                progressDialog.dismiss();
                                                                Toast.makeText(mContext, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(mContext, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //Toast.makeText(mContext, "No", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setMessage("Do You Want To Cancel This Order?").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();
                    }
                });

            }

        }


    }

    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        int pxWidth = displayMetrics.widthPixels;
        float dpWidth = pxWidth / displayMetrics.density;
        int pxHeight = displayMetrics.heightPixels;
        float dpHeight = pxHeight / displayMetrics.density;
        return pxHeight;
    }

    private void deletemessageforme(String chatid) {
        FirebaseDatabase.getInstance().getReference("Chats").child(getchatroot).child(chatid).child("deleted").setValue(FirebaseAuth.getInstance().getUid());
    }

    private void deletemessage(String chatid) {
        HashMap hashMap = new HashMap();
        hashMap.put("message", "\uD835\uDE1B\uD835\uDE29\uD835\uDE2A\uD835\uDE34 \uD835\uDE2E\uD835\uDE26\uD835\uDE34\uD835\uDE34\uD835\uDE22\uD835\uDE28\uD835\uDE26 \uD835\uDE38\uD835\uDE22\uD835\uDE34 \uD835\uDE25\uD835\uDE26\uD835\uDE2D\uD835\uDE26\uD835\uDE35\uD835\uDE26\uD835\uDE25");
        hashMap.put("type", "text");
        FirebaseDatabase.getInstance().getReference("Chats").child(getchatroot).child(chatid).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    Toast toast = Toast.makeText(mContext, "Deleted", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
    }

    private void stopplaying() {
        if (myplayer != null) {
            myplayer.stop();
            myplayer.reset();
            myplayer = null;

        }
    }

    private String calculateDuration(int duration) {
        String finalDuration = "";
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration);
        if (minutes == 0) {
            finalDuration = "0:" + seconds;
        } else {
            if (seconds >= 60) {
                long sec = seconds - (minutes * 60);
                finalDuration = minutes + ":" + sec;
            }
        }
        return finalDuration;
    }

//    Runnable run = new Runnable() {
//        @Override
//        public void run() {
//            // Updateing SeekBar every 100 miliseconds
//            Handler seekHandler = new Handler();
//            mSeekBar.setProgress(mMediaPlayer.getCurrentPosition());
//            seekHandler.postDelayed(run, 100);
//            //For Showing time of audio(inside runnable)
//            int miliSeconds = mMediaPlayer.getCurrentPosition();
//            if (miliSeconds != 0) {
//                //if audio is playing, showing current time;
//                long minutes = TimeUnit.MILLISECONDS.toMinutes(miliSeconds);
//                long seconds = TimeUnit.MILLISECONDS.toSeconds(miliSeconds);
//                if (minutes == 0) {
//                    mTvAudioLength.setText("0:" + seconds);
//                } else {
//                    if (seconds >= 60) {
//                        long sec = seconds - (minutes * 60);
//                        mTvAudioLength.setText(minutes + ":" + sec);
//                    }
//                }
//            } else {
//                //Displaying total time if audio not playing
//                int totalTime = mMediaPlayer.getDuration();
//                long minutes = TimeUnit.MILLISECONDS.toMinutes(totalTime);
//                long seconds = TimeUnit.MILLISECONDS.toSeconds(totalTime);
//                if (minutes == 0) {
//                    mTvAudioLength.setText("0:" + seconds);
//                } else {
//                    if (seconds >= 60) {
//                        long sec = seconds - (minutes * 60);
//                        mTvAudioLength.setText(minutes + ":" + sec);
//                    }
//                }
//            }
//        }
//
//    };


    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public String GetExactLocation(double lati, double longi) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
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
                Toast.makeText(mContext, "Error ", Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();

        }
        return strAdd;
    }

    @Override
    public Filter getFilter() {
        return MessageFilter;
    }

    private Filter MessageFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Chats> filterlist = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filterlist.addAll(mUserFull);
            } else {
                String filterpattern = constraint.toString().toLowerCase().trim();
                for (Chats item : mUserFull) {
                    if (item.getMessage().toLowerCase().contains(filterpattern)) {
                        filterlist.add(item);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterlist;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mChat.clear();
            mChat.addAll((List) filterResults.values);
            notifyDataSetChanged();

        }
    };


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //private static final Object URL =  ;
        public TextView show_message, propertypricetext, p_price, p_bed, p_bath, p_unit, p_for;
        public ImageView profile_image, propimage, txt_seen, txt_seen2, txt_seen1, mv_seen, mvo_seen, checking, mspro_seen;
        public TextView duration, mv_time, mvo_time;
        CardView propCardView;
        LinearLayout linearLayout;
        public ImageView show_Image, propImageView, morder_seen;
        private CardView videoCard;
        public ImageView show_video;
        public TextView txt_time, txt_time1, txt_time2, morder_time;
        public CardView message, propertyinterest;
        public CardView image, Map_link_card;
        public CardView voicecrd, morder_CardView;
        ImageButton btn_play;
        public ProgressBar mLoad, vidload;
        TextView tvAudioLength;
        SeekBar seekBar;

        public ImageView mMap_icon;
        private Button mbtn_cancel, mbtn_order_detail, mbtn_accept;
        private TextView mtxt_address, mtxt_total_product, mtxt_total_Price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mtxt_address = itemView.findViewById(R.id.txt_address);
            mtxt_total_product = itemView.findViewById(R.id.txt_total_product);
            mtxt_total_Price = itemView.findViewById(R.id.txt_total_Price);
            mbtn_cancel = itemView.findViewById(R.id.btn_cancel);
            mbtn_order_detail = itemView.findViewById(R.id.btn_orderdetail);
            mbtn_accept = itemView.findViewById(R.id.btn_accept);

            morder_time = itemView.findViewById(R.id.order_time);
            morder_seen = itemView.findViewById(R.id.order_seen);
            morder_CardView = itemView.findViewById(R.id.order_CardView);
            checking = itemView.findViewById(R.id.checking);

            p_price = itemView.findViewById(R.id.pro_price);
            p_bed = itemView.findViewById(R.id.pro_bedqt);
            p_bath = itemView.findViewById(R.id.pro_bathqt);

            p_unit = itemView.findViewById(R.id.pro_unitqty);
            p_for = itemView.findViewById(R.id.pro_for);
            propertypricetext = itemView.findViewById(R.id.propertypricetext);
            propimage = itemView.findViewById(R.id.propimage);
            propCardView = itemView.findViewById(R.id.property_card);
            propImageView = itemView.findViewById(R.id.prop_image);
            propertyinterest = itemView.findViewById(R.id.propertyinterest);
            linearLayout = itemView.findViewById(R.id.linear1);
            show_message = (TextView) itemView.findViewById(R.id.show_message);
            txt_seen = itemView.findViewById(R.id.txt_seen);
            txt_seen1 = itemView.findViewById(R.id.txt_seen1);
            profile_image = (ImageView) itemView.findViewById(R.id.profile_image);
            duration = (TextView) itemView.findViewById(R.id.duration);
            show_Image = (ImageView) itemView.findViewById(R.id.show_Image);
            show_video = (ImageView) itemView.findViewById(R.id.show_video);
            videoCard = itemView.findViewById(R.id.v_cardView);
            txt_time = (TextView) itemView.findViewById(R.id.text_time);
            message = (CardView) itemView.findViewById(R.id.lyt_thread);
            image = (CardView) itemView.findViewById(R.id.lyt_thread1);
            Map_link_card = itemView.findViewById(R.id.Map_link_card);
            txt_time1 = (TextView) itemView.findViewById(R.id.text_time1);
            txt_time2 = itemView.findViewById(R.id.text_time2);
            btn_play = itemView.findViewById(R.id.play);
            //btn_pause = itemView.findViewById(R.id.pause);
            tvAudioLength = (TextView) itemView.findViewById(R.id.duration);
            seekBar = (SeekBar) itemView.findViewById(R.id.seekBar);
            voicecrd = (CardView) itemView.findViewById(R.id.voic_card);
            txt_seen2 = itemView.findViewById(R.id.txt_seen2);
            mMap_icon = itemView.findViewById(R.id.Map_icon);
            mv_seen = itemView.findViewById(R.id.v_seen);
            mv_time = itemView.findViewById(R.id.v_time);
            mvo_seen = itemView.findViewById(R.id.vo_seen);
            mvo_time = itemView.findViewById(R.id.vo_time);
            mLoad = itemView.findViewById(R.id.vLoad);
            vidload = itemView.findViewById(R.id.vidLoad);
        }

        @Override
        public void onClick(View v) {

        }
    }


    @Override
    public int getItemViewType(int position) {
        try {
            if (mChat.get(position).getSender().equals(FirebaseAuth.getInstance().getUid())) {
                return MSG_TYPE_RIGHT;
            } else {
                return MSG_TYPE_LEFT;
            }
        } catch (Exception e) {
            return MSG_TYPE_RIGHT;
        }

    }

}