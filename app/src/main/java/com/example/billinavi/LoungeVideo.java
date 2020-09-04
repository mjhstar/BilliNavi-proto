package com.example.billinavi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.media.MediaBrowserServiceCompat;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.List;

public class LoungeVideo extends AppCompatActivity {
    private TextView title, writer, day, time, context;
    private String image, video;
    private Toolbar toolbar;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lounge_video);
        {
            title = findViewById(R.id.video_title);
            writer = findViewById(R.id.video_writer);
            context = findViewById(R.id.video_context);
            day = findViewById(R.id.video_day);
            time = findViewById(R.id.video_time);
            videoView = findViewById(R.id.video_view);

            Intent intent = getIntent();
            title.setText(intent.getExtras().getString("title"));
            writer.setText(intent.getExtras().getString("writer"));
            context.setText(intent.getExtras().getString("context"));
            day.setText(intent.getExtras().getString("day"));
            time.setText(intent.getExtras().getString("time"));
            image = intent.getExtras().getString("image");
            video = intent.getExtras().getString("video");
        }
        setToolbar();
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        // Set video link (mp4 format )
        Uri videoU = Uri.parse(video);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(videoU);
        videoView.requestFocus();
        videoView.start();


        /*MediaController controller = new MediaController(LoungeVideo.this);
        videoView.setMediaController(controller);

        videoView.requestFocus();

        videoView.setVideoURI(Uri.parse(video));

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                Toast.makeText(getApplicationContext(), "동영상이 로드 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Toast.makeText(getApplicationContext(),"동영상 재생이 완료되었습니다.",Toast.LENGTH_SHORT).show();
            }
        });*/

    }

    public void setToolbar() {
        //툴바 설정하기
        toolbar = findViewById(R.id.toolbar_sub);
        TextView toolTitle = findViewById(R.id.toolbar_title);//
        toolTitle.setText(title.getText());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    class MediaService extends MediaBrowserServiceCompat{
        private MediaSessionCompat mMediaSession;

        @Nullable
        @Override
        public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
            return null;
        }

        @Override
        public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {

        }
    }
}
