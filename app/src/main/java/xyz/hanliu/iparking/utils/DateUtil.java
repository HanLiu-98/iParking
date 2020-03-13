package xyz.hanliu.iparking.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private static SimpleDateFormat formatGeneral = new SimpleDateFormat("MM-dd HH:mm");

    public static String dateToStr(Date date) {//可根据需要自行截取数据显示
//        Log.d("getTime()", "choice date millis: " + date.getTime());
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }

    public static String dateToStrGeneral(Date date) {//可根据需要自行截取数据显示
//        Log.d("getTime()", "choice date millis: " + date.getTime());
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return formatGeneral.format(date);
    }

    public static Date strToDate(String str) throws ParseException {
        Date date = format.parse(str);
        return date;
    }
}
