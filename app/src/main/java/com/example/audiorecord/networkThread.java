package com.example.audiorecord;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class networkThread extends Thread{

    private int socketNum;
    private String ip;
    private String fileName;






    public networkThread(String ip, int socketNum)
    {
        this.ip = ip;
        this.socketNum = socketNum;
    }

    public int getSocketNum() {
        return socketNum;
    }

    public void setSocketNum(int socketNum) {
        this.socketNum = socketNum;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void run() {
        SendFile();

    }

    public void SendFile()
    {

        try {



                Socket data = new Socket(ip, socketNum);
                OutputStream outputData = data.getOutputStream();
                FileInputStream fileInput = new FileInputStream(fileName);
                int size = -1;
                byte[] buffer = new byte[1024];
                while ((size = fileInput.read(buffer, 0, 1024)) != -1) {
                    outputData.write(buffer, 0, size);
                }
                outputData.close();
                fileInput.close();
                data.close();

        } catch (Exception e) {
            System.out.print("send error");
        }

    }
}
