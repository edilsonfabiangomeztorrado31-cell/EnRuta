package com.empresa.enruta.model.company;

public class Company {
    private String id;
    private String nombre;
    private String nit;
    private String correo;
    private String representante;
    private String tipoEmpresa;
    private String direccion;
    private String contacto;
    private String tipoCarga;
    private String contraseña;

    public Company() {
    }

    public Company(String nombre, String id, String nit, String correo, String representante, String contacto, String tipoCarga,
                   String contraseña, String direccion, String tipoEmpresa) {
        this.nombre = nombre;
        this.id = id;
        this.nit = nit;
        this.correo = correo;
        this.representante = representante;
        this.contacto = contacto;
        this.tipoCarga = tipoCarga;
        this.contraseña = contraseña;
        this.direccion = direccion;
        this.tipoEmpresa = tipoEmpresa;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getRepresentante() {
        return representante;
    }

    public void setRepresentante(String representante) {
        this.representante = representante;
    }

    public String getTipoEmpresa() {
        return tipoEmpresa;
    }

    public void setTipoEmpresa(String tipoEmpresa) {
        this.tipoEmpresa = tipoEmpresa;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTipoCarga() {
        return tipoCarga;
    }

    public void setTipoCarga(String tipoCarga) {
        this.tipoCarga = tipoCarga;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

}
