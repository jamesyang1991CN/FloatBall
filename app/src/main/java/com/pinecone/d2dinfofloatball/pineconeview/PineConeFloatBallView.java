package com.pinecone.d2dinfofloatball.pineconeview;

import android.content.Context;


import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.pinecone.d2dinfofloatball.FloatBallManager;
import com.pinecone.d2dinfofloatball.R;



public class PineConeFloatBallView extends LinearLayout {

    private static final String TAG = "PineConeFloatBallView";
    private FloatBallManager mFBManager = null;

    private View mFB_button = null;
    private ImageView  mFB_ImgBall = null;
    private ImageView  mFB_ImgBigBall = null;
    private TextView mFB_Switch = null;
    private ImageView mFB_hide_icon = null;

    private int mIsShowD2DInFo = 0;

    private int mDownX;
    private int mDownY;

    private int mLastX;
    private int mLastY;

    private String mSwitch_ON,mSwitch_OFF = null;
    private View mfb_layout = null;

    private Handler mFB_handler = null;

    private final static int TIMER_OVER = 1;
    private static Boolean ALIGN_LEFT = false;


    public PineConeFloatBallView(Context context) {
        this(context, null);
    }

    public PineConeFloatBallView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PineConeFloatBallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTimer();
        initView(context, attrs);

    }



    private void initTimer() {

        mFB_handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case TIMER_OVER:
                        mFB_button.setVisibility(View.GONE);
                        mFB_hide_icon.setVisibility(View.VISIBLE);
                        int mFB_hide_width = mFB_hide_icon.getWidth();
                        Log.d(TAG,"mFB_hide_icon W = "+mFB_hide_width +" ALIGN_LEFT = "+ALIGN_LEFT);
                        if (ALIGN_LEFT){
                            mFBManager.updateBallView(0,0);
                        }else{
                            mFBManager.updateBallView(mFB_button.getWidth()-10,0);

                        }
                        break;
                    default:break;

                }
            }
        };

    }



    private void initView(Context context, AttributeSet attrs) {
        mFBManager = FloatBallManager.getInstance(context);
        mfb_layout = inflate(getContext(), R.layout.float_ball_layout, this);
        mFB_button = findViewById(R.id.fl_button);
        mFB_ImgBall = (ImageView) findViewById(R.id.img_ball);
        mFB_ImgBigBall = (ImageView) findViewById(R.id.img_big_ball);
        mFB_Switch = (TextView) findViewById(R.id.tv_switch);
        mFB_hide_icon = (ImageView)findViewById(R.id.iv_hide_icon);
        mSwitch_ON = getResources().getString(R.string.switch_on);
        mSwitch_OFF = getResources().getString(R.string.switch_off);

        mFB_hide_icon.setVisibility(View.GONE);

        mFB_button.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, final MotionEvent event) {
                if (mFB_handler.hasMessages(TIMER_OVER)){
                    mFB_handler.removeMessages(TIMER_OVER);
                }
                int x = (int) event.getRawX();
                int y = (int) event.getRawY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mFB_ImgBigBall.setVisibility(View.VISIBLE);
                        mFB_ImgBall.setVisibility(View.INVISIBLE);
                        Log.d(TAG,"float button ACTION_DOWN X = "+event.getRawX()+" Y = "+event.getRawY());

                        touchDown(x,y);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        touchMove(x,y);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG,"float button ACTION_UP X = "+event.getRawX()+" Y = "+event.getRawY());
                        touchUp(x,y);
                        break;
                }
                return true;
            }
        });


        startAttachBorder(ALIGN_LEFT);

    }



    private void touchDown(int x, int y) {
        mDownX = x;
        mDownY = y;
        mLastX = mDownX;
        mLastY = mDownY;
    }

    private void touchMove(int x, int y) {
        int eachDeltaX = x - mLastX;
        int eachDeltaY = y - mLastY;
        mLastX = x;
        mLastY = y;
        mFBManager.updateBallView(eachDeltaX, eachDeltaY);

    }

    private void touchUp(int x,int y) {
        if (Math.abs(x-mDownX)<this.getWidth()/10 && Math.abs(y-mDownY)<this.getHeight()/10){
            handleClickEvent();
        }
        mFB_ImgBigBall.setVisibility(View.INVISIBLE);
        mFB_ImgBall.setVisibility(View.VISIBLE);
        ALIGN_LEFT = mFBManager.foldFloatballonLeft();
        startAttachBorder(ALIGN_LEFT);
    }

    private void startAttachBorder(final boolean left) {
        if (mSwitch_OFF.equals(mFB_Switch.getText())){
            mFB_hide_icon.setImageResource(R.drawable.pinecone_hide_icon);
            AnimationDrawable animationDrawable = (AnimationDrawable) mFB_hide_icon.getDrawable();
            animationDrawable.start();

            Message msg = new Message();
            msg.what = TIMER_OVER;
            mFB_handler.sendMessageDelayed(msg,5000);



        }
    }


    private void handleClickEvent() {
        Log.d(TAG,"mIsShowD2DInFo = "+mIsShowD2DInFo);
        if (0 == mIsShowD2DInFo){//D2D info is not showed ,so need show
            mFB_Switch.setText(mSwitch_ON);
            mFBManager.showD2DInFo();
        }else{//D2D info is showed ,so need close
            mFB_Switch.setText(mSwitch_OFF);
            mFBManager.hideD2DInFo();
        }
        mIsShowD2DInFo = ~mIsShowD2DInFo;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getRawX();
        float y = event.getRawY();
        if (mFB_hide_icon.getVisibility()== View.VISIBLE){
            Log.d(TAG,"onTouchEvent x = "+x +" mFB_hide_icon x = "+mFB_hide_icon.getX());

            mFB_button.setVisibility(View.VISIBLE);
            mFB_hide_icon.setVisibility(View.GONE);
            startAttachBorder(ALIGN_LEFT);
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mFB_handler.hasMessages(TIMER_OVER)){
            mFB_handler.removeMessages(TIMER_OVER);
        }

    }
}
