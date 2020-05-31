package com.marco.santdelivery.Cloud;


import com.marco.santdelivery.Empresas.Model.Categorias;
import com.marco.santdelivery.Empresas.Model.EmpresasEntity;
import com.marco.santdelivery.Empresas.Model.ProductosImagenesEntity;
import com.marco.santdelivery.Productos.Model.ProductosEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IUsersApi {
    @POST("/api/repartidor/login")
    Call<ResponseLogin> LoginUser(@Body Bodylogin user);

    @GET("/api/repartidor/empresas")
    Call<List<EmpresasEntity>> ObtenerEmpresas();

    @GET("/api/repartidor/empresas/productos")
    Call<List<ProductosEntity>> ObtenerProductos();

    @GET("/api/repartidor/empresas/productosImagenes")
    Call<List<ProductosImagenesEntity>> ObtenerProductosImagenes();

    @GET("/api/repartidor/empresas/categorias")
    Call<List<Categorias>> ObtenerCategorias();

}
