package com.novoda.noplayer.internal.mediaplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.Surface;

import com.novoda.noplayer.internal.mediaplayer.PlaybackStateChecker.PlaybackState;
import com.novoda.noplayer.internal.mediaplayer.forwarder.MediaPlayerForwarder;
import com.novoda.noplayer.internal.utils.NoPlayerLog;
import com.novoda.noplayer.internal.utils.Optional;
import com.novoda.noplayer.model.AudioTracks;
import com.novoda.noplayer.model.PlayerAudioTrack;
import com.novoda.noplayer.model.PlayerSubtitleTrack;
import com.novoda.noplayer.model.PlayerVideoTrack;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.novoda.noplayer.internal.mediaplayer.PlaybackStateChecker.PlaybackState.IDLE;
import static com.novoda.noplayer.internal.mediaplayer.PlaybackStateChecker.PlaybackState.PAUSED;
import static com.novoda.noplayer.internal.mediaplayer.PlaybackStateChecker.PlaybackState.PLAYING;

class AndroidMediaPlayerFacade {

    private static final Map<String, String> NO_HEADERS = null;

    private final Context context;
    private final MediaPlayerForwarder forwarder;
    private final AudioManager audioManager;
    private final AndroidMediaPlayerAudioTrackSelector trackSelector;
    private final PlaybackStateChecker playbackStateChecker;
    private final MediaPlayerCreator mediaPlayerCreator;

    private PlaybackState currentState = IDLE;

    private int currentBufferPercentage;
    private float volume = 1.0f;

    @Nullable
    private MediaPlayer mediaPlayer;

    static AndroidMediaPlayerFacade newInstance(Context context, MediaPlayerForwarder forwarder) {
        TrackInfosFactory trackInfosFactory = new TrackInfosFactory();
        AndroidMediaPlayerAudioTrackSelector trackSelector = new AndroidMediaPlayerAudioTrackSelector(trackInfosFactory);
        PlaybackStateChecker playbackStateChecker = new PlaybackStateChecker();
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        MediaPlayerCreator mediaPlayerCreator = new MediaPlayerCreator();
        return new AndroidMediaPlayerFacade(context, forwarder, audioManager, trackSelector, playbackStateChecker, mediaPlayerCreator);
    }

    AndroidMediaPlayerFacade(Context context,
                             MediaPlayerForwarder forwarder,
                             AudioManager audioManager,
                             AndroidMediaPlayerAudioTrackSelector trackSelector,
                             PlaybackStateChecker playbackStateChecker,
                             MediaPlayerCreator mediaPlayerCreator) {
        this.context = context;
        this.forwarder = forwarder;
        this.audioManager = audioManager;
        this.trackSelector = trackSelector;
        this.playbackStateChecker = playbackStateChecker;
        this.mediaPlayerCreator = mediaPlayerCreator;
    }

    void prepareVideo(Uri videoUri, Surface surface) {
        requestAudioFocus();
        release();
        try {
            currentState = PlaybackState.PREPARING;
            mediaPlayer = createAndBindMediaPlayer(surface, videoUri);
            mediaPlayer.prepareAsync();
        } catch (IOException | IllegalArgumentException | IllegalStateException ex) {
            reportCreationError(ex, videoUri);
        }
    }

    private void requestAudioFocus() {
        audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }

    private MediaPlayer createAndBindMediaPlayer(Surface surface,
                                                 Uri videoUri) throws IOException, IllegalStateException, IllegalArgumentException {
        MediaPlayer mediaPlayer = mediaPlayerCreator.createMediaPlayer();
        mediaPlayer.setOnPreparedListener(internalPreparedListener);
        mediaPlayer.setOnVideoSizeChangedListener(internalSizeChangedListener);
        mediaPlayer.setOnCompletionListener(internalCompletionListener);
        mediaPlayer.setOnErrorListener(internalErrorListener);
        mediaPlayer.setOnBufferingUpdateListener(internalBufferingUpdateListener);
        mediaPlayer.setDataSource(context, videoUri, NO_HEADERS);
        mediaPlayer.setSurface(surface);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setScreenOnWhilePlaying(true);

        currentBufferPercentage = 0;
        volume = 1.0f;

        return mediaPlayer;
    }

    private void reportCreationError(Exception ex, Uri videoUri) {
        NoPlayerLog.w(ex, "Unable to open content: " + videoUri);
        currentState = PlaybackState.ERROR;
        internalErrorListener.onError(mediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
    }

    private final MediaPlayer.OnVideoSizeChangedListener internalSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            MediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedForwarder = forwarder.onSizeChangedListener();
            if (onVideoSizeChangedForwarder == null) {
                throw new IllegalStateException("Should bind a OnVideoSizeChangedListener. Cannot forward events.");
            }
            onVideoSizeChangedForwarder.onVideoSizeChanged(mp, width, height);
        }
    };

    private final MediaPlayer.OnPreparedListener internalPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            currentState = PlaybackState.PREPARED;

            MediaPlayer.OnPreparedListener onPreparedForwarder = forwarder.onPreparedListener();
            if (onPreparedForwarder == null) {
                throw new IllegalStateException("Should bind a OnPreparedListener. Cannot forward events.");
            }
            onPreparedForwarder.onPrepared(mediaPlayer);
        }
    };

    private final MediaPlayer.OnCompletionListener internalCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            currentState = PlaybackState.COMPLETED;
            MediaPlayer.OnCompletionListener onCompletionForwarder = forwarder.onCompletionListener();
            if (onCompletionForwarder == null) {
                throw new IllegalStateException("Should bind a OnCompletionListener. Cannot forward events.");
            }
            onCompletionForwarder.onCompletion(mediaPlayer);
        }
    };

    private final MediaPlayer.OnErrorListener internalErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            NoPlayerLog.d("Error: " + what + "," + extra);
            currentState = PlaybackState.ERROR;
            MediaPlayer.OnErrorListener onErrorForwarder = forwarder.onErrorListener();
            if (onErrorForwarder == null) {
                throw new IllegalStateException("Should bind a OnErrorListener. Cannot forward events.");
            }
            return onErrorForwarder.onError(mediaPlayer, what, extra);
        }
    };

    private final MediaPlayer.OnBufferingUpdateListener internalBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            currentBufferPercentage = percent;
        }
    };

    void release() {
        if (hasPlayer()) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
            currentState = IDLE;
        }
    }

    void start(Surface surface) throws IllegalStateException {
        assertIsInPlaybackState();
        mediaPlayer.setSurface(surface);
        currentState = PLAYING;
        mediaPlayer.start();
    }

    void pause() throws IllegalStateException {
        assertIsInPlaybackState();

        if (isPlaying()) {
            mediaPlayer.pause();
            currentState = PAUSED;
        }
    }

    int mediaDurationInMillis() throws IllegalStateException {
        assertIsInPlaybackState();
        return mediaPlayer.getDuration();
    }

    int currentPositionInMillis() throws IllegalStateException {
        assertIsInPlaybackState();
        return mediaPlayer.getCurrentPosition();
    }

    void seekTo(long positionInMillis) throws IllegalStateException {
        assertIsInPlaybackState();
        mediaPlayer.seekTo((int) positionInMillis);
    }

    boolean isPlaying() {
        return playbackStateChecker.isPlaying(mediaPlayer, currentState);
    }

    int getBufferPercentage() throws IllegalStateException {
        assertIsInPlaybackState();
        return currentBufferPercentage;
    }

    AudioTracks getAudioTracks() throws IllegalStateException {
        assertIsInPlaybackState();
        return trackSelector.getAudioTracks(mediaPlayer);
    }

    boolean selectAudioTrack(PlayerAudioTrack playerAudioTrack) throws IllegalStateException {
        assertIsInPlaybackState();
        return trackSelector.selectAudioTrack(mediaPlayer, playerAudioTrack);
    }

    boolean clearAudioTrackSelection() {
        assertIsInPlaybackState();
        NoPlayerLog.w("Tried to clear audio track selection but has not been implemented for MediaPlayer.");
        return false;
    }

    void setOnSeekCompleteListener(MediaPlayer.OnSeekCompleteListener seekToResettingSeekListener) throws IllegalStateException {
        assertIsInPlaybackState();
        mediaPlayer.setOnSeekCompleteListener(seekToResettingSeekListener);
    }

    boolean hasPlayedContent() {
        return hasPlayer();
    }

    private boolean hasPlayer() {
        return mediaPlayer != null;
    }

    boolean clearSubtitleTrack() throws IllegalStateException {
        assertIsInPlaybackState();
        NoPlayerLog.w("Tried to hide subtitle track but has not been implemented for MediaPlayer.");
        return false;
    }

    boolean selectSubtitleTrack(PlayerSubtitleTrack subtitleTrack) throws IllegalStateException {
        assertIsInPlaybackState();
        NoPlayerLog.w("Tried to select subtitle track but has not been implemented for MediaPlayer.");
        return false;
    }

    List<PlayerSubtitleTrack> getSubtitleTracks() throws IllegalStateException {
        assertIsInPlaybackState();
        NoPlayerLog.w("Tried to get subtitle tracks but has not been implemented for MediaPlayer.");
        return Collections.emptyList();
    }

    private void assertIsInPlaybackState() throws IllegalStateException {
        if (!playbackStateChecker.isInPlaybackState(mediaPlayer, currentState)) {
            throw new IllegalStateException("Video must be loaded and not in an error state before trying to interact with the player");
        }
    }

    Optional<PlayerVideoTrack> getSelectedVideoTrack() {
        assertIsInPlaybackState();
        NoPlayerLog.w("Tried to get the currently playing video track but has not been implemented for MediaPlayer.");
        return Optional.absent();
    }

    List<PlayerVideoTrack> getVideoTracks() {
        assertIsInPlaybackState();
        NoPlayerLog.w("Tried to get video tracks but has not been implemented for MediaPlayer.");
        return Collections.emptyList();
    }

    boolean selectVideoTrack(PlayerVideoTrack videoTrack) {
        assertIsInPlaybackState();
        NoPlayerLog.w("Tried to select a video track but has not been implemented for MediaPlayer.");
        return false;
    }

    boolean clearVideoTrackSelection() {
        assertIsInPlaybackState();
        NoPlayerLog.w("Tried to clear video track selection but has not been implemented for MediaPlayer.");
        return false;
    }

    void setRepeating(boolean repeating) {
        assertIsInPlaybackState();
        mediaPlayer.setLooping(repeating);
    }

    void setVolume(float volume) {
        assertIsInPlaybackState();
        this.volume = volume;
        mediaPlayer.setVolume(volume, volume);
    }

    float getVolume() {
        assertIsInPlaybackState();
        return volume;
    }
}
