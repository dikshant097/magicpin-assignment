package com.magicpin.dikshant.manocha.assignment;

import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;

import com.magicpin.dikshant.manocha.assignment.adapter.CustomRecyclerviewAdapter;
import com.magicpin.dikshant.manocha.assignment.beans.VideoBean;
import com.magicpin.dikshant.manocha.assignment.customUI.CustomRecyclerView;
import com.magicpin.dikshant.manocha.assignment.utility.DividerItemDecoration;

import java.util.ArrayList;

import static android.widget.LinearLayout.VERTICAL;


public class MainActivity extends AppCompatActivity {

    private CustomRecyclerView videoFeed;
    public static ArrayList<VideoBean> videos;
    private CustomRecyclerviewAdapter feedAdapter;
    public static Boolean firstTime=true;
    //setting videos array at class loading time
    static{
        videos=new ArrayList<>();
        VideoBean videoBean=new VideoBean(126544,"https://player.vimeo.com/external/286837767.m3u8?s=42570e8c4a91b98cdec7e7bfdf0eccf54e700b69");
        videos.add(videoBean);
        videoBean=new VideoBean(126545,"https://player.vimeo.com/external/286837810.m3u8?s=610b4fee49a71c2dbf22c01752372ff1c6459b9e");
        videos.add(videoBean);
        videoBean=new VideoBean(126546,"https://player.vimeo.com/external/286837723.m3u8?s=3df60d3c1c6c7a11df4047af99c5e05cc2e7ae96");
        videos.add(videoBean);
        videoBean=new VideoBean(126547,"https://player.vimeo.com/external/286837649.m3u8?s=9e486e9b932be72a8875afc6eaae21bab124a35a");
        videos.add(videoBean);
        videoBean=new VideoBean(126548,"https://player.vimeo.com/external/286837529.m3u8?s=20f83af6ea8fbfc8ce0c2001f32bf037f8b0f65f");
        videos.add(videoBean);
        videoBean=new VideoBean(126549,"https://player.vimeo.com/external/286837402.m3u8?s=7e01c398e2f01c29ecbd46e5e2dd53e0d6c1905d");
        videos.add(videoBean);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        videoFeed=findViewById(R.id.videoFeed);
        feedAdapter=new CustomRecyclerviewAdapter(videos,this);
        videoFeed.setLayoutManager(new LinearLayoutManager(this, VERTICAL, false));
        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.gap_layout);
        videoFeed.addItemDecoration(new DividerItemDecoration(dividerDrawable));
        videoFeed.setItemAnimator(new DefaultItemAnimator());
        videoFeed.setAdapter(feedAdapter);
        videoFeed.scrollToPosition(0);

        if (MainActivity.firstTime) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    videoFeed.playVideo();
                }
            });
            MainActivity.firstTime = false;
        }

    }

    @Override
    protected void onDestroy() {
        if(videoFeed!=null)
            videoFeed.onRelease();
        firstTime=true;
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        videoFeed.pausePlayer();
        super.onStop();
    }

    @Override
    protected void onRestart() {
        videoFeed.resumePlayer();
        super.onRestart();
    }
}
