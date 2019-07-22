package com.novoda.noplayer.internal.exoplayer;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.mediacodec.MediaCodecInfo;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.CheckResult;

class InternalMediaCodecUtil {

    List<MediaCodecInfo> getDecoderInfos(
            String mimeType,
            boolean requiresSecureDecoder,
            boolean requiresTunnelingDecoder
    ) throws MediaCodecUtil.DecoderQueryException {
        return MediaCodecUtil.getDecoderInfos(mimeType, requiresSecureDecoder, requiresTunnelingDecoder);
    }

    MediaCodecInfo getPassthroughDecoderInfo() throws MediaCodecUtil.DecoderQueryException {
        return MediaCodecUtil.getPassthroughDecoderInfo();
    }

    /**
     * Returns a copy of the provided decoder list sorted such that decoders with format support are
     * listed first. The returned list is modifiable for convenience.
     */
    @SuppressWarnings({"PMD.AvoidReassigningParameters"})
    @CheckResult
    static List<MediaCodecInfo> getDecoderInfosSortedByFormatSupport(List<MediaCodecInfo> decoderInfos, final Format format) {
        decoderInfos = new ArrayList<>(decoderInfos);
        sortByScore(
                decoderInfos,
                new ScoreProvider<MediaCodecInfo>() {
                    @Override
                    public int getScore(MediaCodecInfo decoderInfo) {
                        try {
                            return decoderInfo.isFormatSupported(format) ? 1 : 0;
                        } catch (MediaCodecUtil.DecoderQueryException e) {
                            return -1;
                        }
                    }
                }
        );
        return decoderInfos;
    }

    /**
     * Returns a list of decoders that supports the provided format
     */
    static List<MediaCodecInfo> getOnlySupportedDecoderInfos(List<MediaCodecInfo> decoderInfos, Format format) {
        List<MediaCodecInfo> onlySupported = new ArrayList<>();
        for (MediaCodecInfo decoderInfo : decoderInfos) {
            try {
                if (decoderInfo.isFormatSupported(format)) {
                    onlySupported.add(decoderInfo);
                }
            } catch (MediaCodecUtil.DecoderQueryException e) {
                // ignore, decoder will not be added in this case
            }
        }

        return onlySupported;
    }

    static List<MediaCodecInfo> removeUnsupportedVideoDecoders(List<MediaCodecInfo> decoderInfos, List<String> unsupportedDecoders) {
        List<MediaCodecInfo> onlySupported = new ArrayList<>(decoderInfos);
        for (MediaCodecInfo decoderInfo : decoderInfos) {
            for (String unsupportedDecoder : unsupportedDecoders) {
                if (decoderInfo.name.equalsIgnoreCase(unsupportedDecoder)) {
                    onlySupported.remove(decoderInfo);
                    break;
                }
            }
        }
        return onlySupported;
    }

    static List<MediaCodecInfo> removeAllUnsecureDecodersFromHdTrack(Format format, List<MediaCodecInfo> decoderInfos, int hdQualityThreshold) {
        if (format.bitrate < hdQualityThreshold) {
            return decoderInfos;
        }

        List<MediaCodecInfo> onlySupported = new ArrayList<>(decoderInfos);
        for (MediaCodecInfo decoderInfo : decoderInfos) {
            if (!decoderInfo.secure) {
                onlySupported.remove(decoderInfo);
            }
        }
        return onlySupported;
    }

    /**
     * Stably sorts the provided {@code list} in-place, in order of decreasing score.
     */
    private static <T> void sortByScore(List<T> list, final ScoreProvider<T> scoreProvider) {
        Collections.sort(list, new Comparator<T>() {
            @Override
            public int compare(T a, T b) {
                return scoreProvider.getScore(b) - scoreProvider.getScore(a);
            }
        });
    }

    /**
     * Interface for providers of item scores.
     */
    private interface ScoreProvider<T> {
        /**
         * Returns the score of the provided item.
         */
        int getScore(T t);
    }
}
