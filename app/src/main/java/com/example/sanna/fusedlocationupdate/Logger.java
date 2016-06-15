package com.example.sanna.fusedlocationupdate;

import android.os.Environment;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.io.*;

public class Logger {

    public static FileHandler logger = null;
    private static String filename = "MapDisarm_Log", logFileUpdated;
    public static File logFile;
    static boolean isExternalStorageAvailable = false;
    static boolean isExternalStorageWriteable = false;
    public static int flag1 = 1;
    public static String logFileName;
    static String state = Environment.getExternalStorageState();
    public static String phoneID;

    /*public Logger(String phoneVal) {
        this.phoneID = phoneVal;
    }*/

    public static void addRecordToLog(String message) {

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            isExternalStorageAvailable = isExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            isExternalStorageAvailable = true;
            isExternalStorageWriteable = false;
        } else {
            // Something else is wrong. It may be one of many other states, but all we need
            //  to know is we can neither read nor write
            isExternalStorageAvailable = isExternalStorageWriteable = false;
        }

        // Log File Updated with every new entry for onLocationChanged()
        File dir = Environment.getExternalStoragePublicDirectory("DMS/Map/");
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            if(!dir.exists()) {
                Log.d("Dir created ", "Dir created ");
                dir.mkdirs();
            }
            File[] foundFiles = dir.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {

                    return name.startsWith("MapDisarm_Log");
                }
            });

            Log.v("Found Files starting with MapDisarm_Log:" , foundFiles.length+"");

            if(foundFiles != null && foundFiles.length > 0)
            {
                logFile = new File(foundFiles[0].toString());
                Log.v("LogFile:", foundFiles[0].toString());
            }
            else {
                DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                String date = df.format(Calendar.getInstance().getTime());
                logFile = new File(dir, filename + "_" + phoneID + "_" + date + ".txt");
                if (!logFile.exists()) {
                    try {
                        Log.d("File created ", "File created ");
                        logFile.createNewFile();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            try {
                Log.v("log file check :", logFile.toString());
                FileWriter buf = new FileWriter(logFile, true);
                DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                String date = df.format(Calendar.getInstance().getTime());
                buf.write(message+','+date +"\r\n");

                buf.flush();
                buf.close();


                logFileUpdated = logFile.toString().substring(0, logFile.toString().length()-19);

//                Log.v("Phone ID: ", phoneID);
                Log.v("Log File:",logFile.toString());
                File to = new File(logFileUpdated.toString() + "_" + date + ".txt");
                Log.v("Log File Updated:",logFileUpdated.toString()+ "_" + date + ".txt");

                boolean success = logFile.renameTo(to);

                Log.v("Success:",String.valueOf(success));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
