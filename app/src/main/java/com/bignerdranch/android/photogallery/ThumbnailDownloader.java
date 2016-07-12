package com.bignerdranch.android.photogallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by olo35 on 02.07.2016.
 */
public class ThumbnailDownloader<T> extends HandlerThread {
    private static final String TAG = "ThumbnailDownloader";
    private static final int MESSAGE_DOWNLOAD = 0;

    private Handler mRequestHandler;
    private ConcurrentMap<T,String> mRequestMap = new ConcurrentHashMap<>();
    private LruCache<String,Bitmap> mPostLruCache;
    private Handler mResponseHandler;
    private ThumbnailDownloaderListener<T> mTThumbnailDownloaderListener;

    public interface ThumbnailDownloaderListener<T>{
        void onThumbnailDownloaded(T target,Bitmap thumbnail);
    }
    public void setTThumbnailDownloaderListener(ThumbnailDownloaderListener<T> listener){
        mTThumbnailDownloaderListener = listener;
    }

    public ThumbnailDownloader(Handler responseHandler)
    {
        super(TAG);
        mResponseHandler = responseHandler;
        mPostLruCache=new LruCache<String, Bitmap>(16184);
    }

    @Override
    protected void onLooperPrepared(){
        mRequestHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == MESSAGE_DOWNLOAD){
                    T target = (T) msg.obj;
                    Log.i(TAG,"Got a request for url:"+mRequestMap.get(target));
                    handleRequest(target);
                }

            }
        };
    }

    public void queueThumbnail(T target, String url){
        Log.i(TAG,"Got a url:" +url);
        if(url ==null)
        {
            mRequestMap.remove(target);
        }
        else {
            mRequestMap.put(target,url);
            mRequestHandler.obtainMessage(MESSAGE_DOWNLOAD,target).sendToTarget();
        }
    }

    public void clearQueue()
    {
        mRequestHandler.removeMessages(MESSAGE_DOWNLOAD);
    }

    private void handleRequest(final T target){

        final String url = mRequestMap.get(target);

        if(url ==  null){
            return;
        }
        final Bitmap bitmap = getBitmap(url);


        Log.i(TAG,"Bitmap crated");

        mResponseHandler.post(new Runnable() {
            @Override
            public void run() {
                if(mRequestMap.get(target)!= url){
                    return;
                }
                mRequestMap.remove(target);
                mTThumbnailDownloaderListener.onThumbnailDownloaded(target,bitmap);
            }
        });


    }
    private Bitmap getBitmap(String url)
    {
        Bitmap bitmapPost  = mPostLruCache.get(url);
        if(bitmapPost !=  null){
            return bitmapPost;
        }
        try{
            byte[] bitmapBytes = new FlickrFetchr().getUrlBytes(url);
            bitmapPost = BitmapFactory.decodeByteArray(bitmapBytes,0,bitmapBytes.length);
            mPostLruCache.put(url,bitmapPost);
            return bitmapPost;
        }catch(IOException ioe){
        Log.e(TAG,"error download",ioe);
    }
    return null;
    }
}
