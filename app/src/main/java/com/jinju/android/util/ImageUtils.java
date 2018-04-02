package com.jinju.android.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.widget.ImageView;

import com.jinju.android.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public final class ImageUtils {

    public ImageUtils() {
        super();
    }

    /**
     * 图片缓存SD卡和内存中
     */
    private static DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            //将图片不加以压缩的方式，显示到手机端
            .imageScaleType(ImageScaleType.NONE)//设置图片以如何的编码方式显示
            .bitmapConfig(Bitmap.Config.RGB_565)
            .resetViewBeforeLoading(true)
            .displayer(new RoundedBitmapDisplayer(20))
            .displayer(new SimpleBitmapDisplayer())
            .build();

    /**
     * 发现页图片缓存SD卡和内存中
     */
    private static DisplayImageOptions discoverOptions = new DisplayImageOptions.Builder()
            //设置图片在下载期间显示的图片
            .showImageOnLoading(R.mipmap.bg_discover_top)
            //设置图片Uri为空或是错误的时候显示的图片
            .showImageForEmptyUri(R.mipmap.bg_discover_top)
            //设置图片加载/解码过程中错误时候显示的图片
            .showImageOnFail(R.mipmap.bg_discover_top)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            //将图片不加以压缩的方式，显示到手机端
            .imageScaleType(ImageScaleType.NONE)//设置图片以如何的编码方式显示
            .bitmapConfig(Bitmap.Config.RGB_565)
            .resetViewBeforeLoading(true)
            .displayer(new RoundedBitmapDisplayer(20))
            .displayer(new SimpleBitmapDisplayer())
            .build();

    private static DisplayImageOptions getOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                //设置图片在下载期间显示的图片
//				.showImageOnLoading(R.mipmap.ic_launcher)
                //设置图片Uri为空或是错误的时候显示的图片
//				.showImageForEmptyUri(R.mipmap.ic_launcher)
                //设置图片加载/解码过程中错误时候显示的图片
//				.showImageOnFail(R.mipmap.ic_launcher)
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.NONE)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
//				.decodingOptions(android.graphics.BitmapFactory.Options decodingOptions)//设置图片的解码配置
                //.delayBeforeLoading(int delayInMillis)//int delayInMillis为你设置的下载前的延迟时间
                //设置图片加入缓存前，对bitmap进行设置
                //.preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(300))//是否图片加载好后渐入的动画时间
                .build();
        return options;
    }

    /**
     * 缓存在内存中
     */
    private static DisplayImageOptions cacheInMemoryOptions = new DisplayImageOptions.Builder()
            .cacheInMemory(true)//缓存在内存中
            .cacheOnDisk(false)//不缓存在SD卡
            .considerExifParams(true)
//			.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
            .imageScaleType(ImageScaleType.NONE)//设置图片以如何的编码方式显示
            .bitmapConfig(Bitmap.Config.RGB_565)
            .resetViewBeforeLoading(true)
            .displayer(new RoundedBitmapDisplayer(20))
            .displayer(new SimpleBitmapDisplayer())
            .build();


    public static void displayImage(ImageView imageView, String uri) {
        ImageLoader.getInstance().displayImage(uri, imageView, options);
    }

    public static void discoverDisplayImage(ImageView imageView, String uri) {
        ImageLoader.getInstance().displayImage(uri, imageView, discoverOptions);
    }

    public static void noCacheDisplayImage(ImageView imageView, String uri) {
        ImageLoader.getInstance().displayImage(uri, imageView, cacheInMemoryOptions);
    }

    public static void noCacheDisplayImage(ImageView imageView, String uri, ImageLoadingListener listener) {
        ImageLoader.getInstance().displayImage(uri, imageView, cacheInMemoryOptions, listener);
    }

    public static void displayImage(ImageView imageView, String uri, ImageLoadingListener listener) {
        ImageLoader.getInstance().displayImage(uri, imageView, options, listener);
    }

    public static void displayImage(ImageView imageView, String uri, ImageLoadingListener listener, ImageLoadingProgressListener progressListener) {
        ImageLoader.getInstance().displayImage(uri, imageView, options, listener, progressListener);
    }

    public static void loadImage(String uri, ImageLoadingListener listener) {
        ImageLoader.getInstance().loadImage(uri, listener);
    }


    public static Bitmap toRoundCorner(Bitmap bitmap, int corner) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        int color = 0xff424242;
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, width, height);
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, corner, corner, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return result;
    }

    public static String compressImage(Context context, String filePath, int quality) {
        String result = context.getFilesDir().toString() + "/temp/upload.jpg";
        Bitmap bitmap = loadAndAdjustBitmap(filePath);
        saveBitmap(bitmap, result, quality);
        bitmap.recycle();
        return result;
    }

    public static Bitmap loadAndAdjustBitmap(String filePath) {
        Bitmap bitmap = loadBitmap(filePath);
        if (bitmap == null)
            return null;

        int degree = readPictureDegree(filePath);
        if (degree == 0)
            return bitmap;

        Bitmap adjustBitmap = rotateBitmap(bitmap, degree);
        bitmap.recycle();
        return adjustBitmap;
    }

    public static Bitmap loadBitmap(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int maxLength = Math.max(options.outWidth, options.outHeight);

        int scale = 1;
        while (maxLength / scale > 1024)
            scale *= 2;

        Bitmap bitmap = null;
        options.inJustDecodeBounds = false;
        options.inSampleSize = scale;

        try {
            bitmap = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public static boolean saveBitmap(Bitmap bitmap, String filePath, int quality) {
        int index = filePath.lastIndexOf("/");
        File fileDir = new File(filePath.substring(0, index));
        if (!fileDir.exists() && !fileDir.mkdirs())
            return false;

        try {
            File file = new File(filePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file, false);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, float angle) {
        double radians = Math.toRadians(angle);
        int srcWidth = bitmap.getWidth();
        int srcHeight = bitmap.getHeight();
        double scaleX = Math.abs(Math.cos(radians));
        double scaleY = Math.abs(Math.sin(radians));
        int destWidth = (int) (srcWidth * scaleX + srcHeight * scaleY);
        int destHeight = (int) (srcWidth * scaleY + srcHeight * scaleX);
        Bitmap result = Bitmap.createBitmap(destWidth, destHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.rotate(angle, destWidth / 2.0f, destHeight / 2.0f);
        Paint paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bitmap, (destWidth - srcWidth) / 2.0f, (destHeight - srcHeight) / 2.0f, paint);
        return result;
    }

    public static int readPictureDegree(String filePath) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(filePath);
            String tag = ExifInterface.TAG_ORIENTATION;
            int value = ExifInterface.ORIENTATION_NORMAL;
            int orientation = exifInterface.getAttributeInt(tag, value);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

}