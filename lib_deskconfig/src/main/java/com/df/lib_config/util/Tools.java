package com.df.lib_config.util;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

final public class Tools {
    private Tools() {
    }

    private static final String TAG = "Tools";

    public static <T> void saveDataToDesk(T data, String dataPath) {
        File file = new File(dataPath);
        if (file.exists() && file.canRead()) {
            file.delete();
        }
        FileWriter fwriter = null;
        try {
            fwriter = new FileWriter(dataPath, false);
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.excludeFieldsWithoutExposeAnnotation();
            Gson gson = gsonBuilder.create();
            fwriter.write(gson.toJson(data));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (fwriter != null) {
                    fwriter.flush();
                    fwriter.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    public static <T> T loadDeskData(Class<T> clazz, String dataPath) {
        InputStream in = null;
        try {
            File file = new File(dataPath);
            if (file == null || !file.exists() || !file.canRead()) {
                Log.e(TAG, "loadDeskData() called fail,can not access this file:" + dataPath);
                return null;
            }
            in = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            if (!TextUtils.isEmpty(jsonString)) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.excludeFieldsWithoutExposeAnnotation();
                Gson gson = gsonBuilder.create();
                return gson.fromJson(jsonString.toString(), clazz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static boolean copyFileUsingStream(File source, File dest) {
        Log.d(TAG, "copyFileUsingStream() called with: source = [" + source + "], dest = [" + dest + "]");
        try (InputStream is = new FileInputStream(source); OutputStream os = new FileOutputStream(dest)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
