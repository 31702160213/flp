package com.example.administrator.flp.utils;

public class HttpUtil {
    //TODO:http路径拼接
    public static String url ="http://123.207.85.214/chat/";
    public static String getUrl;
    public HttpUtil(String name){
        getUrl=url+name;
    }
}
