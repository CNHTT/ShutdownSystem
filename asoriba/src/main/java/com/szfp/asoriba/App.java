package com.szfp.asoriba;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.HandlerThread;
import android.os.Process;
import android.util.Log;

import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
import com.google.code.microlog4android.appender.FileAppender;
import com.google.code.microlog4android.config.PropertyConfigurator;
import com.szfp.asoriba.utils.GreenDaoManager;
import com.szfp.utils.Utils;

import org.litepal.LitePalApplication;

/**
 * author：ct on 2017/10/9 11:34
 * email：cnhttt@163.com
 */

public class App extends LitePalApplication {

    public  static String email  ="client@asoriba.com";
    public  static String password = "thepassword123";

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
        GreenDaoManager.getInstance();
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
