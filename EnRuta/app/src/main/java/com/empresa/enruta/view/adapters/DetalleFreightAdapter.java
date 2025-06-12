package com.empresa.enruta.view.adapters;

import android.content.Intent;
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
import com.empresa.enruta.contract.fragments.conveyor.HistorialFragmentConveyor;
import com.empresa.enruta.model.freight.Freight;

import java.util.List;

public class DetalleFreightAdapter extends RecyclerView.Adapter<DetalleFreightAdapter.DetalleFreightViewHolder> {

    public interface OnFreightClickListener {
        void onTomarClick(Freight freight);
        void onVerRutaClick(Freight freight);
    }
    private List<Freight> lista;
    private OnFreightClickListener listener;
    private Freight flete;

    public DetalleFreightAdapter(List<Freight> lista, OnFreightClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DetalleFreightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flete_detalle, parent, false);
        return new DetalleFreightViewHolder(vista);
    }

    private SpannableString boldLabel(String label, String value) {
        SpannableString spannable = new SpannableString(label + value);
        spannable.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, label.length(), 0);
        return spannable;
    }

    @Override
    public void onBindViewHolder(@NonNull DetalleFreightAdapter.DetalleFreightViewHolder holder, int position) {
        Freight freight = lista.get(position);
        holder.origen.setText( freight.getUbicacionOrigen());
        holder.destino.setText(freight. getUbicacionDestino());
        holder.tipoCarga.setText( freight.getTipoCarga());
        holder.precio.setText(freight.getPrecio());
        holder.peso.setText( freight.getPeso());
        holder.fecha.setText(freight.getFechaRegistro());

        holder.btnTomarFlete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTomarClick(freight);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }


    static class DetalleFreightViewHolder extends RecyclerView.ViewHolder {
        TextView origen, destino, tipoCarga, precio, peso, fecha;
        Button btnTomarFlete;

        public DetalleFreightViewHolder(@NonNull View itemView) {
            super(itemView);
            origen = itemView.findViewById(R.id.tvOrigen);
            destino = itemView.findViewById(R.id.tvDestino);
            tipoCarga = itemView.findViewById(R.id.tvTipoCarga);
            precio = itemView.findViewById(R.id.tvPrecio);
            peso = itemView.findViewById(R.id.tvPeso);
            fecha = itemView.findViewById(R.id.tvFecha);
            btnTomarFlete = itemView.findViewById(R.id.btnTakeFreight);
        }
    }
}
