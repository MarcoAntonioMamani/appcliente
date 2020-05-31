package com.marco.santdelivery.Productos.Adapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.marco.santdelivery.Productos.Model.ProductosEntity;
import com.marco.santdelivery.Productos.ProductorMvp;
import com.marco.santdelivery.R;
import com.marco.santdelivery.ShareUtil.DataCache;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.EmpresasViewHolder> {
private List<ProductosEntity> listaEmpresas;
private Context context;
private ProductorMvp.View mview;
private Activity activity;
public ProductosAdapter(Context ctx, List<ProductosEntity> s, ProductorMvp.View view,Activity act) {
        this.context = ctx;
        this.listaEmpresas = s;
        this.mview = view;
        this.activity=act;
        }

public ProductosAdapter(Context ctx) {
        this.context = ctx;

        }

@Override
public int getItemCount() {
        return listaEmpresas.size();
        }


@Override
public void onBindViewHolder(EmpresasViewHolder clientesViewHolder, final int i) {
        clientesViewHolder.tvNameMarca.setText((CharSequence) listaEmpresas.get(i).getNombreCategoria());
        clientesViewHolder.tvNombreProducto.setText((CharSequence) listaEmpresas.get(i).getNombreProducto());
        clientesViewHolder.tvPrecios .setText(""+ listaEmpresas.get(i).getPrecio()+" Bs");
        clientesViewHolder.tvCantidad.setNumber(getCantidad(listaEmpresas.get(i).getId()));

    final LinearLayout card=clientesViewHolder.linearFondo;
      if (getCantidadInteger(listaEmpresas.get(i).getId())>0){
          clientesViewHolder.tvCantidad.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
          card.setBackgroundColor(activity.getResources().getColor(R.color.azulclaro));

      }else{
          clientesViewHolder.tvCantidad.setBackgroundColor(activity.getResources().getColor(R.color.amarillo));
          card.setBackgroundColor(activity.getResources().getColor(R.color.white));

      }

        Glide.with(activity.getApplicationContext())
                .load(listaEmpresas.get(i).getImageProducto())
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.2f)
                .override(300, 200)//Target.SIZE_ORIGINAL
                .placeholder(R.drawable.fondoapp)
                .into(clientesViewHolder.ivAdapterImg);



        clientesViewHolder.cardProductos.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        //mview.recyclerViewListClicked(v, listaEmpresas.get(i));
        }
        });

        clientesViewHolder.linearProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mview.recyclerViewListClicked(v, listaEmpresas.get(i));
            }
        });
    clientesViewHolder.tvCantidad.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
        @Override
        public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
            mview.AddCantidadProducto(view,newValue,listaEmpresas.get(i));
            if (newValue>0){
                view.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
                card.setBackgroundColor(activity.getResources().getColor(R.color.azulclaro));

            }else{
                view.setBackgroundColor(activity.getResources().getColor(R.color.amarillo));
                card.setBackgroundColor(activity.getResources().getColor(R.color.white));

            }

        }
    });
        }

        public String getCantidad(int ProductoId){

            for (int i = 0; i < DataCache.listDetalle.size(); i++) {
                if(DataCache.listDetalle.get(i).getProductoId()==ProductoId){
                    return ""+(int)DataCache.listDetalle.get(i).getCantidad();
                }

            }
            return "0";
        }

    public int getCantidadInteger(int ProductoId){

        for (int i = 0; i < DataCache.listDetalle.size(); i++) {
            if(DataCache.listDetalle.get(i).getProductoId()==ProductoId){
                return (int)DataCache.listDetalle.get(i).getCantidad();
            }

        }
        return 0;
    }
@Override
public EmpresasViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
        from(viewGroup.getContext()).
        inflate(R.layout.card_productos, viewGroup, false);

        return new EmpresasViewHolder(itemView);
        }


public static class EmpresasViewHolder extends RecyclerView.ViewHolder {

    protected ImageView ivAdapterImg;
    protected TextView tvNameMarca;
    protected  TextView tvNombreProducto;
    protected TextView tvPrecios;
protected ElegantNumberButton tvCantidad;
    protected CardView cardProductos;
   LinearLayout linearFondo;
   LinearLayout linearProducto;
    public EmpresasViewHolder(View v) {
        super(v);
        ivAdapterImg = (ImageView) v.findViewById(R.id.producto_img);
        tvNameMarca = (TextView) v.findViewById(R.id.nombre_marca);
        tvNombreProducto = (TextView) v.findViewById(R.id.nombre_Producto);
        tvPrecios = (TextView) v.findViewById(R.id.producto_precio);
        tvCantidad=(ElegantNumberButton)v.findViewById(R.id.producto_cantidad);
        linearFondo=(LinearLayout)v.findViewById(R.id.linear_fondo);
        cardProductos = (CardView) v.findViewById(R.id.card_Productos);
        linearProducto=(LinearLayout)v.findViewById(R.id.linear_producto);
    }
}

    public void setFilter(List<ProductosEntity> ListaFiltrada) {
        this.listaEmpresas = new ArrayList<>();
        this.listaEmpresas.addAll(ListaFiltrada);
        notifyDataSetChanged();
    }

}
