package org.edgargomez.bean;


public class DetalleReceta {
   private int CodigoDetalleReceta;
   private String dosis;
   private int codigoReceta;
   private int codigoMedicamento;

    public DetalleReceta() {
    }

    public DetalleReceta(int CodigoDetalleReceta, String dosis, int codigoReceta, int codigoMedicamento) {
        this.CodigoDetalleReceta = CodigoDetalleReceta;
        this.dosis = dosis;
        this.codigoReceta = codigoReceta;
        this.codigoMedicamento = codigoMedicamento;
    }

    public int getCodigoDetalleReceta() {
        return CodigoDetalleReceta;
    }

    public void setCodigoDetalleReceta(int CodigoDetalleReceta) {
        this.CodigoDetalleReceta = CodigoDetalleReceta;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public int getCodigoReceta() {
        return codigoReceta;
    }

    public void setCodigoReceta(int codigoReceta) {
        this.codigoReceta = codigoReceta;
    }

    public int getCodigoMedicamento() {
        return codigoMedicamento;
    }

    public void setCodigoMedicamento(int codigoMedicamento) {
        this.codigoMedicamento = codigoMedicamento;
    }
   
}
