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
import android.widget.ScrollView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarView extends ScrollView {
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

        ParseQuery<ParseObject> query = ParseQuery.getQuery("LogIn");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> valueList, ParseException e) {
                if (e == null) {
                    m_valueList = valueList;
                } else {
                    System.out.println("WTH");
                }
            }
        });
    }

    @Override
    public void onDraw(Canvas canvas) {
        mCanvas = canvas;
        int width = mCanvas.getWidth();
        super.onDraw(canvas);

        int textSize = 30;
        paint.setColor(Color.BLACK);
        paint.setTextSize(textSize);
        int start_time_hour = 8;
        int length_hour = 10;
        int hour_vertical_interval = 300;
        int text_width = 100;

        paint.setAntiAlias(true);
        for (int i = start_time_hour; i < start_time_hour + length_hour; i++){
            int y = (i - start_time_hour + 1) * hour_vertical_interval;
            canvas.drawText(i + ":00", 10, y+textSize/2, paint);
            canvas.drawLine(text_width, y, width, y, paint);
        }

        if (m_valueList != null) {
            for (ParseObject object: m_valueList) {
                int value = object.getInt("value");
                int startX = text_width + (value-1)*(width-text_width)/7;

                Date time = object.getDate("time");

                Calendar cal = Calendar.getInstance();
                cal.setTime(time);
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int hour = cal.get(Calendar.HOUR);
                double minute = cal.get(Calendar.MINUTE);
                System.out.println(this.getHeight());

                int startY = hour_vertical_interval + (hour-start_time_hour+(int)(minute/60)) * hour_vertical_interval;

                canvas.drawRect(startX, startY, startX + (width-text_width)/7 - 10, startY+20, paint);
            }
        }
    }
}