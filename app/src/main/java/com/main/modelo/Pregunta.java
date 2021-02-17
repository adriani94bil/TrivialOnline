package com.main.modelo;

public class Pregunta {
    public int id;
    public String descripcion;
    public String mensaje;
    public boolean respuesta;

    // atributo para guardar la respuesta del usuario

    private Boolean respuestaJugador;

    public Pregunta(int id, String descripcion, boolean isCorrecto, String mensaje) {
        this.id = id;
        this.descripcion = descripcion;
        this.respuesta = isCorrecto;
        this.mensaje = mensaje;
    }

    public Pregunta(String descripcion,  String mensaje, boolean isCorrecto) {
        this.descripcion = descripcion;
        this.respuesta = isCorrecto;
        this.mensaje = mensaje;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isRespuesta() {
        return respuesta;
    }

    public void setRespuesta(boolean correcto) {
        respuesta = correcto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Boolean getRespuestaJugador() {
        return respuestaJugador;
    }

    public void setRespuestaJugador(Boolean respuestaJugador) {
        this.respuestaJugador = respuestaJugador;
    }
}
