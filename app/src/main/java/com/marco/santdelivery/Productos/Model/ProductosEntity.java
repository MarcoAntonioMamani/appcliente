package com.marco.santdelivery.Productos.Model;

import android.support.annotation.NonNull;

import com.marco.santdelivery.Empresas.Model.EmpresasEntity;

public class ProductosEntity  implements Comparable<ProductosEntity> {

    int id;
    String NombreProducto;
    String DescripcionProducto;
    int Estado;
    int CategoriaId;
    int EmpresaId;
    String Descripcion;
    double Precio;
    String ImageProducto;
    String NombreCategoria;
double cantidad;
    public ProductosEntity() {
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreProducto() {
        return NombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        NombreProducto = nombreProducto;
    }

    public String getDescripcionProducto() {
        return DescripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        DescripcionProducto = descripcionProducto;
    }

    public int getEstado() {
        return Estado;
    }

    public void setEstado(int estado) {
        Estado = estado;
    }

    public int getCategoriaId() {
        return CategoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        CategoriaId = categoriaId;
    }

    public int getEmpresaId() {
        return EmpresaId;
    }

    public void setEmpresaId(int empresaId) {
        EmpresaId = empresaId;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public double getPrecio() {
        return Precio;
    }

    public void setPrecio(double precio) {
        Precio = precio;
    }

    public String getImageProducto() {
        return ImageProducto;
    }

    public void setImageProducto(String imageProducto) {
        ImageProducto = imageProducto;
    }

    public String getNombreCategoria() {
        return NombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        NombreCategoria = nombreCategoria;
    }


    @Override
    public int compareTo(@NonNull ProductosEntity o) {
            String a=new String(String.valueOf(this.getNombreProducto()));
        String b=new String(String.valueOf(o.getNombreProducto()));
        return a.compareTo(b);
    }
}
