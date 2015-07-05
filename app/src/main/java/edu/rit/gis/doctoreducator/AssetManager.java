package edu.rit.gis.doctoreducator;


import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AssetManager {

    private static final String ASSETS_FILE = "assets.json";
    private static final String ASSET_DIRECTORY = "assets/";
    private static final String LIST_URL = "content/list?latest=true";

    private FileHelper mFileHelper;
    private File mAssetFile;
    private Map<String, JSONObject> mAssetMap;
    private Context mContext;

    public AssetManager(Context context) throws IOException, JSONException {
        this(context, true);
    }

    /**
     * Create a new AssetManager.
     *
     * Generally {@code read} should only be false if you encountered an exception
     * when read was true.
     *
     * @param context - context necessary to create the FileHelper
     * @param read - read the current asset file or not
     * @throws IOException
     * @throws JSONException
     */
    public AssetManager(Context context, boolean read) throws IOException, JSONException {
        mContext = context;
        mFileHelper = new FileHelper(context);
        mAssetFile = mFileHelper.getFile(ASSETS_FILE);
        if (read) {
            mAssetMap = readCurrentAssets();
        } else {
            mAssetMap = new HashMap<>();
        }
    }

    public Map<String, JSONObject> getCurrentAssets() {
        return mAssetMap;
    }

    /**
     * Determine which file the given asset is in.
     *
     * @param name - Name of asset object already stored in the asset manager
     */
    public File determineFile(String name) throws JSONException {
        return determineFile(mAssetMap.get(name));
    }

    /**
     * Determine which file the given asset is in.
     *
     * @param assetObj - ContentFile object from REST server
     */
    public File determineFile(JSONObject assetObj) throws JSONException {
        String filename = assetObj.getString("name") + "." + assetObj.getString("type");
        return mFileHelper.getFile(ASSET_DIRECTORY, filename);
    }

    /**
     * Read the current assets file. If it doesn't yet exist an empty map will be returned.
     *
     * @return a map of names to {@code JSONObject}s.
     * @throws IOException
     * @throws JSONException
     */
    private Map<String, JSONObject> readCurrentAssets() throws IOException, JSONException {
        File assetFile = mFileHelper.getFile(ASSETS_FILE);
        if (!assetFile.exists()) {
            return new HashMap<>();
        } else {
            Map<String, JSONObject> assetMap = new HashMap<>();
            JSONObject currentAssets = new JSONObject(IOUtil.readString(
                    new FileInputStream(assetFile)));
            Iterator<String> names = currentAssets.keys();
            while(names.hasNext()) {
                String name = names.next();
                assetMap.put(name, currentAssets.getJSONObject(name));
            }
            return assetMap;
        }
    }

    /**
     * Check if the next version is greater than the current one.
     *
     * If there is no current version it will always return true.
     *
     * @param next - a JSONObject with a "version" field
     * @return if the next object's version is greater than the current object's version
     * @throws JSONException if there is a JSON exception
     */
    public boolean isNewVersion(JSONObject next) throws JSONException {
        return isNewVersion(next, mAssetMap.get(next.getString("name")));
    }

    /**
     * Check if the next version is greater than the current one.
     *
     * If {@code current} is null it will always be considered out of date
     *
     * @param next - a JSONObject with a "version" field
     * @param current - a JSONObject with a "version" field or null
     * @return if the next object's version is greater than the current object's version
     * @throws JSONException if there is a JSON exception
     */
    public boolean isNewVersion(JSONObject next, JSONObject current) throws JSONException {
        return current == null || next.getInt("version") > current.getInt("version");
    }

    /**
     * Update an asset.
     *
     * @param asset - the asset data
     * @throws JSONException
     */
    public void updateAsset(JSONObject asset) throws JSONException {
        validateJSON(asset);
        mAssetMap.put(asset.getString("name"), asset);
    }

    /**
     * Save the asset data to the asset file.
     *
     * @throws IOException
     */
    public void save() throws IOException, JSONException {
        JSONObject storageObject = new JSONObject();
        for (JSONObject obj : mAssetMap.values()) {
            storageObject.put(obj.getString("name"), obj);
        }
        FileWriter writer = new FileWriter(mAssetFile);
        writer.write(storageObject.toString());
        writer.close();
    }

    private boolean validateJSON(JSONObject obj) throws JSONException {
        // if all of these succeed we're good
        obj.getString("name");
        obj.getInt("version");
        obj.getString("type");
        obj.getString("file");
        return true;
    }

    public void updateAllAssets() throws IOException, JSONException {
        try {
            RestHelper rest = new RestHelper(mContext.getString(R.string.url_base));

            // first we grab the list of the latest content
            JSONArray newFilesArray = new JSONArray(rest.sendGET(
                    rest.resolve(LIST_URL)));

            // now we try to download each of them
            for (int i = 0; i < newFilesArray.length(); i++) {
                JSONObject nextFile = newFilesArray.getJSONObject(i);
                if (isNewVersion(nextFile)) {
                    // download the file
                    File output = determineFile(nextFile);
                    IOUtil.downloadFile(new URL(nextFile.getString("file")), output);
                    // file is downloaded so update the data
                    updateAsset(nextFile);
                }
            }
        } finally {
            save();
        }
    }
}
