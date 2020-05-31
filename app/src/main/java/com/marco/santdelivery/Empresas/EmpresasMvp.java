package com.marco.santdelivery.Empresas;

import com.marco.santdelivery.Empresas.Model.Categorias;
import com.marco.santdelivery.Empresas.Model.EmpresasEntity;
import com.marco.santdelivery.Empresas.Model.ProductosImagenesEntity;
import com.marco.santdelivery.Productos.Model.ProductosEntity;

import java.util.List;

public interface EmpresasMvp {

    interface View {
        void recyclerViewListClicked(android.view.View v,EmpresasEntity empresa);
        void setPresenter(EmpresasMvp.Presenter presenter);
        void ShowMessageResult(String message);
        void MostrarEmpresas(List<EmpresasEntity> listEmpresa);

        void ShowSyncroMgs(String message);
        void showCellPhone(android.view.View v,EmpresasEntity i);
        void sendMessageWhatsapp(android.view.View v,EmpresasEntity i);
        void viewUbicacion(android.view.View v,EmpresasEntity i);
    }
    interface Presenter{
        void GetEmpresas();
    }
}
