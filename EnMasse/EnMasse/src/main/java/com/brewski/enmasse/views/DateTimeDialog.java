package com.brewski.enmasse.views;

import android.app.Dialog;
import android.content.Context;

/**
 * Created by matt on 10/29/13.
 */
public class DateTimeDialog extends Dialog {

    public DateTimeDialog(Context context) {
        super(context);
    }

    public DateTimeDialog(Context context, int theme) {
        super(context, theme);
    }

    protected DateTimeDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
