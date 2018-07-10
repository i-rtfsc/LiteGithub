/*
 * Copyright (c) 2018 anqi.huang@outlook.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.journeyOS.base.persistence;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.journeyOS.base.utils.JsonHelper;
import com.journeyOS.litetask.BackgroundWork;
import com.journeyOS.litetask.Completion;
import com.journeyOS.litetask.LiteTasks;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Helper class to do operations on regular files/directories.
 */
public class FileHelper {
    private static final String TAG = FileHelper.class.getSimpleName();
    private static String mAppName;

    public static void init(String appName) {
        mAppName = appName;
    }

    /**
     * SDCard root
     */
    public static String sdcardRoot() {
        return Environment.getExternalStorageDirectory().toString();
    }

    public static String getAppFolder() {
        // Create the application workspace
        File cacheDir = new File(sdcardRoot() + File.separator + mAppName + File.separator);
        if (!cacheDir.exists()) {
            makeDir(cacheDir);
        }
        return cacheDir.getPath();
    }

    public static String getLogTrace() {

        File logDir = new File(sdcardRoot() + File.separator + mAppName + File.separator + "log");
        if (!logDir.exists()) {
            makeDir(logDir);
        }
        return logDir.getPath();
    }

    public static boolean makeDir(File dir) {
        if (!dir.exists()) {
            return dir.mkdirs();
        }
        return (dir.exists() && dir.isDirectory());
    }

    /**
     * Build a file, used to be inserted in the disk cache.
     *
     * @param fileId The name build the file.
     * @return A valid file.
     */
    public static File buildFile(String fileId) {
        File file = new File(getAppFolder(), fileId);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }


    /**
     * @param fileId The fileId to write to Disk.
     */
    public static void writeToFile(Context context, String fileId, final Object fileObj) {
        if (fileObj == null) {
            return;
        }
        final File file = buildFile(fileId);
        if (exists(file)) {
            LiteTasks.executeInBackground(context, new BackgroundWork<Boolean>() {
                @Override
                public Boolean doInBackground() {
                    try {
                        String content = fileObj.getClass().equals(String.class)
                                ? fileObj.toString() : JsonHelper.toJson(fileObj);
                        FileWriter writer = new FileWriter(file);
                        writer.write(content);
                        writer.close();
                        return true;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }, new Completion<Boolean>() {
                @Override
                public void onSuccess(Context context, Boolean result) {
                    Log.d(TAG, "write to disk success");
                }

                @Override
                public void onError(Context context, Exception e) {
                    Log.w(TAG, "write to disk error");
                }
            });
        }
    }

    /**
     * 同步读取一个文件，will be block
     */
    public static String readFileContent(String fileId) {
        final File file = buildFile(fileId);
        StringBuilder fileContentBuilder = new StringBuilder();
        if (file.exists()) {
            String stringLine;
            try {
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                while ((stringLine = bufferedReader.readLine()) != null) {
                    fileContentBuilder.append(stringLine).append("\n");
                }
                bufferedReader.close();
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileContentBuilder.toString();
    }

    /**
     * 同步读取一个文件，根据类型返回
     */
    public static <T> T readFileContent(String fileId, Class<T> ype) {
        String fileContent = readFileContent(fileId);
        return JsonHelper.fromJson(fileContent, ype);
    }


    public static boolean isCached(String fileId) {
        final File entityFile = buildFile(fileId);
        return exists(entityFile);
    }

    public static boolean exists(File file) {
        return file.exists();
    }

    /**
     * Warning: Deletes the content of a directory.
     * This is an I/O operation and this method executes in the main thread, so it is recommended to
     * perform the operation using another thread.
     *
     * @param directory The directory which its content will be deleted.
     */
    public static void clearDirectory(Context context, final File directory) {
        LiteTasks.executeInBackground(context, new BackgroundWork<Boolean>() {
            @Override
            public Boolean doInBackground() {
                boolean result = false;
                if (directory.exists()) {
                    for (File file : directory.listFiles()) {
                        result = file.delete();
                    }
                }
                return result;
            }
        }, new Completion<Boolean>() {
            @Override
            public void onSuccess(Context context, Boolean result) {
                Log.d(TAG, "clear directory success");
            }

            @Override
            public void onError(Context context, Exception e) {
                Log.w(TAG, "clear directory error");
            }
        });
    }

    public static String assetFile2String(String fileName, Context context) {
        StringBuilder result = new StringBuilder();
        InputStreamReader inputReader = null;
        BufferedReader bufReader = null;
        try {
            inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            bufReader = new BufferedReader(inputReader);
            String line;

            while ((line = bufReader.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeIO(inputReader, bufReader);

        return result.toString();
    }


    /**
     * 关闭IO
     *
     * @param closeables closeable
     */
    public static void closeIO(Closeable... closeables) {
        if (closeables == null) {
            return;
        }
        try {
            for (Closeable closeable : closeables) {
                if (closeable != null) {
                    closeable.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
