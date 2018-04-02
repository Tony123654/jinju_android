package com.jinju.android.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Libra on 2017/7/28.
 * 添加日历事件
 */

public class CalendarEventUtils {
    private static final String CALANDER_URL = "content://com.android.calendar/calendars";
    private static final String CALANDER_EVENT_URL = "content://com.android.calendar/events";
    private static final String CALANDER_REMIDER_URL = "content://com.android.calendar/reminders";

    private static String CALENDARS_NAME = "test";
    private static String CALENDARS_ACCOUNT_NAME = "test@gmail.com";
    private static String CALENDARS_ACCOUNT_TYPE = "com.android.exchange";
    private static String CALENDARS_DISPLAY_NAME = "金桔账户";

    /**
     * 检查是否有现有存在的账户。存在则返回账户id，否则返回-1
     * @param context
     * @return
     */
    private static int checkCalendarAccount(Context context) {
        Cursor userCursor = context.getContentResolver().query(Uri.parse(CALANDER_URL), null, null, null, null);
        try {
            if (userCursor == null)//查询返回空值
                return -1;
            int count = userCursor.getCount();
            if (count > 0) {//存在现有账户，取第一个账户的id返回
                userCursor.moveToFirst();
                return userCursor.getInt(userCursor.getColumnIndex(CalendarContract.Calendars._ID));
            } else {
                return -1;
            }
        } finally {
            if (userCursor != null) {
                userCursor.close();
            }
        }
    }

    /**
     *  添加账户。账户创建成功则返回账户id，否则返回-1
     * @param context
     * @return
     */
    private static long addCalendarAccount(Context context) {
        TimeZone timeZone = TimeZone.getDefault();
        ContentValues value = new ContentValues();
        value.put(CalendarContract.Calendars.NAME, CALENDARS_NAME);

        value.put(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME);
        value.put(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE);
        value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CALENDARS_DISPLAY_NAME);
        value.put(CalendarContract.Calendars.VISIBLE, 1);
        value.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE);
        value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        value.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
        value.put(CalendarContract.Calendars.OWNER_ACCOUNT, CALENDARS_ACCOUNT_NAME);
        value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0);

        Uri calendarUri = Uri.parse(CALANDER_URL);
        calendarUri = calendarUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE)
                .build();

        Uri result = context.getContentResolver().insert(calendarUri, value);
        long id = result == null ? -1 : ContentUris.parseId(result);
        return id;
    }

    /**
     * //检查是否已经添加了日历账户，如果没有添加先添加一个日历账户再查询
     * @param context
     * @return
     */

    private static int checkAndAddCalendarAccount(Context context){
        int oldId = checkCalendarAccount(context);

        if( oldId >= 0 ){
            return oldId;
        }else{
            long addId = addCalendarAccount(context);
            if (addId >= 0) {
                return checkCalendarAccount(context);
            } else {
                return -1;
            }
        }
    }
    public static void addCalendarEvent(Context context,String title, String description, long endTime){
         //获取日历账户的id
        int calId = checkAndAddCalendarAccount(context);
        if (calId < 0) {
            // 获取账户id失败直接返回，添加日历事件失败
            return;
        }

        ContentValues event = new ContentValues();
        event.put("title", title);
        event.put("description", description);
        // 插入账户的id
        event.put("calendar_id", calId);

        Calendar mCalendar = Calendar.getInstance();

        mCalendar.setTimeInMillis(mCalendar.getTimeInMillis());//设置开始时间

        long start = mCalendar.getTime().getTime();

        mCalendar.setTimeInMillis(start + endTime);//设置终止时间
        long end = mCalendar.getTime().getTime();

        event.put(CalendarContract.Events.DTSTART, start);
        event.put(CalendarContract.Events.DTEND, end);
        event.put(CalendarContract.Events.HAS_ALARM, 1);//设置有闹钟提醒
        event.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Beijing");  //这个是时区，必须有，
        //添加事件
        Uri newEvent = context.getContentResolver().insert(Uri.parse(CALANDER_EVENT_URL), event);
        if (newEvent == null) {
            // 添加日历事件失败直接返回
            return;
        }
        //事件提醒的设定
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Reminders.EVENT_ID, ContentUris.parseId(newEvent));
        // 提前10分钟有提醒
        values.put(CalendarContract.Reminders.MINUTES, 10);
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        Uri uri = context.getContentResolver().insert(Uri.parse(CALANDER_REMIDER_URL), values);
        if(uri == null) {
            // 添加闹钟提醒失败直接返回
            return;
        }

    }
    /**
     * 后台插入一条日历事件
     * @param title 时间标题
     * @param descrip 描述
     * @param startTime 事件开始时间，
     *
     */
    public static void insertCalendar(String title, String descrip, long startTime, Context context,OnAddCalendarListener listener) {


        String calanderURL;
        String calanderEventURL;
        String calanderRemiderURL;
        if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
            calanderURL = "content://com.android.calendar/calendars";
            calanderEventURL = "content://com.android.calendar/events";
            calanderRemiderURL = "content://com.android.calendar/reminders";
        } else {
            calanderURL = "content://calendar/calendars";
            calanderEventURL = "content://calendar/events";
            calanderRemiderURL = "content://calendar/reminders";
        }
        // 获取要出入的gmail账户的id
        int calId = -1;
        Cursor userCursor = context.getContentResolver()
                .query(Uri.parse(calanderURL), null, null, null, null);
        if (userCursor.getCount() > 0) {
            userCursor.moveToFirst();
            calId = userCursor.getInt(userCursor.getColumnIndex("_id"));
        }
        if (calId < 0) {
            // 获取账户id失败直接返回，添加日历事件失败
            return;
        }
        ContentValues event = new ContentValues();
        event.put("title", title);
        event.put("description", descrip);
        event.put("calendar_id", calId);

        // 全天事件
//         event.put("allDay", true);
        event.put("dtstart", startTime);
        //事件结束时间
        event.put("dtend", startTime+60*60*1000);

        event.put("hasAlarm", 1);
        // 设置时区
        event.put("eventTimezone", "Asia/Beijing");

        Uri newEvent = context.getContentResolver().insert(
                Uri.parse(calanderEventURL), event);
        if (newEvent == null) {
            // 添加日历事件失败直接返回
            return;
        }
        long id = Long.parseLong(newEvent.getLastPathSegment());

        ContentValues values = new ContentValues();
        values.put("event_id", id);
        // 提前 10分钟提醒
        values.put(CalendarContract.Reminders.MINUTES, 5);
        values.put(CalendarContract.Reminders.EVENT_ID, id);
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        ContentResolver cr1 = context.getContentResolver(); // 为刚才新添加的event添加reminder
        cr1.insert(Uri.parse(calanderRemiderURL), values);

        listener.addCalendarFinish();
    }
    /**
     * 带图形界面
     * @author meitu
     */
    public static void insertWithActivity(Context context) {
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", cal.getTimeInMillis());
        intent.putExtra("allDay", true);
        intent.putExtra("rrule", "FREQ=YEARLY");
        intent.putExtra("endTime", cal.getTimeInMillis() + 60 * 60 * 2000);
        intent.putExtra("title", "Test Event");
        intent.putExtra("description", "This is a sample description");
        context.startActivity(intent);
    }
    public interface OnAddCalendarListener {
        void addCalendarFinish();
    }

}
