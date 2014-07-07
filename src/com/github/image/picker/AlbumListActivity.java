package com.github.image.picker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.appunity.image.picker.R;
import com.github.image.picker.adapter.ImageBucketAdapter;
import com.github.image.picker.pojo.ImageBucket;
import com.github.image.picker.util.AlbumHelper;

public class AlbumListActivity extends Activity {
    private static final String TAG = "AlbumListActivity";
    private static final boolean DEBUG = true;
    

    protected static final String EXTRA_IMAGE_LIST = "com.github.image.picker.AlbumListActivity.EXTRA_IMAGE_LIST";
    public static final String ACTION_PICK = "com.github.image.picker.AlbumListActivity.ACTION_PICK";
    public static final String ACTION_MULTIPLE_PICK = "com.github.image.picker.AlbumListActivity.ACTION_MULTIPLE_PICK";
    public static final String EXTRA_MAX_NUMBER_OF_PICTURES = "com.github.image.picker.AlbumListActivity.EXTRA_MAX_NUMBER_OF_PICTURES";
    public static final String EXTRA_PICKED_NUMBER_OF_PICTURES = "com.github.image.picker.AlbumListActivity.EXTRA_PICKED_NUMBER_OF_PICTURES";
    public static final String EXTRA_PICKED_PICTURES = "com.github.image.picker.AlbumListActivity.EXTRA_PICKED_PICTURES";

    private AlbumHelper mAlbumHelper;
    private GridView mGridView;
    private ImageBucketAdapter mAdatper;
    private List<ImageBucket> mAlbumList;
    // 最多允许选择的图片数量
    private int mMaxNumber;
    private ArrayList<String> mPickedList;
//    private HeadNavigateView head_view;
    public  static Bitmap thumb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (DEBUG) Log.v(TAG, "onCreate(savedInstanceState)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_list);
        mPickedList = new ArrayList<String>();
        thumb = BitmapFactory.decodeResource(getResources(),
                R.drawable.icon_addpic_unfocused);
//        head_view = (HeadNavigateView) findViewById(R.id.head_view);
//        head_view.getBtn_left().setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        
//        head_view.getBtn_right().setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        mMaxNumber = getIntent().getIntExtra(EXTRA_MAX_NUMBER_OF_PICTURES, 1);

        mAlbumHelper = AlbumHelper.getHelper();
        mAlbumHelper.init(getApplicationContext());
        mGridView = (GridView) findViewById(R.id.activity_album_list_gridView);
        mAlbumList = mAlbumHelper.getImagesBucketList(false);
        mAdatper = new ImageBucketAdapter(AlbumListActivity.this, mAlbumList);
        mGridView.setAdapter(mAdatper);

        mGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                Intent intent = new Intent(AlbumListActivity.this,
                        ImagePickerActivity.class);
                intent.putExtra(AlbumListActivity.EXTRA_IMAGE_LIST,
                        (Serializable) mAlbumList.get(position).imageList);
                intent.putExtra(EXTRA_MAX_NUMBER_OF_PICTURES, mMaxNumber);
                intent.putExtra(EXTRA_PICKED_NUMBER_OF_PICTURES,
                        mPickedList.size());
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (DEBUG) Log.v(TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (DEBUG)
            Log.v(TAG, "onActivityResult(requestCode" + requestCode
                    + ", resultCode" + resultCode + ", data" + data + ")");
        String[] arrayExtra = data.getStringArrayExtra(EXTRA_PICKED_PICTURES);
        if (arrayExtra !=null) {
            if (arrayExtra.length<1) {
                return;
            }
            mPickedList.addAll(Arrays.asList(arrayExtra));
            if (DEBUG) Log.v(TAG, Arrays.toString(arrayExtra));
            setResult();
            finish();
        }
    }

    private void setResult() {
        Intent intent = new Intent();
        intent.putExtra(AlbumListActivity.EXTRA_PICKED_PICTURES,
                mPickedList.toArray(new String[mPickedList.size()]));
        setResult(0, intent);
    }
}
