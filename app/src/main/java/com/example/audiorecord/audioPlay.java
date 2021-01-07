package com.example.audiorecord;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class audioPlay {

    private final static int AUDIO_SAMPLE_RATE = 48000;
    //声道 单声道
    private final static int AUDIO_CHANNEL = AudioFormat.CHANNEL_IN_MONO  ;
    //编码
    private final static int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    //发射缓存区
    private Map playBuffData = null;

    //播放对象
    private AudioTrack audioTrack;

    private PlayStatus playStatus = PlayStatus.STATUSPLAY_NO_READY;




    void initPlay(InputStream in) throws IOException {

        if(playStatus == PlayStatus.STATUSPLAY_NO_READY) {
            playBuffData = FileUtil.readFileFromInputStream(in);

            if (audioTrack == null) {
                int bufferSize = AudioTrack.getMinBufferSize(AUDIO_SAMPLE_RATE, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                        AUDIO_ENCODING);
                audioTrack = new AudioTrack(AudioManager.STREAM_SYSTEM,
                        48000, 2, AudioFormat.ENCODING_PCM_16BIT, bufferSize,
                        AudioTrack.MODE_STREAM);

            }

            if (playStatus != null && audioTrack != null) {

                playStatus = PlayStatus.STATUSPLAY_READY;
            }
        }





    }

    void play()
    {
        if(playStatus == PlayStatus.STATUSPLAY_NO_READY)
            throw new IllegalStateException("播放尚未初始化,请检查是否禁止了录音权限~");

        if(playStatus == PlayStatus.STATUSPLAY_START)
            throw new IllegalStateException("正在播放");


        audioTrack.play();
        playStatus = PlayStatus.STATUSPLAY_START;

        new Thread(new Runnable() {
            @Override
            public void run() {
                int buffLength = (int) playBuffData.get("length");
                short [] buffData = (short[]) playBuffData.get("data");
                while (playStatus == PlayStatus.STATUSPLAY_START)
                {
                    audioTrack.write(buffData,0,buffLength);

                }
            }
        }).start();




    }

    void stopPlay()
    {
        playStatus = PlayStatus.STATUSPLAY_STOP;
        audioTrack.stop();

        if (audioTrack != null) {
            audioTrack.release();
            audioTrack = null;
        }
    }

    public enum PlayStatus
    {
        //未发射声波
        STATUSPLAY_NO_READY,
        //预备发射声波
        STATUSPLAY_READY,
        //发射声波
        STATUSPLAY_START,
        //停止发射声波
        STATUSPLAY_STOP,
    }
}
