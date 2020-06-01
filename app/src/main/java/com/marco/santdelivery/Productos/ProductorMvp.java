package com.marco.santdelivery.Productos;

import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.marco.santdelivery.Empresas.EmpresasMvp;
import com.marco.santdelivery.Empresas.Model.Categorias;
import com.marco.santdelivery.Empresas.Model.EmpresasEntity;
import com.marco.santdelivery.Pedidos.PedidoDetalle;
import com.marco.santdelivery.Productos.Model.ProductosEntity;

import java.util.List;

public interface ProductorMvp {

    interface View {
        void recyclerViewListClicked(android.view.View v, ProductosEntity empresa);
        void recyclerViewListClickedCategoria(android.view.View v, Categorias empresa, TextView tvCategoria);
        void setPresenter(ProductorMvp.Presenter presenter);
        void ShowMessageResult(String message);
        void MostrarDatos(List<ProductosEntity> listEmpresa);
        void AddCantidadProducto(ElegantNumberButton v, int valor, ProductosEntity producto);
        void ShowSyncroMgs(String message);

    }
    interface Presenter{
        void GetDatos();
    }
}
