package cn.itcast.securityguard.chapter02;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2019/4/3.
 */
public class BootConpleteReciever {
    private static final String TAG = BootConpleteReciever.class.getSimpleName();
    public void onReceive(Context context,Intent intent) {
        ((App)context.getApplicationContext()).correctSIM();
    }
}
