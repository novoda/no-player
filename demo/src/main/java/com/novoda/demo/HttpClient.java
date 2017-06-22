package com.novoda.demo;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

class HttpClient {

    private static final String POST = "POST";
    private static final int RESPONSE_BUFFER_SIZE = 16384;

    static byte[] post(String url, byte[] data) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod(POST);
            connection.setDoOutput(true);
            connection.setDoInput(true);

            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();

            return readResponseFrom(connection);
        } catch (IOException e) {
            throw new HttpClientException(e);
        } finally {
            release(connection);
        }
    }

    private static void release(HttpURLConnection connection) {
        if (connection != null) {
            connection.disconnect();
        }
    }

    private static byte[] readResponseFrom(URLConnection connection) throws IOException {
        InputStream inputStream = null;
        ByteArrayOutputStream buffer = null;
        try {
            inputStream = connection.getInputStream();
            buffer = new ByteArrayOutputStream();

            int currentReadPosition;
            byte[] readHolder = new byte[RESPONSE_BUFFER_SIZE];

            while ((currentReadPosition = inputStream.read(readHolder, 0, readHolder.length)) != -1) {
                buffer.write(readHolder, 0, currentReadPosition);
            }

            buffer.flush();
            return buffer.toByteArray();
        } catch (IOException e) {
            throw new HttpClientException(e);
        } finally {
            release(inputStream, buffer);
        }
    }

    private static void release(InputStream inputStream, ByteArrayOutputStream buffer) throws IOException {
        if (inputStream != null) {
            inputStream.close();
        }

        if (buffer != null) {
            buffer.close();
        }
    }

    private static class HttpClientException extends RuntimeException {

        HttpClientException(Throwable cause) {
            super(cause);
        }
    }
}
