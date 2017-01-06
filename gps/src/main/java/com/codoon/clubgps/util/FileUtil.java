package com.codoon.clubgps.util;

import android.graphics.Bitmap;
import android.os.Environment;

import com.amap.api.trace.TraceLocation;
import com.codoon.clubgps.application.GPSApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * Created by Frankie on 2017/1/5.
 */

public class FileUtil {

    private static final String RECORD_THUM_FOLDER = "record_thum";

    /**
     * 保存GPS缩略图
     *
     * @param record_id
     * @return
     */
    public static File saveGPSThum(Bitmap thumBitmap, String record_id) {
        File thumFile = createThumFile(RECORD_THUM_FOLDER, record_id);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(thumFile);
            thumBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return thumFile;
    }

    public static File writePoints(List<TraceLocation> list){
        File logFile = createTraceLogFile("trace_log");
        try {
            Writer out = new FileWriter(logFile,true);
            int i = 1;
            for(TraceLocation traceLocation : list){
                out.write("\r\n↓↓↓↓↓↓↓↓↓↓↓第"+i+"个点↓↓↓↓↓↓↓↓↓↓↓\r\n");
                out.write(traceLocation.toString()+"\r\n");
                i++;
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return logFile;
    }

    /**
     * 创建保存GPS路线缩略图的文件
     *
     * @return
     */
    private static File createThumFile(String folderName, String fileName) {
        File folder;

        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 已挂载 SDCard
            folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        } else {
            folder = GPSApplication.getAppContext().getCacheDir();
        }

        folder = new File(folder, folderName);
        if (!folder.exists())
            folder.mkdir();

        createNoMediaFile(folder);
        return new File(folder, "record_thum_"+fileName+".jpg");
    }

    /**
     * 创建保存GPS路线内容保存的文件
     *
     * @return
     */
    private static File createTraceLogFile(String folderName) {
        File folder;

        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 已挂载 SDCard
            folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        } else {
            folder = GPSApplication.getAppContext().getCacheDir();
        }

        folder = new File(folder, folderName);
        if (!folder.exists())
            folder.mkdir();

        createNoMediaFile(folder);
        return new File(folder, "trace_log.txt");
    }

    /**
     * 创建.nomedia文件，防止系统图库扫描该文件夹
     *
     * @param folder
     */
    private static void createNoMediaFile(File folder) {
        File nomedia = new File(folder, ".nomedia");
        if (!nomedia.exists())
            try {
                nomedia.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

}
