package com.example.healthysmile.gui.extraAndroid.adaptadores;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class CustomDateDecorator implements DayViewDecorator {
    private final CalendarDay day;
    private final int color;

    public CustomDateDecorator(CalendarDay day, int color) {
        this.day = day;
        this.color = color;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return this.day.equals(day); // Decora solo la fecha específica
    }

    @Override
    public void decorate(DayViewFacade view) {
        ShapeDrawable circle = new ShapeDrawable(new OvalShape());
        circle.getPaint().setColor(color);

        circle.setIntrinsicWidth(2);  // Ajusta el tamaño según lo que desees
        circle.setIntrinsicHeight(2); // Ajusta el tamaño según lo que desees

        // Establecer el fondo con el drawable circular
        view.setBackgroundDrawable(circle);
    }
}
