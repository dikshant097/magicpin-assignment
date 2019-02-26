package com.magicpin.dikshant.manocha.assignment.adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.magicpin.dikshant.manocha.assignment.R;
import com.magicpin.dikshant.manocha.assignment.beans.VideoBean;

import java.util.List;


public class CustomRecyclerviewAdapter extends RecyclerView.Adapter<CustomRecyclerviewAdapter.MyViewHolder> {

    private List<VideoBean> videos;
    private Context context;
    public CustomRecyclerviewAdapter(List<VideoBean> videos,Context context) {
        this.videos = videos;
        this.context=context;
    }


    @NonNull
    @Override
    public CustomRecyclerviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.feed_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomRecyclerviewAdapter.MyViewHolder myViewHolder, int i) {
            myViewHolder.video_desc.setText(VideoBean.getDescription());
            myViewHolder.video_num.setText("Video Number:" + String.valueOf(videos.get(i).getSerial()));
            myViewHolder.parent.setTag(myViewHolder);
            myViewHolder.sample.setImageResource(R.drawable.magicpin);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView video_num,video_desc;
        public ProgressBar progressBar;
        public FrameLayout video_layout;
        public ImageView sample;
        public View parent;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parent=itemView;
            video_desc=itemView.findViewById(R.id.video_desc);
            video_num=itemView.findViewById(R.id.video_num);
            progressBar=itemView.findViewById(R.id.progressBar);
            video_layout=itemView.findViewById(R.id.video_layout);


            sample=itemView.findViewById(R.id.sample);
        }
    }
}
