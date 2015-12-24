package edu.rit.gis.vurawiga.tip_of_day;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import edu.rit.gis.vurawiga.R;

/**
 * Created by siddeshpillai on 10/25/15.
 */
public class VideoTutorialsActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    public static final String API_KEY = "AIzaSyDQiMzy7RmoaNutLDRusQSn5BYDH0_3ldE";

    //http://youtu.be/<VIDEO_ID>
    public static String VIDEO_ID = "dKLftgvYsVU";
    public static final String VIDEO_ID_1 = "Ko1zgvw0wL4";
    public static final String VIDEO_ID_2 = "pnnYXqHWYoU";
    public static final String VIDEO_ID_3 = "5-ctkmp9LfA";
    public static final String VIDEO_ID_4 = "1FMf3Hnh5xQ";
    public static final String VIDEO_ID_5 = "liXgfl53VV0";
    public static final String VIDEO_ID_6 = "NXnx-8gca1o";
    public static final String VIDEO_ID_7 = "zhSyNkzQPfs";
    public static final String VIDEO_ID_8 = "2FGQdcbZMWc";
    public static final String VIDEO_ID_9 = "2tOWIPySYWc";
    public static final String VIDEO_ID_10 = "xjg71DCW7Is";
    public static final String VIDEO_ID_11 = "u4ArefVIIvM";

    CardView v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11;
    private YouTubePlayer youTubePlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** attaching layout xml **/
        setContentView(R.layout.video_tutorials);

        /** Initializing YouTube player view **/
        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player);
        youTubePlayerView.initialize(API_KEY, this);

        v1 = (CardView) findViewById(R.id.video1);
        v2 = (CardView) findViewById(R.id.video2);
        v3 = (CardView) findViewById(R.id.video3);
        v4 = (CardView) findViewById(R.id.video4);
        v5 = (CardView) findViewById(R.id.video5);
        v6 = (CardView) findViewById(R.id.video6);
        v7 = (CardView) findViewById(R.id.video7);
        v8 = (CardView) findViewById(R.id.video8);
        v9 = (CardView) findViewById(R.id.video9);
        v10 = (CardView) findViewById(R.id.video10);
        v11 = (CardView) findViewById(R.id.video11);

        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VIDEO_ID = VIDEO_ID_1;
                youTubePlayer.loadVideo(VIDEO_ID);
            }
        });

        v2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VIDEO_ID = VIDEO_ID_2;
                youTubePlayer.loadVideo(VIDEO_ID);
            }
        });

        v3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VIDEO_ID = VIDEO_ID_3;
                youTubePlayer.loadVideo(VIDEO_ID);
            }
        });

        v4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VIDEO_ID = VIDEO_ID_4;
                youTubePlayer.loadVideo(VIDEO_ID);
            }
        });

        v5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VIDEO_ID = VIDEO_ID_5;
                youTubePlayer.loadVideo(VIDEO_ID);
            }
        });

        v6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VIDEO_ID = VIDEO_ID_6;
                youTubePlayer.loadVideo(VIDEO_ID);
            }
        });

        v7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VIDEO_ID = VIDEO_ID_7;
                youTubePlayer.loadVideo(VIDEO_ID);
            }
        });

        v8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VIDEO_ID = VIDEO_ID_8;
                youTubePlayer.loadVideo(VIDEO_ID);
            }
        });

        v9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VIDEO_ID = VIDEO_ID_9;
                youTubePlayer.loadVideo(VIDEO_ID);
            }
        });

        v10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VIDEO_ID = VIDEO_ID_10;
                youTubePlayer.loadVideo(VIDEO_ID);
            }
        });

        v11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VIDEO_ID = VIDEO_ID_11;
                youTubePlayer.loadVideo(VIDEO_ID);
            }
        });


    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayers, boolean wasRestored) {

        youTubePlayer = youTubePlayers;
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        youTubePlayer.setPlaybackEventListener(playbackEventListener);

        /** Start buffering **/
        if (!wasRestored) {
            youTubePlayer.cueVideo(VIDEO_ID);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(this, "Failured to Initialize!", Toast.LENGTH_LONG).show();
    }

    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {

        @Override
        public void onBuffering(boolean arg0) {
        }

        @Override
        public void onPaused() {
        }

        @Override
        public void onPlaying() {
        }

        @Override
        public void onSeekTo(int arg0) {
        }

        @Override
        public void onStopped() {
        }

    };

    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {

        @Override
        public void onAdStarted() {
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason arg0) {
        }

        @Override
        public void onLoaded(String arg0) {
        }

        @Override
        public void onLoading() {
        }

        @Override
        public void onVideoEnded() {
        }

        @Override
        public void onVideoStarted() {
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
