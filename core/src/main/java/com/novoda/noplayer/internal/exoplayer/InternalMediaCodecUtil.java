package com.novoda.noplayer.internal.exoplayer;

import androidx.annotation.CheckResult;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.mediacodec.MediaCodecInfo;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
