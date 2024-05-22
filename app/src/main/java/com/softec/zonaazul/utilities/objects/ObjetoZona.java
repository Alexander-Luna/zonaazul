package com.softec.zonaazul.utilities.objects;


import com.softec.zonaazul.utilities.Utilidades;

public class ObjetoZona {
    private String calle, recaudador, cuadras, orientacion, interseccion1, interseccion2, nombre, estado = Utilidades.ESTADO_ACTIVA;
    private String id;

    public String getOrientacion() {
        try {
            return orientacion;
        } catch (Exception e) {
            return "";
        }
    }

    public void setOrientacion(String orientacion) {
        this.orientacion = orientacion;
    }

    public String getRecaudador() {
        try {
            return recaudador;
        } catch (Exception e) {
            return "";
        }
    }

    public void setRecaudador(String recaudador) {
        this.recaudador = recaudador;
    }

    public String getCalle() {
        try {
            return calle;
        } catch (Exception e) {
            return "";
        }
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getCuadras() {
        try {
            return cuadras;
        } catch (Exception e) {
            return "";
        }
    }

    public void setCuadras(String cuadras) {
        this.cuadras = cuadras;
    }

    public String getInterseccion1() {
        try {
            return interseccion1;
        } catch (Exception e) {
            return "";
        }
    }

    public void setInterseccion1(String interseccion1) {
        this.interseccion1 = interseccion1;
    }

    public String getInterseccion2() {
        try {
            return interseccion2;
        } catch (Exception e) {
            return "";
        }
    }

    public void setInterseccion2(String interseccion2) {
        this.interseccion2 = interseccion2;
    }

    public String getNombre() {
        try {
            return nombre;
        } catch (Exception e) {
            return "";
        }
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEstado() {
        try {
            return estado;
        } catch (Exception e) {
            return "";
        }
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public ObjetoZona() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        boolean v = false;
        ObjetoZona sC = (ObjetoZona) o;
        if (this.getId().equals(sC.getId())) {
            v = true;
        }
        return v;
    }

}