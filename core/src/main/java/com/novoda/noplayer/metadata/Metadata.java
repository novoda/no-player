package com.novoda.noplayer.metadata;

import java.util.List;

public class Metadata {

    private final List<Entry> entries;

    public Metadata(List<Entry> entries) {
        this.entries = entries;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public interface Entry { }

    public static class BinaryFrame implements Entry {

        private final String id;
        private final String data;

        public BinaryFrame(String id, String data) {
            this.id = id;
            this.data = data;
        }

        public String getId() {
            return id;
        }

        public String getData() {
            return data;
        }
    }

    public static class EventMessage implements Entry {

        private final String schemeIdUri;
        private final String messageData;

        public EventMessage(String schemeIdUri, String messageData) {
            this.schemeIdUri = schemeIdUri;
            this.messageData = messageData;
        }

        public String getSchemeIdUri() {
            return schemeIdUri;
        }

        public String getMessageData() {
            return messageData;
        }
    }
}
