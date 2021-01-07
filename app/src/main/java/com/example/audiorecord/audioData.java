package com.example.audiorecord;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Environment;
import android.provider.VoicemailContract;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class audioData {
    //音频输入-麦克风
    private final static int AUDIO_INPUT = MediaRecorder.AudioSource.MIC;
    //采用频率
    //44100是目前的标准，但是某些设备仍然支持22050，16000，11025
    //采样频率一般共分为22.05KHz、44.1KHz、48KHz三个等级
    private final static int AUDIO_SAMPLE_RATE = 48000;
    //声道 单声道
    private final static int AUDIO_CHANNEL = AudioFormat.CHANNEL_IN_MONO  ;
    //编码
    private final static int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    // 缓冲区字节大小
    private int bufferSizeInBytes = 4800;

    private DataOutputStream   out = null;

    //录音对象
    private AudioRecord audioRecord;




    //文件名
  //  private String fileName;

    //录音文件
    private List<String> filesName = new ArrayList<>();

    Status status;









    void initRecord()
    {
        if(audioRecord == null)
            audioRecord = new AudioRecord(AUDIO_INPUT, AUDIO_SAMPLE_RATE, AUDIO_CHANNEL, AUDIO_ENCODING, bufferSizeInBytes);

        status = Status.STATUS_READY;




    }




    public void getData() throws IOException {

        if (status == Status.STATUS_NO_READY ) {
            throw new IllegalStateException("录音尚未初始化,请检查是否禁止了录音权限~");
        }
        if (status == Status.STATUS_START) {
            throw new IllegalStateException("正在录音");
        }
        Log.d("AudioRecorder", "===startRecord===" + audioRecord.getState());
        audioRecord.startRecording();


        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        //File file = FileUtil.createFile(path, "recordData" + System.currentTimeMillis() + ".txt");
        out = new DataOutputStream(new FileOutputStream(path+ '/' +"recordData" + System.currentTimeMillis() + ".txt"));




        new Thread(new Runnable() {
            @Override
            public void run() {
                //writeDataTOFile(listener);
                byte[] audiodata = new byte[bufferSizeInBytes] ;
                status = Status.STATUS_START;
                while (status == Status.STATUS_START) {
                    int readsize = audioRecord.read(audiodata, 0,bufferSizeInBytes);
                    if (AudioRecord.ERROR_INVALID_OPERATION != readsize && out != null) {
                        try {
                            out.write(audiodata);
//                        if (listener != null) {
//                            //用于拓展业务
//                            listener.recordOfByte(audiodata, 0, audiodata.length);
//                        }
                        } catch (IOException e) {
                            Log.e("AudioRecorder", e.getMessage());
                        }

                    }
                }
            }
        }).start();

    }


    /**
     * 暂停录音
     */
    public void pauseRecord() {
        Log.d("AudioRecorder", "===pauseRecord===");
        if (status != Status.STATUS_START) {
            throw new IllegalStateException("没有在录音");
        } else {
            audioRecord.stop();
            status = Status.STATUS_PAUSE;
        }
    }

    /**
     * 停止录音
     */
    public void stopRecord() {
        Log.d("AudioRecorder", "===stopRecord===");
        if (status == Status.STATUS_NO_READY || status == Status.STATUS_READY) {
            throw new IllegalStateException("录音尚未开始");
        } else {
            audioRecord.stop();
            status = Status.STATUS_STOP;
            release();
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        Log.d("AudioRecorder", "===release===");
        //假如有暂停录音
 //       try {
//            if (filesName.size() > 0) {
//                List<String> filePaths = new ArrayList<>();
//                for (String fileName : filesName) {
//                    filePaths.add(FileUtil.getPcmFileAbsolutePath(fileName));
//                }
//                //清除
//                filesName.clear();
//                //将多个pcm文件转化为wav文件
//         //        mergePCMFilesToWAVFile(filePaths);
//
//            } else {
//                //这里由于只要录音过filesName.size都会大于0,没录音时fileName为null
//                //会报空指针 NullPointerException
//                // 将单个pcm文件转化为wav文件
//                //Log.d("AudioRecorder", "=====makePCMFileToWAVFile======");
//                //makePCMFileToWAVFile();
//            }
//        } catch (IllegalStateException e) {
//            throw new IllegalStateException(e.getMessage());
//        }

        if (audioRecord != null) {
            audioRecord.release();
            audioRecord = null;
        }

        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            Log.d("fail","文件关闭失败");
            e.printStackTrace();
        }

        status = Status.STATUS_NO_READY;
    }

    /**
     * 取消录音
     */
    public void canel() {
        filesName.clear();
        //fileName = null;
        if (audioRecord != null) {
            audioRecord.release();
            audioRecord = null;
        }

        status = Status.STATUS_NO_READY;
    }



//    public void writeDataTOFile()
//    {
//        // new一个byte数组用来存一些字节数据，大小为缓冲区大小
//        byte[] audiodata = new byte[bufferSizeInBytes];
//
//        FileOutputStream fos = null;
//        int readsize = 0;
//        try {
//            String currentFileName = fileName;
//            if (status == Status.STATUS_PAUSE) {
//                //假如是暂停录音 将文件名后面加个数字,防止重名文件内容被覆盖
//                currentFileName += filesName.size();
//
//            }
//            filesName.add(currentFileName);
//            File file = new File(FileUtil.getPcmFileAbsolutePath(currentFileName));
//            if (file.exists()) {
//                file.delete();
//            }
//            fos = new FileOutputStream(file);// 建立一个可存取字节的文件
//        } catch (IllegalStateException e) {
//            Log.e("AudioRecorder", e.getMessage());
//            throw new IllegalStateException(e.getMessage());
//        } catch (FileNotFoundException e) {
//            Log.e("AudioRecorder", e.getMessage());
//
//        }
//        //将录音状态设置成正在录音状态
//        status = Status.STATUS_START;
//        while (status == Status.STATUS_START) {
//            readsize = audioRecord.read(audiodata, 0, bufferSizeInBytes);
//            if (AudioRecord.ERROR_INVALID_OPERATION != readsize && fos != null) {
//                try {
//                    fos.write(audiodata);
//                    if (listener != null) {
//                        //用于拓展业务
//                        listener.recordOfByte(audiodata, 0, audiodata.length);
//                    }
//                } catch (IOException e) {
//                    Log.e("AudioRecorder", e.getMessage());
//                }
//            }
//        }
//        try {
//            if (fos != null) {
//                fos.close();// 关闭写入流
//            }
//        } catch (IOException e) {
//            Log.e("AudioRecorder", e.getMessage());
//        }
//    }

    public enum Status {
        //未开始
        STATUS_NO_READY,
        //预备
        STATUS_READY,
        //录音
        STATUS_START,
        //暂停
        STATUS_PAUSE,
        //停止
        STATUS_STOP
    }



}
