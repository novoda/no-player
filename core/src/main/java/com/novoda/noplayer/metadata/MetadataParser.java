package com.novoda.noplayer.metadata;

import com.google.android.exoplayer2.metadata.emsg.EventMessage;
import com.google.android.exoplayer2.metadata.id3.BinaryFrame;

import java.util.ArrayList;
import java.util.List;

public class MetadataParser {

    public Metadata parseMetadata(com.google.android.exoplayer2.metadata.Metadata metadata) {
        List<Metadata.Entry> entries = new ArrayList<>();
        for (int i = 0; i < metadata.length(); i++) {
            com.google.android.exoplayer2.metadata.Metadata.Entry entry = metadata.get(i);
            if (entry instanceof BinaryFrame) {
                String data = new String(((BinaryFrame) entry).data);
                String id = ((BinaryFrame) entry).id;
                entries.add(new Metadata.BinaryFrame(id, data));
            } else if (entry instanceof EventMessage) {
                String schemeIdUri = ((EventMessage) entry).schemeIdUri;
                String messageData = new String(((EventMessage) entry).messageData);
                entries.add(new Metadata.EventMessage(schemeIdUri, messageData));
            }
        }

        return new Metadata(entries);
    }
}
