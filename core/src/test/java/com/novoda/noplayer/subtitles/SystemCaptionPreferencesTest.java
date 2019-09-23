package com.novoda.noplayer.subtitles;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.view.accessibility.CaptioningManager;
import android.view.accessibility.CaptioningManager.CaptionStyle;

import androidx.annotation.NonNull;

import com.google.android.exoplayer2.util.Util;
import com.novoda.noplayer.test.utils.StaticMutationRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static android.content.Context.*;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR2;
import static android.os.Build.VERSION_CODES.KITKAT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static com.novoda.noplayer.test.utils.RelectionFinalMutationUtils.*;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(Parameterized.class)
public class SystemCaptionPreferencesTest {

    private static final int FALLBACK_COLOR = Color.BLUE;
    private static final float TEXT_SIZE = 20f;
    private static final int SYSTEM_BACKGROUND_COLOR = Color.RED;
    private static final int SYSTEM_FOREGROUND_COLOR = Color.GREEN;
    private static final int SYSTEM_WINDOW_COLOR = Color.GRAY;
    private static final float SYSTEM_TEXT_SCALE_RATIO = 2f;
    private static final Typeface SYSTEM_TYPEFACE = mock(Typeface.class);
    private static final Typeface NO_TYPEFACE = null;

    private final TestCase testCase;

    public SystemCaptionPreferencesTest(TestCase testCase) {
        this.testCase = testCase;
    }

    @Parameterized.Parameters(name = "{0}")
    public static TestCase[][] data() {
        return new TestCase[][] {
            { PRE_KITKAT_TEST_CASE },
            { KITKAT_TEST_CASE },
            { POST_LOLLIPOP_TEST_CASE }
        };
    }

    @Rule
    public final StaticMutationRule mutations = new StaticMutationRule();

    @Before
    public void setUp() {
        mutations.mutateStatic(Build.VERSION.class, "SDK_INT", testCase.sdkLevel);
        mutations.mutateStatic(Util.class, "SDK_INT", testCase.sdkLevel);
    }

    @Test
    public void shouldProvideBackgroundColor() {
        SystemCaptionPreferences preferences = givenSystemCaptionPreferences();

        int color = preferences.getStyle().backgroundColorOr(FALLBACK_COLOR);

        assertThat(color).isEqualTo(testCase.expectedBackgroundColor);
    }

    @Test
    public void shouldProvideForegroundColor() {
        SystemCaptionPreferences preferences = givenSystemCaptionPreferences();

        int color = preferences.getStyle().foregroundColorOr(FALLBACK_COLOR);

        assertThat(color).isEqualTo(testCase.expectedForegroundColor);
    }

    @Test
    public void shouldProvideWindowColor() {
        SystemCaptionPreferences preferences = givenSystemCaptionPreferences();

        int color = preferences.getStyle().windowColorOr(FALLBACK_COLOR);

        assertThat(color).isEqualTo(testCase.expectedWindowColor);
    }

    @Test
    public void shouldProvideTextSize() {
        SystemCaptionPreferences preferences = givenSystemCaptionPreferences();

        float actualTextSize = preferences.getStyle().scaleTextSize(TEXT_SIZE);

        assertThat(actualTextSize).isEqualTo(testCase.expectedTextSize);
    }

    @Test
    public void shouldProvideTypeface() {
        SystemCaptionPreferences preferences = givenSystemCaptionPreferences();

        Typeface actualTextSize = preferences.getStyle().typeface();

        assertThat(actualTextSize).isEqualTo(testCase.expectedTypeface);
    }

    private static final TestCase PRE_KITKAT_TEST_CASE = new TestCase(JELLY_BEAN_MR2, "PRE_KITKAT")
        .expectedBackgroundColor(FALLBACK_COLOR)
        .expectedForegroundColor(FALLBACK_COLOR)
        .expectedWindowColor(FALLBACK_COLOR)
        .expectedTextSize(TEXT_SIZE)
        .expectedTypeface(NO_TYPEFACE);

    private static final TestCase KITKAT_TEST_CASE = new TestCase(KITKAT, "KITKAT")
        .expectedBackgroundColor(SYSTEM_BACKGROUND_COLOR)
        .expectedForegroundColor(SYSTEM_FOREGROUND_COLOR)
        .expectedWindowColor(Color.TRANSPARENT)
        .expectedTextSize(TEXT_SIZE * SYSTEM_TEXT_SCALE_RATIO)
        .expectedTypeface(SYSTEM_TYPEFACE);

    private static final TestCase POST_LOLLIPOP_TEST_CASE = new TestCase(LOLLIPOP, "POST_LOLLIPOP")
        .expectedBackgroundColor(SYSTEM_BACKGROUND_COLOR)
        .expectedForegroundColor(SYSTEM_FOREGROUND_COLOR)
        .expectedWindowColor(SYSTEM_WINDOW_COLOR)
        .expectedTextSize(TEXT_SIZE * SYSTEM_TEXT_SCALE_RATIO)
        .expectedTypeface(SYSTEM_TYPEFACE);

    private static class TestCase {

        final int sdkLevel;
        final String name;
        int expectedBackgroundColor;
        int expectedForegroundColor;
        float expectedTextSize;
        int expectedWindowColor;
        Typeface expectedTypeface;

        private TestCase(int sdkLevel, String name) {
            this.sdkLevel = sdkLevel;
            this.name = name;
        }

        TestCase expectedBackgroundColor(int expectedBackgroundColor) {
            this.expectedBackgroundColor = expectedBackgroundColor;
            return this;
        }

        TestCase expectedForegroundColor(int expectedForegroundColor) {
            this.expectedForegroundColor = expectedForegroundColor;
            return this;
        }

        TestCase expectedTextSize(float expectedTextSize) {
            this.expectedTextSize = expectedTextSize;
            return this;
        }

        TestCase expectedWindowColor(int expectedWindowColor) {
            this.expectedWindowColor = expectedWindowColor;
            return this;
        }

        TestCase expectedTypeface(Typeface expectedTypeface) {
            this.expectedTypeface = expectedTypeface;
            return this;
        }

        @NonNull
        @Override
        public String toString() {
            return name;
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
            when(style.hasForegroundColor()).thenReturn(true);
            when(style.hasBackgroundColor()).thenReturn(true);
            when(style.hasWindowColor()).thenReturn(true);
            when(style.getTypeface()).thenReturn(SYSTEM_TYPEFACE);
            setFinalField(style, "foregroundColor", SYSTEM_FOREGROUND_COLOR);
            setFinalField(style, "backgroundColor", SYSTEM_BACKGROUND_COLOR);
            setFinalField(style, "windowColor", SYSTEM_WINDOW_COLOR);
            return style;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}