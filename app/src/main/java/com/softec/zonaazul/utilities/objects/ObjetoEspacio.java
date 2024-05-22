package com.softec.zonaazul.utilities.objects;


import com.softec.zonaazul.utilities.Utilidades;

public class ObjetoEspacio {
    private String nombreCalle, nombreEspacio, placa, hora_in, hora_ven, estado = Utilidades.ESTADO_DISPONIBLE, recaudador, zona;
    private String id;
    private String zonaId;

    public String getZonaId() {
        try {
            return zonaId;
        } catch (Exception e) {
            return "0";
        }
    }

    public void setZonaId(String zonaId) {
        this.zonaId = zonaId;
    }

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

    public String getNombreCalle() {
        try {
            return nombreCalle;
        } catch (Exception e) {
            return "";
        }
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

    public void setNombreCalle(String nombreCalle) {
        this.nombreCalle = nombreCalle;
    }

    public String getNombreEspacio() {
        try {
            return nombreEspacio;
        } catch (Exception e) {
            return "";
        }
    }

    public void setNombreEspacio(String nombreEspacio) {
        this.nombreEspacio = nombreEspacio;
    }

    public String getPlaca() {
        try {
            return placa;
        } catch (Exception e) {
            return "";
        }
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getHora_in() {
        try {
            return hora_in;
        } catch (Exception e) {
            return "";
        }
    }

    public void setHora_in(String hora_in) {
        this.hora_in = hora_in;
    }

    public String getHora_ven() {
        try {
            return hora_ven;
        } catch (Exception e) {
            return "";
        }
    }

    public void setHora_ven(String hora_ven) {
        this.hora_ven = hora_ven;
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

    public ObjetoEspacio() {

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
        ObjetoEspacio sC = (ObjetoEspacio) o;
        if (this.getId().equals(sC.getId())) {
            v = true;
        }
        return v;
    }

}