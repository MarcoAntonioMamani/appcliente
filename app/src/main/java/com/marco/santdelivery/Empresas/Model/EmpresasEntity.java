package com.marco.santdelivery.Empresas.Model;

import java.util.Date;

public class EmpresasEntity  implements Comparable<EmpresasEntity> {
    int Id;
    String Nombre;
    String DescripcionCorta;
    String Descripcion;
    Double Latitud;
    Double Longitud;
    String imagen;
    String Propietario;
    String Telefono;
    int HorarioAtencionInicio;
    int HorarioAtencionFin;
    int Delivery;
    int CategoriaEmpresa;
    String Mensaje;

    public EmpresasEntity() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getDescripcionCorta() {
        return DescripcionCorta;
    }

    public void setDescripcionCorta(String descripcionCorta) {
        DescripcionCorta = descripcionCorta;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public Double getLatitud() {
        return Latitud;
    }

    public void setLatitud(Double latitud) {
        Latitud = latitud;
    }

    public Double getLongitud() {
        return Longitud;
    }

    public void setLongitud(Double longitud) {
        Longitud = longitud;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getPropietario() {
        return Propietario;
    }

    public void setPropietario(String propietario) {
        Propietario = propietario;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public int getHorarioAtencionInicio() {
        return HorarioAtencionInicio;
    }

    public void setHorarioAtencionInicio(int horarioAtencionInicio) {
        HorarioAtencionInicio = horarioAtencionInicio;
    }

    public int getHorarioAtencionFin() {
        return HorarioAtencionFin;
    }

    public void setHorarioAtencionFin(int horarioAtencionFin) {
        HorarioAtencionFin = horarioAtencionFin;
    }

    public int getDelivery() {
        return Delivery;
    }

    public void setDelivery(int delivery) {
        Delivery = delivery;
    }

    public int getCategoriaEmpresa() {
        return CategoriaEmpresa;
    }

    public void setCategoriaEmpresa(int categoriaEmpresa) {
        CategoriaEmpresa = categoriaEmpresa;
    }

    public String getMensaje() {
        return Mensaje;
    }

    public void setMensaje(String mensaje) {
        Mensaje = mensaje;
    }

    @Override
    public int compareTo( EmpresasEntity cliente) {
        String a=new String(String.valueOf(this.getNombre()));
        String b=new String(String.valueOf(cliente.getNombre()));
        return a.compareTo(b);
    }
}
