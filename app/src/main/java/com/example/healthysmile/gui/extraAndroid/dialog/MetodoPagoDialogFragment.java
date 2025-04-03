package com.example.healthysmile.gui.extraAndroid.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

public class MetodoPagoDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String[] opcionesDePago = {"BBVA", "Tarjeta de Crédito", "PayPal"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Seleccionar Método de Pago")
                .setItems(opcionesDePago, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Llamar a la función de confirmación de pago
                        switch (which) {
                            case 0:
                                mostrarConfirmacionPago("BBVA");
                                break;
                            case 1:
                                mostrarConfirmacionPago("Tarjeta de Crédito");
                                break;
                            case 2:
                                mostrarConfirmacionPago("PayPal");
                                break;
                        }
                    }
                });
        return builder.create();
    }

    private void mostrarConfirmacionPago(String metodoPago) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Confirmar Compra")
                .setMessage("¿Estás seguro de que deseas pagar con " + metodoPago + "?")
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Aquí se llamaría a la función para procesar el pago según el método seleccionado
                        procesarPago(metodoPago);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void procesarPago(String metodoPago) {
        // Simulación de procesamiento de pago
        Toast.makeText(getActivity(), "Pago procesado exitosamente con " + metodoPago, Toast.LENGTH_SHORT).show();
    }
}
