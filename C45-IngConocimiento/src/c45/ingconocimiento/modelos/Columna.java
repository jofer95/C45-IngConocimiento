/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c45.ingconocimiento.modelos;

import java.awt.List;
import java.util.ArrayList;

/**
 *
 * @author jorgebarraza
 */
public class Columna {

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getNumDeTipos() {
        return numDeTipos;
    }

    public void setNumDeTipos(int numDeTipos) {
        this.numDeTipos = numDeTipos;
    }

    public ArrayList<String> getElemtos() {
        return elemtos;
    }

    public void setElemtos(ArrayList<String> elemtos) {
        this.elemtos = elemtos;
    }
    private String nombre;
    private int numDeTipos;
    private ArrayList<String> elemtos;
}
