package com.pinecone.d2dinfofloatball;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.pinecone.d2dinfofloatball.pineconeview.PineConeFloatBallView;


public class D2DInfoFBService extends Service {

    private static final String TAG = "D2DInfoFBService";
    public static final int TYPE_ADD = 0;
    public static final int TYPE_DEL = 1;
    private FloatBallManager mFBManager;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"onCreate");
        mFBManager = FloatBallManager.getInstance(this);
    }



    private void showFloatBall(Intent intent) {
        Log.d(TAG,"showFloatBall");
        Bundle data = null;
        if (null != intent) {data = intent.getExtras();}

        if (data != null) {
            int type = data.getInt("type");
            if (type == TYPE_ADD) {
                mFBManager.addBallView();
            } else {
                mFBManager.removeBallView();
            }
        }else{
            mFBManager.addBallView();
        }
    }





    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand");

        showFloatBall(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");

    }

}


