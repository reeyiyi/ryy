package com.example.myapplication.CalendarClass;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import com.example.myapplication.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;

/**
 * Decorate several days with a dot
 */
public class EventDecorator implements DayViewDecorator {

    private int color;
    private HashSet<CalendarDay> dates;
    private int w, h;

    public EventDecorator(int color, Collection<CalendarDay> dates, Activity context, MaterialCalendarView m) {
        this.color = color;
        this.dates = new HashSet<>(dates);
        this.w = m.getTileWidth();
        this.h = m.getTileHeight();
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(8, color)); // 날자밑에 점
    }
}
