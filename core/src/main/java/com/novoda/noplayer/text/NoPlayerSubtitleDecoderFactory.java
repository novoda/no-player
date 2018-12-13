package com.novoda.noplayer.text;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.text.SubtitleDecoder;
import com.google.android.exoplayer2.text.SubtitleDecoderFactory;
import com.google.android.exoplayer2.text.cea.Cea608Decoder;
import com.google.android.exoplayer2.text.cea.Cea708Decoder;
import com.google.android.exoplayer2.text.dvb.DvbDecoder;
import com.google.android.exoplayer2.text.pgs.PgsDecoder;
import com.google.android.exoplayer2.text.ssa.SsaDecoder;
import com.google.android.exoplayer2.text.subrip.SubripDecoder;
import com.google.android.exoplayer2.text.ttml.TtmlDecoder;
import com.google.android.exoplayer2.text.tx3g.Tx3gDecoder;
import com.google.android.exoplayer2.text.webvtt.Mp4WebvttDecoder;
import com.google.android.exoplayer2.util.MimeTypes;
import com.novoda.noplayer.external.exoplayer.text.webvtt.WebvttDecoder;

// This is a factory and we need to consider all the supported formats when creating a decoder
@SuppressWarnings({"PMD.CyclomaticComplexity", "PMD.StdCyclomaticComplexity"})
public class NoPlayerSubtitleDecoderFactory implements SubtitleDecoderFactory {

    @Override
    public boolean supportsFormat(Format format) {
        String mimeType = format.sampleMimeType;
        return MimeTypes.TEXT_VTT.equals(mimeType)
            || MimeTypes.TEXT_SSA.equals(mimeType)
            || MimeTypes.APPLICATION_TTML.equals(mimeType)
            || MimeTypes.APPLICATION_MP4VTT.equals(mimeType)
            || MimeTypes.APPLICATION_SUBRIP.equals(mimeType)
            || MimeTypes.APPLICATION_TX3G.equals(mimeType)
            || MimeTypes.APPLICATION_CEA608.equals(mimeType)
            || MimeTypes.APPLICATION_MP4CEA608.equals(mimeType)
            || MimeTypes.APPLICATION_CEA708.equals(mimeType)
            || MimeTypes.APPLICATION_DVBSUBS.equals(mimeType)
            || MimeTypes.APPLICATION_PGS.equals(mimeType);
    }

    @Override
    public SubtitleDecoder createDecoder(Format format) {
        switch (format.sampleMimeType) {
            case MimeTypes.TEXT_VTT:
                return new WebvttDecoder();
            case MimeTypes.TEXT_SSA:
                return new SsaDecoder(format.initializationData);
            case MimeTypes.APPLICATION_MP4VTT:
                return new Mp4WebvttDecoder();
            case MimeTypes.APPLICATION_TTML:
                return new TtmlDecoder();
            case MimeTypes.APPLICATION_SUBRIP:
                return new SubripDecoder();
            case MimeTypes.APPLICATION_TX3G:
                return new Tx3gDecoder(format.initializationData);
            case MimeTypes.APPLICATION_CEA608:
            case MimeTypes.APPLICATION_MP4CEA608:
                return new Cea608Decoder(format.sampleMimeType, format.accessibilityChannel);
            case MimeTypes.APPLICATION_CEA708:
                return new Cea708Decoder(format.accessibilityChannel, format.initializationData);
            case MimeTypes.APPLICATION_DVBSUBS:
                return new DvbDecoder(format.initializationData);
            case MimeTypes.APPLICATION_PGS:
                return new PgsDecoder();
            default:
                throw new IllegalArgumentException("Attempted to create decoder for unsupported format");
        }
    }
}
