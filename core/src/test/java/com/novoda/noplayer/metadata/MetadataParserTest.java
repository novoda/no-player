package com.novoda.noplayer.metadata;


import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.emsg.EventMessage;
import com.google.android.exoplayer2.metadata.flac.VorbisComment;
import com.google.android.exoplayer2.metadata.id3.BinaryFrame;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MetadataParserTest {

    private MetadataParser parser;

    @Before
    public void setUp() {
        parser = new MetadataParser();
    }

    @Test
    public void parseValidMetaDataEntries() {
        Metadata metadata = getExoValidMetadata();
        com.novoda.noplayer.metadata.Metadata parseMetadata = parser.parseMetadata(metadata);

        List<com.novoda.noplayer.metadata.Metadata.Entry> entries = parseMetadata.getEntries();
        assertEquals(4, entries.size());
        assertTrue(entries.get(0) instanceof com.novoda.noplayer.metadata.Metadata.BinaryFrame);
        assertTrue(entries.get(1) instanceof com.novoda.noplayer.metadata.Metadata.EventMessage);
    }

    @Test
    public void parseMetaDataAndIgnoreExtraEntry() {
        Metadata metadata = getExoExtrarMetadata();
        com.novoda.noplayer.metadata.Metadata parseMetadata = parser.parseMetadata(metadata);

        List<com.novoda.noplayer.metadata.Metadata.Entry> entries = parseMetadata.getEntries();
        assertEquals(4, entries.size());
        assertTrue(entries.get(0) instanceof com.novoda.noplayer.metadata.Metadata.BinaryFrame);
        assertTrue(entries.get(1) instanceof com.novoda.noplayer.metadata.Metadata.EventMessage);
    }

    @Test
    public void parseMetaDataEmptyEntry() {
        Metadata metadata = new Metadata();
        com.novoda.noplayer.metadata.Metadata parseMetadata = parser.parseMetadata(metadata);

        List<com.novoda.noplayer.metadata.Metadata.Entry> entries = parseMetadata.getEntries();
        assertEquals(0, entries.size());
    }

    @Test
    public void parseNullMetadata() {
        com.novoda.noplayer.metadata.Metadata parseMetadata = parser.parseMetadata(null);
        assertNull(parseMetadata);
    }

    private Metadata getExoValidMetadata() {
        return new Metadata(getBasicEntries());
    }

    private Metadata getExoExtrarMetadata() {
        List<Metadata.Entry> entries = getBasicEntries();
        // add an unconsumed event
        entries.add(0,new VorbisComment("0", "0"));
        return new Metadata(entries);
    }

    private List<Metadata.Entry> getBasicEntries() {
        List<Metadata.Entry> entries = new ArrayList<>();
        entries.add(new BinaryFrame("0", "0".getBytes()));
        entries.add(new EventMessage("uri:scheme", "0", 0, 0, "0".getBytes()));
        entries.add(new BinaryFrame("1", "1".getBytes()));
        entries.add(new EventMessage("uri:scheme", "1", 1, 1, "1".getBytes()));
        return entries;
    }

}