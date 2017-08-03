package com.barrery.parkbuddy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Barrery on 2017/7/17.
 */

public class Receiver extends BroadcastReceiver {
    private final static String TAG = "VRMusicReceiver";
    private final static String ACTION_VR_INTENT = "com.vrmms.intent.MUSIC";
    private final static String OPERATE = "operate";
    private final static String OPEN = "app_open";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String operate = intent.getStringExtra(OPERATE);
        Log.i(TAG, "VR action = " + action);
        if (action.equals(ACTION_VR_INTENT)) {
            long timer = intent.getLongExtra("timer", System.currentTimeMillis());
            if (operate.equals(OPEN)) {
                LoadingActivity.responseVR(context, true, timer);
                Intent openActivity = new Intent(context, LoadingActivity.class);
                openActivity.putExtra("open", 1);
                openActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(openActivity);
                Log.i(TAG, "VR open Activity OK");
                return;
            } //else  VR其他功能的实现，如FM/AM切换，呼出指定频率等

        }
    }
}




