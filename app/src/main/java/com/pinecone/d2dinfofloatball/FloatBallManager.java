package com.pinecone.d2dinfofloatball;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.pinecone.d2dinfofloatball.pineconeview.PineConeFloatBallView;


public class FloatBallManager {
    private static final String TAG = "FloatBallManager";
    private PineConeFloatBallView mFloatBallView;

    private WindowManager mWindowManager;
    private LayoutParams mFBParams = null;
    private int mScreenWidth = 0;
    private int mScreenHeight = 0;
    private static Context  mCtx = null;
    private View mFBWindow = null;
    private LayoutParams mD2dInfoLayoutParams = null;
    private View mD2dInfoLayout = null;
    private volatile static FloatBallManager instance = null;



    private TextView mD2dULFreqHop = null;
    private TextView mD2dDLFreqHop = null;
    private TextView mD2dRadioPower = null;
    private TextView mD2dControllerSignalStrength = null;
    private TextView mD2dPlaneSignal = null;
    private TextView mD2dPlaneSS = null;
    private TextView mD2dPlaneQoS = null;
    private TextView mD2dDLFreq = null;
    private TextView mD2dIfaceDev = null;
    private TextView mD2dULBW = null;
    private TextView mD2dDLBW = null;

    private FloatBallManager(Context ctx){
        mCtx = ctx;
        mWindowManager = (WindowManager) mCtx.getSystemService(Context.WINDOW_SERVICE);

    }

    public static  FloatBallManager getInstance(Context ctx){
        if(instance == null){
            synchronized(FloatBallManager.class){
                if(instance == null){
                    instance = new FloatBallManager(ctx);
                }
            }
        }
        return instance;
    }





    public void addBallView() {
        if (mFloatBallView == null) {
            DisplayMetrics dm = new DisplayMetrics();

            mWindowManager.getDefaultDisplay().getMetrics(dm);
            mScreenWidth = dm.widthPixels;
            mScreenHeight = dm.heightPixels;
            mFBWindow = LayoutInflater.from(mCtx).inflate(R.layout.float_ball, null);
            mFloatBallView = mFBWindow.findViewById(R.id.pinecone_float_ball);
            mFBParams = new LayoutParams();
            mFBParams.x = mScreenWidth-mFloatBallView.getWidth();
            mFBParams.y = mScreenHeight / 2;
            mFBParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            mFBParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            mFBParams.gravity = Gravity.LEFT | Gravity.TOP;
            mFBParams.type = LayoutParams.TYPE_SYSTEM_ALERT | LayoutParams.TYPE_SYSTEM_OVERLAY;
            mFBParams.format = PixelFormat.TRANSLUCENT;
            mFBParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
            mWindowManager.addView(mFBWindow, mFBParams);
            setUpD2dInfoView();
        }
    }


    public void removeBallView() {
        if (mFloatBallView != null) {
            mWindowManager.removeView(mFBWindow);
            mFloatBallView = null;
        }
    }


    public void updateBallView(int deltaX,int deltaY) {
        mFBParams.x += deltaX;
        mFBParams.y += deltaY;
        Log.d(TAG,"mFBParams.x = "+mFBParams.x+" deltaX = "+deltaX );
        if (mWindowManager != null) {
            mWindowManager.updateViewLayout(mFBWindow, mFBParams);
        }
    }


    private void setUpD2dInfoView() {

        mD2dInfoLayout = LayoutInflater.from(mCtx).inflate(R.layout.d2d_info_layout, null);
        mD2dInfoLayoutParams = new LayoutParams();
        mD2dInfoLayoutParams.width = mScreenWidth/2;
        mD2dInfoLayoutParams.height = mScreenHeight*9/10;
        mD2dInfoLayoutParams.gravity = Gravity.CENTER;
        mD2dInfoLayoutParams.type = LayoutParams.TYPE_PHONE;
        mD2dInfoLayoutParams.format = PixelFormat.TRANSLUCENT;
        mD2dInfoLayoutParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;

        intD2dInfoItem(mD2dInfoLayout);

    }


    private void intD2dInfoItem(View d2dInfoLayout) {

         mD2dULFreqHop = (TextView)d2dInfoLayout.findViewById(R.id.d2d_ul_freq_hop);
         mD2dDLFreqHop = (TextView)d2dInfoLayout.findViewById(R.id.d2d_dl_freq_hop);
         mD2dRadioPower = (TextView)d2dInfoLayout.findViewById(R.id.d2d_radio_power);
         mD2dControllerSignalStrength = (TextView)d2dInfoLayout.findViewById(R.id.d2d_controller_signal_strength);
         mD2dPlaneSignal = (TextView)d2dInfoLayout.findViewById(R.id.d2d_plane_signal);
         mD2dPlaneSS = (TextView)d2dInfoLayout.findViewById(R.id.d2d_plane_service_state);
         mD2dPlaneQoS = (TextView)d2dInfoLayout.findViewById(R.id.d2d_plane_qos);
         mD2dDLFreq = (TextView)d2dInfoLayout.findViewById(R.id.d2d_dl_frequency);
         mD2dIfaceDev = (TextView)d2dInfoLayout.findViewById(R.id.d2d_iface_dev);
         mD2dULBW = (TextView)d2dInfoLayout.findViewById(R.id.d2d_ul_bandwidth);
         mD2dDLBW = (TextView)d2dInfoLayout.findViewById(R.id.d2d_dl_bandwidth);

        updateD2DInFo();
    }


    public void showD2DInFo() {
        mWindowManager.addView(mD2dInfoLayout, mD2dInfoLayoutParams);


    }

    public void hideD2DInFo() {
        mWindowManager.removeView(mD2dInfoLayout);
    }

    private void updateD2DInFo() {

    }

    public boolean foldFloatballonLeft() {
        int fb_width = mFloatBallView.getWidth();
        int centerX = (mScreenWidth-fb_width)/2;
        int destX = (mFBParams.x<centerX)? 0:mScreenWidth-fb_width;
        mFBParams.x = destX;
        updateBallView(0,0);
        if (destX == 0){
            return true;
        }
        return false;
    }

}
