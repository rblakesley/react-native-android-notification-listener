package com.lesimoes.androidnotificationlistener;

import android.graphics.BitmapFactory;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.app.Notification;
import android.util.Log;
import java.util.ArrayList;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;
import java.io.ByteArrayOutputStream;
import android.util.Base64;
import android.content.Context;
import java.lang.Exception;
import android.graphics.drawable.Icon;
import android.graphics.drawable.BitmapDrawable;

public class RNNotification {
    private static final String TAG = "RNAndroidNotificationListener";
    
    protected String event;
    protected String app;
    protected String key;
    protected int id;
    protected String category;
    protected String title;
    protected String titleBig;
    protected String text;
    protected String subText;
    protected String summaryText;
    protected String bigText;
    protected ArrayList<RNGroupedNotification> groupedMessages;
    protected String audioContentsURI;
    protected String imageBackgroundURI;
    protected String extraInfoText;
    protected String icon;
    protected String image;
    protected long time;
    protected String iconLarge;
    public RNNotification(Context context, StatusBarNotification sbn, String addRemove) {

        String packageName = sbn.getPackageName();

        this.event = addRemove;
        this.app = TextUtils.isEmpty(packageName) ? "Unknown App" : packageName;
        this.time = sbn.getPostTime();
        this.key = sbn.getKey();
        this.id = sbn.getId();

        if (addRemove=="add") {
            Notification notification = sbn.getNotification();

            if (notification != null && notification.extras != null) {
                this.category = notification.category;
                this.title = this.getPropertySafely(notification, Notification.EXTRA_TITLE);
                this.titleBig = this.getPropertySafely(notification, Notification.EXTRA_TITLE_BIG);
                this.text = this.getPropertySafely(notification, Notification.EXTRA_TEXT);
                this.subText = this.getPropertySafely(notification, Notification.EXTRA_SUB_TEXT);
                this.summaryText = this.getPropertySafely(notification, Notification.EXTRA_SUMMARY_TEXT);
                this.bigText = this.getPropertySafely(notification, Notification.EXTRA_BIG_TEXT);
                this.audioContentsURI = this.getPropertySafely(notification, Notification.EXTRA_AUDIO_CONTENTS_URI);
                this.imageBackgroundURI = this.getPropertySafely(notification, Notification.EXTRA_BACKGROUND_IMAGE_URI);
                this.extraInfoText = this.getPropertySafely(notification, Notification.EXTRA_INFO_TEXT);
                this.iconLarge = this.getNotificationLargeIcon(context, notification);
                this.icon = this.getNotificationIcon(context, notification);
                this.image = this.getNotificationImage(notification);
                this.groupedMessages = this.getGroupedNotifications(notification);
            } else {
                Log.d(TAG, "The notification received has no data");
            }
        }
    }

    private String getPropertySafely(Notification notification, String propKey) {
        try {
            CharSequence propCharSequence = notification.extras.getCharSequence(propKey);

            return propCharSequence == null ? "" : propCharSequence.toString().trim();
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return "";
        }
    }

    private ArrayList<RNGroupedNotification> getGroupedNotifications(Notification notification) {
        ArrayList<RNGroupedNotification> result = new ArrayList<RNGroupedNotification>();

        try {
            CharSequence[] lines = notification.extras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES);

            if (lines != null && lines.length > 0) {
                for (CharSequence line : lines) {
                    if (!TextUtils.isEmpty(line)) {
                        RNGroupedNotification groupedNotification = new RNGroupedNotification(this, line);
                        result.add(groupedNotification);
                    }
                }
            }

            return result;
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return result;
        }
    }

    private String getNotificationIcon(Context context, Notification notification) {
        try {
           
            String result = "";
 
            Icon iconInstance = notification.getSmallIcon();
            Drawable iconDrawable = iconInstance.loadDrawable(context);
            Bitmap iconBitmap = ((BitmapDrawable) iconDrawable).getBitmap();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            iconBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

            result = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);

            return TextUtils.isEmpty(result) ? result : "data:image/png;base64," + result;
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return "";
        }
    }
    
    
    private String getNotificationLargeIcon(Context context, Notification notification) {
        try {
            
            String result = "";
 
            Icon iconInstance = notification.getLargeIcon();
            Drawable iconDrawable = iconInstance.loadDrawable(context);
            Bitmap iconBitmap = ((BitmapDrawable) iconDrawable).getBitmap();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            iconBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

            result = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);

            return TextUtils.isEmpty(result) ? result : "data:image/png;base64," + result;
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return "";
        }
    }

    private String getNotificationImage(Notification notification) {
        try {
            if (!notification.extras.containsKey(Notification.EXTRA_PICTURE)) return "";

            Bitmap imageBitmap = (Bitmap) notification.extras.get(Notification.EXTRA_PICTURE);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = this.calculateInSampleSize(options, 100,100);
            options.inJustDecodeBounds = false;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 30, outputStream);

            String result = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);

            return TextUtils.isEmpty(result) ? result : "data:image/png;base64," + result;
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return "";
        }
    }

    public int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
