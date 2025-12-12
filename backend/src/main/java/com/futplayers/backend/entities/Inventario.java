package com.futplayers.backend.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity @Table(name = "inventario")
public class Inventario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String talla;
    private int stock;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTalla() { return talla; }
    public void setTalla(String talla) { this.talla = talla; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
}