package com.empresa.enruta.model.freight;

import android.util.Log;

import com.google.firebase.database.PropertyName;

public class Freight {

    private String id;
    private String ubicacionOrigen;
    private String ubicacionDestino;
    private String tipoCarga;
    private String precio;
    private String peso;
    private String fechaRegistro;

    public Freight() {
    }

    public Freight(String ubicacionOrigen, String ubicacionDestino, String tipoCarga, String precio, String peso, String fechaRegistro) {
        this.ubicacionOrigen = ubicacionOrigen;
        this.ubicacionDestino = ubicacionDestino;
        this.tipoCarga = tipoCarga;
        this.precio = precio;
        this.peso = peso;
        this.fechaRegistro = fechaRegistro;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUbicacionOrigen() {
        Log.i("GET UBICACION ORIGEN", "UBICACION GUARDADA EN LA BASE DE DATOS: " + ubicacionOrigen);
        return ubicacionOrigen;
    }

    public void setUbicacionOrigen(String ubicacionOrigen) {
        this.ubicacionOrigen = ubicacionOrigen;
    }

    public String getUbicacionDestino() {
        return ubicacionDestino;
    }

    public void setUbicacionDestino(String ubicacionDestino) {
        this.ubicacionDestino = ubicacionDestino;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getTipoCarga() {
        return tipoCarga;
    }

    public void setTipoCarga(String tipoCarga) {
        this.tipoCarga = tipoCarga;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
