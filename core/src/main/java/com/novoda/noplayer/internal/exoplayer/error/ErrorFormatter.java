package com.novoda.noplayer.internal.exoplayer.error;

import android.media.MediaCodec;
import android.os.Build;

import androidx.annotation.RequiresApi;

final class ErrorFormatter {

    private ErrorFormatter() {
        // Static class.
    }

    static String formatMessage(Throwable throwable) {
        return throwable.getClass().getName() + ": " + throwable.getMessage();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    static String formatCodecException(MediaCodec.CodecException exception) {
        String diagnosticInformation = "diagnosticInformation=" + exception.getDiagnosticInfo();
        String isTransient = " : isTransient=" + exception.isTransient();
        String isRecoverable = " : isRecoverable=" + exception.isRecoverable();

        return diagnosticInformation + isTransient + isRecoverable;
    }
}
