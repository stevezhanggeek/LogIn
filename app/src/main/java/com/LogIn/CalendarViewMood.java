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

public class CalendarViewMood extends View {
    private ShapeDrawable mDrawable;
    private int index_day;
    private List<ParseObject> m_valueList;

    Paint paint = new Paint();
    Canvas mCanvas;

    private void init() {
        mCanvas = new Canvas();
        m_valueList = Utility.getDataFromParse();
    }

    public CalendarViewMood(Context context) {
        super(context);
        init();
    }

    public CalendarViewMood(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public void setPageIndex(int position) {
        index_day = position;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (Utility.m_valueList != null) {
            m_valueList = Utility.m_valueList;
        }
        mCanvas = canvas;
        super.onDraw(canvas);

        int width = mCanvas.getWidth();

        int textSize = 30;
        paint.setColor(Color.BLACK);
        paint.setTextSize(textSize);
        int hour_vertical_interval = 300;
        int text_width = 100;

        paint.setAntiAlias(true);
        for (int i = Utility.hour_start; i < Utility.hour_start + Utility.num_hour_experiment_length; i++) {
            int y = (i - Utility.hour_start + 1) * hour_vertical_interval;
            canvas.drawText(i + ":00", 10 + (width - text_width)/2, y + textSize / 2, paint);
            canvas.drawLine(0, y, (width - text_width)/2, y, paint);
            canvas.drawLine((width + text_width)/2, y, width, y, paint);
        }

        if (m_valueList != null) {
            for (ParseObject object : m_valueList) {
                int negative_positive = object.getInt("negative_positive");
                int low_high = object.getInt("low_high");
                int startX_negative_positive = (negative_positive + 5) * (width - text_width) / 20;
                int startX_low_high = (width + text_width)/2 + (low_high + 5) * (width - text_width) / 20;

                Date time = object.getDate("time");
                Calendar cal = Calendar.getInstance();
                cal.setTime(time);
                cal.add(Calendar.DATE, -index_day);
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                if (year == Utility.year_start && month == Utility.month_start && day == Utility.day_start) {
                    int hour = cal.get(Calendar.HOUR_OF_DAY);
                    int minute = cal.get(Calendar.MINUTE);

                    int startY = hour_vertical_interval + (int) ((hour - Utility.hour_start + (double) minute / 60) * hour_vertical_interval);
                    paint.setColor(Utility.convertMoodValueToColor(negative_positive));
                    RectF negative_positive_rect = new RectF(startX_negative_positive, startY, startX_negative_positive + (width - text_width) / 20, startY + 20);
                    canvas.drawRoundRect(negative_positive_rect, 15, 15, paint);
                    paint.setColor(Utility.convertMoodValueToColor(low_high));
                    RectF low_high_rect = new RectF(startX_low_high, startY, startX_low_high + (width - text_width) / 20, startY + 20);
                    canvas.drawRoundRect(low_high_rect, 15, 15, paint);
                }
            }
        }
    }
}