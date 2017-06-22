package com.novoda.demo;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class HttpClient {

    private static final String POST = "POST";
    private static final int BUFFER_SIZE = 16384;

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

            InputStream inputStream = connection.getInputStream();
            return readResponse(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static byte[] readResponse(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int currentReadPosition;
        byte[] readHolder = new byte[BUFFER_SIZE];

        while ((currentReadPosition = inputStream.read(readHolder, 0, readHolder.length)) != -1) {
            buffer.write(readHolder, 0, currentReadPosition);
        }

        buffer.flush();

        return buffer.toByteArray();
    }
}
