package com.marco.santdelivery.Cloud;

import android.content.Context;


import com.marco.santdelivery.Empresas.Model.Categorias;
import com.marco.santdelivery.Empresas.Model.EmpresasEntity;
import com.marco.santdelivery.Empresas.Model.ProductosImagenesEntity;
import com.marco.santdelivery.Productos.Model.ProductosEntity;
import com.marco.santdelivery.ShareUtil.DataPreferences;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {

    private static IUsersApi service;
    public static ApiManager apiManager;
private static Context mcontext;
    private ApiManager(Context context) {
        String Url="";
        if (DataPreferences.getPref("servicio",context)==null){
           Url="http://181.188.134.10:3050";
        }else{
            Url=DataPreferences.getPref("servicio",context);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(IUsersApi.class);
    }

    public static ApiManager getInstance(Context context) {
       if (apiManager == null) {
            apiManager = new ApiManager( context);
        }
        mcontext=context;
        return apiManager;
    }

    public void LoginUser(Bodylogin user, Callback<ResponseLogin> callback) {
        Call<ResponseLogin> userCall = service.LoginUser(user);
        userCall.enqueue(callback);
    }





    public void ObtenerEmpresas( Callback<List<EmpresasEntity>> callback) {
        Call<List<EmpresasEntity>> userCall = service.ObtenerEmpresas();
        userCall.enqueue(callback);
    }
    public void ObtenerProductos( Callback<List<ProductosEntity>> callback) {
        Call<List<ProductosEntity>> userCall = service.ObtenerProductos() ;
        userCall.enqueue(callback);
    }
    public void ObtenerProductosImagenes( Callback<List<ProductosImagenesEntity>> callback) {
        Call<List<ProductosImagenesEntity>> userCall = service.ObtenerProductosImagenes() ;
        userCall.enqueue(callback);
    }
    public void ObtenerCategorias( Callback<List<Categorias>> callback) {
        Call<List<Categorias>> userCall = service.ObtenerCategorias() ;
        userCall.enqueue(callback);
    }
}
