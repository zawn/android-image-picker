/**
 * File name: ImagePickerAdapter.java
 * Copyright: 2006-2014 House365 Inc, All rights reserved
 * Modified : 2014年5月26日
 */
package com.github.image.picker.adapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.appunity.image.picker.R;
import com.github.image.picker.pojo.ImageItem;
import com.github.image.picker.util.BitmapCache;
import com.github.image.picker.util.BitmapCache.ImageCallback;

/**
 * @author ZhangZhenli
 * 
 */
public class ImagePickerAdapter extends ArrayAdapter<ImageItem> {

    private static final boolean DEBUG = true;
    private static final String TAG = "ImagePickerAdapter";

    private BitmapCache cache;
    private TextCallback mTextcallback;
    private HashSet<String> mPickSet;

    /**
     * @return mPickSet
     */
    public String[] getPicked() {
        return mPickSet.toArray(new String[mPickSet.size()]);
    }

    // 最多允许选择的图片数量
    private int mMaxNumber;
    private int mPickedNumber;
    private Context mContext;

    /**
     * @param context
     * @param resource
     * @param objects
     * @param picked
     */
    public ImagePickerAdapter(Context context, List<ImageItem> objects,
            int max, int picked, Handler handler) {
        super(context, R.layout.item_image_grid, objects);
        this.mContext = context;
        cache = new BitmapCache();
        mMaxNumber = max;
        mPickedNumber = picked;
        mPickSet = new HashSet<String>(max + 1);
        mHandler = handler;
    }

    private ImageCallback callback = new ImageCallback() {
        @Override
        public void imageLoad(ImageView imageView, Bitmap bitmap,
                Object... params) {
            if (imageView != null && bitmap != null) {
                String url = (String) params[0];
                if (url != null && url.equals((String) imageView.getTag())) {
                    ((ImageView) imageView).setImageBitmap(bitmap);
                } else {
                    Log.e(TAG, "callback, bmp not match");
                }
            } else {
                Log.e(TAG, "callback, bmp null");
            }
        }
    };
    protected Handler mHandler;

    @Override
    public View attachDataToView(int position, final ImageItem item,
            View convertView) {
        if (DEBUG) Log.v(TAG, "attachDataToView");
        final Holder holder;
        Holder tempholder = (Holder) convertView.getTag();
        if (tempholder == null) {
            holder = new Holder();
            holder.iv = (ImageView) convertView.findViewById(R.id.image);
            holder.selected = (ImageView) convertView
                    .findViewById(R.id.isselected);
            holder.text = (TextView) convertView
                    .findViewById(R.id.item_image_grid_text);
            convertView.setTag(holder);
        } else {
            holder = tempholder;
        }
        holder.iv.setTag(item.imagePath);
        cache.displayBmp(holder.iv, item.thumbnailPath, item.imagePath,
                callback);
        if (item.isSelected) {
            holder.selected.setImageResource(R.drawable.icon_data_select);
            holder.text.setBackgroundResource(R.drawable.bgd_relatly_line);
        } else {
            holder.selected.setImageResource(R.drawable.none);
            holder.text.setBackgroundColor(mContext.getResources().getColor(
                    R.color.Black));
        }
        holder.iv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String path = (String) v.getTag();
                if (item.isSelected) {
                    // 如果图片已选择,则去除.
                    item.isSelected = false;
                    holder.selected.setImageResource(R.drawable.none);
                    holder.text.setBackgroundColor(mContext.getResources()
                            .getColor(R.color.Black));
                    mPickSet.remove(path);
                    if (mTextcallback != null)
                        mTextcallback.onListen(mPickSet.size());
                } else {
                    if ((mPickedNumber + mPickSet.size()) < mMaxNumber) {
                        // 图片未选择,则选择
                        item.isSelected = true;
                        holder.selected
                                .setImageResource(R.drawable.icon_data_select);
                        holder.text
                                .setBackgroundResource(R.drawable.bgd_relatly_line);
                        mPickSet.add(path);
                        if (mTextcallback != null)
                            mTextcallback.onListen(mPickSet.size());
                    } else {
                        // 提示已经选择了足够的图片
                        if (mHandler != null) {
                            Message message = Message.obtain(mHandler, 0);
                            message.sendToTarget();
                        }
                    }
                }

            }

        });
        return convertView;
    }

    class Holder {
        private ImageView iv;
        private ImageView selected;
        private TextView text;
    }

    public static interface TextCallback {
        public void onListen(int count);
    }

    public void setTextCallback(TextCallback listener) {
        mTextcallback = listener;
    }
}
