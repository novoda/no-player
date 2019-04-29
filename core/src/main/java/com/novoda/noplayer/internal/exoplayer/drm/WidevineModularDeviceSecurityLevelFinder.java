package com.novoda.noplayer.internal.exoplayer.drm;

import android.media.MediaDrm;
import android.os.Build;

import com.novoda.noplayer.internal.utils.Optional;

class WidevineModularDeviceSecurityLevelFinder {

    private static final boolean DO_NOT_RETRY_ON_FAIL = false;
    private static final boolean RETRY_ON_FAIL = true;

    WidevineSecurityLevel findSecurityLevel(Optional<MediaDrm> mediaDrm) {
        return queryWidevineSecurityLevel(RETRY_ON_FAIL, mediaDrm);
    }

    private WidevineSecurityLevel queryWidevineSecurityLevel(boolean retry, Optional<MediaDrm> mediaDrmOptional) throws IllegalStateException {
        return mediaDrmOptional.isPresent() ? getWidevineSecurityLevel(mediaDrmOptional.get(), retry) : WidevineSecurityLevel.UNKNOWN;
    }

    private WidevineSecurityLevel getWidevineSecurityLevel(MediaDrm mediaDrm, boolean retry) {
        try {
            return getWidevineSecurityLevel(mediaDrm);
        } catch (IllegalStateException exception) { // IllegalStateException as MediaDrmResetException is api 23+
            if (retry) {
                return queryWidevineSecurityLevel(DO_NOT_RETRY_ON_FAIL, Optional.of(mediaDrm));
            } else {
                return WidevineSecurityLevel.UNKNOWN;
            }
        }
    }

    private WidevineSecurityLevel getWidevineSecurityLevel(MediaDrm mediaDrm) {
        if (is23MarshmallowOrOver()) {
            return new MarshmallowSecurityLevelFinder().findWidevineSecurityLevel(mediaDrm);
        } else {
            return new KitKatAndLollipopSecurityLevelFinder().findWidevineSecurityLevel(mediaDrm);
        }
    }

    boolean is23MarshmallowOrOver() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
