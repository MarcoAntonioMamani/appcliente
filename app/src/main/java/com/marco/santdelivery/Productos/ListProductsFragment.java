package com.marco.santdelivery.Productos;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
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
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.labters.lottiealertdialoglibrary.ClickListener;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.marco.santdelivery.Empresas.Adapter.EmpresasAdapter;
import com.marco.santdelivery.Empresas.EmpresasFragment;
import com.marco.santdelivery.Empresas.EmpresasMvp;
import com.marco.santdelivery.Empresas.EmpresasPresenter;
import com.marco.santdelivery.Empresas.Model.Categorias;
import com.marco.santdelivery.Empresas.Model.EmpresasEntity;
import com.marco.santdelivery.MainActivity;
import com.marco.santdelivery.Pedidos.PedidoDetalle;
import com.marco.santdelivery.Productos.Adapter.CategoriaAdapter;
import com.marco.santdelivery.Productos.Adapter.ProductosAdapter;
import com.marco.santdelivery.Productos.Model.ProductosEntity;
import com.marco.santdelivery.R;
import com.marco.santdelivery.ShareUtil.DataCache;
import com.marco.santdelivery.ShareUtil.DataPreferences;
import com.marco.santdelivery.ShareUtil.LocationGeo;
import com.marco.santdelivery.ViewProductos.ViewProductFragment;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
/**
 * A simple {@link Fragment} subclass.
 */
public class ListProductsFragment extends Fragment implements ProductorMvp.View, SearchView.OnQueryTextListener {
    private ProductorMvp.Presenter mSincronizarPresenter;
    LottieAlertDialog alertDialog;

    public ProductosAdapter adapterPerfil;
    public CategoriaAdapter adapterCategoria;
    RecyclerView recListCategoria;
    Context context;
    RecyclerView recList;
    SearchView simpleSearchView;
    List<ProductosEntity> mListEmpresas;
EmpresasEntity empresa;
List<Categorias> ListCategorias;
LinearLayout linearTotal;
List<PedidoDetalle> listDetalle;
TextView CantidadPedido;
TextView TotalPedido;

    public ListProductsFragment() {
        // Required empty public constructor
    }
    public ListProductsFragment(EmpresasEntity empresa) {
        this.empresa=empresa;
        // Required empty public constructor
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("");
        DataCache.tvTitleMenu.setText("Ver Productos");
        adapterCategoria.setFilter(ListCategorias);
        adapterCategoria.notifyDataSetChanged();
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
        adapterPerfil.setFilter(mListEmpresas);
        adapterPerfil.notifyDataSetChanged();
        SetearTotalesResumen();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LocationGeo.getInstance(getContext(),getActivity());
        LocationGeo.PedirPermisoApp();
        adapterCategoria.setFilter(ListCategorias);
        adapterCategoria.notifyDataSetChanged();
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

        adapterPerfil.setFilter(mListEmpresas);
        adapterPerfil.notifyDataSetChanged();
        SetearTotalesResumen();

    }

    public void SetearListCategorias(){
        List<Categorias> list=new ArrayList<>();
        list.add(new Categorias(0,"  Todos  ",empresa.getId(),1));
        for (int i = 0; i < DataCache.ListCategorias.size(); i++) {
            EmpresasEntity empresa=DataCache.EmpresaSelected;

            if (DataCache.ListCategorias.get(i).getEmpresaId()==empresa.getId()){
                list.add(DataCache.ListCategorias.get(i));

                }
            }
            ListCategorias=list;


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_products, container, false);
        recListCategoria=(RecyclerView) view.findViewById(R.id.Productos_CardCategoria);
        linearTotal=(LinearLayout)view.findViewById(R.id.linear_totalorder);
        CantidadPedido=(TextView)view.findViewById(R.id.cantidad_order);
        TotalPedido=(TextView)view.findViewById(R.id.total_order);
        recListCategoria.setHasFixedSize(true);
        recList = (RecyclerView) view.findViewById(R.id.Productos_CardList);
        recList.setHasFixedSize(true);
        simpleSearchView = (SearchView) view.findViewById (R.id.simpleSearchViewProductos);
        simpleSearchView.setOnQueryTextListener((SearchView.OnQueryTextListener) this);
        simpleSearchView.setIconifiedByDefault(false);
        listDetalle=new ArrayList<>();
        DataCache.listDetalle=listDetalle;
        new ProductosPresenter(this,getContext());
        if (DataCache.listProductos==null){
            if (isOnline()){
                showDialogs();
                new ChecarNotificaciones().execute();
            }else{
                ShowMessageResult("Sin Conexion a Internet.");
            }


        }else{
            mListEmpresas=DataCache.listProductos;
            Collections.sort(mListEmpresas);
            CargarRecycler(mListEmpresas);
            SetearListCategorias();
            CargarRecyclerCategoria(ListCategorias);
        }

        CantidadPedido.setText(0+" Items");
        TotalPedido.setText(0+" Bs");


        return view;
    }
    public List<ProductosEntity> filter (List<ProductosEntity> bares ,String texto){
        List<ProductosEntity>ListaFiltrada=new ArrayList<>();
        try{
            texto=texto.toLowerCase();
            for (ProductosEntity b:bares){
                String name=b.getNombreProducto().toLowerCase();
                if(name.contains(texto)){
                    ListaFiltrada.add(b);
                }
            }
        }catch (Exception e){

        }
        return ListaFiltrada;
    }


    public void showDialogs() {
        ShowDialogSincronizando();
        alertDialog.show();
    }
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    public void CargarRecycler(List<ProductosEntity> listCliente){
        if (listCliente!=null){
            adapterPerfil = new ProductosAdapter(context,listCliente,this,getActivity());
            GridLayoutManager  llm = new GridLayoutManager(getActivity(),2);
            llm.setOrientation(GridLayoutManager .VERTICAL);
            final LayoutAnimationController controller =
                    AnimationUtils.loadLayoutAnimation(getActivity().getApplicationContext(), R.anim.layout_animation_fall_down);
            recList.setLayoutAnimation(controller);
            recList.setLayoutManager(llm);
            recList.setAdapter(adapterPerfil);

           recList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dy > 0 && linearTotal.getVisibility() == View.VISIBLE) {
                      linearTotal.setVisibility(View.GONE);
                    } else if (dy < 0 && linearTotal.getVisibility() != View.VISIBLE) {
                        linearTotal.setVisibility(View.VISIBLE);

                    }
                }
            });
        }

    }
    public void CargarRecyclerCategoria(List<Categorias> listCliente){
        if (listCliente!=null){
            adapterCategoria = new CategoriaAdapter(context,listCliente,this,getActivity());
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.HORIZONTAL);
            final LayoutAnimationController controller =
                    AnimationUtils.loadLayoutAnimation(getActivity().getApplicationContext(), R.anim.layout_animation_fall_down);
            recListCategoria.setLayoutAnimation(controller);
            recListCategoria.setLayoutManager(llm);
            recListCategoria.setAdapter(adapterCategoria);

          /*  recList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dy > 0 && btnAddCliente.getVisibility() == View.VISIBLE) {
                        btnAddCliente.hide();
                    } else if (dy < 0 && btnAddCliente.getVisibility() != View.VISIBLE) {
                        btnAddCliente.show();
                    }
                }
            });*/
        }

    }
    private void ShowDialogSincronizando(){

        try
        {

            alertDialog = new LottieAlertDialog.Builder(getContext(), DialogTypes.TYPE_LOADING).setTitle("Tiendas")
                    .setDescription("Obteniendo Datos .....")
                    .build();

            alertDialog.setCancelable(false);
        }catch (Error e){

            String d=e.getMessage();

        }

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        try{
            List<ProductosEntity> ListaFiltrada=filter(mListEmpresas,newText);
            adapterPerfil.setFilter(ListaFiltrada);
            CambiarEstado(new Categorias(0,"  Todos  ",empresa.getId(),1));
        }catch (Exception e){

        }
        return false;

    }

    @Override
    public void recyclerViewListClicked(View v, ProductosEntity empresa) {
        Gson gson = new Gson();
        String json = gson.toJson(listDetalle);
        DataPreferences.putPref("Pedido",json,getContext());
        DataCache.ProductoSelected=empresa;
        Fragment frag = new ViewProductFragment();
        MainActivity fca = (MainActivity) getActivity();
        fca.switchFragment(frag,"VIEW_PRODUCTOS");
    }

    public void CambiarEstado(Categorias categoria){
        List<Categorias> list=new ArrayList<>();
        for (int i = 0; i < ListCategorias.size(); i++) {
            if (ListCategorias.get(i).getId()==categoria.getId()){
                Categorias ca= null;
                try {
                    ca = ListCategorias.get(i).clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                ca.setEstado(1);
                list.add(ca);
            }else{
                Categorias ca= null;
                try {
                    ca = ListCategorias.get(i).clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                ca.setEstado(0);
                list.add(ca);
            }


        }
       ListCategorias=list;

        adapterCategoria.setFilter(list);
        adapterCategoria.notifyDataSetChanged();

    }
    @Override
    public void recyclerViewListClickedCategoria(View v, Categorias empresa, TextView tvCategoria) {


        CambiarEstado(empresa);


        List<ProductosEntity> list=new ArrayList<>();
        if (empresa.getId()==0){
           adapterPerfil.setFilter(mListEmpresas);
        }else{
            for (int i = 0; i < mListEmpresas.size(); i++) {
                if (mListEmpresas.get(i).getCategoriaId()==empresa.getId()){
                    list.add(mListEmpresas.get(i));
                }

            }

            adapterPerfil.setFilter(list);
        }

    }

    @Override
    public void setPresenter(ProductorMvp.Presenter presenter) {
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
    public void MostrarDatos(List<ProductosEntity> listEmpresa) {
        if (alertDialog.isShowing()){
            alertDialog.dismiss();
        }
        mListEmpresas=listEmpresa;
        Collections.sort(mListEmpresas);
        CargarRecycler(mListEmpresas);
        SetearListCategorias();
        CargarRecyclerCategoria(ListCategorias);
    }

    @Override
    public void AddCantidadProducto(ElegantNumberButton v, int valor, ProductosEntity producto) {

        if (ExisteProductoDetalle(producto)){
            ModificarCantidad(producto,valor);
        }else{
            PedidoDetalle detalle=new PedidoDetalle();
            detalle.setCantidad(valor);
            detalle.setDetalle("");
            detalle.setId(0);
            detalle.setPedidoId(0);
            detalle.setPrecio(producto.getPrecio());
            detalle.setProductoId(producto.getId());
            detalle.setSubTotal(valor*producto.getPrecio());
            listDetalle.add(detalle);
        }
        SetearTotalesResumen();
        Gson gson = new Gson();
        String json = gson.toJson(listDetalle);
        DataPreferences.putPref("Pedido",json,getContext());
    }

    public void SetearTotalesResumen(){

        int cantidad=0;
        double total =0;

        for (int i = 0; i < listDetalle.size(); i++) {

            cantidad+=listDetalle.get(i).getCantidad();
            total+=listDetalle.get(i).getSubTotal();
        }

        CantidadPedido.setText(cantidad+" Items");
        TotalPedido.setText(total+" Bs");

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

    @Override
    public void ShowSyncroMgs(String message) {
        if (alertDialog.isShowing()){
            alertDialog.dismiss();
        }
        Snackbar snackbar= Snackbar.make(simpleSearchView, message, Snackbar.LENGTH_LONG);
        View snackbar_view=snackbar.getView();
        TextView snackbar_text=(TextView)snackbar_view.findViewById(android.support.design.R.id.snackbar_text);
        snackbar_text.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_checked,0);
        snackbar_text.setGravity(Gravity.CENTER);
        snackbar.show();
    }

    private class ChecarNotificaciones extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            //NUESTRO CODIGO
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    mSincronizarPresenter.GetDatos();
                }
            }, 1 * 2000);
            super.onPostExecute(result);
        }
    }
}
