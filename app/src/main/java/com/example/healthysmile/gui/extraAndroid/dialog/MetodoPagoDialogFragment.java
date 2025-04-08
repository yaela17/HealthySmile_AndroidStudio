package com.example.healthysmile.gui.extraAndroid.dialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.healthysmile.R;
import com.example.healthysmile.controller.ApiNodeMySqlRespuesta;
import com.example.healthysmile.model.ItemCarrito;
import com.example.healthysmile.model.TemplanteParamsCorreoCompra;
import com.example.healthysmile.repository.NodeApiRetrofitClient;
import com.example.healthysmile.service.ApiNodeMySqlService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.openpay.android.Openpay;
import mx.openpay.android.OperationCallBack;
import mx.openpay.android.OperationResult;
import mx.openpay.android.exceptions.OpenpayServiceException;
import mx.openpay.android.exceptions.ServiceUnavailableException;
import mx.openpay.android.model.Card;
import mx.openpay.android.model.Token;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MetodoPagoDialogFragment extends DialogFragment {

    private EditText cardNumberEditText, holderNameEditText, cvvEditText, expiryMonthEditText, expiryYearEditText;
    private Button btnRealizarPago;
    private double montoTotal;
    private Context context;
    private List<ItemCarrito> carrito;
    String deviceSessionId;

    private Openpay openpay;

    public MetodoPagoDialogFragment(Context context,double montoTotal,List<ItemCarrito> carrito) {
        this.context = context;
        this.montoTotal = montoTotal;
        this.carrito = carrito;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_metodo_pago, container, false);

        cardNumberEditText = view.findViewById(R.id.cardNumberEditText);
        holderNameEditText = view.findViewById(R.id.holderNameEditText);
        cvvEditText = view.findViewById(R.id.cvvEditText);
        expiryMonthEditText = view.findViewById(R.id.expiryMonthEditText);
        expiryYearEditText = view.findViewById(R.id.expiryYearEditText);
        btnRealizarPago = view.findViewById(R.id.btnRealizarPago);

        // Inicializa OpenPay con el modo de pruebas (false) o producción (true)
        openpay = new Openpay("m5moicorylwarp8ause3", "pk_7e96ffe11a59420693d1382b3fb15367", false); // Cambia false por true si estás en producción
        deviceSessionId = openpay.getDeviceCollectorDefaultImpl().setup(getActivity());

        btnRealizarPago.setOnClickListener(v -> procesarPago());

        return view;
    }

    private void procesarPago() {
        String cardNumber = cardNumberEditText.getText().toString().trim();
        String holderName = holderNameEditText.getText().toString().trim();
        String cvv = cvvEditText.getText().toString().trim();
        String expMonth = expiryMonthEditText.getText().toString().trim();
        String expYear = expiryYearEditText.getText().toString().trim();

        // Convertimos los valores de mes y año de expiración a enteros
        int expirationMonth = Integer.parseInt(expMonth);
        int expirationYear = Integer.parseInt(expYear);

        if (!validarCampos(cardNumber, holderName, cvv, expMonth, expYear)) {
            return;
        }

        Card card = new Card();
        card.holderName(holderName);
        card.cardNumber(cardNumber);
        card.expirationMonth(expirationMonth);
        card.expirationYear(expirationYear);
        card.cvv2(cvv);

        Toast.makeText(getContext(), "Generando token...", Toast.LENGTH_SHORT).show();

        // Usando OperationCallback para crear el token
        openpay.createToken(card, new OperationCallBack<Token>() {
            @Override
            public void onSuccess(OperationResult<Token> operationResult) {
                Token token = operationResult.getResult();
                String tokenId = token.getId();
                Log.d("TOKEN_OK", "Token generado: " + tokenId);
                realizarCargoBackend(tokenId,holderName,context);
            }

            @Override
            public void onError(OpenpayServiceException serviceException) {
                Log.e("TOKEN_ERROR", "Error OpenPay: " + serviceException.getMessage(), serviceException);
                Toast.makeText(getContext(), "Error al generar token", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCommunicationError(ServiceUnavailableException e) {
                Log.e("TOKEN_ERROR", "Error de comunicación: " + e.getMessage(), e);
                Toast.makeText(getContext(), "Error de red al generar token", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void realizarCargoBackend(String tokenId,String name,Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String correoUsuario = sharedPreferences.getString("correoUsuario", "Vacio");

        if("Vacio".equals(correoUsuario)){
            return;
        }

        Map<String, Object> datosCargo = new HashMap<>();
        datosCargo.put("token_id", tokenId);
        datosCargo.put("amount", montoTotal);
        datosCargo.put("descripcion", "Compra desde app Android");
        datosCargo.put("device_session_id", deviceSessionId);

        // Crear el objeto customer
        Map<String, String> customer = new HashMap<>();
        customer.put("name", name);
        customer.put("email", "yaelangelvv@gmail.com");
        // Agregar el customer al cuerpo
        datosCargo.put("customer", customer);

        ApiNodeMySqlService service = NodeApiRetrofitClient.getApiService();
        Call<ApiNodeMySqlRespuesta> call = service.crearCargo(datosCargo);

        call.enqueue(new Callback<ApiNodeMySqlRespuesta>() {
            @Override
            public void onResponse(Call<ApiNodeMySqlRespuesta> call, Response<ApiNodeMySqlRespuesta> response) {
                if (response.isSuccessful()) {
                    ApiNodeMySqlRespuesta resultado = response.body();
                    Log.d("CARGO_OK", "Cargo realizado: " + resultado.getMensaje());
                    Toast.makeText(getContext(), "Pago exitoso", Toast.LENGTH_SHORT).show();
                    enviarCorreoCompra(name,correoUsuario);
                    dismiss();
                } else {
                    Log.e("CARGO_ERROR", "Respuesta fallida: " + response.code());
                    Toast.makeText(getContext(), "Error en el pago", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiNodeMySqlRespuesta> call, Throwable t) {
                Log.e("CARGO_FAIL", "Fallo conexión: " + t.getMessage());
                Toast.makeText(getContext(), "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enviarCorreoCompra(String userName, String user_email) {
        TemplanteParamsCorreoCompra templateParams = new TemplanteParamsCorreoCompra();
        templateParams.setUser_name(userName);
        templateParams.setUser_email("yaelangelvv@gmail.com"); // Usar el email proporcionado

        // Verificamos que el carrito no esté vacío
        if (carrito != null && !carrito.isEmpty()) {
            templateParams.setCarrito(carrito);
            templateParams.setTotal(montoTotal); // Total de la compra

            // Crear la llamada al servicio para enviar el correo
            ApiNodeMySqlService service = NodeApiRetrofitClient.getApiService();
            Call<ApiNodeMySqlRespuesta> call = service.enviarCorreoCompra(templateParams);

            call.enqueue(new Callback<ApiNodeMySqlRespuesta>() {
                @Override
                public void onResponse(Call<ApiNodeMySqlRespuesta> call, Response<ApiNodeMySqlRespuesta> response) {
                    if (response.isSuccessful()) {
                        Log.d("EMAIL_OK", "Correo enviado: " + response.body().getMensaje());
                    } else {
                        Log.e("EMAIL_ERROR", "Error al enviar correo: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<ApiNodeMySqlRespuesta> call, Throwable t) {
                    Log.e("EMAIL_FAIL", "Fallo al enviar correo: " + t.getMessage());
                }
            });
        } else {
            Log.e("EMAIL_ERROR", "El carrito está vacío");
        }
    }

    private boolean validarCampos(String cardNumber, String holderName, String cvv, String expMonth, String expYear) {
        if (cardNumber.isEmpty() || cardNumber.length() != 16) {
            Toast.makeText(getContext(), "Número de tarjeta inválido", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (holderName.isEmpty()) {
            Toast.makeText(getContext(), "Nombre del titular es obligatorio", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (cvv.isEmpty() || cvv.length() != 3) {
            Toast.makeText(getContext(), "CVV inválido", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (expMonth.isEmpty() || expYear.isEmpty()) {
            Toast.makeText(getContext(), "Fecha de expiración es obligatoria", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
