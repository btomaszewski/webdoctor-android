package edu.rit.gis.doctoreducator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URL;

/**
 * Utility class with a bunch of misc methods with nowhere else to go.
 */
public class IOUtil {

    private static final int BUFFER_SIZE = 1024 * 2;
    private static final String UTF_8 = "UTF-8";

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
        byte[] stringData = data.getBytes(UTF_8);
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
        Reader inBus = new InputStreamReader(in, UTF_8);
        StringBuilder sb = new StringBuilder();
        char[] readBuffer = new char[BUFFER_SIZE];
        int length;
        while( (length = inBus.read(readBuffer)) > 0 ) {
            sb.append(readBuffer, 0, length);
        }
        inBus.close();
        return sb.toString();
    }
}
