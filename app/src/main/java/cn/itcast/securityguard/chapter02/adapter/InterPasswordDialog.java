package cn.itcast.securityguard.chapter02.adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.itcast.securityguard.R;

/**
 * Created by Administrator on 2019/3/20.
 */

public class InterPasswordDialog extends Dialog implements
        View.OnClickListener{
    private TextView mTitleTV;//标题
    public EditText mInterET;
    private Button mOkBtn;
    private Button mCancleBtn;
    private MyCallBack myCallBack;
    private Context context;

    public InterPasswordDialog(Context context) {
        super(context,R.style.dialog_custom);
        this.context = context;
    }
    public void setCallBack(MyCallBack myCallBack) {
        this.myCallBack = myCallBack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inter_password_dialog);
        initView();
    }
    private void initView() {
        mTitleTV = (TextView) findViewById(R.id.tv_interpwd_title);
        mInterET = (EditText) findViewById(R.id.et_inter_password);
        mOkBtn = (Button) findViewById(R.id.btn_comfirm);
        mCancleBtn = (Button) findViewById(R.id.btn_dismiss);
        mOkBtn.setOnClickListener(this);
        mCancleBtn.setOnClickListener(this);
    }

    //设置对话框标题
    public void setTitle(String text) {
        if (!TextUtils.isEmpty(text))
        {
            mTitleTV.setText(text);
        }
    }

    public String getPassword() {
        return  mInterET.getText().toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_comfirm:
                myCallBack.confirm();
                break;
            case R.id.btn_dismiss:
                myCallBack.cancle();
                break;
        }

    }

    public interface MyCallBack {
        void confirm();

        void cancle();
    }
}
