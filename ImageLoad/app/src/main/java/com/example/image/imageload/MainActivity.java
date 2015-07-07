package com.example.image.imageload;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;


public class MainActivity extends ActionBarActivity {
    private ImageView mImg;
    private String imageUrl = "http://gtms03.alicdn.com/tps/i3/TB1_O23HpXXXXXgaXXXvWhATXXX-170-280.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);
        mImg = (ImageView) findViewById(R.id.image);
        ImageSize mImgSize = new ImageSize(100, 100);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        //来源于SD卡
        String imagePath = "/mnt/sdcard/image.png";
        String imgUrl = ImageDownloader.Scheme.FILE.wrap(imagePath);
        //来源于Content provider
        String contentPrividerUrl = "content://media/external/audio/albumart13";
        //图片来源于asserts
        String assertsUrl = ImageDownloader.Scheme.ASSETS.wrap("image/png");
        //图片来源于
        String drawableUrl = ImageDownloader.Scheme.DRAWABLE.wrap("R.drawable.image");

        ImageLoader.getInstance().loadImage(imageUrl, mImgSize, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                mImg.setImageBitmap(bitmap);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        }, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String s, View view, int current, int total) {

            }
        });
    }

}
