package com.example.dell.internshipapp.Network;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.dell.internshipapp.materialTest.MyApplication;

/**
 * Created by DELL on 26-07-2017.
 */

public class VolleySingleton {
    private ImageLoader imageLoader;
    private static VolleySingleton sInstance=null;
    private RequestQueue mRequestQueue;
    private VolleySingleton(){
        mRequestQueue= Volley.newRequestQueue(MyApplication.getAppContext());
        imageLoader= new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private LruCache<String,Bitmap> cache=new LruCache<String, Bitmap>((int)(Runtime.getRuntime().maxMemory()/1024)/8);
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);

            }
        });
    }
    public static VolleySingleton getsInstance(){
        if(sInstance==null)
        {
            sInstance=new VolleySingleton();
        }
        return sInstance;
    }
    public RequestQueue getRequestQueue(){
        return mRequestQueue;
    }
    public ImageLoader getImageLoader(){
        return imageLoader;
    }
}
