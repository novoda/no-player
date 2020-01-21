package com.novoda.noplayer.drm;

public interface KeyRequestExecutor {

    KeyRequestExecutor NOT_REQUIRED = new KeyRequestExecutor() {
        @Override
        public byte[] executeKeyRequest(ModularDrmKeyRequest request) throws IllegalStateException {
            throw new IllegalStateException("No key request execution expected.");
        }
    };

    byte[] executeKeyRequest(ModularDrmKeyRequest request) throws DrmRequestException;

    final class DrmRequestException extends Exception {

        public static DrmRequestException from(Exception e) {
            return new DrmRequestException("Drm http request failed : " + e.getMessage(), e);
        }

        public static DrmRequestException invalidHttpCode(int code, String body) {
            return new DrmRequestException("Unexpected response HTTP code: " + code + " | " + body);
        }

        private DrmRequestException(String detailMessage) {
            super(detailMessage);
        }

        private DrmRequestException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
