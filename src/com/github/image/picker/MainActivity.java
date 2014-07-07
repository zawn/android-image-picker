package com.github.image.picker;

import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.appunity.image.picker.R;

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
            Log.v(TAG, "onActivityResult(requestCode" + requestCode
                    + ", resultCode" + resultCode + ", data" + data + ")");
        String[] arrayExtra = data.getStringArrayExtra(AlbumListActivity.EXTRA_PICKED_PICTURES);
        if (DEBUG) Log.v(TAG, Arrays.toString(arrayExtra));
    }
}
