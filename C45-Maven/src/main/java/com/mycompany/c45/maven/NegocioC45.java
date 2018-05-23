/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.c45.maven;

import java.util.ArrayList;
import modelos.Columna;

/**
 *
 * @author jorgebarraza
 */
public class NegocioC45 {

    private ArrayList<Columna> tablaPrincipal;
    private ArrayList<Columna> columnaPrincipal;
    private double entropiaGlobal;

    public static void main(String[] args) {
        NegocioC45 context = new NegocioC45();
        context.llenadoDeDatos();
    }

    public void llenadoDeDatos() {
        ArrayList<String> datosColumnas = new ArrayList<>();
        Columna columna;      
        tablaPrincipal = new ArrayList<>();
        columnaPrincipal = new ArrayList<>();
        
        columna = new Columna();
        columna.setNombre("Outlook");
        columna.setNumDeTipos(3);
        datosColumnas.addAll(generarListaElementos(5, "sunny"));
        datosColumnas.addAll(generarListaElementos(4, "overcast"));
        datosColumnas.addAll(generarListaElementos(5, "rainy"));
        columna.setElemtos(datosColumnas);
        tablaPrincipal.add(columna);
        datosColumnas = null;
        columna = null;
        
        datosColumnas = new ArrayList<>();       
        columna = new Columna();
        columna.setNombre("Temperature");
        columna.setNumDeTipos(3);
        datosColumnas.addAll(generarListaElementos(4, "hot"));
        datosColumnas.addAll(generarListaElementos(6, "mild"));
        datosColumnas.addAll(generarListaElementos(4, "cold"));
        columna.setElemtos(datosColumnas);
        tablaPrincipal.add(columna);
        datosColumnas = null;
        columna = null;
        
        datosColumnas = new ArrayList<>();       
        columna = new Columna();
        columna.setNombre("Humidity");
        columna.setNumDeTipos(2);
        datosColumnas.addAll(generarListaElementos(7, "high"));
        datosColumnas.addAll(generarListaElementos(7, "normal"));
        columna.setElemtos(datosColumnas);
        tablaPrincipal.add(columna);
        datosColumnas = null;
        columna = null;
        
        datosColumnas = new ArrayList<>();       
        columna = new Columna();
        columna.setNombre("Windy");
        columna.setNumDeTipos(2);
        datosColumnas.addAll(generarListaElementos(6, "true"));
        datosColumnas.addAll(generarListaElementos(8, "flase"));
        columna.setElemtos(datosColumnas);
        tablaPrincipal.add(columna);
        datosColumnas = null;
        columna = null;
        
        datosColumnas = new ArrayList<>();       
        columna = new Columna();
        columna.setNombre("Play");
        columna.setNumDeTipos(2);
        datosColumnas.addAll(generarListaElementos(9, "yes"));
        datosColumnas.addAll(generarListaElementos(5, "no"));
        columna.setElemtos(datosColumnas);
        columnaPrincipal.add(columna);
        datosColumnas = null;
        columna = null;
        
        
        entropiaGlobal = -((double)9/14)*Math.log((double)9/14)/Math.log(2) -((double)5/14)*Math.log((double)5/14)/Math.log(2);
        String x  = "";
    }

    public ArrayList<String> generarListaElementos(int cantidad, String elemento) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            arrayList.add(elemento);
        }
        return arrayList;
    }

}
