package com.github.image.picker;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.appunity.image.picker.R;
import com.github.image.picker.adapter.ImagePickerAdapter;
import com.github.image.picker.adapter.ImagePickerAdapter.TextCallback;
import com.github.image.picker.pojo.ImageItem;
import com.github.image.picker.util.AlbumHelper;

public class ImagePickerActivity extends Activity {

    private static final String TAG = "ImagePickerActivity";
    private static final boolean DEBUG = true;

    private AlbumHelper mAlbumHelper;
    private GridView mGridView;
    private ImagePickerAdapter mAdatper;
    private List<ImageItem> mDataList;
    private Button mBtnComplete;
    // 最多允许选择的图片数量
    private int mMaxNumber;
    // 已选择的图片数量
    private int mPickedNumber;
//    private HeadNavigateView head_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (DEBUG) Log.v(TAG, "onCreate(savedInstanceState)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);

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

        mMaxNumber = getIntent().getIntExtra(
                AlbumListActivity.EXTRA_MAX_NUMBER_OF_PICTURES, 1);
        mPickedNumber = getIntent().getIntExtra(
                AlbumListActivity.EXTRA_PICKED_NUMBER_OF_PICTURES, 0);
        mDataList = (List<ImageItem>) getIntent().getSerializableExtra(
                AlbumListActivity.EXTRA_IMAGE_LIST);

        mAlbumHelper = AlbumHelper.getHelper();
        mAlbumHelper.init(getApplicationContext());

        mGridView = (GridView) findViewById(R.id.activity_image_picker_gridView);
        mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        // mAdatper = new ImageGridAdapter(ImagePickerActivity.this, mDataList,
        // mHandler);
        mAdatper = new ImagePickerAdapter(this, mDataList, mMaxNumber,
                mPickedNumber, mHandler);
        mGridView.setAdapter(mAdatper);
        mAdatper.setTextCallback(new TextCallback() {
            public void onListen(int count) {
                mBtnComplete.setText("完成" + "(" + (count + mPickedNumber) + "/"
                        + mMaxNumber + ")");
            }
        });

        mGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                mAdatper.notifyDataSetChanged();
            }

        });

        mBtnComplete = (Button) findViewById(R.id.activity_image_picker_button);
        mBtnComplete.setText("完成" + "(" + (mPickedNumber) + "/" + mMaxNumber
                + ")");
        mBtnComplete.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                setResult();
                finish();
            }

        });
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case 0:
                Toast.makeText(ImagePickerActivity.this,
                        "最多选择" + mMaxNumber + "张图片", 400).show();
                break;

            default:
                break;
            }
        }
    };

    /*
     * （非 Javadoc）
     * 
     * @see android.app.Activity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
        if (DEBUG) Log.v(TAG, "onBackPressed");
        setResult();
        super.onBackPressed();
    }

    private void setResult() {
        Intent intent = new Intent();
        intent.putExtra(AlbumListActivity.EXTRA_PICKED_PICTURES,
                mAdatper.getPicked());
        setResult(0, intent);
    }

}
