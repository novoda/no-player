# no-player [![](https://ci.novoda.com/buildStatus/icon?job=no-player)](https://ci.novoda.com/job/no-player/lastBuild/console) [![Download](https://api.bintray.com/packages/novoda/maven/no-player/images/download.svg) ](https://bintray.com/novoda/maven/no-player/_latestVersion) [![](https://raw.githubusercontent.com/novoda/novoda/master/assets/btn_apache_lisence.png)](LICENSE.txt)

A simplified `Player` wrapper for [MediaPlayer](https://developer.android.com/reference/android/media/MediaPlayer.html) and [ExoPlayer](https://google.github.io/ExoPlayer/)

## Description

Some of the benefits are -

- Unified playback interface and event listeners.
- `MediaPlayer` buffering.
- `ExoPlayer` local, streaming and provisioning widevine modular DRM.
- Aspect ratio maintaining.
- Player selection based on `ContentType` and DRM.


## Adding to your project

To start using this library, add these lines to the `build.gradle` of your project:

```groovy
repositories {
    jcenter()
}

dependencies {
    compile 'com.novoda:no-player:<latest-version>'
}
```


## Simple usage
Creating a `Player`

```java
Player player = new PlayerBuilder()
        .withPriority(PlayerType.EXO_PLAYER)
        .withWidevineModularStreamingDrm(drmHandler)
        .build(this);
```

Creating the `PlayerView`

```xml
R.layout.player_activity

  <com.novoda.noplayer.NoPlayerView
    android:id="@+id/player_view"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_gravity="center" />
```

Attaching to a `PlayerView`

```java
PlayerView playerView = findViewById(R.id.player_view);
player.attach(playerView);
```


Playing Content

```java
player.addPreparedListener(new Player.PreparedListener() {
    @Override
    public void onPrepared(PlayerState playerState) {
        player.play();
    }
});

Uri uri = Uri.parse("http://yt-dash-mse-test.commondatastorage.googleapis.com/media/car-20120827-manifest.mpd");
player.loadVideo(uri, ContentType.DASH);
```

## Links

Here are a list of useful links:

 * We always welcome people to contribute new features or bug fixes, [here is how](https://github.com/novoda/novoda/blob/master/CONTRIBUTING.md).
 * If you have a problem check the [Issues Page](https://github.com/novoda/no-player/issues) first to see if we are working on it.
 * Looking for community help, browse the already asked [Stack Overflow Questions](http://stackoverflow.com/questions/tagged/support-no-player) or use the tag: `support-no-player` when posting a new question. 

