package com.example.audiorecord;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;


import static android.content.ContentValues.TAG;

public class GyroSensor extends Thread implements  SensorEventListener,closeSensor{
//    final int frequency = 20;
    private Sensor Sensor = null;
    private SensorManager sensorManager;
    private File file = null;
    private FileWriter fileWriter = null;
    private boolean statusFlag  = false;
    private Message message;
    private Handler handler;
    private int sensorKind;

    public GyroSensor(File file,SensorManager sensorManager,Handler handler,int sensorKind)
    {
        this.sensorManager = sensorManager;
        this.file = file;
        this.handler = handler;
        this.sensorKind = sensorKind;
    }

    @Override
    public synchronized void start() {
        try {
            fileWriter = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "start: record data error");
        }
        Sensor = sensorManager.getDefaultSensor(sensorKind);

        if(Sensor != null)
        {
            sensorManager.registerListener(this,Sensor,10000);

            statusFlag = true;





        }else
        {
            Log.d("Sensor","获取传感器失败");
        }



    }

    public void closeGyroSensor()
    {
        statusFlag = false;
        sensorManager.unregisterListener(this);
        try {
            fileWriter.close();
        } catch (IOException e) {
            Log.d("fail","文件关闭失败");
            e.printStackTrace();
        }
        Log.d("Sensor","传感器记录结束");



    }






    @Override
    public void onSensorChanged(SensorEvent event) {

            if (event.sensor.getType() == sensorKind && statusFlag) {
                String data =  event.values[0] + " " + event.values[1] + " " + event.values[2] + '\n';
                message = new Message();
                message.what = 1;
                message.obj = data;
 //               handler.sendMessage(message);
                try {
                    fileWriter.write(data);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("file", "Sensor write error");
                }

                handler.sendMessage(message);



            }
        }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public String close() {
        closeGyroSensor();
        String s = file.getAbsolutePath();

        return s;

    }
}
