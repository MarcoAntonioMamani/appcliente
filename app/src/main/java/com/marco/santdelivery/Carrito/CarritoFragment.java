package com.marco.santdelivery.Carrito;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.labters.lottiealertdialoglibrary.ClickListener;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.marco.santdelivery.Carrito.Adapter.ItemCarritoAdapter;
import com.marco.santdelivery.Empresas.Model.EmpresasEntity;
import com.marco.santdelivery.MainActivity;
import com.marco.santdelivery.Mapa.MapaTiendaActivity;
import com.marco.santdelivery.Pedidos.PedidoDetalle;
import com.marco.santdelivery.Productos.Adapter.ProductosAdapter;
import com.marco.santdelivery.Productos.Model.ProductosEntity;
import com.marco.santdelivery.Productos.ProductorMvp;
import com.marco.santdelivery.Productos.ProductosPresenter;
import com.marco.santdelivery.R;
import com.marco.santdelivery.ShareUtil.DataCache;
import com.marco.santdelivery.ShareUtil.DataPreferences;
import com.marco.santdelivery.ShareUtil.LocationGeo;
import com.pdfjet.Line;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarritoFragment extends Fragment implements CarritoMvp.View {

    TextView tvNameEmpresa;
    TextView tvDescripcionEmpresa;
    ImageView imgEmpresa;
    List<PedidoDetalle> listDetalle;
    EmpresasEntity empresa=null;
    private CarritoMvp.Presenter mSincronizarPresenter;
    LottieAlertDialog alertDialog;
    TextView tvTotal;
    ItemCarritoAdapter adapterItems;
    Context context;
    RecyclerView recList;
    LinearLayout btnWhatsapp;
    LinearLayout btnLocation;
    public CarritoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LocationGeo.getInstance(getContext(),getActivity());
        LocationGeo.PedirPermisoApp();
        ObtenerEmpresas();
        ObtenerProductos();
        EliminarCantidad0();
        context=getContext();
    }
    public void ObtenerEmpresas(){
    Gson gson = new Gson();
    String json = DataPreferences.getPref("Empresas",getContext());
    if (json==null){
        DataCache.ListEmpresas=new ArrayList<>();
    }else{
        if(json.equalsIgnoreCase("")){
            DataCache.ListEmpresas=new ArrayList<>();
        }else{
            Type listType = new TypeToken<ArrayList<EmpresasEntity>>(){}.getType();
            List<EmpresasEntity> list=  gson.fromJson(json,listType);
            DataCache.ListEmpresas=list;
        }
    }
}
    public void ObtenerProductos(){
        Gson gson = new Gson();
        String json = DataPreferences.getPref("Productos",getContext());
        if (json==null){
            DataCache.listProductos=new ArrayList<>();
        }else{
            if(json.equalsIgnoreCase("")){
                DataCache.listProductos=new ArrayList<>();
            }else{
                Type listType = new TypeToken<ArrayList<ProductosEntity>>(){}.getType();
                List<ProductosEntity> list=  gson.fromJson(json,listType);
                DataCache.listProductos=list;
            }
        }
    }
    public void EliminarCantidad0(){
        Gson gson = new Gson();
        String json = DataPreferences.getPref("Pedido",getContext());
        if (json==null){
            listDetalle=new ArrayList<>();
        }else{
            if(json.equalsIgnoreCase("")){
                listDetalle=new ArrayList<>();
            }else{
                Type listType = new TypeToken<ArrayList<PedidoDetalle>>(){}.getType();
                listDetalle=  gson.fromJson(json,listType);
                DataCache.listDetalle=listDetalle;
            }
        }
        List<PedidoDetalle> list=new ArrayList<>();
        for (int i = 0; i < listDetalle.size(); i++) {
            if (listDetalle.get(i).getCantidad()>0){
                list.add(listDetalle.get(i));
            }
        }
        listDetalle=list;
         gson = new Gson();
         json = gson.toJson(list);
        DataPreferences.putPref("Pedido",json,getContext());

    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("");
        DataCache.tvTitleMenu.setText("Ver Carrito");
        ObtenerProductos();
        ObtenerEmpresas();
        EliminarCantidad0();
        context=getContext();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_carrito, container, false);
        ObtenerEmpresas();
        ObtenerProductos();
        tvNameEmpresa=(TextView)view.findViewById(R.id.carrito_name_empresa);
        tvDescripcionEmpresa=(TextView)view.findViewById(R.id.carrito_empresa_descripcion);
        imgEmpresa=(ImageView) view.findViewById(R.id.carrito_img_empresa);
        tvTotal=(TextView)view.findViewById(R.id.carrito_total);
        btnWhatsapp=(LinearLayout)view.findViewById(R.id.carrito_btn_whatsapp);
        btnLocation=(LinearLayout)view.findViewById(R.id.carrito_btn_ubicacion);
        recList = (RecyclerView) view.findViewById(R.id.Productos_carrito);

        recList.setHasFixedSize(true);
        new CarritoPresenter(this,getContext());
        EliminarCantidad0();
        ObtenerListarDetalle();
        RellenarDatos();
        CargarRecycler(listDetalle);
        onClickWhatsapp();
        onClickLocation();
        return view;
    }

    public void onClickWhatsapp(){
        btnWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (empresa!=null){
                    if (!empresa.getTelefono().toString() .isEmpty()){
                        String url="https://api.whatsapp.com/send?phone=591"+empresa.getTelefono()+"&text="+empresa.getMensaje();
                        Uri uri = Uri.parse(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }

                }else{
                    ShowMessageResult("La empresa no Tiene registrado un numero Telefonico");
                }
            }
        });
    }
    public void onClickLocation(){
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (empresa!=null){


                    if (empresa.getLatitud()==0 || empresa.getLongitud()==0){
                        ShowMessageResult("La tienda seleccionado no tiene registrado una ubicación");
                    }else{

                        MainActivity fca = ((MainActivity) getActivity());
                        fca.startActivity(new Intent(getActivity(), MapaTiendaActivity.class));
                        fca.overridePendingTransition(R.transition.left_in, R.transition.left_out);
                    }


                }else{
                    ShowMessageResult("La empresa no Tiene registrado un numero Telefonico");
                }
            }
        });
    }

    public void CargarRecycler(List<PedidoDetalle> listCliente){
        if (listCliente!=null){
            adapterItems = new ItemCarritoAdapter(context,listCliente,this,getActivity());
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(GridLayoutManager .VERTICAL);
            final LayoutAnimationController controller =
                    AnimationUtils.loadLayoutAnimation(getActivity().getApplicationContext(), R.anim.layout_animation_fall_down);
            recList.setLayoutAnimation(controller);
            recList.setLayoutManager(llm);
            recList.setAdapter(adapterItems);

        }

    }
    public void ObtenerListarDetalle(){
        Gson gson = new Gson();
        String json = DataPreferences.getPref("Pedido",getContext());
        if (json==null){
            listDetalle=new ArrayList<>();
        }else{
            if(json.equalsIgnoreCase("")){
                listDetalle=new ArrayList<>();
            }else{
                Type listType = new TypeToken<ArrayList<PedidoDetalle>>(){}.getType();
                listDetalle=  gson.fromJson(json,listType);
                DataCache.listDetalle=listDetalle;
            }
        }
    }
    public void RellenarDatos(){
        List<EmpresasEntity> listEmpresa=DataCache.ListEmpresas;
        int EmpresaId=ObtenerIdEmpresa();

        for (int i = 0; i < listEmpresa.size(); i++) {
            if (listEmpresa.get(i).getId()==EmpresaId){
                empresa=listEmpresa.get(i);
            }
        }
        if (empresa!=null){
            tvNameEmpresa.setText(empresa.getNombre());
            tvDescripcionEmpresa.setText(empresa.getDescripcion());
            Glide.with(getActivity())
                    .load(empresa.getImagen())
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.2f)
                    .override(300, 200)//Target.SIZE_ORIGINAL
                    .placeholder(R.drawable.fondoapp)
                    .into(imgEmpresa);

        }
        CalcularTotal();

    }
    public void CalcularTotal(){

        double total=0;
        for (int i = 0; i < listDetalle.size(); i++) {
            total+=listDetalle.get(i).getSubTotal();
        }
        tvTotal.setText(""+total+"");
    }

    public int ObtenerIdEmpresa(){
        for (int i = 0; i < listDetalle.size(); i++) {
            if (listDetalle.get(i).getCantidad()>0){
                return ObtenerEmpresaPorProducto(listDetalle.get(i).getProductoId());
            }

        }
        return -1;
    }
    public int ObtenerEmpresaPorProducto(int ProductoId){
        List<ProductosEntity> listProducto=DataCache.listProductos;

        for (int i = 0; i < listProducto.size(); i++) {
            if (listProducto.get(i).getId()==ProductoId){
                return listProducto.get(i).getEmpresaId();
            }
        }
        return -1;
    }

    @Override
    public void setPresenter(CarritoMvp.Presenter presenter) {
        mSincronizarPresenter = Preconditions.checkNotNull(presenter);
    }

    @Override
    public void ShowMessageResult(String message) {
        if (alertDialog.isShowing()){
            alertDialog.dismiss();
        }
        alertDialog=new LottieAlertDialog.Builder(getContext(), DialogTypes.TYPE_WARNING)
                .setTitle("Advertencia")
                .setDescription(message)
                .setPositiveText("Aceptar")
                .setPositiveButtonColor(Color.parseColor("#008ebe"))
                .setPositiveTextColor(Color.parseColor("#ffffff"))
                .setPositiveListener(new ClickListener() {
                    @Override
                    public void onClick(@NotNull LottieAlertDialog lottieAlertDialog) {
                        lottieAlertDialog.dismiss();
                    }
                }).build();
        alertDialog.show();
    }

    @Override
    public void AddCantidadProducto(ElegantNumberButton v, int valor, PedidoDetalle producto,TextView tvSubTotal) {

        if (ExisteProductoDetalle(producto)){
            if (valor==0){
                ModificarCantidad(producto,valor);
                tvSubTotal.setText(""+valor*producto.getPrecio()+" Bs");

               Gson  gson = new Gson();
                String json = gson.toJson(listDetalle);
                DataPreferences.putPref("Pedido",json,getContext());


                EliminarCantidad0();
                adapterItems.setFilter(listDetalle);
                adapterItems.notifyDataSetChanged();
            }else{

                ModificarCantidad(producto,valor);
                tvSubTotal.setText(""+valor*producto.getPrecio()+" Bs");

            }

        }else{
            PedidoDetalle detalle=new PedidoDetalle();
            detalle.setCantidad(valor);
            detalle.setDetalle("");
            detalle.setId(0);
            detalle.setPedidoId(0);
            detalle.setPrecio(producto.getPrecio());
            detalle.setProductoId(producto.getId());
            detalle.setSubTotal(valor*producto.getPrecio());
            tvSubTotal.setText(""+valor*producto.getPrecio()+" Bs");
            listDetalle.add(detalle);
        }

        Gson gson = new Gson();
        String json = gson.toJson(listDetalle);
        DataPreferences.putPref("Pedido",json,getContext());
        CalcularTotal();

    }
    void ModificarCantidad(PedidoDetalle p , int value){
        for (int i = 0; i < listDetalle.size(); i++) {

            if (p.getProductoId()==listDetalle.get(i).getProductoId()){
                listDetalle.get(i).setCantidad(value);
                listDetalle.get(i).setSubTotal(value*p.getPrecio());
            }
        }

    }
    boolean ExisteProductoDetalle(PedidoDetalle p ){

        for (int i = 0; i < listDetalle.size(); i++) {

            if (p.getProductoId()==listDetalle.get(i).getProductoId()){
                return true;
            }
        }
        return false;
    }

    @Override
    public void ShowSyncroMgs(String message) {
        if (alertDialog.isShowing()){
            alertDialog.dismiss();
        }
        Snackbar snackbar= Snackbar.make(tvDescripcionEmpresa, message, Snackbar.LENGTH_LONG);
        View snackbar_view=snackbar.getView();
        TextView snackbar_text=(TextView)snackbar_view.findViewById(android.support.design.R.id.snackbar_text);
        snackbar_text.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_checked,0);
        snackbar_text.setGravity(Gravity.CENTER);
        snackbar.show();
    }
}
