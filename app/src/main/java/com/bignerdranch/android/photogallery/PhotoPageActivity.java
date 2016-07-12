package com.bignerdranch.android.photogallery;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;

/**
 * Created by olo35 on 07.07.2016.
 */
public class PhotoPageActivity extends SingleFragmentActivity {

    private PhotoPageFragment mFragment;
    public static Intent newIntent(Context context,Uri photoPageUri){
        Intent i = new Intent(context,PhotoPageActivity.class);
        i.setData(photoPageUri);
        return i;
    }

    @Override
    protected Fragment createFragment() {
        mFragment =PhotoPageFragment.newInstance(getIntent().getData());
        return mFragment;
    }

    @Override
    public void onBackPressed() {
        if(mFragment.checkIfPrevious())
        {
            super.onBackPressed();
        }
    }
}
