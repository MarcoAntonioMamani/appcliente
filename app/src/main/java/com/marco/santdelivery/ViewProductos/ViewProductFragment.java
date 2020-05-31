package com.marco.santdelivery.ViewProductos;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.labters.lottiealertdialoglibrary.ClickListener;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.marco.santdelivery.Empresas.Model.EmpresasEntity;
import com.marco.santdelivery.Empresas.Model.ProductosImagenesEntity;
import com.marco.santdelivery.MainActivity;
import com.marco.santdelivery.Mapa.MapaTiendaActivity;
import com.marco.santdelivery.Pedidos.PedidoDetalle;
import com.marco.santdelivery.Productos.Model.ProductosEntity;
import com.marco.santdelivery.R;
import com.marco.santdelivery.ShareUtil.DataCache;
import com.marco.santdelivery.ShareUtil.DataPreferences;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.Manifest.permission.CALL_PHONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewProductFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

TextView tvNombreProducto;
TextView tvMarcaProducto;
TextView tvDescripcion;
TextView tvPrecio;
    SliderLayout sliderLayout;
    LinearLayout linearWhatsapp;
    LottieAlertDialog alertDialog;
    LinearLayout linearUbicacion;
    ElegantNumberButton btnCantidad;
    TextView tvCantidadItems;
    TextView tvTotalPrecio;
    List<PedidoDetalle> listDetalle;
    public ViewProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_product, container, false);
        tvNombreProducto=view.findViewById(R.id.view_nameProducto);
        tvMarcaProducto=view.findViewById(R.id.view_marcaProducto);
        tvDescripcion=view.findViewById(R.id.view_descripcionProduct);
        tvCantidadItems=view.findViewById(R.id.product_cantidad_order);
        tvTotalPrecio=view.findViewById(R.id.product_total_order);
        tvPrecio=view.findViewById(R.id.view_producto_precio);
        sliderLayout = (SliderLayout)view.findViewById(R.id.producto_slider);
        linearWhatsapp=(LinearLayout)view.findViewById(R.id.linear_whatsapp);
        linearUbicacion=(LinearLayout)view.findViewById(R.id.linear_ubicacion);
        btnCantidad=(ElegantNumberButton)view.findViewById(R.id.view_producto_cantidad);

        sliderLayout.setPresetTransformer(SliderLayout.Transformer.ZoomIn);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

        sliderLayout.addOnPageChangeListener(this);
        sliderLayout.stopAutoCycle();
        onClickWhatsapp();
        onClickUbicacion();
        CargarImagenes();
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
        SetearTotalesResumen();
        EventCantidad();
        return view;
    }
    public void EventCantidad(){
        btnCantidad.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                ProductosEntity producto= DataCache .ProductoSelected;
                if (ExisteProductoDetalle(producto)){
                    ModificarCantidad(producto,newValue);
                }else{
                    PedidoDetalle detalle=new PedidoDetalle();
                    detalle.setCantidad(newValue);
                    detalle.setDetalle("");
                    detalle.setId(0);
                    detalle.setPedidoId(0);
                    detalle.setPrecio(producto.getPrecio());
                    detalle.setProductoId(producto.getId());
                    detalle.setSubTotal(newValue*producto.getPrecio());
                    listDetalle.add(detalle);
                }
                SetearTotalesResumen();
                Gson gson = new Gson();
                String json = gson.toJson(listDetalle);
                DataPreferences.putPref("Pedido",json,getContext());
            }
        });
    }
    void ModificarCantidad(ProductosEntity p , int value){
        for (int i = 0; i < listDetalle.size(); i++) {

            if (p.getId()==listDetalle.get(i).getProductoId()){
                listDetalle.get(i).setCantidad(value);
                listDetalle.get(i).setSubTotal(value*p.getPrecio());
            }
        }

    }

    boolean ExisteProductoDetalle(ProductosEntity p ){

        for (int i = 0; i < listDetalle.size(); i++) {

            if (p.getId()==listDetalle.get(i).getProductoId()){
                return true;
            }
        }
        return false;
    }
    public void SetearTotalesResumen(){

        int cantidad=0;
        double total =0;
        if (listDetalle!=null){
            for (int i = 0; i < listDetalle.size(); i++) {

                cantidad+=listDetalle.get(i).getCantidad();
                total+=listDetalle.get(i).getSubTotal();
            }
            tvCantidadItems.setText(cantidad+" Items");
            tvTotalPrecio.setText(total+" Bs");
        }else{
            tvCantidadItems.setText(0+" Items");
            tvTotalPrecio.setText(0+" Bs");
        }




    }
    public void onClickWhatsapp(){
        linearWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmpresasEntity empresa=DataCache.EmpresaSelected;
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

    public void onClickUbicacion(){
        linearUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmpresasEntity empresa=DataCache.EmpresaSelected;
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


    public void CargarImagenes(){
        ProductosEntity producto= DataCache .ProductoSelected;
        if (producto!=null){
            List<ProductosImagenesEntity> list=new ArrayList<>();
            for (int i = 0; i < DataCache.listImagenes.size(); i++) {
                if (DataCache.listImagenes.get(i).getProductoId()==producto.getId()){
                    list.add(DataCache.listImagenes.get(i));
                }

            }
            for (int i = 0; i < list.size(); i++) {
                TextSliderView textSliderView = new TextSliderView(getActivity());
                textSliderView
                        .description("Precio = "+producto.getPrecio()+" Bs")
                        .image(list.get(i).getNombreImage())
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(this);
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra",producto.getNombreProducto());
                sliderLayout.addSlider(textSliderView);
            }

        }
        tvNombreProducto.setText(producto.getNombreProducto());
        tvMarcaProducto.setText(producto.getNombreCategoria());
        tvDescripcion.setText(producto.getDescripcionProducto());
        tvPrecio.setText(producto.getPrecio()+" Bs");
        btnCantidad.setNumber(getCantidad(producto.getId()));
    }

    public String getCantidad(int ProductoId){

        for (int i = 0; i < DataCache.listDetalle.size(); i++) {
            if(DataCache.listDetalle.get(i).getProductoId()==ProductoId){
                return ""+(int)DataCache.listDetalle.get(i).getCantidad();
            }

        }
        return "0";
    }
    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
