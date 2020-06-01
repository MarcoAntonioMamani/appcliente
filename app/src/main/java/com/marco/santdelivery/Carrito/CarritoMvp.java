package com.marco.santdelivery.Carrito;

import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.marco.santdelivery.Empresas.Model.Categorias;
import com.marco.santdelivery.Pedidos.PedidoDetalle;
import com.marco.santdelivery.Productos.Model.ProductosEntity;
import com.marco.santdelivery.Productos.ProductorMvp;

import java.util.List;

public interface CarritoMvp {

    interface View {

        void setPresenter(CarritoMvp.Presenter presenter);
        void ShowMessageResult(String message);
        void AddCantidadProducto(ElegantNumberButton v, int valor, PedidoDetalle producto,TextView tvSubTotal);
        void ShowSyncroMgs(String message);

    }
    interface Presenter{
        void GetDatos();
    }
}
