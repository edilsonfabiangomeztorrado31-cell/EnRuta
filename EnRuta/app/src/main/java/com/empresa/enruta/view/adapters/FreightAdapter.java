package com.empresa.enruta.view.adapters;

import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.empresa.enruta.R;
import com.empresa.enruta.model.freight.Freight;

import java.util.List;

public class FreightAdapter extends RecyclerView.Adapter<FreightAdapter.FreightViewHolder> {

    public interface OnFreightClickListener {
        void onDetalleClick(Freight freight);
    }

    private OnFreightClickListener listener;
    private List<Freight> lista;

    public FreightAdapter(List<Freight> lista, OnFreightClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FreightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flete, parent, false);
        return new FreightViewHolder(vista);
    }

    private SpannableString boldLabel(String label, String value) {
        SpannableString spannable = new SpannableString(label + value);
        spannable.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, label.length(), 0);
        return spannable;
    }

    @Override
    public void onBindViewHolder(@NonNull FreightViewHolder holder, int position) {
        Freight freight = lista.get(position);
        holder.origen.setText(boldLabel("Origen: " , freight.getUbicacionOrigen()));
        holder.destino.setText(boldLabel("Destino: " , freight. getUbicacionDestino()));
       // holder.tipoCarga.setText(boldLabel("Tipo de carga: " , freight.getTipoCarga()));
        holder.precio.setText(boldLabel("Precio: ", "$"  + freight.getPrecio()));
        //holder.peso.setText(boldLabel("Peso: " , freight.getPeso()));
        //holder.fecha.setText(boldLabel("Fecha de registro: " , freight.getFechaRegistro()));

//        holder.btnDetalle.setOnClickListener(v -> {
//            if (listener != null) {
//                Log.i("DEBUG_FLETE_DETALLE", "ID seleccionado: " + freight.getId());
//                listener.onDetalleClick(freight);
//            }
//        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                Log.i("DEBUG_FLETE_DETALLE", "ID seleccionado: " + freight.getId());
                listener.onDetalleClick(freight);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class FreightViewHolder extends RecyclerView.ViewHolder {
        TextView origen, destino, tipoCarga, precio, peso, fecha;
        Button btnDetalle;

        public FreightViewHolder(@NonNull View itemView) {
            super(itemView);
            origen = itemView.findViewById(R.id.tvOrigen);
            destino = itemView.findViewById(R.id.tvDestino);
           // tipoCarga = itemView.findViewById(R.id.tvTipoCarga);
            precio = itemView.findViewById(R.id.tvPrecio);
           // peso = itemView.findViewById(R.id.tvPeso);
           // fecha = itemView.findViewById(R.id.tvFecha);
            //btnDetalle = itemView.findViewById(R.id.btnDetalleFlete);
        }
    }
}
