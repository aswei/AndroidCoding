package cn.com.coding.androidcodinglibrary.utils.base;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;

import java.util.List;

import cn.com.coding.androidcodinglibrary.utils.log.LogUtils;


/**
 * 拨打电话，发送短信
 *
 * Created by
 * 作者：luoluo on 2017-2-23.
 *
 */

public class TelMsmUtils {

    public static final String SCHEME_TEL = "tel:";

    /**
     * 拨打电话
     *
     * @param context
     * @param phoneNumber 电话号码
     */
    public static void callPhone(final Context context, final String phoneNumber) {
        if (context == null) {
            throw new IllegalArgumentException("context can not be null.");
        }
        try {
            final Uri uri = Uri.parse(SCHEME_TEL + phoneNumber);
            final Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL);
            intent.setData(uri);
            context.startActivity(intent);
        } catch (Exception e) {
            LogUtils.e("CallTelException",e);
        }
    }

    /**
     * 发送短信息
     *
     * @param phoneNumber 接收号码
     * @param content     短信内容
     */
    private void toSendSMS(Context context, String phoneNumber, String content) {
        if (context == null) {
            throw new IllegalArgumentException("context can not be null.");
        }
        PendingIntent sentIntent = PendingIntent.getBroadcast(context, 0, new Intent(), 0);
        SmsManager smsManager = SmsManager.getDefault();

        if (content.length() >= 70) {
            //短信字数大于70，自动分条
            List<String> ms = smsManager.divideMessage(content);
            for (String str : ms) {
                //短信发送
                smsManager.sendTextMessage(phoneNumber, null, str, sentIntent, null);
            }
        } else {
            smsManager.sendTextMessage(phoneNumber, null, content, sentIntent, null);
        }
    }
}
