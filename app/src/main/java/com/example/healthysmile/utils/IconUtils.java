package com.example.healthysmile.utils;

import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.widget.EditText;

import com.example.healthysmile.R;

public class IconUtils {

    public static void setupPasswordVisibility(final EditText passwordField) {
        passwordField.setOnTouchListener((v, event) -> {
            int drawableRight = 2;  // El índice de drawableRight
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // Verificar si el toque fue en el área del ícono derecho
                if (event.getRawX() >= (passwordField.getRight() - passwordField.getCompoundDrawables()[drawableRight].getBounds().width())) {
                    togglePasswordVisibility(passwordField);
                    return true;
                }
            }
            return false;
        });
    }

    private static void togglePasswordVisibility(EditText passwordField) {
        int currentInputType = passwordField.getInputType();

        if (currentInputType == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
            passwordField.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

            Drawable[] drawables = passwordField.getCompoundDrawables();
            passwordField.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    drawables[0], // drawableLeft
                    null,          // No cambiamos drawableTop
                    passwordField.getContext().getResources().getDrawable(R.drawable.icon_visible), // Nuevo ícono
                    null           // No cambiamos drawableBottom
            );

        } else {
            passwordField.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordField.setTransformationMethod(PasswordTransformationMethod.getInstance());

            Drawable[] drawables = passwordField.getCompoundDrawables();
            passwordField.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    drawables[0], // drawableLeft
                    null,          // No cambiamos drawableTop
                    passwordField.getContext().getResources().getDrawable(R.drawable.icon_visible_off), // Nuevo ícono
                    null           // No cambiamos drawableBottom
            );
        }

        // Reseteamos el cursor en la última posición
        passwordField.post(() -> passwordField.setSelection(passwordField.getText().length()));
    }
}
