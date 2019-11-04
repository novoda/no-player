package com.novoda.noplayer.internal.exoplayer.forwarder;

import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.MetadataOutput;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ExoPlayerMetadataListener implements MetadataOutput {

    private final List<MetadataOutput> listeners = new CopyOnWriteArrayList<>();

    public void add(MetadataOutput listener) {
        listeners.add(listener);
    }

    public void remove(MetadataOutput listener) {
        listeners.remove(listener);
    }

    @Override
    public void onMetadata(Metadata metadata) {
        for(MetadataOutput listener : listeners) {
            listener.onMetadata(metadata);
        }
    }
}
