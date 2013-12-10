package com.brewski.enmasse.util;

import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by matt on 11/13/13.
 */
public class Utilities {

    public static void SetDatePicker(DatePicker picker, String serial) {

        try {

            String[] dateValues = serial.split("|");

            int year = Integer.parseInt(dateValues[0]);
            int month = Integer.parseInt(dateValues[1]);
            int day = Integer.parseInt(dateValues[2]);

            picker.updateDate(year, month, day);

        } catch (Exception e) {
            Calendar cal=Calendar.getInstance();

            int year=cal.get(Calendar.YEAR);
            int month=cal.get(Calendar.MONTH);
            int day=cal.get(Calendar.DAY_OF_MONTH);

            picker.updateDate(year, month, day);
        }
    }

    public static void SetTimePicker(TimePicker picker, String serial) {

        try {

            String[] timeValues = serial.split("|");

            int hour = Integer.parseInt(timeValues[0]);
            int min = Integer.parseInt(timeValues[1]);

            picker.setCurrentHour(hour);
            picker.setCurrentMinute(min);

        } catch (Exception e) {
            Calendar cal=Calendar.getInstance();

            int hour = cal.get(Calendar.HOUR);
            int min = cal.get(Calendar.MINUTE);

            picker.setCurrentHour(hour);
            picker.setCurrentMinute(min);
        }
    }

}
