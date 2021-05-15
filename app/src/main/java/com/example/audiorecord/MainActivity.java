package com.example.audiorecord;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private Button sensorStartButton;
    private Button sensorEndButton;
    private SensorManager sensorManager;
    private GlobalBean bean;
    private Button startPlayButton;
    private Button stopPlayButton;
    private EditText text;
    private Button recordButton;
    private Button stopRecordButton;
    private Button accelerateButton;
    private Button accelerateStop;
    private Button sendBarkButton;
    private Button sendCIRButton;
    private Button sendJUMPButton;
    private Button playRecordButton;
    private Button playFixButton;
    private EditText fileName;
    private Button playMutiPointCir;
    private EditText ip;
    private EditText port;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorStartButton = findViewById(R.id.button1);
        sensorEndButton = findViewById(R.id.button2);
        recordButton = findViewById(R.id.button3);
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        stopRecordButton = findViewById(R.id.button4);
        startPlayButton = findViewById(R.id.button5);
        stopPlayButton = findViewById(R.id.button6);
        accelerateButton = findViewById(R.id.button7);
        accelerateStop = findViewById(R.id.button8);
        sendBarkButton = findViewById(R.id.button9);
        sendCIRButton = findViewById(R.id.button10);
        sendJUMPButton = findViewById(R.id.button11);
        playRecordButton = findViewById(R.id.button12);
        fileName = findViewById(R.id.fileName);
        playFixButton = findViewById(R.id.button14);
        playMutiPointCir = findViewById(R.id.button13);
        ip = findViewById(R.id.text3);
        port = findViewById(R.id.text4);



        text = findViewById(R.id.text);

        checkPermission();

        bean = GlobalBean.getInstance();

        init();

    }

    public void init()
    {
        bean.setSensorManager(sensorManager);
        bean.setSensorStartButton(sensorStartButton);
        bean.setSensorEndButton(sensorEndButton);
        bean.setRecordButton(recordButton);
        bean.setStopRecordButton(stopRecordButton);
        bean.setText(text);
        bean.setStartPlayButton(startPlayButton);
        bean.setStopPlayButton(stopPlayButton);
        bean.setAccelerateButton(accelerateButton);
        bean.setAccelereateStop(accelerateStop);
        bean.setPlayFilePath(getResources().openRawResource(R.raw.highfre));
        bean.setActivity(this);
        bean.setPlayBarkerButton(sendBarkButton);
        bean.setPlayCirButton(sendCIRButton);
        bean.setPlayCirJumpButton(sendJUMPButton);
        bean.setPlayTestButton(playRecordButton);
        bean.setFileNameToTest(fileName);
        bean.setPlayFixButton(playFixButton);
        bean.setPlayMultiPoint(playMutiPointCir);
        bean.setIp(ip);
        bean.setPort(port);


        bean.init();


    }


//    public InputStream getRawResources()
//    {
//        return getResources().openRawResource(R.raw.highfre);
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            boolean isAllGranted = true;
            // 判断是否所有的权限都已经授予了
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }
            if (isAllGranted) {
                // 所有的权限都授予了
                Log.e("err","权限都授权了");
            } else {
                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
                //容易判断错
                //MyDialog("提示", "某些权限未开启,请手动开启", 1) ;
            }
        }

    }

    private boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                //Log.e("err","权限"+permission+"没有授权");
                return false;
            }
        }
        return true;
    }

    public void checkPermission() {
        int targetSdkVersion = 0;
        String[] PermissionString={Manifest.permission.READ_EXTERNAL_STORAGE,
               Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO};
        try {
            final PackageInfo info = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            targetSdkVersion = info.applicationInfo.targetSdkVersion;//获取应用的Target版本
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
//            Log.e("err", "检查权限_err0");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Build.VERSION.SDK_INT是获取当前手机版本 Build.VERSION_CODES.M为6.0系统
            //如果系统>=6.0
            if (targetSdkVersion >= Build.VERSION_CODES.M) {
                //第 1 步: 检查是否有相应的权限
                boolean isAllGranted = checkPermissionAllGranted(PermissionString);
                if (isAllGranted) {
                    //Log.e("err","所有权限已经授权！");
                    return;
                }
                // 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
                ActivityCompat.requestPermissions(this,
                        PermissionString, 1);
            }
        }
    }




}
