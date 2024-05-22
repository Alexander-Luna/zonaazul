package com.softec.zonaazul.utilities.objects;

public class ObjetoPersona {
    private String rol = "";
    private String nombre = "", ID;
    private String ci = "";
    private String Cargo = "";

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    private String celular = "";
    private String direccion = "";
    private String email = "";
    private String zona = "";

    public String getZona() {
        try {
            return zona;
        } catch (Exception e) {
            return "";
        }
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public ObjetoPersona() {
    }

    public String getCargo() {
        if (Cargo == null) {
            return "";
        } else {
            return Cargo;
        }
    }

    public void setCargo(String cargo) {
        Cargo = cargo;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNombre() {
        if (nombre.length() == 0) {
            return "";
        } else {
            return nombre;
        }
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        if (email != null && email != " ") {
            return email;
        } else {
            return "";
        }
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        boolean v = false;
        ObjetoPersona sC = (ObjetoPersona) o;
        if (this.getID().equals(sC.getID())) {
            v = true;
        }
        return v;
    }


}