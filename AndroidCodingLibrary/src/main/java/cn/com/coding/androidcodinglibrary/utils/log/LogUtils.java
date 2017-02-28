package cn.com.coding.androidcodinglibrary.utils.log;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

/**
 * Created by
 * 作者：luoluo on 2017-2-23.
 *
 * Log工具类，可控制Log输出开关、保存Log到文件、过滤输出等级
 * Log工具，类似android.util.Log。 tag自动产生，格式:
 * customTagPrefix:className.methodName(Line:lineNumber),
 * customTagPrefix为空时只输出：className.methodName(Line:lineNumber)
 *
 * * 打印log的类, 会调用系统的log, 将调用该log的类名作为tag, 并且自动在log信息中添加方法信息和行信息.<br/>
 * 如果需要将Log保存到文件中, 只需要调用 {@link #enableFileLog(Context)} 方法.(默认不将Log保存到文件中) <br/>
 * 如果需要取消将Log保存到文件中, 只需要调用 {@link #disableFileLog()} 方法.(默认不将Log保存到文件中) <br/>
 *
 * <pre>
 *   如果应用程序的Application是继承自 BaseApplication}, 那么久不需要做特殊处理.
 *   否则的话, 推荐使用方式:
 *   在Application的onCreate方法中添加如下函数. 这样在打混淆包的时候, 不需要做额外的处理, 会自动取消打印Log.
 *   if(!BuildConfig.DEBUG){//如果为非debug模式
 *       L.disableDebug();
 *   }
 *
 * </pre>
 */

public class LogUtils {
    public static String customTagPrefix = "finddreams"; // 自定义Tag的前缀，可以是作者名
    private static final boolean isSaveLog = false; // 是否把保存日志到SD卡中
    public static final String ROOT = Environment.getExternalStorageDirectory()
            .getPath() + "/finddreams/"; // SD卡中的根目录
    private static final String PATH_LOG_INFO = ROOT + "info/";
    private static boolean DEBUG = true;
    private static boolean LOG_ON_FILE = false;
    private static String callerClazzName = null;
    private static String sMethodName = null;
    private static String packageName = "";
    /**
     * 获取系统分隔符
     */
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    /**
     * Json缩进
     */
    private static final int JSON_INDENT = 4;

    private LogUtils() {
    }

    // 容许打印日志的类型，默认是true，设置为false则不打印
    public static boolean allowD = true;
    public static boolean allowE = true;
    public static boolean allowI = true;
    public static boolean allowV = true;
    public static boolean allowW = true;
    public static boolean allowWtf = true;

    //日志存储写入
//    private static Boolean LOG_SWITCH = true; // 日志文件总开关
//    private static Boolean LOG_TO_FILE = false; // 日志写入文件开关
//    private static String LOG_TAG = "TAG"; // 默认的tag
//    private static char LOG_TYPE = 'v';// 输入日志类型，v代表输出所有信息,w则只输出警告...
    private static int LOG_SAVE_DAYS = 7;// sd卡中日志文件的最多保存天数
    private final static SimpleDateFormat LOG_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 日志的输出格式
    private final static SimpleDateFormat FILE_SUFFIX = new SimpleDateFormat("yyyy-MM-dd");// 日志文件格式
    private static String LOG_FILE_PATH; // 日志文件保存路径
    private static String LOG_FILE_NAME;// 日志文件保存名称

    private static String generateTag(StackTraceElement caller) {
        String tag = "%s.%s(Line:%d)"; // 占位符
        String callerClazzName = caller.getClassName(); // 获取到类名
        callerClazzName = callerClazzName.substring(callerClazzName
                .lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(),
                caller.getLineNumber()); // 替换
        tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":"
                + tag;
        sMethodName = new StringBuilder().append(
                caller.getMethodName() + "->" + caller.getLineNumber() + ": ")
                .toString();
        return tag;
    }

    /**
     * 自定义的logger
     */
    public static CustomLogger customLogger;

    public interface CustomLogger {
        void d(String tag, String content);

        void d(String tag, String content, Throwable tr);

        void e(String tag, String content);

        void e(String tag, String content, Throwable tr);

        void i(String tag, String content);

        void i(String tag, String content, Throwable tr);

        void v(String tag, String content);

        void v(String tag, String content, Throwable tr);

        void w(String tag, String content);

        void w(String tag, String content, Throwable tr);

        void w(String tag, Throwable tr);

        void wtf(String tag, String content);

        void wtf(String tag, String content, Throwable tr);

        void wtf(String tag, Throwable tr);
    }

    public static void d(String content) {
        if (!allowD)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.d(tag, content);
        } else {
            Log.d(tag, content);
        }
    }

    public static void d(String content, Throwable tr) {
        if (!allowD)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.d(tag, content, tr);
        } else {
            Log.d(tag, content, tr);
        }
    }

    public static void e(String content) {
        if (!allowE)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.e(tag, content);
        } else {
            Log.e(tag, content);
        }
        if (isSaveLog) {
            point(PATH_LOG_INFO, tag, content);
        }
    }

    public static void e(String content, Throwable tr) {
        if (!allowE)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.e(tag, content, tr);
        } else {
            Log.e(tag, content, tr);
        }
        if (isSaveLog) {
            point(PATH_LOG_INFO, tag, tr.getMessage());
        }
    }

    public static void i(String content) {
        if (!allowI)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.i(tag, content);
        } else {
            Log.i(tag, content);
        }

    }

    public static void i(String content, Throwable tr) {
        if (!allowI)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.i(tag, content, tr);
        } else {
            Log.i(tag, content, tr);
        }

    }

    public static void v(String content) {
        if (!allowV)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.v(tag, content);
        } else {
            Log.v(tag, content);
        }
    }

    public static void v(String content, Throwable tr) {
        if (!allowV)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.v(tag, content, tr);
        } else {
            Log.v(tag, content, tr);
        }
    }

    public static void w(String content) {
        if (!allowW)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.w(tag, content);
        } else {
            Log.w(tag, content);
        }
    }

    public static void w(String content, Throwable tr) {
        if (!allowW)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.w(tag, content, tr);
        } else {
            Log.w(tag, content, tr);
        }
    }

    public static void w(Throwable tr) {
        if (!allowW)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.w(tag, tr);
        } else {
            Log.w(tag, tr);
        }
    }

    public static void wtf(String content) {
        if (!allowWtf)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.wtf(tag, content);
        } else {
            Log.wtf(tag, content);
        }
    }

    public static void wtf(String content, Throwable tr) {
        if (!allowWtf)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.wtf(tag, content, tr);
        } else {
            Log.wtf(tag, content, tr);
        }
    }

    public static void wtf(Throwable tr) {
        if (!allowWtf)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.wtf(tag, tr);
        } else {
            Log.wtf(tag, tr);
        }
    }

    private static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }

    public static void point(String path, String tag, String msg) {
        if (isSDAva()) {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("",
                    Locale.SIMPLIFIED_CHINESE);
            dateFormat.applyPattern("yyyy");
            path = path + dateFormat.format(date) + "/";
            dateFormat.applyPattern("MM");
            path += dateFormat.format(date) + "/";
            dateFormat.applyPattern("dd");
            path += dateFormat.format(date) + ".log";
            dateFormat.applyPattern("[yyyy-MM-dd HH:mm:ss]");
            String time = dateFormat.format(date);
            File file = new File(path);
            if (!file.exists())
                createDipPath(path);
            BufferedWriter out = null;
            try {
                out = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(file, true)));
                out.write(time + " " + tag + " " + msg + "\r\n");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 根据文件路径 递归创建文件
     *
     * @param file
     */
    public static void createDipPath(String file) {
        String parentFile = file.substring(0, file.lastIndexOf("/"));
        File file1 = new File(file);
        File parent = new File(parentFile);
        if (!file1.exists()) {
            parent.mkdirs();
            try {
                file1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * A little trick to reuse a formatter in the same thread
     */
    private static class ReusableFormatter {

        private Formatter formatter;
        private StringBuilder builder;

        public ReusableFormatter() {
            builder = new StringBuilder();
            formatter = new Formatter(builder);
        }

        public String format(String msg, Object... args) {
            formatter.format(msg, args);
            String s = builder.toString();
            builder.setLength(0);
            return s;
        }
    }

    private static final ThreadLocal<ReusableFormatter> thread_local_formatter = new ThreadLocal<ReusableFormatter>() {
        protected ReusableFormatter initialValue() {
            return new ReusableFormatter();
        }
    };

    public static String format(String msg, Object... args) {
        ReusableFormatter formatter = thread_local_formatter.get();
        return formatter.format(msg, args);
    }

    public static boolean isSDAva() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)
                || Environment.getExternalStorageDirectory().exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 打开日志文件并写入日志
     *
     * @return
     **/
    private synchronized static void log2File(String mylogtype, String tag, String text) {
        Date nowtime = new Date();
        String date = FILE_SUFFIX.format(nowtime);
        String dateLogContent = LOG_FORMAT.format(nowtime) + ":" + mylogtype + ":" + tag + ":" + text; // 日志输出格式
        File destDir = new File(LOG_FILE_PATH);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        File file = new File(LOG_FILE_PATH, LOG_FILE_NAME + date);
        try {
            FileWriter filerWriter = new FileWriter(file, true);
            BufferedWriter bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(dateLogContent);
            bufWriter.newLine();
            bufWriter.close();
            filerWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除指定的日志文件
     */
    public static void delFile() {// 删除日志文件
        String needDelFiel = FILE_SUFFIX.format(getDateBefore());
        File file = new File(LOG_FILE_PATH, needDelFiel + LOG_FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 得到LOG_SAVE_DAYS天前的日期
     *
     * @return
     */
    private static Date getDateBefore() {
        Date nowtime = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(nowtime);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - LOG_SAVE_DAYS);
        return now.getTime();
    }

    /**
     * 格式化Json字符串 不打tag的情况下，默认使用当前类名作为Tag
     * @param msg
     */
    public static void json(String msg){

        if (!DEBUG)
            return;

        if (TextUtils.isEmpty(msg)) {
            LogUtils.d("Empty or Null json content");
            return;
        }
        StackTraceElement caller = getCallerStackTraceElement();
        generateTag(caller);

        String message = "";

        try {
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.toString(JSON_INDENT);
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(JSON_INDENT);
            }
        } catch (JSONException e) {
            Log.d(callerClazzName,e.getCause().getMessage() + "\n" + msg);
            return;
        }

        printLine(callerClazzName, true);

        message = callerClazzName + "->" + sMethodName  + LINE_SEPARATOR + message;
        String[] lines = message.split(LINE_SEPARATOR);
        StringBuilder jsonContent = new StringBuilder();
        for (String line : lines) {
            jsonContent.append("║ ").append(line).append(LINE_SEPARATOR);
        }
        Log.d(callerClazzName, jsonContent.toString());

        printLine(callerClazzName, false);

        if (LOG_ON_FILE)
            logOnFile(Log.DEBUG, callerClazzName, message);

    }

    /**
     * 格式化json 并打自己标签
     * @param tag  tag
     * @param msg  json
     */
    public static void jsonT(String tag , String msg){

        StackTraceElement caller = getCallerStackTraceElement();
        generateTag(caller);

        String message = "";

        try {
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.toString(JSON_INDENT);
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(JSON_INDENT);
            }
        } catch (JSONException e) {
            d(e.getCause().getMessage() + "\n" + msg);
            return;
        }

        printLine(tag, true);

        message = callerClazzName + "->" + sMethodName  + LINE_SEPARATOR + message;
        String[] lines = message.split(LINE_SEPARATOR);
        StringBuilder jsonContent = new StringBuilder();
        for (String line : lines) {
            jsonContent.append("║ ").append(line).append(LINE_SEPARATOR);
        }
        Log.d(tag, jsonContent.toString());

        printLine(tag, false);

        if (LOG_ON_FILE)
            logOnFile(Log.DEBUG, tag, message);

    }
    private static void printLine(String tag, boolean isTop) {
        if (isTop) {
            Log.d(tag, "╔═══════════════════════════════════════════════════════════════════════════");
        } else {
            Log.d(tag, "╚═══════════════════════════════════════════════════════════════════════════");
        }
    }

    private static void logOnFile(int level, String tag, String text) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.CHINA);
        String time = sdf.format(new Date());
        FileWriter writer = null;
        try {
            File logFile = new File(getCachedFolder() + "log.log");
            if (logFile.exists() && logFile.length() > 100 * 1024) {// 大于100KB
                logFile.renameTo(new File(getCachedFolder() + "log_" + time
                        + ".log"));
            }

            writer = new FileWriter(logFile, true);
            writer.write(time + "/" + level + "/" + tag + "/" + text);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) {

            }
        }

    }

    /**
     * 获取Log缓存路径
     *
     * @return Log缓存路径
     */
    private static String getCachedFolder() {
        String path;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {// 有SD卡
            path = Environment.getExternalStorageDirectory().getPath()
                    + "/Android/data/" + packageName + "/log/";
        } else {
            path = Environment.getDataDirectory().getPath() + "/data/"
                    + packageName + "/log/";
        }

        File file = new File(path);
        if (!file.isDirectory()) {
            file.mkdirs();
        }

        return path;
    }

    /**
     * 允许将Log保存到文件中(默认不保存到文件中)
     */
    public static void enableFileLog(Context context) {
        LOG_ON_FILE = true;

        if (context != null)
            packageName = context.getPackageName();
    }

    /**
     * 取消将Log保存到文件中(默认不保存到文件中)
     */
    public static void disableFileLog() {
        LOG_ON_FILE = false;
    }
}
