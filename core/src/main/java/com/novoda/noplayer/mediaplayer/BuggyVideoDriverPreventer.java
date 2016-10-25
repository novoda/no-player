package com.novoda.noplayer.mediaplayer;

import android.view.View;

import com.novoda.noplayer.Player;

/**
 * The intent for this component is to workaround a buggy video driver affecting AwesomePlayer on Nexus 5.
 * After inserting the headphones the screen goes black, causing layout changes, after recovering
 * from the freeze subsequent calls to {@link android.media.MediaPlayer#pause()} are ignored, the internal status machine got corrupted.
 *
 * It can be workaround by forcing {@link android.media.MediaPlayer#start()} when it was already playing.
 */
class BuggyVideoDriverPreventer {

    private final View videoContainer;
    private final Player player;
    private final PlayerChecker playerChecker;

    public static BuggyVideoDriverPreventer newInstance(View videoContainer, Player player) {
        PlayerChecker playerChecker = PlayerChecker.newInstance();
        return new BuggyVideoDriverPreventer(videoContainer, player, playerChecker);
    }

    BuggyVideoDriverPreventer(View videoContainer, Player player, PlayerChecker playerChecker) {
        this.videoContainer = videoContainer;
        this.player = player;
        this.playerChecker = playerChecker;
    }

    public void preventVideoDriverBug() {
        if (videoDriverCanBeBuggy()) {
            attemptToCorrectMediaPlayerStatus();
        }
    }

    private boolean videoDriverCanBeBuggy() {
        return playerChecker.getPlayerType() == AndroidMediaPlayerType.AWESOME;
    }

    private void attemptToCorrectMediaPlayerStatus() {
        videoContainer.addOnLayoutChangeListener(new OnPotentialBuggyDriverLayoutListener(player));
    }

}
