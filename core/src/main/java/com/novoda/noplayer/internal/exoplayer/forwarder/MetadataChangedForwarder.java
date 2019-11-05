package com.novoda.noplayer.internal.exoplayer.forwarder;

import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.MetadataOutput;
import com.novoda.noplayer.metadata.MetadataParser;
import com.novoda.noplayer.NoPlayer;

class MetadataChangedForwarder implements MetadataOutput {

    private final NoPlayer.MetadataChangedListener metadataChangedListener;
    private final MetadataParser metadataParser;

    MetadataChangedForwarder(NoPlayer.MetadataChangedListener metadataChangedListener, MetadataParser metadataParser) {
        this.metadataChangedListener = metadataChangedListener;
        this.metadataParser = metadataParser;
    }

    @Override
    public void onMetadata(Metadata metadata) {
        if (metadata != null) {
            metadataChangedListener.onMetadataChanged(metadataParser.parseMetadata(metadata));
        }
    }
}
