package com.marco.santdelivery.Cloud;

public class ResponseLogin {
   int code;
   String message;
   String token;
   int id;
    int zona;
    int mapa;
    int pedido;
    int update_cliente;
    int categoria;
    int stock;

    public ResponseLogin(int code, String message, String token, int id, int zona, int mapa, int pedido, int update_cliente, int categoria, int stock) {
        this.code = code;
        this.message = message;
        this.token = token;
        this.id = id;
        this.zona = zona;
        this.mapa = mapa;
        this.pedido = pedido;
        this.update_cliente = update_cliente;
        this.categoria = categoria;
        this.stock = stock;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getZona() {
        return zona;
    }

    public void setZona(int zona) {
        this.zona = zona;
    }

    public int getMapa() {
        return mapa;
    }

    public void setMapa(int mapa) {
        this.mapa = mapa;
    }

    public int getPedido() {
        return pedido;
    }

    public void setPedido(int pedido) {
        this.pedido = pedido;
    }

    public int getUpdate_cliente() {
        return update_cliente;
    }

    public void setUpdate_cliente(int update_cliente) {
        this.update_cliente = update_cliente;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
