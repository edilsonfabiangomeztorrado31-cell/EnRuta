package com.empresa.enruta.view.adapters;

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

public class FreightHomeAdapter extends RecyclerView.Adapter<FreightHomeAdapter.FreightHomeViewHolder> {

    public interface OnCrudClickListener{
        void onCrudClick(Freight freight);
    }

    private OnCrudClickListener listener;
    private List<Freight> lista;

    public FreightHomeAdapter(OnCrudClickListener listener, List<Freight> lista) {
        this.listener = listener;
        this.lista = lista;
    }

    @NonNull
    @Override
    public FreightHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_freight, parent, false);
        return new FreightHomeViewHolder (vista);
    }

    @Override
    public void onBindViewHolder(@NonNull FreightHomeAdapter.FreightHomeViewHolder holder, int position) {
        Freight freight = lista.get(position);
        holder.origen.setText( freight.getUbicacionOrigen());
        holder.destino.setText(freight. getUbicacionDestino());
        holder.precio.setText( freight.getPrecio());
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
                listener.onCrudClick(freight);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }


    static class FreightHomeViewHolder extends RecyclerView.ViewHolder {
        TextView origen, destino, tipoCarga, precio, peso, fecha;
        Button btnDetalle;

        public FreightHomeViewHolder(@NonNull View itemView) {
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
