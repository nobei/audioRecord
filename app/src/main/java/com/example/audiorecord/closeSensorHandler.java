package com.example.audiorecord;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class closeSensorHandler implements InvocationHandler {
    private Object tar;
    private networkThread net;

    public closeSensorHandler(Object tar,networkThread net) {
        this.net = net;
        this.tar = tar;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Object result = method.invoke(tar,args);
        String res = (String) result;
        if (!res.equals("") && net != null) {
            net.setFileName(res);
            net.start();
        }
        return result;
    }

    public Object getProxyInstance(){
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),tar.getClass().getInterfaces(),this);
    }
}
