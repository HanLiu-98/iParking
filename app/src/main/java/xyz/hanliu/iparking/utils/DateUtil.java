package xyz.hanliu.iparking.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private static SimpleDateFormat formatGeneral = new SimpleDateFormat("MM-dd HH:mm");

    public static String dateToStr(Date date) {
        return format.format(date);
    }

    public static String dateToStrGeneral(Date date) {
        return formatGeneral.format(date);
    }

}
