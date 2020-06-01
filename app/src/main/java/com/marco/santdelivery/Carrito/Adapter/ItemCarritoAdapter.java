package com.marco.santdelivery.Carrito.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.marco.santdelivery.Carrito.CarritoMvp;
import com.marco.santdelivery.Pedidos.PedidoDetalle;
import com.marco.santdelivery.Productos.Model.ProductosEntity;
import com.marco.santdelivery.Productos.ProductorMvp;
import com.marco.santdelivery.R;
import com.marco.santdelivery.ShareUtil.DataCache;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ItemCarritoAdapter extends RecyclerView.Adapter<ItemCarritoAdapter.EmpresasViewHolder> {
    private List<PedidoDetalle> listaDetalle;
    private Context context;
    private CarritoMvp.View mview;
    private Activity activity;

    public ItemCarritoAdapter(Context ctx, List<PedidoDetalle> s, CarritoMvp.View view, Activity act) {
        this.context = ctx;
        this.listaDetalle = s;
        this.mview = view;
        this.activity = act;
    }

    public ItemCarritoAdapter(Context ctx) {
        this.context = ctx;

    }

    @Override
    public int getItemCount() {
        return listaDetalle.size();
    }


    @Override
    public void onBindViewHolder(EmpresasViewHolder clientesViewHolder, final int i) {
        ProductosEntity producto=ObtenerProducto(listaDetalle.get(i).getProductoId());
        if (producto!=null){

            clientesViewHolder.tvNameProducto.setText(producto.getNombreProducto()  + " \n Precio : "+producto.getPrecio());
        }

        clientesViewHolder.tvPrecio.setText(listaDetalle.get(i).getSubTotal()+" Bs");
        clientesViewHolder.tvCantidad.setNumber(""+ (int)listaDetalle.get(i).getCantidad());

        final TextView tvSubtotal=clientesViewHolder.tvPrecio;

        clientesViewHolder.tvCantidad.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                mview.AddCantidadProducto(view, newValue, listaDetalle.get(i),tvSubtotal);


            }
        });
    }

public ProductosEntity ObtenerProducto(int ProductoId){

        List<ProductosEntity> listProducto= DataCache.listProductos;
    for (int i = 0; i < listProducto.size(); i++) {
        if (listProducto.get(i).getId()==ProductoId){
            return listProducto.get(i);
        }
    }
    return null;
}


    @Override
    public EmpresasViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_carito_detail, viewGroup, false);

        return new EmpresasViewHolder(itemView);
    }


    public static class EmpresasViewHolder extends RecyclerView.ViewHolder {

        protected TextView tvNameProducto;
        protected TextView tvPrecio;
        protected ElegantNumberButton tvCantidad;


        public EmpresasViewHolder(View v) {
            super(v);

            tvCantidad = (ElegantNumberButton) v.findViewById(R.id.carrito_cantidad);
            tvNameProducto=(TextView) v.findViewById(R.id.carito_nombre);
            tvPrecio=(TextView)v.findViewById(R.id.carito_subtotal);
        }
    }

    public void setFilter(List<PedidoDetalle> ListaFiltrada) {
        this.listaDetalle = new ArrayList<>();
        this.listaDetalle.addAll(ListaFiltrada);
        notifyDataSetChanged();
    }
}