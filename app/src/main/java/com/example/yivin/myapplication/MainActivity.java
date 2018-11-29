package com.example.yivin.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.videolan.libvlc.media.VideoView;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private SparseArray<String> array;
    public static Context context;
    private VideoView videoView;
    private String URLMP4 = "http://cdndms.ott4china.com/data/publish/2018/11/content/ODIwYzZkNjMtMjNmYy00NWIyLTliNTEtNmY2NWM5MWE2ZWRh.mp4";

    private String URLRTSP = "rtsp://123.147.113.182:1554/iptv/import/Tvod/iptv/001/040000010400000200000001/01000000004000000000000000000285.rsc/9366_Uni.sdp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.tv);
        textView.measure(0,0);
        Log.d("yivin123","getMeasuredWidth:"+textView.getMeasuredWidth()+"  ,getMeasuredHeight:"+textView.getMeasuredHeight());
        textView.post(new Runnable() {
            @Override
            public void run() {
                int width = textView.getWidth();
                int heigth = textView.getHeight();
                Log.d("yivin123","width:"+width+"  ,heigth:"+heigth);
//                Toast.makeText(MainActivity.this,"width:"+width+"  ,heigth:"+heigth,Toast.LENGTH_LONG).show();
                textView.setText("点击跳转下个界面");
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context = MainActivity.this;
                startActivity(new Intent(MainActivity.this,FlowTextureActivity.class));
            }
        });

        videoView  = findViewById(R.id.vlcview);
//        videoView.setVideoURI(Uri.parse(URLMP4));
//        videoView.start();

    }
}
