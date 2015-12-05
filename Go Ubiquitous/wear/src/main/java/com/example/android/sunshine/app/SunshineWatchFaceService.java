package com.example.android.sunshine.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.WindowInsets;

import com.example.android.sunshine.weathericonlibrary.LibraryUtility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

public class SunshineWatchFaceService extends CanvasWatchFaceService {
    private static final String TAG = "SunshineWatchFaceSvc";

    private static final Typeface NORMAL_TYPEFACE = Typeface.create("sans-serif-thin", Typeface.NORMAL);
    private static final Typeface BOLD_TYPEFACE = Typeface.create("sans-serif-thin", Typeface.BOLD);

    /**
     * Update rate in milliseconds. We update every minute.
     */
    private static final long UPDATE_RATE_MS = 60000;

    @Override
    public Engine onCreateEngine() {
        return new Engine();
    }

    private class Engine extends CanvasWatchFaceService.Engine implements DataApi.DataListener,
            GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

        static final int MSG_UPDATE_TIME = 0;

        /** How often {@link #mUpdateTimeHandler} ticks in milliseconds. */
        long mInteractiveUpdateRateMs = UPDATE_RATE_MS;

        /** Handler to update the time periodically in interactive mode. */
        final Handler mUpdateTimeHandler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                switch (message.what) {
                    case MSG_UPDATE_TIME:
                        if (Log.isLoggable(TAG, Log.VERBOSE)) {
                            Log.v(TAG, "updating time");
                        }
                        invalidate();
                        if (shouldTimerBeRunning()) {
                            long timeMs = System.currentTimeMillis();
                            long delayMs =
                                    mInteractiveUpdateRateMs - (timeMs % mInteractiveUpdateRateMs);
                            mUpdateTimeHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, delayMs);
                        }
                        break;
                }
            }
        };

        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(SunshineWatchFaceService.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();

        /**
         * Handles time zone and locale changes.
         */
        final BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mCalendar.setTimeZone(TimeZone.getDefault());
                initFormats();
                invalidate();
            }
        };

        /**
         * Unregistering an unregistered receiver throws an exception. Keep track of the
         * registration state to prevent that.
         */
        boolean mRegisteredReceiver = false;

        boolean mIsRound;
        Paint mBackgroundPaint;
        Paint mOvalPaint;

        Paint mTimePaint;
        Paint mMeridiemPaint;
        Paint mDayOfWeekPaint;
        Paint mHighPaint;
        Paint mLowPaint;

        boolean mAmbient;

        Calendar mCalendar;
        Date mDate;
        SimpleDateFormat mTimeFormat;
        SimpleDateFormat mDayOfWeekFormat;
        SimpleDateFormat mDateFormat;

        String mAmString;
        String mPmString;

        String mWeatherHigh;
        String mWeatherLow;
        Bitmap mWeatherIcon;

        float mDP = getResources().getDisplayMetrics().density;

        int mInteractiveBackgroundColor = SunshineWatchFaceUtil.COLOR_VALUE_DEFAULT_AND_AMBIENT_BACKGROUND;
        int mInteractiveTimeDigitsColor = SunshineWatchFaceUtil.COLOR_VALUE_INTERACTIVE_TIME_DIGITS;
        int mInteractiveTimeMeridiemColor = SunshineWatchFaceUtil.COLOR_VALUE_INTERACTIVE_TIME_MERIDIEM;
        int mInteractiveOvalColor = SunshineWatchFaceUtil.COLOR_VALUE_DEFAULT_AND_AMBIENT_OVAL;
        int mInteractiveHighColor = SunshineWatchFaceUtil.COLOR_VALUE_DEFAULT_AND_AMBIENT_HIGH_TEMP;
        int mInteractiveLowColor = SunshineWatchFaceUtil.COLOR_VALUE_DEFAULT_AND_AMBIENT_LOW_TEMP;

        /**
         * Whether the display supports fewer bits for each color in ambient mode. When true, we
         * disable anti-aliasing in ambient mode.
         */
        boolean mLowBitAmbient;

        @Override
        public void onCreate(SurfaceHolder holder) {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "onCreate");
            }
            super.onCreate(holder);

            setWatchFaceStyle(new WatchFaceStyle.Builder(SunshineWatchFaceService.this)
                    .setCardPeekMode(WatchFaceStyle.PEEK_MODE_VARIABLE)
                    .setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
                    .setShowSystemUiTime(false)
                    .build());

            Resources resources = SunshineWatchFaceService.this.getResources();
            mAmString = resources.getString(R.string.am);
            mPmString = resources.getString(R.string.pm);

            mBackgroundPaint = new Paint();
            mBackgroundPaint.setColor(mInteractiveBackgroundColor);
            mTimePaint = createTextPaint(mInteractiveTimeDigitsColor, NORMAL_TYPEFACE);
            mMeridiemPaint = createTextPaint(mInteractiveTimeMeridiemColor, NORMAL_TYPEFACE);
            mDayOfWeekPaint = createTextPaint(mInteractiveTimeMeridiemColor, BOLD_TYPEFACE);

            mOvalPaint = new Paint();
            mOvalPaint.setColor(mInteractiveOvalColor);

            mHighPaint = createTextPaint(mInteractiveHighColor, BOLD_TYPEFACE);
            mLowPaint = createTextPaint(mInteractiveLowColor, NORMAL_TYPEFACE);

            mCalendar = Calendar.getInstance();
            mDate = new Date();
            initFormats();
        }

        @Override
        public void onDestroy() {
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            super.onDestroy();
        }

        private Paint createTextPaint(int defaultInteractiveColor, Typeface typeface) {
            Paint paint = new Paint();
            paint.setColor(defaultInteractiveColor);
            paint.setTypeface(typeface);
            paint.setAntiAlias(true);
            return paint;
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "onVisibilityChanged: " + visible);
            }
            super.onVisibilityChanged(visible);

            if (visible) {
                mGoogleApiClient.connect();

                registerReceiver();

                // Update time zone and date formats, in case they changed while we weren't visible.
                mCalendar.setTimeZone(TimeZone.getDefault());
                initFormats();
            } else {
                unregisterReceiver();

                if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                    Wearable.DataApi.removeListener(mGoogleApiClient, this);
                    mGoogleApiClient.disconnect();
                }
            }

            // Whether the timer should be running depends on whether we're visible (as well as
            // whether we're in ambient mode), so we may need to start or stop the timer.
            updateTimer();
        }

        private void initFormats() {
            mTimeFormat = new SimpleDateFormat("h:mm");
            mDayOfWeekFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
            mDayOfWeekFormat.setCalendar(mCalendar);
            mDateFormat = new SimpleDateFormat("MMMM d, yyyy");
            mDateFormat.setCalendar(mCalendar);
        }

        private void registerReceiver() {
            if (mRegisteredReceiver) {
                return;
            }
            mRegisteredReceiver = true;
            IntentFilter filter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
            filter.addAction(Intent.ACTION_LOCALE_CHANGED);
            SunshineWatchFaceService.this.registerReceiver(mReceiver, filter);
        }

        private void unregisterReceiver() {
            if (!mRegisteredReceiver) {
                return;
            }
            mRegisteredReceiver = false;
            SunshineWatchFaceService.this.unregisterReceiver(mReceiver);
        }

        @Override
        public void onApplyWindowInsets(WindowInsets insets) {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "onApplyWindowInsets: " + (insets.isRound() ? "round" : "square"));
            }
            super.onApplyWindowInsets(insets);

            // Load resources that have alternate values for round watches.
            Resources resources = SunshineWatchFaceService.this.getResources();
            mIsRound = insets.isRound();

            float textSize = resources.getDimension(mIsRound
                    ? R.dimen.watchface_time_text_size_round : R.dimen.watchface_time_text_size);
            float subscriptTextSize = resources.getDimension(mIsRound
                    ? R.dimen.am_pm_size_round : R.dimen.am_pm_size);
            float highlowTextSize = resources.getDimension(mIsRound
                    ? R.dimen.high_low_size_round : R.dimen.high_low_size);

            mTimePaint.setTextSize(textSize);
            mMeridiemPaint.setTextSize(subscriptTextSize);
            mDayOfWeekPaint.setTextSize(subscriptTextSize);
            mHighPaint.setTextSize(highlowTextSize);
            mLowPaint.setTextSize(highlowTextSize);
        }

        @Override
        public void onPropertiesChanged(Bundle properties) {
            super.onPropertiesChanged(properties);

            boolean burnInProtection = properties.getBoolean(PROPERTY_BURN_IN_PROTECTION, false);
            mDayOfWeekPaint.setTypeface(burnInProtection ? NORMAL_TYPEFACE : BOLD_TYPEFACE);
            mHighPaint.setTypeface(burnInProtection ? NORMAL_TYPEFACE : BOLD_TYPEFACE);

            mLowBitAmbient = properties.getBoolean(PROPERTY_LOW_BIT_AMBIENT, false);

            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "onPropertiesChanged: burn-in protection = " + burnInProtection
                        + ", low-bit ambient = " + mLowBitAmbient);
            }
        }

        /**
         * Called when the device enters or exits ambient mode.
         */
        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "onAmbientModeChanged: " + inAmbientMode);
            }

            adjustPaintColorToCurrentMode(mBackgroundPaint, mInteractiveBackgroundColor,
                    SunshineWatchFaceUtil.COLOR_VALUE_DEFAULT_AND_AMBIENT_BACKGROUND);
            adjustPaintColorToCurrentMode(mTimePaint, mInteractiveTimeDigitsColor,
                    SunshineWatchFaceUtil.COLOR_VALUE_AMBIENT_TIME_DIGITS);
            adjustPaintColorToCurrentMode(mMeridiemPaint, mInteractiveTimeMeridiemColor,
                    SunshineWatchFaceUtil.COLOR_VALUE_AMBIENT_TIME_MERIDIEM);
            adjustPaintColorToCurrentMode(mDayOfWeekPaint, mInteractiveTimeMeridiemColor,
                    SunshineWatchFaceUtil.COLOR_VALUE_AMBIENT_TIME_MERIDIEM);
            adjustPaintColorToCurrentMode(mOvalPaint, mInteractiveOvalColor,
                    SunshineWatchFaceUtil.COLOR_VALUE_DEFAULT_AND_AMBIENT_OVAL);
            adjustPaintColorToCurrentMode(mHighPaint, mInteractiveHighColor,
                    SunshineWatchFaceUtil.COLOR_VALUE_DEFAULT_AND_AMBIENT_HIGH_TEMP);
            adjustPaintColorToCurrentMode(mLowPaint, mInteractiveLowColor,
                    SunshineWatchFaceUtil.COLOR_VALUE_DEFAULT_AND_AMBIENT_LOW_TEMP);

            if (mAmbient != inAmbientMode) {
                mAmbient = inAmbientMode;

                mDayOfWeekPaint.setTypeface(mAmbient ? NORMAL_TYPEFACE : BOLD_TYPEFACE);

                if (mLowBitAmbient) {
                    boolean antiAlias = !inAmbientMode;
                    mTimePaint.setAntiAlias(antiAlias);
                    mMeridiemPaint.setAntiAlias(antiAlias);
                    mDayOfWeekPaint.setAntiAlias(antiAlias);
                    mHighPaint.setAntiAlias(antiAlias);
                    mLowPaint.setAntiAlias(antiAlias);
                }
                invalidate();
            }

            // Whether the timer should be running depends on whether we're in ambient mode (as well
            // as whether we're visible), so we may need to start or stop the timer.
            updateTimer();
        }

        private void adjustPaintColorToCurrentMode(Paint paint, int interactiveColor,
                                                   int ambientColor) {
            paint.setColor(isInAmbientMode() ? ambientColor : interactiveColor);
        }

        private String getAmPmString(int amPm) {
            return amPm == Calendar.AM ? mAmString : mPmString;
        }

        /**
         * Draws the watch face.
         */
        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            long now = System.currentTimeMillis();
            mCalendar.setTimeInMillis(now);
            mDate.setTime(now);

            // Draw the background.
            if (mAmbient) {
                canvas.drawColor(Color.BLACK);
            } else {
                canvas.drawRect(0, 0, bounds.width(), bounds.height(), mBackgroundPaint);
            }

            // Draw the current time
            String timeString = mTimeFormat.format(mDate);
            float timeTextLen = mTimePaint.measureText(timeString);

            String meridiemString = getAmPmString(mCalendar.get(Calendar.AM_PM));
            float paddingBetweenTimeAndMeridiem = (mDP * 4);
            float meridiemTextLen = mMeridiemPaint.measureText(meridiemString);

            float xOffset = (bounds.width() - timeTextLen - paddingBetweenTimeAndMeridiem - meridiemTextLen) / 2;
            float yOffset = (mDP * (mIsRound ? 65 : 50));

            canvas.drawText(mTimeFormat.format(mDate), xOffset, yOffset, mTimePaint);

            // Draw the AM / PM meridiem indicator
            xOffset += timeTextLen + paddingBetweenTimeAndMeridiem;
            canvas.drawText(meridiemString, xOffset, yOffset, mMeridiemPaint);

            // Draw the Day of week
            String dayOfWeekString = mDayOfWeekFormat.format(mDate).toUpperCase();
            float dayOfWeekTextLen = mDayOfWeekPaint.measureText(dayOfWeekString);
            xOffset = (bounds.width() - dayOfWeekTextLen) / 2;
            yOffset += (mDP * 25);

            canvas.drawText(dayOfWeekString, xOffset, yOffset, mDayOfWeekPaint);

            // Draw the literal date
            String dateString = mDateFormat.format(mDate).toUpperCase();
            float dateTextLen = mMeridiemPaint.measureText(dateString);
            xOffset = (bounds.width() - dateTextLen) / 2;
            yOffset += (mDP * 25);
            canvas.drawText(dateString, xOffset, yOffset, mMeridiemPaint);

            yOffset += (mDP * 15);

            if (!mAmbient) {
                // Draw a blue oval for decorative effect
                float halfWidth = (bounds.width() / 2);
                canvas.drawOval(new RectF(halfWidth * -1, yOffset, bounds.width() + halfWidth, bounds.height() + halfWidth), mOvalPaint);

                // Draw high and low temp if we have it
                if (mWeatherHigh != null && mWeatherLow != null && mWeatherIcon != null) {
                    // High
                    String highString = mWeatherHigh;
                    float highTextLen = mHighPaint.measureText(highString);

                    xOffset = bounds.centerX() + ((bounds.centerX() - highTextLen) / 4);
                    yOffset += (mDP * (mIsRound ? 35 : 30));
                    canvas.drawText(mWeatherHigh, xOffset, yOffset, mHighPaint);

                    // Low
                    yOffset += (mDP * 25);
                    canvas.drawText(mWeatherLow, xOffset, yOffset, mLowPaint);

                    xOffset = (bounds.centerX() - mWeatherIcon.getWidth());
                    yOffset -= (mDP * 35);
                    canvas.drawBitmap(mWeatherIcon, xOffset, yOffset, null);
                }
            }
        }

        /**
         * Starts the {@link #mUpdateTimeHandler} timer if it should be running and isn't currently
         * or stops it if it shouldn't be running but currently is.
         */
        private void updateTimer() {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "updateTimer");
            }
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            if (shouldTimerBeRunning()) {
                mUpdateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
            }
        }

        /**
         * Returns whether the {@link #mUpdateTimeHandler} timer should be running. The timer should
         * only run when we're visible and in interactive mode.
         */
        private boolean shouldTimerBeRunning() {
            return isVisible() && !isInAmbientMode();
        }

        @Override // DataApi.DataListener
        public void onDataChanged(DataEventBuffer dataEvents) {
            Log.d(TAG, "Wearable onDataChanged");
            for (DataEvent dataEvent : dataEvents) {
                if (dataEvent.getType() != DataEvent.TYPE_CHANGED) {
                    continue;
                }

                DataItem dataItem = dataEvent.getDataItem();
                if (!dataItem.getUri().getPath().equals("/weather")) {
                    continue;
                }
                Log.d(TAG, "WEAR /weather");

                DataMapItem dataMapItem = DataMapItem.fromDataItem(dataItem);
                DataMap dataMap = dataMapItem.getDataMap();

                if (dataMap.containsKey("high")) {
                    mWeatherHigh = dataMap.getString("high");
                }

                if (dataMap.containsKey("low")) {
                    mWeatherLow = dataMap.getString("low");
                }

                if (dataMap.containsKey("weatherId")) {
                    int weatherId = dataMap.getInt("weatherId");
                    Drawable b = getResources().getDrawable(LibraryUtility.getIconResourceForWeatherCondition(weatherId));
                    Bitmap icon = ((BitmapDrawable) b).getBitmap();
                    float scaledWidth = (mTimePaint.getTextSize() / icon.getHeight()) * icon.getWidth();
                    mWeatherIcon = Bitmap.createScaledBitmap(icon, (int) scaledWidth, (int) mTimePaint.getTextSize(), true);
                }

                invalidate();
            }
        }

        /**
         * After calling connect(), this method will be invoked asynchronously when the connect request
         * has successfully completed. After this callback, the application can make requests on other
         * methods provided by the client and expect that no user intervention is required to call
         * methods that use account and scopes provided to the client constructor.
         */
        @Override  // GoogleApiClient.ConnectionCallbacks
        public void onConnected(Bundle connectionHint) {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "onConnected: " + connectionHint);
            }

            requestWeatherInfo();
            Wearable.DataApi.addListener(mGoogleApiClient, Engine.this);
        }

        @Override  // GoogleApiClient.ConnectionCallbacks
        public void onConnectionSuspended(int cause) {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "onConnectionSuspended: " + cause);
            }
        }

        @Override  // GoogleApiClient.OnConnectionFailedListener
        public void onConnectionFailed(ConnectionResult result) {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "onConnectionFailed: " + result);
            }
        }

        public void requestWeatherInfo() {
            PutDataMapRequest putDataMapRequest = PutDataMapRequest.create("/weather");
            putDataMapRequest.getDataMap().putString("uuid", UUID.randomUUID().toString());
            PutDataRequest request = putDataMapRequest.asPutDataRequest();

            Wearable.DataApi.putDataItem(mGoogleApiClient, request)
                .setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                    @Override
                    public void onResult(DataApi.DataItemResult dataItemResult) {
                        if (!dataItemResult.getStatus().isSuccess()) {
                            Log.d(TAG, "Failed asking phone for weather data");
                        } else {
                            Log.d(TAG, "Successfully asked for weather data");
                        }
                    }
                });
        }
    }
}