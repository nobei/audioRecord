package com.example.audiorecord;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.example.audiorecord.MainActivity;

public class FileUtil {



    public static File createFile(String path,String name) {
        File file = null;
        String tmp = Environment.getExternalStorageState();
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                file = new File(path + '/' + name);
                if (!file.exists()) {
                    file.createNewFile();
                }
            } catch (Exception e) {
                Log.d("File", "创建文件失败");
                e.printStackTrace();
            }
        }
        return file;
    }

    public static Map readFile(String path) throws IOException {
        FileInputStream in = null;
        short[] r = new short[4800];
        Map<String,Object> result = new HashMap<String,Object>();

        try {
            in = new FileInputStream(new File(path));
            result = readFileFromInputStream(in);



        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return result;


    }


    public static Map readFileFromInputStream(InputStream in) throws IOException {

        Map<String,Object> result = new HashMap<String,Object>();
        short []r = new short[4800];
        InputStreamReader stream = new InputStreamReader(in);
        BufferedReader file= new BufferedReader(stream);
        int i = 0;
        try{
            String length = file.readLine();
            while(length != null) {
                float num = Float.parseFloat(length);
                short n = (short) num;
                r[i++] = n;
                length = file.readLine();
            }}
        finally {

            if (file != null) file.close();
            if(stream != null) stream.close();
        }

        result.put("length",i);
        result.put("data",r);
        return result;

    }





}
