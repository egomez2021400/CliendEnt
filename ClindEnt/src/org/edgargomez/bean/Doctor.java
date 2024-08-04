package org.edgargomez.bean;


public class Doctor {
    private int numeroColegiado;
    private String nombresDoctores;
    private String apellidosDoctor;
    private String telefonoContacto;
    private int codigoEspecialidad;

    public Doctor() {
    }

    public Doctor(int numeroColegiado, String nombresDoctores, String apellidosDoctor, String telefonoContacto, int codigoEspecialidad) {
        this.numeroColegiado = numeroColegiado;
        this.nombresDoctores = nombresDoctores;
        this.apellidosDoctor = apellidosDoctor;
        this.telefonoContacto = telefonoContacto;
        this.codigoEspecialidad = codigoEspecialidad;
    }

    public int getNumeroColegiado() {
        return numeroColegiado;
    }

    public void setNumeroColegiado(int numeroColegiado) {
        this.numeroColegiado = numeroColegiado;
    }

    public String getNombresDoctores() {
        return nombresDoctores;
    }

    public void setNombresDoctores(String nombresDoctores) {
        this.nombresDoctores = nombresDoctores;
    }

    public String getApellidosDoctor() {
        return apellidosDoctor;
    }

    public void setApellidosDoctor(String apellidosDoctor) {
        this.apellidosDoctor = apellidosDoctor;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public int getCodigoEspecialidad() {
        return codigoEspecialidad;
    }

    public void setCodigoEspecialidad(int codigoEspecialidad) {
        this.codigoEspecialidad = codigoEspecialidad;
    }

    public int getCodigoPaciente() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toString() {
        return  numeroColegiado + "|" + nombresDoctores +  "|" + codigoEspecialidad ;
    }
    
}
