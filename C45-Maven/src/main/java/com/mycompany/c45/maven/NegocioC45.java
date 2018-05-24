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
import modelos.Nodo;
import modelos.SumaTiposPorColumna;
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
    private ArrayList<Nodo> arbolFinal = new ArrayList<>();

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
            calcularEntropiaGlobar();
        } catch (Exception ex) {
            String error = "Error" + ex.getStackTrace();
        }
    }

    /**
     * Metodo que calcula la entropia globar inicial del paso 1.
     */
    public void calcularEntropiaGlobar() {
        //Obtener el total de los "Si" y los "No"
        ArrayList<String> columnaDeCualesJuegan = tablaPrincipal.get(totalColumnas - 1).getElemtos();
        int totalSi = Collections.frequency(columnaDeCualesJuegan, "Yes");
        int totalNo = Collections.frequency(columnaDeCualesJuegan, "No");
        //Calcular la entropia globar:
        entropiaGlobal = -((double) totalSi / totalRegistros) * Math.log((double) totalSi / totalRegistros) / Math.log(2) - ((double) totalNo / totalRegistros) * Math.log((double) totalNo / totalRegistros) / Math.log(2);
        String x = "";
        paso1();
    }

    public void paso1() {
        int totalSi = 0;
        int totalNo = 0;
        int totalDeUnElementoDeUnaColumna = 0;
        ArrayList<Double> resultadosPorTipoEnColumna = new ArrayList<>();
        ArrayList<SumaTiposPorColumna> resultadosSumaPorcentajesPorColumna = new ArrayList<>();
        double sumaResultadosTipos = 0;
        String nombreColumnaGanadora;
        Nodo nodoPrincipal;
        //Se recorrerán toas las columnas y se le resta 2 porque 1 es por el indice del arreglo
        //y el otro porque la ultima columna es la que indican si juegan o no.
        for (int i = 0; i < totalColumnas - 1; i++) {
            //Filtrar los elementos de cada columna y hacer un Distinct para contarlos
            List<String> listDistinct = tablaPrincipal.get(i).getElemtos().stream().distinct().collect(Collectors.toList());
            tablaPrincipal.get(i).setNumDeTipos(listDistinct.size());
            //Recorrer cada elemento diferente en la columan para buscar sus "Si" y "No"
            for (int k = 0; k < listDistinct.size(); k++) {
                nodoPrincipal = new Nodo();
                for (int j = 0; j < tablaPrincipal.get(i).getElemtos().size(); j++) {
                    /*Si el elemnto actual recorriendo de la columna es == al 
                    primero de la lista con el Distinct: validar sus total Si y no */
                    String elemento = listDistinct.get(k);
                    if (tablaPrincipal.get(i).getElemtos().get(j).equals(listDistinct.get(k))) {
                        if (tablaPrincipal.get(totalColumnas - 1).getElemtos().get(j).equals("Yes")) {
                            totalSi++;
                        } else {
                            totalNo++;
                        }
                    }
                }
                //If para agregar si un tipo en la columna son puros "Si" o "No"
                //para agregarlos directo
                if(totalSi == 0){
                    Nodo nodoAgregar = new Nodo();
                    nodoAgregar.setNombre("No");
                    ArrayList<Nodo> arregloNodos = new ArrayList<>();
                    arregloNodos.add(nodoAgregar);
                    nodoPrincipal.setOpcionesNodos(arregloNodos);
                }else if(totalNo == 0){
                    Nodo nodoAgregar = new Nodo();
                    nodoAgregar.setNombre("Yes");
                    ArrayList<Nodo> arregloNodos = new ArrayList<>();
                    arregloNodos.add(nodoAgregar);
                    nodoPrincipal.setOpcionesNodos(arregloNodos);
                }
                totalDeUnElementoDeUnaColumna = totalSi + totalNo;
                /*Calcular el resultado de ese registro para luego sumarlos y sacar
                    el total de la columna*/
                double subPorcentaje = -((double) totalSi / totalDeUnElementoDeUnaColumna) * Math.log((double) totalSi / totalDeUnElementoDeUnaColumna) / Math.log(2) - ((double) totalNo / totalDeUnElementoDeUnaColumna) * Math.log((double) totalNo / totalDeUnElementoDeUnaColumna) / Math.log(2);
                double totalTipoEntreTotalRegistros = (double) totalDeUnElementoDeUnaColumna / (double) totalRegistros;
                double porcentaje = subPorcentaje * totalTipoEntreTotalRegistros;
                resultadosPorTipoEnColumna.add(porcentaje);
                totalSi = 0;
                totalNo = 0;
            }
            //Sumar todos los porcentajes de los tipos diferentes de la columna
            for (double obj : resultadosPorTipoEnColumna) {
                if(Double.isNaN(obj)){
                    obj = 0;
                }
                sumaResultadosTipos += obj;
            }  
            //AGREGAR A LA LISTA DE DE LAS COLUMNAS SUS SUMAS PARA OBTENER LA GANADORA.
            resultadosPorTipoEnColumna = new ArrayList<>();
            SumaTiposPorColumna totalSuma = new SumaTiposPorColumna();
            totalSuma.setNombreColumna(tablaPrincipal.get(i).getNombre());
            totalSuma.setTotalSumaTipos(sumaResultadosTipos);
            resultadosSumaPorcentajesPorColumna.add(totalSuma);
            sumaResultadosTipos = 0;
        }
        SumaTiposPorColumna ganador = Collections.min(resultadosSumaPorcentajesPorColumna);
        nodoPrincipal = new Nodo();
        nodoPrincipal.setNombre(ganador.getNombreColumna());
        String x = "";
    }
    
    public void paso2(){
        
    }
    
}
