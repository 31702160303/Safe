package cn.itcast.securityguard.chapter02;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import cn.itcast.securityguard.R;
import cn.itcast.securityguard.chapter02.adapter.HomeAdapter;
import cn.itcast.securityguard.chapter02.adapter.InterPasswordDialog;
import cn.itcast.securityguard.chapter02.adapter.MD5Utils;
import cn.itcast.securityguard.chapter02.adapter.SetUpPasswordDialog;

public class HomeActivity extends AppCompatActivity {
    private GridView gv_home;
    private SharedPreferences msharedPreferences;
    private long mExitTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        msharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
        gv_home = (GridView) findViewById(R.id.gv_home);
        gv_home.setAdapter(new HomeAdapter(HomeActivity.this));
        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        if (isSetUpRassword()) {
                            showInterPswdDialog();
                        } else {
                            showSetUpPswdDialog();
                        }
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        break;
                    case 8:
                        break;
                }
            }
        });
    }

    private void showSetUpPswdDialog () {
        final SetUpPasswordDialog setUpPasswordDialog = new SetUpPasswordDialog(HomeActivity.this);
        setUpPasswordDialog.setCallBack(new SetUpPasswordDialog.MyCallBack() {

            @Override
            public void ok() {
                String firstPwsd = setUpPasswordDialog.mFirstPWDET.getText().toString().trim();
                String affirmPwsd = setUpPasswordDialog.mAffirmET.getText().toString().trim();
                if (!TextUtils.isEmpty(firstPwsd) && !TextUtils.isEmpty(affirmPwsd)) {
                    if (firstPwsd.equals(affirmPwsd)) {
                        savePswd(affirmPwsd);
                        setUpPasswordDialog.dismiss();
                        showInterPswdDialog();
                    } else {
                        Toast.makeText(HomeActivity.this,"两次密码不一致",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(HomeActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void cancle() {
                setUpPasswordDialog.dismiss();
            }
        });
        setUpPasswordDialog.setCancelable(true);
        setUpPasswordDialog.show();
    }

    private void showInterPswdDialog() {
        final String password = getPassword();
        final InterPasswordDialog mInpswdDialog = new InterPasswordDialog(HomeActivity.this);
        mInpswdDialog.setCallBack(new InterPasswordDialog.MyCallBack() {
            @Override
            public void confirm() {
                if (TextUtils.isEmpty(mInpswdDialog.getPassword())) {
                    Toast.makeText(HomeActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                }else if (password.equals(MD5Utils.encode(mInpswdDialog.getPassword()))){
                    mInpswdDialog.dismiss();
                    startActivty(SetUp1Activty.class);
                } else {
                    mInpswdDialog.dismiss();
                    Toast.makeText(HomeActivity.this,"密码有误，请重新输入",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void cancle() {
                mInpswdDialog.dismiss();
            }
        });
        mInpswdDialog.setCancelable(true);
        mInpswdDialog.show();
    }

    private void savePswd (String affirmPwsd) {
        SharedPreferences.Editor editor = msharedPreferences.edit();
        editor.putString("PhoneAntiTheftPWD",MD5Utils.encode(affirmPwsd));
        editor.commit();
    }

    private String getPassword() {
        String passwrod = msharedPreferences.getString("PhoneAntiTheftPWD",null);
        if (TextUtils.isEmpty(passwrod)) {
            return "";
        }
        return passwrod;
    }
    private boolean isSetUpRassword() {
        String password = msharedPreferences.getString("PhoneAntiTheftPWD",null);
        if (TextUtils.isEmpty(password)) {
            return false;
        }
        return true;
    }

    public void startActivty(Class<?> cls) {
        Intent intent = new Intent(HomeActivity.this,cls);
        startActivity(intent);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis()-mExitTime)<2000) {
                System.exit(0);
            } else {
                Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
                mExitTime= System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
}
