package com.szfp.ss;

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
import com.szfp.ss.retrofit.HttpUtil;
import com.szfp.ss.utils.GreenDaoManager;
import com.szfp.utils.Utils;

import android_serialport_api.M1CardAPI;


/**
 * author：ct on 2017/8/25 15:32
 * email：cnhttt@163.com
 */

public class App extends Application {

    public static boolean isHF =true;


    public static String localhost = "http://192.168.1.117:8080";
    private  String rootPath;
    public static String companyUUID="";
    //操作员编号 信息
    public static String managerUUID="";
    public static String operator = "00001";

    //终端号信息
    public static String terminalNumber="00001";
    public static String terminalUUID="00001";


    //公司信息
    public static String companyName="SZFP TECHNOLOGY LIMITED";
    public static String tel = "0086-0755-86276295";

    public static String website ="http://www.szfptech.com";

    public static String address="Room 506-507,HuaTong Business Building,XinGao Road,XiLi Town,NanShan District,ShenZhen,China.Post Code 518055";


    //停车场编号
    public static String parkingNumber="000009";
    public static String parkingLotUUID="";

    public static  final Logger logger = LoggerFactory.getLogger();

    private HandlerThread handlerThread;

    public String getRootPath() {
        return rootPath;
    }

    public HandlerThread getHandlerThread() {
        return handlerThread;
    }


    /**
     * 采用HF  默认数据
     */

    public  static  int KeyType= M1CardAPI.KEY_A;  //卡片类型
    public  static String DefaultKeyA = "ffffffffffff";// 默认密码A
    public  static String DefaultKeyB = "ffffffffffff";// 默认密码B
    public  static  int num =1;  //每次执行一次
    public  static  int block =2;  //获取块号
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
        new HttpUtil.SingletonBuilder(this,localhost)
//                .addServerUrl("")//备份服务器ip地址，可多次调用传递
//                .addCallFactory()//不传默认StringConverterFactory
//                .addConverterFactory()//不传默认RxJavaCallAdapterFactory
//                .client()//OkHttpClient,不传默认OkHttp3
//                .paramsInterceptor(mParamsInterceptor)//不传不进行参数统一处理
//               .headersInterceptor(mHeadersInterceptor)//不传不进行headers统一处理
                .build();

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
