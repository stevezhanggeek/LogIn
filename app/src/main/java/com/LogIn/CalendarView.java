package com.LogIn;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.view.View;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;
import java.util.List;

public class CalendarView extends View {
    private ShapeDrawable mDrawable;
    private int m_data;
    private List<ParseObject> m_valueList;

    Paint paint = new Paint();
    Canvas mCanvas;

    public CalendarView(Context context) {
        super(context);
    }

    public CalendarView(Context context, int temp) {
        super(context);
        m_data = temp;

        System.out.println("Here");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("LogIn");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> valueList, ParseException e) {
                if (e == null) {
                    m_valueList = valueList;
                    System.out.println("Good");
                } else {
                    System.out.println("WTH");
                }
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mCanvas = canvas;
        int width = mCanvas.getWidth();
        super.onDraw(canvas);

        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        int start = 8;
        int hour_vertical_interval = 300;
        int hour_width = 100;

        paint.setAntiAlias(true);
        for (int i = start; i < start+10; i++){
            int y = (i - start + 1) * hour_vertical_interval;
            canvas.drawText(i + ":00", 10, y, paint);
            canvas.drawLine(hour_width, y-10, width, y-10, paint);
        }

        if (m_valueList != null) {
            for (ParseObject object: m_valueList) {
                int value = object.getInt("value");
                Date time = object.getDate("time");
                int startX = hour_width + (value-1)*(width-hour_width)/7;
                canvas.drawRect(startX, hour_vertical_interval, startX + (width-hour_width)/7 - 10, hour_vertical_interval+10, paint);
            }
        }
//        canvas.drawRect(hour_width, hour_vertical_interval, hour_width + (width-hour_width)/7, hour_vertical_interval+10, paint);
    }
}