# This project is no longer under maintenance - 13/7/2020
Upgrades to latests version of `ExoPlayer` requires a significative amount of changes. The project is no longer maintained from our end.

-----

![noplayer](art/noplayer-header.png)

[![CI status](https://github.com/novoda/no-player/workflows/Production%20Builder/badge.svg)](https://github.com/novoda/no-player/actions?query=workflow%3A%22Production+Builder%22) [![Download from Bintray](https://api.bintray.com/packages/novoda/maven/no-player/images/download.svg)](https://bintray.com/novoda/maven/no-player/_latestVersion) ![Tests](https://img.shields.io/jenkins/t/https/ci.novoda.com/view/Open%20source/job/no-player.svg) ![Coverage](https://img.shields.io/jenkins/j/https/ci.novoda.com/view/Open%20source/job/no-player.svg) [![Apache 2.0 Licence](https://img.shields.io/github/license/novoda/no-player.svg)](https://github.com/novoda/no-player/blob/master/LICENSE)

A simplified Android `Player` wrapper for [MediaPlayer](https://developer.android.com/reference/android/media/MediaPlayer.html) and [ExoPlayer](https://google.github.io/ExoPlayer/).

## Description

Some of the benefits are:

- Unified playback interface and event listeners for ExoPlayer and MediaPlayer
- `MediaPlayer` buffering
- `ExoPlayer` local, streaming and provisioning WideVine Modular DRM
- Maintains video Aspect Ratio by default
- Player selection based on `ContentType` and DRM

Experimental Features, use with caution:
- Support for TextureView

## Adding to your project

To start using this library, add these lines to the `build.gradle` of your project:

```groovy
repositories {
    jcenter()
}

dependencies {
    implementation 'com.novoda:no-player:<latest-version>'
}
```

From no-player 4.5.0 this is also needed in the android section of your `build.gradle`

```groovy
compileOptions {
        targetCompatibility JavaVersion.VERSION_1_8
}
```

### Simple usage

 1. Create a `Player`:

    ```java
    Player player = new PlayerBuilder()
      .withPriority(PlayerType.EXO_PLAYER)
      .withWidevineModularStreamingDrm(drmHandler)
      .build(this);
    ```
    
    ```kotlin
    val player = PlayerBuilder()
        .withPriority(PlayerType.EXO_PLAYER)
        .build(this)
    ```

 2. Create the `PlayerView`:
  
    ```xml
    <com.novoda.noplayer.NoPlayerView
      android:id="@+id/player_view"
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      android:layout_gravity="center" />
    ```

 3. Attach to a `PlayerView`:

    ```java
    NoPlayerView playerView = findViewById(R.id.player_view);
    player.attach(playerView);
    ```
    
    ```kotlin
    val playerView = findViewById<NoPlayerView>(R.id.player_view)
    player.attach(playerView)
    ```


 4. Play some content:

    ```java
    player.getListeners().addPreparedListener(playerState -> player.play());
    
    Uri uri = Uri.parse(mpdUrl);
    player.loadVideo(uri, new OptionsBuilder()
                .withContentType(ContentType.DASH)
                .build());
    ```
    
    ```kotlin
    player.listeners.addPreparedListener { player.play() }
    
    val uri = Uri.parse("mpdUrl")
    player.loadVideo(uri, OptionsBuilder()
            .withContentType(ContentType.DASH)
            .build())

    ```

## Snapshots

[![CI status](https://github.com/novoda/no-player/workflows/Snapshot%20Builder/badge.svg)](https://github.com/novoda/no-player/actions?query=workflow%3A%22Snapshot+Builder%22) [![Download from Bintray](https://api.bintray.com/packages/novoda-oss/snapshots/no-player/images/download.svg)](https://bintray.com/novoda-oss/snapshots/no-player/_latestVersion)

Snapshot builds from [`develop`](https://github.com/novoda/no-player/compare/master...develop) are automatically deployed to a [repository](https://bintray.com/novoda-oss/snapshots/no-player/_latestVersion) that is not synced with JCenter.
To consume a snapshot build add an additional maven repo as follows:
```
repositories {
    maven {
        url 'https://dl.bintray.com/novoda-oss/snapshots/'
    }
}
```

You can find the latest snapshot version following this [link](https://bintray.com/novoda-oss/snapshots/no-player/_latestVersion).

## Contributing

We always welcome people to contribute new features or bug fixes, [here is how](https://github.com/novoda/novoda/blob/master/CONTRIBUTING.md).

If you have a problem, check the [Issues Page](https://github.com/novoda/no-player/issues) first to see if we are already working on it.

Looking for community help? Browse the already asked [Stack Overflow Questions](http://stackoverflow.com/questions/tagged/support-no-player) or use the tag `support-no-player` when posting a new question.
