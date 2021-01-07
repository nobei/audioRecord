package com.example.audiorecord;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.media.AudioRecord;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static android.content.Context.SENSOR_SERVICE;
import static android.hardware.Sensor.TYPE_GYROSCOPE;
import static androidx.core.content.ContextCompat.getExternalFilesDirs;
import static androidx.core.content.ContextCompat.getSystemService;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ViewUtils;
import androidx.core.content.res.ResourcesCompat;

public class GlobalBean {


    private Button sensorStartButton;
    private Button sensorEndButton;
    private Button recordButton;
    private Button stopRecordButton;
    private Button startPlayButton;
    private Button stopPlayButton;
    private SensorManager sensorManager = null;
    private GyroSensor gyroSensor;
    private GyroSensor accelrateSensor;
    private EditText text;
    private audioData audioRecord;
    private audioPlay audioPlay;
    private InputStream playFilePath;
    private MainActivity activity;
    private Button accelerateButton;
    private Button accelereateStop;
    private Button playBarkerButton;
    private Button playCirButton;
    private Button playCirJumpButton;
    private Button playTestButton;
    private EditText fileNameToTest;

    public EditText getFileNameToTest() {
        return fileNameToTest;
    }

    public void setFileNameToTest(EditText fileNameToTest) {
        this.fileNameToTest = fileNameToTest;
    }

    public Button getPlayCirButton() {
        return playCirButton;
    }

    public void setPlayCirButton(Button playCirButton) {
        this.playCirButton = playCirButton;
    }

    private static final GlobalBean instance = new GlobalBean();

    private GlobalBean() {}

    public static GlobalBean getInstance() {
        return instance;
    }

    public Button getPlayBarkerButton() {
        return playBarkerButton;
    }

    public void setPlayBarkerButton(Button playBarkerButton) {
        this.playBarkerButton = playBarkerButton;
    }

    public Button getPlayTestButton() {
        return playTestButton;
    }

    public void setPlayTestButton(Button playTestButton) {
        this.playTestButton = playTestButton;
    }

    public Button getPlayCirJumpButton() {
        return playCirJumpButton;
    }

    public void setPlayCirJumpButton(Button playCirJumpButton) {
        this.playCirJumpButton = playCirJumpButton;
    }

    public Button getAccelerateButton() {
        return accelerateButton;
    }

    public void setAccelerateButton(Button accelerateButton) {
        this.accelerateButton = accelerateButton;
    }

    public Button getAccelereateStop() {
        return accelereateStop;
    }

    public void setAccelereateStop(Button accelereateStop) {
        this.accelereateStop = accelereateStop;
    }

    public MainActivity getActivity() {
        return activity;
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch(msg.what)
            {
                case 1:text.setText((CharSequence) msg.obj);
                break;
            }

        }
    };


    public InputStream getPlayFilePath() {
        return playFilePath;
    }

    public void setPlayFilePath(InputStream playFilePath) {
        this.playFilePath = playFilePath;
    }

    public Button getStartPlayButton() {
        return startPlayButton;
    }

    public void setStartPlayButton(Button startPlayButton) {
        this.startPlayButton = startPlayButton;
    }

    public Button getStopPlayButton() {
        return stopPlayButton;
    }

    public void setStopPlayButton(Button stopPlayButton) {
        this.stopPlayButton = stopPlayButton;
    }

    public Button getStopRecordButton() {
        return stopRecordButton;
    }

    public void setStopRecordButton(Button stopRecordButton) {
        this.stopRecordButton = stopRecordButton;
    }

    public Button getRecordButton() {
        return recordButton;
    }

    public void setRecordButton(Button recordButton) {
        this.recordButton = recordButton;
    }

    public Button getSensorStartButton() {
        return sensorStartButton;
    }

    public Button getSensorEndButton() {
        return sensorEndButton;
    }

    public EditText getText() {
        return text;
    }

    public void setText(EditText text) {
        this.text = text;
    }

    public SensorManager getSensorManager() {
        return sensorManager;
    }


    public void setSensorStartButton(Button sensorStartButton) {
        this.sensorStartButton = sensorStartButton;
    }

    public void setSensorEndButton(Button sensorEndButton) {
        this.sensorEndButton = sensorEndButton;
    }

    public void setSensorManager(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
    }



    void init() {
        this.sensorStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                File file = FileUtil.createFile(path, "gyroSensorData" + System.currentTimeMillis() + ".txt");

                if (file != null) {

                    gyroSensor = new GyroSensor(file, sensorManager, handler,Sensor.TYPE_GYROSCOPE);

                    if (gyroSensor != null)
                        gyroSensor.start();

                }

            }
        });

        this.accelerateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                File file = FileUtil.createFile(path, "LinearaccelerateSensorData" + System.currentTimeMillis() + ".txt");

                if (file != null) {

                    accelrateSensor = new GyroSensor(file, sensorManager, handler,Sensor.TYPE_LINEAR_ACCELERATION);

                    if (accelrateSensor != null)
                        accelrateSensor.start();

                }

            }
        });

        this.accelereateStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(accelrateSensor != null)
                {
                    accelrateSensor.closeGyroSensor();
                    accelrateSensor = null;
                }
            }
        });

        this.sensorEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gyroSensor != null) {
                    gyroSensor.closeGyroSensor();
                    gyroSensor = null;
                }
            }
        });

        this.recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                audioRecord = new audioData();

                audioRecord.initRecord();
                try {
                    audioRecord.getData();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        });

        this.stopRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audioRecord != null) {
                    audioRecord.stopRecord();
                    audioRecord = null;
                }
            }
        });

        this.startPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playFilePath = activity.getResources().openRawResource(R.raw.zadoff);


                audioPlay = new audioPlay();
                if (audioPlay != null) {
                    try {
                        audioPlay.initPlay(playFilePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    audioPlay.play();
                }
            }
        });


        this.stopPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audioPlay != null) {
                    audioPlay.stopPlay();
                }
            }
        });


        this.playBarkerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                playFilePath = activity.getResources().openRawResource(R.raw.barkcode);


                audioPlay = new audioPlay();
                if (audioPlay != null) {
                    try {
                        audioPlay.initPlay(playFilePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    audioPlay.play();
                }
            }
        });

        this.playCirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                playFilePath = activity.getResources().openRawResource(R.raw.cirphase);


                audioPlay = new audioPlay();
                if (audioPlay != null) {
                    try {
                        audioPlay.initPlay(playFilePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    audioPlay.play();
                }

            }
        });

        this.playCirJumpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                playFilePath = activity.getResources().openRawResource(R.raw.cirjump);


                audioPlay = new audioPlay();
                if (audioPlay != null) {
                    try {
                        audioPlay.initPlay(playFilePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    audioPlay.play();
                }

            }
        });

        this.playTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String path = Environment.getExternalStorageDirectory().getAbsolutePath();

                String fileName = fileNameToTest.getText().toString();

                try {
                    playFilePath = new FileInputStream(new File(path+"/"+fileName+".txt"));
                    audioPlay = new audioPlay();
                    audioPlay.initPlay(playFilePath);
                    audioPlay.play();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }



            }
        });
    }

}
