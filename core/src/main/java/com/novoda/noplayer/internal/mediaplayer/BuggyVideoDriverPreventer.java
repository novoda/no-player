package com.novoda.noplayer.internal.mediaplayer;

import android.view.View;

import com.novoda.noplayer.NoPlayer;

/**
 * The intent for this component is to workaround a buggy video driver affecting AwesomePlayer on Nexus 5.
 * After inserting the headphones the screen goes black, causing layout changes, after recovering
 * from the freeze subsequent calls to {@link android.media.MediaPlayer#pause()} are ignored, the internal status machine got corrupted.
 * <p>
 * It can be workaround by forcing {@link android.media.MediaPlayer#start()} when it was already playing.
 */
class BuggyVideoDriverPreventer {

    private final MediaPlayerTypeReader mediaPlayerTypeReader;

    private OnPotentialBuggyDriverLayoutListener preventerListener;

    BuggyVideoDriverPreventer(MediaPlayerTypeReader mediaPlayerTypeReader) {
        this.mediaPlayerTypeReader = mediaPlayerTypeReader;
    }

    void preventVideoDriverBug(NoPlayer player, View containerView) {
        if (videoDriverCanBeBuggy()) {
            attemptToCorrectMediaPlayerStatus(player, containerView);
        }
    }

    private boolean videoDriverCanBeBuggy() {
        return mediaPlayerTypeReader.getPlayerType() == AndroidMediaPlayerType.AWESOME;
    }

    private void attemptToCorrectMediaPlayerStatus(NoPlayer player, View containerView) {
        preventerListener = new OnPotentialBuggyDriverLayoutListener(player);
        containerView.addOnLayoutChangeListener(preventerListener);
    }

    void clear(View containerView) {
        containerView.removeOnLayoutChangeListener(preventerListener);
        preventerListener = null;
    }
}
