package com.msp.jdbc.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    /**
     * Check String is date ?
     *
     * @param date   String
     * @param format String
     * @return boolean
     */
    public static boolean isValidDate(String date, String format) {
        if (StringUtils.isNullOrBlank(date)) return false;
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setLenient(false);
        if (date.length() != dateFormat.toPattern().length()) {
            return false;

        }
        try {
            dateFormat.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            return false;
        }
        return true;
    }

    /**
     * Convert date to string
     *
     * @param date Date
     * @return String
     */
    public static String convertDatetoString(Date date, String format) {
        if (date == null) return null;
        DateFormat dateFormat = new SimpleDateFormat(format);
        String stringDate = dateFormat.format(date);
        return stringDate;
    }

    /**
     * Convert string to date
     *
     * @param dateString String
     * @param format     String
     * @return Date
     */
    public static Date convertStringtoDate(String dateString, String format) {
        Date date = null;
        if (DateUtils.isValidDate(dateString, format)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            try {
                date = dateFormat.parse(dateString);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return date;

    }

    /**
     * convert date type string from format to new format
     *
     * @param date      String
     * @param oldFormat String
     * @param newFormat String
     * @return String
     */
    public static String convertDatetoStringByFormat(String date, String oldFormat, String newFormat) {

        String newStringDate = null;

        SimpleDateFormat dateFormat = new SimpleDateFormat(oldFormat);
        try {
            Date d = dateFormat.parse(date);
            dateFormat.applyPattern(newFormat);
            newStringDate = dateFormat.format(d);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return newStringDate;

    }

    /**
     * Compare date
     *
     * @param date1 Date
     * @param date2 Date
     * @return boolean
     */
    public static boolean compareDate(Date date1, Date date2) {
        boolean result = false;
        if (date1.before(date2)) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    /*
     * public static Date plusAndMinusDate(int day,String dateString, String format)
     * {
     *
     *
     * SimpleDateFormat dateFormat = new SimpleDateFormat(format); Calendar calendar
     * = Calendar.getInstance();
     *
     * Date date = dateFormat.parse(dateString); date = calendar.roll(calendar.DATE,
     * 8);
     *
     *
     *
     * }
     */

    /**
     * Get last day of month
     *
     * @param month int
     * @param year  int
     * @return
     */
    public static String getLastDayOfTheMonth(String date, String format) {
        String lastDayOfTheMonth = "";

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
            Date dt = formatter.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dt);

            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.DATE, -1);

            Date lastDay = calendar.getTime();

            lastDayOfTheMonth = formatter.format(lastDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return lastDayOfTheMonth;
    }


}
