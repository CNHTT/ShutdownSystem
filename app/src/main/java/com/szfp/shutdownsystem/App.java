package com.szfp.shutdownsystem;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.HandlerThread;
import android.os.Process;
import android.util.Log;

import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
import com.google.code.microlog4android.appender.FileAppender;
import com.google.code.microlog4android.config.PropertyConfigurator;
import com.szfp.utils.Utils;


/**
 * author：ct on 2017/8/25 15:32
 * email：cnhttt@163.com
 */

public class App extends Application {
    private  String rootPath;

    public static  final Logger logger = LoggerFactory.getLogger();

    private HandlerThread handlerThread;

    public String getRootPath() {
        return rootPath;
    }

    public HandlerThread getHandlerThread() {
        return handlerThread;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        PropertyConfigurator.getConfigurator(this).configure();
        FileAppender fa = (FileAppender) logger.getAppender(1);
        fa.setAppend(true);
        logger.debug("---------Enter   APPLICATION-----------");
        handlerThread = new HandlerThread("handlerThread", Process.THREAD_PRIORITY_BACKGROUND);// New  Child Thread
        handlerThread.start();
        setRootPath();
    }

    private void setRootPath() {
        PackageManager manager = getPackageManager();

        try {
            PackageInfo info = manager.getPackageInfo(getPackageName(),0);
            rootPath = info.applicationInfo.dataDir;
            Log.d("ROOT PATH","-----------ROOT PATH ="+rootPath);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
