package com.novoda.noplayer.subtitles;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.accessibility.CaptioningManager;
import android.view.accessibility.CaptioningManager.CaptionStyle;

import com.novoda.noplayer.test.utils.StaticMutationRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.content.Context.*;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR2;
import static android.os.Build.VERSION_CODES.KITKAT;
import static com.novoda.noplayer.test.utils.RelectionFinalMutationUtils.*;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class SystemCaptionPreferencesTest {

    private static final int FALLBACK_COLOR = Color.BLUE;
    private static final float TEXT_SIZE = 20f;
    private static final int SYSTEM_BACKGROUND_COLOR = Color.RED;
    private static final int SYSTEM_FOREGROUND_COLOR = Color.GREEN;
    private static final int SYSTEM_WINDOW_COLOR = Color.GRAY;
    private static final float SYSTEM_TEXT_SCALE_RATIO = 2f;

    public static class PreKitKat {

        @Rule
        public final StaticMutationRule mutations = new StaticMutationRule();

        @Before
        public void setUp() {
            mutations.mutateStatic(Build.VERSION.class, "SDK_INT", JELLY_BEAN_MR2);
        }

        @Test
        public void shouldProvideFallbackForBackgroundColor() {
            SystemCaptionPreferences preferences = givenSystemCaptionPreferences();

            int color = preferences.getStyle().backgroundColorOr(FALLBACK_COLOR);

            assertThat(color).isEqualTo(FALLBACK_COLOR);
        }

        @Test
        public void shouldProvideFallbackForForegroundColor() {
            SystemCaptionPreferences preferences = givenSystemCaptionPreferences();

            int color = preferences.getStyle().foregroundColorOr(FALLBACK_COLOR);

            assertThat(color).isEqualTo(FALLBACK_COLOR);
        }

        @Test
        public void shouldProvideTheSameTextSize() {
            SystemCaptionPreferences preferences = givenSystemCaptionPreferences();

            float actualTextSize = preferences.getStyle().scaleTextSize(TEXT_SIZE);

            assertThat(actualTextSize).isEqualTo(TEXT_SIZE);
        }

    }

    public static class PostKitKat {

        @Rule
        public final StaticMutationRule mutations = new StaticMutationRule();

        @Before
        public void setUp() {
            mutations.mutateStatic(Build.VERSION.class, "SDK_INT", KITKAT);
        }

        @Test
        public void shouldProvideFallbackForBackgroundColor() {
            SystemCaptionPreferences preferences = givenSystemCaptionPreferences();

            int color = preferences.getStyle().backgroundColorOr(FALLBACK_COLOR);

            assertThat(color).isEqualTo(SYSTEM_BACKGROUND_COLOR);
        }

        @Test
        public void shouldProvideFallbackForForegroundColor() {
            SystemCaptionPreferences preferences = givenSystemCaptionPreferences();

            int color = preferences.getStyle().foregroundColorOr(FALLBACK_COLOR);

            assertThat(color).isEqualTo(SYSTEM_FOREGROUND_COLOR);
        }

        @Test
        public void shouldProvideTheSameTextSize() {
            SystemCaptionPreferences preferences = givenSystemCaptionPreferences();

            float actualTextSize = preferences.getStyle().scaleTextSize(TEXT_SIZE);

            assertThat(actualTextSize).isEqualTo(TEXT_SIZE * SYSTEM_TEXT_SCALE_RATIO);
        }

    }

    private static SystemCaptionPreferences givenSystemCaptionPreferences() {
        return new SystemCaptionPreferences(contextWithCaptionManager());
    }

    private static Context contextWithCaptionManager() {
        Context context = mock(Context.class);
        CaptioningManager captioningManager = systemCaptioningManager();
        when(context.getSystemService(CAPTIONING_SERVICE)).thenReturn(captioningManager);
        return context;
    }

    private static CaptioningManager systemCaptioningManager() {
        CaptioningManager manager = mock(CaptioningManager.class);
        CaptionStyle captionStyle = systemCaptionStyle();
        when(manager.getUserStyle()).thenReturn(captionStyle);
        when(manager.getFontScale()).thenReturn(SYSTEM_TEXT_SCALE_RATIO);
        return manager;
    }

    private static CaptionStyle systemCaptionStyle() {
        try {
            CaptionStyle style = mock(CaptionStyle.class);
            setFinalField(style, "foregroundColor", SYSTEM_FOREGROUND_COLOR);
            setFinalField(style, "backgroundColor", SYSTEM_BACKGROUND_COLOR);
            setFinalField(style, "windowColor", SYSTEM_WINDOW_COLOR);
            return style;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}