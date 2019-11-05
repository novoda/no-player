package com.novoda.noplayer.metadata;

import com.google.android.exoplayer2.metadata.emsg.EventMessage;
import com.google.android.exoplayer2.metadata.id3.BinaryFrame;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MetadataParser {

    private static final Charset UTF8 = Charset.forName("UTF-8");

    public Metadata parseMetadata(com.google.android.exoplayer2.metadata.Metadata metadata) {
        if (metadata == null) {
            return null;
        }

        List<Metadata.Entry> entries = new ArrayList<>();
        for (int i = 0; i < metadata.length(); i++) {
            com.google.android.exoplayer2.metadata.Metadata.Entry entry = metadata.get(i);
            if (entry instanceof BinaryFrame) {
                String data = new String(((BinaryFrame) entry).data, UTF8);
                String id = ((BinaryFrame) entry).id;
                entries.add(new Metadata.BinaryFrame(id, data));
            } else if (entry instanceof EventMessage) {
                String schemeIdUri = ((EventMessage) entry).schemeIdUri;
                String messageData = new String(((EventMessage) entry).messageData, UTF8);
                entries.add(new Metadata.EventMessage(schemeIdUri, messageData));
            }
        }

        return new Metadata(entries);
    }
}
