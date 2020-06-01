package com.marco.santdelivery.Productos;

import android.content.Context;
import com.google.android.gms.common.internal.Preconditions;

import com.google.gson.Gson;
import com.marco.santdelivery.Cloud.ApiManager;
import com.marco.santdelivery.Empresas.EmpresasMvp;
import com.marco.santdelivery.Empresas.Model.Categorias;
import com.marco.santdelivery.Empresas.Model.ProductosImagenesEntity;
import com.marco.santdelivery.Productos.Model.ProductosEntity;
import com.marco.santdelivery.ShareUtil.DataCache;
import com.marco.santdelivery.ShareUtil.DataPreferences;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductosPresenter  implements ProductorMvp.Presenter{


    private final ProductorMvp.View mSincronizarview;
    private final Context mContext;
 int contador=0;
    public ProductosPresenter(ProductorMvp.View Sincronizarview, Context context) {

        this.mContext=context;
        mSincronizarview = Preconditions.checkNotNull(Sincronizarview);
        mSincronizarview.setPresenter(this);
    }

    @Override
    public void GetDatos() {
        contador=0;
        ApiManager apiManager=ApiManager.getInstance(mContext);
        apiManager.ObtenerProductos( new Callback<List<ProductosEntity>>() {
            @Override
            public void onResponse(Call<List<ProductosEntity>> call, Response<List<ProductosEntity>> response) {
                final List<ProductosEntity> responseUser = (List<ProductosEntity>) response.body();
                if (response.code()==404){
                    mSincronizarview.ShowMessageResult("No es posible conectarse con el servicio. "+ response.message());
                    return;
                }
                if (response.isSuccessful() && responseUser != null) {
                    contador++;
                    Gson gson = new Gson();
                    String json = gson.toJson(responseUser);
                    DataPreferences.putPref("Productos",json,mContext);
                    DataCache.listProductos=responseUser;
                    GetImagenes();
                } else {
                    mSincronizarview.ShowMessageResult("No se pudo Obtener Datos del Servidor para Clientes");
                }
            }

            @Override
            public void onFailure(Call<List<ProductosEntity>> call, Throwable t) {
                mSincronizarview.ShowMessageResult("No es posible conectarse con el servicio.");
            }
        });

    }

    public void GetImagenes(){
        ApiManager apiManager=ApiManager.getInstance(mContext);
        apiManager.ObtenerProductosImagenes( new Callback<List<ProductosImagenesEntity>>() {
            @Override
            public void onResponse(Call<List<ProductosImagenesEntity>> call, Response<List<ProductosImagenesEntity>> response) {
                final List<ProductosImagenesEntity> responseUser = (List<ProductosImagenesEntity>) response.body();
                if (response.code()==404){
                    mSincronizarview.ShowMessageResult("No es posible conectarse con el servicio. "+ response.message());
                    return;
                }
                if (response.isSuccessful() && responseUser != null) {
                    contador++;
                    DataCache.listImagenes=responseUser;
                    GetCategorias();
                    if (contador==3){
                        mSincronizarview.MostrarDatos(DataCache.listProductos);
                    }
                } else {
                    mSincronizarview.ShowMessageResult("No se pudo Obtener Datos del Servidor para Clientes");
                }
            }

            @Override
            public void onFailure(Call<List<ProductosImagenesEntity>> call, Throwable t) {
                mSincronizarview.ShowMessageResult("No es posible conectarse con el servicio.");
            }
        });
        GetCategorias();
    }

    public void GetCategorias(){
        ApiManager apiManager=ApiManager.getInstance(mContext);
        apiManager.ObtenerCategorias( new Callback<List<Categorias>>() {
            @Override
            public void onResponse(Call<List<Categorias>> call, Response<List<Categorias>> response) {
                final List<Categorias> responseUser = (List<Categorias>) response.body();
                if (response.code()==404){
                    mSincronizarview.ShowMessageResult("No es posible conectarse con el servicio. "+ response.message());
                    return;
                }
                if (response.isSuccessful() && responseUser != null) {
                    Gson gson = new Gson();
                    String json = gson.toJson(responseUser);
                    DataPreferences.putPref("Categorias",json,mContext);
                    DataCache.ListCategorias=responseUser;
                    contador++;
                    if (contador==3){
                        mSincronizarview.MostrarDatos(DataCache.listProductos);
                    }

                } else { mSincronizarview.ShowMessageResult("No se pudo Obtener Datos del Servidor para Clientes");
                }
            }

            @Override
            public void onFailure(Call<List<Categorias>> call, Throwable t) {
                mSincronizarview.ShowMessageResult("No es posible conectarse con el servicio.");
            }
        });
    }
}
