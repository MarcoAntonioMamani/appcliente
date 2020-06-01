package com.marco.santdelivery.Carrito;

import android.content.Context;

import com.google.android.gms.common.internal.Preconditions;
import com.marco.santdelivery.Productos.ProductorMvp;

public class CarritoPresenter implements CarritoMvp.Presenter{

    private final CarritoMvp.View mSincronizarview;
    private final Context mContext;
    int contador=0;
    public CarritoPresenter(CarritoMvp.View Sincronizarview, Context context) {

        this.mContext=context;
        mSincronizarview = Preconditions.checkNotNull(Sincronizarview);
        mSincronizarview.setPresenter(this);
    }

    @Override
    public void GetDatos() {

    }
}
