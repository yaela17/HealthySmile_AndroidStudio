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
import com.example.healthysmile.service.tiendaVirtual.CrearCompraService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
    private final double montoTotal;
    private final Context context;
    private final List<ItemCarrito> carrito;
    private final int idProducto;
    private final boolean esProducto;
    Map<String, Object> datosCargo;
    String deviceSessionId;

    private Openpay openpay;

    public MetodoPagoDialogFragment(Context context,double montoTotal,List<ItemCarrito> carrito, int idProducto,boolean esProducto) {
        this.context = context;
        this.montoTotal = montoTotal;
        this.carrito = carrito;
        this.idProducto = idProducto;
        this.esProducto = esProducto;
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

        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String tipoUsuario = sharedPreferences.getString("tipoUsuario", "Paciente");
        boolean peticion = !"Paciente".equals(tipoUsuario);

        btnRealizarPago.setOnClickListener(v -> {
            if (peticion) {
                crearCompra(true);
            } else {
                procesarPago();
            }
        });


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
        openpay.createToken(card, new OperationCallBack<>() {
            @Override
            public void onSuccess(OperationResult<Token> operationResult) {
                Token token = operationResult.getResult();
                String tokenId = token.getId();
                Log.d("TOKEN_OK", "Token generado: " + tokenId);
                realizarCargoBackend(tokenId, holderName, context);
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

        datosCargo = new HashMap<>();
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

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ApiNodeMySqlRespuesta> call, @NonNull Response<ApiNodeMySqlRespuesta> response) {
                if (response.isSuccessful()) {
                    ApiNodeMySqlRespuesta resultado = response.body();
                    assert resultado != null;
                    Log.d("CARGO_OK", "Cargo realizado: " + resultado.getMensaje());
                    Toast.makeText(getContext(), "Pago exitoso", Toast.LENGTH_SHORT).show();
                    enviarCorreoCompra(name, correoUsuario);
                    crearCompra(false);
                    dismiss();
                } else {
                    Log.e("CARGO_ERROR", "Respuesta fallida: " + response.code());
                    Toast.makeText(getContext(), "Error en el pago", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiNodeMySqlRespuesta> call, @NonNull Throwable t) {
                Log.e("CARGO_FAIL", "Fallo conexión: " + t.getMessage());
                Toast.makeText(getContext(), "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void crearCompra(boolean peticion){
        CrearCompraService crearCompraService = new CrearCompraService();
        String metodoPago = "Tarjeta";
        // Obtener la fecha actual solo con día, mes y año
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String fechaCompra = dateFormat.format(new Date());
        // Obtener tipo de usuario desde SharedPreferences

        if (esProducto) {
            int cantidadProducto = carrito.get(0).getCantidad();
            Log.d("CARGO_OK", "Cantidad producto: " + cantidadProducto);
            crearCompraService.crearCompra(context, true, cantidadProducto, fechaCompra, metodoPago, peticion,idProducto);
        } else{
            crearCompraService.crearCompra(context,false,0,fechaCompra,metodoPago,peticion,idProducto);
        }
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

            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<ApiNodeMySqlRespuesta> call, @NonNull Response<ApiNodeMySqlRespuesta> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        Log.d("EMAIL_OK", "Correo enviado: " + response.body().getMensaje());
                    } else {
                        Log.e("EMAIL_ERROR", "Error al enviar correo: " + response.code());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiNodeMySqlRespuesta> call, @NonNull Throwable t) {
                    Log.e("EMAIL_FAIL", "Fallo al enviar correo: " + t.getMessage());
                }
            });
        } else {
            Log.e("EMAIL_ERROR", "El carrito está vacío");
        }
    }

    private boolean validarCampos(String cardNumber, String holderName, String cvv, String expMonth, String expYear) {
        if (cardNumber.length() != 16) {
            Toast.makeText(getContext(), "Número de tarjeta inválido", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (holderName.isEmpty()) {
            Toast.makeText(getContext(), "Nombre del titular es obligatorio", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (cvv.length() != 3) {
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
