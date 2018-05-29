/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import java.util.ArrayList;
import no.geosoft.cc.graphics.GObject;

/**
 *
 * @author jorgebarraza
 */
public class Nodo {

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public GObject getPadre() {
        return padre;
    }

    public void setPadre(GObject padre) {
        this.padre = padre;
    }

    public Nodo() {
        resultados = new ArrayList<>();
        OpcionesNodos = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<String> getResultados() {
        return resultados;
    }

    public void setResultados(ArrayList<String> resultados) {
        this.resultados = resultados;
    }

    public ArrayList<Nodo> getOpcionesNodos() {
        return OpcionesNodos;
    }

    public void setOpcionesNodos(ArrayList<Nodo> OpcionesNodos) {
        this.OpcionesNodos = OpcionesNodos;
    }
    private String nombre;
    private ArrayList<String> resultados;
    private ArrayList<Nodo> OpcionesNodos;
    private GObject padre;
    private int nivel;
}
