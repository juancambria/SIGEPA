package com.sigepa.model;

public class Cliente {

    private int idCliente;
    private String nombre;
    private String documento;
    private String telefono;
    private String email;
    private String direccion;
    private boolean activo;

    public Cliente() {
    }

    public Cliente(
            String nombre,
            String documento,
            String telefono,
            String email,
            String direccion
    ) {

        this.nombre = nombre;
        this.documento = documento;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.activo = true;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {

        return idCliente
                + " | "
                + nombre
                + " | "
                + documento
                + " | "
                + telefono;
    }
}