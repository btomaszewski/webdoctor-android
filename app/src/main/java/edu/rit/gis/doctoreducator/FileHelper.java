package edu.rit.gis.doctoreducator;

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
        mRootDirectory = new File(Environment.getExternalStorageDirectory(),
                DEFAULT_ROOT_DIRECTORY);
        mContext = context;
    }

    public FileHelper(Context context, File rootDirectory) {
        mRootDirectory = rootDirectory;
        mContext = context;
    }

    public File ensureDirectory(String path) {
        File dir = new File(mRootDirectory, path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public File getFile(String path, String name) {
        File dir = ensureDirectory(path);
        return new File(dir, name);
    }

    public File getFile(String pathAndName) {
        File file = new File(mRootDirectory, pathAndName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
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
