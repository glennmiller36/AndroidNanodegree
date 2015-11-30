package com.example.android.sunshine.app;

import android.graphics.Color;

/**
 * Created by MillerMac on 11/24/15.
 */
public final class SunshineWatchFaceUtil {

//    private static final String TAG = "SunshineWatchFaceUtil";

    /**
     * The {@link DataMap} key for {@link SunshineWatchFaceService} background color name.
     * The color name must be a {@link String} recognized by {@link Color#parseColor}.
     */
//    public static final String KEY_BACKGROUND_COLOR = "BACKGROUND_COLOR";

    /**
     * The {@link DataMap} key for {@link SunshineWatchFaceService} hour digits color name.
     * The color name must be a {@link String} recognized by {@link Color#parseColor}.
     */
//    public static final String KEY_HOURS_COLOR = "HOURS_COLOR";

    /**
     * The {@link DataMap} key for {@link SunshineWatchFaceService} minute digits color name.
     * The color name must be a {@link String} recognized by {@link Color#parseColor}.
     */
//    public static final String KEY_MINUTES_COLOR = "MINUTES_COLOR";

    /**
     * The {@link DataMap} key for {@link SunshineWatchFaceService} second digits color name.
     * The color name must be a {@link String} recognized by {@link Color#parseColor}.
     */
//    public static final String KEY_SECONDS_COLOR = "SECONDS_COLOR";

    /**
     * The path for the {@link DataItem} containing {@link SunshineWatchFaceService} configuration.
     */
//    public static final String PATH_WITH_FEATURE = "/watch_face_config/Sunshine";

    /**
     * Name of the default interactive mode background color and the ambient mode background color.
     */
    public static final String COLOR_NAME_DEFAULT_AND_AMBIENT_BACKGROUND = "Blue";
    public static final int COLOR_VALUE_DEFAULT_AND_AMBIENT_BACKGROUND =
            parseColor(COLOR_NAME_DEFAULT_AND_AMBIENT_BACKGROUND);

    /**
     * Name of the default interactive mode time digits color and the ambient mode time digits
     * color.
     */
    public static final String COLOR_NAME_DEFAULT_AND_AMBIENT_TIME_DIGITS = "Black";
    public static final int COLOR_VALUE_DEFAULT_AND_AMBIENT_TIME_DIGITS =
            parseColor(COLOR_NAME_DEFAULT_AND_AMBIENT_TIME_DIGITS);

    /**
     * Name of the default interactive mode minute digits color and the ambient mode minute digits
     * color.
     */
//    public static final String COLOR_NAME_DEFAULT_AND_AMBIENT_MINUTE_DIGITS = "White";
//    public static final int COLOR_VALUE_DEFAULT_AND_AMBIENT_MINUTE_DIGITS =
//            parseColor(COLOR_NAME_DEFAULT_AND_AMBIENT_MINUTE_DIGITS);

    /**
     * Name of the default interactive mode second digits color and the ambient mode second digits
     * color.
     */
//    public static final String COLOR_NAME_DEFAULT_AND_AMBIENT_SECOND_DIGITS = "Gray";
//    public static final int COLOR_VALUE_DEFAULT_AND_AMBIENT_SECOND_DIGITS =
//            parseColor(COLOR_NAME_DEFAULT_AND_AMBIENT_SECOND_DIGITS);

    /**
     * Callback interface to perform an action with the current config {@link DataMap} for
     * {@link SunshineWatchFaceService}.
     */
//    public interface FetchConfigDataMapCallback {
//        /**
//         * Callback invoked with the current config {@link DataMap} for
//         * {@link SunshineWatchFaceService}.
//         */
//        void onConfigDataMapFetched(DataMap config);
//    }

    private static int parseColor(String colorName) {
        return Color.parseColor(colorName.toLowerCase());
    }

    /**
     * Asynchronously fetches the current config {@link DataMap} for {@link SunshineWatchFaceService}
     * and passes it to the given callback.
     * <p>
     * If the current config {@link DataItem} doesn't exist, it isn't created and the callback
     * receives an empty DataMap.
     */
//    public static void fetchConfigDataMap(final GoogleApiClient client,
//                                          final FetchConfigDataMapCallback callback) {
//        Wearable.NodeApi.getLocalNode(client).setResultCallback(
//                new ResultCallback<NodeApi.GetLocalNodeResult>() {
//                    @Override
//                    public void onResult(NodeApi.GetLocalNodeResult getLocalNodeResult) {
//                        String localNode = getLocalNodeResult.getNode().getId();
//                        Uri uri = new Uri.Builder()
//                                .scheme("wear")
//                                .path(SunshineWatchFaceUtil.PATH_WITH_FEATURE)
//                                .authority(localNode)
//                                .build();
//                        Wearable.DataApi.getDataItem(client, uri)
//                                .setResultCallback(new DataItemResultCallback(callback));
//                    }
//                }
//        );
//    }

    /**
     * Overwrites (or sets, if not present) the keys in the current config {@link DataItem} with
     * the ones appearing in the given {@link DataMap}. If the config DataItem doesn't exist,
     * it's created.
     * <p>
     * It is allowed that only some of the keys used in the config DataItem appear in
     * {@code configKeysToOverwrite}. The rest of the keys remains unmodified in this case.
     */
//    public static void overwriteKeysInConfigDataMap(final GoogleApiClient googleApiClient,
//                                                    final DataMap configKeysToOverwrite) {
//
//        SunshineWatchFaceUtil.fetchConfigDataMap(googleApiClient,
//                new FetchConfigDataMapCallback() {
//                    @Override
//                    public void onConfigDataMapFetched(DataMap currentConfig) {
//                        DataMap overwrittenConfig = new DataMap();
//                        overwrittenConfig.putAll(currentConfig);
//                        overwrittenConfig.putAll(configKeysToOverwrite);
//                        SunshineWatchFaceUtil.putConfigDataItem(googleApiClient, overwrittenConfig);
//                    }
//                }
//        );
//    }

    /**
     * Overwrites the current config {@link DataItem}'s {@link DataMap} with {@code newConfig}.
     * If the config DataItem doesn't exist, it's created.
     */
//    public static void putConfigDataItem(GoogleApiClient googleApiClient, DataMap newConfig) {
//        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(PATH_WITH_FEATURE);
//        DataMap configToPut = putDataMapRequest.getDataMap();
//        configToPut.putAll(newConfig);
//        Wearable.DataApi.putDataItem(googleApiClient, putDataMapRequest.asPutDataRequest())
//                .setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
//                    @Override
//                    public void onResult(DataApi.DataItemResult dataItemResult) {
//                        if (Log.isLoggable(TAG, Log.DEBUG)) {
//                            Log.d(TAG, "putDataItem result status: " + dataItemResult.getStatus());
//                        }
//                    }
//                });
//    }

//    private static class DataItemResultCallback implements ResultCallback<DataApi.DataItemResult> {
//
//        private final FetchConfigDataMapCallback mCallback;
//
//        public DataItemResultCallback(FetchConfigDataMapCallback callback) {
//            mCallback = callback;
//        }
//
//        @Override
//        public void onResult(DataApi.DataItemResult dataItemResult) {
//            if (dataItemResult.getStatus().isSuccess()) {
//                if (dataItemResult.getDataItem() != null) {
//                    DataItem configDataItem = dataItemResult.getDataItem();
//                    DataMapItem dataMapItem = DataMapItem.fromDataItem(configDataItem);
//                    DataMap config = dataMapItem.getDataMap();
//                    mCallback.onConfigDataMapFetched(config);
//                } else {
//                    mCallback.onConfigDataMapFetched(new DataMap());
//                }
//            }
//        }
//    }

    private SunshineWatchFaceUtil() { }
}