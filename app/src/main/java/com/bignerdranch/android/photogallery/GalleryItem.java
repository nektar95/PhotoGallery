package com.bignerdranch.android.photogallery;

import android.net.Uri;

import java.net.URI;

/**
 * Created by olo35 on 02.04.2016.
 */
public class GalleryItem {
    private String mCaption;
    private String mId;
    private String mUrl;

    public String getOwner() {
        return mOwner;
    }

    public void setOwner(String owner) {
        mOwner = owner;
    }

    private String mOwner;

    @Override
    public String toString()
    {
        return mCaption;
    }

    public String getCaption() {
        return mCaption;
    }

    public Uri getPhotoPageUri(){
        return Uri.parse("http://www.flickr.com/photos/").buildUpon().appendPath(mOwner).appendPath(mId).build();
    }

    public void setCaption(String caption) {
        mCaption = caption;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }
}
