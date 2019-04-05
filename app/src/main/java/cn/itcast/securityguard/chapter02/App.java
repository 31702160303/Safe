package cn.itcast.securityguard.chapter02;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Administrator on 2019/4/3.
 */
public class App extends Application {
    public void onCreate() {
        super.onCreate();
        correctSIM();
    }

    public void correctSIM() {
        SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean protection = sp.getBoolean("protection",true);
        if (protection) {
            String bindsim = sp.getString("sim","");
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELECOM_SERVICE);
            String realsim = tm.getSimSerialNumber();
            if (bindsim.equals(realsim)) {
                Log.i("","SIM卡未发生变化，还是您的手机");
            } else {
                Log.i("","SIM卡变化了");
                String safenumber = sp.getString("safephone","");
                if (!TextUtils.isEmpty(safenumber)) {
                    SmsManager smsManage = SmsManager.getDefault();
                    smsManage.sendTextMessage(safenumber,null,"您的亲友手机的SIM卡已经被更换！",null,null);
                }
            }
        }
    }
}
