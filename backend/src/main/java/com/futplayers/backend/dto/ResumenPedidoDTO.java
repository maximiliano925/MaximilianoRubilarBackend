package com.futplayers.backend.dto;

public class ResumenPedidoDTO {
    private double totalProductos;
    private double costoEnvio;
    private double totalFinal;
    private String mensajeEnvio; // Ej: "Envío Gratis por llevar 3 productos"

    // Constructor
    public ResumenPedidoDTO(double totalProductos, double costoEnvio, double totalFinal, String mensajeEnvio) {
        this.totalProductos = totalProductos;
        this.costoEnvio = costoEnvio;
        this.totalFinal = totalFinal;
        this.mensajeEnvio = mensajeEnvio;
    }

    // Getters y Setters
    public double getTotalProductos() { return totalProductos; }
    public void setTotalProductos(double totalProductos) { this.totalProductos = totalProductos; }
    
    public double getCostoEnvio() { return costoEnvio; }
    public void setCostoEnvio(double costoEnvio) { this.costoEnvio = costoEnvio; }
    
    public double getTotalFinal() { return totalFinal; }
    public void setTotalFinal(double totalFinal) { this.totalFinal = totalFinal; }
    
    public String getMensajeEnvio() { return mensajeEnvio; }
    public void setMensajeEnvio(String mensajeEnvio) { this.mensajeEnvio = mensajeEnvio; }
}