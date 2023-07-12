package com.phone.rxjava2andretrofit2.receiver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.service.notification.StatusBarNotification
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import cn.jpush.android.api.JPushInterface
import com.phone.library_base.manager.LogManager
import com.phone.library_base.manager.SharedPreferencesManager
import com.phone.library_common.manager.MapManager.jsonStrToMap
import com.phone.library_login.ui.LoginActivity
import com.phone.module_main.MainActivity
import com.phone.module_main.R
import org.json.JSONException
import org.json.JSONObject


class MainJpushReceiver : BroadcastReceiver() {

    companion object {
        private val TAG = MainJpushReceiver::class.java.simpleName
    }

    private val NOTIFICATION_ID = 106 //推送ID
    private val NOTIFICATION_SHOW_SHOW_AT_MOST = 5 //推送通知最多显示条数


    override fun onReceive(context: Context, intent: Intent) {
        LogManager.i(TAG, "onReceive")
        try {
            val bundle = intent.extras
            LogManager.i(
                TAG,
                "onReceive*******" + intent.action + ", extras: " + printBundle(bundle)
            )
            if (JPushInterface.ACTION_REGISTRATION_ID == intent.action) {
                val regId = bundle!!.getString(JPushInterface.EXTRA_REGISTRATION_ID)
                LogManager.i(TAG, "[MyReceiver] 接收Registration Id : $regId")
                //send the Registration Id to your server...
            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED == intent.action) {
                LogManager.i(
                    TAG,
                    "ACTION_MESSAGE_RECEIVED 接收到推送下来的自定义消息: " + bundle!!.getString(
                        JPushInterface.EXTRA_MESSAGE
                    )
                )
                LogManager.i(TAG, "ACTION_MESSAGE_RECEIVED processCustomMessage")
                //在这里自定义通知声音（需要服务端配置）
                processCustomMessage(context, bundle)
            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED == intent.action) {
                val title = bundle!!.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE)
                val content = bundle.getString(JPushInterface.EXTRA_ALERT)
                val extra = bundle.getString(JPushInterface.EXTRA_EXTRA)
                LogManager.i(
                    TAG,
                    "ACTION_NOTIFICATION_RECEIVED 标题:【$title】，内容：【$content】，附加参数:【$extra】"
                )
                LogManager.i(TAG, "ACTION_NOTIFICATION_RECEIVED 接收到推送下来的通知")
            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED == intent.action) {
                LogManager.i(TAG, "[MyReceiver] 用户点击打开了通知")

                //打开自定义的Activity
                val i = Intent(context, MainActivity::class.java)
                i.putExtras(bundle!!)
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                context.startActivity(i)
            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK == intent.action) {
                LogManager.i(
                    TAG,
                    "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle!!.getString(
                        JPushInterface.EXTRA_EXTRA
                    )
                )
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
            } else if (JPushInterface.ACTION_CONNECTION_CHANGE == intent.action) {
                val connected =
                    intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false)
                LogManager.i(
                    TAG,
                    "[MyReceiver]" + intent.action + " connected state change to " + connected
                )
            } else {
                LogManager.i(TAG, "[MyReceiver] Unhandled intent - " + intent.action)
            }
        } catch (e: Exception) {
        }
    }

    // 打印所有的 intent extra 数据
    private fun printBundle(bundle: Bundle?): String {
        val stringBuilder = StringBuilder()
        for (key: String in bundle!!.keySet()) {
            if (key == JPushInterface.EXTRA_NOTIFICATION_ID) {
                stringBuilder.append("\nkey:" + key + ", value:" + bundle.getInt(key))
            } else if (key == JPushInterface.EXTRA_CONNECTION_CHANGE) {
                stringBuilder.append("\nkey:" + key + ", value:" + bundle.getBoolean(key))
            } else if (key == JPushInterface.EXTRA_EXTRA) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    LogManager.i(TAG, "This message has no Extra data")
                    continue
                }
                try {
                    val json = JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA))
                    val it: Iterator<String> = json.keys()
                    while (it.hasNext()) {
                        val myKey = it.next()
                        stringBuilder.append(
                            ("\nkey:" + key + ", value: [" +
                                    myKey + " - " + json.optString(myKey)).toString() + "]"
                        )
                    }
                } catch (e: JSONException) {
                    LogManager.i(TAG, "Get message extra JSON error!")
                }
            } else {
                stringBuilder.append("\nkey:" + key + ", value:" + bundle[key])
            }
        }
        return stringBuilder.toString()
    }

    /**
     * 实现自定义推送声音（需要服务端配置）
     *
     * @param context
     * @param bundle
     */
    private fun processCustomMessage(context: Context, bundle: Bundle?) {
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context)
        val title = bundle!!.getString(JPushInterface.EXTRA_TITLE)
        val msg = bundle.getString(JPushInterface.EXTRA_MESSAGE)
        val extras = bundle.getString(JPushInterface.EXTRA_EXTRA)

        if (!TextUtils.isEmpty(extras)) {
            val bodyParams: Map<String, String?> = jsonStrToMap(extras)
            if (bodyParams.size > 0) {
                for (key in bodyParams.keys) {
                    if (bodyParams[key] != null) { //如果参数不是null，才处理数据
                        if ("Snow on the Broken Bridge" == bodyParams[key]) {
                            val bitmap =
                                BitmapFactory.decodeResource(
                                    context.resources,
                                    R.mipmap.main_ic_launcher_round
                                )
                            val notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID)
                            LogManager.i(TAG, "ACTION_MESSAGE_RECEIVED的ID: $notifactionId")
                            LogManager.i(TAG, "extras*****$extras")
                            // 跳转到你所需要的Activity
                            val mIntent =
                                if (SharedPreferencesManager.get("isLogin", false) as Boolean) {
                                    Intent(
                                        context,
                                        MainActivity::class.java
                                    )
                                } else {
                                    Intent(
                                        context,
                                        LoginActivity::class.java
                                    )
                                }
                            mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            val pendingIntent = PendingIntent.getActivity(
                                context,
                                0,
                                mIntent,
                                PendingIntent.FLAG_IMMUTABLE
                            )
                            builder.setContentIntent(pendingIntent)
                                .setAutoCancel(true)
                                .setContentText(msg)
                                .setContentTitle(if ((title == "")) "title" else title)
                                .setSmallIcon(R.mipmap.main_ic_launcher_round)
                                .setLargeIcon(bitmap)
                                .setNumber(NOTIFICATION_SHOW_SHOW_AT_MOST)
                            val manager: NotificationManager =
                                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                val CHANNEL_ID = "MineJpushReceiverId"
                                val CHANNEL_NAME = "MineJpushReceiverName"
                                val CHANNEL_DESCRIPTION = "MineJpushReceiverDescription"
                                if (manager.getNotificationChannel(CHANNEL_ID) != null) {
                                    manager.deleteNotificationChannel(CHANNEL_ID)
                                }

                                //修改安卓8.1以上系统报错
                                val notificationChannel = NotificationChannel(
                                    CHANNEL_ID,
                                    CHANNEL_NAME,
                                    NotificationManager.IMPORTANCE_DEFAULT
                                )
                                notificationChannel.enableLights(true) //如果使用中的设备支持通知灯，则说明此通知通道是否应显示灯
                                notificationChannel.setShowBadge(false) //是否显示角标
                                notificationChannel.enableVibration(true)
                                // 设置描述 最长30字符
                                notificationChannel.description = CHANNEL_DESCRIPTION
                                // 设置显示模式
                                notificationChannel.lockscreenVisibility =
                                    NotificationCompat.VISIBILITY_PUBLIC
                                notificationChannel.setSound(
                                    Uri.parse("android.resource://" + context.packageName + "/" + R.raw.notice_sound),
                                    Notification.AUDIO_ATTRIBUTES_DEFAULT
                                )
                                manager.createNotificationChannel(notificationChannel)
                                builder.setChannelId(CHANNEL_ID)
                            } else {
                                builder.setSound(Uri.parse("android.resource://" + context.packageName + "/" + R.raw.notice_sound))
                            }
                            //id随意，正好使用定义的常量做id，0除外，0为默认的Notification
                            manager.notify(NOTIFICATION_ID, builder.build())
                        }
                    }
                }
            }
        }

//        if (!TextUtils.isEmpty(extras)) {
//            try {
//                JSONObject extraJson = new JSONObject(extras);
//                if (null != extraJson && extraJson.length() > 0) {
//                    String sound = extraJson . getString ("sound");
//                    LogManager.i(TAG, "processCustomMessage sound***" + sound);
//                    if ("sound".equals(sound)) {
//                        notification.setSound(Uri.parse("android.resource://" + contextSub.getPackageName() + "/" + R.raw.notice_sound));
//                    } else {
//                        notification.setSound(Uri.parse("android.resource://" + contextSub!!.packageName + "/" + R.raw.notice_sound))
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
    }


    private fun deleteNoNumberNotification(manager: NotificationManager, newChannelId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var notificationChannels: List<NotificationChannel>? = null
            notificationChannels = manager.notificationChannels

//            LogManager.i(TAG, "deleteNoNumberNotification notificationChannels******" + notificationChannels, toString());
//            if (notificationChannels == null || notificationChannels.size() < 5) {
//                return;
//            }
            if (notificationChannels == null) {
                return
            }
            for (channel: NotificationChannel in notificationChannels) {
                if (channel.id == null || (channel.id == newChannelId)) {
                    continue
                }
                val notificationNumbers = getNotificationNumbers(manager, channel.id)
                LogManager.i(
                    TAG,
                    "notificationNumbers: " + notificationNumbers + " channelId:" + channel.id
                )
                if (notificationNumbers == 0) {
                    LogManager.i(TAG, "deleteNoNumberNotification: " + channel.id)
                    manager.deleteNotificationChannel(channel.id)
                }
            }
        }
    }

    /**
     * 获取某个渠道下状态栏上通知显示个数
     *
     * @param mNotificationManager NotificationManager
     * @param channelId            String
     * @return int
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun getNotificationNumbers(
        mNotificationManager: NotificationManager?,
        channelId: String
    ): Int {
        if (mNotificationManager == null || TextUtils.isEmpty(channelId)) {
            return -1
        }
        var numbers = 0
        val activeNotifications = mNotificationManager.activeNotifications
        for (item: StatusBarNotification in activeNotifications) {
            val notification: Notification? = item.notification
            if (notification != null) {
                if ((channelId == notification.getChannelId())) {
                    numbers++
                }
            }
        }
        return numbers
    }


}