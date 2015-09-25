package edu.rit.gis.doctoreducator.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * The {@code FileHelper} assists with keeping all files in the same directory.
 */
public class FileHelper {

    private static final String DEFAULT_ROOT_DIRECTORY = "WebDoctor";

    private Context mContext;
    private File mRootDirectory;

    public FileHelper(Context context) {
        if (!isExternalStorageWritable()) {
            throw new RuntimeException("Unable to write to external storage");
        } else {
            mRootDirectory = new File(Environment.getExternalStorageDirectory(),
                    DEFAULT_ROOT_DIRECTORY);
            mContext = context;
            return;
        }
    }

    public FileHelper(Context context, File rootDirectory) {
        mRootDirectory = rootDirectory;
        mContext = context;
    }

    public File ensureDirectory(String path) {
        return ensureDirectory(new File(mRootDirectory, path));
    }

    /**
     * Ensures that the directory exists. Create it if it doesn't.
     * Throws a RuntimeException if it fails.
     *
     * @param directory - directory to create
     * @return the directory passed as an argument
     */
    private File ensureDirectory(File directory) {
        if (!directory.exists()) {
            boolean result = directory.mkdir();
            if (!result) {
                throw new RuntimeException("Failed to create directory " + directory);
            }
        }
        return directory;
    }

    public File getFile(String path, String name) {
        File dir = ensureDirectory(path);
        return new File(dir, name);
    }

    public File getFile(String pathAndName) {
        File file = new File(mRootDirectory, pathAndName);
        ensureDirectory(file.getParentFile());
        return file;
    }

    public File getFile(int id) {
        return getFile(mContext.getResources().getString(id));
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}
