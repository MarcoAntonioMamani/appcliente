package com.marco.santdelivery.Empresas;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.CompoundButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.common.base.Preconditions;
import com.labters.lottiealertdialoglibrary.ClickListener;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.marco.santdelivery.Empresas.Adapter.EmpresasAdapter;
import com.marco.santdelivery.Empresas.Model.EmpresasEntity;
import com.marco.santdelivery.MainActivity;
import com.marco.santdelivery.Mapa.MapaTiendaActivity;
import com.marco.santdelivery.Productos.ListProductsFragment;
import com.marco.santdelivery.R;
import com.marco.santdelivery.ShareUtil.DataCache;
import com.marco.santdelivery.ShareUtil.LocationGeo;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.Manifest.permission.CALL_PHONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmpresasFragment extends Fragment  implements EmpresasMvp.View, SearchView.OnQueryTextListener {


    private EmpresasMvp.Presenter mSincronizarPresenter;
    LottieAlertDialog alertDialog;

    public EmpresasAdapter adapterPerfil;
    Context context;
    RecyclerView recList;
    SearchView simpleSearchView;
List<EmpresasEntity> mListEmpresas;
    public EmpresasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LocationGeo.getInstance(getContext(),getActivity());
        LocationGeo.PedirPermisoApp();

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("");
        DataCache.tvTitleMenu.setText("Ver Tiendas");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_empresas, container, false);
        recList = (RecyclerView) view.findViewById(R.id.Empresa_CardList);
        recList.setHasFixedSize(true);
        simpleSearchView = (SearchView) view.findViewById (R.id.simpleSearchView);
        simpleSearchView.setOnQueryTextListener((SearchView.OnQueryTextListener) this);
        simpleSearchView.setIconifiedByDefault(false);
        new EmpresasPresenter(this,getContext());
        if (DataCache.ListEmpresas==null){
            if (isOnline()){
                showDialogs();
                new ChecarNotificaciones().execute();
            }else{
                ShowMessageResult("Sin Conexion a Internet.");
            }


        }else{
            mListEmpresas=DataCache.ListEmpresas;
            Collections.sort(mListEmpresas);
            CargarRecycler(mListEmpresas);
        }




        return view;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        try{
            List<EmpresasEntity> ListaFiltrada=filter(mListEmpresas,newText);
            adapterPerfil.setFilter(ListaFiltrada);

        }catch (Exception e){

        }
        return false;
    }
    public  void Refresh(){
        if(recList!=null){
            recList.scrollToPosition(0);
        }
    }
    public List<EmpresasEntity> filter (List<EmpresasEntity> bares ,String texto){
        List<EmpresasEntity>ListaFiltrada=new ArrayList<>();
        try{
            texto=texto.toLowerCase();
            for (EmpresasEntity b:bares){
                String name=b.getNombre().toLowerCase();
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

    public void CargarRecycler(List<EmpresasEntity> listCliente){
        if (listCliente!=null){
            adapterPerfil = new EmpresasAdapter(context,listCliente,this,getActivity());
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            final LayoutAnimationController controller =
                    AnimationUtils.loadLayoutAnimation(getActivity().getApplicationContext(), R.anim.layout_animation_fall_down);
            recList.setLayoutAnimation(controller);
            recList.setLayoutManager(llm);
            recList.setAdapter(adapterPerfil);

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
    public void recyclerViewListClicked(View v, EmpresasEntity empresa) {
        DataCache.EmpresaSelected=empresa;
        Fragment frag = new ListProductsFragment(empresa);
        MainActivity fca = (MainActivity) getActivity();
        fca.switchFragment(frag,"VIEW_PRODUCTOS");
    }

    @Override
    public void setPresenter(EmpresasMvp.Presenter presenter) {
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
    public void MostrarEmpresas(List<EmpresasEntity> listEmpresa) {
        if (alertDialog.isShowing()){
            alertDialog.dismiss();
        }
        mListEmpresas=listEmpresa;
        DataCache.ListEmpresas=listEmpresa;
        Collections.sort(mListEmpresas);
        CargarRecycler(mListEmpresas);
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

    @Override
    public void showCellPhone(View v, EmpresasEntity i) {

        if (!i.getTelefono().toString().isEmpty()){
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"+i.getTelefono().toString()));

            if (ContextCompat.checkSelfPermission(getContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                startActivity(intent);
            } else {
                requestPermissions(new String[]{CALL_PHONE}, 1);
            }

        }else{
            ShowMessageResult("El Usuario no tiene registrado un numero de telefono");
        }
    }

    @Override
    public void sendMessageWhatsapp(View v, EmpresasEntity i) {
        if (!i.getTelefono().toString() .isEmpty()){
String url="https://api.whatsapp.com/send?phone=591"+i.getTelefono()+"&text="+i.getMensaje();
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

    }

    @Override
    public void viewUbicacion(View v, EmpresasEntity i) {
        if (i!=null){

            if (i.getLatitud()==0 || i.getLongitud()==0){
                ShowMessageResult("La tienda seleccionado no tiene registrado una ubicación");
            }else{
                DataCache.EmpresaSelected=i;
                MainActivity fca = ((MainActivity) getActivity());
                fca.startActivity(new Intent(getActivity(), MapaTiendaActivity.class));
                fca.overridePendingTransition(R.transition.left_in, R.transition.left_out);
            }
        }else{
            ShowMessageResult("La tienda no tiene Registrado Ubicacion");
        }
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
                    mSincronizarPresenter.GetEmpresas();
                }
            }, 1 * 2000);
            super.onPostExecute(result);
        }
    }
}
