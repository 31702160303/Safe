package cn.itcast.securityguard.chapter01.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import cn.itcast.securityguard.R;
import cn.itcast.securityguard.chapter02.HomeActivity;

/**
 * Created by Administrator on 2019/3/6.
 */

public class VersionUpdateUtils {
    private static final int MESSAGE_NET_EEOR = 101;
    private static final int MESSAGE_IO_EEOR = 102;
    private static final int MESSAGE_JSON_EEOR = 103;
    private static final int MESSAGE_SHOEW_DIALOG = 104;
    protected static final int MESSAGE_ENTERHOME = 105;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_IO_EEOR:
                    Toast.makeText(context,"IO异常",Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MESSAGE_JSON_EEOR:
                    Toast.makeText(context,"JSON异常",Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MESSAGE_NET_EEOR:
                    Toast.makeText(context,"网络异常",Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MESSAGE_SHOEW_DIALOG:
                    showUpdateDialog(versionEntity);
                    break;
                case MESSAGE_ENTERHOME:
                    Intent intent = new Intent(context, HomeActivity.class);
                    context.startActivity(intent);
                    context.finish();
                    break;
            }
        };
    };
    private String mVersion;
    private Activity context;
    private ProgressDialog mProgressDialog;
    private VersionEntity versionEntity;
    public VersionUpdateUtils(String Version,Activity activity) {
        mVersion = Version;
        context = activity;
    }
    public void getCloudVersion() {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(),5000);
            HttpConnectionParams.setSoTimeout(client.getParams(),5000);
//            HttpGet httpGet = new HttpGet("http://172.26.42.58/updateinfo.html");
            HttpGet httpGet = new HttpGet("http://192.168.31.213/updateinfo.html");
            HttpResponse execute = client.execute(httpGet);
            if (execute.getStatusLine().getStatusCode()==200) {
                HttpEntity entity = execute.getEntity();
                String result = EntityUtils.toString(entity,"gbk");
                JSONObject jsonObject = new JSONObject(result);
                versionEntity = new VersionEntity();

                String code = jsonObject.getString("code");
                String des = jsonObject.getString("des");
                String apkurl = jsonObject.getString("apkurl");

                versionEntity.versioncode = code;
                versionEntity.description = des;
                versionEntity.apkurl = apkurl;

                if (!mVersion.equals(versionEntity.versioncode)) {
                    handler.sendEmptyMessage(MESSAGE_SHOEW_DIALOG);
                }
            }
        }catch (ClientProtocolException e) {
            handler.sendEmptyMessage(MESSAGE_NET_EEOR);
            e.printStackTrace();
        }catch (IOException e) {
            handler.sendEmptyMessage(MESSAGE_IO_EEOR);
            e.printStackTrace();
        }catch (JSONException e) {
            handler.sendEmptyMessage(MESSAGE_JSON_EEOR);
            e.printStackTrace();
        }
    }
    private void showUpdateDialog(final VersionEntity versionEntity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("检查到新版本：" + versionEntity.versioncode);
        builder.setMessage(versionEntity.description);
        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setPositiveButton("立即升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //下载进度条的调用
                initProgressDialog();
                //下载新版本apk的调用
                downloadNewApk(versionEntity.apkurl);
            }
        });
        //暂不升级点击事件
        builder.setNegativeButton("暂不升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                enterHome();
            }
        });
        builder.show();
    }
        //初始化进度条对话框
        private void initProgressDialog() {
            mProgressDialog = new ProgressDialog(context);//进度条对象声明
            mProgressDialog.setMessage("准备下载。。。");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.show();
        }
        //下载新版本apk
        protected void downloadNewApk(String apkurl) {
            DownLoadUtils downLoadUtils = new DownLoadUtils();
            downLoadUtils.downapk(apkurl, "mnt/sdcard/WeChat.apk", new MyCallBack() {

                public void onSuccess(ResponseInfo<File> arg0) {
                    mProgressDialog.dismiss();
                    MyUtils.installApk(context);
                }

                @Override
                public void onFailure(HttpException arg0, String arg1) {
                    mProgressDialog.setMessage("下载失败...");
                    mProgressDialog.dismiss();
                    enterHome();
                }

                public void onLoadding(long total, long current, boolean isUploading) {
                    mProgressDialog.setMax((int)total);
                    mProgressDialog.setMessage("正在下载...");
                    mProgressDialog.setProgress((int)current);
                }
            });
        }
    private void enterHome() {
        handler.sendEmptyMessageDelayed(MESSAGE_ENTERHOME, 2000);
    }
}
