package com.marco.santdelivery.Pedidos.Model;

import com.marco.santdelivery.Pedidos.PedidoDetalle;

import java.util.Date;
import java.util.List;

public class Pedidos {

    int Id;
    int ClienteId;
    int RepartidorId;
    int VendedorId;
    int ZonaId;
    int EstadoPedido;
    Date  FechaPedido;
    Date FechaEntrega;
    String HoraPedido;
    String DetallePedido;
    double Latitud;
    double Longitud;
    List<PedidoDetalle> listDetalle;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getClienteId() {
        return ClienteId;
    }

    public void setClienteId(int clienteId) {
        ClienteId = clienteId;
    }

    public int getRepartidorId() {
        return RepartidorId;
    }

    public void setRepartidorId(int repartidorId) {
        RepartidorId = repartidorId;
    }

    public int getVendedorId() {
        return VendedorId;
    }

    public void setVendedorId(int vendedorId) {
        VendedorId = vendedorId;
    }

    public int getZonaId() {
        return ZonaId;
    }

    public void setZonaId(int zonaId) {
        ZonaId = zonaId;
    }

    public int getEstadoPedido() {
        return EstadoPedido;
    }

    public void setEstadoPedido(int estadoPedido) {
        EstadoPedido = estadoPedido;
    }

    public Date getFechaPedido() {
        return FechaPedido;
    }

    public void setFechaPedido(Date fechaPedido) {
        FechaPedido = fechaPedido;
    }

    public Date getFechaEntrega() {
        return FechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        FechaEntrega = fechaEntrega;
    }

    public String getHoraPedido() {
        return HoraPedido;
    }

    public void setHoraPedido(String horaPedido) {
        HoraPedido = horaPedido;
    }

    public String getDetallePedido() {
        return DetallePedido;
    }

    public void setDetallePedido(String detallePedido) {
        DetallePedido = detallePedido;
    }

    public double getLatitud() {
        return Latitud;
    }

    public void setLatitud(double latitud) {
        Latitud = latitud;
    }

    public double getLongitud() {
        return Longitud;
    }

    public void setLongitud(double longitud) {
        Longitud = longitud;
    }

    public List<PedidoDetalle> getListDetalle() {
        return listDetalle;
    }

    public void setListDetalle(List<PedidoDetalle> listDetalle) {
        this.listDetalle = listDetalle;
    }
}
