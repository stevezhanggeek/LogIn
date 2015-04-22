package com.LogIn;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
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

public class CalendarView extends View {
    private ShapeDrawable mDrawable;
    private int m_data;
    private List<ParseObject> m_valueList;

    Paint paint = new Paint();
    Canvas mCanvas;

    private void init() {
        mCanvas = new Canvas();

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

    public CalendarView(Context context) {
        super(context);
        init();
    }

    public CalendarView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public void setPageIndex(int position) {
        m_data = position;
    }


    @Override
    public void onDraw(Canvas canvas) {
        mCanvas = canvas;
        super.onDraw(canvas);

        int width = mCanvas.getWidth();

        int textSize = 30;
        paint.setColor(Color.BLACK);
        paint.setTextSize(textSize);
        int start_time_hour = 8;
        int length_hour = 10;
        int hour_vertical_interval = 300;
        int text_width = 100;

        paint.setAntiAlias(true);
        for (int i = start_time_hour; i < start_time_hour + length_hour; i++) {
            int y = (i - start_time_hour + 1) * hour_vertical_interval;
            canvas.drawText(i + ":00", 10, y + textSize / 2, paint);
            canvas.drawLine(text_width, y, width, y, paint);
        }

        System.out.println(m_data);

        if (m_data==1) {
            if (m_valueList != null) {
                for (ParseObject object : m_valueList) {
                    int value = object.getInt("value");
                    int startX = text_width + (value - 1) * (width - text_width) / 7;

                    Date time = object.getDate("time");

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(time);
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    int hour = cal.get(Calendar.HOUR_OF_DAY);
                    int minute = cal.get(Calendar.MINUTE);

                    int startY = hour_vertical_interval + (int) ((hour - start_time_hour + (double) minute / 60) * hour_vertical_interval);

                    switch (value) {
                        case 1:
                            paint.setColor(Color.rgb(0,255,0));
                            break;
                        case 2:
                            paint.setColor(Color.rgb(35,220,0));
                            break;
                        case 3:
                            paint.setColor(Color.rgb(70,185,0));
                            break;
                        case 4:
                            paint.setColor(Color.rgb(105,150,0));
                            break;
                        case 5:
                            paint.setColor(Color.rgb(140,115,0));
                            break;
                        case 6:
                            paint.setColor(Color.rgb(175,80,0));
                            break;
                        case 7:
                            paint.setColor(Color.rgb(210,45,0));
                            break;
                        default:
                            paint.setColor(Color.BLACK);
                    }
                    RectF rf = new RectF(startX, startY, startX + (width - text_width) / 7 - 10, startY + 20);
                    canvas.drawRoundRect(rf, 15, 15, paint);
                }
            }
        }

        if (m_data==2) {
            if (m_valueList != null) {
                for (ParseObject object : m_valueList) {
                    int value = object.getInt("value");
                    int startX = text_width + (value - 1) * (width - text_width) / 7;

                    Date time = object.getDate("time");

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(time);
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    int hour = cal.get(Calendar.HOUR_OF_DAY);
                    int minute = cal.get(Calendar.MINUTE);

                    int startY = hour_vertical_interval + (int) ((hour - start_time_hour + (double) minute / 60) * hour_vertical_interval);

                    switch (value) {
                        default:
                            paint.setColor(Color.BLACK);
                    }
                    RectF rf = new RectF(startX, startY, startX + (width - text_width) / 7 - 10, startY + 20);
                    canvas.drawRoundRect(rf, 15, 15, paint);
                }
            }
        }
    }
}