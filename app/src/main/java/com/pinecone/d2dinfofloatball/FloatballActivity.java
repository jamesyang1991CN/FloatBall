package com.pinecone.d2dinfofloatball;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

public class FloatballActivity extends Activity {



    private Button mBtnStart;
    private Button mBtnQuit;
    private RemoteViews mRemoteViews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 1);
                Toast.makeText(this, "请先允许FloatBall出现在顶部", Toast.LENGTH_SHORT).show();
            }
        }
        mRemoteViews = new RemoteViews(getPackageName(),R.layout.notification_d2d_float_ball);



        startNotification();
    }

    private void startNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        int id = R.string.app_name;
        Intent d2dInfoIntent = new Intent(this, FloatballActivity.class);
        d2dInfoIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent d2dInfoPendingIntent = PendingIntent.getActivity(this, 0, d2dInfoIntent, 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setContent(mRemoteViews)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSmallIcon(R.drawable.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(d2dInfoPendingIntent)
                .setOngoing(true);
        notificationManager.notify(id, mBuilder.build());

    }

    private void initView() {
        mBtnStart = (Button) findViewById(R.id.btn_start);
        mBtnQuit = (Button) findViewById(R.id.btn_quit);
        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                checkAccessibility();
                Intent intent = new Intent(FloatballActivity.this, D2DInfoFBService.class);
                Bundle data = new Bundle();
                data.putInt("type", D2DInfoFBService.TYPE_ADD);
                intent.putExtras(data);
                startService(intent);
            }
        });
        mBtnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FloatballActivity.this, D2DInfoFBService.class);
                Bundle data = new Bundle();
                data.putInt("type", D2DInfoFBService.TYPE_DEL);
                intent.putExtras(data);
                startService(intent);
            }
        });
    }

    private void checkAccessibility() {
        // 判断辅助功能是否开启
        if (!isAccessibilitySettingsOn(this)) {
            // 引导至辅助功能设置页面
            startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            Toast.makeText(this, "请先开启FloatBall辅助功能", Toast.LENGTH_SHORT).show();
        }
    }


    public static boolean isAccessibilitySettingsOn(Context context) {
        int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        if (accessibilityEnabled == 1) {
            String services = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (services != null) {
                return services.toLowerCase().contains(context.getPackageName().toLowerCase());
            }
        }

        return false;
    }

}
