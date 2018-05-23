/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.c45.maven;

import static com.mycompany.c45.maven.ExcelReader.SAMPLE_XLSX_FILE_PATH;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import modelos.Columna;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author jorgebarraza
 */
public class NegocioC45 {

    public static final String SAMPLE_XLSX_FILE_PATH = "./pruebaPoi.xlsx";
    private ArrayList<Columna> tablaPrincipal;
    private ArrayList<Columna> columnaPrincipal;
    private double entropiaGlobal;
    private int totalColumnas;
    private int totalRegistros;

    public static void main(String[] args) throws IOException, InvalidFormatException {
        NegocioC45 context = new NegocioC45();
        context.llenadoDeDatos();
    }

    /**
     * Metodo para llenar las listas y variables necesarias desde el archivo de
     * Excel
     *
     * @throws IOException
     * @throws InvalidFormatException
     */
    public void llenadoDeDatos() throws IOException, InvalidFormatException {
        try {
            Columna columna;
            tablaPrincipal = new ArrayList<>();
            columnaPrincipal = new ArrayList<>();

            //Crear un Workbook desde el archivo Excel (.xls o .xlsx)
            Workbook workbook = WorkbookFactory.create(new File(SAMPLE_XLSX_FILE_PATH));

            //Obteniendo la hoja numero 0
            Sheet sheet = workbook.getSheetAt(0);

            // Create a DataFormatter to format and get each cell's value as String
            DataFormatter dataFormatter = new DataFormatter();

            //Obtener el numero de columnas en el excel
            for (Row row : sheet) { //Por cada Row
                totalColumnas = row.getPhysicalNumberOfCells();
                break;
            }

            //Obtener el total de registros en la tabla
            for (Row row : sheet) { //Por cada Row
                totalRegistros++;
            }
            totalRegistros = totalRegistros - 1;

            for (int i = 0; i < totalColumnas; i++) {
                ArrayList<String> datosColumnas = new ArrayList<>();
                int contador = 0;
                columna = new Columna();
                for (Row row : sheet) { //Por cada Row
                    totalColumnas = row.getPhysicalNumberOfCells();
                    Cell cell = row.getCell(i); //Obtener la celda en la columna a escoger.               
                    if (contador > 0) {
                        datosColumnas.add(dataFormatter.formatCellValue(cell));
                    } else {
                        columna.setNombre(dataFormatter.formatCellValue(cell));
                    }
                    contador++;
                }
                columna.setElemtos(datosColumnas);
                tablaPrincipal.add(columna);
                datosColumnas = null;
                columna = null;
            }

            //Llamar al siguiente paso
            paso1();

            /*columna = new Columna();
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
        columna = null;*/
            //entropiaGlobal = -((double) 9 / 14) * Math.log((double) 9 / 14) / Math.log(2) - ((double) 5 / 14) * Math.log((double) 5 / 14) / Math.log(2);
            //String x = "";
        } catch (Exception ex) {
            String error = "Error" + ex.getStackTrace();
        }
    }

    /**
     * Metodo que calcula la entropia globar inicial del paso 1.
     */
    public void paso1() {
        //Obtener el total de los "Si" y los "No"
        ArrayList<String> columnaDeCualesJuegan = tablaPrincipal.get(totalColumnas - 1).getElemtos();
        int totalSi = Collections.frequency(columnaDeCualesJuegan, "Yes");
        int totalNo = Collections.frequency(columnaDeCualesJuegan, "No");
        //Calcular la entropia globar:
        entropiaGlobal = -((double) totalSi / totalRegistros) * Math.log((double) totalSi / totalRegistros) / Math.log(2) - ((double) totalNo / totalRegistros) * Math.log((double) totalNo / totalRegistros) / Math.log(2);
        String x = "";
    }

    public void paso2() {
        //Se recorrerán toas las columnas y se le resta 2 porque 1 es por el indice del arreglo
        //y el otro porque la ultima columna es la que indican si juegan o no.
        for (int i = 0; i < totalColumnas - 2; i++) {
            //Filtrar los elementos de cada columna y hacer un Distinct para contarlos
            List<String> listDistinct = tablaPrincipal.get(i).getElemtos().stream().distinct().collect(Collectors.toList());
            tablaPrincipal.get(i).setNumDeTipos(listDistinct.size());
            for(int j = 0; j < tablaPrincipal.get(i).getElemtos().size(); j++){
                
            }
        }
    }

    public ArrayList<String> generarListaElementos(int cantidad, String elemento) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            arrayList.add(elemento);
        }
        return arrayList;
    }

}
