package com.novoda.noplayer.internal.exoplayer.forwarder;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.novoda.noplayer.NoPlayer;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class TracksChangedForwarderTest {

    private static final TrackGroupArray EMPTY_TRACK_GROUPS = TrackGroupArray.EMPTY;
    private static final TrackGroupArray NOT_EMPTY_TRACK_GROUPS = new TrackGroupArray(
            new TrackGroup(
                    Format.createTextContainerFormat(
                            null,
                            null,
                            null,
                            null,
                            null,
                            0,
                            C.SELECTION_FLAG_DEFAULT,
                            C.ROLE_FLAG_MAIN,
                            null)
            )
    );
    private static final TrackSelectionArray EMPTY_SELECTION_ARRAY = new TrackSelectionArray();
    private final NoPlayer.TracksChangedListener tracksChangedListener = mock(NoPlayer.TracksChangedListener.class);

    private final TracksChangedForwarder tracksChangedForwarder = new TracksChangedForwarder(tracksChangedListener);

    @Test
    public void forwardsOnTrackChangedWhenTrackGroupsAreNotEmpty() {

        tracksChangedForwarder.onTracksChanged(NOT_EMPTY_TRACK_GROUPS, EMPTY_SELECTION_ARRAY);

        verify(tracksChangedListener).onTracksChanged();
    }

    @Test
    public void forwardsOnTrackChangedWhenTrackGroupsAreEmpty() {

        tracksChangedForwarder.onTracksChanged(EMPTY_TRACK_GROUPS, EMPTY_SELECTION_ARRAY);

        verify(tracksChangedListener).onTracksChanged();
    }

    @Test
    public void doesNotforwardOnTrackChangedAfterSeekDiscontinuityWhenTrackGroupsAreEmpty() {
        tracksChangedForwarder.onPositionDiscontinuity(Player.DISCONTINUITY_REASON_SEEK);

        tracksChangedForwarder.onTracksChanged(EMPTY_TRACK_GROUPS, EMPTY_SELECTION_ARRAY);

        verify(tracksChangedListener, never()).onTracksChanged();
    }

    @Test
    public void forwardsOnTrackChangedAfterSeekDiscontinuityWhenTrackGroupsAreNotEmpty() {
        tracksChangedForwarder.onPositionDiscontinuity(Player.DISCONTINUITY_REASON_SEEK);

        tracksChangedForwarder.onTracksChanged(NOT_EMPTY_TRACK_GROUPS, EMPTY_SELECTION_ARRAY);

        verify(tracksChangedListener).onTracksChanged();
    }

    @Test
    public void forwardsOnTrackChangedAfterPeriodTransitionDiscontinuityWhenTrackGroupsAreNotEmpty() {
        tracksChangedForwarder.onPositionDiscontinuity(Player.DISCONTINUITY_REASON_PERIOD_TRANSITION);

        tracksChangedForwarder.onTracksChanged(NOT_EMPTY_TRACK_GROUPS, EMPTY_SELECTION_ARRAY);

        verify(tracksChangedListener).onTracksChanged();
    }

    @Test
    public void forwardsOnTrackChangedAfterSeekAdjustmentDiscontinuityWhenTrackGroupsAreNotEmpty() {
        tracksChangedForwarder.onPositionDiscontinuity(Player.DISCONTINUITY_REASON_SEEK_ADJUSTMENT);

        tracksChangedForwarder.onTracksChanged(NOT_EMPTY_TRACK_GROUPS, EMPTY_SELECTION_ARRAY);

        verify(tracksChangedListener).onTracksChanged();
    }

    @Test
    public void forwardsOnTrackChangedAfterAdInsertionDiscontinuityWhenTrackGroupsAreNotEmpty() {
        tracksChangedForwarder.onPositionDiscontinuity(Player.DISCONTINUITY_REASON_AD_INSERTION);

        tracksChangedForwarder.onTracksChanged(NOT_EMPTY_TRACK_GROUPS, EMPTY_SELECTION_ARRAY);

        verify(tracksChangedListener).onTracksChanged();
    }

    @Test
    public void forwardsOnTrackChangedAfterInternalDiscontinuityWhenTrackGroupsAreNotEmpty() {
        tracksChangedForwarder.onPositionDiscontinuity(Player.DISCONTINUITY_REASON_INTERNAL);

        tracksChangedForwarder.onTracksChanged(NOT_EMPTY_TRACK_GROUPS, EMPTY_SELECTION_ARRAY);

        verify(tracksChangedListener).onTracksChanged();
    }
}
