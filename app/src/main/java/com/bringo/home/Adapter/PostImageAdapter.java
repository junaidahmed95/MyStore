package com.bringo.home.Adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bringo.home.R;

import java.util.List;

import static com.bringo.home.MessagingActivity.mimagesList;
import static com.bringo.home.MuImagesActivity.mSendTotatl;
import static com.bringo.home.MuImagesActivity.myviewPager;

public class PostImageAdapter extends RecyclerView.Adapter<PostImageAdapter.ImageViewHolder> {
    private List<Uri> mArrayUri;
    Context mContext;

    public PostImageAdapter(Context context, List<Uri> mArrayUri) {
        mContext = context;
        this.mArrayUri = mArrayUri;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.postimage_item, viewGroup, false);
        return new PostImageAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder imageViewHolder, final int i) {
        if (mArrayUri.get(i) != null) {
            imageViewHolder.mVideoLayout.setVisibility(View.GONE);

            Glide.with(mContext).asBitmap().load(mArrayUri.get(i)).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(imageViewHolder.pic);
            imageViewHolder.pic.setVisibility(View.VISIBLE);
        }
        imageViewHolder.cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mArrayUri.size() == 1) {
                    mimagesList.clear();
                    ((Activity) mContext).finish();
                } else {
                    mArrayUri.remove(i);
                    notifyItemRemoved(i);
                    notifyItemRangeChanged(i, mArrayUri.size());
                    mSendTotatl.setText("" + mArrayUri.size());
                    multiviewpageradapter viewpagerAdapter = new multiviewpageradapter(mArrayUri);
                    myviewPager.setAdapter(viewpagerAdapter);
                }
            }
        });

        imageViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myviewPager.setCurrentItem(i);
            }
        });

    }

    public void setPostImages(List<Uri> productImages) {
        mArrayUri = productImages;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mArrayUri.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView pic;
        RelativeLayout cross;
        ImageView mImageVideo;
        RelativeLayout mVideoLayout;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.post_image);
            mImageVideo = itemView.findViewById(R.id.post_video);
            cross = itemView.findViewById(R.id.cancel_btn);
            mVideoLayout = itemView.findViewById(R.id.video_layout);
        }
    }

}
