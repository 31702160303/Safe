package cn.itcast.securityguard.chapter02;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.Toast;

import cn.itcast.securityguard.R;

public class SetUp1Activty extends BaseSetUpActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
        inintView();
    }

    @Override
    public void showPre() {
        Toast.makeText(this,"当前页面已经是第一页",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNext() {
        startActivityAndFinishSelf(SetUp2Activity.class);
    }

    private void inintView() {
        ((RadioButton) findViewById(R.id.rb_first)).setChecked(true);
    }
}
