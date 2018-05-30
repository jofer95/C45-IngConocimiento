/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.c45.maven;

import java.io.IOException;
import java.util.ArrayList;
import modelos.Columna;
import modelos.Nodo;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 *
 * @author jorgebarraza
 */
public class pruebas {

    public static void main(String[] args) throws IOException, InvalidFormatException {
               
    }
    
    public ArrayList<Columna> llenadoDeDatos(){
        ArrayList<Columna> columnas = new ArrayList<>();
        
        Columna columna = new Columna();
        columna.setNombre("Tiempo");
        ArrayList<String> elementos = new ArrayList<>();
        elementos.add("sunny");
        elementos.add("sunny");
        elementos.add("overcast");
        columna.setElemtos(elementos);
        columna.setNumDeTipos(14);
        columnas.add(columna);
        columna = null;
        elementos = null;
        
        columna = new Columna();
        columna.setNombre("Tiempo");
        elementos.add("sunny");
        elementos.add("sunny");
        elementos.add("overcast");
        columna.setElemtos(elementos);
        columna.setNumDeTipos(14);
        columnas.add(columna);
        columna = null;
        elementos = null;
        
        columna = new Columna();
        columna.setNombre("Tiempo");
        elementos = new ArrayList<>();
        elementos.add("sunny");
        elementos.add("sunny");
        elementos.add("overcast");
        columna.setElemtos(elementos);
        columna.setNumDeTipos(14);
        columnas.add(columna);
        columna = null;
        elementos = null;
        
        columna = new Columna();
        columna.setNombre("Tiempo");
        elementos = new ArrayList<>();
        elementos.add("sunny");
        elementos.add("sunny");
        elementos.add("overcast");
        columna.setElemtos(elementos);
        columna.setNumDeTipos(14);
        columnas.add(columna);
        columna = null;
        elementos = null;
        
        columna = new Columna();
        columna.setNombre("Tiempo");
        elementos = new ArrayList<>();
        elementos.add("sunny");
        elementos.add("sunny");
        elementos.add("overcast");
        columna.setElemtos(elementos);
        columna.setNumDeTipos(14);
        columnas.add(columna);
        columna = null;
        elementos = null;
        
        return columnas;
    }
}
