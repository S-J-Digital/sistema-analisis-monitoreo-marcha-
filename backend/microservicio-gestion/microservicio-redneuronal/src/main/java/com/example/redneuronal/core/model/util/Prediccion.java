package com.example.redneuronal.core.model.util;
public class Prediccion {
    int indice;
    double valor;
    public Prediccion(int i, double v) {
        this.indice = i;
        this.valor = v;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
