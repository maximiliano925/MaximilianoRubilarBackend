package com.futplayers.backend.dto;

public class PagoRequest {
    private Long pedidoId;

    // Constructor vacío
    public PagoRequest() {}

    // GETTER (Esto es lo que te faltaba)
    public Long getPedidoId() {
        return pedidoId;
    }

    // SETTER
    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }
}