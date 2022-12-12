package com.solifungi.handtalkgobang.util;

import javafx.application.Platform;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

public class Utilities {
    public static String getFormattedTime(){
        Calendar cal = new GregorianCalendar();
        int[] list = {cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND)};

        StringBuilder timeString = new StringBuilder();
        formattedAppend(timeString, list[0], list[1], list[2]);
        timeString.append("_");
        formattedAppend(timeString, list[3], list[4], list[5]);

        return timeString.toString();
    }

    private static void formattedAppend(StringBuilder sb, int... data){
        for(int datum : data){
            if(datum < 10){
                sb.append(0);
            }
            sb.append(datum);
        }
    }

    public static Locale localeFromString(String localeString){
        String[] parts = localeString.split("_",-1);
        switch(parts.length){
            case 0: return Locale.getDefault();
            case 1: return new Locale(parts[0]);
            case 2: return new Locale(parts[0], parts[1]);
            default: return new Locale(parts[0], parts[1], parts[2].replaceFirst("#",""));
        }
    }

}
