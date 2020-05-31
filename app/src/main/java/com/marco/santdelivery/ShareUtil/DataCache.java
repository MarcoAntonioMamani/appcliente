package com.marco.santdelivery.ShareUtil;

import android.widget.TextView;

import com.marco.santdelivery.Empresas.Model.Categorias;
import com.marco.santdelivery.Empresas.Model.EmpresasEntity;
import com.marco.santdelivery.Empresas.Model.ProductosImagenesEntity;
import com.marco.santdelivery.Pedidos.PedidoDetalle;
import com.marco.santdelivery.Productos.Model.ProductosEntity;

import java.util.ArrayList;
import java.util.List;

public class DataCache {

    public static List<EmpresasEntity> ListEmpresas=null;
    public static EmpresasEntity EmpresaSelected=null;
    public static TextView tvTitleMenu;
    public static List<ProductosEntity> listProductos=null;
    public static List<ProductosImagenesEntity> listImagenes=null;
    public static List<Categorias> ListCategorias=null;
    public static ProductosEntity ProductoSelected=null;
    public static List<PedidoDetalle> listDetalle=null;


}
