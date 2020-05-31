package com.marco.santdelivery.Empresas.Adapter;

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
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.marco.santdelivery.Empresas.EmpresasMvp;
import com.marco.santdelivery.Empresas.Model.EmpresasEntity;
import com.marco.santdelivery.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class EmpresasAdapter extends RecyclerView.Adapter<EmpresasAdapter.EmpresasViewHolder> {
    private List<EmpresasEntity> listaEmpresas;
    private Context context;
    private EmpresasMvp.View mview;
private Activity activity;
    public EmpresasAdapter(Context ctx, List<EmpresasEntity> s, EmpresasMvp.View view,Activity act) {
        this.context = ctx;
        this.listaEmpresas = s;
        this.mview = view;
        this.activity=act;
    }

    public EmpresasAdapter(Context ctx) {
        this.context = ctx;

    }

    @Override
    public int getItemCount() {
        return listaEmpresas.size();
    }


    @Override
    public void onBindViewHolder(EmpresasViewHolder clientesViewHolder, final int i) {
        clientesViewHolder.tvNameEmpresa.setText((CharSequence) listaEmpresas.get(i).getNombre());
        clientesViewHolder.tvDescripcionCorta.setText((CharSequence) listaEmpresas.get(i).getDescripcionCorta());
        clientesViewHolder.tvDescripcion .setText((CharSequence) listaEmpresas.get(i).getDescripcion());
        Glide.with(activity.getApplicationContext())
                .load(listaEmpresas.get(i).getImagen())
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.2f)
                .override(300, 200)//Target.SIZE_ORIGINAL
                .placeholder(R.drawable.fondoapp)
                .into(clientesViewHolder.ivAdapterImg);

        /*Picasso.with(activity).load(listaEmpresas.get(i).getImagen())
                .error(R.drawable.fondoapp).fit().into(clientesViewHolder.ivAdapterImg);*/

        Calendar c = Calendar.getInstance();

        long FechaActual=c.getTime().getTime();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR,listaEmpresas.get(i).getHorarioAtencionInicio());
        long FechaApertura=cal.getTime().getTime();
        Calendar calFin = Calendar.getInstance();
        calFin.set(Calendar.HOUR,listaEmpresas.get(i).getHorarioAtencionFin());
        long FechaCierre=calFin.getTime().getTime();
        Calendar calendario = Calendar.getInstance();
        if (FechaActual<FechaApertura){
            clientesViewHolder.tvEstado .setText("Abre a las "+listaEmpresas.get(i).getHorarioAtencionInicio()+":00");
           clientesViewHolder.tvEstado.setTextColor(activity.getResources().getColor(R.color.PrimariLightYelow));
        }
        if (FechaActual>=FechaApertura && FechaActual<=FechaCierre){
            clientesViewHolder.tvEstado .setText("Abierto hasta las "+listaEmpresas.get(i).getHorarioAtencionFin()+":00");
          clientesViewHolder.tvEstado.setTextColor(activity.getResources().getColor(R.color.intro_slide_2_light));
        }else{
            clientesViewHolder.tvEstado .setText("Vuelve a Abrir mañana a las "+listaEmpresas.get(i).getHorarioAtencionInicio()+":00");
            clientesViewHolder.tvEstado.setTextColor(activity.getResources().getColor(R.color.accept));
        }

        clientesViewHolder.cardEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mview.recyclerViewListClicked(v, listaEmpresas.get(i));
            }
        });

        clientesViewHolder.btnLlamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mview.showCellPhone(view, listaEmpresas.get(i));
            }
        });
        clientesViewHolder.btnWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mview.sendMessageWhatsapp(view, listaEmpresas.get(i));
            }
        });
        clientesViewHolder.btnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mview.viewUbicacion(view, listaEmpresas.get(i));
            }
        });
    }

    @Override
    public EmpresasViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_store, viewGroup, false);

        return new EmpresasViewHolder(itemView);
    }


    public static class EmpresasViewHolder extends RecyclerView.ViewHolder {

        protected ImageView ivAdapterImg;
        protected TextView tvNameEmpresa;
        protected  TextView tvDescripcionCorta;
        protected TextView tvDescripcion;
        protected TextView tvEstado;
        protected ImageButton btnMapa;
        protected ImageButton btnLlamar;
        protected ImageButton btnWhatsapp;
        protected CardView cardEmpresa;

        public EmpresasViewHolder(View v) {
            super(v);
            ivAdapterImg = (ImageView) v.findViewById(R.id.img_empresa);
            tvNameEmpresa = (TextView) v.findViewById(R.id.name_empresa);
            tvDescripcionCorta = (TextView) v.findViewById(R.id.empresa_descripcioncorta);
            tvDescripcion = (TextView) v.findViewById(R.id.empresa_descripcion);
            tvEstado=(TextView)v.findViewById(R.id.empresa_estado);
            btnMapa=(ImageButton)v.findViewById(R.id.empresa_btnmapa);
            btnLlamar=(ImageButton)v.findViewById(R.id.empresa_btnllamar);
            btnWhatsapp=(ImageButton)v.findViewById(R.id.empresa_btnwhatsapp);
            cardEmpresa = (CardView) v.findViewById(R.id.card_empresa);
        }
    }

    public void setFilter(List<EmpresasEntity> ListaFiltrada) {
        this.listaEmpresas = new ArrayList<>();
        this.listaEmpresas.addAll(ListaFiltrada);
        notifyDataSetChanged();
    }
}