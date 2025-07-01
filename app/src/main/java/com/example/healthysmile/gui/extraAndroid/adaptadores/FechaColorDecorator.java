package com.example.healthysmile.gui.extraAndroid.adaptadores;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

public class FechaColorDecorator implements DayViewDecorator {

    private final int color;
    private final HashSet<CalendarDay> fechas;

    public FechaColorDecorator(Collection<CalendarDay> fechas, int color) {
        this.fechas = new HashSet<>(fechas);
        this.color = color;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return fechas.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(new ColorDrawable(color));
    }
}
