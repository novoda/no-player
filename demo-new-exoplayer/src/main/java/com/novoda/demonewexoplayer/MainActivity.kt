package com.novoda.demonewexoplayer

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager
import com.google.android.exoplayer2.drm.HttpMediaDrmCallback
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var exoPlayer: SimpleExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        exoPlayer = SimpleExoPlayer.Builder(this).build()
        exoPlayer.playWhenReady = true

        player_view.requestFocus()
        player_view.player = exoPlayer

        val mediaSource = mediaSource(
                licenseUrl = "https://proxy.uat.widevine.com/proxy?provider=widevine_test",
                manifestUrl = "https://storage.googleapis.com/wvmedia/cenc/hevc/tears/tears.mpd"
        )

        exoPlayer.prepare(mediaSource)
    }

    private fun mediaSource(licenseUrl: String, manifestUrl: String): DashMediaSource {
        val defaultHttpDataSourceFactory = DefaultHttpDataSourceFactory(Util.getUserAgent(this, "demo new exoplayer"))
        val mediaDrmCallback = HttpMediaDrmCallback(
                licenseUrl,
                defaultHttpDataSourceFactory
        )
        val drmSessionManager = DefaultDrmSessionManager.Builder()
                .setMultiSession(true)
                .setUseDrmSessionsForClearContent()
                .build(mediaDrmCallback)
        val factory = DefaultDataSourceFactory(
                this,
                defaultHttpDataSourceFactory
        )
        val uriDashManifest: Uri = Uri.parse(manifestUrl)
        return DashMediaSource.Factory(factory)
                .setDrmSessionManager(drmSessionManager)
                .createMediaSource(uriDashManifest)
    }

    override fun onDestroy() {
        exoPlayer.release()
        super.onDestroy()
    }
}
