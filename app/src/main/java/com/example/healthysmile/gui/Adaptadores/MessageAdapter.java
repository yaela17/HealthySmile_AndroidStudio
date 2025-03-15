package com.example.healthysmile.gui.Adaptadores;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.healthysmile.R;
import com.example.healthysmile.model.entities.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messageList;
    private long idUsuarioActual;

    public MessageAdapter(List<Message> messageList, long idUsuarioActual) {
        this.messageList = messageList;
        this.idUsuarioActual = idUsuarioActual;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message = messageList.get(position);

        // Configurar texto del mensaje
        holder.messageText.setText(message.getMensaje());

        // Formatear fecha
        Date fecha = message.getFecha();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String fechaFormateada = dateFormat.format(fecha);
        holder.messageDate.setText(fechaFormateada);

        // Ajustar LayoutParams del contenedor
        LinearLayout.LayoutParams layoutParams =
                (LinearLayout.LayoutParams) holder.messageContainer.getLayoutParams();

        if (message.getEmisor() == idUsuarioActual) {
            layoutParams.gravity = Gravity.END; // Alineación a la derecha
        } else {
            layoutParams.gravity = Gravity.START; // Alineación a la izquierda
        }

        holder.messageContainer.setLayoutParams(layoutParams);
    }



    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView messageDate;
        LinearLayout messageContainer; // Contenedor de los mensajes

        public MessageViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
            messageDate = itemView.findViewById(R.id.messageDate);
            messageContainer = itemView.findViewById(R.id.messageContainer); // Nuevo contenedor
        }
    }
}
