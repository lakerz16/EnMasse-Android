package com.brewski.enmasse.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.echo.holographlibrary.PieGraph;

/**
 * Created by matt on 10/7/13.
 */
public class MyPieGraph extends PieGraph {

    public MyPieGraph(Context context) {
        super(context);
    }

    public MyPieGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}
