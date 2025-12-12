package com.futplayers.backend.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.futplayers.backend.dto.CompraRequestDTO;
import com.futplayers.backend.dto.ItemCompraDTO;
import com.futplayers.backend.dto.ResumenPedidoDTO;
import com.futplayers.backend.entities.Comuna;
import com.futplayers.backend.entities.DetallePedido;
import com.futplayers.backend.entities.Inventario;
import com.futplayers.backend.entities.Pedido;
import com.futplayers.backend.entities.Producto;
import com.futplayers.backend.entities.Usuario;
import com.futplayers.backend.repositories.ComunaRepository;
import com.futplayers.backend.repositories.InventarioRepository;
import com.futplayers.backend.repositories.PedidoRepository;
import com.futplayers.backend.repositories.ProductoRepository;
import com.futplayers.backend.repositories.RegionRepository;
import com.futplayers.backend.repositories.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class PedidoService {

    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private ProductoRepository productoRepository;
    @Autowired private InventarioRepository inventarioRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private ComunaRepository comunaRepository;
    @Autowired private RegionRepository regionRepository;

    @Transactional
    public Pedido procesarCompra(CompraRequestDTO compra) {
        
        Usuario usuario = usuarioRepository.findById(compra.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Comuna comuna = comunaRepository.findById(compra.getComunaId())
                .orElseThrow(() -> new RuntimeException("Comuna no válida"));

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstado("PAGADO"); 
        
        if (compra.getDireccionCalle() != null && !compra.getDireccionCalle().isEmpty()) {
            pedido.setDireccionEnvio(compra.getDireccionCalle());
        } else {
            pedido.setDireccionEnvio(usuario.getDireccionCalle());
        }
        pedido.setComuna(comuna);

        double totalProductos = 0;
        int cantidadTotalUnidades = 0;
        List<DetallePedido> detalles = new ArrayList<>();

        for (ItemCompraDTO item : compra.getItems()) {
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no existe ID: " + item.getProductoId()));

            Inventario inv = inventarioRepository.findByProductoIdAndTalla(item.getProductoId(), item.getTalla())
                    .orElseThrow(() -> new RuntimeException("Sin stock disponible"));

            // 👇👇 AQUÍ ESTÁ EL CAMBIO DEL MENSAJE 👇👇
            if (inv.getStock() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para " + producto.getNombre() 
                    + ". disponibilidad " + inv.getStock() + " unidades en talla " + item.getTalla());
            }
            // 👆👆 AHORA MUESTRA LA CANTIDAD REAL Y LA TALLA 👆👆

            inv.setStock(inv.getStock() - item.getCantidad());
            inventarioRepository.save(inv);

            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setProducto(producto);
            detalle.setTalla(item.getTalla());
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecio());
            
            detalles.add(detalle);
            totalProductos += (producto.getPrecio() * item.getCantidad());
            cantidadTotalUnidades += item.getCantidad();
        }

        pedido.setDetalles(detalles);

        double costoEnvio = 0;
        if (cantidadTotalUnidades >= 3) {
            costoEnvio = 0; 
        } else {
            costoEnvio = comuna.getRegion().getPrecioEnvio();
        }

        pedido.setCostoEnvio(costoEnvio);
        pedido.setTotal(totalProductos + costoEnvio);

        return pedidoRepository.save(pedido);
    }

    // Calculadora
    public ResumenPedidoDTO calcularResumen(CompraRequestDTO compra) {
        double totalProductos = 0;
        int cantidadTotalUnidades = 0;

        for (ItemCompraDTO item : compra.getItems()) {
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado ID: " + item.getProductoId()));
            
            totalProductos += (producto.getPrecio() * item.getCantidad());
            cantidadTotalUnidades += item.getCantidad();
        }

        double costoEnvio = 0;
        String mensaje = "";

        if (cantidadTotalUnidades >= 3) {
            costoEnvio = 0;
            mensaje = "¡Envío GRATIS por promoción!";
        } else {
            if (compra.getComunaId() != null) {
                Comuna comuna = comunaRepository.findById(compra.getComunaId())
                    .orElseThrow(() -> new RuntimeException("Comuna no válida para cálculo"));
                
                if (comuna.getRegion() != null) {
                    costoEnvio = comuna.getRegion().getPrecioEnvio();
                    mensaje = "Envío a región: " + comuna.getRegion().getNombre();
                } else {
                    throw new RuntimeException("Error: La comuna existe, pero no tiene precio de envío asociado.");
                }
            } else {
                costoEnvio = 0;
                mensaje = "Seleccione una comuna para calcular envío";
            }
        }

        double totalFinal = totalProductos + costoEnvio;
        return new ResumenPedidoDTO(totalProductos, costoEnvio, totalFinal, mensaje);
    }

    public List<Pedido> obtenerHistorialUsuario(Long usuarioId) {
        return pedidoRepository.findByUsuarioIdOrderByFechaDesc(usuarioId);
    }

    public List<Pedido> listarTodosLosPedidos() {
        return pedidoRepository.findAll();
    }
}