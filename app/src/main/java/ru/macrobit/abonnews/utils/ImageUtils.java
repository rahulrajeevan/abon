package ru.macrobit.abonnews.utils;

import android.content.Context;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class ImageUtils {

    private static ImageLoader mImageLoader;

    private static volatile ImageUtils instance;

    public static ImageUtils  getInstance() {
        ImageUtils  localInstance = instance;
        if (localInstance == null) {
            synchronized (ImageUtils .class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ImageUtils();
                }
            }
        }
        return localInstance;
    }

    public static ImageLoader getUIL(Context ctx) {
        if (mImageLoader != null) {
            return mImageLoader;
        } else {
            mImageLoader = initUIL(ctx);
            return mImageLoader;
        }
    }

    private static ImageLoader initUIL(Context context) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .build();
        ImageLoaderConfiguration conf = new  ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        imageLoader.init(conf);
        return imageLoader;
    }
}
