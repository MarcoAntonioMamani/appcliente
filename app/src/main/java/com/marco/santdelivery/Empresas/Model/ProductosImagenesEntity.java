package com.marco.santdelivery.Empresas.Model;

public class ProductosImagenesEntity {

    int id;
    int  ProductoId;
    String NombreImage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductoId() {
        return ProductoId;
    }

    public void setProductoId(int productoId) {
        ProductoId = productoId;
    }

    public String getNombreImage() {
        return NombreImage;
    }

    public void setNombreImage(String nombreImage) {
        NombreImage = nombreImage;
    }
}
