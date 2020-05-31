package com.marco.santdelivery.Empresas.Model;

public class Categorias implements Cloneable {

    int id;
    String NombreCategoria;
    int EmpresaId;
    int Estado;
    public Categorias clone() throws CloneNotSupportedException {
        return (Categorias) super.clone();
    }
    public int getEstado() {
        return Estado;
    }

    public void setEstado(int estado) {
        Estado = estado;
    }

    public Categorias(int id, String nombreCategoria, int empresaId,int Estado) {
        this.id = id;
        NombreCategoria = nombreCategoria;
        EmpresaId = empresaId;
        this.Estado=Estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreCategoria() {
        return NombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        NombreCategoria = nombreCategoria;
    }

    public int getEmpresaId() {
        return EmpresaId;
    }

    public void setEmpresaId(int empresaId) {
        EmpresaId = empresaId;
    }
}
