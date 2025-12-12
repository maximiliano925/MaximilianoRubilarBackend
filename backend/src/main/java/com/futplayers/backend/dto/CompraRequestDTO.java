package com.futplayers.backend.dto;
import java.util.List;

public class CompraRequestDTO {
    private Long usuarioId;
    private Long comunaId;         // <--- ID de la comuna seleccionada
    private String direccionCalle; // <--- "Pasaje Los Alerces 123"
    private List<ItemCompraDTO> items;

    // Getters y Setters
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public Long getComunaId() { return comunaId; }
    public void setComunaId(Long comunaId) { this.comunaId = comunaId; }
    public String getDireccionCalle() { return direccionCalle; }
    public void setDireccionCalle(String direccionCalle) { this.direccionCalle = direccionCalle; }
    public List<ItemCompraDTO> getItems() { return items; }
    public void setItems(List<ItemCompraDTO> items) { this.items = items; }
}