package com.example.yivin.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.zip.Inflater;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class FlowViewActivity extends Activity {

    private WindowManager windowManager;

    private TextView tv;
    private float lastX, lastY, start_X, start_Y;
    WindowManager.LayoutParams params;
    private SurfaceView surfaceView, flowSV;
    private SurfaceHolder holder1, holder2;
//    private MediaPlayer mediaPlayer;
    View view;
    private String URLMP4 = "http://cdndms.ott4china.com/data/publish/2018/11/content/ODIwYzZkNjMtMjNmYy00NWIyLTliNTEtNmY2NWM5MWE2ZWRh.mp4";

    private String URLRTSP = "rtsp://123.147.113.182:1554/iptv/import/Tvod/iptv/001/040000010400000200000001/01000000004000000000000000000285.rsc/9366_Uni.sdp";

    private IjkMediaPlayer mediaPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_view);
        view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_flow, null);
        tv = findViewById(R.id.flow_tv);
        surfaceView = findViewById(R.id.surface_view_activity);

        createVideo();
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFlowView();
                finish();
            }
        });

    }

    private void createVideo() {
        try {
            mediaPlayer = new IjkMediaPlayer();
            mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "rtsp_transport", "tcp");
//            mediaPlayer = new MediaPlayer();
            AssetFileDescriptor afd = getAssets().openFd("test.mp4");
//            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                        mediaPlayer.setDataSource(URLRTSP);
            holder1 = surfaceView.getHolder();
            holder1.addCallback(new MyHolder(1));
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(IMediaPlayer mp) {
                    mediaPlayer.start();
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                createFlowView();
                finish();
                return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void createFlowView() {
        windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= 26) {//8.0新特性
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
//设置效果为背景透明.
        params.format = PixelFormat.RGBA_8888;
        //设置flags.不可聚焦及不可使用按钮对悬浮窗进行操控.
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //设置窗口坐标参考系
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.width = 200;
        params.height = 200;
//        params.x = 20;
//        params.y = 100;

        flowSV = view.findViewById(R.id.flow_sv);
        holder2 = flowSV.getHolder();
        holder2.addCallback(new MyHolder(2));
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                Log.d("yivinnn", action + "  ");
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = event.getRawX();
                        lastY = event.getRawY();

                        start_X = event.getRawX();
                        start_Y = event.getRawY();

                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 记录移动后的位置
                        float moveX = event.getRawX();
                        float moveY = event.getRawY();
                        // 获取当前窗口的布局属性, 添加偏移量, 并更新界面, 实现移动
                        params.x += (int) (moveX - lastX);
                        params.y += (int) (moveY - lastY);
                        Log.d("yivinnn", params.x + "  ===   " + params.y);
                        windowManager.updateViewLayout(view, params);

                        lastX = moveX;
                        lastY = moveY;

                        break;
                    case MotionEvent.ACTION_UP:
//                        float moveX = event.getRawX();
//                        float moveY = event.getRawY();
                        break;
                }
                return false;
            }
        });
        windowManager.addView(view, params);

    }

    class MyHolder implements SurfaceHolder.Callback {

        private int state;

        public MyHolder(int mState) {
            state = mState;
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mediaPlayer.setDisplay(holder);
            if (state == 1) {
                Log.d("yivinsv", "surfaceCreated:" + state);
//                mediaPlayer.setDisplay(holder1);
            } else if (state == 2) {
                Log.d("yivinsv", "surfaceCreated:" + state);
//                mediaPlayer.setDisplay(holder2);
            }
//            try {
//                mediaPlayer.prepareAsync();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }
}
