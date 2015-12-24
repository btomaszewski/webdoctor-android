package edu.rit.gis.vurawiga.utils;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;

/**
 * Utility class for a bunch of misc IO methods with nowhere else to go.
 * You should feel bad for these poor, homeless methods. Maybe give them a home
 * if you can. Donate money to them, for they are poor and homeless and it would
 * be charitable of you. These methods regularly go to food shelters to get food
 * because otherwise they would starve.
 */
public class IOUtil {

    /** Tag for logging */
    private static final String LOG_TAG = "IOUtil";

    /** Preferred charset for the application. */
    private static final String PREFERRED_CHARSET = "UTF-8";

    /** Size (in bytes) of the buffer used for downloading data and copying streams */
    private static final int BUFFER_SIZE = 1024 * 2;

    /** The charset to use when converting Strings to bytes and vice versa */
    private static Charset appCharset;


    /**
     * Copy data from the input to the output streams. Note that the caller is responsible for
     * calling {@code close()} on the provided streams.
     *
     * @param input - Stream to read from
     * @param output - Stream to write to
     * @throws IOException - in case an IO problem happens
     */
    public static void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int length;
        while((length = input.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }
    }

    /**
     * Downloads the file at the given url and saves it to the output file.
     * @param url - the url to download from
     * @param file - the file to which we will save the content
     */
    public static void downloadFile(URL url, File file) throws IOException {
        File directory = file.getParentFile();
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }

        //this will be used to write the downloaded data into the file we created
        FileOutputStream fileOutput = null;
        //this will be used in reading the data from the internet
        InputStream inputStream = null;
        try {
            fileOutput = new FileOutputStream(file);
            inputStream = url.openStream();
            // now simply do a stream copy
            copyStream(inputStream, fileOutput);
        } finally {
            // ... and close the files
            if (fileOutput != null) {
                fileOutput.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    /**
     * Convert String to bytes in UTF-8 and write data to output stream.
     * Note: this will call close() on the stream.
     *
     * @param out - where to write the data
     * @param data - what data to write
     * @throws IOException - when things go wrong, and dark clouds fill
     *                       the sky.
     */
    public static void writeString(OutputStream out, String data)
            throws IOException {
        byte[] stringData = data.getBytes(getCharset());
        out.write(stringData);
        out.close();
    }

    /**
     * Read an InputStream and convert it to a String.
     * Note: this will call close() on the stream.
     *
     * @param in - input stream to read
     * @return a string containing the data from the input stream
     * @throws IOException - when a read error occurs
     */
    public static String readString(InputStream in) throws IOException {
        Reader inBus = new InputStreamReader(in, getCharset());
        StringBuilder sb = new StringBuilder();
        char[] readBuffer = new char[BUFFER_SIZE];
        int length;
        while( (length = inBus.read(readBuffer)) > 0 ) {
            sb.append(readBuffer, 0, length);
        }
        inBus.close();
        return sb.toString();
    }

    /**
     * Get the charset to use for converting strings to and from bytes.
     */
    public static Charset getCharset() {
        if (appCharset == null) {
            try {
                appCharset = Charset.forName(PREFERRED_CHARSET);
            } catch (IllegalCharsetNameException | UnsupportedCharsetException e) {
                appCharset = Charset.defaultCharset();
                Log.w(LOG_TAG, "Preferred charset (" + PREFERRED_CHARSET + ") not available");
            }
        }
        return appCharset;
    }

    /**
     * Convenience method to get the name of the charset we're using.
     */
    public static String getCharsetName() {
        return getCharset().name();
    }

    /**
     * Configure the RestHelper to send and receive json data. This sets
     * the Content-Type and Accept headers.
     *
     * @param helper - helper to configure
     * @return the helper
     */
    public static RestHelper configureJson(RestHelper helper) {
        helper.setHeader("Content-Type", "application/json");
        helper.setHeader("Accept", "application/json");
        return helper;
    }
}
