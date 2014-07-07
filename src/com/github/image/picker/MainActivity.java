package com.github.image.picker;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.appunity.image.picker.R;
import com.github.image.picker.util.FileSize;
import com.github.image.picker.util.ImageZip;
import com.github.image.picker.util.Utils;

public class MainActivity extends Activity {
    private static final boolean DEBUG = true;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onBtnClick(View v) {
        if (DEBUG) Log.v(TAG, "onBtnClick(v)");
        Intent intent = new Intent(this, AlbumListActivity.class);
        intent.putExtra(AlbumListActivity.EXTRA_MAX_NUMBER_OF_PICTURES, 3);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (DEBUG) Log.v(TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if (DEBUG)
            Log.v(TAG, "onActivityResult(requestCode" + requestCode + ", resultCode" + resultCode
                    + ", data" + data + ")");
        String[] arrayExtra = data.getStringArrayExtra(AlbumListActivity.EXTRA_PICKED_PICTURES);
        Toast.makeText(this, "已选择了" + (arrayExtra != null ? arrayExtra.length : 0) + "张图片",
                Toast.LENGTH_LONG).show();
        File cacheDir = Utils.getDiskCacheDir(this, "image_upload");
        for (String path : arrayExtra) {
            File file = new File(path);
            try {
                Log.v(TAG, path + ", size=" + FileSize.setFile(file).getSize());
                String zipImage = ImageZip.zipImage(path, cacheDir);
                Log.v(TAG, zipImage + ", size=" + FileSize.setFile(zipImage).getSize());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
