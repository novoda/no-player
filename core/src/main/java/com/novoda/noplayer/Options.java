package com.novoda.noplayer;

public class Options {

    private final ContentType contentType;

    Options(ContentType contentType) {
        this.contentType = contentType;
    }

    public ContentType contentType() {
        return contentType;
    }
}
