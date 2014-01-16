package com.brewski.enmasse.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.brewski.enmasse.activities.Globals;
import com.brewski.enmasse.models.DeveloperProfile;
import com.brewski.enmasse.models.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by matt on 11/13/13.
 */
public class Utilities {

    public static void SetDeveloperProfile(Context context, DeveloperProfile profile) {
        SharedPreferences prefs = context.getSharedPreferences("developer", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("user", profile.getName());
        edit.putString("userId", profile.getObjectId());
        edit.commit();

        ((Globals) context.getApplicationContext()).profile = profile;
    }

    public static DeveloperProfile GetSavedDeveloperProfile(Context context) {
        String user = context.getSharedPreferences("developer", Context.MODE_PRIVATE).getString("user", "");
        String id = context.getSharedPreferences("developer", Context.MODE_PRIVATE).getString("userId", "");

        if (user.equals("") || id.equals("")) {
            return new DeveloperProfile("NA", "NA");
        }

        return new DeveloperProfile(user, id);
    }

    public static void SetDatePicker(DatePicker picker, String serial) {

        try {

            String[] dateValues = serial.split("|");

            int year = Integer.parseInt(dateValues[0]);
            int month = Integer.parseInt(dateValues[1]);
            int day = Integer.parseInt(dateValues[2]);

            picker.updateDate(year, month, day);

        } catch (Exception e) {
            Calendar cal = Calendar.getInstance();

            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

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
            Calendar cal = Calendar.getInstance();

            int hour = cal.get(Calendar.HOUR);
            int min = cal.get(Calendar.MINUTE);

            picker.setCurrentHour(hour);
            picker.setCurrentMinute(min);
        }
    }

    public static void SortEventsList(ArrayList<Event> events) {
        final Long currentTime = System.currentTimeMillis();
        Collections.sort(events, new Comparator<Event>() {
            public int compare(Event e1, Event e2) {

                if (e1.GetDateMillis() > currentTime && e2.GetDateMillis() > currentTime) { // both future
                    if (e1.GetDateMillis() > e2.GetDateMillis())
                        return 1;
                    return -1;
                }

                if (e1.GetDateMillis() < currentTime && e2.GetDateMillis() < currentTime) { // both past
                    if (e1.GetDateMillis() > e2.GetDateMillis())
                        return -1;
                    return 1;
                }

                if (e1.GetDateMillis() < e2.GetDateMillis()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
    }

    public static boolean ShouldCheckWeather(Event e) {

        long diff = e.GetDateMillis() - System.currentTimeMillis();

        if (diff < 0 || diff > 432000000) // event must be 0-5 days away
            return false;

        Log.e(Long.toString(e.GetDateMillis()), Long.toString(diff));

        return true;
    }

    public static String climaconsMapping(String weather) {
        if (weather.equals("rain")) {
            return "$";
        } else if (weather.equals("clear-day")) {
            return "I";
        } else if (weather.equals("clear-night")) {
            return "N";
        } else if (weather.equals("snow")) {
            return "9";
        } else if (weather.equals("sleet")) {
            return "0";
        } else if (weather.equals("wind")) {
            return "B";
        } else if (weather.equals("fog")) {
            return "<";
        } else if (weather.equals("cloudy")) {
            return "!";
        } else if (weather.equals("partly-cloudy-day")) {
            return "!";
        } else if (weather.equals("partly-cloudy-night")) {
            return "#";
        } else if (weather.equals("hail")) {
            return "3";
        } else if (weather.equals("thunderstorm")) {
            return "F";
        } else if (weather.equals("tornado")) {
            return "X";
        }

        return "?";
    }

}
