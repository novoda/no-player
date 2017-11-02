package com.novoda.demo;

import android.support.annotation.NonNull;

import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.drm.DrmType;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Videos implements Iterable<Video> {

    private static final String EXAMPLE_MODULAR_LICENSE_SERVER_PROXY = "https://proxy.uat.widevine.com/proxy?provider=widevine_test";
    private static final DataPostingModularDrm DRM_HANDLER = new DataPostingModularDrm(EXAMPLE_MODULAR_LICENSE_SERVER_PROXY);

    private final List<Video> videos = Arrays.asList(
            Video.from("MP4", "http://yt-dash-mse-test.commondatastorage.googleapis.com/media/car-20120827-85.mp4", ContentType.HLS),
            Video.from("MPD", "https://storage.googleapis.com/content-samples/multi-audio/manifest.mpd", ContentType.DASH),
            Video.from("widevine", "https://storage.googleapis.com/wvmedia/cenc/h264/tears/tears.mpd", ContentType.DASH, DrmType.WIDEVINE_MODULAR_STREAM, DRM_HANDLER)
    );

    public static Videos getInstance() {
        return LazySingleton.INSTANCE;
    }

    public Video get(int position) {
        return videos.get(position);
    }

    private static class LazySingleton {

        private static final Videos INSTANCE = new Videos();
    }

    @NonNull
    @Override
    public Iterator<Video> iterator() {
        return videos.iterator();
    }
}
