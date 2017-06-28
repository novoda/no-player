package com.novoda.noplayer.internal.drm.provision;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class HttpUrlConnectionPoster {

    private static final String POST_REQUEST_METHOD = "POST";
    private static final int RESPONSE_BUFFER_SIZE = 1024 * 4;

    byte[] post(String url) throws IOException {
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) new URL(url).openConnection();
            urlConnection.setRequestMethod(POST_REQUEST_METHOD);
            urlConnection.setDoInput(true);
            return byteArrayFrom(urlConnection);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    private byte[] byteArrayFrom(HttpURLConnection urlConnection) throws IOException {
        InputStream inputStream = urlConnection.getInputStream();
        try {
            return byteArrayFrom(inputStream);
        } finally {
            inputStream.close();
        }
    }

    private byte[] byteArrayFrom(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[RESPONSE_BUFFER_SIZE];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        try {
            return outputStream.toByteArray();
        } finally {
            outputStream.close();
        }
    }
}
